<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveProject">
   	<ext:tab/>
   	<script>
   		function declare() {
			DialogUtils.openInputDialog('申报年度', [{name:'year', title:'申报年度', inputMode:'text'}], 360, 130, 'doDeclare({value}.year)');
		}
		function doDeclare(declareYear) {
			if(declareYear!="") {
				FormUtils.doAction("declare", "declareYear=" + declareYear);
			}
		}
	</script>
</ext:form>