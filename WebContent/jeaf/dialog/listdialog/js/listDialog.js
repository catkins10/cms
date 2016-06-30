//允许的字段名称:id(源数据的序号),value,title
//初始化数据,由继承者实现
function initData() {
	var multiSelect = document.getElementsByName("multiSelect")[0].value=="true";
	var html = '<table class="view" width="100%" border="0" cellpadding="0" cellspacing="1">' +
			   (multiSelect ? '<col align="center" class="viewColOdd">' : '') +
			   '	<col align="left" class="viewColEven">' +
			   '	<col align="left">' +
			   '	<tr align="center" valign="middle">' +
			   (multiSelect ? '<td width="32px" class="viewHeader" align="center" nowrap>&nbsp;</td>' : '') +
			   '		<td width="100%" class="viewHeader" align="center" id="title">' + document.title + '</td>' +
			   '		<td style="display:none" class="viewHeader" align="center" id="value"></td>' +
			   '	</tr>' +
			   '</table>';
	document.getElementById("divData").innerHTML = html;
	var table = document.getElementById("divData").getElementsByTagName("table")[0];
	var openerDocument = DialogUtils.getDialogOpener().document;
	var list = document.getElementsByName("itemsText")[0].value;
	if(list=="") {
		var source = openerDocument.getElementsByName(document.getElementsByName("source")[0].value)[0];
		list = (source ? source.value : document.getElementsByName("source")[0].value);
	}
	if(document.getElementsByName("separator")[0].value=="") {
		document.getElementsByName("separator")[0].value = ",";
	}
	list = list.split(",");
	for(var i=0; list[0]!="" && i<list.length; i++) {
		if(list[i]=="") {
			continue;
		}
		var values = list[i].split("|");
		var newtr = table.insertRow(-1);
		newtr.id = i;
		newtr.style.cursor = "pointer";
		newtr.onclick = function() {
			onSelectData(this);
		};
		newtr.ondblclick = function() {
			onDblClickData();
		};
		if(multiSelect) {
			var td = newtr.insertCell(-1);
			td.className = "viewData";
			td.align = "center";
			td.width = "32px";
			td.innerHTML = '<input type="checkbox" class="checkbox" name="select">';
		}
		//标题
		td = newtr.insertCell(-1);
		td.className = "viewData";
		td.width = "100%";
		td.innerHTML = values[0];
		//值
		td = newtr.insertCell(-1);
		td.className = "viewData";
		td.style.display = "none";
		td.innerHTML = values[values.length>1 ? 1:0];
	}
}
//获取当前记录,由继承者实现
function getCurrentData() {
	return trSelectedData;
}
//获取选中的记录列表,由继承者实现
function getSelectedData() {
	var selectedData = new Array();
	var selects = document.getElementById("divData").getElementsByTagName("input");
	if(selects) {
		for(var i=0; i<selects.length; i++) {
			if(selects[i].checked) {
				selectedData[selectedData.length] = selects[i].parentElement.parentElement;
			}
		}
	}
	return selectedData.length==0 ? null : selectedData;
}
//获取全部的记录列表,全选时调用,由继承者实现
function getAllData() {
	var allData = new Array();
	var rows = document.getElementById("divData").getElementsByTagName("table")[0].rows
	for(var i=1; i<rows.length; i++) {
		allData[allData.length] = rows[i];
	}
	return allData.length==0 ? null : allData;
}
//获取字段值,由继承者实现
function getDataFieldValue(data, fieldName) {
	if(fieldName=="id") {
		return data.id;
	}
	if(fieldName=="CATEGORY") {
		return document.getElementsByName("viewPackage.categories")[0].value;
	}
	var trFirst = document.getElementById("divData").getElementsByTagName("table")[0].rows[0];
	for(var i=0; i<trFirst.cells.length; i++) {
		if(trFirst.cells[i].id==fieldName) {
			return data.cells[i].innerHTML.replace(/&nbsp;/gi, ' ');
		}
	}
	return null;
}
//焦点移动到下一个记录,由继承者实现
function moveToNext() {
	var rows = document.getElementById("divData").getElementsByTagName("table")[0].rows;
	var index = trSelectedData.rowIndex + 1;
	if(rows.length > index) {
		onSelectData(rows[index]);
		rows[index].cells[0].focus();
	}
}
//重置记录列表,由继承者实现
function resetData() {
	var selects = document.getElementById("divData").getElementsByTagName("input");
	if(selects) {
		for(var i=0; i<selects.length; i++) {
			selects[i].checked = false;
		}
	}
}
//检查类型是否匹配,由继承者实现
function validTargetType(data) {
	return true;
}
var trSelectedData;
function onSelectData(src) { //选中数据行
	if(trSelectedData) {
		setSelected(trSelectedData, false);
	}
	trSelectedData = src;
	setSelected(src, true);
}

