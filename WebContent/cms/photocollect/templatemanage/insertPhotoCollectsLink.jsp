<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertPhotoCollectsLink">
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
		if(!obj) {
			return;
		}
		//设置隶属站点
		var urn = obj.getAttribute("urn");
		var relationSiteId =  StringUtils.getPropertyValue(urn, "siteId");
		var relationSiteName =  StringUtils.getPropertyValue(urn, "siteName");
		if(relationSiteId && relationSiteId!="" && relationSiteId!="-1") {
			document.getElementsByName("relationSite")[1].checked = true;
			relationSiteModeChanged("specialSite");
			document.getElementsByName("relationSiteId")[0].value = relationSiteId;
			document.getElementsByName("relationSiteName")[0].value = relationSiteName;
		}
		var photoCollectCategory = StringUtils.getPropertyValue(urn, "photoCollectCategory");
		if(photoCollectCategory && photoCollectCategory!="") {
			document.getElementsByName("photoCollectCategory")[0].value = StringUtils.utf8Decode(photoCollectCategory);
		}
		//解析打开方式
		setLinkOpenMode(obj.target);
	}
	function doOk() {
		dialogArguments.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
		if(!obj) {
			obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
			if(!obj) {
				return false;
			}
		}
		if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
			obj.innerHTML = '更多';
		}
		var photoCollectCategory = document.getElementsByName("photoCollectCategory")[0].value;
		obj.id = "systemLink";
		obj.href = "/cms/sitemanage/applicationIndex.shtml?applicationName=cms/photocollect&amp;pageName=photoCollects" + (photoCollectCategory=="" ? "" : "&photoCollectCategories=" + StringUtils.utf8Encode(photoCollectCategory));
		obj.target = getLinkOpenMode();
		obj.setAttribute("urn", (document.getElementsByName("relationSite")[0].checked ? "siteId=-1" : "siteId=" + document.getElementsByName("relationSiteId")[0].value) +
				  "&siteName=" + document.getElementsByName("relationSiteName")[0].value +
				  "&photoCollectCategory=" + photoCollectCategory);
		DialogUtils.closeDialog();
	}
	function relationSiteModeChanged(mode) {
		document.getElementById("selectedRelationSite").style.display = (mode=="currentSite" ? "none" : "");
	}
	//选择图片分类
	function selectPhotoCollectCategory() {
		var siteId = document.getElementsByName('relationSite')[0].checked ? dialogArguments.editor.document.getElementsByName('siteId')[0].value : document.getElementsByName('relationSiteId')[0].value;
		DialogUtils.openSelectDialog("cms/photocollect", "selectPhotoCollectCategory", 560, 360, true, "photoCollectCategory{type},photoCollectCategory{category|分类|100%}", "", "", "", ",", false, "siteId=" + siteId);
	}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td>隶属站点：</td>
			<td nowrap="nowrap" width="160px"><ext:field property="relationSite" onclick="relationSiteModeChanged(value)"/></td>
			<td id="selectedRelationSite" style="display:none"><ext:field property="relationSiteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">图片分类：</td>
			<td colspan="2"><ext:field property="photoCollectCategory"/></td>
		</tr>
		<tr valign="bottom">
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>