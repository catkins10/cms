window.attachmentUploader.onUploadStart = function(filePath) {
	var validator = DomUtils.createActiveXObject("clsid:FA1DC52B-699F-439C-99C6-EC099AF3E45F", "gefValidator");
	if(validator.IsGEFfile(filePath)!=0) {
		alert('您选择的不是GEF文件，不允许上传');
		return false;
	}
	return true;
};