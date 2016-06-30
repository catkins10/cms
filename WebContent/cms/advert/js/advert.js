//类:广告
Advert = function(advertPutId, putMode, loadMode, hideMode, displaySeconds, html, minimizeHtml, x, y, speed, width, height, minimizeWidth, minimizeHeight, href) {
	this.advertPutId = advertPutId; //广告投放ID
	this.putMode = putMode; //投放方式,飘动|fly\0窗口左上角|windowLeftTop\0窗口右上角|windowRightTop\0窗口右下角|windowRightBottom\0窗口左下角|windowLeftBottom\0页面左上角|pageLeftTop\0页面右上角|pageRightTop\0页面右下角|pageRightBottom\0页面左下角|pageLeftBottom\0页面顶部(居中)|pageTop\0页面底部(居中)|pageBottom\0窗口中部|windowCenter\0窗口上方(左右对称)|windowTopBalance\0窗口下方(左右对称)|windowBottomBalance\0页面上方(左右对称)|pageTopBalance\0页面下方(左右对称)|pageBottomBalance\0绝对位置|static
	this.loadMode = loadMode; //加载方式,0/直接显示,1/从下至上移动,2/从上至下移动,3/从左至右移动,4/从右至左移动
	this.hideMode = hideMode; //隐藏方式,0/直接关闭,1/从下至上收缩,2/从上至下收缩,3/从左至右收缩,4/从右至左收缩
	this.displaySeconds = displaySeconds; //显示时长
	this.html = html; //HTML
	this.minimizeHtml = minimizeHtml; //最小化时HTML
	this.x = x; //水平边距
	this.y = y; //垂直边距
	if(speed==0) {
		speed = 1;
	}
	if(speed>=1) {
		this.amout = Math.round(speed); //移动距离(像素)
		this.speed = 1; //速度(ms)
	}
	else {
		this.amout = 1; //移动距离(像素)
		this.speed = Math.round(1/speed); //速度(ms)
	}
	this.width = width==Number(width) ? width + "px" : width; //宽度
	this.height = height==Number(height) ? height + "px" : height; //高度
	this.minimizeWidth = minimizeWidth==Number(minimizeWidth) ? minimizeWidth + "px" : minimizeWidth; //最小化时宽度
	this.minimizeHeight = minimizeHeight==Number(minimizeHeight) ? minimizeHeight + "px" : minimizeHeight; //最小化时高度
	this.href = href; //链接
	
	this.fixedPositionSupport = true; //是否支持固定位置
	this.isMinimize = false; //是否最小化
	this.flying = "fly"==this.putMode; //是否在漂移,漂移模式时有效
};

Advert.prototype.create = function(hostElement) { //显示广告,hostElement:宿主对象,固定位置广告时有效
	var advert = this;
	this.advertBody = document.createElement("div");
	this.advertBody.id = 'advert_' + this.advertPutId;
	this.advertBody.style.zIndex = 10000;
	this.advertBody.style.overflow = "hidden";
	this.advertBody.style.fontSize = "0px";
	this.advertBody.style.textAlign = "center";
	this.advertBody.style.width = "0px";
	this.advertBody.style.height = "0px";
	this.advertBody.style.borderStyle = "none";
	this.advertBody.style.visibility = 'hidden';
	this.advertBody.style.backgroundColor = "transparent";
	if("pageTop"==this.putMode) {
		this.advertBody.style.zIndex = -1;
	}
	if(hostElement) {
		this.advertBody.style.position = 'static';
		hostElement.parentNode.replaceChild(this.advertBody, hostElement);
	}
	else {
		this.advertBody.style.position = 'fixed';
		this.advertBody.style.left = "100px";
		if(document.body.childNodes.length==0 || "pageBottom"==this.putMode) {
			document.body.appendChild(this.advertBody);
		}
		else {
			document.body.insertBefore(this.advertBody, document.body.childNodes[0]);
		}
	}
	//创建DIV用来显示广告
	var divContent = document.createElement("div");
	divContent.style.width = (",pageTop,pageBottom,".indexOf("," + this.putMode + ",")!=-1 ? this.width : "100%");
	divContent.style.height = "100%";
	divContent.style.backgroundColor = "transparent";
	divContent.style.overflow = "hidden";
	divContent.style.margin = "auto";
	this.advertBody.appendChild(divContent);
	
	//pageTop模式时,创建div,高度y
	if("pageTop"==this.putMode) {
		this.divPadding = document.createElement("div");
		this.divPadding.id = 'advert_' + this.advertPutId;
		this.divPadding.style.position = 'static';
		this.divPadding.backgroundColor = "transparent";
		this.divPadding.style.height = this.y==0 ? this.height : this.y + "px";
		this.divPadding.style.overflow = "hidden";
		document.body.appendChild(this.divPadding);
	}
	
	//判断是否支持fixed样式
	this.fixedPositionSupport = this.advertBody.offsetLeft==100;
	if(!hostElement && (!this.fixedPositionSupport || this.putMode.indexOf("page")==0)) {
		this.advertBody.style.position = "absolute";
	}
	this.advertBody.style.fontSize = "12px";
	this.advertBody.style.left = "0px";
	this.restore(); //输出广告
	
	//有设置显示时间,定时关闭广告
	if(this.displaySeconds>0) {
		var advert = this;
		window.setTimeout(function() {
			advert.display(true); //关闭
		}, this.displaySeconds * 1000);
	};
	
	if("fly"==this.putMode) { //漂移
		this.fly();
	}
	
	//绑定事件
	var adjustPosition = function() {
		if(!advert.flying) {
			advert.adjustPosition();
		}
	};
	if(document.all) {
		window.attachEvent('onload', adjustPosition);
		window.attachEvent('onresize', adjustPosition);
		if(this.putMode.indexOf("page")!=0) {
			window.attachEvent('onscroll', adjustPosition);
		}
	}
	else {
		window.addEventListener("load", adjustPosition, false);
		window.addEventListener("resize", adjustPosition, false);
		if(this.putMode.indexOf("page")!=0) {
			window.addEventListener("scroll", adjustPosition, false);
		}
	}
};

Advert.prototype.destory = function() { //销毁广告
	for(var element = document.getElementById('advert_' + this.advertPutId); element; element = document.getElementById('advert_' + this.advertPutId)) {
		element.parentNode.removeChild(element);
	}
};

Advert.prototype.display = function(hide) { //显示或关闭广告
	var displayMode = hide ? this.hideMode : this.loadMode;
	if(displayMode==0 || this.isMinimize) {
		this.advertBody.style.visibility = 'visible';
		if(hide) {
			this.destory();
		}
		return;
	}
	var advert = this;
	var size = (displayMode<3 ? ("pageTop"==this.putMode ? this.y : this.advertBody.offsetHeight) : this.advertBody.offsetWidth);
	var padding = hide ? 0 : size;
	var amout = this.amout;
	var timer = window.setInterval(function() {
		if(displayMode>2) { //从左至右移动,从右至左移动
			advert.advertBody.childNodes[0].style.marginLeft = (displayMode==3 ? "-" : "") + padding + "px";
		}
		else if(hide && "pageTop"==advert.putMode) {
			advert.divPadding.style.height = (size - padding) + "px";
		}
		else if(hide && "static"==advert.putMode) {
			advert.advertBody.style.height = (size - padding) + "px";
		}
		else {
			advert.advertBody.childNodes[0].style.marginTop = (displayMode==2 ? "-" : "") + padding + "px";
		}
		advert.advertBody.style.visibility = 'visible';
		var stop = (hide && padding>=size) || (!hide && padding==0);
		if(!hide) {
			padding = Math.max(0, padding - amout);
		}
		else {
			padding = Math.min(size, padding + amout);
		}
		if(stop) {
			window.clearInterval(timer);
			if(hide) {
				advert.destory(); //销毁广告
			}
		}
	}, this.speed);
};

Advert.prototype.fly = function() { //漂移
	var advert = this;
	if(this.speed==1) {
		this.speed *= 2;
		this.amout *= 2;
	}
	this.advertBody.onmouseover = function() {
		advert.flying = false;
	};
	this.advertBody.onmouseout = function() {
		advert.flying = true;
	};
	var direction = "right_bottom"; //方向
	window.setInterval(function() {
		if(!advert.flying) {
			return;
		}
		if(direction.indexOf("right")!=-1) { //右移
			var width = advert.advertBody.offsetWidth;
			var clientWidth = DomUtils.getClientWidth(document);
			advert.x = Math.min(advert.x + advert.amout, clientWidth - width);
			if(advert.x + width >= clientWidth) {
				direction = direction.replace("right", "left");	
			}
		}
		if(direction.indexOf("left")!=-1) { //左移
			advert.x = Math.max(advert.x - advert.amout, 0);
			if(advert.x==0) {
				direction = direction.replace("left", "right");	
			}
		}
		if(direction.indexOf("bottom")!=-1) { //下移
			var height = advert.advertBody.offsetHeight;
			var clientHeight = DomUtils.getClientHeight(document);
			advert.y = Math.min(advert.y + advert.amout, clientHeight - height);
			if(advert.y + height >= clientHeight) {
				direction = direction.replace("bottom", "top");	
			}
		}
		if(direction.indexOf("top")!=-1) { //上移
			advert.y = Math.max(advert.y - advert.amout, 0);
			if(advert.y==0) {
				direction = direction.replace("top", "bottom");	
			}
		}
		advert.adjustPosition();
	}, this.speed);
};

Advert.prototype.adjustPosition = function() { //调整坐标
	if(this.advertBody.style.position=="static") {
		return;
	}
	if(",pageTop,pageBottom,".indexOf("," + this.putMode + ",")!=-1) { //页面顶部或者底部
		var advert = this;
		advert.advertBody.style.left = "0px";
		advert.advertBody.style.width = "100%";
		window.setTimeout(function() {
			advert.advertBody.style.width = document.body.scrollWidth + "px";
		}, 1);
		return;
	}
	var absolute = this.putMode.indexOf("page")==0;
	var left = (this.fixedPositionSupport || absolute  ? 0 : DomUtils.getScrollLeft(document));
	var top = (this.fixedPositionSupport || absolute ? 0 : DomUtils.getScrollTop(document));
	var clientWidth = absolute ? document.body.scrollWidth : DomUtils.getClientWidth(document);
	var clientHeight = absolute ? document.body.scrollHeight : DomUtils.getClientHeight(document);
	//设置左边距
	if(",fly,windowLeftTop,windowLeftBottom,pageLeftTop,pageLeftBottom,".indexOf("," + this.putMode + ",")!=-1) {
		left += this.x;
	}
	else if(",windowRightTop,windowRightBottom,pageRightTop,pageRightBottom,".indexOf("," + this.putMode + ",")!=-1) {
		left = left + clientWidth - this.advertBody.offsetWidth - this.x;
	}
	else if(",windowCenter,".indexOf("," + this.putMode + ",")!=-1) {
		left += (clientWidth - this.advertBody.offsetWidth) / 2;
	}
	//设置上边距
	if(",fly,windowLeftTop,windowRightTop,pageLeftTop,pageRightTop,".indexOf("," + this.putMode + ",")!=-1) {
		top += this.y;
	}
	else if(",windowLeftBottom,windowRightBottom,pageLeftBottom,pageRightBottom,".indexOf("," + this.putMode + ",")!=-1) {
		top += clientHeight - this.advertBody.offsetHeight - this.y;
	}
	else if(",windowCenter,".indexOf("," + this.putMode + ",")!=-1) {
		top += (clientHeight - this.advertBody.offsetHeight) / 2;
	}
	this.advertBody.style.left = left + "px";
	this.advertBody.style.top = top + "px";
};

Advert.prototype.minimize = function() { //最小化广告
	this.isMinimize = true;
	this.advertBody.style.visibility = 'hidden';
	var advert = this;
	window.setTimeout(function() {
		advert.advertBody.onclick = function() {
			var list = Advert.getAdverts(advert.advertPutId);
			for(var i=0; i<list.length; i++) {
				list[i].restore();
			}
		}
	}, 10);
	this.advertBody.childNodes[0].innerHTML = this.minimizeHtml;
	this.advertBody.style.width = this.minimizeWidth;
	this.advertBody.style.height = this.minimizeHeight;
	this.resetActions(); //设置关闭、还原操作
	this.adjustPosition(); //设置广告坐标
	this.display(false); //加载广告
};

Advert.prototype.restore = function() { //还原广告
	this.isMinimize = false;
	this.advertBody.style.visibility = 'hidden';
	this.advertBody.childNodes[0].innerHTML = this.html;
	this.advertBody.style.width = this.width;
	this.advertBody.style.height = this.height;
	this.resetActions(); //设置关闭、还原操作
	this.adjustPosition(); //设置广告坐标
	this.display(false); //加载广告
	var advert = this;
	advert.advertBody.onclick = function() {
		if(advert.href && advert.href!="") {
			window.open(advert.href); //打开链接
		}
	};
};

Advert.prototype.resetActions = function() { //设置关闭、还原操作
	var advert = this;
	var findActions = function(parentNode) {
		for(var i=parentNode.childNodes.length-1; i>=0; i--) {
			if(parentNode.childNodes[i].id!="closeAdvert" && parentNode.childNodes[i].id!="minimizeAdvert") {
				findActions(parentNode.childNodes[i]);
			}
			else { //关闭广告,最小化广告
				parentNode.childNodes[i].onmouseup = function() {
					var list = Advert.getAdverts(advert.advertPutId);
					for(var j=0; j<list.length; j++) {
						if(this.id=="closeAdvert") {
							list[j].display(true);
						}
						else {
							list[j].minimize();
						}
					}
				};
			}
		}
	};
	findActions(this.advertBody.childNodes[0]);
};

Advert.adverts = new Array(); //广告列表

Advert.showAdverts = function(prepose) { //显示广告
	for(var i=0; i<Advert.adverts.length; i++) {
		if(Advert.adverts[i].putMode=="static") { //固定位置广告
			continue;
		}
		if((prepose && Advert.adverts[i].putMode=="pageTop") || //前置显示时,只显示投放模式为页面顶部的广告
		   (!prepose && Advert.adverts[i].putMode!="pageTop")) {
			Advert.adverts[i].create();
		}
	}
};

Advert.getAdverts = function(advertPutId) { //获取广告列表
	var list = Array();
	for(var i=0; i<Advert.adverts.length; i++) {
		if(Advert.adverts[i].advertPutId==advertPutId) {
			list[list.length] = Advert.adverts[i];
		}
	}
	return list;
};