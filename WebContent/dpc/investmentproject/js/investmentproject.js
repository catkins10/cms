//选择行业
function selectIndustry(width, height, multiSelect, param, scriptEndSelect, types, key, separator, leafNodeOnly) {
   var url = RequestUtils.getContextPath() + "/dpc/investmentproject/selectIndustry.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (types && types!="" ? "&selectNodeTypes=" + types : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
}