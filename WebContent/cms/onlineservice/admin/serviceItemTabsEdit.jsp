<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tabBody tabId="according">
	<div>
		<ext:field property="serviceItemGuide.according"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="condition">
	<div>
		<ext:field property="serviceItemGuide.condition"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="program">
	<div>
		<ext:field property="serviceItemGuide.program"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="legalRight">
	<div>
		<ext:field property="serviceItemGuide.legalRight"/>
	</div>
</ext:tabBody>
	
<ext:tabBody tabId="charge">
	<table valign="middle" width="100%" style="table-layout:fixed;" border="1" cellpadding="0" cellspacing="0" class="table">
		<col width=86px">
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">收费标准</td>
			<td class="tdcontent">
				<ext:field property="serviceItemGuide.chargeStandard" />
			</td>
		</tr>	
		<tr valign="top">
			<td class="tdtitle" nowrap="nowrap">收费依据</td>
			<td class="tdcontent"><ext:field property="serviceItemGuide.chargeAccording"/></td>
		</tr>
	</table>
</ext:tabBody>

<ext:tabBody tabId="materials">
	<jsp:include page="serviceItemMaterials.jsp" />
</ext:tabBody>

<ext:tabBody tabId="faqs">
	<jsp:include page="serviceItemFaqs.jsp" />
</ext:tabBody>

<ext:tabBody tabId="complaints">
	<script>
		function newComplaint() {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemComplaint.shtml?id=<ext:write property="id"/>', 800, 480);
		}
		function openComplaint(complaintId) {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemComplaint.shtml?id=<ext:write property="id"/>&complaint.id=' + complaintId, 800, 480);
		}
	</script>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="投诉登记" onclick="newComplaint()">
	</div>
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="100%">投诉主题</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="100px">投诉人</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="120px">投诉时间</td>
		</tr>
		<ext:iterate id="complaint" indexId="complaintIndex" property="complaints">
			<tr style="cursor:pointer" valign="top" onclick="openComplaint('<ext:write name="complaint" property="id"/>')">
				<td class="tdcontent" align="center"><ext:writeNumber name="complaintIndex" plus="1"/></td>
				<td class="tdcontent"><ext:write name="complaint" property="subject"/></td>
				<td class="tdcontent"><ext:write name="complaint" property="creator"/></td>
				<td class="tdcontent" align="center"><ext:write name="complaint" property="created" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
		</ext:iterate>
	</table>
</ext:tabBody>

<ext:tabBody tabId="consults">
	<script>
		function newConsult() {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemConsult.shtml?id=<ext:write property="id"/>', 800, 480);
		}
		function openConsult(consultId) {
			DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemConsult.shtml?id=<ext:write property="id"/>&consult.id=' + consultId, 800, 480);
		}
	</script>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="咨询登记" onclick="newConsult()">
	</div>
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="100%">咨询主题</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="100px">咨询人</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="120px">咨询时间</td>
		</tr>
		<ext:iterate id="consult" indexId="consultIndex" property="consults">
			<tr style="cursor:pointer" valign="top" onclick="openConsult('<ext:write name="consult" property="id"/>')">
				<td class="tdcontent" align="center"><ext:writeNumber name="consultIndex" plus="1"/></td>
				<td class="tdcontent"><ext:write name="consult" property="subject"/></td>
				<td class="tdcontent"><ext:write name="consult" property="creator"/></td>
				<td class="tdcontent" align="center"><ext:write name="consult" property="created" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
		</ext:iterate>
	</table>
</ext:tabBody>