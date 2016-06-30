function insertPredefinedPage(type, normalCssUrl) {
	var predefinedPage = document.getElementsByName("predefinedPage")[0].value;
	if(predefinedPage=="") {
		return true;
	}
	var dialogArguments = DialogUtils.getDialogArguments();
	if(!dialogArguments.range) {
		alert("插入位置不正确，请重新选择。");
		return;
	}
	if(dialogArguments.window==dialogArguments.editor.editorWindow) { //为模板插入预置页面
		dialogArguments.editor.saveUndoStep();
	}
	var doc = dialogArguments.document;
	//添加CSS
	if(document.getElementsByName("cssUrl")[0] && document.getElementsByName("cssUrl")[0].value!="") {
		CssUtils.appendCssFile(doc, normalCssUrl, document.getElementsByName("cssUrl")[0].value); //引入CSS
	}
	if(!type) {
		DomSelection.pasteHTML(dialogArguments.window, dialogArguments.range, predefinedPage);
	}
	else {
		DomSelection.pasteHTML(dialogArguments.window, dialogArguments.range, "<a id=\"tempElement1\">&nbsp;</a><!-- " + type + " begin -->" + predefinedPage + "<!-- " + type + " end --><a id=\"tempElement2\">&nbsp;</a>");
		var obj = doc.getElementById("tempElement1");
		obj.parentNode.removeChild(obj);
		obj = dialogArguments.document.getElementById("tempElement2");
		obj.parentNode.removeChild(obj);
	}
}
function previewPage(isForm) {
	var doc = frames['pagePreview'].document;
	doc.open();
	doc.write('<html>');
	doc.write('<head>');
	if(document.getElementsByName("cssUrl")[0]) {
		doc.write('<link href="' + document.getElementsByName("cssUrl")[0].value + '?seq=' + Math.random() + '" rel="stylesheet" type="text/css">');
	}
	doc.write('</head>');
	doc.write('<body onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:auto; margin:3px;">');
	if(isForm) {
		doc.write('<form class="form">');
	}
	doc.write(document.getElementsByName("predefinedPage")[0].value);
	if(isForm) {
		doc.write('</form>');
	}
	doc.write('</body>');
	doc.write('</html>');
	doc.close();
}
function customCssFile(cssUrl) {
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/customCssFile.shtml" +
					"?siteId=" + dialogArguments.editor.document.getElementsByName("siteId")[0].value +
					"&cssUrl=" + cssUrl;
	DialogUtils.openDialog(dialogUrl, 600, 400);
}