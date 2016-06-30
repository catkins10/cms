FormField = function(inputElementHTML, fieldType, style, styleClass, isTextArea) { //输出一个字段
	this.inputElementHTML = inputElementHTML;
	this.id = "field_" + ("" + Math.random()).substring(2);
	this.fieldType = fieldType;
	this.style = !style || style=='null' || style=='' ? '' : style + (style.substring(style.length-1)==';' ? '' : ';');
	this.styleClass = !styleClass || styleClass=='null' ? '' : styleClass;
	this.isTextArea = isTextArea;
};
FormField.prototype.writeFieldElement = function(fieldBodyHTML, parentElement) { //输出字段HTML
	var html = (this.fieldType=='hidden' ? this.resetInputElementHTML() : '') +
			   '<div id="' + this.id + '"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="display:block; ' + this.style + 'text-indent: 0px !important; background-color:transparent !important; padding:0px 0px 0px 0px !important; border: #ffffff 0px none !important; outline:none !important;">\n' +
		   	   '	<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">\n' +
		   	   '		<tr>' +
		   	   '			<td' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.style + 'width:100% !important; height:100% !important; text-indent: 0px !important; outline:none !important;">' +
			   '		' + fieldBodyHTML +
			   '			</td>' +
			   '		</tr>' +
			   '	</table>\n' +
			   '</div>';
	if(parentElement) {
		this.document = parentElement.ownerDocument;
		parentElement.innerHTML = html;
	}
	else {
		this.document = document;
		document.write(html);
	}
	this.getInputElement().formField = this;
};
FormField.prototype.resetInputElementHTML = function() { //获取重置以后的文本框HTML
	this._resetAttribute("id", 'input_' + this.id);
	if(this.fieldType) {
		this._resetAttribute("type", this.fieldType);
	}
	if(this.fieldType!='hidden') {
		this._resetAttribute("style", this.getInputElementCssText());
		this._resetAttribute("class", this.styleClass);
	}
	return this.inputElementHTML;
};
FormField.prototype._resetAttribute = function(attributeName, attributeValue) { //重置属性
	var index = this.inputElementHTML.indexOf(attributeName + '="');
	if(index==-1) {
		index = this.inputElementHTML.indexOf(' ');
		this.inputElementHTML = this.inputElementHTML.substring(0, index) + ' ' + attributeName + '="' + attributeValue + '"' + this.inputElementHTML.substring(index);
	}
	else {
		index += (attributeName + '="').length;
		var indexEnd = this.inputElementHTML.indexOf('"', index);
		this.inputElementHTML = this.inputElementHTML.substring(0, index) + attributeValue + this.inputElementHTML.substring(indexEnd);
	}
};
FormField.prototype.getAttribute = function(attributeName) { //获取属性值
	var index = this.inputElementHTML.indexOf(attributeName + '="');
	if(index==-1) {
		return "";
	}
	index += (attributeName + '="').length;
	var indexEnd = this.inputElementHTML.indexOf('"', index);
	return this.inputElementHTML.substring(index, indexEnd);
};
FormField.prototype.getFieldElement = function() { //获取字段元素
	return this.document.getElementById(this.id);
};
FormField.prototype.getInputElement = function() { //获取输入框元素
	return this.document.getElementById('input_' + this.id);
};
FormField.prototype.getInputElementCssText = function() { //获取输入框样式
	var height = this.styleClass=='' ? null : CssUtils.getStyleByName(document, '.' + this.styleClass, 'height');
	if(!height || height=='') {
		height = CssUtils.getStyle(this.style, 'height');
	}
	return (this.isTextArea ? "" : ";line-height:normal !important") + ';' + this.style + (!height || height=='' ? '' : 'height:100% !important;') + 'width:100% !important; background-color:transparent !important; padding:1px 0px 1px 1px !important; margin:0px !important; margin-left:-1px !important; border: 0px #fff none !important; outline:none !important;';
};
FormField.getFormField = function(fieldName) { //获取表单字段对象
	var field = document.getElementsByName(fieldName)[0];
	return field ? field.formField : null;
};