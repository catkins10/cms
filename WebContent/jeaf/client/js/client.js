//客户端
Client = function(pilotEnabled, channelBar, columnBar, preferenceBar, columnContainer, channelIconSpan, channelIconWidth, channelIconHeight, channelNameSpan, columnIconSpan, columnIconWidth, columnIconHeight, columnNameSpan, showChannelBarButton, showColumnBarButton) {
	this.pilotEnabled = pilotEnabled && location.href.indexOf('client.firstRun=true')!=-1; //引导页面是否有效
	this.channelBar = channelBar; //频道栏
	this.columnBar = columnBar; //栏目栏
	this.preferenceBar = preferenceBar; //参数设置栏
	this.columnContainerElement = columnContainer; //内容容器
	this.channelIconSpan = channelIconSpan; //频道图标SPAN
	this.channelIconWidth = channelIconWidth; //频道图标宽度
	this.channelIconHeight = channelIconHeight; //频道图标高度
	this.channelNameSpan = channelNameSpan; //频道名称SPAN
	this.columnIconSpan = columnIconSpan; //栏目图标SPAN
	this.columnIconWidth = columnIconWidth; //栏目图标宽度
	this.columnIconHeight = columnIconHeight; //栏目图标高度
	this.columnNameSpan = columnNameSpan; //栏目名称SPAN
	this.showChannelBarButton = showChannelBarButton; //显示频道栏按钮
	this.showColumnBarButton = showColumnBarButton; //显示栏目栏按钮
	this.siteId = StringUtils.getPropertyValue(location.href, "siteId", "");
	
	this.clientWidth = document.documentElement.clientWidth; //宽度
	this.clientHeight = document.documentElement.clientHeight; //高度
	this.density = Number(StringUtils.getPropertyValue(window.location.href, "client.deviceWidthPixels", "")) / this.clientWidth; //密度
	
	EventUtils.addEvent(window, 'resize', function(event) {
		if(window.client.clientWidth != document.documentElement.clientWidth || window.client.clientHeight != document.documentElement.clientHeight) {
			window.client.clientWidth = document.documentElement.clientWidth; //宽度
			window.client.clientHeight = document.documentElement.clientHeight; //高度
			window.client.density = Number(StringUtils.getPropertyValue(window.location.href, "client.deviceWidthPixels", "")) / window.client.clientWidth; //密度
			document.body.style.width = window.client.clientWidth + 'px';
			document.body.style.height = window.client.clientHeight + 'px';
		}
	});
	
	//设置body样式,避免页面变形
	document.body.style.position = 'absolute';
	document.body.style.width = this.clientWidth + 'px';
	document.body.style.height = this.clientHeight + 'px';
	document.body.style.overflow = 'hidden';
	
	//创建触摸事件拦截器
	this.touchEventInterceptor = new TouchEventInterceptor();
	
	//客户端调用处理
	window.onBackKeyDown = function() { //客户端调用:手机返回键按下
		window.client.onBackKeyDown();
	};
	window.onSoftKeyboardAction = function(imeActionType) { //客户端调用:软键盘操作,imeActionType:next/search/go/done/send
		window.client.onSoftKeyboardAction(imeActionType);
	};
	window.showToast = function(message, showSecnonds) { //客户端调用:显示消息
		window.client.showToast(message, showSecnonds);
	};
	window.fileDownloadStart = function(url, fileSize) { //客户端调用:文件下载开始
		window.client.fileDownloadStart(url, fileSize);
	};
	window.fileDownloading = function(url, fileSize, completed, percentage) { //客户端调用:文件下载中
		window.client.fileDownloading(url, fileSize, completed, percentage);
	};
	window.fileDownloadComplete = function(url) { //客户端调用:文件下载完成
		window.client.fileDownloadComplete(url);
	};
	window.loadNewestPage = function(url) { //客户端调用:重新加载页面
		window.client.loadNewestPage(url);
	};
	window.hideRefreshingPrompt = function(url) { //客户端调用:隐藏刷新提示
		window.client.hideRefreshingPrompt(url);
	};
	window.updatePageLastModified = function(url) { //客户端调用:重设页面修改时间
		window.client.updatePageLastModified(url);
	};
	window.fillMoreData = function(url) { //客户端调用:通知页面更多数据已经加载完成
		window.client.fillMoreData(url);
	};
	window.getVolume = function(callback) { //获取音量, callback = function(volume);
		window.client.callNativeMethod('getVolume()', function(returnValue) {
			callback.call(null, Number(returnValue));
		});
	};
	window.setVolume = function(volume) { //设置音量
		if(window.volumeTimer) {
			return;
		}
		window.volumeTimer = window.setTimeout(function() {
			window.client.callNativeMethod('setVolume(' + volume + ')');
			window.volumeTimer = null;
		}, 50);
	};
	window.getBrightness = function(callback) { //获取亮度, callback = function(brightness);
		window.client.callNativeMethod('getBrightness()', function(returnValue) {
			callback.call(null, Number(returnValue));
		});
	};
	window.setBrightness = function(brightness) { //设置亮度
		if(window.brightnessTimer) {
			return;
		}
		window.brightnessTimer = window.setTimeout(function() {
			window.client.callNativeMethod('setBrightness(' + brightness + ')');
			window.brightnessTimer = null;
		}, 50);
	};
	window.requestFullScreen = function(landscapeOrientation) { //全屏,landscapeOrientation:是否横屏
		window.client.callNativeMethod('requestFullScreen(' + landscapeOrientation + ')');
	};
	window.exitFullScreen = function() { //退出全屏
		window.client.callNativeMethod('exitFullScreen()');
	};
	window.keepScreenOn = function() { //阻止关闭屏幕
		window.client.callNativeMethod('keepScreenOn()');
	};
	window.cancelKeepScreenOn = function() { //取消阻止关闭屏幕
		window.client.callNativeMethod('cancelKeepScreenOn()');
	};
	window.createVideoPlayer = function(win, videoUrl, previewImageUrl, logoUrl, videoDuration, videoWidth, videoHeight, autoPlay, hideControls) { //加载视频
		window.client.createVideoPlayer(win, videoUrl, previewImageUrl, logoUrl, videoDuration, videoWidth, videoHeight, autoPlay, hideControls);
	};
	//替换浏览器alert
	window.alert = function(message) {
		window.client.alert(message);
	};
	window.open = function(url) {
		window.client.openPage(url, this);
	};
	//频道栏事件处理:频道改变
	if(this.channelBar) {
		this.channelBar.onChannelChanged = function(currentChannel) {
			window.client.onChannelChanged(currentChannel);
		};
		//点击频道图标、频道名称、显示频道栏按钮时显示频道栏
		var showChannelBar = function(event) {
			if(event.stopPropagation) {
				event.stopPropagation();
			}
			else {
				event.cancelBubble = true; 
			}
			if(channelBar.displayMode=="left2right") {
				window.client.showSidebar(window.client.leftBar, true);
			}
			else if(channelBar.displayMode=="right2left") {
				window.client.showSidebar(window.client.rightBar, true);
			}
			return false;
		};
		if(channelIconSpan) {
			channelIconSpan.onclick = showChannelBar;
		};
		if(channelNameSpan) {
			channelNameSpan.onclick = showChannelBar;
		}
		if(showChannelBarButton) {
			showChannelBarButton.onclick = showChannelBar;
		}
	}
	//栏目栏事件处理:栏目改变
	if(this.columnBar) {
		this.columnBar.onColumnChanged = function(currentColumn, columnCount) {
			//设置栏目图标
			if(client.columnIconSpan && currentColumn.icon && currentColumn.icon!="" && currentColumn.icon!="null") {
				client.columnIconSpan.innerHTML = '<img src="' + currentColumn.icon + '"' + (client.columnIconWidth>0 ? ' width="' + client.columnIconWidth + '"' : '') + (client.columnIconHeight>0 ? ' height="' + client.columnIconHeight + '"' : '') + '/>';
			}
			//设置栏目名称
			if(client.columnNameSpan) {
				client.columnNameSpan.innerHTML = currentColumn.name;
			}
			var displayColumnNameAndIcon = columnCount==1 && client.channelNameSpan ? 'none' : '';
			if(client.columnIconSpan) {
				client.columnIconSpan.style.display = displayColumnNameAndIcon;
			}
			if(client.columnNameSpan) {
				client.columnNameSpan.style.display = displayColumnNameAndIcon;
			}
			if(client.showColumnBarButton) {
				client.showColumnBarButton.style.display = columnCount==1 ? 'none' : '';
			}
		};
		//点击栏目名称、栏目图标、显示栏目栏按钮时显示栏目栏
		var showColumnBar = function(event) {
	 		if(event.stopPropagation) {
				event.stopPropagation();
			}
			else {
				event.cancelBubble = true; 
			}
	 		window.client.columnBar.show();
			return false;
		};
		if(columnIconSpan) {
			columnIconSpan.onclick = showColumnBar;
		};
		if(columnNameSpan) {
			columnNameSpan.onclick = showColumnBar;
		}
		if(showColumnBarButton) {
			showColumnBarButton.onclick = showColumnBar;
		}
	}
	
	//显示引导页面
	if(this.pilotEnabled) {
		var onload = function(popupFrame) {
			popupFrame.contentWindow.setTimeout('window.pilot.create();', 1);
			window.client.closePopupFrame(document.getElementById('startupFrame'), 1000); //引导页面加载完成后,关闭启动页面
		};
		this.pilotFrame = this.createPopupFrame(RequestUtils.getContextPath() + "/jeaf/client/pilot.shtml" + (this.siteId=="" ? "" : "?siteId=" + this.siteId), onload, null, false, false);
	}
};
Client.prototype.callNativeMethod = function(method, callback) { //调用客户端本地方法,例如:method=isBusing(); callback=function(returnValue)
	if(window.top.androidClient) { //安卓
		var returnValue = "" + eval('window.top.androidClient.' + method);
		if(callback) {
			callback.call(null, returnValue);
		}
	}
	else { //ios
		if(!this.nativeMethods) {
			this.nativeMethods = [];
		}
		this.nativeMethods.push({method:method, callback:callback});
		if(this.nativeMethods.length==1) {
			this.callIosNativeMethod();
		}
	}
};
Client.prototype.callIosNativeMethod = function() {
	window.top.location.href = "nativemethod:" + StringUtils.utf8Encode(this.nativeMethods[0].method + "&callback=window.top.client.executeNativeMethodCallback(returnValue)");
};
Client.prototype.executeNativeMethodCallback = function(returnValue) { //调用本地方法回调
	if(this.nativeMethods[0].callback) {
		try {
			this.nativeMethods[0].callback.call(null, returnValue);
		}
		catch(e) {
		
		}
	}
	this.nativeMethods = this.nativeMethods.slice(1);
	if(this.nativeMethods.length>0) {
		this.callIosNativeMethod();
	}
};
Client.prototype.debug = function(msg) { //调试
	this.callNativeMethod('debug("' + msg.replace(/"/g, '\\"').replace(/\r/g, '').replace(/\n/g, '\\\\n') + '")');
};
Client.prototype.onPilotComplete = function() { //引导结束
	this.callNativeMethod("pilotComplete()"); //通知客户端,引导已经完成
	this.pilotEnabled = false;
	this.create();
};
Client.prototype.openHintPage = function(contentWindow) { //打开提示页面
	if(contentWindow) {
		var hintPagePath = DomUtils.getMetaContent(contentWindow.document, "client.hintPagePath");
		if(!hintPagePath) {
			return;
		}
		if(!this.hintPages) {
			this.hintPages = [];
		}
		this.hintPages.push(hintPagePath);
		if(this.hintPages.length>1) {
			return;
		}
	}
	//检查是否已经提示过
	this.callNativeMethod("isPageHinted('" + this.hintPages[0] + "')", function(returnValue) {
		window.top.client._showHintPage(returnValue)
	});
};
Client.prototype._showHintPage = function(hinted) { //显示提示页面
	var client = this;
	var showNextHintPage = function() { //显示下一个提示
		client.hintPages.shift();
		if(client.hintPages.length>0) {
			client.openHintPage();
		}
	};
	if(hinted=='true') {
		showNextHintPage.call(null);
		return;
	}
	var onload = function(popupFrame) {
		client.callNativeMethod("pageHinted(\"" + client.hintPages[0] + "\")"); //通知客户端,记录页面已经提示过
	};
	var hintUrl = RequestUtils.getContextPath() + "/jeaf/client/hint.shtml?hintPagePath=" + StringUtils.utf8Encode(this.hintPages[0]);
	this.hintFrame = this.createPopupFrame(hintUrl, onload, showNextHintPage, true, false);
};
Client.prototype.closeHintPage = function() { //关闭提示页面
	this.closePopupFrame(this.hintFrame, 1);
};
Client.prototype.create = function() { //创建客户端
	if(this.pilotEnabled) { //引导页面是否有效
		return;
	}
	//设置属性: 客户端宽度、高度
	document.body.style.width = this.clientWidth + "px";
	document.body.style.height = this.clientHeight + "px";
	
	//根据频道栏和参数设置栏显示属性,设置客户端的左边栏和右边栏
	if(this.channelBar && this.channelBar.displayMode!="alwaysDisplay") {
		if(this.channelBar.displayMode=="left2right") {
			this.leftBar = {src:this.channelBar};
		}
		else if(this.channelBar.displayMode=="right2left") {
			this.rightBar = {src:this.channelBar};
		}
	}
	if(this.preferenceBar && this.preferenceBar.displayMode!="alwaysDisplay") {
		if(this.preferenceBar.displayMode=="left2right") {
			this.leftBar = {src:this.preferenceBar};
		}
		else if(this.preferenceBar.displayMode=="right2left") {
			this.rightBar = {src:this.preferenceBar};
		}
	}
	
	var client = this;
	//创建客户端滚动条
	var scoller = new Scroller(document.body, true, true, false, false, false);
	scoller.onScrollingFromSide = function(x, y, startX, startY, isLeft, isRight, isTop, isBottom) {
		if(client.popupPages && client.popupPages.length>0) {
			return false;
		}
		if((client.leftBar && client.leftBar.enabled) || (client.rightBar && client.rightBar.enabled)) {
			return false;
		}
		var sidebar = isLeft ? client.leftBar : (isRight ? client.rightBar : null);
		if(!sidebar || this==sidebar.scroller || startY < sidebar.src.marginTop || startY > client.clientHeight-sidebar.src.marginBottom) {
			return false;
		}
		client.createSidebar(sidebar);
		sidebar.scroller.scroll(x, y);
		return true;
	};
	scoller.onAfterScrollFromSide = function(x, y, startX, startY, isLeft, isRight, isTop, isBottom) {
		var sidebar = isLeft ? client.leftBar : (isRight ? client.rightBar : null);
		if(sidebar) {
			sidebar.scroller.afterScroll(x, y, false, false, false, false, true);
		}
	};

	//创建触摸事件拦截器
	this.touchEventInterceptor.addToInterceptElement(document.body);
	
	//创建栏目容器
	this.columnContainer = new ColumnContainer(this.columnContainerElement);
	this.columnContainer.onColumnLoaded = function(columnWindow) {
		columnWindow.alert = function(message) {
			client.alert(message);
		};
		client.touchEventInterceptor.addToInterceptElement(columnWindow.document.body);
		client.resetForm(columnWindow.document); //重置表单
	};
	
	//创建频道栏
	if(this.channelBar) {
		this.channelBar.create();
	}
	
	//关闭启动页面
	this.closePopupFrame(document.getElementById('startupFrame'), 3000);
	
	//关闭引导页面
	this.closePopupFrame(this.pilotFrame, 1);
	
	//重置表单
	this.resetForm(document);
	
	//显示提示页面
	this.openHintPage(window);
};
Client.prototype.getZIndex = function() { //获取当前最大的zIndex
	if(!this.maxZIndex) {
		this.maxZIndex = 100;
	}
	return this.maxZIndex++;	
};
Client.prototype.createCover = function() { //创建div覆盖在页面上面
	var cover = document.createElement("div");
	cover.style.position = "absolute";
	cover.style.backgroundColor = "rgba(0, 0, 0, 0.3)";
	cover.style.zIndex = this.getZIndex();
	cover.style.left = "0px";
	cover.style.top = "0px";
	cover.style.width = this.clientWidth + "px";
	cover.style.height = this.clientHeight + "px";
	document.body.appendChild(cover);
	return cover;
}
//创建弹出窗口,onload=funtion(popupFrame); onclose=function();
Client.prototype.createPopupFrame = function(src, onload, onclose, closeAfterBackKeyDown, animationSupport) {
	var client = this;
	var onPopupFrameLoaded = function() {
		var iframe = this;
		if(iframe.canceled) {
			client.closePopupFrame(iframe);
			return;
		}
		this.contentWindow.alert = function(message) {
			client.alert(message);
		};
		this.contentWindow.open = function(url) {
			client.openPage(url, this);
		};
		this.contentWindow.close = this.contentWindow.closeWindow = function() {
			client.closePopupFrame(iframe);
		};
		client.touchEventInterceptor.addToInterceptElement(this.contentWindow.document.body);
		var iframeBody = iframe.contentWindow.document.body;
		iframe.style.backgroundColor = CssUtils.getElementComputedStyle(iframeBody, 'background-color');
		iframeBody.style.width = (client.clientWidth - DomUtils.getSpacing(iframeBody, 'left') - DomUtils.getSpacing(iframeBody, 'right')) + 'px';
		iframeBody.style.height = (client.clientHeight - DomUtils.getSpacing(iframeBody, 'top') - DomUtils.getSpacing(iframeBody, 'bottom')) + 'px';
		if(animationSupport && !frame.loaded) { //支持动画
			iframe.className = 'pageMoveFromRight';
			iframe.previousFrame.className = 'pageMoveToLeft';
			iframe.previousFrame.style.display = 'none';
			iframe.previousFrame.style.display = '';
		}
		client.activeFrame = frame;
		frame.style.display = '';
		
		if(onload) {
			onload.call(null, this);
		}
		if(frame.closeAfterBackKeyDown) {
			client.unregistPopupPage(frame.popupPageId);
			frame.popupPageId = client.registPopupPage(function() {
				client.closePopupFrame(frame);
			});
		}
		if(animationSupport) {
			var scroller = new Scroller(this.contentWindow.document.body, true, false, false, false, false);
			scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
				if(x<0 && -x > iframe.offsetWidth/5) { //从左向右移动
					client.closePopupFrame(iframe);
				}
			};
		}
		//显示提示页面
		client.openHintPage(iframe.contentWindow);
		frame.loaded = true;
	};
	var frame = this.closedPopupFrames ? this.closedPopupFrames.shift() : null;
	var isNew = !frame;
	if(isNew) {
		frame = document.createElement('iframe');
	}
	frame.loaded = false;
	frame.canceled = false;
	frame.closed = false;
	frame.src = src;
	frame.allowTransparency = true;
	frame.onload = onPopupFrameLoaded;
	frame.closeAfterBackKeyDown = closeAfterBackKeyDown;
	frame.onclose = onclose;
	frame.animationSupport = animationSupport;
	frame.style.display = 'none';
	frame.style.zIndex = this.getZIndex();
	frame.style.position = 'absolute';
	frame.style.border = '#000 0px none';
	frame.style.left = '0px';
	frame.style.top = '0px';
	frame.style.backgroundColor = 'transparent';
	frame.style.width = this.clientWidth + 'px';
	frame.style.height = this.clientHeight + 'px';
	if(isNew) {
		if(!this.activeFrame) {
			this.activeFrame = document.getElementsByTagName('div')[0];
		}
		document.body.insertBefore(frame, document.body.childNodes[0]);
		EventUtils.addEvent(frame, 'webkitAnimationEnd', function() { //事件处理:动画播放完成
			this.previousFrame.removeAttribute("class");
			this.removeAttribute("class");
			if(this.closed) { //关闭
				client._doClosePopupFrame(this);
			}
		});
	}
	frame.previousFrame = this.activeFrame;
	return frame;
};
Client.prototype.closePopupFrame = function(frame, delay) { //关闭弹出窗口
	if(!frame) {
		return;
	}
	if(frame.style.display=='none' && !frame.canceled) {
		frame.canceled = true;
		return;
	}
	if(frame.closeAfterBackKeyDown) {
		this.unregistPopupPage(frame.popupPageId);
	}
	frame.closed = true;
	var client = this;
	window.setTimeout(function() {
		if(!frame.animationSupport || frame.style.display=='none') {
			client._doClosePopupFrame(frame);
		}
		else {
			//设置关闭动画
			frame.className = 'pageMoveToRight';
			frame.previousFrame.className = 'pageMoveFromLeft';
			frame.previousFrame.style.display = 'none';
			frame.previousFrame.style.display = '';
		}
	}, !delay ? 1 : delay);
};
Client.prototype._doClosePopupFrame = function(frame) { //执行关闭弹出窗口
	if(frame.previousFrame) {
		this.activeFrame = frame.previousFrame;
		frame.previousFrame = null;
	}
	frame.style.zIndex = 0;
	frame.style.display = 'none'; //隐藏窗口
	frame.style.left = "0px";
	if(frame.onclose) { //指定了关闭后的动作
		frame.onclose.call(null, frame);
		frame.onclose = null;
	}
	//检查是否内嵌iframe,如果没有则清除内容, 注:直接清除含iframe的页面,会报错
	var childIframes = frame.contentWindow.document.getElementsByTagName('iframe');
	if(!childIframes || childIframes.length==0) {
		frame.contentWindow.document.body.innerHTML = '';
		if(!client.closedPopupFrames) {
			client.closedPopupFrames = [];
		}
		client.closedPopupFrames.push(frame);
	}
};
Client.prototype.alert = function(message, title) {
	new Dialog(title ? title : '提示', '<div style="padding: 6px 6px 6px 6px;">' + message + '</div>', false, true).open(); //Dialog(dialogTitle, dialogBodyHTML, hideOkButton, hideCancelButton, okButtonName, cancelButtonName)
};
//创建侧边栏
Client.prototype.createSidebar = function(sidebar) {
	if(sidebar.container) {
		if(sidebar.container.style.display=='none') {
			sidebar.container.style.display = '';
			sidebar.container.scrollLeft = sidebar==this.leftBar ? sidebar.width : 0;
		}
		return;
	}
	//创建DIV放置侧边栏内容
	var div = document.createElement('div');
	div.style.display = 'inline-block';
	div.style.position = 'absolute';
	div.style.overflow = 'hidden';
	div.style.top = sidebar.src.marginTop + 'px';
	div.style.height = (this.clientHeight - sidebar.src.marginTop - sidebar.src.marginBottom) + 'px';
	div.appendChild(sidebar.src.pageElement);
	sidebar.src.pageElement.style.position = 'static';
	sidebar.src.pageElement.style.display = '';
	
	//创建背景DIV
	var cover = document.createElement('div');
	cover.style.backgroundColor = "#000000";
	cover.style.opacity = 0.1;
	cover.style.position = 'absolute';
	cover.style.height = this.clientHeight + 'px'; //div.style.height;
	cover.style.left = '0px';
	cover.style.top = '0px'; //div.style.top;

	//创建侧边栏容器
	sidebar.container = document.createElement('div');
	sidebar.container.style.position = 'absolute';
	sidebar.container.style.width = '100%';
	sidebar.container.style.height = '100%';
	sidebar.container.style.left = '0px';
	sidebar.container.style.top = '0px';
	sidebar.container.style.visibility = 'hidden';
	sidebar.container.style.overflow = 'hidden';
	
	sidebar.container.appendChild(cover);
	sidebar.container.appendChild(div);
	document.body.appendChild(sidebar.container);
	
	var client = this;
	sidebar.container.onclick = function(event) {
		if(event.y < sidebar.src.marginTop ||
		   event.y > client.clientHeight - sidebar.src.marginBottom ||
		   (sidebar==client.leftBar && event.x > sidebar.width) ||
		   (sidebar==client.rightBar && event.x < client.clientWidth - sidebar.width)) {
		   client.showSidebar(sidebar, false);
		}
	};
	
	sidebar.width = div.offsetWidth;
	if(sidebar==this.leftBar) { //左边栏
		div.style.paddingRight = document.body.clientWidth + 'px';
	}
	else { //右边栏
		div.style.paddingLeft = document.body.clientWidth + 'px';
	}
	cover.style.width = (sidebar.width + document.body.clientWidth) + 'px';
	sidebar.container.scrollLeft = sidebar==this.leftBar ? sidebar.width : 0;
	sidebar.container.style.visibility = 'visible';
	
	//创建滚动条
	sidebar.scroller = new Scroller(sidebar.container, true, false, false, false, false);
	sidebar.scroller.afterScroll = sidebar.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		client.showSidebar(sidebar, sidebar==client.leftBar ? (sidebar.enabled ? (x < sidebar.width / 3) : (-x > sidebar.width/2)) : (sidebar.enabled ? (-x < sidebar.width/2) : (x > sidebar.width/2)));
	};
};
//显示或隐藏侧边栏
Client.prototype.showSidebar = function(sidebar, isVisible) {
	this.createSidebar(sidebar);
	sidebar.enabled = isVisible;
	sidebar.scroller.scrollWithTimer(sidebar==client.leftBar ? (sidebar.enabled ? 0 : sidebar.width) : (sidebar.enabled ? sidebar.width : 0), false, 6, function() {
		sidebar.container.style.display = sidebar.enabled ? '' : 'none';
	});
};
Client.prototype.onChannelChanged = function(currentChannel) { //频道改变
	var client = this;
	//设置频道图标
	if(this.channelIconSpan && currentChannel.icon && currentChannel.icon!="" && currentChannel.icon!="null") {
		this.channelIconSpan.innerHTML = '<img src="' + currentChannel.icon + '"' + (this.channelIconWidth>0 ? ' width="' + this.channelIconWidth + '"' : '') + (this.channelIconHeight>0 ? ' height="' + this.channelIconHeight + '"' : '') + '/>';
	}
	//设置频道名称
	if(this.channelNameSpan) {
		this.channelNameSpan.innerHTML = currentChannel.name;
	}
	//初始化栏目列表
	this.columnContainer.initColumns(currentChannel);
	var currentColumnIndex = 0; //TODO: 从个性化设置参数中读取
	if(currentColumnIndex>=currentChannel.columns.length) {
		currentColumnIndex = 0;
	}
	//加载栏目
	this.columnContainer.loadColumn(currentChannel.columns[currentColumnIndex], currentColumnIndex, currentColumnIndex);
	//重建栏目栏
	if(this.columnBar) {
		this.columnBar.create(currentChannel, currentColumnIndex);
	}
	//关闭频道栏
	if(channelBar.displayMode=="left2right") {
		window.client.showSidebar(window.client.leftBar, false);
	}
	else if(channelBar.displayMode=="right2left") {
		window.client.showSidebar(window.client.rightBar, false);
	}
};
//登记页面,手机返回键按下后关闭,closePageFunction=function()
Client.prototype.registPopupPage = function(closePageFunction) {
	if(!this.popupPages) {
		this.popupPages = [];
		this.popupPageId = 0;
	}
	this.popupPages.push({id:++this.popupPageId, closePage:closePageFunction});
	return this.popupPageId; 
};
Client.prototype.unregistPopupPage = function(pageId) { //注销页面
	for(var i=0; i<(!this.popupPages ? 0 : this.popupPages.length); i++) {
		if(this.popupPages[i].id==pageId) {
			this.popupPages = this.popupPages.slice(0, i).concat(this.popupPages.slice(i+1));
			return;
		}
	}
};
Client.prototype.onBackKeyDown = function() { //客户端调用:手机返回键按下
	//关闭文本输入框
	this.removeTextBox();
	//关闭弹出页面
	if(this.popupPages && this.popupPages.length>0) {
		this.popupPages.pop().closePage.call(null);
		return;
	}
	//没有弹出页面
	if(!this.exitEnabled) {
		this.exitEnabled = true;
		this.showToast("再按一次退出", 2);
		return;
	}
	this.callNativeMethod("exit()");
};
Client.prototype.showToast = function(message, showSecnonds) { //客户端调用:显示消息
	if(this.showToastTimer && this.showToastTimer!=0) {
		window.clearTimeout(this.showToastTimer);
	}
	if(this.hideToastTimer && this.hideToastTimer!=0) {
		window.clearInterval(this.hideToastTimer);
	}
	this.toastMessage = message;
	if(!this.clientToast) {
		this.clientToast = document.getElementById('clientToast');
	}
	if(message==null || message=="") {
		this._hideToast();
		return;
	}
	this.clientToast.innerHTML = message;
	this.clientToast.style.display = '';
	CssUtils.setOpacity(this.clientToast, 1);
	var client = this;
	this.showToastTimer = window.setTimeout(function() {
		client._hideToast();
		client.showToastTimer = 0;
	}, (!showSecnonds ? 5 : showSecnonds) * 1000);
};
Client.prototype._hideToast = function() { //客户端调用:隐藏消息
	var client = this;
	var opacity = 1;
	this.hideToastTimer = window.setInterval(function() {
		opacity -= 0.1;
		CssUtils.setOpacity(client.clientToast, opacity);
		if(opacity>0) {
			return;
		}
		window.clearInterval(client.hideToastTimer);
		client.hideToastTimer = 0;
		client.clientToast.innerHTML = '';
		client.clientToast.style.display = 'none';
		client.toastMessage = null;
	}, 20);
};
Client.prototype.fileDownloadStart = function(url, fileSize) { //客户端调用:文件下载开始
	var downloadTask = this.getFileDowloadTask(url);
	if(downloadTask && downloadTask.onFileDownloadStart) {
		downloadTask.onFileDownloadStart.call(null, fileSize);
	}
};
Client.prototype.fileDownloading = function(url, fileSize, completed, percentage) { //客户端调用:文件下载中
	var downloadTask = this.getFileDowloadTask(url);
	if(downloadTask && downloadTask.onFileDownloading) {
		downloadTask.onFileDownloading.call(null, fileSize, completed, percentage);
	}
};
Client.prototype.fileDownloadComplete = function(url) { //客户端调用:文件下载完成
	var downloadTask = this.getFileDowloadTask(url);
	if(downloadTask && downloadTask.onFileDownloadComplete) {
		downloadTask.onFileDownloadComplete.call(null);
	}
	this.unregistFileDowloadTask(url);
};
Client.prototype.loadNewestPage = function(url) { //客户端调用:重新加载页面
	var frame = this.findPageFrame(url, window.top);
	if(!frame) {
		return;
	}
	try {
		if(frame.contentWindow.document.autoRefreshDisabled) { //禁止被自动刷新
			return;
		}
	}
	catch(e) {
		
	}
	if(url.indexOf('client.loadNewestPage')==-1) {
		url += (url.indexOf('?')==-1 ? "?" : "&") + "client.loadNewestPage=true";
	}
	if(frame==window.top) {
		frame.location.replace(url);
	}
	else {
		frame.src = url;
	}
};
Client.prototype.hideRefreshingPrompt = function(url) { //客户端调用:隐藏刷新提示
	var frame = this.findPageFrame(url, window.top);
	if(frame && frame.hideRefreshing) {
		frame.hideRefreshing();
	}
};
Client.prototype.updatePageLastModified = function(url) { //客户端调用:重设页面修改时间
	var frame = this.findPageFrame(url, window.top);
	if(frame) {
		(frame==window.top ? frame : frame.contentWindow).document.pageLastModified = new Date();
	}
};
Client.prototype.fillMoreData = function(url) { //客户端调用:通知页面更多数据已经加载完成
	var frame = this.findPageFrame(url, window.top);
	if(frame) {
		frame.contentWindow.document.updater.fillMoreData();
	}
};
Client.prototype.findPageFrame = function(url, parentFrame) { //按URL查找iframe
	if(parentFrame==window.top) {
		if(this.isSameURL(parentFrame.location.href, url)) {
			return parentFrame;
		}
	}
	else {
		if(this.isSameURL(parentFrame.src, url) || this.isSameURL(parentFrame.contentWindow.location.href, url)) {
			return parentFrame;
		}
	}
	var iframes = (parentFrame==window.top ? parentFrame : parentFrame.contentWindow).document.getElementsByTagName("iframe");
	for(var i=0; i<(iframes ? iframes.length : 0); i++) {
		var found = this.findPageFrame(url, iframes[i]);
		if(found!=null) {
			return found;
		}
	}
	return null;
};
Client.prototype.isSameURL = function(href, url) { //URL是否相同
	var index = href.indexOf('://');
	if(index!=-1) {
		index = href.indexOf('/', index + 3);
		href = index==-1 ? '' : href.substring(index);
	}
	return href==url || StringUtils.utf8Decode(href)==StringUtils.utf8Decode(url);
};

//登记文件下载任务,onFileDownloadStart=function(fileSize),onFileDownloading=function(fileSize, completed, percentage),onFileDownloadComplete=function()
Client.prototype.registFileDowloadTask = function(url, onFileDownloadStart, onFileDownloading, onFileDownloadComplete) {
	if(!this.fileDowloadTasks) {
		this.fileDowloadTasks = new Array();
	}
	this.callNativeMethod("registFileDowloadTask('" + url + "')");
	this.fileDowloadTasks[this.fileDowloadTasks.length] = {url:url, onFileDownloadStart:onFileDownloadStart, onFileDownloading:onFileDownloading, onFileDownloadComplete:onFileDownloadComplete};
};
//取消文件下载任务
Client.prototype.cancelFileDowloadTask = function(url) {
	this.callNativeMethod("cancelFileDowloadTask('" + url + "')");
};
Client.prototype.getFileDowloadTask = function(url) { //获取文件下载任务
	for(var i=0; i<(!this.fileDowloadTasks ? 0 : this.fileDowloadTasks.length); i++) {
		if(this.fileDowloadTasks[i].url==url || StringUtils.utf8Encode(this.fileDowloadTasks[i].url).replace(/%25/g, '%')==StringUtils.utf8Encode(url)) {
			return this.fileDowloadTasks[i];
		}
	}
	return null;
};
Client.prototype.unregistFileDowloadTask = function(url) { //注销文件下载任务
	for(var i=0; i<(!this.fileDowloadTasks ? 0 : this.fileDowloadTasks.length); i++) {
		if(this.fileDowloadTasks[i].url==url) {
			this.fileDowloadTasks = this.fileDowloadTasks.slice(0, i).concat(this.fileDowloadTasks.slice(i+1));
			return;
		}
	}
};
Client.prototype.createVideoPlayer = function(win, videoUrl, previewImageUrl, logoUrl, videoDuration, videoWidth, videoHeight, autoPlay, hideControls) {
	var videoPlayerId = 'videoPlayer_' + Math.random();
	//创建SPAN用于控制视频显示位置
	win.document.write('<span id="' + videoPlayerId + '" style="display:inline-block; width:' + videoWidth + 'px; height:' + videoHeight + 'px"/>');
	//解密视频URL
	this.callNativeMethod('retrieveVideoUrl("' + videoUrl + '")', function(returnValue) {
		var script = 'var logoUrl = null;' +
					 'new VideoPlayer(document.getElementById("' + videoPlayerId + '"), "' + returnValue + '", "' + previewImageUrl + '", "' + logoUrl + '", ' + videoWidth + ', ' + videoHeight + ', ' + autoPlay + ', ' + hideControls + ', ' + videoDuration + ');';
		win.setTimeout(script, 1);
	});
};

//触摸事件拦截器
TouchEventInterceptor = function() {

};
TouchEventInterceptor.prototype.addToInterceptElement = function(pageElement) {
	var touchEventInterceptor = this;
	EventUtils.addEvent(pageElement, 'touchstart', function(event) {
		touchEventInterceptor.onTouchStart(event, pageElement);
	});
	EventUtils.addEvent(pageElement, 'touchmove', function(event) {
		touchEventInterceptor.onTouchMove(event, pageElement);
	});
	EventUtils.addEvent(pageElement, 'touchend', function(event) {
		touchEventInterceptor.onTouchEnd(event, pageElement);
	});
	EventUtils.addEvent(pageElement, 'click', function(event) {
		if(window.top.client.clicked) {
			return true;
		}
		event.preventDefault();
		if(event.stopPropagation) {
			event.stopPropagation();
		}
		else {
			event.cancelBubble = true; 
		}
		return false;
	});
};
TouchEventInterceptor.prototype.onTouchStart = function(event, pageElement) {
	window.top.client.exitEnabled = false; //禁止退出
	var isTextBox = "TEXTAREA"==event.srcElement.tagName ||
					("INPUT"==event.srcElement.tagName && (event.srcElement.type=='text' || event.srcElement.type=='password'));
	if(!isTextBox) { //不是文本框
		event.preventDefault();
	}
	else if(event.stopPropagation) {
		event.stopPropagation();
	}
	else {
		event.cancelBubble = true; 
	}
	this.touch = event.touches[0];
	this.startX = this.touch.screenX;
	this.startY = this.touch.screenY;
	this.moved = false;
	window.top.client.clicked = false;
	window.top.client.removeTextBox(); //关闭文本输入框
	//修改样式
	for(var element = event.srcElement; ; element=element.parentNode) {
		if(element.onclick || (element.tagName=='A' && element.getAttribute('url'))) {
			this.setFocusStyle(element);
		}
		else if(element!=pageElement && element.tagName!="BODY") {
			continue;
		}
		break;
	}
};
TouchEventInterceptor.prototype.onTouchMove = function(event, pageElement) {
	event.preventDefault();
	var touch = event.touches[0];
	if(Math.abs(touch.screenX - this.startX) > SCROLL_MIN_STEP || Math.abs(touch.screenY - this.startY) > SCROLL_MIN_STEP) { //移动距离不能超过8,如果超过则认为是滚动条滚动
		this.moved = true;
	}
};
TouchEventInterceptor.prototype.onTouchEnd = function(event, pageElement) {
	this.clearFocusStyle();
	var isTextBox = "TEXTAREA"==event.srcElement.tagName ||
					("INPUT"==event.srcElement.tagName && (event.srcElement.type=='text' || event.srcElement.type=='password'));
	if(isTextBox && !this.moved) { //文本框、且不是移动
		if(event.stopPropagation) {
			event.stopPropagation();
		}
		else {
			event.cancelBubble = true; 
		}
		return true;
	}
	event.preventDefault();
	if(this.moved) {
		return;
	}
	window.top.client.clicked = true;
	event.x = this.touch.screenX;
	event.y = this.touch.screenY;
	//检查是否有onclick属性,如果有则执行onclick方法
	for(var element = event.srcElement; ; element=element.parentNode) {
		if(element.onclick) {
			var a;
			if("EventUtils.clickElement(getElementsByTagName('a')[0])"!=element.getAttribute("onclick") && "getElementsByTagName('a')[0].click()"!=element.getAttribute("onclick")) {
				element.onclick(event);
			}
			else if((a = element.getElementsByTagName('a')[0]).onclick) {
				a.onclick(event);
			}
			else {
				window.top.client.openLink(a); //打开链接
			}
		}
		else if(element.tagName=='A' && element.getAttribute('url')) { //A
			window.top.client.openLink(element); //打开链接
		}
		else if(element.tagName=='VIDEO') {
			element.play();
		}
		else if("INPUT"==element.tagName && (element.type=='radio' || element.type=='checkbox')) {
			element.checked = element.type=='radio' ? true : !element.checked;
		}
		else if("LABEL"==element.tagName && element.getAttribute("for")) {
			var box = element.ownerDocument.getElementById(element.getAttribute("for"));
			if(box) {
				box.checked = box.type=='radio' ? true : !box.checked;
			}
		}
		else if(element!=pageElement && element.tagName!="BODY") {
			continue;
		}
		if(event.stopPropagation) {
			event.stopPropagation();
		}
		else {
			event.cancelBubble = true; 
		}
		window.top.client.clicked = false;
		break;
	}
};
TouchEventInterceptor.prototype.setFocusStyle = function(element) { //设置获取焦点后的样式
	if(element!=this.focusElement) {
		this.clearFocusStyle();
	}
	this.focusElement = element;
	if(element.originalClass) {
		return;
	}
	window.setTimeout(function() {
		var className = element.className;
		element.originalClass = className && className!="" ? className : "NULL"; //记录原始样式
		element.className = element.originalClass=="NULL" ? "focus" : className + " " + className + "Focus";
	}, 10);
};
TouchEventInterceptor.prototype.clearFocusStyle = function() { //清除获取焦点后的样式
	if(!this.focusElement) {
		return;
	}
	var element = this.focusElement;
	this.focusElement = null;
	window.setTimeout(function() {
		try {
			var className = element.className.replace(new RegExp(element.originalClass=="NULL" ? "focus" : " " + element.originalClass + "Focus", "gi"), "");
			element.originalClass = null;
			if(className.substring(0, 1)==" ") {
				className = className.substring(0, 1);
			}
			element.className = className;
		}
		catch(e) {
		
		}
	}, 100);
};