<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">评价细则</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">自查时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="evalYear" style="width:60px"/></td>
					<td>&nbsp;年&nbsp;&nbsp;</td>
					<td><ext:field property="evalMonth" style="width:40px"/></td>
					<td>&nbsp;月</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">自查情况说明</td>
		<td class="tdcontent"><ext:field property="evalResult"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">本月提交的资料</td>
		<td>
			<ext:iterate property="dataList" id="data" indexId="dataIndex">
				<ext:writeNumber name="dataIndex" plus="1"/>、<a href="<%=request.getContextPath()%>/chd/evaluation/data/data.shtml?id=<ext:write name="data" property="id"/>" target="_blank"><ext:write name="data" property="name"/></a><br>
			</ext:iterate>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在部门</td>
		<td class="tdcontent"><ext:field property="department"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>