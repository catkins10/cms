FileUploadClient = function(uploadFrame) {
	if(!(this.uploader = new FileUploadClient.ExtensionUploader(this)).isEnabled() && 
	   !(this.uploader = new FileUploadClient.PluginUploader(this)).isEnabled() && 
	   !(this.uploader = new FileUploadClient.Html5Uploader(this, uploadFrame)).isEnabled()) { 
	   this.uploader = new FileUploadClient.HtmlUploader(this, uploadFrame); 
	}
};
FileUploadClient.prototype.onFileUploadStart = function(filePath) {
	return true;
};
FileUploadClient.prototype.onFileUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime) {
	
};
FileUploadClient.prototype.onFileUploadStopped = function() {
};
FileUploadClient.prototype.onFileUploaded = function(remoteFileNames) {
};
FileUploadClient.prototype.onFileUploadError = function(errorDescription) {
};
FileUploadClient.prototype.selectFileToUpload = function(dialogTitle, fileFilter, multiSelect, uploadPassportUrl, overwrite) {
	this.uploadPassportUrl = uploadPassportUrl;
	this.overwrite = overwrite;
	return this.uploader.selectFile(dialogTitle, fileFilter, multiSelect);
};
FileUploadClient.prototype.selectDirectoryToUpload = function(dialogTitle, uploadPassportUrl) {
	this.uploadPassportUrl = uploadPassportUrl;
	this.overwrite = overwrite;
	return this.uploader.selectDirectory(dialogTitle);
};
FileUploadClient.prototype._onFileSelected = function(filePath, isDirectory) {
	if(filePath=='') {
		return;
	}
	if(!isDirectory) { 
		var names = filePath.split("\n");
		var i = 0;
		for(; i < names.length && this.onFileUploadStart(names[i]); i++);
		if(i < names.length) {
			return;
		}
	}
	this.upload(filePath, this.uploadPassportUrl, this.overwrite, isDirectory); 
};
FileUploadClient.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	this.uploader.upload(filePath, uploadPassportUrl, overwrite, isDirectory);
};
FileUploadClient.prototype.isCancelable = function() {
	return this.uploader.isCancelable();
};
FileUploadClient.prototype.cancelUpload = function() {
	this.uploader.cancelUpload();
};
FileUploadClient.prototype.stopUpload = function() {
	this.uploader.stopUpload();
};
FileUploadClient.prototype.processUploadError = function(error) {
	this.uploader.processUploadError(error);
};

FileUploadClient.Uploader = function(fileUploadClient) {
	this.fileUploadClient = fileUploadClient;
};
FileUploadClient.Uploader.prototype.isEnabled = function() {
	return false;
};
FileUploadClient.Uploader.prototype.selectFile = function(dialogTitle, fileFilter, multiSelect) {
	return false;
};
FileUploadClient.Uploader.prototype.selectDirectory = function(dialogTitle) {
	return false;
};
FileUploadClient.Uploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	
};
FileUploadClient.Uploader.prototype.isCancelable = function() {
	return false; 
};
FileUploadClient.Uploader.prototype.cancelUpload = function() {
	return false;
};
FileUploadClient.Uploader.prototype.stopUpload = function() {
	return false;
};
FileUploadClient.Uploader.prototype.processUploadError = function(error) {
	
};

FileUploadClient.HtmlUploader = function(fileUploadClient, uploadFrame) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
	this.uploadFrame = uploadFrame;
	uploadFrame.onFileSelected = function(htmlFileField, filePath) { 
		fileUploadClient._onFileSelected(filePath, false);
	};
	uploadFrame.onUploaded = function(lastUploadedAttachmentName) { 
		fileUploadClient.onFileUploaded(lastUploadedAttachmentName);
	};
	uploadFrame.onUploadError = function(errorDescription) { 
		if("\u4F1A\u8BDD\u65E0\u6548"==errorDescription) {
			fileUploadClient.onFileUploadStopped();
			LoginUtils.openLoginDialog(function() { 
				alert("\u8BF7\u91CD\u65B0\u9009\u62E9\u9700\u8981\u4E0A\u4F20\u7684\u6587\u4EF6\u3002");
				uploadFrame.src = uploadFrame.src + (uploadFrame.src.lastIndexOf("?")==-1 ? "?" : "&") + "seq=" + Math.random();
			});
			return;
		}
		fileUploadClient.onFileUploadError(errorDescription);
	};
};
FileUploadClient.HtmlUploader.prototype = new FileUploadClient.Uploader();
FileUploadClient.HtmlUploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	this.fileNames = filePath;
	var document = this.uploadFrame.contentWindow.document;
	document.getElementsByName("attachmentSelector.action")[0].value = "upload";
	document.forms[0].action += this.uploadFrame.contentWindow.location.search;
	document.forms[0].submit(); 
	this.fileUploadClient.onFileUploading(filePath, null, -1, null, -1, -1, -1, -1, -1, -1, -1);
};

FileUploadClient.ExtensionUploader = function(fileUploadClient) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
};
FileUploadClient.ExtensionUploader.prototype = new FileUploadClient.Uploader();
window.setTimeout(function() {
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/echo?value=' + StringUtils.utf8Encode('FileUploadClient.ExtensionUploader.borwserExtensionIsRunning=true;') + '&seq=' + Math.random(), "scriptBorwserExtension");
}, 10);
FileUploadClient.ExtensionUploader.prototype.isEnabled = function() {
	return FileUploadClient.ExtensionUploader.borwserExtensionIsRunning;
};
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
FileUploadClient.ExtensionUploader.prototype.selectDirectory = function(dialogTitle) {
	return true;
};
FileUploadClient.ExtensionUploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	var fileUploader = this;
	
	window.onUploadPassportCreated = function(passport) {
		fileUploader._doUpload(passport, filePath, uploadPassportUrl, overwrite, isDirectory);
	};
	ScriptUtils.appendJsFile(document, uploadPassportUrl + "&writeAsJs=true&clientVersion=2.0&seq=" + Math.random(), "scriptBorwserExtension");
};
FileUploadClient.ExtensionUploader.prototype.isCancelable = function() {
	return true; 
};
FileUploadClient.ExtensionUploader.prototype.cancelUpload = function() {
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileUpload?action=cancel&taskId=' + this.taskId + '&seq=' + Math.random(), "scriptBorwserExtension");
	return true;
};
FileUploadClient.ExtensionUploader.prototype.stopUpload = function() {
	ScriptUtils.appendJsFile(document, 'http://127.0.0.1:50718/fileUpload?action=stop&taskId=' + this.taskId + '&seq=' + Math.random(), "scriptBorwserExtension");
	return true;
};
FileUploadClient.ExtensionUploader.prototype._doUpload = function(passport, filePath, uploadPassportUrl, overwrite, isDirectory) {
	var fileUploader = this;
	if(passport.indexOf("PASSPORT")==0) { 
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
        LoginUtils.openLoginDialog(function() { 
			fileUploader.upload(filePath, uploadPassportUrl, overwrite, isDirectory);
		});
		return;
	}
    var error;
    if(passport == "NO_PRIVILEGE") {
    	error = "\u6CA1\u6709\u4E0A\u4F20\u6743\u9650";
    }
    else if(passport == "LOCK_FAILED") {
    	error = "\u4E0D\u80FD\u9501\u5B9A\u5F53\u524D\u8BB0\u5F55";
    }
    else if(passport == "BUSY") {
        error = "\u670D\u52A1\u5668\u5FD9";
    }
	else {
        error = "\u8FDC\u7A0B\u670D\u52A1\u5668\u9519\u8BEF";
    }
    this.fileUploadClient.onFileUploadError(error);
}

FileUploadClient.Html5Uploader = function(fileUploadClient, uploadFrame) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
	var html5Uploader = this;
	this.uploadFrame = uploadFrame;
	uploadFrame.onFileSelected = function(htmlFileField, filePath) { 
		html5Uploader.htmlFileField = htmlFileField;
		html5Uploader.file = htmlFileField.files[0];
		html5Uploader.filePath = filePath;
		fileUploadClient._onFileSelected(filePath, false);
	};
};
FileUploadClient.Html5Uploader.prototype = new FileUploadClient.HtmlUploader();
FileUploadClient.Html5Uploader.prototype.isEnabled = function() {
	return window.FileReader ? true : false;
};
FileUploadClient.Html5Uploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	if(!this._getPassport(uploadPassportUrl)) { 
		return false;
	}
	if(!this._sendFileStartCommand()) { 
		return false;
	}
	var html5Uploader = this;
	this.stopped = false;
	
	this.fileReader = new FileReader();
	this.fileReader.onload = function() { 
		html5Uploader._sendFileBlock();
	};
	this.fileReader.onprogress = function(event) { 
		
	};
	this.fileReader.onerror = function() { 
		html5Uploader.fileUploadClient.onFileUploadError("\u8BFB\u53D6\u6587\u4EF6\u65F6\u51FA\u9519");
		html5Uploader.stopped = true;
	};
	this.fileReader.onabort = function() { 
	
	};
	this.fileReader.onloadstart = function() { 
	
	};
	this.fileReader.onloadend = function() { 
	
	};
	this.fileSeek = 0;
	this.fileStep = 65536;
	this.startTime = new Date().valueOf();
	this.speed = 0;
	this.speedTotal = 0;
	this.speedStart = this.startTime;
	this._readFile();
};
FileUploadClient.Html5Uploader.prototype.isCancelable = function() {
	return true; 
};
FileUploadClient.Html5Uploader.prototype.cancelUpload = function() {
	this.stopped = true;
	this.htmlFileField.value = "";
	return this._sendCacelCommand();
};
FileUploadClient.Html5Uploader.prototype._readFile = function() {
	if(this.stopped) {
		return;
	}
	var blob; 
   	if(this.file.webkitSlice) {
		blob = this.file.webkitSlice(this.fileSeek, this.fileSeek + this.fileStep);  
	}
	else if(this.file.mozSlice) {
		blob = this.file.mozSlice(this.fileSeek, this.fileSeek + this.fileStep);  
	}
	else {
		blob = this.file.slice(this.fileSeek, this.fileSeek + this.fileStep);  
	}
    this.fileReader.readAsArrayBuffer(blob);
};
FileUploadClient.Html5Uploader.prototype._sendFileBlock = function() {
	var buffer = new Uint8Array(this.fileReader.result);
   	var validate = 0;
   	for(var i=0; i < buffer.length; i+=10) {
		validate += (buffer[i] > 127 ? buffer[i] - 256 : buffer[i]);
	}
	var blockSize = buffer.length;
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&seek=" + this.fileSeek +
			  "&validate=" + validate +
			  "&len=" + blockSize +
			  "&seq=" + Math.random();
	var uploader = this;
	this._sendData("POST", url, this.fileReader.result, true, 
		function() {
			uploader._onFileBlockUploaded(blockSize);
		},
		function() {
			uploader.fileUploadClient.onFileUploadError("\u8FDC\u7A0B\u670D\u52A1\u5668\u9519\u8BEF");
		});
};
FileUploadClient.Html5Uploader.prototype._onFileBlockUploaded = function(blockSize) { 
	if(this.stopped) {
		return;
	}
	this.fileSeek += blockSize;
	this.speedTotal += blockSize;
	var now = new Date().valueOf();
	if(now - this.speedStart >= (this.speed==0 ? 1000 : 3000)) {
		this.speed = this.speedTotal * 1000 / (now - this.speedStart);
		this.speedStart = now;
		this.speedTotal = 0;
	}
	var useTime = (now - this.startTime) / 1000;
	this.fileUploadClient.onFileUploading(this.filePath, 1, this.file.size, this.filePath, 1, this.file.size, this.fileSeek, 1, this.fileSeek, this.speed, useTime);
	if(this.fileSeek != this.file.size) { 
	   	this._readFile(); 
		return;
	}
	
	this.htmlFileField.value = "";
	this._sendFileCompleteCommand();
	this._sendCompleteUploadCommand();
	this.fileUploadClient.onFileUploaded(this.remoteFileName);
};
FileUploadClient.Html5Uploader.prototype._getPassport = function(uploadPassportUrl) {
	
	var returnValue = this._sendData("GET", uploadPassportUrl + "&clientVersion=2.0&seq=" + Math.random(), "", false);
	
    if(returnValue.indexOf("PASSPORT")==0) { 
    	var values = returnValue.split(',');
        this.passport = values[0].split(':')[1]; 
		var maxUploadSize = Number(values[1].split(':')[1]);
		if(maxUploadSize>0 && this.file.size > maxUploadSize) {
			 this.fileUploadClient.onFileUploadError("\u4E0A\u4F20\u7684\u6587\u4EF6\u4E0D\u5141\u8BB8\u8D85\u8FC7" + StringUtils.formatBytes(maxUploadSize));
   			 return false;
		}
		return true;
   	}
   	if(returnValue == "SESSION_EXCEPTION") {
   		var fileUploader = this;
        LoginUtils.openLoginDialog(function() { 
			fileUploader.upload();
		});
		return false;
	}
   	var error;
    if(returnValue == "NO_PRIVILEGE") {
    	error = "\u6CA1\u6709\u4E0A\u4F20\u6743\u9650";
    }
    else if(returnValue == "LOCK_FAILED") {
    	error = "\u4E0D\u80FD\u9501\u5B9A\u5F53\u524D\u8BB0\u5F55";
    }
    else if(returnValue == "BUSY") {
        error = "\u670D\u52A1\u5668\u5FD9";
    }
	else {
        error = "\u8FDC\u7A0B\u670D\u52A1\u5668\u9519\u8BEF";
    }
    this.fileUploadClient.onFileUploadError(error);
    return false;
};
FileUploadClient.Html5Uploader.prototype._sendFileStartCommand = function() {
	var index = this.filePath.lastIndexOf('\\');
	if(index==-1) {
		index = this.filePath.lastIndexOf('/');
	}
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=fileStart" +
			  "&characterEncoding=utf-8" +
			  "&fileName="  + StringUtils.utf8Encode(this.filePath.substring(index + 1)) +
			  "&fileLength=" + this.file.size +
			  "&overwrite=false" +
			  "&seq=" + Math.random();
	var returnValue = this._sendData("GET", url, "", false);
	if(returnValue.indexOf("OK")==0) { 
		this.remoteFileName = returnValue.substring("OK:".length); 
		return true;
	}
	var error;
	if(returnValue == "UNALLOWED_FILE") {
		error = "\u6587\u4EF6\u88AB\u7981\u6B62\u4E0A\u4F20";
	}
	else if(returnValue.indexOf("FILE_TOO_LARGE")==0) {
		error = "\u4E0A\u4F20\u7684\u6587\u4EF6\u4E0D\u5141\u8BB8\u8D85\u8FC7" + StringUtils.formatBytes(Number(returnValue.substring("FILE_TOO_LARGE:".length)));
	}
	else {
		error = "\u8FDC\u7A0B\u670D\u52A1\u5668\u9519\u8BEF";
	}
	this.fileUploadClient.onFileUploadError(error);
	return false;
};
FileUploadClient.Html5Uploader.prototype._sendFileCompleteCommand = function() {
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=fileComplete" +
			  "&characterEncoding=utf-8" +
			  "&seq=" + Math.random();
	var returnValue = this._sendData("GET", url, "", false);
	if(returnValue=="OK") { 
		return true;
	}
	this.fileUploadClient.onFileUploadError("\u8FDC\u7A0B\u670D\u52A1\u5668\u9519\u8BEF");
	return false;
};
FileUploadClient.Html5Uploader.prototype._sendCompleteUploadCommand = function() {
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=complete" +
			  "&characterEncoding=utf-8" +
			  "&seq=" + Math.random();
	var returnValue = this._sendData("GET", url, "", false);
	if(returnValue=="OK") { 
		return true;
	}
	this.fileUploadClient.onFileUploadError("\u8FDC\u7A0B\u670D\u52A1\u5668\u9519\u8BEF");
	return false;
};
FileUploadClient.Html5Uploader.prototype._sendCacelCommand = function() {
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=cancel" +
			  "&characterEncoding=utf-8" +
			  "&seq=" + Math.random();
	return this._sendData("GET", url, "", false)=="OK";
};
FileUploadClient.Html5Uploader.prototype._sendData = function(method, url, data, async, onDataArriveCallback, onErrorCallback) {
	if(!this.ajax) {
		this.ajax = new Ajax();
	}
	this.ajax.openRequest(method, url, data, async, onDataArriveCallback, onErrorCallback);
	return this.ajax.getResponseText();
};

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
FileUploadClient.PluginUploader.prototype.createPlugin = function() {
	this.uploadServerUrl = RequestUtils.getFullURL(RequestUtils.getContextPath() + "/fileUpload");
	
	this.activeX = document.createElement('object'); 
	this.activeX.setAttribute("classid", "clsid:F1FA1744-EB4F-486A-8D10-7586B50E7BB1");
	this.activeX.style.width = "0px";
	this.activeX.style.height = "0px";
	this.activeX.id = ("" + Math.random()).substring(2);
	this.activeX = document.body.insertBefore(this.activeX, document.body.childNodes[0]);
	this.activeX.fileUploader = this;
	var fileUploader = this;
	
	var script = document.createElement('script');
	script.language = "javascript";
	script.setAttribute("for", this.activeX.id);
	script.setAttribute("event", "uploadComplete(remoteFileNames)");
	script.text = 'this.fileUploader.finished = true;' +
				  'this.fileUploader.fileUploadClient.onFileUploaded(remoteFileNames);';
	document.body.insertBefore(script, this.activeX.nextSibling);
	EventUtils.addEvent(this.activeX, 'uploadComplete', function(remoteFileNames) { 
		fileUploader.finished = true;
		fileUploader.fileUploadClient.onFileUploaded(remoteFileNames);
	});
	
	script = document.createElement('script');
	script.language = "javascript";
	script.setAttribute("for", this.activeX.id);
	script.setAttribute("event", "uploadError(errorDescription, errorNumber)");
	script.text = 'this.fileUploader._processFileUploadError(errorDescription, errorNumber);';
	document.body.insertBefore(script, this.activeX.nextSibling);
	EventUtils.addEvent(this.activeX, "uploadError", function(errorDescription, errorNumber) { 
		fileUploader._processFileUploadError(errorDescription, errorNumber);
	});
	
	script = document.createElement('script');
	script.language = "javascript";
	script.setAttribute("for", this.activeX.id);
	script.setAttribute("event", "uploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime)");
	script.text = 'if(!this.fileUploader.finished) { this.fileUploader.fileUploadClient.onFileUploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime);}';
	document.body.insertBefore(script, this.activeX.nextSibling);
	EventUtils.addEvent(this.activeX, "uploading", function(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime) { 
		if(!fileUploader.finished) {
			fileUploader.fileUploadClient.onFileUploading(uploadFilePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime);
		}
	});
	
	EventUtils.addEvent(window, 'unload', function() {
		fileUploader.cancelUpload();
	});
};
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
FileUploadClient.PluginUploader.prototype.selectFile = function(dialogTitle, fileFilter, multiSelect) {
	this.fileUploadClient._onFileSelected(this.activeX.selectFile(dialogTitle, fileFilter, multiSelect), false);
	return true;
};
FileUploadClient.PluginUploader.prototype.selectDirectoryToUpload = function(dialogTitle) {
	this.fileUploadClient._onFileSelected(this.activeX.selectDirectory(dialogTitle), true);
	return true;
};
FileUploadClient.PluginUploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	this.finished = false;
	var fileUploader = this;
	this.fileNames = filePath;
	this.task = function() {
		fileUploader.activeX.uploadFile(RequestUtils.getFullURL(uploadPassportUrl), fileUploader.uploadServerUrl, filePath, overwrite, true, isDirectory ? true : false);
	};
	this.task.call(null);
};
FileUploadClient.PluginUploader.prototype.isCancelable = function() {
	return true; 
};
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
FileUploadClient.PluginUploader.prototype._processFileUploadError = function(errorDescription, errorNumber) {
	var fileUploader = this;
	if("\u4F1A\u8BDD\u65E0\u6548"==errorDescription) {
		LoginUtils.openLoginDialog(function() { 
			fileUploader.task.call(null);
		});
		return;
	}
	this.fileUploadClient.onFileUploadError(errorDescription);
};

