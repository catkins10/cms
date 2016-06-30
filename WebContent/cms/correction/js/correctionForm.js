function pasteErrorContent() {
	if(document.forms['cms/correction/correctionForm'] && document.forms['cms/correction/correctionForm'].content.value!="") {
		return;
	}
	var contentEditor = HtmlEditor.getEditor('content');
	if(!contentEditor) {
		window.setTimeout(pasteErrorContent, 100); //等待100ms后重试
		return;
	}
	var range = DomSelection.createRange(contentEditor.editorDocument, contentEditor.editorDocument.body);
	if(range.execCommand) { //IE
		range.execCommand("paste");
	}
}
window.setTimeout(pasteErrorContent, 300); //从剪贴板粘帖错误内容