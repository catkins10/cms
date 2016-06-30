<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<html:html>
<head>
	<title>互动教育网 - 注册学生账号</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
	<script>
		function FormUtils.reloadValidateCodeImage() {
			document.getElementById('validateCodeImage').src = document.getElementById('validateCodeImage').src;
		}
		function checkLoginNameInUse() {
			var loginName = document.getElementsByName("loginName")[0].value;
			if(loginName!="") {
				ScriptUtils.appendJsFile(document, "isLoginNameInUse.shtml?loginName=" + StringUtils.utf8Encode(loginName) + "&seq=" + Math.random(), "scriptCheckLoginNameInUse");
			}
		}
		function loginNameInUse(inUse) {
			var obj = document.getElementById("loginNamePrompt");
			obj.innerHTML = (inUse ? "用户名已经被占用" : "");
			obj.style.display = (inUse ? "" : "none");
		}
		function validate() {
			if(!validateRegistStudent(document.forms[0])) {
				return false;
			}
			if(document.getElementsByName("passwordConfirm")[0].value!=document.getElementsByName("password")[0].value) {
				alert("密码不一致！");
				return false;
			}
			return true;
		}
	</script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/passwordStrength.js"></script>
</head>
<body>
<ext:form action="/doRegistStudent" onsubmit="return validate();">
	<div id="header" style="width:100%;">
		<div class="inside">
			<table cellSpacing="0" cellPadding="0" border="0" style="width:100%; height:100%">
				<tr>
					<td class="title" valign="middle"></td>
					<td valign="bottom" align="right" style="padding-right:20px;padding-bottom:10px;font-size:14px">
						注册学生账号
					</td>
				</tr>
			</table>
		</div>
	</div>
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:80%" align="center">
		<col align="left">
		<col width="100%">
		<col>
		<tr>
			<td colspan="3" align="left" style="font-size:14px; height:30px; border-bottom:1 solid #a0c0ff" valign="bottom">基本信息</td>
		</tr>
		<tr>
			<td colspan="3" height="6px"></td>
		</tr>
		<tr>
			<td>所在班级：</td>
			<td>
				<html:hidden property="orgId" />
				<ext:field selectOnly="true" styleClass="field required" property="orgFullName" onSelect="DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgFullName{fullName}', '', 'class')"/>
			</td>
			<td nowrap>选择班级,浏览器设置成允许弹出窗口</td>
		</tr>
		<tr>
			<td>姓名：</td>
			<td><html:text property="name" styleClass="field required" maxlength="4"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>性别：</td>
			<td>
				<html:radio style="border-style:none; width:20px" property="sex" value="M" styleClass="radio" styleId="male" /><label for="male">&nbsp;男</label>
				<html:radio style="border-style:none; width:20px" property="sex" value="F" styleClass="radio" styleId="female" /><label for="female">&nbsp;女</label>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>座号：</td>
			<td><html:text property="seatNumber" styleClass="field required" value="" maxlength="4"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>电子邮箱：</td>
			<td><html:text property="mailAddress" maxlength="50" styleClass="field required"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>家庭地址：</td>
			<td><html:text property="familyAddress" maxlength="50"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>电话：</td>
			<td><html:text property="telFamily" maxlength="16"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td colspan="3" align="left" style="font-size:14px; height:30px; border-bottom:1 solid #a0c0ff" valign="bottom">账户信息</td>
		</tr>
		<tr>
			<td colspan="3" height="6px"></td>
		</tr>
		<tr valign="middle">
			<td>帐户名：</td>
			<td><html:text onblur="checkLoginNameInUse()" property="loginName" styleClass="field required" maxlength="20"/></td>
			<td nowrap>字母开头,允许使用字母、数字和下划线<div id="loginNamePrompt" style="color:#FF0000; display:none"></div></td>
		</tr>
		<tr>
			<td>口令：</td>
			<td><html:password property="password" styleClass="field required" maxlength="20"/></td>
			<td nowrap>不能包含字符%&amp;?+</td>
		</tr>
		<tr>
			<td nowrap="nowrap">口令安全等级：</td>
			<td>
				<span id="passwordStrength"></span>
				<script>writePasswordStrength(document.getElementsByName('password')[0], document.getElementById('passwordStrength'));</script>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>口令确认：</td>
			<td><input type="password" name="passwordConfirm" class="required" value="<ext:write property="password"/>" maxlength="20"></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td colspan="3" align="left" style="font-size:14px; height:30px; border-bottom:1 solid #a0c0ff" valign="bottom">家长信息</td>
		</tr>
		<tr>
			<td colspan="3" height="6px"></td>
		</tr>
		<tr>
			<td>家长称谓：</td>
			<td><ext:field styleClass="field required" property="genearchTitle" itemsText="父亲\0母亲\0祖父\0祖母\0外祖父\0外祖母" maxlength="15"/></td>
		</tr>
		<tr>
			<td>家长姓名：</td>
			<td><html:text property="genearchName" styleClass="field required" maxlength="4"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>家长手机/小灵通：</td>
			<td><html:text property="genearchMobile" styleClass="field required" maxlength="20"/></td>
			<td nowrap>小灵通必须输入区号</td>
		</tr>
		<tr>
			<td>家长邮箱：</td>
			<td><html:text property="genearchMail"  maxlength="50"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td colspan="3" align="left" style="font-size:14px; height:30px; border-bottom:1 solid #a0c0ff" valign="bottom">注册</td>
		</tr>
		<tr>
			<td colspan="3" height="6px"></td>
		</tr>
		<tr>
			<td>验证码：</td>
			<td>
				<img id="validateCodeImage" src="<%=request.getContextPath()%>/jeaf/validatecode/generateValidateCodeImage.shtml"> <a style="color:blue" href="javascript:FormUtils.reloadValidateCodeImage()">看不清，换一张</a>
				<html:text property="validateCode" styleClass="field required" maxlength="10"/>
			</td>
			<td nowrap="nowrap"></td>
		</tr>
		<tr>
			<td colspan="3" align="center" style="padding-top:8px">
				<input type="button" class="button" value="下一步" onclick="FormUtils.submitForm();">&nbsp;&nbsp;
				<input type="button" class="button" value="取消" onclick="window.close();">
			</td>
		</tr>
	</table>
</ext:form>
</body>
</html:html>