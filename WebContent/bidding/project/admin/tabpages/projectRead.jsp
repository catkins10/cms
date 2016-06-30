<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectName"/></td>
		<td class="tdtitle" nowrap="nowrap">工程类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectCategory"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectProcedure"/></td>
		<td class="tdtitle" nowrap="nowrap">所属地区</td>
		<td class="tdcontent"><ext:field writeonly="true" property="city"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="biddingMode"/></td>
		<td class="tdtitle" nowrap="nowrap">是否委托招标代理</td>
		<td class="tdcontent"><ext:field writeonly="true" property="agentEnable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资格审查方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="approvalMode"/></td>
		<td class="tdtitle" nowrap="nowrap">代理产生方式</td>
		<td class="tdcontent"><ext:field writeonly="true" property="agentMode"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设单位</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="owner"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目人员需求</td>
		<td class="tdcontent" colspan="3">
			<ext:iterate id="jobholder" property="jobholders">
				<span style="display:inline-block; padding-right:8px">
					<ext:write name="jobholder" property="jobholderCategory"/><ext:notEmpty name="jobholder" property="qualifications">(<ext:write name="jobholder" property="qualifications"/>)</ext:notEmpty>:<ext:write name="jobholder" property="jobholderNumber"/>人
				</span>
			</ext:iterate>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerLinkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field writeonly="true" property="ownerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">建设规模</td>
		<td class="tdcontent"><ext:field writeonly="true" property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建筑面积</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/>㎡</td>
		<td class="tdtitle" nowrap="nowrap">招标编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="projectNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金开户行</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pledgeBank"/></td>
		<td class="tdtitle" nowrap="nowrap">保证金帐户名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pledgeAccountName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保证金帐号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pledgeAccount"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>