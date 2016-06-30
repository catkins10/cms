window.onload = function() {
	window.projectId = StringUtils.getPropertyValue(window.location.href, "projectId", "");
	window.sectionId = StringUtils.getPropertyValue(window.location.href, "id", "");
	if(window.sectionId=='') { //新建,隐藏删除、设为当前路段按钮
		document.getElementById('button_删除').style.display = 'none';
		document.getElementById('button_设为当前路段').style.display = 'none';
	}
	else { //编辑
		window.top.client.setTextValue(StringUtils.utf8Decode(StringUtils.getPropertyValue(window.location.href, "name", "")), document.getElementsByName('name')[0]);
	}
};
function saveSection() {
	var name = document.getElementsByName('name')[0].value;
	if(name=='') {
		alert('路段名称不能为空');
		return;
	}
	if(window.sectionId=='') { //新建
		//保存记录
		var section = {projectId: window.projectId, name: name, created: new Date()};
		window.top.database.saveRecord("road_project_section", section);
		window.sectionId = section.id
		document.getElementById('button_删除').style.display = '';
		document.getElementById('button_设为当前路段').style.display = '';
		window.top.client.showToast('保存成功', 2);
		//刷新页面
		window.opener.location.reload();
		//设置当前路段
		setAsCurrentSection();
	}
	else {
		//更新记录
		var section = {id: window.sectionId, name: name};
		window.top.database.updateRecord("road_project_section", section);
		window.top.client.showToast('保存成功', 2);
		//更新页面
		window.top.database.updateRecordProperty(window.opener.document.getElementById('record'), window.sectionId, "name", name);
	}
}
function deleteSection() {
	var confirm = window.top.client.openConfirmDialog("删除", "删除后不可恢复，是否确定删除？"); //打开确认对话框
	confirm.onOK = function(clientDialogBody) {
		//删除记录
		window.top.database.deleteRecord("road_project_section", window.sectionId);
		//更新页面
		window.top.database.removeRecordElement(window.opener.document.getElementById('record'), window.sectionId);
		//关闭路段窗口
		window.closeWindow();
		return true;
	};
}
function setAsCurrentSection() {
	window.top.client.callNativeMethod("setPreference('currentSection', '" + window.sectionId + "')");
	window.top.writeCurrentSection(window.sectionId);
	window.top.client.showToast('设置成功', 2);
};