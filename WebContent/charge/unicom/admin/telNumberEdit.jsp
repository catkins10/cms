<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="100px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td>地区</td>
		<td>
			<html:hidden property="areaId"/>
			<ext:field property="area" styleClass="field required" selectOnly="true" onSelect="DialogUtils.selectOrg(600, 400, false, 'areaId{id},area{name}', '', 'area')"/>
		</td>
	</tr>
	<tr>
		<td>区号</td>
		<td>
			<html:text property="areaCode" styleClass="field required"/>
		</td>
	</tr>
	<tr>
		<td>起始号码</td>
		<td>
			<html:text property="beginNumber" styleClass="field required"/>
		</td>
	</tr>
	<tr>
		<td>结束号码</td>
		<td>
			<html:text property="endNumber" styleClass="field required"/>
		</td>
	</tr>
</table>