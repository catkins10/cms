<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script type="text/javascript">
	function openProduct(id) {
		PageUtils.openrecord('credit/bank', 'admin/product', id, 'mode=fullscreen','act=read');
	}
</script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
<tr height="23px">
				<td align="center" class="tdtitle" nowrap="nowrap">名称</td>
				<td align="center" class="tdtitle" nowrap="nowrap">种类</td>
				<td align="center" class="tdtitle" nowrap="nowrap">适用对象</td>
			</tr>
	<ext:iterate id="product" indexId="productIndex" property="products">
		<tr>
			<td class="tdcontent" align="left" onclick="openProduct(<ext:write name = "product" property="id"/>)"><ext:write name = "product" property="name"/></td>
			<td class="tdcontent" align="left" onclick="openProduct(<ext:write name = "product" property="id"/>)"><ext:write name = "product" property="type"/></td>
			<td class="tdcontent" align="left" onclick="openProduct(<ext:write name = "product" property="id"/>)" ><ext:write  name = "product" property="forWho"/></td>
		</tr>
	</ext:iterate>
</table>