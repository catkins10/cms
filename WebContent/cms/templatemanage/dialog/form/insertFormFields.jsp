<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertFormElement">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		function getFields() {
			var fields = DropdownField.getListValues('field').split("\0");
			for(var i=0; i<fields.length; i++) {
				eval('fields[i] = ' + fields[i].substring(fields[i].indexOf('|') + 1));
			}
			return fields;
		}
		function doOk() {
			dialogArguments.editor.saveUndoStep();
			var table = DomUtils.createElement(dialogArguments.window, dialogArguments.range, "TABLE");
			table.border = 0;
			table.cellSpaceing = 0;
			table.cellPadding = 3;
			var formTabelWidth = document.getElementsByName("formTabelWidth")[0].value;
			if(formTabelWidth!="") {
				table.style.width = formTabelWidth;
			}
			table.style.borderCollapse = "collapse";
			var cols = Number(document.getElementsByName("formTabelCols")[0].value);
			if(isNaN(cols)) {
				cols = 1;
			}
			var cssName = document.getElementsByName("cssName")[0].value;
			var fields = getFields();
			var insertFields = new Array();
			for(var i=0; i<fields.length; i++) {
				if(fields[i].inputMode!="hidden" && fields[i].inputMode!="readonly" && fields[i].inputMode!="none") {
					insertFields[insertFields.length] = fields[i];
				}
			}
			var rows = Math.ceil(insertFields.length/cols);
			var fieldIndex = 0;
			for(var i=0; i<rows; i++) {
				var row = table.insertRow(-1);
				for(var j=0; j<cols; j++) {
					var cell = row.insertCell(-1);
					if(fieldIndex<insertFields.length) {
						cell.noWrap = "true";
						cell.align = "right";
						cell.innerHTML = insertFields[fieldIndex].title + ":";
					}
					cell = row.insertCell(-1);
					if(i==0) { //第一行,设置列宽度
						cell.width = Math.floor(100/cols) + "%";
					}
					if(fieldIndex>=insertFields.length) {
						fieldIndex++;
						continue;
					}
					//插入输入框
					var html = "";
					switch(insertFields[fieldIndex].inputMode) {
					case "textarea":
					case "htmleditor":
						html = '<textarea title="' + insertFields[fieldIndex].title  + '" id="field" name="' + insertFields[fieldIndex].name + '"' + (cssName!="" ? ' class="' + cssName + '"' : "") + '>';
						break;
						
					case "checkbox":
						html = '<input type="checkbox" name="' + insertFields[fieldIndex].name + '" value="' + insertFields[fieldIndex].checkboxValue + '" id="field"><label for="' + insertFields[fieldIndex].name + '" id="label_' + insertFields[fieldIndex].name + '"> ' + insertFields[fieldIndex].checkboxLabel + '</label>';
						break;

					case "multibox":
					case "radio":
						var items = (insertFields[fieldIndex].itemsText=="" ? "选项1\0选项2\0选项3" : insertFields[fieldIndex].itemsText);
						items = items.split("\0");
						html = '<span id="fieldSpan" title="' + insertFields[fieldIndex].title  + '">';
						for(var k=0; k<items.length; k++) {
							html += '<input type="' + ("multibox"==insertFields[fieldIndex].type ? "checkbox" : "radio")  + '" id="field" name="' + insertFields[fieldIndex].name +'">';
							html += '&nbsp;' + items[k].split("|")[0];
							if(k<items.length-1) {
								html += '&nbsp;';
							}
						}
						html += "</span>";
						break;
						
					default:
						html = '<input type="text" title="' + insertFields[fieldIndex].title  + '" id="field" name="' + insertFields[fieldIndex].name + '"' + (cssName!="" ? ' class="' + cssName + '"' : "") + '>';
						break;
					}
					cell.innerHTML = html;
					fieldIndex++;
				}
			}
			DialogUtils.closeDialog();
		}
	</script>
	<span style="display:none"><ext:field property="field"/></span>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">表格宽度：</td>
			<td><ext:field property="formTabelWidth"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">显示列数：</td>
			<td><ext:field property="formTabelCols"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">字段样式名称：</td>
			<td><ext:field property="cssName"/></td>
		</tr>
	</table>
</ext:form>