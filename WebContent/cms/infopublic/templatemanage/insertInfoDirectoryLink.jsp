<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertInfoDirectoryLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.id=="infoDirectoryLink") {
				var siteNames = obj.innerText;
				var properties = obj.getAttribute("urn");
				//解析目录名称
				document.getElementsByName("directoryName")[0].value = StringUtils.getPropertyValue(properties, "directoryName");
				//解析目录ID
				document.getElementsByName("directoryId")[0].value = StringUtils.getPropertyValue(properties, "directoryId");
				//解析打开方式
				setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
				//解析是否按名称
				var isLinkByName = "true"==StringUtils.getPropertyValue(properties, "linkByName");
				document.getElementsByName("linkByName")[0].checked = isLinkByName;
				onClickLinkByName(isLinkByName);
				//设置隶属站点
				var siteId = StringUtils.getPropertyValue(properties, "siteId");
				var siteName = StringUtils.getPropertyValue(properties, "siteName");
				if(siteId && siteId!="" && siteId!="-1") {
					document.getElementsByName("relationSite")[1].checked = true;
					relationSiteModeChanged("specialSite");
					document.getElementsByName("siteId")[0].value = siteId;
					document.getElementsByName("siteName")[0].value = siteName;
				}
			}
		}
		function doOk() {
			//目录名称
			var directoryName = document.getElementsByName("directoryName")[0].value;
			if(directoryName=='') {
				alert("目录不能为空");
				return;
			}
			//目录ID
			var directoryId = document.getElementsByName("directoryId")[0].value;

			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
				if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
					obj.innerHTML = directoryName;
				}
			}
			obj.id = "infoDirectoryLink";
			obj.setAttribute("urn", "directoryName=" + directoryName +
					  "&directoryId=" + directoryId + 
					  "&openMode=" + getLinkOpenMode() +
					  (document.getElementsByName("linkByName")[0].checked ? "&linkByName=true" : "") +
					  (document.getElementsByName("relationSite")[0].checked ? "&siteId=-1" : "&siteId=" + document.getElementsByName("siteId")[0].value + "&siteName=" + document.getElementsByName("siteName")[0].value));
			DialogUtils.closeDialog();
		}
		function onClickLinkByName(checked) {
			document.getElementById("trSite").style.display = checked ? "" : "none";
			DialogUtils.adjustDialogSize();
		}
		function relationSiteModeChanged(mode) {
			document.getElementById("selectedRelationSite").style.display = (mode=="currentSite" ? "none" : "");
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">目录选择：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%"><ext:field property="directoryName"/></td>
						<td nowrap="nowrap"><ext:field onclick="onClickLinkByName(checked)" property="linkByName"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="trSite" style="display:none">
			<td nowrap="nowrap">隶属站点：</td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="160px" nowrap="nowrap"><ext:field onclick="relationSiteModeChanged(value)" property="relationSite"/></td>
						<td id="selectedRelationSite" style="display:none"><ext:field property="siteName"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>