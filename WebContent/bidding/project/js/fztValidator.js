window.attachmentUploader.onUploadStart = function(filePath) {
	var validator = DomUtils.createActiveXObject("clsid:BDB92251-226C-4DC7-93D5-26E83CA6CBF4", "fztValidator");
	//检查是否GEF文件
	if(validator.Check(filePath, "fzt")!=1) {
		alert('您选择的不是FZT文件，不允许上传');
		return false;
	}
	//检查项目编号是否匹配
	validator.QueryInfomation(filePath, "fzt");	
	if("" + validator.GetID()!=window.top.document.getElementById("project.projectNumber").innerHTML) {
		alert('当前FZT文件的项目编号是:' + validator.GetID() + ',和项目不一致，不允许上传');
		return false;
	}
	return true;
};