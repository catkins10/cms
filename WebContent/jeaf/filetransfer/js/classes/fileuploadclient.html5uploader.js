//HTML5文件上传类
FileUploadClient.Html5Uploader = function(fileUploadClient, uploadFrame) {
	if(!fileUploadClient) {
		return;
	}
	FileUploadClient.Uploader.call(this, fileUploadClient);
	var html5Uploader = this;
	this.uploadFrame = uploadFrame;
	uploadFrame.onFileSelected = function(htmlFileField, filePath) { //事件:完成文件选择
		html5Uploader.htmlFileField = htmlFileField;
		html5Uploader.file = htmlFileField.files[0];
		html5Uploader.filePath = filePath;
		fileUploadClient._onFileSelected(filePath, false);
	};
};
FileUploadClient.Html5Uploader.prototype = new FileUploadClient.HtmlUploader();
//是否有效
FileUploadClient.Html5Uploader.prototype.isEnabled = function() {
	return window.FileReader ? true : false;
};
//上传
FileUploadClient.Html5Uploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	if(!this._getPassport(uploadPassportUrl)) { //获取许可
		return false;
	}
	if(!this._sendFileStartCommand()) { //发送文件上传命令
		return false;
	}
	var html5Uploader = this;
	this.stopped = false;
	//读取文件
	this.fileReader = new FileReader();
	this.fileReader.onload = function() { //事件:文件读取成功完成时触发 
		html5Uploader._sendFileBlock();
	};
	this.fileReader.onprogress = function(event) { //事件:读取中
		
	};
	this.fileReader.onerror = function() { //事件:出错时触发
		html5Uploader.fileUploadClient.onFileUploadError("读取文件时出错");
		html5Uploader.stopped = true;
	};
	this.fileReader.onabort = function() { //事件:中断时触发
	
	};
	this.fileReader.onloadstart = function() { //事件:读取开始时触发
	
	};
	this.fileReader.onloadend = function() { //事件:读取完成触发，无论成功或失败
	
	};
	this.fileSeek = 0;
	this.fileStep = 65536;
	this.startTime = new Date().valueOf();
	this.speed = 0;
	this.speedTotal = 0;
	this.speedStart = this.startTime;
	this._readFile();
};
//是否可以取消上传
FileUploadClient.Html5Uploader.prototype.isCancelable = function() {
	return true; 
};
//取消上传
FileUploadClient.Html5Uploader.prototype.cancelUpload = function() {
	this.stopped = true;
	this.htmlFileField.value = "";
	return this._sendCacelCommand();
};
//读取文件
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
//发送文件
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
			uploader.fileUploadClient.onFileUploadError("远程服务器错误");
		});
};
FileUploadClient.Html5Uploader.prototype._onFileBlockUploaded = function(blockSize) { //文件块上传完成
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
	   	this._readFile(); //继续读取文件
		return;
	}
	//上传完成
	this.htmlFileField.value = "";
	this._sendFileCompleteCommand();
	this._sendCompleteUploadCommand();
	this.fileUploadClient.onFileUploaded(this.remoteFileName);
};
FileUploadClient.Html5Uploader.prototype._getPassport = function(uploadPassportUrl) {
	//获取上传许可
	var returnValue = this._sendData("GET", uploadPassportUrl + "&clientVersion=2.0&seq=" + Math.random(), "", false);
	//检查返回值
    if(returnValue.indexOf("PASSPORT")==0) { //正常返回
    	var values = returnValue.split(',');
        this.passport = values[0].split(':')[1]; //许可证ID
		var maxUploadSize = Number(values[1].split(':')[1]);
		if(maxUploadSize>0 && this.file.size > maxUploadSize) {
			 this.fileUploadClient.onFileUploadError("上传的文件不允许超过" + StringUtils.formatBytes(maxUploadSize));
   			 return false;
		}
		return true;
   	}
   	if(returnValue == "SESSION_EXCEPTION") {
   		var fileUploader = this;
        LoginUtils.openLoginDialog(function() { //登录成功, 重新上传文件
			fileUploader.upload();
		});
		return false;
	}
   	var error;
    if(returnValue == "NO_PRIVILEGE") {
    	error = "没有上传权限";
    }
    else if(returnValue == "LOCK_FAILED") {
    	error = "不能锁定当前记录";
    }
    else if(returnValue == "BUSY") {
        error = "服务器忙";
    }
	else {
        error = "远程服务器错误";
    }
    this.fileUploadClient.onFileUploadError(error);
    return false;
};
//服务器命令:开始上传一个文件
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
	if(returnValue.indexOf("OK")==0) { //成功
		this.remoteFileName = returnValue.substring("OK:".length); //服务器上的文件名
		return true;
	}
	var error;
	if(returnValue == "UNALLOWED_FILE") {
		error = "文件被禁止上传";
	}
	else if(returnValue.indexOf("FILE_TOO_LARGE")==0) {
		error = "上传的文件不允许超过" + StringUtils.formatBytes(Number(returnValue.substring("FILE_TOO_LARGE:".length)));
	}
	else {
		error = "远程服务器错误";
	}
	this.fileUploadClient.onFileUploadError(error);
	return false;
};
//服务器命令:完成一个文件的上传
FileUploadClient.Html5Uploader.prototype._sendFileCompleteCommand = function() {
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=fileComplete" +
			  "&characterEncoding=utf-8" +
			  "&seq=" + Math.random();
	var returnValue = this._sendData("GET", url, "", false);
	if(returnValue=="OK") { //成功
		return true;
	}
	this.fileUploadClient.onFileUploadError("远程服务器错误");
	return false;
};
//服务器命令:完成上传
FileUploadClient.Html5Uploader.prototype._sendCompleteUploadCommand = function() {
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=complete" +
			  "&characterEncoding=utf-8" +
			  "&seq=" + Math.random();
	var returnValue = this._sendData("GET", url, "", false);
	if(returnValue=="OK") { //成功
		return true;
	}
	this.fileUploadClient.onFileUploadError("远程服务器错误");
	return false;
};
//服务器命令:放弃一个任务
FileUploadClient.Html5Uploader.prototype._sendCacelCommand = function() {
	var url = RequestUtils.getContextPath() + "/fileUpload" +
			  "?passport=" + this.passport +
			  "&clientVersion=2.0" +
			  "&cmd=cancel" +
			  "&characterEncoding=utf-8" +
			  "&seq=" + Math.random();
	return this._sendData("GET", url, "", false)=="OK";
};
//发送数据
FileUploadClient.Html5Uploader.prototype._sendData = function(method, url, data, async, onDataArriveCallback, onErrorCallback) {
	if(!this.ajax) {
		this.ajax = new Ajax();
	}
	this.ajax.openRequest(method, url, data, async, onDataArriveCallback, onErrorCallback);
	return this.ajax.getResponseText();
};