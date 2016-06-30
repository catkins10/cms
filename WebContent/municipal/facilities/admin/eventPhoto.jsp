<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

案件照片：<br>
<ext:iterateImage applicationName="municipal/facilities" propertyRecordId="id" imageType="images" id="image" breviaryWidth="300" breviaryHeight="300" breviaryId="breviary">
	<ext:img nameImageModel="breviary"/>
</ext:iterateImage>
<br><br>
修复后照片：<br>
<ext:iterateImage applicationName="municipal/facilities" propertyRecordId="id" imageType="processImages" id="image" breviaryWidth="300" breviaryHeight="300" breviaryId="processBreviary">
	<ext:img nameImageModel="processBreviary"/>
</ext:iterateImage>