<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertRecordLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj) {
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
				//解析打开方式
				setLinkOpenMode(obj.target);
				//设置链接选项
				var linkTitle = StringUtils.getPropertyValue(urn, "linkTitle");
				if(!linkTitle || linkTitle=="") {
					linkTitle = obj.getAttribute("urn");
				}
				DropdownField.setValue('linkTitle', linkTitle);
			}
		}
		function doOk() {
			var linkTitle = document.getElementsByName("linkTitle")[0].value;
			if(linkTitle=="") {
				alert('未选择链接');
				return;
			}
			if(dialogArguments.window==dialogArguments.editor.editorWindow) { //不是为记录列表插入字段
				dialogArguments.editor.saveUndoStep();
			}
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			//打开方式
			obj.target = getLinkOpenMode('linkOpenMode');
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = linkTitle;
			}
			obj.setAttribute("urn", (document.getElementsByName("relationSite")[0].checked ? "siteId=-1" : "siteId=" + document.getElementsByName("relationSiteId")[0].value + "&siteName=" + document.getElementsByName("relationSiteName")[0].value) +
					  "&linkTitle=" + linkTitle);
			obj.id = 'recordLink';
			obj.removeAttribute("href");
			DialogUtils.closeDialog();
		}
		function relationSiteModeChanged(mode) {
			document.getElementById("selectedRelationSite").style.display = (mode=="currentSite" ? "none" : "");
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">选择链接：</td>
			<td colspan="2"><ext:field property="linkTitle"/></td>
		</tr>
		<tr>
			<td>隶属站点：</td>
			<td width="160px" nowrap="nowrap"><ext:field property="relationSite" onclick="relationSiteModeChanged(value)"/></td>
			<td width="100%" id="selectedRelationSite" style="display:none;"><ext:field property="relationSiteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td colspan="2"><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
    </table>
</ext:form>