<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tabBody tabId="according">
	<div style="height:300px">
		<ext:write property="serviceItemGuide.according" filter="false"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="condition">
	<div style="height:300px">
		<ext:write property="serviceItemGuide.condition" filter="false"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="program">
	<div style="height:300px">
		<ext:write property="serviceItemGuide.program" filter="false"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="legalRight">
	<div style="height:300px">
		<ext:write property="serviceItemGuide.legalRight" filter="false"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="charge">
	<table valign="middle" width="100%" style="table-layout:fixed;" border="1" cellpadding="0" cellspacing="0" class="table">
		<col width=86px">
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">收费标准</td>
			<td class="tdcontent"><ext:write property="serviceItemGuide.chargeStandard"/></td>
		</tr>	
		<tr valign="top">
			<td class="tdtitle" nowrap="nowrap">收费依据</td>
			<td class="tdcontent"><ext:write property="serviceItemGuide.chargeAccording" filter="false"/></td>
		</tr>
	</table>
</ext:tabBody>

<ext:tabBody tabId="materials">
	<jsp:include page="serviceItemMaterials.jsp" />
</ext:tabBody>

<ext:tabBody tabId="faqs">
	<jsp:include page="serviceItemFaqs.jsp" />
</ext:tabBody>