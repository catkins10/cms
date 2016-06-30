/**
 * 线,从GraphicsEditorShape继承
 * transition:连接,定义:{fromElementId:[源元素ID], toElementId:[目标元素ID]}
 **/
GraphicsEditor.Line = function(graphicsEditor, transition, surface, drawArrow, turningRadii, title) {
	if(!graphicsEditor) {
		return;
	}
	this.brokenLine = surface.uuid.indexOf('BrokenLine')!=-1; //是否折线
	this.drawArrow = drawArrow; //是否绘制箭头
	this.turningMin = graphicsEditor.gridSize * 2; //最小转折宽度
	this.turningRadii = turningRadii; //转角半径
	this.transition = transition; //连接
	this.arrowWidth = 8;
	this.arrowCove = 6;
	this.textDistance = 3;
	this.textPosition = 10;
	this.arrowRadian = 0.45;
	this.xArrow1 = -1;
	this.yArrow1 = -1;
	this.xArrow2 = -1;
	this.yArrow2 = -1;
	this.xArrow3 = -1;
	this.yArrow3 = -1;
	this.titleWidth = -1;
	this.titleHeight = -1;
	this.titleX = -1;
	this.titleY = -1;
	this.fromElement = this.transition.fromElementId ? ListUtils.findObjectByProperty(graphicsEditor.elements, "surface.id", this.transition.fromElementId) : null;
	this.toElement = this.transition.toElementId ? ListUtils.findObjectByProperty(graphicsEditor.elements, "surface.id", this.transition.toElementId) : null;
	GraphicsEditor.Shape.call(this, graphicsEditor, surface, title);
	if(this.fromElement) {
		this.fromElement.addExitLine(this);
	}
	if(this.toElement) {
		this.toElement.addEnterLine(this);
	}
	this.defaultWidth = 0;
	this.defaultHeight = 8 * graphicsEditor.gridSize;
};
GraphicsEditor.Line.prototype = new GraphicsEditor.Shape();
//抽象方法(由继承者实现):在元素位置变更时重置元素显示属性（包括焦点信息、连接点信息等）
GraphicsEditor.Line.prototype.resetDisplayProperties = function(resetFor) {
	if(!this.brokenLine) { //不是折线
		//设置焦点信息
		this.focusPoints.length = 0;
		this.focusPoints.push({x:this.surface.startX, y:this.surface.startY, cursor:'crosshair', resizeMode:'crosshair'});
		this.focusPoints.push({x:this.surface.endX, y:this.surface.endY, cursor:'crosshair', resizeMode:'crosshair'});
		//设置箭头信息
		this._setArrow(this.surface.startX, this.surface.startY, this.surface.endX, this.surface.endY);
		return;
	}
	if(!this.surface.turningXPoints || ',create,creating,createComplete,moveStartPoint,moveEndPoint,'.indexOf(',' + resetFor + ',')!=-1) {
		this._setTurningPoints(); //设置转折点
	}
	//设置焦点信息
	var flag = this.surface.turningXPoints[0]==this.surface.startX;
	if(flag && this.surface.turningYPoints[0]==this.surface.startY) {
		flag = this.surface.turningXPoints[this.surface.turningXPoints.length - 1] == this.surface.endX;
		flag = this.surface.turningXPoints.length % 2 == 0 ? flag : !flag;
	}
	this.focusPoints.length = 0;
	this.focusPoints.push({x:this.surface.startX, y:this.surface.startY, cursor:'crosshair', resizeMode:'crosshair'});
	this.focusPoints.push({x:this.surface.endX, y:this.surface.endY, cursor:'crosshair', resizeMode:'crosshair'});
	var startX = this.surface.turningXPoints[0];
	var startY = this.surface.turningYPoints[0];
	for(var i=1; i<this.surface.turningXPoints.length; i++) {
		if(flag) {
			this.focusPoints.push({x:startX + (this.surface.turningXPoints[i] - startX) / 2, y:startY, cursor:'n-resize', resizeMode:i});
		}
		else {
			this.focusPoints.push({x:startX, y:startY + (this.surface.turningYPoints[i] - startY) / 2, cursor:'w-resize', resizeMode:i});
		}
		flag = !flag;
		startX = this.surface.turningXPoints[i];
		startY = this.surface.turningYPoints[i];
	}
	//设置箭头信息
	var index = this.surface.turningXPoints.length - 1;
	this._setArrow(this.surface.turningXPoints[index], this.surface.turningYPoints[index], this.surface.endX, this.surface.endY);
};
//设置箭头信息
GraphicsEditor.Line.prototype._setArrow = function(startX, startY, endX, endY) {
	var radian;
	if(startX==endX) {
		radian = (startY > endY ? 1.5 : 0.5) * Math.PI;
	}
	else {
		radian = Math.atan2(endY-startY, endX-startX);
	}
	this.xArrow2 = Math.round(endX- this.arrowCove * Math.cos(radian));
	this.yArrow2 = Math.round(endY- this.arrowCove * Math.sin(radian));
	
	radian -= this.arrowRadian;
	this.xArrow1 = Math.floor(endX - this.arrowWidth * Math.cos(radian));
	this.yArrow1 = Math.floor(endY - this.arrowWidth * Math.sin(radian));
	
	radian = Math.PI/2 - radian - 2 * this.arrowRadian;
	this.xArrow3 = Math.floor(endX - this.arrowWidth * Math.sin(radian));
	this.yArrow3 = Math.floor(endY - this.arrowWidth * Math.cos(radian));
};
//抽象方法(由继承者实现):绘制
GraphicsEditor.Line.prototype.draw = function(canvasContext) {
	canvasContext.beginPath();
	canvasContext.strokeStyle = this.getLineColor(); //设置线条颜色
	if(!this.brokenLine) { //不是折线
		canvasContext.moveTo(this.surface.startX - 0.5, this.surface.startY - 0.5);
		canvasContext.lineTo(this.surface.endX - 0.5, this.surface.endY - 0.5);
	}
	else { //折线
		canvasContext.moveTo(this.surface.startX - 0.5, this.surface.startY - 0.5);
		for(var i=0; i < this.surface.turningXPoints.length; i++) {
			canvasContext.arcTo(Math.floor(this.surface.turningXPoints[i]) - 0.5,
							    Math.floor(this.surface.turningYPoints[i]) - 0.5,
							    i==this.surface.turningXPoints.length-1 ? this.surface.endX - 0.5 : Math.floor(this.surface.turningXPoints[i + 1]) - 0.5,
							    i==this.surface.turningXPoints.length-1 ? this.surface.endY - 0.5 : Math.floor(this.surface.turningYPoints[i + 1]) - 0.5,
							    this.turningRadii);
		}
		canvasContext.lineTo(this.surface.endX - 0.5, this.surface.endY - 0.5);
	}
	canvasContext.lineWidth = 1;
	canvasContext.stroke(); 
	canvasContext.closePath();
	//显示箭头
	this._drawArrow(canvasContext);
};
//抽象方法(由继承者实现):输出标题
GraphicsEditor.Line.prototype.drawTitle = function(canvasContext) {
	this._setTitlePosition(canvasContext); //设置显示位置
	if(this.title!=null && this.titleX!=-1) {
		canvasContext.beginPath;
		canvasContext.fillStyle = this.getTextColor();
		canvasContext.fillText(this.title, this.titleX, this.titleY - 3);
		canvasContext.closePath();
	}
};
//抽象方法(由继承者实现):获取显示区域{left, top, right, bottom}
GraphicsEditor.Line.prototype.getDisplayArea = function() {
	var area = {left: Math.min(this.getLeft(), this.titleX==-1 ? 888888 : this.titleX),
				top: Math.min(this.getTop(), this.titleY==-1 ? 888888 : this.titleY - this.titleHeight),
				right: Math.max(this.getRight(), this.titleX + this.titleWidth),
				bottom: Math.max(this.getBottom(), this.titleY)};
	if(!this.brokenLine) { //不是折线
		return area;
	}
	//折线
	for(var i=0; i < this.surface.turningXPoints.length; i++) {
		area.left = Math.min(area.left, this.surface.turningXPoints[i]);
		area.top = Math.min(area.top, this.surface.turningYPoints[i]);
		area.right = Math.max(area.right, this.surface.turningXPoints[i]);
		area.bottom = Math.max(area.bottom, this.surface.turningYPoints[i]);
	}
	return area;
};
//删除本元素,从父类继承
GraphicsEditor.Line.prototype.deleteElement = function() {
	if(!GraphicsEditor.Shape.prototype.deleteElement.call(this)) {
		return;
	}
	if(this.fromElement) {
		this.fromElement.removeExitLine(this);
	}
	if(this.toElement) {
		this.toElement.removeEnterLine(this);
	}
	return true;
};
//创建新元素（单击鼠标）
GraphicsEditor.Line.prototype.create = function(x, y, connectElement, connectIndex) {
	this.setFromElement(connectElement, connectIndex);
	GraphicsEditor.Shape.prototype.create.call(this, x, y, connectElement, connectIndex);
};
//修改元素尺寸或位置（移动鼠标）
GraphicsEditor.Line.prototype.creating = function(x, y, connectElement, connectIndex) {
	this.setToElement(connectElement, connectIndex);
	return GraphicsEditor.Shape.prototype.creating.call(this, x, y, connectElement, connectIndex);
};
//完成创建新元素（放开鼠标）,如果尺寸小于最小尺寸,返回false
GraphicsEditor.Line.prototype.createComplete = function(x, y, connectElement, connectIndex) {
	var ret =  GraphicsEditor.Shape.prototype.createComplete.call(this, x, y, connectElement, connectIndex);
	this.setToElement(ret ? connectElement : null, ret ? connectIndex : -1);
	return ret;
};
//设置源元素
GraphicsEditor.Line.prototype.setFromElement = function(element, connectIndex) {
	if(this.fromElement) {
		this.fromElement.removeExitLine(this);
	}
	this.transition.fromElementId = element ? element.surface.id : null;
	this.transition.fromElement = element ? {uuid: element.model.uuid} : null;
	this.surface.enterIndex = connectIndex;
	this.fromElement = element;
	if(element) {
		element.addExitLine(this);
	}
};
//设置目标元素
GraphicsEditor.Line.prototype.setToElement = function(element, connectIndex) {
	if(this.toElement) {
		this.toElement.removeEnterLine(this);
	}
	this.transition.toElementId = element ? element.surface.id : null;
	this.transition.toElement = element ? {uuid: element.model.uuid} : null;
	this.surface.exitIndex = connectIndex;
	this.toElement = element;
	if(element) {
		element.addEnterLine(this);
	}
};
//绘制箭头
GraphicsEditor.Line.prototype._drawArrow = function(canvasContext) {
    if(!this.drawArrow) {
    	return;
    }
    canvasContext.beginPath();
	canvasContext.strokeStyle = this.getLineColor();
	canvasContext.moveTo(this.surface.endX - 0.5, this.surface.endY - 0.5);
	canvasContext.lineTo(this.xArrow1 - 0.5, this.yArrow1 - 0.5);
	canvasContext.lineTo(this.xArrow2 - 0.5, this.yArrow2 - 0.5);
	canvasContext.lineTo(this.xArrow3 - 0.5, this.yArrow3 - 0.5);
	canvasContext.lineTo(this.surface.endX - 0.5, this.surface.endY - 0.5);
	canvasContext.fillStyle = canvasContext.strokeStyle;
	canvasContext.fill();
	canvasContext.closePath();
};
//是否与rect相交
GraphicsEditor.Line.prototype.intersects = function(left, top, right, bottom) {
	if(!this.brokenLine) { //不是折线
		if(this.getLeft() >= left && this.getRight() <= right && this.getTop() >= top && this.getBottom() <= bottom) {
			return true;
		}
		if(this.titleX!=-1 && this.titleX <= right && this.titleX + this.titleWidth >= left && this.titleY - this.titleHeight <= bottom && this.titleY >= top) {
			return true;
		}
		var x, y;
		if(left >= this.getLeft() && left <= this.getRight() && (y=this._getY(left)) >= top && y <= bottom) {
			return true;
		}
		if(right >= this.getLeft() && right <= this.getRight() && (y=this._getY(right)) >= top && y <= bottom) {
			return true;
		}
		if(top >= this.getTop() && top <= this.getBottom() && (x=this._getX(top)) >= left && x <= right) {
			return true;
		}
		if(bottom >= this.getTop() && bottom <= this.getBottom() && (x=this._getX(bottom)) >= left && x <= right) {
			return true;
		}
		return false;
	}
	//折线
	if(this.titleX!=-1 && this.titleX <= right && this.titleX + this.titleWidth >= left && this.titleY >= top && this.titleY - this.titleHeight <= bottom) {
		return true;
	}
	var x1 = this.surface.startX;
	var y1 = this.surface.startY;
	for(var i=0; i < this.surface.turningXPoints.length + 1; i++) {
		var x2 = i == this.surface.turningXPoints.length ? this.surface.endX : this.surface.turningXPoints[i];
		var y2 = i == this.surface.turningXPoints.length ?  this.surface.endY : this.surface.turningYPoints[i];
		if(Math.min(x1, x2) <= right && Math.min(y1, y2) <= bottom && Math.max(x1, x2) >= left && Math.max(y1, y2) >= top) {
			return true;
		}
		x1 = x2;
		y1 = y2;
	}
	return false;
};
//根据Y坐标求X坐标
GraphicsEditor.Line.prototype._getX = function(y) {
	return this.surface.startX + (y - this.surface.startY) / (this.surface.endY - this.surface.startY) * (this.surface.endX - this.surface.startX);
};
//根据X坐标求Y坐标
GraphicsEditor.Line.prototype._getY = function(x) {
	return this.surface.startY + (x - this.surface.startX) / (this.surface.endX - this.surface.startX) * (this.surface.endY - this.surface.startY);
};
//抽象方法(由继承者实现):鼠标是否移动到本元素上
GraphicsEditor.Line.prototype.isMouseOver = function(x, y, offset) {
	if(offset<3) {
		offset=3;
	}
	if(!this.brokenLine) { //不是折线
		if(x < this.getLeft() - offset || y < this.getTop() - offset || x > this.getRight() + offset || y > this.getBottom() + offset) {
			return false;
		}
		var d;
		if(this.surface.startX==this.surface.endX) {
			d = Math.abs(x - this.surface.startX);
		}
		else {
			var k = (this.surface.startY - this.surface.endY) / (this.surface.startX - this.surface.endX);
			var b = this.surface.startY - k * this.surface.startX;
			d = Math.abs(k * x - y + b) / Math.sqrt(k * k + 1);
		}
		return d<=offset;
	}
	//折线
	var x1 = this.surface.startX;
	var y1 = this.surface.startY;
	for(var i=0; i < this.surface.turningXPoints.length + 1; i++) {
		var x2 = i == this.surface.turningXPoints.length ? this.surface.endX : this.surface.turningXPoints[i];
		var y2 = i == this.surface.turningXPoints.length ? this.surface.endY : this.surface.turningYPoints[i];
		if(x >= Math.min(x1, x2) - offset && y >= Math.min(y1, y2) - offset && x <= Math.max(x1, x2) + offset && y <= Math.max(y1, y2) + offset) {
			return true;
		}
		x1 = x2;
		y1 = y2;
	}
	return false;
};
//抽象方法(由继承者实现):尺寸调整
GraphicsEditor.Line.prototype.move = function(startX, startY, endX, endY) {
	if(!this.brokenLine || this.surface.moveDisable || this.focusIndex < 2) { //不是折线、位置移动（鼠标点中元素本身）、移动头尾
		var offsetX = endX - startX;
		var offsetY = endY - startY;
		if(this.surface.moveDisable) { //禁止移动
			return {x:offsetX, y:offsetY};
		}
		if(this.focusIndex==-1) { //位置移动（鼠标点中元素本身）
			offsetX -= offsetX % this.graphicsEditor.gridSize;
			offsetY -= offsetY % this.graphicsEditor.gridSize;
			if(offsetX==0 && offsetY==0) {
				return {x:offsetX, y:offsetY};
			}
			this.graphicsEditor.offsetElements(offsetX, offsetY, offsetX, offsetY, 'move');
		}
		else {
			this.graphicsEditor.checkNear(endX, endY);
			//修改元素尺寸或位置
			var point = this.graphicsEditor.getNearestConnect(endX, endY, true);
			var element = this.graphicsEditor.connectElement;
			if(this.focusIndex==0) { //起始点
				if(startX==point.x && startY==point.y) {
					return {x:0, y:0};
				}
				this.setFromElement(element, this.graphicsEditor.connectIndex);
				this.surface.startX = point.x;
				this.surface.startY = point.y;
			}
			else { //结束点
				if(endX==point.x && endY==point.y) {
					return {x:0, y:0};
				}
				this.setToElement(element, this.graphicsEditor.connectIndex);
				this.surface.endX = point.x;
				this.surface.endY = point.y;
			}
			offsetX = point.x - startX;
			offsetY = point.y - startY;
			this.resetDisplayProperties(this.focusIndex==0 ? 'moveStartPoint' : 'moveEndPoint');
		}
		return {x:offsetX, y:offsetY};
	}
	//折线
	var focus = this.focusPoints[this.focusIndex];
	if(focus.cursor=='n-resize') { //上下移动
		endY -= endY % this.graphicsEditor.gridSize;
		this.surface.turningYPoints[focus.resizeMode - 1] = this.surface.turningYPoints[focus.resizeMode] = endY;
	}
	else { //左右移动
		endX -= endX % this.graphicsEditor.gridSize;
		this.surface.turningXPoints[focus.resizeMode - 1] = this.surface.turningXPoints[focus.resizeMode] = endX;
	}
	this.resetDisplayProperties('move');
	return {x:0, y:0};
};
//抽象方法(由继承者实现):根据mode指定方式调整尺寸
GraphicsEditor.Line.prototype.offset = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	if(mode!='move' || this.surface.moveDisable) {
		return;
	}
	var selectEnter = false; //入口元素是否被选中
	if(this.fromElement) {
		 selectEnter = this.fromElement.selected;
	}
	var selectExit = false; //出口元素是否被选中
	if(this.toElement) {
		 selectExit = this.toElement.selected;
	}
	if((selectEnter && !selectExit) || (!selectEnter && selectExit)) {
		return;
	}
	if(!selectEnter) {
		this.setFromElement(null, -1);
		this.setToElement(null, -1);
	}
	this.surface.startX += offsetLeft;
	this.surface.endX += offsetRight;
	this.surface.startY += offsetTop;
	this.surface.endY += offsetBottom;
	this._afterOffset(offsetLeft, offsetTop);
};
//调整尺寸后
GraphicsEditor.Line.prototype._afterOffset = function(offsetLeft, offsetTop) {
	for(var i = (this.brokenLine ? this.surface.turningXPoints.length - 1 : -1); i >= 0; i--) {
		this.surface.turningXPoints[i] += offsetLeft;
		this.surface.turningYPoints[i] += offsetTop;
	}
	this.resetDisplayProperties('offset');
};
//移动起始点
GraphicsEditor.Line.prototype.moveStartPoint = function(point) {
	this.surface.startX = point.x;
	this.surface.startY = point.y;
	this.resetDisplayProperties('moveStartPoint');
};
//移动结束点
GraphicsEditor.Line.prototype.moveEndPoint = function(point) {
	this.surface.endX = point.x;
	this.surface.endY = point.y;
	this.resetDisplayProperties('moveEndPoint');
};
//设置标题的显示位置
GraphicsEditor.Line.prototype._setTitlePosition = function(canvasContext) {
	this.titleWidth = -1;
	this.titleHeight = -1;
	this.titleX = -1;
	this.titleY = -1;
	if(this.title==null) {
		return;
	}
	if(!this.brokenLine) { //不是折线
		var size = this.measureText(canvasContext, this.title, this.graphicsEditor.font);
		this.titleWidth = size.width;
		this.titleHeight = size.height;
		var width = size.width / 2;
		var height = size.height / 2;
		if(this.surface.startX==this.surface.endX) {
			this.titleX = this.surface.startX + this.textDistance;
			if(this.textPosition>=1) {
				this.titleY = Math.floor(this.surface.startY + (this.surface.endY > this.surface.startY ? this.textPosition + size.height : -this.textPosition));
			}
			else {
				this.titleY = this.surface.startY + Math.floor((this.surface.endY - this.surface.startY) * this.textPosition + height);
			}
			return;
		}
		var radian = Math.atan2(this.surface.startY - this.surface.endY, this.surface.endX - this.surface.startX);
		this.titleX = -1;
		var distance;
		if(this.textPosition < 1) {
			distance = Math.floor(Math.sqrt(Math.pow(this.surface.startX - this.surface.endX, 2) + Math.pow(this.surface.startY - this.surface.endY, 2)) * this.textPosition);
		}
		else if(Math.abs(Math.sin(radian)) > 0.88) {
			distance = (width + height / Math.abs(Math.tan(radian))) * Math.abs(Math.cos(radian)) + height / Math.abs(Math.sin(radian)) + this.textPosition;
		}
		else {
			distance = width * Math.abs(Math.cos(radian)) + this.textPosition;
		}
		this.titleX = this.surface.startX + Math.floor(distance * Math.cos(radian));
		this.titleY = this.surface.startY - Math.floor(distance * Math.sin(radian));
		if(radian<=0.8 && radian>-2.5) {
			distance = ((width + this.textDistance) * Math.abs(Math.tan(radian)) + height + this.textDistance) * Math.abs(Math.cos(radian));
			this.titleX -= Math.floor(distance * Math.sin(radian) + width);
			this.titleY -= Math.floor(distance * Math.cos(radian) - height);
			if(radian < -0.5 * Math.PI) {
				this.yTitle -= this.textDistance;
			}
		}
		else {
			distance = ((width + this.textDistance) * Math.abs(Math.tan(radian)) + height + this.textDistance) * Math.abs(Math.cos(radian));
			this.titleX += Math.floor(distance * Math.sin(radian) - width);
			this.titleY += Math.floor(distance * Math.cos(radian) + height);
			if(radian < 0.5 * Math.PI) {
				this.yTitle -= this.textDistance;
			}
		}
	}
	//折线
	if(this.surface.turningXPoints.length==0) {
		return;
	}
	var size = this.measureText(canvasContext, this.title, this.graphicsEditor.font);
	this.titleWidth = size.width;
	this.titleHeight = size.height;
	
	var endX = this.surface.turningXPoints[0];
	var endY = this.surface.turningYPoints[0];
	var x3, y3, x4, y4;
	if(this.surface.turningXPoints.length==1) {
		x3 = x4 = this.surface.endX;
		y3 = y4 = this.surface.endY;
	}
	else {
		x3 = this.surface.turningXPoints[1];
		y3 = this.surface.turningYPoints[1];
		if(this.surface.turningXPoints.length==2) {
			x4 = this.surface.endX;
			y4 = this.surface.endY;
		}
		else {
			x4 = this.surface.turningXPoints[2];
			y4 = this.surface.turningYPoints[2];
		}
	}
	if(this.surface.startX==endX) {
		this.titleX = this.surface.startX + this.textDistance;
		if(this.textPosition < 1 && Math.abs(endY - this.surface.startY) < size.height) {
			this.titleY = this.surface.startY + (this.surface.startY > endY ? -this.textDistance : this.textDistance + size.height);
		}
		else  if(this.textPosition >= 1) {
			this.titleY = this.surface.startY + Math.floor(endY > this.surface.startY ? this.textPosition + size.height : -this.textPosition);
		}
		else {
			this.titleY = this.surface.startY + Math.floor((endY - this.surface.startY) * this.textPosition + size.height / 2);
		}
		if(x3 > this.surface.startX) {
			if(Math.abs(endY - this.surface.startY) < size.height + (this.textPosition < 1 ? 0 : this.textPosition) + this.textDistance) {
				this.titleX = this.surface.startX - this.textDistance - size.width;
			}
			else if(x3 < this.titleX + size.width && ((this.surface.startY <= endY && y4 < this.titleY) || (this.surface.startY > endY && y4 > this.titleY - size.height))) {
				this.titleX = this.surface.startX - this.textDistance - size.width;
			}
		}
	}
	else {
		this.titleY = this.surface.startY - this.textDistance;
		if(this.textPosition < 1 && Math.abs(endX - this.surface.startX) < size.width) {
			this.titleX = this.surface.startX + (this.surface.startX > endX ? -this.textDistance - size.width : this.textDistance);
		}
		else if(this.textPosition >= 1) {
			this.titleX = this.surface.startX + Math.floor(endX > this.surface.startX ? this.textPosition : -this.textPosition - size.width);
		}
		else {
			this.titleX = this.surface.startX + Math.floor((endX - this.surface.startX) * this.textPosition - size.width / 2);
		}
		if(y3 < this.surface.startY) {
			if(Math.abs(endX - this.surface.startX) < size.width + (this.textPosition < 1 ? 0 : this.textPosition) + this.textDistance) {
				this.titleY = this.surface.startY + this.textDistance + size.height;
			}
			else if(y3 > this.titleY - size.height && ((this.surface.startX <= endX && x4 < this.titleX + size.width) || (this.surface.startX > endX && x4 > this.titleX + size.width))) {
				this.titleY = this.surface.startY + this.textDistance + size.height;
			}
		}
	}
};
//设置转折点
GraphicsEditor.Line.prototype._setTurningPoints = function() {
	this.surface.turningXPoints = [];
	this.surface.turningYPoints = [];
	var startOrientation = '', endOrientation = ''; //起点、终点方向
	var startElement = null, endElement = null;
	if(this.fromElement) { //获取起点信息
		startElement = this.fromElement;
		if(this.surface.startX==startElement.getLeft()) {
			startOrientation = 'w-resize'; //向西
		}
		else if(this.surface.startX==startElement.getRight()) {
			startOrientation = 'e-resize'; //向东
		}
		else if(this.surface.startY==startElement.getTop()) {
			startOrientation = 'n-resize'; //向北
		}
		else if(this.surface.startY==startElement.getBottom()) {
			startOrientation = 's-resize'; //向南
		}
	}
	if(this.toElement) { //获取终点信息
		endElement = this.toElement;
		if(this.surface.endX==endElement.getLeft()) {
			endOrientation = 'w-resize'; //向西
		}
		else if(this.surface.endX==endElement.getRight()) {
			endOrientation = 'e-resize'; //向东
		}
		else if(this.surface.endY==endElement.getTop()) {
			endOrientation = 'n-resize'; //向北
		}
		else if(this.surface.endY==endElement.getBottom()) {
			endOrientation = 's-resize'; //向南
		}
	}
	if(startOrientation=='') { //起点不与节点相连
		if(endOrientation=='') {
			this._setTurningPointsNoneToNone();
		}
		else if(endOrientation=='w-resize') { //终点向西
			this._setTurningPointsNoneToWest(endElement, false);
		}
		else if(endOrientation=='e-resize') { //终点向东
			this._setTurningPointsNoneToEast(endElement, false);
		}
		else if(endOrientation=='n-resize') { //终点向北
			this._setTurningPointsNoneToNorth(endElement, false);
		}
		else if(endOrientation=='s-resize') { //终点向南
			this._setTurningPointsNoneToSouth(endElement, false);
		}
	}
	else if(startOrientation=='w-resize') { //起点向西
		if(endOrientation=='') {
			this._setTurningPointsNoneToWest(startElement, true);
		}
		else if(endOrientation=='w-resize') { //终点向西
			this._setTurningPointsWestToWest(startElement, endElement);
		}
		else if(endOrientation=='e-resize') { //终点向东
			this._setTurningPointsWestToEast(startElement, endElement, false);
		}
		else if(endOrientation=='n-resize') { //终点向北
			this._setTurningPointsWestToNorth(startElement, endElement, false);
		}
		else if(endOrientation=='s-resize') { //终点向南
			this._setTurningPointsWestToSouth(startElement, endElement, false);
		}
	}
	else if(startOrientation=='e-resize') { //起点向东
		if(endOrientation=='') {
			this._setTurningPointsNoneToEast(startElement, true);
		}
		else if(endOrientation=='w-resize') { //终点向西
			this._setTurningPointsWestToEast(endElement, startElement, true);
		}
		else if(endOrientation=='e-resize') { //终点向东
			this._setTurningPointsEastToEast(startElement, endElement);
		}
		else if(endOrientation=='n-resize') { //终点向北
			this._setTurningPointsEastToNorth(startElement, endElement, false);
		}
		else if(endOrientation=='s-resize') { //终点向南
			this._setTurningPointsEastToSouth(startElement, endElement, false);
		}
	}
	else if(startOrientation=='n-resize') {  //起点向北
		if(endOrientation=='') {
			this._setTurningPointsNoneToNorth(startElement, true);
		}
		else if(endOrientation=='w-resize') { //终点向西
			this._setTurningPointsWestToNorth(endElement, startElement, true);
		}
		else if(endOrientation=='e-resize') { //终点向东
			this._setTurningPointsEastToNorth(endElement, startElement, true);
		}
		else if(endOrientation=='n-resize') { //终点向北
			this._setTurningPointsNorthToNorth(startElement, endElement);
		}
		else if(endOrientation=='s-resize') { //终点向南
			this._setTurningPointsNorthToSouth(startElement, endElement, false);
		}
	}
	else if(startOrientation=='s-resize') {  //起点向南
		if(endOrientation=='') {
			this._setTurningPointsNoneToSouth(startElement, true);
		}
		else if(endOrientation=='w-resize') { //终点向西
			this._setTurningPointsWestToSouth(endElement, startElement, true);
		}
		else if(endOrientation=='e-resize') { //终点向东
			this._setTurningPointsEastToSouth(endElement, startElement, true);
		}
		else if(endOrientation=='n-resize') { //终点向北
			this._setTurningPointsNorthToSouth(endElement, startElement, true);
		}
		else if(endOrientation=='s-resize') { //终点向南
			this._setTurningPointsSouthToSouth(startElement, endElement);
		}
	}
};
//添加转折点
GraphicsEditor.Line.prototype._addTurningPoint = function(x, y) {
	this.surface.turningXPoints.push(x);
	this.surface.turningYPoints.push(y);
};
//转折点倒序
GraphicsEditor.Line.prototype._reverseTurningPoints = function() {
	this.surface.turningXPoints = this.surface.turningXPoints.reverse();
	this.surface.turningYPoints = this.surface.turningYPoints.reverse();
};
//获取转折点:空连接－空连接
GraphicsEditor.Line.prototype._setTurningPointsNoneToNone = function() {
	if(Math.abs(this.surface.startX - this.surface.endX) < Math.abs(this.surface.startY - this.surface.endY)) {
		// |-| 横线在两点中间
		this._addTurningPoint(this.surface.startX, this.surface.startY + (this.surface.endY - this.surface.startY) / 2);
		this._addTurningPoint(this.surface.endX, this.surface.startY + (this.surface.endY - this.surface.startY) / 2);
	}
	else {
		// -|- 竖线在两点中间
		this._addTurningPoint(this.surface.startX + (this.surface.endX - this.surface.startX) / 2, this.surface.startY);
		this._addTurningPoint(this.surface.startX + (this.surface.endX - this.surface.startX) / 2, this.surface.endY);
	}
};
//获取转折点:空连接－西
GraphicsEditor.Line.prototype._setTurningPointsNoneToWest = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX <= endElement.getLeft() - this.turningMin) {
		// -|-
		var x = startX + (endX - startX) / 2;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endY);
	}
	else if(startY > endElement.getTop() - this.turningMin && startY < endElement.getBottom() + this.turningMin) {
		// |-|-
		this._addTurningPoint(startX, endElement.getTop()-this.turningMin);
		this._addTurningPoint(endElement.getLeft() - this.turningMin, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endElement.getLeft() - this.turningMin, endY);
	}
	else {
		// -|- 竖线贴左边缘
		this._addTurningPoint(endElement.getLeft() - this.turningMin, startY);
		this._addTurningPoint(endElement.getLeft() - this.turningMin, endY);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:空连接－东
GraphicsEditor.Line.prototype._setTurningPointsNoneToEast = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX >= endElement.getRight() + this.turningMin) {
		// -|-
		var x = endX + (startX - endX) / 2;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endY);
	}
	else if(startY > endElement.getTop() - this.turningMin && startY < endElement.getBottom() + this.turningMin) {
		// |-|-
		this._addTurningPoint(startX, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endElement.getRight() + this.turningMin, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endElement.getRight() + this.turningMin, endY);
	}
	else {
		// -|- 竖线贴左边缘
		this._addTurningPoint(endElement.getRight() + this.turningMin, startY);
		this._addTurningPoint(endElement.getRight() + this.turningMin, endY);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:空连接－北
GraphicsEditor.Line.prototype._setTurningPointsNoneToNorth = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startY <= endElement.getTop() - this.turningMin) {
		// |-| 横线在两点中间 
		var y = startY + (endY - startY) / 2;
		this._addTurningPoint(startX, y);
		this._addTurningPoint(endX, y);
	}
	else if(startX <= endElement.getLeft() - this.turningMin || startX >= endElement.getRight() + this.turningMin) {
		// |-| 横线贴上边缘
		this._addTurningPoint(startX, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endX, endElement.getTop() - this.turningMin);
	}
	else {
		// -|-|
		var x = startX <= endElement.getRight() ? endElement.getLeft() - this.turningMin : endElement.getRight() + this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endX, endElement.getTop() - this.turningMin);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:空连接－南
GraphicsEditor.Line.prototype._setTurningPointsNoneToSouth = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startY >= endElement.getBottom() + this.turningMin) {
		// |-| 横线在两点中间
		var y = startY + (endY - startY) / 2;
		this._addTurningPoint(startX, y);
		this._addTurningPoint(endX, y);
	}
	else if(startX >= endElement.getRight() + this.turningMin || startX <= endElement.getLeft() - this.turningMin) {
		// |-| 横线贴上边缘
		this._addTurningPoint(startX, endElement.getBottom() + this.turningMin);
		this._addTurningPoint(endX, endElement.getBottom() + this.turningMin);
	}
	else {
		// -|-|
		var x = startX >= endElement.getLeft() ? endElement.getRight() + this.turningMin : endElement.getLeft() - this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endElement.getBottom() + this.turningMin);
		this._addTurningPoint(endX, endElement.getBottom() + this.turningMin);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:西－西
GraphicsEditor.Line.prototype._setTurningPointsWestToWest = function(startElement, endElement) {
	if(this.surface.startY < endElement.getTop() || this.surface.startY > endElement.getBottom()) {
		// [
		var x = Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
		this._addTurningPoint(x, this.surface.startY);
		this._addTurningPoint(x, this.surface.endY);
	}
	else {
		// -|-|-
		var y = Math.min(startElement.getBottom(), endElement.getBottom()) + this.turningMin;
		if(this.surface.startX < this.surface.endX) {
			this._addTurningPoint(startElement.getLeft() - this.turningMin, this.surface.startY);
			this._addTurningPoint(startElement.getLeft() - this.turningMin, y);
			var x = endElement.getLeft() - Math.min((endElement.getLeft() - startElement.getRight()) / 2, this.turningMin);
			this._addTurningPoint(x, y);
			this._addTurningPoint(x, this.surface.endY);
		}
		else {
			var x = startElement.getLeft() - Math.min((startElement.getLeft() - endElement.getRight()) / 2, this.turningMin);
			this._addTurningPoint(x, this.surface.startY);
			this._addTurningPoint(x, y);
			this._addTurningPoint(endElement.getLeft() - this.turningMin, y);
			this._addTurningPoint(endElement.getLeft() - this.turningMin, this.surface.endY);
		}
	}
};
//获取转折点:西－东
GraphicsEditor.Line.prototype._setTurningPointsWestToEast = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX > endX) {
		// -|-
		this._addTurningPoint(startX - (startX - endX) / 2, startY);
		this._addTurningPoint(startX - (startX-endX) / 2, endY);
	}
	else if(startElement.getTop() > endElement.getBottom()) {
		// |-|-
		var y = startElement.getTop() - (startElement.getTop() - endElement.getBottom()) / 2;
		this._addTurningPoint(startX - this.turningMin, startY);
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, endY);
	}
	else if(startElement.getBottom() < endElement.getTop()) {
		// |-|-
		var y = startElement.getBottom() + (endElement.getTop() - startElement.getBottom()) / 2;
		this._addTurningPoint(startX - this.turningMin, startY);
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, endY);
	}
	else {
		// -|-|-
		var left = startY > endElement.getTop() ? Math.min(startX, endElement.getLeft()) - this.turningMin : startX - this.turningMin;
		var right = endY < startElement.getBottom() ? Math.max(endX, startElement.getRight()) + this.turningMin : endX + this.turningMin;
		var y = Math.min(startElement.getTop(), endElement.getTop()) - this.turningMin;
		this._addTurningPoint(left, startY);
		this._addTurningPoint(left, y);
		this._addTurningPoint(right,y);
		this._addTurningPoint(right, endY);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:西－北
GraphicsEditor.Line.prototype._setTurningPointsWestToNorth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX > endX && startY < endY) {
		// -|
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getBottom() < endY) {
		// -|-|
		this._addTurningPoint(startX - this.turningMin, startY);
		var y = endY - (endY - startElement.getBottom()) / 2;
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		// -|-|
		var x = startX > endElement.getRight() ? startX - (startX - endElement.getRight()) / 2 : Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
		var y = Math.min(startElement.getTop(), endY) - this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, y);
		this._addTurningPoint(endX, y);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:西－南
GraphicsEditor.Line.prototype._setTurningPointsWestToSouth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX > endX && startY > endY) {
		// -|
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getTop() > endY) {
		// -|-|
		this._addTurningPoint(startX - this.turningMin, startY);
		var y = endY + (startElement.getTop() - endY) / 2;
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		// -|-|
		var x = startX > endElement.getRight() ? startX - (startX - endElement.getRight()) / 2 : Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
		var y = Math.max(startElement.getBottom(), endY) + this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, y);
		this._addTurningPoint(endX, y);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:东－东
GraphicsEditor.Line.prototype._setTurningPointsEastToEast = function(startElement, endElement) {
	if(this.surface.startY < endElement.getTop() || this.surface.startY > endElement.getBottom()) {
		// [
		var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
		this._addTurningPoint(x, this.surface.startY);
		this._addTurningPoint(x, this.surface.endY);
	}
	else {
		// -|-|-
		var y = Math.min(startElement.getTop(), endElement.getTop()) - this.turningMin;
		if(this.surface.startX > this.surface.endX) {
			this._addTurningPoint(startElement.getRight() + this.turningMin, this.surface.startY);
			this._addTurningPoint(startElement.getRight() + this.turningMin, y);
			var x = endElement.getRight() + Math.min((startElement.getLeft() - endElement.getRight()) / 2, this.turningMin);
			this._addTurningPoint(x, y);
			this._addTurningPoint(x, this.surface.endY);
		}
		else {
			var x = startElement.getRight() + Math.min((endElement.getLeft() - startElement.getRight()) / 2, this.turningMin);
			this._addTurningPoint(x, this.surface.startY);
			this._addTurningPoint(x, y);
			this._addTurningPoint(endElement.getRight() + this.turningMin, y);
			this._addTurningPoint(endElement.getRight() + this.turningMin, this.surface.endY);
		}
	}
};
//获取转折点:东－北
GraphicsEditor.Line.prototype._setTurningPointsEastToNorth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX < endX && startY < endY) {
		// -|
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getBottom() < endY) {
		// -|-|
		this._addTurningPoint(startX + this.turningMin, startY);
		var y = endY - (endY - startElement.getBottom()) / 2;
		this._addTurningPoint(startX + this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		// -|-|
		var x = startX < endElement.getLeft() ? startX + (endElement.getLeft() - startX) / 2 : Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
		var y = Math.min(startElement.getTop(), endY) - this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, y);
		this._addTurningPoint(endX, y);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:东—南
GraphicsEditor.Line.prototype._setTurningPointsEastToSouth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX < endX && startY > endY) {
		// -|
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getTop() > endY) {
		// -|-|
		this._addTurningPoint(startX + this.turningMin, startY);
		var y = endY+(startElement.getTop() - endY) / 2;
		this._addTurningPoint(startX + this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		// -|-|
		var x = startX < endElement.getLeft() ? startX + (endElement.getLeft() - startX) / 2 : Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
		var y = Math.max(startElement.getBottom(), endY) + this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, y);
		this._addTurningPoint(endX, y);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:北－北
GraphicsEditor.Line.prototype._setTurningPointsNorthToNorth = function(startElement, endElement) {
	if(this.surface.startY < this.surface.endY) {
		this._addTurningPoint(this.surface.startX, this.surface.startY - this.turningMin);
		if(startElement.getRight() < this.surface.endX || startElement.getLeft() > this.surface.endX) {
			// |-|
			this._addTurningPoint(this.surface.endX, this.surface.startY - this.turningMin);
		}
		else { 
			// |-|-|
			var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
			this._addTurningPoint(x, this.surface.startY - this.turningMin);
			var y = this.surface.endY - (this.surface.endY - startElement.getBottom()) / 2;
			this._addTurningPoint(x, y);
			this._addTurningPoint(this.surface.endX, y);
		}
	}
	else {
		if(endElement.getRight() < this.surface.startX || endElement.getLeft() > this.surface.startX) {
			// |-|
			this._addTurningPoint(this.surface.startX, this.surface.endY-this.turningMin);
		}
		else { 
			// |-|-|
			var y = this.surface.startY - (this.surface.startY - endElement.getBottom()) / 2;
			var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
			this._addTurningPoint(this.surface.startX, y);
			this._addTurningPoint(x, y);
			this._addTurningPoint(x, this.surface.endY - this.turningMin);
		}
		this._addTurningPoint(this.surface.endX, this.surface.endY - this.turningMin);
	}
};
//获取转折点:北－南
GraphicsEditor.Line.prototype._setTurningPointsNorthToSouth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startY > endY) {
		// |-|
		var y = startY + (endY - startY) / 2;
		this._addTurningPoint(startX, y);
		this._addTurningPoint(endX, y);
	}
	else if(startElement.getLeft() > endElement.getRight()) {
		// -|-|
		var x = startElement.getLeft() - (startElement.getLeft() - endElement.getRight()) / 2;
		this._addTurningPoint(startX, startY - this.turningMin);
		this._addTurningPoint(x, startY - this.turningMin);
		this._addTurningPoint(x, endY + this.turningMin);
		this._addTurningPoint(endX, endY + this.turningMin);
	}
	else if(startElement.getRight() < endElement.getLeft()) {
		// -|-|
		var x = startElement.getRight() + (endElement.getLeft() - startElement.getRight()) / 2;
		this._addTurningPoint(startX, startY - this.turningMin);
		this._addTurningPoint(x, startY - this.turningMin);
		this._addTurningPoint(x, endY + this.turningMin);
		this._addTurningPoint(endX, endY + this.turningMin);
	}
	else {
		// |-|-|
		var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
		var top = startX < endElement.getRight() ? Math.min(startY, endElement.getTop()) - this.turningMin : startY - this.turningMin;
		var bottom = endX < startElement.getLeft() ? Math.max(endY, startElement.getBottom()) + this.turningMin : endY + this.turningMin;
		this._addTurningPoint(startX, top);
		this._addTurningPoint(x, top);
		this._addTurningPoint(x, bottom);
		this._addTurningPoint(endX, bottom);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
//获取转折点:南－南
GraphicsEditor.Line.prototype._setTurningPointsSouthToSouth = function(startElement, endElement) {
	if(this.surface.startY > this.surface.endY) {
		this._addTurningPoint(this.surface.startX, this.surface.startY + this.turningMin);
		if(startElement.getRight() < this.surface.endX || startElement.getLeft() > this.surface.endX) {
			// |-|
			this._addTurningPoint(this.surface.endX, this.surface.startY + this.turningMin);
		}
		else { 
			// |-|-|
			var x = Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
			this._addTurningPoint(x, this.surface.startY + this.turningMin);
			var y = this.surface.endY + (startElement.getTop() - this.surface.endY) / 2;
			this._addTurningPoint(x, y);
			this._addTurningPoint(this.surface.endX, y);
		}
	}
	else {
		if(endElement.getRight() < this.surface.startX || endElement.getLeft() > this.surface.startX) {
			// |-|
			this._addTurningPoint(this.surface.startX, this.surface.endY + this.turningMin);
		}
		else { 
			// |-|-|
			var y = this.surface.startY + (endElement.getTop() - this.surface.startY) / 2;
			var x = Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
			this._addTurningPoint(this.surface.startX, y);
			this._addTurningPoint(x, y);
			this._addTurningPoint(x, this.surface.endY + this.turningMin);
		}
		this._addTurningPoint(this.surface.endX, this.surface.endY + this.turningMin);
	}
};