//浏览器命令,包括:bold,italic,underline,strikeThrough,subscript,superscript等
EditorNamedCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorNamedCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	if("" + editorDocument.queryCommandEnabled(this.name)!="true") { //命令不被允许执行
		if(!range) {
			return HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
		}
		if('paste'==this.name) { //粘贴,总是允许
			return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
		}
		if(',cut,copy,'.indexOf(',' + this.name + ',')!=-1 && range && range.startOffset!=range.endOffset) { //拷贝和剪切,有选择时允许
			return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
		}
		return HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
	}
	if(',bold,italic,underline,strikeThrough,subscript,superscript,insertOrderedList,insertUnorderedList,'.indexOf(',' + this.name + ',')!=-1) { 
		return "" + editorDocument.queryCommandValue(this.name)=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE : HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	}
	if(this.name.indexOf('justify')==0 && selectedElement) {
		var element = DomUtils.getParentNode(selectedElement, 'p,div,li');
		if(element) {
			var align = element.align ? element.align : element.style.textAlign;
			if(align && (align.toLowerCase()==this.name.substring("justify".length).toLowerCase() || (this.name=='justifyFull' && align.toLowerCase()=='justify'))) {
				return HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE;
			}
		}
	}
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorNamedCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	if(',cut,copy,paste,'.indexOf(',' + this.name + ',')!=-1 && "" + editorDocument.queryCommandEnabled(this.name)!="true") { //不支持
		alert('您的浏览器安全设置不允许执行' + ('cut'==this.name ? '剪切' : ('copy'==this.name ? '复制' : '粘贴')) + '操作，请使用键盘快捷键(Ctrl+' + ('cut'==this.name ? 'X' : ('copy'==this.name ? 'C' : 'V')) + ')来完成');
		return;
	}
	if(',cut,copy,paste,'.indexOf(',' + this.name + ',')==-1) {
		editor.saveUndoStep();
	}
	editorDocument.execCommand(this.name, false, null);
};