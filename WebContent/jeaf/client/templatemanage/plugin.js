//插入客户端操作
ClientCommand = function() {
	EditorMenuCommand.call(this, 'client', '客户端', -1, RequestUtils.getContextPath() + "/jeaf/client/templatemanage/icons/client.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
ClientCommand.prototype = new EditorMenuCommand;
ClientCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ClientCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'device', title:'设备调用', iconIndex:-1},
			{id:'channelBar', title:'设为频道栏', iconIndex:-1},
			{id:'channel', title:'设为频道', iconIndex:-1},
			{id:'showChannelBarButton', title:'设为显示频道栏按钮', iconIndex:-1},
			{id:'columnBar', title:'设为栏目栏', iconIndex:-1},
			{id:'preferenceBar', title:'设为参数设置栏', iconIndex:-1},
			{id:'showColumnBarButton', title:'设为显示栏目栏按钮', iconIndex:-1},
			{id:'channelName', title:'插入频道名称', iconIndex:-1},
			{id:'channelIcon', title:'插入频道图标', iconIndex:-1},
			{id:'columnName', title:'插入栏目名称', iconIndex:-1},
			{id:'columnIcon', title:'插入栏目图标', iconIndex:-1},
			{id:'columnContainer', title:'设为栏目容器', iconIndex:-1},
			{id:'clientToast', title:'设为少量信息提示框', iconIndex:-1},
			{id:'clientDialog', title:'设为对话框', iconIndex:-1},
			{id:'clientDialogTitle', title:'插入对话框标题', iconIndex:-1},
			{id:'clientDialogBody', title:'设为对话框主体', iconIndex:-1},
			{id:'clientDialogOkButton', title:'设为对话框确定按钮', iconIndex:-1},
			{id:'clientDialogCancelButton', title:'设为对话框取消按钮', iconIndex:-1},
			{id:'clientDialogOtherButton', title:'设为对话框“其它”按钮', iconIndex:-1},
			{id:'clientStyleDefine', title:'样式配置', iconIndex:-1}];
};
ClientCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("device"==itemId) { //设备调用
		this.setupDevice();
	}
	else if("channelBar"==itemId) { //设为频道栏
		this.createChannelBar();
	}
	else if("channel"==itemId) { //设为频道
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,div,span");
		if(!obj) {
			alert("频道必须是TD、DIV或者SPAN，请重新选择。");
			return;
		}
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/jeaf/client/templatemanage/channel.shtml", 680, 400, "", HtmlEditor.getDialogArguments());
	}
	else if("columnBar"==itemId) { //设为栏目栏
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,div");
		if(!obj) {
			alert("栏目栏必须是TD或者DIV，请重新选择。");
			return;
		}
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/jeaf/client/templatemanage/columnBar.shtml", 550, 350, "", HtmlEditor.getDialogArguments());
	}
	else if("preferenceBar"==itemId) { //设为参数设置栏
		this.createPreferenceBar();
	}
	else if("showChannelBarButton"==itemId || "showColumnBarButton"==itemId) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "img,button,input,span,div,td");
		if(!obj) {
			alert("位置不正确，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = itemId;
		obj.title = "showColumnBarButton"==itemId ? "显示栏目栏" : "显示频道栏";
		alert("设置完成");
	}
	else if("channelIcon"==itemId || "columnIcon"==itemId) { //插入频道图标,插入栏目图标
		this.insertIcon(itemId);
	}
	else if("channelName"==itemId || "columnName"==itemId) { //插入频道名称,插入栏目名称
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a');
		if(!obj) {
			obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(!obj) {
				alert('请重新选择插入的位置');
				return;
			}
		}
		obj.id = itemId;
		obj.innerHTML = "<" + ("channelName"==itemId ? "频道名称" : "栏目名称") + ">";
	}
	else if("columnContainer"==itemId) { //设为栏目容器
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td");
		if(!obj) {
			alert("栏目容器必须是TD或者DIV，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = "columnContainer";
		obj.title = "栏目容器";
		alert("设置完成");
	}
	else if("clientToast"==itemId) { //设为少量信息提示框
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,span,td");
		if(!obj) {
			alert("少量信息提示框必须是TD、SPAN或者DIV，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = "clientToast";
		obj.title = "少量信息提示框";
		alert("设置完成");
	}
	else if("clientDialogTitle"==itemId) { //插入对话框标题
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a');
		if(!obj) {
			obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(!obj) {
				alert('请重新选择插入的位置');
				return;
			}
		}
		obj.id = "clientDialogTitle";
		obj.innerHTML = "<对话框标题>";
	}
	else if(itemId.indexOf("clientDialog")==0) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,span,td");
		if(!obj) {
			alert(itemId.substring(2) + "必须是TD、SPAN或者DIV，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		if("clientDialog"==itemId) {
			obj.title = "对话框";
		}
		else if("clientDialogBody"==itemId) {
			obj.title = "对话框主体";
		}
		else if("clientDialogOkButton"==itemId) {
			obj.title = "对话框确定按钮";
		}
		else if("clientDialogCancelButton"==itemId) {
			obj.title = "对话框取消按钮";
		}
		else if("clientDialogOtherButton"==itemId) {
			obj.title = "对话框“其它”按钮";
		}
		obj.id = itemId;
		alert("设置完成");
	}
	else if("clientStyleDefine"==itemId) {
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/styleDefine.shtml?templateId=" + document.getElementsByName("id")[0].value, 640, 480, "", HtmlEditor.getDialogArguments());
	}
};
ClientCommand.prototype.setupDevice = function() { //设备调用
	var database = {name:'database', title:'本地数据库', inputMode:'checkbox', label:'使用', value:'true'};
	var databaseApplicationNames = {name:'databaseApplicationNames', title:'使用数据库的应用名称', inputMode:'hidden'};
	var databaseApplicationTitles = {name:'databaseApplicationTitles', title:'使用数据库的应用', inputMode:'select', js:'/eai/js/eai.js', selectOnly:true, execute:'EAISelect(600, 360, true, "databaseApplicationNames{id},databaseApplicationTitles{name|应用名称|100%}", "", ",", "application")'};
	var fileSystem = {name:'fileSystem', title:'文件系统', inputMode:'checkbox', label:'使用', value:'true'};
	var camera = {name:'camera', title:'摄像头', inputMode:'checkbox', label:'使用', value:'true'};
	var deviceMeta = DomUtils.getMeta(HtmlEditor.editorDocument, "deviceMeta", false);
	if(deviceMeta) {
		database.defaultValue = StringUtils.getPropertyValue(deviceMeta.content, "database")=="true";
		databaseApplicationNames.defaultValue = StringUtils.getPropertyValue(deviceMeta.content, "databaseApplicationNames");
		databaseApplicationTitles.defaultValue = StringUtils.utf8Decode(StringUtils.getPropertyValue(deviceMeta.content, "databaseApplicationTitles"));
		fileSystem.defaultValue = StringUtils.getPropertyValue(deviceMeta.content, "fileSystem")=="true";
		camera.defaultValue = StringUtils.getPropertyValue(deviceMeta.content, "camera")=="true";
	}
	DialogUtils.openInputDialog("设备调用", [database, databaseApplicationNames, databaseApplicationTitles, fileSystem, camera], 500, 200, "", function(values) {
		HtmlEditor.editor.saveUndoStep();
		var deviceMeta = DomUtils.getMeta(HtmlEditor.editorDocument, "deviceMeta", true);
		deviceMeta.content = "database=" + (values.database=="true") +
							 "&databaseApplicationNames=" + values.databaseApplicationNames +
							 "&databaseApplicationTitles=" + StringUtils.utf8Encode(values.databaseApplicationTitles) +
					  		 "&fileSystem=" + (values.fileSystem=="true") +
					  		 "&camera=" + (values.camera=="true");
	});
};
ClientCommand.prototype.createChannelBar = function() { //设为频道栏
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,div");
	for(var parentNode = obj; parentNode.tagName!='BODY'; parentNode = parentNode.parentNode) {
		if(parentNode.id=='channelBar') {
			obj = parentNode;
			break;
		}
	}
	if(!obj) {
		alert("频道栏必须是TD或者DIV，请重新选择。");
		return;
	}
	var displayMode = {name:'display', title:'显示方式', defaultValue:'alwaysDisplay', inputMode:'dropdown', selectOnly:true, itemsText:'总是显示|alwaysDisplay\\0从左向右滑动时显示|left2right\\0从右向左滑动时显示|right2left'};
	var currentChannelStyle = {name:'currentChannelStyle', title:'当前频道样式', inputMode:'text'};
	var overflowX = {name:'overflowX', title:'水平滚动条', inputMode:'checkbox', label:'显示', value:'true'};
	var overflowY = {name:'overflowY', title:'垂直滚动条', inputMode:'checkbox', label:'显示', value:'true'};
	var marginTop = {name:'marginTop', title:'顶部边距', inputMode:'text'};
	var marginBottom = {name:'marginBottom', title:'底部边距', inputMode:'text'};
	if(obj.id=="channelBar") {
		var urn = obj.getAttribute("urn");
		displayMode.defaultValue = StringUtils.getPropertyValue(urn, "displayMode");
		overflowX.defaultValue = StringUtils.getPropertyValue(urn, "overflowX");
		overflowY.defaultValue = StringUtils.getPropertyValue(urn, "overflowY");
		currentChannelStyle.defaultValue = StringUtils.getPropertyValue(urn, "currentChannelStyle");
		marginTop.defaultValue = StringUtils.getPropertyValue(urn, "marginTop");
		marginBottom.defaultValue = StringUtils.getPropertyValue(urn, "marginBottom");
	}
	DialogUtils.openInputDialog("设为频道栏", [displayMode, currentChannelStyle, overflowX, overflowY, marginTop, marginBottom], 480, 120, "", function(values) {
		HtmlEditor.editor.saveUndoStep();
		obj.id = "channelBar";
		obj.title = "频道栏";
		obj.setAttribute("urn", "displayMode=" + values.display +
								"&overflowX=" + (values.overflowX=="true") +
								"&overflowY=" + (values.overflowY=="true") +
								"&currentChannelStyle=" + values.currentChannelStyle +
								"&marginTop=" + values.marginTop +
								"&marginBottom=" + values.marginBottom);
		
	});
};
ClientCommand.prototype.createPreferenceBar = function() { //设为参数配置栏
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,div");
	for(var parentNode = obj; parentNode.tagName!='BODY'; parentNode = parentNode.parentNode) {
		if(parentNode.id=='preferenceBar') {
			obj = parentNode;
			break;
		}
	}
	if(!obj) {
		alert("参数栏必须是TD或者DIV，请重新选择。");
		return;
	}
	var displayMode = {name:'display', title:'显示方式', defaultValue:'alwaysDisplay', inputMode:'dropdown', selectOnly:true, itemsText:'总是显示|alwaysDisplay\\0从左向右滑动时显示|left2right\\0从右向左滑动时显示|right2left'};
	var overflowX = {name:'overflowX', title:'水平滚动条', inputMode:'checkbox', label:'显示', value:'true'};
	var overflowY = {name:'overflowY', title:'垂直滚动条', inputMode:'checkbox', label:'显示', value:'true'};
	var marginTop = {name:'marginTop', title:'顶部边距', inputMode:'text'};
	var marginBottom = {name:'marginBottom', title:'底部边距', inputMode:'text'};
	if(obj.id=="preferenceBar") {
		var urn = obj.getAttribute("urn");
		displayMode.defaultValue = StringUtils.getPropertyValue(urn, "displayMode");
		overflowX.defaultValue = StringUtils.getPropertyValue(urn, "overflowX");
		overflowY.defaultValue = StringUtils.getPropertyValue(urn, "overflowY");
		marginTop.defaultValue = StringUtils.getPropertyValue(urn, "marginTop");
		marginBottom.defaultValue = StringUtils.getPropertyValue(urn, "marginBottom");
	}
	DialogUtils.openInputDialog("设为参数设置栏", [displayMode, overflowX, overflowY, marginTop, marginBottom], 480, 120, "", function(values) {
		HtmlEditor.editor.saveUndoStep();
		obj.id = "preferenceBar";
		obj.title = "参数设置栏";
		obj.setAttribute("urn", "displayMode=" + values.display +
								"&overflowX=" + (values.overflowX=="true") +
								"&overflowY=" + (values.overflowY=="true") +
								"&marginTop=" + values.marginTop +
								"&marginBottom=" + values.marginBottom);
	});
};
ClientCommand.prototype.insertIcon = function(type) { //插入图标
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
	var iconWidth = {name:'iconWidth', title:'宽度', inputMode:'text'};
	var iconHeight = {name:'iconHeight', title:'高度', inputMode:'text'};
	if(obj && obj.id && obj.id.indexOf("Icon")!=-1) {
		var urn = obj.getAttribute("urn");
		iconWidth.defaultValue = StringUtils.getPropertyValue(urn, "iconWidth");
		iconHeight.defaultValue = StringUtils.getPropertyValue(urn, "iconHeight");
	}
	DialogUtils.openInputDialog("插入图标", [iconWidth, iconHeight], 360, 120, "", function(values) {
		HtmlEditor.editor.saveUndoStep();
		if(!obj) {
			obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
			if(!obj) {
				alert('请重新选择插入的位置');
				return;
			}
		}
		obj.id = type;
		obj.innerHTML = type=="channelIcon" ? "<频道图标>" : "<栏目图标>";
		obj.setAttribute("urn", "iconWidth=" + values.iconWidth + "&iconHeight=" + values.iconHeight);
	});
};
HtmlEditor.registerCommand(new ClientCommand());

//插入客户端引导页操作
ClientPilotCommand = function() {
	EditorMenuCommand.call(this, 'clientPilotPage', '引导页面', -1, RequestUtils.getContextPath() + "/jeaf/client/templatemanage/icons/client.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
ClientPilotCommand.prototype = new EditorMenuCommand;
ClientPilotCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ClientPilotCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'addPage', title:'添加页面', iconIndex:-1},
			{id:'previousPageButton', title:'设为“前一页”按钮', iconIndex:-1},
			{id:'nextPageButton', title:'设为“后一页”按钮', iconIndex:-1},
			{id:'startToUseButton', title:'设为“开始使用”按钮', iconIndex:-1},
			{id:'pilotScroll', title:(DomUtils.getMeta(HtmlEditor.editorDocument, "pilotVerticalScroll", false) ? "设为垂直滚动" : "设为水平滚动"), iconIndex:-1}];
};
ClientPilotCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("pilotScroll"==itemId) {
		var meta = DomUtils.getMeta(HtmlEditor.editorDocument, "pilotVerticalScroll", false);
		if(meta) {
			meta.parentNode.removeChild(meta);
		}
		else {
			DomUtils.getMeta(HtmlEditor.editorDocument, "pilotVerticalScroll", true).content = "true";
		}
		alert("设置完成");
	}
	else if("addPage"==itemId) {
		var themePageWidth = document.getElementsByName("themePageWidth")[0].value;
		var div = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'div');
		div.id = div.name = "pilotPage";
		div.style.float = "left";
		div.style.marginRight = "10px";
		div.style.marginBottom = "10px";
		div.style.border = "#000080 1px solid";
		div.style.width = themePageWidth + "px";
		div.style.height = (themePageWidth*4/3) + "px";
	}
	else if("previousPageButton"==itemId || "nextPageButton"==itemId || "startToUseButton"==itemId) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "img,a,input,td,div,span");
		if(!obj || obj.id=="pilotPage") {
			alert("按钮位置不正确，请重新选择。");
			return;
		}
		obj.id = itemId;
		if("previousPageButton"==itemId) {
			obj.title = "前一页";
		}
		else if("nextPageButton"==itemId) {
			obj.title = "后一页";
		}
		else if("startToUseButton"==itemId) {
			obj.title = "开始使用";
		}
		alert("设置完成");
	}
};
HtmlEditor.registerCommand(new ClientPilotCommand());

//插入提示页操作
ClientHintCommand = function() {
	EditorMenuCommand.call(this, 'clientHintPage', '提示页面', -1, RequestUtils.getContextPath() + "/jeaf/client/templatemanage/icons/client.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
ClientHintCommand.prototype = new EditorMenuCommand;
ClientHintCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ClientHintCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'closeButton', title:'设为“关闭”按钮', iconIndex:-1}];
};
ClientHintCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("closeButton"==itemId) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "img,a,input,td,div,span");
		if(!obj || obj.id=="hintPage") {
			alert("按钮位置不正确，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = itemId;
		obj.title = "关闭";
		alert("设置完成");
	}
};
HtmlEditor.registerCommand(new ClientHintCommand());

//插入文件浏览页面操作
ClientFileBrowseCommand = function(name, title) {
	EditorMenuCommand.call(this, name, title, -1, RequestUtils.getContextPath() + "/jeaf/client/templatemanage/icons/client.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
ClientFileBrowseCommand.prototype = new EditorMenuCommand;
ClientFileBrowseCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ClientFileBrowseCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	if(this.name=='selectFile') { //选择文件
		return [{id:'setAsTitleBar', title:'设为标题栏', iconIndex:-1},
				{id:'insertCloseButton', title:'插入“关闭”按钮', iconIndex:-1},
				{id:'okButton', title:'设为“确定”按钮', iconIndex:-1},
				{id:'fileListArea', title:'设为文件列表区域', iconIndex:-1},
				{id:'folderName', title:'插入当前目录名称', iconIndex:-1},
				{id:'fileName', title:'插入文件名称', iconIndex:-1},
				{id:'fileSize', title:'插入文件大小', iconIndex:-1},
				{id:'fileModified', title:'插入文件修改时间', iconIndex:-1},
				{id:'fileIcon', title:'插入文件图标', iconIndex:-1},
				{id:'fileSelectBox', title:'插入文件选择框', iconIndex:-1}];
	}
	else { //保存
		return [{id:'setAsTitleBar', title:'设为标题栏', iconIndex:-1},
				{id:'insertCloseButton', title:'插入“关闭”按钮', iconIndex:-1},
				{id:'okButton', title:'设为“确定”按钮', iconIndex:-1},
				{id:'newFolderButton', title:'设为“新建文件夹”按钮', iconIndex:-1},
				{id:'fileListArea', title:'设为文件列表区域', iconIndex:-1},
				{id:'folderName', title:'插入当前目录名称', iconIndex:-1},
				{id:'fileName', title:'插入文件名称', iconIndex:-1},
				{id:'fileSize', title:'插入文件大小', iconIndex:-1},
				{id:'fileModified', title:'插入文件修改时间', iconIndex:-1},
				{id:'fileIcon', title:'插入文件图标', iconIndex:-1},
				{id:'fileNameInput', title:'设为文件名称输入框', iconIndex:-1}];
	}
};
ClientFileBrowseCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("setAsTitleBar"==itemId) {
		ClientPageCommand.setAsTitleBar();
	}
	else if("insertCloseButton"==itemId) {
		ClientPageCommand.insertCloseButton();
	}
	else if("okButton,newFolderButton".indexOf(itemId)!=-1) { //设为“关闭”按钮,设为“确定”按钮,设为“新建文件夹”按钮
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "img,a,input,td,div,span");
		if(!obj) {
			alert("按钮位置不正确，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = itemId;
		if("closeButton"==itemId) {
			obj.title = "关闭";
		}
		else if("okButton"==itemId) {
			obj.title = "确定";
		}
		else if("newFolderButton"==itemId) {
			obj.title = "新建文件夹";
		}
		alert("设置完成");
	}
	else if(",folderName,fileName,fileSize,fileModified,fileIcon,fileSelectBox,".indexOf(',' + itemId + ',')!=-1) { //插入当前目录名称,插入文件名称,插入文件大小,插入文件修改时间,插入文件图标,插入文件选择框
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!obj) {
			obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range) ;
		}
		obj.id = itemId;
		var title;
		if("folderName"==itemId) {
			title = "当前目录名称";
		}
		else if("fileName"==itemId) {
			title = "文件名称";
		}
		else if("fileSize"==itemId) {
			title = "文件大小";
		}
		else if("fileModified"==itemId) {
			title = "文件修改时间";
		}
		else if("fileIcon"==itemId) {
			title = "文件图标";
		}
		else if("fileSelectBox"==itemId) {
			title = "文件选择框";
		}
		obj.innerHTML = "<" + title + ">";
	}
	else if("fileListArea"==itemId) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "div");
		if(!obj) {
			alert("文件列表区域必须是DIV，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = "fileListArea";
		obj.title = "文件列表区域";
		alert("设置完成");
	}
	else if("fileNameInput"==itemId) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "input");
		if(!obj) {
			alert("请选择文本框");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		DialogUtils.openInputDialog("输入提示", [{name:'inputPrompt', title:'输入提示', inputMode:'text', defaultValue:obj.getAttribute("alt")}], 350, 120, "", function(value) {
			if(value=="") {
				obj.removeAttribute("alt");
			}
			else {
				obj.setAttribute("alt", value.inputPrompt);
			}
			DomUtils.setAttribute(obj, "name", "fileName")
		});
	}
};
HtmlEditor.registerCommand(new ClientFileBrowseCommand('selectFile', '选择文件'));
HtmlEditor.registerCommand(new ClientFileBrowseCommand('saveFile', '保存文件'));

//插入客户端页面操作
ClientPageCommand = function() {
	EditorMenuCommand.call(this, 'clientPage', '客户端页面', -1, RequestUtils.getContextPath() + "/jeaf/client/templatemanage/icons/client.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
ClientPageCommand.prototype = new EditorMenuCommand;
ClientPageCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ClientPageCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'setAsTitleBar', title:'设为标题栏', iconIndex:-1},
			{id:'insertCloseButton', title:'插入“关闭”按钮', iconIndex:-1},
			{id:'setAsContentArea', title:'设为内容区域', iconIndex:-1}];
};
ClientPageCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("setAsTitleBar"==itemId) {
		ClientPageCommand.setAsTitleBar();
	}
	else if("insertCloseButton"==itemId) {
		ClientPageCommand.insertCloseButton();
	}
	else if("setAsContentArea"==itemId) {
		ClientPageCommand.setAsContentArea();
	}
};
ClientPageCommand.setAsTitleBar = function() { //设为标题栏
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,span");
	if(!obj) {
		alert("标题栏必须是DIV或者SPAN，请重新选择。");
		return;
	}
	DialogUtils.openInputDialog('设为标题栏', [{name:"className", title:"样式", defaultValue:"default", inputMode:'radio', itemsText:"默认|default\\0 自定义|custom", selectOnly:true, required:true}], 360, 200, '', function(value) {
		HtmlEditor.editor.saveUndoStep();
		obj.id = 'titleBar';
		if(value.className=='default' && obj.className.indexOf('titleBar')==-1) {
			obj.className = (!obj.className || obj.className=='' ? '' : obj.className + ' ') + 'titleBar';
		}
	});
};
ClientPageCommand.insertCloseButton = function() { //插入“关闭”按钮
	HtmlEditor.editor.saveUndoStep();
	var button = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'button');
	button.className = "returnButton";
	button.innerHTML = "&nbsp;";
	button.setAttribute("onclick", "window.closeWindow();");
	button.title = "关闭";
};
ClientPageCommand.setAsContentArea = function(tdEnabled) {
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, tdEnabled ? "td,div" : "div");
	if(!obj) {
		alert("内容区域必须是" + (tdEnabled ? "TD或者" : "") + "DIV，请重新选择。");
		return;
	}
	HtmlEditor.editor.saveUndoStep();
	obj.id = "contentArea";
	obj.title = "内容区域";
	alert("设置完成");
};
if(document.getElementsByName('themeType')[0].value=='3' && document.getElementsByName('applicationName')[0].value!='jeaf/client' && document.getElementsByName('pageName')[0].value.indexOf('Hint')==-1) {
	HtmlEditor.registerCommand(new ClientPageCommand());
}