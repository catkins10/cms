<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function sendMissingNotify() {
		if(document.getElementsByName('missing.description')[0].value=='') {
			alert('请填写缺件说明。');
			return;
		}
		FormUtils.doAction('missing');
	}
</script>
<div style="width:430px">
	缺件说明：
	<html:textarea property="missing.description" rows="8"/>
</div>