//分组
EAIEditor.Group = function(eaiEditor, group, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, group, surface, group.name, 15 * eaiEditor.gridSize, 3 * eaiEditor.gridSize, 0);
	this.eaiEditor = eaiEditor;
	this.group = group;
};
EAIEditor.Group.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
EAIEditor.Group.prototype.getPropertyTree = function() {
	return {id: this.group.id, label: this.group.name};
};
//抽象方法(由继承者实现):获取属性列表
EAIEditor.Group.prototype.getPropertyList = function(propertyName) {
	var groupElement = this;
	return [new GraphicsEditor.TextProperty("name", "分组名称", this.group.name, this.surface.readOnly || this.surface.configOnly, function(value) {
		groupElement.group.name = value;
		groupElement.setTitle(groupElement.group.name);
	})];
};