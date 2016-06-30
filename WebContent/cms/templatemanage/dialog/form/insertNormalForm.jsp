<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertNormalForm">
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/template/js/insertPredefinedPage.js"></script>
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			if(!frames['pagePreview']) {
				previewPage(true);
			}
		}
		function doOk() {
			try {
				insertPredefinedPage(null, document.getElementsByName("cssUrl")[0].value);
			}
			catch(e) {
			
			}
			DialogUtils.closeDialog();
		}
	</script>
	<ext:empty property="predefinedPage">
		<div style="font-size:14px; line-height: 20px;">
			没有预置页面
		</div>
	</ext:empty>
	<ext:notEmpty property="predefinedPage">
		<table border="0" width="100%" cellspacing="0" cellpadding="3px">
			<tr>
				<td nowrap="nowrap" align="right">CSS：</td>
				<td width="100%"><ext:field onchange="previewPage(true)" property="cssUrl"/></td>
				<td nowrap="nowrap"><input type="button" class="button" value="自定义CSS" style="width: 70px" onclick="customCssFile(document.getElementsByName('cssUrl')[0].value)"></td>
			</tr>
			<tr>
				<td valign="top" nowrap="nowrap" align="right">预览：</td>
				<td colspan="2"><iframe name="pagePreview" frameBorder="0" class="frame" style="width:100%; height:280px;"></iframe></td>
			</tr>
		</table>
	</ext:notEmpty>
</ext:form>