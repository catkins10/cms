<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%	
	com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam projectTeam = (com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam)request.getAttribute("projectTeam");
%>
<ext:tab name="projectTeamTabs">
	<ext:tabBody tabId="<%="teamBasic" + projectTeam.getId()%>">
		<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col width="100%">
			<tr>
				<td class="tdtitle" valign="top">工作内容</td>
				<td class="tdcontent"><ext:write name="projectTeam" property="workContent"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">项目组负责人</td>
				<td class="tdcontent"><ext:write name="projectTeam" property="projectTeamManagerNames"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">项目组成员</td>
				<td class="tdcontent"><ext:write name="projectTeam" property="projectTeamMemberNames"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">计划完成时间</td>
				<td class="tdcontent"><ext:write name="projectTeam" property="expectingDate" format="yyyy-MM-dd"/></td>
			</tr>
			<ext:notEmpty name="projectTeam" property="completionDate">
				<tr>
					<td class="tdtitle" nowrap="nowrap">完成时间</td>
					<td class="tdcontent"><ext:write name="projectTeam" property="completionDate" format="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td class="tdtitle" valign="top">完成情况</td>
					<td class="tdcontent"><ext:write name="projectTeam" property="completionDescription"/></td>
				</tr>
				<tr>
					<td class="tdtitle" valign="top">设计质量</td>
					<td class="tdcontent"><ext:write name="projectTeam" property="designQuality"/></td>
				</tr>
			</ext:notEmpty>
			<ext:equal value="true" name="projectTeam" property="manager">
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目进展</td>
					<td class="tdcontent"><ext:field property="projectProgress"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">附件</td>
					<td class="tdcontent"><ext:field property="projectTeam.attachments"/></td>
				</tr>
			</ext:equal>
			<ext:equal value="false" name="projectTeam" property="manager">
				<tr>
					<td class="tdtitle" nowrap="nowrap">附件</td>
					<td class="tdcontent">
						<ext:iterateAttachment id="attachment" indexId="attachmentIndex" nameRecordId="projectTeam" propertyRecordId="id" propertyApplicationName="formDefine.applicationName" attachmentType="attachments">
							<a href="<ext:write name="attachment" property="urlInline"/>" target="_blank">
								 <ext:writeNumber plus="1" name="attachmentIndex"/>、<ext:write name="attachment" property="title"/>
							</a>&nbsp;
						</ext:iterateAttachment>
					</td>
				</tr>
			</ext:equal>
			<tr>
				<td class="tdtitle" nowrap="nowrap">设计文件</td>
				<td class="tdcontent"><ext:field property="projectTeam.design"/></td>
			</tr>
		</table>
	</ext:tabBody>
</ext:tab>