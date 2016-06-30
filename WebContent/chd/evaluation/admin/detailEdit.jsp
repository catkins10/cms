<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<ext:equal value="0" property="sourceDirectoryId">
		<tr>
			<td>细则</td>
			<td class="tdcontent"><ext:field property="directoryName"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="0" property="sourceDirectoryId">
		<tr>
			<td>细则</td>
			<td class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td  class="tdtitle" nowrap="nowrap" valign="top">评价方法</td>
		<td class="tdcontent"><ext:field property="method"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap" valign="top">扣分条款</td>
		<td class="tdcontent"><ext:field property="deduct"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上传频率</td>
		<td class="tdcontent"><ext:field property="dataCycle"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上传截止时间</td>
		<td class="tdcontent"><ext:field property="dataCycleEnd" title="按周:1～7；按月:1~28；按年:日期，如:1-1"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上传催办提前天数</td>
		<td class="tdcontent"><ext:field property="dataUrgency"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">评价项目</td>
		<td class="tdcontent">
			<ext:equal value="true" property="changeParentDirectoryDisabled">
				<ext:field writeonly="true" property="parentDirectoryName"/>
			</ext:equal>
			<ext:equal value="false" property="changeParentDirectoryDisabled">
				<ext:field property="parentDirectoryName"/>
			</ext:equal>
		</td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td>登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td>登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>