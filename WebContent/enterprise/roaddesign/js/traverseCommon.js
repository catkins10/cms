var traverseFieldNames;
function setTraverseListItems(fieldNames) {
	if(fieldNames) {
		traverseFieldNames = fieldNames;
	}
	//获取导线点列表
	window.top.database.onAfterQuery = function(records) {
		if(!records || records.length<2) {
			return;
		}
		var listItems = "";
		for(var i=1; i<records.length; i++) {
			listItems += (listItems=='' ? '' : '\0') + records[i].name + '<div style=color:#666>X:' + records[i].x + ' Y:' + records[i].y + ' Z:' + records[i].z + '</div>';
		}
		doSetTraverseListItems(listItems);
	};
	var sql = "select name, x, y, z" +
			  " from road_traverse" +
			  " where sectionId=" + window.top.sectionId +
			  " order by name";
	window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function doSetTraverseListItems(listItems) {
	var fieldNames = traverseFieldNames.split(",");
	for(var i=0; i<fieldNames.length; i++) {
		var field = document.getElementsByName(fieldNames[i])[0];
		if(!field) {
			continue;
		}
		DropdownField.setListValues(fieldNames[i], listItems);
		field.onchange = function() {
			var index = this.value.indexOf("<div");
			if(index==-1) {
				return;
			}
			var values = this.value.substring(this.value.indexOf(">", index) + 1, this.value.lastIndexOf('<')).split(" ");
			this.setAttribute("x", values[0].split(":")[1]);
			this.setAttribute("y", values[1].split(":")[1]);
			this.setAttribute("z", values[2].split(":")[1]);
			DropdownField.getDropdownField(this.name).getField(true).value = this.value = this.value.substring(0, index);
		};
	}
}
function addTraverse(name, x, y, z) { //添加导线点
	if(name!="") {
		doAddTraverse(name, x, y, z);
		return;
	}
	var dialog = window.top.client.openInputDialog("导线点", [{name:"名称", value:"", dataType:"string", maxLength:10}], false, false, "确定", "取消");
	dialog.onOK = function(clientDialogBody) {
		var name = clientDialogBody.getValue("名称");
		if(name!="") {
			doAddTraverse(name, x, y, z);
		}
		return true;
	};
}
function doAddTraverse(name, x, y, z) { //执行添加导线点
	//保存记录
	var traverse = {projectId:window.top.projectId, sectionId:window.top.sectionId, name:name, x:x, y:y, z:z};
	window.top.database.saveRecord("road_traverse", traverse);
	//重设下拉选项列表
	setTraverseListItems();
}