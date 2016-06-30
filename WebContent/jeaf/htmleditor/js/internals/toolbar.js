//工具栏命令状态
HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED = -1; //禁用状态
HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL = 0; //一般状态
HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE = 1; //选中状态

HtmlEditor.prototype.createToolbar = function(toolbarContainer) { //创建工具栏
	var toolbarSet = document.createElement("div");
	toolbarSet.className = "toolbarSet";
	toolbarSet.onselectstart = function() {
		return false;
	};
	toolbarContainer.appendChild(toolbarSet);
	this.toolbarButtons = [];
	var toolbarGroups = this.commands.split(';');
	for(var i=0; i<toolbarGroups.length; i++) {
		var toolbarGroup = document.createElement("div");
		toolbarSet.appendChild(toolbarGroup);
		var toolbars = toolbarGroups[i].split(/\|/);
		for(var j=0; j<toolbars.length; j++) {
			var toolbar = document.createElement("span");
			toolbar.className = "toolbar";
			var separatorLine = document.createElement("span");
			separatorLine.style.float = "left";
			separatorLine.className = 'toolbarSeparatorLine';
			toolbar.appendChild(separatorLine);
			toolbarGroup.appendChild(toolbar);
			
			var buttons = toolbars[j].split(',');
			var previousButtonName = '-';
			for(var k=0; k<buttons.length; k++) {
				if(buttons[k]=='-' && previousButtonName=='-') { //分隔符且前一个按钮也是分隔符
					continue;
				}
				var toolbarButton = this.createToolbarButton(toolbar, buttons[k]); //创建工具栏命令
				if(toolbarButton) {
					this.toolbarButtons.push(toolbarButton);
				}
				if(buttons[k]=='-' || toolbarButton) {
					previousButtonName = buttons[k];
				}
			}
			if(previousButtonName=='-') {
				if(toolbar.childNodes.length<=1) {
					toolbar.parentNode.removeChild(toolbar);
				}
				else {
					toolbar.removeChild(toolbar.childNodes[toolbar.childNodes.length-1]);
				}
			}
			if(toolbar.childNodes.length==0) {
				toolbarGroup.removeChild(toolbar);
			}
		}
		if(toolbarGroup.childNodes.length==0) {
			toolbarSet.removeChild(toolbarGroup);
		}
	}
};
HtmlEditor.prototype.createToolbarButton = function(toolbarElement, buttonName) { //创建工具栏命令
	if(buttonName=='-') { //分隔符
		var separator = document.createElement("span");
		separator.className = "toolbarButtonSeparatorBar";
		toolbarElement.appendChild(separator);
		var separatorLine = document.createElement("span");
		separatorLine.style.float = "left";
		separatorLine.className = 'toolbarButtonSeparatorLine';
		separator.appendChild(separatorLine);
		return;
	}
	var command = this.getCommand(buttonName);
	if(!command) {
		return null;
	}
	var editor = this;
	var toolbarButton = document.createElement("span");
	toolbarButton.title = command.title;
	toolbarButton.style.display = 'inline-block';
	toolbarElement.appendChild(toolbarButton);
	if(command.create) {
		command.create(toolbarButton);
		return toolbarButton;
	}
	toolbarButton.command = command;
	toolbarButton.className = "toolbarButton";
	toolbarButton.onmouseover = function() { //鼠标经过
		if(this.className.indexOf('toolbarButtonDisabled')==-1) {
			this.className += ' toolbarButtonOver';
		}
	};
	toolbarButton.onmouseout = function() { //鼠标移出
		if(this.className.indexOf('toolbarButtonDisabled')==-1) {
			this.className = this.className.replace(' toolbarButtonOver', '');
		}
	};
	toolbarButton.onmousedown = function() { //单击
		if(this.className.indexOf('toolbarButtonDisabled')!=-1) {
			return;
		}
		var button = this;
		var range = editor.getRange();
		var selectedElement = DomSelection.getSelectedElement(range);
		HtmlEditor.editor = editor;
		HtmlEditor.editorWindow = editor.editorWindow;
		HtmlEditor.editorDocument = editor.editorDocument;
		HtmlEditor.range = range;
		HtmlEditor.selectedElement = selectedElement;
		editor.editorWindow.setTimeout(function() {
			try {
				DomSelection.selectRange(editor.editorWindow, range);
			}
			catch(e) {
			
			}
			command.execute(button, editor, editor.editorWindow, editor.editorDocument, range, selectedElement);
			editor.refreshToolbarButtonState(); //更新工具栏命令状态
		}, 10);
	};
	//插入图标
	if(command.iconUrl || command.iconIndex>=0) {
		var icon = command.iconUrl;
		if(!icon) {
			icon = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/icons.gif,0,' + (16 * command.iconIndex);
		}
		var values = icon.split(",");
		var img = document.createElement("img");
		img.border = 0;
		img.src = values[0];
		if(values.length>1) {
			img.style.marginLeft = "-" + values[1] + "px";
			img.style.marginTop = "-" + values[2] + "px";
		}
		var iconSpan = document.createElement("span");
		iconSpan.className = "toolbarButtonIcon";
		iconSpan.style.overflow = "hidden";
		iconSpan.appendChild(img);
		toolbarButton.appendChild(iconSpan);
	}
	//插入标题
	if(command.showTitle) { //显示标题
		var titleSpan = document.createElement("span");
		titleSpan.innerHTML = command.title;
		titleSpan.className = 'toolbarButtonTitle';
		toolbarButton.appendChild(titleSpan);
	}
	//插入下拉按钮
	if(command.showDropButton) { //显示下拉按钮
		var img = document.createElement("img");
		img.border = 0;
		img.src = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/dropdown.gif';
		var dropButton = document.createElement("span");
		dropButton.className = "toolbarButtonDropButton";
		dropButton.appendChild(img);
		toolbarButton.appendChild(dropButton);
		
	}
	return toolbarButton;
};
HtmlEditor.prototype.refreshToolbarButtonState = function() { //更新工具栏命令状态
	var editor = this;
	window.setTimeout(function() {
		editor._doRefreshToolbarButtonState();
	}, 100);
}
HtmlEditor.prototype._doRefreshToolbarButtonState = function() { //更新工具栏命令状态
	var range = this.getRange();
	var selectedElement = DomSelection.getSelectedElement(range);
	for(var i=0; i<this.toolbarButtons.length; i++) {
		var state = HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
		if(!this.toolbarButtons[i].command) {
			continue;
		}
		if(!this.sourceCodeMode || this.toolbarButtons[i].command.enabledInSourceCodeMode) { //不是源码模式, 或者在源码模式下有效
			try {
				state = this.toolbarButtons[i].command.getState(this, this.sourceCodeMode, this.editorWindow, this.editorDocument, range, selectedElement);
			}
			catch(e) {
				
			}
		}
		var className;
		if(state==HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED) { //禁用状态
			className = "toolbarButton toolbarButtonDisabled";
		}
		else if(state==HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL) { //一般状态
			className = "toolbarButton";
		}
		else if(state==HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE) { //选中状态
			className = "toolbarButton toolbarButtonActive";
		}
		if(this.toolbarButtons[i].className!=className) {
			this.toolbarButtons[i].className = className;
		}
	}
};