<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
	function setLinkImage() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/selectLinkAttachment.shtml?id=<ext:write property="id"/>&siteId=<ext:write property="siteId"/>&attachmentSelector.scriptRunAfterSelect=afterSelectImage("{URL}")&attachmentSelector.type=images', 640, 400);
	}
	function afterSelectImage(url) {
		//设置链接图片
		var linkImage = document.getElementById('linkImage');
		if(!linkImage) {
			try {
				linkImage = document.createElement('<img id="linkImage">');
			}
			catch(e) {
				linkImage = document.createElement('img');
				linkImage.id = 'linkImage';
			}
			document.getElementById("tdLinkImage").insertBefore(linkImage, document.getElementById("tdLinkImage").childNodes[0]);
		}
		linkImage.src = url;
		linkImage.style.display = '';
		//设置图片名称
		url = StringUtils.utf8Decode(url.substring(url.lastIndexOf('/') + 1));
		document.getElementsByName('firstImageName')[0].value = url;
		document.getElementsByName('imageCount')[0].value = 1;
	}
</script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">链接</td>
		<td class="tdcontent"><ext:field property="link"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">链接图片</td>
		<td id="tdLinkImage" class="tdcontent">
			<html:hidden property="imageCount"/>
			<html:hidden property="firstImageName"/>
			<ext:notEmpty property="firstImageName">
				<ext:iterateImage id="image" propertyRecordId="id" applicationName="cms/siteresource" imageType="images">
					<ext:equal property="firstImageName" nameCompare="image" propertyCompare="name">
						<img id="linkImage" src="<ext:write name="image" property="url"/>">
					</ext:equal>
				</ext:iterateImage>
			</ext:notEmpty>
			<input type="button" class="button" onclick="setLinkImage()" value="设置">
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属栏目</td>
		<td class="tdcontent"><ext:field property="columnFullName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属其它栏目</td>
		<td class="tdcontent"><ext:field property="otherColumnNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
	</tr>
	<ext:notEmpty property="readers.visitorNames">
		<tr>
			<td class="tdtitle" nowrap="nowrap">访问者</td>
			<td class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
</table>