<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="/bidding/project/admin/tabpages/noticeRead.jsp"/>
<br>
<ext:contains property="formActions" propertyName="title" propertyValue="发放中标通知书">
	<center>
	<ext:button name="发放中标通知书" onclick="publicNotice()"/>
	</center>
	<script>
	function publicNotice() {
		if(!confirm('是否确定发放中标通知书？')) {
			return false;
		}
		FormUtils.doAction('publicNotice');
	}
	</script>
</ext:contains>