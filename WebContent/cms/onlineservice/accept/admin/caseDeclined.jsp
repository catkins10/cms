<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function caseDeclined() {
		if(document.getElementsByName('caseDeclinedReason')[0].value=='') {
			alert('请填写不同意受理的原因。');
			return;
		}
		FormUtils.doAction("answer", "caseAccept=0&back=1");
	}
</script>
<div style="width:430px">
	不同意受理的原因：
	<html:textarea property="caseDeclinedReason" rows="8"/>
</div>