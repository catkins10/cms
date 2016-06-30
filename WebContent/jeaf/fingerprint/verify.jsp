<%@ page contentType="text/html; charset=UTF-8" %>

<script>
	/* 指纹校验子页面,由调用者实现下面的js函数
	function showCaptureImage(fingerImgSrc); //显示采集的指纹图像
	function submitVerifyFeature(encodedFeature); //提交校验样本数据
	function promptRecapture();  //当本数据不合格时,提醒用户重新输入
	*/
	function onVerifyComplete(encodedFeature) { //指纹仪事件:校验数据采集完成
		document.getElementsByName("fingerprintFeatureData")[0].value = encodedFeature;
		try{submitVerifyFeature(encodedFeature);}catch(e) {} //提交校验样本数据
	}
	function onCaptureComplete(sampleQuality, good, featuresNeeded) { //指纹仪事件:获取到一组指纹数据
		try {showCaptureImage(document.getElementById("FPCtl").GetSampleBitmapFile());}catch(e) {} //显示采集的指纹图像
		if(!good) {
			try {promptRecapture();}catch(e) {} //当本数据不合格时,提醒用户重新输入
		}
	}
</script>
<input type="hidden" name="fingerprintFeatureData">
<object classid="clsid:7564ABAA-07DC-45A9-8C95-BB83B055C99E" id="FPCtl" style="display:none"></object>
<script language="jscript" FOR="FPCtl" event="OnVerifyComplete(encodedFeature)">onVerifyComplete(encodedFeature)</script>    
<script language="jscript" FOR="FPCtl" event="OnCaptureComplete(sampleQuality, good, featuresNeeded)">onCaptureComplete(sampleQuality, good, featuresNeeded)</script>
<script>
	try {
		document.getElementById("FPCtl").StartVerify();
	}
	catch(e) {
		
	}
</script>