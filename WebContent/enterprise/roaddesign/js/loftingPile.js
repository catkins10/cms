var ingredientName; //要素名称
var loftingPoint; //放样交点
window.onload = function() {
	document.getElementsByName("loftingResult")[0].style.display = 'none';
};
function compute() { //计算
	if(!window.top.baseStation) { //判断基站是否已经选择
        alert("请先选择基站");
    	return;
    }
    window.top.database.onAfterQuery = function(records) {
		doCompute(records);
	};
    //获取交点列表
    var sql = "select * from road_cross_point" +
    		  " where road_cross_point.sectionId=" + window.top.sectionId +
    		  " order by sn, name"; 
    window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function doCompute(crossPoints) {
    if(crossPoints.length<3) {
   		alert("少于两个交点,不允许放样");
   		return;
   	}
   	document.getElementsByName("x")[0].value = "";
    document.getElementsByName("y")[0].value = "";
    document.getElementsByName("z")[0].value = "";
    document.getElementsByName("horizontalAngle")[0].value = "";
    document.getElementsByName("horizontalDistance")[0].value = "";
    //桩号
    var pile = toDouble(document.getElementsByName("pile")[0].value);
    if(ingredientName=="GQ") { //要素名称
    	pile += 0.001;
    }
	var leftOrRight = document.getElementsByName("leftOrRight")[0].checked ? -1 : 1;
	var curve = null; //曲线要素
	if(crossPoints.length==3) { //仅有BP，EP
   		loftingPoint = crossPoints[1];
   		var nextPoint = crossPoints[2];
 		if(pile < toDouble(loftingPoint.jd) || pile > toDouble(nextPoint.jd)) {
       		alert("不在区间");
       		return;
       	}
       	//BP->EP按桩号放样
		curve = pileLoftingBPtoEP(toDouble(loftingPoint.jd), toDouble(loftingPoint.jx), toDouble(loftingPoint.jy), toDouble(nextPoint.jd), toDouble(nextPoint.jx), toDouble(nextPoint.jy), window.top.baseStation.surveyStationX, window.top.baseStation.surveyStationY, window.top.baseStation.backsightSurveyStationX, window.top.baseStation.backsightSurveyStationY, window.top.baseStation.backsightSurveyStationZ, pile, Math.abs(toDouble(document.getElementsByName('edgeDistance')[0].value)) * leftOrRight, toDouble(document.getElementsByName('actualHorizontalDistance')[0].value), window.top.baseStation.backHeight, window.top.baseStation.backMirrorHeight, toDouble(document.getElementsByName('height')[0].value), window.top.baseStation.backMirrorHeight);
    }
   	else { //超出2个交点
	   	for(var i = 2; i<crossPoints.length-1; i++) {
			var previousPoint = crossPoints[i-1];
			var nextPoint = crossPoints[i+1];
			//判断是否比BP桩号小
			if(i==2 && pile < (toDouble(previousPoint.jd) + toDouble(previousPoint.tb) - toDouble(previousPoint.d))) {
			    alert("不在区间");
			    return;
			}
			//判断是否比EP桩号大
			if(i==crossPoints.length-2 && pile > toDouble(nextPoint.jd) - toDouble(nextPoint.ta)) {
			    alert("不在区间");
			    return;
			}
			if(pile >= (toDouble(previousPoint.jd) + toDouble(previousPoint.tb) - toDouble(previousPoint.d)) &&
			   (pile < toDouble(nextPoint.jd) - toDouble(nextPoint.ta) || (pile == toDouble(nextPoint.jd) - toDouble(nextPoint.ta) && i==crossPoints.length-2))) {
	    		//开始放样
				loftingPoint = crossPoints[i];
				curve = pileLofting(toDouble(loftingPoint.lx), toDouble(loftingPoint.jd), toDouble(loftingPoint.pj), toDouble(loftingPoint.jx), toDouble(loftingPoint.jy), toDouble(loftingPoint.qfw), toDouble(loftingPoint.hfw), toDouble(loftingPoint.eta), toDouble(loftingPoint.r), toDouble(loftingPoint.lsa), toDouble(loftingPoint.lsb), toDouble(loftingPoint.ly), toDouble(loftingPoint.ta), toDouble(loftingPoint.tb), toDouble(previousPoint.jd), toDouble(previousPoint.ta), toDouble(previousPoint.tb), toDouble(previousPoint.d), toDouble(previousPoint.r), toDouble(previousPoint.jx), toDouble(previousPoint.jy), toDouble(previousPoint.qfw), toDouble(previousPoint.hfw), toDouble(previousPoint.lsa), toDouble(previousPoint.lsb), toDouble(previousPoint.ly), toDouble(nextPoint.jd), toDouble(nextPoint.ta), toDouble(nextPoint.r), window.top.baseStation.surveyStationX, window.top.baseStation.surveyStationY, window.top.baseStation.backsightSurveyStationX, window.top.baseStation.backsightSurveyStationY, window.top.baseStation.backsightSurveyStationZ, pile, Math.abs(toDouble(document.getElementsByName('edgeDistance')[0].value)) * leftOrRight, toDouble(document.getElementsByName('actualHorizontalDistance')[0].value), window.top.baseStation.backHeight, window.top.baseStation.backMirrorHeight, toDouble(document.getElementsByName('height')[0].value), toDouble(document.getElementsByName('mirrorHeight')[0].value));
				break;
			}
		}
	}
	if(!curve) {
	    alert("不在区间");
	    return;
	}
	//输出放样结果
	document.getElementsByName("x")[0].value = curve.X;
    document.getElementsByName("y")[0].value = curve.Y;
    document.getElementsByName("z")[0].value = curve.Z;
    document.getElementsByName("horizontalAngle")[0].value = curve.SPJ;
    document.getElementsByName("horizontalDistance")[0].value = curve.LL;
    var loftingResult = document.getElementsByName("loftingResult")[0];
    loftingResult.value = curve.DL <= 0 ? "前进" + (0 - curve.DL) + "米" : "后退" + curve.DL + "米";
	loftingResult.style.display = '';
	//保存到放样表
	saveLofting(round(pile, 4), curve.Z);
	//清空实测水平值
	document.getElementsByName("actualHorizontalDistance")[0].value = "";
}
//曲线要素
function generateCurve(LX, JD, LSa, LSb, LY, Ta, Tb) {
    var ZH, HY, GQ, YH, HZ;
    if(LX == 11 || LX == 21) {
        ZH = JD - Ta;
        HY = ZH + LSa;
        GQ = HY + LY;
    }
    else if(LX == 12 || LX == 22) {
        GQ = JD - Ta;
        YH = GQ + LY;
        HZ = YH + LSb;
    }
    else if(LX == 31) {
        ZH = JD - Ta;
        HY = ZH + LSa;
        YH = HY + LY;
        GQ = YH + LSb;
    }
    else if(LX == 32) {
        GQ = JD - Ta;
        HY = GQ + LSa;
        YH = HY + LY;
        HZ = YH + LSb;
    }
    else {
        ZH = JD - Ta;
        HY = ZH + LSa;
        GQ = HY + LY / 2;
        YH = GQ + LY / 2;
        HZ = YH + LSb;
    }
	var typeName = getCurveName(LX); //曲线类型
	JD = round(JD, 4); //交点桩号
	if(LX == 0) {
    	return {ingredientNames: 'LX,JD,ZH,HY,GQ,YH,HZ',
    			LX: typeName,
    			JD: JD,
        		ZH: round(ZH, 3),
        		HY: round(HY, 3),
        		GQ: round(GQ, 3),
        		YH: round(YH, 3),
        		HZ: round(HZ, 3)};
    }
    else if(LX == 11 || LX == 21) {
    	return {ingredientNames: 'LX,JD,ZH,HY,GQ',
    			LX: typeName,
    			JD: JD,
        		ZH: round(ZH, 3),
        		HY: round(HY, 3),
        		GQ: round(GQ, 3)};
    }
    else if(LX == 12 || LX == 22) {
    	return {ingredientNames: 'LX,JD,GQ,YH,HZ',
    			LX: typeName,
    			JD: JD,
        		GQ: round(GQ, 3),
        		YH: round(YH, 3),
        		HZ: round(HZ, 3)};
    }
    else if(LX == 31) {
    	return {ingredientNames: 'LX,JD,ZH,HY,YH,GQ',
    			LX: typeName,
    			JD: JD,
        		ZH: round(ZH, 3),
        		HY: round(HY, 3),
        		YH: round(YH, 3),
        		GQ: round(GQ, 3)};
    }
    else if(LX == 32) {
   		return {ingredientNames: 'LX,JD,GQ,HY,YH,HZ',
   				LX: typeName,
   				JD: JD,
        		GQ: round(GQ, 3),
        		HY: round(HY, 3),
        		YH: round(YH, 3),
        		HZ: round(HZ, 3)};
    }
}
//BP->EP按桩号放样
function pileLoftingBPtoEP(JD, Jx, Jy, HJD, HJx, HJy, cx, cy, X0, Y0, Z0, LC, W, L, HH, HJG, QH, QJG) {
    var HDJX, HDJY, HAA, FWc, FW2, FW1;
    HDJX = HJx - Jx;
    HDJY = HJy - Jy;
    if(HDJX == 0) {
        HCT = Math.PI / 2;
    }
    else {
        HCT = Math.atan(Math.abs(HDJY / HDJX));
    }
    if(HDJX >= 0 && HDJY >= 0) {
    	HAA = HCT;
    }
    if(HDJX <= 0 && HDJY >= 0) {
    	HAA = Math.PI - HCT;
    }
    if(HDJX <= 0 && HDJY <= 0) {
    	HAA = Math.PI + HCT;
    }
    if(HDJX >= 0 && HDJY <= 0) {
    	HAA = 2 * Math.PI - HCT;
    }
    SL = crossPointDistance(Jx, Jy, HJx, HJy); //距离

    F = HAA
    X = Jx + (LC - JD) * Math.cos(F);
    Y = Jy + (LC - JD) * Math.sin(F);
    if(F > 2 * Math.PI) {
        F = FWc - 2 * Math.PI;
    }
    else if(FWc < 0) {
        F = 2 * Math.PI - FWc;
    }
    J = 1;
    if(W < 0) {
    	J = -J
    }
    X = X + J * W * Math.cos(F + J * Math.PI / 2);
    Y = Y + J * W * Math.sin(F + J * Math.PI / 2);
    var DXC, DYC;
    DXC = cx - X0;
    DYC = cy - Y0;
    if(DXC == 0) {
        CT1 = Math.PI / 2;
    }
    else {
        CT1 = Math.atan(Math.abs(DYC / DXC));
    }
    if(DXC >= 0 && DYC >= 0) {
        FW1 = CT1;
    }
    if(DXC <= 0 && DYC >= 0) {
        FW1 = Math.PI - CT1;
    }
    if(DXC <= 0 && DYC <= 0) {
        FW1 = Math.PI + CT1;
    }
    if(DXC >= 0 && DYC <= 0) {
        FW1 = 2 * Math.PI - CT1;
    }
    var DXQ, DYQ;
    DXQ = X - cx;
    DYQ = Y - cy;
    if(DXQ == 0) {
        CT2 = Math.PI / 2;
    }
    else {
        CT2 = Math.atan(Math.abs(DYQ / DXQ));
    }
    if(DXQ >= 0 && DYQ >= 0) {
        FW2 = CT2;
    }
    if(DXQ <= 0 && DYQ >= 0) {
        FW2 = Math.PI - CT2;
    }
    if(DXQ <= 0 && DYQ <= 0) {
        FW2 = Math.PI + CT2;
    }
    if(DXQ >= 0 && DYQ <= 0) {
        FW2 = 2 * Math.PI - CT2;
    }
    SPJ = FW2 - FW1 + Math.PI;
    if(SPJ < 0) {
        SPJ = 2 * Math.PI + SPJ;
    }
    else if(SPJ > 2 * Math.PI) {
        SPJ = SPJ - 2 * Math.PI;
    }
    LL = Math.sqrt(DXQ * DXQ + DYQ * DYQ);
    DL = LL - L;
    Z = Z0 + (QH - QJG) - (HH - HJG);

    return {X: round(X, 4),
    		Y: round(Y, 4),
    		Z: round(Z, 4),
    		SPJ: round(dms(SPJ / Math.PI * 180), 4),
    		LL: round(LL, 4),
    		DL: round(DL, 2)};
}

//按桩号放样
//输入放样曲线交点桩号: JD
//点选仪器站CX，CY
//点选后视站X0，X0, Z0
//输入放样桩号LC
//前方位、后方位QFW, HFW
function pileLofting(LX, JD, PJ, Jx, Jy, QFW, HFW, ETa, R, LSa, LSb, LY, Ta, Tb, QJD, QTa, QTb, QD, QR, QJx, QJy, QQFW, QHFW, QLSa, QLSb, QLY, HJD, HTa, HR, cx, cy, X0, Y0, Z0, LC, W, L, HH, HJG, QH, QJG) {
    var F = 0, FWc = 0, X = 0, Y = 0, FW1 = 0, FW2 = 0;
    if(LSa == 0) {
    	LSa = 0.000000001;
    }
    if(LSb == 0) {
    	LSb = 0.000000001;
    }

    QFW = deg(QFW) * Math.PI / 180;
    HFW = deg(HFW) * Math.PI / 180;
    QQFW = deg(QQFW) * Math.PI / 180;
    QHFW = deg(QHFW) * Math.PI / 180;
    PJ = deg(PJ) * Math.PI / 180;

    var curve = generateCurve(LX, JD, LSa, LSb, LY, Ta, Tb);

    XHa = LSa - Math.pow(LSa, 3) / (40 * R * R) + Math.pow(LSa, 5) / (3456 * Math.pow(R, 4)) - Math.pow(LSa, 7) / (599040 * Math.pow(R, 6));
    XHb = LSb - Math.pow(LSb, 3) / (40 * R * R) + Math.pow(LSb, 5) / (3456 * Math.pow(R, 4)) - Math.pow(LSb, 7) / (599040 * Math.pow(R, 6));

    ZHX = Jx + Ta * Math.cos(QFW + Math.PI);
    ZHY = Jy + Ta * Math.sin(QFW + Math.PI);
    HZX = Jx + Tb * Math.cos(HFW);
    HZY = Jy + Tb * Math.sin(HFW);

    if(LX == 31) { //卵型曲线31
        if(LC <= curve.ZH) {
            X = Jx + (Ta + curve.ZH - LC) * Math.cos(QFW + Math.PI);
            Y = Jy + (Ta + curve.ZH - LC) * Math.sin(QFW + Math.PI);
            F = QFW;
        }
        else if(LC > curve.ZH && LC <= curve.HY) {
            LS = LSa
            L1 = LC - curve.ZH;
            X1 = L1 - Math.pow(L1, 5) / (40 * Math.pow(R, 2) * Math.pow(LS, 2)) + Math.pow(L1, 9) / (3456 * Math.pow(R, 4) * Math.pow(LS, 4)) - Math.pow(L1, 13) / (599040 * Math.pow(R, 6) * Math.pow(LS, 6));
            X = ZHX + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.cos(QFW + ETa * Math.pow(L1, 2) / 6 / R / LS);
            Y = ZHY + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.sin(QFW + ETa * Math.pow(L1, 2) / 6 / R / LS);
            F = Math.pow(L1, 2) * ETa / (2 * R * LS) + QFW;
        }
        else if(LC > curve.HY && LC <= curve.YH) {
            LS = LSa;
            L1 = LC - curve.HY;
            HYX = ZHX + XHa / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.cos(QFW + ETa * Math.pow(LS, 2) / 6 / R / LS);
            HYY = ZHY + XHa / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.sin(QFW + ETa * Math.pow(LS, 2) / 6 / R / LS);
            X = HYX + 2 * R * Math.sin(L1 / 2 / R) * Math.cos(QFW + ETa * (L1 + LS) / 2 / R);
            Y = HYY + 2 * R * Math.sin(L1 / 2 / R) * Math.sin(QFW + ETa * (L1 + LS) / 2 / R);
            F = QFW + ETa * LS / (2 * R) + L1 * ETa / R;
        }
        else if(LC > curve.YH && LC <= curve.GQ) {
            LS = LSa;
            L1 = LY;
            HYX = ZHX + XHa / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.cos(QFW + ETa * Math.pow(LS, 2) / 6 / R / LS);
            HYY = ZHY + XHa / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.sin(QFW + ETa * Math.pow(LS, 2) / 6 / R / LS);
            YHX = HYX + 2 * R * Math.sin(L1 / 2 / R) * Math.cos(QFW + ETa * (L1 + LS) / 2 / R);
            YHY = HYY + 2 * R * Math.sin(L1 / 2 / R) * Math.sin(QFW + ETa * (L1 + LS) / 2 / R);
            FWa = QFW + ETa * LS / (2 * R) + L1 * ETa / R;
            RAA = 1 / R;
            RBB = 1 / HR;
            X = ETa * (RBB - RAA) / (2 * LSb);
            T = Math.abs(LC - curve.YH);
            i = X * T;
            F = FWa + (i + 2 * ETa * RAA) * T / 2;
            U = 0;
            V = 0;
            WW = 0;
            Z = 0;
            for(S=1; S<=12; S++) {
                U = U + Math.cos(FWa + (i * (2 * S - 1) / 24 + 2 * ETa * RAA) * (2 * S - 1) / 24 * T / 2);
                V = V + Math.sin(FWa + (i * (2 * S - 1) / 24 + 2 * ETa * RAA) * (2 * S - 1) / 24 * T / 2);
            }
            for(S=1; S<=11; S++) {
                WW = WW + Math.cos(FWa + (i * S / 12 + 2 * ETa * RAA) * S / 12 * T / 2);
                Z = Z + Math.sin(FWa + (i * S / 12 + 2 * ETa * RAA) * S / 12 * T / 2);
            }
            X = YHX + T * (Math.cos(FWa) + 4 * U + 2 * WW + Math.cos(F)) / 72;
            Y = YHY + T * (Math.sin(FWa) + 4 * V + 2 * Z + Math.sin(F)) / 72;
        }
    }
    else if(LX == 32) { //卵型曲线32
        if(LC >= curve.HZ) {
            X = Jx + (Tb - curve.HZ + LC) * Math.cos(HFW);
            Y = Jy + (Tb - curve.HZ + LC) * Math.sin(HFW);
            F = HFW;
        }
        else if(LC > curve.GQ && LC <= curve.HY) {
            LS = QLSa;
            L1 = QLY;
            XHa = LS - Math.pow(LS, 3) / (40 * Math.pow(QR, 2)) + Math.pow(LS, 5) / (3456 * Math.pow(QR, 4)) - Math.pow(LS, 7) / (599040 * Math.pow(QR, 6));
            ZHX = QJx + QTa * Math.cos(QQFW + Math.PI);
            ZHY = QJy + QTa * Math.sin(QQFW + Math.PI);
            HYX = ZHX + XHa / Math.cos(Math.pow(LS, 2) / 6 / QR / LS) * Math.cos(QQFW + ETa * Math.pow(LS, 2) / 6 / QR / LS);
            HYY = ZHY + XHa / Math.cos(Math.pow(LS, 2) / 6 / QR / LS) * Math.sin(QQFW + ETa * Math.pow(LS, 2) / 6 / QR / LS);
            YHX = HYX + 2 * QR * Math.sin(L1 / 2 / QR) * Math.cos(QQFW + ETa * (L1 + LS) / 2 / QR);
            YHY = HYY + 2 * QR * Math.sin(L1 / 2 / QR) * Math.sin(QQFW + ETa * (L1 + LS) / 2 / QR);
            FWa = QQFW + ETa * LS / (2 * QR) + L1 * ETa / QR;
            RAA = 1 / QR;
            RBB = 1 / R;
            X = ETa * (RBB - RAA) / (2 * LSa);
            T = Math.abs(LC - curve.GQ + LSa);
            i = X * T;
            F = FWa + (i + 2 * ETa * RAA) * T / 2;
            U = 0;
            V = 0;
            WW = 0;
            Z = 0;
            for(S = 1; S<=12; S++) {
                U = U + Math.cos(FWa + (i * (2 * S - 1) / 24 + 2 * ETa * RAA) * (2 * S - 1) / 24 * T / 2);
                V = V + Math.sin(FWa + (i * (2 * S - 1) / 24 + 2 * ETa * RAA) * (2 * S - 1) / 24 * T / 2);
            }
            for(S = 1; S<=11; S++) {
                WW = WW + Math.cos(FWa + (i * S / 12 + 2 * ETa * RAA) * S / 12 * T / 2);
                Z = Z + Math.sin(FWa + (i * S / 12 + 2 * ETa * RAA) * S / 12 * T / 2);
            }
            X = YHX + T * (Math.cos(FWa) + 4 * U + 2 * WW + Math.cos(F)) / 72;
            Y = YHY + T * (Math.sin(FWa) + 4 * V + 2 * Z + Math.sin(F)) / 72;
        }
        else if(LC > curve.HY && LC <= curve.YH) {
            LS = LSb;
            L1 = curve.YH - LC;
            YHX = HZX + XHb / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.cos(HFW + Math.PI - ETa * Math.pow(LS, 2) / 6 / R / LS);
            YHY = HZY + XHb / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.sin(HFW + Math.PI - ETa * Math.pow(LS, 2) / 6 / R / LS);
            X = YHX + 2 * R * Math.sin(L1 / 2 / R) * Math.cos(HFW + Math.PI - ETa * (L1 + LS) / 2 / R);
            Y = YHY + 2 * R * Math.sin(L1 / 2 / R) * Math.sin(HFW + Math.PI - ETa * (L1 + LS) / 2 / R);
            F = HFW - ETa * LS / (2 * R) - L1 * ETa / R;
        }
        else if(curve.YH && curve.HZ && LC > curve.YH && LC < curve.HZ) {
            LS = LSb;
            L1 = curve.HZ - LC;
            X1 = L1 - Math.pow(L1, 5) / (40 * Math.pow(R, 2) * Math.pow(LS, 2)) + Math.pow(L1, 9) / (3456 * Math.pow(R, 4) * Math.pow(LS, 4)) - Math.pow(L1, 13) / (599040 * Math.pow(R, 6) * Math.pow(LS, 6));
            X = HZX + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.cos(HFW + Math.PI - ETa * Math.pow(L1, 2) / 6 / R / LS);
            Y = HZY + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.sin(HFW + Math.PI - ETa * Math.pow(L1, 2) / 6 / R / LS);
            F = HFW - Math.pow(L1, 2) * ETa / (2 * R * LS);
        }
	}
    else { //其他曲线
        if(curve.ZH && LC <= curve.ZH) {
            X = Jx + (Ta + curve.ZH - LC) * Math.cos(QFW + Math.PI);
            Y = Jy + (Ta + curve.ZH - LC) * Math.sin(QFW + Math.PI);
            F = QFW;
        }
        else if(curve.HZ && LC >= curve.HZ) {
            X = Jx + (Tb - curve.HZ + LC) * Math.cos(HFW);
            Y = Jy + (Tb - curve.HZ + LC) * Math.sin(HFW);
            F = HFW;
        }
        else if(curve.ZH && curve.HY && LC > curve.ZH && LC <= curve.HY) {
            LS = LSa;
            L1 = LC - curve.ZH;
            X1 = L1 - Math.pow(L1, 5) / (40 * Math.pow(R, 2) * Math.pow(LS, 2)) + Math.pow(L1, 9) / (3456 * Math.pow(R, 4) * Math.pow(LS, 4)) - Math.pow(L1, 13) / (599040 * Math.pow(R, 6) * Math.pow(LS, 6));
            X = ZHX + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.cos(QFW + ETa * Math.pow(L1, 2) / 6 / R / LS);
            Y = ZHY + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.sin(QFW + ETa * Math.pow(L1, 2) / 6 / R / LS);
            F = Math.pow(L1, 2) * ETa / (2 * R * LS) + QFW;
        }
        else if(curve.HY && LC > curve.HY && LC <= curve.GQ) {
            LS = LSa;
            L1 = LC - curve.HY;
            HYX = ZHX + XHa / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.cos(QFW + ETa * Math.pow(LS, 2) / 6 / R / LS);
            HYY = ZHY + XHa / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.sin(QFW + ETa * Math.pow(LS, 2) / 6 / R / LS);
            X = HYX + 2 * R * Math.sin(L1 / 2 / R) * Math.cos(QFW + ETa * (L1 + LS) / 2 / R);
            Y = HYY + 2 * R * Math.sin(L1 / 2 / R) * Math.sin(QFW + ETa * (L1 + LS) / 2 / R);
            F = QFW + ETa * LS / (2 * R) + L1 * ETa / R;
        }
        else if(curve.YH && LC > curve.GQ && LC <= curve.YH) {
            LS = LSb;
            L1 = curve.YH - LC;
            YHX = HZX + XHb / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.cos(HFW + Math.PI - ETa * Math.pow(LS, 2) / 6 / R / LS);
            YHY = HZY + XHb / Math.cos(Math.pow(LS, 2) / 6 / R / LS) * Math.sin(HFW + Math.PI - ETa * Math.pow(LS, 2) / 6 / R / LS);
            X = YHX + 2 * R * Math.sin(L1 / 2 / R) * Math.cos(HFW + Math.PI - ETa * (L1 + LS) / 2 / R);
            Y = YHY + 2 * R * Math.sin(L1 / 2 / R) * Math.sin(HFW + Math.PI - ETa * (L1 + LS) / 2 / R);
            F = HFW - ETa * LS / (2 * R) - L1 * ETa / R;
        }
        else if(curve.YH && curve.HZ && LC > curve.YH && LC < curve.HZ) {
            LS = LSb;
            L1 = curve.HZ - LC;
            X1 = L1 - Math.pow(L1, 5) / (40 * Math.pow(R, 2) * Math.pow(LS, 2)) + Math.pow(L1, 9) / (3456 * Math.pow(R, 4) * Math.pow(LS, 4)) - Math.pow(L1, 13) / (599040 * Math.pow(R, 6) * Math.pow(LS, 6));
            X = HZX + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.cos(HFW + Math.PI - ETa * Math.pow(L1, 2) / 6 / R / LS);
            Y = HZY + X1 / Math.cos(Math.pow(L1, 2) / 6 / R / LS) * Math.sin(HFW + Math.PI - ETa * Math.pow(L1, 2) / 6 / R / LS);
            F = HFW - Math.pow(L1, 2) * ETa / (2 * R * LS);
        }
    }

    if(F > 2 * Math.PI) {
        F = FWc - 2 * Math.PI;
    }
    else if(FWc < 0) {
        F = 2 * Math.PI - FWc;
    }
    J = 1
    if(W < 0) {
    	J = -J;
    }
    X = X + J * W * Math.cos(F + J * Math.PI / 2);
    Y = Y + J * W * Math.sin(F + J * Math.PI / 2);

    var DXC, DYC;
    DXC = cx - X0;
    DYC = cy - Y0;
    if(DXC == 0) {
        CT1 = Math.PI / 2;
    }
    else {
        CT1 = Math.atan(Math.abs(DYC / DXC));
    }
    if(DXC >= 0 && DYC >= 0) {
        FW1 = CT1;
    }
    if(DXC <= 0 && DYC >= 0) {
        FW1 = Math.PI - CT1;
    }
    if(DXC <= 0 && DYC <= 0) {
        FW1 = Math.PI + CT1;
    }
    if(DXC >= 0 && DYC <= 0) {
        FW1 = 2 * Math.PI - CT1;
    }
    var DXQ, DYQ;
    DXQ = X - cx;
    DYQ = Y - cy;
    if(DXQ == 0) {
        CT2 = Math.PI / 2;
    }
    else {
        CT2 = Math.atan(Math.abs(DYQ / DXQ));
    }
    if(DXQ >= 0 && DYQ >= 0) {
        FW2 = CT2;
    }
    if(DXQ <= 0 && DYQ >= 0) {
        FW2 = Math.PI - CT2;
    }
    if(DXQ <= 0 && DYQ <= 0) {
        FW2 = Math.PI + CT2;
    }
    if(DXQ >= 0 && DYQ <= 0) {
        FW2 = 2 * Math.PI - CT2;
    }

    SPJ = FW2 - FW1 + Math.PI;
    if(SPJ < 0) {
        SPJ = 2 * Math.PI + SPJ
    }
    else if(SPJ > 2 * Math.PI) {
        SPJ = SPJ - 2 * Math.PI;
    }

    LL = Math.sqrt(DXQ * DXQ + DYQ * DYQ);
    DL = LL - L;
    Z = Z0 + (QH - QJG) - (HH - HJG);
    curve.X = round(X, 4);
    curve.Y = round(Y, 4);
    curve.Z = round(Z, 4);
    curve.SPJ = round(dms(SPJ / Math.PI * 180), 4);
    curve.LL = round(LL, 4);
    curve.DL = round(DL, 2);
    return curve;
}
function switchPile(nextPile) { //切换桩号
    ingredientName = ""; //要素名称
    var pile = toDouble(document.getElementsByName("pile")[0].value);
    var newValue = Math.floor(pile / 10) * 10 + (nextPile ? 20 : -20);
    if(loftingPoint) {
        var curve = generateCurve(toDouble(loftingPoint.lx), toDouble(loftingPoint.jd), toDouble(loftingPoint.lsa), toDouble(loftingPoint.lsb), toDouble(loftingPoint.ly), toDouble(loftingPoint.ta), toDouble(loftingPoint.tb));
        var ingredientNames = curve.ingredientNames.split(",");
        for(var i = 2; i<ingredientNames.length; i++) {
            var value = eval("curve." + ingredientNames[i]);
            if((nextPile && pile < value && newValue > value) || (!nextPile && pile > value && newValue < value)) {
                newValue = value;
                ingredientName = ingredientNames[i]; //要素名称
                break;
            }
        }
    }
    document.getElementsByName("pile")[0].value = newValue;
    document.getElementsByName("height")[0].value = "";
    document.getElementsByName("mirrorHeight")[0].value = "1.3";
    compute(); //重新计算
}
function showCurveIngredients() { //显示曲线元素
	//获取交点参数
	if(loftingPoint && loftingPoint.name!="BP" && loftingPoint.name!="EP") {
		var curve = generateCurve(toDouble(loftingPoint.lx), toDouble(loftingPoint.jd), toDouble(loftingPoint.lsa), toDouble(loftingPoint.lsb), toDouble(loftingPoint.ly), toDouble(loftingPoint.ta), toDouble(loftingPoint.tb));
        var ingredientNames = curve.ingredientNames.split(",");
        var values = "";
        for(var i = 0; i<ingredientNames.length; i++) {
		    values += (i==0 ? "" : "<br/>") + ingredientNames[i] + "： " + eval("curve." + ingredientNames[i]);
		}
		window.top.client.alert(values, '计算结果');
	}
}