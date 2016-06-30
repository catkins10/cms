//选择所属区域
function selectArea(width, height, multiSelect, param, scriptEndSelect) {
   var url = RequestUtils.getContextPath() + "/dpc/keyproject/selectArea.shtml?title=" + StringUtils.utf8Encode('区域') + "&multiSelect=" + multiSelect + "&param=" + StringUtils.utf8Encode(param) +(scriptEndSelect ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   DialogUtils.openDialog(url, width, height);
}

//选择行业
function selectIndustry(width, height, multiSelect, param, scriptEndSelect, types, key, separator, leafNodeOnly) {
   var url = RequestUtils.getContextPath() + "/dpc/keyproject/selectIndustry.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (types && types!="" ? "&selectNodeTypes=" + types : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
}

//选择资金来源
function selectInvestSource(width, height, multiSelect, param, scriptEndSelect, types, key, separator, leafNodeOnly) {
   var url = RequestUtils.getContextPath() + "/dpc/keyproject/selectInvestSource.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (types && types!="" ? "&selectNodeTypes=" + types : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
}