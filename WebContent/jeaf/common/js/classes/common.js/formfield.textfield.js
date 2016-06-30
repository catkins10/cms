//单行文本框字段
TextField = function(inputElementHTML, styleClass, style, parentElement) {
	FormField.call(this, inputElementHTML, null, style, styleClass);
	this.writeFieldElement(this.resetInputElementHTML(), parentElement);
};
TextField.prototype = new FormField();