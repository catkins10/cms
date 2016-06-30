window.onload = function() {
	document.getElementsByName("name")[0].onchange = function() {
		loadCurve();
	};
	//获取交点列表
	window.top.database.onAfterQuery = function(records) {
		if(!records || records.length<2) {
			return;
		}
		//设置选项列表
		var listItems = "";
		for(var i=1; i<records.length; i++) {
			listItems += (listItems=='' ? '' : '\0') + records[i].name;
		}
		DropdownField.setListValues("name", listItems);
		//用第一个交点记录填充表单
		document.getElementsByName("name")[0].value = records[1].name;
		document.getElementsByName("name")[0].onchange();
	};
    var sql = "select name from road_cross_point" +
    		  " where road_cross_point.sectionId=" + window.top.sectionId +
    		  " order by sn, name";
	window.top.database.databaseQuery(sql, 0, 0, null, false);
};
function loadCurve(force) {
	var name = document.getElementsByName("name")[0].value;
	if(!force && window.crossPointName==name) {
		return;
	}
	window.crossPointName = name;
	//获取交点记录
	window.top.database.onAfterQuery = function(records) {
		window.crossPointId = records[1].id;
		document.getElementById("detailData").style.visibility = records[1].name=="BP" || records[1].name=="EP" ? "hidden" : "visible";
		//输出字段
		var fieldNames = getFieldNames();
		for(var i=0; i<fieldNames.length; i++) {
			var value;
			if(fieldNames[i]=="lx") {
				value = getCurveName(records[1].lx) + "(" + records[1].lx + ")";
			}
			else {
				value = eval("records[1]." + (fieldNames[i]=="pile" ? "jd" : fieldNames[i]));
			}
			document.getElementsByName(fieldNames[i])[0].value = value;
		}
	};
    var sql = "select * from road_cross_point" +
    		  " where road_cross_point.sectionId=" + window.top.sectionId +
    		  " and road_cross_point.name='" + name + "'";
	window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function getFieldNames() {
	return window.crossPointName=="BP" || window.crossPointName=="EP" ? ["pile"] : ["pile","pj","ly","r","lsa","ta","lsb","tb","lx"];
}
function updateCurve() { //上传曲线资料
	//更新记录
	var crossPoint = {id: window.crossPointId};
	var fieldNames = getFieldNames();
	for(var i=0; i<fieldNames.length; i++) {
		var value = document.getElementsByName(fieldNames[i])[0].value;
		if(fieldNames[i]=="lx") {
			value = value.substring(value.lastIndexOf("(") + 1, value.length-1);
		}
		eval("crossPoint." + (fieldNames[i]=="pile" ? "jd" : fieldNames[i]) + "=value");
	}
	window.top.database.updateRecord("road_cross_point", crossPoint);
	//更新交点的偏角和方位角
	recomputeCrossPoints();
	//重新加载
	loadCurve(true);
}