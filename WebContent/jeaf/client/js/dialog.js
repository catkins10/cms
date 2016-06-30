//对话框
//onOK=function(clientDialogBody); onCancel=function(clientDialogBody); onOther=function(clientDialogBody); 返回false时不关闭对话框
Dialog = function(dialogTitle, dialogBodyHTML, hideOkButton, hideCancelButton, okButtonName, cancelButtonName, otherButtonName) {
	this.dialogTitle = dialogTitle; //对话框标题
	this.dialogBodyHTML = dialogBodyHTML; //对话框主体HTML
	this.hideOkButton = hideOkButton; //是否隐藏确定按钮
	this.hideCancelButton = hideCancelButton; //是否隐藏放弃取消
	this.okButtonName = okButtonName; //确定按钮名称,默认“确定”
	this.cancelButtonName = cancelButtonName; //取消按钮名称,默认“取消”
	this.otherButtonName = otherButtonName; //取消按钮名称,默认不显示
	
	//创建div覆盖在页面上面
	this.cover = window.top.client.createCover();
	var dialog = this;
	this.popupPageId = window.top.client.registPopupPage(function() {
		dialog.close();
	});
	//复制对话框元素
	this.clientDialog = document.getElementById('clientDialog').cloneNode(true);
	this.clientDialog.style.visibility = 'hidden';
	this.clientDialog.style.display = '';
	this.clientDialog.style.zIndex = window.top.client.getZIndex();
	this.clientDialogBody = DomUtils.getElement(this.clientDialog, '', 'clientDialogBody');
	//处理确定按钮
	dialog.clientDialogOkButton = DomUtils.getElement(this.clientDialog, '', 'clientDialogOkButton');
	dialog.clientDialogOkButton.style.display = this.hideOkButton ? 'none' : '';
	dialog.clientDialogOkButton.innerHTML = this.okButtonName ? this.okButtonName : '确定';
	dialog.clientDialogOkButton.onclick = function() {
		if(dialog.onOK && !dialog.onOK.call(null, dialog.clientDialogBody)) {
			return;
		}
		dialog.close();
	};
	//处理取消按钮
	var clientDialogCancelButton = DomUtils.getElement(this.clientDialog, '', 'clientDialogCancelButton');
	clientDialogCancelButton.style.display = this.hideCancelButton ? 'none' : '';
	clientDialogCancelButton.innerHTML = this.cancelButtonName ? this.cancelButtonName : '取消';
	clientDialogCancelButton.onclick = function() {
		if(dialog.onCancel && !dialog.onCancel.call(null, dialog.clientDialogBody)) {
			return;
		}
		dialog.close();
	};
	//处理其它按钮
	var clientDialogOtherButton = DomUtils.getElement(this.clientDialog, '', 'clientDialogOtherButton');
	if(clientDialogOtherButton) {
		clientDialogOtherButton.style.display = !this.otherButtonName || this.otherButtonName=="" ? 'none' : '';
		clientDialogOtherButton.innerHTML = this.otherButtonName;
		clientDialogOtherButton.onclick = function() {
			if(dialog.onOther && !dialog.onOther.call(null, dialog.clientDialogBody)) {
				return;
			}
			dialog.close();
		};
	}
	//设置标题
	DomUtils.getElement(this.clientDialog, '', 'clientDialogTitle').innerHTML = this.dialogTitle;
	//设置对话框主体
	this.clientDialogBody.innerHTML = this.dialogBodyHTML;
	document.body.appendChild(this.clientDialog); //把对话框移动到body的最后
};
Dialog.prototype.open = function() { //打开对话框
	this.clientDialog.style.left = Math.round((document.body.clientWidth - this.clientDialog.offsetWidth)/2) + 'px';
	this.clientDialog.style.top = Math.round((document.body.clientHeight - this.clientDialog.offsetHeight)/2) + 'px';
	this.clientDialog.style.visibility = 'visible';
};
Dialog.prototype.close = function() { //关闭对话框
	window.top.client.unregistPopupPage(this.popupPageId);
	//移除对话框
	this.clientDialog.parentNode.removeChild(this.clientDialog);
	//移除cover
	this.cover.parentNode.removeChild(this.cover);
};
Client.prototype.openConfirmDialog = function(dialogTitle, prompt) { //确认对话框
	var dialog = new Dialog(dialogTitle, '<div style="padding: 6px 6px 6px 6px;">' + prompt + '</div>', false, false);
	dialog.open();
	return dialog;
};
//输入对话框,inputs=[{name:"姓名", value:"测试", dataType:"string", maxLength:10}, {name:"年龄", value:"8", dataType:"number", maxLength:2}], dataType:string/number
Client.prototype.openInputDialog = function(dialogTitle, inputs, hideOkButton, hideCancelButton, okButtonName, cancelButtonName, otherButtonName) {
	var html =	'<form style="width:100%">' +
				' <table id="inputFieldsTable" border="0" cellpadding="3" cellspacing="0" width="100%" style="margin-top:10px;  margin-bottom:10px;">';
	for(var i=0; i<inputs.length; i++) {
		html +=	'  <tr>' +
			   	'	<td style="text-align:' + (inputs[i].inputMode=='radio' || inputs[i].inputMode=='checkbox' ? 'left' : 'center') + '">';
		if(inputs[i].inputMode=='radio' || inputs[i].inputMode=='checkbox') { //单选框,复选框
			var items = inputs[i].itemsText.split("\0");
			for(var j=0; j<items.length; j++) {
				var id = inputs[i].name + "_" + j;
				var values = items[j].split("|");
				value = values[values.length-1];
				html += '	<input id="' + id + '" type="' + inputs[i].inputMode + '" name="' + inputs[i].name + '" value="' + value + '"' + ((',' + inputs[i].value + ',').indexOf(',' + value + ',')==-1 ? '' : ' checked') + '>' +
						'	<label for="' + id + '">' + values[0] + '&nbsp;&nbsp;</label>';
			}
		}
		else {
			html += 	'	<input type="text" alt="' + inputs[i].name + '" name="' + inputs[i].name + '" dataType="' + inputs[i].dataType + '" maxlength="' + inputs[i].maxLength + '" value="' + inputs[i].value + '" style="width:95%;">';
		}
		html += '  	</td>' +
			   	'  </tr>';
	}
	html +=	   	' </table>' +
				'</form>';
	var dialog = new Dialog(dialogTitle, html, hideOkButton, hideCancelButton, okButtonName, cancelButtonName, otherButtonName); //Dialog(dialogTitle, dialogBodyHTML, hideOkButton, hideCancelButton, okButtonName, cancelButtonName, otherButtonName)
	dialog.clientDialogBody.getElementsByTagName('form')[0].onSoftKeyboardAction = function(imeActionType) { //客户端调用:软键盘操作,imeActionType:next/search/go/done/send
		EventUtils.clickElement(dialog.clientDialogOkButton);
	};
	var textBoxes = dialog.clientDialogBody.getElementsByTagName('input');
	for(var i=0; i<textBoxes.length; i++) {
		if(textBoxes[i].type=="text" || textBoxes[i].type=="password") {
			window.top.client.resetTextBox(textBoxes[i]); //重置单个文本框
		}
	}
	dialog.clientDialogBody.getValue = function(inputName) {
		var inputFieldsTable = DomUtils.getElement(this, 'table', 'inputFieldsTable');
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].name!=inputName) {
				continue;
			}
			if(inputs[i].inputMode=='radio' || inputs[i].inputMode=='checkbox') { //单选框,复选框
				var boxes = inputFieldsTable.rows[i].cells[0].getElementsByTagName('input');
				var values = "";
				for(var j=0; j<boxes.length; j++) {
					if(boxes[j].checked) {
						values += (values=="" ? "" : ",") + boxes[j].value;
					}
				}
				return values;
			}
			else {
				return inputFieldsTable.rows[i].cells[0].getElementsByTagName('input')[0].value;
			}
		}
		return null;
	};
	dialog.open();
	return dialog;
};
Client.prototype.confirm = function(dialogTitle, message) {
	var dialog = new Dialog(dialogTitle, '<div style="padding: 6px;">' + message + '</div>', false, false); //Dialog(dialogTitle, dialogBodyHTML, hideOkButton, hideCancelButton, okButtonName, cancelButtonName, otherButtonName)
	dialog.open();
	return dialog;
};