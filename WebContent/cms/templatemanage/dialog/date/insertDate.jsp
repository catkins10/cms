<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertDate">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
			if(obj && obj.id=="systemDate") {
				var format = obj.getAttribute("urn");
				var list = DropdownField.getListValues("selectFormat");
				DropdownField.setValue("selectFormat", list.indexOf(format)==-1 ? "custom" : format);
				document.getElementsByName("format")[0].value = format;
				onFormatSelected();
			}
		}
		function doOk() {
			var format = document.getElementsByName("selectFormat")[0].value;
			if(format=='custom') {
				format = document.getElementsByName("format")[0].value;
			}
			if(format=="") {
				alert("格式未设置");
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
			obj.innerHTML = "<日期>";
			obj.id = "systemDate";
			obj.setAttribute("urn", format);
			DialogUtils.closeDialog();
		}
		function onFormatSelected() {
			var selectFormat = document.getElementsByName("selectFormat")[0].value;
			if(selectFormat!='custom') {
				document.getElementById("trFormat").style.display = "none";
				document.getElementsByName("format")[0].value = selectFormat;
			}
			else {
				document.getElementById("trFormat").style.display = "";
			}
			DialogUtils.adjustDialogSize();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">选择格式：</td>
			<td><ext:field property="selectFormat" onchange="onFormatSelected()"/></td>
		</tr>
		<tr style="display:none" id="trFormat">
			<td valign="top" nowrap="nowrap" style="padding-top:32px;">自定义格式：</td>
			<td>
				<div style="margin-bottom:5px;">
					<input type="button" class="button" style="width: 80px;" onclick="FormUtils.pasteText('format', '&lt;农历年&gt;')" value="插入农历年份"/>
					<input type="button" class="button" style="width: 80px;" onclick="FormUtils.pasteText('format', '&lt;农历月&gt;')" value="插入农历月份"/>
					<input type="button" class="button" style="width: 80px;" onclick="FormUtils.pasteText('format', '&lt;农历日&gt;')" value="插入农历日期"/>
					<input type="button" class="button" style="width: 80px;" onclick="FormUtils.pasteText('format', '&lt;生肖&gt;')" value="插入生肖"/>
				</div>
				<ext:field property="format"/>
			</td>
		</tr>
	</table>
</ext:form>