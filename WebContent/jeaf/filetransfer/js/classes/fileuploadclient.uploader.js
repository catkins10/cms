//文件上传基类
FileUploadClient.Uploader = function(fileUploadClient) {
	this.fileUploadClient = fileUploadClient;
};
//是否有效
FileUploadClient.Uploader.prototype.isEnabled = function() {
	return false;
};
//选择文件,fileFilter:文件过滤,如:视频文件|*.avi;*.rmvb|图片文件|*.bmp;*.jpg|所有文件|*.*|
FileUploadClient.Uploader.prototype.selectFile = function(dialogTitle, fileFilter, multiSelect) {
	return false;
};
//选择目录
FileUploadClient.Uploader.prototype.selectDirectory = function(dialogTitle) {
	return false;
};
//上传
FileUploadClient.Uploader.prototype.upload = function(filePath, uploadPassportUrl, overwrite, isDirectory) {
	
};
//是否可以取消上传
FileUploadClient.Uploader.prototype.isCancelable = function() {
	return false; 
};
//取消上传
FileUploadClient.Uploader.prototype.cancelUpload = function() {
	return false;
};
//停止上传
FileUploadClient.Uploader.prototype.stopUpload = function() {
	return false;
};
//处理错误
FileUploadClient.Uploader.prototype.processUploadError = function(error) {
	
};