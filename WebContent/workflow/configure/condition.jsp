<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/configure/condition">
	<script>
		var CONDITION_DETAIL_CLASS = "com.yuanluesoft.workflow.client.model.definition.ConditionDetail";
		var fieldList = DialogUtils.getDialogArguments()._listDataFields();
		window.onload = function() {
			if(!fieldList) {
				return;
			}
			var options = document.getElementById("fieldSelect").options;
			options.length = fieldList.length;
			for(var i=0; i<fieldList.length; i++) {
				options[i].text = fieldList[i].name;
			}
			fieldChange();
		};
		var expression;
		function fieldChange() {
			document.getElementById('expressionNumber').style.display = "none";
			document.getElementById('expressionString').style.display = "none";
			document.getElementById('expressionDateTime').style.display = "none";
			document.getElementById('expressionEnum').style.display = "none";
			document.getElementById('expressionBoolean').style.display = "none";
			switch(fieldList[document.getElementById("fieldSelect").selectedIndex].type) {
			case "INTEGER":
			case "FLOAT":
				expression = document.getElementById('expressionNumber');
				break;
			case "STRING":
				expression = document.getElementById('expressionString');
				break;
			case "DATETIME":
				expression = document.getElementById('expressionDateTime');
				break;
			case "BOOLEAN":
				expression = document.getElementById('expressionBoolean');
				break;
			default: //ENUM
				expression = document.getElementById('expressionEnum');
			}
			expression.style.display = "";
			expressionChange(expression);
		}
		var compare;
		function expressionChange(src) {
			document.getElementById('compareRange').style.display = "none";
			document.getElementById('compareEdit').style.display = "none";
			document.getElementById('compareSelect').style.display = "none";
			compare = document.getElementById('compareEdit');
			if(fieldList[document.getElementById("fieldSelect").selectedIndex].type=="BOOLEAN") {
				compare = document.getElementById('compareSelect');
				var options = compare.options;
				options.length = 2;
				options[0].text = fieldList[document.getElementById("fieldSelect").selectedIndex].trueTitle;
				options[1].text = fieldList[document.getElementById("fieldSelect").selectedIndex].falseTitle;
			}
			else if(!fieldList[document.getElementById("fieldSelect").selectedIndex].basicType) { //枚举值
				compare = document.getElementById('compareSelect');
				var values = DialogUtils.getDialogOpener().workflowEditor.listEnumerationValues(fieldList[document.getElementById("fieldSelect").selectedIndex].type);
				var options = compare.options;
				options.length = values.length;
				for(var i=0; i<values.length; i++) {
					options[i].text = values[i];
				}
			}
			else if(src.options[src.selectedIndex].value.indexOf("between")!=-1) {
				compare = document.getElementById('compareRange');
			}
			compare.style.display = "";
		}
		function doOK() {
			var field = fieldList[document.getElementById("fieldSelect").selectedIndex];
			var condition = {mode: (document.getElementsByName('mode')[0].checked ? "AND" : "OR"), expression: expression.options[expression.selectedIndex].value};
			if(field.uuid.indexOf("DataField")!=-1) {
				condition.dataField = {uuid: field.uuid};
			}
			else {
				condition.formalParameter = {uuid: field.uuid};
			}
			var option = expression.options[expression.selectedIndex];
			switch(compare.id) {
			case "compareSelect":
				if(field.type=="BOOLEAN") {
					condition.compareValue1 = (compare.selectedIndex==0);
				}
				else {
					condition.compareValue1 = compare.options[compare.selectedIndex].text;
				}
				break;
				
			case "compareEdit":
				var value;
				switch(field.type) {
				case "STRING":
					value = (compare.value=="" ? "[空]" : FieldValidator.validateStringField(compare, "&,?,=", true));
					break;
					
				case "INTEGER":
				case "FLOAT":
					value = FieldValidator.validateNumberField(compare, true)
					break;
					
				case "DATETIME": 
					value = (compare.value=="" ? "[空]" : FieldValidator.validateDateField(compare, true));
					break;
				}
				if(value == "NaN")return;
				condition.compareValue1 = value;
				break;
				
			case "compareRange":
				var value1, value2;
				switch(field.type) {
				case "INTEGER":
				case "FLOAT":
					value1 = FieldValidator.validateNumberField(document.getElementById('value1'), true);
					value2 = FieldValidator.validateNumberField(document.getElementById('value2'), true);
					break;
					
				case "DATETIME":
					value1 = FieldValidator.validateDateField(document.getElementById('value1'), true);
					value2 = FieldValidator.validateDateField(document.getElementById('value2'), true);
					break;
				}
				if(value1 == "NaN" || value2 == "NaN")return;
				condition.compareValue1 = value1;
				condition.compareValue2 = value2;
			}
			condition.title = getConditionTitle(condition, field);
			DialogUtils.getDialogArguments().listProperty.appendItem(CONDITION_DETAIL_CLASS, condition);
			DialogUtils.closeDialog();
		}
		//设置条件显示标题
		function getConditionTitle(condition, field) {
			var title = "OR"==condition.mode ? "或 " : "与 ";
			title += condition.formalParameter ? (!field.description ? field.id : field.description) : field.name;
			if("equal"==condition.expression) {
				title += "等于";
			}
			else if("not equal"==condition.expression) {
				title += "不等于";
			}
			else if("less than"==condition.expression) {
				title += "DATETIME"==field.type ? "早于" : "小于";
			}
			else if("greater than"==condition.expression) {
				title += "DATETIME"==field.type ? "晚于" : "大于";
			}
			else if("between"==condition.expression) {
				title += "介于";
			}
			else if("not between"==condition.expression) {
				title += "不介于";
			}
			else if("contain"==condition.expression) {
				title += "包含";
			}
			else if("not contain"==condition.expression) {
				title += "不包含";
			}
			else if("member"==condition.expression) {
				title += "属于";
			}
			else if("not member"==condition.expression) {
				title += "不属于";
			}
			
			if(condition.compareValue1==null) {
				title += "空";
			}
			else if("BOOLEAN"==field.type) {
				if(condition.compareValue1=="true") {
					title += (!field.trueTitle ? "true" : field.trueTitle);
				}
				else {
					title += (!field.falseTitle ? "false" : field.falseTitle);
				}
			}
			else {
				title += condition.compareValue1;
			}
			if("between"==condition.expression || "not between"==condition.expression) {
				title += "和";
				if(condition.compareValue2==null) {
					title += "空";
				}
				else {
					title += condition.compareValue2;
				}
				title += "之间";
			}
			return title;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="80px" valign="middle" align="right">
		<col width="100%" valign="middle">
		<tr valign="middle">
			<td>连接方式：</td>
			<td valign="top">
				<input value="AND" checked="true" type="radio" Name="mode" class="radio">&nbsp;与
				<input value="OR" type="radio" Name="mode" class="radio">&nbsp;或
			</td>
		</tr>
		<tr>
			<td>字　　段：</td>
			<td>
				<select id="fieldSelect" style="width:100%" onchange="fieldChange()">
			</td>
		</tr>
		<tr>
			<td>比较方式：</td>
			<td>
				<select id="expressionNumber" style="width:100%" onchange="expressionChange(this)">
					<option value="equal">等于</option>
					<option value="not equal">不等于 </option>
					<option value="greater than">大于</option>
					<option value="less than">小于</option>
					<option value="between">介于</option>
					<option value="not between">不介于</option>
				</select>
				<select id="expressionString" style="width:100%;display:none" onchange="expressionChange(this)">
					<option value="equal">等于</option>
					<option value="not equal">不等于 </option>
					<option value="contain">包含</option>
					<option value="not contain">不包含</option>
					<option value="member">属于</option>
					<option value="not member">不属于</option>
				</select>
				<select id="expressionDateTime" style="width:100%;display:none" onchange="expressionChange(this)">
					<option value="equal">等于</option>
					<option value="not equal">不等于 </option>
					<option value="less than">早于</option>
					<option value="greater than">晚于</option>
					<option value="between">介于</option>
					<option value="not between">不介于</option>
				</select>
				<select id="expressionEnum" style="width:100%;display:none" onchange="expressionChange(this)">
					<option value="equal">等于</option>
					<option value="not equal">不等于 </option>
				</select>
				<select id="expressionBoolean" style="width:100%;display:none" onchange="expressionChange(this)">
					<option value="equal">等于</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>比 较 值：</td>
			<td>
				<table id="compareRange" border="0" height="100%" width="100%" cellspacing="0" cellpadding="0px" style="table-layout:fixed">
					<tr>
						<td width="50%"><input id="value1" class="edit" style="width:100%"></td>
						<td width="30px" align="center">和</td>
						<td width="50%"><input id="value2" class="edit" style="width:100%"></td>
					</tr>
				</table>
				<input id="compareEdit" class="edit" style="width:100%;display:none">
				<select id="compareSelect" style="width:100%;display:none"></select>
			</td>
		</tr>
	</table>
</ext:form>