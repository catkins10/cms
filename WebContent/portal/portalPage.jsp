<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/savePortalPage">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col>
		<col width="100%">
		<tr>
			<td nowrap="nowrap" align="right">页面名称：</td>
			<td><ext:field property="title" style="border: #bbbbbb 1px solid;"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">小窗口按钮：</td>
			<td><ext:field property="alwaysDisplayPortletButtons"/></td>
		</tr>
		<tr id="trStyles" style="display:none">
			<td nowrap="nowrap" align="right" valign="bottom" style="padding-bottom:14px">界面主题：</td>
			<td style="padding-top: 6px">
				<table border="0" cellspacing="0" cellpadding="0" width="100%" style="table-layout:fixed">
					<tr>
						<td valign="bottom" width="12px">
							<img src="icons/left.gif" onclick="document.getElementById('divStyles').scrollLeft -= document.getElementById('divStyles').clientWidth * 0.6">
						</td>
						<td>
							<div id="divStyles" style="width:100%; overflow:hidden; float:left">
								<script>
									var portalStyles = DialogUtils.getDialogOpener().portal.portalStyles;
									if(portalStyles && portalStyles.length>0) {
										document.write('<table id="tableStyles" border="0" cellspacing="3" cellpadding="2">');
										document.write('	<tr>');
										for(var i=0; i<portalStyles.length; i++) {
											document.write('	<td onclick="onClickStyle(id)" id="' + portalStyles[i].name + '" title="' + portalStyles[i].name + '" style="border:#aaaaaa 1px solid"><img src="' + portalStyles[i].icon + '"></td>');
										}
										document.write('	</tr>');
										document.write('</table>');
										if(portalStyles.length>1) {
											document.getElementById('trStyles').style.display = '';
										}
									}
								</script>
							</div>
						</td>
						<td valign="bottom" align="right" width="12px">
							<img src="icons/right.gif" onclick="document.getElementById('divStyles').scrollLeft += document.getElementById('divStyles').clientWidth * 0.6">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="bottom" style="padding-bottom:14px">页面布局：</td>
			<td style="padding-top: 6px">
				<table border="0" cellspacing="0" cellpadding="0" width="100%" style="table-layout:fixed">
					<tr>
						<td valign="bottom" width="12px">
							<img src="icons/left.gif" onclick="document.getElementById('divLayouts').scrollLeft -= document.getElementById('divLayouts').clientWidth * 0.6">
						</td>
						<td>
							<div id="divLayouts" style="width:100%; overflow:hidden; float:left">
								<table id="tableLayouts" border="0" cellspacing="3" cellpadding="2">
									<tr>
										<td onclick="onClickLayout(id)" id="3column_33.3_33.4_33.3" title="3列, 比例: 33.3% 33.3% 33.3%" style="border:#aaaaaa 1px solid"><img src="icons/3column_33_33_33.png"></td>
										<td onclick="onClickLayout(id)" id="3column_25_50_25" title="3列, 比例: 25% 50% 25%" style="border:#aaaaaa 1px solid"><img src="icons/3column_25_50_25.png"></td>
										<td onclick="onClickLayout(id)" id="3column_25_25_50" title="3列, 比例: 25% 25% 50%" style="border:#aaaaaa 1px solid"><img src="icons/3column_25_25_50.png"></td>
										<td onclick="onClickLayout(id)" id="3column_50_25_25" title="3列, 比例: 50% 25% 25%" style="border:#aaaaaa 1px solid"><img src="icons/3column_50_25_25.png"></td>
										<td onclick="onClickLayout(id)" id="3column_40_30_30" title="3列, 比例: 40% 30% 30%" style="border:#aaaaaa 1px solid"><img src="icons/3column_40_30_30.png"></td>
										<td onclick="onClickLayout(id)" id="3column_30_40_30" title="3列, 比例: 30% 40% 30%" style="border:#aaaaaa 1px solid"><img src="icons/3column_30_40_30.png"></td>
										<td onclick="onClickLayout(id)" id="3column_30_30_40" title="3列, 比例: 30% 30% 40%" style="border:#aaaaaa 1px solid"><img src="icons/3column_30_30_40.png"></td>
										<td onclick="onClickLayout(id)" id="2column_50_50" title="2列, 比例: 50% 50%" style="border:#aaaaaa 1px solid"><img src="icons/2column_50_50.png"></td>
										<td onclick="onClickLayout(id)" id="2column_60_40" title="2列, 比例: 60% 40%" style="border:#aaaaaa 1px solid"><img src="icons/2column_60_40.png"></td>
										<td onclick="onClickLayout(id)" id="2column_40_60" title="2列, 比例: 40% 60%" style="border:#aaaaaa 1px solid"><img src="icons/2column_40_60.png"></td>
										<td onclick="onClickLayout(id)" id="4column_25_25_25_25" title="4列, 比例: 25% 25% 25% 25%" style="border:#aaaaaa 1px solid"><img src="icons/4column_25_25_25_25.png"></td>
									</tr>
								</table>
							</div>
						</td>
						<td valign="bottom" align="right" width="12px">
							<img src="icons/right.gif" onclick="document.getElementById('divLayouts').scrollLeft += document.getElementById('divLayouts').clientWidth * 0.6">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<ext:equal value="create" property="act">
			<tr>
				<td nowrap="nowrap" align="right">加载指定分类内容：</td>
				<td><ext:field property="initPortletEntityCategory"/></td>
			</tr>
		</ext:equal>
	</table>
	<script>
		function selectCell(table, field) {
			if(!table) {
				return;
			}
			var cells = table.rows[0].cells;
			var selected = false;
			for(var i=0; i<cells.length; i++) {
				if(cells[i].id!=field.value) {
					cells[i].style.border = '#aaaaaa 1px solid';
				}
				else {
					selected = true;
					cells[i].style.border = '#2050ff 1px solid';
					var left = cells[i].offsetLeft;
					var right = left + cells[i].offsetWidth;
					var scrollLeft = table.parentNode.scrollLeft;
					var scrollRight = scrollLeft + table.parentNode.clientWidth;
					if(left<scrollLeft || left>scrollRight || right<scrollLeft || right>scrollRight) {
						table.parentNode.scrollLeft = right - table.parentNode.clientWidth + 20;
					}
				}
			}
			if(!selected) {
				cells[0].style.border = '#2050ff 1px solid';
				field.value = cells[0].id;
			}
		}
		function onClickStyle(styleId) {
			document.getElementsByName('style')[0].value = styleId;
			selectCell(document.getElementById('tableStyles'), document.getElementsByName('style')[0]);
		}
		function onClickLayout(layoutId) {
			document.getElementsByName('layout')[0].value = layoutId;
			selectCell(document.getElementById('tableLayouts'), document.getElementsByName('layout')[0]);
		}
		window.onload = function() {
			selectCell(document.getElementById('tableStyles'), document.getElementsByName('style')[0]);
			selectCell(document.getElementById('tableLayouts'), document.getElementsByName('layout')[0]);
		};
	</script>
</ext:form>