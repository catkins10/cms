<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSystemLink">
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
			//解析打开方式
			setLinkOpenMode(obj.target);
		}
		function doOk() {
			if(document.getElementsByName("relationSite")[1].checked && document.getElementsByName("relationSiteName")[0].value=="") {
				alert("未选择站点");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = "<%=request.getParameter("title")%>";
			}
			obj.id = "systemLink";
			obj.href = "<%=request.getParameter("url")%>";
			obj.target = getLinkOpenMode();
			obj.setAttribute("urn", (document.getElementsByName("relationSite")[0].checked ? "siteId=-1" : "siteId=" + document.getElementsByName("relationSiteId")[0].value + "&siteName=" + document.getElementsByName("relationSiteName")[0].value) +
					  "&applicationName=<%=request.getParameter("applicationName")%>" +
					  "&pageName=<%=request.getParameter("pageName")%>");
			DialogUtils.closeDialog();
		}
		function relationSiteModeChanged(mode) {
			document.getElementById("selectedRelationSite").style.display = (mode=="currentSite" ? "none" : "");
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">隶属站点：</td>
			<td width="160px" nowrap="nowrap"><ext:field onclick="relationSiteModeChanged(id)" property="relationSite"/></td>
			<td width="100%" id="selectedRelationSite" style="display:none"><ext:field property="relationSiteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td colspan="2" width="100%"><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>