var WORKFLOW_USERMODEL_PACKAGE = "com.yuanluesoft.workflow.client.model.user";
var WORKFLOW_DEFINITION_PACKAGE = "com.yuanluesoft.workflow.client.model.definition";
var WORKFLOW_RESOURCE_PACKAGE = "com.yuanluesoft.workflow.client.model.resource";
var USERMODEL_PACKAGE = "com.yuanluesoft.jeaf.base.model.user"
WorkflowEditor.Activity = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 3;
	this.rightConnectPointNumber = 3;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 7 * workflowEditor.gridSize, 5 * workflowEditor.gridSize, 2);
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Activity.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Activity.prototype.getPropertyTree = function() {
	var tree = {id: this.activity.id, label: this.activity.name, childNodes: []};
	tree.childNodes.push({id: "transact", label: "办理方式"});
	tree.childNodes.push({id: "participant", label: "办理人"});
	tree.childNodes.push({id: "query", label: "查询人"});
	tree.childNodes.push({id: "actionList", label: "操作列表"});
	if("PUBLIC"==this.workflowEditor.workflowProcess.accessLevel) {
		var directNode = {id: "direct", label: "直接切入", childNodes: []};
		tree.childNodes.push(directNode);
	    if(this.activity.direct) {
		    directNode.childNodes.push({id: "direct.actionList", label: "操作列表"});
		}
	}
	var subFlowNode = {id: "subFlow", label: "子流程", childNodes: []};
	tree.childNodes.push(subFlowNode);
	subFlowNode.childNodes.push({id: "subFlow.actionList", label: "允许选择的操作"});
	subFlowNode.childNodes.push({id: "subFlow.dataFieldList", label: "允许处理的字段"});
	subFlowNode.childNodes.push({id: "subFlow.applicationList", label: "允许执行的过程"});
	subFlowNode.childNodes.push({id: "subFlow.subFormList", label: "允许选择的表单状态"});
	
	tree.childNodes.push({id: "reverse", label: "回退"});
	tree.childNodes.push({id: "undo", label: "取回"});
	return tree;
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Activity.prototype.getPropertyList = function(propertyName) {
	window.activityElement = this;
	var activityElement = this;
	if("transact"==propertyName) { //办理方式
		var propertyList = [];
		var transactModeList = [];
		for(var i = 0; i < (this.activity.transactModeList ? this.activity.transactModeList.length : 0); i++) {
			transactModeList.push(ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.activity.transactModeList[i].uuid));
		}
		var modifyTransactMode = function(transactMode, add) {
			if(add) {
				transactMode = JsonUtils.addJsonObject(activityElement.workflowEditor.jsonObjectPool, "com.yuanluesoft.workflow.client.model.definition.TransactMode", transactMode);
				if(!activityElement.activity.transactModeList) {
					activityElement.activity.transactModeList = [];
				}
				activityElement.activity.transactModeList.push({uuid: transactMode.uuid});
			}
			else {
				transactMode = ListUtils.removeObjectByProperty(transactModeList, 'name', transactMode.name);
				ListUtils.removeObjectByProperty(activityElement.workflowEditor.jsonObjectPool, 'uuid', transactMode.uuid);
				ListUtils.removeObjectByProperty(activityElement.activity.transactModeList, 'uuid', transactMode.uuid);
			}
		};
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_singleMode", "单人", ListUtils.findObjectByProperty(transactModeList, 'name', 'single')!=null, function(value) {
			modifyTransactMode({name: 'single'}, value);
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_sequenceMode", "多人顺序", ListUtils.findObjectByProperty(transactModeList, 'name', 'sequence')!=null, function(value) {
			modifyTransactMode({name: 'sequence'}, value);
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_parallelMode", "多人并行", ListUtils.findObjectByProperty(transactModeList, 'name', 'parallel')!=null, function(value) {
			modifyTransactMode({name: 'parallel'}, value);
		}));
		var vodeMode = ListUtils.findObjectByProperty(transactModeList, 'name', 'vote');
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_voteMode", "表决", vodeMode!=null, function(value) {
			modifyTransactMode({name: 'vote', voteNumber: 1}, value);
			activityElement.workflowEditor.showPropertyTree();
		}));
		if(vodeMode!=null) {
			propertyList.push(new GraphicsEditor.DropdownProperty("transact_voteNumber", "有效票数", vodeMode.voteNumber, '1\0 2\0 3\0 4\0 5\0 6\0 7\0 8\0 9\0 10', false, function(value) {
				vodeMode.voteNumber = Number(value);
			}));
		}
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_autoSend", "自动发送", this.activity.autoSend, function(value) {
			activityElement.activity.autoSend = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_transmitEnable", "允许转办", this.activity.transmitEnable, function(value) {
			activityElement.activity.transmitEnable = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_addParticipantsEnable", "自行增加办理人", this.activity.addParticipantsEnable, function(value) {
			activityElement.activity.addParticipantsEnable = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_agentDisable", "禁止代办", this.activity.agentDisable, function(value) {
			activityElement.activity.agentDisable = value;
		}));
		return propertyList;
	}
	else if("participant"==propertyName) { //办理人
		this.listProperty = new GraphicsEditor.ListProperty(propertyName, "办理人", this.activity.participantList, this.workflowEditor.jsonObjectPool, false, null, null, function(value) {
			activityElement.activity.participantList = value;
		});
		//添加操作
		this.listProperty.addListButton("添加办理人", "/workflow/configure/icons/person.gif", function(buttonElement) {
			activityElement._addParticipant();
		});
		return [this.listProperty];
	}
	else if("query"==propertyName) { //查询人
		this.listProperty = new GraphicsEditor.ListProperty(propertyName, "查询人", this.activity.visitorList, this.workflowEditor.jsonObjectPool, false, null, null, function(value) {
			activityElement.activity.visitorList = value;
		});
		//添加操作
		this.listProperty.addListButton("添加查询人", "/workflow/configure/icons/person.gif", function(buttonElement) {
			activityElement._addQueryUser();
		});
		return [this.listProperty];
	}
	else if("actionList"==propertyName) { //操作列表
		return [new GraphicsEditor.ListProperty(propertyName, "操作列表", this.activity.actionList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.actionList, null, function(value) {
			activityElement.activity.actionList = value;
		})];
	}
	else if("direct"==propertyName) { //直接切入流程
		var propertyList = [];
		propertyList.push(new GraphicsEditor.CheckBoxProperty("direct", "允许切入", this.activity.direct, function(value) {
			activityElement.activity.direct = value;
			activityElement.workflowEditor.showPropertyTree();
		}));
		if(this.activity.direct) {
			var subForm = this.activity.directSubForm ? ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.activity.directSubForm.uuid) : null;
			propertyList.push(new GraphicsEditor.DropdownProperty("directSubForm", "表单状态", (subForm ? subForm.name : null), this._generateSubFormItemsText(), true, function(value) {
				activityElement.activity.directSubForm = {uuid: value};
			}));
		}
		return propertyList;
	}
	else if("direct.actionList"==propertyName) { //直接切入流程时的操作列表
		return [new GraphicsEditor.ListProperty(propertyName, "直接切入时的操作列表", this.activity.directActionList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.actionList, null, function(value) {
			activityElement.activity.directActionList = value;
		})];
	}
	else if("reverse"==propertyName) { //允许退回的环节
		return [new GraphicsEditor.ListProperty(propertyName, "允许被本环节回退的环节", this.activity.reverseActivityList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.listActivities(true), null, function(value) {
			activityElement.activity.reverseActivityList = value;
		})];
	}
	else if("undo"==propertyName) { //允许撤消的环节
		return [new GraphicsEditor.ListProperty(propertyName, "允许从本环节取回的环节", this.activity.undoActivityList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.listActivities(true), null, function(value) {
			activityElement.activity.undoActivityList = value;
		})];
	}
	else if("subFlow"==propertyName) { //子流程
		var listProperty = new GraphicsEditor.ListProperty(propertyName, "子流程", this.activity.subFlowList, this.workflowEditor.jsonObjectPool, false);
		//添加操作
		listProperty.addListButton("新建子流程", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
			activityElement._createSubFlow(activityElement.activity.id);
		});
		//添加双击事件处理
		listProperty.onItemDblClick = function(item) {
			activityElement._loadSubFlow(item.id);
		};
		return [listProperty];
	}
	else if("subFlow.subFormList"==propertyName) { //允许选择的子表单
		return [new GraphicsEditor.ListProperty(propertyName, "允许选择的表单状态", this.activity.subFlowSubFormList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.subFormList, null, function(value) {
			activityElement.activity.subFlowSubFormList = value;
		})];
	}
	else if("subFlow.actionList"==propertyName) { //允许选择的操作
		return [new GraphicsEditor.ListProperty(propertyName, "允许选择的操作", this.activity.subFlowActionList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.actionList, null, function(value) {
			activityElement.activity.subFlowActionList = value;
		})];
	}
	else if("subFlow.dataFieldList"==propertyName) { //允许处理的字段
		return [new GraphicsEditor.ListProperty(propertyName, "允许处理的字段", this.activity.subFlowDataFieldList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.dataFieldList, null, function(value) {
			activityElement.activity.subFlowDataFieldList = value;
		})];
	}
	else if("subFlow.applicationList"==propertyName) { //允许执行的过程
		return [new GraphicsEditor.ListProperty(propertyName, "允许执行的过程", this.activity.subFlowApplicationList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.applicationList, null, function(value) {
			activityElement.activity.subFlowApplicationList = value;
		})];
	}
	else { //基本属性
		var propertyList = [];
		propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.activity.id, true));
		propertyList.push(new GraphicsEditor.TextProperty("name", "名称", this.activity.name, false, function(value) {
			activityElement.activity.name = value;
			activityElement.setTitle(value);
		}));
		var subForm = this.activity.subForm ? ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.activity.subForm.uuid) : null;
		propertyList.push(new GraphicsEditor.DropdownProperty("subForm", "表单状态", (subForm ? subForm.name : null), this._generateSubFormItemsText(), true, function(value) {
			activityElement.activity.subForm = {uuid: value};
		}));
		propertyList.push(new GraphicsEditor.DropdownProperty("deadlineCondition", "办理期限(天)", this.activity.deadlineCondition, "1\0 2\0 3\0 4\0 5\0 6\0 7\0 8\0 9\0 10", false, function(value) {
			activityElement.activity.deadlineCondition = value;
		}));
		propertyList.push(new GraphicsEditor.DropdownProperty("messageLevel", "消息通知等级", this.activity.messageLevel, "普通\0 高\0 低", true, function(value) {
			activityElement.activity.messageLevel = value;
		}));
		propertyList.push(new GraphicsEditor.DialogProperty("messageFormat", "消息通知格式", this.activity.messageFormat, "/workflow/configure/setMessageFormat.shtml", 430, 230, function(value) {
			activityElement.activity.messageFormat = value;
		}));
		propertyList.push(new GraphicsEditor.DropdownProperty("urgeHours", "催办周期(小时)", this.activity.urgeHours, "0\0 24\0 48\0 72\0 96\0 120\0 12\0 6\0 3\0 2\0 1\0 0.5", false, function(value) {
			activityElement.activity.urgeHours = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("urgeHoursAdjustable", "催办周期允许调整", this.activity.urgeHoursAdjustable, function(value) {
			activityElement.activity.urgeHoursAdjustable = value;
		}));
		return propertyList;
	}
};
WorkflowEditor.Activity.prototype._addParticipant = function() {
	var participantTypes = "个人|Person," +
						   "部门|Department," +
						   "角色|Role," +
						   "部门办理人|ParticipantDepartment," +
						   "角色办理人|ParticipantRole," +
						   "分管领导|Supervisor," +
						   "创建者|Creator," +
						   "指定环节的办理人|ActivityParticipant," +
						   "角色中特定人员|RoleFilter," +
						   "创建者所在部门|DepartmentCreator," +
						   "当前办理人所在部门|DepartmentCurrent," +
						   "指定环节的办理人所在部门|DepartmentActivity";
	var programmingParticipantList = this.workflowEditor.workflow.programmingParticipantList;
	for(var i = 0; i < (programmingParticipantList ? programmingParticipantList.length : 0); i++) {
		var programmingParticipant = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', programmingParticipantList[i].uuid);
		participantTypes += "," + programmingParticipant.name + "|ProgrammingParticipant_" + programmingParticipant.id;
	}
	DialogUtils.openListDialog("办理人类型", null, 330, 410, false, "", "window.activityElement._addUser('{value}', '{title}')", "", participantTypes);
};
WorkflowEditor.Activity.prototype._addQueryUser = function() { //添加查询人
	var userTypes = "个人|Person," + 
					"部门|Department," + 
					"角色|Role," +
					"分管领导|Supervisor," +
					"创建者|Creator," +
					"创建者所在部门|DepartmentCreator," +
					"当前办理人所在部门|DepartmentCurrent," +
					"指定环节的办理人|ActivityParticipant," +
					"指定环节的办理人所在部门|DepartmentActivity," + 
					"创建者所属角色|RoleCreator," + 
					"当前办理人所属角色|RoleCurrent," + 
					"指定环节的办理人所属角色|RoleActivity";
	var programmingParticipantList = this.workflowEditor.workflow.programmingParticipantList;
	for(var i = 0; i < (programmingParticipantList ? programmingParticipantList.length : 0); i++) {
		var programmingParticipant = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', programmingParticipantList[i].uuid);
		userTypes += "," + programmingParticipant.name + "|ProgrammingParticipant_" + programmingParticipant.id;
	}
	DialogUtils.openListDialog("查询人类型", null, 330, 410, false, "",  "window.activityElement._addUser('{value}', '{title}')", "", userTypes);
};
WorkflowEditor.Activity.prototype._addUser = function(type, title) { //添加用户
	if("Person"==type) {
		DialogUtils.selectPerson(560, 360, true, "{id},{name|用户列表|100%}", "window.activityElement._doAddUser('Person', '{id}', '{name}')");
	}
	else if("Department"==type) {
		DialogUtils.selectOrg(560, 360, true, '{id},{name|部门列表|100%}', "window.activityElement._doAddUser('Department', '{id}', '{name}')");
	}
	else if("Role"==type) {
		DialogUtils.selectRole(560, 360, true, '{id},{name|角色列表|100%}', "window.activityElement._doAddUser('Role', '{id}', '{name}')");
	}
	else if("ParticipantDepartment"==type) { //部门办理人
		this._selectParticipantDepartment();
	}
	else if("ParticipantRole"==type) { //角色办理人
		this._selectParticipantRole();
	}
	else if("Creator"==type) { //创建者
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "创建者"});
	}
	else if("DepartmentCreator"==type) { //和创建者相同部门的人员
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "和创建者相同部门的人员"});
	}
	else if("DepartmentCurrent"==type) { //和当前办理人相同部门的人员
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "和当前办理人相同部门的人员"});
	}
	else if("RoleCreator"==type) { //和创建者角色相同的人员
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "和创建者角色相同的人员"});
	}
	else if("RoleCurrent"==type) { //和当前办理人角色相同的人员
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "和当前办理人角色相同的人员"});
	}
	else if("ActivityParticipant"==type) { //指定环节的办理人
		this._selectActivityParticipant();
	}
	else if("RoleFilter"==type) { //角色中特定人员
		this._selectRoleFilter();
	}
	else if("Supervisor"==type) { //分管领导
		this._selectSupervisor();
	}
	else if("DepartmentActivity"==type || "RoleActivity"==type) { //指定环节的办理人所在部门,指定环节的办理人所属角色
		this._selectActivityDepartmentRole(type);
	}
	else if(type.indexOf("ProgrammingParticipant_")==0) { //编程的办理人
		this.listProperty.appendItem(WORKFLOW_RESOURCE_PACKAGE + ".ProgrammingParticipant", {id: type.substring("ProgrammingParticipant_".length), name: title});
	}
};
//选择部门办理人
WorkflowEditor.Activity.prototype._selectParticipantDepartment = function() {
	DialogUtils.selectOrg(560, 360, false, "", "_setParticipantDepartmentMode('{id}', '{name}')");
	window._setParticipantDepartmentMode = function(orgId, orgName) {
		DialogUtils.openListDialog("选择办理人", null, 560, 360, false, "", "_selectPersonOfParticipantDepartment('" + orgId + "', '" + orgName + "', '{value}')", "", "部门中任意一人办理,指定人员");
	};
	window._selectPersonOfParticipantDepartment = function(orgId, orgName, mode) { //指定用户作为部门办理人
		if(mode=="部门中任意一人办理") {
			_addParticipantDepartment(orgId, orgName);
		}
		else {
			DialogUtils.selectPerson(560, 360, true, "{id},{name|指定人员|100%}", "_addParticipantDepartment('" + orgId + "', '" + orgName + "', '{id}', '{name}')", '', '', '', orgId);
		}
	};
	var activityElement = this;
	window._addParticipantDepartment = function(orgId, orgName, personIds, personNames) { //添加部门办理人
		var user = {id: orgId, name: orgName};
		if(personIds) {
			user.personIds = personIds; //指定的办理人
			user.personNames = personNames; //指定的办理人
		}
		user.title = user.name + (!personNames ? "(任意一人办理)":"(由" + personNames + "办理)");
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".ParticipantDepartment", user);
	};
};
//选择角色办理人
WorkflowEditor.Activity.prototype._selectParticipantRole = function() { 
	DialogUtils.selectRole(560,360, false, "", "_setParticipantRoleMode('{id}', '{name}')");
	window._setParticipantRoleMode = function(roleId, roleName) {
		DialogUtils.openListDialog("选择办理人", null, 560, 360, false, "", "_selectPersonOfParticipantRole('" + roleId + "', '" + roleName + "', '{value}')", "", "任意一人办理,指定人员");
	};
	window._selectPersonOfParticipantRole = function(roleId, roleName, mode) { //指定用户作为部门办理人
		if(mode=="任意一人办理") {
			_addParticipantRole(roleId, roleName);
		}
		else {
			DialogUtils.selectPersonByRole(560, 360, true, "{id},{name|指定人员|100%}", "_addParticipantRole('" + roleId + "', '" + roleName + "', '{id}', '{name}')");
		}
	};
	var activityElement = this;
	window._addParticipantRole = function(roleId, roleName, personIds, personNames) { //添加部门办理人
		var user = {id: roleId, name: roleName};
		if(personIds) {
			user.personIds = personIds; //指定的办理人
			user.personNames = personNames; //指定的办理人
		}
		user.title = user.name + (!personNames ? "(任意一人办理)":"(由" + personNames + "办理)");
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".ParticipantRole", user);
	};
};
//选择角色中特定人员
WorkflowEditor.Activity.prototype._selectRoleFilter = function() { 
	var activityElement = this;
	DialogUtils.selectRole(560,360, false, "", "setRoleFilterMode('{id}', '{name}')");
	window.setRoleFilterMode = function(roleId, roleName) { //设置过滤方式
		var itemsText = "与创建者相同部门或单位|creator,与当前办理人相同部门或单位|current,与某环节办理人相同部门或单位|activity,指定部门或单位|department";
		DialogUtils.openListDialog("筛选方式", null, 560, 360, false, "", "_afterSetRoleFilterMode('" + roleId + "', '" + roleName + "', '{value}')", "", itemsText);
	};
	window._afterSetRoleFilterMode = function(roleId, roleName, mode) {
		if(mode=="activity") { //与某环节办理人相同部门或单位
			activityElement.workflowEditor.selectActivity("_doAddRoleFilter('" + roleId + "', '" + roleName + "', '" + mode + "', '{value}', '{title}')");
		}
		else if(mode=="department") { //指定部门或单位
			DialogUtils.selectOrg(560, 360, false, "","_doAddRoleFilter('" + roleId + "', '" + roleName + "', '" + mode + "', '', '', '{id}', '{name}')");
		}
		else {
			_doAddRoleFilter(roleId, roleName, mode);
		}
	};
	window._doAddRoleFilter = function(roleId, roleName, mode, activityUuid, activityName, departmentId, departmentName) { //添加角色中特定人员
		var user = {id: roleId, name: roleName, filter: mode};
		if(mode=="department") { //指定部门或单位
			user.departmentId = departmentId;
			user.departmentName = departmentName;
		}
		else if(mode=="activity") { //与某环节办理人相同部门或单位
			var activity = ListUtils.findObjectByProperty(activityElement.workflowEditor.jsonObjectPool, "uuid", activityUuid);
			user.activityId = activity.id; //环节ID
			user.activity = {uuid: activityUuid};
		}
		var prefix = "";
		if(mode=="creator") {
			prefix = "创建者所在部门或单位";
		}
		else if(mode=="current") {
			prefix = "当前办理人所在部门或单位";
		}
		else if(mode=="activity") {
			prefix = "环节" + (!activityName ? "" : "[" + activityName + "]") + "办理人所在部门或单位";
		}
		else if(mode=="department") {
			prefix = "部门[" + departmentName + "]";
		}
		user.title = prefix + "的[" + roleName + "]"; 
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".RoleFilter", user);
	};
};
//指定环节的办理人
WorkflowEditor.Activity.prototype._selectActivityParticipant = function() {
	this.workflowEditor.selectActivity("_addActivityParticipant('{value}', '{title}')"); //选择环节
	var activityElement = this;
	window._addActivityParticipant = function(activityUuid, activityName) {
		var user = {activity: {uuid: activityUuid}, title: "环节[" + activityName + "]的办理人"};
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".ActivityParticipant", user);
	}
};
//选择指定环节的办理人所在部门或角色
WorkflowEditor.Activity.prototype._selectActivityDepartmentRole = function(type) {
	this.workflowEditor.selectActivity("_addDepartmentRoleActivity('" + type + "', '{value}', '{title}')");
	var activityElement = this;
	window._addDepartmentRoleActivity = function(type, activityUuid, activityName) {
		var activity = ListUtils.findObjectByProperty(activityElement.workflowEditor.jsonObjectPool, "uuid", activityUuid);
		var user = {activityId: activity.id, activity: {uuid: activityUuid}, title: "和环节[" +  activityName + "]的办理人" + ("DepartmentActivity"==type ? "相同部门" : "角色相同") + "的人员"};
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, user);
	};
};
//选择分管领导
WorkflowEditor.Activity.prototype._selectSupervisor = function() {
	//设置过滤方式
	var itemsText = "创建者所在部门或单位的分管领导|creatorDepartment,当前办理人所在部门或单位的分管领导|currentDepartment,某环节办理人所在部门或单位的分管领导|activityDepartment,指定部门或单位的分管领导|department,创建者的分管领导|creator,当前办理人的分管领导|current,某环节办理人的分管领导|activity";
	DialogUtils.openListDialog("筛选方式", null, 560, 360, false, "", "_afterSetSupervisorFilterMode('{value}')", "", itemsText);
	var activityElement = this;
	window._afterSetSupervisorFilterMode = function(mode) {
		if(mode=="activity" || mode=="activityDepartment") { //与某环节办理人相同部门或单位
			activityElement.workflowEditor.selectActivity("_doAddSupervisor('" + mode + "', '{value}', '{title}')");
		}
		else if(mode=="department") { //指定部门或单位
			DialogUtils.selectOrg(560, 360, false, "","_doAddSupervisor('" + mode + "', '', '', '{id}', '{name}')");
		}
		else {
			_doAddSupervisor(mode);
		}
	};
	window._doAddSupervisor = function(mode, activityUuid, activityName, departmentId, departmentName) { //添加角色中特定人员
		var user = {filter: mode}; //过滤方式
		if(mode=="department") { //指定部门或单位
			user.departmentId = departmentId;
			user.departmentName = departmentName;
		}
		else if(mode=="activityDepartment" || mode=="activity") { //与某环节办理人相同部门或单位
			var activity = ListUtils.findObjectByProperty(activityElement.workflowEditor.jsonObjectPool, "uuid", activityUuid);
			user.activityId = activity.id; //环节ID
			user.activity = {uuid: activityUuid};
		}
		var prefix = "";
		if(mode=="creatorDepartment") {
			prefix = "创建者所在部门或单位";
		}
		else if(mode=="currentDepartment") {
			prefix = "当前办理人所在部门或单位";
		}
		else if(mode=="activityDepartment") {
			prefix = "环节[" + activityName + "]办理人所在部门或单位";
		}
		else if(mode=="department") {
			prefix = "部门[" + departmentName + "]";
		}
		else if(mode=="creator") {
			prefix = "创建者";
		}
		else if(mode=="current") {
			prefix = "当前办理人";
		}
		else if(mode=="activity") {
			prefix = "环节[" + activityName + "]办理人";
		}
		user.title = prefix + "的分管领导"; 
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".Supervisor", user);
	};
};
//完成添加: 用户|Person/部门|Department/角色|Role
WorkflowEditor.Activity.prototype._doAddUser = function(userType, ids, names) {
	ids = ids.split(",");
	names = names.split(",");
	for(var i=0; i<ids.length; i++) {
		this.listProperty.appendItem(USERMODEL_PACKAGE + '.' + userType, {id: ids[i], name: names[i]});
	}
};
//生成子表单选项文本
WorkflowEditor.Activity.prototype._generateSubFormItemsText = function() {
	var itemsText = "";
	for(var i = 0; i < (this.workflowEditor.workflow.subFormList ? this.workflowEditor.workflow.subFormList.length : 0); i++) {
		var subForm = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, "uuid", this.workflowEditor.workflow.subFormList[i].uuid);
		itemsText += (itemsText=="" ? "" : "\0") + subForm.name + "|" + subForm.uuid;
	}
	return itemsText;
};
//创建子流程
WorkflowEditor.Activity.prototype._createSubFlow = function(parentActivityId) {
	if(document.getElementsByName("act")[0].value.indexOf("create")!=-1) {
		alert("当前流程尚未保存，不允许创建子流程！");
		return;
	}
	if(!this.workflowEditor.modified || confirm("配置已修改,如果继续,所做的配置将丢失,是否继续?")) {
		var url = RequestUtils.getContextPath() + "/workflow/workflowConfigure.shtml";
		url += "?act=createSubFlow";
		url += "&workflowId=" + document.getElementsByName('workflowId')[0].value;
		url += "&parentProcessId=" + activityElement.workflowEditor.workflowProcess.id;
		url += "&parentActivityId=" + parentActivityId;
		url += "&returnURL=" + StringUtils.utf8Encode(top.location.href);
		url += "&ticket=" + document.getElementsByName("ticket")[0].value;
		window.top.location = url;
	}
};
//加载子流程
WorkflowEditor.Activity.prototype._loadSubFlow = function(subFlowProcessId) {
	if(!this.workflowEditor.modified || confirm("配置已修改,如果继续,所做的配置将丢失,是否继续?")) {
		var url = RequestUtils.getContextPath() + "/workflow/workflowConfigure.shtml";
		url += "?act=editSubFlow";
		url += "&workflowId=" + document.getElementsByName('workflowId')[0].value;
		url += "&subFlowProcessId=" + subFlowProcessId;
		url += "&returnURL=" + StringUtils.utf8Encode(top.location.href);
		url += "&ticket=" + document.getElementsByName("ticket")[0].value;
		window.top.location = url;
	}
};