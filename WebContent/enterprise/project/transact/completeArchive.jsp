<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function completeArchive() {
		var archiveDateFields = document.getElementsByName("archiveDate");
		for(var i=0; i<archiveDateFields.length; i++) {
			if(FieldValidator.validateDateField(archiveDateFields[i], true, "归档时间")=="NaN") {
				return;
			}
		}
		FormUtils.doAction('completeArchive');
	}
</script>
<div style="width:390px">
	<table valign="middle" width="100%" style="table-layout:fixed" border="0" cellpadding="0" cellspacing="0">
		<ext:iterate id="contract" property="contracts">
			<tr>
				<td><ext:write name="contract" property="contractName"/>归档时间：</td>
			</tr>
			<tr>
				<td style="padding-left: 5px; padding-bottom: 6px">
					<ext:field name="contract" property="archiveDate"/>
				</td>
			</tr>
		</ext:iterate>
	</table>
</div>