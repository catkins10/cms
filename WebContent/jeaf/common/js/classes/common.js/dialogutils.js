DialogUtils = function() {

};
//打开对话框,onDialogOpened = function(dialogBodyFrame), onDialogBodyLoad = function(dialogBodyFrame), onDialogClose = function(dialogBodyFrame);;
DialogUtils.openDialog = function(dialogUrl, width, height, dialogTitle, dialogArguments, onDialogLoad, onDialogBodyLoad, onDialogClose) {
	var topWindow = PageUtils.getTopWindow();
	var clientWidth = DomUtils.getClientWidth(topWindow.document);
	var clientHeight = DomUtils.getClientHeight(topWindow.document);
	if(("" + width).lastIndexOf('%')!=-1) {
		width = clientWidth * Number(width.substring(0, width.length - 1)) / 100;
	}
	if(("" + height).lastIndexOf('%')!=-1) {
		height = clientHeight * Number(height.substring(0, height.length - 1)) / 100;
	}
	
	var cover = PageUtils.createCover(topWindow, 6);
	var top  = Math.max((cover.offsetHeight - height - 20) / 2, 0);
	var left = Math.max((cover.offsetWidth - width - 20)  / 2, 0);
	
	//创建iframe,加载dialog.html
	var dialog = topWindow.document.createElement('iframe');
	dialog.dialogUrl = dialogUrl;
	if(onDialogLoad) {
		dialog.onDialogLoad = onDialogLoad;
	}
	if(onDialogBodyLoad) {
		dialog.onDialogBodyLoad = onDialogBodyLoad;
	}
	if(onDialogClose) {
		dialog.onDialogClose = onDialogClose;
	}
	dialog.frameBorder = 0 ;
	dialog.allowTransparency = true;
	
	var siteId = StringUtils.getPropertyValue(location.href, "siteId");
    if(!siteId || siteId=='') {
       siteId = DomUtils.getMetaContent(document, "siteIdMeta");
    }
	if((!siteId || siteId=='') && document.getElementsByName("siteId")[0]) {
       siteId = document.getElementsByName("siteId")[0].value;
    }
	var url = RequestUtils.getContextPath() + "/jeaf/dialog/dialog.shtml";
  	url += siteId && siteId!="" && siteId!="0" ? '?siteId=' + siteId : ''; 
    url += (dialogTitle ? (url.indexOf('?')==-1 ? '?' : '&') + "dialogTitle=" + StringUtils.utf8Encode(dialogTitle) : "");
	dialog.src = url;
	dialog.style.cssText = 'position: absolute; z-index:' + (Number(cover.style.zIndex) + 1) + '; left:' + left + 'px; top:' + top + 'px; width:' + width + 'px; height:' + height + 'px; visibility: hidden; box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.3)';
	dialog.opener = window;
	dialog.id = "dialog";
	dialog.dialogArguments = dialogArguments;
	cover.appendChild(dialog);
};
DialogUtils.closeDialog = function() { //关闭对话框
	if(!window.frameElement) { //使用window.open方式打开
		window.close();
		return;
	}
	var win = PageUtils.getTopWindow();
	var dialogFrame = DialogUtils.getDialogFrame();
	if(dialogFrame.onDialogClose) {
		dialogFrame.onDialogClose.call(null, dialogFrame.contentWindow.document.getElementById("dialogBody"));
	}
	PageUtils.destoryCover(win, dialogFrame.parentNode);
	//获取焦点
	try {
		var range = DomSelection.createRange(win.document, win.document.body);
		range.collapse(true);
		DomSelection.selectRange(win, range);
	}
	catch(e) {
	
	}
};
//调整优先级
DialogUtils.adjustPriority = function(applicationName, viewName, title, width, height, parameter) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/adjustPriority.shtml";
   url += "?applicationName=" + applicationName; //系统名称
   url += "&viewName=" + viewName; //视图名称
   url += "&title=" + StringUtils.utf8Encode(title); //对话框名称
   if(parameter && parameter!="") {
   	  url += "&" + parameter;
   }
   DialogUtils.openDialog(url, width, height);
};
//附件选择
DialogUtils.selectAttachment = function(selectorUrl, recordId, attachmentType, width, height, scriptRunAfterSelect) {
	var url = RequestUtils.getContextPath() + selectorUrl;
	url += (selectorUrl.lastIndexOf('?')==-1 ? '?' : '&') + 'id=' + recordId;
	url += '&attachmentSelector.scriptRunAfterSelect=' + scriptRunAfterSelect;
	url += '&attachmentSelector.type=' + attachmentType;
	DialogUtils.openDialog(url, width, height);
};
/**
 * 打开列表选择对话框
 * @param title:对话框标题
 * @param source:数据来源字段,字段内容格式为:标题1|值1,标题n|值n,或者本身就是:标题1|值1,标题n|值n
 * @param itemsText:指定的值列表,有指定时,source不再起作用
 * 注:其他参数同openSelectDialog
 */
DialogUtils.openListDialog = function(dialogTitle, source, width, height, multiSelect, param, scriptEndSelect, key, itemsText, separator) {
	if(itemsText && itemsText!="") {
		source = "listDialogItemsText";
		var hidden = document.getElementsByName(source)[0];
		if(!hidden) {
			try {
				hidden = document.createElement('<input type="hidden" name="' + source + '"/>');
			}
			catch(e) {
				hidden = document.createElement('input');
				hidden.type = 'hidden';
				hidden.name = source;
			}
			document.body.appendChild(hidden);
		}
		hidden.value = itemsText;
	}
	var url = RequestUtils.getContextPath() + "/jeaf/dialog/listDialog.shtml" +
   			  "?title=" + StringUtils.utf8Encode(dialogTitle) +
   			  "&source=" + StringUtils.utf8Encode(source) +
   			  "&multiSelect=" + multiSelect +
   			  "&param=" + StringUtils.utf8Encode(param) +
   			  (scriptEndSelect ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "") +
   			  (key ? "&key=" + StringUtils.utf8Encode(key) : "") +
   			  (separator && separator!="" ? "&separator=" + StringUtils.utf8Encode(separator) : "");
	DialogUtils.openDialog(url, width, height);
};
/**
 * 打开输入对话框
 * @param title: 对话框标题
 * @param inputs: [{name:"name", title:"姓名", defaultValue:"测试", length:10, inputMode:'textarea', rows:3, itemsText:"1\\0 2\\0 3", selectOnly:true, required:true},...]
 * @param script:输入完成后需要执行的脚本,脚本通过“{value}”来获取输入的值
 * @param callback:回调函数,如:var callback = function(value) {};
 * 注:其他参数同openSelectDialog
 */
DialogUtils.openInputDialog = function(dialogTitle, inputs, width, height, script, callback) {
	var url = RequestUtils.getContextPath() + "/jeaf/dialog/inputDialog.shtml" +
   			 "?formTitle=" + StringUtils.utf8Encode(dialogTitle) +
   			 (script && script!="" ? "&script=" + StringUtils.utf8Encode(script) : "");
   	for(var i=0; i<inputs.length; i++) {
   		var field = "";
		for(var attributeName in inputs[i]) {
			field += "&" + attributeName + "=" + inputs[i][attributeName];
		}
		url += "&field" + i + "=" + StringUtils.utf8Encode(field.substring(1));
	}
	DialogUtils.openDialog(url, width, height, dialogTitle, callback);
};
/**
 * 打开消息对话框
 * @param dialogTitle:对话框标题
 * @param message:消息
 * @param buttonNames:按钮名称列表
 * @param type:对话框类型,info(默认)/warn/error
 * @param script:输入完成后需要执行的脚本,每个按钮对应要执行的脚本,用$$分隔
 * @param callback:回调函数,如:var callback = function(buttonName) {};
 * 注:其他参数同openSelectDialog
 */
DialogUtils.openMessageDialog = function(dialogTitle, message, buttonNames, type, width, height, script, callback) {
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/messageDialog.shtml" +
   			 "?formTitle=" + StringUtils.utf8Encode(dialogTitle) +
   			 "&message=" + StringUtils.utf8Encode(message) +
   			 "&buttonNames=" + StringUtils.utf8Encode(buttonNames) +
   			 "&type=" + type +
   			 "&script=" + StringUtils.utf8Encode(script);
   DialogUtils.openDialog(url, width, height, dialogTitle, callback);
};
/**
 * 打开颜色选取对话框
 * @param dialogTitle:对话框标题
 * @param colorValue:当前颜色值
 * @param script:输入完成后需要执行的脚本,脚本通过“{colorValue}”来获取输入的值
 * @param callback:回调函数,如:var callback = function(colorValue) {};
 * 注:其他参数同openSelectDialog
 */
DialogUtils.openColorDialog = function(dialogTitle, colorValue, script, callback) {
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/colorDialog.shtml" +
   			 "?formTitle=" + StringUtils.utf8Encode(dialogTitle) +
   			 "&colorValue=" + StringUtils.utf8Encode(colorValue) +
   			 "&script=" + StringUtils.utf8Encode(script);
   DialogUtils.openDialog(url, 460, 400, dialogTitle, callback);
};
/**
 * 打开对话框
 * @param applicationName:系统名称
 * @param viewName:对话框名称
 * @param width:对话框宽度
 * @param height:对话框高度
 * @param multiSelect:是否多选
 * @param param:单选时格式为:表单字段名1{数据库字段1},...表单字段名n{数据库字段n}
 * 				多选时格式为:表单关键字字段名{数据库关键字字段},表单字段名1{数据库字段1|标题1|宽度1|默认值1},...表单字段名n{数据库字段n|标题n|宽度n|默认值n},默认值在数据库字段为空时有效
 * 				注:关键字作为判断是否重复的依据,多选数据间用逗号分隔,要求所有字段一一对应,数据库字段为CATEGORY表示当前分类
 * @param scriptEndSelect:完成选择后执行的脚本
 * @param defaultCategory:默认分类
 * @param key:快速过滤关键字
 * @param separator:分隔符
 * @param page:是否分页
 * @param dialogParameter:对话框扩展参数
 */
DialogUtils.openSelectDialog = function(applicationName, viewName, width, height, multiSelect, param, scriptEndSelect, defaultCategory, key, separator, paging, dialogParameter) {
   var url = RequestUtils.getContextPath() + "/jeaf/dialog/viewSelectDialog.shtml";
   url += "?applicationName=" + applicationName; 
   url += "&viewName=" + viewName; 
   url += "&multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (defaultCategory && defaultCategory!="" ? "&viewPackage.categories=" + StringUtils.utf8Encode(defaultCategory) : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += (separator && separator!="" ? "&separator=" + StringUtils.utf8Encode(separator) : ""); 
   url += (paging ? "&paging=true" : "");
   url += (dialogParameter ? "&" + dialogParameter : "");
   DialogUtils.openDialog(url, width, height);
};
//选择用户
DialogUtils.selectPerson = function(width, height, multiSelect, param, scriptEndSelect, personTypes, key, separator, assignOrgId, hideRoot) {
   if(!personTypes || personTypes=="" || personTypes=="all") {
      personTypes = "employee,student,teacher";
   }
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectPerson.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (personTypes && personTypes!="" ? "&selectNodeTypes=" + personTypes : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //总是隐藏根目录,url += (hideRoot ? "&hideRoot=true" : "");
   DialogUtils.openDialog(url, width, height);
};
//选择组织机构
DialogUtils.selectOrg = function(width, height, multiSelect, param, scriptEndSelect, orgTypes, key, separator, assignOrgId, hideRoot, leafNodeOnly, managedOnly) {
   if(!orgTypes || orgTypes=="") {
      orgTypes = "all";
   }
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectOrg.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += (orgTypes && orgTypes!="" ? "&selectNodeTypes=" + orgTypes : ""); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //总是隐藏根目录,url += (hideRoot ? "&hideRoot=true" : "");
   url += (leafNodeOnly ? "&leafNodeOnly=true" : "");
   url += (managedOnly ? "&managedOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
};
//选择角色
DialogUtils.selectRole = function(width, height, multiSelect, param, scriptEndSelect, key, separator, assignOrgId, hideRoot, postOnly) {
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectRole.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += "&selectNodeTypes=role"; 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //总是隐藏根目录,url += (hideRoot ? "&hideRoot=true" : "");
   url += (postOnly ? "&postOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
};
//按角色选择用户
DialogUtils.selectPersonByRole = function(width, height, multiSelect, param, scriptEndSelect, key, separator, assignOrgId, hideRoot, postOnly) {
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/selectRoleMember.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   url += "&selectNodeTypes=employee,student,teacher"; 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += (assignOrgId && assignOrgId!="" ? "&parentNodeId=" + assignOrgId : ""); 
   url += "&hideRoot=true"; //总是隐藏根目录,url += (hideRoot ? "&hideRoot=true" : "");
   url += (postOnly ? "&postOnly=true" : "");
   DialogUtils.openDialog(url, width, height);
};
//选择用户,包括个人/部门/角色
DialogUtils.selectUser = function(src, width, height, multiSelect, param, orgTypes, personTypes, userTypes, key, separator, assignOrgId, hideRoot, postOnly) {
	if(!userTypes || userTypes=="") {
		userTypes = "部门\0角色\0个人(按部门)\0个人(按角色)";
	}
	PopupMenu.popupMenu(userTypes, function(menuItemId, menuItemTitle) {
		switch(menuItemId) {
		case "部门":
			DialogUtils.selectOrg(width, height, multiSelect, param, '', orgTypes, key, separator, assignOrgId, hideRoot, false, false);
			break;
			
		case "角色":
			DialogUtils.selectRole(width, height, multiSelect, param, '', key, separator, assignOrgId, hideRoot, postOnly);
			break;
		
		case "个人":
		case "个人(按部门)":
			DialogUtils.selectPerson(width, height, multiSelect, param, '', personTypes, key, separator, assignOrgId, hideRoot);
			break;
			
		case "个人(按角色)":
			DialogUtils.selectPersonByRole(width, height, multiSelect, param, '', key, separator, assignOrgId, hideRoot, postOnly);
			break;
		}
	}, src, 120, "right");
};
DialogUtils.selectVisitors = function(visitorListFieldName, describe, src, assignOrgId, userTypes) { //用户选择(多选):选择用户类型
	if(visitorListFieldName=='' && src.id && src.id.indexOf("field_")!=-1) { //没有指定字段
		var input = document.getElementById('input_' + src.id.replace('picker_', ''));
		visitorListFieldName =  input.name.substring(0, input.name.indexOf(".visitorNames"));
		describe = input.title;
		if(!describe) {
			describe = "用户";
		}
	}
	DialogUtils.selectUser(src, 640, 400, true, visitorListFieldName + ".visitorIds{id}," + visitorListFieldName + ".visitorNames{name|" + describe + "|100%}", "root,unit,unitDepartment", "", userTypes, "", ",", assignOrgId, false, false);
};
//用户页面模板配置
DialogUtils.userPageTemplateConfigure = function(userId) { //选择页面
   var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectPage.shtml";
   url += "?selectNodeTypes=page";
   url += "&currentApplicationName=jeaf/usermanage"; 
   url += "&script=" + StringUtils.utf8Encode("DialogUtils._openUserPageTemplateView('" + userId + "', '{id}'.split('__')[0], '{id}'.split('__')[1])");
   url += "&internalPageOnly=true";
   url += "&userId=" + userId;
   DialogUtils.openDialog(url, 640, 400);
};
//打开用户页面模板视图
DialogUtils._openUserPageTemplateView = function(userId, applicationName, pageName) {
   var url = RequestUtils.getContextPath() + "/jeaf/usermanage/admin/selectTemplate.shtml";
   url += "?applicationName=" + applicationName;
   url += "&pageName=" + pageName; 
   url += "&userId=" + userId;
   DialogUtils.openDialog(url, 640, 400);
};
DialogUtils.getDialogOpener = function() { //获取打开对话框的窗口
	return DialogUtils.getDialogFrame().opener;
};
DialogUtils.getDialogArguments = function() { //获取对话框参数
	return DialogUtils.getDialogFrame().dialogArguments;
};
DialogUtils.adjustDialogSize = function() { //调整对话框尺寸
	window.parent.setTimeout("adjustDialogSize()", 1);
};
DialogUtils.moveDialog = function(offsetX, offsetY) { //移动对话框对话框
	var dialogFrame = DialogUtils.getDialogFrame();
	dialogFrame.style.left = (Number(dialogFrame.style.left.replace('px', '')) + offsetX) + "px";
	dialogFrame.style.top = (Number(dialogFrame.style.top.replace('px', '')) + offsetY) + "px";
};
DialogUtils.getDialogFrame = function() { //获取对话框iframe
	var dialog = window.frameElement;
	while(dialog.id!='dialog') {
		dialog = (dialog.ownerDocument.parentWindow ? dialog.ownerDocument.parentWindow : dialog.ownerDocument.defaultView).frameElement;
	}
	return dialog;
};