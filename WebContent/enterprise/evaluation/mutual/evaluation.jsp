<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveEvaluation" applicationName="enterprise/evaluation/mutual" pageName="evaluation">
	<table border="0" cellpadding="1px" cellspacing="0">
		<tr>
			<td nowrap="nowrap">互评月份：</td>
			<td nowrap="nowrap"><ext:field property="evaluationYear"/>年<ext:field property="evaluationMonth"/>月&nbsp;&nbsp;</td>
			<td nowrap="nowrap">互评人：</td>
			<td width="100%"><ext:field property="creator"/></td>
			<td nowrap="nowrap">互评时间：</td>
			<td nowrap="nowrap"><ext:field property="created"/></td>
		</tr>
	</table>
	<table id="evaluationTable" class="evaluationTable" border="1" cellpadding="3px" cellspacing="0" style="border-collapse:collapse; margin-top:2px;" bordercolor="#000000">
		<tr align="center" style="font-weight: bold">
			<td nowrap="nowrap">序号</td>
			<td nowrap="nowrap">被评价人员</td>
			<td nowrap="nowrap" width="100%">评价等级</td>
		</tr>
		<ext:iterate id="result" indexId="resultIndex" property="results">
			<tr align="center">
				<td nowrap="nowrap"><ext:writeNumber name="resultIndex" plus="1"/></td>
				<td nowrap="nowrap"><ext:write name="result" property="personName"/></td>
				<td nowrap="nowrap" align="left">
					<input onclick="onClickEvaluateLevel(this)" <ext:equal value="2" name="result" property="evaluateLevel">checked="true"</ext:equal> type="checkbox" name="evaluateLevel_<ext:write name="result" property="personId"/>" value="2" id="evaluateLevel_<ext:write name="result" property="personId"/>_high"><label for="evaluateLevel_<ext:write name="result" property="personId"/>_high">前<ext:write property="voteNumber"/>名</label>&nbsp;
					<input onclick="onClickEvaluateLevel(this)" <ext:equal value="0" name="result" property="evaluateLevel">checked="true"</ext:equal> type="checkbox" name="evaluateLevel_<ext:write name="result" property="personId"/>" value="0" id="evaluateLevel_<ext:write name="result" property="personId"/>_low"><label for="evaluateLevel_<ext:write name="result" property="personId"/>_low">后<ext:write property="voteNumber"/>名</label>
				</td>
			</tr>
		</ext:iterate>
	</table>
	<br/>
	<center>
		<ext:button name="提交" onclick="submitEvaluation()"/>
	</center>
	<script>
		var voteNumber = <ext:write property="voteNumber"/>;
		var highVoteNumber = 0;
		var lowVoteNumber = 0;
		function onClickEvaluateLevel(src) {
			var fields = document.getElementsByName(src.name);
			if(src.checked) {
				if(fields[0]!=src) {
					fields[0].checked = false;
				}
				if(fields[1]!=src) {
					fields[1].checked = false;
				}
			}
			resetEvaluateLevelFields(); //检查投票数是否已满,如果满了,则禁止选择其他的选项
		}
		function voteTotal() { //投票统计
			highVoteNumber = 0;
			lowVoteNumber = 0;
			var evaluationTable = document.getElementById("evaluationTable");
			for(var i=1; i<evaluationTable.rows.length; i++) {
				var fields = evaluationTable.rows[i].cells[2].getElementsByTagName("input");
				if(fields[0].checked) {
					highVoteNumber++;
				}
				if(fields[1].checked) {
					lowVoteNumber++;
				}
			}
		}
		function resetEvaluateLevelFields() { //检查投票数是否已满,如果满了,则禁止选择其他的选项
			voteTotal(); //投票统计
			var evaluationTable = document.getElementById("evaluationTable");
			for(var i=1; i<evaluationTable.rows.length; i++) {
				var fields = evaluationTable.rows[i].cells[2].getElementsByTagName("input");
				if(!fields[0].checked) {
					fields[0].disabled = highVoteNumber>=voteNumber;
				}
				if(!fields[1].checked) {
					fields[1].disabled = lowVoteNumber>=voteNumber;
				}
			}
		}
		function submitEvaluation() {
			//检查投票是否已经完成
			voteTotal(); //投票统计
			if(highVoteNumber!=voteNumber || lowVoteNumber!=voteNumber) {
				alert('投票未完成，提交不能完成。');
				return;
			}
			FormUtils.submitForm();
		}
		window.onload = function() {
			resetEvaluateLevelFields();
		};
	</script>
</ext:form>