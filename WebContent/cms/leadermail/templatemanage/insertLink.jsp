<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertLink">
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
			var leaderMailType = StringUtils.getPropertyValue(urn, "leaderMailType");
			if(leaderMailType && leaderMailType!="") {
				document.getElementsByName("leaderMailType")[0].value = StringUtils.utf8Decode(leaderMailType);
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
			var type = document.getElementsByName("type")[0].value;
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = type=='leaderMailLink' ? '写信' : '更多';
			}
			var leaderMailType = document.getElementsByName("leaderMailType")[0].value;
			obj.id = "systemLink";
			if(type=='leaderMailLink') {
				obj.href = "/cms/leadermail/leaderMail.shtml" + (leaderMailType=="" ? "" : "?type=" + StringUtils.utf8Encode(leaderMailType));
			}
			else {
				obj.href = "/cms/sitemanage/applicationIndex.shtml?applicationName=cms/leadermail&pageName=mails" + (leaderMailType=="" ? "" : "&leaderMailTypes=" + StringUtils.utf8Encode(leaderMailType));
			}
			obj.target = getLinkOpenMode();
			obj.setAttribute("urn", (document.getElementsByName("relationSite")[0].checked ? "siteId=-1" : "siteId=" + document.getElementsByName("relationSiteId")[0].value) +
					  "&siteName=" + document.getElementsByName("relationSiteName")[0].value +
					  "&leaderMailType=" + leaderMailType);
			DialogUtils.closeDialog();
		}
		function relationSiteModeChanged(mode) {
			document.getElementById("selectedRelationSite").style.display = (mode=="currentSite" ? "none" : "");
		}
		//选择信件类型
		function selectLeaderMailType() {
			var siteId = document.getElementsByName('relationSite')[0].checked ? dialogArguments.editor.document.getElementsByName('siteId')[0].value : document.getElementsByName('relationSiteId')[0].value;
			if(document.getElementsByName("type")[0].value=='complaintLink') {
				DialogUtils.openSelectDialog("cms/leadermail", "selectLeaderMailType", 560, 360, false, "leaderMailType{type}", "", "", "", ",", false, "siteId=" + siteId);
			}
			else {		
				DialogUtils.openSelectDialog("cms/leadermail", "selectLeaderMailType", 560, 360, true, "leaderMailType{type},leaderMailType{type|类型|100%}", "", "", "", ",", false, "siteId=" + siteId);
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td>隶属站点：</td>
			<td nowrap="nowrap" width="160px"><ext:field property="relationSite" onclick="relationSiteModeChanged(value)"/></td>
			<td id="selectedRelationSite" style="display:none"><ext:field property="relationSiteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">信件类型：</td>
			<td colspan="2"><ext:field property="leaderMailType"/></td>
		</tr>
		<tr valign="bottom">
			<td nowrap="nowrap">打开方式：</td>
			<td colspan="2"><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>