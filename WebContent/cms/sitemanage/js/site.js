//站点选择
function selectSite(width, height, multiSelect, param, scriptEndSelect, key, separator, selectNodeTypes, currentSiteId, anonymousAlways, displayRecentUsed) {
   var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectSite.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (currentSiteId && currentSiteId!="" ? "&parentNodeId=" + currentSiteId : "");
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += "&selectNodeTypes=" + (selectNodeTypes && selectNodeTypes!="" ?  selectNodeTypes : "column,site,viewReference");
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (anonymousAlways ? "&anonymousAlways=true" : "");
   url += (displayRecentUsed ? "&displayRecentUsed=true" : "");
   DialogUtils.openDialog(url, width, height);
}

//设置头版头条
function setHeadline(directoryIds, headlineName, headlineURL, summarize) {
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/headline.shtml";
	url += "?directoryIds=" + directoryIds;
	document.cookie = "headlineName=" + StringUtils.utf8Encode(headlineName) + ";path=/";
	if(summarize && summarize!="") {
		if(summarize.length>100) {
			summarize = summarize.substring(0, 100) + "...";
		}
		document.cookie = "summarize=" + StringUtils.utf8Encode(summarize) + ";path=/";
	}
	document.cookie = "headlineURL=" + StringUtils.utf8Encode(headlineURL) + ";path=/";
	DialogUtils.openDialog(url, 470, 260);
}

//选择页面
function selectPage(width, height, multiSelect, param, scriptEndSelect, currentApplicationName, sitePageOnly, siteId, internalPageOnly, userId, advertPutSupportOnly) {
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectPage.shtml";
	url += "?multiSelect=" + multiSelect; 
	url += "&selectNodeTypes=page";
	url += "&param=" + StringUtils.utf8Encode(param);
	url += (currentApplicationName && currentApplicationName!="" ? "&currentApplicationName=" + currentApplicationName: ""); 
	url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
	url += (sitePageOnly ? "&sitePageOnly=true" : "");
	url += (siteId ? "&siteId=" + siteId : "");
	url += (internalPageOnly ? "&internalPageOnly=true" : "");
	url += (userId ? "&userId=" + userId : "");
	url += (advertPutSupportOnly ? "&advertPutSupportOnly=true" : "");
	DialogUtils.openDialog(url, width, height);
}

//选择站点的页面
function selectSitePage(width, height, param, scriptEndSelect, currentApplicationName, siteId) {
  selectPage(width, height, false, param, scriptEndSelect, currentApplicationName, true, siteId, false, 0, false);
}

//选择后台管理的页面
function selectInternalPage(width, height, param, scriptEndSelect, currentApplicationName, userId) {
   selectPage(width, height, false, param, scriptEndSelect, currentApplicationName, false, 0, true, userId, false);
}

//选择视图
function selectView(width, height, scriptEndSelect, currentApplicationName, currentPageName, rssChannelSupportOnly, totalSupportOnly, navigatorSupportOnly, conatinsEmbedViewOnly, siteReferenceSupportOnly) {
   var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml";
   url += "?selectNodeTypes=view";
   url += (currentApplicationName && currentApplicationName!="" ? "&currentApplicationName=" + currentApplicationName: ""); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (currentPageName && currentPageName!="" ? "&currentPageName=" + currentPageName : "");
   url += (rssChannelSupportOnly ? "&rssChannelSupportOnly=" + rssChannelSupportOnly : "");
   url += (totalSupportOnly ? "&totalSupportOnly=" + totalSupportOnly : "");
   url += (navigatorSupportOnly ? "&navigatorSupportOnly=" + navigatorSupportOnly : "");
   url += (conatinsEmbedViewOnly ? "&conatinsEmbedViewOnly=" + conatinsEmbedViewOnly : "");
   url += (siteReferenceSupportOnly ? "&siteReferenceSupportOnly=" + siteReferenceSupportOnly : "");
   DialogUtils.openDialog(url, width, height);
}

//选择链接
function selectSiteLink(width, height, scriptEndSelect, currentApplicationName, currentPageName) {
   var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectSiteLink.shtml";
   url += "?selectNodeTypes=link";
   url += (currentApplicationName && currentApplicationName!="" ? "&currentApplicationName=" + currentApplicationName: ""); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += (currentPageName && currentPageName!="" ? "&currentPageName=" + currentPageName : "");
   DialogUtils.openDialog(url, width, height);
}

//选择表单
function selectForm(width, height, scriptEndSelect, currentApplicationName) {
   var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectForm.shtml";
   url += "?selectNodeTypes=form";
   url += (currentApplicationName && currentApplicationName!="" ? "&currentApplicationName=" + currentApplicationName: ""); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   DialogUtils.openDialog(url, width, height);
}

//统计:显示站点资源列表
function listResources(status, categories, searchConditions, siteIds, orgId, unitName, statisticFieldNames) {
	var param = 'status=' + status +
				'&categories=' + StringUtils.utf8Encode(categories) +
				'&searchConditions=' + StringUtils.utf8Encode(searchConditions) +
				'&siteIds=' + siteIds +
				'&orgId=' + orgId +
				'&unitName=' + StringUtils.utf8Encode(unitName) +
				'&statisticFieldNames=' + StringUtils.utf8Encode(statisticFieldNames);
	DialogUtils.openSelectDialog('cms/stat', 'siteResource', '80%', '80%', false, '', 'PageUtils.editrecord("cms/siteresource", "admin/{resourceType}", "{id}", "mode=fullscreen")', '', '', '', true, param);
}