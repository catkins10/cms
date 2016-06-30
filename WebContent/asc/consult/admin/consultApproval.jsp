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
		<td class="tdtitle" nowrap="nowrap" valign="top">详细内容</td>
		<td colspan="3" class="tdcontent"><pre><ext:field writeonly="true" property="content"/></pre></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理事项</td>
		<td class="tdcontent"><ext:field writeonly="true" property="itemName"/></td>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isPublic"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">提交人姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMobile" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorUnit" /></td>
		<td class="tdtitle" nowrap="nowrap">职业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorJob" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP" /></td>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
</table>