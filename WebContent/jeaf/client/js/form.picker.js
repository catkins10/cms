//创建选择器
FormField.Picker.prototype._createTouchPicker = function() {
	window.top.client.formPicker = this;
	window.top.FormField.Picker.createPicker();
};
//显示选择器
FormField.Picker.prototype._showTouchPicker = function(pickerLeft, pickerTop) {
	this.dialog.open();
};
//销毁选择器
FormField.Picker.prototype._destoryTouchPicker = function() {
	
};
//创建选择器
FormField.Picker.createPicker = function() {
	var picker = window.top.client.formPicker;
	picker.dialog = new Dialog(picker.pickerTitle, picker.pickerHTML, false, false); //Dialog(dialogTitle, dialogBodyHTML, hideOkButton, hideCancelButton, okButtonName, cancelButtonName)
	picker.pickerContainer = picker.dialog.clientDialog;
	picker.pickerBody = picker.dialog.clientDialogBody;
	picker.dialog.onOK = function(clientDialogBody) {
		picker.doOK();
		var fields = picker.getRelationFields();
		for(var i = 0; i < (fields ? fields.length : 0); i++) {
			if(fields[i]._placeholder) {
				fields[i]._placeholder.innerHTML = '';
			}
		}
		return true;
	};
};