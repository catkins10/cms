//直线型进度条
LinearProgressBar = function() {

};
LinearProgressBar.prototype.create = function(doc) { //创建进度条,需要配置的样式:.linearProgressBar、.linearProgressBarProgress
	this.progressBar = doc.createElement('div');
	this.progressBar.className = 'linearProgressBar';
	this.progressBar.style.width = "100%";
	var progress = doc.createElement('div');
	progress.className = 'linearProgressBarProgress';
	progress.style.width = '0%';
	this.progressBar.appendChild(progress);
	return this.progressBar;
};
LinearProgressBar.prototype.showProgress = function(percentage) { //显示进度
	this.progressBar.childNodes[0].style.width = percentage + "%";
};

//圆形进度条
CircleProgressBar = function() {

};
CircleProgressBar.prototype.create = function(doc) { //创建进度条,需要配置的样式:.circleProgressBar、.circleProgressBarProgress
	var size = Number(CssUtils.getStyleByName(doc, '.circleProgressBar', 'width').replace('px', '')) + 2 * Number(CssUtils.getStyleByName(doc, '.circleProgressBar', 'borderWidth').replace('px', ''));
	this.progressBar = doc.createElement('div');
	this.progressBar.style.position = 'relative';
	this.progressBar.align = 'left';
	this.progressBar.style.width = this.progressBar.style.height = size + 'px';
	for(var i=0; i<3; i++) {
		var outerDiv = doc.createElement('div');
		outerDiv.style.position = 'absolute';
		outerDiv.style.left = outerDiv.style.top = '0px';
		outerDiv.style.width = outerDiv.style.height = size + 'px';
		if(i==1) {
			CssUtils.setDegree(outerDiv, 180); //旋转180度
		}
		outerDiv.style.zIndex = i==0 ? 2 : (i==1 ? 0 : 1);
		var halfCircleDiv = doc.createElement('div');
		halfCircleDiv.style.width = (size/2 + 1) + 'px';
		halfCircleDiv.style.height = size + 'px';
		halfCircleDiv.style.overflow = 'hidden';
		outerDiv.appendChild(halfCircleDiv);
		var circleDiv = doc.createElement('div');
		circleDiv.className = 'circleProgressBar' + (i==2 ? ' circleProgressBarProgress' : '');
		halfCircleDiv.appendChild(circleDiv);
		this.progressBar.appendChild(outerDiv);
	}
	return this.progressBar;
};
CircleProgressBar.prototype.showProgress = function(percentage) { //显示进度
	CssUtils.setDegree(this.progressBar.childNodes[2], 360/100*percentage);
	this.progressBar.childNodes[0].style.zIndex = percentage>=50 ? 0 : 2;
	this.progressBar.childNodes[1].childNodes[0].childNodes[0].className = 'circleProgressBar' + (percentage>=50 ? ' circleProgressBarProgress' : '');
};