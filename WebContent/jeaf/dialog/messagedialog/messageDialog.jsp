<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/messageDialog">
	<script>
		function onButtonClick(buttonName) {
			var script = document.getElementsByName("script")[0].value;
			if(script=="") {
				DialogUtils.getDialogArguments().call(null, buttonName);	
			}
			else {
				var buttonNames = '<ext:write property="buttonNames"/>'.split(",");
				for(var buttonIndex=0; buttonIndex<buttonNames.length && buttonNames[buttonIndex]!=buttonName; buttonIndex++);
				script = script.split('$$');
				if(script.length>buttonIndex && script[buttonIndex]!='') {
					DialogUtils.getDialogOpener().setTimeout(script[buttonIndex], 300);
				}
			}
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">
				<img src="<%=request.getContextPath()%>/jeaf/dialog/messagedialog/icons/<ext:field property="type" writeonly="true"/>.gif">
			</td>
			<td><ext:field property="message" writeonly="true"/></td>
		</tr>
	</table>
</ext:form>