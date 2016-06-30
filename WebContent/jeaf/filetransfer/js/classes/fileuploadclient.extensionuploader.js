//浏览器扩展文件上传类
FileUploadClient.ExtensionUploader = function(fileUploadClient) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
};
FileUploadClient.ExtensionUploader.prototype = new FileUploadClient.Uploader();
//检查浏览器扩展是否在运行
window.setTimeout(function() {
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/echo?value=' + StringUtils.utf8Encode('FileUploadClient.ExtensionUploader.borwserExtensionIsRunning=true;') + '&seq=' + Math.random(), "scriptBorwserExtension");
}, 10);
//是否有效
FileUploadClient.ExtensionUploader.prototype.isEnabled = function() {
	return FileUploadClient.ExtensionUploader.borwserExtensionIsRunning;
};
//选择文件,fileFilter:文件过滤,如:视频文件|*.avi;*.rmvb|图片文件|*.bmp;*.jpg|所有文件|*.*|
FileUploadClient.ExtensionUploader.prototype.selectFile = function(dialogTitle, fileFilter, multiSelect) {
	if(this.waitFileDialog) {
		return;
	}
	var fileUploader = this;
	window.onFileDialogOpened = function(taskId) {
		fileUploader.waitFileDialog = false;
		window.extensionTimer = window.setInterval(function() {
			ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileSystem?action=getSelectedFiles&taskId=' + taskId + '&seq=' + Math.random(), "scriptBorwserExtension");
		}, 300);
	};
	window.onFileSelected = function(filePath) {
		window.clearInterval(window.extensionTimer);
		fileUploader.fileUploadClient._onFileSelected(filePath, false);
	};
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileSystem?action=selectFiles&fileFilter=' + StringUtils.utf8Encode(fileFilter) + '&multiSelect=' + multiSelect + '&seq=' + Math.random(), "scriptBorwserExtension");
	this.waitFileDialog = true;
	return true;
};
//选择目录
FileUploadClient.ExtensionUploader.prototype.selectDirectory = function(dialogTitle) {
	return true;
};
//上传
FileUploadClient.ExtensionUploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	var fileUploader = this;
	//获取上传许可
	window.onUploadPassportCreated = function(passport) {
		fileUploader._doUpload(passport, filePath, uploadPassportUrl, overwrite, isDirectory);
	};
	ScriptUtils.appendJsFile(document, uploadPassportUrl + "&writeAsJs=true&clientVersion=2.0&seq=" + Math.random(), "scriptBorwserExtension");
};

//是否可以取消上传
FileUploadClient.ExtensionUploader.prototype.isCancelable = function() {
	return true; 
};
//取消上传
FileUploadClient.ExtensionUploader.prototype.cancelUpload = function() {
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileUpload?action=cancel&taskId=' + this.taskId + '&seq=' + Math.random(), "scriptBorwserExtension");
	return true;
};
//停止上传
FileUploadClient.ExtensionUploader.prototype.stopUpload = function() {
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileUpload?action=stop&taskId=' + this.taskId + '&seq=' + Math.random(), "scriptBorwserExtension");
	return true;
};
//执行上传
FileUploadClient.ExtensionUploader.prototype._doUpload = function(passport, filePath, uploadPassportUrl, overwrite, isDirectory) {
	var fileUploader = this;
	if(passport.indexOf("PASSPORT")==0) { //正常返回
		window.onFileUploadStart = function(taskId) {
			fileUploader.taskId = taskId;
			window.extensionTimer = window.setInterval(function() {
				ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileUpload?action=status&taskId=' + taskId + '&seq=' + Math.random(), "scriptBorwserExtension");
			}, 500);
		};
		window.onFileUploading = function(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime) {
			fileUploader.uploading = true;
			fileUploader.fileUploadClient.onFileUploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime);
		};
		window.onFileUploadError = function(error) {
			fileUploader.uploading = false;
			window.clearInterval(window.extensionTimer);
			fileUploader.fileUploadClient.onFileUploadError(error);
		};
		window.onFileUploadStopped = function() {
			fileUploader.uploading = false;
			window.clearInterval(window.extensionTimer);
		};
		window.onFileUploadCanceled = function() {
			fileUploader.uploading = false;
			window.clearInterval(window.extensionTimer);
		};
		window.onFileUploadCompleted = function(remoteFileNames) {
			fileUploader.uploading = false;
			window.clearInterval(window.extensionTimer);
			fileUploader.fileUploadClient.onFileUploaded(remoteFileNames);
		};
		window.onunload = function() {
			if(fileUploader.uploading) {
				fileUploader.cancelUpload();
			}
		};
		var url = 'http://127.0.0.1:50718/fileUpload?action=upload' +
				  '&passport=' + StringUtils.utf8Encode(passport) +
				  '&uploadPassportURL=' + StringUtils.utf8Encode(StringUtils.removeQueryParameter(uploadPassportUrl, "ticket,seq")) +
				  '&uploadServerURL=' + StringUtils.utf8Encode(RequestUtils.getFullURL(RequestUtils.getContextPath() + "/fileUpload")) +
				  '&filePath=' + StringUtils.utf8Encode(filePath) +
				  (isDirectory ? '&isDirectory=true' : '') +
				  (overwrite ? '&overwrite=true' : '') +
				  '&resume=true' +
				  '&seq=' + Math.random();
		ScriptUtils.appendJsFile(document, url, "scriptBorwserExtension");
		return;
   	}
	if(passport == "SESSION_EXCEPTION") {
        var fileUploader = this;
        LoginUtils.openLoginDialog(function() { //登录成功, 重新上传文件
			fileUploader.upload(filePath, uploadPassportUrl, overwrite, isDirectory);
		});
		return;
	}
    var error;
    if(passport == "NO_PRIVILEGE") {
    	error = "没有上传权限";
    }
    else if(passport == "LOCK_FAILED") {
    	error = "不能锁定当前记录";
    }
    else if(passport == "BUSY") {
        error = "服务器忙";
    }
	else {
        error = "远程服务器错误";
    }
    this.fileUploadClient.onFileUploadError(error);
}