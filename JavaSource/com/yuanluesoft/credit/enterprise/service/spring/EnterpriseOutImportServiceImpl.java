package com.yuanluesoft.credit.enterprise.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.credit.enterprise.forms.ImportDateExcel;
import com.yuanluesoft.credit.enterprise.pojo.EnterpriseIn;
import com.yuanluesoft.credit.enterprise.pojo.EnterpriseOut;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.CnToSpell;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author zyh
 *
 */
public class EnterpriseOutImportServiceImpl extends BusinessServiceImpl {
	private AttachmentService attachmentService; //附件服务
	private OrgService orgService;
	private PersonService personService;
	private CryptService cryptService;
	private String mainOrgId;
	private MemberServiceList memberServiceList;
	private PageService pageService;

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void importData(ImportDateExcel excel, SessionInfo sessionInfo)
			throws ServiceException {

		List attachments = attachmentService.list(
				"credit/enterprise", "data", excel.getId(), false, 1,
				null);
		if (attachments == null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}

		Attachment attachment = (Attachment) attachments.get(0);//得到具体的附件对象
		try {
			importDate(attachment, sessionInfo);
			attachmentService.deleteAll("credit/enterprise",
					null, excel.getId());//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("credit/enterprise",
					null, excel.getId());//删除上传的文件
			Logger.exception(e);
			throw new ServiceException("文件解析失败，请检查文件格式及数据类型是否正确");
		}

	}

	/**
	 * 信息导入	
	 * @param attachment
	 * @param sessionInfo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private void importDate(Attachment attachment, SessionInfo sessionInfo)
			throws Exception {
		Workbook workBook = null;
		Sheet sheet = null;
		Row tableTitle = null;
		int firstRow;
		int lastRow;
		String[] colName = new String[] { "注册号", "统一社会信用代码", "企业名称",
				"登记类型", "企业类型" ,"外企国别","法定代表人","投资总额(万美元)","注册资本(万美元)","实收资本(万美元)","外方认缴资本(万美元)","联系电话","公示联络员","联络员电话","行业门类","行业类别","行业代码","经营范围","成立日期","营业期限","核准日期","住所","管片工商所","片区"};
		try {
			workBook = new XSSFWorkbook(new FileInputStream(attachment
					.getFilePath()));
		} catch (Exception ex) {
			workBook = new HSSFWorkbook(new FileInputStream(attachment
					.getFilePath()));
		}
		sheet = workBook.getSheetAt(0);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		int[] colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[0] == -1) {//注册号名称不能为空 
			firstRow++;
			if (firstRow > lastRow) {
				Logger.info("没有标题行！");
				return;
			}
			tableTitle = sheet.getRow(firstRow);
			colNum = findTitleColNumInArray(tableTitle, colName);
		}

		for (int contentRow = firstRow + 1; contentRow <= lastRow; contentRow++) {
			Row content = sheet.getRow(contentRow);// 内容行
			if (content == null) {
				continue;
			}
			if(getStringValue(content.getCell(colNum[0]))==null || getStringValue(content.getCell(colNum[0])).equals("")){
				break;
			}
			EnterpriseOut enterprise = new EnterpriseOut();
			enterprise.setCreator(sessionInfo.getUserName());
			enterprise.setCreatorId(sessionInfo.getUserId());
			enterprise.setCreated(DateTimeUtils.now());
			enterprise.setId(UUIDLongGenerator.generateId());
			
			String loginName = CnToSpell.getShortSpell(getStringValue(content.getCell(colNum[2])));
			enterprise.setPassword("abc,123");//密码
			
			enterprise.setRegistCode(getStringValue(content.getCell(colNum[0])));//注册号
			enterprise.setCreditCode(getStringValue(content.getCell(colNum[1])));//统一社会信用代码
			enterprise.setName(getStringValue(content.getCell(colNum[2])));//企业名称
			enterprise.setRegistType(getStringValue(content.getCell(colNum[3])));//登记类型
			enterprise.setType(getStringValue(content.getCell(colNum[4])));//企业类型
			enterprise.setCountry(getStringValue(content.getCell(colNum[5])));//外企国别
			enterprise.setPerson(getStringValue(content.getCell(colNum[6])));//法定代表人\负责人
			enterprise.setInvest(getDoubleValue(content.getCell(colNum[7])));//投资总额(万美元)
			enterprise.setWorth(getDoubleValue(content.getCell(colNum[8])));//注册资本(万美元)
			enterprise.setRealWorth(getDoubleValue(content.getCell(colNum[9])));//实收资本(万美元)
			enterprise.setOutWorth(getDoubleValue(content.getCell(colNum[10])));//外方认缴资本(万美元)
			enterprise.setTel(getStringValue(content.getCell(colNum[11])));//联系电话
			enterprise.setLinkMan(getStringValue(content.getCell(colNum[12])));//公示联络员
			enterprise.setLinkTel(getStringValue(content.getCell(colNum[13])));//联络员电话
			enterprise.setDoorType(getStringValue(content.getCell(colNum[14])));//行业门类
			enterprise.setIndustry(getStringValue(content.getCell(colNum[15])));//行业类别
			enterprise.setCode(getStringValue(content.getCell(colNum[16])));//行业代码
			enterprise.setBusinessScope(getStringValue(content.getCell(colNum[17])));//经营范围
			enterprise.setStartDate(getDateValue(content.getCell(colNum[18]),"yyyy-MM-dd"));//成立日期
			enterprise.setLimitDate(getStringValue(content.getCell(colNum[19])));//营业期限
			enterprise.setApprovalDate(getDateValue(content.getCell(colNum[20]),"yyyy-MM-dd"));//核准日期
			enterprise.setAddr(getStringValue(content.getCell(colNum[21])));//住所
			enterprise.setAscription(getStringValue(content.getCell(colNum[22])));//管片工商所
			enterprise.setArea(getStringValue(content.getCell(colNum[23])));//片区
			String hql = "from EnterpriseOut EnterpriseOut where EnterpriseOut.registCode = '"+enterprise.getRegistCode()+"'";
			EnterpriseOut old = (EnterpriseOut)getDatabaseService().findRecordByHql(hql);
			if(old!=null){
				old.setCreditCode(getStringValue(content.getCell(colNum[1])));//统一社会信用代码
				old.setName(getStringValue(content.getCell(colNum[2])));//企业名称
				old.setRegistType(getStringValue(content.getCell(colNum[3])));//登记类型
				old.setType(getStringValue(content.getCell(colNum[4])));//企业类型
				old.setCountry(getStringValue(content.getCell(colNum[5])));//外企国别
				old.setPerson(getStringValue(content.getCell(colNum[6])));//法定代表人\负责人
				old.setInvest(getDoubleValue(content.getCell(colNum[7])));//投资总额(万美元)
				old.setWorth(getDoubleValue(content.getCell(colNum[8])));//注册资本(万美元)
				old.setRealWorth(getDoubleValue(content.getCell(colNum[9])));//实收资本(万美元)
				old.setOutWorth(getDoubleValue(content.getCell(colNum[10])));//外方认缴资本(万美元)
				old.setTel(getStringValue(content.getCell(colNum[11])));//联系电话
				old.setLinkMan(getStringValue(content.getCell(colNum[12])));//公示联络员
				old.setLinkTel(getStringValue(content.getCell(colNum[13])));//联络员电话
				old.setDoorType(getStringValue(content.getCell(colNum[14])));//行业门类
				old.setIndustry(getStringValue(content.getCell(colNum[15])));//行业类别
				old.setCode(getStringValue(content.getCell(colNum[16])));//行业代码
				old.setBusinessScope(getStringValue(content.getCell(colNum[17])));//经营范围
				old.setStartDate(getDateValue(content.getCell(colNum[18]),"yyyy-MM-dd"));//成立日期
				old.setLimitDate(getStringValue(content.getCell(colNum[19])));//营业期限
				old.setApprovalDate(getDateValue(content.getCell(colNum[20]),"yyyy-MM-dd"));//核准日期
				old.setAddr(getStringValue(content.getCell(colNum[21])));//住所
				old.setAscription(getStringValue(content.getCell(colNum[22])));//管片工商所
				old.setArea(getStringValue(content.getCell(colNum[23])));//片区
				update(old);
				pageService.rebuildStaticPageForModifiedObject(old, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
			if(memberServiceList.isLoginNameInUse(loginName, 0)) {
//用户名已经被占用,在末尾增加数字
				for(int i=1; i<100000; i++) {
					if(!isLoginNameInUse(loginName + i, 0)) {
						loginName=loginName + i;
						break;
					}
				}
			}
			enterprise.setLoginName(loginName);
			String directoryName = "企业";
			Org department = (Org)orgService.createDirectory(-1,Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);
			//注册用户
			personService.addEmployee(enterprise.getId(), enterprise.getName(), enterprise.getLoginName(), enterprise.getPassword(), 'M', null, null, null, null, null, department.getId()+"", enterprise.getCreatorId(), enterprise.getCreator());
			enterprise.setPassword(encryptPersonPassword(enterprise.getId(), enterprise.getLoginName(), enterprise.getPassword())); //加密口令
			save(enterprise);
			pageService.rebuildStaticPageForModifiedObject(enterprise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}

	}
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select Person.id from Person Person where Person.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
	}

	/**
	 * 查找表头对应的列序号，不存在的列序号为-1.返回的序号和给定的列名顺序一致
	 * @param tableTitle 表头行
	 * @param rowName 表头行单元格名称
	 * @return
	 */
	public static int[] findTitleColNumInArray(Row tableTitle, String[] colName) {

		if (tableTitle == null || colName == null) {
			return null;
		} else {
			int[] colNum = new int[colName.length];
			for (int i = 0; i < colName.length; i++) {
				colNum[i] = -1;//默认为-1。即为-1时表示该值无效，导入文件中不存在该值
				int firstCell = tableTitle.getFirstCellNum();
				int lastCell = tableTitle.getLastCellNum();
				for (; firstCell <= lastCell; firstCell++) {
					if (tableTitle.getCell(firstCell) == null) {
						continue;
					} else {
						try {//不是字符串格式的单元格不符合规范，不提取
							String title = tableTitle.getCell(firstCell)
									.getStringCellValue();
							if (title == null || title.isEmpty()) {
								continue;
							}
							if (title.replaceAll("\\s*|\t|\r|\n", "").indexOf(
									colName[i]) != -1) {//过滤空格换行
								colNum[i] = firstCell;
								break;
							}
						} catch (Exception e) {
							continue;
						}
					}
				}
			}
			return colNum;
		}

	}

	/**
	 * 获取单元格的值并格式化成字符串类型
	 * @param cell
	 * @return
	 */
	public String getStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if(cell==null) {
			return null;
		}
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return null;
			
		case Cell.CELL_TYPE_BOOLEAN:
			return "" + cell.getBooleanCellValue();

		case Cell.CELL_TYPE_ERROR:
			return "" + cell.getErrorCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			double d = cell.getNumericCellValue();  
			if(HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)||DateUtil.isValidExcelDate(d)) { //判断是否为日期
				long time = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).getTime();
				return DateTimeUtils.formatTimestamp(new Timestamp(time), time % (24*3600*1000)==0 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss");
			}
			return StringUtils.format(new Double(cell.getNumericCellValue()), null, null);

		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		}
		return null;
	}

	/**
	 * 获取单元格日期值。Excel中日期是用数字类型存储。展现依然是日期的样式
	 * @param cell 目标单元格
	 * @return
	 */
	public static Date getDateValue(Cell cell,String dataFormat){
		
		if(cell==null){
			return null;
		}
		 int cellType=cell.getCellType();
         switch(cellType){
           case Cell.CELL_TYPE_STRING:
        	                 String date=cell.getStringCellValue();
        	                 if(date!=null&&date.trim().length()!=0){
   			                 	try {
   			                     		return DateTimeUtils.parseDate(date.trim(), dataFormat==null||dataFormat.isEmpty()?"yyyy-MM-dd":dataFormat);
   			                 	} catch (ParseException e) {
   			                 		e.printStackTrace();
   			                 		return null;//无效格式
   			                 	}
                             } break;
//         poi读取data日期格式时默认以数字格式存储。用HSSFDateUtil类转换
           case Cell.CELL_TYPE_NUMERIC:
        	   double d = cell.getNumericCellValue();  
   			if(HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)||DateUtil.isValidExcelDate(d)) { //判断是否为日期
   				java.util.Date dateTemp = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
        	                    return new java.sql.Date(dateTemp.getTime());
        	                }else{
//      	                	不是正确的日期，取出数字直接转换
        	                	try{
        	                		java.util.Date dateTemp= HSSFDateUtil.getJavaDate(cell.getNumericCellValue()); 
        	                		return new java.sql.Date(dateTemp.getTime());
        	                	}catch(Exception e){
        	                		e.printStackTrace();
        	                	}
        	                }break;
           default:return null;//其他类型的不能转换成日期格式
         }
         return null;
	}

	/**
	 * 获取单元格的值并格式化成double类型
	 * @param cell
	 * @return
	 */
	public double getDoubleValue(Cell cell) {
		if (cell == null) {
			return 0;
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return 0;
		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case HSSFCell.CELL_TYPE_STRING:
			try {
				return Double.parseDouble(cell.getStringCellValue());
			} catch (Exception e) {
				return 0;
			}
		default:
			return 0;
		}
	}
	/**
	 * 加密用户口令
	 * @param person
	 * @throws ServiceException
	 */
	private String encryptPersonPassword(long personId, String personLoginName, String password) throws ServiceException {
		if(password==null || password.equals("")) { //口令未设置,则以用户登录名为口令
		    password = personLoginName;
		}
		else if(password.startsWith("{") && password.endsWith("}")) { //口令解密
			return password.substring(1, password.length() - 1);
		}
		//加密口令
		return encryptPassword(personId, password);
	}
	/**
	 * 口令加密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long userId, String password) throws ServiceException {
		return cryptService.encrypt(password, "" + userId, false);
		
	}

	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public CryptService getCryptService() {
		return cryptService;
	}

	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}

	public String getMainOrgId() {
		return mainOrgId;
	}

	public void setMainOrgId(String mainOrgId) {
		this.mainOrgId = mainOrgId;
	}

	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
	}

	public PageService getPageService() {
		return pageService;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
	

}
