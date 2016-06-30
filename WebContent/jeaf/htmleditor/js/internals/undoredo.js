var MAX_UNDO_STEP_COUNT = 10; //最多允许的撤销次数

//撤销和重做相关方法
HtmlEditor.prototype.saveUndoStep = function() { //保存撤销记录
	var html = this.getHTML();
	var range = this.getRange();
	this.undoStepIndex++;
	if(this.undoStepIndex <= MAX_UNDO_STEP_COUNT) { //未超过10次撤销记录
		this.undoSteps = this.undoSteps.slice(0, this.undoStepIndex);
	}
	else {
		this.undoSteps = this.undoSteps.slice(1, this.undoStepIndex);
		this.undoStepIndex--;
	}
	var undoStep;
	if(!range) {
		undoStep = {html:html};
	}
	else {
		undoStep = {html:html, bookmark: DomSelection.getRangeBookmark(range)};
	}
	undoStep.scrollLeft = this.editorDocument.body.scrollLeft;
	undoStep.scrollTop = this.editorDocument.body.scrollTop;
	this.undoSteps.push(undoStep);
	var editor = this;
	window.setTimeout(function() {
		editor._resetElements();
	}, 100);
};
HtmlEditor.prototype._isAllowUndo = function() { //是否允许撤销
	return this.undoStepIndex >= 0;
};
HtmlEditor.prototype._isAllowRedo = function() { //是否允许重做
	return this.undoStepIndex < this.undoSteps.length - 2;
};
HtmlEditor.prototype.undo = function() { //撤销
	if(!this._isAllowUndo()) {
		return;
	}
	if(this.undoStepIndex==this.undoSteps.length-1) {
		this.saveUndoStep();
		this.undoStepIndex--;
	}
	this._applyUndoStep(this.undoStepIndex);
	this.undoStepIndex--;
};
HtmlEditor.prototype.redo = function() { //重做
	if(!this._isAllowRedo()) {
		return;
	}
	this._applyUndoStep(this.undoStepIndex + 2);
	this.undoStepIndex++;
};
HtmlEditor.prototype._applyUndoStep = function(stepIndex) { //执行撤销或者重做操作
	var undoStep = this.undoSteps[stepIndex];
	this.iframe.style.display = 'none';
	this.setHTML(undoStep.html);
	this.iframe.style.display = '';
	this.editorDocument.body.scrollLeft = undoStep.scrollLeft;
	this.editorDocument.body.scrollTop = undoStep.scrollTop;
	if(undoStep.bookmark) {
		var range = DomSelection.createRangeByBookmark(this.editorDocument, undoStep.bookmark);
		DomSelection.selectRange(this.editorWindow, range);
	}
};