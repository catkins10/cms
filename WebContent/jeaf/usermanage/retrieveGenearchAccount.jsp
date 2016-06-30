<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<html:html>
<head>
	<title>开通家长帐号</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
</head>
<body>
<ext:form action="/doRetrieveGenearchAccount">
	<div id="header" style="width:100%;">
		<div class="inside">
			<table cellSpacing="0" cellPadding="0" border="0" style="width:100%; height:100%">
				<tr>
					<td class="title" valign="middle"></td>
					<td valign="bottom" align="right" style="padding-right:20px;padding-bottom:10px;font-size:14px">
						开通家长帐号
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
			<td>您的姓名：</td>
			<td><html:text property="name" styleClass="field required"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>您的手机号码：</td>
			<td><html:text property="mobile" styleClass="field required"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>孩子所在班级：</td>
			<td>
				<html:hidden property="classId" />
				<ext:field selectOnly="true" styleClass="field required" property="classFullName" onSelect="DialogUtils.selectOrg(600, 400, false, 'classId{id},classFullName{fullName}', '', 'class')"/>
			</td>
			<td nowrap>选择班级,浏览器设置成允许弹出窗口</td>
		</tr>
		<tr>
			<td>孩子姓名：</td>
			<td><html:text property="childName" styleClass="field required"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td>验证码：</td>
			<td>
				<html:text property="validateCode" styleClass="field required"/>
			</td>
			<td nowrap="nowrap" style="color:#ff0000">输入短信上的验证码,如果没有,请联系您孩子的班主任<br>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="center" style="padding-top:8px">
				<input type="button" class="button" value="下一步" onclick="submit();">&nbsp;&nbsp;
			</td>
		</tr>
	</table>
</ext:form>
</body>
</html:html>