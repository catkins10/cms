function reference(src, urlFieldName, titleFieldName, scriptEndSelect, align, attachmentSelectUrl) {
	//引入脚本 
	if(!document.getElementById("jsSiteResource")) {
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/cms/siteresource/js/siteresource.js", "jsSiteResource");
	}
	if(!document.getElementById("jsInfoPublic")) {
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/cms/infopublic/js/infopublic.js", "jsInfoPublic");
	}
	if(!document.getElementById("jsOnlineService")) {
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/cms/onlineservice/js/onlineservice.js", "jsOnlineService");
	}
	if(!document.getElementById("jsInquiry")) {
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/cms/inquiry/js/inquiry.js", "jsInquiry");
	}
	if(!document.getElementById("jsScene")) {
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/cms/scene/js/scene.js", "jsScene");
	}
	if(!document.getElementById("jsSite")) {
		ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/cms/sitemanage/js/site.js", "jsSite");
	}
	//选择目标类型
	PopupMenu.popupMenu((attachmentSelectUrl && attachmentSelectUrl!="" ? "附件\0" : "") + "文章\0政府信息\0办事指南\0在线调查\0场景服务\0站点/栏目", function(menuItemId, menuItemTitle) {
		window.urlFieldName = urlFieldName;
		window.titleFieldName = titleFieldName;
		var param = urlFieldName + '{url}' + (titleFieldName ? ',' + titleFieldName + '{name}' : '') ;
		switch(menuItemId) {
		case "附件":
			var scriptRunAfterSelect = 'afterSelectReferenceAttachment("{URL}", "' + urlFieldName + '", "' + titleFieldName + '")';
			DialogUtils.openDialog(RequestUtils.getContextPath() + attachmentSelectUrl + (attachmentSelectUrl.indexOf('?')==-1 ? '?' : '&') + 'attachmentSelector.scriptRunAfterSelect=' + StringUtils.utf8Encode(scriptRunAfterSelect), 640, 400);
			break;
			
		case "文章":
			selectResource(560, 350, false, param, scriptEndSelect);
			break;
			
		case "政府信息":
			selectInfo(560, 350, false, param, scriptEndSelect);
			break;
			
		case "办事指南":
			selectOnlineServiceItem(560, 350, false, param, scriptEndSelect);
			break;
		
		case "在线调查":
			selectInquirySubject(560, 350, false, param, scriptEndSelect);
			break;
			
		case "场景服务":
			selectScene(560, 350, false, param, scriptEndSelect, '', '', 'service');
			break;
		
		case "站点/栏目":
			selectSite(560, 350, false, param, 'FormUtils.getField(document, window.urlFieldName).value=RequestUtils.getContextPath() + "/cms/sitemanage/index.shtml?siteId={id}";' + (titleFieldName ? 'document.getElementsByName(window.titleFieldName)[0].value="{name}";' : '') + (scriptEndSelect && scriptEndSelect!='' ? scriptEndSelect : ''));
			break;
		}
	}, src, 120, align ? align : "right");
}
function afterSelectReferenceAttachment(url, urlFieldName, titleFieldName) {
	FormUtils.getField(document, urlFieldName).value = url;
	var titleField = FormUtils.getField(document, titleFieldName);
	if(titleField.value=='') {
		var title = StringUtils.utf8Decode(url.substring(url.lastIndexOf('/') + 1));
		var index = title.lastIndexOf('.');
		titleField.value = index==-1 ? title : title.substring(0, index); //设置名称
	}
}