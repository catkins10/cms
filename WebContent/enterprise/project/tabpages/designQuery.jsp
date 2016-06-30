<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function openStage(stageId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectStage.shtml?id=<ext:write property="id"/>&openerTabPage=designSchedule&stage.id=' + stageId, 600, 390);
}
</script>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">项目阶段</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="160px">负责人</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="160px">实际负责人</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">计划完成时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">设计完成时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="50%">设计完成情况</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="50%">设计文件</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">设计质量</td>
	</tr>
	<ext:iterate id="stage" property="stages">
		<tr style="cursor:pointer" valign="top">
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')"><ext:write name="stage" property="stage"/></td>
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')"><ext:write name="stage" property="principalNames"/></td>
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')"><ext:write name="stage" property="realPrincipalNames"/></td>
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')" align="center"><ext:write name="stage" property="expectingDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')" align="center"><ext:write name="stage" property="completionDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')"><ext:write name="stage" property="completionDescription"/></td>
			<td class="tdcontent">
				<ext:iterateAttachment id="attachment" indexId="attachmentIndex" nameRecordId="stage" propertyRecordId="id" applicationName="enterprise/project" attachmentType="design">
					<a href="<ext:write name="attachment" property="urlInline"/>" target="_blank">
						 <ext:writeNumber plus="1" name="attachmentIndex"/>、<ext:write name="attachment" property="title"/>
					</a>&nbsp;
				</ext:iterateAttachment>
			</td>
			<td class="tdcontent" onclick="openStage('<ext:write name="stage" property="id"/>')" align="center"><ext:write name="stage" property="designQuality"/></td>
		</tr>
	</ext:iterate>
</table>