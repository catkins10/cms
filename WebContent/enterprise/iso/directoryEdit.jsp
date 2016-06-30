<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">目录名称</td>
		<td colspan="3" class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<ext:notEqual value="0" property="id">
		<tr>
			<td class="tdtitle" nowrap="nowrap">上级目录</td>
			<td colspan="3" class="tdcontent">
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
		<td class="tdtitle" nowrap="nowrap">文件类别</td>
		<td class="tdcontent"><ext:field property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">版本号</td>
		<td class="tdcontent"><ext:field property="version"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">受控状态</td>
		<td class="tdcontent"><ext:field property="control"/></td>
		<td class="tdtitle" nowrap="nowrap">紧急程度</td>
		<td class="tdcontent"><ext:field property="urgency"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件密级</td>
		<td class="tdcontent"><ext:field property="security"/></td>
		<td class="tdtitle" nowrap="nowrap">保存期限</td>
		<td class="tdcontent"><ext:field property="storage"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">管理部门</td>
		<td colspan="3" class="tdcontent"><ext:field property="storageDepartment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编号规则</td>
		<td colspan="3" class="tdcontent">
			<input type="button" value="插入年" onclick="FormUtils.pasteText('numberingRule', '&lt;年*4&gt;')" class="button" style="width:50px">
			<input type="button" value="插入月" onclick="FormUtils.pasteText('numberingRule', '&lt;月*2&gt;')" class="button" style="width:50px">
			<input type="button" value="插入日" onclick="FormUtils.pasteText('numberingRule', '&lt;日*2&gt;')" class="button" style="width:50px">
			<input type="button" value="插入序号" onclick="FormUtils.pasteText('numberingRule', '&lt;序号*4&gt;')" class="button" style="width:60px">
			<input type="button" value="插入版本号" onclick="FormUtils.pasteText('numberingRule', '&lt;版本号&gt;')" class="button" style="width:72px">
			<ext:field property="numberingRule"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件登记流程</td>
		<td class="tdcontent"><ext:field property="createWorkflowName"/></td>
		<td class="tdtitle" nowrap="nowrap">文件修改流程</td>
		<td class="tdcontent"><ext:field property="modifyWorkflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件销毁流程</td>
		<td class="tdcontent"><ext:field property="destroyWorkflowName"/></td>
		<td class="tdtitle" nowrap="nowrap">文件借阅流程</td>
		<td class="tdcontent"><ext:field property="loanWorkflowName"/></td>
	</tr>
<%	request.setAttribute("colspan", "3");%>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>