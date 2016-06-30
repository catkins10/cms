<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">企业规模</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月薪</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">工作地点</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">行业</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">职能类别</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">公司性质</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">工作性质</td>
	</tr>
	<ext:iterate id="intention" property="intentions">
		<tr align="center">
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="intention" property="companyScale"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="intention" property="minMonthlyPay"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="intention" property="areaNames"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="intention" property="industryNames"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="intention" property="postNames"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="intention" property="companyTypes.companyType"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="intention" property="types.type"/></td>
		</tr>
	</ext:iterate>
</table>