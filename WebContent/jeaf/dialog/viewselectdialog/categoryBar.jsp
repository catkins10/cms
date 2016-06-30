<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:notEmpty property="viewPackage.view.categories">
	<table border="0" cellpadding="0" cellspacing="0">
    	<tr>
  			<td align="left" style="padding-top:3px" nowrap>指定分类：</td>
			<td>
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="categoryBox" style="table-layout:fixed">
					<tr>
						<td width="100%" id="displayCategory" class="displayCategory"><ext:write property="viewPackage.categoryTitle"/></td>
						<td onclick="selectCategory(this, '<ext:write property="applicationName"/>', '<ext:write property="viewName"/>')" class="categorySelectButton">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<div id="divSelect" class="divCategory" onblur="hideCategory(this)" onselectstart="return false;" style="display:none; position: absolute;">
		<iframe id="categoryTree" width="100%" height="100%" frameborder="0" allowtransparency="true"></iframe>
	</div>
</ext:notEmpty>