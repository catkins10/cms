var SCROLL_MIN_STEP = 8;
var SCROLL_START_STEP = 20; //触摸后,移动距离超过step后才开始滚动
var SCROLL_SIDE_WIDTH = 30; //边距
//滚动条,Android 3.0以下overflow:auto无效
//在滚动事件: onScrolling=function(x, y, isLeft, isRight, isTop, isBottom, touchEnd, notTouch)
//滚动结束事件: onAfterScroll=function(x, y, isLeft, isRight, isTop, isBottom, touchEnd)
//从边框开始滚动事件: onScrollingFromSide=function(x, y, startX, startY, isLeft, isRight, isTop, isBottom),如果有做处理返回true
//从边框开始滚动结束事件: onAfterScrollFromSide=function(x, y, startX, startY, isLeft, isRight, isTop, isBottom)
//参数inertiaDisabled: 是否禁止惯性滚动
Scroller = function(pageElement, horizontalOnly, verticalOnly, showScrollBar, alwaysShowScrollBar, twoWay, inertiaDisabled) {
	var scollerDivision;
	if(pageElement.tagName=='BODY' && (scollerDivision=pageElement.ownerDocument.getElementById('scollerDivision'))) {
		pageElement = scollerDivision;
		this._resetScrollerElement(scollerDivision);
	}
	else if(pageElement.id=='contentArea') {
		pageElement.style.display = 'none';
		this._resetScrollerElement(pageElement);
		pageElement.style.display = '';
	}
	pageElement.scroller = this;
	this.pageElement = pageElement;
	this.horizontalOnly = horizontalOnly;
	this.verticalOnly = verticalOnly;
	this.showScrollBar = showScrollBar;
	this.alwaysShowScrollBar = alwaysShowScrollBar;
	this.twoWay = twoWay;
	this.inertiaDisabled = inertiaDisabled;
	this.parentScroller = null;
	for(var obj = this.pageElement; obj; obj = obj.parentNode) {
		if(obj!=this.pageElement && obj.scroller) {
			this.parentScroller = obj.scroller;
			break;
		}
		if(obj.tagName=='BODY' && !(obj=(obj.ownerDocument.parentWindow ? obj.ownerDocument.parentWindow : obj.ownerDocument.defaultView).frameElement)) {
			break;
		}
	}
	var scroller = this;
	this.pageElement.ontouchstart = function(event) {
		event.preventDefault();
		return scroller._ontouchstart(event);
	};
	this.pageElement.ontouchmove = function(event) {
		event.preventDefault();
		return scroller._ontouchmove(event);
	};
	this.pageElement.ontouchend = function(event) {
		event.preventDefault();
		return scroller._ontouchend(event);
	};
	this.pageElement.ondragstart = function(event) {
		event.preventDefault();
		return false;
	};
	if(alwaysShowScrollBar) {
		window.setTimeout(function() {
			scroller.showBar();
		}, 800);
	}
};
Scroller.prototype._resetScrollerElement = function(pageElement) { //重置页面元素
	pageElement.style.overflow = 'hidden';
	var parentElement = pageElement.parentNode;
	var values = [['Left', DomUtils.getSpacing(parentElement, 'left')], ['Right', DomUtils.getSpacing(parentElement, 'right')], ['Top', DomUtils.getSpacing(parentElement, 'top')], ['Bottom', DomUtils.getSpacing(parentElement, 'bottom')]];
	if(parentElement.style.width && parentElement.style.width.indexOf('px')!=-1) {
		pageElement.style.width =  parentElement.style.width;
		pageElement.style.height = parentElement.style.height;
	}
	else {
		pageElement.style.width =  ((parentElement.tagName=='BODY' ? parentElement.ownerDocument.documentElement.clientWidth : parentElement.offsetWidth) - values[0][1] - values[1][1]) + 'px';
		pageElement.style.height = ((parentElement.tagName=='BODY' ? parentElement.ownerDocument.documentElement.clientHeight : parentElement.offsetHeight) - values[2][1] - values[3][1]) + 'px';
	}
	for(var i=0; i<4; i++) {
		if(values[i][1]!=0) {
			eval("parentElement.style.margin" + values[i][0] + " = '0px'; parentElement.style.padding" + values[i][0] + " = '0px'; pageElement.style.margin" + values[i][0] + " = values[i][1] + 'px';");
		}
	}
};
try {
	Scroller.prototype.__defineSetter__("onScrolling", function(val) {
		if(!this.onScrollingEventFunctions) {
			this.onScrollingEventFunctions = [];
		}
		this.onScrollingEventFunctions.push(val);
	});
	Scroller.prototype.__defineSetter__("onAfterScroll", function(val) {
		if(!this.onAfterScrollEventFunctions) {
			this.onAfterScrollEventFunctions = [];
		}
		this.onAfterScrollEventFunctions.push(val);
	});
	Scroller.prototype.__defineSetter__("onScrollingFromSide", function(val) {
		if(!this.onScrollingFromSideEventFunctions) {
			this.onScrollingFromSideEventFunctions = [];
		}
		this.onScrollingFromSideEventFunctions.push(val);
	});
	Scroller.prototype.__defineSetter__("onAfterScrollFromSide", function(val) {
		if(!this.onAfterScrollFromSideEventFunctions) {
			this.onAfterScrollFromSideEventFunctions = [];
		}
		this.onAfterScrollFromSideEventFunctions.push(val);
	});
}
catch(e) {

}
Scroller.prototype._getParentScroller = function() { //获取父滚动条
	for(var obj = this.pageElement; obj; obj = obj.parentNode) {
		if(obj!=this.pageElement && obj.scroller) {
			return obj.scroller;
		}
		if(obj.tagName=='BODY' && !(obj=(obj.ownerDocument.parentWindow ? obj.ownerDocument.parentWindow : obj.ownerDocument.defaultView).frameElement)) {
			break;
		}
	}
	return null;
}
Scroller.prototype._isScrolled = function(type, parentScrollerOnly) { //检查是否有滚动过, type: horizontal/vertical/side
	if(!parentScrollerOnly) {
		var scolled;
		if(!type) {
			scolled = this.horizontalScroll || this.verticalScroll || this.sideScroll;
		}
		else {
			eval('scolled = this.' + type + 'Scroll;');
		}
		if(scolled) {
			return true;
		}
	}
	var parentScroller = this._getParentScroller();
	return parentScroller && parentScroller._isScrolled(type, false);
};
Scroller.prototype._ontouchstart = function(event) {
	if(event.stopPropagation) {
		event.stopPropagation();
	}
	else {
		event.cancelBubble = true;
	}
	var touch = event.touches[0];
	this._resetScroller();
	this.beginTime = new Date().getTime();
	this.previousX = touch.screenX;
	this.previousY = touch.screenY;
	this.startX = touch.screenX;
	this.startY = touch.screenY;
	this.horizontalScroll = false;
	this.verticalScroll = false;
	this.sideScroll = false;
	this.parentScroller = this._getParentScroller();
	if(this.inertiaScrollTimer && this.inertiaScrollTimer>0) {
		window.clearInterval(this.inertiaScrollTimer);
		this.inertiaScrollTimer = 0;
	}
	return false;
};
Scroller.prototype._ontouchmove = function(event) {
	if(event.stopPropagation) {
		event.stopPropagation();
	}
	else {
		event.cancelBubble = true;
	}
	var touch = event.touches[0];
	var x = this.previousX - touch.screenX;
	var y = this.previousY - touch.screenY;
	x = Math.abs(x) < SCROLL_MIN_STEP ? 0 : x;
	y = Math.abs(y) < SCROLL_MIN_STEP ? 0 : y;
	if(x==0 && y==0) {
		return;
	}
	if(this._doSideScroll(this, x, y) || this._doScroll(x, y, SCROLL_START_STEP, this.horizontalOnly, this.verticalOnly)) {
		if(x!=0) {
			this.previousX = touch.screenX;
		}
		if(y!=0) {
			this.previousY = touch.screenY;
		}
	}
};
Scroller.prototype._ontouchend = function(event) {
	if(!this._isScrolled()) {
		EventUtils.clickElement(event.srcElement);
		return;
	}
	if(event.stopPropagation) {
		event.stopPropagation();
	}
	else {
		event.cancelBubble = true; 
	}
	if(this._isScrolled('side')) { //从边框滚动
		this._doAfterSideScroll(this);
		return;
	}
	var time = new Date().getTime() - this.beginTime;
	var inertiaTime = 300;
	if(time>inertiaTime || (this.inertiaDisabled && (this.horizontalScroll || this.verticalScroll))) { //触摸开始到结束时间大于300毫秒
		this._doAfterScroll(this);
		return;
	}
	//触摸开始到结束时间小于300毫秒,惯性滚动
	var scroller = this;
	var inertiaScroller = this;
	for(; !inertiaScroller.horizontalScroll && !inertiaScroller.verticalScroll; inertiaScroller=inertiaScroller._getParentScroller());
	var count = 0;
	var distance = Math.min(3000, (inertiaScroller.horizontalScroll ? inertiaScroller.pageElement.scrollWidth : inertiaScroller.pageElement.scrollHeight) / Math.max(time/inertiaTime*10, 3)); //最多移动3000px
	var step = Math.round(5 + 55 * distance/3000); //步长5～60
	var times = distance / step;
	var xStep = !inertiaScroller.horizontalScroll ? 0 : (inertiaScroller.startX > this.previousX ? step : -step);
	var yStep = !inertiaScroller.verticalScroll ? 0 : (inertiaScroller.startY > this.previousY ? step : -step);
	this.inertiaScrollTimer = window.setInterval(function() {
		inertiaScroller.scroll(xStep, yStep, true, true);
		if((++count)<times &&
		   (xStep==0 || (!inertiaScroller.isLeft && !inertiaScroller.isRight)) &&
		   (yStep==0 || (!inertiaScroller.isTop && !inertiaScroller.isBottom))) {
			return;
		}
		window.clearInterval(scroller.inertiaScrollTimer);
		scroller.inertiaScrollTimer = 0;
		scroller._doAfterScroll(scroller);
	}, 10);
};
Scroller.prototype._resetScroller = function() {
	this.horizontalScroll = false;
	this.verticalScroll = false;
	this.sideScroll = false;
	var parentScroller = this._getParentScroller();
	if(parentScroller) {
		parentScroller._resetScroller();
	}
};
Scroller.prototype._doSideScroll = function(srcScoller, x, y) { //从边框滚动,递归函数
	if(!this._isScrolled('side') && Math.abs(x)<SCROLL_START_STEP && Math.abs(y)<SCROLL_START_STEP) {
		return false;
	}
	var isLeft = this.sideScroll ? this.isLeft : x!=0 && srcScoller.startX <= SCROLL_SIDE_WIDTH;
	var isRight = this.sideScroll ? this.isRight : x!=0 && srcScoller.startX >= top.document.body.clientWidth - SCROLL_SIDE_WIDTH;
	var isTop = this.sideScroll ? this.isTop : y!=0 && !isLeft && !isRight && srcScoller.startY <= SCROLL_SIDE_WIDTH;
	var isBottom = this.sideScroll ? this.isBottom : y!=0 && !isLeft && !isRight && srcScoller.startY >= top.document.body.clientHeight - SCROLL_SIDE_WIDTH;
	if(!isLeft && !isRight && isTop && !isBottom) {
		return false;
	}
	//检查当前滚动条以及父滚动条是否有从边框开始滚动事件
	for(var i=0; i<(!this.onScrollingFromSideEventFunctions ? 0 : this.onScrollingFromSideEventFunctions.length); i++) {
		if(this.onScrollingFromSideEventFunctions[i].call(srcScoller, x, y, srcScoller.startX, srcScoller.startY, isLeft, isRight, isTop, isBottom) && !this.sideScroll) {
			this.sideScroll = true;
			this.isLeft = isLeft;
			this.isRight = isRight;
			this.isTop = isTop;
			this.isBottom = isBottom;
		}
	}
	if(this.sideScroll) {
		return true;
	}
	var parentScroller = this._getParentScroller();
	return parentScroller && parentScroller._doSideScroll(srcScoller, x, y);
};
Scroller.prototype._doAfterSideScroll = function(srcScoller) { //结束从边框滚动
	for(var i=0; i<(!this.sideScroll || !this.onAfterScrollFromSideEventFunctions ? 0 : this.onAfterScrollFromSideEventFunctions.length); i++) {
		this.onAfterScrollFromSideEventFunctions[i].call(this, srcScoller.startX - srcScoller.previousX, srcScoller.startY - srcScoller.previousY, srcScoller.startX, srcScoller.startY, this.isLeft, this.isRight, this.isTop, this.isBottom, true);
	}
	var parentScroller = this._getParentScroller();
	if(parentScroller) {
		parentScroller._doAfterSideScroll(srcScoller);
	}
};
Scroller.prototype._doScroll = function(x, y, startStep, horizontalOnly, verticalOnly) { //水平或者垂直滚动,递归函数
	if(this._isScrolled(null, true)) { //父滚动条已经开始滚动
		var parentScroller = this._getParentScroller();
		if(parentScroller) {
			parentScroller._doScroll(x, y, startStep, parentScroller.horizontalScroll, parentScroller.verticalScroll);
		}
		return true;
	}
	if(!verticalOnly && !this.horizontalScroll && (!this.verticalScroll || this.twoWay) && Math.abs(x)>=startStep) { //不是仅垂直滚动,未开始水平滚动,且未开始垂直滚动或者允许双向滚动,且移动距离大于启动步长
		this.horizontalScroll = true;
	}
	if(!horizontalOnly && !this.verticalScroll && (!this.horizontalScroll || this.twoWay) && Math.abs(y)>=startStep) { //不是仅水平滚动,未开始垂直滚动,且未开始水平滚动或者允许双向滚动,且移动距离大于启动步长
		this.verticalScroll = true;
	}
	var scrolled = this.horizontalScroll || this.verticalScroll;
	if(scrolled) {
		this.scroll(this.horizontalScroll ? x : 0, this.verticalScroll ? y : 0, false, true);
	}
	if((!scrolled && (this.verticalOnly || this.horizontalOnly)) || 
	   (horizontalOnly=(this.horizontalScroll && (this.isLeft || this.isRight))) ||
	   (verticalOnly=(this.verticalScroll && (this.isTop || this.isBottom)))) {
		var parentScroller = this._getParentScroller();
		if(parentScroller) {
			if(!parentScroller.horizontalScroll && !parentScroller.verticalScroll) {
				parentScroller.startX = this.previousX;
				parentScroller.startY = this.previousY;
			}
			scrolled = parentScroller._doScroll(x, y, scrolled ? 1 : SCROLL_START_STEP, parentScroller.horizontalOnly || (scrolled && horizontalOnly), parentScroller.verticalOnly || (scrolled && verticalOnly)) || scrolled;
		}
	}
	return scrolled;
};
Scroller.prototype._doAfterScroll = function(srcScroller) { //结束滚动
	for(var i=0; i<((!this.verticalScroll && !this.horizontalScroll) || !this.onAfterScrollEventFunctions ? 0 : this.onAfterScrollEventFunctions.length); i++) {
		this.onAfterScrollEventFunctions[i].call(null, this.startX - srcScroller.previousX, this.startY - srcScroller.previousY, this.isLeft, this.isRight, this.isTop, this.isBottom, false);
	}
	var parentScroller = this._getParentScroller();
	if(parentScroller) {
		parentScroller._doAfterScroll(srcScroller);
	}
};
Scroller.prototype.scroll = function(x, y, touchEnd, byTouch) { //touchEnd:是否结束触摸
	if(!this.verticalOnly && x!=0) {
		var left = Math.round(this.pageElement.scrollLeft + x);
		this.pageElement.scrollLeft = left;
		this.isTop = this.verticalScroll && this.isTop;
		this.isBottom = this.verticalScroll && this.isBottom;
		this.isLeft = this.pageElement.scrollLeft > left;
		this.isRight = this.pageElement.scrollLeft < left;
		this.showBar();
	}
	if(!this.horizontalOnly && y!=0) {
		var top = Math.round(this.pageElement.scrollTop + y);
		this.pageElement.scrollTop = top;
		this.isLeft = this.horizontalScroll && this.isLeft;
		this.isRight = this.horizontalScroll && this.isRight;
		this.isTop = this.pageElement.scrollTop > top;
		this.isBottom = this.pageElement.scrollTop < top;
		this.showBar();
	}
	for(var i=0; i<(!this.onScrollingEventFunctions ? 0 : this.onScrollingEventFunctions.length); i++) {
		this.onScrollingEventFunctions[i].call(null, x, y, this.isLeft, this.isRight, this.isTop, this.isBottom, touchEnd, !byTouch);
	}
};
Scroller.prototype.scrollTo = function(top, left) { //滚动到指定位置
	this.pageElement.scrollLeft = left;
	this.pageElement.scrollTop = top;
	this.showBar();
};
Scroller.prototype.showBar = function() {
	if(!this.showScrollBar) {
		return;
	}
	//启动隐藏滚动条定时器
	if(!this.alwaysShowScrollBar) {
		var scroller = this;
		if(this.hideBarTimer) {
			window.clearTimeout(this.hideBarTimer);
		}
		this.hideBarTimer = window.setTimeout(function() {
			scroller.hideBar();
		}, 2000);
	}
	if(!this.horizontalOnly && (this.isVerticalBarVisible || this.alwaysShowScrollBar || this.verticalScroll)) {
		if(this.pageElement.scrollHeight <= this.pageElement.clientHeight) {
			if(this.verticalScroller && this.verticalScroller.parentNode) {
				this.verticalScroller.parentNode.removeChild(this.verticalScroller);
			}
			this.verticalScroller = null;
			this.isVerticalBarVisible = false;
		}		
		else {	
			if(!this.verticalScroller) { //创建垂直滚动条
				this._createScrollBar(true);
			}
			var height = Math.max(30, Math.round(this.verticalScroller.offsetHeight * this.pageElement.clientHeight / this.pageElement.scrollHeight));
			this.verticalScrollerTrack.style.height = height + "px";
			this.verticalScrollerTrack.style.top = Math.round((this.verticalScroller.offsetHeight - height) * (this.pageElement.scrollTop / (this.pageElement.scrollHeight - this.pageElement.clientHeight))) + "px";
			this.isVerticalBarVisible = true;
		}
	}
	if(!this.verticalOnly && (this.isHorizontalBarVisible || this.alwaysShowScrollBar || this.horizontalScroll)) {
		if(this.pageElement.scrollWidth <= this.pageElement.clientWidth) {
			if(this.horizontalScroller && this.horizontalScroller.parentNode) {
				this.horizontalScroller.parentNode.removeChild(this.horizontalScroller);
			}
			this.horizontalScroller = null;
			this.isHorizontalBarVisible = false;
		}
		else {
			if(!this.horizontalScroller) { //创建水平滚动条
				this._createScrollBar(false);
			}
			var width = Math.max(30, this.horizontalScroller.offsetWidth * this.pageElement.clientWidth / this.pageElement.scrollWidth);
			this.horizontalScrollerTrack.style.width = width + "px";
			this.horizontalScrollerTrack.style.left = Math.round((this.horizontalScroller.offsetWidth - width) * (this.pageElement.scrollLeft / (this.pageElement.scrollWidth - this.pageElement.clientWidth))) + "px";
			this.isHorizontalBarVisible = true;
		}
	}
};
Scroller.prototype._createScrollBar = function(vertical) {
	var pos = DomUtils.getAbsolutePosition(this.pageElement);
	var divScroller = this.pageElement.ownerDocument.createElement("div");
	divScroller.style.position = "absolute";
	divScroller.style.zIndex = 1000;
	
	var marginLeft = CssUtils.getElementComputedStyle(this.pageElement, 'margin-left');
	var marginRight = CssUtils.getElementComputedStyle(this.pageElement, 'margin-right');
	var marginTop = CssUtils.getElementComputedStyle(this.pageElement, 'margin-top');
	var marginBottom = CssUtils.getElementComputedStyle(this.pageElement, 'margin-bottom');
	marginLeft = marginLeft ? Number(marginLeft.replace('px', '')) : 0;
	marginRight = marginRight ? Number(marginRight.replace('px', '')) : 0;
	marginTop = marginTop ? Number(marginTop.replace('px', '')) : 0;
	marginBottom = marginBottom ? Number(marginBottom.replace('px', '')) : 0;
	divScroller.style.top = (pos.top - marginTop + (vertical ? 0 : this.pageElement.clientHeight + marginTop + marginBottom)) + "px";
	divScroller.style.left = (pos.left - marginLeft + (!vertical ? 0 : this.pageElement.clientWidth + marginLeft + marginRight)) + "px";
	divScroller.style.height = vertical ? (this.pageElement.clientHeight + marginTop + marginBottom) + "px" : "0px";
	divScroller.style.width = vertical ? "0px" : (this.pageElement.clientWidth + marginLeft + marginRight) + "px";
	this.pageElement.ownerDocument.body.appendChild(divScroller);
	
	var div = this.pageElement.ownerDocument.createElement("div");
	div.className = 'scrollBar ' + (vertical ? 'vertical' : 'horizontal')  + 'ScrollBar';
	if(vertical) {
		div.style.height = divScroller.style.height;
	}
	else {
		div.style.width = divScroller.style.width;
	}
	divScroller.appendChild(div)
	
	var track = this.pageElement.ownerDocument.createElement("div");
	track.style.position = "absolute";
	track.className = 'scrollerTrack ' + (vertical ? 'vertical' : 'horizontal')  + 'ScrollerTrack';
	div.appendChild(track);
	
	if(vertical) {
		this.verticalScroller = divScroller;
		this.verticalScrollerTrack = track;
	}
	else {
		this.horizontalScroller = divScroller;
		this.horizontalScrollerTrack = track;
	}
};
Scroller.prototype.hideBar = function() {
	if(this.alwaysShowScrollBar) {
		return;
	}
	var opacity = 100;
	var verticalScroller = this.verticalScroller;
	var horizontalScroller = this.horizontalScroller;
	this.verticalScroller = null;
	this.horizontalScroller = null;
	this.isHorizontalBarVisible = false;
	this.isVerticalBarVisible = false;
	var hideBarTimer = window.setInterval(function() {
		opacity -= 10;
		if(opacity<=0) {
			window.clearInterval(hideBarTimer);
		}
		if(verticalScroller) {
			if(opacity<=0) {
				if(verticalScroller.parentNode) {
					verticalScroller.parentNode.removeChild(verticalScroller);
				}
			}
			else if(document.all) {
				verticalScroller.style.filter = 'alpha(opacity=' + opacity + ');';
			}
			else {
				verticalScroller.style.opacity = (opacity / 100.0);
			}
		}
		if(horizontalScroller) {
			if(opacity<=0) {
				if(horizontalScroller.parentNode) {
					horizontalScroller.parentNode.removeChild(horizontalScroller);
				}
			}
			else if(document.all) {
				horizontalScroller.style.filter = 'alpha(opacity=' + opacity + ');';
			}
			else {
				horizontalScroller.style.opacity = (opacity / 100.0);
			}
		}
	}, 50);
};
Scroller.prototype.scrollWithTimer = function(targetPosition, verticalScroll, minStep, onScrollEnd) { //定时滚动
	var scroller = this;
	var scrollPosition = verticalScroll ? this.pageElement.scrollTop : this.pageElement.scrollLeft;
	var step = Math.max((minStep ? minStep : 10), Math.ceil(Math.abs(scrollPosition - targetPosition)/5));
	var previousPosition = 0.1;
	var scrollTimer = window.setInterval(function() {
		var position = verticalScroll ? scroller.pageElement.scrollTop : scroller.pageElement.scrollLeft;
		if(position==targetPosition || scrollPosition!=position || previousPosition==position) { //已经移动到位,或者用户手动移动过
			window.clearInterval(scrollTimer);
			if(onScrollEnd) {
				onScrollEnd.call();
			}
			return;
		}
		previousPosition = position;
		var size = (position<targetPosition ? Math.min(step, targetPosition - position) : -Math.min(step, position - targetPosition));
		scroller.scroll((verticalScroll ? 0 : size), (verticalScroll ? size : 0));
		scrollPosition = verticalScroll ? scroller.pageElement.scrollTop : scroller.pageElement.scrollLeft;
	}, 1);
};