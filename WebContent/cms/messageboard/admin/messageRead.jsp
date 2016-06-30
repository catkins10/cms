<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">内容</td>
		<td class="tdcontent" colspan="3">
			<pre><ext:field writeonly="true" property="content"/></pre>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交人姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMobile"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">工作单位</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="creatorUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">提交人IP地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isPublic"/></td>
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
</table>