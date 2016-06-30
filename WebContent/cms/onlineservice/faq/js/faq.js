//选择常见问题
function selectFaq(width, height, multiSelect, param, scriptEndSelect, key, separator, parentDirectoryId, anonymousAlways) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/cms/onlineservice/faq/selectFaq.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += "&selectNodeTypes=faq";
   url += (parentDirectoryId ? "&parentNodeId=" + parentDirectoryId : "");
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