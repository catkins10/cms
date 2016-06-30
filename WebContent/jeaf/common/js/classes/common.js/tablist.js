TabList = function(tabs) {
	this.tabs = tabs;
	if(!window.tabLists) {
		window.tabLists = [];
	}
	window.tabLists.push(this);
	this.id = 'tabList_' + window.tabLists.length;
	this._writeTabList();
};
TabList.prototype.onTabSelected = function(tabId) { //事件处理:选中TAB,由调用者实现
	
};
TabList.prototype._writeTabList = function() {
	document.write('<div id="' + this.id + '" onselectstart="return false;" style="position:relative;"><span class="tabBarLeft"></span></div>');
	document.write('<div class="tabBarBottomLine"></div>');
	var tabList = this;
	var tabListDiv = document.getElementById(this.id);
	for(var i = 0; i < this.tabs.length; i++) {
		var tab = document.createElement('span');
		tab.id = this.tabs[i].id;
		tab.className = 'tab';
		tab.onclick = function() {
			tabList.selectTab(this.id);
		};
		this.tabs[i].element = tab;
		tab.innerHTML = this.tabs[i].name;
		tabListDiv.appendChild(tab);
		tabListDiv.appendChild(document.createElement('span')).className = 'tabSeparation';
		if(this.tabs[i].selected) {
			this.selectTab(this.tabs[i].id);
		}
	}
	window.setTimeout(function() {
		tabList.onTabSelected.call(null, tabList.getSelectedTabId());
	}, 300);
};
TabList.prototype.selectTab = function(tabId) { //选中TAB
	for(var i = 0; i < this.tabs.length; i++) {
		if(this.tabs[i].id==tabId) {
			this.tabs[i].selected = true;
		}
		else if(this.tabs[i].selected) {
			this.tabs[i].selected = false;
		}
		else {
			continue;
		}
		this.tabs[i].element.className = this.tabs[i].selected ? "tab tabSelected" : "tab";
		if(this.tabs[i].selected) {
//alert(CssUtils.getElementComputedStyle(this.tabs[i].element, 'border-bottom-color'));
		}
		var tabContent = document.getElementById("tab" + this.tabs[i].id);
		if(tabContent) {
			tabContent.style.display = this.tabs[i].selected ? "" : "none";
		}
		if(this.tabs[i].selected) {
			this.onTabSelected.call(null, this.tabs[i].id);
		}
	}
};
TabList.prototype.getSelectedTabId = function() { //获取选中TAB的ID
	var tab = ListUtils.findObjectByProperty(this.tabs, 'selected', true);
	return tab ? tab.id : null;
};