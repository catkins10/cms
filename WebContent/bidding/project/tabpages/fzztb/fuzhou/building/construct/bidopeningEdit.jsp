<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="/bidding/project/admin/tabpages/fzztb/fuzhou/building/construct/bidopeningEdit.jsp"/>
<br>
<center>
	<ext:button name="提交" onclick="submitBidopening()"/>
</center>
<script>
function submitBidopening() {
	if(HtmlEditor.getHtmlContent('bidopening.body')=='') {
		alert('开标公示为空,不允许提交');
		return false;
	}
	if(!confirm('是否确定提交开标公示？')) {
		return false;
	}
	FormUtils.doAction('submitBidopening');
}
</script>