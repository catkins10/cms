//插件方式文件上传类
FileUploadClient.PluginUploader = function(fileUploadClient) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
	try {
		this.createPlugin();
	}
	catch(e) {
		
	}
};
FileUploadClient.PluginUploader.prototype = new FileUploadClient.Uploader();
//插件插件
FileUploadClient.PluginUploader.prototype.createPlugin = function() {
	this.uploadServerUrl = RequestUtils.getFullURL(RequestUtils.getContextPath() + "/fileUpload");
	//加载控件
	this.activeX = document.createElement('object'); 
	this.activeX.setAttribute("classid", "clsid:F1FA1744-EB4F-486A-8D10-7586B50E7BB1");
	this.activeX.style.width = "0px";
	this.activeX.style.height = "0px";
	this.activeX.id = ("" + Math.random()).substring(2);
	this.activeX = document.body.insertBefore(this.activeX, document.body.childNodes[0]);
	this.activeX.fileUploader = this;
	var fileUploader = this;
	//绑定控件事件:上传完毕
	var script = document.createElement('script');
	script.language = "javascript";
	script.setAttribute("for", this.activeX.id);
	script.setAttribute("event", "uploadComplete(remoteFileNames)");
	script.text = 'this.fileUploader.finished = true;' +
				  'this.fileUploader.fileUploadClient.onFileUploaded(remoteFileNames);';
	document.body.insertBefore(script, this.activeX.nextSibling);
	EventUtils.addEvent(this.activeX, 'uploadComplete', function(remoteFileNames) { //上传完成
		fileUploader.finished = true;
		fileUploader.fileUploadClient.onFileUploaded(remoteFileNames);
	});
	//绑定控件事件:上传错误
	script = document.createElement('script');
	script.language = "javascript";
	script.setAttribute("for", this.activeX.id);
	script.setAttribute("event", "uploadError(errorDescription, errorNumber)");
	script.text = 'this.fileUploader._processFileUploadError(errorDescription, errorNumber);';
	document.body.insertBefore(script, this.activeX.nextSibling);
	EventUtils.addEvent(this.activeX, "uploadError", function(errorDescription, errorNumber) { //上传错误
		fileUploader._processFileUploadError(errorDescription, errorNumber);
	});
	//绑定控件事件:正在上传
	script = document.createElement('script');
	script.language = "javascript";
	script.setAttribute("for", this.activeX.id);
	script.setAttribute("event", "uploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime)");
	script.text = 'if(!this.fileUploader.finished) { this.fileUploader.fileUploadClient.onFileUploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime);}';
	document.body.insertBefore(script, this.activeX.nextSibling);
	EventUtils.addEvent(this.activeX, "uploading", function(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime) { //上传中
		if(!fileUploader.finished) {
			fileUploader.fileUploadClient.onFileUploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime);
		}
	});
	//绑定窗口重载事件,当窗口重载后,停止上传
	EventUtils.addEvent(window, 'unload', function() {
		fileUploader.cancelUpload();
	});
};
//是否有效
FileUploadClient.PluginUploader.prototype.isEnabled = function() {
 	try {
		if(this.activeX.version) {
	 		return true;
	 	}
 	}
 	catch(e) {
 		
 	}
 	return false;
};
//选择文件, fileFilter:文件过滤,如:视频文件|*.avi;*.rmvb|图片文件|*.bmp;*.jpg|所有文件|*.*|
FileUploadClient.PluginUploader.prototype.selectFile = function(dialogTitle, fileFilter, multiSelect) {
	this.fileUploadClient._onFileSelected(this.activeX.selectFile(dialogTitle, fileFilter, multiSelect), false);
	return true;
};
//选择目录
FileUploadClient.PluginUploader.prototype.selectDirectoryToUpload = function(dialogTitle) {
	this.fileUploadClient._onFileSelected(this.activeX.selectDirectory(dialogTitle), true);
	return true;
};
//上传
FileUploadClient.PluginUploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	this.finished = false;
	var fileUploader = this;
	this.fileNames = filePath;
	this.task = function() {
		fileUploader.activeX.uploadFile(RequestUtils.getFullURL(uploadPassportUrl), fileUploader.uploadServerUrl, filePath, overwrite, true, isDirectory ? true : false);
	};
	this.task.call(null);
};
//是否可以取消上传
FileUploadClient.PluginUploader.prototype.isCancelable = function() {
	return true; 
};
//取消上传
FileUploadClient.PluginUploader.prototype.cancelUpload = function() {
	if(this.finished) {
		return true;
	}
	try {
		this.activeX.cancelUpload();
		this.finished = true;
	}
	catch(e) {
		
	}
	return true;
};
//停止上传
FileUploadClient.PluginUploader.prototype.stopUpload = function() {
	if(this.finished) {
		return true;
	}
	try {
		this.activeX.stopUpload();
		this.finished = true;
	}
	catch(e) {
		
	}
	return true;
};
//事件处理:上传错误
FileUploadClient.PluginUploader.prototype._processFileUploadError = function(errorDescription, errorNumber) {
	var fileUploader = this;
	if("会话无效"==errorDescription) {
		LoginUtils.openLoginDialog(function() { //登录成功, 重新上传文件
			fileUploader.task.call(null);
		});
		return;
	}
	this.fileUploadClient.onFileUploadError(errorDescription);
};