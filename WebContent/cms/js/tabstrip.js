//类:TAB选项卡
Tabstrip = function(tabstripId, switchMode, timeSwitch, timeInterval, clickOpenMore) {
	this.tabstripId = tabstripId;
	this.switchMode = switchMode;
	this.timeSwitch = timeSwitch;
	this.clickOpenMore = clickOpenMore;
	this.selectedIndex = 0;
	this.buttons = new Array();
	this.stopAutoSwitch = false; //是否停止自动切换
	var tabstrip = this;
	if(timeSwitch) { //定时切换方式
		window.setInterval(function(){tabstrip.autoSwitch();}, timeInterval*1000); //启动切换定时器
	}
};
Tabstrip.prototype.addPage = function(mouseOverStyle, selectedStyle, unselectedStyle) { //添加TAB选项
	var buttonIndex = this.buttons.length;
	var button = new TabstripButton(this, buttonIndex, mouseOverStyle, selectedStyle, unselectedStyle);
	this.buttons[buttonIndex] = button;
	if(buttonIndex==0) {
		button.setStyle(true);
	}
	//定时切换方式,给标签页添加事件处理
	if(this.timeSwitch) {
		var tabstrip = this;
		var tabstripBody = document.getElementById("tabstripBody_" + this.tabstripId + "_" + buttonIndex);
		//添加事件处理
		tabstripBody.onmouseover = function() {
			tabstrip.stopAutoSwitch = true;
		};
		tabstripBody.onmouseout = function() {
			tabstrip.stopAutoSwitch = false;
		};
	}
};
Tabstrip.prototype.setMoreLink = function() { //设置MORE链接
	var tabstrip = this;
	var moreLink = document.getElementById("tabstripMoreLink_" + this.tabstripId);
	//添加事件处理
	moreLink.onmouseover = function() {
		tabstrip.stopAutoSwitch = true;
	};
	moreLink.onmouseout = function() {
		tabstrip.stopAutoSwitch = false;
	};
	this.resetMoreLink(); //设置MORE链接
};
Tabstrip.prototype.autoSwitch = function(tabstrip) { //启动切换定时器
	if(!this.stopAutoSwitch) {
		this.switchPage(this.selectedIndex<this.buttons.length-1 ? this.selectedIndex+1 : 0);
	}
};
Tabstrip.prototype.switchPage = function(pageIndex) { //页面切换
	if(pageIndex==this.selectedIndex) {
		return;
	}
	document.getElementById("tabstripBody_" + this.tabstripId + "_" + this.selectedIndex).style.display = 'none'; //隐藏原来的页面
	this.buttons[this.selectedIndex].setStyle(false); //设置TAB选项的样式	

	this.selectedIndex = pageIndex;
	document.getElementById("tabstripBody_" + this.tabstripId + "_" + this.selectedIndex).style.display = ''; //显示新的页面
	this.buttons[this.selectedIndex].setStyle(true); //设置TAB选项的样式	
	
	this.resetMoreLink(); //设置MORE链接
};
Tabstrip.prototype.resetMoreLink = function() { //重新设置MORE链接
	//设置MORE
	var moreLink = document.getElementById("tabstripMoreLink_" + this.tabstripId);
	if(!moreLink) {
		return;
	}
	var divMore = document.getElementById("tabstripMore_" + this.tabstripId + "_" + this.selectedIndex);
	if(divMore) {
		var link = divMore.getElementsByTagName("A")[0];
		if(link) {
			moreLink.href = link.href;
			moreLink.setAttribute("onclick", link.getAttribute("onclick"));
		}
	}
};
Tabstrip.prototype.openMore = function(buttonIndex) { //打开MORE链接
	var divMore = document.getElementById("tabstripMore_" + this.tabstripId + "_" + this.selectedIndex);
	if(divMore) {
		var link = divMore.getElementsByTagName("A")[0];
		EventUtils.clickElement(link);
	}
}
//类:TAB选项
TabstripButton = function(tabstrip, buttonIndex, mouseOverStyle, selectedStyle, unselectedStyle) {
	this.tabstrip = tabstrip;
	this.buttonIndex = buttonIndex;
	this.buttonElement = document.getElementById("tabstripButton_" + tabstrip.tabstripId + "_" + buttonIndex);
	this.mouseOverStyle = mouseOverStyle;
	this.selectedStyle = selectedStyle;
	this.unselectedStyle = unselectedStyle;
	//添加事件处理:鼠标移动
	this.buttonElement.onmouseover = function() {
		tabstrip.stopAutoSwitch = true;
		if(buttonIndex==tabstrip.selectedIndex) {
			return;
		} 
		if(mouseOverStyle!="" && mouseOverStyle!="null") {
			if(mouseOverStyle.indexOf(':')!=-1) {
				this.style.cssText = mouseOverStyle;
			}
			else {
				this.className = mouseOverStyle;
			}
		}
		if(tabstrip.switchMode=="mouseOver") {
			tabstrip.switchPage(buttonIndex);
		}
	};
	this.buttonElement.onmouseout = function() {
		tabstrip.stopAutoSwitch = false;
		if(buttonIndex!=tabstrip.selectedIndex && mouseOverStyle!="" && mouseOverStyle!="null") {
			if(unselectedStyle.indexOf(':')!=-1) {
				this.style.cssText = unselectedStyle;
			}
			else {
				this.className = unselectedStyle;
			}
		}
	};
	if(tabstrip.switchMode=="click" || tabstrip.clickOpenMore) { //单击切换或者单击打开“更多”
		this.buttonElement.onclick = function() {
			if(tabstrip.switchMode=="click") { //单击切换
				tabstrip.switchPage(buttonIndex);
			}
			else {
				tabstrip.openMore(buttonIndex);
			}
		};
	}
};
TabstripButton.prototype.setStyle = function(selected) {
	var style = (selected ? this.selectedStyle : this.unselectedStyle);
	if(style.indexOf(':')!=-1) {
		this.buttonElement.style.cssText = style;
	}
	else {
		this.buttonElement.className = style;
	}
};