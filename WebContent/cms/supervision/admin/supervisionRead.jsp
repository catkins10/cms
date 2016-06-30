<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" class="tdcontent" width="50%">
	<col>
	<col valign="middle" class="tdcontent" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="isPublic"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">被举报人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap" valign="top">被举报人主体类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unitCategory"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">其他举报人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="otherSupervision"/></td>
		<td class="tdtitle" nowrap="nowrap" valign="top">来源</td>
		<td class="tdcontent"><ext:field writeonly="true" property="source"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">主要涉嫌性质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="mainSuspected"/></td>
		<td class="tdtitle" nowrap="nowrap" valign="top">次要涉嫌性质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="secondarySuspected"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">内容</td>
		<td colspan="3" class="tdcontent"><pre><ext:field writeonly="true" property="content"/></pre></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator" /></td>
		<td class="tdtitle" nowrap="nowrap">邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMail"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorFax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorMobile" /></td>
		<td class="tdtitle" nowrap="nowrap">IP地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorIP" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorUnit" /></td>
		<td class="tdtitle" nowrap="nowrap">职业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorJob" /></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle">站点</td>
		<td class="tdcontent" colspan="3" class="tdcontent"><ext:field writeonly="true" property="siteName"/></td>
	</tr>
	<ext:notEmpty property="readers">
		<tr>
			<td class="tdtitle">访问者</td>
			<td class="tdcontent" colspan="3" class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
</table>