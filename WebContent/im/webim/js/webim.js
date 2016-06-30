//类:WEBIM
Webim = function(userId, webimSetting, isCustomerService) {
	this.userId = userId;
	this.webimSetting = webimSetting;
	this.isCustomerService = isCustomerService; //是否客服
	this.actions = new Array(); //按钮列表
	this.systemMessageIds = new Array(); //系统消息ID列表
	this.messageAjax = new Ajax(); //消息监听AJAX
	this.lastActiveTime = null;
	var div = document.body.childNodes[0];
	window.webim = this;
	window.webimSetting = webimSetting;
	if(parent.frames["frameNotify"]) { //有消息通知帧
		this.notifyURL = location.search.substring(location.search.indexOf("notifyURL=") + "notifyURL=".length); //消息通知用的URL
		this.notifies = new Array(); //通知列表
	}
	if(isCustomerService) { //客服
		//创建对话操作按钮
		var href = location.href;
		this.addAction(null, "留言", "/cms/messageboard/message.shtml?forApplication=im/cs", Number(StringUtils.getPropertyValue(href, "messageDialogWidth", "600")), Number(StringUtils.getPropertyValue(href, "messageDialogHeight", "430")), "center", "", "");
		this.addAction(null, "服务评价", "/im/cs/evaluation.shtml", Number(StringUtils.getPropertyValue(href, "evaluationDialogWidth", "500")), Number(StringUtils.getPropertyValue(href, "evaluationDialogHeight", "300")), "center", "", "");
		this.addAction(null, "对话", "/im/webim/chat.shtml", Number(StringUtils.getPropertyValue(href, "chatDialogWidth", "500")), Number(StringUtils.getPropertyValue(href, "chatDialogHeight", "300")), "center", "", "");

		window.setInterval("if(window.webim.getActiveAction()==null)window.location.reload();", 30000); //每隔30秒更新一次页面
		//页面关闭后,通知服务器关闭客服对话
		window.onunload = window.onbeforeunload  = function() {
			window.webim.closeCustomerServiceChat();
		};
	}
	else {
		this.openAjaxRequest("ajax/login.shtml?status=" + webimSetting.loginStatus, false); //登录(同步请求)
		//页面关闭后,通知服务器检查用户是否下线
		window.onunload = window.onbeforeunload  = function() {
			window.webim.openAjaxRequest("ajax/unloadWebim.shtml", false); //同步请求
		};
		window.setTimeout("window.webim.retrieveMessage();", 3000); //3秒后启动消息监听
	}
}; 

//添加WEBIM按钮
Webim.prototype.addAction = function(elementId, actionName, dialogUrl, dialogWidth, dialogHeight, dialogAlign, selectedStyle, mouseoverStyle) {
	if(elementId==null) {
		var span = document.createElement("span");
		elementId = span.id = "span_" + actionName;
		span.style.width = "0px";
		span.style.height = "0px";
		document.getElementById("divWebim").appendChild(span);
	}
	var webimAction = new WebimAction(this, elementId, actionName, dialogUrl, dialogWidth, dialogHeight, dialogAlign, selectedStyle, mouseoverStyle);
	this.actions[this.actions.length] = webimAction;
	return webimAction;
};

//按名称获取WEBIM按钮
Webim.prototype.getAction = function(actionName) {
	for(var i=this.actions.length-1; i>=0; i--) {
		if(this.actions[i].actionName==actionName) {
			return (actionName=="对话" && this.actions[i].chatId=="" ? null : this.actions[i]);
		}
	}
};

//获取当前活动的WEBIM按钮
Webim.prototype.getActiveAction = function() {
	for(var i=this.actions.length-1; i>=0; i--) {
		if(this.actions[i].selected) {
			return this.actions[i];
		}
	}
};

//关闭Webim
Webim.prototype.closeWebim = function() {
	this.closeWebimDialog(false); //关闭对话框
	this.sendNotify("closeWebim"); //发送通知请求宿主窗口关闭WEBIM
};

Webim.prototype.closeCustomerServiceChat = function() {
	if(this.getAction("对话")!=null) {
		this.openAjaxRequest("ajax/closeCustomerServiceChat.shtml?customerServiceChatId=" + this.getAction("对话").chatId, false); //同步请求
	}
};

//设置webim为活动状态
Webim.prototype.setActive = function() {
	this.lastActiveTime = new Date();
};

//通知宿主页面显示WEBIM主窗口
Webim.prototype.show = function() {
	var div = document.body.childNodes[0];
	if(div.offsetWidth<=5 || div.offsetWidth>=3000) {
		window.setTimeout('window.webim.show();', 100);
	}
	else{
		this.sendNotify("showWebim$$" + div.offsetWidth + "$$" + div.offsetHeight); //发送通知请求宿主窗口调整IFRAME大小
	}
};

//发起一个AJAX请求
Webim.prototype.openAjaxRequest = function(ajaxURL, async, data) {
	var ajax = new Ajax();
	var onAjaxError = function() { //错误处理
		alert("请求错误。"); //输出错误信息
	};
	var onAjaxDataArrive = function() { //处理接收到数据
		window.webim.processMessages(ajax.getResponseXML());
	};
	ajaxURL += (ajaxURL.lastIndexOf("?")==-1 ? "?" : "&") + "seq=" + Math.round(Math.random() * 1000000000);
	if(this.isCustomerService && this.getAction("对话")!=null && ajaxURL.indexOf("customerServiceChatId=")==-1) { //客服页面
		ajaxURL += "&customerServiceChatId=" + this.getAction("对话").chatId;
	}
	ajax.openRequest((data && data!="" ? "POST" : "GET"), ajaxURL, (!data ? "" : data), async, (async ? onAjaxDataArrive : null), onAjaxError);
	if(!async) {
		window.webim.processMessages(ajax.getResponseXML());
	}
};

//接收消息
Webim.prototype.retrieveMessage = function() {
	//错误处理
	var onAjaxError = function() {
		if(window.webim.messageAjax.getErrorCount() < 100) { //连续出错小于100次
			window.setTimeout("window.webim.retrieveMessage();", 20000); //等待20秒后重试
		}
		else {
			alert("出错了,请检查网络连接是否正确。"); //输出错误信息
		}
	};
	
	//处理接收到数据
	var onAjaxDataArrive = function() {
		var result = true;
		try {
			result = window.webim.processMessages(window.webim.messageAjax.getResponseXML());
		}
		catch(e) {
			
		}
		if(result) {
			window.setTimeout("window.webim.retrieveMessage();", 1); //继续获取下一组消息
		}
	};
	var url = "ajax/retrieveMessage.shtml?seq=" + Math.round(Math.random() * 1000000000);
	if(this.isCustomerService) { //客服页面
		if(this.getAction("对话")==null) { //没有对话
			return;
		}
		url += "&customerServiceChatId=" + this.getAction("对话").chatId;
	}
	this.messageAjax.openRequest("GET", url, "", true, onAjaxDataArrive, onAjaxError);
};

//处理收到的消息,如果不是会话错误,返回true
Webim.prototype.processMessages = function(responseXML) {
	var messages = responseXML.getElementsByTagName("Messages")[0];
	if(!messages) {
		return true;
	}
	messages = messages.childNodes;
	for(var i=0; i<messages.length; i++) {
		if("SessionFailed"==messages[i].tagName) { //会话错误
			window.webim.processSessionFailed(messages[i]);
			return false;
		}
		else if("ServerException"==messages[i].tagName) { //服务器错误
			alert("服务器错误");
		}
		else if("LoginAck"==messages[i].tagName) { //登录结果
			window.webim.processLoginAck(messages[i]);
		}
		else if("OnlineCountChanged"==messages[i].tagName) { //登录用户数发生变化
			window.webim.processOnlineCountChanged(messages[i]);
		}
		else if("WebimChat"==messages[i].tagName || "ChatDetail"==messages[i].tagName) { //对话
			window.webim.processWebimChat(messages[i]);
		}
		else if("ChatCreateFailed"==messages[i].tagName) { //对话创建失败
			window.webim.openWebimDialog(window.webim.getAction("留言"), true); //打开留言窗口
		}
		else if("Talk"==messages[i].tagName) { //发言
			window.webim.processWebimTalk(messages[i]);
		}
		else if("TalkDetail"==messages[i].tagName) { //发言详细内容
			window.webim.processTalkDetail(messages[i]);
		}
		else if("SystemMessage"==messages[i].tagName) { //系统消息
			window.webim.processSystemMessage(messages[i]);
		}
		else if("SystemMessageDetail"==messages[i].tagName) { //系统消息详细内容
			window.webim.processSystemMessageDetail(messages[i]);
		}
	}
	return true;
};

//处理会话错误
Webim.prototype.processSessionFailed = function(xmlMessage) {
	new WebimLoader().login(); //重新登录
};

//处理登录结果
Webim.prototype.processLoginAck = function(xmlMessage) {
	//获取离线对话ID列表
	var offlineChats = this.getMessageProperty(xmlMessage, "offlineChats");
	if(offlineChats=="") {
		return;
	}
	var loadOfflineChats = function() {
		var chats = offlineChats.split("##");
		for(var i=0; i<chats.length; i++) {
			window.webim.loadChat(chats[i].split("$$")[0]); //加载对话
		}
		//如果用户允许自动打开对话窗口,则打开第一个对话窗口
		if(window.webimSetting.openChatDialogOnReceived) {
			var chatId = chats[0].split("$$")[0];
			if(window.webim.openChatDialog(chatId, false)) { //打开对话框
				window.webim.retrieveTalks(window.webim.getChatActionByChatId(chatId)); //获取发言记录
			}
		}
	};
	window.setTimeout(loadOfflineChats, 1000);
};
//处理上线用户数变更通知
Webim.prototype.processOnlineCountChanged = function(xmlMessage) {
	var onlineCount = Number(this.getMessageProperty(xmlMessage, "onlineCount"))
	var webim = this;
	var setOnlinePersonCount = function() {
		if(webim.getAction("在线用户")!=null) {
			webim.getAction("在线用户").setRecordCount(onlineCount);
		}
		else {
			window.setTimeout(setOnlinePersonCount, 100);
		}
	};
	window.setTimeout(setOnlinePersonCount, 100);
};

//处理对话,创建对话按钮
Webim.prototype.processWebimChat = function(xmlMessage) {
	//检查是否第一个对话
	var count = 0;
	var chatAction = null;
	for(var i=0; i<this.actions.length; i++) {
		var webimAction = this.actions[i];
		if("对话"==webimAction.actionName) {
			chatAction = webimAction;
			if(chatAction.chatPersonIds!="") {
				count++;
			}
		}
	}
	var chatId = this.getMessageProperty(xmlMessage, "chatId");
	var chatActionElement = chatAction.element;
	if(count==0) { //第一个对话
		this.actions.splice(this.actions.length-1, 1);
	}
	else {
		//创建新的按钮
		chatActionElement = chatActionElement.cloneNode(false);
		chatActionElement.id = "webimAction_" + chatId;
		chatActionElement.style.display = "none";
		chatAction.element.parentNode.insertBefore(chatActionElement, chatAction.element); //插入到最后一个对话前面
	}
	chatActionElement.innerHTML = this.getMessageProperty(xmlMessage, "chatActionHTML"); //设置对话按钮的innerHTML
	var normalStyle = chatAction.normalStyle;
	chatAction = this.addAction(chatActionElement.id, "对话", chatAction.dialogUrl, chatAction.defaultDialogWidth, chatAction.defaultDialogHeight, chatAction.dialogAlign, chatAction.selectedStyle, chatAction.mouseoverStyle); //添加WEBIM按钮
	chatAction.normalStyle = normalStyle;
	chatAction.chatId = chatId; //设置对话ID
	chatAction.isCustomerServiceChat = this.getMessageProperty(xmlMessage, "customerService")=="true"; //是否客服对话
	chatAction.lastReadTime = this.getMessageProperty(xmlMessage, "lastReadTime"); //最后接收消息的时间
	chatAction.chatPersonIds = this.getMessageProperty(xmlMessage, "chatPersonIds"); //对话用户ID
	if(!this.isCustomerService) {
		chatAction.element.style.display = "block"; //设为显示状态
	}
	this.show(); //调整主窗口尺寸
};

//处理发言(不含发言内容)
Webim.prototype.processWebimTalk = function(xmlMessage) {
	var chatId = this.getMessageProperty(xmlMessage, "chatId");
	var selfTalk = this.getMessageProperty(xmlMessage, "selfTalk");
	if(selfTalk!="true") { //不是本人
		//获取焦点
		if(this.webimSetting.setFocusOnReceived) {
			//检查当前WEBIM是否处于活动状态
			if(this.lastActiveTime==null || (new Date()).getTime()-this.lastActiveTime.getTime()>180000) { //超过3分钟没有活动
				top.focus();
			}
		}
		//播放音乐
		if(this.webimSetting.playSoundOnReceived) {
			this.playSound();
		}
	}
	//获取对话按钮
	var chatAction = this.getChatActionByChatId(chatId);
	if(!chatAction) { //对话按钮不存在
		if(!this.loadChat(chatId)) { //请求创建对话
			return;
		}
		chatAction = this.getChatActionByChatId(chatId);
	}
	else { //对话按钮已经存在,修改未读发言数
		chatAction.setRecordCount(chatAction.getRecordCount() + 1);
	}
	//打开对话窗口
	if(!chatAction.selected && this.webimSetting.openChatDialogOnReceived) {
		this.openChatDialog(chatId, false);
	}
	//如果对话框已经打开,请求获取未读发言
	if(chatAction.selected) {
		this.retrieveTalks(chatAction);
	}
};
//处理发言详细内容
Webim.prototype.processTalkDetail = function(xmlMessage) {
	var chatId = this.getMessageProperty(xmlMessage, "chatId");
	var chatAction = this.getChatActionByChatId(chatId);
	if(!chatAction) { //对话已经被关闭
		if(!this.loadChat(chatId)) { //请求创建对话
			return;
		}
		chatAction = this.getChatActionByChatId(chatId);
	}
	chatAction.setRecordCount(0);
	if(!chatAction.selected) {
		this.openWebimDialog(chatAction, true); //显示对话窗口
	}
	//设置最后接收到发言时间
	chatAction.lastReadTime = this.getMessageProperty(xmlMessage, "createdMillis");
	//输出发言
	var task = function() {
		try {
			var chatWindow = parent.frames["frame_" + chatAction.getDialogUrl()]; //获取对话窗口
			if(chatWindow && chatWindow.webimChat && !chatWindow.webimChat.loading) {
				try {
					chatWindow.webimChat.writeTalk(window.webim.getMessageProperty(xmlMessage, "content")); //输出对话内容
				}
				catch(ce) {
					
				}
				return;
			}
		}
		catch(e) {
			
		}
		window.setTimeout(task, 300); //对话窗口未创建好,等待300ms后重试
	};
	task.call();
};

//处理系统消息
Webim.prototype.processSystemMessage = function(xmlMessage) {
	var systemMessageId = this.getMessageProperty(xmlMessage, "systemMessageId");
	//获取焦点
	if(this.webimSetting.setFocusOnReceived) { //有消息到达时,获取焦点
		//检查当前WEBIM是否处于活动状态
		if(this.lastActiveTime==null || (new Date()).getTime()-this.lastActiveTime.getTime()>180000) { //超过3分钟没有活动
			top.focus();
		}
	}
	//播放音乐
	if(this.webimSetting.playSoundOnReceived) {
		this.playSound();
	}
	//添加到系统消息ID列表
	for(var i=0; i<this.systemMessageIds.length; i++) {
		if(this.systemMessageIds[i]==systemMessageId) {
			return; //消息已经存在
		}
	}
	this.systemMessageIds[this.systemMessageIds.length] = systemMessageId;
	//修改未读消息数
	this.getAction("系统消息").setRecordCount(this.getAction("系统消息").getRecordCount() + 1);
	//打开系统消息窗口
	if(!this.getAction("系统消息").selected || this.systemMessageIds.length==1) { //系统消息对话框未打开,或者当前消息第一个系统消息
		if(this.webimSetting.openChatDialogOnReceived || this.getAction("系统消息").selected) {
			this.retrieveSystemMessage();
		}
	}
	if(this.getAction("系统消息").systemMessageId=="") {
		this.getAction("系统消息").systemMessageId = this.systemMessageIds[0];
		this.getAction("系统消息").feedback = false;
	}
};

//显示系统消息
Webim.prototype.retrieveSystemMessage = function() {
	if(this.systemMessageIds.length==0) {
		this.getAction("系统消息").systemMessageId = "";
	}
	else {
		if(this.getAction("系统消息").selected) { //对话框已经是打开的
			this.closeWebimDialog(false);
		}
		this.getAction("系统消息").systemMessageId = this.systemMessageIds[0];
		this.getAction("系统消息").feedback = false;
		//检查是否有窗口已经打开
		var i = this.actions.length-1;
		for(; i>=0 && !this.actions[i].selected; i--);
		if(i>=0) { //有窗口已经打开
			return;
		}
		this.openWebimDialog(this.getAction("系统消息"), true); //打开系统消息窗口
	}
};

//系统消息反馈
Webim.prototype.feedbackSystemMessage = function() {
	if(!this.getAction("系统消息").feedback) {
		this.getAction("系统消息").feedback = true;
		this.openAjaxRequest("ajax/feedbackSystemMessage.shtml?systemMessageId=" + this.systemMessageIds[0], true); //发送反馈到服务器
	}
}

//获取下一个系统消息
Webim.prototype.retrieveNextSystemMessage = function() {
	//从系统消息ID列表中删除
	this.systemMessageIds.splice(0, 1);
	//读取下一个系统消息
	this.retrieveSystemMessage();
};

//播放声音
Webim.prototype.playSound = function() {
	try {
		var soundFile = "../sound/msg.wav";
		var sound = document.getElementById("sound");
		if(document.all) { //IE
			if(!sound) {
				sound = document.createElement("bgsound");
				sound.style.display = "none";
				sound.id = "sound";
				document.body.appendChild(sound);
			}
			sound.src = soundFile;
		}
		else { //firefox
			if(!sound) {
				sound = document.createElement("embed");
				sound.id = "sound";
				sound.src = soundFile;
				sound.type = "audio/wav";
				sound.autostart = "false";
				sound.hidden = "";
				sound.loop = "false";
				sound.width = "0";
				sound.height = "0";
				document.body.appendChild(sound);
			}
			if(sound.Stop){
				sound.Stop();
			}
			if(sound.Play){
				sound.Play();
			}
		}
	}
	catch(e) {
		
	}
};

//获取通知的属性
Webim.prototype.getMessageProperty = function(xmlMessage, propertyName) {
	try {
		return Ajax.getText(xmlMessage.getElementsByTagName(propertyName)[0]);
	}
	catch(e) {
		return "";
	}
};

//通知宿主页面显示一个对话框,url不含主机和contextPath,left仅相对于Webim主窗口
Webim.prototype.openWebimDialog = function(webimAction, closeOtherDialogs) {
	if(closeOtherDialogs) { //关闭其他对话框
		//检查当前对话框时候已经打开
		var selected = false;
		for(var i=this.actions.length-1; i>=0; i--) {
			var action = this.actions[i];
			if(action==webimAction) {
				selected = action.selected;
				break;
			}
		}
		if(!selected) { //当前按钮未选中
			this.closeWebimDialog(true); //最小化其它对话框
		}
	}
	webimAction.selected = true; //设置为选中状态
	if(webimAction.selectedStyle!="" && webimAction.selectedStyle!="null") {
		webimAction.element.style.cssText = webimAction.selectedStyle;
	}
	//打开对话框
	var dialogWidth = webimAction.dialogWidth;
	if(dialogWidth<webimAction.element.offsetWidth) {
		dialogWidth = webimAction.element.offsetWidth + 1;
	}
	var left = 0;
	for(var element = webimAction.element; !element.parentNode.id || element.parentNode.id!="divWebim"; element=element.parentNode) {
		left += element.offsetLeft;
	}
	if(webimAction.dialogAlign=="right") {
		left = left + webimAction.element.offsetWidth - dialogWidth + 1;
	}
	var borderWidth = webimAction.element.style.borderTopWidth;
	var top = (borderWidth ? borderWidth.replace("px", "") : 1);
	this.sendNotify("openWebimDialog$$" + webimAction.getDialogUrl() + "$$" + left + "$$" + top + "$$" + dialogWidth + "$$" + webimAction.dialogHeight + "$$" + webimAction.dialogAlign);
};

//关闭/最小化对话框
Webim.prototype.closeWebimDialog = function(minimize) {
	for(var i=this.actions.length-1; i>=0; i--) {
		var webimAction = this.actions[i];
		if(!webimAction.selected) {
			continue;
		}
		this.sendNotify("closeWebimDialog$$" + webimAction.getDialogUrl() + "$$" + ("个人设置"==webimAction.actionName || (minimize && "对话"==webimAction.actionName)));
		if("对话"==webimAction.actionName && this.isCustomerService) { //客服对话
			this.closeCustomerServiceChat();
			if(webimAction.talked) { //有发言过
				window.webim.getAction("服务评价").dialogUrl = "/im/cs/evaluation.shtml?specialistId=" + webimAction.chatPersonIds;
				var evaluate = function() {
					window.webim.openWebimDialog(window.webim.getAction("服务评价"), false);
				}
				window.setTimeout(evaluate, 1);
			}
		}
		webimAction.selected = false;
		webimAction.element.style.cssText = webimAction.normalStyle;
		if(!minimize && "对话"==webimAction.actionName) { //关闭对话窗口
			this.destoryChatAction(i); //销毁对话按钮
		}
		if("系统消息"==webimAction.actionName && webimAction.systemMessageId!="") {
			this.feedbackSystemMessage(); //发送反馈到服务器
			//等待600ms后显示下一个系统消息
			var webim = this;
			var retrieveNextSystemMessage = function() {
				webim.retrieveNextSystemMessage();
			};
			window.setTimeout(retrieveNextSystemMessage, 600);
		}
	}
};

//放大对话框
Webim.prototype.zoomOutWebimDialog = function(scale) {
	for(var i=this.actions.length-1; i>=0; i--) {
		var webimAction = this.actions[i];
		if(!webimAction.selected) {
			continue;
		}
		webimAction.dialogWidth = Math.round(webimAction.dialogWidth * (1 + scale));
		webimAction.dialogHeight = Math.round(webimAction.dialogHeight * (1 + scale));
		this.openWebimDialog(webimAction, false);
	}
};

//销毁一个对话按钮
Webim.prototype.destoryChatAction = function(actionIndex) {
	var chatAction = this.actions[actionIndex];
	//检查是否最后一个对话
	var count = 0;
	for(var j=this.actions.length-1; j>=0; j--) {
		if("对话"==this.actions[j].actionName) {
			count++;
		}
	}
	chatAction.chatId = "";
	chatAction.talked = false;
	chatAction.chatPersonIds = "";
	chatAction.dialogWidth = chatAction.defaultDialogWidth;
	chatAction.dialogHeight = chatAction.defaultDialogHeight;
	if(count==1) { //最后一个对话
		chatAction.element.style.display = "none"; //隐藏对话按钮
	}
	else {
		chatAction.element.parentNode.removeChild(chatAction.element); //删除对话按钮
		this.actions.splice(actionIndex, 1); //从按钮列表中删除
	}
	this.show(); //调整主窗口尺寸
};

//按用户ID获取对应的对话按钮
Webim.prototype.getChatActionByPersonId = function(personId) {
	for(var i=0; i<this.actions.length; i++) {
		var webimAction = this.actions[i];
		if(webimAction.chatPersonIds==personId) { //对话已经存在
			return webimAction;
		}
	}
	return null;
};

//按对话ID获取对应的对话按钮
Webim.prototype.getChatActionByChatId = function(chatId) {
	for(var i=0; i<this.actions.length; i++) {
		var webimAction = this.actions[i];
		if(webimAction.chatId==chatId) { //对话已经存在
			return webimAction;
		}
	}
	return null;
};

//创建一个对话
Webim.prototype.createChat = function(personId) {
	if(this.userId==personId) { //不允许自己和自己对话
		return;
	}
	//检查对话是否已经存在
	var chatAction = this.getChatActionByPersonId(personId);
	if(chatAction==null) { //对话不存在
		this.openAjaxRequest("ajax/createChat.shtml?chatPersonId=" + personId, false); //请求创建新的对话
		chatAction = window.webim.getChatActionByPersonId(personId);
	}
	window.webim.openWebimDialog(chatAction, true);
	if(chatAction.getRecordCount()!=0) {
		window.webim.retrieveTalks(chatAction); //请求获取发言记录
	}
};

//创建一个客服对话
Webim.prototype.createCustomerServiceChat = function(specialistId, siteId) {
	//检查对话是否已经存在
	if(this.getActiveAction()!=null) {
		return;
	}
	//请求创建新的对话
	this.openAjaxRequest("ajax/createCustomerServiceChat.shtml?specialistId=" + specialistId + "&siteId=" + siteId, false);
	var chatAction = this.getAction("对话");
	if(chatAction!=null) {
		window.webim.openWebimDialog(chatAction, true);
		window.webim.retrieveTalks(chatAction); //请求获取发言记录
		this.retrieveMessage(); //启动消息接收
	}
};

//留言
Webim.prototype.leaveMessage = function() {
	this.openWebimDialog(this.getAction("留言"), true); //打开留言窗口
}

//加载一个对话
Webim.prototype.loadChat = function(chatId) {
	if(this.isCustomerService && this.getAction('对话')==null) { //对话已经关闭
		return false;
	}
	this.openAjaxRequest("ajax/loadChat.shtml?chatId=" + chatId, false);
	return true;
};

//请求获取对话记录
Webim.prototype.retrieveTalks = function(chatAction) {
	if(chatAction.chatId=="") {
		return;
	}
	var url = "ajax/retrieveTalks.shtml?chatId=" + chatAction.chatId + "&beginTime=" + chatAction.lastReadTime;
	this.openAjaxRequest(url, true);
};

//打开对话窗口
Webim.prototype.openChatDialog = function(chatId, force) {
	//检查是否有窗口已经打开
	for(var i=window.webim.actions.length-1; !force && i>=0; i--) {
		if(window.webim.actions[i].selected) { //已经有窗口被选项
			return false;
		}
	}
	var chatAction = window.webim.getChatActionByChatId(chatId); //获取对话按钮
	if(chatAction!=null) { //对话按钮已创建
		window.webim.openWebimDialog(chatAction, true); //打开对话
		return true;
	}
	return false;
};

//关闭一个对话
Webim.prototype.closeChat = function(src) {
	//获取对应的对话按钮
	var parentElement = src.parentNode;
	for(; parentElement.tagName.toLowerCase()!="body" && (!parentElement.id || parentElement.id.indexOf("webimAction")!=0); parentElement = parentElement.parentNode);
	var selectedAction = null;
	for(var i=this.actions.length-1; i>=0; i--) {
		var webimAction = this.actions[i];
		if(webimAction.element!=parentElement) {
			if(webimAction.selected) {
				selectedAction = webimAction;
			}
		}
		else if(webimAction.selected) { //当前是选中状态
			this.closeWebimDialog(false); //直接关闭对话框
		}
		else {
			this.destoryChatAction(i); //销毁对话按钮
		}
	}
	if(selectedAction!=null) {
		this.openWebimDialog(selectedAction, false); //重新显示对话框，用来调整显示位置
	}
};

//发送发言
Webim.prototype.submitTalk = function(chatId, talkContent) {
	if(talkContent=="") {
		return false;
	}
	talkContent = '<font style="font-family:' + this.webimSetting.fontName + ';' +
				  ' font-size:' + this.webimSetting.getFontSize() + 'px;' +
				  ' color:' + this.webimSetting.fontColor + ';' +
				  ' line-height:' + Math.ceil(this.webimSetting.getFontSize() * 1.2) + 'px;' +
				  '">' + talkContent + '</font>';
	if(talkContent.length>2000) {
		alert('您输入的内容过长，不允许提交。');
		return false;
	}
	var url = "ajax/chatTalk.shtml?chatId=" + chatId;
	this.openAjaxRequest(url, false, talkContent);
	this.getChatActionByChatId(chatId).talked = true;
	return true;
};

//发送通知给主窗口
Webim.prototype.sendNotify = function(notifyContent) {
	if(!parent.frames["frameNotify"]) { //没有消息通知帧
		parent.processWebimNotify(notifyContent); //直接调用父窗口函数
		return;
	}
	this.notifies[this.notifies.length] = notifyContent; //加入队列
	if(this.notifies.length==1) { //只有一个通知,立即发送,否则由定时器决定发送时间
		this.doSendNotify(); //启动发送
	}
};

//执行发送通知给主窗口
Webim.prototype.doSendNotify = function() {
	var webim = this;
	var waitForSent = function() { //等待发送完成
		if(webim.notifies.length==0) {
			return;
		}
		try {
			if(parent.frames["frameNotify"].document.location.href.substring(0, webim.notifyURL.length)!=webim.notifyURL) {
				webim.notifies.splice(0, 1);
				if(webim.notifies.length>0) { //有需要发送的通知
					webim.doSendNotify();
				}
				return;
			}
		}
		catch(e) {
		
		}
		setTimeout(waitForSent, 10);
	};
	parent.frames["frameNotify"].onunload = function() {
		window.setTimeout(waitForSent, 10);
	};
	parent.frames["frameNotify"].location.replace(this.notifyURL + "#" + this.notifies[0]); //用修改帧URL的方式给宿主页面发送通知
	window.setTimeout(waitForSent, 200);
};

//创建讨论组
Webim.prototype.createGroupChat = function(fromChatId, personIds) {
	if(personIds!="") { //有选定用户,请求创建讨论组
		this.openAjaxRequest("ajax/createGroupChat.shtml?fromChatId=" + fromChatId + "&chatPersonIds=" + personIds, false);
	}
};

//类:WEBIM按钮
WebimAction = function(webim, elementId, actionName, dialogUrl, dialogWidth, dialogHeight, dialogAlign, selectedStyle, mouseoverStyle) {
	this.webim = webim;
	this.actionName = actionName;
	this.element = document.getElementById(elementId);
	this.recordCountElement = null;
	this.dialogUrl = dialogUrl;
	var links = this.element.getElementsByTagName("a");
	for(var i=0; i<links.length; i++) {
		if(links[i].getAttribute("name")=="recordCount") {
			this.recordCountElement = links[i];
			break;
		}
	}
	if(this.recordCountElement && this.recordCountElement.innerHTML!="0") {
		this.recordCountElement.style.display = "";
	}
	if("对话"==actionName) {
		this.element.style.display = "none";
	}
	this.defaultDialogWidth = this.dialogWidth = Number(dialogWidth);
	this.defaultDialogHeight = this.dialogHeight = Number(dialogHeight);
	this.dialogAlign = dialogAlign;
	this.normalStyle = this.element.style.cssText.replace(/display: none/gi, '');
	this.selectedStyle = selectedStyle;
	this.mouseoverStyle = mouseoverStyle;
	this.selected = false;
	//对话相关属性
	this.chatId = ""; //聊天ID
	this.isCustomerServiceChat = false; //是否客服对话
	this.talked = false;
	this.chatPersonIds = ""; //聊天用户ID列表
	this.lastReadTime = 0; //最后接收的发言的时间
	this.systemMessageId = ""; //系统消息ID
	this.feedback = false;
	//事件绑定
	var webimAction = this;
	this.element.onmouseover = function() {
		if("对话"==actionName && webimAction.chatId=="") { //对话已经被销毁
			return;
		}
		if(!webimAction.selected && webimAction.mouseoverStyle!="" && webimAction.mouseoverStyle!="null") {
			webimAction.element.style.cssText = webimAction.mouseoverStyle;
		}
	};
	this.element.onmouseout = function() {
		if("对话"==actionName && webimAction.chatId=="") { //对话已经被销毁
			return;
		}
		if(!webimAction.selected) {
			webimAction.element.style.cssText = webimAction.normalStyle;
		}
	};
	this.element.onclick = function() {
		if("对话"==actionName && webimAction.chatId=="") { //对话已经被销毁
			return;
		}
		if(webimAction.selected) { //选中
			webimAction.webim.closeWebimDialog(true); //关闭/最小化对话框
		}
		else { //未选中
			webimAction.webim.openWebimDialog(webimAction, true); //打开对话框
			if("对话"==actionName && webimAction.getRecordCount()!=0) { //请求获取发言记录
				webimAction.webim.retrieveTalks(webimAction);
			}
		}
	};
};

//获取对话框URL
WebimAction.prototype.getDialogUrl = function() {
	var url = this.dialogUrl;
	if(this.chatId!="") { //对话
		url += "?chatId=" + this.chatId; //对话ID
		if(this.isCustomerServiceChat) { //客服对话
			url += "&customerService=true";
			if(this.webim.isCustomerService) { //客户的页面
				url += "&customer=true";
			}
		}
	}
	else if(this.systemMessageId!="") { //系统消息
		url += "?systemMessageId=" + this.systemMessageId;
	}
	var href = location.href;
	var siteId = StringUtils.getPropertyValue(href, "siteId", "0");
	if(siteId!="0") {
		url += (url.lastIndexOf('?')==-1 ? "?" : "&") + "siteId=" + siteId;
	}
	return url;
};

//获取未读发言数量
WebimAction.prototype.getRecordCount = function() {
	return this.recordCountElement ? Number(this.recordCountElement.innerHTML) : -1;
};

//修改未读发言数量
WebimAction.prototype.setRecordCount = function(recordCount) {
	if(this.recordCountElement==null) {
		return;
	}
	this.recordCountElement.innerHTML = (recordCount<0 ? 0 : recordCount);
	if(recordCount==0 || this.actionName=="在线用户") { //没有未读记录
		this.recordCountElement.style.display = (recordCount==0 ? "none" : "");
		this.webim.show(); //调整主窗口尺寸
	}
	else if(!this.selected || this.actionName!="对话") { //未选中或者不是对话窗口
		var webimAction = this;
		var showRecordCount = function() {
			if(webimAction.getRecordCount()>0) {
				webimAction.recordCountElement.style.display = "";
				webimAction.webim.show(); //调整主窗口尺寸
			}
		}
		window.setTimeout(showRecordCount, 1000);
	}
};

//类:个人设置
WebimSetting = function(fontName, fontSize, fontColor, loginStatus, ctrlSend, playSoundOnReceived, setFocusOnReceived, openChatDialogOnReceived) {
	this.fontName = fontName;
	this.fontSize = fontSize;
	this.fontColor = fontColor;
	this.loginStatus = loginStatus;
	this.ctrlSend = ctrlSend;
	this.playSoundOnReceived = playSoundOnReceived;
	this.setFocusOnReceived = setFocusOnReceived;
	this.openChatDialogOnReceived = openChatDialogOnReceived;
	this.timer = null;
};

//设置属性值
WebimSetting.prototype.setPropertyValue = function(propertyName, propertyValue) {
	eval("this." + propertyName + "=propertyValue");
	this.saveSetting(3000);
};

//获取字体大小
WebimSetting.prototype.getFontSize = function() {
	var value = Number(this.fontSize);
	return isNaN(value) ? 9 : value;
};

//更新个人设置
WebimSetting.prototype.saveSetting = function(delay) {
	if(window.webim.isCustomerService) { //客服页面
		return;
	}
	if(this.timer!=null) {
		window.clearTimeout(this.timer);
	}
	var setting = this;
	var savePersonalSetting = function() {
		var url = "ajax/savePersonalSetting.shtml" +
				  "?fontName=" + StringUtils.utf8Encode(setting.fontName) +
				  "&fontColor=" + StringUtils.utf8Encode(setting.fontColor) +
				  "&fontSize=" + StringUtils.utf8Encode(setting.getFontSize()) +
				  "&loginStatus=" + StringUtils.utf8Encode(setting.loginStatus) +
				  "&ctrlSend=" + StringUtils.utf8Encode(setting.ctrlSend) +
				  "&playSoundOnReceived=" + StringUtils.utf8Encode(setting.playSoundOnReceived) +
				  "&setFocusOnReceived=" + StringUtils.utf8Encode(setting.setFocusOnReceived) +
				  "&openChatDialogOnReceived=" + StringUtils.utf8Encode(setting.openChatDialogOnReceived);
		window.webim.openAjaxRequest(url, true, ""); //异步请求
		setting.timer = null;
	};
	this.timer = window.setTimeout(savePersonalSetting, delay); //延迟保存,避免频繁请求
};

//WEBIM加载器
WebimLoader = function() {
	
};

//登录
WebimLoader.prototype.login = function(delay) {
	var loginUrl = 'webimLogin.shtml?seq=' + new Date().getTime();
	//创建登录IFRAME,加载Webim登录页面
	var frameLogin = document.createElement('iframe');
	frameLogin.id = frameLogin.name = "frameLogin";
	frameLogin.style.display = "none";
	frameLogin.src = loginUrl;
	document.body.appendChild(frameLogin);
	var checkLogin = function() {
		if(frames['frameLogin'].location.href.indexOf(loginUrl)==-1) {
			alert("登录失败,WEBIM加载不成功。");
		}
	};
	window.setTimeout(checkLogin, 8000);
};