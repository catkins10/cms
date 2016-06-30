function initData() {
}
function doOk() {
	var selectedIds = "";
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	for(var i=1; i<table.rows.length; i++) {
		selectedIds += "," + table.rows[i].id;
	}
	if(selectedIds!="") {
		selectedIds = selectedIds.substring(1);
	}
	document.getElementsByName("selectedRecordIds")[0].value = selectedIds;
	var form = document.forms[0];
	form.action += (location.search ? location.search : "");
	form.submit();
}
//初始化结果数据
function initResults() {
	document.getElementById("divResult").innerHTML = document.getElementById("priorityView").innerHTML;
	document.getElementById("priorityView").innerHTML = "";
}
function addResult(data) {
	var table = document.getElementById("divResult").getElementsByTagName("table")[0];
	var rows = table.rows, rowLen = rows.length;
	for(var i=1; i<rowLen; i++) {
		if(rows[i].id==data.id) {
			return rows[i];
		}
	}
	var newtr = table.insertRow(-1);
	newtr.id = data.id;
	newtr.style.cursor = "pointer";
	newtr.onclick = function() {
		onSelectResult(newtr);
	}
	newtr.ondblclick = function() {
		cleanOne(newtr);
	}
	for(var i=0; i<data.cells.length; i++) {
		td = newtr.insertCell(-1);
		td.className = "viewData";
		td.innerHTML = data.cells[i].innerHTML;
		var select = td.getElementsByTagName("input")[0];
		if(select) {
			select.checked = false;
		}
	}
	return newtr;
}
function selectRecord(viewPackageName, selected, recordId) { //选中或取消选中记录

}