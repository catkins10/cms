//多行文本框字段
TextAreaField = function(inputElementHTML, styleClass, style, parentElement) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass, true);
	this.writeFieldElement(this.resetInputElementHTML(), parentElement);
};
TextAreaField.prototype = new FormField();