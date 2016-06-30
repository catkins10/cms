<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertRssChannel">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.id!="rssLink") {
				return;
			}
			var siteNames = obj.innerText;
			var properties = obj.getAttribute("urn");
			//显示扩展属性
			try {
				window.frames['dialogExtendFrame'].showExtendProperties(StringUtils.getPropertyValue(properties, "extendProperties"), obj.innerHTML)
			}
			catch(e) {
				
			}
			//解析站点ID
			document.getElementsByName("relationSiteId")[0].value = StringUtils.getPropertyValue(properties, "siteId");
			//解析站点名称
			document.getElementsByName("relationSiteName")[0].value = StringUtils.getPropertyValue(properties, "siteName");
			if(document.getElementsByName("relationSiteId")[0].value!="-1") { //不是当前站点
				document.getElementsByName("relationSite")[1].checked = true;
				relationSiteModeChanged("specialSite");
			}
			document.getElementsByName("ttl")[0].value = StringUtils.getPropertyValue(properties, "ttl");
			//解析打开方式
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
		}
		function doOk() {
			//站点ID
			var siteId = document.getElementsByName("relationSite")[0].checked ? "-1" : document.getElementsByName("relationSiteId")[0].value;
			var siteName = document.getElementsByName("relationSiteName")[0].value;
			if(siteId!="-1" && siteName=="") {
				alert("栏目不能为空");
				return;
			}
			var ttl = Number(document.getElementsByName("ttl")[0].value);
			if(ttl + "" == "NaN") {
				alert("更新周期不正确");
				return;
			}
			var extendProperties = "";
			try {
				extendProperties = window.frames['dialogExtendFrame'].getExtendPropertiesAsText();
			}
			catch(e) {
				
			}
			if(extendProperties=="ERROR") {
				return false;
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
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				var elementTitle = "";
				try {
					elementTitle = !document.getElementById('dialogExtendFrame') ? "" : window.frames['dialogExtendFrame'].getElementTitle(false, true);
				}
				catch(e) {
				
				}
				obj.innerHTML = elementTitle=="" ? "<%=request.getParameter("title")%>" : elementTitle;
			}
			obj.id = "rssLink";
			obj.setAttribute("urn", (extendProperties=="" ? "" : "extendProperties=" + StringUtils.encodePropertyValue(extendProperties) + "&") +
					  "applicationName=<%=request.getParameter("applicationName")%>" +
					  "&channel=<%=request.getParameter("channel")%>" +
					  "&siteId=" + siteId + 
					  "&siteName=" + siteName + 
					  "&ttl=" + ttl +
					  "&openMode=" + getLinkOpenMode());
			DialogUtils.closeDialog();
		}
		function relationSiteModeChanged(mode) {
			document.getElementById("selectedRelationSite").style.display = (mode=="currentSite" ? "none" : "");
		}
	</script>
	<table id="mainTable" border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr>
			<td nowrap="nowrap" align="right">更新周期(分钟)：</td>
			<td colspan="2" width="100%"><ext:field property="ttl"/></td>
		</tr>
		<tr height="25px">
			<td nowrap="nowrap" align="right">隶属站点：</td>
			<td nowrap="nowrap" width="160px"><ext:field property="relationSite" onclick="relationSiteModeChanged(id)"/></td>
			<td id="selectedRelationSite" width="100%" style="display:none"><ext:field property="relationSiteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">打开方式：</td>
			<td colspan="2"><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
	<ext:notEmpty property="templateExtendURL">
		<iframe name="dialogExtendFrame" id="dialogExtendFrame" src="<%=request.getContextPath()%><ext:write property="templateExtendURL"/>" frameborder="0" style="width:100%;height: 0px"></iframe>
	</ext:notEmpty>
</ext:form>