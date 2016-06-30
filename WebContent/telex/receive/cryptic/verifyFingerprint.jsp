<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/submitFingerprint">
	<jsp:include page="/jeaf/fingerprint/verify.jsp" />
	<script>
		function showCaptureImage(fingerImgSrc) { //显示采集的指纹图像
			
		}
		function submitVerifyFeature(encodedFeature) { //提交校验样本数据
			document.forms[0].submit();
		}
		function promptRecapture() { //当本数据不合格时,提醒用户重新输入
			alert("指纹数据不合格,请重新采集。");
		}
	</script>
	<img width="100%" height="230" src="<%=request.getContextPath()%>/jeaf/fingerprint/images/fingerprint.jpg">
</ext:form>