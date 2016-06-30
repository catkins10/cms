var mapFunction;
var topWindow = (window.opener ? window.opener.top : window.top);
function displayEvent(eventId, eventX, eventY, displayLevel) { //显示一个事件的位置,displayLevel=2
	mapFunction = function() {
		doClear();
		topWindow.mapWindow.ShowCase(eventId, eventX, eventY, displayLevel);
	};
	loadMap();
}
function clearEvent() { //清除显示的事件
	mapFunction = function() {
		doClear()
	};
	loadMap();
}
function EventUtils.addEvent(addEventCallback) { //添加一个事件,addEventCallback:回调函数
	mapFunction = function() {
		topWindow.mapWindow.AddCaseBack = function(x, y) { //添加案件时,返回值所要调用的函数
		    window.focus();
		    addEventCallback(x, y);
		};
		doClear();
		topWindow.mapWindow.AddCase();
	};
	loadMap();
}
function locatePdaUser(userCode) { //在地图上定位PDA用户的位置
	mapFunction = function() {
		doClear();
		var userCodes = new Array();
		userCodes[0] = userCode;
		topWindow.mapWindow.GotoPDAUserPosition(userCodes);
	};
	loadMap();
}
function trackPdaUser(userCode) { //跟踪PDA用户
	mapFunction = function() {
		doClear();
		var userCodes = new Array();
		userCodes[0] = userCode;
		topWindow.mapWindow.TrackPDAUser(userCodes);
	};
	loadMap();
}
function showPdaUserHistory(userCode) { //显示用户的历史轨迹
	mapFunction = function() {
		doClear();
		var userCodes = new Array();
		userCodes[0] = userCode;
		topWindow.mapWindow.ShowPDAUserHistory(userCodes);
	};
	loadMap();
}
function doClear() { //清除显示的事件
	topWindow.mapWindow.ClearCaseList(); //清除案件列表的图标
	topWindow.mapWindow.ClearCaseOne(); //清除定位查询的案件图标
	topWindow.mapWindow.ClearPDAUserPosition(); //清除地图上的已定位的PDA用户位置
	topWindow.mapWindow.StopTrackPDAUser(); //停止跟踪PDA用户
}
function loadMap() { //加载地图
	try {
		topWindow.mapWindow.focus();
	}
	catch(e) {
		var href = window.location.href;
		href = href.substring(0, href.indexOf(":8080"));
		topWindow.mapWindow = topWindow.open(href + '/MapPath/default.aspx', 'map', 'left=0,top=0,width=' + (screen.availWidth-8) + ',height=' + (screen.availHeight-28) + ',scrollbars=no,status=no,resizable=yes,toolbar=no,menubar=no,location=no');
		topWindow.mapWindow.focus();
	}
	var executeMapFunction = function() {
		try {
			mapFunction();
		}
		catch(e) {
			window.setTimeout(executeMapFunction, 1000);
		}
	};
	window.setTimeout(executeMapFunction, 10);
}