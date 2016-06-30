//交点距离计算
function crossPointDistance(Jx, Jy, HJx, HJy) {
    var HDJX = HJx - Jx;
    var HDJY = HJy - Jy;
    return round(Math.sqrt(HDJX * HDJX + HDJY * HDJY), 4);
}
//交点计算
function computeCrossPoint(Jx, Jy, QJx, QJy, HJx, HJy, LSa, LSb, R, DP, QJD, QSL, QD, JDDL, LX) {
    var DJX, DJy, HDJX, HDJY, HAA, AA, Ta, Tb, LY, d;
    DJX = Jx - QJx;
    DJy = Jy - QJy;
    HDJX = HJx - Jx;
    HDJY = HJy - Jy;
    if(DJX == 0) {
        CT = Math.PI / 2;
    }
    else {
        CT = Math.atan(Math.abs(DJy / DJX));
    }
    if(DJX >= 0 && DJy >= 0) {
    	AA = CT;
    }
    if(DJX <= 0 && DJy >= 0) {
    	AA = Math.PI - CT;
    }
    if(DJX <= 0 && DJy <= 0) {
    	AA = Math.PI + CT;
    }
    if(DJX >= 0 && DJy <= 0) {
    	AA = 2 * Math.PI - CT;
    }

    if(HDJX == 0) {
        HCT = Math.PI / 2
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
    AI = HAA - AA;
    if(AI > Math.PI) {
    	AI = -(2 * Math.PI - AI);
    }
    if(AI < -Math.PI) {
    	AI = 2 * Math.PI + AI;
    }

    SL = crossPointDistance(Jx, Jy, HJx, HJy); //距离

    var PJ = dms(AI / Math.PI * 180);
    if(PJ < 0) {
        ETa = -1;
    }
    else {
        ETa = 1;
    }
    PJ = Math.abs(PJ);
    PJ = deg(PJ);

    if(R != 0) {
        if(LX == 0 || LX == 11 || LX == 12) {  //单圆曲线", "与基线相切双交点复曲线"
            Pa = LSa * LSa / 24 / R - Math.pow(LSa, 4) / 2384 / Math.pow(R, 3);
            Pb = LSb * LSb / 24 / R - Math.pow(LSb, 4) / 2384 / Math.pow(R, 3);
            Qa = LSa / 2 - Math.pow(LSa, 3) / 240 / R / R;
            Qb = LSb / 2 - Math.pow(LSb, 3) / 240 / R / R;
            Ta = Qa + (R + Pb - (R + Pa) * degreesCos(PJ)) / degreesSin(PJ);
            Tb = Qb + (R + Pa - (R + Pb) * degreesCos(PJ)) / degreesSin(PJ);
            LY = (PJ * Math.PI / 180 - LSa / 2 / R - LSb / 2 / R) * R;
        }
        else if(LX == 21) { //与基线不相切双交点复曲线
            Pa = Math.pow(LSa, 2) / 24 / R - Math.pow(LSa, 4) / 2384 / Math.pow(R, 3);
            Qa = LSa / 2 - Math.pow(LSa, 3) / 240 / Math.pow(R, 2);
            Tb = (R + Pa) * degreesTan(PJ / 2);
            Ta = Tb + Qa;
            LY = Math.PI / 180 * PJ * R - LSa / 2;
        }
        else if(LX == 22) { //与基线不相切双交点复曲线
            Pb = Math.pow(LSb, 2) / 24 / R - Math.pow(LSb, 4) / 2384 / Math.pow(R, 3);
            Qb = LSb / 2 - Math.pow(LSb, 3) / 240 / Math.pow(R, 2);
            Ta = (R + Pb) * degreesTan(PJ / 2);
            Tb = Ta + Qb;
            LY = Math.PI / 180 * PJ * R - LSb / 2;
        }
        else if(LX == 31) { //卵型曲线
            Pa = LSa * LSa / 24 / R - Math.pow(LSa, 4) / 2384 / Math.pow(R, 3);
            Qa = LSa / 2 - Math.pow(LSa, 3) / 240 / R / R;
            Tb = (R + Pa) * degreesTan(PJ / 2);
            Ta = Tb + Qa;
            LY = R * PJ * Math.PI / 180 - LSa / 2 - LSb;
        }
        else if(LX == 32) { //卵型曲线
            Pb = LSb * LSb / 24 / R - Math.pow(LSb, 4) / 2384 / Math.pow(R, 3);
            Qb = LSb / 2 - Math.pow(LSb, 3) / 240 / R / R;
            Taa = (R + Pb) * degreesTan(PJ / 2);
            Ta = (R + Pb) * degreesTan(PJ / 2) + DP / degreesTan(PJ);
            Tb = Taa + Qb - DP / degreesSin(PJ);
            LY = R * PJ * Math.PI / 180 - LSb / 2 - LSa;
        }
        d = Ta + Tb - LY - LSa - LSb;
	}
    //桩号计算
    JD = QJD + QSL - QD + JDDL; //交点桩号 = 前点桩号 + 前点SL - 前点D  + 断链差值
    //输出结果
    return {PJ: round(dms(PJ), 4),
    		ETa: Math.round(ETa),
    		Ta: round(Ta, 4),
    		Tb: round(Tb, 4),
	    	LY: round(LY, 4),
    		D: round(d, 4),
    		QFW: round(dms(AA * 180 / Math.PI), 4),
    		HFW: round(dms(HAA * 180 / Math.PI), 5),
    		JD: round(JD, 4),
    		SL: round(SL, 4)};
}

//更新交点的偏角和方位角
function recomputeCrossPoints() {
    window.top.database.onAfterQuery = function(records) {
		doRecomputeCrossPoints(records);
	};
    //获取交点列表
    var sql = "select * from road_cross_point" +
    		  " where road_cross_point.sectionId=" + window.top.sectionId +
    		  " order by sn, name"; 
    window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function doRecomputeCrossPoints(crossPoints) {
    if(crossPoints < 3) {
        return; //少于两个交点,不需要计算
    }
    var QJx, QJy, QJD, QSL, QD, HJx, HJy;
    for(var i = 1; i<crossPoints.length; i++) {
        if(i < crossPoints.length - 1) { //非EP
            HJx = crossPoints[i + 1].jx;
            HJy = crossPoints[i + 1].jy;
        }
        if(i == 1) { //BP
            //计算交点间距
            crossPoints[i].sl = crossPointDistance(toDouble(crossPoints[i].jx), toDouble(crossPoints[i].jy), HJx, HJy);
        }
        else if(i < crossPoints.length - 1) { //非EP
            //交点参数计算
            var curve = computeCrossPoint(toDouble(crossPoints[i].jx), toDouble(crossPoints[i].jy), QJx, QJy, HJx, HJy, toDouble(crossPoints[i].lsa), toDouble(crossPoints[i].lsb), toDouble(crossPoints[i].r), toDouble(crossPoints[i].dp), QJD, QSL, QD, toDouble(crossPoints[i].jddl), toDouble(crossPoints[i].lx));
            //更新记录
            crossPoints[i].pj = curve.PJ;
            crossPoints[i].ta = curve.Ta;
            crossPoints[i].tb = curve.Tb;
            crossPoints[i].d = curve.D;
            crossPoints[i].ly = curve.LY;
            crossPoints[i].qfw = curve.QFW;
            crossPoints[i].hfw = curve.HFW;
            crossPoints[i].eta = curve.ETa;
            crossPoints[i].sl = curve.SL;
            crossPoints[i].jd = curve.JD;
        }
        else { //EP
            crossPoints[i].jd = round(QJD + QSL - QD, 4);
        }
        window.top.database.updateRecord("road_cross_point", crossPoints[i]);
        QJx = crossPoints[i].jx;
        QJy = crossPoints[i].jy;
        QJD = crossPoints[i].jd; //前桩号
        QSL = crossPoints[i].sl; //前距离
        QD = crossPoints[i].d; //前D
	}
}