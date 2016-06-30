WorkflowEditor.Procedure = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 3;
	this.rightConnectPointNumber = 3;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 7 * workflowEditor.gridSize, 5 * workflowEditor.gridSize, 6);
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Procedure.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Procedure.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: this.activity.name};
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Procedure.prototype.getPropertyList = function(propertyName) {
	var procedureElement = this;
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.activity.id, false));
	propertyList.push(new GraphicsEditor.TextProperty("name", "名称", this.activity.name, false, function(value) {
		procedureElement.activity.name = value;
		procedureElement.setTitle(procedureElement.activity.name);
	}));
	//选择属性,itemsText格式:标题1|值1\0...\0标题n|值n
	var applications = null;
	var itemsText = '';
	var doGetItemsText = function() {
		if(applications!=null) {
			return itemsText;
		}
		applications = [];
		var applicationList = procedureElement.workflowEditor.workflow.applicationList;
		for(var i=0; i < (applicationList ? applicationList.length : 0); i++) {
			var application = ListUtils.findObjectByProperty(procedureElement.workflowEditor.jsonObjectPool, 'uuid', applicationList[i].uuid);
			if(application.type=="procedure") {
				applications.push(application);
				itemsText += (itemsText=='' ? '' : '\0') + application.name + '|' + application.id;
			}
		}
		return itemsText;
	};
	var procedureApplication = this.activity.procedureApplication;
	if(procedureApplication) {
		procedureApplication = ListUtils.findObjectByProperty(procedureElement.workflowEditor.jsonObjectPool, 'uuid', procedureApplication.uuid);
	}
	propertyList.push(new GraphicsEditor.DropdownProperty("procedureApplication", "执行过程", procedureApplication ? procedureApplication.id : null, doGetItemsText, true, function(value) {
		var application = ListUtils.findObjectByProperty(applications, "id", value);
		procedureElement.activity.procedureApplication = {uuid: application.uuid};
		if(procedureElement.activity.name.indexOf("过程")==0) {
			procedureElement.activity.name = application.name;
			procedureElement.setTitle(application.name);
		}
	}));
	return propertyList;
};