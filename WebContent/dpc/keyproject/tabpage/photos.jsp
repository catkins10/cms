<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newPhoto() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/photo.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openPhoto(photoId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/photo.shtml?id=<ext:write property="id"/>&photo.id=' + photoId, 500, 300);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加项目进展实景" style="width:150px" onclick="newPhoto()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">图片</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">拍摄时间</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
	</tr>
	<ext:iterate id="photo" property="photos">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("photo")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" align="center">
				<td class="tdcontent" onclick="openPhoto('<ext:write name="photo" property="id"/>')"><ext:write name="photo" property="photoYear"/></td>
				<td class="tdcontent" onclick="openPhoto('<ext:write name="photo" property="id"/>')"><ext:write name="photo" property="photoMonth"/></td>
				<td class="tdcontent">
					<ext:iterateImage id="image" applicationName="dpc/keyproject" imageType="images" nameRecordId="photo" propertyRecordId="id" breviaryWidth="300" breviaryHeight="300" breviaryId="breviary">
						<a href="<ext:write name="image" property="url"/>" target="_blank"><ext:img nameImageModel="breviary" border="0"/></a>
					</ext:iterateImage>
					<br>
					<ext:write name="photo" property="photoSubject"/>
				</td>
				<td class="tdcontent" onclick="openPhoto('<ext:write name="photo" property="id"/>')"><ext:write name="photo" property="shotTime" format="yyyy-MM-dd"/></td>
				<td class="tdcontent" onclick="openPhoto('<ext:write name="photo" property="id"/>')"><ext:write name="photo" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>