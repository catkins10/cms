<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="33%">
			<col>
			<col width="33%">
			<col>
			<col width="33%">
			<tr>
				<td class="tdtitle" valign="top">事项名称</td>
				<td class="tdcontent" colspan="5"><ext:field property="name"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">编号</td>
				<td class="tdcontent"><ext:field property="code"/></td>
				<td class="tdtitle" nowrap="nowrap">是否发布</td>
				<td class="tdcontent"><ext:field property="isPublic"/></td>
				<td class="tdtitle" nowrap="nowrap">事项类别</td>
				<td class="tdcontent"><ext:field property="itemType"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所属目录</td>
				<td class="tdcontent"><ext:field property="directoryName"/></td>
				<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
				<td class="tdcontent"><ext:field property="otherDirectoryNames"/></td>
				<td class="tdtitle" nowrap="nowrap">公共服务类别</td>
				<td class="tdcontent"><ext:field property="publicServiceType"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">处罚种类</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.punishType"/></td>
				<td class="tdtitle" nowrap="nowrap">裁量规则</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.discretionRule"/></td>
				<td class="tdtitle" nowrap="nowrap">监管等级</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.superviseLevel"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">办理机关</td>
				<td class="tdcontent"><ext:field property="serviceItemUnitNames"/></td>
				<td class="tdtitle" nowrap="nowrap">责任部门</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.responsibleDepartment"/></td>
				<td class="tdtitle" nowrap="nowrap">受理方式</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.acceptMode"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">受理地址</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.address"/></td>
				<td class="tdtitle" nowrap="nowrap">交通线路</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.traffic"/></td>
				<td class="tdtitle" nowrap="nowrap">办公时间</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.acceptTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">法定期限</td>
				<td class="tdcontent" colspan="5"><ext:field property="serviceItemGuide.legalLimit"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">承诺期限</td>
				<td class="tdcontent" colspan="5"><ext:field property="serviceItemGuide.timeLimit"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">联系人</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.transactor"/></td>
				<td class="tdtitle" nowrap="nowrap">联系电话</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.transactorTel"/></td>
				<td class="tdtitle" nowrap="nowrap">监督电话</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.complaintTel"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">状态查询</td>
				<td class="tdcontent"><ext:field property="querySupport"/></td>
				<td class="tdtitle" nowrap="nowrap">状态查询URL</td>
				<td class="tdcontent" colspan="3"><ext:field property="queryUrl"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">结果查询</td>
				<td class="tdcontent"><ext:field property="resultSupport"/></td>
				<td class="tdtitle" nowrap="nowrap">结果查询URL</td>
				<td class="tdcontent" colspan="3"><ext:field property="resultUrl"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">在线投诉</td>
				<td class="tdcontent"><ext:field property="complaintSupport"/></td>
				<td class="tdtitle" nowrap="nowrap">在线投诉流程</td>
				<td class="tdcontent" title="在线投诉流程"><ext:field property="complaintWorkflowName"/></td>
				<td class="tdtitle" nowrap="nowrap">在线投诉URL</td>
				<td class="tdcontent"><ext:field property="complaintUrl"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">在线咨询</td>
				<td class="tdcontent"><ext:field property="consultSupport"/></td>
				<td class="tdtitle" nowrap="nowrap">在线咨询流程</td>
				<td class="tdcontent" title="在线咨询流程"><ext:field property="consultWorkflowName"/>	</td>
				<td class="tdtitle" nowrap="nowrap">在线咨询URL</td>
				<td class="tdcontent"><ext:field property="consultUrl"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">公开形式</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.publicMode"/></td>
				<td class="tdtitle" nowrap="nowrap">公开范围</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.publicRange"/></td>
				<td class="tdtitle" nowrap="nowrap">公开时间</td>
				<td class="tdcontent"><ext:field property="serviceItemGuide.publicTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建人</td>
				<td class="tdcontent"><ext:field property="creator"/></td>
				<td class="tdtitle" nowrap="nowrap">创建时间</td>
				<td class="tdcontent" colspan="3"><ext:field property="created"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<%@ include file="serviceItemTabsEdit.jsp" %>
</ext:tab>