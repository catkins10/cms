//关闭按钮
var CloseCommand = function() {
	this.name = 'close';
	this.title = '关闭';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_close.gif";
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
CloseCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
CloseCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var dialog = (document.parentWindow ? document.parentWindow : document.defaultView).frameElement;
	if(!dialog) {
		window.top.close();
	}
	else {
		(dialog.ownerDocument.parentWindow ? dialog.ownerDocument.parentWindow : dialog.ownerDocument.defaultView).setTimeout('DialogUtils.closeDialog()', 1);
	}
};
HtmlEditor.registerCommand(new CloseCommand());

//保存命令
var SaveCommand = function(name, title, iconUrl, parameters) {
	this.name = name;
	this.title = title;
	this.iconUrl = iconUrl;
	this.showTitle = true;
	this.parameters = parameters;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
SaveCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
SaveCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var templateName = document.getElementsByName("templateName")[0].value;
	if(templateName=="") {
		alert("模板名称不能为空");
		return false;
	}
	FormUtils.doAction(SaveCommand.getActionName('save'), this.parameters);
};
//保存按钮
HtmlEditor.registerCommand(new SaveCommand('save', '保存', RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_save.gif"));
//设为默认模板按钮
HtmlEditor.registerCommand(new SaveCommand('saveAsDefaultTemplate', '设为默认', RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_set_default.gif", 'templateAction=setAsDefault'));

//获取模版操作名称
SaveCommand.getActionName = function(actionType) {
	var url = document.forms[0].action;
	url = url.substring(url.lastIndexOf('/') + 1);
	url = url.substring(0, url.lastIndexOf('.'));
	return actionType + url.substring(0, 1).toUpperCase() + url.substring(1);
};

//模板名称
var TemplateNameCommand = function() {
	this.name = 'templateName';
	this.title = '模板名称';
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
TemplateNameCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
TemplateNameCommand.prototype.create = function(toolbarButtonElement) {
	if(!document.getElementsByName("templateName")[0]) {
		return;
	}
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>名称</td>' +
									 '		<td><input onchange="document.getElementsByName(\'templateName\')[0].value=this.value;" value="' + document.getElementsByName("templateName")[0].value + '" type="text" style="width:120px;"></td>' +
									 '	</tr>' +
									 '</table>';
};
HtmlEditor.registerCommand(new TemplateNameCommand());

//上传模板按钮
HtmlEditor.registerCommand(new EditorDialogCommand('uploadTemplate', '上传', true, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_upload_page.gif", RequestUtils.getContextPath() + "/cms/templatemanage/dialog/uploadTemplate.shtml?id=" + document.getElementsByName("id")[0].value + "&siteId=" + document.getElementsByName("siteId")[0].value, 430, 180));

if(document.getElementsByName("act")[0].value!="create") { //不是新建
	//删除按钮
	var DeleteCommand = function() {
		this.name = 'delete';
		this.title = '删除';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_delete.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; //在源码模式下有效
	};
	DeleteCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	DeleteCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
		if(confirm('删除后不可恢复，是否确定删除？')) {
			FormUtils.doAction(SaveCommand.getActionName('delete'));
		}
	};
	HtmlEditor.registerCommand(new DeleteCommand());

	//复制模板按钮
	var CopyTemplateCommand = function() {
		this.name = 'copyTemplate';
		this.title = '复制';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_copy_template.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; //在源码模式下有效
	};
	CopyTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	CopyTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
		document.cookie = 'copiedTemplateId=' + document.getElementsByName("id")[0].value + ';path=/'; //复制的模板ID
		document.cookie = 'copiedTemplateName=' + document.getElementsByName("templateName")[0].value + ';path=/'; //复制的模板名称
	};
	HtmlEditor.registerCommand(new CopyTemplateCommand());
	
	//批量复制模板按钮
	if(document.getElementsByName("applicationName")[0] && document.getElementsByName("applicationName")[0].value!="cms/sitemanage") { //站点的模板不允许批量复制
		var BatchCopyTemplateCommand = function() {
			this.name = 'batchCopyTemplate';
			this.title = '批量复制';
			this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_copy_template.gif";
			this.showTitle = true;
			this.enabledInSourceCodeMode = true; //在源码模式下有效
		};
		BatchCopyTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
			return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
		};
		BatchCopyTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
			if(document.getElementsByName("batchCopyPageIds").length==0) {
				FormUtils.appendInput(document.body, 'hidden', 'batchCopyPageIds');
				FormUtils.appendInput(document.body, 'hidden', 'batchCopyPageNames');
			}
			var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectPage.shtml" +
					  "?multiSelect=true" +
					  "&currentApplicationName=" + document.getElementsByName("applicationName")[0].value +
					  "&param=" + StringUtils.utf8Encode("batchCopyPageIds{id},batchCopyPageNames{name|页面名称|100%}") +
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
	
	//导出按钮
	var ExportTemplateCommand = function() {
		this.name = 'export';
		this.title = '导出';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_export.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; //在源码模式下有效
	};
	ExportTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	ExportTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
		window.location = RequestUtils.getContextPath() + "/cms/templatemanage/exportTemplate.shtml?id=" + document.getElementsByName("id")[0].value;
	};
	HtmlEditor.registerCommand(new ExportTemplateCommand());
		
	//模板还原按钮
	var RestoreCommand = function() {
		this.name = 'restore';
		this.title = '还原';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_restore.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; //在源码模式下有效
	};
	RestoreCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	RestoreCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
		if(confirm('是否确定还原模板？')) {
			FormUtils.doAction(SaveCommand.getActionName('save'), 'templateAction=restore');
		}
	};
	HtmlEditor.registerCommand(new RestoreCommand());
}

//加载预置模板按钮
var LoadNormalTemplateCommand = function() {
	this.name = 'loadNormalTemplate';
	this.title = '预置模板';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_load_normal_template.gif";
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
LoadNormalTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
LoadNormalTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	if(confirm('加载预置模板会覆盖当前模板且不可恢复，是否确定加载？')) {
		FormUtils.doAction(SaveCommand.getActionName('save'), 'templateAction=loadNormal');
	}
};
HtmlEditor.registerCommand(new LoadNormalTemplateCommand());

//粘帖模板按钮
var copiedTemplateId = CookieUtils.getCookie('copiedTemplateId');
if(copiedTemplateId && copiedTemplateId!=document.getElementsByName("id")[0].value) {
	var PasteTemplateCommand = function() {
		this.name = 'pasteTemplate';
		this.title = '粘帖';
		this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_paste_template.gif";
		this.showTitle = true;
		this.enabledInSourceCodeMode = true; //在源码模式下有效
	};
	PasteTemplateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
		return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
	};
	PasteTemplateCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
		var copiedTemplateId = CookieUtils.getCookie('copiedTemplateId');
		if(copiedTemplateId==document.getElementsByName("id")[0].value) {
			alert('不能粘帖模板自己');
			return;
		}
		if(confirm('您要粘帖的模式是：' + CookieUtils.getCookie('copiedTemplateName') + '，粘帖模板会覆盖当前模板且不可恢复，是否确定粘帖？')) {
			FormUtils.doAction(SaveCommand.getActionName('save'), 'templateAction=copy&copiedTemplateId=' + copiedTemplateId);
		}
	};
	HtmlEditor.registerCommand(new PasteTemplateCommand());
}

//编辑局部源代码
HtmlEditor.registerCommand(new EditorDialogCommand('editElementHtml', '局部源码', true, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_edit_element_html.gif", RequestUtils.getContextPath() + "/cms/templatemanage/dialog/editElementHtml.shtml", 600, 380));

//插入子模板
HtmlEditor.registerCommand(new EditorDialogCommand('insertSubTemplate', '子模板', true, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_sub_template.gif", RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSubTemplate.shtml", 600, 400));