<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="/bidding/project/admin/tabpages/answerEdit.jsp"/>
<br>
<center>
	<ext:button name="无答疑会议纪要" onclick="submitAnswer(false)"/>
	<ext:button name="提交答疑会议纪要" onclick="submitAnswer(true)"/>
</center>
<script>
function submitAnswer(need) {
	if(need) {
		var html = HtmlEditor.getHtmlContent("answer.body");
		if(html=='') {
			alert('尚未输入正文');
			return;
		}
		if(!confirm('是否确定提交答疑会议纪要？')) {
			return;
		}
		document.getElementsByName("answer.body")[0].value = html;
	}
	else {
		if(!confirm('是否确定没有答疑会议纪要？')) {
			return;
		}
		document.getElementsByName("answer.body")[0].value = '';
	}
	FormUtils.doAction('submitAnswer', 'hasAnswer=' + StringUtils.utf8Encode(need ? '有' : '没有'));
}
</script>