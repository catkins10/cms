//滚动切换
SlideShow = function(slideShowId, recordCount, imageWidth, imageHeight, imageLazyLoading, verticalScroll, speed, manualSwitch, switchByKey, autoScaling) {
	this.slideShowId = slideShowId;
	this.recordCount = recordCount;
	this.imageWidth = imageWidth;
	this.imageHeight = imageHeight;
	this.imageLazyLoading = imageLazyLoading;
	this.verticalScroll = verticalScroll;
	this.speed = speed;
	this.manualSwitch = manualSwitch;
	this.needScaling = autoScaling && !imageLazyLoading;
	this.scrollIndex = 0; //当前记录
	this.pageElement = document.getElementById("slideShow_" + this.slideShowId);
	this.contentDiv = DomUtils.getElement(this.pageElement, 'div', 'contentDiv');
	this.recordsTable = DomUtils.getElement(this.pageElement, "table", "records");
	
	//获取控制栏
	this.controlBar = DomUtils.getElement(this.pageElement, null, "controlBar");
	//获取控制栏边距
	this.controlBarLeft = this.controlBar.style.left;
	this.controlBarTop = this.controlBar.style.top;
	this.controlBarRight = this.controlBar.style.right;
	this.controlBarBottom = this.controlBar.style.bottom;
	
	//获取控制栏记录容器
	var selectedRecord = DomUtils.getElement(this.controlBar, null, "selectedRecord_0");
	if(selectedRecord) {
		this.controlBarRecordsContainer = selectedRecord.parentNode.parentNode;
		//获取尺寸
		this.controlBarRecordsContainerSize = CssUtils.getStyle(this.controlBarRecordsContainer.style.cssText, this.verticalScroll ? "height" : "width") || "100%";
		//获取overflow样式
		this.controlBarRecordsContainerOverflow = CssUtils.getElementComputedStyle(this.controlBarRecordsContainer, this.verticalScroll ? "overflow-y" : "overflow-x");
	}
	this.appendSlideShowList(); //添加到列表
	var slideShow = this;
	
	if(!this.needScaling) { //不需要自动调整尺寸
		this._resizeControlBar(); //重置控制栏大小
	}
	else {
		this._scaling();
		EventUtils.addEvent(window, 'resize', function() {
			slideShow._scaling();
		});
		EventUtils.addEvent(window, 'load', function() {
			slideShow._scaling(true);
		});
	}
	
	//创建滚动条
	this.scroller = new Scroller(this.contentDiv, !this.verticalScroll, this.verticalScroll, false, false, false, true);
	this.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		var index = slideShow.scrollIndex;
		if(slideShow.verticalScroll) {
			if(Math.abs(y) > slideShow.imageHeight/5) { //超过1/5
				index += y<0 ? -1 : 1;
			}
		}
		else {
			if(Math.abs(x) > slideShow.imageWidth/5) { //超过1/5
				index += x<0 ? -1 : 1;
			}
		}
		slideShow._slide(index==-1 ? slideShow.recordCount-1 : (index==slideShow.recordCount ? 0 : index));
	};
	
	if(switchByKey) { //方向键翻阅
		EventUtils.addEvent(document, "keydown", function(event) {
			if(event.keyCode==37) {
				SlideShow.slideToNextImage(slideShowId, false);
			}
			else if(event.keyCode==39) {
				SlideShow.slideToNextImage(slideShowId, true);
			}
		});
	}
	
	this.startAutoSwitch(); //启动切换
};
SlideShow.prototype._resizeControlBar = function() { //重置控制栏大小
	try {
		if(this.pageElement.parentNode.offsetWidth==0) {
			if(this.resizeControlBarTimer) {
				return;
			}
			var slideShow = this;
			this.resizeControlBarTimer = window.setInterval(function() {
				slideShow._resizeControlBar();
			}, 30);
			return;
		}
		if(this.resizeControlBarTimer) {
			window.clearInterval(this.resizeControlBarTimer);
			this.resizeControlBarTimer = null;
		}
		var imgs = this.controlBar.getElementsByTagName('img');
		if(!imgs[0] || !imgs[0].src || imgs[0].src.indexOf('_breviary_')==-1) { //控制栏中没有缩略图
			return;
		}
		//获取区域宽度或高度
		var areaSize;
		if(!this.verticalScroll) { //水平滚动
			areaSize = this.imageWidth ? this.imageWidth : (this.pageElement.rows.length==1 ? this.getMinImageSize().width : Number(this.contentDiv.parentNode.width.replace('px', '')));
			if(isNaN(areaSize) || areaSize<=0) {
				return;
			}
			this.controlBarRecordsContainer.style.width = '';
			areaSize -= this.controlBar.offsetWidth - this.controlBarRecordsContainer.offsetWidth;
			if(this.controlBarLeft.indexOf('px')!=-1) {
				areaSize -= Number(this.controlBarLeft.replace('px', ''));
			}
			if(this.controlBarRight.indexOf('px')!=-1) {
				areaSize -= Number(this.controlBarRight.replace('px', ''));
			}
		}
		else { //垂直方向滚动
			areaSize = this.imageHeight>0 ? this.imageHeight : this.getMinImageSize().height;
			if(areaSize<=0) {
				return;
			}
			this.controlBarRecordsContainer.style.height = '';
			areaSize -= this.controlBar.offsetHeight - this.controlBarRecordsContainer.offsetHeight;
			if(this.controlBarTop.indexOf('px')!=-1) {
				areaSize -= Number(this.controlBarTop.replace('px', ''));
			}
			if(this.controlBarBottom.indexOf('px')!=-1) {
				areaSize -= Number(this.controlBarBottom.replace('px', ''));
			}
		}
		var size = this.controlBarRecordsContainerSize;
		if(size.indexOf('%')!=-1) {
			size = areaSize * Number(size.replace(/%/, '')) / 100;
		}
		else {
			size = Number(size.replace(/px/, ''));
		}
		if(this.controlBarRecordsContainerOverflow=='hidden' || this.controlBarRecordsContainerOverflow=='auto') { //允许滚动
			eval('this.controlBarRecordsContainer.style.' + (this.verticalScroll ? 'height' : 'width') + '= size + "px";');
			return;
		}
		if(this.controlBarRecordsContainer.style.display=='none') {
			return;
		}
		var distance = (this.verticalScroll ? this.controlBarRecordsContainer.offsetHeight : this.controlBarRecordsContainer.offsetWidth) - size;
		if(!this.controlBarResized && distance<=0) {
			return;
		}
		this.controlBarResized = true;
		distance = Math.round(distance/this.recordCount);
		for(var i=0; i<imgs.length; i++) {
			this._adjustImageSize(imgs[i], (this.verticalScroll ? 0 : -distance), (this.verticalScroll ? -distance : 0));
		}
	}
	finally {
		if(!this.needScaling && this.pageElement.parentNode.offsetWidth!=0) {
			this.pageElement.style.visibility = 'visible';
			this._setScrollIndex(this.scrollIndex);
		}
	}
};
SlideShow.prototype.getMinImageSize = function() { //获取图片的最小尺寸
	var size = {width:0, height:0};
	var images = this.contentDiv.getElementsByTagName('img');
	for(var i=0; i<(images ? images.length : 0); i++) {
		var width = Number(images[i].getAttribute("width", 2));
		var height = Number(images[i].getAttribute("height", 2));
		if(!isNaN(width) && width>0 && (size.width==0 || width<size.width)) {
			size.width = width;
		}
		if(!isNaN(height) && height>0 && (size.height==0 || height<size.height)) {
			size.height = height;
		}
	}
	return size;
};
SlideShow.prototype._adjustImageSize = function(image, widthAdjust, heightAdjust) { //缩放图片
	var width = Number(image.getAttribute("width", 2));
	var height = Number(image.getAttribute("height", 2));
	if(heightAdjust!=0) {
		image.setAttribute("height", height + heightAdjust);
		image.setAttribute("width", Math.round((height + heightAdjust) * width / height));
	}
	else if(widthAdjust!=0) {
		image.setAttribute("width", width + widthAdjust);
		image.setAttribute("height", Math.round((width + widthAdjust) * height / width));
	}
};
SlideShow.prototype._scaling = function(force) { //自动缩放
	if(!this.pageElement.parentNode || this.imageWidth==0) { //没有指定图片大小,不允许自动缩放
		return;
	}
	if(!force && this.lastScaling && new Date().getTime()-this.lastScaling < 2000) {
		return;
	}
	if(this.pageElement.parentNode.offsetWidth==0 || document.body.scrollWidth==0) {
		if(this.scalingTimer) {
			return;
		}
		var slideShow = this;
		this.scalingTimer = window.setInterval(function() {
			slideShow._scaling(true);
		}, 50);
		return;
	}
	this.lastScaling = new Date().getTime();
	if(this.scalingTimer) {
		window.clearInterval(this.scalingTimer);
		this.scalingTimer = null;
	}
	this._setScrollIndex(this.scrollIndex);
	this._resizeControlBar(); //重置控制栏大小
	this.needScaling = false;
	
	//获取新的宽度
	var display = this.pageElement.style.display;
	var div = document.createElement("div");
	this.pageElement.parentNode.replaceChild(div, this.pageElement);
	var targetWidth = div.offsetWidth;
	div.parentNode.replaceChild(this.pageElement, div);
	//获取当前尺寸
	var widthAdd = targetWidth - this.pageElement.offsetWidth;
	if(widthAdd==0) {
		this._resizeControlBar(); //重置控制栏大小
		return;
	}
	var images = this.contentDiv.getElementsByTagName('img');
	//获取控制栏图片
	var controlBarImageWidth = 0;
	var controlBarImages = this.pageElement.rows[0].cells.length==1 ? null : this.controlBar.getElementsByTagName('img');
	if(controlBarImages && controlBarImages[0] && controlBarImages[0].src && controlBarImages[0].src.indexOf('_breviary_')!=-1) { //控制栏中有缩略图
		controlBarImageWidth = Number(controlBarImages[0].getAttribute("width", 2));
	}
	var imageWidthAdd = Math.round(this.imageWidth / (this.imageWidth + controlBarImageWidth) * widthAdd);
	var controlBarImageWidthAdd = widthAdd - imageWidthAdd;
	//重置图片大小
	for(var i=0; i<images.length; i++) {
		this._adjustImageSize(images[i], imageWidthAdd, 0);
	}
	for(var i=0; i<(controlBarImages ? controlBarImages.length : 0); i++) {
		this._adjustImageSize(controlBarImages[i], controlBarImageWidthAdd, 0);
	}
	this.imageWidth = Number(images[0].getAttribute("width", 2));
	this.imageHeight = Number(images[0].getAttribute("height", 2));
	this._resizeControlBar(); //重置控制栏大小
	this._setScrollIndex(this.scrollIndex);
};
SlideShow.prototype.openLink = function(index) {
	var link = DomUtils.getElement(this.recordsTable, "td", "record_" + index).getElementsByTagName("a")[0];
	EventUtils.clickElement(link);
};
//滚动
SlideShow.prototype._slide = function(index) {
	if(!this.pageElement.parentNode) {
		return;
	}
	this.stopAutoSwitch();
	if(this.imageLazyLoading) {
		this._setScrollIndex(index);
		this.startAutoSwitch();
		return;
	}
	var slideShow = this;
	var cellIndex = this.scrollIndex==0 && index==this.recordCount-1 ? 0 : (this.scrollIndex==this.recordCount-1 && index==0 ? this.recordCount + 1 : index + 1);
	var step = this.verticalScroll ? this.recordsTable.rows[cellIndex].cells[0].offsetTop : this.recordsTable.rows[0].cells[cellIndex].offsetLeft;
	step -= this.verticalScroll ? this.contentDiv.scrollTop : this.contentDiv.scrollLeft;
	var loop = Math.abs(step)>100 ? 10 : 5;
	step  = step / loop;
	var count = 0;
	var timer = window.setInterval(function() {
		if((++count)<loop) {
			slideShow.scroller.scroll(slideShow.verticalScroll ? 0 : step, slideShow.verticalScroll ? step : 0);
			return;
		}
		window.clearInterval(timer);
		slideShow._setScrollIndex(index);
	}, 30);
	this.startAutoSwitch();
};
SlideShow.prototype._setScrollIndex = function(index) { //设置当前记录
	if(!this.pageElement.parentNode) {
		return;
	}
	if(this.pageElement.parentNode.offsetWidth==0) {
		return;
	}
	if(this.imageLazyLoading && index!=this.scrollIndex) {
		if(this.verticalScroll) {
			this.recordsTable.rows[this.scrollIndex].cells[0].style.display = 'none';
		}
		else {
			this.recordsTable.rows[0].cells[this.scrollIndex].style.display = 'none';
		}
	}
	var recordCell; 
	if(this.verticalScroll) {
		recordCell = this.recordsTable.rows[index + (this.imageLazyLoading ? 0 : 1)].cells[0];
		recordCell.style.display = '';
		this.contentDiv.scrollTop = recordCell.offsetTop;
	}
	else {
		recordCell = this.recordsTable.rows[0].cells[index + (this.imageLazyLoading ? 0 : 1)];
		recordCell.style.display = '';
		this.contentDiv.scrollLeft = recordCell.offsetLeft;
	}
	
	//显示图片
	var images = recordCell.getElementsByTagName('img');
	for(var i=0; i<(images ? images.length : 0); i++) {
		if(images[i].id) {
			images[i].src = images[i].id;
			images[i].removeAttribute('id');
		}
	}
	
	//设置内容DIV尺寸
	this.contentDiv.style.width = recordCell.offsetWidth + "px";
	this.contentDiv.style.height = recordCell.offsetHeight + "px";
	
	//按图片位置重置控制栏位置
	var image = this.controlBarLeft=="" && this.controlBarTop=="" && this.controlBarRight=="" && this.controlBarBottom=="" ? null : recordCell.getElementsByTagName('img')[0];
	if(image) {
		var pos = DomUtils.getAbsolutePosition(image, recordCell); //获取图片的绝对位置
		if(this.controlBarLeft.indexOf("px")!=-1) {
			this.controlBar.style.left = (this.contentDiv.offsetLeft + pos.left + Number(this.controlBarLeft.replace('px', ''))) + "px";
		}
		else if(this.controlBarLeft=="50%") { //居中
			this.controlBar.style.left = (this.contentDiv.offsetLeft + pos.left + image.offsetWidth/2 - this.controlBar.offsetWidth/2) + "px";
		}
		if(this.controlBarTop.indexOf("px")!=-1) {
			this.controlBar.style.top = (pos.top + Number(this.controlBarTop.replace('px', ''))) + "px";
		}
		else if(this.controlBarTop=="50%") { //居中
			this.controlBar.style.top = (pos.top + image.offsetHeight/2 - this.controlBar.offsetHeight/2) + "px";
		}
		if(this.controlBarRight.indexOf("px")!=-1) {
			this.controlBar.style.right = (this.contentDiv.parentNode.offsetWidth - this.contentDiv.offsetWidth - this.contentDiv.offsetLeft + pos.right + Number(this.controlBarRight.replace('px', ''))) + "px";
		}
		if(this.controlBarBottom.indexOf("px")!=-1) {
			this.controlBar.style.bottom = (pos.bottom + Number(this.controlBarBottom.replace('px', ''))) + "px";
		}
	}
	
	//设置“下一图片”“上一图片”按钮位置
	var previousImageButton = DomUtils.getElement(this.pageElement, 'div', 'previousImageButton');
	if(previousImageButton) {
		previousImageButton.style.top = (previousImageButton.parentNode.offsetHeight - previousImageButton.offsetHeight) /2 + "px";
	}
	var nextImageButton = DomUtils.getElement(this.pageElement, 'div', 'nextImageButton');
	if(nextImageButton) {
		nextImageButton.style.top = (previousImageButton.parentNode.offsetHeight - nextImageButton.offsetHeight) /2 + "px";
	}
	if(index!=this.scrollIndex && this.controlBarRecordsContainer) {
		//修改滚动图片按钮的状态
		DomUtils.getElement(this.controlBar, null, "selectedRecord_" + this.scrollIndex).style.display = 'none';
		DomUtils.getElement(this.controlBar, null, "unselectedRecord_" + this.scrollIndex).style.display = 'inline-block';
		var selectedRecord = DomUtils.getElement(this.controlBar, null, "selectedRecord_" + index);
		selectedRecord.style.display = 'inline-block';
		DomUtils.getElement(this.controlBar, null, "unselectedRecord_" + index).style.display = 'none';
		//修改操作栏位置,使选中的按钮显示在操作栏中间
		if(this.controlBarRecordsContainerOverflow=='hidden' || this.controlBarRecordsContainerOverflow=='auto') { //允许滚动
			if(this.verticalScroll) {
				this.controlBarRecordsContainer.scrollTop = selectedRecord.offsetTop - (this.controlBarRecordsContainer.offsetHeight - selectedRecord.offsetHeight) / 2;
			}
			else {
				this.controlBarRecordsContainer.scrollLeft = selectedRecord.offsetLeft - (this.controlBarRecordsContainer.offsetWidth - selectedRecord.offsetWidth) / 2;
			}
		}
	}
	this.scrollIndex = index;
	this._setPagingButtonState();
};
SlideShow.prototype._setPagingButtonState = function() { //设置分页按钮状态
	if(!this.controlBarRecordsContainer) {
		return;
	}
	var isScrollBegin;
	var isScrollEnd;
	if(this.verticalScroll) {
		isScrollBegin = this.controlBarRecordsContainer.scrollTop==0;
		isScrollEnd = this.controlBarRecordsContainer.scrollTop + this.controlBarRecordsContainer.offsetHeight >= this.controlBarRecordsContainer.scrollHeight;
	}
	else {
		isScrollBegin = this.controlBarRecordsContainer.scrollLeft==0;
		isScrollEnd = this.controlBarRecordsContainer.scrollLeft + this.controlBarRecordsContainer.offsetWidth >= this.controlBarRecordsContainer.scrollWidth;
	}
	var previousPageButton = DomUtils.getElement(this.controlBar, null, 'previousPageButton');
	if(previousPageButton) {
		if(isScrollBegin) {
			CssUtils.setGray(previousPageButton);
		}
		else {
			CssUtils.removeGray(previousPageButton);
		}
	}
	var nextPageButton = DomUtils.getElement(this.controlBar, null, 'nextPageButton');
	if(nextPageButton) {
		if(isScrollEnd) {
			CssUtils.setGray(nextPageButton);
		}
		else {
			CssUtils.removeGray(nextPageButton);
		}
	}
};
SlideShow.prototype.startAutoSwitch = function() { //启动自动切换
	if(!this.manualSwitch) { //不是手动切换
		var slideShow = this;
		this.scrollTimerId = window.setInterval(function() {
			slideShow._slide(slideShow.scrollIndex < slideShow.recordCount-1 ? slideShow.scrollIndex + 1 : 0);
		}, this.speed); //定时切换
	}
};
SlideShow.prototype.stopAutoSwitch = function() { //停止自动切换
	if(this.scrollTimerId) {
		window.clearInterval(this.scrollTimerId);
	}
};
SlideShow.prototype.appendSlideShowList = function() { //添加到列表
	for(var i=0; i<(window.slideShowList ? window.slideShowList.length : 0); i++) {
		if(window.slideShowList[i].id==this.slideShowId) {
			window.slideShowList[i].slideShow = this;
			return;
		}
	}
	if(!window.slideShowList) {
		window.slideShowList = [];
	}
	window.slideShowList[window.slideShowList.length] = {id:this.slideShowId, slideShow:this};
};
SlideShow._getSlideShow = function(slideShowId) { //获取SlideShow对象
	for(var i=0; i<(window.slideShowList ? window.slideShowList.length : 0); i++) {
		if(window.slideShowList[i].id==slideShowId) {
			return window.slideShowList[i].slideShow;
		}
	}
};
SlideShow.slide = function(slideShowId, index) { //切换记录
	var slideShow = SlideShow._getSlideShow(slideShowId);
	if(slideShow) {
		slideShow._slide(index);
	}
};
SlideShow.openLink = function(slideShowId, index) { //打开链接
	var slideShow = SlideShow._getSlideShow(slideShowId);
	if(slideShow) {
		slideShow.openLink(index);
	}
};
SlideShow.showNextPreviousButton = function(slideShowId, show, src, event) { //显示或者隐藏“上一图片”“下一图片”按钮
	var slideShow = SlideShow._getSlideShow(slideShowId);
	if(!slideShow) {
		return;
	}
	var showPreviousImageButton = show, showNextImageButton = show;
	if(show) {
		var pos = DomUtils.getAbsolutePosition(src, null, true);
		showPreviousImageButton = event.clientX < pos.left + src.offsetWidth / 2;
		showNextImageButton = !showPreviousImageButton;
	}
	var previousImageButton = DomUtils.getElement(slideShow.pageElement, 'div', 'previousImageButton');
	if(previousImageButton) {
		previousImageButton.style.visibility = showPreviousImageButton ? 'visible' : 'hidden';
	}
	var nextImageButton = DomUtils.getElement(slideShow.pageElement, 'div', 'nextImageButton');
	if(nextImageButton) {
		nextImageButton.style.visibility = showNextImageButton ? 'visible' : 'hidden';
	}
};
SlideShow.onClickImage = function(slideShowId, imageClickAction, src, event) { //点击图片
	var slideShow = SlideShow._getSlideShow(slideShowId);
	if(slideShow.controlBar && slideShow.controlBar.contains(event.srcElement)) {
		return;
	}
	if(imageClickAction=="nextImage") {
		SlideShow.slideToNextImage(slideShowId, true);
	}
	else {
		var pos = DomUtils.getAbsolutePosition(src, null, true);
		SlideShow.slideToNextImage(slideShowId, (event.clientX ? event.clientX : event.x) >= pos.left + src.offsetWidth / 2);
	}
};
SlideShow.slideToNextImage = function(slideShowId, next) { //切换上一图片或者下一图片
	var slideShow = SlideShow._getSlideShow(slideShowId);
	if(!slideShow) {
		return;
	}
	if(next && slideShow.scrollIndex < slideShow.recordCount-1) {
		slideShow._slide(slideShow.scrollIndex + 1);
	}
	else if(!next && slideShow.scrollIndex > 0) {
		slideShow._slide(slideShow.scrollIndex - 1);
	}
};
SlideShow.gotoPage = function(slideShowId, next) { //翻页
	var slideShow = SlideShow._getSlideShow(slideShowId);
	if(!slideShow) {
		return;
	}
	if(slideShow.verticalScroll) {
		slideShow.controlBarRecordsContainer.scrollTop += (next ? 1 : -1) * slideShow.controlBarRecordsContainer.offsetHeight;
	}
	else {
		slideShow.controlBarRecordsContainer.scrollLeft += (next ? 1 : -1) * slideShow.controlBarRecordsContainer.offsetWidth;
	}
	slideShow.controlBarRecordsContainer.style.overflow = "auto";
	slideShow.controlBarRecordsContainer.style.overflow = "hidden";
	slideShow._setPagingButtonState();
};

//滚动图片：头尾衔接
JoinScrollMarquee = function(marqueeElenent, marqueeContentElement, scrollAmount, speed) {
	this.marqueeElenent = marqueeElenent;
	this.marqueeContentElement = marqueeContentElement;
	this.scrollAmount = scrollAmount;
	this.speed = speed;
	
	var joinScrollMarquee = this;
	//添加事件处理
	marqueeElenent.onmouseover = function() {
		window.clearTimeout(joinScrollMarquee.marqueeTimer);
	};
	marqueeElenent.onmouseout = function() {
		joinScrollMarquee.scroll();
	}
	this.scroll();
};

//滚动
JoinScrollMarquee.prototype.scroll = function() {
	if(this.marqueeElenent.scrollLeft >= this.marqueeContentElement.offsetWidth/2) {
		this.marqueeElenent.scrollLeft -= this.marqueeContentElement.offsetWidth/2;
	}
	else {
		this.marqueeElenent.scrollLeft += this.scrollAmount;
	}
	var joinScrollMarquee = this;
	window.clearTimeout(this.marqueeTimer);
	this.marqueeTimer = window.setTimeout(function() {
		joinScrollMarquee.scroll();
	}, this.speed);
};