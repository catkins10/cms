<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">办事事项</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="itemName"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle">咨询主题</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle">编号</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle">详细内容</td>
		<td colspan="3" class="tdcontent"><pre><ext:field writeonly="true" property="content"/></pre></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">咨询人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">咨询时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">证件名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorCertificateName"/></td>
		<td class="tdtitle" nowrap="nowrap">证件号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIdentityCard"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">是否允许公开</td>
		<td class="tdcontent">
			<ext:equal property="isPublic" value="1">允许</ext:equal>
			<ext:notEqual property="isPublic" value="1">禁止</ext:notEqual>
		</td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
		<td class="tdtitle" nowrap="nowrap">职业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorJob"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorAddress"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">邮编</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorPostalcode"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">咨询人IP</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
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