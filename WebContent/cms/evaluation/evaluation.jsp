<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/submitEvaluation" applicationName="cms/evaluation" pageName="evaluation">
	<ext:equal property="expires" value="true">
		测评已结束
	</ext:equal>
	<ext:notEqual property="expires" value="true">
		<table class="evaluationTable" border="0" cellpadding="3px" cellspacing="0">
			<tr>
				<td nowrap="nowrap">姓名：</td>
				<td nowrap="nowrap"><ext:field property="targetPersonName"/>&nbsp;&nbsp;</td>
				<td nowrap="nowrap">所在部门：</td>
				<td width="100%"><ext:field property="targetPersonOrg"/></td>
				<td nowrap="nowrap">测评时间：</td>
				<td nowrap="nowrap"><ext:field property="evaluateTime"/></td>
			</tr>
		</table>
		<table class="evaluationTable" border="1" cellpadding="3px" cellspacing="0" style="border-collapse:collapse" bordercolor="#000000">
			<tr align="center" style="font-weight: bold">
				<td nowrap="nowrap">项<br/>目</td>
				<td nowrap="nowrap">序<br/>号</td>
				<td width="100%">评议内容</td>
				<ext:iterate id="option" property="topic.options">
					<td nowrap="nowrap"><ext:write name="option" property="name"/></td>
				</ext:iterate>
				<td width="160px" nowrap="nowrap">备注</td>
			</tr>
			<ext:iterate id="category" property="itemCategories">
				<tr align="center">
					<td rowspan="<ext:write name="category" property="itemCount"/>"><b><ext:write name="category" property="category"/></b></td>
					<ext:iterate id="item" indexId="itemIndex" name="category" property="items" length="1">
						<td nowrap="nowrap"><ext:writeNumber name="itemIndex" namePlus="category" propertyPlus="firstItemIndex"/></td>
						<td align="left" title="<ext:write name="item" property="description"/>"><ext:write name="item" property="name"/></td>
						<ext:iterate id="option" property="topic.options">
							<td nowrap="nowrap" onclick="EventUtils.clickElement(childNodes[0]);" title="<ext:write name="option" property="name"/> <ext:write name="option" property="description"/>"><input type="radio" class="radio" name="option_<ext:write name="item" property="id"/>" value="<ext:write name="option" property="id"/>"/></td>
						</ext:iterate>
						<td><input type="text" class="field" name="remark_<ext:write name="item" property="id"/>"/></td>
					</ext:iterate>
				</tr>
				<ext:iterate id="item" indexId="itemIndex" name="category" property="items" offset="1">
					<tr align="center">
						<td nowrap="nowrap"><ext:writeNumber name="itemIndex" namePlus="category" propertyPlus="firstItemIndex"/></td>
						<td align="left" title="<ext:write name="item" property="description"/>"><ext:write name="item" property="name"/></td>
						<ext:iterate id="option" property="topic.options">
							<td nowrap="nowrap" onclick="EventUtils.clickElement(childNodes[0]);" title="<ext:write name="option" property="name"/> <ext:write name="option" property="description"/>"><input type="radio" class="radio" name="option_<ext:write name="item" property="id"/>" value="<ext:write name="option" property="id"/>"/></td>
						</ext:iterate>
						<td><input type="text" class="field" name="remark_<ext:write name="item" property="id"/>"/></td>
					</tr>
				</ext:iterate>
			</ext:iterate>
		</table>
		<br/>
		<center>
			<ext:button name="提交" onclick="submitEvaluation()"/>
		</center>
		<script>
			function submitEvaluation() {
				//检查是否都已经做出选择
				var inputs = document.getElementsByTagName("input");
				for(var i=0; i<inputs.length; i++) {
					if(!inputs[i].name || inputs[i].name.indexOf("option_")!=0) { //不是选择框
						continue;
					}
					var radios = document.getElementsByName(inputs[i].name);
					var j = radios.length - 1;
					for(; j>=0 && !radios[j].checked; j--);
					if(j==-1) {
						alert("尚未完成选择，不允许提交。");
						return false;
					}
				}
				FormUtils.submitForm();
			}
			<ext:iterate id="score" property="scores">
				var radios = document.getElementsByName("option_<ext:write name="score" property="itemId"/>");
				for(var i = 0; i<radios.length; i++) {
					if(radios[i].value=="<ext:write name="score" property="optionId"/>") {
						radios[i].checked = true;
						break;
					}
				}
				document.getElementsByName("remark_<ext:write name="score" property="itemId"/>")[0].value = "<ext:write name="score" property="remark"/>";
			</ext:iterate>
		</script>
	</ext:notEqual>
</ext:form>