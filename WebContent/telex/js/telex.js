function selectUnit(width, height, param, scriptEndSelect, separator) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/telex/selectUnit.shtml";
   url += "?param=" + escape(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + escape(scriptEndSelect) : "");
   url += ("&separator=" + (separator && separator!="" ?  escape(separator) : ","));
   DialogUtils.openDialog(url, width, height);
}

function computeTotalPages() {
	var pages = Number(document.getElementsByName("pages")[0].value);
	var telegramNumber = document.getElementsByName("toUnitNames")[0].value.split(",").length;
	document.getElementsByName("telegramNumber")[0].value = telegramNumber;
	if(pages+""!="NaN" && pages>0) {
		document.getElementsByName("totalPages")[0].value = pages * telegramNumber;
	}
}

function selectFromUnits(src) { //选择发电单位
	PopupMenu.popupMenu("单位\0个人", function(menuItemId, menuItemTitle) {
		switch(menuItemId) {
		case "单位":
			selectUnit(document.body.clientWidth * 0.8, document.body.clientHeight * 0.8, 'fromUnitIds{id},fromUnitNames{name}', '', ',');
			break;
		
		case "个人":
			DialogUtils.selectPersonByRole(640, 400, true, 'fromUnitIds{id},fromUnitNames{name|发电人|100%}');
			break;
		}
	}, src, 120, "right");
}
function selectToUnits(src) { //选择收电单位
	PopupMenu.popupMenu("单位\0个人", function(menuItemId, menuItemTitle) {
		switch(menuItemId) {
		case "单位":
			selectUnit(document.body.clientWidth * 0.8, document.body.clientHeight * 0.8, 'toUnitIds{id},toUnitNames{name}', 'computeTotalPages()', ',');
			break;
		
		case "个人":
			DialogUtils.selectPersonByRole(640, 400, true, 'toUnitIds{id},toUnitNames{name|收电人|100%}', 'computeTotalPages()');
			break;
		}
	}, src, 120, "right");
}
//打印办理单
function printTransactionSheet(isReceive, isCryptic) {
	//选择办理单
	var telegramType = (isCryptic ? "密文" : "明文") + (isReceive ? "收报" : "发报");
	DialogUtils.openSelectDialog("telex", "selectTransactionSheet", 550, 330, false, "", "window.setTimeout('doPrintTransactionSheet(\"{id}\")', 100)", "", telegramType);
}
function doPrintTransactionSheet(sheetId) {
	window.open('printTransactionSheet.shtml?sheetId=' + sheetId + '&telegramId=' + document.getElementsByName("id")[0].value);
}

function appendUnitSigns() { //添加单位
	selectUnit(document.body.clientWidth * 0.8, document.body.clientHeight * 0.8, "signReceiverIds{id},signReceiverNames{name}", "doAppendSigns()", ",");
}
function appendPersonSigns() { //添加个人
	DialogUtils.selectPersonByRole(640, 400, true, "signReceiverIds{id},signReceiverNames{name|签收人|100%}", "doAppendSigns()");
}
function doAppendSigns() {
	if(document.getElementsByName('signReceiverIds')[0].value!='') {
		//同步更新toUnitIds、toUnitNames
		var toUnitIds = document.getElementsByName("toUnitIds")[0];
		var toUnitNames = document.getElementsByName("toUnitNames")[0];
		if(toUnitIds.value=="") {
			toUnitIds.value = document.getElementsByName("signReceiverIds")[0].value;
			toUnitNames.value = document.getElementsByName("signReceiverNames")[0].value;
		}
		else {
			var newReceiverIds = document.getElementsByName("signReceiverIds")[0].value.split(",");
			var newReceiverNames = document.getElementsByName("signReceiverNames")[0].value.split(",");
			for(var i=0; i<newReceiverIds.length; i++) {
				if(("," + toUnitIds.value + ",").indexOf("," + newReceiverIds[i] + ",")==-1) {
					toUnitIds.value += "," + newReceiverIds[i];
					toUnitNames.value += "," + newReceiverNames[i];
				}
			}
		}
		FormUtils.doAction('appendSigns');
	}
}
function deleteSigns() { //删除签收人
	var selectSigns = document.getElementsByName("selectSign");
	var ids = "";
	var unselectedIds = ",";
	for(var i=0; i<selectSigns.length; i++) {
		if(selectSigns[i].checked) {
			ids = (ids=="" ? "" : ids + ",") + selectSigns[i].value;
		}
		else {
			unselectedIds += selectSigns[i].nextSibling.innerHTML + ",";
		}
	}
	//同步更新toUnitIds、toUnitNames
	var toUnitIds = document.getElementsByName("toUnitIds")[0].value.split(",");
	var toUnitNames = document.getElementsByName("toUnitNames")[0].value.split(",");
	var newUnitIds = "";
	var newUnitNames = "";
	for(var j=0; j<toUnitIds.length; j++) {
		if(unselectedIds.indexOf("," + toUnitIds[j] + ",")!=-1) {
			newUnitIds = (newUnitIds=="" ? "" : newUnitIds + ",") + toUnitIds[j];
			newUnitNames = (newUnitNames=="" ? "" : newUnitNames + ",") + toUnitNames[j];
		}
	}
	document.getElementsByName("toUnitIds")[0].value = newUnitIds;
	document.getElementsByName("toUnitNames")[0].value = newUnitNames;
	if(ids!="") {
		document.getElementsByName('signReceiverIds')[0].value = ids;
		FormUtils.doAction("deleteSigns");
	}
}