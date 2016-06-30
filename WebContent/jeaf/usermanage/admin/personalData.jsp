<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/savePersonalData">
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
					DialogUtils.adjustDialogSize();
				}
				personPortrait.src = personPortrait.src + "&seq=" + Math.random();
			};
			window.attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
				alert(errorDescription);
			};
		};
	</script>
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000; width:100%" align="center">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>姓名：</td>
			<td><ext:write property="userName"/></td>
		</tr>
		<tr>
			<td valign="bottom" nowrap>用户头像：</td>
			<td>
				<ext:personPortrait styleId="personPortrait" propertyPersonId="id" autoHeight="true" align="bottom"/>
				&nbsp;
				<input type="button" class="button" id="uploadPortraitButton" value="上传头像"/>
			</td>
		</tr>
		<ext:equal property="type" value="1">
			<tr>
				<td nowrap>所在单位/部门：</td>
				<td><ext:field writeonly="true" property="orgFullName"/></td>
			</tr>
		</ext:equal>
		<ext:equal property="type" value="2">
			<tr>
				<td nowrap>所在学校/部门：</td>
				<td><ext:field writeonly="true" property="orgFullName"/></td>
			</tr>
		</ext:equal>
		<ext:equal property="type" value="3">
			<tr>
				<td nowrap>所在班级：</td>
				<td><ext:field writeonly="true" property="orgFullName"/></td>
			</tr>
			<tr>
				<td nowrap>座号：</td>
				<td><ext:field property="seatNumber"/></td>
			</tr>
		</ext:equal>
		<ext:equal property="type" value="4">
			<tr>
				<td nowrap>孩子所在班级：</td>
				<td><ext:field writeonly="true" property="orgFullName"/></td>
			</tr>
		</ext:equal>
		<tr>
			<td nowrap>电子信箱：</td>
			<td><ext:field property="mailAddress"/></td>
		</tr>
		<tr>
			<td nowrap>家庭住址：</td>
			<td><ext:field property="familyAddress"/></td>
		</tr>
		<tr>
			<td nowrap>电　　话：</td>
			<td><ext:field property="telFamily"/></td>
		</tr>
		<tr>
			<td nowrap>手　　机：</td>
			<td><ext:field property="mobile"/></td>
		</tr>
		<tr>
			<td nowrap>办公室电话：</td>
			<td><ext:field property="tel"/></td>
		</tr>
	</table>
</ext:form>