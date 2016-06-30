Timer = function() {
	
};
Timer.prototype.schedule = function(task, delay, period) { //执行任务
	this.task = task;
	this.period = period;
	this.canceled = false;
	this._processTask(delay);
};
Timer.prototype._processTask = function(delay) { //执行任务
	var timer = this;
	this.timeout = window.setTimeout(function() {
		var time = new Date().valueOf();
		timer.t = time;
		timer.task.call(null);
		if(timer.canceled || !timer.period || timer.period<=0) {
			return;
		}
		timer._processTask(timer.period - (new Date().valueOf() - time));
	}, Math.max(0, delay));
};
Timer.prototype.cancel = function() { //取消
	if(this.timeout) {
		window.clearTimeout(this.timeout);
	}
	this.canceled = true;
};