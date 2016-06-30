//栏目
Column = function(name, category, icon, url) {
	this.name = name; //名称
	this.category = category; //分类
	this.icon = icon; //图标
	this.url = url; //URL
};

//栏目栏
ColumnBar = function(pageElement, displayMode, displaySeconds, currentColumnStyle, columnStyle, moreColumnsOnLeftStyle, moreColumnsOnRightStyle) {
	this.pageElement = pageElement;
	this.displayMode = displayMode; //显示方式,autoHide/alwaysDisplay
	this.displaySeconds = displaySeconds; //自动隐藏时,显示的秒数
	this.currentColumnStyle = currentColumnStyle; //当前栏目样式
	this.columnStyle = columnStyle; //栏目样式
	this.moreColumnsOnLeftStyle = moreColumnsOnLeftStyle; //左侧有栏目提示样式
	this.moreColumnsOnRightStyle = moreColumnsOnRightStyle; //右侧有栏目提示样式
	this.currentColumnIndex = 0; //当前栏目
	window.columnBar = this;
};
ColumnBar.prototype.create = function(currentChannel, currentColumnIndex) { //创建栏目栏, onColumnChanged=function(currentColumn, columnCount){};
	this.currentColumnIndex = currentColumnIndex;
	this.currentChannel = currentChannel;
	if(this.currentChannel.columns.length==1) { //仅有1个栏目
		//隐藏栏目栏
		this.pageElement.style.display = 'none';
		//触发栏目变化事件
		this.onColumnChanged.call(null, this.currentChannel.columns[0], 1);
		return;
	}
	//TODO:从配置文件中获取当前栏目
	var maxColumnIndex = this.currentChannel.getColumnCount();
	//创建栏目栏
	var html = '<table border="0" cellspacing="0" cellpadding="0" style="width: 100%; table-layout: fixed;">' +
			   '	<tr>' +
               '		<td id="moreColumnsOnLeft" class="' + this.moreColumnsOnLeftStyle + '"></td>' +
               '		<td width="100%">' +
			   '			<div id="divColumnItems" style="overflow: hidden">' +
               '				<table border="0" cellspacing="0" cellpadding="2">'
               '					<tr valign="top">';
    for(var i=0; i<maxColumnIndex; i++) {
    	html += '						<td class="' + (i==this.currentColumnIndex ? this.currentColumnStyle : this.columnStyle) + '" nowrap="nowrap" align="center">' +
    			'							<span onclick="window.columnBar.setCurrentColumn(' + i + ');">' + this.currentChannel.columns[i].name + '</span>' +
    			'						</td>';
    }
    html +=	   '						<td id="blankColumn" class="' + this.columnStyle + '" style="width:100%; font-size:1px">&nbsp;</td>' +
               '					</tr>' +
               '				</table>' +
               '			</div>' +
               '		</td>' +
               '		<td id="moreColumnsOnRight" class="' + this.moreColumnsOnRightStyle + '"></td>' +
               '	</tr>' +
               '</table>';
    this.pageElement.innerHTML = html;
    this.onColumnChanged.call(null, this.currentChannel.columns[this.currentColumnIndex], this.currentChannel.columns.length); //触发栏目变动事件
    
    var clumnBar = this;
	//创建频道栏滚动条
	var scroller = new Scroller(document.getElementById('divColumnItems'), true, false, false, false, false);
	scroller.onScrolling = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd, notTouch) {
		clumnBar.show();
	};
	clumnBar.show();
};
ColumnBar.prototype.show = function() { //显示栏目栏
	if(this.currentChannel.columns.length==1) {
		return;
	}
	if(this.hideBarTimer) {
		window.clearTimeout(this.hideBarTimer);
	}
	var divColumnItems = document.getElementById('divColumnItems');
	this.pageElement.style.display = '';
	if(this.scrollLeft && this.scrollLeft>0) {
		divColumnItems.scrollLeft = this.scrollLeft;
		this.scrollLeft = 0;
	}
	
	//判断是否需要显示左(右)侧有栏目提示
	document.getElementById('moreColumnsOnLeft').style.display = divColumnItems.scrollLeft<5 ? "none" : "";
	document.getElementById('moreColumnsOnRight').style.display = divColumnItems.scrollLeft + divColumnItems.offsetWidth >= divColumnItems.scrollWidth - document.getElementById('blankColumn').offsetWidth - 3 ? "none" : "";
	
	if(this.displayMode!='alwaysDisplay') { //显示方式,autoHide/alwaysDisplay
		//自动隐藏栏目栏
		var columnBar = this;
		this.hideBarTimer = window.setTimeout(function() {
			columnBar.scrollLeft = document.getElementById('divColumnItems').scrollLeft;
			columnBar.pageElement.style.display = 'none';
			window.clearTimeout(columnBar.hideBarTimer);
			columnBar.hideBarTimer = 0;
		}, this.displaySeconds * 1000);
	}
};
ColumnBar.prototype.hide = function() { //隐藏栏目栏
	if(this.displayMode!='alwaysDisplay') { //显示方式,autoHide/alwaysDisplay
		columnBar.scrollLeft = document.getElementById('divColumnItems').scrollLeft;
		this.pageElement.style.display = 'none';
	}
};
ColumnBar.prototype.setCurrentColumn = function(currentColumnIndex) { //设置当前栏目
	if(currentColumnIndex==this.currentColumnIndex) {
		return;
	}
	var previousColumnIndex = this.currentColumnIndex;
	this.currentColumnIndex = currentColumnIndex;
	var divColumnItems = document.getElementById('divColumnItems');
	var cells = divColumnItems.getElementsByTagName('table')[0].rows[0].cells;
	cells[previousColumnIndex].className = this.columnStyle;
	cells[currentColumnIndex].className = this.currentColumnStyle;
	
	//调整滚动条位置,使得左右两个频道也能同时显示在屏幕上
	var width = divColumnItems.offsetWidth;
	var left = DomUtils.getAbsolutePosition(cells[Math.max(0, currentColumnIndex-1)], divColumnItems).left;
	var rightChannel = cells[Math.min(this.currentChannel.columns.length-1, currentColumnIndex+1)];
	var right = Math.min(left + width, DomUtils.getAbsolutePosition(rightChannel, divColumnItems).left + rightChannel.offsetWidth);
	if(left < divColumnItems.scrollLeft || right - divColumnItems.scrollLeft > width) {
		divColumnItems.scrollLeft = left - (previousColumnIndex > currentColumnIndex ? 0 : (width - (right-left)));
	}
	this.onColumnChanged.call(null, this.currentChannel.columns[currentColumnIndex], this.currentChannel.columns.length); //触发栏目变动事件
	window.top.columnContainer.loadColumn(this.currentChannel.columns[currentColumnIndex], currentColumnIndex, previousColumnIndex); //加载栏目
	this.show();
};

//栏目容器,onColumnLoaded=function(columnWindow);
ColumnContainer = function(parentElement) {
	if(!parentElement) {
		return;
	}
	this.parentElement = parentElement;
	this.contentWidth = parentElement.clientWidth; //客户端宽度
	this.contentHeight = parentElement.clientHeight; //客户端高度
	window.top.columnContainer = this;
	//初始化栏目容器
	var div = document.createElement('div');
	div.id = "divColumnContents";
	div.style.width = this.contentWidth + "px";
	div.style.height = this.contentHeight + "px";
	div.style.display = 'none';
	div.style.overflow = "hidden";
	var table = document.createElement('table');
	table.id = "tableColumns";
	table.border = '0';
	table.cellPadding = 0;
	table.cellSpacing = 0;
	table.height = "100%";
	table.insertRow(-1);
	div.appendChild(table);
	this.parentElement.appendChild(div);
	
	var columnContainer = this;
	//创建内容区域滚动条
	this.contentsScoller = new Scroller(div, true, false, false, false, false);
	this.contentsScoller.onScrolling = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd, notTouch) {
		if(Math.abs(x)>5 && window.client.columnBar) {
			window.client.columnBar.show(); //显示频道栏
		}
	};
	this.contentsScoller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		if(!window.client.columnBar) {
			columnContainer.loadColumn(columnContainer.currentChannel.columns[0], 0, 0);
			return;
		}
		var columnIndex;
		if(Math.abs(x) < columnContainer.contentWidth/6) { //移动距离小于1/6
			columnIndex = window.client.columnBar.currentColumnIndex;
		}
		else if(x>0) { //移动距离超过1/6,且大于0
			columnIndex = Math.min(window.client.columnBar.currentColumnIndex + 1, columnContainer.currentChannel.columns.length - 1);
		}
		else { //移动距离超过1/6,且小于0
			columnIndex = Math.max(window.client.columnBar.currentColumnIndex - 1, 0);
		}
		if(columnIndex==window.client.columnBar.currentColumnIndex) {
			columnContainer.loadColumn(columnContainer.currentChannel.columns[columnIndex], columnIndex, columnIndex);
		}
		else {
			window.client.columnBar.setCurrentColumn(columnIndex);
		}
	};
};
ColumnContainer.prototype.initColumns = function(currentChannel) { //初始化栏目列表
	this.currentChannel = currentChannel;
	var div = document.getElementById('divColumnContents');
	var table = document.getElementById('tableColumns');
	var row = table.rows[0];
	//清空原来加载的页面
	for(var i=0; i<row.cells.length; i++) {
		//获取iframe
		var iframe = row.cells[i].getElementsByTagName('iframe');
		if(iframe.length>0) {
			iframe[0].src = 'about:blank';
			iframe[0].style.display = "none";
			row.cells[i].childNodes[0].style.display = ""; //把提示信息设置为可见
		}
		//如果单元格数量超过栏目数,则隐藏单元格
		row.cells[i].style.display = i >= currentChannel.getColumnCount() ? "none" : "";
	}
	//如果单元格数量少于栏目数,则创建新的单元格
	for(var i=row.cells.length; i<currentChannel.getColumnCount(); i++) {
		this.createColumnCell(row, i, '<div id="columnLoadingImage" class="pageLoadingImage rotate"></div><div class="columnLoadingText">正在加载,请稍候...</div>');
	}
	table.width = (this.contentWidth * (currentChannel.getColumnCount())) + "px";
	div.style.display = "";
};
ColumnContainer.prototype.createColumnCell = function(row, cellIndex, promptHtml) { //创建栏目单元格
	var cell = row.insertCell(cellIndex);
	cell.style.width = this.contentWidth + "px";
	cell.style.height = this.contentHeight + "px";
	cell.align = "center";
	var div = document.createElement("div");
	cell.appendChild(div);
	div.innerHTML = promptHtml;
};
ColumnContainer.prototype.loadColumn = function(column, currentColumnIndex, previousColumnIndex) { //加载栏目
	//调整滚动条,使内容显示在主屏幕上
	var columnContainer = this;
	var divColumnContents = document.getElementById('divColumnContents');
	var targetScrollLeft = this.contentWidth * currentColumnIndex;
	if(currentColumnIndex==previousColumnIndex) {
		divColumnContents.scrollLeft = targetScrollLeft;
	}
	else {
		this.contentsScoller.scrollWithTimer(targetScrollLeft, false, 10);
	}
	//加载栏目页面
	var url = column.url;
	var tableColumns = document.getElementById('tableColumns');
	var cell = tableColumns.rows[0].cells[currentColumnIndex];
	var iframe = cell.getElementsByTagName('iframe');
	if(iframe.length > 0) {
		if(StringUtils.removeQueryParameter(iframe[0].getAttribute("src", 2), 'client.loadNewestPage')!=url) {
			iframe[0].src = url;
		}
		return;
	}
	//添加帧
	iframe = document.createElement('iframe');
	iframe.showRefreshing = function() { //客户端调用:输出刷新提示
		if(this.contentWindow.document.updater) {
			this.contentWindow.document.updater.createRefreshBar(true);
		}
	};
	iframe.hideRefreshing = function() { //客户端调用:关闭刷新提示
		if(this.contentWindow.document.updater) {
			this.contentWindow.document.updater.destoryRefreshBar(this.contentWindow.document);
		}
	};
	iframe.onload = function() { //客户端调用:初始化页面
		if(this.src == 'about:blank') {
			return;
		}
		var doc = this.contentWindow.document;
		doc.body.style.margin = "0px";
		doc.documentElement.style.width = doc.body.style.width = columnContainer.contentWidth + "px";
		doc.documentElement.style.height = doc.body.style.height = columnContainer.contentHeight + "px";
		this.parentNode.childNodes[0].style.display = 'none';
		//创建滚动条
		doc.scroller = new Scroller(doc.body, false, false, true, false, false);
		this.style.display = '';
		//创建更新器
		new Updater(doc.scroller, false, false);
		//触发栏目加载完成事件
		if(columnContainer.onColumnLoaded) {
			columnContainer.onColumnLoaded.call(null, this.contentWindow);
		}
	}
	iframe.style.borderStyle = 'none';
	iframe.scrolling = "yes";
	iframe.style.display = 'none';
	iframe.style.overflow = 'hidden';
	iframe.style.width = this.contentWidth + "px";
	iframe.style.height = this.contentHeight + "px";
	iframe.src = url;
	cell.appendChild(iframe);
};