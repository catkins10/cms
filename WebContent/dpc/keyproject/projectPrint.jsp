<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<html>
<head>
	<title>会稿传递单</title>
	<LINK href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet" type="text/css">
	<style>
		body {
			background-color: #ffffff;
			margin: 8px;
			font-size: 12px;
		}
	</style>
	<script>
		window.onload = function() {
			window.print();
		};
	</script>
</head>
<body>
<ext:form action="/projectPrint">
	<center>
		<div class="title"><ext:field writeonly="true" property="name"/></div>
	</center>
	<br>
	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="50%">
		<col>
		<col width="50%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目全称</td>
			<td class="tdcontent" title="项目可分父项目和子项目，子项目指具体的项目，父项目只是项目分类，没有明细的信息"><ext:field writeonly="true" property="name"/></td>
			<td class="tdtitle" nowrap="nowrap">建设地点</td>
			<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
		</tr>
		<tr title="概要描述项目的地点、规模、内容、总体安排、投资资金等信息">
			<td class="tdtitle" nowrap="nowrap">项目主要建设目标</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="summary"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">建设规模及主要建设内容</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="constructionScale"/></td>
		</tr>
		<tr title="建成投产新增生产能力">
			<td class="tdtitle" nowrap="nowrap">建成投产新增生产能力</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="constructionEffect"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目当前状态</td>
			<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
			<td class="tdtitle" nowrap="nowrap">项目级别</td>
			<td class="tdcontent"><ext:field writeonly="true" property="level"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">所属行业</td>
			<td class="tdcontent"><ext:field writeonly="true" property="industry"/></td>
			<td class="tdtitle" nowrap="nowrap">所属子行业</td>
			<td class="tdcontent"><ext:field writeonly="true" property="childIndustry"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目类别</td>
			<td class="tdcontent"><ext:field writeonly="true" property="classify"/></td>
			<td class="tdtitle" nowrap="nowrap">所属区域</td>
			<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目分类</td>
			<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
			<td class="tdtitle" nowrap="nowrap">项目挂点领导</td>
			<td class="tdcontent" title="对本项目关注的领导以及挂点领导名单"><ext:field writeonly="true" property="leader"/><ext:notEmpty property="otherLeader">,<ext:field writeonly="true" property="otherLeader"/></ext:notEmpty></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">建设性质</td>
			<td class="tdcontent"><ext:field writeonly="true" property="constructionType"/></td>
			<td class="tdtitle" nowrap="nowrap">建设年限</td>
			<td class="tdcontent">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="50%"><ext:field writeonly="true" property="constructionBeginYear"/></td>
						<td nowrap="nowrap">&nbsp;至&nbsp;</td>
						<td width="50%"><ext:field writeonly="true" property="constructionEndYear"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">大干150天力争完成投资(万元)</td>
			<td class="tdcontent"><ext:field writeonly="true" property="investPlan150"/></td>
			<td class="tdtitle" title="项目总投资资金拼盘中投资列表的总和" nowrap="nowrap">项目总投资</td>
			<td class="tdcontent"><ext:field writeonly="true" property="invest"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">开竣工时间</td>
			<td class="tdcontent">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="50%"><ext:field writeonly="true" property="beginDate"/></td>
						<td nowrap="nowrap">&nbsp;和&nbsp;</td>
						<td width="50%"><ext:field writeonly="true" property="endDate"/></td>
					</tr>
				</table>
			</td>
			<td class="tdtitle" nowrap="nowrap">投资主体</td>
			<td class="tdcontent"><ext:field writeonly="true" property="investmentSubject"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">市或县挂钩责任单位</td>
			<td class="tdcontent"><ext:field writeonly="true" property="managementUnit"/></td>
			<td class="tdtitle" nowrap="nowrap">责任人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="managementUnitResponsible"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">联络人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="managementUnitLinkman"/></td>
			<td class="tdtitle" nowrap="nowrap">电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="managementUnitTel"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目业主单位</td>
			<td class="tdcontent" title="项目建设法人机构或者负责筹建的单位名称"><ext:field writeonly="true" property="unit"/></td>
			<td class="tdtitle" nowrap="nowrap">所属开发区</td>
			<td class="tdcontent"><ext:field writeonly="true" property="developmentArea"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">法人代表或者负责人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="lawPerson"/></td>
			<td class="tdtitle" nowrap="nowrap">联系人</td>
			<td class="tdcontent"><ext:field writeonly="true" property="linkman"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">单位电话</td>
			<td class="tdcontent"><ext:field writeonly="true" property="unitTel"/></td>
			<td class="tdtitle" nowrap="nowrap">单位传真</td>
			<td class="tdcontent"><ext:field writeonly="true" property="unitFax"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">单位地址</td>
			<td class="tdcontent"><ext:field writeonly="true" property="unitAddress"/></td>
			<td class="tdtitle" nowrap="nowrap">邮政编码</td>
			<td class="tdcontent"><ext:field writeonly="true" property="unitPostcode"/></td>
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
	
	关键节点进度:<br>
	<jsp:include page="tabpage/stageProgresses.jsp" />
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
</ext:form>
</body>
</html>