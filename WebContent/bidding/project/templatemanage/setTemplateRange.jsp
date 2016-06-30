<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/setTemplateRange">
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		var document = dialogArguments.editor.document;
		setValues(document.getElementsByName("categories")[0].value, "categoryArray");
		setValues(document.getElementsByName("procedures")[0].value, "procedureArray");
		setValues(document.getElementsByName("cities")[0].value, "cityArray");
		setValues(document.getElementsByName("biddingModes")[0].value, "biddingModeArray");
	}
	function doOk() {
		var document = dialogArguments.editor.document;
		document.getElementsByName("categories")[0].value = getValues("categoryArray");
		document.getElementsByName("procedures")[0].value = getValues("procedureArray");
		document.getElementsByName("cities")[0].value = getValues("cityArray");
		document.getElementsByName("biddingModes")[0].value = getValues("biddingModeArray");
		DialogUtils.closeDialog();
	}
	function setValues(values, fieldName) {
		if(values=="") {
			return;
		}
		var fields = document.getElementsByName(fieldName);
		for(var i=0; i<fields.length; i++) {
			if(values.indexOf(',' + fields[i].value + ',')!=-1) {
				fields[i].checked = true;
			}
		}
	}
	function getValues(fieldName) {
		var values = "";
		var fields = document.getElementsByName(fieldName);
		for(var i=0; i<fields.length; i++) {
			if(fields[i].checked) {
				values += ',' +  fields[i].value;
			}
		}
		return values=="" ? "" : values + ",";
	}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right" valign="top">
		<col width="100%">
		<tr>
			<td nowrap="nowrap" style="padding-top: 7px;">适用的项目分类：</td>
			<td><ext:field property="categoryArray"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="padding-top: 7px;">适用的招标内容：</td>
			<td><ext:field property="procedureArray"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="padding-top: 7px;">适用的地区：</td>
			<td style="line-height:30px"><ext:field property="cityArray"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="padding-top: 7px;">适用的招标方式：</td>
			<td><ext:field property="biddingModeArray"/></td>
		</tr>
	</table>
</ext:form>