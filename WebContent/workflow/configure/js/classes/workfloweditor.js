//EAI编辑器,从GraphicsEditor继承
WorkflowEditor = function(canvasParentElement, drawBarContainer, propertyTreeContainer, propertyDetailContainer, workflowJson) {
	this.workflowJson = workflowJson;
	this.jsonObjectPool = workflowJson.objects;
	this.workflow = ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", workflowJson.uuid); //流程配置
	this.workflowProcess =  ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", this.workflow.workflowProcessList[0].uuid); //流程
	this.workflowExtend = ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", this.workflow.workflowPackageExtend.uuid); //扩展配置
	this.workflowProcessExtend = ListUtils.findObjectByProperty(this.jsonObjectPool, "uuid", this.workflowExtend.workflowProcessExtendList[0].uuid); //流程配置扩展
	var drawBarButtons = [{elementType:'Begin', name:'开始', iconUrl:'/workflow/configure/icons/begin.gif'},
						  {elementType:'End', name:'结束', iconUrl:'/workflow/configure/icons/end.gif'},
						  {elementType:'Activity', name:'环节', iconUrl:'/workflow/configure/icons/element.gif'},
						  {elementType:'Decision', name:'决策', iconUrl:'/workflow/configure/icons/rhombus.gif'},
						  {elementType:'Procedure', name:'过程', iconUrl:'/workflow/configure/icons/roundRect.gif'},
						  {elementType:'Split', name:'同时启动', iconUrl:'/workflow/configure/icons/split.gif'},
						  {elementType:'Join', name:'同时完成', iconUrl:'/workflow/configure/icons/join.gif'},
						  {elementType:'Line', name:'连接', iconUrl:'/workflow/configure/icons/line.gif'},
						  {elementType:'BrokenLine', name:'连接(折线)', iconUrl:'/workflow/configure/icons/brokenLine.gif'}];
	GraphicsEditor.call(this, canvasParentElement, drawBarContainer, drawBarButtons, propertyTreeContainer, propertyDetailContainer);
};
WorkflowEditor.prototype = new GraphicsEditor();
//抽象方法(由继承者实现):初始化
WorkflowEditor.prototype.init = function() {
	//加载节点
    for(var i = 0; i < (this.workflowProcess.activityList ? this.workflowProcess.activityList.length : 0); i++) {
    	var activity = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.workflowProcess.activityList[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, activity.id);
    	var className = activity.uuid.substring(activity.uuid.lastIndexOf('.') + 1, activity.uuid.lastIndexOf('@'));
    	eval('this.addElement(new WorkflowEditor.' + className + '(this, activity, surface));');
	}
	//加载连接
    for(var i = 0; i < (this.workflowProcess.transitionList ? this.workflowProcess.transitionList.length : 0); i++) {
        var transition = ListUtils.findObjectByProperty(this.jsonObjectPool, 'uuid', this.workflowProcess.transitionList[i].uuid);
        var surface = this.getSurface(this.jsonObjectPool, transition.id);
        this.addElement(new WorkflowEditor.Transition(this, transition, surface));
    }
};
//抽象方法(由继承者实现):创建元素
WorkflowEditor.prototype.createElement = function(elementType) {
	if(!this.workflowProcess.activityList) {
		this.workflowProcess.activityList = [];
	}
	if(!this.workflowProcess.transitionList) {
		this.workflowProcess.transitionList = [];
	}
	if('Begin'==elementType) { //开始
		var begin = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Begin', {id: "begin-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: begin.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', begin.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Begin(this, begin, surface);
	}
	else if('End'==elementType) { //结束
		var end = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.End', {id: "end-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: end.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', end.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.End(this, end, surface);
	}
	else if('Activity'==elementType) { //环节
		var activity = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Activity', {id: "activity-" + (this.workflowProcessExtend.nextElementId++)});
		var activities = ListUtils.findObjectsByType(this.elements, 'WorkflowEditor.Activity', 0);
		activity.name = '环节' + (activities ? activities.length + 1 : 1);
		this.workflowProcess.activityList.push({uuid: activity.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', activity.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Activity(this, activity, surface);
	}
	else if('Decision'==elementType) { //决策
		var decision = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Decision', {id: "decision-" + (this.workflowProcessExtend.nextElementId++)});
		var activities = ListUtils.findObjectsByType(this.elements, 'WorkflowEditor.Decision', 0);
		decision.name = '决策' + (activities ? activities.length + 1 : 1);
		this.workflowProcess.activityList.push({uuid: decision.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', decision.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Decision(this, decision, surface);
	}
	else if('Procedure'==elementType) { //过程
		var procedure = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Procedure', {id: "procedure-" + (this.workflowProcessExtend.nextElementId++)});
		var activities = ListUtils.findObjectsByType(this.elements, 'WorkflowEditor.Procedure', 0);
		procedure.name = '过程' + (activities ? activities.length + 1 : 1);
		this.workflowProcess.activityList.push({uuid: procedure.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', procedure.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Procedure(this, procedure, surface);
	}
	else if('Split'==elementType) { //同时启动
		var split = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Split', {id: "split-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: split.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', split.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Split(this, split, surface);
	}
	else if('Join'==elementType) { //同时完成
		var join = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Join', {id: "join-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.activityList.push({uuid: join.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend', join.id);
		this.workflowProcessExtend.elements.push({uuid: surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Join(this, join, surface);
	}
	else if('Line'==elementType || 'BrokenLine'==elementType) { //连接
		//创建应用对象
		var transition = JsonUtils.addJsonObject(this.jsonObjectPool, 'com.yuanluesoft.workflow.client.model.definition.Transition', {id: "transition-" + (this.workflowProcessExtend.nextElementId++)});
		this.workflowProcess.transitionList.push({uuid:transition.uuid});
		//添加外观对象
		var surface = this.addSurface(this.jsonObjectPool, 'com.yuanluesoft.jeaf.graphicseditor.model.extend.' + elementType, transition.id);
		this.workflowProcessExtend.elements.push({uuid:surface.uuid});
		//创建图形对象
		return new WorkflowEditor.Transition(this, transition, surface);
	}
};
//抽象方法(由继承者实现):删除元素
WorkflowEditor.prototype.removeElement = function(element) {
	if(element instanceof WorkflowEditor.Transition) { //连接
	    ListUtils.removeObject(this.jsonObjectPool, element.transition);
		ListUtils.removeObjectByProperty(this.workflowProcess.transitionList, 'uuid', element.transition.uuid);
	}
	else { //节点
		ListUtils.removeObject(this.jsonObjectPool, element.activity);
   		ListUtils.removeObjectByProperty(this.workflowProcess.activityList, 'uuid', element.activity.uuid);
	}
	//删除外观对象
	ListUtils.removeObject(this.jsonObjectPool, element.surface);
	ListUtils.removeObjectByProperty(this.workflowProcessExtend.elements, 'uuid', element.surface.uuid);
};
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.prototype.getPropertyTree = function() {
	var tree = {id: this.workflow.id, label: this.workflowProcess.name, childNodes: []};
	tree.childNodes.push({id: "modifyHistory", label: "修改记录"}); //修改记录
	return tree;
};
//抽象方法(由继承者实现):获取列表
WorkflowEditor.prototype.getPropertyList = function(propertyName) {
	var workflowEditor = this;
	if(propertyName=="modifyHistory") { //修改记录
       	return [new GraphicsEditor.ListProperty(propertyName, "修改记录", this.workflowProcessExtend.modifyHistory, this.jsonObjectPool, true)];
	}
	//获取公共属性
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.workflow.id, true));
	propertyList.push(new GraphicsEditor.TextProperty("name", "名称", this.workflowProcess.name, false, function(value) {
		workflowEditor.workflowProcess.name = value;
		if("PUBLIC"==workflowEditor.workflowProcess.accessLevel) {
			workflowEditor.workflow.name = value;
		}
		workflowEditor.showPropertyTree();
	}));
	propertyList.push(new GraphicsEditor.TextProperty("creator", "创建人", this.workflowProcessExtend.creator, true));
	propertyList.push(new GraphicsEditor.TextProperty("createDate", "创建时间", this.workflowProcessExtend.createDate, true));
	propertyList.push(new GraphicsEditor.TextProperty("description", "描述", this.workflowProcess.description, false, function(value) {
		workflowEditor.workflowProcess.description = value;
	}));
	return propertyList;
};
//保存
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
//校验,无异常时返回true
WorkflowEditor.prototype._validate = function() {
	if(!this.workflowProcess.activityList || this.workflowProcess.activityList.length==0) {
		return {error: "未创建任何流程元素", elements: null};
	}
	if(!this.workflowProcess.transitionList || this.workflowProcess.transitionList.length==0) {
		return {error: "未创建任何连接", elements: null};
	}
	//校验连接
	var validateError = this.validateLines(false);
	if(validateError) {
		return validateError;
	}
	//校验元素
	var containBegin = false;
	var containEnd = false;
	for(var i = 0; i < this.elements.length; i++) {
		var activity = this.elements[i];
		if(!(activity instanceof GraphicsEditor.Node)) {
			continue;
		}
		if(activity instanceof WorkflowEditor.Begin) {
			if(activity.exitLines.length==0) {
				return {error: "出口不能为空", elements: [activity]};		
			}
			if(activity.enterLines.length>0) {
				return {error: "不允许创建到开始元素的连接", elements: activity.enterLines};
			}
			containBegin = true;
		}
		else if(activity instanceof WorkflowEditor.End) {
			if(activity.enterLines.length==0) {
				return {error: "入口不能为空", elements: [activity]};		
			}
			if(activity.exitLines.length>0) {
				return {error: "不允许创建从结束元素到其他流程元素的连接", elements: activity.exitLines};
			}
			containEnd = true;
		}
		else {
			if(activity.enterLines.length==0) {
				return {error: "入口不能为空", elements: [activity]};			
			}
			if(activity.exitLines.length==0) {
				return {error: "出口不能为空", elements: [activity]};			
			}
		}
	}
	if(!containBegin) {
		return {error: "尚未创建开始元素", elements: null};
	}
	if(!containEnd) {
		return {error: "尚未创建结束元素", elements: null};
	}
};
//流程测试
WorkflowEditor.prototype.workflowTest = function() {
	if(this.modified) {
		alert("配置已修改，请先保存！")
		return;
	}
	var testURL = document.getElementsByName("testURL")[0].value;
	if(testURL=="") {
		return;
	}
	DialogUtils.selectPerson(560, 360, false, "", "selectWorkflowTestEntry('{id}')"); //选择测试用户
	var workflowEditor = this;
	window.selectWorkflowTestEntry = function(testUserId) { //选择入口环节
		workflowEditor.selectActivity("enterWorkflowTest('" + testUserId + "', '{value}')");
	};
	window.enterWorkflowTest = function(testUserId, activityUuid) { //打开测试窗口
		var workflowId = document.getElementsByName("workflowId")[0].value;
		var activity = ListUtils.findObjectByProperty(workflowEditor.jsonObjectPool, 'uuid', activityUuid);
		PageUtils.openurl(testURL + (testURL.indexOf("?")==-1 ? "?" : "&") + "workflowId=" + workflowId + "&activityId=" + activity.id + "&workflowTest=true&testUserId=" + testUserId, "mode=fullscreen", "workflowTest");
	};
};
//复制流程
WorkflowEditor.prototype.copyWorkflow = function() {
	document.cookie = 'copiedWorkflowId=' + document.getElementsByName("workflowId")[0].value + ';path=/'; //复制的流程ID
	document.cookie = 'copiedSubFlowProcessId=' + document.getElementsByName("subFlowProcessId")[0].value + ';path=/'; //复制的子流程ID
};
//粘帖流程
WorkflowEditor.prototype.pasteWorkflow = function() {
	if(!confirm('粘帖后，当前流程配置会被覆盖，是否确定？')) {
		return;
	}
	this.saveWorkflow(true);
};
//删除流程
WorkflowEditor.prototype.deleteWorkflow = function() {
	if(confirm("删除后将不能再恢复，是否确定要删除？")) {
		FormUtils.doAction('deleteWorkflowConfigure');
	}
};
//返回
WorkflowEditor.prototype.back = function() {
	if(this.modified && !confirm("配置已修改，是否继续？")) {
		return;
	}
	var returnURL = document.getElementsByName("returnURL")[0].value;
	if(!returnURL || returnURL=="") {
		window.close();
		return;
	}
	window.location = returnURL;
};
//获取枚举值列表
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
//选择流程环节
WorkflowEditor.prototype.selectActivity = function(script) {
	var itemsText = "";
	var activities = this.listActivities(false);
	for(var i = 0; i < activities.length; i++) {
		itemsText += (itemsText=="" ? "" : ",") + activities[i].name + "|" + activities[i].uuid;
	}
	DialogUtils.openListDialog("选择流程环节", null, 560, 360, false, "", script, "", itemsText);
};
//选择流程环节
WorkflowEditor.prototype.listActivities = function(forReverseOrUndo) {
	var activities = [];
	if(forReverseOrUndo) {
		var activityAll = ListUtils.findObjectByProperty(this.jsonObjectPool, 'id', "activity-all");
		if(!activityAll) {
			activityAll = JsonUtils.addJsonObject(this.jsonObjectPool, WORKFLOW_DEFINITION_PACKAGE + ".Activity", {id: "activity-all", name: "[全部]"});
		}
		var activityPrevious = ListUtils.findObjectByProperty(this.jsonObjectPool, 'id', "activity-previous");
		if(!activityPrevious) {
			activityPrevious = JsonUtils.addJsonObject(this.jsonObjectPool, WORKFLOW_DEFINITION_PACKAGE + ".Activity", {id: "activity-previous", name: "[上一环节]"});
		}
		var activityCreator = ListUtils.findObjectByProperty(this.jsonObjectPool, 'id', "activity-creator");
		if(!activityCreator) {
			activityCreator = JsonUtils.addJsonObject(this.jsonObjectPool, WORKFLOW_DEFINITION_PACKAGE + ".Activity", {id: "activity-creator", name: "[创建者]"});
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