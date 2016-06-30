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
					 " where SmsReceive.receiverNumber like '10657306024454004%'"+ 
					 " and SmsReceive.receiverUnitId=0" +
					 " and SmsReceive.receiveTime>TIMESTAMP(2014-03-21 00:00:00)" +
					 " order by SmsReceive.id";
		List messages = databaseService.findRecordsByHql(hql, i, 200);
		for(Iterator iterator = messages==null ? null : messages.iterator(); iterator!=null && iterator.hasNext();) {
			SmsReceive sms = (SmsReceive)iterator.next();
			out.println(sms.getSenderNumber() + "," + sms.getReceiveTime() + "," + sms.getReceiverUnit() + "," + sms.getReceiverUnitId() + "<br>");
			try {
				//获取扩展的短信号码
				String extendNumber = sms.getReceiverNumber().substring("10657306024454004".length());
				//获取短信业务配置
				hql = "select SmsUnitBusiness" +
							 " from SmsUnitBusiness SmsUnitBusiness left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
							 " where SmsUnitBusiness.smsNumber" + (extendNumber==null || extendNumber.isEmpty() ? " is null" : "='" + extendNumber + "'") +
							 " and SmsUnitConfig.smsClientName='" + sms.getSmsClientName() + "'";
				List unitBusinessList = databaseService.findRecordsByHql(hql, 0, 2);
				if(unitBusinessList!=null && unitBusinessList.size()==1) { //只有一个
					SmsUnitBusiness smsUnitBusiness = (SmsUnitBusiness)unitBusinessList.get(0);
					sms.setBusinessId(smsUnitBusiness.getBusinessId()); //短信业务ID
					sms.setBusinessName(smsUnitBusiness.getBusinessName()); //短信业务名称
					sms.setReceiverUnitId(smsUnitBusiness.getUnitConfig().getUnitId()); //单位ID
					sms.setReceiverUnit(smsUnitBusiness.getUnitConfig().getUnitName()); //单位名称
					out.println(smsUnitBusiness.getUnitConfig().getUnitName() + "<br>");
					databaseService.updateRecord(sms);
					method.invoke(appraiseService, new Object[]{sms.getSenderNumber(), sms.getMessage(), sms.getReceiveTime(), sms.getReceiverNumber(), smsUnitBusiness});
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