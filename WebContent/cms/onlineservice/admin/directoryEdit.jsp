<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<ext:notEqual value="0" property="id">
		<tr>
			<td class="tdtitle" nowrap="nowrap">上级目录</td>
			<td class="tdcontent">
				<ext:equal value="true" property="changeParentDirectoryDisabled">
					<ext:field writeonly="true" property="parentDirectoryName"/>
				</ext:equal>
				<ext:equal value="false" property="changeParentDirectoryDisabled">
					<ext:field property="parentDirectoryName"/>
				</ext:equal>
			</td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编号</td>
		<td class="tdcontent"><ext:field property="code"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">隶属站点</td>
		<td class="tdcontent"><ext:field property="siteName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field property="halt"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">在线受理流程</td>
		<td class="tdcontent"><ext:field property="acceptWorkflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">在线投诉流程</td>
		<td class="tdcontent"><ext:field property="complaintWorkflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">在线咨询流程</td>
		<td class="tdcontent"><ext:field property="consultWorkflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">常见问题发布流程</td>
		<td class="tdcontent" title="常见问题发布流程"><ext:field property="faqWorkflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">办理事项同步的网站栏目</td>
		<td class="tdcontent"><ext:field property="itemSynchSiteNames"/></td>
	</tr>
	<tr style="display: none">
		<td class="tdtitle" nowrap="nowrap">投诉同步的网站栏目</td>
		<td class="tdcontent"><ext:field property="complaintSynchSiteNames"/></td>
	</tr>
	<tr style="display: none">
		<td class="tdtitle" nowrap="nowrap">咨询同步的网站栏目</td>
		<td class="tdcontent"><ext:field property="consultSynchSiteNames"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>