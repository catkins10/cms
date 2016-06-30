<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function insertEntryTime() {
		var entryTime = document.getElementsByName("entryTime")[0].value;
		if(entryTime=="") {
			return;
		}
		entryTime = new Date(entryTime.replace(/\-/g, '/')).format('yyyy-MM-dd T hh:mm');
		var offerBody = document.getElementsByName('offer.body')[0];
		if(offerBody.value.indexOf('<入职时间>')!=-1) {
			offerBody.value = offerBody.value.replace(/<入职时间>/g, entryTime);
		}
		else {
			FormUtils.pasteText('offer.body', interviewTime);
		}
	}
	function formOnSubmit() {
		if(document.getElementsByName('offer.body')[0].value.indexOf('<入职时间>')!=-1) {
			alert('入职时间未设置');
			return false;
		}
		return true;
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td width="280px"><ext:field property="entryTime"/></td>
		<td>&nbsp;<input type="button" class="button" value="插入入职时间" onclick="insertEntryTime()"></td>
	</tr>
	<tr>
		<td colspan="2"><ext:field property="offer.body"/></td>
	</tr>
</table>