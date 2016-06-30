<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td  class="tdtitle" nowrap="nowrap">公司名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<ext:notEqual value="0" property="id">
		<tr>
			<td  class="tdtitle" nowrap="nowrap">上级</td>
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
		<td  class="tdtitle" nowrap="nowrap">资料上报流程</td>
		<td class="tdcontent"><ext:field property="dataWorkflowName"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">自查流程</td>
		<td class="tdcontent"><ext:field property="selfEvalWorkflowName"/></td>
	</tr>
	<tr style="display:none">
		<td  class="tdtitle" nowrap="nowrap">当月自查</td>
		<td class="tdcontent"><ext:field property="selfEvalCurrentMonth"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">自查截止时间(1~31)</td>
		<td class="tdcontent"><ext:field property="selfEvalEnd"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">自查催办提前天数</td>
		<td class="tdcontent"><ext:field property="selfEvalUrgency"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
</table>