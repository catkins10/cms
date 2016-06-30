<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">是否校友企业</td>
		<td class="tdcontent" width="50%"><ext:field writeonly="true" property="collegeBuddy"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">公司性质</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
		<td class="tdtitle" nowrap="nowrap">所属行业</td>
		<td class="tdcontent"><ext:field writeonly="true" property="industryNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在地区</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">企业规模</td>
		<td class="tdcontent"><ext:field writeonly="true" property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业简介</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="introduction"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业资质</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="images"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field writeonly="true" property="representative"/></td>
		<td class="tdtitle" nowrap="nowrap">营业执照号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="licenseNo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位地址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
		<td class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="postalcode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系人职务</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanJob"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="linkmanTel"/></td>
		<td class="tdtitle" nowrap="nowrap">传真号码</td>
		<td class="tdcontent"><ext:field writeonly="true" property="fax"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><ext:field writeonly="true" property="email"/></td>
		<td class="tdtitle" nowrap="nowrap">网址</td>
		<td class="tdcontent"><ext:field writeonly="true" property="webSite"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审核时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approvalTime"/></td>
		<td class="tdtitle" nowrap="nowrap">审核人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approver"/></td>
	</tr>
	<ext:notEmpty property="failedReason">
		<tr>
			<td class="tdtitle" nowrap="nowrap">未通过原因</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="failedReason"/></td>
		</tr>
	</ext:notEmpty>
</table>