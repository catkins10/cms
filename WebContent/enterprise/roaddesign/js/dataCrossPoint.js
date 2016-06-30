window.onload = function() {
	var recordElement = document.getElementById('record');
	recordElement.setAttribute("onclick", "openCrossPoint(this)");
	var sql = "select * from road_cross_point" +
			  " where sectionId=" + window.top.sectionId +
			  " order by sn, name";
	window.top.database.databaseQuery(sql, 0, 20, recordElement, true);
};
function newCrossPoint() {
	if(!window.top.sectionId || window.top.sectionId==0) {
		alert("未选定项目和路段，不允许创建交点。");
		return;
	}
	var inputs = [{name:"编号", value:"", dataType:"string", maxLength:10},
				  {name:"N", value:"", dataType:"number", maxLength:12},
				  {name:"E", value:"", dataType:"number", maxLength:12}];
	var dialog = window.top.client.openInputDialog("添加交点", inputs, false, false, "确定", "取消");
	dialog.onOK = function(clientDialogBody) {
		var name = clientDialogBody.getValue("编号");
		if(name!="") {
			var jx = Number(clientDialogBody.getValue("N"));
			var jy = Number(clientDialogBody.getValue("E"));
			
			//保存记录
			var crossPoint = {projectId:window.top.projectId, sectionId:window.top.sectionId, sn: generateSN(name), name: name, jx:(isNaN(jx) ? 0 : jx), jy:(isNaN(jy) ? 0 : jy)};
			window.top.database.saveRecord("road_cross_point", crossPoint);
			recomputeCrossPoints(); //更新交点的偏角和方位角
			//刷新页面
			window.location.reload();
		}
		return true;
	};
}
function openCrossPoint(crossPointElement) {
	var inputs = [{name:"编号", value:DomUtils.getElement(crossPointElement, "span", "name").innerHTML, dataType:"string", maxLength:10},
				  {name:"N", value:DomUtils.getElement(crossPointElement, "span", "jx").innerHTML, dataType:"number", maxLength:12},
				  {name:"E", value:DomUtils.getElement(crossPointElement, "span", "jy").innerHTML, dataType:"number", maxLength:12}];
	var dialog = window.top.client.openInputDialog("交点", inputs, false, false, "保存", "取消", "删除");
	dialog.onOK = function(clientDialogBody) {
		var name = clientDialogBody.getValue("编号");
		if(name!="") {
			var jx = Number(clientDialogBody.getValue("N"));
			var jy = Number(clientDialogBody.getValue("E"));
			//更新记录
			var crossPoint = {id: crossPointElement.id, sn: generateSN(name), name: name, jx:(isNaN(jx) ? 0 : jx), jy:(isNaN(jy) ? 0 : jy)};
			window.top.database.updateRecord("road_cross_point", crossPoint);
			recomputeCrossPoints(); //更新交点的偏角和方位角
			//更新页面
			window.top.database.updateRecordProperty(document.getElementById('record'), crossPoint.id, "name", name);
			window.top.database.updateRecordProperty(document.getElementById('record'), crossPoint.id, "jx", crossPoint.jx);
			window.top.database.updateRecordProperty(document.getElementById('record'), crossPoint.id, "jy", crossPoint.jy);
		}
		return true;
	};
	dialog.onOther = function(clientDialogBody) { //删除
		var confirm = window.top.client.openConfirmDialog("删除", "删除后不可恢复，是否确定删除？"); //打开确认对话框
		confirm.onOK = function(clientDialogBody) {
			//删除记录
			window.top.database.deleteRecord("road_cross_point", crossPointElement.id);
			recomputeCrossPoints(); //更新交点的偏角和方位角
			//更新页面
			window.top.database.removeRecordElement(document.getElementById('record'), crossPointElement.id);
			//关闭对话框
			dialog.close();
			return true;
		};
		return false;
	};
}
function generateSN(name) {
	if(name == "BP") {
	    return -10000;
	}
	else if(name == "EP") {
	    return 10000;
	}
	else {
	    return toDouble(name.replace(/[AB]/gi, "").replace(/[-_]/gi, "."));
	}
}
function importCrossPoint() {
	window.top.fileSystem.onSelectFile = function(filePath) {
		doImportCrossPoint(filePath);
	};
	window.top.fileSystem.selectFile("", "*.jd", false, false); //坐标文件(*.jd)|*.jd|所有文件(*.*)|*.*|
}
function doImportCrossPoint(filePath) {
	//清除原有数据
	window.top.database.executeSQL("delete from road_cross_point where sectionId=" + window.top.sectionId);
	
	//读取数据
	var firstLine = true;
	var crossPoint;
	window.top.fileSystem.onReadLineFromFile = function(line) { //返回true继续读取,返回false放弃,line==null表示文件读取完成
		if(line==null) { //读取完成
			if(!crossPoint) {
				return true;
			}
			crossPoint.name = "EP";
			crossPoint.sn = generateSN("EP");
			window.top.database.updateRecord("road_cross_point", crossPoint);
			recomputeCrossPoints(); //更新交点的偏角和方位角
			//刷新页面
			window.location.reload();
			return true;
		}
		if(line=="") {
			return true;
		}
        var PJ = 0;
        var Jx = 0;
        var Jy = 0;
        var R = 0;
        var LSa = 0;
        var LSb = 0;
        var LX = 0;
        var values = parseLine(line, line.indexOf(",")==-1 ? " " : ",");
        var name = values[0];
        if(firstLine) {
            firstLine = false;
            name = "BP";
        }
        else {
            name = name.replace(/JD/gi, "");
        }
        if(values.length > 1) {
        	Jx = toDouble(values[1]);
        }
        if(values.length > 2) {
        	Jy = toDouble(values[2]);
        }
        if(values.length > 3) {
        	R = toDouble(values[3]);
        }
        if(values.length > 4) {
        	LSa = toDouble(values[4]);
        }
        if(values.length > 5) {
        	LSb = toDouble(values[5]);
        }
        if(values.length > 6) {
        	LX = toDouble(values[6]);
        }
        //保存记录
        crossPoint = {projectId: window.top.projectId,
        			  sectionId: window.top.sectionId,
        			  sn: generateSN(name),
        			  name: name,
					  jx: Jx,
        			  jy: Jy,
			          pj: 0,
			          eta: 0,
		        	  r: R,
			          lsa: LSa,
        			  lsb: LSb,
        			  ly: 0,
			          ta: 0,
        			  d: 0,
			          dp: 0,
        			  sl: 0,
			          lx: LX,
			          jd: 0,
        			  jddl: 0,
				      tb: 0,
			          qfw: 0,
				      hfw: 0};
        window.top.database.saveRecord("road_cross_point", crossPoint);
        return true;
	};
	window.top.fileSystem.readTextFromFile(filePath);
}