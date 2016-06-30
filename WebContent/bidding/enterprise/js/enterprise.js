function selectEnterprise(width, height, multiSelect, param, scriptEndSelect, key, separator) {
   var url = RequestUtils.getContextPath() + "/bidding/enterprise/selectEnterprise.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += "&selectNodeTypes=enterprise";
   DialogUtils.openDialog(url, width, height);
}