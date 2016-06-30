/*
//初始化数据,由继承者实现
function initData();
//获取当前记录,由继承者实现
function getCurrentData();
//获取选中的记录列表,由继承者实现
function getSelectedData();
//获取全部的记录列表,全选时调用,由继承者实现
function getAllData();
//获取字段值,由继承者实现
function getDataFieldValue(data, fieldName);
//焦点移动到下一个记录,由继承者实现
function moveToNext();
//重置记录列表,和getSelectedData()对应,由继承者实现
function resetData();
//检查类型是否匹配,由继承者实现
function validTargetType(data);
//按关键字查询,由继承者实现
function query();
*/
var trSelectedResult;
var dialogParam;
window.onload = function() {
	parseParam();
	try {
		initData();
	}
	catch(e) {
		
	}
	if(document.getElementsByName("multiSelect")[0] && document.getElementsByName("multiSelect")[0].value=="true") {
		initResults();
	}
}
window.onresize = function() {
	if(!BrowserInfo.isIE() && navigator.appName=="Netscape") {
		var height = document.body.clientHeight - document.getElementById('dialogButtonBar').offsetHeight;
		var dialogSearchBar = document.getElementById('dialogSearchBar');
		if(dialogSearchBar) {
			height -= dialogSearchBar.offsetHeight;
		}
		//document.getElementById("divData").style.height  = height;
		if(document.getElementById("divResult")) {
			//document.getElementById("divResult").style.height = height;
		}
	}
}
function initResults() {
	var html = '<table class="view" width="100%" border="0" cellpadding="0" cellspacing="1">' +
			   '	<col align="center" width="20" class="viewColOdd">';
	for(var i=1; i<dialogParam.length; i++) {
		html += '	<col align="left" width="' + dialogParam[i].width + '" class="' + (i%2==0 ? 'viewColOdd' : 'viewColEven') + '">';
	}
	html += '		<tr align="center" valign="middle">' +
			'			<td class="viewHeader" align="center" nowrap>&nbsp;</td>';
	for(var i=1; i<dialogParam.length; i++) {
		html += '		<td class="viewHeader" align="center" nowrap>' + dialogParam[i].title + '</td>';
	}
	html +=	'		</tr>' +
			' </table>';
	document.getElementById("divResult").innerHTML = html;
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	var openerDocument = DialogUtils.getDialogOpener().document;
	var keys = dialogParam[0].name=="" ? "".split(",") : FormUtils.getField(openerDocument, dialogParam[0].name).value.split(document.getElementsByName("separator")[0].value);
	for(var i=0; keys[0]!="" && i<keys.length; i++) {
		var tr = table.insertRow(-1);
		tr.id = keys[i];
		tr.style.cursor = "pointer";
		tr.onclick = function() {
			onSelectResult(this);
		}
		tr.ondblclick = cleanOne;
		//插入复选框
		td = tr.insertCell(-1);
		td.className = "viewData";
		td.innerHTML = '<input type="checkbox" class="checkbox" name="selectResult">';
	}
	//插入字段值
	for(var i=1; i<dialogParam.length; i++) {
		var values = dialogParam[0].name=="" ? "".split(",") : FormUtils.getField(openerDocument, dialogParam[i].name).value.split(document.getElementsByName("separator")[0].value);
		for(var j=0; keys[0]!="" && j<keys.length; j++) {
			var resultValue = values[j].replace(new RegExp("<", "g"), "&lt;").replace(new RegExp(">", "g"), "&gt;");
			td = table.rows[j+1].insertCell(i);
			td.className = "viewData";
			//设置divDate中的数据为选中
			for(var k=0;k<selectedData.length;k++){
				var childs = selectedData[k].childNodes;
		        var childArrTem=new Array();  //  临时数组，用来存储符合条件的节点
			    for(var l=0;l<childs.length;l++){
			        if(childs[l].nodeType==1){
			            childArrTem[childArrTem.length]=childs[l];
			        }
			    }
				for(var h = 0; h<childArrTem.length ; h++){
					if(h==i+1){
						var dateValue = "";
						if(resultValue == childArrTem[h].innerHTML){
							childArrTem[0].firstChild.checked = true;
						}
					}
				}
			}

			//
			td.innerHTML = values[j].replace(new RegExp("<", "g"), "&lt;").replace(new RegExp(">", "g"), "&gt;");
		}
	}
	//隐藏宽度为“0”的列
	for(var i=1; i<dialogParam.length; i++) {
		if(dialogParam[i].width==0) {
			table.rows[0].cells[i].style.display = "none";
			for(var j=0; keys[0]!="" && j<keys.length; j++) {
				table.rows[j+1].cells[i].style.display = "none";
			}
		}
	}
}
/*
 * 单选时格式为:表单字段名1{数据库字段1},...表单字段名n{数据库字段n}
 * 多选时格式为:表单关键字字段名{数据库关键字字段},表单字段名1{数据库字段1|标题1|宽度1|默认值1},...表单字段名n{数据库字段n|标题n|宽度n|默认值n},默认值在数据库字段为空时有效
 */
function parseParam() {
	dialogParam = new Array();
	var param = FormUtils.getField(document, "param").value;
	if(param=='') {
		return;
	}
	var list = param.split(",");
	for(var i=0; i<list.length; i++) {
		var detail = list[i].substring(0, list[i].length-1).split("{");
		var object = new Object();
		object.name = detail[0]; //表单字段名
		detail = detail[1].split("|");
		object.field = detail[0]; //数据库字段
		if(detail.length>1) {
			object.title = detail[1]; 
		}
		if(detail.length>2) {
			object.width = detail[2]; 
		}
		if(detail.length>3) {
			object.value = detail[3]; 
		}
		dialogParam[i] = object;
	}
}
function onSelectResult(src) {
	if(trSelectedResult) {
		setSelected(trSelectedResult, false);
	}
	trSelectedResult = src;
	setSelected(trSelectedResult, true);
}
function setSelected(tr, selected) {
	var cells=tr.cells;
	if(cells) {
		for(var i=cells.length-1;i>=0;i--) {
			cells[i].className = selected ? "viewData selected" : "viewData";
		}
	}
}
function doOk() {
	var openerDocument = DialogUtils.getDialogOpener().document;
	var values = new Array();
	if(document.getElementsByName("multiSelect")[0] && document.getElementsByName("multiSelect")[0].value=="true") {
		var rows = document.getElementById('divResult').getElementsByTagName('table')[0].rows;
		var len = rows.length;
		for(var i=1; i<len; i++) {
			values[0] = values[0] ? values[0] + document.getElementsByName("separator")[0].value + rows[i].id : rows[i].id;
			var cells = rows[i].cells;
			for(var j=1; j<dialogParam.length; j++) {
				values[j] = (values[j] ? values[j] + document.getElementsByName("separator")[0].value : "") +  cells[j].innerHTML.replace(new RegExp("&lt;", "g"), "<").replace(new RegExp("&gt;", "g"), ">");
			}
		}
		for(var j=0; j<dialogParam.length; j++) {
			if(dialogParam[j].name=="") { //没有对应的表单字段
				continue;
			}
			if(FormUtils.getField(openerDocument, dialogParam[j].name)) {
				FormUtils.getField(openerDocument, dialogParam[j].name).value = values[j] ? values[j] : "";
				try {
					FormUtils.getField(openerDocument, dialogParam[j].name).onchange();
				}
				catch(e) {
				
				}
			}
		}
	}
	else {
		var selectedData = getCurrentData();
		if(!selectedData) {
			return;
		}
		if(!validTargetType(selectedData)) {
			return;
		}
		for(var i=dialogParam.length-1; i>=0; i--) {
			if(dialogParam[i].name=="") { //没有对应的表单字段
				continue;
			}
			if(FormUtils.getField(openerDocument, dialogParam[i].name)) {
				FormUtils.getField(openerDocument, dialogParam[i].name).value = dialogParam[i].field=="" ? dialogParam[i].value : getDataFieldValue(selectedData, dialogParam[i].field);
				try {
					FormUtils.getField(openerDocument, dialogParam[i].name).onchange();
				}
				catch(e) {
					
				}
			}
		}
	}
	if(FormUtils.getField(document, "script").value!="") {
		var script = FormUtils.getField(document, "script").value;
		//替换脚本中的字段
		var newScript = "";
		var beginIndex = 0;
		for(var endIndex = script.indexOf("{"); endIndex>0; endIndex = script.indexOf("{", beginIndex)) {
			newScript += script.substring(beginIndex, endIndex);
			beginIndex = endIndex;
			endIndex = script.indexOf('}', beginIndex + "{".length);
			if(endIndex==-1) {
				break;
			}
			var propertyName = script.substring(beginIndex + "{".length, endIndex);
			if(document.getElementsByName("multiSelect")[0] && document.getElementsByName("multiSelect")[0].value == "true") {
				for(var j=0; j<dialogParam.length; j++) {
					if(dialogParam[j].field==propertyName) {
						newScript += values[j] ? values[j] : "";
					}
				}
			}
			else {
				newScript += getDataFieldValue(getCurrentData(), propertyName);
			}
			beginIndex = endIndex + 1;
		}
		newScript += script.substring(beginIndex);
		DialogUtils.getDialogOpener().setTimeout(newScript, 300);
	}
	if(document.getElementsByName("closeDialogOnOK")[0].value=="true") {
		DialogUtils.closeDialog();
	}
}
function addResult(data) {
	if(!validTargetType(data)) {
		return false;
	}
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	var rows = table.rows, rowLen = rows.length;
	var keyValue = getDataFieldValue(data, dialogParam[0].field);
	for(var i=1; i<rowLen; i++) {
		if(rows[i].id.replace(new RegExp("<", "g"), "&lt;").replace(new RegExp(">", "g"), "&gt;")==keyValue) {
			return rows[i];
		}
	}
	var td,newtr = table.insertRow(-1);
	newtr.id = keyValue;
	newtr.style.cursor = "pointer";
	newtr.onclick = function() {
		onSelectResult(this);
	}
	newtr.ondblclick = function() {
		cleanOne(this);
	};
	td = newtr.insertCell(-1);
	td.className = "viewData";
	td.align = "center";
	td.innerHTML = '<input type="checkbox" class="checkbox" name="selectResult">';
	for(var j=1; j<dialogParam.length; j++) {
		td = newtr.insertCell(-1);
		td.className = "viewData";
		td.innerHTML = dialogParam[j].field=="" ? dialogParam[j].value : getDataFieldValue(data, dialogParam[j].field);
	}
	for(var j=1; j<dialogParam.length; j++) {
		if(dialogParam[j].width==0) {
			newtr.cells[j].style.display = "none";
		}
	}
	return newtr;
}
function selectOne() {
	var selectedData = getSelectedData();
	var newtr;
	if(selectedData) {
		for(var i=0; i<selectedData.length; i++) {
			newtr = addResult(selectedData[i]);
			flag = false;
		}
		resetData();
	}
	else if(getCurrentData()) {
		newtr = addResult(getCurrentData());
		moveToNext();
	}
	if(newtr) {
		newtr.cells[0].focus();
		onSelectResult(newtr);
	}
}
function selectAll() {
	var allData = getAllData();
	if(!allData) {
		return;
	}
	var newtr;
	for(var i=0; i<allData.length; i++) {
		newtr = addResult(allData[i]);
	}
	if(newtr) {
		newtr.cells[0].focus();
		onSelectResult(newtr);
	}
}
//获取选中的记录列表,由继承者实现
function getSelectedResultData() {
	
}
function cleanOne() {
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	var selects = document.getElementById("divResult").getElementsByTagName("input");
	var flag = true;
	if(selects.length>0) {
		for(var i=selects.length -1; i>=0; i--) {
			if(selects[i].checked) {
				table.deleteRow(i+1);
				flag = false;
			}
		}
	}
	if(flag && trSelectedResult) {
		var index = trSelectedResult.rowIndex;
		table.deleteRow(index);
		var rows=table.rows;
		trSelectedResult=null;
		if(rows.length>1) {
			onSelectResult(rows[index>rows.length-1 ? rows.length-1:index]);
		}
	}
}
function cleanAll() {
	var table = document.getElementById('divResult').getElementsByTagName('table')[0];
	for(var i=table.rows.length-1;i>0;i--) {
		table.deleteRow(i);
	}
	trSelectedResult = null;
}
function moveUp() {
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	var selects = document.getElementById("divResult").getElementsByTagName("input");
	var flag = true;
	if(selects.length>0) {
		var len=selects.length;
		for(var i=0;i<len;i++) {
			if(selects[i].checked) {
				flag = false;
				if(i==0) {
					return;
				}
				DomUtils.moveTableRow(table, i+1, i);
				selects[i-1].checked = true;
			}
		}
	}
	if(flag && trSelectedResult && trSelectedResult.rowIndex>1) {
		DomUtils.moveTableRow(table, trSelectedResult.rowIndex, trSelectedResult.rowIndex - 1);
	}
}
function moveDown() {
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	var selects = document.getElementById("divResult").getElementsByTagName("input");
	var flag = true;
	if(selects.length>0) {
		if(!selects)selects=new Array(selects);
		var len=selects.length;
		for(var i=len-1; i>=0; i--) {
			if(selects[i].checked) {
				flag = false;
				if(i==len-1) {
					return;
				}
				DomUtils.moveTableRow(table, i+1, i+2);
				selects[i+1].checked = true;
			}
		}
	}
	if(flag && trSelectedResult && trSelectedResult.rowIndex<table.rows.length-1) {
		DomUtils.moveTableRow(table, trSelectedResult.rowIndex, trSelectedResult.rowIndex + 1);
	}
}
function onDblClickData() { //双击数据行
	if(document.getElementsByName("multiSelect")[0] && document.getElementsByName("multiSelect")[0].value=="true") {
			var newtr = addResult(getCurrentData());
			if(newtr) {
				newtr.cells[0].focus();
				onSelectResult(newtr);
			}
	}
	else {
		doOk();
	}
}



