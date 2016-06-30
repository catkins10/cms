<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSiteSearchForm">
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		var form = DomUtils.getParentNode(dialogArguments.selectedElement, 'form');
		if(form && (form.id=="quickSiteSearch" || form.id=="siteSearch")) {
			//从form.action解析搜索站点
			var siteId = StringUtils.getPropertyValue(form.action, "siteId");
			var siteName = StringUtils.getPropertyValue(form.action, "siteName");
			if(!siteId || siteId=="") {
				var values = form.target.split("|"); //从target解析,保持和旧系统兼容
				if(values.length==2) {
					siteId = values[1];
					siteName = values[0];
				}					
			}
		 	if(siteId!="-1") {
	 			document.getElementsByName("siteName")[0].value = siteName;
	 			document.getElementsByName("siteId")[0].value = siteId;
	 			document.getElementsByName("siteSelect")[1].checked = true;
	 			document.getElementById("selectedSite").style.visibility = "visible";
	 		}
		}
	};
	function doOk() {
		var siteName;
		var siteId;
		if(document.getElementsByName("siteSelect")[0].checked) {
			siteName = "本站点/栏目";
			siteId = "-1";
		}
		else {
		 	siteName = document.getElementsByName("siteName")[0].value;
		 	siteId = document.getElementsByName("siteId")[0].value;
			if(siteName=='') {
				alert("站点列表不能为空");
				return false;
			}
		}
		dialogArguments.editor.window.setTimeout("FormCommand.doInsertForm('cms/sitemanage', '<%=("true".equals(request.getParameter("advanceSearch")) ? "siteSearch" : "quickSiteSearch")%>', '<%=("true".equals(request.getParameter("advanceSearch")) ? "高级搜索" : "搜索")%>', 'siteId=" + siteId + "&siteName=" + siteName + "')", 10);
		DialogUtils.closeDialog();
	}
	function siteSelectChanged(mode) {
		document.getElementById("selectedSite").style.visibility = (mode=="current" ? "hidden" : "visible");
	}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr valign="middle" style="<%=("true".equals(request.getParameter("searchResults")) ? "display:none" : "")%>">
			<td nowrap="nowrap">搜索栏目：</td>
			<td nowrap="nowrap" style="width:180px;"><ext:field property="siteSelect" onclick="siteSelectChanged(value)"/></td>
			<td id="selectedSite" style="visibility:hidden; width:100%"><ext:field property="siteName"/></td>
		</tr>
	</table>
</ext:form>