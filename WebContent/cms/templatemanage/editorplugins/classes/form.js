//表单命令命令
FormCommand = function() {
	EditorMenuCommand.call(this, 'form', '表单', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_form.gif");
	this.showDropButton = false;
	this.showTitle = true;
	this.width = 160;
};
FormCommand.prototype = new EditorMenuCommand;
FormCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	if(selectedElement && DomUtils.getParentNode(selectedElement, "form")) {
		this.constructor.prototype.execute.call(this, toolbarButton, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else {
		this.insertForm();
	}
};
FormCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
FormCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	var items = [{id:'insertForm', title:'插入表单'},
				 {id:'deleteForm', title:'删除表单'},
				 {id:'insertNormalForm', title:'插入预置界面'},
				 {id:'insertAllFields', title:'插入全部字段'},
				 {id:'insertField', title:'插入单个字段'},
				 {id:'insertButton', title:'插入按钮'},
				 {id:'insertSystemPrompt', title:'插入系统提示'},
				 {id:'insertValidateCodeImage', title:'插入验证码图片'},
				 {id:'insertValidateCodeSwicthLink', title:'插入验证码图片切换链接'},
				 {id:'insertSmsCodeSendLink', title:'插入验证码短信发送链接'},
				 {id:'insertValidateCodeInputBox', title:'插入验证码输入框'}];
	return items;
};
FormCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var form = DomUtils.getParentNode(HtmlEditor.selectedElement, "form");
	var applicationName, formName;
	if(form) {
		applicationName = StringUtils.getPropertyValue(form.action, "applicationName");
		if(!applicationName || applicationName=="") {
			applicationName = form.name; //保持和旧系统兼容
		}
		formName = form.id;
	}
	if(itemId=='insertForm') { //插入表单
		this.insertForm();
	}
	else if(itemId=='deleteForm') { //删除表单
		HtmlEditor.editor.saveUndoStep();
		form.parentNode.removeChild(form);
	}
	else if(itemId=='insertNormalForm') { //插入预置界面
		if(!formName) {
			return;
		}
		var themeType = document.getElementsByName("themeType")[0];
		var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNormalForm.shtml" +
				  "?applicationName=" + applicationName +
				  "&formName=" + formName +
				  (themeType ? "&themeType=" + themeType.value : "");
		DialogUtils.openDialog(url, 640, 400, '', HtmlEditor.getDialogArguments());	
	}
	else if(itemId=='insertAllFields' || itemId=='insertField' || itemId=='insertButton') { //插入全部字段,插入单个字段,插入按钮
		if(!formName) {
			return;
		}
		var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/" + (itemId=='insertAllFields' ? 'insertFormFields.shtml' : (itemId=='insertField' ? 'insertFormField.shtml' : 'insertFormButton.shtml'));
		url += "?applicationName=" + applicationName;
		url += "&formName=" + formName;
		url += "&siteId=" + document.getElementsByName('siteId')[0].value;
		DialogUtils.openDialog(url, 430, 300, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='insertSystemPrompt') { //插入系统提示
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSystemPrompt.shtml", 550, 400, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='insertValidateCodeImage') { //插入验证码图片
		HtmlEditor.editor.saveUndoStep();
		var img = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'img') ;
		img.src = RequestUtils.getContextPath() + "/jeaf/validatecode/generateValidateCodeImage.shtml";
		img.id = "validateCodeImage";
		img.title = "验证码";
	}
	else if(itemId=='insertValidateCodeSwicthLink') { //插入验证码图片切换链接
		HtmlEditor.editor.saveUndoStep();
		var a = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!a) {
			a = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(a.innerHTML=='') {
				a.innerHTML = "看不清，换一张";
			}
		}
		a.href = "#";
		a.setAttribute("onclick", "document.getElementById('validateCodeImage').src=document.getElementById('validateCodeImage').src.split('?')[0] + '?reload=true&seq=' + Math.random();return false;");
	}
	else if(itemId=='insertSmsCodeSendLink') { //插入验证码短信发送链接
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSmsValidateCode.shtml", 390, 150, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='insertValidateCodeInputBox') { //插入验证码输入框
		HtmlEditor.editor.saveUndoStep();
		var input = HtmlEditor.selectedElement;
		if(!input || !input.type || input.type!="text") {
			input = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'input');
			input = DomUtils.setAttribute(input, 'type', 'text');
			input.className = "field";
		}
		DomUtils.setAttribute(input, 'name', 'validateCode');
	}
};
FormCommand.prototype.insertForm = function() { //插入表单
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectForm.shtml";
	url += "?currentApplicationName=" + document.getElementsByName("applicationName")[0].value;
	url += "&script=" + StringUtils.utf8Encode("FormCommand.insertForm('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}', '{dialogURL}')");
	DialogUtils.openDialog(url, 550, 360);
};
FormCommand.insertForm = function(applicationName, formName, title, dialogUrl) {
	if(dialogUrl=='' || dialogUrl=='null') {
		FormCommand.doInsertForm(applicationName, formName, title, '');
		return;
	}
	dialogUrl = RequestUtils.getContextPath() + dialogUrl +
				(dialogUrl.indexOf('?')==-1 ? '?' : '&') + "formName=" + formName +
				"&applicationName=" + applicationName;
	DialogUtils.openDialog(dialogUrl, 430, 160, '', HtmlEditor.getDialogArguments());
};
FormCommand.doInsertForm = function(applicationName, formName, title, extendProperties) {
	var form = DomUtils.getParentNode(HtmlEditor.selectedElement, "form");
	var html;
	if(form) {
		if(form.id && form.id!=formName) { //之前已经存在一个表单,而且不是当前要插入的表单
			if(!confirm('表单已经存在，是否替换？')) {
				return false;
			}
		}
	}
	else if((html=HtmlEditor.range.htmlText)=="" || html) { //IE,textrange
		if(html.toLowerCase().indexOf("<form")!=-1) { //检查下级是否存在form
			alert("不允许表单嵌套");
			return false;
		}
		HtmlEditor.editor.saveUndoStep();
		if(html=="") {
			html = "&nbsp;";
		}
		try {
			HtmlEditor.range.pasteHTML("<form id=\"tempForm\">" + html + "</form>");
		}
		catch(e) {
			alert("选定的区域不允许插入表单,请重新选择。");
			return true;
		}
		form = HtmlEditor.editorDocument.getElementById("tempForm");
		html = form.innerHTML.toLowerCase().replace(/[\r\n ]/g, "");
		if(html=="<p>&nbsp;</p>" || html=="&nbsp;" || html=="<p></p>") {
			form.innerHTML = "";
		}
	}
	else if(HtmlEditor.range.select) {
		if(HtmlEditor.selectedElement.tagName=='FORM' || HtmlEditor.selectedElement.getElementsByTagName("FORM")[0]) {
			alert("不允许表单嵌套");
			return false;
		}
		HtmlEditor.editor.saveUndoStep();
		form = HtmlEditor.editorDocument.createElement('form');
		HtmlEditor.selectedElement.parentNode.insertBefore(form, HtmlEditor.selectedElement);
		form.appendChild(HtmlEditor.selectedElement);
	}
	else if(HtmlEditor.range.cloneContents) {
		var documentFragment = HtmlEditor.range.cloneContents();
		for(var i=0; i<(documentFragment.children ? documentFragment.children.length : 0); i++) {
			if(documentFragment.children[i].tagName=="FORM" || documentFragment.children[i].getElementsByTagName("FORM")[0]) {
				alert("不允许表单嵌套");
				return false;
			}
		}
		HtmlEditor.editor.saveUndoStep();
		form = HtmlEditor.editorDocument.createElement("form");
        HtmlEditor.range.surroundContents(form);
    }
	form.id = formName;
	form.style.margin= 0;
	form.title = "表单:" + title;
	form.className = "form";
	form.action = "applicationName=" + applicationName + (extendProperties && extendProperties!='' ? "&" + extendProperties : "");
};
HtmlEditor.registerCommand(new FormCommand());