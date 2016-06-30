function compute() {
	var curveLength = !document.getElementsByName("curveLengthEnabled")[0].checked ?  -1 : toDouble(document.getElementsByName("curveLength")[0].value);
    var option = document.getElementsByName("option")[0].value;
    if("外距控制"==option) {
    	WaiJuKongZhi(toDouble(document.getElementsByName("driftAngle")[0].value), curveLength, toDouble(document.getElementsByName("value")[0].value));
    }
	else if("切线长控制"==option) {
		QieXianChangKongZhi(toDouble(document.getElementsByName("driftAngle")[0].value), curveLength, toDouble(document.getElementsByName("value")[0].value));
	}
	else if("曲线长控制"==option) {
		QuXianChangKongZhi(toDouble(document.getElementsByName("driftAngle")[0].value), curveLength, toDouble(document.getElementsByName("value")[0].value));
	}
	else if("支距控制"==option) {
		QuXianZhiJu(toDouble(document.getElementsByName("driftAngle")[0].value), curveLength, toDouble(document.getElementsByName("value")[0].value), toDouble(document.getElementsByName("y0")[0].value));
	}
	else if("半径控制"==option) {
		BanJingKongZhi(toDouble(document.getElementsByName("driftAngle")[0].value), toDouble(document.getElementsByName("lsa")[0].value), toDouble(document.getElementsByName("lsb")[0].value), toDouble(document.getElementsByName("value")[0].value));
	}
}
function onOptionChange() {
	var option = document.getElementsByName("option")[0].value;
	var byRadius = false;
	var labelValue = "";
	var panelY0 = false;
	//外距控制\0切线长控制\0曲线长控制\0支距控制\0半径控制
	if("外距控制"==option) {
    	labelValue = "控制的外距值";
    }
	else if("切线长控制"==option) {
		labelValue = "控制的切线长";
	}
	else if("曲线长控制"==option) {
		labelValue = "平曲线长度";
	}
	else if("支距控制"==option) {
		labelValue = "切向支距";
		panelY0 = true;
	}
	else if("半径控制"==option) {
		labelValue = "半径";
		byRadius = true;
	}
    document.getElementById("labelValue").innerHTML = labelValue;
   	document.getElementById("panelY0").style.display = panelY0 ? "" : "none";
	document.getElementById("panelCurveLength").style.display = byRadius ? "none" : "";
	document.getElementById("panelRadius").style.display = byRadius ? "" : "none";
	window.top.client.resetForm(document); //重置表单
}
/**
 * 单交点平曲线设计:外距控制
 * PJ:偏角值
 * LS:缓和曲线长,-1表示未定
 * E:控制的外距值
 **/
function WaiJuKongZhi(PJ, L0, E) {
    oPJ = PJ;
    PJ = deg(PJ);
    LS = Math.abs(L0);
    F = 1 / degreesCos(PJ / 2);
    if(L0 == -1) {
        R = E / ((1 + Math.pow(PJ * Math.PI / 180, 2) / 96) * F - 1);
        LS = R * PJ * Math.PI / 180 / 2;
        LS = Math.floor(LS);
    }
    a = F - 1;
    b = -E;
    c = LS * LS / 24 * F;

    d = b * b - 4 * a * c;
    R = 0;
    if(d < 0) {
        alert("LS=" + LS + "太大");
        return;
    }
    R = (-b + Math.sqrt(d)) / 2 / a;

    P = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3);
    T = (R + P) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
    E = (R + P) / degreesCos(PJ / 2) - R;
    LY = (PJ * Math.PI / 180 - LS / R) * R;
    L = LS + LY + LS;
	var values = "PJ： " + round(oPJ, 4) + "<br/>" +
				 "R： " + round(R, 4) + "<br/>" +
				 "LS： " + round(LS, 4) + "<br/>" +
				 "T： " + round(T, 4) + "<br/>" +
				 "E： " + round(E, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}

//切线长控制
function QieXianChangKongZhi(PJ, L0, T) {
    oPJ = PJ;
    PJ = deg(PJ);
    LS = Math.abs(L0);
    if(L0 == -1) {
        JP = PJ * Math.PI / 180;
        R = T / ((1 + JP * JP / 96) * degreesTan(PJ / 2) + JP / 4 - Math.pow(JP, 3) / 1920);
        LS = Math.floor(R * JP / 2);
    }

    a = degreesTan(PJ / 2);
    b = LS / 2 - T;
    c = LS * LS / 24 * a;

    d = b * b - 4 * a * c;
    R = 0;
    if(d < 0) {
        alert("LS=" + LS + "太大");
        return;
    }
    R = (-b + Math.pow(d, 0.5)) / 2 / a;

    while(true) {
        T1 = (R + LS * LS / 24 / R) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
        var T2 = T - T1
        if(isNaN(T2) || Math.abs(T2) < 0.001) {
        	break;
        }
        R = R + (T - T1) / degreesTan(PJ / 2);
    }

    LY = (PJ * Math.PI / 180 - LS / R) * R;
    L = LS + LY + LS;
    E = (R + LS * LS / 24 / R) / degreesCos(PJ / 2) - R;

	var values = "PJ： " + round(oPJ, 4) + "<br/>" +
				 "R： " + round(R, 4) + "<br/>" +
				 "LS： " + round(LS, 4) + "<br/>" +
				 "T： " + round(T, 4) + "<br/>" +
				 "E： " + round(E, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}

//曲线长控制
function QuXianChangKongZhi(PJ, L0, L) {
    oPJ = PJ;
    PJ = deg(PJ);
    LS = Math.abs(L0);
    if(L0 == -1) {
    	LS = Math.floor(L / 3);
    }
    LY = L - 2 * LS;
    R = (LY + LS) / PJ * 180 / Math.PI;
    LY = (PJ * Math.PI / 180 - LS / R) * R;
    L = 2 * LS + LY;
    P = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3);
    T = (R + P) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
    E = (R + P) / degreesCos(PJ / 2) - R;

	var values = "PJ： " + round(oPJ, 4) + "<br/>" +
				 "R： " + round(R, 4) + "<br/>" +
				 "LS： " + round(LS, 4) + "<br/>" +
				 "T： " + round(T, 4) + "<br/>" +
				 "E： " + round(E, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4);
	window.top.client.alert(values, '计算结果');
}

//曲线上任意点支距控制
function QuXianZhiJu(PJ, L0, T0, Y0) {
    oPJ = PJ;
    PJ = deg(PJ);
    LS = Math.abs(L0);
    a = Math.pow(degreesTan(PJ / 2), 2);
    b = -2 * (degreesTan(PJ) * T0 + Y0);
    c = T0 * T0 + Y0 * Y0;

    d = b * b - 4 * a * c;
    R = 0;
    if(d < 0) {
        alert("LS=" & LS & "太大")
        return;
    }
    R = (-b + Math.pow(d, 0.5)) / 2 / a;

    if(L0 == -1) {
    	LS = R * PJ * Math.PI / 180 / 2;
    }
    R = R - LS * LS / 24 / R;
    while(true) {
        P = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3);
        T = (R + P) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
        X = T - T0;
        L = X;
        while(true) {
            if(L >= LS) {
                FI = ((L - LS) / R + LS / 2 / R) * 180 / Math.PI;
                Q = LS / 2 - Math.pow(LS, 3) / 240 / R / R;
                X1 = Q + R * degreesSin(FI);
                Y = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3) + R * (1 - degreesCos(FI));
            }
            else {
                X1 = L - Math.pow(L, 5) / 40 / R / R / LS / LS;
                Y = Math.pow(L, 3) / 6 / R / LS - Math.pow(L, 7) / 336 / Math.pow(R, 3) / Math.pow(LS, 3);
            }
            var DX = X - X1
            if(isNaN(DX) || Math.abs(DX) < 0.01) {
            	break;
            }
            L = L + DX;
        }
        var DY = Y - Y0;
        if(isNaN(DY)) {
       		break;
        }
        if(Math.abs(DY) < 0.004) {
            if(isNaN(L0) || L0 >= 0) {
            	break;
            }
            LY = (PJ * Math.PI / 180 - LS / R) * R;
            var L2 = LY - LS;
            if(isNaN(L2) || Math.abs(L2) < 5) {
            	break;
            }
            LS = Math.floor((2 * LS + LY) / 3);
        }
        else {
            //ret = ret & "DY=" & DY & vbCrLf
            R = R - 10 * DY;
            //ret = ret & "R=" & R & vbCrLf
        }
    }
    P = LS * LS / 24 / R - Math.pow(LS, 4) / 2384 / Math.pow(R, 3);
    T = (R + P) * degreesTan(PJ / 2) + LS / 2 - Math.pow(LS, 3) / 240 / R / R;
    E = (R + P) / degreesCos(PJ / 2) - R;
    LY = (PJ * Math.PI / 180 - LS / R) * R;
    L = LS + LY + LS;

	var values = "PJ： " + round(oPJ, 4) + "<br/>" +
				 "R： " + round(R, 4) + "<br/>" +
				 "LS： " + round(LS, 4) + "<br/>" +
				 "T： " + round(T, 4) + "<br/>" +
				 "E： " + round(E, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4) + "<br/>" +
				 "t： " + round(T - X, 4) + "<br/>" +
				 "y： " + round(Y, 4);
	window.top.client.alert(values, '计算结果');
}

//半径控制
function BanJingKongZhi(PJ, LSa, LSb, R) {
    oPJ = PJ;
    PJ = deg(PJ);
    Pa = LSa * LSa / 24 / R - Math.pow(LSa, 4) / 2384 / Math.pow(R, 3);
    Pb = LSb * LSb / 24 / R - Math.pow(LSb, 4) / 2384 / Math.pow(R, 3);
    Qa = LSa / 2 - Math.pow(LSa, 3) / 240 / R / R;
    Qb = LSb / 2 - Math.pow(LSb, 3) / 240 / R / R;

    Ta = Qa + (R + Pb - (R + Pa) * degreesCos(PJ)) / degreesSin(PJ);
    Tb = Qb + (R + Pa - (R + Pb) * degreesCos(PJ)) / degreesSin(PJ);

    BTa = LSa / 2 / R;
    BTb = LSb / 2 / R;

    LY = (PJ * Math.PI / 180 - LSa / 2 / R - LSb / 2 / R) * R;
    L = LSa + LY + LSb;

	var values = "PJ： " + round(oPJ, 4) + "<br/>" +
				 "LSa： " + round(LSa, 4) + "<br/>" +
				 "LSb： " + round(LSb, 4) + "<br/>" +
				 "Ta： " + round(Ta, 4) + "<br/>" +
				 "Tb： " + round(Tb, 4) + "<br/>" +
				 "LY： " + round(LY, 4) + "<br/>" +
				 "L： " + round(L, 4);;
    if(LSa == LSb) {
        E = (R + Pa) / degreesCos(PJ / 2) - R;
        values += "<br/>E： " + round(E, 4);
    }
    window.top.client.alert(values, '计算结果');
}