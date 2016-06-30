var ajax = new Ajax();
function onInputQuestion(src, siteId) { //用户在输入
	var value = src.value;
	if(value=='') {
		showPromptArea(false);
		return;
	}
	var url = RequestUtils.getContextPath() + '/cms/onlineservice/faq/robot.shtml?siteId=' + siteId + '&action=retrieveQuestionPrompt&key=' + StringUtils.utf8Encode(value) + '&seq=' + Math.random();
	ajax.openRequest('get', url, '', true, function() {
		//输出问题提示
		var questionPrompts = document.getElementById('questionPrompts')
		questionPrompts.innerHTML = getResponseContent(ajax.getResponseText(), 'RELATION_FAQS_BEGIN', 'RELATION_FAQS_END');
		showPromptArea(questionPrompts.getElementsByTagName('a').length>0);
	});
}
function sendQuestion(siteId) { //发送
	var input = document.getElementsByName("question")[0];
	if(input.value=='' || input.value==input.alt) {
		return;
	}
	var url = RequestUtils.getContextPath() + '/cms/onlineservice/faq/robot.shtml?siteId=' + siteId + '&action=sendQuestion&question=' + StringUtils.utf8Encode(input.value) + '&seq=' + Math.random();
	doSend(url);
}
function openFaq(id, siteId) {
	var url = RequestUtils.getContextPath() + '/cms/onlineservice/faq/robot.shtml?siteId=' + siteId + '&action=sendQuestion&faqId=' + id + '&seq=' + Math.random();
	doSend(url);
}
function doSend(url) { //执行发送
	showPromptArea(false); //隐藏提示信息
	var input = document.getElementsByName("question")[0];
	input.value = ""; //清空输入框
	input.focus();
	ajax.openRequest('get', url, '', true, function() {
		var response = ajax.getResponseText();
		//输出对话记录
		var talkRecordArea = document.getElementById('talkRecordArea');
		var span = talkRecordArea.appendChild(document.createElement('span')); //在聊天区域创建一个新的span
		span.innerHTML = getResponseContent(response, 'TALK_RECORDS_BEGIN', 'TALK_RECORDS_END');
		
		//获取有滚动条的元素
		for(var obj = talkRecordArea; obj!=document.body ; obj=obj.parentNode) {
			if(obj.clientHeight>0 && obj.scrollHeight>0 && obj.scrollHeight!=obj.clientHeight) {
				obj.scrollTop = (obj.scrollHeight - obj.clientHeight > span.offsetTop ? span.offsetTop - 3 : obj.scrollHeight); //设置滚动条位置
				break;
			}
		}
		
		//输出相关问题列表
		document.getElementById('relationFaqs').innerHTML = getResponseContent(response, 'RELATION_FAQS_BEGIN', 'RELATION_FAQS_END');
	});
}
function showPromptArea(show) {
	document.getElementById('promptArea').style.display = !show ? "none" : "";
}
function getResponseContent(response, beginString, endString) {
	var beginIndex = response.indexOf(beginString);
	if(beginIndex==-1) {
		return;
	}
	beginIndex += beginString.length;
	var endIndex = response.indexOf(endString, beginIndex);
	return response.substring(beginIndex, endIndex);
}