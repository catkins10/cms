<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" valign="top">标题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="keyword"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发文单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="documentUnit"/></td>
		<td class="tdtitle" nowrap="nowrap">签发人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sign"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发文字号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="docWord"/></td>
		<td class="tdtitle" nowrap="nowrap">成文日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="generateDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发文种类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="docType"/></td>
		<td class="tdtitle" nowrap="nowrap">秘密等级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="secureLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">保密期限</td>
		<td class="tdcontent"><ext:field writeonly="true" property="secureTerm"/></td>
		<td class="tdtitle" nowrap="nowrap">紧急程度</td>
		<td class="tdcontent"><ext:field writeonly="true" property="priority"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">印发份数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="printNumber"/></td>
		<td class="tdtitle" nowrap="nowrap">印发日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="distributeDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主送单位</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="mainSend"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抄送单位</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="copySend"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">其他接收单位</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="otherSend"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
		<td class="tdtitle" nowrap="nowrap">发布人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issuePerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>