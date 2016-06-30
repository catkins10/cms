//导航栏命令
PagingCommand = function() {
	EditorMenuCommand.call(this, 'paging', '分页', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_page_action.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
PagingCommand.prototype = new EditorMenuCommand;
PagingCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
PagingCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'biddingRecordList', title:'绑定记录列表', iconIndex:-1},
			{id:'firstPage', title:'第一页', iconIndex:-1},
			{id:'previousPage', title:'上一页', iconIndex:-1},
			{id:'nextPage', title:'下一页', iconIndex:-1},
			{id:'lastPage', title:'最后一页', iconIndex:-1},
			{id:'pageNo', title:'页码', iconIndex:-1},
			{id:'recordCount', title:'记录总数', iconIndex:-1},
			{id:'pageCount', title:'总页数', iconIndex:-1},
			{id:'currentPageNo', title:'当前页码', iconIndex:-1},
			{id:'pageSwitch', title:'页面跳转输入框', iconIndex:-1},
			{id:'pageSwitchButton', title:'页面跳转按钮', iconIndex:-1}];
};
PagingCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if(itemId=="biddingRecordList") { //绑定记录列表
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a');
		if(!obj || obj.id!='recordList') {
			alert("未选定记录列表");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		//清除分页标记
		var elements = HtmlEditor.editorDocument.getElementsByTagName("A");
		for(var i=0; i<elements.length; i++) {
			var urn = elements[i].getAttribute("urn");
			if(urn && urn.indexOf('recordCount=')!=-1 && elements[i].target=='paging') {
				elements[i].removeAttribute("target");
			}
		}
		obj.target = 'paging';
		alert("绑定完成");
	}
	else if(itemId=="pageSwitch") { //页面跳转输入框
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
	else if(itemId=="pageSwitchButton") { //页面跳转按钮
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "button,span,div,input,a,image,li");
		if(!obj) {
			obj = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'button') ;
			obj.innerHTML = '转到';
		}
		DomUtils.setAttribute(obj, "name", "pageSwitchButton");
	}
	else if(itemId=="pageNo") { //页码
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertPageNo.shtml", 430, 220, "", HtmlEditor.getDialogArguments());
	}
	else {
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!obj) {
			obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(!obj) {
				alert('请重新选择插入的位置');
				return;
			}
		}
		var actionName;
		if(itemId=='firstPage') {
			actionName = '第一页';
		}
		else if(itemId=='previousPage') {
			actionName = '上一页';
		}
		else if(itemId=='nextPage') {
			actionName = '下一页';
		}
		else if(itemId=='lastPage') {
			actionName = '最后一页';
		}
		else if(itemId=='recordCount') {
			actionName = '记录总数';
		}
		else if(itemId=='pageCount') {
			actionName = '总页数';
		}
		else if(itemId=='currentPageNo') {
			actionName = '当前页码';
		}
		if(obj.innerHTML=='') {
			obj.innerHTML = "记录总数,当前页码,总页数".indexOf(actionName)!=-1 ? "<" + actionName + ">" : actionName;
		}
		obj.id = "pageAction";
		obj.setAttribute("urn", actionName);
	}
};
HtmlEditor.registerCommand(new PagingCommand());