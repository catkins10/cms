<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal value="true" name="editabled">
	<input type="hidden" name="newRelationFaqQuestions"/>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/onlineservice/faq/js/faq.js"></script>
	<script>
	function newRelationFaqs() {
		selectFaq('80%', '80%', true, 'newRelationFaqIds{id},newRelationFaqQuestions{name|问题|100%}', 'doAddRelationFaqs()', '', ',', 0, false);
	}
	function doAddRelationFaqs() {
		if(document.getElementsByName("newRelationFaqIds")[0].value!="") {
			FormUtils.doAction('addRelationFaqs');
		}
	}
	</script>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加问题" onclick="newRelationFaqs()">
		<input type="button" class="button" value="删除问题" onclick="if(confirm('删除后不可恢复，是否确定删除？'))FormUtils.doAction('removeRelationFaqs')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<ext:equal value="true" name="editabled">
			<td align="center" class="tdtitle" nowrap="nowrap" width="36px">选择</td>
		</ext:equal>
		<td align="center" class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100%">问题</td>
	</tr>
	<ext:iterate id="relationFaq" indexId="relationFaqIndex" property="relationFaqs">
		<tr style="cursor:pointer" valign="top">
			<ext:equal value="true" name="editabled">
				<td class="tdcontent"><input type="checkbox" class="checkbox" name="relationFaqId" value="<ext:write name="relationFaq" property="id"/>"/></td>
			</ext:equal>
			<td class="tdcontent" align="center"><ext:writeNumber name="relationFaqIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="relationFaq" property="question"/></td>
		</tr>
	</ext:iterate>
</table>