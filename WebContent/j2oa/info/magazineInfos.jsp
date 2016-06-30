<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function selectUseInfos(magazineColumn) {
		var useInfoRows = document.getElementById('useInfos_' + magazineColumn).rows;
		var useInfoIds = "";
		var useInfoSubjects = "";
		for(var i=1; i<useInfoRows.length; i++) {
			useInfoIds += (useInfoIds=="" ? "" : ";") + useInfoRows[i].id;
			useInfoSubjects += (useInfoSubjects=="" ? "" : ";") + useInfoRows[i].cells[1].getElementsByTagName('a')[0].innerHTML.replace(/;/g, '');
		}
		document.getElementsByName("useInfoIds")[0].value = useInfoIds;
		document.getElementsByName("useInfoSubjects")[0].value = useInfoSubjects;
		document.getElementsByName("currentColumn")[0].value = magazineColumn; //当前栏目
		DialogUtils.openSelectDialog('j2oa/info', 'selectUseInfos', '85%', '85%', true, 'useInfoIds{id},useInfoSubjects{subject|标题|100%}', 'FormUtils.doAction("saveMagazineUseInfos");', '', '', ';', false, 'magazineDefineId=<ext:write property="defineId"/>');
	}
</script>
<ext:iterate id="magazineColumn" indexId="magazineColumnIndex" property="magazineColumns">
	<div style="padding-bottom: 5px; <ext:notEqual value="0" name="magazineColumnIndex">padding-top: 12px</ext:notEqual>">
		<b><ext:write name="magazineColumn" property="columnName"/></b>
		<ext:equal value="true" name="editabled">
			<input type="button" class="button" value="添加或删除稿件" onclick="selectUseInfos('<ext:write name="magazineColumn" property="columnName"/>')"/>
		</ext:equal>
	</div>
	<table id="useInfos_<ext:write name="magazineColumn" property="columnName"/>" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr align="center">
			<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
			<td class="tdtitle" nowrap="nowrap" width="100%">标题</td>
			<td class="tdtitle" nowrap="nowrap" width="50px">简讯</td>
			<td class="tdtitle" nowrap="nowrap" width="200px">投稿单位</td>
			<td class="tdtitle" nowrap="nowrap" width="110px">投稿时间</td>
		</tr>
<%		int index = 1; %>
		<ext:iterate id="useInfo" indexId="infoIndex" name="magazineColumn" property="columnUseInfos">
			<tr valign="top" align="center" id="<ext:write name="useInfo" property="id"/>">
				<td class="tdcontent"><%=index++%></td>
				<td class="tdcontent" align="left">
					<a href="javascript:PageUtils.editrecord('j2oa/info', 'info', '<ext:field writeonly="true" name="useInfo" property="id"/>')"><ext:field writeonly="true" name="useInfo" property="subject"/></a>
				</td>
				<td class="tdcontent"></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="useInfo" property="infoReceive.fromUnit"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="useInfo" property="infoReceive.contributeTime"/></td>
			</tr>
		</ext:iterate>
		<ext:iterate id="useInfo" indexId="infoIndex" name="magazineColumn" property="columnUseBriefs">
			<tr valign="top" align="center" id="<ext:write name="useInfo" property="id"/>">
				<td class="tdcontent"><%=index++%></td>
				<td class="tdcontent" align="left">
					<a href="javascript:PageUtils.editrecord('j2oa/info', 'info', '<ext:field writeonly="true" name="useInfo" property="id"/>')"><ext:field writeonly="true" name="useInfo" property="subject"/></a>
				</td>
				<td class="tdcontent">√</td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="useInfo" property="infoReceive.fromUnit"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="useInfo" property="infoReceive.contributeTime"/></td>
			</tr>
		</ext:iterate>
	</table>
</ext:iterate>