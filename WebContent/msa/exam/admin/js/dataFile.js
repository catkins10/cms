window.attachmentUploader.onUploadStart = function(filePath) {
	var examName = parent.document.getElementsByName("examName")[0];
	if(examName.value=="") {
		var name = filePath.replace(/\\/g, '/');
		examName.value = name.substring(name.lastIndexOf('/') + 1, name.lastIndexOf('.'));
	}
	return true;
};