var coordinates; //坐标列表
window.onload = function() {
	document.getElementsByName("loftingResult")[0].style.display = 'none';
	document.getElementsByName("pile")[0].onchange = function() {
		for(var i=0; i<coordinates.length; i++) {
			if(coordinates[i].LC==Number(this.value)) {
				document.getElementsByName('x')[0].value = coordinates[i].X;
				document.getElementsByName('y')[0].value = coordinates[i].Y;
				break;
			}
		}
	};
};
function compute() {
	if(!window.top.baseStation) { //判断基站是否已经选择
        alert("请先选择基站");
    	return;
    }
    document.getElementsByName('horizontalAngle')[0].value = "";
    document.getElementsByName('horizontalDistance')[0].value = "";
    document.getElementsByName('z')[0].value = "";
    //放样
    var curve = coordinateLofting(window.top.baseStation.surveyStationX, window.top.baseStation.surveyStationY, window.top.baseStation.backsightSurveyStationX, window.top.baseStation.backsightSurveyStationY, toDouble(document.getElementsByName('x')[0].value), toDouble(document.getElementsByName('y')[0].value), toDouble(document.getElementsByName('actualHorizontalDistance')[0].value), window.top.baseStation.backHeight, window.top.baseStation.backMirrorHeight, toDouble(document.getElementsByName('height')[0].value), toDouble(document.getElementsByName('mirrorHeight')[0].value), window.top.baseStation.backsightSurveyStationZ);
    //输出放样结果
    document.getElementsByName('horizontalAngle')[0].value = curve.SPJ;
    document.getElementsByName('horizontalDistance')[0].value = curve.LL;
    document.getElementsByName('z')[0].value = curve.Z;
    var loftingResult = document.getElementsByName("loftingResult")[0];
    loftingResult.value = curve.DL <= 0 ? "前进" + (0 - curve.DL) + "米" : "后退" + curve.DL + "米";
	loftingResult.style.display = '';
    //清空实测水平值
    document.getElementsByName('actualHorizontalDistance')[0].value = "";
    //保存到放样表
    saveLofting(round(toDouble(document.getElementsByName('pile')[0].value), 4), curve.Z);
}
function readCoordinateFile() { //读取坐标文件
	window.top.fileSystem.onSelectFile = function(filePath) {
		doReadCoordinateFile(filePath);
	};
	window.top.fileSystem.selectFile("", "*.zbb", false, false); //坐标文件(*.zbb)|*.zbb|所有文件(*.*)|*.*
}
function doReadCoordinateFile(filePath) { //读入坐标文件
	var firstLine = true;
	var haiDi = 1;
	var listItems = "";
	coordinates = new Array();
	window.top.fileSystem.onReadLineFromFile = function(line) { //返回true继续读取,返回false放弃,line==null表示文件读取完成
		if(line==null) { //读取完成
			if(listItems!="") {
				//设置桩号列表
				DropdownField.setListValues("pile", listItems);
				//初始化为第一个坐标
				var pile = document.getElementsByName('pile')[0];
				pile.value = coordinates[0].LC;
				pile.onchange();
			}
			return true;
		}
		if(line=="") {
			return true;
		}
		var values = parseLine(line, line.indexOf(",")==-1 ? " " : ",");
        if(firstLine) {
            if(values[0] == "NE" || values[0] == "XY") {
                haiDi = 0;
                values[0] = "";
            }
            firstLine = false;
        }
        if(values[0] != "") {
        	var coordinate = {LC: 0,
            				  X: 0,
            				  Y: 0,
            				  Z: 0};
            if(values.length - haiDi > 0) {
            	coordinate.LC = toDouble(values[1 - haiDi]);
            }
            if(values.length - haiDi > 1) {
            	coordinate.X = toDouble(values[2 - haiDi])
            }
            if(values.length - haiDi > 2) {
            	coordinate.Y = toDouble(values[3 - haiDi]);
            }
            if(values.length - haiDi > 3) {
            	coordinate.Z = toDouble(values[4 - haiDi]);
            }
            listItems += (listItems=='' ? '' : '\0') + coordinate.LC;
            coordinates[coordinates.length] = coordinate;
        }
        return true;
    };
	window.top.fileSystem.readTextFromFile(filePath);
}
//坐标放样
function coordinateLofting(DXa, DYa, DXb, DYb, Xc, Yc, L, HH, HJG, QH, QJG, Z0) {
    //Rem 按坐标放样:CLS
    //读入测站名坐标 DXa, DYa
    //读入后视测站坐标DXb , DYb
    var DHX, DHY, CCTA, HCTA, oPJ;
    DHX = DXa - DXb;
    DHY = DYa - DYb;
    if(DHX == 0) {
        HCT = Math.PI / 2;
    }
    else {
        HCT = Math.atan(Math.abs(DHY / DHX));
    }
    if(DHX >= 0 && DHY >= 0) {
        HCTA = HCT;
    }
    else if(DHX <= 0 && DHY >= 0) {
        HCTA = Math.PI - HCT;
    }
    else if(DHX <= 0 && DHY <= 0) {
        HCTA = Math.PI + HCT;
    }
    else if(DHX >= 0 && DHY <= 0) {
        HCTA = 2 * Math.PI - HCT;
    }

    //输入放样点坐标XC，YC
    //直接读入坐标数据
	var DCX, DCY;
    DCX = Xc - DXa;
    DCY = Yc - DYa;
    LL = Math.sqrt(DCX * DCX + DCY * DCY);
    if(DCX == 0) {
        CCT = Math.PI / 2;
    }
    else {
        CCT = Math.atan(Math.abs(DCY / DCX));
    }

    if(DCX >= 0 && DCY >= 0) {
        CCTA = CCT;
    }
    else if(DCX <= 0 && DCY >= 0) {
        CCTA = Math.PI - CCT;
    }
    else if(DCX <= 0 && DCY <= 0) {
        CCTA = Math.PI + CCT;
    }
    else if(DCX >= 0 && DCY <= 0) {
        CCTA = 2 * Math.PI - CCT;
    }

    AF = CCTA - HCTA + Math.PI;
    if(AF < 0) {
        AF = 2 * Math.PI + AF;
    }
    else if(AF > 2 * Math.PI) {
        AF = AF - 2 * Math.PI;
    }

    //计算Z
    DL = LL - L;
    Z = Z0 + (QH - QJG) - (HH - HJG);

    return {PJ: round(oPJ, 4),
			Z: round(Z, 4),
    		SPJ: round(dms(AF / Math.PI * 180), 4),
    		LL: round(LL, 4),
    		DL: round(DL, 2)};
}
function switchPile(nextPile) { //切换桩号
    var pile = document.getElementsByName("pile")[0];
    for(var i=0; i<coordinates.length; i++) {
		if(coordinates[i].LC==Number(pile.value)) {
			i = nextPile ? i+1 : i-1;
			if(i<0 || i>=coordinates.length) {
				return;
			}
			pile.value = coordinates[i].LC;
			pile.onchange();
		    document.getElementsByName("height")[0].value = "";
    		document.getElementsByName("mirrorHeight")[0].value = "1.3";
    		compute(); //重新计算
			return;
		}
	}
}