<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveWorkloadAssess">
	<table border="0" cellpadding="1px" cellspacing="0">
		<tr>
			<td nowrap="nowrap">考核月份：</td>
			<td nowrap="nowrap"><ext:field property="assessYear"/>年<ext:field property="assessMonth"/>月&nbsp;&nbsp;</td>
			<td nowrap="nowrap">考核人：</td>
			<td width="100%"><ext:field property="creator"/></td>
			<td nowrap="nowrap">考核时间：</td>
			<td nowrap="nowrap"><ext:field property="created"/></td>
		</tr>
	</table>
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr align="center" style="font-weight: bold">
			<td class="tdtitle" nowrap="nowrap">序号</td>
			<td class="tdtitle" nowrap="nowrap">被考核人员</td>
			<td class="tdtitle" nowrap="nowrap" width="100px">工作量</td>
			<td class="tdtitle" width="100%" nowrap="nowrap">考核说明</td>
		</tr>
		<ext:iterate id="result" indexId="resultIndex" property="results">
			<tr align="center">
				<td class="tdcontent" nowrap="nowrap"><ext:writeNumber name="resultIndex" plus="1"/></td>
				<td class="tdcontent" align="left" nowrap="nowrap">
					<html:hidden name="result" property="personId"/>
					<ext:write name="result" property="personName"/>
				</td>
				<td class="tdcontent" nowrap="nowrap"><ext:field writeonly="true" name="result" property="workload"/></td>
				<td class="tdcontent" nowrap="nowrap" align="left"><ext:field writeonly="true" name="result" property="remark"/></td>
			</tr>
		</ext:iterate>
	</table>
	<script>
		window.onload = function() {
			if(document.getElementsByName("act")[0].value=="create") {
				var inputs = document.getElementsByName("workload");
				for(var i=0; i<inputs.length; i++) {
					inputs[i].value = "";
				}
			}
		};
		function submitAssess() {
			//检查是否都已经做出选择
			var inputs = document.getElementsByName("workload");
			for(var i=0; i<inputs.length; i++) {
				if(inputs[i].value=="") {
					alert("尚未完成考核，不允许提交。");
					inputs[i].focus();
					return false;
				}
				var workload = Number(inputs[i].value);
				if(isNaN(workload)) {
					alert("考核成绩输入错误，请重新输入。");
					inputs[i].focus();
					return false;
				}
				if(workload>=1000) {
					alert("考核成绩要小于1000，请重新输入。");
					inputs[i].focus();
					return false;
				}
				if(workload<0) {
					alert("考核成绩不能小于0，请重新输入。");
					inputs[i].focus();
					return false;
				}
				var index = ("" + workload).indexOf(".");
				if(index!=-1 && ("" + workload).length - index > 2) {
					alert("考核成绩最多只能有一位小数，请重新输入。");
					inputs[i].focus();
					return false;
				}
			}
			FormUtils.submitForm();
		}
	</script>
</ext:form>