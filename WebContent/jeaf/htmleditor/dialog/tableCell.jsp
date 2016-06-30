<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/tableCell">
<script>
	window.onload = function() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var tableCell = DomUtils.getParentNode(dialogArguments.selectedElement, 'td');
		document.getElementsByName('width')[0].value = tableCell.width;
		document.getElementsByName('height')[0].value = tableCell.height;
		if(tableCell.borderColor) {
			document.getElementsByName('borderColor')[0].value = tableCell.borderColor;
		}
		if(tableCell.bgColor) {
			document.getElementsByName('backgroundColor')[0].value = tableCell.bgColor;
		}
		document.getElementsByName('nowrap')[0].checked = tableCell.noWrap;
		DropdownField.setValue('align', tableCell.align);
		DropdownField.setValue('valign', tableCell.vAlign);
	}
	function doOK() {
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.saveUndoStep();
		var tableCell = DomUtils.getParentNode(dialogArguments.selectedElement, 'td');
		//设置宽度和高度
		tableCell.width = document.getElementsByName('width')[0].value;
		tableCell.height = document.getElementsByName('height')[0].value;
		
		//设置边框颜色、背景颜色
		var borderColor = document.getElementsByName('borderColor')[0].value;
		if(borderColor=="") {
			tableCell.removeAttribute("borderColor");
		}
		else {
			tableCell.setAttribute("borderColor", borderColor);
		}
		var backgroundColor = document.getElementsByName('backgroundColor')[0].value;
		if(backgroundColor=="") {
			tableCell.removeAttribute("bgColor");
		}
		else {
			tableCell.setAttribute("bgColor", backgroundColor);
		}
		
		//设置对齐方式
		var align = document.getElementsByName('align')[0].value;
		if(align=="auto" || align=="") {
			tableCell.removeAttribute("align");
		}
		else {
			tableCell.align = align;
		}
		var valign = document.getElementsByName('valign')[0].value;
		if(valign=="auto" || valign=="") {
			tableCell.removeAttribute("valign");
		}
		else {
			tableCell.vAlign = valign;
		}
		
		//设置是否自动换行
		if(document.getElementsByName('nowrap')[0].checked) {
			tableCell.noWrap = true;
		}
		else {
			tableCell.removeAttribute("nowrap");
		}
		DialogUtils.closeDialog();
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td nowrap="nowrap" align="right">宽度：</td>
		<td><ext:field property="width"/></td>
		<td nowrap="nowrap" align="right">高度：</td>
		<td><ext:field property="height"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">边框颜色：</td>
		<td><ext:field property="borderColor"/></td>
		<td nowrap="nowrap" align="right">背景颜色：</td>
		<td><ext:field property="backgroundColor"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">水平对齐：</td>
		<td><ext:field property="align"/></td>
		<td nowrap="nowrap" align="right">垂直对齐：</td>
		<td><ext:field property="valign"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">自动换行：</td>
		<td><ext:field property="nowrap"/></td>
		<td nowrap="nowrap" align="right"></td>
		<td></td>
	</tr>
</table>
</ext:form>