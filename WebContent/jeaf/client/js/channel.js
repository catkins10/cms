//频道
Channel = function(pageElement, name, icon, columnCount, columns) {
	this.pageElement = pageElement; //页面元素
	this.name = name; //名称
	this.icon = icon; //图标
	this.columnCount = columnCount; //显示的栏目数,多余的栏目通过用户个人设置选择
	this.columns = columns; //栏目列表
	var channel = this;
	this.pageElement.onclick = function() {
		window.channelBar.changeChannel(channel);
	};
};
Channel.prototype.getColumnCount = function() { //获取显示的栏目数
	var count = this.columnCount;
	if(count<=0 || count>this.columns.length) {
		count = this.columns.length;
	}
	return count;
};
Channel.prototype.select = function(selected) { //选中/取消选择
	if(selected) { //选中
		this.unselectedStyleClass = this.pageElement.className;
		this.pageElement.className = window.channelBar.currentChannelStyle;
	}
	else { //取消选中
		this.pageElement.className = this.unselectedStyleClass;
		this.unselectedStyleClass = null;
	}
};

//频道栏, onChannelChanged = function(currentChannel);
ChannelBar = function(pageElement, displayMode, overflowX, overflowY, marginTop, marginBottom, currentChannelStyle, channels) {
	this.pageElement = pageElement; //页面元素
	this.displayMode = displayMode; //显示方式,alwaysDisplay/总是显示,left2right/从左向右滑动时显示,right2left/从右向左滑动时显示
	this.overflowX = overflowX; //是否显示水平滚动条
	this.overflowY = overflowY; //是否显示垂直滚动条
	this.marginTop = marginTop; //顶部边距
	this.marginBottom = marginBottom; //底部边距
	this.currentChannelStyle = currentChannelStyle; //当前频道样式
	this.channels = channels; //频道列表
	window.channelBar = this;
};
ChannelBar.prototype.create = function() { //创建频道栏
	//TODO:从配置文件中读取当前频道
	this.changeChannel(this.channels[this.channels.length-1]); //默认返回频道列表的最后一个频道
};
ChannelBar.prototype.changeChannel = function(channel) { //改变频道
	if(channel==this.currentChannel) {
		return;
	}
	if(this.currentChannel) {
		this.currentChannel.select(false);
	}
	this.currentChannel = channel;
	this.currentChannel.select(true);
	this.onChannelChanged.call(null, this.currentChannel);
};