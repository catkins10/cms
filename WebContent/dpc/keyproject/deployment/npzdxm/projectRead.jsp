<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

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
		<td class="tdtitle" nowrap="nowrap">项目建设必要性及主要效益</td>
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
		<td class="tdcontent" title="对本项目关注的领导以及挂点领导名单"><ext:field writeonly="true" property="leader"/></td>
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
		<td class="tdtitle" nowrap="nowrap">项目年度计划投资进度目标<br>和主要节点目标</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="plan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" title="项目总投资资金拼盘中投资列表的总和">项目总投资</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="invest"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开工时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="beginDate"/></td>
		<td class="tdtitle" nowrap="nowrap">竣工时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">投资主体</td>
		<td class="tdcontent"><ext:field writeonly="true" property="investmentSubject"/></td>
		<td class="tdtitle" nowrap="nowrap">项目管理部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="managementUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">法人机构或者筹建单位</td>
		<td class="tdcontent" title="项目建设法人机构或者负责筹建的单位名称"><ext:field writeonly="true" property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表或者负责人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="lawPerson"/></td>
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