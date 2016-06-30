//类:弹出菜单
PopupMenu = function() {
	
};
//menuDefinition 菜单定义,格式:[{id:ID, title:标题, subItems:[{id:子菜单ID, title:子菜单标题},...]}, ...], 或者：菜单项标题1|菜单项ID1\0菜单项2\0...\0菜单项n
//onClick = function(menuItemId, menuItemTitle);
PopupMenu.popupMenu = function(menuDefinition, onClick, displayArea, menuWidth, align) {
	new PopupMenu().popupMenu(menuDefinition, onClick, displayArea, menuWidth, align);
};
PopupMenu.prototype.popupMenu = function(menuDefinition, onClick, displayArea, menuWidth, align) {
	var popupMenu = this;
	this.topWindow = PageUtils.getTopWindow(); //获取顶层窗口
	this.cover = PageUtils.createCover(this.topWindow, 0, true);
	this.cover.onclick = function() { //事件处理:点击非选择器区域
		popupMenu._hide();
	};
	if(!menuDefinition[0] || !menuDefinition[0].id) {
		var items = menuDefinition.split("\0");
		menuDefinition = new Array();
		for(var i = 0; i < items.length; i++) {
			var itemValues = items[i].split("|");
			menuDefinition[i] = {id:itemValues[itemValues.length-1], title:itemValues[0]};
		}
	}
	this.window = window;
	this.menuDefinition = menuDefinition;
	this.onClick = onClick;
	this.displayArea = displayArea;
	if(!menuWidth || menuWidth<0 || menuWidth==0) {
		menuWidth = this.displayArea.offsetWidth;
	}
	this.menuWidth = !menuWidth || menuWidth<120 ? 120 : menuWidth;
	this.align = (!align ? "auto" : align);
	
	//创建菜单
	this.popupMenuDiv = this._createMenu(false, this.menuDefinition);
	this.popupMenuFrame = this.topWindow.frames["popupMenuFrame"];
	
	//设置显示位置
	var pos = DomUtils.getAbsolutePosition(this.displayArea, null, true);
	if(this.align=="auto") {
		this.align = pos.left > this.topWindow.document.body.offsetWidth/2 ? "right" : "left";
	}
	if(align=="topRight") {
		this.popupMenuDiv.style.left = (pos.left + this.displayArea.offsetWidth) + "px";
		this.popupMenuDiv.style.top = pos.top + "px";
	}
	else {
		this.popupMenuDiv.style.display = "";
		this.popupMenuDiv.style.left = (align=="right" ? pos.left + this.displayArea.offsetWidth - this.popupMenuDiv.offsetWidth : pos.left) + "px";
		this.popupMenuDiv.style.top = (pos.top + this.displayArea.offsetHeight + this.popupMenuDiv.offsetHeight > this.topWindow.document.body.clientHeight && pos.top - this.popupMenuDiv.offsetHeight > 0 ? pos.top - this.popupMenuDiv.offsetHeight : pos.top + this.displayArea.offsetHeight) + "px";
	}
	this.popupMenuDiv.style.visibility = "visible";
};
PopupMenu.prototype._createMenu = function(isSubMenu, menuItems) { //创建菜单
	var popupMenuDiv = this.topWindow.document.getElementById((isSubMenu ? "popupSubMenu" : "popupMenu") + "Div");
	if(!popupMenuDiv) {
		popupMenuDiv = this.topWindow.document.createElement("div");
		popupMenuDiv.style.position = "absolute";
		popupMenuDiv.style.display = "none";
		popupMenuDiv.style.visibility = "hidden";
		popupMenuDiv.id = (isSubMenu ? "popupSubMenu" : "popupMenu") + "Div";
		popupMenuDiv.style.height = "0px";
		this.topWindow.document.body.insertBefore(popupMenuDiv, this.topWindow.document.body.childNodes[0]);
		popupMenuDiv.innerHTML = '<iframe name="' + (isSubMenu ? "popupSubMenu" : "popupMenu") + 'Frame" style="z-index:-1" frameborder="0" scrolling="no" allowTransparency="true"></iframe>';
	}
	popupMenuDiv.style.zIndex = DomUtils.getMaxZIndex(this.topWindow.document.body) + 1;
	//创建菜单项
	var popupMenuFrame = this.topWindow.frames[(isSubMenu ? "popupSubMenu" : "popupMenu") + "Frame"];
	var doc = popupMenuFrame.document;
	doc.open();
	doc.write('<html><body style="margin:0px"></body></html>');
	doc.close();
	//显示菜单DIV
	popupMenuDiv.style.display = "";
	//复制样式
	CssUtils.cloneStyle(this.window.document, doc);
	var table = doc.createElement("table");
	doc.body.appendChild(table);
	table.style.cssText = "outline-style:none; -moz-outline:none;";
	table.className = "menubar";
	table.border = 0;
	table.cellPadding = 0;
	table.cellSpacing = 0;
	table.width = this.menuWidth + "px";
	var popupMenu = this;
	for(var i=0; i<menuItems.length; i++) {
		var td = table.insertRow(-1).insertCell(-1);
		td.className = "menunormal";
		td.id = menuItems[i].id;
		td.innerHTML = menuItems[i].title;
		td.noWrap = true;
		td.onmouseover = function() {
			popupMenu._mouseOverMenuItem(menuItems[this.parentNode.rowIndex], this, isSubMenu);
		};
		td.onmouseout = function() {
			popupMenu._mouseOutMenuItem(menuItems[this.parentNode.rowIndex], this, isSubMenu);
		};
		td.onmousedown = function() {
			popupMenu._clickMenuItem(menuItems[this.parentNode.rowIndex], this, isSubMenu);
		};
	}
	popupMenuDiv.style.width = popupMenuDiv.childNodes[0].style.width = table.offsetWidth + "px";
	popupMenuDiv.style.height = popupMenuDiv.childNodes[0].style.height = table.offsetHeight + "px";
	return popupMenuDiv;
};
PopupMenu.prototype._hide = function() {
	if(this.popupSubMenuDiv) {
		this.popupSubMenuDiv.parentNode.removeChild(this.popupSubMenuDiv);
	}
	this.popupMenuDiv.parentNode.removeChild(this.popupMenuDiv);
	PageUtils.destoryCover(this.topWindow, this.cover);
};
PopupMenu.prototype._hideSubMenu = function() { //隐藏子菜单
	if(!this.popupSubMenuDiv || this.popupSubMenuDiv.style.display=="none") {
		return;
	}
	this.popupSubMenuDiv.style.display = "none";
	this.popupSubMenuDiv.style.visibility = "hidden";
	this.parentMenuItemElement.className = "menunormal";
	this.parentMenuItemElement = null;
};
PopupMenu.prototype._mouseOverMenuItem = function(menuItemDefine, menuItemElement, isSubMenu) {
	menuItemElement.className = "menuover";
	if(!menuItemDefine.subItems || menuItemDefine.subItems.length==0) { //没有子菜单
		if(!isSubMenu) {
			this._hideSubMenu();
		}
	}
	else { //创建子菜单
		this.popupSubMenuDiv = this._createMenu(true, menuItemDefine.subItems);
		var pos = DomUtils.getAbsolutePosition(menuItemElement, null, true);
		this.popupSubMenuDiv.style.top = (pos.top - 1) + "px";
		this.popupSubMenuDiv.style.left = (this.align=="left" ? pos.left + menuItemElement.offsetWidth : pos.left - this.popupSubMenuDiv.offsetWidth) + "px";
		this.popupSubMenuDiv.style.visibility = "visible";
		this.parentMenuItemElement = menuItemElement;
	}
};
PopupMenu.prototype._mouseOutMenuItem = function(menuItemDefine, menuItemElement, isSubMenu) {
	if(this.parentMenuItemElement!=menuItemElement) {
		menuItemElement.className = "menunormal";
	}
};
PopupMenu.prototype._clickMenuItem = function(menuItemDefine, menuItemElement, isSubMenu) {
	if(!menuItemDefine.subItems || menuItemDefine.subItems.length==0) { //没有子菜单
		this._hide();
		this.onClick(menuItemDefine.id, menuItemDefine.title);
	}
};