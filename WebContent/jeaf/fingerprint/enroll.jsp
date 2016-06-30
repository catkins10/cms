<%@ page contentType="text/html; charset=UTF-8" %>

<object classid="clsid:7564ABAA-07DC-45A9-8C95-BB83B055C99E" id="FPCtl" style="display:none"></object>
<script language="jscript" FOR="FPCtl" event="OnEnrollComplete(encodedTemplate)">onEnrollComplete(encodedTemplate)</script>    
<script language="jscript" FOR="FPCtl" event="OnCaptureComplete(sampleQuality, good, featuresNeeded)">onCaptureComplete(sampleQuality, good, featuresNeeded)</script>    	
<script>
	/* 指纹采集子页面,由调用者实现下面的js函数
	function afterEnrollComplete(); //指纹采集完成
	*/
	function startEnroll() { //启动采集
		document.getElementById("FPCtl").startEnroll();
	}
	function stopEnroll() { //停止采集
		document.getElementById("FPCtl").stopEnroll();
	}
	function redoEnroll() { //重新采集
		stopEnroll();
		startEnroll();
		if(document.getElementById("feature1")) {
			for(var i=1; i<=4; i++) {
				document.getElementById("feature" + i).innerHTML = '&nbsp;';
			}
		}
		document.getElementsByName("template")[0].value = "";
	}
	function onEnrollComplete(encodedTemplate) {
		document.getElementsByName("template")[0].value = encodedTemplate;
		try {
			afterEnrollComplete();
		}
		catch(e) {
		
		}
	}
	function onCaptureComplete(sampleQuality, good, featuresNeeded) {
		if(good && featuresNeeded<4) {
			if(document.getElementById("feature1")) {
				document.getElementById("feature" + (4-featuresNeeded)).innerHTML = '<img src="' + document.getElementById("FPCtl").GetSampleBitmapFile() + '" height="56px">';
				for(var i=0; i<featuresNeeded; i++) {
					document.getElementById("feature" + (4-i)).innerHTML = '&nbsp;';
				}
			}
		}
	}
	startEnroll();
</script>