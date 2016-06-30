<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="100px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td>服务报价</td>
		<td>
			<html:hidden property="servicePriceId"/>
			<ext:field property="servicePriceName" styleClass="field required" selectOnly="true" onSelect="DialogUtils.openSelectDialog('charge/servicemanage', 'selectServicePrice', 600, 400, false, 'servicePriceId{id},servicePriceName{title}')"/>
		</td>
	</tr>
	<tr>
		<td>联通套餐编码</td>
		<td>
			<html:text property="unicomServiceId" styleClass="field required"/>
		</td>
	</tr>
</table>