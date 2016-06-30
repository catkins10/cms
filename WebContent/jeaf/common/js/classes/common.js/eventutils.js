EventUtils = function() {

};
EventUtils.clickElement = function(element) { //模拟单击
	if(document.all) { 
		element.click();
		return;
	}
	if(element.onclick) {
		element.onclick();
		return;
	}
	var win = (element.ownerDocument.parentWindow ? element.ownerDocument.parentWindow : element.ownerDocument.defaultView);
	var evt = win.document.createEvent("MouseEvents");
	evt.initMouseEvent('click', true, true, win, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	element.dispatchEvent(evt);
};
EventUtils.createTouchEevent = function(element, eventType, pageX, pageY, screenX, screenY, clientX, clientY) { //模拟触摸事件,touchstart,touchmove,touchend
	var event = element.ownerDocument.createEvent('TouchEvent'); 
	//createTouch(view, target, identifier, pageX, pageY, screenX, screenY, clientX, clientY, radiusX, radiusY, rotationAngle, force);
	var touch = element.ownerDocument.createTouch(window, element, 1, pageX, pageY, screenX, screenY, clientX, clientY, 0, 0, 0, true);
	var touches = element.ownerDocument.createTouchList(touch);
	var targetTouches = element.ownerDocument.createTouchList(touch);
	var changedTouches = element.ownerDocument.createTouchList(touch);
	//initTouchEvent(type, canBubble, cancelable, view, detail, screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey, touches, targetTouches, changedTouches, scale, rotation);
	event.initTouchEvent(eventType, true, true, window, null, screenX, screenY, clientX, clientY, false, false, false, false, touches, targetTouches, changedTouches, 1, 0);
	element.dispatchEvent(event);
};
EventUtils.addEvent = function(element, eventName, func) { //绑定事件处理函数
	if(element.attachEvent) { //IE
		try {
			element.attachEvent(eventName, func);
		}
		catch(e) {
			
		}
		try {
			element.attachEvent("on" + eventName, func);
		}
		catch(e) {
			
		}
	}
	else if(element.addEventListener) { //Gecko/W3C
		element.addEventListener(eventName, func, true);
	}
	else { // Opera (or old browsers)
		element["on" + eventName] = func;
	}
};
EventUtils.removeEvent = function(element, eventName, func) { //绑定事件处理函数
	if(element.detachEvent) { //IE
		element.detachEvent("on" + eventName, func);
	}
	else if(element.removeEventListener) { //Gecko / W3C
		element.removeEventListener(eventName, func, true);
	}
	else { // Opera (or old browsers)
		//element["on" + eventName] = func;
	}
};
EventUtils.stopPropagation = function(event) { //阻止事件向上传递
	if(event.stopPropagation) {
		event.stopPropagation();
	}
	else {
		event.cancelBubble = true;
	}
};