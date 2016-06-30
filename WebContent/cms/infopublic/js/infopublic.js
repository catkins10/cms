function selectDirectory(width, height, multiSelect, param, scriptEndSelect, key, separator, rootDirectoryId, selectNodeTypes, anonymousAlways, displayRecentUsed, displayDirectoryCode) {
   var url = RequestUtils.getContextPath() + "/cms/infopublic/selectDirectory.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += (rootDirectoryId && rootDirectoryId!="" ? "&parentNodeId=" + rootDirectoryId : "");
   url += "&selectNodeTypes=" + (selectNodeTypes && selectNodeTypes!="" ? selectNodeTypes : "all");
   url += (anonymousAlways ? "&anonymousAlways=true" : "");
   url += (displayRecentUsed ? "&displayRecentUsed=true" : "");
   url += (displayDirectoryCode ? "&displayDirectoryCode=true" : "");
   DialogUtils.openDialog(url, width, height);
}
function selectInfo(width, height, multiSelect, param, scriptEndSelect, key, separator, rootDirectoryId, selectNodeTypes, displayRecentUsed, displayDirectoryCode) {
   var url = RequestUtils.getContextPath() + "/cms/infopublic/selectInfo.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += (rootDirectoryId && rootDirectoryId!="" ? "&parentNodeId=" + rootDirectoryId : "");
   url += "&selectNodeTypes=" + (selectNodeTypes && selectNodeTypes!="" ? selectNodeTypes : "publicInfo,publicArticle");
   url += (displayRecentUsed ? "&displayRecentUsed=true" : "");
   url += (displayDirectoryCode ? "&displayDirectoryCode=true" : "");
   DialogUtils.openDialog(url, width, height);
}