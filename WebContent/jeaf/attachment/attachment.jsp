<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal property="attachmentSelector.uploadFrame" value="true">
	<jsp:include page="uploadFrame.jsp"/>
</ext:equal>

<ext:notEqual property="attachmentSelector.uploadFrame" value="true">
<html style="border-style:none; margin:0px; overflow:hidden; background-color:transparent; padding:0px; background-image: none !important;">
<body style="border-style:none; margin:0px; overflow:hidden; background-color:transparent; padding:0px; background-image: none !important;" onscroll="return false;">
<ext:form styleClass="form" style="margin:0px;overflow:hidden;" action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/filetransfer/js/fileuploadclient.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/attachment/js/attachmentuploader.js"></script>
		<script>
		if(parent.parent.location.href.indexOf('/dialog.shtml')!=-1) { //对话框
			CssUtils.cloneStyle(parent.parent.document, document);
		}
		CssUtils.cloneStyle(parent.document, document);
	</script>
	<ext:equal value="false" property="attachmentSelector.uploadDisabled">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr valign="middle">
				<td width="100%">
					<ext:iterate id="attachment" property="attachmentSelector.attachments">
						<input name="urlInline.<ext:write name="attachment" property="name"/>" type="hidden"" value="<ext:write name="attachment" property="urlInline"/>">
						<input name="urlAttachment.<ext:write name="attachment" property="name"/>" type="hidden"" value="<ext:write name="attachment" property="urlAttachment"/>">
					</ext:iterate>
					<ext:field property="attachmentSelector.selectedTitles"/>
				</td>
				<td nowrap style="padding-right:3px">
					<ext:notEqual value="1" property="attachmentSelector.maxUpload">
						(<ext:write property="attachmentSelector.attachmentCount"/>)
					</ext:notEqual>
				</td>
				<td nowrap>
					<div style="position:relative;">
						<span id="messageSpan" style="display:none; width:140px; border:1px solid #aaaaaa; background:#ffff66; font-family:宋体; font-size:12px; padding: 3px;">正在处理中...</span>
						<input type="button" class="button" name="uploadButton" value="上传" style="width:42px">
						<ext:equal value="false" property="attachmentSelector.simpleMode">
							<input type="button" class="button" value="查看" onclick="downloadAttachment(true)">
							<input type="button" class="button" value="下载" onclick="downloadAttachment(false)">
						</ext:equal>
						<input type="button" class="button" value="删除" onclick="doDelete();">
						<ext:equal value="false" property="attachmentSelector.simpleMode">
							<ext:notEqual value="1" property="attachmentSelector.maxUpload">
								<input type="button" class="button" value="全部删除" onclick="doDeleteAll()" style="width:64px">
							</ext:notEqual>
						</ext:equal>
					</div>
				</td>
			</tr>
		</table>
		<script>
			function downloadAttachment(inline) { //下载附件
				if(window.attachmentUploader && window.attachmentUploader.isUploading) {
					return;
				}
				var attachmentName = document.getElementsByName("attachmentSelector.selectedNames")[0].value;
				if(attachmentName=="") {
					return;
				}
				if(inline) {
					window.open(document.getElementsByName("urlInline." + attachmentName)[0].value);
				}
				else {
					location.href = (document.getElementsByName("urlAttachment." + attachmentName)[0].value);
				}
			}
			function doDelete() { //删除选中的附件
				if(window.attachmentUploader && window.attachmentUploader.isUploading) {
					return;
				}
				if(document.getElementsByName('attachmentSelector.selectedNames')[0].value!='' && confirm("删除后不可恢复，是否确定删除？")) {
					doSubmit("delete");
				}
			}
			function doDeleteAll() { //删除全部附件
				if(window.attachmentUploader && window.attachmentUploader.isUploading) {
					return;
				}
				if(confirm("删除后不可恢复，是否确定删除？")) {
					doSubmit("deleteAll");
				}
			}
			function doSubmit(attachmentAction) { //提交
				document.forms[0].action += location.search;
				document.getElementsByName("attachmentSelector.action")[0].value = attachmentAction;
				FormUtils.submitForm();
			}
			function showProgress(attachmentUploader, percent, speed) { //显示进度
				if(!document.getElementById("uploadPercent")) {
					document.getElementById("messageSpan").style.padding = '0';
					var html = '<span style="display: inline-block; position: relative; width:100%; height:20px;">' +
							   '	<span id="uploadPercent" style="display: inline-block; position: absolute; background-color: #E1F1FF; font-size:1px; height:100%; width:0px">&nbsp;</span>' +
							   '	<span id="uploadText" style="display: inline-block; position: relative; width:116px; height:100%; color:#000000; padding:3px">上传中...</span>' +
							   (window.attachmentUploader.isCancelable() ? '	<img id="cancelButton" title="取消" style="position: relative;" src="<%=request.getContextPath()%>/jeaf/attachment/icons/delete.gif" align="absmiddle"/>&nbsp;' : '') +
							   '</span>';
					document.getElementById("messageSpan").innerHTML = html;
					if(window.attachmentUploader.isCancelable()) {
						document.getElementById("cancelButton").onclick = function() {
							window.attachmentUploader.cancelUpload();
							document.getElementById("messageSpan").style.display = 'none';
						};
					}
				}
				if(percent!=null) {
					document.getElementById("uploadPercent").style.width = percent + "%";
					var uploadText = document.getElementById("uploadText");
					uploadText.innerHTML = "上传中,已完成" + percent + "%";
					if(speed && speed>0) {
						uploadText.title = StringUtils.formatBytes(speed) + "/S";
					}
				}
				document.getElementById("messageSpan").style.display = 'inline-block';
			}
			function createAttachmentUploader() { //创建附件上传器
				var uploadButton = document.getElementsByName('uploadButton')[0];
				if(!uploadButton) {
					return;	
				}
				window.attachmentUploader = new AttachmentUploader();
				window.attachmentUploader.createUploadFrame(uploadButton, window.location.href, '<ext:write property="attachmentSelector.title"/>');
				window.attachmentUploader.onUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent) { //事件:文件上传中
					showProgress(window.attachmentUploader, percent, speed); //显示进度
				};
				window.attachmentUploader.onUploaded = function(attachmentNames, attachments) { //事件:文件上传完成
					document.getElementsByName('attachmentSelector.lastUploadFiles')[0].value = attachmentNames;
					doSubmit();
				};
				window.attachmentUploader.onStopped = function() { //事件:文件停止上传
					document.getElementById("messageSpan").style.display = 'none';
				};
				window.attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
					document.getElementById("messageSpan").style.display = 'none';
					alert(errorDescription);
				};
			}
			window.onload = function() {
				createAttachmentUploader();
				<ext:notEmpty property="attachmentSelector.extendJs">
					ScriptUtils.appendJsFile(document, "<%=request.getContextPath()%><ext:write property="attachmentSelector.extendJs"/>");
				</ext:notEmpty>
				var setFrameHeight = function() {
					if(document.body.scrollHeight <= 0) {
						window.setTimeout(setFrameHeight, 200);
					}
					else {
						window.frameElement.style.height = document.body.scrollHeight + "px";
					}
				};
				setFrameHeight.call(null);
			};
		</script>
	</ext:equal>
</ext:form>
</body>
</html>
</ext:notEqual>