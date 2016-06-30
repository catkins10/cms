function exportTraverse() { //成果输出:导线
	window.top.fileSystem.onSelectFile = function(filePath) {
		doExportTraverse(filePath);
	};
	window.top.fileSystem.saveFile("", window.top.projectName + "_" + window.top.sectionName + ".dxd", 0, "*.dxd");
}
function doExportTraverse(filePath) {
	//获取导线点列表
	window.top.database.onAfterQuery = function(records) {
		if(!records || records.length<2) {
			alert("没有可输出的数据");
			return;
		}
		var recordIndex = 1;
		window.top.fileSystem.onReadLineToWriteFile = function() { //返回null表示已经没有需要写入的行
			if(recordIndex>=records.length) {
				return null;
			}
			var line = records[recordIndex].name + " " + records[recordIndex].x + " " + records[recordIndex].y + " " + records[recordIndex].z;
			recordIndex++;
			return line;
		}
		window.top.fileSystem.writeTextToFile(filePath); //写文本文件
	};
	var sql = "select *" +
			  " from road_traverse" +
			  " where sectionId=" + window.top.sectionId +
			  " order by name";
	window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function exportCrossPoint() { //成果输出:交点
	window.top.fileSystem.onSelectFile = function(filePath) {
		doExportCrossPoint(filePath);
	};
	window.top.fileSystem.saveFile("", window.top.projectName + "_" + window.top.sectionName + ".jdx", 0, "*.jdx");
}
function doExportCrossPoint(filePath) {
	window.top.database.onAfterQuery = function(records) {
		if(!records || records.length<2) {
			alert("没有可输出的数据");
			return;
		}
		var recordIndex = 1;
		window.top.fileSystem.onReadLineToWriteFile = function() { //返回null表示已经没有需要写入的行
			if(recordIndex>=records.length) {
				return null;
			}
			var line;
			if(records[recordIndex].name == "BP" || records[recordIndex].name == "EP") {
                line = records[recordIndex].name + " " + records[recordIndex].jx + " " + records[recordIndex].jy;
            }
            else {
                var lx = records[recordIndex].lx == "0" ? "" : " " + records[recordIndex].lx;
                line = records[recordIndex].name + " " + records[recordIndex].jx + " " + records[recordIndex].jy + " " + records[recordIndex].r + " " + records[recordIndex].lsa + " " + records[recordIndex].lsb + lx;
            }
			recordIndex++;
			return line;
		}
		window.top.fileSystem.writeTextToFile(filePath); //写文本文件
	};
    //获取交点列表
    var sql = "select * from road_cross_point" +
    		  " where road_cross_point.sectionId=" + window.top.sectionId +
    		  " order by sn, name"; 
    window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function exportHeightData() { //成果输出:地面高
	window.top.fileSystem.onSelectFile = function(filePath) {
		doExportHeightData(filePath);
	};
	window.top.fileSystem.saveFile("", window.top.projectName + "_" + window.top.sectionName + ".dmg", 0, "*.dmg");
}
function doExportHeightData(filePath) {
	//获取导线点列表
	window.top.database.onAfterQuery = function(records) {
		if(!records || records.length<2) {
			alert("没有可输出的数据");
			return;
		}
		var recordIndex = 1;
		window.top.fileSystem.onReadLineToWriteFile = function() { //返回null表示已经没有需要写入的行
			if(recordIndex>=records.length) {
				return null;
			}
			var line = records[recordIndex].pile + " " + records[recordIndex].z;
			recordIndex++;
			return line;
		}
		window.top.fileSystem.writeTextToFile(filePath); //写文本文件
	};
	var sql = "select *" +
			  " from road_lofting" +
			  " where sectionId=" + window.top.sectionId +
			  " order by cast(pile as real)";
	window.top.database.databaseQuery(sql, 0, 0, null, false);
}