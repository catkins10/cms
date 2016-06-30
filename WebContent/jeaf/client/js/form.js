Client.prototype.resetForm = function(doc) {
	var forms = doc.forms;
	for(var i=(forms ? forms.length-1 : -1); i>=0; i--) {
		if(forms[i].method && !forms[i].onsubmit)  {
			forms[i].onsubmit = function() {
				if(!window.top.client.currentForm)  {
					window.top.client.currentForm = this;
					window.top.client.callNativeMethod("isBusy()", function(returnValue) {
						window.top.client.resumeSubmit(returnValue);
					});
				}
				return false;
			};
		}
	}
	//重置文本框
	var inputs = doc.getElementsByTagName("input");
	if(inputs && inputs.length > 0) {
		ScriptUtils.appendJsFile(doc, RequestUtils.getContextPath() + '/jeaf/client/js/form.picker.js'); //引用form.picker.js
	}
	var names = "";
	for(var i=0; i<(inputs ? inputs.length : 0); i++) {
		if((inputs[i].type=='text' || inputs[i].type=='password') && inputs[i].style.display!='none') {
			this.resetTextBox(inputs[i]);
		}
	}
	var textareas = doc.getElementsByTagName("textarea");
	for(var i=0; i<(textareas ? textareas.length : 0); i++) {
		if(textareas[i].style.display!='none') {
			this.resetTextBox(textareas[i]);
		}
	}
	if('正在处理,请稍候...'==this.toastMessage) {
		window.top.client.showToast(null);
	}
};
Client.prototype.resumeSubmit = function(isBusy) {
	if(isBusy=="false") {
		this.showToast("正在处理,请稍候...", 50);
		this.currentForm.submit();
	}
	this.currentForm = null;
};
Client.prototype.resetTextBox = function(textBox) { //重置单个文本框
	if(textBox.offsetHeight==0 || textBox._placeholder) {
		return;
	}
	textBox.style.height = textBox.offsetHeight + "px !important";
	var borderLeftWidth = this._getStyleNumberValue(textBox, 'border-left-width', 0);
	var paddingLeft = this._getStyleNumberValue(textBox, 'padding-left', 0);
	//创建一个div,覆盖在输入框上面,以支持滚动
	textBox._placeholder = textBox.ownerDocument.createElement("div");
	textBox._placeholder.className = "placeholder";
	textBox._placeholder.style.display = 'block';
	textBox._placeholder.style.position = 'absolute';
	textBox._placeholder.style.textAlign = "left";
	textBox._placeholder.style.paddingLeft = (borderLeftWidth + paddingLeft) + "px";
	textBox._placeholder.style.width =  (textBox.offsetWidth - borderLeftWidth - paddingLeft) + 'px';
	textBox._placeholder.style.height = textBox._placeholder.style.lineHeight = textBox.offsetHeight + 'px';
	if(textBox.style.float!='right') {
		textBox._placeholder.style.marginTop = '-' + textBox.offsetHeight + 'px';
	}
	textBox._placeholder.style.marginLeft = textBox.offsetLeft + 'px';
	if(textBox.value=='' && textBox.getAttribute("alt")) {
		textBox._placeholder.innerHTML = textBox.getAttribute("alt");
	}
	if(!textBox.readOnly) {
		textBox._placeholder.onclick = function() {
			window.top.client.createTextBox(textBox);
		};
	}
	if(textBox.nextSibling) {
		textBox.parentNode.insertBefore(textBox._placeholder, textBox.nextSibling);
	}
	else {
		textBox.parentNode.appendChild(textBox._placeholder);
	}
};
Client.prototype.createTextBox = function(textBox) { //创建文本框
	this.textBox = textBox;
	textBox.ownerDocument.autoRefreshDisabled = true; //禁止被自动刷新
	//获取背景颜色
	var bgColor;
	var element = textBox;
	while((bgColor=CssUtils.getElementComputedStyle(element, 'background-color'))=='rgba(0, 0, 0, 0)') { //透明
		if(element.tagName=='BODY') {
			break;
		}
		element = element.parentNode;
	}
	var scroller = textBox.ownerDocument.scroller;
	if(scroller) {
		var pos = DomUtils.getAbsolutePosition(textBox, scroller.pageElement, false);
		pos.top -= scroller.pageElement.scrollTop;
		var scrollY;
		if((scrollY=pos.top-10)<0) {
			scroller.scroll(0, scrollY);
		}
		else if((scrollY=pos.top+textBox.offsetHeight+10-scroller.pageElement.clientHeight)>0) {
			scroller.scroll(0, scrollY);
		}
	}
	var pos = DomUtils.getAbsolutePosition(textBox, null, true);
	var borderLeftWidth = this._getStyleNumberValue(textBox, 'border-left-width', 0);
	var borderRightWidth = this._getStyleNumberValue(textBox, 'border-right-width', 0);
	var borderTopWidth = this._getStyleNumberValue(textBox, 'border-top-width', 0);
	var borderBottomWidth = this._getStyleNumberValue(textBox, 'border-bottom-width', 0);
	var paddingLeft = this._getStyleNumberValue(textBox, 'padding-left', 0);
	var paddingRight = this._getStyleNumberValue(textBox, 'padding-right', 0);
	var paddingTop = this._getStyleNumberValue(textBox, 'padding-top', 0);
	var paddingBottom = this._getStyleNumberValue(textBox, 'padding-bottom', 0);
	var maxlength = textBox.getAttribute("maxlength");
	var dataType = textBox.getAttribute("dataType");
	if(!dataType) {
		dataType = "string";
	}
	if(textBox.type=="password") {
		dataType = "password";
	}
	var form = DomUtils.getParentNode(textBox, "form"); //获取表单
	if(!form) {
		form = textBox.ownerDocument.body;
	}
	var formElements = this._getFormElements(form);
	var lastField = formElements.fields[formElements.fields.length-1];
	var formName;
	var imeActionType; //next/search/go/done/send/unspecified
	if(textBox!=lastField) {
		imeActionType = 'next';
	}
	else if(form.onSoftKeyboardAction) {
		imeActionType = 'done';
	}
	else if(formElements.buttons.length!=1 && formElements.defaultButtons.length!=1) {
		imeActionType = 'unspecified';
	}
	else if((formName = form.getAttribute("name") ? form.getAttribute("name").toLowerCase() : '').indexOf('search')!=-1) {
		imeActionType = 'search';
	}
	else if(formName.indexOf('send')!=-1) {
		imeActionType = 'send';
	}
	else if(formName.indexOf('go')!=-1) {
		imeActionType = 'go';
	}
	else {
		imeActionType = 'done';
	}
	this.callNativeMethod("createTextBox(" +
		"'" + textBox.value.replace(/\"/gi, "\\\"").replace(/'/gi, "\\'").replace(/\r/gi, "\\r").replace(/\n/gi, "\\n") + "'," + //value
		"'" + (!textBox.getAttribute("alt") ? "" : textBox.getAttribute("alt").replace(/\"/gi, "\\\"").replace(/'/gi, "\\'").replace(/\r/gi, "\\r").replace(/\n/gi, "\\n")) + "'," + //placeholder
		(!textBox.valueSelectionStart ? 0 : Number(textBox.valueSelectionStart)) + "," + //selectionStart
		(!textBox.valueSelectionEnd ? 0 : Number(textBox.valueSelectionEnd)) + "," + //selectionEnd
		(textBox.tagName=="INPUT" ? 1 : textBox.rows) + "," + //lines
		(maxlength ? Number(maxlength) : 0) + "," + //maxCharacter
		"'" + CssUtils.getElementComputedStyle(textBox, 'font-family') + "'," + //fontFamily
		(this._getStyleNumberValue(textBox, 'font-size', 0) * this.density) + "," + //fontSize
		Math.round(this._getStyleNumberValue(textBox, 'line-height', 0) * this.density) + "," + //lineHeight
		"'" + CssUtils.getElementComputedStyle(textBox, 'color') + "'," + //fontColor
		Math.round((pos.left + borderLeftWidth + paddingLeft) * this.density) + "," + //left
		Math.round((pos.top + borderTopWidth + paddingTop) * this.density) + "," + //top
		Math.round((textBox.offsetWidth - borderLeftWidth - borderRightWidth - paddingLeft - paddingRight) * this.density) + "," + //width
		Math.round((textBox.offsetHeight - borderTopWidth - borderBottomWidth - paddingTop - paddingBottom) * this.density) + "," + //height
		Math.round((lastField ? DomUtils.getAbsolutePosition(lastField, null, true).top + lastField.offsetHeight : 0) * this.density) + "," + //formBottom
		(textBox.scrollLeft * this.density) + "," + //scrollLeft
		(textBox.scrollTop * this.density) + "," + //scrollTop
		"'" + bgColor + "'," + //backgorundColor
		"'" + dataType + "'," +  //inputType
		"'" + imeActionType + "');"); //imeActionType: next/search/go/done/send/unspecified
};
Client.prototype._getStyleNumberValue = function(element, styleName, defaultValue) { //获取样式数字值
	var style = CssUtils.getElementComputedStyle(element, styleName);
	if(!style) {
		return defaultValue;
	}
	var value = Number(style.replace('px', ''));
	return isNaN(value) ? defaultValue : value;
};
Client.prototype._getFormElements = function(form) { //获取表单的字段(隐藏、只读除外)和按钮列表
	var formElements = {fields:[], buttons:[], defaultButtons:[]};
	//遍历页面元素,callback=function(element),返回true时不遍历子元素
	DomUtils.traversalChildElements(form, function(element) {
		if(element.style.display=='none' || element.style.visibility=='hidden') {
			return true;
		}
		if(element.id && element.id.indexOf('button_')==0) {
			formElements.buttons.push(element);
			if(element.getAttribute('default')=='true') {
				formElements.defaultButtons.push(element);
			}
			return true;
		}
		if((element.tagName=='TEXTAREA' || element.tagName=='INPUT') && !element.readOnly && (!element.type || 'password,text,textarea'.indexOf(element.type.toLowerCase())!=-1)) {
			formElements.fields.push(element);
			return true;
		}
	});
	return formElements;
};
Client.prototype.onSoftKeyboardAction = function(imeActionType) { //客户端调用:软键盘操作,imeActionType:next/search/go/done/send
	var textBox = this.textBox;
	var form = DomUtils.getParentNode(textBox, "form"); //获取表单
	if(!form) {
		form = textBox.ownerDocument.body;
	}
	var formElements = this._getFormElements(form);
	this.removeTextBox();
	if('next'!=imeActionType) {
		if(form.onSoftKeyboardAction) {
			form.onSoftKeyboardAction(imeActionType);
		}
		else if(formElements.buttons.length==1) {
			EventUtils.clickElement(formElements.buttons[0]);
		}
		else if(formElements.defaultButtons.length==1) {
			EventUtils.clickElement(formElements.defaultButtons[0]);
		}
		return;
	}
	for(var i=0; i<formElements.fields.length; i++) {
		if(formElements.fields[i]!=textBox) {
			continue;
		}
		var nextField = formElements.fields[i+1];
		if(!nextField.readOnly && !nextField.disabled && (nextField.tagName=='TEXTAREA' || !nextField.type || nextField.type.toLowerCase()=='text')) {
			this.createTextBox(nextField);
		}
		break;
	}
};
Client.prototype.removeTextBox = function() { //关闭文本框
	if(!this.textBox) {
		return;
	}
	var textBox = this.textBox;
	this.textBox = null;
	this.callNativeMethod("removeTextBox()", function(returnValue) {
		window.top.client._updateTextValue(returnValue, textBox)
	});
};
Client.prototype._updateTextValue = function(newValue, textBox) { //更新字段值,newValue(不指定textBox时):光标开始位置#光标结束位置#水平滚动位置#垂直滚动位置##文本内容
	if(!textBox || !textBox.ownerDocument) {
		return;
	}
	var index = newValue.indexOf('##');
	var value = newValue.substring(index + 2);
	textBox._placeholder.innerHTML = (value=='' && textBox.getAttribute("alt") ? textBox.getAttribute("alt") : '');
	textBox.value = value;
	var values = newValue.substring(0, index).split("#");
	textBox.valueSelectionStart = values[0];
	textBox.valueSelectionEnd = values[1];
	textBox.scrollLeft = Number(values[2]);
	textBox.scrollTop = Number(values[3]);
	try {
		textBox.onchange();
	}
	catch(e) {
	
	}
};
Client.prototype.setTextValue = function(newValue, textBox) { //更新字段值
	if(!newValue) {
		newValue = '';
	}
	if(textBox._placeholder) {
		textBox._placeholder.innerHTML = (newValue=='' && textBox.getAttribute("alt") ? textBox.getAttribute("alt") : '');
	}
	textBox.value = newValue;
};