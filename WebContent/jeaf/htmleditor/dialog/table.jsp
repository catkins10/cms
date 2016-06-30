<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/table">
<script>
	window.onload = function() {
		var dialogArguments = DialogUtils.getDialogArguments();
		if(document.getElementsByName('newTable')[0].value=='true') {
			return;
		}
		var table = DomUtils.getParentNode(dialogArguments.selectedElement, 'table');
		document.getElementsByName('rows')[0].value = table.rows.length;
		document.getElementsByName('cols')[0].value = table.rows[0].cells.length;
		document.getElementsByName('rows')[0].disabled = true;
		document.getElementsByName('cols')[0].disabled = true;
		document.getElementsByName('width')[0].value = table.width;
		document.getElementsByName('height')[0].value = table.height;
		document.getElementsByName('border')[0].value = table.border;
		document.getElementsByName('borderColor')[0].value = table.borderColor;
		document.getElementsByName('spacing')[0].value = table.cellSpacing;
		document.getElementsByName('padding')[0].value = table.cellPadding;
		DropdownField.setValue('align', table.align);
	}
	function doOK() {
		var rows = Math.floor(Number(document.getElementsByName('rows')[0].value));
		var cols = Math.floor(Number(document.getElementsByName('cols')[0].value));
		var newTable = document.getElementsByName('newTable')[0].value=='true';
		if(newTable && (isNaN(rows) || rows<=0)) {
			alert('行数不正确');
			return;
		}
		if(newTable && (isNaN(cols) || cols<=0)) {
			alert('列数不正确');
			return;
		}
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.saveUndoStep();
		var table;
		if(!newTable) {
			table = DomUtils.getParentNode(dialogArguments.selectedElement, 'table');
		}
		else {
			var id = Math.random();
			try {
				DomSelection.pasteHTML(dialogArguments.window, dialogArguments.range, '<table id="' + id + '" style="border-collapse:collapse;" border="' + document.getElementsByName('border')[0].value + '" borderColor="' + document.getElementsByName('borderColor')[0].value + '"></table>');
			}
			catch(e) {
				return;
			}
			table = dialogArguments.document.getElementById(id);
			table.removeAttribute("id");
			for(var i=0; i<rows; i++) {
				var row = table.insertRow(-1);
				for(var j=0; j<cols; j++) {
					row.insertCell(-1); //.innerHTML = '<img width="0" height="16">';
				}
			}
		}
		//设置宽度和高度
		table.width = document.getElementsByName('width')[0].value;
		table.height = document.getElementsByName('height')[0].value;
		
		//设置边框
		table.border = document.getElementsByName('border')[0].value;
		table.setAttribute("borderColor", document.getElementsByName('borderColor')[0].value);
		
		//设置边距和间距
		table.cellPadding = document.getElementsByName('padding')[0].value;
		table.cellSpacing = document.getElementsByName('spacing')[0].value;
		
		//设置对齐方式
		var align = document.getElementsByName('align')[0].value;
		if(align=="auto" || align=="") {
			table.removeAttribute("align");
		}
		else {
			table.align = align;
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
		<td nowrap="nowrap" align="right">行数：</td>
		<td><ext:field property="rows"/></td>
		<td nowrap="nowrap" align="right">列数：</td>
		<td><ext:field property="cols"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">宽度：</td>
		<td><ext:field property="width"/></td>
		<td nowrap="nowrap" align="right">高度：</td>
		<td><ext:field property="height"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">边框宽度：</td>
		<td><ext:field property="border"/></td>
		<td nowrap="nowrap" align="right">边框颜色：</td>
		<td><ext:field property="borderColor"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">间距：</td>
		<td><ext:field property="spacing"/></td>
		<td nowrap="nowrap" align="right">边距：</td>
		<td><ext:field property="padding"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">对齐方式：</td>
		<td><ext:field property="align"/></td>
		<td nowrap="nowrap" align="right"></td>
		<td></td>
	</tr>
</table>
</ext:form>