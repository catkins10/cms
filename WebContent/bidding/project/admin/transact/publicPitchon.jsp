<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function publicPitchon() {
	if('<ext:write property="pitchon.pitchonEnterprise"/>'=='') {
		alert('中标人为空,不允许发布');
		return false;
	}
	FormUtils.doAction('publicPitchon');
}
</script>
<div style="width:380px">
	<br>
	<div style="font-size:14px">
		&nbsp;&nbsp;是否确定发布中标结果公示?
	</div>
	<br>
</div>