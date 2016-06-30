<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSiteLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.id!="siteLink") {
				return;
			}
			var properties = obj.getAttribute("urn");
			//解析站点名称
			document.getElementsByName("siteName")[0].value = StringUtils.getPropertyValue(properties, "siteName");
			//解析站点ID
			document.getElementsByName("siteId")[0].value = StringUtils.getPropertyValue(properties, "siteId");
			if(document.getElementsByName("siteId")[0].value=="-2") { //站点
				document.getElementsByName("siteSelect")[0].checked = true;
				siteSelectChanged("site");
			}
			else if(document.getElementsByName("siteId")[0].value=="-1") { //父栏目
				document.getElementsByName("siteSelect")[1].checked = true;
				siteSelectChanged("parent");
			}
			else if("true"==StringUtils.getPropertyValue(properties, "linkByName")) {
				document.getElementsByName("siteSelect")[3].checked = true;
			}
			//解析打开方式
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
		}
		function doOk() {
			//站点名称
			var siteName = document.getElementsByName("siteName")[0].value;
			if(document.getElementsByName("siteSelect")[0].checked) {
				siteName = "<站点>";
			}
			else if(document.getElementsByName("siteSelect")[1].checked) {
				siteName = "<父栏目>";
			}
			if(siteName=='') {
				alert("栏目不能为空");
				return;
			}
			//站点ID
			var siteId;
			if(document.getElementsByName("siteSelect")[0].checked) {
				siteId = "-2";
			}
			else if(document.getElementsByName("siteSelect")[1].checked) {
				siteId = "-1";
			}
			else {
			 	siteId = document.getElementsByName("siteId")[0].value;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>" || obj.innerHTML=='&lt;父栏目&gt;' || obj.innerHTML=='&lt;站点&gt;') {
				obj.innerHTML = siteName;
			}
			obj.id = "siteLink";
			obj.setAttribute("urn", "siteName=" + (siteName.substring(0,1)=="<" ? "" : siteName) +
					  "&siteId=" + siteId +
					  "&openMode=" + getLinkOpenMode() +
					  (document.getElementsByName("siteSelect")[3].checked ? "&linkByName=true" : ""));
			DialogUtils.closeDialog();
		}
		function siteSelectChanged(mode) {
			document.getElementById("selectedSite").style.display = (mode=="column" || mode=="columnByName"  ? "" : "none");
			DialogUtils.adjustDialogSize();
		}
		function selectLinkSite() {
			var siteSelects = document.getElementsByName("siteSelect");
			selectSite(500, 300, false, 'siteId{id},siteName{name}', '', '', ',', 'column,viewReference,site', (siteSelects[3].checked ? dialogArguments.editor.document.getElementsByName("siteId")[0].value : ''));
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">栏目选择：</td>
			<td><ext:field property="siteSelect" onclick="siteSelectChanged(value)"/></td>
		</tr>
		<tr id="selectedSite">
			<td></td>
			<td><ext:field property="siteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>