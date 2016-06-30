<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
String viewPackageName = (String)request.getAttribute(com.yuanluesoft.jeaf.view.model.ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}%>
<br>
<table id="tableSearch" style="table-layout:fixed; background-color: #BFC5CE" width="100%" border="0" cellpadding="3" cellspacing="1">
	<col class="viewColEven">
	<col class="viewColEven">
	<col class="viewColEven">
	<col class="viewColEven">
	<tr height="23" align="center">
		<td width="60px" class="viewHeader"><b>连接</b></td>
		<td width="200px" class="viewHeader"><b>字段</b></td>
		<td width="72px" class="viewHeader"><b>比较方式</b></td>
		<td class="viewHeader"><b>比较条件</b></td>
	</tr>
</table>

<script>
	var CONDITION_CLASS_NAME = "com.yuanluesoft.jeaf.view.model.search.Condition";
	//设置字段列表
	var fields = [
	<ext:iterate id="field" indexId="fieldIndex" property="<%=viewPackageName + ".view.columns"%>">
<%		com.yuanluesoft.jeaf.business.model.Field field = (com.yuanluesoft.jeaf.business.model.Field)pageContext.getAttribute("field"); %>
		<ext:notEqual value="0" name="fieldIndex">,</ext:notEqual>
		{name: '<ext:write name="field" property="name"/>',
		 title: '<ext:write name="field" property="title"/>',
		 type: '<ext:write name="field" property="type"/>',
		 inputMode: '<ext:write name="field" property="inputMode"/>',
		 selectOnly: '<%=field.getParameter("selectOnly")%>',
		 itemsText: '<%=com.yuanluesoft.jeaf.business.util.FieldUtils.getSelectItemsText(field, null, request)%>',
		 valueField: '<%=field.getParameter("valueField")%>',
		 titleField: '<%=field.getParameter("titleField")%>',
		 secondEnabled: '<%=field.getParameter("secondEnabled")%>'}
	</ext:iterate>
	];
	function addCondition(searchCondition) { //增加条件
		var table = document.getElementById("tableSearch");
		var row = table.insertRow(-1);
		row.className = "viewData";
		row.id = "trCondition";
		
		//添加连接方式
		var html = '<select name="linkMode" onfocus="onFocus(this)">' +
				   '	<option value="AND"' + (searchCondition && searchCondition.linkMode=='AND' ? ' selected' : '') + '>并且' +
				   '	<option value="OR"' + (searchCondition && searchCondition.linkMode=='OR' ? ' selected' : '') + '>或者' +
				   '</select>';
		row.insertCell(-1).innerHTML = html;
		
		//添加字段列表
		var fieldIndex = -1;
		if(!searchCondition) {
			fieldIndex = 0;
			if(table.rows.length>2) {
				fieldIndex = table.rows[table.rows.length-2].cells[1].getElementsByTagName("select")[0].selectedIndex + 1;
				if(fieldIndex==fields.length) {
					fieldIndex = 0;
				}
			}
		}
		html = '<select name="field" onchange="onFieldChange(this)" onfocus="onFocus(this)">';
		for(var i=0; i<fields.length; i++) {
			html += '<option value="' + fields[i].name + '"' + (fieldIndex==i || (searchCondition && fields[i].name==searchCondition.fieldName) ? " selected" : "") + '>' + fields[i].title;
		}
		html += '</select>';
		row.insertCell(-1).innerHTML = html;
		
		//添加比较条件单元格
		row.insertCell(-1);
		
		//添加条件输入单元格
		row.insertCell(-1);
		
		onFieldChange(row.cells[1].getElementsByTagName("select")[0], searchCondition);
		focusIndex = row.rowIndex;
	}
	function onFieldChange(fieldSelector, searchCondition) { //字段改变
		var field = fields[fieldSelector.selectedIndex];
		var expression = (searchCondition ? searchCondition.expression : '');
		var html = '<select name="expression" onchange="onExpressionChange(this)" onfocus="onFocus(this)">';
		if(field.type=="number") { //数字
			html += '<option value="equal"' + (expression=='equal' ? " selected" : "") + '>等于</option>' +
					'<option value="not equal"' + (expression=='not equal' ? " selected" : "") + '>不等于</option>' +
					'<option value="greater than"' + (expression=='greater than' ? " selected" : "") + '>大于</option>' +
					'<option value="less than"' + (expression=='less than' ? " selected" : "") + '>小于</option>' +
					'<option value="between"' + (expression=='between' ? " selected" : "") + '>介于</option>' +
					'<option value="not between"' + (expression=='not between' ? " selected" : "") + '>不介于</option>';
		}
		else if(field.type=="date" || field.type=="timestamp" || field.inputMode=="time") { //日期和时间
			html += '<option value="less than"' + (expression=='less than' ? " selected" : "") + '>早于</option>' +
					'<option value="greater than"' + (expression=='greater than' ? " selected" : "") + '>晚于</option>' +
					'<option value="equal"' + (expression=='equal' ? " selected" : "") + '>等于</option>' +
					'<option value="not equal"' + (expression=='not equal' ? " selected" : "") + '>不等于</option>' +
					'<option value="between"' + (expression=='between' ? " selected" : "") + '>介于</option>' +
					'<option value="not between"' + (expression=='not between' ? " selected" : "") + '>不介于</option>';
		}
		else if(field.itemsText=='') { //没有选项,作为字符串处理
			html += '<option value="contain"' + (expression=='contain' ? " selected" : "") + '>包含</option>' +
					'<option value="not contain"' + (expression=='not contain' ? " selected" : "") + '>不包含</option>' +
					'<option value="equal"' + (expression=='equal' ? " selected" : "") + '>等于</option>' +
					'<option value="not equal"' + (expression=='not equal' ? " selected" : "") + '>不等于</option>' +
					'<option value="member"' + (expression=='member' ? " selected" : "") + '>属于</option>' +
					'<option value="not member"' + (expression=='not member' ? " selected" : "") + '>不属于</option>';
		}
		else { //有选项,作为字符串处理
			html += '<option value="equal"' + (expression=='equal' ? " selected" : "") + '>等于</option>' +
					'<option value="not equal"' + (expression=='not equal' ? " selected" : "") + '>不等于</option>' +
					'<option value="contain"' + (expression=='contain' ? " selected" : "") + '>包含</option>' +
					'<option value="not contain"' + (expression=='not contain' ? " selected" : "") + '>不包含</option>' +
					'<option value="member"' + (expression=='member' ? " selected" : "") + '>属于</option>' +
					'<option value="not member"' + (expression=='not member' ? " selected" : "") + '>不属于</option>';
		}
		html += '</select>';
		var td = fieldSelector.parentElement.nextSibling;
		td.innerHTML = html;
		onExpressionChange(td.getElementsByTagName("select")[0], searchCondition, true);
	}
	function onExpressionChange(expressionSelector, searchCondition, fieldChanged) { //比较方式改变
		var row = expressionSelector.parentElement.parentElement;
		var field = fields[row.cells[1].getElementsByTagName("select")[0].selectedIndex];
		var expression = expressionSelector.options[expressionSelector.selectedIndex].value;
		var value1 = "";
		var value2 = "";
		var title1 = "";
		if(searchCondition) {
			value1 = searchCondition.value1 ? searchCondition.value1 : "";
			value2 = searchCondition.value2 ? searchCondition.value2 : "";
			title1 = searchCondition.title1 ? searchCondition.title1 : "";
		}
		else if(!fieldChanged) {
			var inputElement = DomUtils.getElement(row.cells[3], "input", "value1");
			value1 = inputElement ? inputElement.value : "";
			
			inputElement = DomUtils.getElement(row.cells[3], "input", "value2");
			value2 = inputElement ? inputElement.value : "";
			
			inputElement = DomUtils.getElement(row.cells[3], "input", "title1");
			title1 = inputElement ? inputElement.value : "";
		}
		if(expression.indexOf("between")==-1) { //不是“介于”和“不介于”
			if(field.type=="date") {
				new DateField('<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">', 'field', '', 'dropDownButton', '', false, row.cells[3]);
			}
			else if(field.type=="timestamp") {
				new DateTimeField('<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">', 'field', '', 'dropDownButton', '', row.cells[3]);
			}
			else if(field.inputMode=="time") {
				new TimeField('<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">', 'true'==field.secondEnabled, 'field', '', 'dropDownButton', '', row.cells[3]);
			}
			else if(field.itemsText=='') { //没有选项
				row.cells[3].innerHTML = '<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">';
			}
			else if(field.inputMode!='dropdown') { //有选项,输入方式不是下拉列表
				var fieldId = Math.round(Math.random() * 100000000);
				new DropdownField('<input class="field" name="title1" id="title1' + fieldId + '" value="' + title1 + '" type="text" onfocus="onFocus(this)" readonly><input name="value1" id="value1' + fieldId + '" value="' + value1 + '" type="hidden">', field.itemsText, 'value1' + fieldId, 'title1' + fieldId, 'field', '', 'dropDownButton', '', row.cells[3]);
			}
			else if(field.valueField==field.name) { //有选项,直接显示值字段
				var fieldId = Math.round(Math.random() * 100000000);
				new DropdownField('<input class="field" name="value1" id="value1' + fieldId + '" value="' + value1 + '" type="text" onfocus="onFocus(this)">', field.itemsText, 'value1' + fieldId, '', 'field', '', 'dropDownButton', '', row.cells[3]);
			}
			else { //有选项
				var fieldId = Math.round(Math.random() * 100000000);
				new DropdownField('<input class="field" name="value1" id="value1' + fieldId + '" value="' + value1 + '" type="text" onfocus="onFocus(this)">', field.itemsText, '', 'value1' + fieldId, 'field', '', 'dropDownButton', '', row.cells[3]);
			}
		}
		else { //“介于”或者“不介于”
			var html = '<table border="0" cellpadding="0" cellspacing="0">' +
					   '	<tr>' +
					   '		<td' + (field.inputMode!="time" ? ' width="50%"' : '') + '></td>' +
					   '		<td nowrap align="center">&nbsp;和&nbsp;</td>' +
					   '		<td' + (field.inputMode!="time" ? ' width="50%"' : '') + '></td>' +
					   '	</tr>' +
					   '</table>';
			row.cells[3].innerHTML = html;
			var dateRow = row.cells[3].getElementsByTagName('table')[0].rows[0];
			if(field.type=="date") {
				new DateField('<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">', 'field', '', 'dropDownButton', '', false, dateRow.cells[0]);
				new DateField('<input class="field" name="value2" value="' + value2 + '" type="text" onfocus="onFocus(this)">', 'field', '', 'dropDownButton', '', false, dateRow.cells[2]);
			}
			else if(field.type=="timestamp") {
				new DateTimeField('<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">', 'field', '', 'dropDownButton', '', dateRow.cells[0]);
				new DateTimeField('<input class="field" name="value2" value="' + value2 + '" type="text" onfocus="onFocus(this)">', 'field', '', 'dropDownButton', '', dateRow.cells[2]);
			}
			else if(field.inputMode=="time") {
				new TimeField('<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">', 'true'==field.secondEnabled, 'field', '', 'dropDownButton', '', dateRow.cells[0]);
				new TimeField('<input class="field" name="value2" value="' + value2 + '" type="text" onfocus="onFocus(this)">', 'true'==field.secondEnabled, 'field', '', 'dropDownButton', '', dateRow.cells[2]);
			}
			else if(field.type=="number") {
				dateRow.cells[0].innerHTML = '<input class="field" name="value1" value="' + value1 + '" type="text" onfocus="onFocus(this)">';
				dateRow.cells[2].innerHTML = '<input class="field" name="value2" value="' + value2 + '" type="text" onfocus="onFocus(this)">';
			}
		}
	}
	function deleteCondition() { //删除条件
		if(focusIndex!=-1 && document.getElementById('tableSearch').rows.length > 2) {
			document.getElementById('tableSearch').deleteRow(focusIndex);
		}
		focusIndex = -1;
	}
	var focusIndex = -1;
	function onFocus(src) { //获得焦点
		while(src.id != "trCondition") {
			src = src.parentElement;
		}
		focusIndex = src.rowIndex;
	}
	function generateSearchConditions(viewPackageName) { //生成搜索条件
		var describe = "";
		var searchConditions = {uuid:'java.util.ArrayList@0', collection:[], objects:[]};
		var rows = document.getElementById('tableSearch').rows;
		for(var i=1; i<rows.length; i++) {
			var searchCondition = {uuid: CONDITION_CLASS_NAME + '@' + i};
			//设置连接方式
			var selector = DomUtils.getElement(rows[i], "select", "linkMode");
			searchCondition.linkMode = selector.options[selector.selectedIndex].value;
			//设置字段
			selector = DomUtils.getElement(rows[i], "select", "field");
			var field = fields[selector.selectedIndex];
			searchCondition.fieldName = field.name;
			searchCondition.fieldType = field.type;
			//设置表达式
			selector = DomUtils.getElement(rows[i], "select", "expression");
			var expression = selector.options[selector.selectedIndex];
			searchCondition.expression = expression.value;
			//设置条件参数
			if(searchCondition.expression.indexOf("between")==-1) { //不是介于和不介于
				if("number"==field.type) {
					searchCondition.value1 = '' + FieldValidator.validateNumberField(DomUtils.getElement(rows[i], "input", "value1"));
				}
				else if("date"==field.type || "timestamp"==field.type) {
					searchCondition.value1 = FieldValidator.validateDateField(DomUtils.getElement(rows[i], "input", "value1"));
				}
				else {
					searchCondition.value1 = FieldValidator.validateStringField(DomUtils.getElement(rows[i], "input", "value1"), "&,?,=");
					if(searchCondition.value1.indexOf("')")!=-1) {
						alert("搜索条件不能包含')");
						return;
					}
				}
				if(searchCondition.value1=="NaN") {
					return false;
				}
				if(searchCondition.value1!="0" && searchCondition.value1=="") {
					continue;
				}
				if(DomUtils.getElement(rows[i], "input", "title1")) {
					searchCondition.title1 = DomUtils.getElement(rows[i], "input", "title1").value;
				}
				describe += (describe=="" ? "" : "\r\n " + (searchCondition.linkMode=="AND" ? "并且" : "或者"));
				describe += " " + field.title;
				describe += " "  + expression.text;
				describe += " "  + (searchCondition.title1 ? searchCondition.title1 : searchCondition.value1);
			}
			else {
				if("number"==field.type) {
					searchCondition.value1 = '' + FieldValidator.validateNumberField(DomUtils.getElement(rows[i], "input", "value1"));
					searchCondition.value2 = '' + FieldValidator.validateNumberField(DomUtils.getElement(rows[i], "input", "value2"));
				}
				else if("date"==field.type || "timestamp"==field.type) {
					searchCondition.value1 = FieldValidator.validateDateField(DomUtils.getElement(rows[i], "input", "value1"));
					searchCondition.value2 = FieldValidator.validateDateField(DomUtils.getElement(rows[i], "input", "value2"));
				}
				else if("time"==field.inputMode) {
					searchCondition.value1 = DomUtils.getElement(rows[i], "input", "value1").value;
					searchCondition.value2 = DomUtils.getElement(rows[i], "input", "value2").value;
				}
				if(searchCondition.value1=="NaN" || searchCondition.value2=="NaN") {
					return false;
				}
				if(searchCondition.value1=="" || searchCondition.value2=="") {
					continue;
				}
				describe += (describe=="" ? "" : "\r\n " + DomUtils.getElement(rows[i], null, 'linkMode').options[DomUtils.getElement(rows[i], null, 'linkMode').selectedIndex].text);
				describe += " " + field.title;
				describe += " "  + expression.text;
				describe += " "  + searchCondition.value1 + " 和 "  + searchCondition.value2;
			}
			searchConditions.objects.push(searchCondition);
			searchConditions.collection.push({uuid: searchCondition.uuid});
		}
		document.getElementsByName(viewPackageName + ".searchConditions")[0].value = (searchConditions.objects.length==0 ? "" : JsonUtils.stringify(searchConditions));
		document.getElementsByName(viewPackageName + ".searchConditionsDescribe")[0].value = describe;
		return true;
	}
	function startSearch(viewPackageName) { //开始搜索
		//生成搜索条件
		if(!generateSearchConditions(viewPackageName)) {
			return;
		}
		if(document.getElementsByName(viewPackageName + ".searchConditions")[0].value=="") {
			alert("搜索条件为空，搜索不能完成！");
			return;
		}
		document.getElementsByName(viewPackageName + ".selectedIds")[0].value = "";
		FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + ".currentViewAction=search&viewPackageName=" + viewPackageName);
	}
	function initSearchCondition(viewPackageName) {
		var searchConditions = document.getElementsByName(viewPackageName + ".searchConditions")[0].value;
		try {
			eval('searchConditions=' + searchConditions + ';');
			if(searchConditions.collection.length > 0) {
				for(var i = 0; i < searchConditions.collection.length; i++) {
					addCondition(ListUtils.findObjectByProperty(searchConditions.objects, "uuid", searchConditions.collection[i].uuid));
				}
				return;
			}
		}
		catch(e) {
		
		}
		addCondition();
		//自动罗列所有字段
		/*var table = document.getElementById("tableSearch");
		var field = table.rows[table.rows.length - 1].getElementsByTagName("select")[1];
		for(var i=0; i<field.options.length - 1; i++) {
			addCondition();
		}*/
	}
	initSearchCondition('<%=viewPackageName%>');
</script>