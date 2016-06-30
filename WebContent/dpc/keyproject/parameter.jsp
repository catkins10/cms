<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveParameter">
   	<ext:tab>
   		<ext:tabBody tabId="basic">
		   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目状态</td>
					<td class="tdcontent"><ext:field property="projectStatus"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目级别</td>
					<td class="tdcontent"><ext:field property="projectLevel"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目类别</td>
					<td class="tdcontent"><ext:field property="projectClassify"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">所属区域</td>
					<td class="tdcontent"><ext:field property="area"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目分类</td>
					<td class="tdcontent"><ext:field property="projectCategory"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">投资主体</td>
					<td class="tdcontent"><ext:field property="investmentSubject"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目管理部门</td>
					<td class="tdcontent"><ext:field property="managementUnit"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">建设性质</td>
					<td class="tdcontent"><ext:field property="constructionType"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">开发区</td>
					<td class="tdcontent"><ext:field property="developmentArea"/></td>
				</tr>
			</table>
		</ext:tabBody>
			
		<ext:tabBody tabId="industries">
			<script>
			function newIndustry() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/industry.shtml', 550, 300);
			}
			function openIndustry(industryId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/industry.shtml?industry.id=' + industryId, 550, 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加行业配置" style="width:90px" onclick="newIndustry()">
				<input type="button" class="button" value="设置行业优先级" style="width:100px" onclick="DialogUtils.adjustPriority('dpc/keyproject', 'industry', '行业优先级', 600, 400)">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" nowrap="nowrap" class="tdtitle" width="100px">行业</td>
					<td align="center" nowrap="nowrap" class="tdtitle">子行业</td>
				</tr>
				<ext:iterate id="industry" property="industries">
					<tr style="cursor:pointer" valign="top">
						<td class="tdcontent" onclick="openIndustry('<ext:write name="industry" property="id"/>')"><ext:write name="industry" property="industry"/></td>
						<td class="tdcontent" onclick="openIndustry('<ext:write name="industry" property="id"/>')"><ext:write name="industry" property="childIndustry"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="investSources">	
			<script>
			function newInvestSource() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/investSource.shtml', 550, 300);
			}
			function openInvestSource(investSourceId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/investSource.shtml?investSource.id=' + investSourceId, 550, 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加资金来源配置" style="width:120px" onclick="newInvestSource()">
				<input type="button" class="button" value="设置资金来源优先级" style="width:130px" onclick="DialogUtils.adjustPriority('dpc/keyproject', 'investSource', '资金来源优先级', 600, 400)">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" nowrap="nowrap" class="tdtitle" width="150">来源</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="100%">子来源</td>
				</tr>
				<ext:iterate id="investSource" property="investSources">
					<tr style="cursor:pointer" valign="top">
						<td class="tdcontent" onclick="openInvestSource('<ext:write name="investSource" property="id"/>')"><ext:write name="investSource" property="source"/></td>
						<td class="tdcontent" onclick="openInvestSource('<ext:write name="investSource" property="id"/>')"><ext:write name="investSource" property="childSource"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
			
		<ext:tabBody tabId="developmentAreaCategories">	
			<script>
			function newDevelopmentAreaCategory() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/developmentAreaCategory.shtml', 550, 300);
			}
			function openDevelopmentAreaCategory(developmentAreaCategoryId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/developmentAreaCategory.shtml?developmentAreaCategory.id=' + developmentAreaCategoryId, 550, 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加分类" style="width:120px" onclick="newDevelopmentAreaCategory()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr height="23px" valign="bottom">
					<td align="center" nowrap="nowrap" class="tdtitle" width="150px">分类</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="100%">开发区列表</td>
				</tr>
				<ext:iterate id="developmentAreaCategory" property="developmentAreaCategories">
					<tr style="cursor:pointer" valign="top">
						<td class="tdcontent" onclick="openDevelopmentAreaCategory('<ext:write name="developmentAreaCategory" property="id"/>')"><ext:write name="developmentAreaCategory" property="category"/></td>
						<td class="tdcontent" onclick="openDevelopmentAreaCategory('<ext:write name="developmentAreaCategory" property="id"/>')"><ext:write name="developmentAreaCategory" property="developmentAreas"/></td>
					</tr>
				</ext:iterate>
			</table>
			<span id="spanDevelopmentAreaCategories" style="display:none"><ext:iterate id="developmentAreaCategory" property="developmentAreaCategories">,<ext:write name="developmentAreaCategory" property="category"/></ext:iterate></span>
		</ext:tabBody>
	</ext:tab>
</ext:form>