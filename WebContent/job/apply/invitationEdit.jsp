<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function insertInterviewTime() {
		var interviewTime = document.getElementsByName("interviewTime")[0].value;
		if(interviewTime=="") {
			return;
		}
		interviewTime = new Date(interviewTime.replace(/\-/g, '/')).format('yyyy-MM-dd T hh:mm');
		var invitationBody = document.getElementsByName('invitation.body')[0];
		if(invitationBody.value.indexOf('<面试时间>')!=-1) {
			invitationBody.value = invitationBody.value.replace(/<面试时间>/g, interviewTime);
		}
		else {
			FormUtils.pasteText('invitation.body', interviewTime);
		}
	}
	function formOnSubmit() {
		if(document.getElementsByName('invitation.body')[0].value.indexOf('<面试时间>')!=-1) {
			alert('面试时间未设置');
			return false;
		}
		return true;
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td width="280px"><ext:field property="interviewTime"/></td>
		<td>&nbsp;<input type="button" class="button" value="插入面试时间" onclick="insertInterviewTime()"></td>
	</tr>
	<tr>
		<td colspan="2"><ext:field property="invitation.body"/></td>
	</tr>
</table>