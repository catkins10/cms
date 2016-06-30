<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件题名</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject" onchange="parseKeyword(value, 'keyword')"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件字号</td>
		<td class="tdcontent"><ext:field property="docWord"/></td>
		<td class="tdtitle" nowrap="nowrap">公文种类</td>
		<td class="tdcontent"><ext:field property="docCategory"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field property="keyword"/></td>
		<td class="tdtitle" nowrap="nowrap">责任者</td>
		<td class="tdcontent"><ext:field property="responsibilityPerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件密级</td>
		<td class="tdcontent"><ext:field property="secureLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">成文日期</td>
		<td class="tdcontent"><ext:field property="signDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">机构或问题</td>
		<td class="tdcontent"><ext:field property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap">归档日期</td>
		<td class="tdcontent"><ext:field property="filingDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">全宗号</td>
		<td class="tdcontent"><ext:field property="fondsCode"/></td>
		<td class="tdtitle" nowrap="nowrap">归档年度</td>
		<td class="tdcontent"><ext:field property="filingYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保管期限</td>
		<td class="tdcontent"><ext:field property="rotentionPeriod"/></td>
		<td class="tdtitle" nowrap="nowrap">件号</td>
		<td class="tdcontent"><ext:field property="serialNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">档号</td>
		<td class="tdcontent"><ext:field property="archivesCode"/></td>
		<td class="tdtitle" nowrap="nowrap">文件类型</td>
		<td class="tdcontent"><ext:field property="archivesType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">份数</td>
		<td class="tdcontent"><ext:field property="count"/></td>
		<td class="tdtitle" nowrap="nowrap">页数</td>
		<td class="tdcontent"><ext:field property="pageCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">访问者</td>
		<td class="tdcontent" colspan="3"><ext:field property="readers.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文</td>
		<td class="tdcontent" colspan="3"><ext:field property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="3"><ext:field property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
	</tr>
</table>