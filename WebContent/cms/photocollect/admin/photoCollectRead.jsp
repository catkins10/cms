<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">图片</td>
		<td colspan="3" class="tdcontent">
			<ext:iterateImage id="image" applicationName="cms/photocollect" imageType="image" propertyRecordId="id" breviaryWidth="800" breviaryHeight="600" breviaryId="breviary">
				<a href="<ext:write name="image" property="url"/>" target="_blank"><ext:img nameImageModel="breviary" border="0"/></a>
			</ext:iterateImage>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">可否下载</td>
		<td class="tdcontent"><ext:field writeonly="true" property="downloadable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交人姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator" /></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMobile"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP"/></td>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>