var USERMODEL_PACKAGE = "com.yuanluesoft.jeaf.base.model.user";
//属性,onUpdate = function(value);
GraphicsEditor.Property = function(name, label, value, onUpdate) {
	this.name = name; //属性名称
    this.label = label; //标题
    this.value = value; //值
    this.onUpdate = onUpdate;
};
//输出属性
GraphicsEditor.Property.prototype.write = function(parentElement) {
	var table = DomUtils.getElement(parentElement, 'table', 'propertiesTable');
	if(!table) { //创建表格
		table = document.createElement('table');
		table.id = 'propertiesTable';
		table.border = 0;
		table.cellPadding = 0;
		table.cellSpacing = 0;
		table.width = '100%';
		var cell = table.insertRow(-1).insertCell(-1);
		cell.className = 'propertyHeader';
		cell.innerHTML = '属性名称';
		cell.noWrap = true;
		cell = table.rows[0].insertCell(-1);
		cell.className = 'propertyHeader';
		cell.innerHTML = '属性值';
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
//输出属性输入框
GraphicsEditor.Property.prototype._writeInputBox = function(parentElement) {
	
};

//文本属性
GraphicsEditor.TextProperty = function(name, label, value, readOnly, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.readOnly = readOnly;
};
GraphicsEditor.TextProperty.prototype = new GraphicsEditor.Property();
//输出属性输入框
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

//复选框属性
GraphicsEditor.CheckBoxProperty = function(name, label, value, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
};
GraphicsEditor.CheckBoxProperty.prototype = new GraphicsEditor.Property();
//输出属性输入框
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

//选择属性,itemsText格式:标题1|值1\0...\0标题n|值n
GraphicsEditor.DropdownProperty = function(name, label, value, itemsText, selectOnly, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.selectOnly = selectOnly;
	this.itemsText = itemsText; //选择项列表
};
GraphicsEditor.DropdownProperty.prototype = new GraphicsEditor.Property();
//输出属性输入框
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
	field.getField(false).onchange = function() { //为值字段添加onchange事件处理
		property.value = this.value;
		property.onUpdate.call(property, property.value);
	};
};

//对话框属性
GraphicsEditor.DialogProperty = function(name, label, value, dialogURL, dialogWidth, dialogHeight, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.dialogURL = dialogURL; //对话框URL
	this.dialogWidth = dialogWidth; //对话框宽度
	this.dialogHeight = dialogHeight; //对话框高度
};
GraphicsEditor.DialogProperty.prototype = new GraphicsEditor.Property();
//输出属性输入框
GraphicsEditor.DialogProperty.prototype._writeInputBox = function(parentElement) {
	//SelectField = function(fieldHTML, fieldName, onSelect, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement)
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
	field.onchange = function() { //为值字段添加onchange事件处理
		property.value = this.value;
		property.onUpdate.call(property, property.value);
	};
};

//列表属性,itemsText格式:标题1|值1,...,标题n|值n
GraphicsEditor.ListProperty = function(name, label, value, jsonObjectPool, hideDeleteButtons, sourceList, valueTitleProperty, onUpdate) {
	GraphicsEditor.Property.call(this, name, label, value, onUpdate);
	this.jsonObjectPool = jsonObjectPool;
	this.hideDeleteButtons = hideDeleteButtons; //是否隐藏删除操作,包括:删除/全删
	this.extendListButtons = []; //扩展的列表操作列表,{name:[名称], iconUrl:[图标URL], execute:[function(buttonElement){}]}
	this.valueTitleProperty = valueTitleProperty ? valueTitleProperty : "name"; //列表显示时使用的title属性
	if(!sourceList) {
		return;
	}
	//添加操作
	var property = this;
	this.sourceList = sourceList;
	this.addListButton("新建", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
		property._selectFromSourceList();
	});
};
GraphicsEditor.ListProperty.prototype = new GraphicsEditor.Property();
//事件处理:双击选项
GraphicsEditor.ListProperty.prototype.onItemDblClick = function(item) {

};
//添加列表操作,execute=function(buttonElement);
GraphicsEditor.ListProperty.prototype.addListButton = function(name, iconUrl, execute) {
	this.extendListButtons.push({name:name, iconUrl:iconUrl, execute:execute});
};
//从源列表中选择
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
//输出属性
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
	//创建按钮栏
	var buttonsDiv;
	if(!this.hideDeleteButtons || this.extendListButtons.length>0) { //隐藏默认的按钮,且没有扩展的按钮
		var buttonBar = table.insertRow(-1).insertCell(-1);
		//buttonBar.className = "tdtitle";
		buttonBar.vAlign = "top";
		buttonBar.style.cssText = "padding:1px; width:20px; background:#e4edff; border-right:1px solid #647EB9;border-top:1px solid white;border-left:1px solid white";
		buttonBar.rowSpan = 2;
		buttonsDiv = document.createElement('div');
		buttonBar.appendChild(buttonsDiv);
		buttonsDiv.style.display = 'none';
		//添加扩展的按钮
		for(var i = 0; i < this.extendListButtons.length; i++) {
			this._writeButton(buttonsDiv, this.extendListButtons[i].name, this.extendListButtons[i].iconUrl, this.extendListButtons[i].execute);
		}
		this._writeButton(buttonsDiv, "上移", "/jeaf/graphicseditor/icons/arrow_up.gif", function(buttonElement) {
			listProperty._moveListItem(true);
		});
		this._writeButton(buttonsDiv, "下移", "/jeaf/graphicseditor/icons/arrow_down.gif", function(buttonElement) {
			listProperty._moveListItem(false);
		});
		if(!listProperty.hideDeleteButtons) { //添加删除的按钮
			this._writeButton(buttonsDiv, "删除", "/jeaf/graphicseditor/icons/delete.gif", function(buttonElement) {
				listProperty._deleteItem();
			});
			this._writeButton(buttonsDiv, "全部删除", "/jeaf/graphicseditor/icons/deleteall.gif", function(buttonElement) {
				listProperty._deleteAllItems();
			});
		}
	}
	
	//创建标题栏
	var titleBar = (table.rows[0] ? table.rows[0] : table.insertRow(-1)).insertCell(-1);
	titleBar.className = "propertyHeader";
	titleBar.innerHTML = this.label;
	
	//创建列表区域
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
	//创建列表表格
	this.itemsTable = document.createElement('table');
	this.itemsTable.border = 0;
	this.itemsTable.cellPadding = 0;
	this.itemsTable.cellSpacing = 0;
	this.itemsTable.width = '100%';
	this.itemsScrollView.appendChild(this.itemsTable);
	//输出列表
	this._writeItems();
	this.selectedItemIndex = -1;
};
//添加列表按钮
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
//输出列表条目
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
//删除选中条目
GraphicsEditor.ListProperty.prototype._deleteItem = function() {
	if(this.selectedItemIndex==-1) {
		return;
	}
	this.value.splice(this.selectedItemIndex, 1);
	//更新属性
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); //重新显示列表
	this._selectItem(this.selectedItemIndex);
};
//删除全部
GraphicsEditor.ListProperty.prototype._deleteAllItems = function() {
	this.value = [];
	//更新属性
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); //重新显示列表
	this._selectItem(-1);
};
GraphicsEditor.ListProperty.prototype._moveListItem = function(up) { //上移或下移
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
	//更新属性
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); //重新显示列表
	this._selectItem(newIndex);
};
//添加条目
GraphicsEditor.ListProperty.prototype.appendItem = function(className, item) {
	if(className) {
		item = JsonUtils.addJsonObject(this.jsonObjectPool, className, item);
	}
	//检查是否已经添加过
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
	//更新属性
	var value;
	if(this.onUpdate && (value = this.onUpdate.call(this, this.value))) {
		this.value = value;
	}
	this._writeItems(); //重新显示列表
	this._selectItem(this.value.length - 1);
};
//选中条目
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

//用户列表属性
GraphicsEditor.UserListProperty = function(name, label, value, jsonObjectPool, displayOnly, onUpdate) {
	GraphicsEditor.ListProperty.call(this, name, label, value, jsonObjectPool, displayOnly, null, null, onUpdate);
	if(displayOnly) {
		return;	
	}
	var property = this;
	this.extendListButtons = [{name: "添加" + label, iconUrl: "/eai/configure/icons/person.gif", execute: function(buttonElement) {
		property.addUser(buttonElement);
	}}];
};
GraphicsEditor.UserListProperty.prototype = new GraphicsEditor.ListProperty();
//添加用户
GraphicsEditor.UserListProperty.prototype.addUser = function(buttonElement) {
	window.userListProperty = this;
	PopupMenu.popupMenu("个人\0部门\0角色", function(menuItemId, menuItemTitle) {
		if("个人"==menuItemId) {
			DialogUtils.selectPerson(640, 400, true, "{id},{name|用户列表|100%}", "window.userListProperty._doAddUser('Person', '{id}', '{name}')");
		}
		else if("部门"==menuItemId) {
			DialogUtils.selectOrg(640, 400, true, '{id},{name|部门列表|100%}', "window.userListProperty._doAddUser('Department', '{id}', '{name}')");
		}
		else if("角色"==menuItemId) {
			DialogUtils.selectRole(640, 400, true, '{id},{name|角色列表|100%}', "window.userListProperty._doAddUser('Role', '{id}', '{name}')");
		}
	}, buttonElement, 100, "topRight");
};
//执行添加用户
GraphicsEditor.UserListProperty.prototype._doAddUser = function(userType, ids, names) {
	ids = ids.split(",");
	names = names.split(",");
	for(var i=0; i<ids.length; i++) {
		this.appendItem(USERMODEL_PACKAGE + '.' + userType, {id: ids[i], name: names[i]});
	}
};