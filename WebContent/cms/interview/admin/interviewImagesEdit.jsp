<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newImage() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/interview/admin/interviewImage.shtml?id=<ext:write property="id"/>', 380, 160);
}
function openImage(imageId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/interview/admin/interviewImage.shtml?id=<ext:write property="id"/>&interviewImage.id=' + imageId, 380, 160);
}
</script>
<div style="padding-bottom:8px">
	<input type="button" class="button" value="添加访谈图片" style="width:120px" onclick="newImage()">
</div>
<center>
	<ext:iterate id="image" property="interviewImages">
		<span style="cursor:pointer" onclick="openImage('<ext:write name="image" property="id"/>')">
			<img src="<ext:write name="image" property="imageUrl"/>"><br>
			<ext:write name="image" property="subject"/><br><br>
		</span>
	</ext:iterate>
</center>