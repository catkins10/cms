<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">就业类型</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">单位名称</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">岗位</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">薪酬</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">单位地址</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">专业对口度</td>
		<td class="tdtitle" nowrap="nowrap" width="70px">就业满意度</td>
		<td class="tdtitle" nowrap="nowrap" width="85px">是否更换工作单位（第几次）</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">离职原因</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">是否需要学院帮助推荐</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">暂不就业原因</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">登记时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">备注</td>
	</tr>
	<ext:iterate id="employment" property="employments">
		<tr>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="employmentType"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="company"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="post"/></td>
			<td class="tdcontent" align="center"><ext:field writeonly="true" name="employment" property="monthlyPay"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="companyAddress"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="counterpart"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="satisfaction"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="changeCompany"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="leaveReason"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="needHelp"/></td>
			<td class="tdcontent" align="center"><ext:field writeonly="true" name="employment" property="waitReason"/></td>
			<td class="tdcontent" align="center"><ext:field writeonly="true" name="employment" property="created"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="employment" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>