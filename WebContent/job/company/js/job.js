//行业选择
function selectIndustry(width, height, multiSelect, param, selectNodeTypes, scriptEndSelect, separator) {
	_selectParameter(1, width, height, multiSelect, param, selectNodeTypes, scriptEndSelect, separator);
}
//专业选择
function selectSpecialty(width, height, multiSelect, param, selectNodeTypes, scriptEndSelect, separator) {
	_selectParameter(2, width, height, multiSelect, param, selectNodeTypes, scriptEndSelect, separator)
}
//岗位选择
function selectPost(width, height, multiSelect, param, scriptEndSelect, separator) {
	_selectParameter(1, width, height, multiSelect, param, 'post', scriptEndSelect, separator)
}
function _selectParameter(rootDirectoryId, width, height, multiSelect, param, selectNodeTypes, scriptEndSelect, separator) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/job/company/selectParameter.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += "&selectNodeTypes=" + selectNodeTypes;
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += "&parentNodeId=" + rootDirectoryId;
   DialogUtils.openDialog(url, width, height);
}