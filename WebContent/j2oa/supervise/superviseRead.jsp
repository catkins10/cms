<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">工作内容</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="content"/></td>
	</tr>
	<ext:notEmpty property="result">
		<tr>
			<td class="tdtitle" nowrap="nowrap">落实情况</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="result"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">附件</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">督办号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
		<td class="tdtitle" nowrap="nowrap">完成时限</td>
		<td class="tdcontent"><ext:field writeonly="true" property="timeLimit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任部门</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="departmentNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分管领导</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="departmentSupervisors"/></td>
	</tr>
	<ext:notEmpty property="transactorNames">
		<tr>
			<td class="tdtitle" nowrap="nowrap">经办人</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="transactorNames"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>