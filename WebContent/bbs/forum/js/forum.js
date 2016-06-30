function selectDirectory(width, height, multiSelect, param, scriptEndSelect, key, separator, selectNodeTypes) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/bbs/forum/select.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (selectNodeTypes && selectNodeTypes!="" ? "&selectNodeTypes=" + selectNodeTypes : "");
   DialogUtils.openDialog(url, width, height);
}
function selectBbsUsers(typs, idField, nameField, describe, src) { //用户选择(多选):选择用户类型
	PopupMenu.popupMenu(typs, function(menuItemId, menuItemTitle) {
		switch(menuItemId) {
		case "系统用户":
			DialogUtils.selectPerson(600, 400, true, idField + "{id}," + nameField + "{loginName|" + describe + "|100%}");
			break;
		case "部门":
			DialogUtils.selectOrg(600, 400, true, idField + "{id}," + nameField + "{name|" + describe + "|100%}");
			break;
		case "角色":
			DialogUtils.selectRole(600, 400, true, idField + "{id}," + nameField + "{name|" + describe + "|100%}");
			break;
		case "网上注册用户":
			DialogUtils.openSelectDialog("jeaf/usermanage/member", "admin/member", 600, 400, true, idField + "{id}," + nameField + "{loginName|" + describe + "|100%}", "", "", "", ",", true);
			break;
		}
	}, src, 120, "right");
}