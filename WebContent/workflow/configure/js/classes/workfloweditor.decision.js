WorkflowEditor.Decision = function(workflowEditor, decision, surface) {
	GraphicsEditor.RhombusNode.call(this, workflowEditor, decision, surface, decision.name, 7 * workflowEditor.gridSize, 5 * workflowEditor.gridSize);
	this.workflowEditor = workflowEditor;
	this.activity = decision;
};
WorkflowEditor.Decision.prototype = new GraphicsEditor.RhombusNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Decision.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: this.activity.name};
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Decision.prototype.getPropertyList = function(propertyName) {
	var decisionElement = this;
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.activity.id, false));
	propertyList.push(new GraphicsEditor.TextProperty("name", "名称", this.activity.name, false, function(value) {
		decisionElement.activity.name = value;
		decisionElement.setTitle(decisionElement.activity.name);
	}));
	
	//选择属性,itemsText格式:标题1|值1\0...\0标题n|值n
	var applications = null;
	var itemsText = '';
	var doGetItemsText = function() {
		if(applications!=null) {
			return itemsText;
		}
		applications = [];
		var applicationList = decisionElement.workflowEditor.workflow.applicationList;
		for(var i=0; i < (applicationList ? applicationList.length : 0); i++) {
			var application = ListUtils.findObjectByProperty(decisionElement.workflowEditor.jsonObjectPool, 'uuid', applicationList[i].uuid);
			if(application.type=="decision") {
				applications.push(application);
				itemsText += (itemsText=='' ? '' : '\0') + application.name + '|' + application.id;
			}
		}
		return itemsText;
	};
	var decisionApplication = this.activity.decisionApplication;
	if(decisionApplication) {
		decisionApplication = ListUtils.findObjectByProperty(decisionElement.workflowEditor.jsonObjectPool, 'uuid', decisionApplication.uuid);
	}
	propertyList.push(new GraphicsEditor.DropdownProperty("decisionApplication", "决策依据", decisionApplication ? decisionApplication.id : null, doGetItemsText, true, function(value) {
		var application = ListUtils.findObjectByProperty(applications, "id", value);
		decisionElement.activity.decisionApplication = {uuid: application.uuid};
		if(decisionElement.activity.name.indexOf("决策")==0) {
			decisionElement.activity.name = application.name;
			decisionElement.setTitle(application.name);
		}
	}));
	return propertyList;
};