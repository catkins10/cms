var InsertInquiryCommand = function() {
	var iconURL = RequestUtils.getContextPath() + '/cms/inquiry/editorplugins/icons/inquiry.gif';
	var dialogURL = RequestUtils.getContextPath() + "/cms/inquiry/selectInquirySubject.shtml" +
					"?multiSelect=false" +
					"&selectNodeTypes=inquirySubject" +
					"&script=" + StringUtils.utf8Encode("InsertInquiryCommand.insertInquiry('{id}')"); 
	EditorDialogCommand.call(this, 'inquiry', '调查', true, -1, iconURL,  dialogURL, 550, 360);
};
InsertInquiryCommand.prototype = new EditorDialogCommand;
InsertInquiryCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
InsertInquiryCommand.insertInquiry = function(id) {
	var iframe;
	try {
		iframe = document.createElement('<iframe onload="this.command.doInsertInquiry(this);"></iframe>');
	}
	catch(e) {
		iframe = document.createElement('iframe');
		EventUtils.addEvent(iframe, 'load', function() {
			this.command.doInsertInquiry(this);
		});
	}
	iframe.command = this;
	iframe.style.display = "none";
	document.body.appendChild(iframe);
	iframe.src = RequestUtils.getContextPath() + "/cms/inquiry/editorplugins/insertInquiry.shtml?id=" + id + "&seq=" + Math.random();
};
InsertInquiryCommand.doInsertInquiry = function(iframe) {
	HtmlEditor.editor.saveUndoStep();
	DomSelection.pasteHTML(HtmlEditor.editorWindow, HtmlEditor.range, iframe.contentWindow.document.body.innerHTML);
	iframe.parentNode.removeChild(iframe);
};
HtmlEditor.registerCommand(new InsertInquiryCommand());