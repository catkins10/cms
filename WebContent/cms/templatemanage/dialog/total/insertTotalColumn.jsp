<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertTotals">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var opener = DialogUtils.getDialogOpener();
			document.getElementsByName("totalTitle")[0].value = opener.totalTitle;
			document.getElementsByName("totalLink")[0].value = opener.totalLink;
		}
		function doOk() {
			var opener = DialogUtils.getDialogOpener();
			var totalTitle = document.getElementsByName("totalTitle")[0].value;
			if(totalTitle=="") {
				alert("显示标题不能为空");
				return false;
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
			opener.doAddTotalColumn(totalTitle, opener.totalApplication, opener.totalName, document.getElementsByName("totalLink")[0].value, extendProperties);
			DialogUtils.closeDialog();
		}
	</script>
	<table id="mainTable" border="0" width="100%" cellspacing="5px" cellpadding="0">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">显示名称：</td>
			<td><ext:field property="totalTitle"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">链接地址：</td>
			<td><ext:field property="totalLink"/></td>
		</tr>
	</table>
<%	if(request.getParameter("templateExtendURL")!=null) { %>
		<iframe name="dialogExtendFrame" id="dialogExtendFrame" src="<%=request.getContextPath() + request.getParameter("templateExtendURL")%>" frameborder="0" style="width:100%;height: 0px"></iframe>
<%	} %>
</ext:form>