//HTML文件上传类
FileUploadClient.HtmlUploader = function(fileUploadClient, uploadFrame) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
	this.uploadFrame = uploadFrame;
	uploadFrame.onFileSelected = function(htmlFileField, filePath) { //事件:完成文件选择
		fileUploadClient._onFileSelected(filePath, false);
	};
	uploadFrame.onUploaded = function(lastUploadedAttachmentName) { //事件:上传完成
		fileUploadClient.onFileUploaded(lastUploadedAttachmentName);
	};
	uploadFrame.onUploadError = function(errorDescription) { //事件:上传错误
		if("会话无效"==errorDescription) {
			fileUploadClient.onFileUploadStopped();
			LoginUtils.openLoginDialog(function() { //重新登录
				alert("请重新选择需要上传的文件。");
				uploadFrame.src = uploadFrame.src + (uploadFrame.src.lastIndexOf("?")==-1 ? "?" : "&") + "seq=" + Math.random();
			});
			return;
		}
		fileUploadClient.onFileUploadError(errorDescription);
	};
};
FileUploadClient.HtmlUploader.prototype = new FileUploadClient.Uploader();
//上传
FileUploadClient.HtmlUploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	this.fileNames = filePath;
	var document = this.uploadFrame.contentWindow.document;
	document.getElementsByName("attachmentSelector.action")[0].value = "upload";
	document.forms[0].action += this.uploadFrame.contentWindow.location.search;
	document.forms[0].submit(); //提交
	this.fileUploadClient.onFileUploading(filePath, null, -1, null, -1, -1, -1, -1, -1, -1, -1);
};