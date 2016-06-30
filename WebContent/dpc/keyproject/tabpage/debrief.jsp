<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%
	request.setAttribute("debrief", new Character('1'));
%>

<ext:select id="" property="fiveYearPlans" nameValue="debrief" select="needApproval" length="1">
	五年计划
	<jsp:include page="fiveYearPlans.jsp" />
	<br>
</ext:select>

<ext:select id="" property="officialDocuments" nameValue="debrief" select="needApproval" length="1">
	批准文件
	<jsp:include page="officialDocuments.jsp" />
	<br>
</ext:select>

<ext:select id="" property="accountableInvests" nameValue="debrief" select="needApproval" length="1">
	总投资（责任制）
	<jsp:include page="accountableInvests.jsp" />
	<br>
</ext:select>

<ext:select id="" property="invests" nameValue="debrief" select="needApproval" length="1">
	总投资
	<jsp:include page="invests.jsp" />
	<br>
</ext:select>

<ext:select id="" property="units" nameValue="debrief" select="needApproval" length="1">
	参建单位
	<jsp:include page="units.jsp" />
	<br>
</ext:select>

<ext:select id="" property="annualObjectives" nameValue="debrief" select="needApproval" length="1">
	年度目标
	<jsp:include page="annualObjectives.jsp" />
	<br>
</ext:select>

<ext:select id="" property="stageProgresses" nameValue="debrief" select="needApproval" length="1">
	关键节点安排
	<jsp:include page="stageProgresses.jsp" />
	<br>
</ext:select>

<ext:select id="" property="progresses" nameValue="debrief" select="needApproval" length="1">
	形象进度
	<jsp:include page="progresses.jsp" />
	<br>
</ext:select>

<ext:select id="" property="investPaids" nameValue="debrief" select="needApproval" length="1">
	资金来源
	<jsp:include page="investPaids.jsp" />
	<br>
</ext:select>

<ext:select id="" property="investCompletes" nameValue="debrief" select="needApproval" length="1">
	计划完成投资
	<jsp:include page="investCompletes.jsp" />
	<br>
</ext:select>

<ext:select id="" property="problems" nameValue="debrief" select="needApproval" length="1">
	需要协调解决的问题及建议
	<jsp:include page="problems.jsp" />
	<br>
</ext:select>

<ext:select id="" property="plans" nameValue="debrief" select="needApproval" length="1">
	参建单位安排
	<jsp:include page="plans.jsp" />
	<br>
</ext:select>

<ext:select id="" property="photos" nameValue="debrief" select="needApproval" length="1">
	进展实景
	<jsp:include page="photos.jsp" />
	<br>
</ext:select>

<%
	request.removeAttribute("debrief");
%>