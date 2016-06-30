<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td  class="tdtitle" nowrap="nowrap">企业名称</td>
		<td colspan="3" class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
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
		<td  class="tdtitle" nowrap="nowrap">企业类型</td>
		<td class="tdcontent"><ext:field property="type"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">资料上报流程</td>
		<td class="tdcontent"><ext:field property="dataWorkflowName"/></td>
		<td  class="tdtitle" nowrap="nowrap">自查流程</td>
		<td class="tdcontent"><ext:field property="selfEvalWorkflowName"/></td>
	</tr>
	<tr style="display:none">
		<td  class="tdtitle" nowrap="nowrap">当月自查</td>
		<td colspan="3" class="tdcontent"><ext:field property="selfEvalCurrentMonth"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">自查截止时间(1~31)</td>
		<td class="tdcontent"><ext:field property="selfEvalEnd"/></td>
		<td  class="tdtitle" nowrap="nowrap">自查催办提前天数</td>
		<td class="tdcontent"><ext:field property="selfEvalUrgency"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">地址</td>
		<td class="tdcontent"><ext:field property="address"/></td>
		<td  class="tdtitle" nowrap="nowrap">邮政编码</td>
		<td class="tdcontent"><ext:field property="postalCode"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">全厂总装机容量构成(MW)</td>
		<td class="tdcontent"><ext:field property="installedCapacity"/></td>
		<td  class="tdtitle" nowrap="nowrap">建厂时间</td>
		<td class="tdcontent"><ext:field property="constructionDate"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">获得最高星级档次</td>
		<td class="tdcontent"><ext:field property="maxLevel"/></td>
		<td  class="tdtitle" nowrap="nowrap">获得最高星级时间</td>
		<td class="tdcontent"><ext:field property="maxLevelDate"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">企业年销售收入(万元)</td>
		<td class="tdcontent"><ext:field property="saleProceeds"/></td>
		<td  class="tdtitle" nowrap="nowrap">年末在册职工人数</td>
		<td class="tdcontent"><ext:field property="employeeNumber"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">联系部门</td>
		<td class="tdcontent"><ext:field property="contactDepartment"/></td>
		<td  class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="contactPerson"/></td>
	</tr>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
		<td  class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><ext:field property="mobile"/></td>
	</tr>
<%	request.setAttribute("colspan", "3"); %>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td  class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td  class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
</table>