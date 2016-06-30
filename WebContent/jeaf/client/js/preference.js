PreferenceBar = function(pageElement, displayMode, overflowX, overflowY, marginTop, marginBottom) {
	this.pageElement = pageElement; //页面元素
	this.displayMode = displayMode; //显示方式,alwaysDisplay/总是显示,left2right/从左向右滑动时显示,right2left/从右向左滑动时显示
	this.overflowX = overflowX; //是否显示水平滚动条
	this.overflowY = overflowY; //是否显示垂直滚动条
	this.marginTop = marginTop; //顶部边距
	this.marginBottom = marginBottom; //底部边距
};
PreferenceBar.prototype.create = function() { //创建栏目设置栏
	
};