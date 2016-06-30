<%@page import="java.sql.Timestamp"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness"%>
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
	Object appraiseService = (Object)Environment.getService("appraiseService");
	Method method = appraiseService.getClass().getMethod("onShortMessageReceived", new Class[]{String.class, String.class, Timestamp.class, String.class, SmsUnitBusiness.class});
	DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
	for(int i=0; ; i+=200) {
		String hql = "from SmsReceive SmsReceive" +
					 " where SmsReceive.receiveTime>TIMESTAMP(2013-08-13 08:00:00)" +
					 " and SmsReceive.receiverNumber like '10657306882004%'" +
					 " order by SmsReceive.id";
		List messages = databaseService.findRecordsByHql(hql, i, 200);
		for(Iterator iterator = messages==null ? null : messages.iterator(); iterator!=null && iterator.hasNext();) {
			SmsReceive sms = (SmsReceive)iterator.next();
			out.println(sms.getSenderNumber() + "," + sms.getReceiveTime() + "," + sms.getReceiverUnit() + "," + sms.getReceiverUnitId() + "<br>");
			try {
				//获取短信单位配置
				SmsUnitBusiness unitBusiness = (SmsUnitBusiness)databaseService.findRecordByHql("from SmsUnitBusiness SmsUnitBusiness where SmsUnitBusiness.businessId=" + sms.getBusinessId() + " and SmsUnitBusiness.unitConfigId=(select SmsUnitConfig.id from SmsUnitConfig SmsUnitConfig where SmsUnitConfig.unitId=" + sms.getReceiverUnitId() + ")");
				out.println(unitBusiness + "<br><br>");
				if(unitBusiness!=null) {
					method.invoke(appraiseService, new Object[]{sms.getSenderNumber(), sms.getMessage(), sms.getReceiveTime(), sms.getReceiverNumber(), unitBusiness});
				}
			}
			catch(Exception e) {
				out.println(e + e.getMessage());
			}
		}
		if(messages==null || messages.size()<200) {
			break;
		}
	}
%>
</body>
</html>