<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal value="true" property="needBiddingDocumentsStamp">
	<html:hidden property="biddingDocumentsStamp"/>
	<input type="button" class="button" style="width:120px" value="模拟盖章完成事件" onclick="onAfterStamp()">
	<script>
		function onAfterStamp() {
			document.getElementsByName('biddingDocumentsStamp')[0].value = 'true'; //设置为已盖章
		}
	</script>
</ext:equal>
<jsp:include flush="true" page="biddingDocumentRead.jsp"/>