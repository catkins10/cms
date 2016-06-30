<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newFaq() {
		if(document.getElementsByName("name")[0] && document.getElementsByName("name")[0].value=="") {
			alert("办理事项名称不能为空");
			return;
		}
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemFaq.shtml?id=<ext:write property="id"/>', 800, 400);
	}
	function openFaq(faqId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/serviceItemFaq.shtml?id=<ext:write property="id"/>&faq.id=' + faqId, 800, 400);
	}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加常见问题解答" style="width:110px" onclick="newFaq()">
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('cms/onlineservice', 'admin/onlineServiceFaq', '常见问题解答', 600, 380, 'itemId=<ext:write property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="36px">序号</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">问题</td>
	</tr>
	<ext:iterate id="faq" indexId="faqIndex" property="faqs">
		<tr style="cursor:pointer;" align="center" valign="top">
			<td class="tdcontent" onclick="openFaq('<ext:write name="faq" property="id"/>')" align="center"><ext:writeNumber name="faqIndex" plus="1"/></td>
			<td class="tdcontent" onclick="openFaq('<ext:write name="faq" property="id"/>')" align="left"><ext:write name="faq" property="question"/></td>
		</tr>
	</ext:iterate>
</table>