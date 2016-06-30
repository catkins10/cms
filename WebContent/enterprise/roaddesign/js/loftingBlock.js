var loftingBlock, nextBlock;
window.onload = function() {
	document.getElementsByName("loftingResult")[0].style.display = 'none';
};
function compute() {
	if(!window.top.baseStation) { //判断基站是否已经选择
        alert("请先选择基站");
    	return;
    }
    window.top.database.onAfterQuery = function(records) {
		doCompute(records);
	};
    //获取积木法资料
    var sql = "select * from road_block" +
			  " where sectionId=" + window.top.sectionId +
			  " order by cast(lc as real)";
    window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function doCompute(blocks) {
	if(blocks.length<3) {
   		alert("少于两个点,不允许放样");
   		return;
   	}
   	document.getElementsByName("x")[0].value = "";
    document.getElementsByName("y")[0].value = "";
    document.getElementsByName("z")[0].value = "";
    document.getElementsByName("horizontalAngle")[0].value = "";
    document.getElementsByName("horizontalDistance")[0].value = "";
    
    var pile = toDouble(document.getElementsByName("pile")[0].value);
    loftingBlock = blocks[1];
    if(pile < loftingBlock.lc) {
        alert("不在区间")
        return;
    }
    for(var i = 2; i < blocks.length; i++) {
        nextBlock = blocks[i];
        if(pile < toDouble(nextBlock.lc) || (pile == toDouble(nextBlock.lc) && i == blocks.length - 1)) {
            break;
        }
        else if(i == blocks.length - 1) { //终点
            alert("不在区间")
            return;
        }
        loftingBlock = nextBlock;
    }

    //开始放样
    var leftOrRight = document.getElementsByName("leftOrRight")[0].checked ? -1 : 1;
    var curve = blockLofting(toDouble(loftingBlock.ra), toDouble(loftingBlock.eta), toDouble(loftingBlock.x), toDouble(loftingBlock.y), toDouble(loftingBlock.lc), toDouble(loftingBlock.fw), toDouble(nextBlock.ra), toDouble(nextBlock.lc), pile, Math.abs(toDouble(document.getElementsByName("edgeDistance")[0].value)) * leftOrRight, toDouble(document.getElementsByName("actualHorizontalDistance")[0].value), window.top.baseStation.backHeight, window.top.baseStation.backMirrorHeight, toDouble(document.getElementsByName("height")[0].value), toDouble(document.getElementsByName("mirrorHeight")[0].value), window.top.baseStation.surveyStationX, window.top.baseStation.surveyStationY, window.top.baseStation.backsightSurveyStationX, window.top.baseStation.backsightSurveyStationY, window.top.baseStation.backsightSurveyStationZ);

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
//积木法放样
function blockLofting(Ra, ETa, Xa, Ya, LCa, FWa, Rb, LCb, LC, W, L, HH, HJG, QH, QJG, cx, cy, X0, Y0, Z0) {
    var FW1, FW2;

    //计算任何桩号LC的坐标
    FWa = deg(FWa) * Math.PI / 180;

    if(Ra == Rb) {
        if(Ra == 0) {
            X = Xa + (LC - LCa) * Math.cos(FWa);
            Y = Ya + (LC - LCa) * Math.sin(FWa);
            FWc = FWa
        }
        else {
            MA = (LC - LCa) / Ra;
            IA = Ra * Math.sin(MA);
            Ja = ETa * (Ra * (1 - Math.cos(MA)));
            X = Xa + (IA) * Math.cos(FWa) - Ja * Math.sin(FWa);
            Y = Ya + (IA) * Math.sin(FWa) + Ja * Math.cos(FWa);
            FWc = FWa + ETa * MA;
        }
    }
    else {
        if(Ra != 0) {
            RAA = 1 / Ra;
        }
        else {
            RAA = 0;
        }
        if(Rb != 0) {
            RBB = 1 / Rb;
        }
        else {
            RBB = 0;
        }
        X = ETa * (RBB - RAA) / (LCb - LCa);
        T = Math.abs(LC - LCa);
        i = X * T;
        FWc = FWa + (i + 2 * ETa * RAA) * T / 2;
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
        X = Xa + T * (Math.cos(FWa) + 4 * U + 2 * WW + Math.cos(FWc)) / 72;
        Y = Ya + T * (Math.sin(FWa) + 4 * V + 2 * Z + Math.sin(FWc)) / 72;
    }

    if(FWc > 2 * Math.PI) {
        FWc = FWc - 2 * Math.PI;
    }
    else if(FWc < 0) {
        FWc = 2 * Math.PI - FWc;
    }

    //INPUT "W=:";W 计算中桩坐标W=0  左边桩-？，右边桩+？
    //计算Z
    J = 1;
    if(W < 0) {
    	J = -J;
    }
    X = X + J * W * Math.cos(FWc + J * Math.PI / 2);
    Y = Y + J * W * Math.sin(FWc + J * Math.PI / 2);
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
function switchPile(nextPile) { //切换桩号
    var pile = toDouble(document.getElementsByName("pile")[0].value);
    var newValue = Math.floor(pile / 10) * 10 + (nextPile ? 20 : -20);
    if(nextPile && nextBlock) {
        if(pile < toDouble(nextBlock.lc) && newValue > toDouble(nextBlock.lc)) {
            newValue = nextBlock.lc;
        }
    }
    else if(!nextPile && loftingBlock) {
        if(pile > toDouble(loftingBlock.lc) && newValue < toDouble(loftingBlock.lc)) {
            newValue = loftingBlock.lc;
        }
    }
    document.getElementsByName("pile")[0].value = newValue;
    document.getElementsByName("height")[0].value = "";
    document.getElementsByName("mirrorHeight")[0].value = "1.3";
    compute(); //重新计算
}