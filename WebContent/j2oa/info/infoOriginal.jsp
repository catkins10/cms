<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:empty property="combineInfos">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">标题</td>
			<td class="tdcontent"><ext:field writeonly="true" property="infoReceive.subject"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
			<td class="tdcontent" colspan="3" style="font-size:14px; line-height: 20px; text-indent:28px;"><ext:field writeonly="true" property="infoReceive.body"/></td>
		</tr>
	</table>
</ext:empty>
<ext:notEmpty property="combineInfos">
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr height="23px" valign="bottom" align="center">
			<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
			<td class="tdtitle" nowrap="nowrap" width="100%">标题</td>
			<td class="tdtitle" nowrap="nowrap" width="50px">简讯</td>
			<td class="tdtitle" nowrap="nowrap" width="200px">投稿单位</td>
			<td class="tdtitle" nowrap="nowrap" width="110px">投稿时间</td>
		</tr>
		<ext:iterate id="info" indexId="infoIndex" property="combineInfos">
			<tr valign="top" align="center">
				<td class="tdcontent"><ext:writeNumber name="infoIndex" plus="1"/></td>
				<td class="tdcontent" align="left">
					<a href="javascript:PageUtils.editrecord('j2oa/info', 'info', '<ext:field writeonly="true" name="info" property="id"/>')"><ext:field writeonly="true" name="info" property="subject"/></a>
				</td>
				<td class="tdcontent"><ext:equal value="1" name="info" property="isBrief">√</ext:equal></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="info" property="infoReceive.fromUnit"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="info" property="infoReceive.contributeTime"/></td>
			</tr>
		</ext:iterate>
	</table>
</ext:notEmpty>