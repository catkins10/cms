<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>修改个人资料</title>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/js/subPageCssImport.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/filetransfer/js/fileuploadclient.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/attachment/js/attachmentuploader.js"></script>
	<script>
		window.onload = function() {
			window.attachmentUploader = new AttachmentUploader();
			window.attachmentUploader.createUploadFrame(document.getElementById("uploadPortraitButton"), "<%=request.getContextPath()%>/jeaf/usermanage/uploadPortrait.shtml", "用户头像");
			window.attachmentUploader.onUploaded = function(attachmentNames, attachments) { //事件:文件上传完成
				var personPortrait = document.getElementById("personPortrait");
				personPortrait.onload = function() {
					AttachmentUploader.adjustUploadFramePosition();
				}
				personPortrait.src = personPortrait.src + "&seq=" + Math.random();
			};
			window.attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
				alert(errorDescription);
			};
		};
	</script>
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
<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" style="border-style:none; overflow-x:hidden; overflow-y:hidden">
    <ext:form action="/saveBbsUser">
  		<br>
		<center>
			<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="80%">
				<col width="120px" align="right">
				<col>
				<tr>
					<td nowrap>姓名：</td>
					<td><ext:write name="SessionInfo" property="userName" scope="session"/></td>
				</tr>
				<tr>
					<td nowrap>昵称：</td>
					<td><html:text styleClass="field" property="nickname"/></td>
				</tr>
				<tr>
					<td valign="top" nowrap>用户头像：</td>
					<td>
						<ext:personPortrait styleId="personPortrait" propertyPersonId="id" autoHeight="true" align="bottom"/>
						&nbsp;
						<span id="uploadPortraitButton" style="display:inline-block"><ext:button name="上传头像" width="80"/></span>
					</td>
				</tr>
				<ext:equal value="true" name="SessionInfo" property="systemUser" scope="session">
					<tr>
						<td>性别：</td>
						<td>
							<html:radio style="border-style:none; width:20px" property="sex" value="M" styleClass="radio" styleId="male" /><label for="male">&nbsp;男</label>
							<html:radio style="border-style:none; width:20px" property="sex" value="F" styleClass="radio" styleId="female" /><label for="female">&nbsp;女</label>
						</td>
						<td nowrap></td>
					</tr>
					<tr>
						<td nowrap>电子信箱：</td>
						<td><html:text styleClass="field" property="mailAddress"/></td>
					</tr>
					<tr>
						<td nowrap>家庭住址：</td>
						<td><html:text styleClass="field" property="familyAddress"/></td>
					</tr>
					<tr>
						<td nowrap>电　　话：</td>
						<td><html:text styleClass="field" property="telFamily"/></td>
					</tr>
					<tr>
						<td nowrap>手　　机：</td>
						<td><html:text styleClass="field" property="mobile"/></td>
					</tr>
					<tr>
						<td nowrap>办公室电话：</td>
						<td><html:text styleClass="field" property="tel"/></td>
					</tr>
				</ext:equal>
				<ext:equal value="false" name="SessionInfo" property="systemUser" scope="session">
					<tr>
						<td>电子邮箱：</td>
						<td><html:text styleClass="field" property="email" maxlength="50" styleClass="field required"/></td>
					</tr>
					<tr>
						<td>真实姓名：</td>
						<td><html:text styleClass="field" property="name" styleClass="field required"/></td>
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
						<td>所属省份：</td>
						<td><html:text styleClass="field" property="area"/></td>
					</tr>
					<tr>
						<td nowrap>工作单位/所在院校：</td>
						<td><html:text styleClass="field" property="company"/></td>
					</tr>
					<tr>
						<td>单位所属行业：</td>
						<td><html:text styleClass="field" property="organization"/></td>
					</tr>
					<tr>
						<td>部门：</td>
						<td><html:text styleClass="field" property="department"/></td>
					</tr>
					<tr>
						<td>职务：</td>
						<td><html:text styleClass="field" property="duty"/></td>
					</tr>
					<tr>
						<td>联系电话：</td>
						<td><html:text styleClass="field" property="telephone" maxlength="16"/></td>
					</tr>
					<tr>
						<td>手机：</td>
						<td><html:text styleClass="field" property="cell" maxlength="16"/></td>
					</tr>
					<tr>
						<td nowrap>是否公开个人资料：</td>
						<td>
							<html:radio style="border-style:none; width:20px" property="hideDetail" value="0" styleClass="radio" styleId="hideDetailNo" /><label for="hideDetailNo">&nbsp;公开</label>
							<html:radio style="border-style:none; width:20px" property="hideDetail" value="1" styleClass="radio" styleId="hideDetailYes" /><label for="hideDetailYes">&nbsp;不公开</label>
						</td>
					</tr>
				</ext:equal>
				<tr>
					<td colspan="2" align="center" style="padding-top:8px">
						<ext:button name="确定"  onclick="FormUtils.doAction('saveBbsUser');" width="50"/>
					</td>
				</tr>
			</table>
		</center>
		<html:hidden property="siteId"/>
    </ext:form>
</body>
</html:html>