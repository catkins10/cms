<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function answer() {
		var options = document.getElementsByName("answerOption");
		if(options[0].checked) { //同意受理
			FormUtils.doAction("answer", "caseAccept=1");
		}
		else if(options[1].checked) { //不同意受理
			FormUtils.doAction("switchPage", "pageName=caseDeclined");
		}
		else if(options[2].checked) { //发送缺件通知
			FormUtils.doAction("switchPage", "pageName=missing");
		}
	}
</script>
<div style="width:360px">
	<br>
	<div style="font-size:12px; padding-left:20px">
		<input type="radio" class="radio" name="answerOption" id="answerAccept" checked="checked">&nbsp;<label for="answerAccept">同意受理</label><br><br>
		<input type="radio" class="radio" name="answerOption" id="answerDeclined">&nbsp;<label for="answerDeclined">不同意受理</label><br><br>
		<input type="radio" class="radio" name="answerOption" id="answerMissing">&nbsp;<label for="answerMissing">发送缺件通知</label>
	</div>
</div>