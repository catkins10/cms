WorkflowEditor.Join = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 5;
	this.rightConnectPointNumber = 3;
	this.topConnectPointNumber = 0;
	this.bottomConnectPointNumber = 0;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 1 * workflowEditor.gridSize, 9 * workflowEditor.gridSize, 0);
	this.fillColor = '#808080';
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Join.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Join.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: '同时完成'};
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Join.prototype.getPropertyList = function(propertyName) {
	
};