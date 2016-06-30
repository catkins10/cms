function selectScene(width, height, multiSelect, param, scriptEndSelect, key, separator, selectNodeTypes, parentNodeId) {
   var url = RequestUtils.getContextPath() + "/cms/scene/selectScene.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += "&selectNodeTypes=" + (!selectNodeTypes ? "service" : selectNodeTypes);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (parentNodeId ? "&parentNodeId=" + parentNodeId : "");
   DialogUtils.openDialog(url, width, height);
}