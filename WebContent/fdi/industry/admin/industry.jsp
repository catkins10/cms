<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveIndustry">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col>
		<col width="100%">
		<tr>
			<td nowrap="nowrap" align="right">行业分类：</td>
			<td><ext:field property="category"/></td>
		</tr>
		<ext:notEmpty property="parentCategory">
			<tr>
				<td nowrap="nowrap" align="right">上级分类：</td>
				<td><ext:field property="parentCategory"/></td>
			</tr>
		</ext:notEmpty>
		<ext:notEmpty property="childCategories">
			<tr>
				<td nowrap="nowrap" align="right">下级分类：</td>
				<td>
					<ext:iterate id="childCategory" indexId="childCategoryIndex" property="childCategories">
						<ext:notEqual value="0" name="childCategoryIndex">、</ext:notEqual><a href="javascript:PageUtils.editrecord('fdi/industry', 'admin/industry', '<ext:write name="childCategory" property="id"/>', 'mode=dialog,width=500,height=300')"><ext:write name="childCategory" property="category"/></a>
					</ext:iterate>
				</td>
			</tr>
		</ext:notEmpty>
		<tr>
			<td nowrap="nowrap" align="right">编辑人员：</td>
			<td><ext:field property="editors.visitorNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">查询人员：</td>
			<td><ext:field property="readers.visitorNames"/></td>
		</tr>
	</table>
</ext:form>