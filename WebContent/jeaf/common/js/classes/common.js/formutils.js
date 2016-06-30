FormUtils = function() {

};
FormUtils.doAction = function(actionName, param, notLockForm, formName, target) { //执行操作
	if(window.submitting) {
		return false;
	}
	var form = FormUtils._getForm(formName);
	form.oldAction = form.action;
	form.oldTarget = form.target;
	if(target) {
		form.target = target;
	}
	if(!param) {
		param = "";
	}
	if(actionName.substring(0, 1)=="/") {
		form.action = actionName + (param=="" ? "" : (actionName.indexOf('?')==-1 ? '?' : '&') + param);
	}
	else {
		var action = form.action;
		var index = action.lastIndexOf(".shtml");
		if(index==-1) {
			return false;
		}
		index = action.lastIndexOf("/", index);
		if(index==-1) {
			return false;
		}
		form.action = action.substring(0, index + 1) + actionName + ".shtml" + (param=="" ? "" : "?" + param);
	}
	FormUtils.submitForm(notLockForm, formName);
	return true;
};
FormUtils.submitForm = function(notLockForm, formName) { //表单提交
	if(window.submitting) {
		return false;
	}
	try {
		HtmlEditor.saveHtmlContent(); //保存正文
	}
	catch(e) {

	}
	if(!formOnSubmit()) {
		return false;
	}
	var form = FormUtils._getForm(formName);
	FormUtils.resetBoxElements(form);
	try {
		form.ownerDocument.body.focus(); //for ie11
	}
	catch(e) {
	
	}
	window.submitting = !notLockForm && (!form.target || form.target=='' || form.target=='_self');
	if(window.submitting) {
		PageUtils.showToast('正在处理中, 请稍候...');  //<img style="border:none" src="' + RequestUtils.getContextPath() + '/jeaf/common/img/loading.gif">
	}
	var displayMode = DomUtils.getElement(form, 'input', 'displayMode');
	var internalForm = DomUtils.getElement(form, 'input', 'internalForm');
	if(form.target=='_self' || !window.submitting || (displayMode && displayMode.value!='window') || !internalForm || internalForm.value!='true') {
		form.submit();
		FormUtils._restoreForm(form);
		return true;
	}
	DialogUtils.openDialog(null, 480, 200, null, null,
		function(dialogBodyFrame) { //onDialogLoad
			form.target = dialogBodyFrame.name;
			if(!form.oldAction) {
				form.oldAction = form.action;
			}
			form.action += (form.action.indexOf('?')==-1 ? '?' : '&') + 'displayMode=dialog&internalForm=true' + (window.tabLists ? '&tabSelected=' + window.tabLists[0].getSelectedTabId() : '');
			form.submit();
			FormUtils._restoreForm(form);
		},
		function(dialogBodyFrame) { //onDialogBodyLoad
			PageUtils.removeToast();
		},
		function(dialogBodyFrame) { //onDialogClose
			var reloadPageURL = dialogBodyFrame.contentWindow.document.getElementsByName("reloadPageURL")[0];
			if(reloadPageURL && reloadPageURL.value!='') {
				PageUtils.showToast('重新加载中, 请稍候...');
				window.location.href = reloadPageURL.value;
			}
		}
	);
};
FormUtils._getForm = function(formName) { //获取表单
	return !formName || formName=='' ? document.forms[0] : (document.forms[formName] ? document.forms[formName] : document.getElementById(formName));
};
FormUtils._restoreForm = function(form) { //还原表单的action和target
	if(form.oldAction) {
		form.action = form.oldAction;
	}
	if(form.oldTarget && form.oldTarget!='') {
		form.target = form.oldTarget;
	}
	else {
		form.removeAttribute("target");
	}
	form.oldAction = null;
	form.oldTarget = null;
};
FormUtils.resetBoxElements = function(form) { //提交前,检查check、radio是否都没有选中,如果是直接添加一个隐藏域,这样就能识别空数据
	var inputs = form.getElementsByTagName("input");
	for(var i=(inputs ? inputs.length-1: -1); i>=0; i--) {
		if(inputs[i].id && inputs[i].parentNode==form && inputs[i].id.indexOf("hidden_")==0) {
			form.removeChild(inputs[i]);
		}
		if(inputs[i].type=="text" && inputs[i].value==inputs[i].alt) { //文本
			inputs[i].value = "";
		}
	}
	inputs = form.getElementsByTagName("textarea");
	for(var i=(inputs ? inputs.length-1: -1); i>=0; i--) {
		if(inputs[i].value==inputs[i].getAttribute("alt")) {
			inputs[i].value = "";
		}
	}
	inputs = form.getElementsByTagName("input");
	var checkedFields = new Array(); //有作出选择的
	var uncheckedFields = new Array(); //未作出选择的
	var processedFields = "";
	for(var i=0; i<(inputs ? inputs.length : 0); i++) {
		if(!inputs[i].type || !inputs[i].name) {
			continue;
		}
		var type = inputs[i].type.toLowerCase();
		if(type!="checkbox" && type!="radio" || processedFields.indexOf("[" + inputs[i].name + "]")!=-1) {
			continue;
		}
		processedFields += "[" + inputs[i].name + "]";
		//检查是否都没有被选中
		var checked = false;
		for(j=i; j<inputs.length; j++) {
			if(inputs[i].name==inputs[j].name && inputs[j].checked) {
				checked = true;
				break;
			}
		}
		if(checked) {
			checkedFields.push(inputs[i].name);
		}
		else {
			uncheckedFields.push(inputs[i].name);
		}
	}
	
	for(var i=0; i<checkedFields.length; i++) {
		var hidden = document.getElementById("hidden_" + checkedFields[i]);
		if(hidden) {
			hidden.parentNode.removeChild(hidden);
		}
	}
	for(var i=0; i<uncheckedFields.length; i++) {
		if(document.getElementById("hidden_" + uncheckedFields[i])) {
			continue;
		}
		var hidden = document.createElement("input");
		hidden.id = "hidden_" + uncheckedFields[i];
		hidden.type = "hidden";
		hidden.name = uncheckedFields[i];
		hidden.value = "";
		form.appendChild(hidden);
	}
};
function formOnSubmit() { //事件:表单提交,由调用者实现
	return true;
}
FormUtils.getCurrentAction = function() {
	var action = window.location.pathname;
	var endIndex = action.lastIndexOf(".shtml");
	if(endIndex == - 1) {
		endIndex = action.lastIndexOf(".do");
			if(endIndex == - 1)return "";
	}
	var beginIndex = action.lastIndexOf("/", endIndex);
	return action.substring(beginIndex + 1, endIndex);
};
FormUtils.reloadValidateCodeImage = function(validateCodeImageId) { //重新载入校验码图片
	if(!validateCodeImageId) {
		validateCodeImageId = "validateCodeImage";
	}
	var src = document.getElementById(validateCodeImageId).src;
	var index = src.lastIndexOf("?");
	if(index!=-1) {
		src = src.substring(0, index);
	}
	document.getElementById(validateCodeImageId).src = src + "?reload=true&seq=" + Math.random();
};
FormUtils.sendValidateCodeSms = function(mobile, sendLimit, timeInterval, siteId) { //发送校验短信
	if(!mobile || mobile=='') {
		return;
	}
	var iframe = document.getElementById('validateCodeSmsFrame');
	if(!iframe) {
		iframe = document.createElement("iframe");
		iframe.style.display = 'none';
		iframe.id = 'validateCodeSmsFrame';
		var childNodes = document.body.childNodes;
		if(childNodes.length==0) {
			document.body.appendChild(iframe);
		}
		else {
			document.body.insertBefore(iframe, childNodes[0]);
		}
	}
	iframe.src = RequestUtils.getContextPath() + "/jeaf/validatecode/sendValidateCodeSms.shtml?mobile=" + mobile + "&reload=true&sendLimit=" + sendLimit + "&siteId=" + siteId + "&seq=" + Math.random();
};
FormUtils.getTimeValue = function(fieldName) {
	var time = document.getElementsByName(fieldName)[0].value;
	if(time=="") {
		return "";
	}
	return new Date(time.replace(new RegExp("-", "g"), "/").replace(new RegExp("\\x2E0", "g"), ""));
};
//在文本框或者多行文本框中粘贴文本
FormUtils.pasteText = function(inputFieldName, text) {
	var textbox = document.getElementsByName(inputFieldName)[0];
	textbox.focus();
	if(!textbox.selectionStart && textbox.selectionStart!=0) { //IE6
		document.selection.createRange().text = text;
	}
	else {
		var startPos = textbox.selectionStart;
		var endPos = textbox.selectionEnd;
		textbox.value = textbox.value.substring(0, startPos) + text + textbox.value.substring(endPos);
		startPos += text.length;
		textbox.selectionStart = startPos;
		textbox.selectionEnd = startPos;
	}
};
FormUtils.getSelectText = function(inputFieldName) { //获取文本框或者多行文本框中选中的文本
	var textbox = document.getElementsByName(inputFieldName)[0];
	textbox.focus();
	if(!textbox.selectionStart && textbox.selectionStart!=0) { //IE
		return document.selection.createRange().text;
	}
	else {
		return textbox.value.substring(textbox.selectionStart, textbox.selectionEnd);
	}
};
FormUtils.getField = function(doc, fieldName) { //获取字段,排除meta和form
	var fields = doc.getElementsByName(fieldName);
	for(var i=0; i < fields.length; i++) {
		if(',INPUT,TEXTAREA,'.indexOf(',' + fields[i].tagName + ',')!=-1) {
			return fields[i];
		}
	}
	return null;
};
//添加INPUT
FormUtils.appendInput = function(parentElement, type, name) {
	var input;
	try {
		input = parentElement.ownerDocument.createElement('<input type="' + type + '" name="' + name + '"/>');
	}
	catch(e) {
		input = parentElement.ownerDocument.createElement('input');
		input.type = type;
		input.name = name;
	}
	parentElement.appendChild(input);
};
//获取附件数量
FormUtils.getAttachmentCount = function(attachmentType) {
	var count = Number(document.getElementById("attachmentFrame_" + attachmentType).contentWindow.document.getElementsByName("attachmentSelector.attachmentCount")[0].value);
	return isNaN(count) ? 0 : count;
};