//段落格式命令
EditorParagraphCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showTitle = true;
};
EditorParagraphCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return selectedElement && (selectedElement.getElementsByTagName('p')[0] || DomUtils.getParentNode(selectedElement, 'P')) ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorParagraphCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var paragraphs = this._listSelectedParagraphs(editorDocument, range, 0);
	if(!paragraphs) {
		return;
	}
	var textIndent = {name:'textIndent', title:'首行缩进(字数)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.textIndent)};
	var alignMode = {name:'alignMode', title:'对齐方式', inputMode:'radio', defaultValue:paragraphs[0].align, itemsText:'居左|left\\0居中|center\\0居右|right'};
	var marginTop = {name:'marginTop', title:'段前间距(字数)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.marginTop)};
	var marginBottom = {name:'marginBottom', title:'段后间距(字数)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.marginBottom)};
	var lineHeight = {name:'lineHeight', title:'行高(字数)', inputMode:'text', defaultValue:this._parseWordCount(paragraphs[0].style.lineHeight)};
	DialogUtils.openInputDialog('段落格式', [textIndent, alignMode, marginTop, marginBottom, lineHeight], 430, 300, '', function(values) {
		editor.saveUndoStep();
		for(var i=0; i<paragraphs.length; i++) {
			//设置段落属性：首行缩进
			var value = Number(values.textIndent);
			paragraphs[i].style.textIndent = isNaN(value) || value<0 ? '' : value + "em";
			//对齐方式
			paragraphs[i].align = values.alignMode;
			//段前间距
			value = Number(values.marginTop);
			paragraphs[i].style.marginTop = isNaN(value) || value<0 ? '' : value + "em";
			//段后间距
			value = Number(values.marginBottom);
			paragraphs[i].style.marginBottom = isNaN(value) || value<0 ? '' : value + "em";
			//行高
			value = Number(values.lineHeight);
			paragraphs[i].style.lineHeight = isNaN(value) || value<=0 ? '' : value + "em";
		}
	});
};
EditorParagraphCommand.prototype._parseWordCount = function(style) {
	if(!style || style.indexOf('em')==-1) {
		return "";
	}
	return Number(style.replace('em', ''));
};
EditorParagraphCommand.prototype._listSelectedParagraphs = function(editorDocument, range, max) {
	var paragraphs = editorDocument.getElementsByTagName('p');
	if(paragraphs.length==0) {
		return null;
	}
	var selectedParagraphs = [];
	for(var i=0; i<paragraphs.length && (max==0 || selectedParagraphs.length<max); i++) {
		if(DomSelection.inRange(range, paragraphs[i])) { //检查段落是否在选中的范围内
			selectedParagraphs.push(paragraphs[i]);
		}
	}
	return selectedParagraphs.length==0 ? null : selectedParagraphs;
};