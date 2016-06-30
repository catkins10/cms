<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<center>
	<ext:iterate id="video" property="interviewVideos">
		<ext:videoPlayer width="400" height="300" nameVideoUrl="video" propertyVideoUrl="videoUrl"/>
		<br>
		<br>
		<ext:write name="video" property="subject"/><br><br>
	</ext:iterate>
</center>