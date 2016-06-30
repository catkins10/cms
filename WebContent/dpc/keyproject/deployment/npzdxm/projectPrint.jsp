<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<style>
	body {
		background-color: #ffffff;
		margin: 8px;
	}
</style>

<center>
	<div class="title"><ext:field writeonly="true" property="name"/></div>
</center>
<br>
<table valign="middle" width="100%" style="border-collapse:collapse" border="1" cellpadding="3" cellspacing="0" bordercolor="black">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr title="项目可分父项目和子项目，子项目指具体的项目，父项目只是项目分类，没有明细的信息">
		<td>项目全称</td>
		<td><ext:field writeonly="true" property="name"/></td>
		<td>建设地点</td>
		<td><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr title="概要描述项目的地点、规模、内容、总体安排、投资资金等信息">
		<td nowrap="nowrap">项目建设必要性及主要效益</td>
		<td colspan="3"><ext:field writeonly="true" property="summary"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">建设规模及主要建设内容</td>
		<td colspan="3"><ext:field writeonly="true" property="constructionScale"/></td>
	</tr>
	<tr title="建成投产新增生产能力">
		<td nowrap="nowrap">建成投产新增生产能力</td>
		<td colspan="3"><ext:field writeonly="true" property="constructionEffect"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">项目当前状态</td>
		<td><ext:field writeonly="true" property="status"/></td>
		<td>项目级别</td>
		<td><ext:field writeonly="true" property="level"/></td>
	</tr>
	<tr>
		<td>所属行业</td>
		<td><ext:field writeonly="true" property="industry"/></td>
		<td nowrap="nowrap">所属子行业</td>
		<td><ext:field writeonly="true" property="childIndustry"/></td>
	</tr>
	<tr>
		<td>项目类别</td>
		<td><ext:field writeonly="true" property="classify"/></td>
		<td>所属区域</td>
		<td><ext:field writeonly="true" property="area"/></td>
	</tr>
	<tr>
		<td>项目分类</td>
		<td><ext:field writeonly="true" property="category"/></td>
		<td nowrap="nowrap">项目挂点领导</td>
		<td><ext:field writeonly="true" property="leader"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">项目年度计划投资进度目标<br>和主要节点目标</td>
		<td colspan="3"><ext:field writeonly="true" property="plan"/></td>
	</tr>
	<tr>
		<td title="项目总投资资金拼盘中投资列表的总和">项目总投资</td>
		<td colspan="3"><ext:field writeonly="true" property="invest"/></td>
	</tr>
	<tr>
		<td>开工时间</td>
		<td><ext:field writeonly="true" property="beginDate"/></td>
		<td>竣工时间</td>
		<td><ext:field writeonly="true" property="endDate"/></td>
	</tr>
	<tr>
		<td>投资主体</td>
		<td><ext:field writeonly="true" property="investmentSubject"/></td>
		<td nowrap="nowrap">项目管理部门</td>
		<td><ext:field writeonly="true" property="managementUnit"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">法人机构或者筹建单位</td>
		<td><ext:field writeonly="true" property="unit"/></td>
		<td nowrap="nowrap">法人代表或者负责人</td>
		<td><ext:field writeonly="true" property="lawPerson"/></td>
	</tr>
	<tr>
		<td>单位电话</td>
		<td><ext:field writeonly="true" property="unitTel"/></td>
		<td>单位传真</td>
		<td><ext:field writeonly="true" property="unitFax"/></td>
	</tr>
	<tr>
		<td>单位地址</td>
		<td><ext:field writeonly="true" property="unitAddress"/></td>
		<td>邮政编码</td>
		<td><ext:field writeonly="true" property="unitPostcode"/></td>
	</tr>
	<tr>
		<td>建设性质</td>
		<td><ext:field writeonly="true" property="constructionType"/></td>
		<td>建设年限</td>
		<td><ext:field writeonly="true" property="constructionBeginYear"/>&nbsp;至&nbsp;<ext:field writeonly="true" property="constructionEndYear"/>	</td>
	</tr>
</table>
<br><br>

申报情况:<br>
<jsp:include page="tabpage/declares.jsp" />
<br><br>

批准文件:<br>
<jsp:include page="tabpage/officialDocuments.jsp" />
<br><br>

总投资:<br>
<jsp:include page="tabpage/invests.jsp" />
<br><br>

年度目标:<br>
<jsp:include page="tabpage/annualObjectives.jsp" />
<br><br>

实际完成投资:<br>
<jsp:include page="tabpage/investCompletes.jsp" />
<br><br>

资金到位:<br>
<jsp:include page="tabpage/investPaids.jsp" />
<br><br>

形象进度:<br>
<jsp:include page="tabpage/progresses.jsp" />
<br><br>

存在的问题及工作建议:<br>
<jsp:include page="tabpage/problems.jsp" />
<br><br>

参建单位<br>
<jsp:include page="tabpage/units.jsp" />
<br><br>

进展实景:<br>
<jsp:include page="tabpage/photos.jsp" />
<br><br>

审核意见:<br>
<jsp:include page="/jeaf/opinionmanage/opinionListByType.jsp"/>

<div id="tabaccountableInvests" style="display:none">
	总投资(责任制):<br>
	<jsp:include page="tabpage/accountableInvests.jsp" />
</div>

<div id="tabplans" style="display:none">
	参建单位安排:<br>
	<jsp:include page="ta	bpage/plans.jsp" />
</div>

<script>
	window.print();
</script>