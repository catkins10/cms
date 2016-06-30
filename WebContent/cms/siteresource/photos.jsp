<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newPhoto() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/photo.shtml?id=<ext:write property="id"/>', 640, 400);
}
function openPhoto(photoId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/photo.shtml?id=<ext:write property="id"/>&photo.id=' + photoId, 640, 400);
}
function adjustPhotoPriority() {
	DialogUtils.adjustPriority('cms/sitemanage', 'admin/adjustPhotoPriority', '调整优先级', '80%', '80%', 'resourceId=<ext:write property="id"/>');
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加图片" onclick="newPhoto()">
		<input type="button" class="button" value="调整优先级" onclick="adjustPhotoPriority()">
	</div>
</ext:equal>
<ext:iterate id="photo" indexId="photoIndex" property="resourcePhotos">
	<span style="cursor:pointer; display:inline-block; margin-right: 8px; margin-bottom: 8px; text-align:center; background-color:#ffffff; border:#cccccc 1px solid; padding: 3px 3px 3px 3px" onclick="openPhoto('<ext:write name="photo" property="id"/>')">
		<ext:iterateImage id="image" breviaryId="breviary" breviaryWidth="280" breviaryHeight="210" applicationName="cms/siteresource" imageType="images" propertyRecordId="id">
			<ext:equal name="image" property="name" nameCompare="photo" propertyCompare="name">
				<ext:img nameImageModel="breviary" align="absmiddle"/>
			</ext:equal>
		</ext:iterateImage>
		<br>
		<span style="display:inline-block; line-height:23px; width: 280px; height: 23px; overflow:hidden;">
			<ext:write name="photo" property="subject"/>
		</span>
	</span>
</ext:iterate>