//节点,从GraphicsEditorShape继承
GraphicsEditor.Node = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight) {
	if(!graphicsEditor) {
		return;
	}
	this.leftConnectPointNumber = this.leftConnectPointNumber || this.leftConnectPointNumber==0 ? this.leftConnectPointNumber : 1;
	this.rightConnectPointNumber = this.rightConnectPointNumber || this.rightConnectPointNumber==0 ? this.rightConnectPointNumber : 1;
	this.topConnectPointNumber = this.topConnectPointNumber || this.topConnectPointNumber==0 ? this.topConnectPointNumber : 1;
	this.bottomConnectPointNumber = this.bottomConnectPointNumber || this.bottomConnectPointNumber==0 ? this.bottomConnectPointNumber : 1;
	this.exitLines = []; //起点为当前节点的线条
	this.enterLines = []; //终点为当前节点的线条
	GraphicsEditor.Shape.call(this, graphicsEditor, surface, title);
	this.model = model;
	this.defaultWidth = defaultWidth;
	this.defaultHeight = defaultHeight;
};
GraphicsEditor.Node.prototype = new GraphicsEditor.Shape();
//在元素位置变更时重置元素属性（包括焦点信息、连接点信息等）
GraphicsEditor.Node.prototype.resetDisplayProperties = function(resetFor) {
	//设置焦点信息
	var width = (this.surface.endX - this.surface.startX) / 2;
	var height = (this.surface.endY - this.surface.startY) / 2;
	var north, south, east, west;
	if(this.surface.startX < this.surface.endX) {
		west = 0;
		east = 1;
	}
	else {
		east = 0;
		west = 1;
	}
	if(this.surface.startY < this.surface.endY) {
		north = 0;
		south = 1;
	}
	else {
		south = 0;
		north = 1;
	}
	var cursors = ['nw-resize', 'ne-resize', 'sw-resize', 'se-resize', 'w-resize', 'e-resize', 'n-resize', 's-resize'];
	this.focusPoints.length = 0;
	this.focusPoints.push({x:this.surface.startX, y:this.surface.startY, cursor:cursors[north * 2 + west], resizeMode:'nw-resize'});
	this.focusPoints.push({x:this.surface.startX, y:this.surface.startY + height, cursor:cursors[west + 4], resizeMode:'w-resize'});
	this.focusPoints.push({x:this.surface.startX, y:this.surface.endY, cursor:cursors[south * 2 + west], resizeMode:'sw-resize'});
	this.focusPoints.push({x:this.surface.startX + width, y:this.surface.endY, cursor:cursors[south + 6], resizeMode:'s-resize'});
	this.focusPoints.push({x:this.surface.endX, y:this.surface.endY, cursor:cursors[south * 2 + east], resizeMode:'se-resize'});
	this.focusPoints.push({x:this.surface.endX, y:this.surface.startY + height, cursor:cursors[east + 4], resizeMode:'e-resize'});
	this.focusPoints.push({x:this.surface.endX, y:this.surface.startY, cursor:cursors[north * 2 + east], resizeMode:'ne-resize'});
	this.focusPoints.push({x:this.surface.startX + width, y:this.surface.startY, cursor:cursors[north + 6], resizeMode:'n-resize'});
	//设置连接点信息
	if(!this.surface.readOnly && !this.surface.configOnly) {
		this.connectPoints.length = 0;
		for(var i = 1; i <= this.leftConnectPointNumber; i++) {
			this.connectPoints.push({x:this.surface.startX, y:this.surface.startY + (this.surface.endY - this.surface.startY) / (this.leftConnectPointNumber + 1) * i});
		}
		for(var i = 1; i <= this.bottomConnectPointNumber; i++) {
			this.connectPoints.push({x:this.surface.startX + (this.surface.endX - this.surface.startX) / (this.bottomConnectPointNumber + 1) * i, y:this.surface.endY});
		}
		for(var i = 1; i <= this.rightConnectPointNumber; i++) {
			this.connectPoints.push({x:this.surface.endX, y:this.surface.startY + (this.surface.endY - this.surface.startY) / (this.rightConnectPointNumber + 1) * i});
		}
		for(var i = 1; i <= this.topConnectPointNumber; i++) {
			this.connectPoints.push({x:this.surface.startX + (this.surface.endX - this.surface.startX) / (this.topConnectPointNumber + 1) * i, y:this.surface.startY});
		}
	}
};
//鼠标是否移动到本元素上
GraphicsEditor.Node.prototype.isMouseOver = function(x, y, offset) {
	return this.getLeft() - offset <= x && this.getRight() + offset >= x && this.getTop() - offset <= y && this.getBottom() + offset >= y;
};
//是否与rect相交
GraphicsEditor.Node.prototype.intersects = function(left, top, right, bottom) {
	return this.getLeft() <= right && this.getRight() >= left && this.getTop() <= bottom && this.getBottom() >= top;
};
//尺寸调整
GraphicsEditor.Node.prototype.move = function(startX, startY, endX, endY) {
	var offsetX = endX - startX;
	var offsetY = endY - startY;
	if(this.surface.moveDisable) {
		return {x:offsetX, y:offsetY};
	}
	offsetX -= offsetX % this.graphicsEditor.gridSize;
	offsetY -= offsetY % this.graphicsEditor.gridSize;
	if(offsetX==0 && offsetY==0) {
		return {x:offsetX, y:offsetY};
	}
	if(this.focusIndex==-1) { //位置移动（鼠标点中元素本身）
		this.graphicsEditor.offsetElements(offsetX, offsetY, offsetX, offsetY, 'move');
		return {x:offsetX, y:offsetY};
	}
	switch(this.focusPoints[this.focusIndex].resizeMode) {
		case 'n-resize': //向上扩大
			this.graphicsEditor.offsetElements(0, offsetY, 0, 0, 'n-resize');
			break;
		case 's-resize': //向下扩大
			this.graphicsEditor.offsetElements(0, 0, 0, offsetY, 's-resize');
			break;
		case 'w-resize': //向左扩大
			this.graphicsEditor.offsetElements(offsetX, 0, 0, 0, 'w-resize');
			break;
		case 'e-resize': //向右扩大
			this.graphicsEditor.offsetElements(0, 0, offsetX, 0, 'e-resize');
			break;
		case 'nw-resize': //向左上扩大
			this.graphicsEditor.offsetElements(offsetX, offsetY, 0, 0, 'nw-resize');
			break;
		case 'sw-resize': //向左下扩大
			this.graphicsEditor.offsetElements(offsetX, 0, 0, offsetY, 'sw-resize');
			break;
		case 'ne-resize': //向右上扩大
			this.graphicsEditor.offsetElements(0, offsetY, offsetX, 0, 'ne-resize');
			break;
		case 'se-resize': //向右下扩大
			this.graphicsEditor.offsetElements(0, 0, offsetX, offsetY, 'se-resize');
			break;
	}
	return {x:offsetX, y:offsetY};
};
//根据mode指定方式调整尺寸
GraphicsEditor.Node.prototype.offset = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	if(this.surface.moveDisable) {
		return;
	}
	this.surface.startX += offsetLeft;
	this.surface.endX += offsetRight;
	this.surface.startY += offsetTop;
	this.surface.endY += offsetBottom;
	this.resetDisplayProperties('offset');
	//移动连接线
	var element;
	for(var i = 0; i < this.exitLines.length; i++) {
		if(mode!='move' || !this.exitLines[i].selected || ((element=this.exitLines[i].toElement) && !element.selected)) {
			this.exitLines[i].moveStartPoint(this.connectPoints[this.exitLines[i].surface.enterIndex]);
		}
	}
	for(var i = 0; i < this.enterLines.length; i++) {
		if(mode!='move' || !this.enterLines[i].selected || ((element=this.enterLines[i].fromElement) && !element.selected)) {
			this.enterLines[i].moveEndPoint(this.connectPoints[this.enterLines[i].surface.exitIndex]);
		}
	}
};
//抽象方法(由继承者实现):输出标题
GraphicsEditor.Node.prototype.drawTitle = function(canvasContext) {
	if(this.title==null) {
		return;
	}
	var x = this.getLeft();
	var y = this.getTop();
	var width = this.getWidth();
	var height = this.getHeight();
	var size = this.measureText(canvasContext, this.title, this.graphicsEditor.font);
	if(height < size.height) {
		return; //高度不足一行
	}
	if(width <= 4 * this.graphicsEditor.gridSize && height / width > 2) { //竖向显示
	    var heightTotal = 0;
	    var i = 0;
	    for(; i < this.title.length; i++) {
		    size = this.measureText(canvasContext, this.title.substring(i, i+1), this.graphicsEditor.font);
		    if(width < size.width) {
		        return; //宽度不足一个字
		    }
		    if(heightTotal + size.height > height) {
		        break;
		    }
		    heightTotal += size.height + 2;
		}
		y += (height - heightTotal) / 2;
		canvasContext.beginPath();
		for(var k = 0; k < i; k++) {
		    size = this.measureText(canvasContext, this.title.substring(k, k + 1), this.graphicsEditor.font);
			canvasContext.fillStyle = this.getTextColor();
		    canvasContext.fillText(this.title.substring(k, k + 1), x + (width - size.width) / 2 + 1, y + size.height);
		    y += size.height + 2;
		}
		canvasContext.closePath();
	    return;
	}
	if(width - 2 > size.width) {
		canvasContext.beginPath();
		canvasContext.fillStyle = this.getTextColor();
		canvasContext.fillText(this.title, x + (width - size.width) / 2, y + (height - size.height) / 2 + size.height - 1);
		canvasContext.closePath();
		return;
	}
	//分行显示
	var list = [];
	var lineStr = "";
	var heightTotal = 0;
	var i = 0;
	for(; i < this.title.length; i++) {
	    lineStr += this.title.substring(i, i+1);
	    size = this.measureText(canvasContext, lineStr, this.graphicsEditor.font);
	    if(heightTotal + size.height + 2 > height) {
	        lineStr = list[list.length-1];
	        for(var j = 1; j <= Math.min(lineStr.length, 3); j++) {
	            var newStr = lineStr.substring(0, lineStr.length - j) + "...";
	            size = this.measureText(canvasContext, newStr, this.graphicsEditor.font);
	            if(size.width < width) {
	                list[list.length-1] = newStr;
	                break;
	            }
	        }
	        break;
	    }
	    if(size.width > width) {
	        if(lineStr.length==1) {
	            return; //宽度不足一列
	        }
	        lineStr = lineStr.substring(0, lineStr.length - 1);
	        heightTotal += this.measureText(canvasContext, lineStr, this.graphicsEditor.font).height + 2;
	        list.push(lineStr);
	        lineStr = "";
	        i--;
	    }
	}
	if(i>=this.title.length) {
	    heightTotal += this.measureText(canvasContext, lineStr, this.graphicsEditor.font).height + 2;
	    list.push(lineStr);
	}
	y += (height - heightTotal) / 2;
	canvasContext.beginPath();
	canvasContext.fillStyle = this.getTextColor();
	for(i=0; i < list.length; i++) {
	    size = this.measureText(canvasContext, list[i], this.graphicsEditor.font);
	    canvasContext.fillText(list[i], x + (width - size.width) / 2 + 1, y + size.height);
	    y += size.height + 2;
	}
	canvasContext.closePath();
};
//抽象方法(由继承者实现):获取显示区域{left, top, right, bottom}
GraphicsEditor.Node.prototype.getDisplayArea = function() {
	return {left:this.getLeft(), top:this.getTop(), right:this.getRight(), bottom:this.getBottom()};
};
//删除本元素,从父类继承
GraphicsEditor.Node.prototype.deleteElement = function() {
	if(!GraphicsEditor.Shape.prototype.deleteElement.call(this)) {
		return;
	}
	for(var i = 0; i < this.exitLines.length; i++) {
		this.exitLines[i].setFromElement(null, -1);
	}
	for(var i = 0; i < this.enterLines.length; i++) {
		this.enterLines[i].setToElement(null, -1);
	}
	return true;
};
//添加起点为当前节点的线条
GraphicsEditor.Node.prototype.addExitLine = function(line) {
	this.exitLines.push(line);
};
//添加终点为当前节点的线条
GraphicsEditor.Node.prototype.addEnterLine = function(line) {
	this.enterLines.push(line);
};
//删除起点为当前节点的线条
GraphicsEditor.Node.prototype.removeExitLine = function(line) {
	ListUtils.removeObject(this.exitLines, line);
};
//删除终点为当前节点的线条
GraphicsEditor.Node.prototype.removeEnterLine = function(line) {
	ListUtils.removeObject(this.enterLines, line);
};

//矩形,从GraphicsEditor.Node继承
GraphicsEditor.RectNode = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight, radius) {
	GraphicsEditor.Node.call(this, graphicsEditor, model, surface, title, defaultWidth, defaultHeight);
	this.radius = radius;
};
GraphicsEditor.RectNode.prototype = new GraphicsEditor.Node();
//抽象方法(由继承者实现):绘制
GraphicsEditor.RectNode.prototype.draw = function(canvasContext) {
	this.drawRect(canvasContext, this.surface.startX, this.surface.startY, this.surface.endX, this.surface.endY, this.getLineColor(), this.getFillColor(), 1, this.radius);
};

//圆形,从GraphicsEditor.Node继承
GraphicsEditor.RoundNode = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight, lineWidth) {
	GraphicsEditor.Node.call(this, graphicsEditor, model, surface, title, defaultWidth, defaultHeight);
	this.lineWidth = lineWidth;
};
GraphicsEditor.RoundNode.prototype = new GraphicsEditor.Node();
//抽象方法(由继承者实现):绘制
GraphicsEditor.RoundNode.prototype.draw = function(canvasContext) {
	canvasContext.beginPath();
	var x = this.getLeft() + this.getWidth() / 2;
	var y = this.getTop() + this.getHeight() / 2;
	var a = this.getWidth() / 2;
	var b = this.getHeight() / 2;
	var k = .5522848, 
	ox = a * k, // 水平控制点偏移量 
	oy = b * k; // 垂直控制点偏移量 
	//从椭圆的左端点开始顺时针绘制四条三次贝塞尔曲线 
	canvasContext.moveTo(x - a, y); 
	canvasContext.bezierCurveTo(x - a, y - oy, x - ox, y - b, x, y - b); 
	canvasContext.bezierCurveTo(x + ox, y - b, x + a, y - oy, x + a, y); 
	canvasContext.bezierCurveTo(x + a, y + oy, x + ox, y + b, x, y + b); 
	canvasContext.bezierCurveTo(x - ox, y + b, x - a, y + oy, x - a, y); 
	canvasContext.lineWidth = this.lineWidth;
	canvasContext.fillStyle = this.getFillColor();
	canvasContext.fill();
	canvasContext.strokeStyle = this.getLineColor();
	canvasContext.stroke();
	canvasContext.closePath();
};

//菱形,从GraphicsEditor.Node继承
GraphicsEditor.RhombusNode = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight) {
	GraphicsEditor.Node.call(this, graphicsEditor, model, surface, title, defaultWidth, defaultHeight);
};
GraphicsEditor.RhombusNode.prototype = new GraphicsEditor.Node();
//抽象方法(由继承者实现):绘制
GraphicsEditor.RhombusNode.prototype.draw = function(canvasContext) {
	canvasContext.beginPath();
	canvasContext.moveTo(this.getLeft(), this.getTop() + this.getHeight() / 2); 
	canvasContext.lineTo(this.getLeft() + this.getWidth() / 2, this.getTop());
	canvasContext.lineTo(this.getRight(), this.getTop() + this.getHeight() / 2); 
	canvasContext.lineTo(this.getLeft() + this.getWidth() / 2, this.getBottom());  
	canvasContext.lineTo(this.getLeft(), this.getTop() + this.getHeight() / 2);
	canvasContext.lineWidth = 1;
	canvasContext.fillStyle = this.getFillColor();
	canvasContext.fill();
	canvasContext.strokeStyle = this.getLineColor();
	canvasContext.stroke();
	canvasContext.closePath();
};