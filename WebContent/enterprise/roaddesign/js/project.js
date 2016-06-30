window.onload = function() {
	window.projectId = StringUtils.getPropertyValue(window.location.href, "id", "");
	if(window.projectId=='') { //新建,隐藏删除、路段管理按钮
		document.getElementById('button_删除').style.display = 'none';
		document.getElementById('button_路段管理').style.display = 'none';
	}
	else { //编辑
		window.top.client.setTextValue(StringUtils.utf8Decode(StringUtils.getPropertyValue(window.location.href, "name", "")), document.getElementsByName('name')[0]);
	}
};
function saveProject() {
	var name = document.getElementsByName('name')[0].value;
	if(name=='') {
		alert('项目名称不能为空');
		return;
	}
	if(window.projectId=='') { //新建
		//保存记录
		var project = {name: name, created: new Date()};
		window.top.database.saveRecord("road_project", project);
		window.projectId = project.id;
		document.getElementById('button_删除').style.display = '';
		document.getElementById('button_路段管理').style.display = '';
		window.top.client.showToast('保存成功', 2);
		//刷新页面
		window.opener.location.reload();
	}
	else {
		//更新记录
		var project = {id: window.projectId, name: name};
		window.top.database.updateRecord("road_project", project);
		window.top.client.showToast('保存成功', 2);
		//刷新项目页面
		window.top.database.updateRecordProperty(window.opener.document.getElementById('record'), window.projectId, "name", name);
	}
}
function deleteProject() {
	var confirm = window.top.client.openConfirmDialog("删除", "删除后不可恢复，是否确定删除？"); //打开确认对话框
	confirm.onOK = function(clientDialogBody) {
		//删除记录
		window.top.database.deleteRecord("road_project", window.projectId);
		//刷新项目页面
		window.top.database.removeRecordElement(window.opener.document.getElementById('record'), window.projectId);
		//关闭页面
		window.closeWindow();
		return true;
	};
}
function listSections() {
	var url = window.location.href.replace("project", "sectionList");
	url = StringUtils.removeQueryParameter(url, "id,name") + "&projectId=" + window.projectId;
	window.top.client.openPage(url, window);
}