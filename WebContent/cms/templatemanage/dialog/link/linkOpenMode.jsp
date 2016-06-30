<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/templatemanage/dialog/link/js/linkmode.js"></script>
<table border="0" cellspacing="0" cellpadding="0px">
	<tr>
		<td width="100px"><ext:field property="linkOpenMode.openMode"/></td>
		<td id="linkDialogSizeArea" style="display:none">
			<table border="0" cellspacing="0" cellpadding="0px">
				<tr>
					<td>&nbsp;&nbsp;宽度：</td>
					<td width="38px"><ext:field property="linkOpenMode.linkDialogWidth"/></td>
					<td>&nbsp;&nbsp;高度：</td>
					<td width="38px"><ext:field property="linkOpenMode.linkDialogHeight"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>