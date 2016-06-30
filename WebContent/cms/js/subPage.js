//子页面加载
function onSubPageLoad(pageName, widthMode, heightMode, horizontalScrol, verticalScroll) {
	var page = document.getElementById(pageName).contentWindow;
	var pageBody = page.document.body;
	if(pageBody.style.backgroundColor=='') {
		pageBody.style.backgroundColor = 'transparent';
	}
	pageBody.style.overflowX = (horizontalScrol ? "auto" : "hidden");
	pageBody.style.overflowY = (verticalScroll ? "auto" : "hidden");
	if(widthMode=="auto" || heightMode=="auto") { //需要自动调整iframe的尺寸
		adjustIFrameSize(pageName, widthMode, heightMode); //调整iframe尺寸
		//绑定事件
		var win = window;
		var index = 0;
		var adjustSize = function() {
			adjustIFrameSize(pageName, widthMode, heightMode);
			if((index++)<60) { //重复执行60次
				win.setTimeout(adjustSize, 100);
			}
			else {
				index = 0;
			}
		};
		EventUtils.addEvent(pageBody, "mousedown", adjustSize);
		EventUtils.addEvent(pageBody, "mouseup", adjustSize);
		EventUtils.addEvent(pageBody, "propertychange", adjustSize);
	}
}
//调整iframe的尺寸
function adjustIFrameSize(pageName, widthMode, heightMode) {
	var iframe = document.getElementById(pageName);
	var pageBody = iframe.contentWindow.document.body;
	if(!pageBody) {
		return;
	}
	var scrollWidth = pageBody.scrollWidth, scrollHeight = pageBody.scrollHeight;
	if(!document.all) { //如果不是IE追加一个div,用来获取页面的实际高度
		var div = iframe.contentWindow.document.createElement("div");
		div.style.width = "100%";
		div.style.height = "1px";
		div.style.clear = "both";
		pageBody.appendChild(div);
		scrollWidth = div.offsetWidth;
		scrollHeight = div.offsetTop;
		pageBody.removeChild(div);
		if(pageBody.style.paddingLeft) {
			scrollWidth += Number(pageBody.style.paddingLeft.replace("px", ""));
		}
		if(pageBody.style.paddingRight) {
			scrollWidth += Number(pageBody.style.paddingRight.replace("px", ""));
		}
		if(pageBody.style.paddingTop) {
			scrollHeight += Number(pageBody.style.paddingTop.replace("px", ""));
		}
		if(pageBody.style.paddingBottom) {
			scrollHeight += Number(pageBody.style.paddingBottom.replace("px", ""));
		}
		if(pageBody.style.marginLeft) {
			scrollWidth += Number(pageBody.style.marginLeft.replace("px", ""));
		}
		if(pageBody.style.marginRight) {
			scrollWidth += Number(pageBody.style.marginRight.replace("px", ""));
		}
		if(pageBody.style.marginTop) {
			scrollHeight += Number(pageBody.style.marginTop.replace("px", ""));
		}
		if(pageBody.style.marginBottom) {
			scrollHeight += Number(pageBody.style.marginBottom.replace("px", ""));
		}
	}
	if(widthMode=="auto") { //宽度调整
		if(iframe.offsetWidth < scrollWidth || iframe.offsetWidth > scrollWidth + 30) {
			iframe.width = scrollWidth + 10;
		}
	}
	if(heightMode=="auto") { //高度调整
		if(iframe.offsetHeight < scrollHeight || iframe.offsetHeight > scrollHeight + 30) {
			iframe.height = scrollHeight + 10;
		}
	}
}