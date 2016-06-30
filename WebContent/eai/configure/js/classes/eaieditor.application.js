//EAI应用
EAIEditor.Application = function(eaiEditor, application, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, application, surface, application.title, 3 * eaiEditor.gridSize, 15 * eaiEditor.gridSize, 0);
	this.application = application;
	this.eaiEditor = eaiEditor;
	this.jsonObjectPool = eaiEditor.jsonObjectPool;
};
EAIEditor.Application.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
EAIEditor.Application.prototype.getPropertyTree = function() {
	var tree = {id: this.application.id, label: this.application.title==null ? '应用' : this.application.title, childNodes: []};
	if(this.surface.readOnly) {
		return tree;
	}
	if(!this.surface.configOnly) { //不是仅配置
		tree.childNodes.push({id: "manager", label: "管理员"}); //管理员
		tree.childNodes.push({id: "visitor", label: "访问权限"}); //访问者
		//管理单元
		for(var i = 0; i < (this.application.manageUnits ? this.application.manageUnits.length : 0); i++) {
			var manageUnit = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.application.manageUnits[i].uuid);
			tree.childNodes.push({id: "manageUnit." + manageUnit.uuid, label: manageUnit.name});
		}
	}
	if(!this.application.workflowSupport || (this.surface.configOnly && !this.application.workflows)) {
		return tree;
	}
	//流程配置
	var workflowsNode = {id: "workflows", label: "工作流", childNodes: []};
	tree.childNodes.push(workflowsNode);
	for(var i = 0; i < (this.application.workflows ? this.application.workflows.length : 0); i++) {
		var workflow = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.application.workflows[i].uuid);
		var workflowNode = {id: "workflow." + workflow.id, label: workflow.name, childNodes: []};
		workflowsNode.childNodes.push(workflowNode);
		//子流程列表
		if(!workflow.subFlows) {
			continue;
		}
     	var subFlowsNode = {id: "workflow." + workflow.id + ".subflows",  label: "子流程", childNodes: []};
     	workflowNode.childNodes.push(subFlowsNode);
		if(this.surface.configOnly) { //只配置
			continue;
		}
		for(var j = 0; j < workflow.subFlows.length; j++) {
			var subFlow = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', workflow.subFlows[j].uuid);
			subFlowsNode.childNodes.push({id: "workflow." + workflow.id + ".subflow." + subFlow.id, label: subFlow.name});
		}
	}
	return tree;
};
//抽象方法(由继承者实现):获取属性列表
EAIEditor.Application.prototype.getPropertyList = function(propertyName) {
	var applicationElement = this;
	var application = this.application;
	if(propertyName=="manager") { //管理员
		return [new GraphicsEditor.UserListProperty(propertyName, "管理员", application.managers, this.jsonObjectPool, this.surface.displayOnly, function(value) {
			application.managers = value;
		})];
	}
	else if(propertyName=="visitor") { //访问者
		return [new GraphicsEditor.UserListProperty(propertyName, "访问权限", application.visitors, this.jsonObjectPool, this.surface.displayOnly, function(value) {
			application.visitors = value;
		})];
	}
	else if(propertyName.indexOf("manageUnit.")==0) { //管理单元
		var manageUnit = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', propertyName.substring("manageUnit.".length));
		if(manageUnit!=null) {
			return [new GraphicsEditor.UserListProperty(propertyName, manageUnit.name, manageUnit.visitors, this.jsonObjectPool, this.surface.displayOnly, function(value) {
				manageUnit.visitors = value;
			})];
		}
	}
	else if("workflows"==propertyName && !this.surface.displayOnly) { //工作流列表
		var property = new GraphicsEditor.ListProperty(propertyName, "工作流定义列表", application.workflows, this.jsonObjectPool, true);
		property.onItemDblClick = function(item) {
			applicationElement.loadWorkflow(item.id); //设置双击时的动作
		};
		//添加流程配置按钮
		property.addListButton("新建工作流", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
			applicationElement.createWorkflow();
		});
		return [property];
	}
	else if(propertyName.indexOf("workflow.")==0) {
	    //获取工作流定义ID
	    var beginIndex = "workflow.".length;
	    var endIndex = propertyName.indexOf('.', beginIndex);
	    var workflowId = endIndex==-1 ? propertyName.substring(beginIndex) : propertyName.substring(beginIndex, endIndex);
	    //获得工作流
	    var workflow = ListUtils.findObjectByProperty(this.jsonObjectPool, "id", workflowId);
	    if(endIndex==-1) { //工作流定义本身
	    	if(!this.surface.configOnly) {
				var property = new GraphicsEditor.UserListProperty(propertyName, workflow.name + "的配置权限", workflow.managers, this.jsonObjectPool, this.surface.displayOnly, function(value) {
					workflow.managers = value;
				});
				//添加配置工作流按钮
	        	property.addListButton("配置工作流", "/jeaf/graphicseditor/icons/config.gif", function(buttonElement) {
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
	        if(type=="subflows") { //子流程列表
	        	var property = new GraphicsEditor.ListProperty(propertyName, "子流程列表", workflow.subFlows, this.jsonObjectPool, true);
				property.onItemDblClick = function(item) {
					applicationElement.loadSubFlow(workflow.id, item.id);
				};
				return [property];
	        }
	        else if(type=="subflow") { //子流程
	            var subflow = ListUtils.findObjectByProperty(this.jsonObjectPool, "id", propertyName.substring(beginIndex));
	            var property = new GraphicsEditor.UserListProperty(propertyName, subflow.name + "的配置权限", subflow.managers, this.jsonObjectPool, this.surface.displayOnly, function(value) {
					subflow.managers = value;
				});
				//添加配置工作流按钮
	        	property.addListButton("配置子流程", "/jeaf/graphicseditor/icons/config.gif", function(buttonElement) {
	        		applicationElement.loadSubFlow(workflow.id, subflow.id);
	        	});
	        	return [property];
	        }
	    }
	}
	else if(!this.surface.readOnly && !this.surface.configOnly) {
	    //基本属性
	    var propertyList = [];
	   	propertyList.push(new GraphicsEditor.TextProperty("title", "标题", application.title, this.surface.displayOnly, function(value) {
			application.title = value;
			applicationElement.setTitle(application.title);
		}));
        if(this.surface.deleteDisable || this.surface.displayOnly || application.name!=null) {
        	propertyList.push(new GraphicsEditor.TextProperty("name", "名称", application.name, true));
        }
        else { //新增的应用,配置选择列表
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
		    propertyList.push(new GraphicsEditor.DropdownProperty("name", "名称", application.name, doGetItemsText, true, function(value) {
				//必须是未配置过的应用
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
			    if(!title.indexOf("应用")==0) {
			        sourceApplication.title = title;
			    }
				applicationElement.setTitle(sourceApplication.title);
				applicationElement.eaiEditor.showPropertyTree();
			}));
        }
       	propertyList.push(new GraphicsEditor.TextProperty("description", "描述", application.description, this.surface.displayOnly, function(value) {
			application.description = value;
		}));
		return propertyList;
	}
	return null;
};
EAIEditor.Application.prototype.createWorkflow = function() { //新建流程
	if(!this.eaiEditor.modified || confirm("配置已修改,如果继续,所做的配置将丢失,是否继续?")) {
		window.top.location = RequestUtils.getContextPath() + "/eai/workflowConfig.shtml" +
							  "?act=createWorkflow" +
							  "&applicationSetName=" + this.application.applicationSetName +
							  "&applicationName=" + this.application.name;
	}
};
EAIEditor.Application.prototype.loadWorkflow = function(workflowId) { //加载流程
	if(!this.eaiEditor.modified || confirm("配置已修改,如果继续,所做的配置将丢失,是否继续?")) {
		window.top.location = RequestUtils.getContextPath() + "/eai/workflowConfig.shtml" +
							  "?act=editWorkflow" +
							  "&applicationSetName=" + this.application.applicationSetName +
							  "&applicationName=" + this.application.name +
							  "&workflowId=" + workflowId;
	}
};
EAIEditor.Application.prototype.loadSubFlow = function(workflowId, subFlowProcessId) { //加载子流程
	if(!this.eaiEditor.modified || confirm("配置已修改,如果继续,所做的配置将丢失,是否继续?")) {
		window.top.location = RequestUtils.getContextPath() + "/eai/workflowConfig.shtml" +
							  "?act=editSubFlow" +
							  "&applicationSetName=" + this.application.applicationSetName +
							  "&applicationName=" + this.application.name +
							  "&workflowId=" + workflowId +
							  "&subFlowProcessId=" + subFlowProcessId;
	}
};