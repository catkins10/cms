//连接线校验,validateDuplex:检查是否重复连接
GraphicsEditor.prototype.validateLines = function(validateDuplex) {
	for(var i = 0; i < this.elements.length; i++) {
		if(!(this.elements[i] instanceof GraphicsEditor.Line)) {
			continue;
		}
		if(!this.elements[i].fromElement || !this.elements[i].toElement) {
			return {error: "连接不能为空", elements:[this.elements[i]]};
		}
		if(!validateDuplex) {
			continue;
		}
		for(var j = i + 1; j < this.elements.length; j++) {
			if(!(this.elements[j] instanceof GraphicsEditor.Line)) {
				continue;
			}
			if(this.elements[j].fromElement==this.elements[i].fromElement && this.elements[j].toElement==this.elements[i].toElement) {
				return {error: "不允许重复连接", elements:[this.elements[i], this.elements[j]]};
			}
		}
	}
};
//循环关联检测
GraphicsEditor.prototype.validateLoop = function(element) {
	return this._validateLoop(element, [element]);
};
//递归函数:循环关联检测
GraphicsEditor.prototype._validateLoop = function(element, validated) {
	for(var i = 0; i < element.exitLines.length; i++) {
		if(!element.exitLines[i].toElement) {
			continue;
		}
		if(ListUtils.indexOf(validated, element.exitLines[i].toElement)!=-1) {
			return {error: "不允许循环关联", elements: validated};
		}
		var list = [].concat(validated);
		list.push(element.exitLines[i]);
		list.push(element.exitLines[i].toElement);
		var validateError = this._validateLoop(element.exitLines[i].toElement, list); //递归检查
		if(validateError) {
			return validateError;
		}
	}
};