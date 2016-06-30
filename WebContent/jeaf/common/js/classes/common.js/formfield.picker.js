//选择器,通过pickerFrame可以访问选择器主体帧,pickerHtml:可以是文本或者函数,terminalType:终端类型,computer/android/iphone/symbian
FormField.Picker = function(pickerTitle, pickerHtml, displayArea, pickerWidth, pickerHeight, alignRight, autoSize, transparentDisabled, coverDisabled, terminalType) {
	if(!pickerHtml || pickerHtml=='') {
		return;
	}
	this.pickerTitle = pickerTitle;
	this.displayArea = displayArea; //显示区域
	this.pickerWidth = (pickerWidth && pickerWidth>0 ? pickerWidth : (!this.displayArea ? 200 : this.displayArea.offsetWidth)); //宽度
	this.pickerHeight = (pickerHeight && pickerHeight>0 ? pickerHeight : 165); //高度
	this.autoSize = autoSize; //是否允许自动调整尺寸
	this.alignRight = alignRight; //是否右对齐
	this.transparentDisabled = transparentDisabled; //是否禁止透明
	this.coverDisabled = coverDisabled;
	this.isTouchMode = terminalType && terminalType!='' && terminalType!='computer';
	this.pickerHTML = typeof pickerHtml == 'function' ? pickerHtml.call(this) : pickerHtml;
	if(!this.pickerHTML || this.pickerHTML=='') {
		return;
	}
	this.create();
	this.created = true;
};
//创建选择器
FormField.Picker.prototype.create = function() {
	if(!this.isTouchMode) {
		this._createComputerPicker();
	}
	else {
		this._createTouchPicker();
	}
};
//显示选择器
FormField.Picker.prototype.show = function(pickerLeft, pickerTop) { //displayArea=null时需要指定显示坐标
	if(!this.created) {
		var picker = this;
		window.setTimeout(function() {
			picker.show(pickerLeft, pickerTop);
		}, 30);
		return;
	}
	if(!this.isTouchMode) {
		this._showComputerPicker(pickerLeft, pickerTop);
	}
	else {
		this._showTouchPicker(pickerLeft, pickerTop);
	}
};
//隐藏选择器
FormField.Picker.prototype.hide = function() {
	this.pickerContainer.style.display = 'none';
	if(this.cover) {
		this.cover.style.display = 'none';
	}
};
//销毁选择器
FormField.Picker.prototype.destory = function() {
	if(!this.isTouchMode) {
		this._destoryComputerPicker();
	}
	else {
		this._destoryTouchPicker();
	}
};
//获取关联的字段列表,由继承者实现
FormField.Picker.prototype.getRelationFields = function() {
	
};

//创建电脑版选择器
FormField.Picker.prototype._createComputerPicker = function() {
	this.topWindow = PageUtils.getTopWindow(); //获取顶层窗口
	if(!this.coverDisabled) {
		var picker = this;
		this.cover = PageUtils.createCover(this.topWindow, 0, true);
		this.cover.onclick = function() { //事件处理:点击非选择器区域
			picker.destory();
		};
	}
	//创建DIV用来显示选择器
	this.pickerContainer = this.topWindow.document.createElement("div");
	this.pickerContainer.style.zIndex = this.cover ? Number(this.cover.style.zIndex) + 1 : DomUtils.getMaxZIndex(this.topWindow.document.body) + 1; //置顶
	this.pickerContainer.style.position = "absolute";
	this.pickerContainer.style.width = this.pickerWidth + "px";
	this.pickerContainer.style.height = "1px";
	this.pickerContainer.style.visibility = 'hidden';
	this.topWindow.document.body.insertBefore(this.pickerContainer, this.topWindow.document.body.childNodes[0]);
	this.pickerContainer.innerHTML = '<iframe style="width:' + this.pickerWidth + 'px; height:1px;" frameborder="0" scrolling="no"' + (this.transparentDisabled ? '' : ' allowTransparency="true"') + '></iframe>';
	//输出选择器主体
	this.pickerFrame = this.pickerContainer.getElementsByTagName("iframe")[0].contentWindow;
	var doc = this.pickerFrame.document;
	doc.open();
	doc.write(this.pickerHTML);
	CssUtils.cloneStyle(document, doc); //复制样式
	doc.close();
};
//显示电脑版选择器
FormField.Picker.prototype._showComputerPicker = function(pickerLeft, pickerTop) {
	this.pickerContainer.style.visibility = 'hidden';
	this.pickerContainer.style.display = '';
	if(this.cover) {
		this.cover.style.display = '';
	}
	//获取displayArea坐标
	if(this.displayArea) {
		var pos = DomUtils.getAbsolutePosition(this.displayArea, null, true);
		pickerLeft = pos.left;
		pickerTop = pos.top;
	}
	//计算上下空间,用来决定是向上弹出还是向下弹出
	var body = this.topWindow.document.body;
	var bottomSpacing = DomUtils.getClientHeight(this.topWindow.document) - (pickerTop + (this.displayArea ? this.displayArea.offsetHeight : 0) - DomUtils.getScrollTop(this.topWindow.document)) - 3; //下方的空间
	var topSpacing = pickerTop - DomUtils.getScrollTop(this.topWindow.document) - 3; //上方的空间
	
	//设置宽度
	var width = this.pickerWidth;
	if(this.autoSize) { //允许调整宽度
		this.pickerContainer.getElementsByTagName("iframe")[0].style.width = "1px";
		width = Math.max(this.pickerWidth, this.pickerFrame.document.body.scrollWidth);
	}
	this.pickerContainer.style.width = width + "px";
	this.pickerContainer.getElementsByTagName("iframe")[0].style.width = width + "px";
	//设置高度
	var height = this.pickerHeight;
	if(this.autoSize) {
		this.pickerContainer.getElementsByTagName("iframe")[0].style.height = "1px";
		height = Math.min(this.pickerFrame.document.body.scrollHeight, Math.max(bottomSpacing, topSpacing));
	}
	this.pickerContainer.style.height = height + "px";
	this.pickerContainer.getElementsByTagName("iframe")[0].style.height = height + "px";
	
	//设置显示位置:垂直方向
	if(!this.displayArea && this.autoSize) {
		this.pickerContainer.style.top = Math.max(DomUtils.getScrollTop(this.topWindow.document), Math.min(pickerTop, DomUtils.getScrollTop(this.topWindow.document) + DomUtils.getClientHeight(this.topWindow.document) - height)) + "px";
	}
	else if(bottomSpacing>=height || topSpacing<height) { //向下弹出
		this.pickerContainer.style.top = (pickerTop + (this.displayArea ? this.displayArea.offsetHeight : 0) - 2) + "px";
	}
	else { //向上弹出
		this.pickerContainer.style.top = (pickerTop - height + 1) + "px";
	}
	//设置显示位置:水平方向
	if(this.displayArea) {
		this.pickerContainer.style.left = Math.max(3, Math.min(this.alignRight ? pickerLeft + this.displayArea.offsetWidth - width : pickerLeft, DomUtils.getScrollLeft(this.topWindow.document) + DomUtils.getClientWidth(this.topWindow.document) - this.pickerContainer.offsetWidth - 3)) + "px";
	}
	else {
		this.pickerContainer.style.left = Math.min(pickerLeft, DomUtils.getScrollLeft(this.topWindow.document) + DomUtils.getClientWidth(this.topWindow.document) - this.pickerContainer.offsetWidth - 3) + "px";
	}
	//设置滚动条位置
	this.pickerFrame.document.body.scrollTop = 0;
	this.pickerContainer.style.visibility = 'visible';
};
//销毁电脑版选择器
FormField.Picker.prototype._destoryComputerPicker = function() {
	if(this.cover) {
		PageUtils.destoryCover(this.topWindow, this.cover);
	}
	this.pickerContainer.parentNode.removeChild(this.pickerContainer);
};

//创建触摸屏选择器
FormField.Picker.prototype._createTouchPicker = function() {
	this.topWindow = PageUtils.getTopWindow(); //获取顶层窗口
	this.cover = PageUtils.createCover(this.topWindow, 20, false);
	this.pickerContainer = this.topWindow.document.createElement('div');
	this.pickerContainer.className = 'touchPicker';
	this.pickerContainer.style.visibility = 'hidden';
	this.cover.appendChild(this.pickerContainer);
	var html = '<div class="touchPickerTitle">' + this.pickerTitle + '</div>' +
			   '<div class="pickerBody" id="pickerBody">' + this.pickerHTML + '</div>' +
			   '<div>' +
			   '	<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
			   '		<tr>' +
			   '			<td class="touchPickerCancelButton" id="touchPickerCancelButton">取消</td>' +
			   '			<td class="touchPickerOkButton" id="touchPickerOkButton">确定</td>' +
			   '		</tr>' +
			   '	</table>' +
			   '</div>';
	this.pickerContainer.innerHTML = html;
	this.pickerBody = this.topWindow.document.getElementById('pickerBody');
	var picker = this;
	this.topWindow.document.getElementById('touchPickerOkButton').onclick = function() {
		 picker.doOK();
		 picker.destory();
	};
	this.topWindow.document.getElementById('touchPickerCancelButton').onclick = function() {
		picker.destory();
	};
};
//显示触摸屏选择器
FormField.Picker.prototype._showTouchPicker = function(pickerLeft, pickerTop) {
	if(this.cover) {
		this.cover.style.display = '';
	}
	this.pickerContainer.style.left = Math.round((this.cover.offsetWidth - this.pickerContainer.offsetWidth)/2) + 'px';
	this.pickerContainer.style.top = Math.round((this.cover.offsetHeight - this.pickerContainer.offsetHeight)/2) + 'px';
	this.pickerContainer.style.visibility = 'visible';
};
//销毁触摸屏选择器
FormField.Picker.prototype._destoryTouchPicker = function() {
	PageUtils.destoryCover(this.topWindow, this.cover);
};