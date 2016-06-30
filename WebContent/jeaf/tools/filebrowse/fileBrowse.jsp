<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>文件浏览器</title>
	<style>
		* {
			font-family:宋体;
			font-size:12px;
		}
		A:link {
			color:#003366;
			text-decoration:none;
		}
		A:visited {
			color:#003366;
			text-decoration:none;
		}
		A:hover {
			color:#E00000;
			text-decoration:none;
		}
		.file:link {
			color:blue;
			text-decoration:none;
		}
		.file:visited {
			color:blue;
			text-decoration:none;
		}
		.file:hover {
			color:#E00000;
			text-decoration:none;
		}
		.progressBar {
			height:16px;
			border:#808080 1px solid;
			padding:1px;
			width:100%;
		}
		.progress {
			background-color:#00008B;
		}
		.button {
			padding: 3px 6px 3px 6px !important;
			padding: 3px 0px 0px 0px;
			width: auto;
			height: auto;
			border: 1px outset #aaaaaa;
		}
	</style>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/filetransfer/js/fileuploadclient.js"></script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/attachment/js/attachmentuploader.js"></script>
	<script>
    	function listFiles() {
			location.href = 'fileBrowse.shtml?path=' + StringUtils.utf8Encode(document.getElementsByName('path')[0].value);
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
		window.onload = function() {
			window.attachmentUploader = new AttachmentUploader(true);
			window.attachmentUploader.createUploadFrame(document.getElementById('uploadButton'), '<%=request.getContextPath()%>/jeaf/tools/attachmentEditor.shtml?path=<ext:write property="encodedPath"/>&attachmentSelector.field=file&attachmentSelector.type=file', '文件');
			window.attachmentUploader.onUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime, percent) { //事件:文件上传中
				showProgress(window.attachmentUploader, percent, speed); //显示进度
			};
			window.attachmentUploader.onUploaded = function(attachmentNames, attachments) { //事件:文件上传完成
				document.getElementsByName('lastUploadFiles')[0].value = attachmentNames;
				document.getElementsByName('path')[0].value = StringUtils.utf8Decode('<ext:write property="encodedPath"/>');
				FormUtils.doAction("processUploadedFiles"); //提交
			};
			window.attachmentUploader.onStopped = function() { //事件:文件停止上传
				PageUtils.removeToast();
			};
			window.attachmentUploader.onError =  function(errorDescription) { //事件:文件上传错误
				PageUtils.removeToast();
				alert(errorDescription);
			};
		};
	</script>
</head>
<body style="overflow:auto">
    <ext:form action="/fileBrowse">
    	<html:hidden property="lastUploadFiles"/>
    	<table border="0" cellpadding="3" cellspacing="0" style="height:100%; width:100%">
    		<tr>
    			<td height="30">
		    		当前目录：<html:text property="path" onkeypress="if(event.keyCode==13)return false;" onkeydown="if(event.keyCode==13)listFiles();" style="height:18px;width:500px;border: 1 solid gray;font-family:宋体;font-size:12px"/>
					<input type="button" onclick="listFiles()" value="打开" class="button">
				</td>
			</tr>
			<tr>
				<td height="30">
			    	上传文件：<input type="button" id="uploadButton" value="上传" class="button">
			    	<html:checkbox styleClass="checkbox" property="unzip" value="true"/>解压缩文件到当前目录&nbsp;
				</td>
			</tr>
			<tr>
				<td height="30">
			    	　　命令：<html:text property="command" style="height:18px;width:500px; border:1px solid gray; font-family:宋体; font-size:12px"/>
					<input type="button" onclick="FormUtils.doAction('runOnServer')" value="运行" class="button">
				</td>
			</tr>
			<tr>
				<td>
					<div style="border:1px solid gray; width:100%; height:100%; overflow:auto">
				    	<table border="0" cellpadding="3" cellspacing="0" style="font-family:宋体;font-size:12px; width:500px">
				    		<tr style="background-color:buttonface; border: 1 outset gray;">
				    			<td width="100px" nowrap>文件名</td>
				    			<td width="100px" nowrap>大小(字节)</td>
				    			<td nowrap>完整路径</td>
				    			<td nowrap width="180px">最后修改时间</td>
				    		</tr>
				    		<tr>
				    			<td nowrap>
				    				<a href="fileBrowse.shtml?path=<ext:write property="encodedPath"/>\\..">
				    					<img src="<%=request.getContextPath()%>/jeaf/attachment/icons/folder.png" align="absmiddle" width="22px" border="0"/>&nbsp;..
				    				</a>
				    			</td>
				    			<td nowrap></td>
				    			<td nowrap></td>
				    		</tr>
				    		<ext:iterate id="filder" property="folders">
				    			<tr>
					    			<td nowrap>
					    				<a href="fileBrowse.shtml?path=<ext:write name="filder" property="encodedPath"/>">
					    					<img src="<%=request.getContextPath()%>/jeaf/attachment/icons/folder.png" align="absmiddle" width="22px" border="0"/>&nbsp;<ext:write name="filder" property="name"/>
					    				</a>
					    			</td>
					    			<td nowrap></td>
					    			<td nowrap><ext:write name="filder" property="path"/></td>
					    		</tr>
				    		</ext:iterate>
				    		<ext:iterate id="file" property="files">
				    			<tr>
					    			<td nowrap>
					    				<a class="file" href="<ext:write name="file" property="urlAttachment"/>">
						    				<img src="<ext:write name="file" property="iconURL"/>" align="absmiddle" width="22px" border="0"/>&nbsp;<ext:write name="file" property="name"/>
						    			</a>
					    			</td>
					    			<td nowrap style="padding-left:20px;padding-right:20px;" title="<ext:write name="file" property="size"/>"><ext:write name="file" property="fileSize"/></td>
					    			<td nowrap><ext:write name="file" property="filePath"/></td>
					    			<td nowrap><ext:write name="file" property="lastModifiedTime" format="yyyy-MM-dd HH:mm:ss"/></td>
					    		</tr>
				    		</ext:iterate>
				    	</table>
			    	</div>
			    </td>
		    </tr>
		</table>
    </ext:form>
</body>
</html>