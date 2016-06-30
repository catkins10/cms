
GraphicsEditor = function(canvasView, drawBarContainer, drawBarButtons, propertyTreeContainer, propertyDetailContainer, readOnly) {
	if(!canvasView) {
		return;
	}
	
	this.selectColor = '#cccccc'; 
	this.lineColor = '#000000'; 
	this.textColor = '#000000'; 
	this.bgColor = '#ffffff'; 
	this.gridColor = '#e4edff'; 
	this.focusColor = '#20cc50'; 
	this.connectColor = '#1010ff'; 
	this.readOnlyColor = '#3f7f5f'; 
	this.gridSize = 8; 
	this.connectRange = 15; 
	this.font = '12px \u5B8B\u4F53'; 
	
	this.modified = false; 
	this.elements = []; 
	this.toCreateElementType = null; 
	this.currentElement = null; 
	this.connectElement = null; 
	this.connectIndex = -1; 
	this.selectStartX = -1; 
	this.selectStartY = -1; 
	this.selectX = -1; 
	this.selectY = -1; 
	
	var editor = this;
	this.propertyTreeContainer = propertyTreeContainer;
	this.propertyDetailContainer = propertyDetailContainer;
	
	this.drawBar = new GraphicsEditor.DrawBar(this, drawBarContainer, drawBarButtons, readOnly);
	
	this.canvas = canvasView.ownerDocument.createElement('canvas');
	this.width = this.canvas.width = 7500;
	this.height = this.canvas.height = 1500;
	canvasView.appendChild(this.canvas);
	if(!this.canvas.getContext) {
		document.open();
		document.write('<html><body style="font-size:12px;">\u5F53\u524D\u6D4F\u89C8\u5668\u4E0D\u652F\u6301HTML5,\u8BF7\u4F7F\u7528\u652F\u6301HTML5\u7684\u6D4F\u89C8\u5668\u6253\u5F00\u672C\u9875\u9762</body></html>');
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
	
	this.init();
	
	this._repaint({left:0, top:0, right:this.width, bottom:this.height});
	
	this.showPropertyTree();
};
GraphicsEditor.prototype.init = function() {
};
GraphicsEditor.prototype.createElement = function(elementType) {
};
GraphicsEditor.prototype.removeElement = function(element) {
};
GraphicsEditor.prototype.getPropertyTree = function() {
};
GraphicsEditor.prototype.getPropertyList = function(propertyName) {
};
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
	else if(event.button==2) { 
		this.finishCreateElement();
		this._setCursor(event.offsetX, event.offsetY);
	}
	else if(event.button==0) { 
		this.modified = true;
		this.deselect();
		var point = this.getNearestConnect(event.offsetX, event.offsetY, this.toCreateElementType.indexOf('Line')!=-1);
		this.currentElement = this.createElement(this.toCreateElementType); 
		this.elements.push(this.currentElement);
		this.currentElement.create(point.x, point.y, this.connectElement, this.connectIndex);
		this._repaint(this.currentElement.getDisplayArea());
	}
};
GraphicsEditor.prototype._onMouseMove = function(event) {
	if(!this.drag) { 
		if(this.toCreateElementType==null) {
			this._setCursor(event.offsetX, event.offsetY);
		}
		else if(this.toCreateElementType.indexOf('Line')!=-1) {
			this.checkNear(event.offsetX, event.offsetY);
		}
	}
	else if(this.toCreateElementType!=null) { 
		if(this.toCreateElementType.indexOf('Line')!=-1) {
			this.checkNear(event.offsetX, event.offsetY);
		}
		var point = this.getNearestConnect(event.offsetX, event.offsetY, this.toCreateElementType.indexOf('Line')!=-1);
		var displayArea = this.currentElement.getDisplayArea();
		this.currentElement.creating(point.x, point.y, this.connectElement, this.connectIndex);
		this.connectElement = null;
		this._repaint(this._mergeArea(displayArea, this.currentElement.getDisplayArea()));
	}
	else if(this.currentElement==null) { 
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
	else { 
		var displayArea = this.currentElement.getDisplayArea();
		var point = this.currentElement.move(this.selectStartX, this.selectStartY, event.offsetX, event.offsetY);
		this.selectStartX += point.x;
		this.selectStartY += point.y;
		this.modified = true;
		this._repaint(this._mergeArea(displayArea, this.currentElement.getDisplayArea()));
	}
};
GraphicsEditor.prototype._onMouseUp = function(event) {
	if(!this.drag) {
		return;
	}
	this.drag = false;
	if(event.button!=0) { 
		return;
	}
	if(this.toCreateElementType==null) { 
		var area = {left:this.selectStartX, top:this.selectStartY, right:this.selectX, bottom:this.selectY};
		this.selectX = -1;
		this.selectY = -1;
		this._repaint(area);
		this._cleanNear();
	}
	else if(this.currentElement!=null) { 
		var area = this.currentElement.getDisplayArea();
		var isLine = this.toCreateElementType.indexOf('Line')!=-1;
		var point = this.getNearestConnect(event.offsetX, event.offsetY, isLine);
		this.currentElement.createComplete(point.x, point.y, this.connectElement, this.connectIndex);
		this.showPropertyTree();
		this._repaint(this._mergeArea(area, this.currentElement.getDisplayArea()));
	}
};
GraphicsEditor.prototype.addElement = function(element) {
	this.elements.push(element);
}
GraphicsEditor.prototype.setToCreateElementType = function(elementType) {
	this.toCreateElementType = elementType;
};
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
	this._drawElements(left, top, right, bottom, false); 
	this._drawElements(left, top, right, bottom, true); 
	this._drawSelectBox();
};
GraphicsEditor.prototype._drawElements = function(left, top, right, bottom, selectedElements) {
	for(var i = 0; i<this.elements.length; i++) {
		if(selectedElements!=this.elements[i].selected) {
			continue;
		}
		if(!this.elements[i].intersects(left, top, right, bottom)) {
			continue;
		}
		this.elements[i].draw(this.canvasContext);
		
		if(this.elements[i].title!=null) {
			this.elements[i].drawTitle(this.canvasContext);
		}
		
		if(this.elements[i].selected) {
			this.elements[i].drawFocus(this.canvasContext);
		}
		
		if(this.elements[i].near) {
			this.elements[i].drawConnect(this.canvasContext);
		}
	}
};
GraphicsEditor.prototype.redrawElement = function(element) {
	this._repaint(element.getDisplayArea());
};
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
GraphicsEditor.prototype._setCursor = function(x, y) {
	for(var i = this.elements.length - 1; i>=0; i--) { 
		if(!this.elements[i].selected) {
			continue;
		}
		var cursor = this.elements[i].getCursor(x, y);
		if(cursor!='default') {
			this.canvas.style.cursor = cursor;
			return;
		}
	}
	for(var i = this.elements.length - 1; i>=0; i--) { 
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
GraphicsEditor.prototype.finishCreateElement = function() {
	var createLine = this.toCreateElementType && this.toCreateElementType.indexOf('Line')!=-1;
	this.toCreateElementType = null;
	this.selectStartX = -1;
	if(createLine) {
		this._cleanNear();
	}
	this.drawBar.cleanSelect();
};
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
GraphicsEditor.prototype.getNearestConnect = function(x, y, isLine) {
	var point = null;
	if(isLine) { 
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
GraphicsEditor.prototype.showPropertyTree = function() {
	var propertyTree = this.currentElement!=null ? this.currentElement.getPropertyTree() : this.getPropertyTree();
	this.propertyTreeContainer.innerHTML = "";
	if(!propertyTree) {
		return;
	}
	
	this.propertyTree = new Tree(propertyTree.id, propertyTree.label, '', RequestUtils.getContextPath() + "/jeaf/graphicseditor/icons/propertyRoot.gif", '', this.propertyTreeContainer, false);
	this._appendChildNodes(propertyTree);
	var editor = this;
	this.propertyTree.onNodeSeleced = function(nodeId, nodeText, nodeType, leafNode) {
		editor._showPropertyDetail(nodeId);
	};
	
	if(!this.lastPropertyName || !this.propertyTree.selectNodeById(this.lastPropertyName)) {
		this.propertyTree.selectNodeById(propertyTree.id);
	}
};
GraphicsEditor.prototype._appendChildNodes = function(parentNode) {
	for(var i=0; i < (parentNode.childNodes ? parentNode.childNodes.length : 0); i++) {
		var node = parentNode.childNodes[i];
	 	this.propertyTree.appendNode(parentNode.id, node.id, node.label, '', RequestUtils.getContextPath() + "/jeaf/graphicseditor/icons/propertyNode.gif", '', node.childNodes && node.childNodes.length>0, false);
	 	this._appendChildNodes(node); 
	}
};
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
GraphicsEditor.prototype.addSurface = function(jsonObjectPool, className, id) {
	var surface = JsonUtils.addJsonObject(jsonObjectPool, className, {id:id});
	surface.startX = surface.startY = surface.endX = surface.endY = -1;
	if(className.indexOf('BrokenLine')!=-1) {
		surface.turningXPoints = [];
		surface.turningYPoints = [];
	}
	return surface;
};
GraphicsEditor.prototype.getSurface = function(jsonObjectPool, elementId) {
	for(var i=0; i<(jsonObjectPool ? jsonObjectPool.length : 0); i++) {
		if(jsonObjectPool[i].id==elementId && (jsonObjectPool[i].startX || jsonObjectPool[i].startX==0)) {
			return jsonObjectPool[i];
		}
	}
	return null;
};


GraphicsEditor.Shape = function(graphicsEditor, surface, title) {
	if(!graphicsEditor) {
		return;
	}
	this.focusRadii = 2; 
	this.connectRadii = 1; 
	this.fillColor = null; 
	this.lineColor = null; 
	this.textColor = null; 
	
	this.graphicsEditor = graphicsEditor; 
	this.surface = surface; 
	this.title = title; 
	
	
	this.minWidth = 2 * graphicsEditor.gridSize;
	this.minHeight = 2 * graphicsEditor.gridSize;
	
	
	this.defaultWidth = 10 * graphicsEditor.gridSize;
	this.defaultHeight = 6 * graphicsEditor.gridSize;
	
	
	this.focusPoints = []; 
	this.connectPoints = []; 
	this.focusIndex = -1; 
	this.selected = false; 
	this.near = false; 
	this.resetDisplayProperties('init');
};
GraphicsEditor.Shape.prototype.draw = function(canvasContext) {
	
};
GraphicsEditor.Shape.prototype.drawTitle = function(canvasContext) {
};
GraphicsEditor.Shape.prototype.resetDisplayProperties = function(resetFor) {
	
};
GraphicsEditor.Shape.prototype.isMouseOver = function(x, y) {
	
};
GraphicsEditor.Shape.prototype.intersects = function(left, top, right, bottom) {
	
};
GraphicsEditor.Shape.prototype.move = function(startX, startY, endX, endY) {
	
};
GraphicsEditor.Shape.prototype.offset = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	
};
GraphicsEditor.Shape.prototype.getDisplayArea = function() {
	
};
GraphicsEditor.Shape.prototype.getPropertyTree = function() {
	
};
GraphicsEditor.Shape.prototype.getPropertyList = function(propertyName) {
	
};
GraphicsEditor.Shape.prototype.getLeft = function() {
	return Math.min(this.surface.startX, this.surface.endX);
};
GraphicsEditor.Shape.prototype.getTop = function() {
	return Math.min(this.surface.startY, this.surface.endY);
};
GraphicsEditor.Shape.prototype.getRight = function() {
	return Math.max(this.surface.startX, this.surface.endX);
};
GraphicsEditor.Shape.prototype.getBottom = function() {
	return Math.max(this.surface.startY, this.surface.endY);
};
GraphicsEditor.Shape.prototype.getWidth = function() {
	return Math.abs(this.surface.startX - this.surface.endX);
};
GraphicsEditor.Shape.prototype.getHeight = function() {
	return Math.abs(this.surface.startY - this.surface.endY);
};
GraphicsEditor.Shape.prototype.create = function(x, y, connectElement, connectIndex) {
	this.surface.startX = x;
	this.surface.startY = y;
	this.surface.endX = x;
	this.surface.endY = y;
	this.selected = true;
	this.resetDisplayProperties('create');
};
GraphicsEditor.Shape.prototype.creating = function(x, y, connectElement, connectIndex) {
	if(this.surface.endX==x && this.surface.endY==y) {
		return false;
	}
	this.surface.endX = x;
	this.surface.endY = y;
	this.resetDisplayProperties('creating');
	return true;
};
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
GraphicsEditor.Shape.prototype.deselect = function() {
	if(this.selected) {
		this.selected = false;
		return true;
	}
	return false;
};
GraphicsEditor.Shape.prototype.getLineColor = function() {
	return this.lineColor!=null ? this.lineColor : (this.surface.readOnly ? this.graphicsEditor.readOnlyColor : this.graphicsEditor.lineColor);
};
GraphicsEditor.Shape.prototype.getFillColor = function() {
	return this.fillColor!=null ? this.fillColor : this.graphicsEditor.bgColor;
};
GraphicsEditor.Shape.prototype.getTextColor = function() {
	return this.textColor!=null ? this.textColor : (this.surface.readOnly ? this.graphicsEditor.readOnlyColor : this.graphicsEditor.textColor);
};
GraphicsEditor.Shape.prototype.measureText = function(canvasContext, str, font) {
	canvasContext.font = font;
	return {width:canvasContext.measureText(str).width, height:canvasContext.measureText('\u5BAA').width};
};
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
GraphicsEditor.Shape.prototype.drawFocus = function(canvasContext) {
	for(var i = this.focusPoints.length - 1; i>=0; i--) {
		this.drawRect(canvasContext, this.focusPoints[i].x - this.focusRadii, this.focusPoints[i].y - this.focusRadii, this.focusPoints[i].x + this.focusRadii, this.focusPoints[i].y + this.focusRadii, this.graphicsEditor.focusColor, this.graphicsEditor.bgColor, 1, 0);
	}
};
GraphicsEditor.Shape.prototype.drawConnect = function(canvasContext) {
	for(var i = this.connectPoints.length - 1; i >= 0; i--) {
		this.drawRect(canvasContext, this.connectPoints[i].x - this.connectRadii, this.connectPoints[i].y - this.connectRadii, this.connectPoints[i].x + this.connectRadii, this.connectPoints[i].y + this.connectRadii, this.graphicsEditor.connectColor, this.graphicsEditor.connectColor, 1, 0);
	}
};
GraphicsEditor.Shape.prototype.deleteElement = function() {
	if(!this.selected || this.surface.deleteDisable) {
		return false;
	}
	this.graphicsEditor.removeElement(this);
	return true;
};
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
GraphicsEditor.Shape.prototype.isMouseOverFocus = function(focus, x, y) {
	x -= focus.x;
	y -= focus.y;
	return x >= this.connectRadii - 3 && y >= this.connectRadii - 3 && x <= this.connectRadii + 3 && y <= this.connectRadii + 3;
};
GraphicsEditor.Shape.prototype.setSelected = function(selected) {
	if(this.selected!=selected) {
		this.selected = selected;
		this.resetDisplayProperties(selected ? 'select' : 'deselect');
	}
};
GraphicsEditor.Shape.prototype.setTitle = function(title) {
	if(title==this.title) {
		return;
	}
	this.title = title;
	this.graphicsEditor.redrawElement(this);
	this.graphicsEditor.showPropertyTree();
};

GraphicsEditor.DrawBar = function(graphicsEditor, drawBarContainer, drawBarButtons, disabled) {
	this.graphicsEditor = graphicsEditor;
	this.disabled = disabled;
	var drawBar = this;
	for(var i=0; i<drawBarButtons.length; i++) {
		var div = drawBarContainer.ownerDocument.createElement("div");
		div.className = "drawAction";
		div.elementType = drawBarButtons[i].elementType;
		div.title = drawBarButtons[i].name;
		div.onclick = function() {
			if(drawBar.disabled) {
				return;
			}
			if(this.className=="drawAction actionSelect") {
				return;
			}
			drawBar.cleanSelect();
			this.className = "drawAction actionSelect";
			drawBar.graphicsEditor.setToCreateElementType(this.elementType);
			drawBar.selectedButton = this;
		};
		div.onmouseover = function() {
			if(!drawBar.disabled && this.className!="drawAction actionSelect") {
				this.className = "drawAction actionOver";
			}
		};
		div.onmouseout = function() {
			if(!drawBar.disabled && this.className!="drawAction actionSelect") {
				this.className = "drawAction";
			}
		};
		var img = drawBarContainer.ownerDocument.createElement('img');
		img.src = RequestUtils.getContextPath() + drawBarButtons[i].iconUrl;
		img.alt = drawBarButtons[i].name;
		div.appendChild(img);
		if(drawBar.disabled) {
			CssUtils.setGray(img);
		}
		drawBarContainer.appendChild(div);
	}
};
GraphicsEditor.DrawBar.prototype.cleanSelect = function() {
	if(this.selectedButton) {
		this.selectedButton.className = "drawAction";
		this.selectedButton = null;
	}
};


GraphicsEditor.Line = function(graphicsEditor, transition, surface, drawArrow, turningRadii, title) {
	if(!graphicsEditor) {
		return;
	}
	this.brokenLine = surface.uuid.indexOf('BrokenLine')!=-1; 
	this.drawArrow = drawArrow; 
	this.turningMin = graphicsEditor.gridSize * 2; 
	this.turningRadii = turningRadii; 
	this.transition = transition; 
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
GraphicsEditor.Line.prototype.resetDisplayProperties = function(resetFor) {
	if(!this.brokenLine) { 
		
		this.focusPoints.length = 0;
		this.focusPoints.push({x:this.surface.startX, y:this.surface.startY, cursor:'crosshair', resizeMode:'crosshair'});
		this.focusPoints.push({x:this.surface.endX, y:this.surface.endY, cursor:'crosshair', resizeMode:'crosshair'});
		
		this._setArrow(this.surface.startX, this.surface.startY, this.surface.endX, this.surface.endY);
		return;
	}
	if(!this.surface.turningXPoints || ',create,creating,createComplete,moveStartPoint,moveEndPoint,'.indexOf(',' + resetFor + ',')!=-1) {
		this._setTurningPoints(); 
	}
	
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
	
	var index = this.surface.turningXPoints.length - 1;
	this._setArrow(this.surface.turningXPoints[index], this.surface.turningYPoints[index], this.surface.endX, this.surface.endY);
};
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
GraphicsEditor.Line.prototype.draw = function(canvasContext) {
	canvasContext.beginPath();
	canvasContext.strokeStyle = this.getLineColor(); 
	if(!this.brokenLine) { 
		canvasContext.moveTo(this.surface.startX - 0.5, this.surface.startY - 0.5);
		canvasContext.lineTo(this.surface.endX - 0.5, this.surface.endY - 0.5);
	}
	else { 
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
	
	this._drawArrow(canvasContext);
};
GraphicsEditor.Line.prototype.drawTitle = function(canvasContext) {
	this._setTitlePosition(canvasContext); 
	if(this.title!=null && this.titleX!=-1) {
		canvasContext.beginPath;
		canvasContext.fillStyle = this.getTextColor();
		canvasContext.fillText(this.title, this.titleX, this.titleY - 3);
		canvasContext.closePath();
	}
};
GraphicsEditor.Line.prototype.getDisplayArea = function() {
	var area = {left: Math.min(this.getLeft(), this.titleX==-1 ? 888888 : this.titleX),
				top: Math.min(this.getTop(), this.titleY==-1 ? 888888 : this.titleY - this.titleHeight),
				right: Math.max(this.getRight(), this.titleX + this.titleWidth),
				bottom: Math.max(this.getBottom(), this.titleY)};
	if(!this.brokenLine) { 
		return area;
	}
	
	for(var i=0; i < this.surface.turningXPoints.length; i++) {
		area.left = Math.min(area.left, this.surface.turningXPoints[i]);
		area.top = Math.min(area.top, this.surface.turningYPoints[i]);
		area.right = Math.max(area.right, this.surface.turningXPoints[i]);
		area.bottom = Math.max(area.bottom, this.surface.turningYPoints[i]);
	}
	return area;
};
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
GraphicsEditor.Line.prototype.create = function(x, y, connectElement, connectIndex) {
	this.setFromElement(connectElement, connectIndex);
	GraphicsEditor.Shape.prototype.create.call(this, x, y, connectElement, connectIndex);
};
GraphicsEditor.Line.prototype.creating = function(x, y, connectElement, connectIndex) {
	this.setToElement(connectElement, connectIndex);
	return GraphicsEditor.Shape.prototype.creating.call(this, x, y, connectElement, connectIndex);
};
GraphicsEditor.Line.prototype.createComplete = function(x, y, connectElement, connectIndex) {
	var ret =  GraphicsEditor.Shape.prototype.createComplete.call(this, x, y, connectElement, connectIndex);
	this.setToElement(ret ? connectElement : null, ret ? connectIndex : -1);
	return ret;
};
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
GraphicsEditor.Line.prototype.intersects = function(left, top, right, bottom) {
	if(!this.brokenLine) { 
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
GraphicsEditor.Line.prototype._getX = function(y) {
	return this.surface.startX + (y - this.surface.startY) / (this.surface.endY - this.surface.startY) * (this.surface.endX - this.surface.startX);
};
GraphicsEditor.Line.prototype._getY = function(x) {
	return this.surface.startY + (x - this.surface.startX) / (this.surface.endX - this.surface.startX) * (this.surface.endY - this.surface.startY);
};
GraphicsEditor.Line.prototype.isMouseOver = function(x, y, offset) {
	if(offset<3) {
		offset=3;
	}
	if(!this.brokenLine) { 
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
GraphicsEditor.Line.prototype.move = function(startX, startY, endX, endY) {
	if(!this.brokenLine || this.surface.moveDisable || this.focusIndex < 2) { 
		var offsetX = endX - startX;
		var offsetY = endY - startY;
		if(this.surface.moveDisable) { 
			return {x:offsetX, y:offsetY};
		}
		if(this.focusIndex==-1) { 
			offsetX -= offsetX % this.graphicsEditor.gridSize;
			offsetY -= offsetY % this.graphicsEditor.gridSize;
			if(offsetX==0 && offsetY==0) {
				return {x:offsetX, y:offsetY};
			}
			this.graphicsEditor.offsetElements(offsetX, offsetY, offsetX, offsetY, 'move');
		}
		else {
			this.graphicsEditor.checkNear(endX, endY);
			
			var point = this.graphicsEditor.getNearestConnect(endX, endY, true);
			var element = this.graphicsEditor.connectElement;
			if(this.focusIndex==0) { 
				if(startX==point.x && startY==point.y) {
					return {x:0, y:0};
				}
				this.setFromElement(element, this.graphicsEditor.connectIndex);
				this.surface.startX = point.x;
				this.surface.startY = point.y;
			}
			else { 
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
	
	var focus = this.focusPoints[this.focusIndex];
	if(focus.cursor=='n-resize') { 
		endY -= endY % this.graphicsEditor.gridSize;
		this.surface.turningYPoints[focus.resizeMode - 1] = this.surface.turningYPoints[focus.resizeMode] = endY;
	}
	else { 
		endX -= endX % this.graphicsEditor.gridSize;
		this.surface.turningXPoints[focus.resizeMode - 1] = this.surface.turningXPoints[focus.resizeMode] = endX;
	}
	this.resetDisplayProperties('move');
	return {x:0, y:0};
};
GraphicsEditor.Line.prototype.offset = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	if(mode!='move' || this.surface.moveDisable) {
		return;
	}
	var selectEnter = false; 
	if(this.fromElement) {
		 selectEnter = this.fromElement.selected;
	}
	var selectExit = false; 
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
GraphicsEditor.Line.prototype._afterOffset = function(offsetLeft, offsetTop) {
	for(var i = (this.brokenLine ? this.surface.turningXPoints.length - 1 : -1); i >= 0; i--) {
		this.surface.turningXPoints[i] += offsetLeft;
		this.surface.turningYPoints[i] += offsetTop;
	}
	this.resetDisplayProperties('offset');
};
GraphicsEditor.Line.prototype.moveStartPoint = function(point) {
	this.surface.startX = point.x;
	this.surface.startY = point.y;
	this.resetDisplayProperties('moveStartPoint');
};
GraphicsEditor.Line.prototype.moveEndPoint = function(point) {
	this.surface.endX = point.x;
	this.surface.endY = point.y;
	this.resetDisplayProperties('moveEndPoint');
};
GraphicsEditor.Line.prototype._setTitlePosition = function(canvasContext) {
	this.titleWidth = -1;
	this.titleHeight = -1;
	this.titleX = -1;
	this.titleY = -1;
	if(this.title==null) {
		return;
	}
	if(!this.brokenLine) { 
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
GraphicsEditor.Line.prototype._setTurningPoints = function() {
	this.surface.turningXPoints = [];
	this.surface.turningYPoints = [];
	var startOrientation = '', endOrientation = ''; 
	var startElement = null, endElement = null;
	if(this.fromElement) { 
		startElement = this.fromElement;
		if(this.surface.startX==startElement.getLeft()) {
			startOrientation = 'w-resize'; 
		}
		else if(this.surface.startX==startElement.getRight()) {
			startOrientation = 'e-resize'; 
		}
		else if(this.surface.startY==startElement.getTop()) {
			startOrientation = 'n-resize'; 
		}
		else if(this.surface.startY==startElement.getBottom()) {
			startOrientation = 's-resize'; 
		}
	}
	if(this.toElement) { 
		endElement = this.toElement;
		if(this.surface.endX==endElement.getLeft()) {
			endOrientation = 'w-resize'; 
		}
		else if(this.surface.endX==endElement.getRight()) {
			endOrientation = 'e-resize'; 
		}
		else if(this.surface.endY==endElement.getTop()) {
			endOrientation = 'n-resize'; 
		}
		else if(this.surface.endY==endElement.getBottom()) {
			endOrientation = 's-resize'; 
		}
	}
	if(startOrientation=='') { 
		if(endOrientation=='') {
			this._setTurningPointsNoneToNone();
		}
		else if(endOrientation=='w-resize') { 
			this._setTurningPointsNoneToWest(endElement, false);
		}
		else if(endOrientation=='e-resize') { 
			this._setTurningPointsNoneToEast(endElement, false);
		}
		else if(endOrientation=='n-resize') { 
			this._setTurningPointsNoneToNorth(endElement, false);
		}
		else if(endOrientation=='s-resize') { 
			this._setTurningPointsNoneToSouth(endElement, false);
		}
	}
	else if(startOrientation=='w-resize') { 
		if(endOrientation=='') {
			this._setTurningPointsNoneToWest(startElement, true);
		}
		else if(endOrientation=='w-resize') { 
			this._setTurningPointsWestToWest(startElement, endElement);
		}
		else if(endOrientation=='e-resize') { 
			this._setTurningPointsWestToEast(startElement, endElement, false);
		}
		else if(endOrientation=='n-resize') { 
			this._setTurningPointsWestToNorth(startElement, endElement, false);
		}
		else if(endOrientation=='s-resize') { 
			this._setTurningPointsWestToSouth(startElement, endElement, false);
		}
	}
	else if(startOrientation=='e-resize') { 
		if(endOrientation=='') {
			this._setTurningPointsNoneToEast(startElement, true);
		}
		else if(endOrientation=='w-resize') { 
			this._setTurningPointsWestToEast(endElement, startElement, true);
		}
		else if(endOrientation=='e-resize') { 
			this._setTurningPointsEastToEast(startElement, endElement);
		}
		else if(endOrientation=='n-resize') { 
			this._setTurningPointsEastToNorth(startElement, endElement, false);
		}
		else if(endOrientation=='s-resize') { 
			this._setTurningPointsEastToSouth(startElement, endElement, false);
		}
	}
	else if(startOrientation=='n-resize') {  
		if(endOrientation=='') {
			this._setTurningPointsNoneToNorth(startElement, true);
		}
		else if(endOrientation=='w-resize') { 
			this._setTurningPointsWestToNorth(endElement, startElement, true);
		}
		else if(endOrientation=='e-resize') { 
			this._setTurningPointsEastToNorth(endElement, startElement, true);
		}
		else if(endOrientation=='n-resize') { 
			this._setTurningPointsNorthToNorth(startElement, endElement);
		}
		else if(endOrientation=='s-resize') { 
			this._setTurningPointsNorthToSouth(startElement, endElement, false);
		}
	}
	else if(startOrientation=='s-resize') {  
		if(endOrientation=='') {
			this._setTurningPointsNoneToSouth(startElement, true);
		}
		else if(endOrientation=='w-resize') { 
			this._setTurningPointsWestToSouth(endElement, startElement, true);
		}
		else if(endOrientation=='e-resize') { 
			this._setTurningPointsEastToSouth(endElement, startElement, true);
		}
		else if(endOrientation=='n-resize') { 
			this._setTurningPointsNorthToSouth(endElement, startElement, true);
		}
		else if(endOrientation=='s-resize') { 
			this._setTurningPointsSouthToSouth(startElement, endElement);
		}
	}
};
GraphicsEditor.Line.prototype._addTurningPoint = function(x, y) {
	this.surface.turningXPoints.push(x);
	this.surface.turningYPoints.push(y);
};
GraphicsEditor.Line.prototype._reverseTurningPoints = function() {
	this.surface.turningXPoints = this.surface.turningXPoints.reverse();
	this.surface.turningYPoints = this.surface.turningYPoints.reverse();
};
GraphicsEditor.Line.prototype._setTurningPointsNoneToNone = function() {
	if(Math.abs(this.surface.startX - this.surface.endX) < Math.abs(this.surface.startY - this.surface.endY)) {
		
		this._addTurningPoint(this.surface.startX, this.surface.startY + (this.surface.endY - this.surface.startY) / 2);
		this._addTurningPoint(this.surface.endX, this.surface.startY + (this.surface.endY - this.surface.startY) / 2);
	}
	else {
		
		this._addTurningPoint(this.surface.startX + (this.surface.endX - this.surface.startX) / 2, this.surface.startY);
		this._addTurningPoint(this.surface.startX + (this.surface.endX - this.surface.startX) / 2, this.surface.endY);
	}
};
GraphicsEditor.Line.prototype._setTurningPointsNoneToWest = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX <= endElement.getLeft() - this.turningMin) {
		
		var x = startX + (endX - startX) / 2;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endY);
	}
	else if(startY > endElement.getTop() - this.turningMin && startY < endElement.getBottom() + this.turningMin) {
		
		this._addTurningPoint(startX, endElement.getTop()-this.turningMin);
		this._addTurningPoint(endElement.getLeft() - this.turningMin, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endElement.getLeft() - this.turningMin, endY);
	}
	else {
		
		this._addTurningPoint(endElement.getLeft() - this.turningMin, startY);
		this._addTurningPoint(endElement.getLeft() - this.turningMin, endY);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
GraphicsEditor.Line.prototype._setTurningPointsNoneToEast = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX >= endElement.getRight() + this.turningMin) {
		
		var x = endX + (startX - endX) / 2;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endY);
	}
	else if(startY > endElement.getTop() - this.turningMin && startY < endElement.getBottom() + this.turningMin) {
		
		this._addTurningPoint(startX, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endElement.getRight() + this.turningMin, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endElement.getRight() + this.turningMin, endY);
	}
	else {
		
		this._addTurningPoint(endElement.getRight() + this.turningMin, startY);
		this._addTurningPoint(endElement.getRight() + this.turningMin, endY);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
GraphicsEditor.Line.prototype._setTurningPointsNoneToNorth = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startY <= endElement.getTop() - this.turningMin) {
		
		var y = startY + (endY - startY) / 2;
		this._addTurningPoint(startX, y);
		this._addTurningPoint(endX, y);
	}
	else if(startX <= endElement.getLeft() - this.turningMin || startX >= endElement.getRight() + this.turningMin) {
		
		this._addTurningPoint(startX, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endX, endElement.getTop() - this.turningMin);
	}
	else {
		
		var x = startX <= endElement.getRight() ? endElement.getLeft() - this.turningMin : endElement.getRight() + this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endElement.getTop() - this.turningMin);
		this._addTurningPoint(endX, endElement.getTop() - this.turningMin);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
GraphicsEditor.Line.prototype._setTurningPointsNoneToSouth = function(endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startY >= endElement.getBottom() + this.turningMin) {
		
		var y = startY + (endY - startY) / 2;
		this._addTurningPoint(startX, y);
		this._addTurningPoint(endX, y);
	}
	else if(startX >= endElement.getRight() + this.turningMin || startX <= endElement.getLeft() - this.turningMin) {
		
		this._addTurningPoint(startX, endElement.getBottom() + this.turningMin);
		this._addTurningPoint(endX, endElement.getBottom() + this.turningMin);
	}
	else {
		
		var x = startX >= endElement.getLeft() ? endElement.getRight() + this.turningMin : endElement.getLeft() - this.turningMin;
		this._addTurningPoint(x, startY);
		this._addTurningPoint(x, endElement.getBottom() + this.turningMin);
		this._addTurningPoint(endX, endElement.getBottom() + this.turningMin);
	}
	if(reverse) {
		this._reverseTurningPoints();
	}
};
GraphicsEditor.Line.prototype._setTurningPointsWestToWest = function(startElement, endElement) {
	if(this.surface.startY < endElement.getTop() || this.surface.startY > endElement.getBottom()) {
		
		var x = Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
		this._addTurningPoint(x, this.surface.startY);
		this._addTurningPoint(x, this.surface.endY);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsWestToEast = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX > endX) {
		
		this._addTurningPoint(startX - (startX - endX) / 2, startY);
		this._addTurningPoint(startX - (startX-endX) / 2, endY);
	}
	else if(startElement.getTop() > endElement.getBottom()) {
		
		var y = startElement.getTop() - (startElement.getTop() - endElement.getBottom()) / 2;
		this._addTurningPoint(startX - this.turningMin, startY);
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, endY);
	}
	else if(startElement.getBottom() < endElement.getTop()) {
		
		var y = startElement.getBottom() + (endElement.getTop() - startElement.getBottom()) / 2;
		this._addTurningPoint(startX - this.turningMin, startY);
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, y);
		this._addTurningPoint(endX + this.turningMin, endY);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsWestToNorth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX > endX && startY < endY) {
		
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getBottom() < endY) {
		
		this._addTurningPoint(startX - this.turningMin, startY);
		var y = endY - (endY - startElement.getBottom()) / 2;
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsWestToSouth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX > endX && startY > endY) {
		
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getTop() > endY) {
		
		this._addTurningPoint(startX - this.turningMin, startY);
		var y = endY + (startElement.getTop() - endY) / 2;
		this._addTurningPoint(startX - this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsEastToEast = function(startElement, endElement) {
	if(this.surface.startY < endElement.getTop() || this.surface.startY > endElement.getBottom()) {
		
		var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
		this._addTurningPoint(x, this.surface.startY);
		this._addTurningPoint(x, this.surface.endY);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsEastToNorth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX < endX && startY < endY) {
		
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getBottom() < endY) {
		
		this._addTurningPoint(startX + this.turningMin, startY);
		var y = endY - (endY - startElement.getBottom()) / 2;
		this._addTurningPoint(startX + this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsEastToSouth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startX < endX && startY > endY) {
		
		this._addTurningPoint(endX, startY);
	}
	else if(startElement.getTop() > endY) {
		
		this._addTurningPoint(startX + this.turningMin, startY);
		var y = endY+(startElement.getTop() - endY) / 2;
		this._addTurningPoint(startX + this.turningMin, y);
		this._addTurningPoint(endX, y);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsNorthToNorth = function(startElement, endElement) {
	if(this.surface.startY < this.surface.endY) {
		this._addTurningPoint(this.surface.startX, this.surface.startY - this.turningMin);
		if(startElement.getRight() < this.surface.endX || startElement.getLeft() > this.surface.endX) {
			
			this._addTurningPoint(this.surface.endX, this.surface.startY - this.turningMin);
		}
		else { 
			
			var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
			this._addTurningPoint(x, this.surface.startY - this.turningMin);
			var y = this.surface.endY - (this.surface.endY - startElement.getBottom()) / 2;
			this._addTurningPoint(x, y);
			this._addTurningPoint(this.surface.endX, y);
		}
	}
	else {
		if(endElement.getRight() < this.surface.startX || endElement.getLeft() > this.surface.startX) {
			
			this._addTurningPoint(this.surface.startX, this.surface.endY-this.turningMin);
		}
		else { 
			
			var y = this.surface.startY - (this.surface.startY - endElement.getBottom()) / 2;
			var x = Math.max(startElement.getRight(), endElement.getRight()) + this.turningMin;
			this._addTurningPoint(this.surface.startX, y);
			this._addTurningPoint(x, y);
			this._addTurningPoint(x, this.surface.endY - this.turningMin);
		}
		this._addTurningPoint(this.surface.endX, this.surface.endY - this.turningMin);
	}
};
GraphicsEditor.Line.prototype._setTurningPointsNorthToSouth = function(startElement, endElement, reverse) {
	var startX = this.surface.startX, startY = this.surface.startY, endX = this.surface.endX, endY = this.surface.endY;
	if(reverse) {
		startX = this.surface.endX;
		startY = this.surface.endY;
		endX = this.surface.startX;
		endY = this.surface.startY;
	}
	if(startY > endY) {
		
		var y = startY + (endY - startY) / 2;
		this._addTurningPoint(startX, y);
		this._addTurningPoint(endX, y);
	}
	else if(startElement.getLeft() > endElement.getRight()) {
		
		var x = startElement.getLeft() - (startElement.getLeft() - endElement.getRight()) / 2;
		this._addTurningPoint(startX, startY - this.turningMin);
		this._addTurningPoint(x, startY - this.turningMin);
		this._addTurningPoint(x, endY + this.turningMin);
		this._addTurningPoint(endX, endY + this.turningMin);
	}
	else if(startElement.getRight() < endElement.getLeft()) {
		
		var x = startElement.getRight() + (endElement.getLeft() - startElement.getRight()) / 2;
		this._addTurningPoint(startX, startY - this.turningMin);
		this._addTurningPoint(x, startY - this.turningMin);
		this._addTurningPoint(x, endY + this.turningMin);
		this._addTurningPoint(endX, endY + this.turningMin);
	}
	else {
		
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
GraphicsEditor.Line.prototype._setTurningPointsSouthToSouth = function(startElement, endElement) {
	if(this.surface.startY > this.surface.endY) {
		this._addTurningPoint(this.surface.startX, this.surface.startY + this.turningMin);
		if(startElement.getRight() < this.surface.endX || startElement.getLeft() > this.surface.endX) {
			
			this._addTurningPoint(this.surface.endX, this.surface.startY + this.turningMin);
		}
		else { 
			
			var x = Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
			this._addTurningPoint(x, this.surface.startY + this.turningMin);
			var y = this.surface.endY + (startElement.getTop() - this.surface.endY) / 2;
			this._addTurningPoint(x, y);
			this._addTurningPoint(this.surface.endX, y);
		}
	}
	else {
		if(endElement.getRight() < this.surface.startX || endElement.getLeft() > this.surface.startX) {
			
			this._addTurningPoint(this.surface.startX, this.surface.endY + this.turningMin);
		}
		else { 
			
			var y = this.surface.startY + (endElement.getTop() - this.surface.startY) / 2;
			var x = Math.min(startElement.getLeft(), endElement.getLeft()) - this.turningMin;
			this._addTurningPoint(this.surface.startX, y);
			this._addTurningPoint(x, y);
			this._addTurningPoint(x, this.surface.endY + this.turningMin);
		}
		this._addTurningPoint(this.surface.endX, this.surface.endY + this.turningMin);
	}
};

GraphicsEditor.Node = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight) {
	if(!graphicsEditor) {
		return;
	}
	this.leftConnectPointNumber = this.leftConnectPointNumber || this.leftConnectPointNumber==0 ? this.leftConnectPointNumber : 1;
	this.rightConnectPointNumber = this.rightConnectPointNumber || this.rightConnectPointNumber==0 ? this.rightConnectPointNumber : 1;
	this.topConnectPointNumber = this.topConnectPointNumber || this.topConnectPointNumber==0 ? this.topConnectPointNumber : 1;
	this.bottomConnectPointNumber = this.bottomConnectPointNumber || this.bottomConnectPointNumber==0 ? this.bottomConnectPointNumber : 1;
	this.exitLines = []; 
	this.enterLines = []; 
	GraphicsEditor.Shape.call(this, graphicsEditor, surface, title);
	this.model = model;
	this.defaultWidth = defaultWidth;
	this.defaultHeight = defaultHeight;
};
GraphicsEditor.Node.prototype = new GraphicsEditor.Shape();
GraphicsEditor.Node.prototype.resetDisplayProperties = function(resetFor) {
	
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
GraphicsEditor.Node.prototype.isMouseOver = function(x, y, offset) {
	return this.getLeft() - offset <= x && this.getRight() + offset >= x && this.getTop() - offset <= y && this.getBottom() + offset >= y;
};
GraphicsEditor.Node.prototype.intersects = function(left, top, right, bottom) {
	return this.getLeft() <= right && this.getRight() >= left && this.getTop() <= bottom && this.getBottom() >= top;
};
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
	if(this.focusIndex==-1) { 
		this.graphicsEditor.offsetElements(offsetX, offsetY, offsetX, offsetY, 'move');
		return {x:offsetX, y:offsetY};
	}
	switch(this.focusPoints[this.focusIndex].resizeMode) {
		case 'n-resize': 
			this.graphicsEditor.offsetElements(0, offsetY, 0, 0, 'n-resize');
			break;
		case 's-resize': 
			this.graphicsEditor.offsetElements(0, 0, 0, offsetY, 's-resize');
			break;
		case 'w-resize': 
			this.graphicsEditor.offsetElements(offsetX, 0, 0, 0, 'w-resize');
			break;
		case 'e-resize': 
			this.graphicsEditor.offsetElements(0, 0, offsetX, 0, 'e-resize');
			break;
		case 'nw-resize': 
			this.graphicsEditor.offsetElements(offsetX, offsetY, 0, 0, 'nw-resize');
			break;
		case 'sw-resize': 
			this.graphicsEditor.offsetElements(offsetX, 0, 0, offsetY, 'sw-resize');
			break;
		case 'ne-resize': 
			this.graphicsEditor.offsetElements(0, offsetY, offsetX, 0, 'ne-resize');
			break;
		case 'se-resize': 
			this.graphicsEditor.offsetElements(0, 0, offsetX, offsetY, 'se-resize');
			break;
	}
	return {x:offsetX, y:offsetY};
};
GraphicsEditor.Node.prototype.offset = function(offsetLeft, offsetTop, offsetRight, offsetBottom, mode) {
	if(this.surface.moveDisable) {
		return;
	}
	this.surface.startX += offsetLeft;
	this.surface.endX += offsetRight;
	this.surface.startY += offsetTop;
	this.surface.endY += offsetBottom;
	this.resetDisplayProperties('offset');
	
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
		return; 
	}
	if(width <= 4 * this.graphicsEditor.gridSize && height / width > 2) { 
	    var heightTotal = 0;
	    var i = 0;
	    for(; i < this.title.length; i++) {
		    size = this.measureText(canvasContext, this.title.substring(i, i+1), this.graphicsEditor.font);
		    if(width < size.width) {
		        return; 
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
	            return; 
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
GraphicsEditor.Node.prototype.getDisplayArea = function() {
	return {left:this.getLeft(), top:this.getTop(), right:this.getRight(), bottom:this.getBottom()};
};
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
GraphicsEditor.Node.prototype.addExitLine = function(line) {
	this.exitLines.push(line);
};
GraphicsEditor.Node.prototype.addEnterLine = function(line) {
	this.enterLines.push(line);
};
GraphicsEditor.Node.prototype.removeExitLine = function(line) {
	ListUtils.removeObject(this.exitLines, line);
};
GraphicsEditor.Node.prototype.removeEnterLine = function(line) {
	ListUtils.removeObject(this.enterLines, line);
};
GraphicsEditor.RectNode = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight, radius) {
	GraphicsEditor.Node.call(this, graphicsEditor, model, surface, title, defaultWidth, defaultHeight);
	this.radius = radius;
};
GraphicsEditor.RectNode.prototype = new GraphicsEditor.Node();
GraphicsEditor.RectNode.prototype.draw = function(canvasContext) {
	this.drawRect(canvasContext, this.surface.startX, this.surface.startY, this.surface.endX, this.surface.endY, this.getLineColor(), this.getFillColor(), 1, this.radius);
};
GraphicsEditor.RoundNode = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight, lineWidth) {
	GraphicsEditor.Node.call(this, graphicsEditor, model, surface, title, defaultWidth, defaultHeight);
	this.lineWidth = lineWidth;
};
GraphicsEditor.RoundNode.prototype = new GraphicsEditor.Node();
GraphicsEditor.RoundNode.prototype.draw = function(canvasContext) {
	canvasContext.beginPath();
	var x = this.getLeft() + this.getWidth() / 2;
	var y = this.getTop() + this.getHeight() / 2;
	var a = this.getWidth() / 2;
	var b = this.getHeight() / 2;
	var k = .5522848, 
	ox = a * k, 
	oy = b * k; 
	
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
GraphicsEditor.RhombusNode = function(graphicsEditor, model, surface, title, defaultWidth, defaultHeight) {
	GraphicsEditor.Node.call(this, graphicsEditor, model, surface, title, defaultWidth, defaultHeight);
};
GraphicsEditor.RhombusNode.prototype = new GraphicsEditor.Node();
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

var USERMODEL_PACKAGE = "com.yuanluesoft.jeaf.base.model.user";
GraphicsEditor.Property = function(name, label, value, onUpdate) {
	this.name = name; 
    this.label = label; 
    this.value = value; 
    this.onUpdate = onUpdate;
};
GraphicsEditor.Property.prototype.write = function(parentElement) {
	var table = DomUtils.getElement(parentElement, 'table', 'propertiesTable');
	if(!table) { 
		table = document.createElement('table');
		table.id = 'propertiesTable';
		table.border = 0;
		table.cellPadding = 0;
		table.cellSpacing = 0;
		table.width = '100%';
		var cell = table.insertRow(-1).insertCell(-1);
		cell.className = 'propertyHeader';
		cell.innerHTML = '\u5C5E\u6027\u540D\u79F0';
		cell.noWrap = true;
		cell = table.rows[0].insertCell(-1);
		cell.className = 'propertyHeader';
		cell.innerHTML = '\u5C5E\u6027\u503C';
		cell.width = '100%';
		parentElement.appendChild(table);
	}
	if(this.label) {
		var row = table.insertRow(-1);
		var cell = row.insertCell(-1);
		cell.className = 'propertyLabel';
		cell.noWrap = true;
		cell.innerHTML = this.label;
		cell = row.insertCell(-1);
		cell.className = 'propertyContent';
		this._writeInputBox(cell);
	}
};
GraphicsEditor.Property.prototype._writeInputBox = function(parentElement) {
	
};
GraphicsEditor.TextProperty = function(name, label, value, readOnly, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.readOnly = readOnly;
};
GraphicsEditor.TextProperty.prototype = new GraphicsEditor.Property();
GraphicsEditor.TextProperty.prototype._writeInputBox = function(parentElement) {
	var input = document.createElement('input');
	input.type = 'text';
	input.readOnly = this.readOnly;
	input.value = this.value ? this.value : '';
	input.className = 'propertyInput';
	parentElement.appendChild(input);
	if(!this.onUpdate) {
		return;
	}
	var property = this;
	input.onchange = function() {
		property.value = this.value;
		property.onUpdate.call(property, property.value);
	};
};
GraphicsEditor.CheckBoxProperty = function(name, label, value, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
};
GraphicsEditor.CheckBoxProperty.prototype = new GraphicsEditor.Property();
GraphicsEditor.CheckBoxProperty.prototype._writeInputBox = function(parentElement) {
	var checkbox = document.createElement('input');
	checkbox.type = 'checkbox';
	checkbox.checked = this.value==true;
	checkbox.className = 'checkbox';
	parentElement.appendChild(checkbox);
	if(!this.onUpdate) {
		return;
	}
	var property = this;
	checkbox.onclick = function() {
		property.value = this.checked;
		property.onUpdate.call(property, property.value);
	};
};
GraphicsEditor.DropdownProperty = function(name, label, value, itemsText, selectOnly, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.selectOnly = selectOnly;
	this.itemsText = itemsText; 
};
GraphicsEditor.DropdownProperty.prototype = new GraphicsEditor.Property();
GraphicsEditor.DropdownProperty.prototype._writeInputBox = function(parentElement) {
	var fieldId = Math.random();
	var field = new DropdownField((this.selectOnly ? '<input id="title_' + fieldId + '" type="text" readonly>' : '') +
					  			  '<input id="value_' + fieldId + '" type="' + (this.selectOnly ? 'hidden' : 'text') + '">',
								  this.itemsText,
								  'value_' + fieldId,
								  (this.selectOnly ? 'title_' : 'value_') + fieldId,
								  'propertyInput',
								  '',
								  '',
								  '',
								  parentElement);
	if(this.value || this.value==0) {
		field.setValue(this.value);
	}
	if(!this.onUpdate) {
		return;
	}
	var property = this;
	field.getField(false).onchange = function() { 
		property.value = this.value;
		property.onUpdate.call(property, property.value);
	};
};
GraphicsEditor.DialogProperty = function(name, label, value, dialogURL, dialogWidth, dialogHeight, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.dialogURL = dialogURL; 
	this.dialogWidth = dialogWidth; 
	this.dialogHeight = dialogHeight; 
};
GraphicsEditor.DialogProperty.prototype = new GraphicsEditor.Property();
GraphicsEditor.DialogProperty.prototype._writeInputBox = function(parentElement) {
	
	var url = this.dialogURL
	if(url.substring(0, 1)=="/") {
		url = RequestUtils.getContextPath() + url;
	}
	new SelectField('<input name="' + this.name + '" type="text">',
					'DialogUtils.openDialog("' + url + '", ' + this.dialogWidth + ', ' + this.dialogHeight + ')',
					'propertyInput',
					'',
					'',
					'',
					parentElement);
	var field = document.getElementsByName(this.name)[0];
	if(this.value || this.value==0) {
		field.value = this.value;
	}
	if(!this.onUpdate) {
		return;
	}
	var property = this;
	field.onchange = function() { 
		property.value = this.value;
		property.onUpdate.call(property, property.value);
	};
};
GraphicsEditor.ListProperty = function(name, label, value, jsonObjectPool, hideDeleteButtons, sourceList, valueTitleProperty, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.jsonObjectPool = jsonObjectPool;
	this.hideDeleteButtons = hideDeleteButtons; 
	this.extendListButtons = []; 
	this.valueTitleProperty = valueTitleProperty ? valueTitleProperty : "name"; 
	if(!sourceList) {
		return;
	}
	
	var property = this;
	this.sourceList = sourceList;
	this.addListButton("\u65B0\u5EFA", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
		property._selectFromSourceList();
	});
};
GraphicsEditor.ListProperty.prototype = new GraphicsEditor.Property();
GraphicsEditor.ListProperty.prototype.onItemDblClick = function(item) {
};
GraphicsEditor.ListProperty.prototype.addListButton = function(name, iconUrl, execute) {
	this.extendListButtons.push({name:name, iconUrl:iconUrl, execute:execute});
};
GraphicsEditor.ListProperty.prototype._selectFromSourceList = function() {
	var itemsText = "";
	for(var i = 0; i < this.sourceList.length; i++) {
		var item = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.sourceList[i].uuid);
		itemsText += (itemsText=="" ? "" : ",") + eval("item.title || item." + this.valueTitleProperty) + "|" + item.uuid;
	}
	var property = this;
	window._addListItem = function(uuids) {
		if(uuids=='') {
			return;
		}
		uuids = uuids.split(',');
		for(var i = 0; i < uuids.length; i++) {
			property.appendItem(null, ListUtils.findObjectByProperty(property.jsonObjectPool, 'uuid', uuids[i]));
		}
	};
	DialogUtils.openListDialog(this.label, null, 560, 360, true, "{value},{title|" + this.label + "|100%}", "_addListItem('{value}')", "", itemsText);
};
GraphicsEditor.ListProperty.prototype.write = function(parentElement) {
	var table = document.createElement('table');
	table.border = 0;
	table.cellPadding = 0;
	table.cellSpacing = 0;
	table.style.cssText = "width:100%; height:100%;";
	table.onselectstart = function() {
		return false;
	};
	parentElement.appendChild(table);
	
	var listProperty = this;
	
	var buttonsDiv;
	if(!this.hideDeleteButtons || this.extendListButtons.length>0) { 
		var buttonBar = table.insertRow(-1).insertCell(-1);
		//buttonBar.className = "tdtitle";
		buttonBar.vAlign = "top";
		buttonBar.style.cssText = "padding:1px; width:20px; background:#e4edff; border-right:1px solid #647EB9;border-top:1px solid white;border-left:1px solid white";
		buttonBar.rowSpan = 2;
		buttonsDiv = document.createElement('div');
		buttonBar.appendChild(buttonsDiv);
		buttonsDiv.style.display = 'none';
		
		for(var i = 0; i < this.extendListButtons.length; i++) {
			this._writeButton(buttonsDiv, this.extendListButtons[i].name, this.extendListButtons[i].iconUrl, this.extendListButtons[i].execute);
		}
		this._writeButton(buttonsDiv, "\u4E0A\u79FB", "/jeaf/graphicseditor/icons/arrow_up.gif", function(buttonElement) {
			listProperty._moveListItem(true);
		});
		this._writeButton(buttonsDiv, "\u4E0B\u79FB", "/jeaf/graphicseditor/icons/arrow_down.gif", function(buttonElement) {
			listProperty._moveListItem(false);
		});
		if(!listProperty.hideDeleteButtons) { 
			this._writeButton(buttonsDiv, "\u5220\u9664", "/jeaf/graphicseditor/icons/delete.gif", function(buttonElement) {
				listProperty._deleteItem();
			});
			this._writeButton(buttonsDiv, "\u5168\u90E8\u5220\u9664", "/jeaf/graphicseditor/icons/deleteall.gif", function(buttonElement) {
				listProperty._deleteAllItems();
			});
		}
	}
	
	
	var titleBar = (table.rows[0] ? table.rows[0] : table.insertRow(-1)).insertCell(-1);
	titleBar.className = "propertyHeader";
	titleBar.innerHTML = this.label;
	
	
	var listArea = table.insertRow(-1).insertCell(-1);
	listArea.vAlign = "top";
	listArea.height = '100%';
	this.itemsScrollView = document.createElement('div');
	this.itemsScrollView.style.cssText = "overflow:auto;width:" + listArea.offsetWidth + "px;height:" + listArea.offsetHeight + "px;";
	if(buttonsDiv) {
		buttonsDiv.style.display = '';
	}
	listArea.appendChild(this.itemsScrollView);
	EventUtils.addEvent(window, 'resize', function() {
		listProperty.itemsScrollView.style.display = 'none';
		if(buttonsDiv) {
			buttonsDiv.style.display = 'none';
		}
		listProperty.itemsScrollView.style.width = listArea.offsetWidth + "px";
		listProperty.itemsScrollView.style.height = listArea.offsetHeight + "px";
		if(buttonsDiv) {
			buttonsDiv.style.display = '';
		}
		listProperty.itemsScrollView.style.display = '';
	});
	
	this.itemsTable = document.createElement('table');
	this.itemsTable.border = 0;
	this.itemsTable.cellPadding = 0;
	this.itemsTable.cellSpacing = 0;
	this.itemsTable.width = '100%';
	this.itemsScrollView.appendChild(this.itemsTable);
	
	this._writeItems();
	this.selectedItemIndex = -1;
};
GraphicsEditor.ListProperty.prototype._writeButton = function(buttonsDiv, name, iconUrl, execute) {
	var button = document.createElement('div');
	button.className = "listButton";
	button.onmouseover = function() {
		button.className = "listButton listButtonOver";
	};
	button.onmouseout = function() {
		button.className = "listButton";
	};
	button.onclick = function() {
		execute.call(button, button);
	};
	button.title = name;
	button.style.backgroundImage = "url(" + RequestUtils.getContextPath() + iconUrl + ")";
	buttonsDiv.appendChild(button);
};
GraphicsEditor.ListProperty.prototype._writeItems = function() {
	var count = this.itemsTable.rows.length;
	var listProperty = this;
	for(var i=0; i < (this.value ? this.value.length : 0); i++) {
		var item = this.itemsTable.insertRow(-1).insertCell(-1);
		item.noWrap = true;
		item.className = "listItem";
		item.onclick = function() {
			listProperty._selectItem(this.parentNode.rowIndex);
		};
		item.ondblclick = function() {
			listProperty.onItemDblClick(ListUtils.findObjectByProperty(listProperty.jsonObjectPool, 'uuid', listProperty.value[this.parentNode.rowIndex].uuid));
		};
		var value = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.value[i].uuid);
		eval("item.innerText = value.title || value." + this.valueTitleProperty + ";");
	}
	for(var i=count-1; i>=0; i--) {
		this.itemsTable.deleteRow(i);
	}
};
GraphicsEditor.ListProperty.prototype._deleteItem = function() {
	if(this.selectedItemIndex==-1) {
		return;
	}
	this.value.splice(this.selectedItemIndex, 1);
	
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); 
	this._selectItem(this.selectedItemIndex);
};
GraphicsEditor.ListProperty.prototype._deleteAllItems = function() {
	this.value = [];
	
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); 
	this._selectItem(-1);
};
GraphicsEditor.ListProperty.prototype._moveListItem = function(up) { 
	if(this.selectedItemIndex==-1) {
		return;
	}
	var newIndex = this.selectedItemIndex + (up ? -1 : 1);
	if(newIndex<0 || newIndex==this.value.length) {
		return;
	}
	var item = this.value[this.selectedItemIndex];
	this.value[this.selectedItemIndex] = this.value[newIndex];
	this.value[newIndex] = item;
	
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); 
	this._selectItem(newIndex);
};
GraphicsEditor.ListProperty.prototype.appendItem = function(className, item) {
	if(className) {
		item = JsonUtils.addJsonObject(this.jsonObjectPool, className, item);
	}
	
	for(var i=0; i < (this.value ? this.value.length : 0); i++) {
		if(JsonUtils.isEqual(ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.value[i].uuid), item)) {
			this._selectItem(i);
			if(className) {
				ListUtils.removeObject(this.jsonObjectPool, item);
			}
			return;
		}
	}
	if(!this.value) {
		this.value = [];
	}
	this.value.push({uuid: item.uuid});
	
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); 
	this._selectItem(this.value.length - 1);
};
GraphicsEditor.ListProperty.prototype._selectItem = function(index) {
	if(this.selectedItemIndex!=-1 && this.itemsTable.rows.length>this.selectedItemIndex) {
		this.itemsTable.rows[this.selectedItemIndex].cells[0].className = 'listItem';
	}
	this.selectedItemIndex = Math.min(this.value.length - 1, index);
	if(this.selectedItemIndex==-1) {
		return;
	}
	var cell = this.itemsTable.rows[this.selectedItemIndex].cells[0];
	cell.className = 'listItem listItemSelected';
	if(this.itemsScrollView.scrollTop > cell.offsetTop) {
		this.itemsScrollView.scrollTop = cell.offsetTop - 3;
	}
	else if(this.itemsScrollView.scrollTop + this.itemsScrollView.clientHeight < cell.offsetTop + cell.offsetHeight) {
		this.itemsScrollView.scrollTop = cell.offsetTop + cell.offsetHeight - this.itemsScrollView.clientHeight + 3;
	}
};
GraphicsEditor.UserListProperty = function(name, label, value, jsonObjectPool, displayOnly, onUpdate) {
	GraphicsEditor.ListProperty.call(this, name, label, value, jsonObjectPool, displayOnly, null, null, onUpdate);
	if(displayOnly) {
		return;	
	}
	var property = this;
	this.extendListButtons = [{name: "\u6DFB\u52A0" + label, iconUrl: "/eai/configure/icons/person.gif", execute: function(buttonElement) {
		property.addUser(buttonElement);
	}}];
};
GraphicsEditor.UserListProperty.prototype = new GraphicsEditor.ListProperty();
GraphicsEditor.UserListProperty.prototype.addUser = function(buttonElement) {
	window.userListProperty = this;
	PopupMenu.popupMenu("\u4E2A\u4EBA\0\u90E8\u95E8\0\u89D2\u8272", function(menuItemId, menuItemTitle) {
		if("\u4E2A\u4EBA"==menuItemId) {
			DialogUtils.selectPerson(640, 400, true, "{id},{name|\u7528\u6237\u5217\u8868|100%}", "window.userListProperty._doAddUser('Person', '{id}', '{name}')");
		}
		else if("\u90E8\u95E8"==menuItemId) {
			DialogUtils.selectOrg(640, 400, true, '{id},{name|\u90E8\u95E8\u5217\u8868|100%}', "window.userListProperty._doAddUser('Department', '{id}', '{name}')");
		}
		else if("\u89D2\u8272"==menuItemId) {
			DialogUtils.selectRole(640, 400, true, '{id},{name|\u89D2\u8272\u5217\u8868|100%}', "window.userListProperty._doAddUser('Role', '{id}', '{name}')");
		}
	}, buttonElement, 100, "topRight");
};
GraphicsEditor.UserListProperty.prototype._doAddUser = function(userType, ids, names) {
	ids = ids.split(",");
	names = names.split(",");
	for(var i=0; i<ids.length; i++) {
		this.appendItem(USERMODEL_PACKAGE + '.' + userType, {id: ids[i], name: names[i]});
	}
};

GraphicsEditor.prototype.validateLines = function(validateDuplex) {
	for(var i = 0; i < this.elements.length; i++) {
		if(!(this.elements[i] instanceof GraphicsEditor.Line)) {
			continue;
		}
		if(!this.elements[i].fromElement || !this.elements[i].toElement) {
			return {error: "\u8FDE\u63A5\u4E0D\u80FD\u4E3A\u7A7A", elements:[this.elements[i]]};
		}
		if(!validateDuplex) {
			continue;
		}
		for(var j = i + 1; j < this.elements.length; j++) {
			if(!(this.elements[j] instanceof GraphicsEditor.Line)) {
				continue;
			}
			if(this.elements[j].fromElement==this.elements[i].fromElement && this.elements[j].toElement==this.elements[i].toElement) {
				return {error: "\u4E0D\u5141\u8BB8\u91CD\u590D\u8FDE\u63A5", elements:[this.elements[i], this.elements[j]]};
			}
		}
	}
};
GraphicsEditor.prototype.validateLoop = function(element) {
	return this._validateLoop(element, [element]);
};
GraphicsEditor.prototype._validateLoop = function(element, validated) {
	for(var i = 0; i < element.exitLines.length; i++) {
		if(!element.exitLines[i].toElement) {
			continue;
		}
		if(ListUtils.indexOf(validated, element.exitLines[i].toElement)!=-1) {
			return {error: "\u4E0D\u5141\u8BB8\u5FAA\u73AF\u5173\u8054", elements: validated};
		}
		var list = [].concat(validated);
		list.push(element.exitLines[i]);
		list.push(element.exitLines[i].toElement);
		var validateError = this._validateLoop(element.exitLines[i].toElement, list); 
		if(validateError) {
			return validateError;
		}
	}
};

