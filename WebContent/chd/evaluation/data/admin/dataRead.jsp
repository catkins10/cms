<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">评价细则</td>
		<td class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评价要点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="point"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资料名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资料类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="dataType" onclick="onDataTypeChanged()"/></td>
	</tr>
	<ext:equal value="0" property="dataType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">文件上传</td>
			<td class="tdcontent"><ext:field writeonly="true" property="attachments"/></td>
		</tr>
	</ext:equal>
	<ext:equal value="1" property="dataType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">链接地址</td>
			<td><a href="<ext:field writeonly="true" property="link"/>" target="_blank" class="tdcontent"><ext:field writeonly="true" property="link"/></a></td>
		</tr>
	</ext:equal>
	<ext:equal value="2" property="dataType">
		<tr>
			<td class="tdtitle" nowrap="nowrap">存放位置</td>
			<td class="tdcontent"><ext:field writeonly="true" property="place"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">提交时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">责任人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>