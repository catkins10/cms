EAIEditor = function(canvasParentElement, drawBarContainer, propertyTreeContainer, propertyDetailContainer, eaiJson) {
	this.eaiJson = eaiJson;
	this.jsonObjectPool = eaiJson.objects;
	this.eai = ListUtils.findObjectByProperty(eaiJson.objects, "uuid", eaiJson.uuid);
	this.extend = ListUtils.findObjectByProperty(eaiJson.objects, "uuid", this.eai.extend.uuid);
	var drawBarButtons = [{elementType:'Group', name:'\u5206\u7EC4', iconUrl:'/eai/configure/icons/group.gif'},
						  {elementType:'Application', name:'\u5E94\u7528', iconUrl:'/eai/configure/icons/element.gif'},
						  {elementType:'Link', name:'\u94FE\u63A5', iconUrl:'/eai/configure/icons/link.gif'},
						  {elementType:'Line', name:'\u8FDE\u63A5', iconUrl:'/eai/configure/icons/line.gif'},
						  {elementType:'BrokenLine', name:'\u8FDE\u63A5(\u6298\u7EBF)', iconUrl:'/eai/configure/icons/brokenLine.gif'}];
	GraphicsEditor.call(this, canvasParentElement, drawBarContainer, drawBarButtons, propertyTreeContainer, propertyDetailContainer);
};
EAIEditor.prototype = new GraphicsEditor();
EAIEditor.prototype.init = function() {
	
	this.addElement(new EAIEditor.EAI(this, this.eai, this.getSurface(this.jsonObjectPool, "EAI")));
	
    for(var i = 0; i < (this.eai.groups ? this.eai.groups.length : 0); i++) {
        var group = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.groups[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, group.id);
        this.addElement(new EAIEditor.Group(this, group, surface));
    }
	
    for(var i = 0; i < (this.eai.applications ? this.eai.applications.length : 0); i++) {
        var application = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.applications[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, application.id);
        if(surface) {
	    	this.addElement(new EAIEditor.Application(this, application, surface));
        }
    }
	
	for(var i = 0; i < (this.eai.links ? this.eai.links.length : 0); i++) {
        var link = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.links[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, link.id);
        if(surface) {
	    	this.addElement(new EAIEditor.Link(this, link, surface));
        }
    }
	
    for(var i = 0; i < (this.eai.transitions ? this.eai.transitions.length : 0); i++) {
        var transition = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.transitions[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, transition.id);
        if(surface) {
			this.addElement(new EAIEditor.Transition(this, transition, surface));
        }
    }
};
EAIEditor.prototype.createElement = function(elementType) {
	if('Group'==elementType) { 
		
		if(!this.eai.groups) {
		    this.eai.groups = [];
		}
	    var group = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.eai.server.model.Group', {id: "group-" + (this.extend.nextElementId++), name: "\u5206\u7EC4" + (this.eai.groups.length + 1)});
		this.eai.groups.push({uuid:group.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', group.id);
		this.extend.elements.push({uuid:surface.uuid});
		
		return new EAIEditor.Group(this, group, surface);
	}
	else if('Application'==elementType) { 
		
		var application = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.eai.server.model.Application', {id: "application-" + (this.extend.nextElementId++), title: "\u5E94\u7528" + (this.eai.applications.length + 1)});
		if(!this.eai.applications) {
		    this.eai.applications = [];
		}
		this.eai.applications.push({uuid:application.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', application.id);
		this.extend.elements.push({uuid:surface.uuid});
		
		return new EAIEditor.Application(this, application, surface);
	}
	else if('Link'==elementType) { 
		if(!this.eai.links) {
		    this.eai.links = [];
		}
		var link = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.eai.server.model.Link', {id: "link-" + (this.extend.nextElementId++), title: "\u94FE\u63A5" + (this.eai.links.length + 1)});
		this.eai.links.push({uuid:link.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', link.id);
		this.extend.elements.push({uuid:surface.uuid});
		
		return new EAIEditor.Link(this, link, surface);
	}
	else if('Line'==elementType || 'BrokenLine'==elementType) { 
		
		var transition = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.Transition', {id: "transition-" + (this.extend.nextElementId++)});
		if(!this.eai.transitions) {
		    this.eai.transitions = [];
		}
		this.eai.transitions.push({uuid:transition.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.' + elementType, transition.id);
		this.extend.elements.push({uuid:surface.uuid});
		
		return new EAIEditor.Transition(this, transition, surface);
	}
};
EAIEditor.prototype.removeElement = function(element) {
	if(element instanceof EAIEditor.Group) { 
		ListUtils.removeObject(this.jsonObjectPool, element.group);
		ListUtils.removeObjectByProperty(this.eai.groups, 'uuid', element.group.uuid);
	}
	else if(element instanceof EAIEditor.Transition) { 
	    ListUtils.removeObject(this.jsonObjectPool, element.transition);
		ListUtils.removeObjectByProperty(this.eai.transitions, 'uuid', element.transition.uuid);
	}
	else if((element instanceof EAIEditor.Application) && !element.application.name) { 
		ListUtils.removeObject(this.jsonObjectPool, element.application);
   		ListUtils.removeObjectByProperty(this.eai.applications, 'uuid', element.application.uuid);
   	}
	else if((element instanceof EAIEditor.Link) && !element.link.name) { 
		ListUtils.removeObject(this.jsonObjectPool, element.link);
   		ListUtils.removeObjectByProperty(this.eai.links, 'uuid', element.link.uuid);
	}
	
	ListUtils.removeObject(this.jsonObjectPool, element.surface);
	ListUtils.removeObjectByProperty(this.extend.elements, 'uuid', element.surface.uuid);
};
EAIEditor.prototype.getPropertyTree = function() {
	var tree = {id: 'eai', label: this.eai.name, childNodes: []};
	var surface = this.getSurface(this.jsonObjectPool, "EAI");
	if(surface.readOnly) {
		return tree;
	}
	tree.childNodes.push({id: "manager", label: "\u7BA1\u7406\u5458"}); 
	tree.childNodes.push({id: "visitor", label: "\u8BBF\u95EE\u6743\u9650"}); 
	tree.childNodes.push({id: "modifyHistory", label: "\u914D\u7F6E\u5386\u53F2"}); 
	return tree;
};
EAIEditor.prototype.getPropertyList = function(propertyName) {
	var eaiEditor = this;
	var surface = this.getSurface(this.jsonObjectPool, "EAI");
	if(propertyName=="manager") { 
		return [new GraphicsEditor.UserListProperty(propertyName, "\u7BA1\u7406\u5458", this.eai.managers, this.jsonObjectPool, surface.readOnly || surface.configOnly, function(value) {
			eaiEditor.eai.managers = value;
		})];
	}
	else if(propertyName=="visitor") { 
		return [new GraphicsEditor.UserListProperty(propertyName, "\u8BBF\u95EE\u6743\u9650", this.eai.visitors, this.jsonObjectPool, surface.readOnly || surface.configOnly, function(value) {
			eaiEditor.eai.visitors = value;
		})];
	}
	else if(propertyName=="modifyHistory") { 
       	return [new GraphicsEditor.ListProperty(propertyName, "\u4FEE\u6539\u8BB0\u5F55", this.extend.modifyHistory, this.jsonObjectPool, true)];
	}
	else { 
		var propertyList = [];
		propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", this.eai.name, this.extend.readOnly || this.extend.configOnly, function(value) {
			eaiEditor.eai.name = value;
			eaiEditor.elements[0].setTitle(eaiEditor.eai.name);
		}));
		propertyList.push(new GraphicsEditor.TextProperty("creator", "\u521D\u59CB\u914D\u7F6E", this.extend.creator, true));
		propertyList.push(new GraphicsEditor.TextProperty("createDate", "\u914D\u7F6E\u65F6\u95F4", this.extend.createDate, true));
		propertyList.push(new GraphicsEditor.TextProperty("description", "\u7CFB\u7EDF\u63CF\u8FF0", this.eai.description, this.extend.readOnly || this.extend.configOnly, function(value) {
			eaiEditor.eai.description = value;
		}));
		return propertyList;
	}
};
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
EAIEditor.prototype._validate = function() {
	
	var validateError = this.validateLines(true);
	if(validateError) {
		return validateError;
	}
	
	var eaiElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', 'EAI');
	if(eaiElement.enterLines.length > 0) {
		return {error: "\u4E0D\u5141\u8BB8\u521B\u5EFA\u5230" + this.eai.name + "\u7684\u8FDE\u63A5\uFF0C\u8BF7\u4FEE\u6539\u8FDE\u63A5\u7684\u65B9\u5411", elements: eaiElement.enterLines};
	}
	
	for(var i = 0; i < (this.eai.groups ? this.eai.groups.length : 0); i++) {
		var group = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.groups[i].uuid);
		var groupElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', group.id);
		if(!group.name || group.name=="") {
			return {error: "\u5206\u7EC4\u540D\u79F0\u4E0D\u5141\u8BB8\u4E3A\u7A7A", elements: [groupElement]};	
		}
		if(groupElement.enterLines.length==0) {
			return {error: "\u5206\u7EC4\"" + group.name + "\"\u4E0A\u7EA7\u8FDE\u63A5\u4E0D\u80FD\u4E3A\u7A7A", elements: [groupElement]};			
		}
		if(validateError = this.validateLoop(groupElement)) { 
			return validateError;
		}
	}
	
	for(var i = 0; i < (this.eai.applications ? this.eai.applications.length : 0); i++) {
		var application = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.applications[i].uuid);
		var applicationElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', application.id);
		if(!applicationElement) {
			continue;
		}
		
		if(applicationElement.exitLines.length>0) {
			return {error: "\u4E0D\u5141\u8BB8\u4ECE\u5E94\u7528\u8FDE\u63A5\u5230\u5176\u4ED6\u5143\u7D20", elements: applicationElement.exitLines};
		}
	}
	
	for(var i = 0; i < (this.eai.links ? this.eai.links.length : 0); i++) {
		var link = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.eai.links[i].uuid);
		var linkElement = ListUtils.findObjectByProperty(this.elements, 'surface.id', link.id);
		if(!linkElement) {
			continue;
		}
		
		if(linkElement.exitLines.length>0) {
			return {error: "\u4E0D\u5141\u8BB8\u4ECE\u8FDE\u63A5\u8FDE\u63A5\u5230\u5176\u4ED6\u5143\u7D20", elements: linkElement.exitLines};
		}
	}
};
EAIEditor.EAI = function(eaiEditor, eai, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, eai, surface, eai.name, 15 * eaiEditor.gridSize, 3 * eaiEditor.gridSize, 0);
	this.eaiEditor = eaiEditor;
};
EAIEditor.EAI.prototype = new GraphicsEditor.RectNode();
EAIEditor.EAI.prototype.getPropertyTree = function() {
	return this.eaiEditor.getPropertyTree();
};
EAIEditor.EAI.prototype.getPropertyList = function(propertyName) {
	return this.eaiEditor.getPropertyList(propertyName);
};
EventUtils.addEvent(window, 'load', function() {
	var json;
	eval('json=' + document.getElementsByName('json')[0].value + ';');
	window.eaiEditor = new EAIEditor(document.getElementById('canvasView'), document.getElementById('drawActionBar'), document.getElementById('propertyTree'), document.getElementById('propertyDetail'), json);
});

EAIEditor.Application = function(eaiEditor, application, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, application, surface, application.title, 3 * eaiEditor.gridSize, 15 * eaiEditor.gridSize, 0);
	this.application = application;
	this.eaiEditor = eaiEditor;
	this.jsonObjectPool = eaiEditor.jsonObjectPool;
};
EAIEditor.Application.prototype = new GraphicsEditor.RectNode();
EAIEditor.Application.prototype.getPropertyTree = function() {
	var tree = {id: this.application.id, label: this.application.title==null ? '\u5E94\u7528' : this.application.title, childNodes: []};
	if(this.surface.readOnly) {
		return tree;
	}
	if(!this.surface.configOnly) { 
		tree.childNodes.push({id: "manager", label: "\u7BA1\u7406\u5458"}); 
		tree.childNodes.push({id: "visitor", label: "\u8BBF\u95EE\u6743\u9650"}); 
		
		for(var i = 0; i < (this.application.manageUnits ? this.application.manageUnits.length : 0); i++) {
			var manageUnit = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.application.manageUnits[i].uuid);
			tree.childNodes.push({id: "manageUnit." + manageUnit.uuid, label: manageUnit.name});
		}
	}
	if(!this.application.workflowSupport || (this.surface.configOnly && !this.application.workflows)) {
		return tree;
	}
	
	var workflowsNode = {id: "workflows", label: "\u5DE5\u4F5C\u6D41", childNodes: []};
	tree.childNodes.push(workflowsNode);
	for(var i = 0; i < (this.application.workflows ? this.application.workflows.length : 0); i++) {
		var workflow = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.application.workflows[i].uuid);
		var workflowNode = {id: "workflow." + workflow.id, label: workflow.name, childNodes: []};
		workflowsNode.childNodes.push(workflowNode);
		
		if(!workflow.subFlows) {
			continue;
		}
     	var subFlowsNode = {id: "workflow." + workflow.id + ".subflows",  label: "\u5B50\u6D41\u7A0B", childNodes: []};
     	workflowNode.childNodes.push(subFlowsNode);
		if(this.surface.configOnly) { 
			continue;
		}
		for(var j = 0; j < workflow.subFlows.length; j++) {
			var subFlow = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', workflow.subFlows[j].uuid);
			subFlowsNode.childNodes.push({id: "workflow." + workflow.id + ".subflow." + subFlow.id, label: subFlow.name});
		}
	}
	return tree;
};
EAIEditor.Application.prototype.getPropertyList = function(propertyName) {
	var applicationElement = this;
	var application = this.application;
	if(propertyName=="manager") { 
		return [new GraphicsEditor.UserListProperty(propertyName, "\u7BA1\u7406\u5458", application.managers, this.jsonObjectPool, this.surface.displayOnly, function(value) {
			application.managers = value;
		})];
	}
	else if(propertyName=="visitor") { 
		return [new GraphicsEditor.UserListProperty(propertyName, "\u8BBF\u95EE\u6743\u9650", application.visitors, this.jsonObjectPool, this.surface.displayOnly, function(value) {
			application.visitors = value;
		})];
	}
	else if(propertyName.indexOf("manageUnit.")==0) { 
		var manageUnit = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', propertyName.substring("manageUnit.".length));
		if(manageUnit!=null) {
			return [new GraphicsEditor.UserListProperty(propertyName, manageUnit.name, manageUnit.visitors, this.jsonObjectPool, this.surface.displayOnly, function(value) {
				manageUnit.visitors = value;
			})];
		}
	}
	else if("workflows"==propertyName && !this.surface.displayOnly) { 
		var property = new GraphicsEditor.ListProperty(propertyName, "\u5DE5\u4F5C\u6D41\u5B9A\u4E49\u5217\u8868", application.workflows, this.jsonObjectPool, true);
		property.onItemDblClick = function(item) {
			applicationElement.loadWorkflow(item.id); 
		};
		
		property.addListButton("\u65B0\u5EFA\u5DE5\u4F5C\u6D41", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
			applicationElement.createWorkflow();
		});
		return [property];
	}
	else if(propertyName.indexOf("workflow.")==0) {
	    
	    var beginIndex = "workflow.".length;
	    var endIndex = propertyName.indexOf('.', beginIndex);
	    var workflowId = endIndex==-1 ? propertyName.substring(beginIndex) : propertyName.substring(beginIndex, endIndex);
	    
	    var workflow = ListUtils.findObjectByProperty(this.jsonObjectPool, "id", workflowId);
	    if(endIndex==-1) { 
	    	if(!this.surface.configOnly) {
				var property = new GraphicsEditor.UserListProperty(propertyName, workflow.name + "\u7684\u914D\u7F6E\u6743\u9650", workflow.managers, this.jsonObjectPool, this.surface.displayOnly, function(value) {
					workflow.managers = value;
				});
				
	        	property.addListButton("\u914D\u7F6E\u5DE5\u4F5C\u6D41", "/jeaf/graphicseditor/icons/config.gif", function(buttonElement) {
	        		applicationElement.loadWorkflow(workflow.id);
	        	});
	        	return [property];
	        }
	    }
	    else {
	        beginIndex = endIndex + 1;
	        endIndex = propertyName.indexOf('.', beginIndex);
	        var type = (endIndex==-1 ? propertyName.substring(beginIndex) : propertyName.substring(beginIndex, endIndex));
	        beginIndex = endIndex + 1;
	        if(type=="subflows") { 
	        	var property = new GraphicsEditor.ListProperty(propertyName, "\u5B50\u6D41\u7A0B\u5217\u8868", workflow.subFlows, this.jsonObjectPool, true);
				property.onItemDblClick = function(item) {
					applicationElement.loadSubFlow(workflow.id, item.id);
				};
				return [property];
	        }
	        else if(type=="subflow") { 
	            var subflow = ListUtils.findObjectByProperty(this.jsonObjectPool, "id", propertyName.substring(beginIndex));
	            var property = new GraphicsEditor.UserListProperty(propertyName, subflow.name + "\u7684\u914D\u7F6E\u6743\u9650", subflow.managers, this.jsonObjectPool, this.surface.displayOnly, function(value) {
					subflow.managers = value;
				});
				
	        	property.addListButton("\u914D\u7F6E\u5B50\u6D41\u7A0B", "/jeaf/graphicseditor/icons/config.gif", function(buttonElement) {
	        		applicationElement.loadSubFlow(workflow.id, subflow.id);
	        	});
	        	return [property];
	        }
	    }
	}
	else if(!this.surface.readOnly && !this.surface.configOnly) {
	    
	    var propertyList = [];
	   	propertyList.push(new GraphicsEditor.TextProperty("title", "\u6807\u9898", application.title, this.surface.displayOnly, function(value) {
			application.title = value;
			applicationElement.setTitle(application.title);
		}));
        if(this.surface.deleteDisable || this.surface.displayOnly || application.name!=null) {
        	propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", application.name, true));
        }
        else { 
        	var doGetItemsText = function() {
				var itemsText = '';
				var applications = applicationElement.eaiEditor.eai.applications;
		        for(var i = 0; i < applications.length; i++) {
					if(ListUtils.findObjectByProperty(applicationElement.eaiEditor.elements, "application.uuid", applications[i].uuid)==null) {
						var application = ListUtils.findObjectByProperty(applicationElement.jsonObjectPool, "uuid", applications[i].uuid);
						itemsText += (itemsText=='' ? '' : '\0') + application.title + '|' + application.uuid;
					}
			    }
			    return itemsText;
		    };
		    propertyList.push(new GraphicsEditor.DropdownProperty("name", "\u540D\u79F0", application.name, doGetItemsText, true, function(value) {
				
			    if(application.name) {
			    	return;
			    }
			    var sourceApplication = ListUtils.findObjectByProperty(applicationElement.jsonObjectPool, 'uuid', value);
			    ListUtils.removeObjectByProperty(applicationElement.eaiEditor.eai.applications, 'uuid', sourceApplication.uuid);
			    ListUtils.removeObjectByProperty(applicationElement.jsonObjectPool, 'uuid', application.uuid);
			    var title = application.title;
			    sourceApplication.uuid = application.uuid;
			    sourceApplication.id = application.id;
			    applicationElement.application =  applicationElement.model = sourceApplication;
			    if(!title.indexOf("\u5E94\u7528")==0) {
			        sourceApplication.title = title;
			    }
				applicationElement.setTitle(sourceApplication.title);
				applicationElement.eaiEditor.showPropertyTree();
			}));
        }
       	propertyList.push(new GraphicsEditor.TextProperty("description", "\u63CF\u8FF0", application.description, this.surface.displayOnly, function(value) {
			application.description = value;
		}));
		return propertyList;
	}
	return null;
};
EAIEditor.Application.prototype.createWorkflow = function() { 
	if(!this.eaiEditor.modified || confirm("\u914D\u7F6E\u5DF2\u4FEE\u6539,\u5982\u679C\u7EE7\u7EED,\u6240\u505A\u7684\u914D\u7F6E\u5C06\u4E22\u5931,\u662F\u5426\u7EE7\u7EED?")) {
		window.top.location = RequestUtils.getContextPath() + "/eai/workflowConfig.shtml" +
							  "?act=createWorkflow" +
							  "&applicationSetName=" + this.application.applicationSetName +
							  "&applicationName=" + this.application.name;
	}
};
EAIEditor.Application.prototype.loadWorkflow = function(workflowId) { 
	if(!this.eaiEditor.modified || confirm("\u914D\u7F6E\u5DF2\u4FEE\u6539,\u5982\u679C\u7EE7\u7EED,\u6240\u505A\u7684\u914D\u7F6E\u5C06\u4E22\u5931,\u662F\u5426\u7EE7\u7EED?")) {
		window.top.location = RequestUtils.getContextPath() + "/eai/workflowConfig.shtml" +
							  "?act=editWorkflow" +
							  "&applicationSetName=" + this.application.applicationSetName +
							  "&applicationName=" + this.application.name +
							  "&workflowId=" + workflowId;
	}
};
EAIEditor.Application.prototype.loadSubFlow = function(workflowId, subFlowProcessId) { 
	if(!this.eaiEditor.modified || confirm("\u914D\u7F6E\u5DF2\u4FEE\u6539,\u5982\u679C\u7EE7\u7EED,\u6240\u505A\u7684\u914D\u7F6E\u5C06\u4E22\u5931,\u662F\u5426\u7EE7\u7EED?")) {
		window.top.location = RequestUtils.getContextPath() + "/eai/workflowConfig.shtml" +
							  "?act=editSubFlow" +
							  "&applicationSetName=" + this.application.applicationSetName +
							  "&applicationName=" + this.application.name +
							  "&workflowId=" + workflowId +
							  "&subFlowProcessId=" + subFlowProcessId;
	}
};

EAIEditor.Group = function(eaiEditor, group, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, group, surface, group.name, 15 * eaiEditor.gridSize, 3 * eaiEditor.gridSize, 0);
	this.eaiEditor = eaiEditor;
	this.group = group;
};
EAIEditor.Group.prototype = new GraphicsEditor.RectNode();
EAIEditor.Group.prototype.getPropertyTree = function() {
	return {id: this.group.id, label: this.group.name};
};
EAIEditor.Group.prototype.getPropertyList = function(propertyName) {
	var groupElement = this;
	return [new GraphicsEditor.TextProperty("name", "\u5206\u7EC4\u540D\u79F0", this.group.name, this.surface.readOnly || this.surface.configOnly, function(value) {
		groupElement.group.name = value;
		groupElement.setTitle(groupElement.group.name);
	})];
};

EAIEditor.Link = function(eaiEditor, link, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, link, surface, link.title, 3 * eaiEditor.gridSize, 15 * eaiEditor.gridSize, 0);
	this.eaiEditor = eaiEditor;
	this.link = link;
	this.jsonObjectPool = eaiEditor.jsonObjectPool;
};
EAIEditor.Link.prototype = new GraphicsEditor.RectNode();
EAIEditor.Link.prototype.getPropertyTree = function() {
	var tree = {id: this.link.id, label: this.link.title==null ? '\u94FE\u63A5' : this.link.title, childNodes: []};
	if(this.surface.readOnly) {
		return tree;
	}
	tree.childNodes.push({id: "manager", label: "\u7BA1\u7406\u5458"}); 
	tree.childNodes.push({id: "visitor", label: "\u8BBF\u95EE\u6743\u9650"}); 
	return tree;
};
EAIEditor.Link.prototype.getPropertyList = function(propertyName) {
	var linkElement = this;
	var link = this.link;
	if(propertyName=="manager") { 
		return [new GraphicsEditor.UserListProperty(propertyName, "\u7BA1\u7406\u5458", link.managers, this.jsonObjectPool, this.surface.readOnly || this.surface.configOnly, function(value) {
			link.managers = value;
		})];
	}
	else if(propertyName=="visitor") { 
		return [new GraphicsEditor.UserListProperty(propertyName, "\u8BBF\u95EE\u6743\u9650", link.visitors, this.jsonObjectPool, this.surface.readOnly || this.surface.configOnly, function(value) {
			link.visitors = value;
		})];
	}
	else if(!this.surface.readOnly && !this.surface.configOnly) { 
	    var propertyList = [];
	   	propertyList.push(new GraphicsEditor.TextProperty("title", "\u540D\u79F0", link.title, this.surface.displayOnly, function(value) {
			link.title = value;
			linkElement.setTitle(link.title);
		}));
		var href = link.url ? link.url.replace(/'/g,  '\"') : '';
		if(this.surface.displayOnly) {
			propertyList.push(new GraphicsEditor.TextProperty("href", "\u94FE\u63A5\u5730\u5740", href, true));
		}
		else {
	       	var doGetItemsText = function() {
				var itemsText = '';
				var links = linkElement.eaiEditor.eai.links;
		        for(var i = 0; i < links.length; i++) {
					if(ListUtils.findObjectByProperty(linkElement.eaiEditor.elements, "link.uuid", links[i].uuid)==null) {
						var link = ListUtils.findObjectByProperty(linkElement.jsonObjectPool, "uuid", links[i].uuid);
						itemsText += (itemsText=='' ? '' : '\0') + link.title + '|' + link.uuid;
					}
			    }
			    return itemsText;
		    };
		    propertyList.push(new GraphicsEditor.DropdownProperty("href", "\u94FE\u63A5\u5730\u5740", href, doGetItemsText, false, function(value) {
				
		    	var selectedLink = ListUtils.findObjectByProperty(linkElement.jsonObjectPool, 'uuid', value);
				if(!selectedLink) { 
			    	link.url = value;
			    }
				else if(link.name) { 
			    	link.url = selectedLink.url;
			    }
			    else {
			    	ListUtils.removeObjectByProperty(linkElement.eaiEditor.eai.links, 'uuid', selectedLink.uuid);
			    	ListUtils.removeObjectByProperty(linkElement.jsonObjectPool, 'uuid', link.uuid);
			    	var title = link.title;
				    selectedLink.uuid = link.uuid;
				    selectedLink.id = link.id;
				    linkElement.link = linkElement.model = selectedLink;
				    if(title.indexOf("\u94FE\u63A5")!=0) {
				        selectedLink.title = title;
				    }
					linkElement.setTitle(selectedLink.title);
				}
				linkElement.eaiEditor.showPropertyTree();
			}));
		}
		return propertyList;
	}
};

EAIEditor.Transition = function(eaiEditor, transition, surface) {
	GraphicsEditor.Line.call(this, eaiEditor, transition, surface, false, 0, null);
};
EAIEditor.Transition.prototype = new GraphicsEditor.Line();
EAIEditor.Transition.prototype.getPropertyTree = function() {
	return {id: this.transition.id, label: '\u8FDE\u63A5'};
};
EAIEditor.Transition.prototype.getPropertyList = function(propertyName) {
	
};

