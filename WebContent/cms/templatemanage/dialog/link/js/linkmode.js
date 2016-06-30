function setLinkOpenMode(openMode, fieldName) {
	if(!fieldName) {
		fieldName = "linkOpenMode";
	}
	if(!openMode) {
		openMode = "";
	}
	if(openMode.indexOf('dialog')==0) { //对话框
		document.getElementsByName(fieldName + ".linkDialogWidth")[0].value = openMode.split("_")[1];
		document.getElementsByName(fieldName + ".linkDialogHeight")[0].value = openMode.split("_")[2];
		document.getElementById('linkDialogSizeArea').style.display = '';
	}
	if(openMode.indexOf('dialog')==0) { //对话框
		openMode = "dialog";
	}
	else if(openMode=='_blank') { //当前窗口
		openMode = "newWindow";
	}
	else if(openMode!='recordWindow' && openMode!='newWindow') { //记录窗口
		openMode = 'currentWindow';
	}
	DropdownField.setValue(fieldName + ".openMode", openMode); //设置值
}
function getLinkOpenMode(fieldName) {
	if(!fieldName) {
		fieldName = "linkOpenMode";
	}
	var linkOpenMode = document.getElementsByName(fieldName + ".openMode")[0].value;
	if(linkOpenMode=='newWindow') {
		return "_blank";
	}
	else if(linkOpenMode=='currentWindow') {
		return "_self";
	}
	else if(linkOpenMode=='recordWindow') {
		return "recordWindow";
	}
	else {
		return "dialog_" + document.getElementsByName(fieldName + ".linkDialogWidth")[0].value + "_" + document.getElementsByName(fieldName + ".linkDialogHeight")[0].value;
	}
}
function onLinkOpenModeChange(openMode) {
	document.getElementById('linkDialogSizeArea').style.display = openMode=='dialog' ? '' : 'none';
}
function resetLinkByOpenMode(a, href, fieldName) { //按链接打开方式重置链接
	if(!fieldName) {
		fieldName = "linkOpenMode";
	}
	var openMode = getLinkOpenMode(fieldName); //打开方式
	if(href.indexOf("javascript:")!=-1) {
		href = href;
		a.removeAttribute("target");
	}
	else if(openMode.indexOf("dialog")==0) {
		a.removeAttribute("target");
		var values = openMode.split("_");
		href = "javascript:DialogUtils.openDialog('" + href + "', '" + values[1] + "', '" + values[2] + "')";
	}
	else {
		a.target = openMode;
	}
	a.href = href;
}