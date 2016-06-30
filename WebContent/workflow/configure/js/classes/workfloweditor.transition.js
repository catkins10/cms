//连接
WorkflowEditor.Transition = function(workflowEditor, transition, surface) {
	GraphicsEditor.Line.call(this, workflowEditor, transition, surface, true, 3, transition.title);
	this.workflowEditor = workflowEditor;
};
WorkflowEditor.Transition.prototype = new GraphicsEditor.Line();
//抽象方法(由继承者实现):获取属性目录树
WorkflowEditor.Transition.prototype.getPropertyTree = function() {
	var tree = {id: this.transition.id, label: (this.transition.name ? this.transition.name : '连接'), childNodes: []};
	tree.childNodes.push({id: "conditionDetailList", label: "条件列表"});
	return tree;
};
//抽象方法(由继承者实现):获取属性列表
WorkflowEditor.Transition.prototype.getPropertyList = function(propertyName) {
	var transitionElement = this;
	if("conditionDetailList"==propertyName) {
		this.listProperty = new GraphicsEditor.ListProperty(propertyName, "条件列表", this.transition.conditionDetailList, this.workflowEditor.jsonObjectPool, false, null, null, function(value) {
			transitionElement.transition.conditionDetailList = value;
			if(value && value.length>0 && !transitionElement.transition.conditionType) {
				transitionElement.transition.conditionType = "CONDITION";
			}
			transitionElement._setTitle();
		});
		//添加"添加条件"按钮
		this.listProperty.addListButton("添加条件", "/jeaf/graphicseditor/icons/new.gif", function(buttonElement) {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/workflow/configure/condition.shtml", 400, 200, null, transitionElement);
		});
		return [this.listProperty];
	}
	var propertyList = [];
	propertyList.push(new GraphicsEditor.TextProperty("id", "ID", this.transition.id, true));
	propertyList.push(new GraphicsEditor.TextProperty("name", "名称", this.transition.name, false, function(value) {
		transitionElement.transition.name = value;
		transitionElement._setTitle();
	}));
	propertyList.push(new GraphicsEditor.DropdownProperty("conditionType", "跳转条件", this.transition.conditionType, "自定义|CONDITION\0其他|OTHERWISE\0抛出异常时跳转|EXCEPTION\0无条件跳转|NONE", true, function(value) {
		transitionElement.transition.conditionType = value=='NONE' ? null : value;
		transitionElement._setTitle();
	}));
	var doGetItemsText = function() {
		var itemsText = '不签名|none';
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
	propertyList.push(new GraphicsEditor.DropdownProperty("signField", "签名字段", signField ? signField.name : null, doGetItemsText, true, function(value) {
		transitionElement.transition.signField = value=='none' ? null : {uuid: value};
	}));
	/*String mode = getWorkflowTransition().getSignMode();
	if("MANUSCRIPT".equals(mode)) {
		mode = "手写";
	}
	else {
		mode = "标准";
	}
	//propertyDatail.addProperty("签名方式", "signMode", mode, "SELECTONLY");
	*/
	return propertyList;
};
//设置标题
WorkflowEditor.Transition.prototype._setTitle = function() {
	var title = this.transition.name;
	if(!title || title=='') {
		if("OTHERWISE"==this.transition.conditionType) {
			title = "其他";
		}
		else if("EXCEPTION"==this.transition.conditionType) {
			title = "异常";
		}
		else if("CONDITION"==this.transition.conditionType && this.transition.conditionDetailList && this.transition.conditionDetailList.length > 0) {
			var conditionDetail = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.transition.conditionDetailList[0].uuid);
			title = conditionDetail.title.substring(2) + (this.transition.conditionDetailList.length > 1 ? "..." : "");
		}
	}
	this.transition.title = title;
	this.setTitle(title);
};
//获取字段列表
WorkflowEditor.Transition.prototype._listDataFields = function() {
    var dataFields = [];
    var formalParameterList = null;
    if(this.fromElement instanceof WorkflowEditor.Procedure) { //过程执行结果
        if(this.fromElement.activity.procedureApplication) {
            formalParameterList = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.fromElement.activity.procedureApplication.uuid).formalParameterList;
        }
    }
    else if(this.fromElement instanceof WorkflowEditor.Decision) { //判断结果
        if(this.fromElement.activity.decisionApplication) {
            formalParameterList = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.fromElement.activity.decisionApplication.uuid).formalParameterList;
        }
    }
    for(var i = 0; i < (formalParameterList ? formalParameterList.length : 0); i++) {
        var formalParameter = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', formalParameterList[i].uuid);
        if(!formalParameter.mode=="IN") { //程序执行输出
            dataFields.push(formalParameter);
        }
    }
    for(var i = 0; i < (this.workflowEditor.workflow.dataFieldList ? this.workflowEditor.workflow.dataFieldList.length : 0); i++) {
        var dataField = ListUtils.findObjectByProperty(this.workflowEditor.jsonObjectPool, 'uuid', this.workflowEditor.workflow.dataFieldList[i].uuid);
    	dataFields.push(dataField);
    }
	return dataFields;
};