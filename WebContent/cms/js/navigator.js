//类:导航栏
Navigator = function(navigatorId, type, linkItemStyle, linkItemSelectedStyle, menuItemStyle, menuItemSelectedStyle, menuItemDropdownStyle, menuItemSelectedDropdownStyle, popupMenuStyle, popupMenuItemStyle, popupMenuItemSelectedStyle) {
	this.element = document.getElementById("navigator_" + navigatorId);
	this.navigatorId = navigatorId;
	this.type = type; //horizontalMenuTop/horizontalMenuBottom/horizontalLineTop/horizontalLineBottom/verticalPopupLeft/verticalPopupRight/verticalDropdown
	this.linkItemStyle = linkItemStyle=='null' ? '' : linkItemStyle; //导航栏链接项目：未选中
    this.linkItemSelectedStyle = linkItemSelectedStyle=='null' ? '' : linkItemSelectedStyle; //导航栏链接项目：选中
	this.menuItemStyle = menuItemStyle=='null' ? '' : menuItemStyle; //导航栏菜单项目：未选中
	this.menuItemSelectedStyle = menuItemSelectedStyle=='null' ? '' : menuItemSelectedStyle; //导航栏菜单项目：选中
	this.menuItemDropdownStyle = menuItemDropdownStyle=='null' ? '' : menuItemDropdownStyle; //导航栏菜单项目：未选中时的下拉按钮
	this.menuItemSelectedDropdownStyle = menuItemSelectedDropdownStyle=='null' ? '' : menuItemSelectedDropdownStyle; //导航栏菜单项目：选中时的下拉按钮
	this.popupMenuStyle = popupMenuStyle=='null' ? '' : popupMenuStyle; //菜单
	this.popupMenuItemStyle = popupMenuItemStyle=='null' ? '' : popupMenuItemStyle; //菜单条目：未选中
	this.popupMenuItemSelectedStyle = popupMenuItemSelectedStyle=='null' ? '' : popupMenuItemSelectedStyle; //菜单条目：选中
	this.items = new Array(); //项目列表
	this.menuElement = null; //菜单元素
	this.menuTimer = null; //菜单计时器
	this.selectedItem = null; //选中的项目
};
Navigator.prototype.addItem = function(menuItems, linkItemStyle, linkItemSelectedStyle, menuItemStyle, menuItemSelectedStyle, menuItemDropdownStyle, menuItemSelectedDropdownStyle) { //添加导航栏项目
	var itemIndex = this.items.length;
	var item = new NavigatorItem(this, itemIndex, menuItems, linkItemStyle, linkItemSelectedStyle, menuItemStyle, menuItemSelectedStyle, menuItemDropdownStyle, menuItemSelectedDropdownStyle);
	this.items[itemIndex] = item;
};
Navigator.prototype.hideAllMenuDiv = function() { //导航栏类型为下拉列表时,隐藏菜单DIV
	//导航栏类型为下拉列表时,隐藏菜单DIV
	if('verticalDropdown'==this.type) {
		for(var i=0; i<this.items.length; i++) {
			var menuDiv = document.getElementById(this.items[i].itemElement.id + '_menu');
			if(menuDiv) {
				menuDiv.style.display = 'none';
			}
		}
	}
};
Navigator.prototype.showMenu = function(navigatorItem) { //显示菜单
	if(this.menuElement==null) {
		//创建菜单
		this.menuElement = document.createElement("div");
		this.menuElement.style.position = "absolute";
		this.menuElement.style.visibility = "hidden";
		this.menuElement.style.zIndex = 1000;
		this.menuElement.innerHTML = '<iframe style="position:absolute; z-index:-1; top:0; left:0; scrolling:no;" frameborder="0" src="about:blank"></iframe><div></div>';
		document.body.insertBefore(this.menuElement, document.body.childNodes[0]);
		this.menuElement.childNodes[1].style.cssText = this.popupMenuStyle; //设置菜单样式
		this.menuTimer = null;
		var navigator = this;
		this.menuElement.onmouseover = function() {
			navigator.hideMenu(true); //停止隐藏菜单
		};
		this.menuElement.onmouseout = function() {
			navigator.hideMenu(false); //隐藏菜单
		};
	}
	
	this.hideMenu(true); //停止隐藏菜单
	this.hideAllMenuDiv(); //导航栏类型为下拉列表时,隐藏菜单DIV
	
	//创建菜单项
	var lineMode = (this.type.indexOf('Line')!=-1); //是否单行模式
	var menuItemsHTML = '<table border="0" cellspacing="0" cellpadding="0">' + (lineMode ? '<tr>' : '');
	for(var i=0; i<navigatorItem.menuItems.length; i++) {
		menuItemsHTML += (!lineMode ? '<tr>' : '') +
						 '<td nowrap="true">' + navigatorItem.menuItems[i] + '</td>' +
						 (!lineMode ? '</tr>' : '');
	}
	menuItemsHTML += (lineMode ? '</tr>' : '') + '</table>';
	this.menuElement.childNodes[1].innerHTML = menuItemsHTML;
	var menuItemsTable = this.menuElement.childNodes[1].childNodes[0];
	//设置菜单项样式、鼠标事件绑定
	for(var i=0; i<menuItemsTable.rows.length; i++) {
		for(var j=0; j<menuItemsTable.rows[i].cells.length; j++) {
			var cell = menuItemsTable.rows[i].cells[j];
			this.setMenuItemStyle(cell, false);
			var navigator = this;
			cell.onmouseover = function() {
				navigator.setMenuItemStyle(this, true);
			};
			cell.onmouseout = function() {
				navigator.setMenuItemStyle(this, false);
			};
			cell.onclick = function(event) {
				if(!event) {
					event = window.event;
				}
				var a = this.getElementsByTagName('a')[0];
				if(event.srcElement!=a) {
					EventUtils.clickElement(a);
				}
			}
		}
	}
	//如果菜单宽度小于导航栏项目宽度,自动修改宽度
	if(menuItemsTable.offsetWidth < navigatorItem.itemElement.offsetWidth) {
		menuItemsTable.style.width = (navigatorItem.itemElement.offsetWidth + ('verticalDropdown'==this.type ? 0 : 10)) + "px";
	}
	//获取菜单对齐方式：水平方向
	var menuHorizontalAlign = "left/left"; //水平方向对齐方式,第一个为子菜单,第二个为导航栏条目,默认左对齐
	if('verticalPopupLeft'==this.type || 'verticalLineLeft'==this.type) {
		menuHorizontalAlign = "left/right"; //菜单栏的左侧和导航栏条目右侧对齐
	}
	else if('verticalPopupRight'==this.type || 'verticalLineRight'==this.type) {
		menuHorizontalAlign = "right/left"; //菜单栏的右侧和导航栏条目左侧对齐
	}
	else if(this.type.indexOf("Right")!=-1) {
		menuHorizontalAlign = "right/right"; //右对齐
	}
	else if(this.type.indexOf("horizontalLine")!=-1) {
		menuHorizontalAlign = "center"; //居中
	}
	//获取菜单对齐方式：垂直方向
	var menuVerticalAlign = "top/bottom"; //垂直方向对齐方式,默认菜单顶端和导航栏条目对齐
	if(this.type.indexOf("Bottom")!=-1) {
		menuVerticalAlign = "bottom/top"; //菜单底部和导航栏条目顶部对齐
	}
	else if(',verticalPopupLeft,verticalPopupRight,verticalLineLeft,verticalLineRight,'.indexOf(',' + this.type + ',')!=-1) {
		menuVerticalAlign = "top/top"; //顶端对齐
	}
	
	//获取导航栏和导航栏条目的位置
	var navigatorPosition = DomUtils.getAbsolutePosition(this.element);
	var itemPosition = DomUtils.getAbsolutePosition(navigatorItem.itemElement);
	navigatorItem.itemElement.style.position = "relative";
	
	//设置IFRAME的尺寸
	var menuWidth = this.menuElement.offsetWidth;
	var menuHeight = this.menuElement.offsetHeight;
	this.menuElement.childNodes[0].style.width = menuWidth + "px";
	this.menuElement.childNodes[0].style.height = menuHeight + "px";

	//获取菜单左侧位置
	var adjust = (document.all ? 0 : -1);
	var menuLeft = itemPosition.left - adjust;
	if(menuHorizontalAlign=="left/right") {
		menuLeft = itemPosition.left + navigatorItem.itemElement.offsetWidth - adjust;
	}
	else if(menuHorizontalAlign=="right/left") {
		menuLeft = itemPosition.left - menuWidth + adjust;
	}
	else if(menuHorizontalAlign=="right/right") {
		menuLeft = itemPosition.left + navigatorItem.itemElement.offsetWidth - menuWidth - adjust;
	}
	else if(menuHorizontalAlign=="center") {
		menuLeft = itemPosition.left + navigatorItem.itemElement.offsetWidth/2 - menuWidth/2;
		if(menuLeft < navigatorPosition.left) {
			menuLeft = navigatorPosition.left;
		}
		else if(menuLeft + menuWidth > navigatorPosition.left + this.element.offsetWidth) {
			menuLeft = navigatorPosition.left + this.element.offsetWidth - menuWidth;
		}
	}
	//获取菜单顶部位置
	adjust = (!this.menuElement.childNodes[1].style.zIndex || !navigatorItem.itemElement.style.zIndex || this.menuElement.childNodes[1].style.zIndex < navigatorItem.itemElement.style.zIndex ? 0 : -1);
	var menuTop = itemPosition.top + navigatorItem.itemElement.offsetHeight - adjust;
	if(menuVerticalAlign=="bottom/top") {
		menuTop = itemPosition.top - menuHeight + adjust;
	}
	else if(menuVerticalAlign=="top/top") {
		menuTop = itemPosition.top - adjust;
	}
	
	//设置坐标
	this.menuElement.style.left = menuLeft + "px";
	this.menuElement.style.top = menuTop + "px";
	
	//导航栏类型为下拉列表时,展开菜单DIV
	if('verticalDropdown'==this.type) {
		var menuDiv = document.getElementById(navigatorItem.itemElement.id + '_menu');
		menuDiv.style.height = menuHeight + "px";
		menuDiv.style.display = 'block';
	}
	
	//设置为可见
	this.menuElement.style.visibility = "visible";
};
Navigator.prototype.hideMenu = function(stop, force) { //隐藏菜单
	if(stop) { //停止隐藏菜单
		if(this.menuTimer!=null) {
			window.clearTimeout(this.menuTimer);
		}
		return;
	}
	var navigator = this;
	var doHideMenu = function() {
		if(navigator.menuElement) {
			navigator.menuElement.style.visibility = "hidden";
		}
		if(navigator.selectedItem!=null && navigator.selectedItem.menuItems!=null) {
			navigator.selectedItem.setNavigatorItemStyle(false);
			navigator.selectedItem = null;
		}
		navigator.hideAllMenuDiv(); //导航栏类型为下拉列表时,隐藏菜单DIV
	};
	this.menuTimer = window.setTimeout(doHideMenu, force ? 1 : 300);
};
Navigator.prototype.setMenuItemStyle = function(menuItem, selected) { //设置菜单项样式
	menuItem.style.cssText = (selected ? this.popupMenuItemSelectedStyle : this.popupMenuItemStyle);
	//设置链接的样式
	var links = menuItem.getElementsByTagName("a");
	for(var i=0; i<links.length; i++) {
		links[i].style.color = menuItem.style.color;
	}
};


//类:导航栏项目
NavigatorItem = function(navigator, itemIndex, menuItems, linkItemStyle, linkItemSelectedStyle, menuItemStyle, menuItemSelectedStyle, menuItemDropdownStyle, menuItemSelectedDropdownStyle) {
	this.itemElement = document.getElementById("navigatorItem_" + navigator.navigatorId + "_" + itemIndex);
	this.navigator = navigator;
	this.itemIndex = itemIndex;
	this.linkItemStyle = linkItemStyle=='null' || linkItemStyle=='' ? navigator.linkItemStyle : linkItemStyle; //导航栏链接项目：未选中
    this.linkItemSelectedStyle = linkItemSelectedStyle=='null' || linkItemSelectedStyle=='' ? navigator.linkItemSelectedStyle : linkItemSelectedStyle; //导航栏链接项目：选中
	this.menuItemStyle = menuItemStyle=='null' || menuItemStyle=='' ? navigator.menuItemStyle : menuItemStyle; //导航栏菜单项目：未选中
	this.menuItemSelectedStyle = menuItemSelectedStyle=='null' || menuItemSelectedStyle=='' ? navigator.menuItemSelectedStyle : menuItemSelectedStyle; //导航栏菜单项目：选中
	this.menuItemDropdownStyle = menuItemDropdownStyle=='null' || menuItemDropdownStyle=='' ? navigator.menuItemDropdownStyle : menuItemDropdownStyle; //导航栏菜单项目：未选中时的下拉按钮
	this.menuItemSelectedDropdownStyle = menuItemSelectedDropdownStyle=='null' || menuItemSelectedDropdownStyle=='' ? navigator.menuItemSelectedDropdownStyle : menuItemSelectedDropdownStyle; //导航栏菜单项目：选中时的下拉按钮
	this.menuItems = menuItems;
	
	//添加事件处理:鼠标移动
	var navigatorItem = this;
	this.itemElement.onmouseover = function() {
		if(navigatorItem.menuItems!=null) {
			navigatorItem.navigator.hideMenu(true); //停止隐藏菜单
		}
		if(navigatorItem.navigator.selectedItem==navigatorItem) { //当前选中的导航栏项目就是本项目
			return;
		}
		if(navigatorItem.navigator.selectedItem!=null) {
			navigatorItem.navigator.selectedItem.setNavigatorItemStyle(false); //修改样式
		}
		navigator.selectedItem = navigatorItem; //设置为当前项目
		navigatorItem.setNavigatorItemStyle(true); //修改样式
		if(navigatorItem.menuItems!=null) {
			navigatorItem.navigator.showMenu(navigatorItem); //显示菜单
		}
		else {
			navigatorItem.navigator.hideMenu(false, true); //隐藏菜单
		}
	};
	this.itemElement.onmouseout = function() {
		if(navigatorItem.menuItems==null) { //没有菜单
			navigatorItem.setNavigatorItemStyle(false); //修改样式
			navigatorItem.navigator.selectedItem = null;
		}
		else {
			navigatorItem.navigator.hideMenu(false); //隐藏菜单
		}
	};
};
NavigatorItem.prototype.setNavigatorItemStyle = function(selected) {
	var style = "position:static; float:" + (this.itemElement.style.styleFloat ? this.itemElement.style.styleFloat : this.itemElement.style.float) + ";";
	if(this.menuItems==null) { //没有菜单
		style += (selected ? this.linkItemSelectedStyle : this.linkItemStyle);
	}
	else {
		style += (selected ? this.menuItemSelectedStyle : this.menuItemStyle);
		//设置下拉按钮样式
		document.getElementById(this.itemElement.id + "_dropdown").style.cssText = "float:left; font-size:1px;" + (selected ? this.menuItemSelectedDropdownStyle : this.menuItemDropdownStyle);
	}
	this.itemElement.style.cssText = style;
	//设置链接的样式
	var links = this.itemElement.getElementsByTagName("a");
	for(var i=0; i<links.length; i++) {
		links[i].style.color = this.itemElement.style.color;
	}
};