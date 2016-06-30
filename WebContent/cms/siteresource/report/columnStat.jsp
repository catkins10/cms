<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@page import="com.yuanluesoft.cms.siteresource.report.model.columnstat.UnitStat"%>
<%@page import="com.yuanluesoft.cms.siteresource.report.model.columnstat.ColumnStat"%>
<%@page import="java.util.List"%>
<%!
	public void writeRows(List columnStats, int level, JspWriter out) throws Exception {
		if(columnStats==null || columnStats.isEmpty()) {
			if(level==0) {
				return;
			}
			for(int i=level; i<4; i++) {
				out.write("<td></td>\n");
			}
			return;
		}
		for(int i=0; i<columnStats.size(); i++) {
			ColumnStat columnStat = (ColumnStat)columnStats.get(i);
			if(i>0 || level==0) {
				out.write("<tr>\r");
			}
			out.write("<td rowspan=\"" + columnStat.getRowspan() + "\">" + columnStat.getColumnName() + "[" + columnStat.getTotal() + "]</td>\n");
			//输出子栏目
			writeRows(columnStat.getColumnStats(), level+1, out);
			if(i>0 || level==0) {
				out.write("</tr>\r");
			}
		}
	}
%>
<ext:form action="/report/writeColumnStatReport">
	<script>document.body.style.overflowX = "hidden";</script>
	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td nowrap="nowrap">时间:</td>
			<td width="86px" nowrap="nowrap"><ext:field property="beginDate"/></td>
			<td nowrap="nowrap" style="padding:0 3 0 3">至</td>
			<td width="86px" nowrap="nowrap"><ext:field property="endDate"/></td>
			<td nowrap="nowrap">&nbsp;站点:</td>
			<td width="120px" nowrap="nowrap"><ext:field property="siteName"/></td>
			<td nowrap="nowrap">&nbsp;单位:</td>
			<td width="150px"><ext:field property="unitNames"/></td>
			<td nowrap="nowrap">&nbsp;<input type="button" class="button" value="确定" onclick="FormUtils.submitForm()"/></td>
		</tr>
	</table>
	<br/>
	<ext:iterate id="unitStat" property="unitStats">
		<div style="padding-bottom:3px; font-weight:bold"><ext:write name="unitStat" property="unitName"/>[<ext:write name="unitStat" property="total"/>]：</div>
		<table style="border-collapse: collapse;" border="1" bordercolor="#000000" cellpadding="5" cellspacing="0" width="600px" style="background-color: #ffffff">
			<tr align="center">
				<td>一级栏目</td>
				<td>二级栏目</td>
				<td>三级栏目</td>
				<td>四级栏目</td>
			</tr>
<%			UnitStat unitStat = (UnitStat)pageContext.getAttribute("unitStat");
			writeRows(unitStat.getColumnStats(), 0, out); %>
		</table>
		<br/>
	</ext:iterate>
</ext:form>