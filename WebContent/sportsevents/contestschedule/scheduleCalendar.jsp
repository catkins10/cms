<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<html:html>
<head>
	<title>赛程安排</title>
	<style type="text/css">
		body {
			background-color: transparent; 
			margin: 0px; 
			font: 12px 宋体; 
			overflow: hidden;
			
		}
		td,input{font: 12px 宋体; }
		A{color:#000000; text-decoration: none}
		A:link {color: #fff; text-decoration: none}
		A:visited {color: #000000; text-decoration: none}
		A:hover {color: #8fb7fc; text-decoration: none}
		A:active {color: #000000; text-decoration: none	}
		.day { height:19px;background-color:#4b82ca;color:#FFF;}
		.today {height:19px;font-weight:bold;background-color:#f7c21e;color:#FFF;}
		.previousMonth {padding-left:10px; padding-bottom:8px; font-size:10px; width:50px;background-color:#0a2b65;color:#FFF;}
		.currentMonth {padding-bottom:5px;background-color:#0a2b65;color:#FFF;}
		.nextMonth {padding-right:10px; padding-bottom:8px; font-size:10px; width:50px;background-color:#0a2b65;color:#FFF;}
		.week{background-color:#2354a9;color:#8fb7fc;}
	    .weekend{background-color:#2354a9;}
		.daywork{background-color:#f7f7f7;color:#999;}
		/**table tr td {border: solid thin #326ebe;}**/
		
	</style>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>
</head>
<body style="border:0; margin:0">
<ext:form action="/showScheduleCalendar">
	<center>
		<jsp:include page="/jeaf/view/calendarview/monthSimpleSwitch.jsp" />
		<jsp:include page="/jeaf/view/calendarview/monthSmallView.jsp" />
	</center>
</ext:form>
</body>
</html:html>