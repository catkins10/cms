var curve;
function compute() {
	curve = null;
	curve = doCompute(toDouble(document.getElementsByName("driftAngle")[0].value), 
		  			  toDouble(document.getElementsByName("radius")[0].value), 
					  toDouble(document.getElementsByName("curveLength")[0].value), 
					  toDouble(document.getElementsByName("crossPointPile")[0].value), 
					  toDouble(document.getElementsByName("width")[0].value), 
					  toDouble(document.getElementsByName("visibilityRange")[0].value), 
					  toDouble(document.getElementsByName("pile")[0].value));
	if(curve) {
		document.getElementsByName("clearDistance")[0].value  = curve.横净距W;
	}
}
function showCurveIngredients() { //查看曲线要素
	if(!curve) {
		return;
	}
	var propertyNames = curve.propertyNames.split(",");
    var values = "";
    for(var i = 0; i<propertyNames.length-1; i++) {
    	values += (i==0 ? "" : "<br/>") + propertyNames[i] + "： " + eval("curve." + propertyNames[i]);
	}
	window.top.client.alert(values, '计算结果');
}
/**
 * 横净距计算
 * 偏角PJ="；JP
 * 路中线半径R="；R0
 * 缓和曲线长LS="；LS0
 * 交点桩号JD="；JD0
 * 路面宽度B="；B
 * 视距长度S="；S
 * 中桩桩号(L = "；LL")
 **/
function doCompute(JP, R0, LS0, JD0, B, S, LL) {
	if(R0==0) {
		alert("路中线半径不能为0");
		return;
	}
	R0 = [R0];
	LS0 = [LS0];
	JD0 = [JD0];
	LL = [LL];
    var XS = new Array(), YS = new Array();
    var PJ = [0];
    var T0 = [0];
    var ZH0 = [0];
    var HY0 = [0];
    var QZ0 = [0];
    var YH0 = [0];
    var HZ0 = [0];
    var R1 = [0];
    var LS = 0;
    var LS1 = [0];
    var T = 0;
    var JD1 = [0];
    var T1 = [0];
    var ZH1 = [0];
    var HY1 = [0];
    var QZ1 = [0];
    var YH1 = [0];
    var HZ1 = [0];
    var LQ = 0;
    var LZ = 0;
    var N = 0, I = 0;
    var L1 = [0];

    PJ[0] = deg(JP) / 180 * Math.PI;
    //求路中线曲线要素
    QXYS(T0, ZH0, HY0, QZ0, YH0, HZ0, JD0, R0, LS0, PJ);
    var curve = {propertyNames: "交点桩号JD,路线偏角PJ,路中线半径R,缓和曲线长LS,ZH,HY,QZ,YH,HZ",
    			 交点桩号JD: round(JD0[0], 3),
    			 路线偏角PJ: round(JP, 3),
				 路中线半径R: round(R0, 3),
				 缓和曲线长LS: round(LS0[0], 3),
				 ZH: round(ZH0[0], 3),
				 HY: round(HY0[0], 3),
				 QZ: round(QZ0[0], 3),
				 YH: round(YH0[0], 3),
				 HZ: round(HZ0[0], 3)};

    R1[0] = R0[0] - B / 2 + 1.5;
    T = T0[0] - (B / 2 - 1.5) * Math.tan(PJ[0] / 2);
    LS = LS0[0];
    //迭代法求行车轨迹线缓和曲线长
    while(Math.abs(LS - LS1[0]) >= 0.01) {
        LS1[0] = 2 * (T + Math.pow(LS, 3) / 240 / R1[0] / R1[0] - (R1[0] + Math.pow(LS, 2) / 24 / R1[0]) * Math.tan(PJ[0] / 2));
        LS = LS1[0];
    }
    JD1[0] = JD0[0] - (B / 2 - 1.5) * Math.tan(PJ[0] / 2);
    //求行车轨迹线曲线要素
    QXYS(T1, ZH1, HY1, QZ1, YH1, HZ1, JD1, R1, LS1, PJ);

	curve.propertyNames += ",行车轨迹线曲线交点桩号JD1,行车轨迹线曲线半径RS,行车轨迹线曲线路缓和曲线长LS1,行车轨迹线曲线ZH1,行车轨迹线曲线HY1,行车轨迹线曲线QZ1,行车轨迹线曲线YH1,行车轨迹线曲线HZ1";
    curve.行车轨迹线曲线交点桩号JD1 = round(JD1[0], 3);
    curve.行车轨迹线曲线半径RS = round(R1[0], 3);
    curve.行车轨迹线曲线路缓和曲线长LS1 = round(LS1[0], 3);
    curve.行车轨迹线曲线ZH1 = round(ZH1[0], 3);
    curve.行车轨迹线曲线HY1 = round(HY1[0], 3);
    curve.行车轨迹线曲线QZ1 = round(QZ1[0], 3);
    curve.行车轨迹线曲线YH1 = round(YH1[0], 3);
    curve.行车轨迹线曲线HZ1 = round(HZ1[0], 3);

    LQ = ZH1[0] - S;
    LZ = HZ1[0] + S;
    N = 1;
    L1[0] = LQ;

    var L2 = [0];
    var L3 = [0];
    var L4 = [0];
    var K = [0];
    var XS1 = [0];
    var YS1 = [0];
    var X11 = [0];
    var Y11 = [0];
    var X22 = [0];
    var Y22 = [0];
    var X33 = [0];
    var Y33 = [0];
    var X44 = [0];
    var Y44 = [0];

    //求视线端点坐标，相邻视线交点坐标XS,YS,形成视距曲线点集XS(N),YS(N)
    while(true) {
        LXY(X11, Y11, L1, ZH1, YH1, HZ1, R1, LS1, T1, PJ);
        L2[0] = L1[0] + S;
        L3[0] = L1[0] + 1;
        L4[0] = L3[0] + S;
        LXY(X22, Y22, L2, ZH1, YH1, HZ1, R1, LS1, T1, PJ);
        LXY(X33, Y33, L3, ZH1, YH1, HZ1, R1, LS1, T1, PJ);
        LXY(X44, Y44, L4, ZH1, YH1, HZ1, R1, LS1, T1, PJ);
        LDS(XS1, YS1, X11, Y11, X22, Y22, X33, Y33, X44, Y44);
        YS1[0] = YS1[0] + B / 2 - 1.5;
        XS[N] = XS1[0];
        YS[N] = YS1[0];
        N = N + 1;
        L1[0] = L1[0] + 1;
        if(isNaN(L4[0]) || isNaN(LZ) || L4[0] > LZ) {
        	break;
        }
    }
    var FI = 0;
    var L = 0;
    var XSI = [0];
    var YSI = [0];
    var XSII = [0];
    var YSII = [0];
    var PD1 = 0;
    var PD2 = 0;
    var HJJ = 0;
    var X0 = [0];
    var Y0 = [0];
    var X1 = [0];
    var Y1 = [0];
 
    if(LL[0] > LZ - 20 || LL[0] < LQ + 20) {
        alert("中桩超出曲线范围");
        return;
    }

    //求中桩坐标X0,Y0
    LXY(X0, Y0, LL, ZH0, YH0, HZ0, R0, LS0, T0, PJ);
    //求中桩横断面方向斜率K
    if(LL[0] > ZH0[0] && LL[0] < HZ0[0]) {
        L = LL[0] - ZH0[0];
        if(LL[0] > YH0[0]) {
        	L = HZ0[0] - LL[0];
        }
        if(L <= LS0[0]) {
            K[0] = -1 / Math.tan(L / 2 / R0[0]);
            if(LL[0] > YH0[0]) {
            	K[0] = -1 / Math.tan(PJ[0] - L / 2 / R0[0]);
            }
        }
        else {
            FI = (L - LS0[0]) / R0[0] + LS0[0] / 2 / R0[0];
            K[0] = -1 / Math.tan(FI);
        }
    }
    else {
        if(LL[0] <= ZH0[0]) {
        	K[0] = 1.0E+30;
        }
        if(LL[0] >= HZ0[0]) {
        	K[0] = -1 / Math.tan(PJ[0]);
        }
    }
    //求横断面方向线与视距曲线交点X1,Y1
    I = 1
    while(true) {
        XSI[0] = XS[I];
        YSI[0] = YS[I];
        XSII[0] = XS[I + 1];
        YSII[0] = YS[I + 1];
        DXS(X1, Y1, X0, Y0, K, XSI, YSI, XSII, YSII);
        PD1 = (X1[0] - XS[I + 1]) * (X1[0] - XS[I]);
        PD2 = (Y1[0] - YS[I + 1]) * (Y1[0] - YS[I]);
        if(PD1 <= 0 && PD2 <= 0) {
        	break;
        }
        I = I + 1;
    }
    HJJ = Math.sqrt(Math.pow(X1[0] - X0[0], 2) + Math.pow(Y1[0] - Y0[0], 2)) - (B / 2 - 1.5);
    //计算横净距HJJ
    curve.propertyNames += ",中桩桩号L,横净距W";
    curve.中桩桩号L = round(LL[0], 3);
    curve.横净距W = round(HJJ, 3);
    return curve;
}

function DXS(XDXS, YDXS, X3, Y3, K2, X1, Y1, X2, Y2) {
    var DX = 0;
    var K1 = 0;
    DX = X2[0] - X1[0];
    if(DX == 0) {
        XDXS[0] = X1[0];
        YDXS[0] = Y3[0] + K2[0] * (XDXS[0] - X3[0]);
    }
    else {
        K1 = (Y2[0] - Y1[0]) / (X2[0] - X1[0]);
        if(K2[0] > 1.0E+29) {
            XDXS[0] = X3[0];
            YDXS[0] = Y1[0] + K1 * (XDXS[0] - X1[0]);
        }
        else {
            XDXS[0] = (Y3[0] - Y1[0] + K1 * X1[0] - X3[0] * K2[0]) / (K1 - K2[0]);
            YDXS[0] = Y3[0] + K2[0] * (XDXS[0] - X3[0]);
        }
    }
}

function LDS(XLDS, YLDS, X1, Y1, X2, Y2, X3, Y3, X4, Y4) {
    var K3 = [0];
    if(X2[0] == X1[0]) {
    	K3[0] = 1.0E+30;
    }
    else {
    	K3[0] = (Y2[0] - Y1[0]) / (X2[0] - X1[0]);
    }
    DXS(XLDS, YLDS, X1, Y1, K3, X3, Y3, X4, Y4);
}

function LXY(X, Y, PL, ZH, YH, HZ, R, LS, T, PJ) {
    var P = 0;
    var Q = 0;
    var B = 0;
    var L = 0;
    var X1 = 0;
    var Y1 = 0;
    var FI = 0;

    P = LS[0] * LS[0] / 24 / R[0] - Math.pow(LS[0], 4) / 2384 / Math.pow(R[0], 3);
    Q = LS[0] / 2 - Math.pow(LS[0], 3) / 240 / R[0] / R[0];
    B = LS[0] / 2 / R[0];
    L = PL[0] - ZH[0];
    if(PL[0] > YH[0]) {
    	L = HZ[0] - PL[0];
    }
    if(L < 0) {
        X[0] = L;
        Y[0] = 0;
        if(PL[0] > YH[0]) {
            X1 = X[0] - T[0];
            Y1 = Y[0];
            X[0] = X1 * Math.cos(PJ[0]) + Y1 * Math.sin(PJ[0]);
            Y[0] = Y1 * Math.cos(PJ[0]) - X1 * Math.sin(PJ[0]);
            X[0] = T[0] - X[0];
        }
    }
    else {
        if(L <= LS[0]) {
            X[0] = L - Math.pow(L, 5) / 40 / R[0] / R[0] / LS[0] / LS[0];
            Y[0] = Math.pow(L, 3) / 6 / R[0] / LS[0] - Math.pow(L, 7) / 336 / Math.pow(R[0], 3) / Math.pow(LS[0], 3);
            if(PL[0] > YH[0]) {
                X1 = X[0] - T[0];
                Y1 = Y[0];
                X[0] = X1 * Math.cos(PJ[0]) + Y1 * Math.sin(PJ[0]);
                Y[0] = Y1 * Math.cos(PJ[0]) - X1 * Math.sin(PJ[0]);
                X[0] = T[0] - X[0];
            }
        }
        else {
            FI = ((L - LS[0]) / R[0] + LS[0] / 2 / R[0]);
            X[0] = Q + R[0] * Math.sin(FI);
            Y[0] = P + R[0] * (1 - Math.cos(FI));
        }
    }
}

function QXYS(T, ZH, HY, QZ, YH, HZ, JD, R, LS, PJ1) {
    var P = 0;
    var Q = 0;
    var B = 0;
    var E = 0;
    var LY = 0;
    var L = 0;

    P = LS[0] * LS[0] / 24 / R[0] - Math.pow(LS[0], 4) / 2384 / Math.pow(R[0], 3);
    Q = LS[0] / 2 - Math.pow(LS[0], 3) / 240 / R[0] / R[0];
    B = LS[0] / 2 / R[0];
    T[0] = (R[0] + P) * Math.tan(PJ1[0] / 2) + Q;
    E = (R[0] + P) / Math.cos(PJ1[0] / 2) - R[0];
    LY = (PJ1[0] - 2 * B) * R[0];
    L = LY + 2 * LS[0];
    ZH[0] = JD[0] - T[0];
    HY[0] = ZH[0] + LS[0];
    QZ[0] = HY[0] + LY / 2;
    YH[0] = HY[0] + LY;
    HZ[0] = YH[0] + LS[0];
}