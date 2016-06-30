function selectItemType(dialogTitle, width, height, multiSelect, param, scriptEndSelect, separator) {
   var url = RequestUtils.getContextPath() + "/cms/onlineservice/selectItemType.shtml" +
   			 "?title=" + StringUtils.utf8Encode(dialogTitle) +
   			 "&multiSelect=" + multiSelect +
   			 "&param=" + StringUtils.utf8Encode(param) +
   			 (scriptEndSelect ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "") +
   			 (separator && separator!="" ? "&separator=" + StringUtils.utf8Encode(separator) : "");
   DialogUtils.openDialog(url, width, height);
};

//选择网上办事目录
function selectOnlineServiceDirectory(width, height, multiSelect, param, scriptEndSelect, key, separator, selectNodeTypes, leafNodeOnly, anonymousAlways) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/cms/onlineservice/selectDirectory.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += "&selectNodeTypes=" + (selectNodeTypes && selectNodeTypes!="" ? selectNodeTypes : "all");
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   var siteId = StringUtils.getPropertyValue(window.location.href, "siteId", "");
   if(!siteId || siteId=='') {
       siteId = DomUtils.getMetaContent(document, "siteIdMeta");
   }
   if((!siteId || siteId=='') && document.getElementsByName("siteId")[0]) {
       siteId = document.getElementsByName("siteId")[0].value;
   }
   if(siteId && siteId!='') {
   	  url += "&siteId=" + siteId;
   }
   url += (anonymousAlways ? "&anonymousAlways=true" : "");
   DialogUtils.openDialog(url, width, height);
}
//选择办理事项
function selectOnlineServiceItem(width, height, multiSelect, param, scriptEndSelect, key, separator, filterDirectoryId, parentDirectoryId, acceptSupportOnly, complaintSupportOnly, consultSupportOnly, querySupportOnly, anonymousAlways) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/cms/onlineservice/selectServiceItem.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += "&selectNodeTypes=serviceItem";
   url += (filterDirectoryId ? "&filterDirectoryId=" + filterDirectoryId : "");
   url += (parentDirectoryId ? "&parentNodeId=" + parentDirectoryId : "");
   url += (acceptSupportOnly ? "&acceptSupportOnly=true" : "");
   url += (complaintSupportOnly ? "&complaintSupportOnly=true" : "");
   url += (consultSupportOnly ? "&consultSupportOnly=true" : "");
   url += (querySupportOnly ? "&querySupportOnly=true" : "");
   var siteId = StringUtils.getPropertyValue(window.location.href, "siteId", "");
   if(!siteId || siteId=='') {
       siteId = DomUtils.getMetaContent(document, "siteIdMeta");
   }
   if((!siteId || siteId=='') && document.getElementsByName("siteId")[0]) {
       siteId = document.getElementsByName("siteId")[0].value;
   }
   if(siteId && siteId!='') {
   	  url += "&siteId=" + siteId;
   }
   url += (anonymousAlways ? "&anonymousAlways=true" : "");
   DialogUtils.openDialog(url, width, height);
}