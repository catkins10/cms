//EAI编辑器,从GraphicsEditor继承
EAIEditor = function(canvasParentElement, drawBarContainer, propertyTreeContainer, propertyDetailContainer, eaiJson) {
	this.eaiJson = eaiJson;
	this.jsonObjectPool = eaiJson.objects;
	this.eai = ListUtils.findObjectByProperty(eaiJson.objects, "uuid", eaiJson.uuid);
	this.extend = ListUtils.findObjectByProperty(eaiJson.objects, "uuid", this.eai.extend.uuid);
	var drawBarButtons = [{elementType:'Group', name:'分组', iconUrl:'/eai/configure/icons/group.gif'},
						  {elementType:'Application', name:'应用', iconUrl:'/eai/configure/icons/element.gif'},
						  {elementType:'Link', name:'链接', iconUrl:'/eai/configure/icons/link.gif'},
						  {elementType:'Line', name:'连接', iconUrl:'/eai/configure/icons/line.gif'},
						  {elementType:'BrokenLine', name:'连接(折线)', iconUrl:'/eai/configure/icons/brokenLine.gif'}];
	GraphicsEditor.call(this, canvasParentElement, drawBarContainer, drawBarButtons, propertyTreeContainer, propertyDetailContainer);
};
EAIEditor.prototype = new GraphicsEditor();
//抽象方法(由继承者实现):初始化
EAIEditor.prototype.init = function() {
	//加载系统元素
	this.addElement(new EAIEditor.EAI(this, this.eai, this.getSurface(this.jsonObjectPool, "EAI")));
	//加载分组
    for(var i = 0; i < (this.eai.groups ? this.eai.groups.length : 0); i++) {
        var group = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.groups[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, group.id);
        this.addElement(new EAIEditor.Group(this, group, surface));
    }
	//加载应用
    for(var i = 0; i < (this.eai.applications ? this.eai.applications.length : 0); i++) {
        var application = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.applications[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, application.id);
        if(surface) {
	    	this.addElement(new EAIEditor.Application(this, application, surface));
        }
    }
	//加载链接
	for(var i = 0; i < (this.eai.links ? this.eai.links.length : 0); i++) {
        var link = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.links[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, link.id);
        if(surface) {
	    	this.addElement(new EAIEditor.Link(this, link, surface));
        }
    }
	//加载连接
    for(var i = 0; i < (this.eai.transitions ? this.eai.transitions.length : 0); i++) {
        var transition = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.transitions[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, transition.id);
        if(surface) {
			this.addElement(new EAIEditor.Transition(this, transition, surface));
        }
    }
};
//抽象方法(由继承者实现):创建元素
EAIEditor.prototype.createElement = function(elementType) {
	if('Group'==elementType) { //分组
		//创建分组对象
		if(!this.eai.groups) {
		    this.eai.groups = [];
		}
	    var group = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.eai.server.model.Group', {id: "group-" + (this.extend.nextElementId++), name: "分组" + (this.eai.groups.length + 1)});
		this.eai.groups.push({uuid:group.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', group.id);
		this.extend.elements.push({uuid:surface.uuid});
		//创建图形对象
		return new EAIEditor.Group(this, group, surface);
	}
	else if('Application'==elementType) { //应用
		//创建应用对象
		var application = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.eai.server.model.Application', {id: "application-" + (this.extend.nextElementId++), title: "应用" + (this.eai.applications.length + 1)});
		if(!this.eai.applications) {
		    this.eai.applications = [];
		}
		this.eai.applications.push({uuid:application.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', application.id);
		this.extend.elements.push({uuid:surface.uuid});
		//创建图形对象
		return new EAIEditor.Application(this, application, surface);
	}
	else if('Link'==elementType) { //链接
		if(!this.eai.links) {
		    this.eai.links = [];
		}
		var link = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.eai.server.model.Link', {id: "link-" + (this.extend.nextElementId++), title: "链接" + (this.eai.links.length + 1)});
		this.eai.links.push({uuid:link.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', link.id);
		this.extend.elements.push({uuid:surface.uuid});
		//创建图形对象
		return new EAIEditor.Link(this, link, surface);
	}
	else if('Line'==elementType || 'BrokenLine'==elementType) { //连接
		//创建应用对象
		var transition = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.Transition', {id: "transition-" + (this.extend.nextElementId++)});
		if(!this.eai.transitions) {
		    this.eai.transitions = [];
		}
		this.eai.transitions.push({uuid:transition.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.' + elementType, transition.id);
		this.extend.elements.push({uuid:surface.uuid});
		//创建图形对象
		return new EAIEditor.Transition(this, transition, surface);
	}
};
//抽象方法(由继承者实现):删除元素
EAIEditor.prototype.removeElement = function(element) {
	if(element instanceof EAIEditor.Group) { //分组
		ListUtils.removeObject(this.jsonObjectPool, element.group);
		ListUtils.removeObjectByProperty(this.eai.groups, 'uuid', element.group.uuid);
	}
	else if(element instanceof EAIEditor.Transition) { //连接对象
	    ListUtils.removeObject(this.jsonObjectPool, element.transition);
		ListUtils.removeObjectByProperty(this.eai.transitions, 'uuid', element.transition.uuid);
	}
	else if((element instanceof EAIEditor.Application) && !element.application.name) { //应用
		ListUtils.removeObject(this.jsonObjectPool, element.application);
   		ListUtils.removeObjectByProperty(this.eai.applications, 'uuid', element.application.uuid);
   	}
	else if((element instanceof EAIEditor.Link) && !element.link.name) { //链接
		ListUtils.removeObject(this.jsonObjectPool, element.link);
   		ListUtils.removeObjectByProperty(this.eai.links, 'uuid', element.link.uuid);
	}
	//删除外观对象
	ListUtils.removeObject(this.jsonObjectPool, element.surface);
	ListUtils.removeObjectByProperty(this.extend.elements, 'uuid', element.surface.uuid);
};
//抽象方法(由继承者实现):获取属性目录树
EAIEditor.prototype.getPropertyTree = function() {
	var tree = {id: 'eai', label: this.eai.name, childNodes: []};
	var surface = this.getSurface(this.jsonObjectPool, "EAI");
	if(surface.readOnly) {
		return tree;
	}
	tree.childNodes.push({id: "manager", label: "管理员"}); //管理员
	tree.childNodes.push({id: "visitor", label: "访问权限"}); //访问者
	tree.childNodes.push({id: "modifyHistory", label: "配置历史"}); //修改记录
	return tree;
};
//抽象方法(由继承者实现):获取列表
EAIEditor.prototype.getPropertyList = function(propertyName) {
	var eaiEditor = this;
	var surface = this.getSurface(this.jsonObjectPool, "EAI");
	if(propertyName=="manager") { //管理员
		return [new GraphicsEditor.UserListProperty(propertyName, "管理员", this.eai.managers, this.jsonObjectPool, surface.readOnly || surface.configOnly, function(value) {
			eaiEditor.eai.managers = value;
		})];
	}
	else if(propertyName=="visitor") { //访问者
		return [new GraphicsEditor.UserListProperty(propertyName, "访问权限", this.eai.visitors, this.jsonObjectPool, surface.readOnly || surface.configOnly, function(value) {
			eaiEditor.eai.visitors = value;
		})];
	}
	else if(propertyName=="modifyHistory") { //修改记录
       	return [new GraphicsEditor.ListProperty(propertyName, "修改记录", this.extend.modifyHistory, this.jsonObjectPool, true)];
	}
	else { //获取公共属性
		var propertyList = [];
		propertyList.push(new GraphicsEditor.TextProperty("name", "名称", this.eai.name, this.extend.readOnly || this.extend.configOnly, function(value) {
			eaiEditor.eai.name = value;
			eaiEditor.elements[0].setTitle(eaiEditor.eai.name);
		}));
		propertyList.push(new GraphicsEditor.TextProperty("creator", "初始配置", this.extend.creator, true));
		propertyList.push(new GraphicsEditor.TextProperty("createDate", "配置时间", this.extend.createDate, true));
		propertyList.push(new GraphicsEditor.TextProperty("description", "系统描述", this.eai.description, this.extend.readOnly || this.extend.configOnly, function(value) {
			eaiEditor.eai.description = value;
		}));
		return propertyList;
	}
};
//保存
EAIEditor.prototype.save = function() {
	var validateError = this._validate();
	if(validateError) {
		this.selectElements(validateError.elements);
		alert(validateError.error);
		return;
	}
	document.getElementsByName('json')[0].value = JsonUtils.stringify(this.eaiJson);
	FormUtils.doAction('saveConfigure');
};
//校验
EAIEditor.prototype._validate = function() {
	//校验连接
	var validateError = this.validateLines(true);
	if(validateError) {
		return validateError;
	}
	//获取根元素
	var eaiElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', 'EAI');
	if(eaiElement.enterLines.length > 0) {
		return {error: "不允许创建到" + this.eai.name + "的连接，请修改连接的方向", elements: eaiElement.enterLines};
	}
	//校验分组
	for(var i = 0; i < (this.eai.groups ? this.eai.groups.length : 0); i++) {
		var group = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.groups[i].uuid);
		var groupElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', group.id);
		if(!group.name || group.name=="") {
			return {error: "分组名称不允许为空", elements: [groupElement]};	
		}
		if(groupElement.enterLines.length==0) {
			return {error: "分组\"" + group.name + "\"上级连接不能为空", elements: [groupElement]};			
		}
		if(validateError = this.validateLoop(groupElement)) { //循环调用校验
			return validateError;
		}
	}
	//校验应用
	for(var i = 0; i < (this.eai.applications ? this.eai.applications.length : 0); i++) {
		var application = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.applications[i].uuid);
		var applicationElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', application.id);
		if(!applicationElement) {
			continue;
		}
		/*if(applicationElement.enterLines.length==0) {
			return {error: "上级连接不能为空", elements: [applicationElement]};
		}*/
		if(applicationElement.exitLines.length>0) {
			return {error: "不允许从应用连接到其他元素", elements: applicationElement.exitLines};
		}
	}
	//校验链接
	for(var i = 0; i < (this.eai.links ? this.eai.links.length : 0); i++) {
		var link = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.links[i].uuid);
		var linkElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', link.id);
		if(!linkElement) {
			continue;
		}
		/*if(linkElement.enterLines.length==0) {
			return {error: "上级连接不能为空", elements: [linkElement]};
		}*/
		if(linkElement.exitLines.length>0) {
			return {error: "不允许从连接连接到其他元素", elements: linkElement.exitLines};
		}
	}
};

//EAI
EAIEditor.EAI = function(eaiEditor, eai, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, eai, surface, eai.name, 15 * eaiEditor.gridSize, 3 * eaiEditor.gridSize, 0);
	this.eaiEditor = eaiEditor;
};
EAIEditor.EAI.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
EAIEditor.EAI.prototype.getPropertyTree = function() {
	return this.eaiEditor.getPropertyTree();
};
//抽象方法(由继承者实现):获取属性列表
EAIEditor.EAI.prototype.getPropertyList = function(propertyName) {
	return this.eaiEditor.getPropertyList(propertyName);
};
EventUtils.addEvent(window, 'load', function() {
	var json;
	eval('json=' + document.getElementsByName('json')[0].value + ';');
	window.eaiEditor = new EAIEditor(document.getElementById('canvasView'), document.getElementById('drawActionBar'), document.getElementById('propertyTree'), document.getElementById('propertyDetail'), json);
});