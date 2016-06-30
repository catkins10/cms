var CloseCommand = function() {
	this.name = 'close';
	this.title = '\u5173\u95ED';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_close.gif";
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; 
};
CloseCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
CloseCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var dialog = (document.parentWindow ? document.parentWindow : document.defaultView).frameElement;
	if(!dialog) {
		window.top.close();
	}
	else {
		(dialog.ownerDocument.parentWindow ? dialog.ownerDocument.parentWindow : dialog.ownerDocument.defaultView).setTimeout('DialogUtils.closeDialog()', 1);
	}
};
HtmlEditor.registerCommand(new CloseCommand());
var SaveCommand = function(name, title, iconUrl, parameters) {
	this.name = name;
	this.title = title;
	this.iconUrl = iconUrl;
	this.showTitle = true;
	this.parameters = parameters;
	this.enabledInSourceCodeMode = true; 
};
SaveCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
SaveCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var templateName = document.getElementsByName("templateName")[0].value;
	if(templateName=="") {
		alert("\u6A21\u677F\u540D\u79F0\u4E0D\u80FD\u4E3A\u7A7A");
		return false;
	}
	FormUtils.doAction(SaveCommand.getActionName('save'), this.parameters);
};
HtmlEditor.registerCommand(new SaveCommand('save', '\u4FDD\u5B58', RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_save.gif"));
HtmlEditor.registerCommand(new SaveCommand('saveAsDefaultTemplate', '\u8BBE\u4E3A\u9ED8\u8BA4', RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_set_default.gif", 'templateAction=setAsDefault'));
SaveCommand.getActionName = function(actionType) {
	var url = document.forms[0].action;
	url = url.substring(url.lastIndexOf('/') + 1);
	url = url.substring(0, url.lastIndexOf('.'));
	return actionType + url.substring(0, 1).toUpperCase() + url.substring(1);
};
var TemplateNameCommand = function() {
	this.name = 'templateName';
	this.title = '\u6A21\u677F\u540D\u79F0';
	this.enabledInSourceCodeMode = true; 
};
TemplateNameCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
TemplateNameCommand.prototype.create = function(toolbarButtonElement) {
	if(!document.getElementsByName("templateName")[0]) {
		return;
	}
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>\u540D\u79F0</td>' +
									 '		<td><input onchange="document.getElementsByName(\'templateName\')[0].value=this.value;" value="' + document.getElementsByName("templateName")[0].value + '" type="text" style="width:120px;"></td>' +
									 '	</tr>' +
									 '</table>';
};
HtmlEditor.registerCommand(new TemplateNameCommand());
HtmlEditor.registerCommand(new EditorDialogCommand('uploadTemplate', '\u4E0A\u4F20', true, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_upload_page.gif", RequestUtils.getContextPath() + "/cms/templatemanage/dialog/uploadTemplate.shtml?id=" + document.getElementsByName("id")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value, 430, 180));
if(document.getElementsByName("act")[0].value!="create") { 
	
	var DeleteCommand = function() {
		this.name = 'delete';
		this.title = '\u5220\u9664';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_delete.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; 
	};
	DeleteCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	DeleteCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
		if(confirm('\u5220\u9664\u540E\u4E0D\u53EF\u6062\u590D\uFF0C\u662F\u5426\u786E\u5B9A\u5220\u9664\uFF1F')) {
			FormUtils.doAction(SaveCommand.getActionName('delete'));
		}
	};
	HtmlEditor.registerCommand(new DeleteCommand());
	
	var CopyTemplateCommand = function() {
		this.name = 'copyTemplate';
		this.title = '\u590D\u5236';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_copy_template.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; 
	};
	CopyTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	CopyTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
		document.cookie = 'copiedTemplateId=' + document.getElementsByName("id")[0].value + ';path=/'; 
		document.cookie = 'copiedTemplateName=' + document.getElementsByName("templateName")[0].value + ';path=/'; 
	};
	HtmlEditor.registerCommand(new CopyTemplateCommand());
	
	
	if(document.getElementsByName("applicationName")[0] && document.getElementsByName("applicationName")[0].value!="cms/sitemanage") { 
		var BatchCopyTemplateCommand = function() {
			this.name = 'batchCopyTemplate';
			this.title = '\u6279\u91CF\u590D\u5236';
			this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_copy_template.gif";
			this.showTitle = true;
			this.enabledInSourceCodeMode = true; 
		};
		BatchCopyTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
			return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
		};
		BatchCopyTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
			if(document.getElementsByName("batchCopyPageIds").length==0) {
				FormUtils.appendInput(document.body, 'hidden', 'batchCopyPageIds');
				FormUtils.appendInput(document.body, 'hidden', 'batchCopyPageNames');
			}
			var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectPage.shtml" +
					  "?multiSelect=true" +
					  "&currentApplicationName=" + document.getElementsByName("applicationName")[0].value +
					  "&param=" + StringUtils.utf8Encode("batchCopyPageIds{id},batchCopyPageNames{name|\u9875\u9762\u540D\u79F0|100%}") +
					  "&script=BatchCopyTemplateCommand.BatchCopy()";
			DialogUtils.openDialog(url, 550, 360);
		};
		BatchCopyTemplateCommand.BatchCopy = function() {
			var url = RequestUtils.getContextPath() +
					  "/cms/templatemanage/batchCopyTemplate.shtml?sourceTemplateId=" + document.getElementsByName("id")[0].value +
					  "&targetPageNames=" + document.getElementsByName("batchCopyPageIds")[0].value;
			DialogUtils.openDialog(url, 360, 180);
			document.getElementsByName("batchCopyPageIds")[0].value = "";
			document.getElementsByName("batchCopyPageNames")[0].value = "";
		};
		HtmlEditor.registerCommand(new BatchCopyTemplateCommand());
	}
	
	
	var ExportTemplateCommand = function() {
		this.name = 'export';
		this.title = '\u5BFC\u51FA';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_export.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; 
	};
	ExportTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	ExportTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
		window.location = RequestUtils.getContextPath() + "/cms/templatemanage/exportTemplate.shtml?id=" + document.getElementsByName("id")[0].value;
	};
	HtmlEditor.registerCommand(new ExportTemplateCommand());
		
	
	var RestoreCommand = function() {
		this.name = 'restore';
		this.title = '\u8FD8\u539F';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_restore.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; 
	};
	RestoreCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	RestoreCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
		if(confirm('\u662F\u5426\u786E\u5B9A\u8FD8\u539F\u6A21\u677F\uFF1F')) {
			FormUtils.doAction(SaveCommand.getActionName('save'), 'templateAction=restore');
		}
	};
	HtmlEditor.registerCommand(new RestoreCommand());
}
var LoadNormalTemplateCommand = function() {
	this.name = 'loadNormalTemplate';
	this.title = '\u9884\u7F6E\u6A21\u677F';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_load_normal_template.gif";
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; 
};
LoadNormalTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
LoadNormalTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(confirm('\u52A0\u8F7D\u9884\u7F6E\u6A21\u677F\u4F1A\u8986\u76D6\u5F53\u524D\u6A21\u677F\u4E14\u4E0D\u53EF\u6062\u590D\uFF0C\u662F\u5426\u786E\u5B9A\u52A0\u8F7D\uFF1F')) {
		FormUtils.doAction(SaveCommand.getActionName('save'), 'templateAction=loadNormal');
	}
};
HtmlEditor.registerCommand(new LoadNormalTemplateCommand());
var copiedTemplateId = CookieUtils.getCookie('copiedTemplateId');
if(copiedTemplateId && copiedTemplateId!=document.getElementsByName("id")[0].value) {
	var PasteTemplateCommand = function() {
		this.name = 'pasteTemplate';
		this.title = '\u7C98\u5E16';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_paste_template.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; 
	};
	PasteTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	PasteTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
		var copiedTemplateId = CookieUtils.getCookie('copiedTemplateId');
		if(copiedTemplateId==document.getElementsByName("id")[0].value) {
			alert('\u4E0D\u80FD\u7C98\u5E16\u6A21\u677F\u81EA\u5DF1');
			return;
		}
		if(confirm('\u60A8\u8981\u7C98\u5E16\u7684\u6A21\u5F0F\u662F\uFF1A' + CookieUtils.getCookie('copiedTemplateName') + '\uFF0C\u7C98\u5E16\u6A21\u677F\u4F1A\u8986\u76D6\u5F53\u524D\u6A21\u677F\u4E14\u4E0D\u53EF\u6062\u590D\uFF0C\u662F\u5426\u786E\u5B9A\u7C98\u5E16\uFF1F')) {
			FormUtils.doAction(SaveCommand.getActionName('save'), 'templateAction=copy&copiedTemplateId=' + copiedTemplateId);
		}
	};
	HtmlEditor.registerCommand(new PasteTemplateCommand());
}
HtmlEditor.registerCommand(new EditorDialogCommand('editElementHtml', '\u5C40\u90E8\u6E90\u7801', true, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_edit_element_html.gif", RequestUtils.getContextPath() + "/cms/templatemanage/dialog/editElementHtml.shtml", 600, 380));
HtmlEditor.registerCommand(new EditorDialogCommand('insertSubTemplate', '\u5B50\u6A21\u677F', true, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_sub_template.gif", RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSubTemplate.shtml", 600, 400));

AdvertCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/advert/templatemanage/insertAdvertSpace.shtml";
	EditorDialogCommand.call(this, 'advert', '\u5E7F\u544A\u4F4D', false, -1, RequestUtils.getContextPath() + "/cms/advert/icons/advert.gif", url, 500, 300);
	this.showTitle = true;
};
AdvertCommand.prototype = new EditorDialogCommand;
AdvertCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new AdvertCommand());

ApplicationTitleCommand = function() {
	this.name = 'applicationTitle';
	this.title = '\u5E94\u7528\u540D\u79F0';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_application_title.gif";
	this.showTitle = true;
};
ApplicationTitleCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ApplicationTitleCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	HtmlEditor.editor.saveUndoStep();
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
	if(!obj) {
		obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
		if(!obj) {
			alert('\u8BF7\u91CD\u65B0\u9009\u62E9\u63D2\u5165\u7684\u4F4D\u7F6E');
			return;
		}
	}
	obj.innerHTML = "<\u5E94\u7528\u540D\u79F0>";
	obj.id = "applicationTitle";
};
HtmlEditor.registerCommand(new ApplicationTitleCommand());

var EditCssCommand = function() {
	this.name = 'editCss';
	this.title = '\u7F16\u8F91CSS';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_edit_css.gif";
	this.showTitle = true;
};
EditCssCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditCssCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	HtmlEditor.editorDocument = editorDocument;
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/selectTemplateAttachment.shtml" +
			  "?id=" + document.getElementsByName("id")[0].value +
			  "&attachmentSelector.scriptRunAfterSelect=" + StringUtils.utf8Encode("EditCssCommand.editCss('{fileName}', '{URL}')") +
			  "&attachmentSelector.type=css";
	DialogUtils.openDialog(url, 600, 360);
};
EditCssCommand.editCss = function(fileName, cssUrl) {
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/templateCssFile.shtml" +
			  "?id=" + document.getElementsByName("id")[0].value +
			  "&fileName=" + StringUtils.utf8Encode(fileName) +
			  "&script=" + StringUtils.utf8Encode("EditCssCommand.afterCssModified('" + cssUrl + "')");
	DialogUtils.openDialog(url, 760, 480);
};
EditCssCommand.afterCssModified = function(cssUrl) {
	var links = HtmlEditor.editorDocument.getElementsByTagName("link");
	for(var i=0; i<(!links ? 0 : links.length); i++) {
		var href = links[i].getAttribute('href', 2);
		if(href && href.substring(0, cssUrl.length)==cssUrl) {
			links[i].href = cssUrl + "?seq=" + ("" + Math.random()).substring(2);
		}
	}
};
HtmlEditor.registerCommand(new EditCssCommand());

DateCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertDate.shtml";
	EditorDialogCommand.call(this, 'date', '\u65E5\u671F', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_date.gif", url, 470, 200);
	this.showTitle = true;
};
DateCommand.prototype = new EditorDialogCommand;
DateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new DateCommand());

FieldCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
			  "?applicationName=" + document.getElementsByName("applicationName")[0].value +
			  "&pageName=" + document.getElementsByName("pageName")[0].value;
	EditorDialogCommand.call(this, 'field', '\u5B57\u6BB5', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_field.gif", url, 550, 360);
	this.showTitle = true;
};
FieldCommand.prototype = new EditorDialogCommand;
FieldCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
if(document.getElementsByName("pageName")[0]) {
	HtmlEditor.registerCommand(new FieldCommand());
}

FormCommand = function() {
	EditorMenuCommand.call(this, 'form', '\u8868\u5355', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_form.gif");
	this.showDropButton = false;
	this.showTitle = true;
	this.width = 160;
};
FormCommand.prototype = new EditorMenuCommand;
FormCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(selectedElement && DomUtils.getParentNode(selectedElement, "form")) {
		this.constructor.prototype.execute.call(this, toolbarButton, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else {
		this.insertForm();
	}
};
FormCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
FormCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	var items = [{id:'insertForm', title:'\u63D2\u5165\u8868\u5355'},
				 {id:'deleteForm', title:'\u5220\u9664\u8868\u5355'},
				 {id:'insertNormalForm', title:'\u63D2\u5165\u9884\u7F6E\u754C\u9762'},
				 {id:'insertAllFields', title:'\u63D2\u5165\u5168\u90E8\u5B57\u6BB5'},
				 {id:'insertField', title:'\u63D2\u5165\u5355\u4E2A\u5B57\u6BB5'},
				 {id:'insertButton', title:'\u63D2\u5165\u6309\u94AE'},
				 {id:'insertSystemPrompt', title:'\u63D2\u5165\u7CFB\u7EDF\u63D0\u793A'},
				 {id:'insertValidateCodeImage', title:'\u63D2\u5165\u9A8C\u8BC1\u7801\u56FE\u7247'},
				 {id:'insertValidateCodeSwicthLink', title:'\u63D2\u5165\u9A8C\u8BC1\u7801\u56FE\u7247\u5207\u6362\u94FE\u63A5'},
				 {id:'insertSmsCodeSendLink', title:'\u63D2\u5165\u9A8C\u8BC1\u7801\u77ED\u4FE1\u53D1\u9001\u94FE\u63A5'},
				 {id:'insertValidateCodeInputBox', title:'\u63D2\u5165\u9A8C\u8BC1\u7801\u8F93\u5165\u6846'}];
	return items;
};
FormCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	var form = DomUtils.getParentNode(HtmlEditor.selectedElement, "form");
	var applicationName, formName;
	if(form) {
		applicationName = StringUtils.getPropertyValue(form.action, "applicationName");
		if(!applicationName || applicationName=="") {
			applicationName = form.name; 
		}
		formName = form.id;
	}
	if(itemId=='insertForm') { 
		this.insertForm();
	}
	else if(itemId=='deleteForm') { 
		HtmlEditor.editor.saveUndoStep();
		form.parentNode.removeChild(form);
	}
	else if(itemId=='insertNormalForm') { 
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
	else if(itemId=='insertAllFields' || itemId=='insertField' || itemId=='insertButton') { 
		if(!formName) {
			return;
		}
		var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/" + (itemId=='insertAllFields' ? 'insertFormFields.shtml' : (itemId=='insertField' ? 'insertFormField.shtml' : 'insertFormButton.shtml'));
		url += "?applicationName=" + applicationName;
		url += "&formName=" + formName;
		url += "&siteId=" + document.getElementsByName('siteId')[0].value;
		DialogUtils.openDialog(url, 430, 300, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='insertSystemPrompt') { 
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSystemPrompt.shtml", 550, 400, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='insertValidateCodeImage') { 
		HtmlEditor.editor.saveUndoStep();
		var img = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'img') ;
		img.src = RequestUtils.getContextPath() + "/jeaf/validatecode/generateValidateCodeImage.shtml";
		img.id = "validateCodeImage";
		img.title = "\u9A8C\u8BC1\u7801";
	}
	else if(itemId=='insertValidateCodeSwicthLink') { 
		HtmlEditor.editor.saveUndoStep();
		var a = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!a) {
			a = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(a.innerHTML=='') {
				a.innerHTML = "\u770B\u4E0D\u6E05\uFF0C\u6362\u4E00\u5F20";
			}
		}
		a.href = "#";
		a.setAttribute("onclick", "document.getElementById('validateCodeImage').src=document.getElementById('validateCodeImage').src.split('?')[0] + '?reload=true&seq=' + Math.random();return false;");
	}
	else if(itemId=='insertSmsCodeSendLink') { 
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSmsValidateCode.shtml", 390, 150, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='insertValidateCodeInputBox') { 
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
FormCommand.prototype.insertForm = function() { 
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
		if(form.id && form.id!=formName) { 
			if(!confirm('\u8868\u5355\u5DF2\u7ECF\u5B58\u5728\uFF0C\u662F\u5426\u66FF\u6362\uFF1F')) {
				return false;
			}
		}
	}
	else if((html=HtmlEditor.range.htmlText)=="" || html) { 
		if(html.toLowerCase().indexOf("<form")!=-1) { 
			alert("\u4E0D\u5141\u8BB8\u8868\u5355\u5D4C\u5957");
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
			alert("\u9009\u5B9A\u7684\u533A\u57DF\u4E0D\u5141\u8BB8\u63D2\u5165\u8868\u5355,\u8BF7\u91CD\u65B0\u9009\u62E9\u3002");
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
			alert("\u4E0D\u5141\u8BB8\u8868\u5355\u5D4C\u5957");
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
				alert("\u4E0D\u5141\u8BB8\u8868\u5355\u5D4C\u5957");
				return false;
			}
		}
		HtmlEditor.editor.saveUndoStep();
		form = HtmlEditor.editorDocument.createElement("form");
        HtmlEditor.range.surroundContents(form);
    }
	form.id = formName;
	form.style.margin= 0;
	form.title = "\u8868\u5355:" + title;
	form.className = "form";
	form.action = "applicationName=" + applicationName + (extendProperties && extendProperties!='' ? "&" + extendProperties : "");
};
HtmlEditor.registerCommand(new FormCommand());

var LinkCommand = function() {
	this.name = 'templateLink';
	this.title = '\u94FE\u63A5';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_link.gif";
	this.showTitle = true;
};
LinkCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
LinkCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectSiteLink.shtml";
	var applicationName = document.getElementsByName("applicationName")[0];
	var pageName = document.getElementsByName("pageName")[0];
	url += "?currentApplicationName=" + (applicationName ?  applicationName.value : "");
	url += "&currentPageName=" + (pageName ?  pageName.value : "");
	url += "&script=" + StringUtils.utf8Encode("LinkCommand.insertLink('{id}', '{name}', '{dialogURL}')");
	DialogUtils.openDialog(url, 550, 360);
};
LinkCommand.insertLink = function(id, title, dialogURL, url, applicationTitle) {
	var themeType = document.getElementsByName("themeType")[0];
	var url = RequestUtils.getContextPath() + dialogURL +
			 (dialogURL.indexOf('?')==-1 ? '?' : '&') + "themeType=" + (themeType ?  themeType.value : "");
	DialogUtils.openDialog(url, 500, 180, '', HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new LinkCommand());

LocationCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/templatemanage/insertSiteLocation.shtml";
	EditorDialogCommand.call(this, 'location', '\u680F\u76EE\u4F4D\u7F6E', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_location.gif", url, 430, 180);
	this.showTitle = true;
};
LocationCommand.prototype = new EditorDialogCommand;
LocationCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new LocationCommand());

NavigatorCommand = function() {
	EditorMenuCommand.call(this, 'navigator', '\u5BFC\u822A\u680F', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_navigator.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
NavigatorCommand.prototype = new EditorMenuCommand;
NavigatorCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
NavigatorCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	return [{id:'insertNavigator', title:'\u521B\u5EFA\u5BFC\u822A\u680F', iconIndex:-1},
			{id:'insertNavigatorLinkItem', title:'\u8BBE\u4E3A\u5BFC\u822A\u680F\u94FE\u63A5', iconIndex:-1},
			{id:'insertNavigatorMenuItem', title:'\u8BBE\u4E3A\u5BFC\u822A\u680F\u83DC\u5355', iconIndex:-1},
			{id:'insertNavigatorItemList', title:'\u63D2\u5165\u5BFC\u822A\u9879\u76EE\u5217\u8868', iconIndex:-1}];
};
NavigatorCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	var themeType = document.getElementsByName("themeType")[0];
	if(itemId=="insertNavigator") { 
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,ul");
		if(element==null) {
			alert("\u5BFC\u822A\u680F\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u5BFC\u822A\u680F\u4F4D\u7F6E\u3002");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigator.shtml", 705, 390, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertNavigatorLinkItem") { 
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,li");
		if(element==null || element.id=="navigator") {
			alert("\u94FE\u63A5\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u94FE\u63A5\u4F4D\u7F6E\u3002");
		}
		else if(this.getNavigator(element)==null) {
			alert("\u94FE\u63A5\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u94FE\u63A5\u4F4D\u7F6E\u3002");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigatorLinkItem.shtml", 562, 230, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertNavigatorMenuItem") { 
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,li");
		if(element==null || element.id=="navigator") {
			alert("\u83DC\u5355\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u83DC\u5355\u4F4D\u7F6E\u3002");
		}
		else if(this.getNavigator(element)==null) {
			alert("\u94FE\u63A5\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u94FE\u63A5\u4F4D\u7F6E\u3002");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigatorMenuItem.shtml", 660, 380, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertNavigatorItemList") { 
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,ul");
		if(element==null) {
			alert("\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u5217\u8868\u63D2\u5165\u4F4D\u7F6E\u3002");
		}
		else if(element.id!="navigator" && this.getNavigator(element)==null) {
			alert("\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9\u5217\u8868\u63D2\u5165\u4F4D\u7F6E\u3002");
		}
		else {
			
			var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml";
			url += "?navigatorSupportOnly=true";
			url += "&currentApplicationName=" + document.getElementsByName("applicationName")[0].value;
			url += "&currentPageName=" + document.getElementsByName("pageName")[0].value;
			url += "&script=" + StringUtils.utf8Encode("NavigatorCommand.insertNavigatorItemList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}')");
			DialogUtils.openDialog(url, 550, 360, "", HtmlEditor.getDialogArguments());
		}
	}
};
NavigatorCommand.prototype.getNavigator = function(element) { 
	while(element.tagName.toLowerCase()!="body") {
		if(element.id=="navigator") {
			return element;
		}
		element = element.parentElement;
	}
	return null;
};
NavigatorCommand.insertNavigatorItemList = function(applicationName, recordListName, title) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigatorItemList.shtml" +
					"?recordListName=" + recordListName +
					(applicationName!="" ? "&applicationName=" + applicationName : "") +
					"&themeType=" + (themeType ?  themeType.value : "");
	DialogUtils.openDialog(dialogUrl, 696, 300, "", HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new NavigatorCommand());

PagingCommand = function() {
	EditorMenuCommand.call(this, 'paging', '\u5206\u9875', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_page_action.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
PagingCommand.prototype = new EditorMenuCommand;
PagingCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
PagingCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	return [{id:'biddingRecordList', title:'\u7ED1\u5B9A\u8BB0\u5F55\u5217\u8868', iconIndex:-1},
			{id:'firstPage', title:'\u7B2C\u4E00\u9875', iconIndex:-1},
			{id:'previousPage', title:'\u4E0A\u4E00\u9875', iconIndex:-1},
			{id:'nextPage', title:'\u4E0B\u4E00\u9875', iconIndex:-1},
			{id:'lastPage', title:'\u6700\u540E\u4E00\u9875', iconIndex:-1},
			{id:'pageNo', title:'\u9875\u7801', iconIndex:-1},
			{id:'recordCount', title:'\u8BB0\u5F55\u603B\u6570', iconIndex:-1},
			{id:'pageCount', title:'\u603B\u9875\u6570', iconIndex:-1},
			{id:'currentPageNo', title:'\u5F53\u524D\u9875\u7801', iconIndex:-1},
			{id:'pageSwitch', title:'\u9875\u9762\u8DF3\u8F6C\u8F93\u5165\u6846', iconIndex:-1},
			{id:'pageSwitchButton', title:'\u9875\u9762\u8DF3\u8F6C\u6309\u94AE', iconIndex:-1}];
};
PagingCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(itemId=="biddingRecordList") { 
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a');
		if(!obj || obj.id!='recordList') {
			alert("\u672A\u9009\u5B9A\u8BB0\u5F55\u5217\u8868");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		
		var elements = HtmlEditor.editorDocument.getElementsByTagName("A");
		for(var i=0; i<elements.length; i++) {
			var urn = elements[i].getAttribute("urn");
			if(urn && urn.indexOf('recordCount=')!=-1 && elements[i].target=='paging') {
				elements[i].removeAttribute("target");
			}
		}
		obj.target = 'paging';
		alert("\u7ED1\u5B9A\u5B8C\u6210");
	}
	else if(itemId=="pageSwitch") { 
		var obj = HtmlEditor.selectedElement;
		if(!obj || obj.type!="text") {
			obj = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'input') ;
			obj = DomUtils.setAttribute(obj, "type", "text");
			obj.style.width = "50px";
			obj.style.border = "#a0a0a0 1px solid";
			obj.style.textAlign = "center";
		}
		DomUtils.setAttribute(obj, "name", "pageSwitch");
	}
	else if(itemId=="pageSwitchButton") { 
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "button,span,div,input,a,image,li");
		if(!obj) {
			obj = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'button') ;
			obj.innerHTML = '\u8F6C\u5230';
		}
		DomUtils.setAttribute(obj, "name", "pageSwitchButton");
	}
	else if(itemId=="pageNo") { 
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertPageNo.shtml", 430, 220, "", HtmlEditor.getDialogArguments());
	}
	else {
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!obj) {
			obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(!obj) {
				alert('\u8BF7\u91CD\u65B0\u9009\u62E9\u63D2\u5165\u7684\u4F4D\u7F6E');
				return;
			}
		}
		var actionName;
		if(itemId=='firstPage') {
			actionName = '\u7B2C\u4E00\u9875';
		}
		else if(itemId=='previousPage') {
			actionName = '\u4E0A\u4E00\u9875';
		}
		else if(itemId=='nextPage') {
			actionName = '\u4E0B\u4E00\u9875';
		}
		else if(itemId=='lastPage') {
			actionName = '\u6700\u540E\u4E00\u9875';
		}
		else if(itemId=='recordCount') {
			actionName = '\u8BB0\u5F55\u603B\u6570';
		}
		else if(itemId=='pageCount') {
			actionName = '\u603B\u9875\u6570';
		}
		else if(itemId=='currentPageNo') {
			actionName = '\u5F53\u524D\u9875\u7801';
		}
		if(obj.innerHTML=='') {
			obj.innerHTML = "\u8BB0\u5F55\u603B\u6570,\u5F53\u524D\u9875\u7801,\u603B\u9875\u6570".indexOf(actionName)!=-1 ? "<" + actionName + ">" : actionName;
		}
		obj.id = "pageAction";
		obj.setAttribute("urn", actionName);
	}
};
HtmlEditor.registerCommand(new PagingCommand());

RecordListCommand = function() {
	EditorMenuCommand.call(this, 'recordList', '\u8BB0\u5F55\u5217\u8868', 38);
	this.showDropButton = false;
	this.showTitle = true;
};
RecordListCommand.prototype = new EditorMenuCommand;
RecordListCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	if((document.getElementsByName('searchPage')[0] && document.getElementsByName('searchPage')[0].value=="true") ||
	   (selectedElement && selectedElement.id=="recordList")) {
		this.constructor.prototype.execute.call(this, toolbarButton, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else {
		this.insertRecordList();
	}
};
RecordListCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
RecordListCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	var items = [{id:'insertRecordList', title:'\u63D2\u5165\u8BB0\u5F55\u5217\u8868', iconIndex:80}];
	if(document.getElementsByName('searchPage')[0] && document.getElementsByName('searchPage')[0].value=="true") { 
		items.push({id:'insertSearchResult', title:'\u63D2\u5165\u641C\u7D22\u7ED3\u679C', iconIndex:4});
	}
	if(selectedElement && selectedElement.id=="recordList") {
		items.push({id:'editRecordList', title:'\u7F16\u8F91\u8BB0\u5F55\u5217\u8868', iconIndex:38});
		items.push({id:'deleteRecordList', title:'\u5220\u9664\u8BB0\u5F55\u5217\u8868', iconIndex:81});
	}
	return items;
};
RecordListCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	if(itemId=="insertRecordList") { 
		this.insertRecordList();
	}
	else if(itemId=="insertSearchResult") { 
		RecordListCommand.doInsertRecordList(document.getElementsByName("applicationName")[0].value, document.getElementsByName("searchResultsName")[0].value, "\u641C\u7D22\u7ED3\u679C", true, false);
	}
	else if(itemId=="editRecordList") { 
		this.editRecordList();
	}
	else if(itemId=="deleteRecordList") { 
		this.deleteRecordList();
	}
};
RecordListCommand.prototype.insertRecordList = function() { 
	var applicationName = document.getElementsByName("applicationName")[0];
	var pageName = document.getElementsByName("pageName")[0];
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml" +
			  "?currentApplicationName=" + (applicationName ?  applicationName.value : "") +
			  "&currentPageName=" + (pageName ?  pageName.value : "") +
			  "&script=" + StringUtils.utf8Encode("RecordListCommand.doInsertRecordList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}', false, '{private}', '{recordClassName}')");
	DialogUtils.openDialog(url, 550, 360);
};
RecordListCommand.prototype.editRecordList = function() { 
	
	if(!HtmlEditor.selectedElement || HtmlEditor.selectedElement.id!='recordList') {
		alert("\u672A\u9009\u5B9A\u8BB0\u5F55\u5217\u8868");
		return;
	}
	var applicationName = '';
	var recordListName = '';
	var privateRecordList = '';
	var recordClassName = '';
	var urn = HtmlEditor.selectedElement.getAttribute("urn");
	var index = urn.indexOf('applicationName=');
	if(index!=-1) {
		index += 'applicationName='.length;
		var indexEnd = urn.indexOf("&", index);
		applicationName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
	}
	index = urn.indexOf('recordListName=');
	if(index!=-1) {
		index += 'recordListName='.length;
		var indexEnd = urn.indexOf("&", index);
		recordListName = indexEnd==-1 ? urn.substring(index) : urn.substring(index, indexEnd);
	}
	index = urn.indexOf('privateRecordList=');
	if(index!=-1) {
		index += 'privateRecordList='.length;
		var indexEnd = urn.indexOf("&", index);
		privateRecordList = indexEnd==-1 ? urn.substring(index) : urn.substring(index, indexEnd);
	}
	index = urn.indexOf('recordClassName=');
	if(index!=-1) {
		index += 'recordClassName='.length;
		var indexEnd = urn.indexOf("&", index);
		recordClassName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
	}
	if(recordListName=='') {
		alert("\u8BB0\u5F55\u5217\u8868\u683C\u5F0F\u4E0D\u6B63\u786E");
		return;
	}
	RecordListCommand.doInsertRecordList(applicationName, recordListName, HtmlEditor.selectedElement.innerHTML.replace(/&lt;/gi, "").replace(/&gt;/gi, ""), urn.indexOf("searchResults=true")!=-1, privateRecordList, recordClassName);
};
RecordListCommand.prototype.deleteRecordList = function() { 
	if(!HtmlEditor.selectedElement || HtmlEditor.selectedElement.id!='recordList') {
		alert("\u672A\u9009\u5B9A\u8BB0\u5F55\u5217\u8868");
		return;
	}
	if(confirm("\u662F\u5426\u786E\u5B9A\u5220\u9664\u8BB0\u5F55\u5217\u8868<" + HtmlEditor.selectedElement.innerHTML.replace(/&[^;]*;/gi, "") + ">")) {
		HtmlEditor.editor.saveUndoStep();
		HtmlEditor.selectedElement.parentNode.removeChild(HtmlEditor.selectedElement);
	}
};
RecordListCommand.doInsertRecordList = function(applicationName, recordListName, title, isSearchResults, privateRecordList, recordClassName) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordList.shtml" +
					"?recordListName=" + recordListName +
					(applicationName!="" ? "&applicationName=" + applicationName : "") +
					(isSearchResults ? "&searchResults=true" : "") +
					"&themeType=" + (themeType ?  themeType.value : "") +
					(privateRecordList=="true" ? "&privateRecordList=true&recordClassName=" + recordClassName : "");
	DialogUtils.openDialog(dialogUrl, 780, 400, '', HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new RecordListCommand());

RssCommand = function() {
	EditorMenuCommand.call(this, 'rss', 'RSS', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_rss.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
RssCommand.prototype = new EditorMenuCommand;
RssCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
RssCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	return [{id:'insertRssSubscribeLink', title:'\u63D2\u5165RSS\u8BA2\u9605\u94FE\u63A5', iconIndex:-1},
			{id:'insertRssChannel', title:'\u63D2\u5165RSS\u9891\u9053', iconIndex:-1}];
};
RssCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	var themeType = document.getElementsByName("themeType")[0];
	if(itemId=="insertRssSubscribeLink") { 
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRssSubscribeLink.shtml?themeType=" + (themeType ?  themeType.value : ""), 455, 180, "", HtmlEditor.getDialogArguments());
	}
	else if(itemId=="insertRssChannel") { 
		var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml";
		url += "?rssChannelSupportOnly=true";
		url += "&script=" + StringUtils.utf8Encode("RssCommand.InsertRSSChannel('{id}'.split('__')[0], '{id}'.split('__')[1], '{templateExtendURL}', '{name}')");
		DialogUtils.openDialog(url, 550, 360);
	}
};
RssCommand.InsertRSSChannel = function(applicationName, channel, templateExtendURL, title) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRssChannel.shtml" +
					"?channel=" + channel +
					"&applicationName=" + applicationName +
					"&title=" + title +
					"&themeType=" + (themeType ?  themeType.value : "") +
					(templateExtendURL!="" && templateExtendURL!="null" ? "&templateExtendURL=" + StringUtils.utf8Encode(templateExtendURL) : "");
	DialogUtils.openDialog(dialogUrl, 680, 220, "", HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new RssCommand());

CounterCommand = function() {
	this.name = 'counter';
	this.title = '\u8BBF\u95EE\u6B21\u6570';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_counter.gif";
	this.showTitle = true;
};
CounterCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
CounterCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { 
	HtmlEditor.editor.saveUndoStep();
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
	if(!obj) {
		obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
		if(!obj) {
			alert('\u8BF7\u91CD\u65B0\u9009\u62E9\u63D2\u5165\u7684\u4F4D\u7F6E');
			return;
		}
	}
	obj.innerHTML = "<\u8BBF\u95EE\u6B21\u6570>";
	obj.id = "counter";
};
HtmlEditor.registerCommand(new CounterCommand());

SubPageCommand = function() {
	var themeType = document.getElementsByName("themeType")[0];
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSubPage.shtml" +
			  "?applicationName=" + document.getElementsByName("applicationName")[0].value +
			  "&pageName=" + document.getElementsByName("pageName")[0].value +
			  (themeType ? "&themeType=" + themeType.value : "");
	EditorDialogCommand.call(this, 'subPage', '\u9884\u7F6E\u9875\u9762', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_sub_page.gif", url, 550, 360);
	this.showTitle = true;
};
SubPageCommand.prototype = new EditorDialogCommand;
SubPageCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
if(document.getElementsByName("pageName")[0]) {
	HtmlEditor.registerCommand(new SubPageCommand());
}

TabstripCommand = function() {
	EditorMenuCommand.call(this, 'tabstrip', '\u9009\u9879\u5361', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_tabstrip.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
TabstripCommand.prototype = new EditorMenuCommand;
TabstripCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
TabstripCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	return [{id:'insertTabstripBody', title:'\u63D2\u5165\u6807\u7B7E\u9875', iconIndex:-1},
			{id:'insertTabstripButton', title:'\u8BBE\u7F6E\u4E3A\u9009\u9879', iconIndex:-1},
			{id:'insertTabstripButonList', title:'\u63D2\u5165\u9009\u9879\u5217\u8868', iconIndex:-1},
			{id:'insertTabstripMoreLink', title:'\u63D2\u5165"\u66F4\u591A"\u94FE\u63A5', iconIndex:-1}];
};
TabstripCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	var themeType = document.getElementsByName("themeType")[0];
	if(itemId=="insertTabstripBody") { 
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripBody.shtml", 470, 300, "", HtmlEditor.getDialogArguments());	
	}
	else if(itemId=="insertTabstripButton") { 
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,li,a,img");
		if(element==null) {
			alert("TAB\u9009\u9879\u4F4D\u7F6E\u4E0D\u6B63\u786E\uFF0C\u8BF7\u91CD\u65B0\u9009\u62E9TAB\u9009\u9879\u4F4D\u7F6E\u3002");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripButton.shtml", 600, 380, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertTabstripButonList") { 
		
		var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml" +
				  "?conatinsEmbedViewOnly=true" +
				  "&currentApplicationName=" + document.getElementsByName("applicationName")[0].value +
				  "&currentPageName=" + document.getElementsByName("pageName")[0].value +
				  "&script=" + StringUtils.utf8Encode("TabstripCommand.insertTabstripButonList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}')");
		DialogUtils.openDialog(url, 550, 360);
	}
	else if(itemId=="insertTabstripMoreLink") { //\u63D2\u5165"\u66F4\u591A"\u94FE\u63A5
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripMoreLink.shtml", 430, 180, "", HtmlEditor.getDialogArguments());
	}
};
TabstripCommand.insertTabstripButonList = function(applicationName, recordListName, title) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripButtonList.shtml" +
					"?recordListName=" + recordListName +
					(applicationName!="" ? "&applicationName=" + applicationName : "") +
					"&themeType=" + (themeType ?  themeType.value : "");
	DialogUtils.openDialog(dialogUrl, 720, 400, "", HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new TabstripCommand());

UserCommand = function() {
	EditorMenuCommand.call(this, 'user', '\u7528\u6237', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_login_user_action.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
UserCommand.prototype = new EditorMenuCommand;
UserCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { 
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
UserCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { 
	return [{id:'insertUserName', title:'\u63D2\u5165\u7528\u6237\u540D', iconIndex:-1},
			{id:'insertPortrait', title:'\u63D2\u5165\u5934\u50CF', iconIndex:-1},
			{id:'insertPoint', title:'\u63D2\u5165\u79EF\u5206', iconIndex:-1},
			{id:'insertLoginName', title:'\u63D2\u5165\u767B\u5F55\u7528\u6237\u540D', iconIndex:-1},
			{id:'insertDepartment', title:'\u63D2\u5165\u7528\u6237\u6240\u5728\u90E8\u95E8', iconIndex:-1},
			{id:'insertLogoutUrl', title:'\u63D2\u5165\u6CE8\u9500\u94FE\u63A5', iconIndex:-1},
			{id:'insertChangePasswordUrl', title:'\u63D2\u5165\u4FEE\u6539\u5BC6\u7801\u94FE\u63A5', iconIndex:-1},
			{id:'insertPersonalDataUrl', title:'\u63D2\u5165\u4FEE\u6539\u4E2A\u4EBA\u8D44\u6599\u94FE\u63A5', iconIndex:-1},
			{id:'insertTemplateUrl', title:'\u63D2\u5165\u6A21\u677F\u914D\u7F6E\u94FE\u63A5', iconIndex:-1}];
};
UserCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { 
	HtmlEditor.editor.saveUndoStep();
	var a = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a');
	if(!a) {
		a = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
	}
	a.id = "loginUserAction";
	if(itemId=="insertUserName") { 
		a.setAttribute("urn", "\u7528\u6237\u540D");
		a.innerHTML = "<\u7528\u6237\u540D>";
	}
	else if(itemId=="insertPortrait") { 
		a.setAttribute("urn", "\u5934\u50CF");
		if(a.innerHTML=="") {
			a.innerHTML = "<\u5934\u50CF>";
		}
	}
	else if(itemId=="insertPoint") { 
		a.setAttribute("urn", "\u79EF\u5206");
		a.innerHTML = "<\u79EF\u5206>";
	}
	else if(itemId=="insertLoginName") { 
		a.setAttribute("urn", "\u767B\u5F55\u7528\u6237\u540D");
		a.innerHTML = "<\u767B\u5F55\u7528\u6237\u540D>";
	}
	else if(itemId=="insertDepartment") { 
		a.setAttribute("urn", "\u7528\u6237\u6240\u5728\u90E8\u95E8");
		a.innerHTML = "<\u7528\u6237\u6240\u5728\u90E8\u95E8>";
	}
	else if(itemId=="insertLogoutUrl") { 
		a.setAttribute("urn", "\u6CE8\u9500");
	}
	else if(itemId=="insertChangePasswordUrl") { 
		a.setAttribute("urn", "\u4FEE\u6539\u5BC6\u7801");
	}
	else if(itemId=="insertPersonalDataUrl") { 
		a.setAttribute("urn", "\u4FEE\u6539\u4E2A\u4EBA\u8D44\u6599");
	}
	else if(itemId=="insertTemplateUrl") { 
		a.setAttribute("urn", "\u6A21\u677F\u914D\u7F6E");
	}
	if(a.innerHTML=='') {
		a.innerHTML = a.getAttribute("urn");
	}
};
HtmlEditor.registerCommand(new UserCommand());

