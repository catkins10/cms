<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">书籍名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="title"/></td>
		<td class="tdtitle" nowrap="nowrap">书籍编号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="serialNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">书籍类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
		<td class="tdtitle" nowrap="nowrap">作者姓名</td>
		<td class="tdcontent"><ext:field writeonly="true" property="author"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出版社名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="publishingHouse"/></td>
		<td class="tdtitle" nowrap="nowrap">出版日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="publicationDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">书籍页数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pages"/></td>
		<td class="tdtitle" nowrap="nowrap">关键词</td>
		<td class="tdcontent"><ext:field writeonly="true" property="keyword"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否被借阅</td>
		<td class="tdcontent"><ext:field writeonly="true" property="isBorrowing"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>