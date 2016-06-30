<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function publicBidopening() {
	var opened = <ext:empty property="grades">false</ext:empty><ext:notEmpty property="grades">true</ext:notEmpty>;
	if(!opened) {
		opened = <ext:notEmpty property="bidopening.body">true</ext:notEmpty><ext:empty property="bidopening.body">false</ext:empty>;
	}
	if(!opened) {
		alert('开标信息不完整,不允许发布');
		return false;
	}
	FormUtils.doAction('publicBidopening');
}
</script>
<div style="width:380px">
	<br>
	<div style="font-size:14px">
		&nbsp;&nbsp;是否确定发布开标公示?
	</div>
	<br>
</div>