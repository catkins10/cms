function selectAdvert(width, height, multiSelect, param, scriptEndSelect, separator, currentSiteId, floatOnly, fixedOnly) {
   var url = RequestUtils.getContextPath() + "/cms/advert/selectAdvert.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (currentSiteId && currentSiteId!="" ? "&parentNodeId=" + currentSiteId : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += "&selectNodeTypes=advert";
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (floatOnly ? "&floatOnly=true" : "");
   url += (fixedOnly ? "&fixedOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
}