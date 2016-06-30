<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">标题</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在目录</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
		<td class="tdtitle" nowrap="nowrap">文件字</td>
		<td class="tdcontent"><ext:field writeonly="true" property="docmark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">指定访问者</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="dataVisitors.visitorNames"/></td>
		<td class="tdtitle" nowrap="nowrap">成文日期</td>
		<td class="tdcontent"><ext:field writeonly="true" property="generateDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">秘密等级</td>
		<td class="tdcontent"><ext:field writeonly="true" property="secureLevel"/></td>
		<td class="tdtitle" nowrap="nowrap">文件类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="dataType"/></td>
		<td class="tdtitle" nowrap="nowrap">来文单位</td>
		<td class="tdcontent"><ext:field writeonly="true" property="fromUnit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">附注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">附件</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="body"/></td>
	</tr>
</table>