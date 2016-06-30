<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/displayInterviewLiveLogin" applicationName="cms/interview" pageName="interviewLiveLogin">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/md5.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/sso/js/login.js"></script>
	<script>
	function interviewLogin() {
		var form = document.forms[0];
		var radios = document.getElementsByName("userType");
		if(radios[0].checked) {
			var userName = document.getElementsByName("guest")[0].value;
			if(userName=="") {
				alert("请选择访谈嘉宾");
				return;
			}
			document.getElementsByName("userName")[0].value = "interviewGuests_<ext:write property="id"/>_" + userName;
		}
		else if(document.getElementsByName("userName")[0].value=="") {
			alert("请输入用户名");
			return;
		}
		if(document.getElementsByName("password")[0].value=="") {
			alert("请输入口令");
			return;
		}
		form.action = "<%=request.getContextPath()%>/jeaf/sso/submitlogin.shtml";
		doLogin();
	}
	function onSelectUserType(type) {
		document.getElementById("trGuest").style.display = (type=="guest" ? "" : "none");
		document.getElementById("trSystemUser").style.display = (type=="systemUser" ? "" : "none");
	}
	</script>
	<table border="0" cellpadding="0" cellspacing="0" bgcolor="#ffffff" align="center" width="360px">
		<tr>
			<td width="100px" height="35" align="right" nowrap="nowrap">用户类型：</td>
			<td height="35"><ext:field onclick="onSelectUserType(value)" property="userType"/></td>
		</tr>
		<tr id="trGuest">
			<td height="35" align="right" nowrap="nowrap">访谈嘉宾：</td>
			<td height="35" align="left"><ext:field property="guest" /></td>
		</tr>
		<tr id="trSystemUser" style="display:none">
			<td height="35" align="right" nowrap="nowrap">主持人/审核人：</td>
			<td height="35" align="left"><ext:field property="userName"/></td>
		</tr>
		<tr>
			<td height="35" align="right" nowrap="nowrap">口令：</td>
			<td height="35" align="left"><ext:field property="password" onkeypress="if(event.keyCode==13)interviewLogin()"/></td>
		</tr>
		<tr>
			<td height="35" colspan="2" align="center">
				<input type="button" class="button" value="登 录" onclick="interviewLogin()"/>
			</td>
		</tr>
		<tr>
			<td height="35" colspan="2" align="center">
				<jsp:include page="/jeaf/form/warn.jsp" />
			</td>
		</tr>
	</table>
	<script>
		<ext:write property="changePasswordScript" filter="false"/>
	</script>
</ext:form>