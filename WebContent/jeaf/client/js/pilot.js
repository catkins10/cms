Pilot = function(pilotPageContainer, verticalScroll) {
	this.pilotPageContainer = pilotPageContainer;
	this.pilotPages = pilotPageContainer.childNodes;
	this.verticalScroll = verticalScroll;
};
Pilot.prototype.create = function() {
	this.pageIndex = 0;
	for(var i=0; i<this.pilotPages.length; i++) {
		this.pilotPages[i].style.overflow = "hidden";
		this.pilotPages[i].style.width = window.top.client.clientWidth + "px";
		this.pilotPages[i].style.height = window.top.client.clientHeight + "px";
	}
	this.pilotPageContainer.style.width = (window.top.client.clientWidth * (this.verticalScroll ? 1 : this.pilotPages.length)) + "px";
	this.pilotPageContainer.style.height = (window.top.client.clientHeight * (this.verticalScroll ? this.pilotPages.length : 1)) + "px";
	
	var pilot = this;
	//创建内容区域滚动条
	document.body.style.width = '100%';// window.top.client.clientWidth + "px";
	document.body.style.height = '100%';//window.top.client.clientHeight + "px";
	this.scroller = new Scroller(document.body, !this.verticalScroll, this.verticalScroll, false, false, false);
	this.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		var pageIndex;
		var moved = pilot.verticalScroll ? y : x;
		var size = pilot.verticalScroll ? window.top.client.clientHeight : window.top.client.clientWidth;
		if(Math.abs(moved) < size / 8) { //移动距离小于1/8
			pageIndex = pilot.pageIndex;
		}
		else if(moved>0) { //移动距离超过1/8,且大于0
			if(pilot.pageIndex==pilot.pilotPages.length - 1) { //已经是最后一页
				pilot.showMainPage(); //显示主页面
				return;
			}
			pageIndex = Math.min(pilot.pageIndex + 1, pilot.pilotPages.length - 1);
		}
		else { //移动距离超过1/8,且小于0
			pageIndex = Math.max(pilot.pageIndex - 1, 0);
		}
		pilot.gotoPage(pageIndex);
	};
};
Pilot.prototype.gotoPage = function(pageIndex) { //翻页
	this.scroller.scrollWithTimer((this.verticalScroll ? window.top.client.clientHeight : window.top.client.clientWidth) * pageIndex, this.verticalScroll, 10);
	this.pageIndex = pageIndex;
};
Pilot.prototype.showMainPage = function() { //显示主页
	window.top.setTimeout("window.client.onPilotComplete();", 1);
};