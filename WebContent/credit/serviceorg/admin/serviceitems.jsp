<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script type="text/javascript">
	function openProduct(id) {
		PageUtils.openrecord('credit/serviceorg', 'admin/serviceorg', id, 'mode=fullscreen','act=read');
	}
</script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
<tr height="23px">
				<td align="center" class="tdtitle" nowrap="nowrap">事项名称</td>
				<td align="center" class="tdtitle" nowrap="nowrap">联系人</td>
				<td align="center" class="tdtitle" nowrap="nowrap">电话</td>
			</tr>
	<ext:iterate id="serviceItem" indexId="serviceItemIndex" property="serviceItems">
		<tr>
			<td class="tdcontent" align="left" onclick="openProduct(<ext:write name = "serviceItem" property="id"/>)"><ext:write name = "serviceItem" property="name"/></td>
			<td class="tdcontent" align="left" onclick="openProduct(<ext:write name = "serviceItem" property="id"/>)"><ext:write name = "serviceItem" property="person"/></td>
			<td class="tdcontent" align="left" onclick="openProduct(<ext:write name = "serviceItem" property="id"/>)" ><ext:write  name = "serviceItem" property="tel"/></td>
		</tr>
	</ext:iterate>
</table>