//数据更新类, loadMoreAfterTouchEnd:是否在松开时加载数据 refreshDisabled:是否禁止刷新
Updater = function(scroller, loadMoreAfterTouchEnd, refreshDisabled) {
	this.scroller = scroller;
	this.loadMoreAfterTouchEnd = loadMoreAfterTouchEnd;
	this.document = scroller.pageElement.ownerDocument;
	this.refreshDisabled = refreshDisabled;
	this.document.updater = this;
	var updater = this;
	//绑定滚动条事件
	scroller.onScrolling = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd, notTouch) {
		if(notTouch) {
			return;
		}
		updater.onContentPageScrolling(x, y, isLeft, isRight, isTop, isBottom, touchEnd)
	};
	scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		updater.onAfterContentPageScroll(x, y, isLeft, isRight, isTop, isBottom, touchEnd);
	};
};
Updater.prototype.createRefreshBar = function(y, refreshStarted) { //创建刷新栏
	if(this.refreshDisabled || !this.document.loadMoreEnabled) {
		return;
	}
	if(this.refreshBar) {
		this.destoryRefreshBar();
	}
	this.refreshBar = new RefreshBar(this.scroller, true, refreshStarted);
	this.refreshBar.create();
};
Updater.prototype.destoryRefreshBar = function() { //销毁刷新栏
	if(this.refreshBar) {
		this.refreshBar.destory();
		this.refreshBar = null;
	}
};
Updater.prototype.onContentPageScrolling = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd, notTouch) { //内容页面滚动
	if(this.refreshBar) {
		this.refreshBar.scrolling(y, isTop, isBottom);
	}
	else if(this.loadMoreBar) {
		this.loadMoreBar.scrolling(y, isTop, isBottom);
	}
	else if(isTop && !touchEnd) { //已经滚动到顶部
		this.createRefreshBar(false);
	}
	else if(isBottom && this.document.loadMoreEnabled) { //已经滚动到底部
		if(!this.loadMoreAfterTouchEnd) { //不等触摸结束后加载更多
			this.loadMoreBar = new RefreshBar(this.scroller, false, true);
			this.loadMoreBar.create();
			this.loadMore(); //加载更多
		}
		else if(!touchEnd) { //不是惯性滚动	
			this.loadMoreBar = new RefreshBar(this.scroller, false, false);
			this.loadMoreBar.create();
		}
	}
};
Updater.prototype.onAfterContentPageScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) { //内容页面滚动结束
	if(this.refreshBar && !this.refreshBar.refreshStarted) {
		if(!isTop) {
			this.destoryRefreshBar(this.document);
		}
		else {
			this.refreshBar.startRefresh();
			window.top.client.callNativeMethod("refreshPage('" + this.document.location.href + "', '" + this.document.cookie + "')"); //请客户端重建加载页面
		}
	}
	else if(this.loadMoreBar) {
		if(isBottom && (!this.loadMoreBar.refreshStarted || new Date().valueOf()-this.loadMoreBar.created>8000)) { //超过8s，允许重新请求数据
			this.loadMoreBar.startRefresh();
			this.loadMore(); //加载更多
 		}
		else if(!this.loadMoreBar.refreshStarted) {
			this.loadMoreBar.destory();
			this.loadMoreBar = null;
		}
	}
};
Updater.prototype.loadMore = function() { //加载更多
	if(this.document.loadMore) {
		this.doFillMoreData();
		return;
	}
	//URL追加参数client.loadMore=true&client.recordCount_-1601058781=[已加载记录数]&client.lastRecordId_-1601058781=[最后的记录ID]
	var url = this.document.location.href;
	url += (url.indexOf('?')==-1 ? '?' : '&') + 'client.loadMore=true';
	DomUtils.traversalChildElements(this.document.body, function(element) { //遍历页面元素
		try {
			var attribute = element.getAttribute('TABINDEX');
			if(attribute && attribute.indexOf('paging=')==0) {
				var recordListCode = StringUtils.getPropertyValue(attribute, 'paging', '0');
				url += '&client.recordCount_' + recordListCode + '=' + StringUtils.getPropertyValue(attribute, 'recordCount', '0');
				url += '&client.lastRecordId_' + recordListCode + '=' + StringUtils.getPropertyValue(attribute, 'lastRecordId', '0');
				return true;
			}
		}
		catch(e) {
		
		}
	});
	this.loadMoreURL = url;
	//请客户端获取更多的记录
	window.top.client.callNativeMethod("loadMore('" + this.document.location.href + "', '" + url + "', '" + this.document.cookie + "')");
};
Updater.prototype.fillMoreData = function() { //填充数据
	if(!this.loadMoreBar) {
		return;
	}
	//创建IFRAME,放置返回的内容
	var iframeLoadMore = window.top.document.getElementById('iframeLoadMore');
	if(iframeLoadMore) {
		iframeLoadMore.updater = this;
		iframeLoadMore.src = this.loadMoreURL;
		return;
	}
	iframeLoadMore = window.top.document.createElement("iframe");
	iframeLoadMore.id = 'iframeLoadMore';
	iframeLoadMore.style.display = 'none';
	iframeLoadMore.src = this.loadMoreURL;
	iframeLoadMore.updater = this;
	iframeLoadMore.onload = function() { //加载完成
		this.updater.doFillMoreData();
	};
	window.top.document.body.insertBefore(iframeLoadMore, window.top.document.body.childNodes[0]);
};

Updater.prototype.doFillMoreData = function() { //执行填充数据
	//设置加载时间
	this.document.loadMoreTime = new Date();
	//销毁“加载更多栏”
	this.loadMoreBar.destory();
	this.loadMoreBar = null;
	//更新数据
	if(this.document.loadMore) {
		this.document.loadMore.call(null);
	}
	else {
		//遍历页面,更新数据
		var moreDataDocument = window.top.document.getElementById('iframeLoadMore').contentWindow.document;
		DomUtils.traversalChildElements(this.document.body, function(element) { //遍历页面元素
			var attribute;
			try {
				attribute = element.getAttribute('TABINDEX');
			}
			catch(e) {
				return;
			}
			if(!attribute || attribute.indexOf('paging=')!=0) {
				return;
			}
			var recordListCode = StringUtils.getPropertyValue(attribute, 'paging', '0');
			var moreData;
			var recordCount;
			var lastRecordId;
			DomUtils.traversalChildElements(moreDataDocument.body, function(element) { //遍历数据页面,获取新数据
				var attribute;
				try {
					attribute = element.getAttribute('TABINDEX');
				}
				catch(e) {
					return;
				}
				if(!attribute || attribute.indexOf('paging=')!=0) {
					return;
				}
				moreData = element;
				recordCount = Number(StringUtils.getPropertyValue(attribute, 'recordCount', '0'));
				lastRecordId = StringUtils.getPropertyValue(attribute, 'lastRecordId', '0');
				return true;
			});
			if(!moreData) {
				return true;
			}
			var nodes = moreData.childNodes;
			var length = nodes ? nodes.length : 0;
			for(var i=0; i<length; i++) {
				element.appendChild(nodes[i].cloneNode(true));
			}
			moreData.innerHTML = "";
			//更新TABINDEX属性
			element.setAttribute('TABINDEX', 'paging=' + recordListCode + '&recordCount=' + (Number(StringUtils.getPropertyValue(attribute, 'recordCount', '0')) + recordCount) + '&lastRecordId=' + lastRecordId);
			return true;
		});
		moreDataDocument.body.innerHTML = "";
	}
	this.scroller.showBar(); //重新显示滚动条
};

RefreshBar = function(scroller, isRefreshBar, refreshStarted) {
	this.parentElement = scroller.pageElement;
	this.scroller = scroller;
	this.isRefreshBar = isRefreshBar;
	this.refreshStarted = refreshStarted;
};
RefreshBar.prototype.create = function() {
	var now = new Date();
	this.created = now.valueOf();
	var lastModified = this.parentElement.ownerDocument.pageLastModified;
	if(!this.isRefreshBar && this.parentElement.ownerDocument.loadMoreTime) { //加载更多
		lastModified = this.parentElement.ownerDocument.loadMoreTime;
	}
	var timeText;
	var interval = now.valueOf() - lastModified.valueOf();
	if(interval < 60 * 1000) {  //小于1分钟
		timeText = Math.ceil(interval/1000) + "秒钟前";
	}
	else if(interval < 3600 * 1000) {  //小于60分钟
		timeText = Math.round(interval/(60*1000)) + "分钟前";
	}
	else if(interval < 24 * 3600 * 1000) { //小于24小时
		timeText = Math.round(interval/(3600*1000)) + "小时前";
	}
	else {
		timeText = (lastModified.getMonth() + 1) + "-" + lastModified.getDate() + " " + lastModified.getHours() + ":" + lastModified.getMinutes();
	}
	this.scrollingPrompt = (this.isRefreshBar ? '下拉可以刷新, ' : '上拉加载更多, ')  + timeText + '已加载';
	this.topOrBottomPrompt = (this.isRefreshBar ? '松开开始刷新, ' : '上拉加载更多, ') + timeText + '已刷新';
	this.refreshPrompt = this.isRefreshBar ? "正在刷新..." : "正在加载...";
	
	//创建刷新栏
	this.bar = this.parentElement.ownerDocument.createElement('div');
	this.bar.style.width = this.parentElement.offsetWidth + 'px';
	this.bar.style.marginLeft = this.parentElement.scrollLeft + 'px';
	this.bar.style.clear = "both";
	this.bar.style.overflow = "hidden";
	this.bar.className = this.isRefreshBar ? "refreshBar" : "loadMoreBar";
	if(!this.refreshStarted) {
		this.bar.style.height = '1px';
	}
	
	//创建图片DIV
	this.imageSpan = this.parentElement.ownerDocument.createElement('span');
	this.imageSpan.innerHTML = "&nbsp;";
	this.bar.appendChild(this.imageSpan);
	
	//创建提示文本DIV
	this.promptSpan = this.parentElement.ownerDocument.createElement('span');
	this.promptSpan.className = "promptText";
	this.promptSpan.innerHTML = this.refreshStarted ? this.refreshPrompt : this.scrollingPrompt;
	this.bar.appendChild(this.promptSpan);
	
	if(this.refreshStarted) {
		this.startRefresh(true);
	}
	else {
		this.imageSpan.className = "scrollingImage";
		if(!this.isRefreshBar) {
			CssUtils.setDegree(this.imageSpan, 180);
		}
	}
	if(!this.isRefreshBar || !this.parentElement.childNodes || this.parentElement.childNodes.length==0) {
		this.parentElement.appendChild(this.bar);
	}
	else {
		this.parentElement.insertBefore(this.bar, this.parentElement.childNodes[0]);
	}
};
RefreshBar.prototype.destory = function() {
	var height = this.bar.clientHeigh;
	this.bar.parentNode.removeChild(this.bar);
	if(this.scroller) {
		this.scroller.scroll(0, this.isRefreshBar ? -height : 1);
	}
};
RefreshBar.prototype.scrolling = function(y, isTop, isBottom) {
	this.bar.style.marginLeft = this.parentElement.scrollLeft + 'px';
	if(this.refreshStarted) {
		return;
	}
	if((this.isRefreshBar && isTop) || (!this.isRefreshBar && isBottom)) {
		this.bar.style.height = Math.ceil(this.bar.clientHeight + (this.isRefreshBar && isTop ? -1 : 1)  * (y / 2 * (window.top.client.clientHeight - this.bar.clientHeight * 5) / window.top.client.clientHeight)) + 'px';
	}
	var prompt = isTop || isBottom ? this.topOrBottomPrompt : this.scrollingPrompt;
	if(this.promptSpan.innerHTML!=prompt) {
		this.promptSpan.innerHTML = prompt;
		CssUtils.rotate(this.imageSpan, 10, 10, this.isRefreshBar ? (isTop ? 180 : 360) : (isBottom ? 360 : 180));
	}
};
RefreshBar.prototype.setScrollingImageRotate = function(rotate, animatic) {
	var imageSpan = this.imageSpan;
	var transform = function(rotate) {
		imageSpan.style.cssText = '-moz-transform: rotate(' + rotate + 'deg);' +
	    						  '-o-transform: rotate(' + rotate + 'deg);' +
	   							  '-webkit-transform: rotate(' + rotate + 'deg);' +
	    						  'transform: rotate(' + rotate + 'deg);';
    };
    if(!animatic) {
    	transform.call(null, rotate);
    	return;
    }
	var currentRotate = 0;
	var match;
	if(imageSpan.style.cssText && (match = imageSpan.style.cssText.match(/rotate\([^\)]*deg\)/i))) {
		match = match.toString();	
		currentRotate = Number(match.substring('rotate('.length, match.length - 'deg)'.length));
	}
	var step = rotate > currentRotate ? 10 : -10;
	if(this.rotateTimer && this.rotateTimer!=0) {
		window.clearInterval(this.rotateTimer);
	}
	var refreshBar = this;
	this.rotateTimer = window.setInterval(function() {
		currentRotate += step;
		if((step<0 && currentRotate<=rotate) || (step>0 && currentRotate>=rotate)) {
			window.clearInterval(refreshBar.rotateTimer);
			refreshBar.rotateTimer = 0;
			transform.call(null, rotate >= 360 ? 0 : (rotate<0 ? 0 : rotate));
			return;
		}
		transform.call(null, currentRotate);
	}, 10);
};
RefreshBar.prototype.startRefresh = function(force) {
	if(this.refreshStarted && !force) {
		return;
	}
	this.bar.style.height = '';
	this.refreshStarted = true;
	this.imageSpan.className = "loadingImage rotate";
	this.promptSpan.innerHTML = this.refreshPrompt;
};