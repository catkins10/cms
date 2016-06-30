function selectResource(width, height, multiSelect, param, scriptEndSelect, key, separator, selectNodeTypes, currentSiteId) {
   var url = RequestUtils.getContextPath() + "/cms/siteresource/selectResource.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (currentSiteId && currentSiteId!="" ? "&parentNodeId=" + currentSiteId : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += "&selectNodeTypes=" + (selectNodeTypes && selectNodeTypes!="" ? selectNodeTypes : "article");
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   DialogUtils.openDialog(url, width, height);
}