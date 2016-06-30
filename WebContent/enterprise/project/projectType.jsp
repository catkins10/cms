<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveProjectType">
   	<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col valign="middle">
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目类型</td>
			<td class="tdcontent"><ext:field property="projectType" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">处理流程</td>
			<td class="tdcontent"><ext:field property="workflowName"/></td>
		</tr>
		<tr>
			<td valign="bottom" class="tdtitle" nowrap="nowrap">合同编号规则</td>
			<td class="tdcontent">
				<div style="padding-bottom: 3px;">
					<input type="button" value="插入年" onclick="FormUtils.pasteText('contractNumberRule', '&lt;年*4&gt;')" class="button" style="width:50px">
					<input type="button" value="插入月" onclick="FormUtils.pasteText('contractNumberRule', '&lt;月*2&gt;')" class="button" style="width:50px">
					<input type="button" value="插入日" onclick="FormUtils.pasteText('contractNumberRule', '&lt;日*2&gt;')" class="button" style="width:50px">
					<input type="button" value="插入序号" onclick="FormUtils.pasteText('contractNumberRule', '&lt;序号*4&gt;')" class="button" style="width:60px">
				</div>
				<ext:field property="contractNumberRule"/>
			</td>
		</tr>
	</table>
</ext:form>