<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal property="attachmentSelector.uploadFrame" value="true">
	<jsp:include page="uploadFrame.jsp"/>
</ext:equal>

<ext:notEqual property="attachmentSelector.uploadFrame" value="true">
<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
	<style>
		.normal {
			border:#c0c0c0 1px solid; background-color: #ffffff; padding:0px;
		}
		.selected {
			border:#0022FF 1px solid; background-color: #ffffff !important; padding:0px;
		}
	</style>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/filetransfer/js/fileuploadclient.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/attachment/js/attachmentuploader.js"></script>
	<table border="0" height="100%" width="100%" cellspacing="0" cellpadding="0px" style="table-layout:fixed">
		<tr>
			<td height="20px"><ext:write property="attachmentSelector.title"/>列表:</td>
		</tr>
		<tr>
			<td height="100%">
				<div id="divAttachments" style="background:white; border:gray 1px solid; width:100%; height:100%; overflow:auto; padding:0px; display: none;">
					<ext:iterate id="attachment" property="attachmentSelector.attachments">
						<div onclick="clickAttachment(this, event.ctrlKey)" ondblclick="dblclickAttachment(this)" style="cursor:pointer; float:left; width:110px; height:130px; padding:5px" title="<ext:write name="attachment" property="description"/>">
							<input name="attachmentURL" type="hidden" value="<ext:write name="attachment" property="urlInline"/>">
							<input name="attachmentName" type="hidden" value="<ext:write name="attachment" property="name"/>">
							<ext:equal value="true" property="attachmentSelector.image">
								<input name="imageWidth" type="hidden" value="<ext:write name="attachment" property="width"/>">
								<input name="imageHeight" type="hidden" value="<ext:write name="attachment" property="height"/>">
							</ext:equal>
							<ext:equal value="true" property="attachmentSelector.video">
								<input name="videoWidth" type="hidden" value="<ext:write name="attachment" property="videoWidth"/>">
								<input name="videoHeight" type="hidden" value="<ext:write name="attachment" property="videoHeight"/>">
								<input name="previewImageUrl" type="hidden" value="<ext:write name="attachment" property="previewImageUrl"/>">
							</ext:equal>
							<table border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed; width:100%">
								<tr>
									<td align="center" valign="middle" height="100px" class="normal">
										<img src="<ext:write name="attachment" property="iconURL"/>">
									</td>
								</tr>
								<tr>
									<td align="center" valign="top" style="padding-top:3px">
										<ext:write name="attachment" property="name" maxCharCount="16" ellipsis="..."/>
									</td>
								</tr>
							</table>
						</div>
					</ext:iterate>
				</div>
			</td>
		</tr>
	</table>
	<script>
		var selectedAttachments = [];
		function clickAttachment(attachment, multiSelect) {
			//判断原来是不是已经选中
			var i = selectedAttachments.length - 1;
			for(; i>=0 && selectedAttachments[i]!=attachment; i--);
			if(multiSelect) {
				if(i>=0) { //已经选中
					selectedAttachments.splice(i, 1); //从选中的列表中删除
					setAttachmentStyle(attachment, "normal"); //设置为非选中的状态
				}
				else {
					selectedAttachments.push(attachment); //添加到选中的列表
					setAttachmentStyle(attachment, "selected"); //设置为选中的状态
				}
			}
			else if(i<0) { //原来没有选中
				for(i=selectedAttachments.length - 1; i>=0; i--) {
					setAttachmentStyle(selectedAttachments[i], "normal"); //设置为非选中的状态
				}
				//设置原来选中的为非选中的状态
				selectedAttachments.splice(0, selectedAttachments.length); //清除对列
				selectedAttachments.push(attachment);
				setAttachmentStyle(attachment, "selected"); //设置为选中的状态
			}
		}
		function setAttachmentStyle(attachment, styleClass) {
			attachment.getElementsByTagName("table")[0].rows[0].cells[0].className = styleClass;
		}
		function dblclickAttachment(attachment) {
			doOK();
		}
		function doDeleteAll() { //删除全部
			if(confirm("删除后不可恢复，是否确定删除？")) {
				doSubmit('deleteAll');
			}
		}
		function doDelete() { //删除附件
			if(selectedAttachments.length>0 && confirm("删除后不可恢复，是否确定删除？")) {
				var selectedNames = "";
				for(var i=0; i<selectedAttachments.length; i++) {
					selectedNames += (i==0 ? "" : "*") + selectedAttachments[i].getElementsByTagName("input")[1].value
				}
				document.getElementsByName("attachmentSelector.selectedNames")[0].value = selectedNames;
				doSubmit("delete");
			}
		}
		function doOK() { //完成选择
			if(selectedAttachments.length==0) {
				return;
			}
			var script = document.getElementsByName("attachmentSelector.scriptRunAfterSelect")[0].value;
			if(script!="") {
				script = script.replace("{URL}", DomUtils.getElement(selectedAttachments[0], 'input', 'attachmentURL').value);
				var attachmentName = DomUtils.getElement(selectedAttachments[0], 'input', 'attachmentName').value;
				script = script.replace("{attachmentName}", attachmentName);
				script = script.replace("{fileName}", attachmentName);
				script = script.replace("{fileShortName}", attachmentName.substring(0, attachmentName.lastIndexOf('.')));
				script = script.replace("{recordClassName}", "<ext:write property="formDefine.recordClassName"/>");
				script = script.replace("{applicationName}", "<ext:write property="formDefine.applicationName"/>");
				script = script.replace("{id}", document.getElementsByName("id")[0].value);
				script = script.replace("{attachmentType}", "<ext:write property="attachmentSelector.type"/>");
				var width = 0;
				var height = 0;
				var isImage = <ext:write property="attachmentSelector.image"/>;
				var isVideo = <ext:write property="attachmentSelector.video"/>;
				if(isImage || isVideo) {
					width = DomUtils.getElement(selectedAttachments[0], 'input', (isImage ? "image" : "video") + 'Width').value;
					height = DomUtils.getElement(selectedAttachments[0], 'input', (isImage ? "image" : "video") + 'Height').value;
				}
				script = script.replace("{WIDTH}", width).replace("WIDTH", width);
				script = script.replace("{HEIGHT}", height).replace("HEIGHT", height);
				if(isVideo) {
					script = script.replace("{previewImageUrl}", DomUtils.getElement(selectedAttachments[0], 'input', 'previewImageUrl').value);
				}
				DialogUtils.getDialogOpener().setTimeout(script, 100);
			}
			DialogUtils.closeDialog();
		}
		function doSubmit(attachmentAction) { //提交
			document.forms[0].action += location.search;
			document.getElementsByName("attachmentSelector.action")[0].value = attachmentAction;
			FormUtils.submitForm();
		}
		function showProgress(attachmentUploader, percent, speed) { //显示进度
			if(!document.getElementById("uploadPercent")) {
				var html = '<span id="uploadPercent" style="display: inline-block; width:200px; color:#000000;">正在上传, 请稍候...</span>' +
						   (window.attachmentUploader.isCancelable() ? '<img id="cancelButton" title="取消" style="position: relative;" src="<%=request.getContextPath()%>/jeaf/attachment/icons/delete.gif" align="absmiddle"/>&nbsp;' : '');
				PageUtils.showToast(html);
				if(window.attachmentUploader.isCancelable()) {
					document.getElementById("cancelButton").onclick = function() {
						window.attachmentUploader.cancelUpload();
						PageUtils.removeToast();
					};
				}
			}
			if(percent!=null) { //显示进度
				document.getElementById("uploadPercent").innerHTML = '正在上传,已完成' + percent + "%" + (speed && speed>0 ? "," + StringUtils.formatBytes(speed) + "/S" : "");
			}
		}
		function createAttachmentUploader() { //创建附件上传器
			var uploadButton = document.getElementById('button_上传');
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
				PageUtils.removeToast();
			};
			window.attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
				PageUtils.removeToast();
				alert(errorDescription);
			};
		}
		window.onload = function() {
			var divAttachments = document.getElementById('divAttachments');
			divAttachments.style.height = (divAttachments.parentElement.offsetHeight-2) + "px";
			divAttachments.style.display = '';
			createAttachmentUploader();
			<ext:notEmpty property="attachmentSelector.extendJs">
				ScriptUtils.appendJsFile(document, "<%=request.getContextPath()%><ext:write property="attachmentSelector.extendJs"/>");
			</ext:notEmpty>
			//把选中的附件设为选中状态
			var selectedNames = document.getElementsByName('attachmentSelector.selectedNames')[0].value;
			selectedNames = selectedNames=="" ? null : selectedNames.split("*");
			for(var i = 0; i < (selectedNames!=null && divAttachments.childNodes ? divAttachments.childNodes.length : 0); i++) {
				if(divAttachments.childNodes[i].tagName!='DIV') {
					continue;
				}
				var name = divAttachments.childNodes[i].getElementsByTagName("input")[1].value;
				var j = 0;
				for(; j < selectedNames.length && name != selectedNames[j]; j++);
				if(j < selectedNames.length) {
					clickAttachment(divAttachments.childNodes[i], true);
				}
			}
			if(selectedAttachments.length==0) {
				return;
			}
			var offsetTop = selectedAttachments[0].offsetTop;
			var offsetBottom = selectedAttachments[selectedAttachments.length - 1].offsetTop + selectedAttachments[selectedAttachments.length - 1].offsetHeight;
			if(divAttachments.scrollTop > offsetTop || divAttachments.scrollTop + divAttachments.offsetHeight < offsetBottom) {
				window.setTimeout(function() {
					divAttachments.scrollTop = offsetTop - 5;
				}, 1000);
			}
		};
	</script>
</ext:form>
</ext:notEqual>