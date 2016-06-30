<%@page import="com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProgress"%>
<%@page import="com.yuanluesoft.dpc.keyproject.pojo.KeyProjectDeclare"%>
<%@page import="com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAnnualObjective"%>
<%@page import="com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestComplete"%>
<%@page import="com.yuanluesoft.dpc.keyproject.service.KeyProjectService"%>
<%@page import="com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvest"%>
<%@page import="com.yuanluesoft.workflow.client.model.instance.WorkflowInstance"%>
<%@page import="com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage"%>
<%@page import="com.yuanluesoft.jeaf.base.model.Element"%>
<%@page import="com.yuanluesoft.jeaf.util.DateTimeUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.UUIDLongGenerator"%>
<%@page import="com.yuanluesoft.dpc.keyproject.pojo.KeyProject"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@page import="com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="jxl.Cell"%>
<%@page import="jxl.Sheet"%>
<%@page import="java.io.File"%>
<%@page import="jxl.Workbook"%>
<%@page import="com.yuanluesoft.jeaf.sessionmanage.service.SessionService"%>
<%@page import="com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%!
	//获取单元格内容
	public String getContent(Cell[] cells, Map columnNameMap, String columnName, String nullValue) {
		Integer column = (Integer)columnNameMap.get(columnName);
		if(column==null) {
			return nullValue;
		}
		String content = cells[column.intValue()].getContents();
		if(content==null) {
			return nullValue;
		}
		content = content.trim();
		return content.equals("") ? nullValue : content;
	}

	//导入完成投资
	public void importInvestComplete(KeyProjectService keyProjectService, KeyProject keyProject, int year, int month, char tenDay, double investComplete) throws Exception {
		KeyProjectInvestComplete keyProjectInvestComplete = new KeyProjectInvestComplete();
		keyProjectInvestComplete.setId(UUIDLongGenerator.generateId()); //ID
		keyProjectInvestComplete.setProjectId(keyProject.getId()); //项目ID
		keyProjectInvestComplete.setApproverId(0); //审核人ID
		keyProjectInvestComplete.setApprovalTime(null); //审核时间
		keyProjectInvestComplete.setCompleteYear(year); //年份
		keyProjectInvestComplete.setCompleteMonth(month); //月份
		keyProjectInvestComplete.setCompleteTenDay(tenDay); //旬,上旬/1、中旬/2、下旬/3
		keyProjectInvestComplete.setInvestPlan(0); //计划完成投资（万元）
		keyProjectInvestComplete.setCompleted('1'); //是否已提交完成情况
		keyProjectInvestComplete.setCompleteInvest(investComplete); //完成投资（万元）
		//private double yearInvest; //年初至报告期累计完成投资（万元）
		//private double percentage; //占年计划（%）
		//private double totalComplete; //开工至报告期累计完成投资（万元）
		//private double completePercentage; //占总投资（%）
		keyProjectService.save(keyProjectInvestComplete);
	}
%>

<%
	String[] columnNames = {"序号", "项目名称", "项目性质", "建设内容及规模", "建设起止时间", "总投资（万元）", "至2009年底累计完成投资（万元）", 
							"2010年计划投资（万元）", "2010年1-7月完成投资（万元）", "8月上旬完成投资", "8月中旬完成投资", "后五个月关键节点的目标情况说明",
							"大干150天力争完成的投资额（万元）", "2010年力争完成的投资额（万元）", "项目业主单位业主名称", "项目业主单位负责人",
							"项目业主单位联系人", "项目业主单位电话及传真", "挂钩责任单位单位名称", "挂钩责任单位责任人",
							"挂钩责任单位联络人", "挂钩责任单位电话及传真", "挂钩领导", "行业", "报表名", "导入用户", "项目级别", "区域", "所属开发区"};
	Map columnNameMap = new HashMap();
	for(int i=0; i<columnNames.length; i++) {
		columnNameMap.put(columnNames[i], new Integer(i));
	}
	
	WorkflowExploitService workflowExploitService = (WorkflowExploitService)Environment.getService("workflowExploitService");
	SessionService sessionService = (SessionService)Environment.getService("sessionService");
	DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
	KeyProjectService keyProjectService = (KeyProjectService)Environment.getService("keyProjectService");
	SessionInfo sessionInfo = null; //开发区用户会话
	WorkflowEntry workflowEntry = null;
	Workbook workbook = Workbook.getWorkbook(new File("d:\\update\\漳州市新增长区域发展战役项目管理系统项目明细表.xls")); //读入报表模板
	Sheet sheet = workbook.getSheet(0);
	for(int i=1; i<sheet.getRows(); i++) {
		Cell[] cells = sheet.getRow(i);
		String userName = getContent(cells, columnNameMap, "导入用户", null);
		if(userName==null || userName.equals("")) {
			continue;
		}
		if(sessionInfo==null || !userName.equals(sessionInfo.getUserName())) {
			sessionInfo = sessionService.getSessionInfo((String)databaseService.findRecordByHql("select Person.loginName from Person Person where Person.name='" + userName + "'"));
			System.out.println(sessionInfo.getLoginName());
			List workflowEntries = workflowExploitService.listWorkflowEntries("dpc/keyproject", null, sessionInfo);
			workflowEntry = (WorkflowEntry)workflowEntries.get(0);
		}
		//检查项目是否已经存在
		String projectName = getContent(cells, columnNameMap, "项目名称", null);
		if(databaseService.findRecordByHql("select KeyProject.id from KeyProject KeyProject where KeyProject.name='" + projectName + "'")!=null) {
			continue;
		}
		System.out.println("import a project thar name is " + projectName + ".");
		//创建项目
		KeyProject keyProject = new KeyProject();
		keyProject.setId(UUIDLongGenerator.generateId());
		keyProject.setName(projectName); //项目全称（子项目）,重点项目全称（又称为子项目）
		//keyProject.setParentName(); //父项目名称,项目可分父项目和子项目，子项目指具体的项目成，父项目只是项目分类，没有明细的信息。
		keyProject.setSummary("　"); //项目简介,概要描述项目的地点、规模、内容、总体安排、投资资金等信息。
		keyProject.setLevel(getContent(cells, columnNameMap, "项目级别", "重点项目")); //分级管理（项目级别）,省级重点、市级重点、县级重点
		keyProject.setIndustry(getContent(cells, columnNameMap, "行业", "　")); //所属行业,包括交通、能源、农林业、工业园区、工业、社会事业、城建环保、旅游业、商贸服务业
		keyProject.setChildIndustry(null); //所属子行业,高速公路、铁路、热电、水利、石油化工、高新技术产业、电子信息技术、机械、传统产业等
		keyProject.setStatus(getContent(cells, columnNameMap, "项目性质", "　")); //项目状态,前期、在建、竣工、其他
		keyProject.setClassify(null); //项目类别,储备、在建和续建
		keyProject.setCategory(null); //项目分类,考核类、跟踪服务类
		keyProject.setLeader(getContent(cells, columnNameMap, "挂钩领导", "　")); //项目挂点领导,对本项目关注的领导以及挂点领导名单
		//keyProject.setOtherLeader(getContent(cells, columnNameMap, "挂钩领导")); //其他项目挂点领导
		keyProject.setDevelopmentArea(getContent(cells, columnNameMap, "所属开发区", "　")); //所属开发区
		keyProject.setAddress("　"); //建设地点
		keyProject.setInvestmentSubject("　"); //投资主体,包括国有、国有控股与外资合股、国有控股与民营合股、民营、民营控股与国有合资、外资独资、外资控股与国有合资、外资控股与民营合资、其他
		keyProject.setManagementUnit(getContent(cells, columnNameMap, "挂钩责任单位单位名称", "　")); //项目管理或责任部门,市经贸委、建设局、交通局、教育局、卫生局、水利局、商贸办、延平区、武夷山市、邵武市、建瓯市、建阳市、顺昌县、浦城县、光泽县、松溪县、政和县
		keyProject.setManagementUnitResponsible(getContent(cells, columnNameMap, "挂钩责任单位责任人", "　")); //管理部门责任人
		keyProject.setManagementUnitLinkman(getContent(cells, columnNameMap, "挂钩责任单位联络人", "　")); //管理部门联络人
		keyProject.setManagementUnitTel(getContent(cells, columnNameMap, "挂钩责任单位电话及传真", "　")); //管理部门电话
		keyProject.setUnit(getContent(cells, columnNameMap, "项目业主单位业主名称", "　")); //法人机构或者筹建单位,项目建设法人机构或者负责筹建的单位名称
		keyProject.setLawPerson(getContent(cells, columnNameMap, "项目业主单位负责人", "　")); //法人代表或者负责人
		keyProject.setLinkman(getContent(cells, columnNameMap, "项目业主单位联系人", "　")); //联系人
		keyProject.setUnitTel(getContent(cells, columnNameMap, "项目业主单位电话及传真", "　")); //单位电话
		keyProject.setUnitFax(null); //单位传真
		keyProject.setUnitAddress(null); //单位地址
		keyProject.setUnitPostcode(null); //邮政编码
		keyProject.setConstructionType("　"); //建设性质,新建、扩建
		
		String constructionYear = getContent(cells, columnNameMap, "建设起止时间", null);
		if(constructionYear!=null && !constructionYear.equals("")) {
			String[] values = constructionYear.trim().replace('－', '-').split("-");
			if(values.length>1) {
				int index;
				keyProject.setConstructionBeginYear((index=values[0].indexOf('.'))==-1 ? Integer.parseInt(values[0].trim()) : 2000 + Integer.parseInt(values[0].substring(0, index).trim())); //建设开始年限
				keyProject.setConstructionEndYear((index=values[1].indexOf('.'))==-1 ? Integer.parseInt(values[1].trim()) : 2000 + Integer.parseInt(values[1].substring(0, index).trim())); //建设结束年限
			}
		}
		
		keyProject.setEstimateBeginDate(null); //预计开工时间
		keyProject.setEstimateEndDate(null); //预计竣工时间
		keyProject.setBeginDate(null); //开工时间
		keyProject.setEndDate(null); //竣工时间
		keyProject.setAccountableInvest(0); //项目总投资（责任制）,项目总投资=项目总投资资金（责任制）拼盘中投资列表的总和
		
		try {
			keyProject.setInvest(Double.parseDouble(getContent(cells, columnNameMap, "总投资（万元）", "0"))); //项目总投资,项目总投资=项目总投资资金拼盘中投资列表的总和
		}
		catch(Exception e) {
			
		}
		keyProject.setInvestmentRemark(null); //总投资不一致备注,详细说明总投资不一致的原因
		keyProject.setConstructionScale(getContent(cells, columnNameMap, "建设内容及规模", "　")); //建设规模及主要建设内容
		keyProject.setConstructionEffect(null); //建成投产后增生生产能力,项目建设投产的成果和产能
		keyProject.setPlan(null); //总体安排以及各年计划
		keyProject.setCreated(DateTimeUtils.now()); //登记时间
		keyProject.setCreatorId(sessionInfo.getUserId()); //登记人ID
		keyProject.setCreator(sessionInfo.getUserName()); //登记人
		keyProject.setFiveYearPlan('0'); //是否五年计划项目
		try {
			keyProject.setInvestPlan150(Double.parseDouble(getContent(cells, columnNameMap, "大干150天力争完成的投资额（万元）", "0"))); //150天力争完成投资
		}
		catch(Exception e) {
			
		}

		//创建流程实例并发送
		WorkflowInstance workflowInstance = null;
		try {	
			Element activity = (Element)workflowEntry.getActivityEntries().get(0);
			WorkflowMessage workflowMessage = new WorkflowMessage("重点项目", keyProject.getName(), null);
			workflowInstance = workflowExploitService.createWorkflowInstanceAndSend(workflowEntry.getWorkflowId(), activity.getId(), keyProject, workflowMessage, null, sessionInfo);
			
			//保存项目
			keyProject.setWorkflowInstanceId(workflowInstance.getId());
			keyProjectService.save(keyProject);
			keyProjectService.updateProjectArea(keyProject, getContent(cells, columnNameMap, "区域", "漳州市"));
			
			//总投资（万元）
			KeyProjectInvest keyProjectInvest = new KeyProjectInvest();
			keyProjectInvest.setId(UUIDLongGenerator.generateId()); //ID
			keyProjectInvest.setProjectId(keyProject.getId()); //项目ID
			keyProjectInvest.setApproverId(0); //审核人ID
			keyProjectInvest.setApprovalTime(null); //审核时间
			keyProjectInvest.setSource("自筹"); //资金来源
			keyProjectInvest.setChildSource(null); //资金子来源
			keyProjectInvest.setAmount(keyProject.getInvest()); //资金金额
			keyProjectInvest.setRemark(null); //来源说明
			keyProjectService.save(keyProjectInvest);
			
			//至2009年底累计完成投资（万元）
			try {
				importInvestComplete(keyProjectService, keyProject, 2009, 12, '1', Double.parseDouble(getContent(cells, columnNameMap, "至2009年底累计完成投资（万元）", "0")));
			}
			catch(Exception e) {
				
			}
			
			//2010年1-7月完成投资（万元）
			try {
				importInvestComplete(keyProjectService, keyProject, 2010, 7, '1', Double.parseDouble(getContent(cells, columnNameMap, "2010年1-7月完成投资（万元）", "0")));
			}
			catch(Exception e) {
				
			}
			
			//8月上旬完成投资
			try {
				importInvestComplete(keyProjectService, keyProject, 2010, 8, '1', Double.parseDouble(getContent(cells, columnNameMap, "8月上旬完成投资", "0")));
			}
			catch(Exception e) {
				
			}
			
			//8月中旬完成投资
			try {
				importInvestComplete(keyProjectService, keyProject, 2010, 8, '2', Double.parseDouble(getContent(cells, columnNameMap, "8月中旬完成投资", "0"))); //完成投资（万元）
			}
			catch(Exception e) {
				
			}
			
			//2010年计划投资（万元）
			KeyProjectAnnualObjective keyProjectAnnualObjective = new KeyProjectAnnualObjective();
			keyProjectAnnualObjective.setId(UUIDLongGenerator.generateId()); //ID
			keyProjectAnnualObjective.setProjectId(keyProject.getId()); //项目ID
			keyProjectAnnualObjective.setApproverId(0); //审核人ID
			keyProjectAnnualObjective.setApprovalTime(null); //审核时间
			keyProjectAnnualObjective.setObjectiveYear(2010); //年度
			//计划完成投资
			try {
				keyProjectAnnualObjective.setInvestPlan(Double.parseDouble(getContent(cells, columnNameMap, "2010年计划投资（万元）", "0"))); //计划完成投资
			}
			catch(Exception e) {
				
			}
			//private double investCompleted; //已完成投资
			keyProjectAnnualObjective.setObjective("　"); //目标
			keyProjectService.save(keyProjectAnnualObjective);
			
			//后五个月关键节点的目标情况说明
			String plans = getContent(cells, columnNameMap, "后五个月关键节点的目标情况说明", "");
			if(plans!=null && !plans.equals("")) {
				KeyProjectProgress keyProjectProgress = new KeyProjectProgress();
				keyProjectProgress.setId(UUIDLongGenerator.generateId()); //ID
				keyProjectProgress.setProjectId(keyProject.getId()); //项目ID
				keyProjectProgress.setApproverId(0); //审核人ID
				keyProjectProgress.setApprovalTime(null); //审核时间
				keyProjectProgress.setProgressYear(2010); //年份
				keyProjectProgress.setProgressMonth(8); //月份
				keyProjectProgress.setProgressTenDay('1'); //旬,上旬/1、中旬/2、下旬/3
				keyProjectProgress.setPlan(plans); //安排
				keyProjectProgress.setCompleted('0'); //是否已汇报项目进度
				keyProjectProgress.setProgress(null); //进度
				keyProjectService.save(keyProjectProgress);
			}
			
			//申报重点项目
			KeyProjectDeclare keyProjectDeclare = new KeyProjectDeclare();
			keyProjectDeclare.setId(UUIDLongGenerator.generateId()); //ID
			keyProjectDeclare.setProjectId(keyProject.getId()); //项目ID
			keyProjectDeclare.setDeclareYear(2010); //申报年度
			keyProjectDeclare.setDeclareTime(DateTimeUtils.now()); //申报时间
			keyProjectDeclare.setIsKeyProject('1'); //是否列入重点项目
			keyProjectDeclare.setApprovalTime(DateTimeUtils.now()); //批准日期
			//keyProjectDeclare.setPriority(); //优先级
			keyProjectService.save(keyProjectDeclare);
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				workflowExploitService.removeWorkflowInstance(workflowInstance.getId(), sessionInfo);
			}
			catch(Exception we) {
				
			}
			try {
				databaseService.deleteRecord(keyProject);
			}
			catch(Exception we) {
				
			}
		}
	}
	workbook.close(); //关闭只读的Excel对象
	out.println("import complete.");
%>