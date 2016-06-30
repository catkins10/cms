<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">货源(车源)</td>
		<td class="tdcontent">
			<ext:notEqual value="0" property="supplyId">
				<a href="javascript:PageUtils.editrecord('logistics/supply', 'admin/supply', '<ext:write property="supplyId"/>', 'mode=fullscreen')">
					<ext:field writeonly="true" property="supplyDescription"/>
				</a>
			</ext:notEqual>
			<ext:notEqual value="0" property="vehicleSupplyId">
				<a href="javascript:PageUtils.editrecord('logistics/vehicle', 'admin/vehicleSupply', '<ext:write property="vehicleSupplyId"/>', 'mode=fullscreen')">
					<ext:field writeonly="true" property="supplyDescription"/>
				</a>
			</ext:notEqual>
		</td>
		<td class="tdtitle" nowrap="nowrap">被投诉次数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="supplyComplaintTimes"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">被投诉公司(个人)</td>
		<td class="tdcontent"><ext:field writeonly="true" property="userName"/></td>
		<td class="tdtitle" nowrap="nowrap">被投诉次数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="userComplaintTimes"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否允许公开</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="isPublic"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
		<td class="tdtitle" nowrap="nowrap">投诉时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">详细说明</td>
		<td colspan="3" class="tdcontent"><pre><ext:field writeonly="true" property="content"/></pre></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">投诉人姓名</td>
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
		<td class="tdtitle" nowrap="nowrap">处罚结果</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="sanctionResult"/></td>
	</tr>
</table>