<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertRssSubscribeLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.id!="rssSubscribeLink") {
				return;
			}
			var siteNames = obj.innerText;
			var properties = obj.getAttribute("urn");
			//解析站点名称
			document.getElementsByName("siteName")[0].value = StringUtils.getPropertyValue(properties, "siteName");
			//解析站点ID
			var siteId = StringUtils.getPropertyValue(properties, "siteId");
			document.getElementsByName("siteId")[0].value = siteId;
			if(siteId=="-1" || siteId=="-2") { //父栏目/当前栏目
				document.getElementsByName("siteSelect")[siteId=="-1" ? 1 : 0].checked = true;
				siteSelectChanged(siteId=="-1" ? "parent" : "current");
			}
			//解析打开方式
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
		}
		function doOk() {
			var siteSelect = document.getElementsByName("siteSelect");
			var siteName; //站点名称
			var siteId; //站点ID
			if(siteSelect[0].checked) {
				siteName = "<当前栏目RSS订阅>";
				siteId = "-2";
			}
			else if(siteSelect[1].checked) {
				siteName = "<父栏目RSS订阅>";
				siteId = "-1";
			}
			else {
				siteName = document.getElementsByName("siteName")[0].value;
				siteId = document.getElementsByName("siteId")[0].value;
			}
			if(siteName=='') {
				alert("栏目不能为空");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>" || obj.innerHTML=='&lt;当前栏目RSS订阅&gt;' || obj.innerHTML=='&lt;父栏目RSS订阅&gt;') {
				obj.innerHTML = siteName;
			}
			obj.id = "rssSubscribeLink";
			obj.setAttribute("urn", "siteName=" + siteName + "&siteId=" + siteId + "&openMode=" + getLinkOpenMode());
			DialogUtils.closeDialog();
		}
		function siteSelectChanged(mode) {
			document.getElementById("selectedSite").style.display = (mode!="other" ? "none" : "");
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">栏目选择：</td>
			<td width="230px" nowrap="nowrap"><ext:field property="siteSelect" onclick="siteSelectChanged(value)"/></td>
			<td id="selectedSite" width="100%"><ext:field property="siteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td colspan="2" width="100%"><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>