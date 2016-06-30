<%@page import="com.yuanluesoft.jeaf.util.DateTimeUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.UUIDLongGenerator"%>
<%@page import="com.yuanluesoft.cms.capture.pojo.CmsCaptureLog"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
	if(!request.getServerName().equals("localhost")) { //只允许在服务器上操作
		throw new Exception("localhost only");
	}
	BufferedReader bufferedReader = null;
	try {
		//2011-11-09 02:30:12,625 [DefaultQuartzScheduler_Worker-2] DEBUG - CaptureService: capture record page http://www.zzrqxh.com/web/view.asp?ID=22051
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:/Tomcat 5.0/logs/stdout.log"), "utf-8"));
		bufferedReader.skip(1);
		int i = 0;
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		for(String line = bufferedReader.readLine(); line!=null; line = bufferedReader.readLine()) {
			if((i++)<606020) {
				continue;
			}
			int index = line.indexOf("CaptureService: capture record page ");
			if(index==-1) {
				continue;
			}
			String url = line.substring(index + "CaptureService: capture record page ".length());
			if(databaseService.findRecordByHql("select CmsCaptureLog.id from CmsCaptureLog CmsCaptureLog where CmsCaptureLog.captureUrl='" + url + "'")==null) {
				CmsCaptureLog captureLog = new CmsCaptureLog();
				captureLog.setId(UUIDLongGenerator.generateId());
				captureLog.setCaptureTime(DateTimeUtils.parseTimestamp(line.substring(0, line.indexOf(",")), null));
				captureLog.setCaptureUrl(url);
				captureLog.setCaptureSuccess('1');
				databaseService.saveRecord(captureLog);
			}
		}
		out.print("ok");
	}
	catch(Exception e) {
		
	}
	finally {
		try {
			bufferedReader.close();
		}
		catch(Exception e) {
			
		}
	}
%>
