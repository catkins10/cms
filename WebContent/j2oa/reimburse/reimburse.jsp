<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/save">
	<ext:tab/>
	<script>
	function countDays() {
		var beginTime = FormUtils.getTimeValue("beginTime");
		var endTime = FormUtils.getTimeValue("endTime");
		if(beginTime!="" && endTime!="") {
			beginTime.setHours(0);
			beginTime.setMinutes(0);
			beginTime.setSeconds(0);
			beginTime.setMilliseconds(0);
			endTime.setHours(0);
			endTime.setMinutes(0);
			endTime.setSeconds(0);
			endTime.setMilliseconds(0);
			var days = (endTime - beginTime)/24/3600000;
			document.getElementsByName("dayCount")[0].value = Math.abs(days) + 1;
		}
	}
	function pay() {
		if(confirm(document.getElementById("loan") ? "是否确定要核销借款并支付报销款？" : "是否确定要支付报销款？")) {
			FormUtils.doAction('pay')
		}
	}
	</script>
</ext:form>