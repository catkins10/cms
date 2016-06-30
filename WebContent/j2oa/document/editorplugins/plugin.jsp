<%@ page contentType="text/javascript; charset=UTF-8" %>

DocumentFieldCommand = function() {
	EditorMenuCommand.call(this, 'documentField', '字段', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_field.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
DocumentFieldCommand.prototype = new EditorMenuCommand;
DocumentFieldCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
DocumentFieldCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	var items = [];
<%	java.util.List fields = com.yuanluesoft.jeaf.business.util.FieldUtils.listRecordFields((String)request.getParameter("recordClassName"), null, "attachment", null, "hidden", false, true, false, false, 0);
	for(int i=0; i<fields.size(); i++) {
		com.yuanluesoft.jeaf.business.model.Field field = (com.yuanluesoft.jeaf.business.model.Field)fields.get(i);
		out.println("items.push({id:'" + field.getTitle() + "', title:'" + field.getTitle() + "', iconIndex:-1});");
	} %>
	return items;
};
DocumentFieldCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	HtmlEditor.editor.saveUndoStep();
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
	if(!obj) {
		obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
		if(!obj) {
			alert("请重新选择插入位置。");
			return;
		}
	}
	obj.id = "field";
	obj.innerHTML = "<" + itemId + ">";
};
HtmlEditor.registerCommand(new DocumentFieldCommand());