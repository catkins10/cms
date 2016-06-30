<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	request.setAttribute("colspan", "3");
%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
		<td class="tdtitle" nowrap="nowrap">全称</td>
		<td class="tdcontent"><ext:field property="fullName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上级机构</td>
		<td class="tdcontent">
			<ext:equal value="true" property="changeParentDirectoryDisabled">
				<ext:field writeonly="true" property="parentDirectoryName"/>
			</ext:equal>
			<ext:equal value="false" property="changeParentDirectoryDisabled">
				<ext:field property="parentDirectoryName"/>
			</ext:equal>
		</td>
		<td class="tdtitle" nowrap="nowrap">单位编码</td>
		<td class="tdcontent">
			<ext:equal value="true" property="changeParentDirectoryDisabled">
				<ext:field writeonly="true" property="unitCode"/>
			</ext:equal>
			<ext:equal value="false" property="changeParentDirectoryDisabled">
				<ext:field property="unitCode"/>
			</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主要职责</td>
		<td class="tdcontent"><ext:field property="responsibility"/></td>
		<td class="tdtitle" nowrap="nowrap">单位地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">交通线路</td>
		<td class="tdcontent"><ext:field property="way"/></td>
		<td class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:field property="postcode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">网站</td>
		<td class="tdcontent"><ext:field property="webSite"/></td>
		<td class="tdtitle" nowrap="nowrap">EMAIL</td>
		<td class="tdcontent"><ext:field property="email"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">二级单位</td>
		<td class="tdcontent"><ext:field property="secondaryUnitNames"/></td>
		<td class="tdtitle" nowrap="nowrap">下级单位</td>
		<td class="tdcontent"><ext:field property="subordinateUnitNames"/></td>
	</tr>
	<jsp:include page="orgLeader.jsp" />
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>