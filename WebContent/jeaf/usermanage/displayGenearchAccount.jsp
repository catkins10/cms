<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<html:html>
<head>
	<title>索取家长帐号</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/passwordStrength.js"></script>
	<script>
		function checkLoginNameInUse() {
			var loginName = document.getElementsByName("loginName")[0].value;
			var newLoginName = document.getElementsByName("newLoginName")[0].value;
			if(newLoginName!="" && newLoginName!=loginName) {
				ScriptUtils.appendJsFile(document, "isLoginNameInUse.shtml?loginName=" + StringUtils.utf8Encode(newLoginName) + "&seq=" + Math.random(), "scriptCheckLoginNameInUse");
			}
		}
		function loginNameInUse(inUse) {
			var obj = document.getElementById("loginNamePrompt");
			obj.innerHTML = (inUse ? "用户名已经被占用" : "");
			obj.style.display = (inUse ? "" : "none");
		}
		function validate() {
			if(document.getElementsByName("newPassword").length==0) {
				return true;
			}
			if(document.getElementsByName("passwordConfirm")[0].value!=document.getElementsByName("newPassword")[0].value) {
				alert("密码不一致！");
				return false;
			}
			return true;			
		}
		function showModifyAccount() {
			document.getElementById("trNewLoginName").style.display = "";
			document.getElementById("trNewPassword").style.display = "";
			document.getElementById("trCheckPassword").style.display = "";
			document.getElementById("trPasswordConfirm").style.display = "";
			document.getElementById("buttonSubmit").style.display = "";
			document.getElementById("buttonModifyAccount").style.display = "none";
		}
	</script>
</head>
<body>
<ext:form action="/modifyGenearchAccount" onsubmit="return validate();">
	<div id="header" style="width:100%;">
		<div class="inside">
			<table cellSpacing="0" cellPadding="0" border="0" style="width:100%; height:100%">
				<tr>
					<td class="title" valign="middle"></td>
					<td valign="bottom" align="right" style="padding-right:20px;padding-bottom:10px;font-size:14px">
						索取家长帐号
					</td>
				</tr>
			</table>
		</div>
	</div>
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:80%" align="center">
		<col align="left">
		<col width="100%">
		<col>
		<ext:empty property="loginName">
			<tr>
				<td colspan="3" style="color:#ff0000; padding:30px; font-size:14px" align="center">未发现有效的家长账户，请确认您输入的信息是否正确？</td>
			</tr>
			<tr>
				<td colspan="3" align="center" style="padding-top:8px">
					<input type="button" class="button" value="上一步" onclick="FormUtils.doAction('retrieveGenearchAccount');">&nbsp;&nbsp;
				</td>
			</tr>
		</ext:empty>
		<ext:notEmpty property="loginName">
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td nowrap>用户名：</td>
				<td>
					<ext:write property="name"/>
					<html:hidden property="name"/>
				</td>
				<td nowrap></td>
			</tr>
			<tr>
				<td nowrap>帐户名：</td>
				<td>
					<html:text property="loginName" readonly="true"/>
				</td>
				<td nowrap style="color:#ff0000">请您务必记住您的账户名和密码</td>
			</tr>
			<tr>
				<td nowrap>口令：</td>
				<td>
					<html:text property="password" readonly="true"/>
				</td>
				<td nowrap></td>
			</tr>
			<tr valign="middle" id="trNewLoginName" style="display:none">
				<td>新帐户名：</td>
				<td><html:text onblur="checkLoginNameInUse()" property="newLoginName" styleClass="field required" maxlength="20"/></td>
				<td nowrap>字母开头,允许使用字母、数字和下划线<div id="loginNamePrompt" style="color:#FF0000; display:none"></div></td>
			</tr>
			<tr id="trNewPassword" style="display:none">
				<td>新口令：</td>
				<td><html:password property="newPassword" styleClass="field required"/></td>
				<td nowrap></td>
			</tr>
			<tr id="trCheckPassword" style="display:none">
				<td nowrap="nowrap">口令安全等级：</td>
				<td>
					<span id="passwordStrength"></span>
					<script>writePasswordStrength(document.getElementsByName('newPassword')[0], document.getElementById('passwordStrength'));</script>
				</td>
				<td nowrap></td>
			</tr>
			<tr id="trPasswordConfirm" style="display:none">
				<td>新口令确认：</td>
				<td><input type="password" name="passwordConfirm" class="required" value="<ext:write property="newPassword"/>"></td>
				<td nowrap></td>
			</tr>
			<tr>
				<td colspan="3" align="center" style="padding-top:20px">
					<input id="buttonModifyAccount" type="button" class="button" value="修改帐户名或密码" onclick="showModifyAccount();" style="width:120px">
					<input id="buttonSubmit" type="button" class="button" value="确定" onclick="if(document.forms[0].onsubmit())submit();" style="display:none;width:36px">
					<input type="button" class="button" value="关闭窗口" onclick="top.close();" style="width:66px">
				</td>
			</tr>
		</ext:notEmpty>
	</table>
	<html:hidden property="name"/>
	<html:hidden property="mobile"/>
	<html:hidden property="classId" />
	<html:hidden property="classFullName"/>
	<html:hidden property="childName"/>
	<html:hidden property="validateCode"/>
</ext:form>
</body>
</html:html>