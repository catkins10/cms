<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveFileConfig">
 	<ext:tab>
   		<ext:tabBody tabId="basic">
		   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">适用的项目分类</td>
					<td class="tdcontent"><ext:field property="categoryArray"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">适用的招标内容</td>
					<td class="tdcontent"><ext:field property="procedureArray"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">适用的地区</td>
					<td class="tdcontent"><ext:field property="cityArray"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">全部齐全才能继续</td>
					<td class="tdcontent"><ext:field property="needFull"/></td>
				</tr>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="prophaseFiles">
			<table width="100%" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td nowrap="nowrap">序号:</td>
					<td nowrap="nowrap" width="50px"><ext:field property="prophaseFileSn"/></td>
					<td nowrap="nowrap">资料名称:</td>
					<td nowrap="nowrap" width="200px"><ext:field property="prophaseFileName"/></td>
					<td nowrap="nowrap"><input type="button" class="button" value="添加" onclick="FormUtils.doAction('addProphaseFile')"></td>
					<td width="100%"></td>
				</tr>
			</table>
			<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
				<col valign="middle" align="center" width="50px">
				<col valign="middle" align="left" width="100%">
				<col valign="middle" align="center" width="50px">
				<tr height="23px" align="center" bordercolor="E1E8F5" valign="bottom">
					<td align="center" class="tdtitle" nowrap="nowrap">序号</td>
					<td align="center" class="tdtitle">名称</td>
					<td align="center" class="tdtitle"></td>
				</tr>
				<ext:select id="file" property="items" select="fileType" value="前期资料">
					<tr>
						<td class="tdcontent" nowrap="nowrap"><ext:write name="file" property="sn" format="#.##"/></td>
						<td class="tdcontent"><ext:write name="file" property="name"/></td>
						<td class="tdcontent" nowrap="nowrap"><a href="#" onclick="FormUtils.doAction('deleteProphaseFile', 'prophaseFileId=<ext:write name="file" property="id"/>')">删除</a></td>
					</tr>
				</ext:select>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="archiveFiles">
			<table width="100%" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td nowrap="nowrap">序号:</td>
					<td nowrap="nowrap" width="50px"><ext:field property="archiveFileSn"/></td>
					<td nowrap="nowrap">资料名称:</td>
					<td nowrap="nowrap" width="200px"><ext:field property="archiveFileName"/></td>
					<td nowrap="nowrap">分类:</td>
					<td nowrap="nowrap" width="200px"><ext:field property="archiveFileCategory"/></td>
					<td nowrap="nowrap"><input type="button" class="button" value="添加" onclick="FormUtils.doAction('addArchiveFile')"></td>
					<td width="100%"></td>
				</tr>
			</table>
			<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
				<col valign="middle" align="center" width="50px">
				<col valign="middle" align="left" width="50%">
				<col valign="middle" align="left" width="50%">
				<col valign="middle" align="center" width="50px">
				<tr height="23px" align="center" bordercolor="E1E8F5" valign="bottom">
					<td align="center" class="tdtitle" nowrap="nowrap">序号</td>
					<td align="center" class="tdtitle">名称</td>
					<td align="center" class="tdtitle">分类</td>
					<td align="center" class="tdtitle"></td>
				</tr>
				<ext:select id="file" property="items" select="fileType" value="归档资料">
					<tr>
						<td class="tdcontent" nowrap="nowrap"><ext:write name="file" property="sn" format="#.##"/></td>
						<td class="tdcontent"><ext:write name="file" property="name"/></td>
						<td class="tdcontent"><ext:write name="file" property="fileCategory"/></td>
						<td class="tdcontent" nowrap="nowrap"><a href="#" onclick="FormUtils.doAction('deleteArchiveFile', 'archiveFileId=<ext:write name="file" property="id"/>')">删除</a></td>
					</tr>
				</ext:select>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:form>