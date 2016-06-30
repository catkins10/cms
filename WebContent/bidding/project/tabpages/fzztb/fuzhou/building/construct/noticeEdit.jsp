<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="/bidding/project/admin/tabpages/fzztb/fuzhou/building/construct/noticeEdit.jsp"/>
<br>
<center>
	<ext:button name="提交" onclick="submitNotice()"/>
</center>
<script>
function submitNotice() {
	if(!confirm('是否确定提交中标通知书？')) {
		return false;
	}
	FormUtils.doAction('submitNotice');
}
</script>