<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<jsp:include flush="true" page="/bidding/project/admin/tabpages/supplementEdit.jsp"/>
<br>
<html:checkbox property="adjustBidopeningTime" value="是" styleClass="checkbox" styleId="adjustBidopeningTime"/> <label for="adjustBidopeningTime">是否需要调整开标时间</label>
<br>
<center>
	<ext:button name="无补充通知" onclick="submitSupplement(false)"/>
	<ext:button name="提交补充通知" onclick="submitSupplement(true)"/>
</center>
<script>
function submitSupplement(need) {
	if(need) {
		var html = HtmlEditor.getHtmlContent("supplement.body");
		if(html=='') {
			alert('尚未输入正文');
			return;
		}
		if(!confirm('是否确定提交补充通知？')) {
			return;
		}
		document.getElementsByName("supplement.body")[0].value = html;
	}
	else {
		if(!confirm('是否确定没有补充通知？')) {
			return;
		}
		document.getElementsByName("supplement.body")[0].value = '';
	}
	FormUtils.doAction('submitSupplement', 'hasSupplement=' + StringUtils.utf8Encode(need ? '有' : '没有'));
}
</script>