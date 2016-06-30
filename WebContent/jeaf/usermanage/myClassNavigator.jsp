<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<LINK href="css/navigator.css" type="text/css" rel="stylesheet">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script>
	function selectItem(item) {
		var rows = document.getElementById("navigator").rows;
		for(var i=0; i<rows.length; i++) {
			if(rows[i].cells[0].className=="navigatorItemSelected") {
				rows[i].cells[0].className = "navigatorItem";
				break;
			}
		}
		item.className = "navigatorItemSelected";
	}
	</script>
</head>
<style>
	
</style>
<body leftmargin="0px" rightmargin="0px" topmargin="0" bottommargin="0" style="overflow:hidden; border:none">
<ext:form action="/displayClassNavigator">
	<table id="navigator" cellSpacing="0" cellPadding="0" border="0" style="width:100%;table-layout:fixed" class="navigator">
		<tr><td class="navigatorHead"><ext:write property="className"/></td></tr>
		<tr><td style="height:5px">&nbsp;</td></tr>
		<tr><td class="navigatorItemSelected" onclick="selectItem(this);top.frames[2].location='listToApprovalStudents.shtml'" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">待审批注册</td></tr>
		<tr><td class="navigatorItem" onclick="selectItem(this);top.frames[2].location='listClassStudents.shtml'" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">学生列表</td></tr>
		<tr><td class="navigatorItem" onclick="PageUtils.editrecord('jeaf/usermanage','schoolClass','<ext:write property="classId"/>','width=720,height=480');" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">修改班级资料</td></tr>
	</table>
</ext:form>
</body>
</html>