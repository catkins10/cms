function compute() {
	doCompute(toDouble(document.getElementsByName("pile")[0].value), 
			  toDouble(document.getElementsByName("hz")[0].value), 
			  toDouble(document.getElementsByName("widen")[0].value), 
			  toDouble(document.getElementsByName("slope")[0].value), 
			  toDouble(document.getElementsByName("length")[0].value), 
			  toDouble(document.getElementsByName("b")[0].value), 
			  toDouble(document.getElementsByName("a")[0].value), 
			  toDouble(document.getElementsByName("ic")[0].value), 
			  toDouble(document.getElementsByName("ij")[0].value), 
			  (document.getElementsByName("rotateMode")[0].checked ? 1 : 2), 
			  (document.getElementsByName("widenMode")[0].value=="直线比例加宽" ? 1 : 2), 
			  toDouble(document.getElementsByName("l")[0].value));
}
/**
 * 超高加宽计算程序
 * 直缓点桩号 ZH=″;ZH
 * 缓点直桩号 HZ=″;HZ
 * 弯道最大加宽 W=″;W: INPUT ″弯道超高坡度 iy%=″; IY: INPUT ″超高缓和段长Lc=″; LC
 * 路面宽度 b=″; B: INPUT ″路肩宽度 a=″;A: INPUT ″路拱横坡 ic%=″;IC
 * 路肩横坡ij%=″;IJ
 * 超高方式为：（1）内边轴旋转  （2）中轴旋转″;CGFS$
 * 加宽方式为：（1）直线比例加宽 （2）高次抛物线加宽″;JKFS$
 * L弯道内中桩桩号
 **/
function doCompute(ZH, HZ, W, IY, LC, B, A, IC, IJ, CGFS, JKFS, L) {
    IY = IY / 100;
    IC = IC / 100;
    IJ = IJ / 100;
    if(L < ZH || L > HZ) {
        alert("不在区间");
        return;
    }
    var X = 0, WX = 0, K = 0, X0 = 0, HCW = 0, HCZ = 0, HCL = 0;
    X = L - ZH;
    if(L > HZ - LC) {
    	X = HZ - L;
    }
    //加宽计算
    if(JKFS == 1) {
        WX = W;
        if(X < LC) {
        	WX = X / LC * W;
        }
    }
    else {
        WX = W;
        if(X < LC) {
            K = X / LC;
            WX = (4 * Math.pow(K, 3) - 3 * Math.pow(K, 4)) * W;
        }
    }
    //超高值计算
    if(CGFS == 1) {
        X0 = IC / IY * LC;
        if(X > LC) {
            HCW = A * IJ + (A + B) * IY;
            HCZ = A * IJ + B / 2 * IY;
            HCL = A * IJ - (A + WX) * IY;
        }
        else {
            HCW = A * (IJ - IC) + (A * IC + (A + B) * IY) * X / LC;
            HCZ = A * IJ + B / 2 * IC;
            if(X > X0) {
            	HCZ = A * IJ + B / 2 * X / LC * IY;
            }
            HCL = A * IJ - (A + WX) * IC;
            if(X > X0) {
            	HCL = A * IJ - (A + WX) * X / LC * IY;
            }
        }
    }
    else {
        X0 = 2 * IC / (IC + IY) * LC;
        if(X > LC) {
            HCW = A * IJ + B / 2 * IC + (A + B / 2) * IY;
            HCZ = A * IJ + B / 2 * IC;
            HCL = A * IJ + B / 2 * IC - (A + B / 2 + WX) * IY;
        }
        else {
            HCW = A * (IJ - IC) + (A + B / 2) * (IC + IY) * X / LC;
            HCZ = A * IJ + B / 2 * IC;
            HCL = A * IJ - (A + WX) * IC;
            if(X > X0) {
            	HCL = A * IJ + B / 2 * IC - (A + B / 2 + WX) * X / LC * IY;
            }
        }
    }
    var values = "中桩桩号： " + round(L, 3) + "<br/>" +
    			 "加宽值： " + round(WX, 3) + "<br/>" +
				 "路基外侧超高值： " + (isNaN(HCW) ? "无效" : round(HCW, 3)) + "<br/>" +
				 "路中线超高值： " + round(HCZ, 3) + "<br/>" +
				 "路基内侧超高值： " + round(HCL, 3);
	window.top.client.alert(values, '计算结果');
}