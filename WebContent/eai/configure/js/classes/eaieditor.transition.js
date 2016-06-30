//连接
EAIEditor.Transition = function(eaiEditor, transition, surface) {
	GraphicsEditor.Line.call(this, eaiEditor, transition, surface, false, 0, null);
};
EAIEditor.Transition.prototype = new GraphicsEditor.Line();
//抽象方法(由继承者实现):获取属性目录树
EAIEditor.Transition.prototype.getPropertyTree = function() {
	return {id: this.transition.id, label: '连接'};
};
//抽象方法(由继承者实现):获取属性列表
EAIEditor.Transition.prototype.getPropertyList = function(propertyName) {
	
};