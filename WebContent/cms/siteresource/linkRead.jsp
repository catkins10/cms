<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">标题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">链接</td>
		<td class="tdcontent"><a href="<ext:field writeonly="true" property="link"/>" target="_blank"><ext:field writeonly="true" property="link"/></a></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">链接图片</td>
		<td class="tdcontent">
			<ext:notEmpty property="firstImageName">
				<ext:iterateImage id="image" propertyRecordId="id" applicationName="cms/siteresource" imageType="images">
					<ext:equal property="firstImageName" nameCompare="image" propertyCompare="name">
						<img id="linkImage" src="<ext:write name="image" property="url"/>">
					</ext:equal>
				</ext:iterateImage>
			</ext:notEmpty>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属栏目</td>
		<td class="tdcontent"><ext:field writeonly="true" property="columnFullName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属其它栏目</td>
		<td class="tdcontent"><ext:field writeonly="true" property="otherColumnNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent">
			<ext:field writeonly="true" property="issueTime"/>
			<ext:notEmpty property="issueEndTime">&nbsp;至&nbsp;<ext:field writeonly="true" property="issueEndTime"/></ext:notEmpty>
		</td>
	</tr>
	<ext:notEmpty property="readers.visitorNames">
		<tr>
			<td class="tdtitle" nowrap="nowrap">访问者</td>
			<td class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
</table>