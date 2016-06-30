<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<LINK href="<%=request.getContextPath()%>/jeai/usermanage/css/usermanage.css" type="text/css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
	<style>
		a.fileUpload {
			background-image:url(image/uploadPortrait.gif);
			background-repeat:no-repeat;
			margin-top:-1px;
			position:relative;
			text-decoration:none;
			top:0pt;
			width:70px;
			height:20px;
			border-style: none;
		}
		input.fileUpload {
			cursor:pointer;
			height:18px;
			filter:alpha(opacity=0); 
			position:absolute;
			width:70px;
			z-index: 0;
			border-style: none;
		}
	</style>
</head>
<body>
	<ext:form action="/savePersonalData" onsubmit="return formOnSubmit();">
	   	<center><div style="width:430px"><%@ include file="/edu/skins/v2/default/detailFormPrompt.jsp" %></div></center>
  		<table border="0" cellpadding="3" cellspacing="3" style="color:#000000; width:550px" align="center">
			<col align="right">
			<col width="60%">
			<col width="40%">
			<tr>
				<td nowrap>姓名：</td>
				<td><ext:write property="userName"/></td>
			</tr>
			<ext:equal property="type" value="1">
				<tr>
					<td nowrap>所在单位/部门：</td>
					<td>
						<html:hidden property="orgId"/>
						<ext:field selectOnly="true" property="orgFullName" onSelect="DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgFullName{fullName}', '', 'school,schoolDepartment,unit,unitDepartment', '', '', '', true)"/>
					</td>
					<td></td>
				</tr>
			</ext:equal>
			<ext:equal property="type" value="2">
				<tr>
					<td nowrap>所在学校/部门：</td>
					<td>
						<html:hidden property="orgId"/>
						<ext:field selectOnly="true" property="orgFullName" onSelect="DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgFullName{fullName}', '', 'school,schoolDepartment', '', '', '', true)"/>
					</td>
					<td></td>
				</tr>
			</ext:equal>
			<ext:equal property="type" value="3">
				<ext:notEqual property="remark" value="unicom">
					<tr>
						<td nowrap>所在班级：</td>
						<td>
							<html:hidden property="orgId"/>
							<ext:equal value="true" property="finishSchool">
								<ext:field selectOnly="true" property="orgFullName" onSelect="DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgFullName{fullName}', '', 'class', '', '', '', false)"/>
							</ext:equal>
							<ext:equal value="false" property="finishSchool">
								<ext:field selectOnly="true" property="orgFullName" onSelect="DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgFullName{fullName}', '', 'class', '', '', '', true)"/>
							</ext:equal>
						</td>
						<td></td>
					</tr>
					<tr>
						<td nowrap>座号：</td>
						<td><html:text property="seatNumber"/></td>
						<td></td>
					</tr>
				</ext:notEqual>
				<ext:empty property="unicomPersonId">
					<tr>
						<td nowrap valign="top">身份证号码：</td>
						<td>
							<html:text property="identityCard"/>
						</td>
						<td>本人或父母的身份证号码</td>
					</tr>
					<tr>
						<td nowrap>身份证号码确认：</td>
						<td><input type="text" name="identityCardConfirm"></td>
						<td></td>
					</tr>
					<tr>
						<td nowrap>身份证姓名：</td>
						<td><html:text property="identityCardName"/></td>
						<td></td>
					</tr>
				</ext:empty>
				<ext:notEmpty property="unicomPersonId">
					<tr>
						<td nowrap>联通号码：</td>
						<td><ext:write property="unicomPersonId"/></td>
						<td></td>
					</tr>
					<tr>
						<td nowrap>身份证号码：</td>
						<td><ext:write property="identityCard"/></td>
						<td></td>
					</tr>
					<tr>
						<td nowrap>身份证姓名：</td>
						<td><ext:write property="identityCardName"/></td>
						<td></td>
					</tr>
				</ext:notEmpty>
			</ext:equal>
			<ext:equal property="type" value="4">
				<tr>
					<td nowrap>孩子所在班级：</td>
					<td>
						<ext:write property="orgFullName"/>
					</td>
					<td></td>
				</tr>
			</ext:equal>
			<tr>
				<td valign="top" nowrap>用户头像：</td>
				<td>
					<ext:personPortrait propertyPersonId="id" autoHeight="true" align="bottom"/>
					&nbsp;<a class="fileUpload"><html:file styleClass="fileUpload" property="portraitUpload" onchange="uploadImage('portraitUpload', 'uploadPortrait')"/></a>
				</td>
				<td></td>
			</tr>
			<tr>
				<td nowrap>性别：</td>
				<td>
					<html:radio property="sex" value="M" styleClass="radio" styleId="male"/><label for="male">&nbsp;男</label>
					<html:radio property="sex" value="F" styleClass="radio" styleId="female"/><label for="female">&nbsp;女</label>
				</td>
				<td></td>
			</tr>
			<tr>
				<td nowrap>电子信箱：</td>
				<td><html:text property="mailAddress"/></td>
				<td></td>
			</tr>
			<tr>
				<td nowrap>家庭住址：</td>
				<td><html:text property="familyAddress"/></td>
				<td></td>
			</tr>
			<tr>
				<td nowrap>电话：</td>
				<td><html:text property="telFamily"/></td>
				<td></td>
			</tr>
			<tr>
				<td nowrap>手机：</td>
				<td><html:text property="mobile"/></td>
				<td></td>
			</tr>
			<tr>
				<td nowrap>办公室电话：</td>
				<td><html:text property="tel"/></td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2" align="center" style="padding-top:8px">
					<input type="button" class="button" value="确定" onclick="FormUtils.submitForm();"/>&nbsp;&nbsp;
				</td>
				<td></td>
			</tr>
		</table>
		<script>
			function formOnSubmit() {
				if(document.getElementsByName("identityCardConfirm")[0] && document.getElementsByName("identityCardConfirm")[0].value!=document.getElementsByName("identityCard")[0].value) {
					alert("身份证号码不一致！");
					return false;
				}
				return true;
			}
		</script>
	</ext:form>
</body>
</html:html>