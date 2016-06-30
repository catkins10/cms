<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveEvaluation">
	<table border="0" cellpadding="1px" cellspacing="0">
		<tr>
			<td nowrap="nowrap">考核月份：</td>
			<td nowrap="nowrap"><ext:field property="evaluationYear"/>年<ext:field property="evaluationMonth"/>月&nbsp;&nbsp;</td>
			<td nowrap="nowrap">考核人：</td>
			<td width="100%"><ext:field property="creator"/></td>
			<td nowrap="nowrap">考核时间：</td>
			<td nowrap="nowrap"><ext:field property="created"/></td>
		</tr>
	</table>
	<table class="evaluationTable" border="1" cellpadding="3px" cellspacing="0" style="border-collapse:collapse" bordercolor="#000000">
		<tr align="center" style="font-weight: bold">
			<td class="tdtitle" nowrap="nowrap">序号</td>
			<td class="tdtitle" nowrap="nowrap">部门</td>
			<td class="tdtitle" nowrap="nowrap" width="100px">权重</td>
			<td class="tdtitle" width="100%" nowrap="nowrap">考核说明</td>
		</tr>
		<ext:iterate id="result" indexId="resultIndex" property="results">
			<tr align="center">
				<td class="tdcontent" nowrap="nowrap"><ext:writeNumber name="resultIndex" plus="1"/></td>
				<td class="tdcontent" align="left" nowrap="nowrap">
					<html:hidden name="result" property="departmentId"/>
					<ext:write name="result" property="departmentName"/>
				</td>
				<td class="tdcontent" nowrap="nowrap"><ext:field writeonly="true" name="result" property="result"/></td>
				<td class="tdcontent" nowrap="nowrap"><ext:field writeonly="true" name="result" property="remark"/></td>
			</tr>
		</ext:iterate>
	</table>
	<br/>
	<script>
		window.onload = function() {
			if(document.getElementsByName("act")[0].value=="create") {
				var inputs = document.getElementsByName("result");
				for(var i=0; i<inputs.length; i++) {
					inputs[i].value = "";
				}
			}
		};
		function submitEvaluation() {
			//检查是否都已经做出选择
			var inputs = document.getElementsByName("result");
			for(var i=0; i<inputs.length; i++) {
				if(inputs[i].value=="") {
					alert("尚未完成考核，不允许提交。");
					inputs[i].focus();
					return false;
				}
				var result = Number(inputs[i].value);
				if(isNaN(result)) {
					alert("权重输入错误，请重新输入。");
					inputs[i].focus();
					return false;
				}
				if(result>=10) {
					alert("权重要小于10，请重新输入。");
					inputs[i].focus();
					return false;
				}
				if(result<0) {
					alert("权重不能小于0，请重新输入。");
					inputs[i].focus();
					return false;
				}
				var index = ("" + result).indexOf(".");
				if(index!=-1 && ("" + result).length - index > 3) {
					alert("权重最多只能有两位小数，请重新输入。");
					inputs[i].focus();
					return false;
				}
			}
			FormUtils.submitForm();
		}
	</script>
</ext:form>