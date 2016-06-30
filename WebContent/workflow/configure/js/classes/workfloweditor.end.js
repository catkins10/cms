WorkflowEditor.End = function(workflowEditor, activity, surface) {
	GraphicsEditor.RoundNode.call(this, workflowEditor, activity, surface, null, 3 * workflowEditor.gridSize, 3 * workflowEditor.gridSize, 2);
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.End.prototype = new GraphicsEditor.RoundNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.End.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: "结束"};
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.End.prototype.getPropertyList = function(propertyName) {
	
};