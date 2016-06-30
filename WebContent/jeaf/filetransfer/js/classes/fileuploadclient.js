//文件上传客户端
FileUploadClient = function(uploadFrame) {
	if(!(this.uploader = new FileUploadClient.ExtensionUploader(this)).isEnabled() && //浏览器扩展
	   !(this.uploader = new FileUploadClient.PluginUploader(this)).isEnabled() && //插件
	   !(this.uploader = new FileUploadClient.Html5Uploader(this, uploadFrame)).isEnabled()) { //HTML5
	   this.uploader = new FileUploadClient.HtmlUploader(this, uploadFrame); //HTML
	}
};
//事件:校验,由调用者实现,校验通过返回true
FileUploadClient.prototype.onFileUploadStart = function(filePath) {
	return true;
};
//事件:上传中,由调用者实现
FileUploadClient.prototype.onFileUploading = function(filePath, totalFiles, totalSize, currentFile, currentFileNumber, currentFileSize, currentFileComplete, threads, complete, speed, usedTime) {
	
};
//事件:停止上传,由调用者实现
FileUploadClient.prototype.onFileUploadStopped = function() {

};
//事件:上传完成,由调用者实现
FileUploadClient.prototype.onFileUploaded = function(remoteFileNames) {

};
//事件:上传错误,由调用者实现
FileUploadClient.prototype.onFileUploadError = function(errorDescription) {

};
//选择文件并上传,不支持选择文件时返回false, fileFilter:文件过滤,如:视频文件|*.avi;*.rmvb|图片文件|*.bmp;*.jpg|所有文件|*.*|
FileUploadClient.prototype.selectFileToUpload = function(dialogTitle, fileFilter, multiSelect, uploadPassportUrl, overwrite) {
	this.uploadPassportUrl = uploadPassportUrl;
	this.overwrite = overwrite;
	return this.uploader.selectFile(dialogTitle, fileFilter, multiSelect);
};
//选择目录并上传,不支持选择目录时返回false
FileUploadClient.prototype.selectDirectoryToUpload = function(dialogTitle, uploadPassportUrl) {
	this.uploadPassportUrl = uploadPassportUrl;
	this.overwrite = overwrite;
	return this.uploader.selectDirectory(dialogTitle);
};
//事件:文件/或者目录已经选择
FileUploadClient.prototype._onFileSelected = function(filePath, isDirectory) {
	if(filePath=='') {
		return;
	}
	if(!isDirectory) { //不是目录
		var names = filePath.split("\n");
		var i = 0;
		for(; i < names.length && this.onFileUploadStart(names[i]); i++);
		if(i < names.length) {
			return;
		}
	}
	this.upload(filePath, this.uploadPassportUrl, this.overwrite, isDirectory); //启动上传
};
//上传指定的文件
FileUploadClient.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	this.uploader.upload(filePath, uploadPassportUrl, overwrite, isDirectory);
};
//是否可以取消上传
FileUploadClient.prototype.isCancelable = function() {
	return this.uploader.isCancelable();
};
//取消上传
FileUploadClient.prototype.cancelUpload = function() {
	this.uploader.cancelUpload();
};
//停止上传
FileUploadClient.prototype.stopUpload = function() {
	this.uploader.stopUpload();
};
//处理错误
FileUploadClient.prototype.processUploadError = function(error) {
	this.uploader.processUploadError(error);
};