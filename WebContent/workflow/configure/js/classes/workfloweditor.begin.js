WorkflowEditor.Begin = function(workflowEditor, begin, surface) {
	GraphicsEditor.RoundNode.call(this, workflowEditor, begin, surface, null, 3 * workflowEditor.gridSize, 3 * workflowEditor.gridSize, 1);
	this.workflowEditor = workflowEditor;
	this.activity = begin;
};
WorkflowEditor.Begin.prototype = new GraphicsEditor.RoundNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Begin.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: "开始"};
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Begin.prototype.getPropertyList = function(propertyName) {
	
};