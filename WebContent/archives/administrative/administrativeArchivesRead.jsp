<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件题名</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件字号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="docWord"/></td>
		<td class="tdtitle" nowrap="nowrap">公文种类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="docCategory"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field writeonly="true" property="keyword"/></td>
		<td class="tdtitle" nowrap="nowrap">责任者</td>
		<td class="tdcontent"><ext:field writeonly="true" property="responsibilityPerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文件密级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="secureLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">成文日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">机构或问题</td>
		<td class="tdcontent"><ext:field writeonly="true" property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap">归档日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="filingDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">全宗号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="fondsCode"/>
		</td>
		<td class="tdtitle" nowrap="nowrap">归档年度</td>
		<td class="tdcontent"><ext:field writeonly="true" property="filingYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保管期限</td>
		<td class="tdcontent"><ext:field writeonly="true" property="rotentionPeriod"/></td>
		<td class="tdtitle" nowrap="nowrap">件号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="serialNumber"/></td>
	</tr>	
	<tr>
		<td class="tdtitle" nowrap="nowrap">档号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="archivesCode"/></td>
		<td class="tdtitle" nowrap="nowrap">文件类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="archivesType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">份数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="count"/></td>
		<td class="tdtitle" nowrap="nowrap">页数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pageCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">访问者</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">正文</td>
		<td colspan="3" id="attachments" class="tdcontent"><ext:field writeonly="true" property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td colspan="3" id="attachments" class="tdcontent"><ext:field writeonly="true" property="attachment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>