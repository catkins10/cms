<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newVideo() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/interview/admin/interviewVideo.shtml?id=<ext:write property="id"/>', 380, 160);
}
function openVideo(videoId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/interview/admin/interviewVideo.shtml?id=<ext:write property="id"/>&interviewVideo.id=' + videoId, 380, 160);
}
</script>
<div style="padding-bottom:8px">
	<input type="button" class="button" value="添加访谈视频" style="width:120px" onclick="newVideo()">
</div>
<center>
	<ext:iterate id="video" property="interviewVideos">
		<span style="cursor:pointer" onclick="openVideo('<ext:write name="video" property="id"/>')">
			<ext:videoPlayer width="400" height="300" nameVideoUrl="video" propertyVideoUrl="videoUrl"/>
			<br>
			<br>
			<ext:write name="video" property="subject"/>
			<br><br>
		</span>
	</ext:iterate>
</center>