<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>" enctype="multipart/form-data" style="margin:0px; background-color: transparent;">
	<%request.setAttribute("writeFormPrompt", "true");%>
	<div onclick="return !window.frameElement.attachmentUploader.uploadAttachment();">
		<html:file style="width:300px; height:300px; font-size:300px; margin-left:-10px;" onchange="window.frameElement.onFileSelected(this, value)" property="attachmentSelector.upload"/>
	</div>
	<ext:equal value="upload" property="attachmentSelector.action">
		<script>
			<ext:iterate property="errors" length="1" id="errorMessage">
				window.frameElement.onUploadError("<ext:write name="errorMessage"/>");
			</ext:iterate>
			<ext:empty property="errors">
				window.frameElement.onUploaded('<ext:write property="attachmentSelector.lastUploadFiles"/>');
			</ext:empty>
		</script>
	</ext:equal>
	<ext:equal value="processUploadFiles" property="attachmentSelector.action">
		<script>
			<ext:iterate property="errors" length="1" id="errorMessage">
				window.frameElement.onProcessUploadAttachmentsError("<ext:write name="errorMessage"/>");
			</ext:iterate>
			<ext:empty property="errors">
				window.frameElement.onUploadAttachmentsProcessed('<ext:write property="attachmentSelector.lastUploadFiles"/>', [<ext:iterate indexId="attachmentIndex" id="attachment" property="attachmentSelector.lastUploadAttachments"><ext:notEqual value="0" name="attachmentIndex">,</ext:notEqual>{name:'<ext:write name="attachment" property="name"/>', urlAttachment:'<ext:write name="attachment" property="urlAttachment"/>', urlInline:'<ext:write name="attachment" property="urlInline"/>', size:<ext:write name="attachment" property="size"/>, sizeAsText:'<ext:write name="attachment" property="fileSize"/>'}</ext:iterate>]);
			</ext:empty>
		</script>
	</ext:equal>
</ext:form>