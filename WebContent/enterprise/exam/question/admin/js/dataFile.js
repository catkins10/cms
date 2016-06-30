window.attachmentUploader.onUploadStart = function(filePath) {
	var description = parent.document.getElementsByName("description")[0];
	if(description.value=="") {
		var name = filePath.replace(/\\/g, '/');
		description.value = name.substring(name.lastIndexOf('/') + 1, name.lastIndexOf('.'));
	}
	return true;
};