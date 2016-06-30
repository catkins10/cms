//初始化数据,由继承者实现
function initData() {
	
}
//获取当前记录,由继承者实现
function getCurrentData() {
	var doc = frames["frameViewData"].document;
	var rows = doc.getElementsByTagName("table")[0].rows
	for(var i=1; i<rows.length; i++) {
		if(rows[i].cells[0].className.indexOf('selected')!=-1) {
			return rows[i];
		}
	}
	return null;
}
//获取选中的记录列表,由继承者实现
function getSelectedData() {
	var doc = frames["frameViewData"].document;
	var selectedData = new Array();
	var selects = doc.getElementsByName("viewPackage.select");
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
	var doc = frames["frameViewData"].document;
	var allData = new Array();
	var rows = doc.getElementsByTagName("table")[0].rows
	for(var i=1; i<rows.length; i++) {
		allData[allData.length] = rows[i];
	}
	return allData.length==0 ? null : allData;
}
//获取字段值,由继承者实现
function getDataFieldValue(data, fieldName) {
	var doc = frames["frameViewData"].document;
	if(fieldName=="id") {
		return data.id;
	}
	if(fieldName=="CATEGORY") {
		return document.getElementsByName("viewPackage.categories")[0].value;
	}
	var trFirst = doc.getElementsByTagName("table")[0].rows[0];
	for(var i=0; i<trFirst.cells.length; i++) {
		if(trFirst.cells[i].id==fieldName) {
			var value = data.cells[i].innerHTML;
			return value=='&nbsp;' ? '' : value.replace(/&nbsp;/gi, ' ');
		}
	}
	return null;
}
//焦点移动到下一个记录,由继承者实现
function moveToNext() {
	var doc = frames["frameViewData"].document;
	var rows = doc.getElementsByTagName("table")[0].rows;
	var selectedRow = getCurrentData();
	var index = selectedRow.rowIndex + 1;
	if(rows.length > index) {
		setSelected(selectedRow, false);
		setSelected(rows[index], true);
		rows[index].cells[0].focus();
	}
}
//重置记录列表,由继承者实现
function resetData() {
	var doc = frames["frameViewData"].document;
	var selects = doc.getElementsByName("viewPackage.select");
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
//按关键字查询,由继承者实现
function query() {
	loadSelectViewData();
}
//加载数据
function loadSelectViewData(mode) {
	var queryString = document.getElementsByName("queryString")[0].value;
	queryString = StringUtils.removeQueryParameter(queryString, "applicationName");
	queryString = StringUtils.removeQueryParameter(queryString, "viewName");
	queryString = StringUtils.removeQueryParameter(queryString, "key");
	queryString = "applicationName=" + document.getElementsByName("applicationName")[0].value + "&viewName=" + document.getElementsByName("viewName")[0].value + "&" + queryString;
	if(document.getElementsByName("key")[0]) {
		queryString += "&key=" + StringUtils.utf8Encode(document.getElementsByName("key")[0].value).replace('+', '%2b');
	}
	queryString += "&viewPackage.categories=" + StringUtils.utf8Encode(document.getElementsByName("viewPackage.categories")[0].value);
	if(mode=='previousPage' || mode=='nextPage' || mode=='firstPage' || mode=='lastPage') {
		var doc = frames["frameViewData"].document;
		queryString += "&viewPackage.recordCount=" + doc.getElementsByName("viewPackage.recordCount")[0].value;
		queryString += "&viewPackage.pageCount=" + doc.getElementsByName("viewPackage.pageCount")[0].value;
		var pageCount = new Number(doc.getElementsByName("viewPackage.pageCount")[0].value);
		var page = new Number(doc.getElementsByName("viewPackage.curPage")[0].value);
		if(mode=='previousPage') {
			page--;
		}
		else if(mode=='nextPage') {
			page++;
		}
		else if(mode=='firstPage') {
			page = 1;
		}
		else if(mode=='lastPage') {
			page = pageCount;
		}
		if(page<1 || page>pageCount) {
			return;
		}
		queryString += "&viewPackage.curPage=" + page;
	}
	frames["frameViewData"].location = RequestUtils.getContextPath() + "/jeaf/dialog/selectViewData.shtml?" + queryString;
}
//选择分类
function selectCategory(src, applicationName, viewName) { 
	if(document.getElementById('categoryTree').src.indexOf('categoryTree.shtml')==-1) {
		document.getElementById('categoryTree').src = RequestUtils.getContextPath() + '/jeaf/view/categoryTree.shtml?applicationName=' + applicationName + '&viewName=' + viewName;
	}
	src = src.offsetParent;
	var width = src.offsetWidth;
	if(width<180) {
		width=180;
	}
	var pos = DomUtils.getAbsolutePosition(src);
	var left = pos.left + src.offsetWidth - width;
	var top = pos.top + src.offsetHeight - 1;
	var categoryBox = document.getElementById("divSelect");
	var style = categoryBox.style;
	style.left = left<5 ? 5 : left;
	style.top = top;
	style.width = width;
	style.height = 220;
	style.display = "";
	categoryBox.focus();
}
function hideCategory(src, force) { //隐藏分类选择
	var e = document.activeElement;
	if(src.contains(e) && !force) {
		src.focus();
	}
	else {
		src.style.display = "none";
	}
}
//事件:分类切换
function onCategoryChanged(categories) {
	hideCategory(document.getElementById("divSelect"), true);
	if(document.getElementsByName("viewPackage.categories")[0].value==categories) {
		return false;
	}
	document.getElementsByName("viewPackage.categories")[0].value=categories;
	var list = categories.split(",");
	document.getElementById('displayCategory').innerHTML = list[list.length-1].split("|")[0];
	loadSelectViewData();
	return true;
}
function selectRecord(viewPackageName, selected, recordId) { //选中或取消选中记录

}