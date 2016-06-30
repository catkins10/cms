function submitTest(force) { //提交
	if(!force) { //不是强制提交
		//检查是否还有未完成的题目
		var spans  = document.getElementsByTagName("span");
		var notAnswered = new Array();
		for(var i=0; i<spans.length; i++) {
			if(!spans[i].id || spans[i].id.indexOf("answerArea_")!=0) {
				continue;
			}
			var id = spans[i].id.substring("answerArea_".length);
			var answered = false;
			var inputs = spans[i].getElementsByTagName("input");
			for(var j=0; !answered && j<(inputs ? inputs.length : 0); j++) {
				if(!inputs[j].name || inputs[j].name!="answer_" + id) {
					continue;
				}
				if(inputs[j].type=="radio" || inputs[j].type=="checkbox") {
					answered = inputs[j].checked;
				}
				else {
					answered = inputs[j].value!="";
				}
			}
			if(answered) {
				continue;
			}
			inputs = spans[i].getElementsByTagName("textarea");
			for(var j=0; j<(inputs ? inputs.length : 0); j++) {
				if(inputs[j].name && inputs[j].name=="answer_" + id) {
					answered = inputs[j].value!="";
					break;
				}
			}
			if(!answered) {
				notAnswered[notAnswered.length] = spans[i].title;
			}
		}
		if(notAnswered.length>0) {
			if(!confirm((notAnswered.length<=10 ? notAnswered.join("、") : notAnswered.slice(0,10).join("、") + "等" + notAnswered.length + "题目") + "未作答，是否继续提交?")) {
				return false;
			}
		}
	}
	FormUtils.submitForm();
}
function correctTest() { //批改试卷
	var selects  = document.getElementsByTagName("select");
	var notCorrect = new Array();
	for(var i=0; i<selects.length; i++) {
		if(!selects[i].name || selects[i].name.indexOf("score_")!=0) {
			continue;
		}
		if(selects[i].selectedIndex<=0) {
			notCorrect[notCorrect.length] = selects[i].title;
		}
	}
	if(notCorrect.length>0) {
		if(!confirm((notCorrect.length<=10 ? notCorrect.join("、") : notCorrect.slice(0,10).join("、") + "等" + notCorrect.length + "题目") + "未批改，是否继续提交?")) {
			return false;
		}
	}
	FormUtils.submitForm();
}
function checkTest() { //复核
	FormUtils.submitForm();
}

var startTime; //开始时间
var examSeconds; //考试时长
function updateTimeLeft() { //更新考试剩余时间
	var usedTime = new Date().getTime() - startTime;
	var timeLeft = examSeconds - Math.floor(usedTime/1000);
	if(timeLeft<=0) { //考试时间已到
		alert("考试时间已到");
		submitTest(true); //提交试卷
		return;
	}
	var leftSeconds = timeLeft % 60;
	var leftMinutes = (timeLeft - leftSeconds) / 60;
	document.getElementById("timeLeft").innerHTML = leftMinutes>0 ? (leftMinutes + Math.round(leftSeconds/60)) + "分钟" : leftSeconds + "秒钟"; //(leftMinutes>0 ? leftMinutes + "分" + (leftSeconds==0 ? "钟" : "") : "") + (leftSeconds>0 ? leftSeconds + "秒" : "");
}

window.onload = function() {
	startTime = new Date().getTime();
	examSeconds = Number(document.getElementById("timeLeft").innerHTML.replace("分钟", "")) * 60;
	window.setInterval(updateTimeLeft, 800);
};