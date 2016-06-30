/**
 * 图形编辑器,继承者包括应用配置、流程配置等
 **/
GraphicsEditor = function(canvasView, drawBarContainer, drawBarButtons, propertyTreeContainer, propertyDetailContainer, readOnly) {
	if(!canvasView) {
		return;
	}
	//定义常量
	this.selectColor = '#cccccc'; //选取框颜色
	this.lineColor = '#000000'; //线条颜色
	this.textColor = '#000000'; //文本颜色
	this.bgColor = '#ffffff'; //背景颜色
	this.gridColor = '#e4edff'; //网格颜色
	this.focusColor = '#20cc50'; //焦点颜色
	this.connectColor = '#1010ff'; //连接点颜色
	this.readOnlyColor = '#3f7f5f'; //只读时颜色
	this.gridSize = 8; //网格尺寸
	this.connectRange = 15; //连接范围
	this.font = '12px 宋体'; //字体
	
	this.modified = false; //是否修改过
	this.elements = []; //元素列表
	this.toCreateElementType = null; //将要创建的元素类型
	this.currentElement = null; //当前元素
	this.connectElement = null; //被连接元素
	this.connectIndex = -1; //被连接元素的连接点序号
	this.selectStartX = -1; //选择开始X坐标
	this.selectStartY = -1; //选择开始Y坐标
	this.selectX = -1; //当前选择位置X坐标
	this.selectY = -1; //当前选择位置Y坐标
	
	var editor = this;
	this.propertyTreeContainer = propertyTreeContainer;
	this.propertyDetailContainer = propertyDetailContainer;
	//创建按钮栏
	this.drawBar = new GraphicsEditor.DrawBar(this, drawBarContainer, drawBarButtons, readOnly);
	//创建画布
	this.canvas = canvasView.ownerDocument.createElement('canvas');
	this.width = this.canvas.width = 3500;
	this.height = this.canvas.height = 1500;
	canvasView.appendChild(this.canvas);
	if(!this.canvas.getContext) {
		document.open();
		document.write('<html><body style="font-size:12px;">当前浏览器不支持HTML5,请使用支持HTML5的浏览器打开本页面</body></html>');
		document.close();
		return;
	}
	this.canvasContext = this.canvas.getContext("2d");
	this.canvas.onmousedown = function(event) {
		editor._onMouseDown(event);
	};
	this.canvas.onmousemove = function(event) {
		editor._onMouseMove(event);
	};
	this.canvas.onmouseup = function(event) {
		editor._onMouseUp(event);
	};
	this.canvas.onmouseout = function(event) {
		editor._onMouseUp(event);
	};
	this.canvas.ondragstart = function(event) {
		return false;
	};
	canvasView.ownerDocument.body.oncontextmenu = function() {
		return false;
	};
	//初始化
	this.init();
	//重绘整个画布
	this._repaint({left:0, top:0, right:this.width, bottom:this.height});
	//显示属性目录树
	this.showPropertyTree();
};
//抽象方法(由继承者实现):初始化
GraphicsEditor.prototype.init = function() {

};
//抽象方法(由继承者实现):创建元素
GraphicsEditor.prototype.createElement = function(elementType) {

};
//抽象方法(由继承者实现):删除元素
GraphicsEditor.prototype.removeElement = function(element) {

};
//抽象方法(由继承者实现):获取属性目录树
GraphicsEditor.prototype.getPropertyTree = function() {

};
//抽象方法(由继承者实现):获取属性列表
GraphicsEditor.prototype.getPropertyList = function(propertyName) {

};
//鼠标按下
GraphicsEditor.prototype._onMouseDown = function(event) {
	this.drag = event.button==0;
	if(this.toCreateElementType==null) {
		if(event.button==0) {
			this._doSelect(event.offsetX, event.offsetY, event.ctrlKey);
		}
		else {
			this.selectStartX = -1;
		}
	}
	else if(event.button==2) { //鼠标右键按下,停止创建新元素
		this.finishCreateElement();
		this._setCursor(event.offsetX, event.offsetY);
	}
	else if(event.button==0) { //鼠标左键按下,且有需要创建的元素
		this.modified = true;
		this.deselect();
		var point = this.getNearestConnect(event.offsetX, event.offsetY, this.toCreateElementType.indexOf('Line')!=-1);
		this.currentElement = this.createElement(this.toCreateElementType); //创建图形元素,由继承者实现
		this.elements.push(this.currentElement);
		this.currentElement.create(point.x, point.y, this.connectElement, this.connectIndex);
		this._repaint(this.currentElement.getDisplayArea());
	}
};
//鼠标移动
GraphicsEditor.prototype._onMouseMove = function(event) {
	if(!this.drag) { //不在拖动中
		if(this.toCreateElementType==null) {
			this._setCursor(event.offsetX, event.offsetY);
		}
		else if(this.toCreateElementType.indexOf('Line')!=-1) {
			this.checkNear(event.offsetX, event.offsetY);
		}
	}
	else if(this.toCreateElementType!=null) { //正在创建新元素
		if(this.toCreateElementType.indexOf('Line')!=-1) {
			this.checkNear(event.offsetX, event.offsetY);
		}
		var point = this.getNearestConnect(event.offsetX, event.offsetY, this.toCreateElementType.indexOf('Line')!=-1);
		var displayArea = this.currentElement.getDisplayArea();
		this.currentElement.creating(point.x, point.y, this.connectElement, this.connectIndex);
		this.connectElement = null;
		this._repaint(this._mergeArea(displayArea, this.currentElement.getDisplayArea()));
	}
	else if(this.currentElement==null) { //鼠标在画板上拖动选择元素
		if(event.ctrlKey || this.selectStartX==-1) {
			return;
		}
		var area = {left:this.selectStartX, top:this.selectStartY, right:this.selectX, bottom:this.selectY};
		this.selectX = event.offsetX;
		this.selectY = event.offsetY;
		var left = Math.min(this.selectStartX, this.selectX);
		var top = Math.min(this.selectStartY, this.selectY);
		var right = Math.max(this.selectStartX, this.selectX);
		var bottom = Math.max(this.selectStartY, this.selectY);
		for(var i = this.elements.length - 1; i>=0; i--) {
			if(!this.elements[i].readOnly && this.elements[i].selected != this.elements[i].intersects(left, top, right, bottom)) {
				this.elements[i].setSelected(!this.elements[i].selected);
				area = this._mergeArea(area, this.elements[i].getDisplayArea());
			}
		}
		this._repaint(this._mergeArea(area, {left:left, top:top, right:right, bottom:bottom}));
	}
	else { //鼠标在元素上拖动，修改元素位置或尺寸
		var displayArea = this.currentElement.getDisplayArea();
		var point = this.currentElement.move(this.selectStartX, this.selectStartY, event.offsetX, event.offsetY);
		this.selectStartX += point.x;
		this.selectStartY += point.y;
		this.modified = true;
		this._repaint(this._mergeArea(displayArea, this.currentElement.getDisplayArea()));
	}
};
//鼠标弹起
GraphicsEditor.prototype._onMouseUp = function(event) {
	if(!this.drag) {
		return;
	}
	this.drag = false;
	if(event.button!=0) { //不是鼠标左键按下
		return;
	}
	if(this.toCreateElementType==null) { //不在创建新元素
		var area = {left:this.selectStartX, top:this.selectStartY, right:this.selectX, bottom:this.selectY};
		this.selectX = -1;
		this.selectY = -1;
		this._repaint(area);
		this._cleanNear();
	}
	else if(this.currentElement!=null) { //完成创建新元素
		var area = this.currentElement.getDisplayArea();
		var isLine = this.toCreateElementType.indexOf('Line')!=-1;
		var point = this.getNearestConnect(event.offsetX, event.offsetY, isLine);
		this.currentElement.createComplete(point.x, point.y, this.connectElement, this.connectIndex);
		this.showPropertyTree();
		this._repaint(this._mergeArea(area, this.currentElement.getDisplayArea()));
	}
};
//添加元素
GraphicsEditor.prototype.addElement = function(element) {
	this.elements.push(element);
}
//设置将要创建的元素类型
GraphicsEditor.prototype.setToCreateElementType = function(elementType) {
	this.toCreateElementType = elementType;
};
//合并区域
GraphicsEditor.prototype._mergeArea = function(area0, area1) {
	if(!area0) {
		return area1;
	}
	if(!area1) {
		return area0;
	}
	var newArea = {};
	newArea.left = Math.min(Math.min(area0.left, area0.right), Math.min(area1.left, area1.right));
	newArea.top = Math.min(Math.min(area0.top, area0.bottom), Math.min(area1.top, area1.bottom))
	newArea.right = Math.max(Math.max(area0.left, area0.right), Math.max(area1.left, area1.right));
	newArea.bottom = Math.max(Math.max(area0.top, area0.bottom), Math.max(area1.top, area1.bottom));
	return newArea;
};
//合并连接线区域
GraphicsEditor.prototype._mergeNodeLinesArea = function(area, node) {
	if(!(node instanceof GraphicsEditor.Node)) {
		return area;
	}
	for(var i = 0; i < node.exitLines.length; i++) {
		area = this._mergeArea(area, node.exitLines[i].getDisplayArea());
	}
	for(var i = 0; i < node.enterLines.length; i++) {
		area = this._mergeArea(area, node.enterLines[i].getDisplayArea());
	}
	return area;
};
//重绘指定区域
GraphicsEditor.prototype._repaint = function(area) {
	if(!area) {
		return;
	}
	var left = Math.min(area.left, area.right) - 10;
	var top =  Math.min(area.top, area.bottom) - 10;
	var right = Math.max(area.left, area.right) + 10;
	var bottom = Math.max(area.top, area.bottom) + 10;
	this.canvasContext.clearRect(left, top, right - left, bottom - top);
	this._drawGrid(left, top, right, bottom);
	this._drawElements(left, top, right, bottom, false); //重绘未选中的元素
	this._drawElements(left, top, right, bottom, true); //重绘选中的元素
	this._drawSelectBox();
};
//重绘元素
GraphicsEditor.prototype._drawElements = function(left, top, right, bottom, selectedElements) {
	for(var i = 0; i<this.elements.length; i++) {
		if(selectedElements!=this.elements[i].selected) {
			continue;
		}
		if(!this.elements[i].intersects(left, top, right, bottom)) {
			continue;
		}
		this.elements[i].draw(this.canvasContext);
		//输出标题
		if(this.elements[i].title!=null) {
			this.elements[i].drawTitle(this.canvasContext);
		}
		//绘制焦点
		if(this.elements[i].selected) {
			this.elements[i].drawFocus(this.canvasContext);
		}
		//绘制连接点
		if(this.elements[i].near) {
			this.elements[i].drawConnect(this.canvasContext);
		}
	}
};
//重绘一个元素
GraphicsEditor.prototype.redrawElement = function(element) {
	this._repaint(element.getDisplayArea());
};
//绘制网格
GraphicsEditor.prototype._drawGrid = function(left, top, right, bottom) {
	this.canvasContext.beginPath();
	this.canvasContext.lineWidth = 1;
	for(var i = Math.ceil(left / this.gridSize) * this.gridSize - 0.5; i <= right; i += this.gridSize) {
		this.canvasContext.moveTo(i, top);
		this.canvasContext.lineTo(i, bottom);
	}
	for(var i = Math.ceil(top / this.gridSize) * this.gridSize - 0.5; i <= bottom; i += this.gridSize) {
		this.canvasContext.moveTo(left, i);
		this.canvasContext.lineTo(right, i);
	}
	this.canvasContext.strokeStyle = this.gridColor;
	this.canvasContext.stroke();
	this.canvasContext.closePath();
};
//显示选取框
GraphicsEditor.prototype._drawSelectBox = function() {
	if(this.selectX==-1) {
		return;
	}
	this.canvasContext.beginPath();
	var left = Math.min(this.selectStartX, this.selectX);
	var top = Math.min(this.selectStartY, this.selectY);
	this.canvasContext.strokeStyle = this.selectColor;
	this.canvasContext.strokeRect(left - 0.5 , top - 0.5, Math.abs(this.selectStartX - this.selectX), Math.abs(this.selectStartY - this.selectY));
	this.canvasContext.closePath();
};
//取消选择
GraphicsEditor.prototype.deselect = function() {
	var area = null;
	for(var i = 0; i < this.elements.length; i++) {
		if(!this.elements[i].deselect()) {
			continue;
		}
		area = this._mergeArea(area, this.elements[i].getDisplayArea());
	}
	this._repaint(area);
};
//选中一组元素
GraphicsEditor.prototype.selectElements = function(elements) {
	if(!elements) {
		return;
	}
	this.deselect();
	var area = null;
	for(var i = elements.length - 1; i>=0; i--) {
		if(!elements[i].readOnly) {
			elements[i].setSelected(true);
			area = this._mergeArea(area, elements[i].getDisplayArea());
		}
	}
	this._repaint(area);
};
//根据指定方式调整尺寸
GraphicsEditor.prototype.offsetElements = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	var area = null;
	for(var i = this.elements.length - 1; i>=0; i--) {
		if(!this.elements[i].selected) {
			continue;
		}
		area = this._mergeArea(area, this.elements[i].getDisplayArea());
		area = this._mergeNodeLinesArea(area, this.elements[i]);
		this.elements[i].offset(offsetLeft, offsetTop, offsetRight, offsetBottom, mode);
		area = this._mergeNodeLinesArea(area, this.elements[i]);
		area = this._mergeArea(area, this.elements[i].getDisplayArea());
	}
	this._repaint(area);
};
//检查鼠标位置，设置鼠标样式
GraphicsEditor.prototype._setCursor = function(x, y) {
	for(var i = this.elements.length - 1; i>=0; i--) { //检查选中的元素
		if(!this.elements[i].selected) {
			continue;
		}
		var cursor = this.elements[i].getCursor(x, y);
		if(cursor!='default') {
			this.canvas.style.cursor = cursor;
			return;
		}
	}
	for(var i = this.elements.length - 1; i>=0; i--) { //检查非选中的元素
		if(this.elements[i].selected) {
			continue;
		}
		var cursor = this.elements[i].getCursor(x, y);
		if(cursor!='default') {
			this.canvas.style.cursor = cursor;
			return;
		}
	}
	this.canvas.style.cursor = 'default';
};
//选择
GraphicsEditor.prototype._doSelect = function(x, y, multiSelect) {
	this.selectStartX = x;
	this.selectStartY = y;
	var selected = false;
	this.currentElement = null;
	var area;
	for(var i = this.elements.length - 1; i >= 0; i--) {
		if(!this.elements[i].selected) {
			continue;
		}
		var cursor = this.elements[i].getCursor(x, y);
		if(cursor!='default') {
			if(multiSelect) {
				this.elements[i].deselect();
				this._setCursor(x, y);
				area = this._mergeArea(area, this.elements[i].getDisplayArea());
				selected = true;
				break;
			}
			else {
				this.currentElement = this.elements[i];
				this.canvas.style.cursor = cursor;
				selected = true;
				break;
			}
		}
	}
	if(!selected) {
		for(var i = this.elements.length - 1; i >= 0; i--) {
			if(this.elements[i].selected) {
				if(!multiSelect) {
					this.elements[i].deselect();
					area = this._mergeArea(area, this.elements[i].getDisplayArea());
				}
			}
			else if(!selected && !this.elements[i].readOnly && this.elements[i].isMouseOver(x, y, 0)) {
				this.elements[i].setSelected(true);
				this.canvas.style.cursor = this.elements[i].getCursor(x, y);
				this.currentElement = this.elements[i];
				selected = true;
				area = this._mergeArea(area, this.elements[i].getDisplayArea());
			}
		}
	}
	var editor = this;
	window.setTimeout(function() {
		editor.showPropertyTree();
	}, 100);
	this._repaint(area);
};
//停止创建新元素
GraphicsEditor.prototype.finishCreateElement = function() {
	var createLine = this.toCreateElementType && this.toCreateElementType.indexOf('Line')!=-1;
	this.toCreateElementType = null;
	this.selectStartX = -1;
	if(createLine) {
		this._cleanNear();
	}
	this.drawBar.cleanSelect();
};

//判断鼠标是否接近某个元素,有发生变化时返回true
GraphicsEditor.prototype.checkNear = function(x, y) {
	var area;
	for(var i = this.elements.length - 1; i>=0; i--) {
		var over = this.elements[i].isMouseOver(x, y, this.connectRange);
		if(over!=this.elements[i].near) {
			this.elements[i].near = over;
			area = this._mergeArea(area, this.elements[i].getDisplayArea());
		}
	}
	this._repaint(area);
};
//清除连接点显示
GraphicsEditor.prototype._cleanNear = function() {
	var area;
	for(var i = this.elements.length - 1; i>=0; i--) {
		if(this.elements[i].near) {
			this.elements[i].near = false;
			area = this._mergeArea(area, this.elements[i].getDisplayArea());
		}
	}
	this._repaint(area);
};
//获取最近的连接点
GraphicsEditor.prototype.getNearestConnect = function(x, y, isLine) {
	var point = null;
	if(isLine) { //画线
		var spaceMin = 1000000;
		for(var i = this.elements.length - 1; i>=0; i--) {
			if(!this.elements[i].near) {
				continue;
			}
			for(var j = this.elements[i].connectPoints.length - 1; j>=0; j--) {
				var space = Math.pow(x - this.elements[i].connectPoints[j].x, 2) + Math.pow(y - this.elements[i].connectPoints[j].y, 2);
				if(space >= spaceMin) {
					continue;
				}
				point = this.elements[i].connectPoints[j];
				this.connectElement = this.elements[i];
				this.connectIndex = j;
				spaceMin = space;
			}
		}
	}
	if(point==null) {
		this.connectElement = null;
		point = {x: x - x % this.gridSize, y: y - y % this.gridSize};
	}
	return point;
};
//全选
GraphicsEditor.prototype.selectAll = function() {
	this.finishCreateElement();
	var area;
	for(var i = this.elements.length - 1; i>=0; i--) {
		if(!this.elements[i].readOnly) {
			this.elements[i].setSelected(true);
			area = this._mergeArea(area, this.elements[i].getDisplayArea());
		}
	}
	this._repaint(area);
};
//删除选中的元素
GraphicsEditor.prototype.deleteElements = function() {
	this.finishCreateElement();
	var flag = false;
	var area;
	for(var i = this.elements.length - 1; i>=0; i--) {
		var displayArea = this.elements[i].getDisplayArea();
		if(this.elements[i].deleteElement()) {
			area = this._mergeArea(area, displayArea);
			this.elements.splice(i, 1);
			flag = true;
		}
	}
	this.currentElement = null;
	if(flag) {
		this._repaint(area);
		this.modified = true;
		this.showPropertyTree();
	}
};
//显示属性目录树
GraphicsEditor.prototype.showPropertyTree = function() {
	var propertyTree = this.currentElement!=null ? this.currentElement.getPropertyTree() : this.getPropertyTree();
	this.propertyTreeContainer.innerHTML = "";
	if(!propertyTree) {
		return;
	}
	//创建树
	this.propertyTree = new Tree(propertyTree.id, propertyTree.label, '', RequestUtils.getContextPath() + "/jeaf/graphicseditor/icons/propertyRoot.gif", '', this.propertyTreeContainer, false);
	this._appendChildNodes(propertyTree);
	var editor = this;
	this.propertyTree.onNodeSeleced = function(nodeId, nodeText, nodeType, leafNode) {
		editor._showPropertyDetail(nodeId);
	};
	//打开和同名属性页,如果没有打开根的属性页
	if(!this.lastPropertyName || !this.propertyTree.selectNodeById(this.lastPropertyName)) {
		this.propertyTree.selectNodeById(propertyTree.id);
	}
};
//递归:添加子节点
GraphicsEditor.prototype._appendChildNodes = function(parentNode) {
	for(var i=0; i < (parentNode.childNodes ? parentNode.childNodes.length : 0); i++) {
		var node = parentNode.childNodes[i];
	 	this.propertyTree.appendNode(parentNode.id, node.id, node.label, '', RequestUtils.getContextPath() + "/jeaf/graphicseditor/icons/propertyNode.gif", '', node.childNodes && node.childNodes.length>0, false);
	 	this._appendChildNodes(node); //添加下级
	}
};
//显示属性明细
GraphicsEditor.prototype._showPropertyDetail = function(propertyName) {
	this.propertyDetailContainer.innerHTML = '';
	this.lastPropertyName = propertyName;
	var propertyList = this.currentElement!=null ? this.currentElement.getPropertyList(propertyName) : this.getPropertyList(propertyName);
	if(!propertyList) {
		propertyList = [new GraphicsEditor.Property(null, null, null)];
	}
	for(var i=0; i < propertyList.length; i++) {
		propertyList[i].write(this.propertyDetailContainer);
	}
};
//添加surface
GraphicsEditor.prototype.addSurface = function(jsonObjectPool, className, id) {
	var surface = JsonUtils.addJsonObject(jsonObjectPool, className, {id:id});
	surface.startX = surface.startY = surface.endX = surface.endY = -1;
	if(className.indexOf('BrokenLine')!=-1) {
		surface.turningXPoints = [];
		surface.turningYPoints = [];
	}
	return surface;
};
//获取surface
GraphicsEditor.prototype.getSurface = function(jsonObjectPool, elementId) {
	for(var i=0; i<(jsonObjectPool ? jsonObjectPool.length : 0); i++) {
		if(jsonObjectPool[i].id==elementId && (jsonObjectPool[i].startX || jsonObjectPool[i].startX==0)) {
			return jsonObjectPool[i];
		}
	}
	return null;
};