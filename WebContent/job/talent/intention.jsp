<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveIntention">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">工作地点：</td>
			<td><ext:field property="areaNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">行业：</td>
			<td><ext:field property="industryNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">职能类别：</td>
			<td><ext:field property="postNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">工作性质：</td>
			<td><ext:field property="typeArray"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">公司性质：</td>
			<td><ext:field property="companyTypeArray"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">公司规模：</td>
			<td><ext:field property="companyScale"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">到岗时间：</td>
			<td><ext:field property="entryDate"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">期望月薪：</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="60px"><ext:field property="minMonthlyPay"/></td>
						<td nowrap="nowrap">&nbsp;元&nbsp;至&nbsp;</td>
						<td width="60px"><ext:field property="maxMonthlyPay"/></td>
						<td nowrap="nowrap">&nbsp;元</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</ext:form>