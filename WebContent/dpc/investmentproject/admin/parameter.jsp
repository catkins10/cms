<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveParameter">
   	<ext:tab>
   		<ext:tabBody tabId="basic">
		   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">区域和开发区</td>
					<td class="tdcontent"><ext:field property="area"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">利用外资方式</td>
					<td class="tdcontent"><ext:field property="investMode"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">币种</td>
					<td class="tdcontent"><ext:field property="currency"/></td>
				</tr>
			</table>
		</ext:tabBody>
			
		<ext:tabBody tabId="industries">
			<script>
				function newIndustry() {
					DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/investmentproject/admin/industry.shtml', 550, 300);
				}
				function openIndustry(industryId) {
					DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/investmentproject/admin/industry.shtml?industry.id=' + industryId, 550, 300);
				}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加行业配置" style="width:90px" onclick="newIndustry()">
				<input type="button" class="button" value="设置行业优先级" style="width:100px" onclick="DialogUtils.adjustPriority('dpc/investmentproject', 'industry', '行业优先级', 600, 400)">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td class="tdtitle" align="center" nowrap="nowrap" width="150">行业</td>
					<td class="tdtitle" align="center" nowrap="nowrap" width="150">子行业</td>
				</tr>
				<ext:iterate id="industry" property="industries">
					<tr style="cursor:pointer" valign="top">
						<td class="tdcontent" onclick="openIndustry('<ext:write name="industry" property="id"/>')"><ext:write name="industry" property="industry"/></td>
						<td class="tdcontent" onclick="openIndustry('<ext:write name="industry" property="id"/>')"><ext:write name="industry" property="childIndustry"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:form>