<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveCompany">
	<script>
		function approval() {
			var inputs = [{name:'approvalPass', title:'审核结果', inputMode:'radio', itemsText:'通过|true\\0不通过|false', defaultValue:true},
						  {name:'failedReason', title:'未通过原因', inputMode:'textarea', rows:5}];
			DialogUtils.openInputDialog('企业审核', inputs, 430, 200, '', function(value) {
				if(value.approvalPass=='false' && value.failedReason=="") {
					alert('未通过原因不能为空');
					return;
				}
				document.getElementsByName('failedReason')[0].value = value.failedReason;
				FormUtils.doAction('approvalCompany', 'approvalPass=' + value.approvalPass);
			});
		}
	</script>
	<ext:tab/>
</ext:form>