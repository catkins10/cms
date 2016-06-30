package com.yuanluesoft.cms.inquiry.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.yuanluesoft.cms.inquiry.pojo.Inquiry;
import com.yuanluesoft.cms.inquiry.pojo.InquiryOption;
import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 在线调查导入,SQL格式如下:
 "select " +
 " as importDataId," + //被导入的记录ID
 " as subject," + //调查主题
 " as endTime," + //调查截止时间
 " as isMultiSelect," + //是否多选, '1'/多选, '0'/单选
 " as publishResult," + //投票结果公示,0/不对外公开,1/投票结束后公开,2/总是公开
 " as isAnonymous," + //是否匿名投票, '1'、'0'
 " as minVote," + //最小投票数,0表示不限制
 " as maxVote," + //最大投票数,0表示不限制
 " as creator," + //创建人
 " as created," + //创建时间
 " as inquiryOptions," + //选项列表
 " as inquiryOptionSeparator," + //选项列表分隔符
 " as inquiryVotes," + //投票数列表
 " as inquiryVotesSeparator" + //投票数列表分隔符
 " from [原表名]";
 * @author linchuan
 *
 */
public abstract class InquiryImporter extends DataImporter {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "在线调查";
	}
	
	/**
	 * 获取生成调查选项的SQL
	 * "select " +
	 " as inquiryOption," + //选项
	 " as description," + //描述
	 " as vote" + //投票数
	 " from [原表名]";
	 * @param inquirySubjectId
	 * @return
	 */
	public abstract String generateInquiryOptionsSql(String inquirySubjectId);
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#saveImportedData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DataImporterCallback, java.sql.ResultSet)
	 */
	protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		InquiryService inquiryService = (InquiryService)Environment.getService("inquiryService");
		if(importedRecordId>0 && databaseService.findRecordById(InquirySubject.class.getName(), importedRecordId)!=null) { //检查是否原来导入的记录是否存在
			return importedRecordId;
		}
		InquirySubject inquirySubject = new InquirySubject();
		JdbcUtils.copyFields(inquirySubject, resultSet);
		afterPojoGenerated(inquirySubject, resultSet, connection); //回调
		
		inquirySubject.setId(UUIDLongGenerator.generateId()); //ID
		inquirySubject.setSiteId(targetSite.getId());
		inquirySubject.setIsPublic('1'); //总是设为已发布
		inquiryService.save(inquirySubject);
		
		//创建调查
		Inquiry inquiry = new Inquiry();
		JdbcUtils.copyFields(inquiry, resultSet);
		afterPojoGenerated(inquiry, resultSet, connection); //回调
		inquiry.setId(UUIDLongGenerator.generateId()); //ID
		inquiry.setSubjectId(inquirySubject.getId()); //主题ID
		inquiry.setDescription(null); //说明
		//inquiry.setisMultiSelect = '0'; //单选/多选
		//inquiry.setminVote; //最低投票数,0表示不限制
		//inquiry.setmaxVote; //最高投票数,0表示不限制
		//inquiry.setcreated; //创建时间
		//inquiry.setpriority; //优先级
		//inquiry.setInquirySubject(inquirySubject); //调查主题
		inquiryService.save(inquiry);
		
		//保存选项
		String options = resultSet.getString("inquiryOptions"); //选项列表
		String optionSeparator = resultSet.getString("inquiryOptionSeparator"); //分隔符
		if(options!=null && !options.isEmpty() && optionSeparator!=null && !optionSeparator.isEmpty()) {
			String[] inquiryOptions = options.split(optionSeparator);
			String inquiryVotes = resultSet.getString("inquiryVotes"); //选项列表
			String inquiryVotesSeparator = resultSet.getString("inquiryVotesSeparator"); //分隔符
			String[] votes = (inquiryVotes==null || inquiryVotes.isEmpty() || inquiryVotesSeparator==null || inquiryVotesSeparator.isEmpty() ? null : inquiryVotes.split(inquiryVotesSeparator));
			for(int i=0; i<inquiryOptions.length; i++) {
				InquiryOption inquiryOption = new InquiryOption();
				inquiryOption.setId(UUIDLongGenerator.generateId()); //ID
				inquiryOption.setInquiryId(inquiry.getId()); //调查ID
				inquiryOption.setInquiryOption(inquiryOptions[i]);
				inquiryService.save(inquiryOption);
				if(votes!=null && votes.length>i) {
					int vote = Integer.parseInt(votes[i]);
					if(vote>0) {
						inquiryService.setVoteNumber(inquiryOption.getId(), vote);
					}
				}
			}
		}
		else {
			String sql = generateInquiryOptionsSql(sourceDataId);
			if(sql!=null && sql.isEmpty()) {
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				try {
					while(rs.next()) {
						InquiryOption inquiryOption = new InquiryOption();
						JdbcUtils.copyFields(inquiryOption, rs);
						inquiryOption.setId(UUIDLongGenerator.generateId()); //ID
						inquiryOption.setInquiryId(inquiry.getId()); //调查ID
						inquiryService.save(inquiryOption);
						int vote = rs.getInt("vote");
						if(vote>0) {
							inquiryService.setVoteNumber(inquiryOption.getId(), vote);
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					rs.close();
					statement.close();
				}
			}
		}
		return inquirySubject.getId();
	}
}