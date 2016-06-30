window.onload = function() {
	var recordElement = document.getElementById('record');
	recordElement.setAttribute("onclick", "openTraverse(this)");
	var sql = "select * from road_traverse" +
			  " where sectionId=" + window.top.sectionId +
			  " order by name";
	window.top.database.databaseQuery(sql, 0, 20, recordElement, true);
};
function newTraverse() {
	if(!window.top.sectionId || window.top.sectionId==0) {
		alert("未选定项目和路段，不允许创建导线点。");
		return;
	}
	//window.top.client.openPage(window.location.href.replace("projectList", "project"), window);
	var inputs = [{name:"名称", value:"", dataType:"string", maxLength:10},
				  {name:"X坐标", value:"", dataType:"number", maxLength:12},
				  {name:"Y坐标", value:"", dataType:"number", maxLength:12},
				  {name:"高程", value:"", dataType:"number", maxLength:12}];
	var dialog = window.top.client.openInputDialog("添加导线点", inputs, false, false, "确定", "取消");
	dialog.onOK = function(clientDialogBody) {
		var name = clientDialogBody.getValue("名称");
		if(name!="") {
			var x = Number(clientDialogBody.getValue("X坐标"));
			var y = Number(clientDialogBody.getValue("Y坐标"));
			var z = Number(clientDialogBody.getValue("高程"));
			//保存记录
			var traverse = {projectId:window.top.projectId, sectionId:window.top.sectionId, name: name, x:(isNaN(x) ? 0 : x), y:(isNaN(y) ? 0 : y), z:(isNaN(z) ? 0 : z)};
			window.top.database.saveRecord("road_traverse", traverse);
			//刷新页面
			window.location.reload();
		}
		return true;
	};
}
function openTraverse(traverseElement) {
	var inputs = [{name:"名称", value:DomUtils.getElement(traverseElement, "span", "name").innerHTML, dataType:"string", maxLength:10},
				  {name:"X坐标", value:DomUtils.getElement(traverseElement, "span", "x").innerHTML, dataType:"number", maxLength:12},
				  {name:"Y坐标", value:DomUtils.getElement(traverseElement, "span", "y").innerHTML, dataType:"number", maxLength:12},
				  {name:"高程", value:DomUtils.getElement(traverseElement, "span", "z").innerHTML, dataType:"number", maxLength:12}];
	var dialog = window.top.client.openInputDialog("导线点", inputs, false, false, "保存", "取消", "删除");
	dialog.onOK = function(clientDialogBody) {
		var name = clientDialogBody.getValue("名称");
		if(name!="") {
			var x = Number(clientDialogBody.getValue("X坐标"));
			var y = Number(clientDialogBody.getValue("Y坐标"));
			var z = Number(clientDialogBody.getValue("高程"));
			//更新记录
			var traverse = {id: traverseElement.id, name: name, x:(isNaN(x) ? 0 : x), y:(isNaN(y) ? 0 : y), z:(isNaN(z) ? 0 : z)};
			window.top.database.updateRecord("road_traverse", traverse);
			//更新页面
			window.top.database.updateRecordProperty(document.getElementById('record'), traverse.id, "name", name);
			window.top.database.updateRecordProperty(document.getElementById('record'), traverse.id, "x", traverse.x);
			window.top.database.updateRecordProperty(document.getElementById('record'), traverse.id, "y", traverse.y);
			window.top.database.updateRecordProperty(document.getElementById('record'), traverse.id, "z", traverse.z);
		}
		return true;
	};
	dialog.onOther = function(clientDialogBody) { //删除
		var confirm = window.top.client.openConfirmDialog("删除", "删除后不可恢复，是否确定删除？"); //打开确认对话框
		confirm.onOK = function(clientDialogBody) {
			//删除记录
			window.top.database.deleteRecord("road_traverse", traverseElement.id);
			//更新页面
			window.top.database.removeRecordElement(document.getElementById('record'), traverseElement.id);
			//关闭对话框
			dialog.close();
			return true;
		};
		return false;
	};
}