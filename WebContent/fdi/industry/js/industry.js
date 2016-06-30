//选择行业
function selectIndustry(width, height, multiSelect, param, scriptEndSelect, key, separator, leafNodeOnly, editabled, readabled) {
   var url = RequestUtils.getContextPath() + "/fdi/industry/admin/selectIndustry.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   url += (editabled ? "&editabled=true" : "");
   url += (readabled ? "&readabled=true" : "");
   DialogUtils.openDialog(url, width, height);
}