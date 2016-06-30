<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:write property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">内容</td>
		<td class="tdcontent" colspan="3">
			<pre><ext:write property="content"/></pre>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交人姓名</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:write property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:write property="creatorMobile"/></td>
		<td class="tdtitle" nowrap="nowrap">工作单位</td>
		<td class="tdcontent"><ext:write property="creatorUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系地址</td>
		<td class="tdcontent" colspan="3"><ext:write property="creatorAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle">站点</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="siteName"/></td>
	</tr>
	<ext:notEmpty property="readers">
		<tr>
			<td class="tdtitle">访问者</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td class="tdcontent">
			<ext:equal property="isPublic" value="1">允许</ext:equal>
			<ext:notEqual property="isPublic" value="1">禁止</ext:notEqual>
		</td>
	</tr>
</table>