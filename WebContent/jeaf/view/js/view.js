var CONDITION_CLASS_NAME = "com.yuanluesoft.jeaf.view.model.search.Condition";
function gotopage(viewPackageName, flag, event) { //页跳转
	var page;
	if(flag=="keypress") {
		event = event ? event : window.event;
		if(event.keyCode!=13)return;
	}
	if(flag=="next" || flag=="prev" || flag=="last" || flag=="first") {
		FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + '.currentViewAction=gotoPage&viewPackageName=' + viewPackageName + "&" + viewPackageName + '.page=' + flag);
	}
	else {
		page = Number(event.srcElement.value);
		if(!isNaN(page)) {
			FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + '.currentViewAction=gotoPage&viewPackageName=' + viewPackageName + "&" + viewPackageName + '.page=' + page);
		}
	}
}
function switchDate(viewPackageName, flag) { //切换日期
	var mode = document.getElementsByName(viewPackageName + ".calendarMode")[0].value;
	var field = document.getElementsByName(viewPackageName + ".beginCalendarDate")[0];
	if(flag=="prev" || flag=="next") {
		var dateValue = new Date(field.value.replace(new RegExp("-", "g"), "/"));
		if(mode=="month") {
			dateValue = dateValue.setMonth(dateValue.getMonth() + (flag=="next" ? 1 : - 1));
		}
		else if(mode=="week") {
			dateValue = dateValue.setDate(dateValue.getDate() + (flag=="next" ? 7 : - 7));
		}
		else if(mode=="day") {
			dateValue = dateValue.setDate(dateValue.getDate() + (flag=="next" ? 1 : - 1));
		}
		dateValue = new Date(dateValue);
		field.value = dateValue.getFullYear() + "-" + (dateValue.getMonth() + 1) + "-" + dateValue.getDate();
	}
	else {
		field.value = flag;
	}
	FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + '.currentViewAction=refreshView');
}
function printAsExcel(viewPackageName) {
	document.forms[0].target = "_blank";
	FormUtils.doAction(FormUtils.getCurrentAction(), (viewPackageName ? viewPackageName : 'viewPackage') + '.currentViewAction=printAsExcel');
	window.setTimeout("document.forms[0].removeAttribute('target')", 1000);
}
function printAsPdf(viewPackageName) {
	document.forms[0].target = "_blank";
	FormUtils.doAction(FormUtils.getCurrentAction(), (viewPackageName ? viewPackageName : 'viewPackage') + '.currentViewAction=printAsPdf');
	window.setTimeout("document.forms[0].removeAttribute('target')", 1000);
}

//分类相关
function selectCategory(td, applicationName, viewName, currentCategories) { //选择分类
	if(document.getElementById('categoryTree').src.indexOf('categoryTree.shtml')==-1) {
		var queryString = document.getElementsByName('queryString');
		queryString = queryString && queryString.length>0 ? queryString[0].value : "";
		document.getElementById('categoryTree').src = RequestUtils.getContextPath() + '/jeaf/view/categoryTree.shtml?applicationName=' + applicationName + '&viewName=' + viewName + '&currentCategories=' + StringUtils.utf8Encode(currentCategories) + (queryString=="" ? "" : "&" + queryString);
	}
	var previousTd = td.previousSibling;
	if(previousTd.tagName!="TD") {
		previousTd = previousTd.previousSibling;
	}
	var width = previousTd.offsetWidth + 17;
	if(width<180) {
		width = 180;
	}
	var pos = DomUtils.getAbsolutePosition(td);
	var left = pos.left + td.offsetWidth - width;
	var top = pos.top + td.offsetHeight;
	
	var categoryBox = document.getElementById("divCategoryTree");
	var style = categoryBox.style;
	style.left = left<5 ? 5 : left;
	style.top = top;
	style.width = width;
	style.height = 220;
	style.display = "";
	categoryBox.focus();
}
function hideCategory(src) { //隐藏分类选择
	var e = document.activeElement;
	if(src.contains(e)) {
		src.focus();
	}
	else {
		src.style.display = "none";
	}
}
function onCategoryChanged(categories) { //分类改变
	if(document.getElementsByName("viewPackage.categories")[0].value!=categories) {
		document.getElementsByName("viewPackage.categories")[0].value = categories;
		FormUtils.doAction(FormUtils.getCurrentAction(), 'viewPackage.currentViewAction=opencategory');
	}
}

function customView() { //视图定制
	DialogUtils.openDialog(RequestUtils.getContextPath() + "/jeaf/view/customView.shtml?applicationName=" + document.getElementsByName("viewPackage.applicationName")[0].value + "&viewName=" + document.getElementsByName("viewPackage.viewName")[0].value, 680, 450);
}
function selectAll(viewPackageName) { //全部选择
	var selects = document.getElementsByName(viewPackageName + ".select");
	var checked = document.getElementById(viewPackageName + ".selectAllBox").checked;
	for(var i=0; i<selects.length; i++) {
		selects[i].checked = checked;
		selectRecord(viewPackageName, checked, selects[i].value);
	}
}
function selectRecord(viewPackageName, selected, recordId) { //选中或取消选中记录
	var selectedIdsField = document.getElementsByName(viewPackageName + ".selectedIds")[0];
	var selectedIds = selectedIdsField.value=="" ? "," : "," + selectedIdsField.value + ",";
	if(!selected) { //从选中ID列表中删除
		selectedIds = selectedIds.replace("," + recordId + ",", ",");
	}
	else if(selectedIds.indexOf("," + recordId + ",")==-1) {
		selectedIds += recordId + ","; //添加到选中ID列表
	}
	selectedIdsField.value = selectedIds=="," ? "" : selectedIds.substring(1, selectedIds.length-1);
}
function initViewSelectBox(viewPackageName) { //初始化视图选择框
	var selectedIds = document.getElementsByName(viewPackageName + ".selectedIds")[0].value;
	if(selectedIds && selectedIds!="") {
		selectedIds += ",";
		var selects = document.getElementsByName(viewPackageName + ".select");
		for(var i=0; i<selects.length; i++) {
			if(selectedIds.indexOf(selects[i].value + ",")!=-1) {
				selects[i].checked = true;
			}
		}
	}
}
function sort(viewPackageName, columnName) { //排序
	if(!viewPackageName) {
		viewPackageName = "viewPackage";
	}
	document.getElementsByName(viewPackageName + ".selectedIds")[0].value = "";
	FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + '.currentViewAction=sort&' + viewPackageName + '.sortColumn=' + columnName);
}
function refreshView(viewPackageName) { //刷新视图
	if(!viewPackageName) {
		viewPackageName = "viewPackage";
	}
	document.getElementsByName(viewPackageName + ".selectedIds")[0].value = "";
	FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + '.currentViewAction=refreshView');
}
function batchSend(actionName, viewPackageName) { //批量发送
	if(!viewPackageName) {
		viewPackageName = "viewPackage";
	}
	var selectedIds = document.getElementsByName(viewPackageName + ".selectedIds")[0].value;
	if(selectedIds=="") {
		return;
	}
	var index = selectedIds.indexOf(",");
	var firstRecordId = (index==-1 ? selectedIds : selectedIds.substring(0, index));
	DialogUtils.openDialog(RequestUtils.getContextPath() + "/" + document.getElementsByName(viewPackageName + ".applicationName")[0].value + "/" + actionName + ".shtml?workflowAction=batchSend&displayMode=dialog&id=" + firstRecordId + "&batchIds=" + selectedIds, 500, 300);
}
function quickFilter(viewPackageName) { //快速搜索
	var field = document.getElementsByName(viewPackageName + ".quickFilter")[0];
	if(field.value!="") {
		if(field.value.indexOf("')")!=-1) {
			alert("搜索条件不能包含')");
			return;
		}
		var value = FieldValidator.validateStringField(field, "&,?,="); //快速过滤,quickFilterKey中不能包含&?=
		if(value!="NaN") {
			var searchCondition = {uuid: CONDITION_CLASS_NAME + '@0'};
			searchCondition.expression = "key";
			searchCondition.value1 = value;
			var searchConditions = {uuid:'java.util.ArrayList@0', collection:[{uuid:searchCondition.uuid}], objects:[searchCondition]};
			document.getElementsByName(viewPackageName + ".searchConditions")[0].value = JsonUtils.stringify(searchConditions);
			document.getElementsByName(viewPackageName + ".selectedIds")[0].value = "";
			FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + ".currentViewAction=search&viewPackageName=" + viewPackageName);
		}
	}
}
function switchToSearch(viewPackageName) { //转到搜索
	FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + ".currentViewAction=switchToSearch&viewPackageName=" + viewPackageName);
}
function finishSearch(viewPackageName) { //结束搜索
	document.getElementsByName(viewPackageName + ".selectedIds")[0].value = "";
	FormUtils.doAction(FormUtils.getCurrentAction(), viewPackageName + ".currentViewAction=finishSearch&viewPackageName=" + viewPackageName);
}