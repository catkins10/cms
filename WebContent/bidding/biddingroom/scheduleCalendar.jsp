<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<html:html>
<head>
	<title>会场安排</title>
	<style type="text/css">
		body {
			background-color: transparent; 
			margin: 0px; 
			font: 12px 宋体; 
			overflow: hidden;
		}
		td,input{font: 12px 宋体; }
		A{color:#000000; text-decoration: none}
		A:link {color: #000000; text-decoration: none}
		A:visited {color: #000000; text-decoration: none}
		A:hover {color: #000000; text-decoration: none}
		A:active {color: #000000; text-decoration: none	}
		.day { height:19px}
		.today { height:19px; font-weight:bold}
		.previousMonth {padding-left:10px; padding-bottom:8px; font-size:10px; width:50px;}
		.currentMonth {padding-bottom:5px}
		.nextMonth {padding-right:10px; padding-bottom:8px; font-size:10px; width:50px;}
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