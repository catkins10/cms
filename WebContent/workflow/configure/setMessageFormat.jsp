<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/configure/setMessageFormat">
	<script>
		window.onload = function() {
			var fieldList = DialogUtils.getDialogOpener().workflowEditor.workflow.dataFieldList;
			var options = document.getElementsByName("fieldSelect")[0].options;
			for(var i=0; i < (fieldList ? fieldList.length : 0); i++) {
				var field = ListUtils.findObjectByProperty(DialogUtils.getDialogOpener().workflowEditor.jsonObjectPool, 'uuid', fieldList[i].uuid);
				var option = document.createElement("OPTION");
				options.add(option);
				option.innerText = field.name;
				option.Value = field.name;
			}
			document.getElementsByName("messageFormat")[0].value = DialogUtils.getDialogOpener().document.getElementsByName("messageFormat")[0].value.replace(new RegExp("<BR>", "g"), "\r\n");
		};
		function doOK() {
			var messageFormatField = DialogUtils.getDialogOpener().document.getElementsByName("messageFormat")[0];
			messageFormatField.value = document.getElementsByName("messageFormat")[0].value.replace(/\r/g, "").replace(/\n/g, "<BR>");
			messageFormatField.onchange();
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<tr>
			<td>
				<select name="fieldSelect" style="width:120px">
					<option>流程名称
					<option>环节名称
				</select>
				<input type="button" value="插入字段" onclick="FormUtils.pasteText('messageFormat', '&lt;' + document.getElementsByName('fieldSelect')[0].options[document.getElementsByName('fieldSelect')[0].selectedIndex].label + '>')" class="button" style="width:60px">
			</td>
		</tr>
		<tr>
			<td>
				<textarea name="messageFormat" rows="6" style="width:100%"></textarea>
			</td>
		</tr>
	</table>
</ext:form>