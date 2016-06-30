function EAISelect(width, height, multiSelect, param, scriptEndSelect, separator, selectNodeTypes) {
   var url = RequestUtils.getContextPath() + "/eai/select.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += "&selectNodeTypes=" + (selectNodeTypes && selectNodeTypes!="" ?  selectNodeTypes : "application"); //默认选择应用
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   DialogUtils.openDialog(url, width, height);
}