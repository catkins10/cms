<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newVideo() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/video.shtml?id=<ext:write property="id"/>', 640, 400);
}
function openVideo(videoId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/video.shtml?id=<ext:write property="id"/>&video.id=' + videoId, 640, 400);
}
function adjustVideoPriority() {
	DialogUtils.adjustPriority('cms/sitemanage', 'admin/adjustVideoPriority', '调整优先级', '80%', '80%', 'resourceId=<ext:write property="id"/>');
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加视频" onclick="newVideo()">
		<input type="button" class="button" value="调整优先级" onclick="adjustVideoPriority()">
	</div>
</ext:equal>
<ext:iterate id="video" indexId="videoIndex" property="resourceVideos">
	<span style="display:inline-block; margin-right: 8px; margin-bottom: 8px; text-align:center; background-color:#ffffff; border:#cccccc 1px solid; padding: 3px 3px 3px 3px">
		<ext:iterateVideo id="videoFile" applicationName="cms/siteresource" videoType="videos" propertyRecordId="id">
			<ext:equal name="videoFile" property="name" nameCompare="video" propertyCompare="name">
				<ext:videoPlayer width="280" height="210" nameVideoUrl="videoFile" propertyVideoUrl="url" hideControlBar="true"/>
			</ext:equal>
		</ext:iterateVideo>
		<br>
		<span style="cursor:pointer; display:inline-block; line-height:23px; width: 280px; height: 23px; overflow:hidden;" onclick="openVideo('<ext:write name="video" property="id"/>')">
			<ext:write name="video" property="subject"/>
		</span>
	</span>
</ext:iterate>