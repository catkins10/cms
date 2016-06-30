WorkflowEditor = function(canvasParentElement, drawBarContainer, propertyTreeContainer, propertyDetailContainer, workflowJson) {
	this.workflowJson = workflowJson;
	this.jsonObjectPool = workflowJson.objects;
	this.workflow = ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", workflowJson.uuid); 
	this.workflowProcess =  ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", this.workflow.workflowProcessList[0].uuid); 
	this.workflowExtend = ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", this.workflow.workflowPackageExtend.uuid); 
	this.workflowProcessExtend = ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", this.workflowExtend.workflowProcessExtendList[0].uuid); 
	var drawBarButtons = [{elementType:'Begin', name:'\u5F00\u59CB', iconUrl:'/workflow/configure/icons/begin.gif'},
						  {elementType:'End', name:'\u7ED3\u675F', iconUrl:'/workflow/configure/icons/end.gif'},
						  {elementType:'Activity', name:'\u73AF\u8282', iconUrl:'/workflow/configure/icons/element.gif'},
						  {elementType:'Decision', name:'\u51B3\u7B56', iconUrl:'/workflow/configure/icons/rhombus.gif'},
						  {elementType:'Procedure', name:'\u8FC7\u7A0B', iconUrl:'/workflow/configure/icons/roundRect.gif'},
						  {elementType:'Split', name:'\u540C\u65F6\u542F\u52A8', iconUrl:'/workflow/configure/icons/split.gif'},
						  {elementType:'Join', name:'\u540C\u65F6\u5B8C\u6210', iconUrl:'/workflow/configure/icons/join.gif'},
						  {elementType:'Line', name:'\u8FDE\u63A5', iconUrl:'/workflow/configure/icons/line.gif'},
						  {elementType:'BrokenLine', name:'\u8FDE\u63A5(\u6298\u7EBF)', iconUrl:'/workflow/configure/icons/brokenLine.gif'}];
	GraphicsEditor.call(this, canvasParentElement, drawBarContainer, drawBarButtons, propertyTreeContainer, propertyDetailContainer);
};
WorkflowEditor.prototype = new GraphicsEditor();
WorkflowEditor.prototype.init = function() {
	
    for(var i = 0; i < (this.workflowProcess.activityList ? this.workflowProcess.activityList.length : 0); i++) {
    	var activity = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.workflowProcess.activityList[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, activity.id);
    	var className = activity.uuid.substring(activity.uuid.lastIndexOf('.') + 1, activity.uuid.lastIndexOf('@'));
    	eval('this.addElement(new WorkflowEditor.' + className + '(this, activity, surface));');
	}
	
    for(var i = 0; i < (this.workflowProcess.transitionList ? this.workflowProcess.transitionList.length : 0); i++) {
        var transition = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.workflowProcess.transitionList[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, transition.id);
        this.addElement(new WorkflowEditor.Transition(this, transition, surface));
    }
};
WorkflowEditor.prototype.createElement = function(elementType) {
	if(!this.workflowProcess.activityList) {
		this.workflowProcess.activityList = [];
	}
	if(!this.workflowProcess.transitionList) {
		this.workflowProcess.transitionList = [];
	}
	if('Begin'==elementType) { 
		var begin = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Begin', {id: "begin-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: begin.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', begin.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.Begin(this, begin, surface);
	}
	else if('End'==elementType) { 
		var end = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.End', {id: "end-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: end.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', end.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.End(this, end, surface);
	}
	else if('Activity'==elementType) { 
		var activity = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Activity', {id: "activity-" + (this.workflowProcessExtend.nextElementId++)});
		var activities = ListUtils.findObjectsByType(this.elements, 'WorkflowEditor.Activity', 0);
		activity.name = '\u73AF\u8282' + (activities ? activities.length + 1 : 1);
		this.workflowProcess.activityList.push({uuid: activity.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', activity.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.Activity(this, activity, surface);
	}
	else if('Decision'==elementType) { 
		var decision = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Decision', {id: "decision-" + (this.workflowProcessExtend.nextElementId++)});
		var activities = ListUtils.findObjectsByType(this.elements, 'WorkflowEditor.Decision', 0);
		decision.name = '\u51B3\u7B56' + (activities ? activities.length + 1 : 1);
		this.workflowProcess.activityList.push({uuid: decision.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', decision.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.Decision(this, decision, surface);
	}
	else if('Procedure'==elementType) { 
		var procedure = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Procedure', {id: "procedure-" + (this.workflowProcessExtend.nextElementId++)});
		var activities = ListUtils.findObjectsByType(this.elements, 'WorkflowEditor.Procedure', 0);
		procedure.name = '\u8FC7\u7A0B' + (activities ? activities.length + 1 : 1);
		this.workflowProcess.activityList.push({uuid: procedure.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', procedure.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.Procedure(this, procedure, surface);
	}
	else if('Split'==elementType) { 
		var split = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Split', {id: "split-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: split.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', split.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.Split(this, split, surface);
	}
	else if('Join'==elementType) { 
		var join = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Join', {id: "join-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: join.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', join.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		
		return new WorkflowEditor.Join(this, join, surface);
	}
	else if('Line'==elementType || 'BrokenLine'==elementType) { 
		
		var transition = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Transition', {id: "transition-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.transitionList.push({uuid:transition.uuid});
		
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.' + elementType, transition.id);
		this.workflowProcessExtend.elements.push({uuid:surface.uuid});
		
		return new WorkflowEditor.Transition(this, transition, surface);
	}
};
WorkflowEditor.prototype.removeElement = function(element) {
	if(element instanceof WorkflowEditor.Transition) { 
	    ListUtils.removeObject(this.jsonObjectPool, element.transition);
		ListUtils.removeObjectByProperty(this.workflowProcess.transitionList, 'uuid', element.transition.uuid);
	}
	else { 
		ListUtils.removeObject(this.jsonObjectPool, element.activity);
   		ListUtils.removeObjectByProperty(this.workflowProcess.activityList, 'uuid', element.activity.uuid);
	}
	
	ListUtils.removeObject(this.jsonObjectPool, element.surface);
	ListUtils.removeObjectByProperty(this.workflowProcessExtend.elements, 'uuid', element.surface.uuid);
};
WorkflowEditor.prototype.getPropertyTree = function() {
	var tree = {id: this.workflow.id, label: this.workflowProcess.name, childNodes: []};
	tree.childNodes.push({id: "modifyHistory", label: "\u4FEE\u6539\u8BB0\u5F55"}); 
	return tree;
};
WorkflowEditor.prototype.getPropertyList = function(propertyName) {
	var workflowEditor = this;
	if(propertyName=="modifyHistory") { 
       	return [new GraphicsEditor.ListProperty(propertyName, "\u4FEE\u6539\u8BB0\u5F55", this.workflowProcessExtend.modifyHistory, this.jsonObjectPool, true)];
	}
	
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.workflow.id, true));
	propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", this.workflowProcess.name, false, function(value) {
		workflowEditor.workflowProcess.name = value;
		if("PUBLIC"==workflowEditor.workflowProcess.accessLevel) {
			workflowEditor.workflow.name = value;
		}
		workflowEditor.showPropertyTree();
	}));
	propertyList.push(new GraphicsEditor.TextProperty("creator", "\u521B\u5EFA\u4EBA", this.workflowProcessExtend.creator, true));
	propertyList.push(new GraphicsEditor.TextProperty("createDate", "\u521B\u5EFA\u65F6\u95F4", this.workflowProcessExtend.createDate, true));
	propertyList.push(new GraphicsEditor.TextProperty("description", "\u63CF\u8FF0", this.workflowProcess.description, false, function(value) {
		workflowEditor.workflowProcess.description = value;
	}));
	return propertyList;
};
WorkflowEditor.prototype.saveWorkflow = function(pasteWorkflow) {
	var validateError = this._validate();
	if(validateError) {
		this.selectElements(validateError.elements);
		alert(validateError.error);
		return;
	}
	document.getElementsByName('json')[0].value = JsonUtils.stringify(this.workflowJson);
	FormUtils.doAction('saveWorkflowConfigure', (pasteWorkflow ? 'pasteWorkflow=true' : ''));
};
WorkflowEditor.prototype._validate = function() {
	if(!this.workflowProcess.activityList || this.workflowProcess.activityList.length==0) {
		return {error: "\u672A\u521B\u5EFA\u4EFB\u4F55\u6D41\u7A0B\u5143\u7D20", elements: null};
	}
	if(!this.workflowProcess.transitionList || this.workflowProcess.transitionList.length==0) {
		return {error: "\u672A\u521B\u5EFA\u4EFB\u4F55\u8FDE\u63A5", elements: null};
	}
	
	var validateError = this.validateLines(false);
	if(validateError) {
		return validateError;
	}
	
	var containBegin = false;
	var containEnd = false;
	for(var i = 0; i < this.elements.length; i++) {
		var activity = this.elements[i];
		if(!(activity instanceof GraphicsEditor.Node)) {
			continue;
		}
		if(activity instanceof WorkflowEditor.Begin) {
			if(activity.exitLines.length==0) {
				return {error: "\u51FA\u53E3\u4E0D\u80FD\u4E3A\u7A7A", elements: [activity]};		
			}
			if(activity.enterLines.length>0) {
				return {error: "\u4E0D\u5141\u8BB8\u521B\u5EFA\u5230\u5F00\u59CB\u5143\u7D20\u7684\u8FDE\u63A5", elements: activity.enterLines};
			}
			containBegin = true;
		}
		else if(activity instanceof WorkflowEditor.End) {
			if(activity.enterLines.length==0) {
				return {error: "\u5165\u53E3\u4E0D\u80FD\u4E3A\u7A7A", elements: [activity]};		
			}
			if(activity.exitLines.length>0) {
				return {error: "\u4E0D\u5141\u8BB8\u521B\u5EFA\u4ECE\u7ED3\u675F\u5143\u7D20\u5230\u5176\u4ED6\u6D41\u7A0B\u5143\u7D20\u7684\u8FDE\u63A5", elements: activity.exitLines};
			}
			containEnd = true;
		}
		else {
			if(activity.enterLines.length==0) {
				return {error: "\u5165\u53E3\u4E0D\u80FD\u4E3A\u7A7A", elements: [activity]};			
			}
			if(activity.exitLines.length==0) {
				return {error: "\u51FA\u53E3\u4E0D\u80FD\u4E3A\u7A7A", elements: [activity]};			
			}
		}
	}
	if(!containBegin) {
		return {error: "\u5C1A\u672A\u521B\u5EFA\u5F00\u59CB\u5143\u7D20", elements: null};
	}
	if(!containEnd) {
		return {error: "\u5C1A\u672A\u521B\u5EFA\u7ED3\u675F\u5143\u7D20", elements: null};
	}
};
WorkflowEditor.prototype.workflowTest = function() {
	if(this.modified) {
		alert("\u914D\u7F6E\u5DF2\u4FEE\u6539\uFF0C\u8BF7\u5148\u4FDD\u5B58\uFF01")
		return;
	}
	var testURL = document.getElementsByName("testURL")[0].value;
	if(testURL=="") {
		return;
	}
	DialogUtils.selectPerson(560, 360, false, "", "selectWorkflowTestEntry('{id}')"); 
	var workflowEditor = this;
	window.selectWorkflowTestEntry = function(testUserId) { 
		workflowEditor.selectActivity("enterWorkflowTest('" + testUserId + "', '{value}')");
	};
	window.enterWorkflowTest = function(testUserId, activityUuid) { 
		var workflowId = document.getElementsByName("workflowId")[0].value;
		var activity = ListUtils.findObjectByProperty(workflowEditor.jsonObjectPool, 'uuid', activityUuid);
		PageUtils.openurl(testURL + (testURL.indexOf("?")==-1 ? "?" : "&") + "workflowId=" + workflowId + "&activityId=" + activity.id + "&workflowTest=true&testUserId=" + testUserId, "mode=fullscreen", "workflowTest");
	};
};
WorkflowEditor.prototype.copyWorkflow = function() {
	document.cookie = 'copiedWorkflowId=' + document.getElementsByName("workflowId")[0].value + ';path=/'; 
	document.cookie = 'copiedSubFlowProcessId=' + document.getElementsByName("subFlowProcessId")[0].value + ';path=/'; 
};
WorkflowEditor.prototype.pasteWorkflow = function() {
	if(!confirm('\u7C98\u5E16\u540E\uFF0C\u5F53\u524D\u6D41\u7A0B\u914D\u7F6E\u4F1A\u88AB\u8986\u76D6\uFF0C\u662F\u5426\u786E\u5B9A\uFF1F')) {
		return;
	}
	this.saveWorkflow(true);
};
WorkflowEditor.prototype.deleteWorkflow = function() {
	if(confirm("\u5220\u9664\u540E\u5C06\u4E0D\u80FD\u518D\u6062\u590D\uFF0C\u662F\u5426\u786E\u5B9A\u8981\u5220\u9664\uFF1F")) {
		FormUtils.doAction('deleteWorkflowConfigure');
	}
};
WorkflowEditor.prototype.back = function() {
	if(this.modified && !confirm("\u914D\u7F6E\u5DF2\u4FEE\u6539\uFF0C\u662F\u5426\u7EE7\u7EED\uFF1F")) {
		return;
	}
	var returnURL = document.getElementsByName("returnURL")[0].value;
	if(!returnURL || returnURL=="") {
		window.close();
		return;
	}
	window.location = returnURL;
};
WorkflowEditor.prototype.listEnumerationValues = function(enumerationType) {
    var enumeration;
    for(var i = 0; !enumeration && i < (this.workflow.enumerationList ? this.workflow.enumerationList.length : 0); i++) {
    	var obj = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.workflow.enumerationList[i].uuid);
    	if(obj.id==enumerationType) {
    		enumeration = obj;
    	}
    }
    return enumeration.valueList;
};
WorkflowEditor.prototype.selectActivity = function(script) {
	var itemsText = "";
	var activities = this.listActivities(false);
	for(var i = 0; i < activities.length; i++) {
		itemsText += (itemsText=="" ? "" : ",") + activities[i].name + "|" + activities[i].uuid;
	}
	DialogUtils.openListDialog("\u9009\u62E9\u6D41\u7A0B\u73AF\u8282", null, 560, 360, false, "", script, "", itemsText);
};
WorkflowEditor.prototype.listActivities = function(forReverseOrUndo) {
	var activities = [];
	if(forReverseOrUndo) {
		var activityAll = ListUtils.findObjectByProperty(this.jsonObjectPool, 'id', "activity-all");
		if(!activityAll) {
			activityAll = JsonUtils.addJsonObject(this.jsonObjectPool, WORKFLOW_DEFINITION_PACKAGE + ".Activity", {id: "activity-all", name: "[\u5168\u90E8]"});
		}
		var activityPrevious = ListUtils.findObjectByProperty(this.jsonObjectPool, 'id', "activity-previous");
		if(!activityPrevious) {
			activityPrevious = JsonUtils.addJsonObject(this.jsonObjectPool, WORKFLOW_DEFINITION_PACKAGE + ".Activity", {id: "activity-previous", name: "[\u4E0A\u4E00\u73AF\u8282]"});
		}
		var activityCreator = ListUtils.findObjectByProperty(this.jsonObjectPool, 'id', "activity-creator");
		if(!activityCreator) {
			activityCreator = JsonUtils.addJsonObject(this.jsonObjectPool, WORKFLOW_DEFINITION_PACKAGE + ".Activity", {id: "activity-creator", name: "[\u521B\u5EFA\u8005]"});
		}
		activities.push(activityAll);
		activities.push(activityPrevious);
		activities.push(activityCreator);
	}
	for(var i = 0; i < this.elements.length; i++) {
		if(this.elements[i].surface.id.indexOf('activity-')==0) {
			activities.push(this.elements[i].activity);
		}
	}
	return activities;
};
EventUtils.addEvent(window, 'load', function() {
	var json;
	eval('json=' + document.getElementsByName('json')[0].value + ';');
	window.workflowEditor = new WorkflowEditor(document.getElementById('canvasView'), document.getElementById('drawActionBar'), document.getElementById('propertyTree'), document.getElementById('propertyDetail'), json);
});

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
WorkflowEditor.Activity.prototype.getPropertyTree = function() {
	var tree = {id: this.activity.id, label: this.activity.name, childNodes: []};
	tree.childNodes.push({id: "transact", label: "\u529E\u7406\u65B9\u5F0F"});
	tree.childNodes.push({id: "participant", label: "\u529E\u7406\u4EBA"});
	tree.childNodes.push({id: "query", label: "\u67E5\u8BE2\u4EBA"});
	tree.childNodes.push({id: "actionList", label: "\u64CD\u4F5C\u5217\u8868"});
	if("PUBLIC"==this.workflowEditor.workflowProcess.accessLevel) {
		var directNode = {id: "direct", label: "\u76F4\u63A5\u5207\u5165", childNodes: []};
		tree.childNodes.push(directNode);
	    if(this.activity.direct) {
		    directNode.childNodes.push({id: "direct.actionList", label: "\u64CD\u4F5C\u5217\u8868"});
		}
	}
	var subFlowNode = {id: "subFlow", label: "\u5B50\u6D41\u7A0B", childNodes: []};
	tree.childNodes.push(subFlowNode);
	subFlowNode.childNodes.push({id: "subFlow.actionList", label: "\u5141\u8BB8\u9009\u62E9\u7684\u64CD\u4F5C"});
	subFlowNode.childNodes.push({id: "subFlow.dataFieldList", label: "\u5141\u8BB8\u5904\u7406\u7684\u5B57\u6BB5"});
	subFlowNode.childNodes.push({id: "subFlow.applicationList", label: "\u5141\u8BB8\u6267\u884C\u7684\u8FC7\u7A0B"});
	subFlowNode.childNodes.push({id: "subFlow.subFormList", label: "\u5141\u8BB8\u9009\u62E9\u7684\u8868\u5355\u72B6\u6001"});
	
	tree.childNodes.push({id: "reverse", label: "\u56DE\u9000"});
	tree.childNodes.push({id: "undo", label: "\u53D6\u56DE"});
	return tree;
};
WorkflowEditor.Activity.prototype.getPropertyList = function(propertyName) {
	window.activityElement = this;
	var activityElement = this;
	if("transact"==propertyName) { 
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
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_singleMode", "\u5355\u4EBA", ListUtils.findObjectByProperty(transactModeList, 'name', 'single')!=null, function(value) {
			modifyTransactMode({name: 'single'}, value);
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_sequenceMode", "\u591A\u4EBA\u987A\u5E8F", ListUtils.findObjectByProperty(transactModeList, 'name', 'sequence')!=null, function(value) {
			modifyTransactMode({name: 'sequence'}, value);
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_parallelMode", "\u591A\u4EBA\u5E76\u884C", ListUtils.findObjectByProperty(transactModeList, 'name', 'parallel')!=null, function(value) {
			modifyTransactMode({name: 'parallel'}, value);
		}));
		var vodeMode = ListUtils.findObjectByProperty(transactModeList, 'name', 'vote');
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_voteMode", "\u8868\u51B3", vodeMode!=null, function(value) {
			modifyTransactMode({name: 'vote', voteNumber: 1}, value);
			activityElement.workflowEditor.showPropertyTree();
		}));
		if(vodeMode!=null) {
			propertyList.push(new GraphicsEditor.DropdownProperty("transact_voteNumber", "\u6709\u6548\u7968\u6570", vodeMode.voteNumber, '1\0 2\0 3\0 4\0 5\0 6\0 7\0 8\0 9\0 10', false, function(value) {
				vodeMode.voteNumber = Number(value);
			}));
		}
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_autoSend", "\u81EA\u52A8\u53D1\u9001", this.activity.autoSend, function(value) {
			activityElement.activity.autoSend = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_transmitEnable", "\u5141\u8BB8\u8F6C\u529E", this.activity.transmitEnable, function(value) {
			activityElement.activity.transmitEnable = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_addParticipantsEnable", "\u81EA\u884C\u589E\u52A0\u529E\u7406\u4EBA", this.activity.addParticipantsEnable, function(value) {
			activityElement.activity.addParticipantsEnable = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("transact_agentDisable", "\u7981\u6B62\u4EE3\u529E", this.activity.agentDisable, function(value) {
			activityElement.activity.agentDisable = value;
		}));
		return propertyList;
	}
	else if("participant"==propertyName) { 
		this.listProperty = new GraphicsEditor.ListProperty(propertyName, "\u529E\u7406\u4EBA", this.activity.participantList, this.workflowEditor.jsonObjectPool, false, null, null, function(value) {
			activityElement.activity.participantList = value;
		});
		
		this.listProperty.addListButton("\u6DFB\u52A0\u529E\u7406\u4EBA", "/workflow/configure/icons/person.gif", function(buttonElement) {
			activityElement._addParticipant();
		});
		return [this.listProperty];
	}
	else if("query"==propertyName) { 
		this.listProperty = new GraphicsEditor.ListProperty(propertyName, "\u67E5\u8BE2\u4EBA", this.activity.visitorList, this.workflowEditor.jsonObjectPool, false, null, null, function(value) {
			activityElement.activity.visitorList = value;
		});
		
		this.listProperty.addListButton("\u6DFB\u52A0\u67E5\u8BE2\u4EBA", "/workflow/configure/icons/person.gif", function(buttonElement) {
			activityElement._addQueryUser();
		});
		return [this.listProperty];
	}
	else if("actionList"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u64CD\u4F5C\u5217\u8868", this.activity.actionList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.actionList, null, function(value) {
			activityElement.activity.actionList = value;
		})];
	}
	else if("direct"==propertyName) { 
		var propertyList = [];
		propertyList.push(new GraphicsEditor.CheckBoxProperty("direct", "\u5141\u8BB8\u5207\u5165", this.activity.direct, function(value) {
			activityElement.activity.direct = value;
			activityElement.workflowEditor.showPropertyTree();
		}));
		if(this.activity.direct) {
			var subForm = this.activity.directSubForm ? ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.activity.directSubForm.uuid) : null;
			propertyList.push(new GraphicsEditor.DropdownProperty("directSubForm", "\u8868\u5355\u72B6\u6001", (subForm ? subForm.name : null), this._generateSubFormItemsText(), true, function(value) {
				activityElement.activity.directSubForm = {uuid: value};
			}));
		}
		return propertyList;
	}
	else if("direct.actionList"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u76F4\u63A5\u5207\u5165\u65F6\u7684\u64CD\u4F5C\u5217\u8868", this.activity.directActionList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.actionList, null, function(value) {
			activityElement.activity.directActionList = value;
		})];
	}
	else if("reverse"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u5141\u8BB8\u88AB\u672C\u73AF\u8282\u56DE\u9000\u7684\u73AF\u8282", this.activity.reverseActivityList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.listActivities(true), null, function(value) {
			activityElement.activity.reverseActivityList = value;
		})];
	}
	else if("undo"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u5141\u8BB8\u4ECE\u672C\u73AF\u8282\u53D6\u56DE\u7684\u73AF\u8282", this.activity.undoActivityList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.listActivities(true), null, function(value) {
			activityElement.activity.undoActivityList = value;
		})];
	}
	else if("subFlow"==propertyName) { 
		var listProperty = new GraphicsEditor.ListProperty(propertyName, "\u5B50\u6D41\u7A0B", this.activity.subFlowList, this.workflowEditor.jsonObjectPool, false);
		
		listProperty.addListButton("\u65B0\u5EFA\u5B50\u6D41\u7A0B", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
			activityElement._createSubFlow(activityElement.activity.id);
		});
		
		listProperty.onItemDblClick = function(item) {
			activityElement._loadSubFlow(item.id);
		};
		return [listProperty];
	}
	else if("subFlow.subFormList"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u5141\u8BB8\u9009\u62E9\u7684\u8868\u5355\u72B6\u6001", this.activity.subFlowSubFormList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.subFormList, null, function(value) {
			activityElement.activity.subFlowSubFormList = value;
		})];
	}
	else if("subFlow.actionList"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u5141\u8BB8\u9009\u62E9\u7684\u64CD\u4F5C", this.activity.subFlowActionList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.actionList, null, function(value) {
			activityElement.activity.subFlowActionList = value;
		})];
	}
	else if("subFlow.dataFieldList"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u5141\u8BB8\u5904\u7406\u7684\u5B57\u6BB5", this.activity.subFlowDataFieldList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.dataFieldList, null, function(value) {
			activityElement.activity.subFlowDataFieldList = value;
		})];
	}
	else if("subFlow.applicationList"==propertyName) { 
		return [new GraphicsEditor.ListProperty(propertyName, "\u5141\u8BB8\u6267\u884C\u7684\u8FC7\u7A0B", this.activity.subFlowApplicationList, this.workflowEditor.jsonObjectPool, false, this.workflowEditor.workflow.applicationList, null, function(value) {
			activityElement.activity.subFlowApplicationList = value;
		})];
	}
	else { 
		var propertyList = [];
		propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.activity.id, true));
		propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", this.activity.name, false, function(value) {
			activityElement.activity.name = value;
			activityElement.setTitle(value);
		}));
		var subForm = this.activity.subForm ? ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.activity.subForm.uuid) : null;
		propertyList.push(new GraphicsEditor.DropdownProperty("subForm", "\u8868\u5355\u72B6\u6001", (subForm ? subForm.name : null), this._generateSubFormItemsText(), true, function(value) {
			activityElement.activity.subForm = {uuid: value};
		}));
		propertyList.push(new GraphicsEditor.DropdownProperty("deadlineCondition", "\u529E\u7406\u671F\u9650(\u5929)", this.activity.deadlineCondition, "1\0 2\0 3\0 4\0 5\0 6\0 7\0 8\0 9\0 10", false, function(value) {
			activityElement.activity.deadlineCondition = value;
		}));
		propertyList.push(new GraphicsEditor.DropdownProperty("messageLevel", "\u6D88\u606F\u901A\u77E5\u7B49\u7EA7", this.activity.messageLevel, "\u666E\u901A\0 \u9AD8\0 \u4F4E", true, function(value) {
			activityElement.activity.messageLevel = value;
		}));
		propertyList.push(new GraphicsEditor.DialogProperty("messageFormat", "\u6D88\u606F\u901A\u77E5\u683C\u5F0F", this.activity.messageFormat, "/workflow/configure/setMessageFormat.shtml", 430, 230, function(value) {
			activityElement.activity.messageFormat = value;
		}));
		propertyList.push(new GraphicsEditor.DropdownProperty("urgeHours", "\u50AC\u529E\u5468\u671F(\u5C0F\u65F6)", this.activity.urgeHours, "0\0 24\0 48\0 72\0 96\0 120\0 12\0 6\0 3\0 2\0 1\0 0.5", false, function(value) {
			activityElement.activity.urgeHours = value;
		}));
		propertyList.push(new GraphicsEditor.CheckBoxProperty("urgeHoursAdjustable", "\u50AC\u529E\u5468\u671F\u5141\u8BB8\u8C03\u6574", this.activity.urgeHoursAdjustable, function(value) {
			activityElement.activity.urgeHoursAdjustable = value;
		}));
		return propertyList;
	}
};
WorkflowEditor.Activity.prototype._addParticipant = function() {
	var participantTypes = "\u4E2A\u4EBA|Person," +
						   "\u90E8\u95E8|Department," +
						   "\u89D2\u8272|Role," +
						   "\u90E8\u95E8\u529E\u7406\u4EBA|ParticipantDepartment," +
						   "\u89D2\u8272\u529E\u7406\u4EBA|ParticipantRole," +
						   "\u5206\u7BA1\u9886\u5BFC|Supervisor," +
						   "\u521B\u5EFA\u8005|Creator," +
						   "\u6307\u5B9A\u73AF\u8282\u7684\u529E\u7406\u4EBA|ActivityParticipant," +
						   "\u89D2\u8272\u4E2D\u7279\u5B9A\u4EBA\u5458|RoleFilter," +
						   "\u521B\u5EFA\u8005\u6240\u5728\u90E8\u95E8|DepartmentCreator," +
						   "\u5F53\u524D\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8|DepartmentCurrent," +
						   "\u6307\u5B9A\u73AF\u8282\u7684\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8|DepartmentActivity";
	var programmingParticipantList = this.workflowEditor.workflow.programmingParticipantList;
	for(var i = 0; i < (programmingParticipantList ? programmingParticipantList.length : 0); i++) {
		var programmingParticipant = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', programmingParticipantList[i].uuid);
		participantTypes += "," + programmingParticipant.name + "|ProgrammingParticipant_" + programmingParticipant.id;
	}
	DialogUtils.openListDialog("\u529E\u7406\u4EBA\u7C7B\u578B", null, 330, 410, false, "", "window.activityElement._addUser('{value}', '{title}')", "", participantTypes);
};
WorkflowEditor.Activity.prototype._addQueryUser = function() { 
	var userTypes = "\u4E2A\u4EBA|Person," + 
					"\u90E8\u95E8|Department," + 
					"\u89D2\u8272|Role," +
					"\u5206\u7BA1\u9886\u5BFC|Supervisor," +
					"\u521B\u5EFA\u8005|Creator," +
					"\u521B\u5EFA\u8005\u6240\u5728\u90E8\u95E8|DepartmentCreator," +
					"\u5F53\u524D\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8|DepartmentCurrent," +
					"\u6307\u5B9A\u73AF\u8282\u7684\u529E\u7406\u4EBA|ActivityParticipant," +
					"\u6307\u5B9A\u73AF\u8282\u7684\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8|DepartmentActivity," + 
					"\u521B\u5EFA\u8005\u6240\u5C5E\u89D2\u8272|RoleCreator," + 
					"\u5F53\u524D\u529E\u7406\u4EBA\u6240\u5C5E\u89D2\u8272|RoleCurrent," + 
					"\u6307\u5B9A\u73AF\u8282\u7684\u529E\u7406\u4EBA\u6240\u5C5E\u89D2\u8272|RoleActivity";
	var programmingParticipantList = this.workflowEditor.workflow.programmingParticipantList;
	for(var i = 0; i < (programmingParticipantList ? programmingParticipantList.length : 0); i++) {
		var programmingParticipant = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', programmingParticipantList[i].uuid);
		userTypes += "," + programmingParticipant.name + "|ProgrammingParticipant_" + programmingParticipant.id;
	}
	DialogUtils.openListDialog("\u67E5\u8BE2\u4EBA\u7C7B\u578B", null, 330, 410, false, "",  "window.activityElement._addUser('{value}', '{title}')", "", userTypes);
};
WorkflowEditor.Activity.prototype._addUser = function(type, title) { 
	if("Person"==type) {
		DialogUtils.selectPerson(560, 360, true, "{id},{name|\u7528\u6237\u5217\u8868|100%}", "window.activityElement._doAddUser('Person', '{id}', '{name}')");
	}
	else if("Department"==type) {
		DialogUtils.selectOrg(560, 360, true, '{id},{name|\u90E8\u95E8\u5217\u8868|100%}', "window.activityElement._doAddUser('Department', '{id}', '{name}')");
	}
	else if("Role"==type) {
		DialogUtils.selectRole(560, 360, true, '{id},{name|\u89D2\u8272\u5217\u8868|100%}', "window.activityElement._doAddUser('Role', '{id}', '{name}')");
	}
	else if("ParticipantDepartment"==type) { 
		this._selectParticipantDepartment();
	}
	else if("ParticipantRole"==type) { 
		this._selectParticipantRole();
	}
	else if("Creator"==type) { 
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "\u521B\u5EFA\u8005"});
	}
	else if("DepartmentCreator"==type) { 
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "\u548C\u521B\u5EFA\u8005\u76F8\u540C\u90E8\u95E8\u7684\u4EBA\u5458"});
	}
	else if("DepartmentCurrent"==type) { 
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "\u548C\u5F53\u524D\u529E\u7406\u4EBA\u76F8\u540C\u90E8\u95E8\u7684\u4EBA\u5458"});
	}
	else if("RoleCreator"==type) { 
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "\u548C\u521B\u5EFA\u8005\u89D2\u8272\u76F8\u540C\u7684\u4EBA\u5458"});
	}
	else if("RoleCurrent"==type) { 
		this.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, {title: "\u548C\u5F53\u524D\u529E\u7406\u4EBA\u89D2\u8272\u76F8\u540C\u7684\u4EBA\u5458"});
	}
	else if("ActivityParticipant"==type) { 
		this._selectActivityParticipant();
	}
	else if("RoleFilter"==type) { 
		this._selectRoleFilter();
	}
	else if("Supervisor"==type) { 
		this._selectSupervisor();
	}
	else if("DepartmentActivity"==type || "RoleActivity"==type) { 
		this._selectActivityDepartmentRole(type);
	}
	else if(type.indexOf("ProgrammingParticipant_")==0) { 
		this.listProperty.appendItem(WORKFLOW_RESOURCE_PACKAGE + ".ProgrammingParticipant", {id: type.substring("ProgrammingParticipant_".length), name: title});
	}
};
WorkflowEditor.Activity.prototype._selectParticipantDepartment = function() {
	DialogUtils.selectOrg(560, 360, false, "", "_setParticipantDepartmentMode('{id}', '{name}')");
	window._setParticipantDepartmentMode = function(orgId, orgName) {
		DialogUtils.openListDialog("\u9009\u62E9\u529E\u7406\u4EBA", null, 560, 360, false, "", "_selectPersonOfParticipantDepartment('" + orgId + "', '" + orgName + "', '{value}')", "", "\u90E8\u95E8\u4E2D\u4EFB\u610F\u4E00\u4EBA\u529E\u7406,\u6307\u5B9A\u4EBA\u5458");
	};
	window._selectPersonOfParticipantDepartment = function(orgId, orgName, mode) { 
		if(mode=="\u90E8\u95E8\u4E2D\u4EFB\u610F\u4E00\u4EBA\u529E\u7406") {
			_addParticipantDepartment(orgId, orgName);
		}
		else {
			DialogUtils.selectPerson(560, 360, true, "{id},{name|\u6307\u5B9A\u4EBA\u5458|100%}", "_addParticipantDepartment('" + orgId + "', '" + orgName + "', '{id}', '{name}')", '', '', '', orgId);
		}
	};
	var activityElement = this;
	window._addParticipantDepartment = function(orgId, orgName, personIds, personNames) { 
		var user = {id: orgId, name: orgName};
		if(personIds) {
			user.personIds = personIds; 
			user.personNames = personNames; 
		}
		user.title = user.name + (!personNames ? "(\u4EFB\u610F\u4E00\u4EBA\u529E\u7406)":"(\u7531" + personNames + "\u529E\u7406)");
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".ParticipantDepartment", user);
	};
};
WorkflowEditor.Activity.prototype._selectParticipantRole = function() { 
	DialogUtils.selectRole(560,360, false, "", "_setParticipantRoleMode('{id}', '{name}')");
	window._setParticipantRoleMode = function(roleId, roleName) {
		DialogUtils.openListDialog("\u9009\u62E9\u529E\u7406\u4EBA", null, 560, 360, false, "", "_selectPersonOfParticipantRole('" + roleId + "', '" + roleName + "', '{value}')", "", "\u4EFB\u610F\u4E00\u4EBA\u529E\u7406,\u6307\u5B9A\u4EBA\u5458");
	};
	window._selectPersonOfParticipantRole = function(roleId, roleName, mode) { 
		if(mode=="\u4EFB\u610F\u4E00\u4EBA\u529E\u7406") {
			_addParticipantRole(roleId, roleName);
		}
		else {
			DialogUtils.selectPersonByRole(560, 360, true, "{id},{name|\u6307\u5B9A\u4EBA\u5458|100%}", "_addParticipantRole('" + roleId + "', '" + roleName + "', '{id}', '{name}')");
		}
	};
	var activityElement = this;
	window._addParticipantRole = function(roleId, roleName, personIds, personNames) { 
		var user = {id: roleId, name: roleName};
		if(personIds) {
			user.personIds = personIds; 
			user.personNames = personNames; 
		}
		user.title = user.name + (!personNames ? "(\u4EFB\u610F\u4E00\u4EBA\u529E\u7406)":"(\u7531" + personNames + "\u529E\u7406)");
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".ParticipantRole", user);
	};
};
WorkflowEditor.Activity.prototype._selectRoleFilter = function() { 
	var activityElement = this;
	DialogUtils.selectRole(560,360, false, "", "setRoleFilterMode('{id}', '{name}')");
	window.setRoleFilterMode = function(roleId, roleName) { 
		var itemsText = "\u4E0E\u521B\u5EFA\u8005\u76F8\u540C\u90E8\u95E8\u6216\u5355\u4F4D|creator,\u4E0E\u5F53\u524D\u529E\u7406\u4EBA\u76F8\u540C\u90E8\u95E8\u6216\u5355\u4F4D|current,\u4E0E\u67D0\u73AF\u8282\u529E\u7406\u4EBA\u76F8\u540C\u90E8\u95E8\u6216\u5355\u4F4D|activity,\u6307\u5B9A\u90E8\u95E8\u6216\u5355\u4F4D|department";
		DialogUtils.openListDialog("\u7B5B\u9009\u65B9\u5F0F", null, 560, 360, false, "", "_afterSetRoleFilterMode('" + roleId + "', '" + roleName + "', '{value}')", "", itemsText);
	};
	window._afterSetRoleFilterMode = function(roleId, roleName, mode) {
		if(mode=="activity") { 
			activityElement.workflowEditor.selectActivity("_doAddRoleFilter('" + roleId + "', '" + roleName + "', '" + mode + "', '{value}', '{title}')");
		}
		else if(mode=="department") { 
			DialogUtils.selectOrg(560, 360, false, "","_doAddRoleFilter('" + roleId + "', '" + roleName + "', '" + mode + "', '', '', '{id}', '{name}')");
		}
		else {
			_doAddRoleFilter(roleId, roleName, mode);
		}
	};
	window._doAddRoleFilter = function(roleId, roleName, mode, activityUuid, activityName, departmentId, departmentName) { 
		var user = {id: roleId, name: roleName, filter: mode};
		if(mode=="department") { 
			user.departmentId = departmentId;
			user.departmentName = departmentName;
		}
		else if(mode=="activity") { 
			var activity = ListUtils.findObjectByProperty(activityElement.workflowEditor.jsonObjectPool, "uuid", activityUuid);
			user.activityId = activity.id; 
			user.activity = {uuid: activityUuid};
		}
		var prefix = "";
		if(mode=="creator") {
			prefix = "\u521B\u5EFA\u8005\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D";
		}
		else if(mode=="current") {
			prefix = "\u5F53\u524D\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D";
		}
		else if(mode=="activity") {
			prefix = "\u73AF\u8282" + (!activityName ? "" : "[" + activityName + "]") + "\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D";
		}
		else if(mode=="department") {
			prefix = "\u90E8\u95E8[" + departmentName + "]";
		}
		user.title = prefix + "\u7684[" + roleName + "]"; 
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".RoleFilter", user);
	};
};
WorkflowEditor.Activity.prototype._selectActivityParticipant = function() {
	this.workflowEditor.selectActivity("_addActivityParticipant('{value}', '{title}')"); 
	var activityElement = this;
	window._addActivityParticipant = function(activityUuid, activityName) {
		var user = {activity: {uuid: activityUuid}, title: "\u73AF\u8282[" + activityName + "]\u7684\u529E\u7406\u4EBA"};
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".ActivityParticipant", user);
	}
};
WorkflowEditor.Activity.prototype._selectActivityDepartmentRole = function(type) {
	this.workflowEditor.selectActivity("_addDepartmentRoleActivity('" + type + "', '{value}', '{title}')");
	var activityElement = this;
	window._addDepartmentRoleActivity = function(type, activityUuid, activityName) {
		var activity = ListUtils.findObjectByProperty(activityElement.workflowEditor.jsonObjectPool, "uuid", activityUuid);
		var user = {activityId: activity.id, activity: {uuid: activityUuid}, title: "\u548C\u73AF\u8282[" +  activityName + "]\u7684\u529E\u7406\u4EBA" + ("DepartmentActivity"==type ? "\u76F8\u540C\u90E8\u95E8" : "\u89D2\u8272\u76F8\u540C") + "\u7684\u4EBA\u5458"};
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + "." + type, user);
	};
};
WorkflowEditor.Activity.prototype._selectSupervisor = function() {
	
	var itemsText = "\u521B\u5EFA\u8005\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D\u7684\u5206\u7BA1\u9886\u5BFC|creatorDepartment,\u5F53\u524D\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D\u7684\u5206\u7BA1\u9886\u5BFC|currentDepartment,\u67D0\u73AF\u8282\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D\u7684\u5206\u7BA1\u9886\u5BFC|activityDepartment,\u6307\u5B9A\u90E8\u95E8\u6216\u5355\u4F4D\u7684\u5206\u7BA1\u9886\u5BFC|department,\u521B\u5EFA\u8005\u7684\u5206\u7BA1\u9886\u5BFC|creator,\u5F53\u524D\u529E\u7406\u4EBA\u7684\u5206\u7BA1\u9886\u5BFC|current,\u67D0\u73AF\u8282\u529E\u7406\u4EBA\u7684\u5206\u7BA1\u9886\u5BFC|activity";
	DialogUtils.openListDialog("\u7B5B\u9009\u65B9\u5F0F", null, 560, 360, false, "", "_afterSetSupervisorFilterMode('{value}')", "", itemsText);
	var activityElement = this;
	window._afterSetSupervisorFilterMode = function(mode) {
		if(mode=="activity" || mode=="activityDepartment") { 
			activityElement.workflowEditor.selectActivity("_doAddSupervisor('" + mode + "', '{value}', '{title}')");
		}
		else if(mode=="department") { 
			DialogUtils.selectOrg(560, 360, false, "","_doAddSupervisor('" + mode + "', '', '', '{id}', '{name}')");
		}
		else {
			_doAddSupervisor(mode);
		}
	};
	window._doAddSupervisor = function(mode, activityUuid, activityName, departmentId, departmentName) { 
		var user = {filter: mode}; 
		if(mode=="department") { 
			user.departmentId = departmentId;
			user.departmentName = departmentName;
		}
		else if(mode=="activityDepartment" || mode=="activity") { 
			var activity = ListUtils.findObjectByProperty(activityElement.workflowEditor.jsonObjectPool, "uuid", activityUuid);
			user.activityId = activity.id; 
			user.activity = {uuid: activityUuid};
		}
		var prefix = "";
		if(mode=="creatorDepartment") {
			prefix = "\u521B\u5EFA\u8005\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D";
		}
		else if(mode=="currentDepartment") {
			prefix = "\u5F53\u524D\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D";
		}
		else if(mode=="activityDepartment") {
			prefix = "\u73AF\u8282[" + activityName + "]\u529E\u7406\u4EBA\u6240\u5728\u90E8\u95E8\u6216\u5355\u4F4D";
		}
		else if(mode=="department") {
			prefix = "\u90E8\u95E8[" + departmentName + "]";
		}
		else if(mode=="creator") {
			prefix = "\u521B\u5EFA\u8005";
		}
		else if(mode=="current") {
			prefix = "\u5F53\u524D\u529E\u7406\u4EBA";
		}
		else if(mode=="activity") {
			prefix = "\u73AF\u8282[" + activityName + "]\u529E\u7406\u4EBA";
		}
		user.title = prefix + "\u7684\u5206\u7BA1\u9886\u5BFC"; 
		activityElement.listProperty.appendItem(WORKFLOW_USERMODEL_PACKAGE + ".Supervisor", user);
	};
};
WorkflowEditor.Activity.prototype._doAddUser = function(userType, ids, names) {
	ids = ids.split(",");
	names = names.split(",");
	for(var i=0; i<ids.length; i++) {
		this.listProperty.appendItem(USERMODEL_PACKAGE + '.' + userType, {id: ids[i], name: names[i]});
	}
};
WorkflowEditor.Activity.prototype._generateSubFormItemsText = function() {
	var itemsText = "";
	for(var i = 0; i < (this.workflowEditor.workflow.subFormList ? this.workflowEditor.workflow.subFormList.length : 0); i++) {
		var subForm = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, "uuid", this.workflowEditor.workflow.subFormList[i].uuid);
		itemsText += (itemsText=="" ? "" : "\0") + subForm.name + "|" + subForm.uuid;
	}
	return itemsText;
};
WorkflowEditor.Activity.prototype._createSubFlow = function(parentActivityId) {
	if(document.getElementsByName("act")[0].value.indexOf("create")!=-1) {
		alert("\u5F53\u524D\u6D41\u7A0B\u5C1A\u672A\u4FDD\u5B58\uFF0C\u4E0D\u5141\u8BB8\u521B\u5EFA\u5B50\u6D41\u7A0B\uFF01");
		return;
	}
	if(!this.workflowEditor.modified || confirm("\u914D\u7F6E\u5DF2\u4FEE\u6539,\u5982\u679C\u7EE7\u7EED,\u6240\u505A\u7684\u914D\u7F6E\u5C06\u4E22\u5931,\u662F\u5426\u7EE7\u7EED?")) {
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
WorkflowEditor.Activity.prototype._loadSubFlow = function(subFlowProcessId) {
	if(!this.workflowEditor.modified || confirm("\u914D\u7F6E\u5DF2\u4FEE\u6539,\u5982\u679C\u7EE7\u7EED,\u6240\u505A\u7684\u914D\u7F6E\u5C06\u4E22\u5931,\u662F\u5426\u7EE7\u7EED?")) {
		var url = RequestUtils.getContextPath() + "/workflow/workflowConfigure.shtml";
		url += "?act=editSubFlow";
		url += "&workflowId=" + document.getElementsByName('workflowId')[0].value;
		url += "&subFlowProcessId=" + subFlowProcessId;
		url += "&returnURL=" + StringUtils.utf8Encode(top.location.href);
		url += "&ticket=" + document.getElementsByName("ticket")[0].value;
		window.top.location = url;
	}
};

WorkflowEditor.Begin = function(workflowEditor, begin, surface) {
	GraphicsEditor.RoundNode.call(this, workflowEditor, begin, surface, null, 3 * workflowEditor.gridSize, 3 * workflowEditor.gridSize, 1);
	this.workflowEditor = workflowEditor;
	this.activity = begin;
};
WorkflowEditor.Begin.prototype = new GraphicsEditor.RoundNode();
WorkflowEditor.Begin.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: "\u5F00\u59CB"};
};
WorkflowEditor.Begin.prototype.getPropertyList = function(propertyName) {
	
};

WorkflowEditor.Decision = function(workflowEditor, decision, surface) {
	GraphicsEditor.RhombusNode.call(this, workflowEditor, decision, surface, decision.name, 7 * workflowEditor.gridSize, 5 * workflowEditor.gridSize);
	this.workflowEditor = workflowEditor;
	this.activity = decision;
};
WorkflowEditor.Decision.prototype = new GraphicsEditor.RhombusNode();
WorkflowEditor.Decision.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: this.activity.name};
};
WorkflowEditor.Decision.prototype.getPropertyList = function(propertyName) {
	var decisionElement = this;
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.activity.id, false));
	propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", this.activity.name, false, function(value) {
		decisionElement.activity.name = value;
		decisionElement.setTitle(decisionElement.activity.name);
	}));
	
	
	var applications = null;
	var itemsText = '';
	var doGetItemsText = function() {
		if(applications!=null) {
			return itemsText;
		}
		applications = [];
		var applicationList = decisionElement.workflowEditor.workflow.applicationList;
		for(var i=0; i < (applicationList ? applicationList.length : 0); i++) {
			var application = ListUtils.findObjectByProperty(decisionElement.workflowEditor.jsonObjectPool, 'uuid', applicationList[i].uuid);
			if(application.type=="decision") {
				applications.push(application);
				itemsText += (itemsText=='' ? '' : '\0') + application.name + '|' + application.id;
			}
		}
		return itemsText;
	};
	var decisionApplication = this.activity.decisionApplication;
	if(decisionApplication) {
		decisionApplication = ListUtils.findObjectByProperty(decisionElement.workflowEditor.jsonObjectPool, 'uuid', decisionApplication.uuid);
	}
	propertyList.push(new GraphicsEditor.DropdownProperty("decisionApplication", "\u51B3\u7B56\u4F9D\u636E", decisionApplication ? decisionApplication.id : null, doGetItemsText, true, function(value) {
		var application = ListUtils.findObjectByProperty(applications, "id", value);
		decisionElement.activity.decisionApplication = {uuid: application.uuid};
		if(decisionElement.activity.name.indexOf("\u51B3\u7B56")==0) {
			decisionElement.activity.name = application.name;
			decisionElement.setTitle(application.name);
		}
	}));
	return propertyList;
};

WorkflowEditor.End = function(workflowEditor, activity, surface) {
	GraphicsEditor.RoundNode.call(this, workflowEditor, activity, surface, null, 3 * workflowEditor.gridSize, 3 * workflowEditor.gridSize, 2);
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.End.prototype = new GraphicsEditor.RoundNode();
WorkflowEditor.End.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: "\u7ED3\u675F"};
};
WorkflowEditor.End.prototype.getPropertyList = function(propertyName) {
	
};

WorkflowEditor.Join = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 5;
	this.rightConnectPointNumber = 3;
	this.topConnectPointNumber = 0;
	this.bottomConnectPointNumber = 0;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 1 * workflowEditor.gridSize, 9 * workflowEditor.gridSize, 0);
	this.fillColor = '#808080';
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Join.prototype = new GraphicsEditor.RectNode();
WorkflowEditor.Join.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: '\u540C\u65F6\u5B8C\u6210'};
};
WorkflowEditor.Join.prototype.getPropertyList = function(propertyName) {
	
};

WorkflowEditor.Procedure = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 3;
	this.rightConnectPointNumber = 3;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 7 * workflowEditor.gridSize, 5 * workflowEditor.gridSize, 6);
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Procedure.prototype = new GraphicsEditor.RectNode();
WorkflowEditor.Procedure.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: this.activity.name};
};
WorkflowEditor.Procedure.prototype.getPropertyList = function(propertyName) {
	var procedureElement = this;
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.activity.id, false));
	propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", this.activity.name, false, function(value) {
		procedureElement.activity.name = value;
		procedureElement.setTitle(procedureElement.activity.name);
	}));
	
	var applications = null;
	var itemsText = '';
	var doGetItemsText = function() {
		if(applications!=null) {
			return itemsText;
		}
		applications = [];
		var applicationList = procedureElement.workflowEditor.workflow.applicationList;
		for(var i=0; i < (applicationList ? applicationList.length : 0); i++) {
			var application = ListUtils.findObjectByProperty(procedureElement.workflowEditor.jsonObjectPool, 'uuid', applicationList[i].uuid);
			if(application.type=="procedure") {
				applications.push(application);
				itemsText += (itemsText=='' ? '' : '\0') + application.name + '|' + application.id;
			}
		}
		return itemsText;
	};
	var procedureApplication = this.activity.procedureApplication;
	if(procedureApplication) {
		procedureApplication = ListUtils.findObjectByProperty(procedureElement.workflowEditor.jsonObjectPool, 'uuid', procedureApplication.uuid);
	}
	propertyList.push(new GraphicsEditor.DropdownProperty("procedureApplication", "\u6267\u884C\u8FC7\u7A0B", procedureApplication ? procedureApplication.id : null, doGetItemsText, true, function(value) {
		var application = ListUtils.findObjectByProperty(applications, "id", value);
		procedureElement.activity.procedureApplication = {uuid: application.uuid};
		if(procedureElement.activity.name.indexOf("\u8FC7\u7A0B")==0) {
			procedureElement.activity.name = application.name;
			procedureElement.setTitle(application.name);
		}
	}));
	return propertyList;
};

WorkflowEditor.Split = function(workflowEditor, activity, surface) {
	this.leftConnectPointNumber = 3;
	this.rightConnectPointNumber = 5;
	this.topConnectPointNumber = 0;
	this.bottomConnectPointNumber = 0;
	GraphicsEditor.RectNode.call(this, workflowEditor, activity, surface, activity.name, 1 * workflowEditor.gridSize, 9 * workflowEditor.gridSize, 0);
	this.fillColor = '#D3D3D3';
	this.workflowEditor = workflowEditor;
	this.activity = activity;
};
WorkflowEditor.Split.prototype = new GraphicsEditor.RectNode();
WorkflowEditor.Split.prototype.getPropertyTree = function() {
	return {id: this.activity.id, label: '\u540C\u65F6\u542F\u52A8'};
};
WorkflowEditor.Split.prototype.getPropertyList = function(propertyName) {
	
};

WorkflowEditor.Transition = function(workflowEditor, transition, surface) {
	GraphicsEditor.Line.call(this, workflowEditor, transition, surface, true, 3, transition.title);
	this.workflowEditor = workflowEditor;
};
WorkflowEditor.Transition.prototype = new GraphicsEditor.Line();
WorkflowEditor.Transition.prototype.getPropertyTree = function() {
	var tree = {id: this.transition.id, label: (this.transition.name ? this.transition.name : '\u8FDE\u63A5'), childNodes: []};
	tree.childNodes.push({id: "conditionDetailList", label: "\u6761\u4EF6\u5217\u8868"});
	return tree;
};
WorkflowEditor.Transition.prototype.getPropertyList = function(propertyName) {
	var transitionElement = this;
	if("conditionDetailList"==propertyName) {
		this.listProperty = new GraphicsEditor.ListProperty(propertyName, "\u6761\u4EF6\u5217\u8868", this.transition.conditionDetailList, this.workflowEditor.jsonObjectPool, false, null, null, function(value) {
			transitionElement.transition.conditionDetailList = value;
			if(value && value.length>0 && !transitionElement.transition.conditionType) {
				transitionElement.transition.conditionType = "CONDITION";
			}
			transitionElement._setTitle();
		});
		//\u6DFB\u52A0"\u6DFB\u52A0\u6761\u4EF6"\u6309\u94AE
		this.listProperty.addListButton("\u6DFB\u52A0\u6761\u4EF6", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/workflow/configure/condition.shtml", 400, 200, null, transitionElement);
		});
		return [this.listProperty];
	}
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.transition.id, true));
	propertyList.push(new GraphicsEditor.TextProperty("name", "\u540D\u79F0", this.transition.name, false, function(value) {
		transitionElement.transition.name = value;
		transitionElement._setTitle();
	}));
	propertyList.push(new GraphicsEditor.DropdownProperty("conditionType", "\u8DF3\u8F6C\u6761\u4EF6", this.transition.conditionType, "\u81EA\u5B9A\u4E49|CONDITION\0\u5176\u4ED6|OTHERWISE\0\u629B\u51FA\u5F02\u5E38\u65F6\u8DF3\u8F6C|EXCEPTION\0\u65E0\u6761\u4EF6\u8DF3\u8F6C|NONE", true, function(value) {
		transitionElement.transition.conditionType = value=='NONE' ? null : value;
		transitionElement._setTitle();
	}));
	var doGetItemsText = function() {
		var itemsText = '\u4E0D\u7B7E\u540D|none';
		var fields = transitionElement.workflowEditor.workflow.dataFieldList;
		for(var i = 0; i < (fields ? fields.length : 0); i++) {
			var field = ListUtils.findObjectByProperty(transitionElement.workflowEditor.jsonObjectPool, "uuid", fields[i].uuid);
			if(field.sign) {
				itemsText += '\0' + field.name + '|' + field.uuid;
			}
		}
	    return itemsText;
    };
    var signField = this.transition.signField ? ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.transition.signField.uuid) : null;
	propertyList.push(new GraphicsEditor.DropdownProperty("signField", "\u7B7E\u540D\u5B57\u6BB5", signField ? signField.name : null, doGetItemsText, true, function(value) {
		transitionElement.transition.signField = value=='none' ? null : {uuid: value};
	}));
	
	return propertyList;
};
WorkflowEditor.Transition.prototype._setTitle = function() {
	var title = this.transition.name;
	if(!title || title=='') {
		if("OTHERWISE"==this.transition.conditionType) {
			title = "\u5176\u4ED6";
		}
		else if("EXCEPTION"==this.transition.conditionType) {
			title = "\u5F02\u5E38";
		}
		else if("CONDITION"==this.transition.conditionType && this.transition.conditionDetailList && this.transition.conditionDetailList.length > 0) {
			var conditionDetail = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.transition.conditionDetailList[0].uuid);
			title = conditionDetail.title.substring(2) + (this.transition.conditionDetailList.length > 1 ? "..." : "");
		}
	}
	this.transition.title = title;
	this.setTitle(title);
};
WorkflowEditor.Transition.prototype._listDataFields = function() {
    var dataFields = [];
    var formalParameterList = null;
    if(this.fromElement instanceof WorkflowEditor.Procedure) { 
        if(this.fromElement.activity.procedureApplication) {
            formalParameterList = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.fromElement.activity.procedureApplication.uuid).formalParameterList;
        }
    }
    else if(this.fromElement instanceof WorkflowEditor.Decision) { 
        if(this.fromElement.activity.decisionApplication) {
            formalParameterList = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.fromElement.activity.decisionApplication.uuid).formalParameterList;
        }
    }
    for(var i = 0; i < (formalParameterList ? formalParameterList.length : 0); i++) {
        var formalParameter = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', formalParameterList[i].uuid);
        if(!formalParameter.mode=="IN") { 
            dataFields.push(formalParameter);
        }
    }
    for(var i = 0; i < (this.workflowEditor.workflow.dataFieldList ? this.workflowEditor.workflow.dataFieldList.length : 0); i++) {
        var dataField = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.workflowEditor.workflow.dataFieldList[i].uuid);
    	dataFields.push(dataField);
    }
	return dataFields;
};

