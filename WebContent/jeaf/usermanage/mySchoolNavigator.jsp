<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<LINK href="css/navigator.css" type="text/css" rel="stylesheet">
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
<ext:form action="/displaySchoolNavigator">
	<table id="navigator" cellSpacing="0" cellPadding="0" border="0" style="width:100%;table-layout:fixed" class="navigator">
		<tr><td class="navigatorHead"><ext:write property="schoolName"/></td></tr>
		<tr><td style="height:5px">&nbsp;</td></tr>
		<tr style="display:none"><td class="navigatorItem" onclick="top.frames[2].location='listToApprovalTeachers.shtml'">待审核注册</td></tr>
		<tr><td class="navigatorItemSelected" onclick="selectItem(this);top.frames[2].location='listSchoolTeachers.shtml'" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">教师列表</td></tr>
		<tr><td class="navigatorItem" onclick="selectItem(this);top.frames[2].location='listSchoolDepartments.shtml'" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">部门/教研组列表</td></tr>
		<tr><td class="navigatorItem" onclick="selectItem(this);top.frames[2].location='listSchoolClasses.shtml'" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">班级列表</td></tr>
		<tr><td class="navigatorItem" onclick="selectItem(this);top.frames[2].location='schoolRegistCode.shtml'" onmouseover="if(className=='navigatorItem')className='navigatorItemHover';" onmouseout="if(className=='navigatorItemHover')className='navigatorItem';">设置教师验证码</td></tr>
	</table>
</ext:form>
</body>
</html>