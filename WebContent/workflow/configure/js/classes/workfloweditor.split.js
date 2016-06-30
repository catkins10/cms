WorkflowEditor.Split = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 3;
	this.rightConnectPointNumber = 5;
	this.topConnectPointNumber = 0;
	this.bottomConnectPointNumber = 0;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 1 * workflowEditor.gridSize, 9 * workflowEditor.gridSize, 0);
	this.fillColor = '#D3D3D3';
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Split.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Split.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: '同时启动'};
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Split.prototype.getPropertyList = function(propertyName) {
	
};