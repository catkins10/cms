function selectDirectory(width, height, multiSelect, param, scriptEndSelect, key, separator, rootDirectoryId) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/j2oa/databank/selectDirectory.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += (rootDirectoryId && rootDirectoryId!="" ? "&parentNodeId=" + rootDirectoryId : "");
   url += "&selectNodeTypes=directory";
   DialogUtils.openDialog(url, width, height);
}