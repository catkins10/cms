/**
 * 进度条
 * 定位中事件: onSeeking(ratio), ratio: 0~1
 * 定位完成事件: onSeeked(ratio), ratio: 0~1
 **/
ProgressBar = function(parentElement, progressBarClassName, progressClassName, secondProgressClassName, thumbClassName, thumbOverClassName) {
	this.parentElement = parentElement;
	//创建进度条
	this.progressBar = parentElement.ownerDocument.createElement('div');
	this.progressBar.className = progressBarClassName;
	parentElement.appendChild(this.progressBar);
	
	//第二进度
	if(secondProgressClassName) {
		this.secondProgress = parentElement.ownerDocument.createElement('div');
		this.secondProgress.className = secondProgressClassName;
		this.secondProgress.style.width = '0%';
		this.progressBar.appendChild(this.secondProgress);
	}
	
	//进度
	this.progress = parentElement.ownerDocument.createElement('div');
	this.progress.className = progressClassName;
	if(secondProgressClassName) {
		this.progress.style.marginTop = '-' + CssUtils.getElementComputedStyle(this.progressBar, 'height');
	}
	this.progress.style.width = '0%';
	this.progressBar.appendChild(this.progress);
	
	//进度调整块
	if(thumbClassName) {
		this.thumbContainer = parentElement.ownerDocument.createElement('div');
		this.thumbContainer.style.display = 'inline-block';
		this.progressBar.appendChild(this.thumbContainer);
		
		this.thumb = parentElement.ownerDocument.createElement('div');
		this.thumb.style.display = 'block';
		this.thumb.className = thumbClassName;
		this.thumbContainer.appendChild(this.thumb);
		
		this.thumbClassName = thumbClassName;
		this.thumbOverClassName = thumbOverClassName;
	}
	this._processEvents();
};
ProgressBar.prototype._processEvents = function() { //处理进度条事件
	var bar = this;
	this.progressBar.onclick = function(event) {
		var ratio = event.offsetX / bar.progressBar.offsetWidth;
		if(bar.onSeeking) {
			bar.onSeeking(ratio); //定位中
		}
		if(bar.onSeeked) {
			bar.onSeeked(ratio); //定位
		}
		bar._setProgress(ratio);
	};
	if(!this.thumb) {
		return;
	}
	this.thumb.onmouseover = function() {
		bar.thumbOver = true;
		bar.thumb.className = bar.thumbOverClassName;
	};
	this.thumb.onmouseout = function() {
		bar.thumbOver = false;
		bar.thumb.className = bar.thumbPressed ? bar.thumbOverClassName : bar.thumbClassName;
	};
	this.thumb.onmousedown = function(event) {
		bar.thumbPressed = true;
		var startX = event.screenX;
		var startLeft = Number(bar.thumbContainer.style.marginLeft.replace('px', ''));
		var onmousemove = this.ownerDocument.body.onmousemove;
		var onmouseup = this.ownerDocument.body.onmouseup;
		var onselectstart = this.ownerDocument.body.onselectstart;
		this.ownerDocument.body.onselectstart = function() {
			return false;
		};
		this.ownerDocument.body.onmousemove = function(event) {
			var left = Math.min(Math.max(0, startLeft + event.screenX - startX), bar.progressBar.offsetWidth - bar.thumbContainer.offsetWidth);
			bar.thumbContainer.style.marginLeft = left + 'px';
			bar._setProgress(left / bar.progressBar.offsetWidth);
			if(bar.onSeeking) {
				bar.onSeeking(left / bar.progressBar.offsetWidth); //定位中
			}
		};
		this.ownerDocument.body.onmouseup = function(event) {
			bar.thumbPressed = false;
			bar.thumb.className = bar.thumbOver ? bar.thumbOverClassName : bar.thumbClassName;
			this.onselectstart = onselectstart;
			this.onmousemove = onmousemove;
			this.onmouseup = onmouseup;
			if(bar.onSeeked) {
				bar.onSeeked(Number(bar.thumbContainer.style.marginLeft.replace('px', '')) / bar.progressBar.offsetWidth); //定位
			}
		};
	};
	this.thumb.ontouchstart = function(event) {
		bar.thumbPressed = true;
		this.startX = event.touches[0].screenX;
		this.startLeft = Number(bar.thumbContainer.style.marginLeft.replace('px', ''));
		bar.thumb.className = bar.thumbOverClassName;
	};
	this.thumb.ontouchmove = function(event) {
		var left = Math.min(Math.max(0, this.startLeft + event.touches[0].screenX - this.startX), bar.progressBar.offsetWidth - bar.thumbContainer.offsetWidth);
		bar.thumbContainer.style.marginLeft = left + 'px';
		bar._setProgress(left / bar.progressBar.offsetWidth);
		if(bar.onSeeking) {
			bar.onSeeking(left / bar.progressBar.offsetWidth); //定位中
		}
	};
	this.thumb.ontouchend = function(event) {
		bar.thumbPressed = false;
		bar.thumb.className = bar.thumbClassName;
		if(bar.onSeeked) {
			bar.onSeeked(Number(bar.thumbContainer.style.marginLeft.replace('px', '')) / bar.progressBar.offsetWidth); //定位
		}
	};
	this.thumb.onclick = function(event) {
		if(event.stopPropagation) {
			event.stopPropagation();
		}
		else {
			event.cancelBubble = true;
		}
	};
};
ProgressBar.prototype.showThumb = function(show) { //显示或隐藏进度调整块
	this.thumb.style.display = this.thumbPressed || show ? 'block' : 'none';
	this._setProgress(this.ratio);
};
ProgressBar.prototype.setProgress = function(ratio) { //设置进度
	if(!this.thumbPressed) {
		this._setProgress(ratio);
	}
};
ProgressBar.prototype._setProgress = function(ratio) { //设置进度
	this.ratio = Math.min(1, Math.max(0, ratio));
	this.progress.style.width = (this.ratio * 100) + "%";
	if(!this.thumbPressed && this.thumb) {
		this.thumbContainer.style.marginLeft = Math.min(Math.floor(this.ratio * this.progressBar.offsetWidth), this.progressBar.offsetWidth - this.thumbContainer.offsetWidth) + 'px';
	}
};
ProgressBar.prototype.setSecondProgress = function(ratio) { //设置第二进度
	this.secondProgress.style.width = (Math.min(1, Math.max(0, ratio)) * 100) + "%"
};