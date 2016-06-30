<%@page import="java.sql.Timestamp"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness"%>
<%@page import="com.yuanluesoft.jeaf.sms.pojo.SmsUnitConfig"%>
<%@page import="com.yuanluesoft.appraise.appraiser.pojo.Appraiser"%>
<%@page import="com.yuanluesoft.jeaf.sms.pojo.SmsReceive"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	
	try {
		Object appraiseService = (Object)Environment.getService("appraiseService");
		
		Method method = appraiseService.getClass().getMethod("onShortMessageReceived", new Class[]{String.class, String.class, Timestamp.class, String.class, SmsUnitBusiness.class});
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		for(int i=0; ;i+=200) {
			String hql = "from SmsReceive SmsReceive" +
						 " where SmsReceive.businessName is null" +
						 " and SmsReceive.receiveTime>TIMESTAMP(2013-05-31 12:00:00)" +
						 " order by SmsReceive.id";
			List messages = databaseService.findRecordsByHql(hql, i, 200);
			for(Iterator iterator = messages==null ? null : messages.iterator(); iterator.hasNext();) {
				SmsReceive sms = (SmsReceive)iterator.next();
				out.println(sms.getSenderNumber());
				//获取所在单位
				List appraisers = databaseService.findRecordsByHql("from Appraiser Appraiser where Appraiser.mobileNumber='" + sms.getSenderNumber() + "' and Appraiser.status=1 and Appraiser.type=1 and Appraiser.orgName like '平和%'");
				if(appraisers!=null && appraisers.size()==1) {
					Appraiser appraiser = (Appraiser)appraisers.get(0);
					out.println(appraiser.getOrgName());
					//获取短信单位配置
					SmsUnitBusiness unitBusiness = (SmsUnitBusiness)databaseService.findRecordByHql("from SmsUnitBusiness SmsUnitBusiness where SmsUnitBusiness.unitConfigId=(select SmsUnitConfig.id from SmsUnitConfig SmsUnitConfig where SmsUnitConfig.unitId=" + appraiser.getOrgId() + ")");
					if(unitBusiness!=null) {
						method.invoke(appraiseService, new Object[]{sms.getSenderNumber(), sms.getMessage(), sms.getReceiveTime(), sms.getReceiverNumber(), unitBusiness});
					}
				}
			}
		}
	}
	catch(Exception e) {
		out.println(e + e.getMessage());
	}
%>
</body>
</html>