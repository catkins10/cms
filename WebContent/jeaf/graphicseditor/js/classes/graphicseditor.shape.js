/**
 * 形状
 * 参数 graphicsEditor: 图形编辑器
 * 参数 surface:外观,定义:{id: [ID], startX: [起点X坐标], startY: [起点Y坐标], endX: [终点Y坐标], endY: [终点Y坐标], readOnly: [是否只读], configOnly: [是否只允许配置], deleteDisable: [是否禁止删除], moveDisable: [是否禁止移动], displayOnly: [是否只显示,不允许配置]}
 **/
GraphicsEditor.Shape = function(graphicsEditor, surface, title) {
	if(!graphicsEditor) {
		return;
	}
	this.focusRadii = 2; //焦点半径
	this.connectRadii = 1; //连接点半径
	this.fillColor = null; //填充颜色
	this.lineColor = null; //边框颜色
	this.textColor = null; //文本颜色
	
	this.graphicsEditor = graphicsEditor; //图形编辑器
	this.surface = surface; //外观
	this.title = title; //标题
	
	//最小尺寸
	this.minWidth = 2 * graphicsEditor.gridSize;
	this.minHeight = 2 * graphicsEditor.gridSize;
	
	//默认尺寸
	this.defaultWidth = 10 * graphicsEditor.gridSize;
	this.defaultHeight = 6 * graphicsEditor.gridSize;
	
	//显示属性
	this.focusPoints = []; //焦点列表
	this.connectPoints = []; //连接点列表
	this.focusIndex = -1; //鼠标按下时所在的焦点序号，-1表示不在焦点上
	this.selected = false; //是否被选中
	this.near = false; //创建连接时，鼠标位置是否接近本元素
	this.resetDisplayProperties('init');
};
//抽象方法(由继承者实现):绘制
GraphicsEditor.Shape.prototype.draw = function(canvasContext) {
	
};
//抽象方法(由继承者实现):输出标题
GraphicsEditor.Shape.prototype.drawTitle = function(canvasContext) {

};
//抽象方法(由继承者实现):在元素位置变更时重置元素显示属性（包括焦点信息、连接点信息等）
GraphicsEditor.Shape.prototype.resetDisplayProperties = function(resetFor) {
	
};
//抽象方法(由继承者实现):鼠标是否移动到本元素上
GraphicsEditor.Shape.prototype.isMouseOver = function(x, y) {
	
};
//抽象方法(由继承者实现):是否与rect相交
GraphicsEditor.Shape.prototype.intersects = function(left, top, right, bottom) {
	
};
//抽象方法(由继承者实现):尺寸调整
GraphicsEditor.Shape.prototype.move = function(startX, startY, endX, endY) {
	
};
//抽象方法(由继承者实现):根据mode指定方式调整尺寸
GraphicsEditor.Shape.prototype.offset = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	
};
//抽象方法(由继承者实现):获取显示区域{left, top, right, bottom}
GraphicsEditor.Shape.prototype.getDisplayArea = function() {
	
};
//抽象方法(由继承者实现):获取属性目录树
GraphicsEditor.Shape.prototype.getPropertyTree = function() {
	
};
//抽象方法(由继承者实现):获取属性列表
GraphicsEditor.Shape.prototype.getPropertyList = function(propertyName) {
	
};
//获取左侧坐标
GraphicsEditor.Shape.prototype.getLeft = function() {
	return Math.min(this.surface.startX, this.surface.endX);
};
//获取顶部坐标
GraphicsEditor.Shape.prototype.getTop = function() {
	return Math.min(this.surface.startY, this.surface.endY);
};
//获取右侧坐标
GraphicsEditor.Shape.prototype.getRight = function() {
	return Math.max(this.surface.startX, this.surface.endX);
};
//获取底部坐标
GraphicsEditor.Shape.prototype.getBottom = function() {
	return Math.max(this.surface.startY, this.surface.endY);
};
//获取宽度
GraphicsEditor.Shape.prototype.getWidth = function() {
	return Math.abs(this.surface.startX - this.surface.endX);
};
//获取高度
GraphicsEditor.Shape.prototype.getHeight = function() {
	return Math.abs(this.surface.startY - this.surface.endY);
};
//创建新元素（单击鼠标）
GraphicsEditor.Shape.prototype.create = function(x, y, connectElement, connectIndex) {
	this.surface.startX = x;
	this.surface.startY = y;
	this.surface.endX = x;
	this.surface.endY = y;
	this.selected = true;
	this.resetDisplayProperties('create');
};
//修改元素尺寸或位置（移动鼠标）
GraphicsEditor.Shape.prototype.creating = function(x, y, connectElement, connectIndex) {
	if(this.surface.endX==x && this.surface.endY==y) {
		return false;
	}
	this.surface.endX = x;
	this.surface.endY = y;
	this.resetDisplayProperties('creating');
	return true;
};
//完成创建新元素（放开鼠标）,如果尺寸小于最小尺寸,返回false
GraphicsEditor.Shape.prototype.createComplete = function(x, y, connectElement, connectIndex) {
	this.surface.endX = x;
	this.surface.endY = y;
	var flag = true;
	if(this.getWidth() < this.minWidth && this.getHeight() < this.minHeight) {
		this.surface.endX = this.surface.startX + this.defaultWidth;
		this.surface.endY = this.surface.startY + this.defaultHeight;
		flag = false;
	}
	this.resetDisplayProperties('createComplete');
	return flag;
};
//取消选中
GraphicsEditor.Shape.prototype.deselect = function() {
	if(this.selected) {
		this.selected = false;
		return true;
	}
	return false;
};
//获取边框颜色
GraphicsEditor.Shape.prototype.getLineColor = function() {
	return this.lineColor!=null ? this.lineColor : (this.surface.readOnly ? this.graphicsEditor.readOnlyColor : this.graphicsEditor.lineColor);
};
//获取填充颜色
GraphicsEditor.Shape.prototype.getFillColor = function() {
	return this.fillColor!=null ? this.fillColor : this.graphicsEditor.bgColor;
};
//设置文本颜色
GraphicsEditor.Shape.prototype.getTextColor = function() {
	return this.textColor!=null ? this.textColor : (this.surface.readOnly ? this.graphicsEditor.readOnlyColor : this.graphicsEditor.textColor);
};
//获取字符串输出时的尺寸,font示例:12px 宋体 
GraphicsEditor.Shape.prototype.measureText = function(canvasContext, str, font) {
	canvasContext.font = font;
	return {width:canvasContext.measureText(str).width, height:canvasContext.measureText('宪').width};
};
//绘制矩形
GraphicsEditor.Shape.prototype.drawRect = function(canvasContext, startX, startY, endX, endY, lineColor, fillColor, lineWidth, radius) {
	var left = Math.min(startX, endX) - 0.5;
	var top = Math.min(startY, endY) - 0.5;
	var width = Math.abs(startX - endX);
	var height = Math.abs(startY - endY);
	if(width < 2 * radius) {
		radius = width / 2;
	}
	if(height < 2 * radius) {
		radius = height / 2;
	}
	canvasContext.beginPath();
	canvasContext.moveTo(left + radius, top);
	canvasContext.arcTo(left + width, top, left + width, top + height, radius);
	canvasContext.arcTo(left + width, top + height, left, top + height, radius);
	canvasContext.arcTo(left, top + height, left, top, radius);
	canvasContext.arcTo(left, top, left + width, top, radius);
	canvasContext.lineWidth = lineWidth;
	canvasContext.fillStyle = fillColor;
	canvasContext.fill();
	canvasContext.strokeStyle = lineColor;
	canvasContext.stroke();
	canvasContext.closePath();
};
//绘制焦点
GraphicsEditor.Shape.prototype.drawFocus = function(canvasContext) {
	for(var i = this.focusPoints.length - 1; i>=0; i--) {
		this.drawRect(canvasContext, this.focusPoints[i].x - this.focusRadii, this.focusPoints[i].y - this.focusRadii, this.focusPoints[i].x + this.focusRadii, this.focusPoints[i].y + this.focusRadii, this.graphicsEditor.focusColor, this.graphicsEditor.bgColor, 1, 0);
	}
};
//绘制连接点
GraphicsEditor.Shape.prototype.drawConnect = function(canvasContext) {
	for(var i = this.connectPoints.length - 1; i >= 0; i--) {
		this.drawRect(canvasContext, this.connectPoints[i].x - this.connectRadii, this.connectPoints[i].y - this.connectRadii, this.connectPoints[i].x + this.connectRadii, this.connectPoints[i].y + this.connectRadii, this.graphicsEditor.connectColor, this.graphicsEditor.connectColor, 1, 0);
	}
};
//删除本元素
GraphicsEditor.Shape.prototype.deleteElement = function() {
	if(!this.selected || this.surface.deleteDisable) {
		return false;
	}
	this.graphicsEditor.removeElement(this);
	return true;
};
//获取鼠标光标样式 
GraphicsEditor.Shape.prototype.getCursor = function(x, y) {
	if(this.surface.readOnly) {
		return "default";
	}
	if(this.selected) {
		this.focusIndex = -1;
		for(var i = this.focusPoints.length - 1; i>=0; i--) {
			if(this.isMouseOverFocus(this.focusPoints[i], x, y)) {
				this.focusIndex = i;
				return this.focusPoints[i].cursor;
			}
		}
		if(this.isMouseOver(x, y, 0)) {
			return "move";
		}
	}
	else if(this.isMouseOver(x, y, 0)) {
		return "pointer";
	}
	return "default";
};
//检查坐标是否位于焦点内
GraphicsEditor.Shape.prototype.isMouseOverFocus = function(focus, x, y) {
	x -= focus.x;
	y -= focus.y;
	return x >= this.connectRadii - 3 && y >= this.connectRadii - 3 && x <= this.connectRadii + 3 && y <= this.connectRadii + 3;
};
//设为选中或者非选中
GraphicsEditor.Shape.prototype.setSelected = function(selected) {
	if(this.selected!=selected) {
		this.selected = selected;
		this.resetDisplayProperties(selected ? 'select' : 'deselect');
	}
};
//设置标题
GraphicsEditor.Shape.prototype.setTitle = function(title) {
	if(title==this.title) {
		return;
	}
	this.title = title;
	this.graphicsEditor.redrawElement(this);
	this.graphicsEditor.showPropertyTree();
};