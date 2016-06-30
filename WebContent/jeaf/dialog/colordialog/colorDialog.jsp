<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/colorDialog">
	<script>
		function doOk(buttonName) {
			var value = document.getElementsByName('colorValue')[0].value;
			var script = document.getElementsByName("script")[0].value;
			if(script=="") {
				DialogUtils.getDialogArguments().call(null, value);	
			}
			else {
				DialogUtils.getDialogOpener().setTimeout(script.replace(/{colorValue}/g, value), 300);
			}
			DialogUtils.closeDialog();
		}
		window.onload = function() {
			document.getElementById('colorPreview').style.backgroundColor = document.getElementsByName('colorValue')[0].value;
			document.getElementById('selectedColorPreview').style.backgroundColor = document.getElementsByName('colorValue')[0].value;
		}
		function mouseMoveOnColorChart(event) {
			if(!event) {
				event = window.event;
			}
			document.getElementById('colorPreview').style.backgroundColor = event.srcElement.bgColor;
		}
		function clickOnColorChart(event) {
			if(!event) {
				event = window.event;
			}
			document.getElementsByName('colorValue')[0].value = event.srcElement.bgColor;
			document.getElementById('selectedColorPreview').style.backgroundColor = event.srcElement.bgColor;
		}
	</script>
	<style>
		#colorchart {
			padding: 0px;
			border: 0px currentColor;
			border-image: none;
			border-collapse: collapse;
		}
		#colorchart td {
			width: 12px;
			height: 16px;
		}
	</style>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td>
				<table id="colorChart" onmousemove="mouseMoveOnColorChart(event)" onclick="clickOnColorChart(event)">
					<tr><td bgcolor="#fbefef"></td><td bgcolor="#fbf2ef"></td><td bgcolor="#fbf5ef"></td><td bgcolor="#fbf8ef"></td><td bgcolor="#fbfbef"></td><td bgcolor="#f8fbef"></td><td bgcolor="#f5fbef"></td><td bgcolor="#f2fbef"></td><td bgcolor="#effbef"></td><td bgcolor="#effbf2"></td><td bgcolor="#effbf5"></td><td bgcolor="#effbf8"></td><td bgcolor="#effbfb"></td><td bgcolor="#eff8fb"></td><td bgcolor="#eff5fb"></td><td bgcolor="#eff2fb"></td><td bgcolor="#efeffb"></td><td bgcolor="#f2effb"></td><td bgcolor="#f5effb"></td><td bgcolor="#f8effb"></td><td bgcolor="#fbeffb"></td><td bgcolor="#fbeff8"></td><td bgcolor="#fbeff5"></td><td bgcolor="#fbeff2"></td><td bgcolor="#ffffff"></td></tr>
					<tr><td bgcolor="#f8e0e0"></td><td bgcolor="#f8e6e0"></td><td bgcolor="#f8ece0"></td><td bgcolor="#f7f2e0"></td><td bgcolor="#f7f8e0"></td><td bgcolor="#f1f8e0"></td><td bgcolor="#ecf8e0"></td><td bgcolor="#e6f8e0"></td><td bgcolor="#e0f8e0"></td><td bgcolor="#e0f8e6"></td><td bgcolor="#e0f8ec"></td><td bgcolor="#e0f8f1"></td><td bgcolor="#e0f8f7"></td><td bgcolor="#e0f2f7"></td><td bgcolor="#e0ecf8"></td><td bgcolor="#e0e6f8"></td><td bgcolor="#e0e0f8"></td><td bgcolor="#e6e0f8"></td><td bgcolor="#ece0f8"></td><td bgcolor="#f2e0f7"></td><td bgcolor="#f8e0f7"></td><td bgcolor="#f8e0f1"></td><td bgcolor="#f8e0ec"></td><td bgcolor="#f8e0e6"></td><td bgcolor="#fafafa"></td></tr>
					<tr><td bgcolor="#f6cece"></td><td bgcolor="#f6d8ce"></td><td bgcolor="#f6e3ce"></td><td bgcolor="#f5ecce"></td><td bgcolor="#f5f6ce"></td><td bgcolor="#ecf6ce"></td><td bgcolor="#e3f6ce"></td><td bgcolor="#d8f6ce"></td><td bgcolor="#cef6ce"></td><td bgcolor="#cef6d8"></td><td bgcolor="#cef6e3"></td><td bgcolor="#cef6ec"></td><td bgcolor="#cef6f5"></td><td bgcolor="#ceecf5"></td><td bgcolor="#cee3f6"></td><td bgcolor="#ced8f6"></td><td bgcolor="#cecef6"></td><td bgcolor="#d8cef6"></td><td bgcolor="#e3cef6"></td><td bgcolor="#eccef5"></td><td bgcolor="#f6cef5"></td><td bgcolor="#f6ceec"></td><td bgcolor="#f6cee3"></td><td bgcolor="#f6ced8"></td><td bgcolor="#f2f2f2"></td></tr>
					<tr><td bgcolor="#f5a9a9"></td><td bgcolor="#f5bca9"></td><td bgcolor="#f5d0a9"></td><td bgcolor="#f3e2a9"></td><td bgcolor="#f2f5a9"></td><td bgcolor="#e1f5a9"></td><td bgcolor="#d0f5a9"></td><td bgcolor="#bcf5a9"></td><td bgcolor="#a9f5a9"></td><td bgcolor="#a9f5bc"></td><td bgcolor="#a9f5d0"></td><td bgcolor="#a9f5e1"></td><td bgcolor="#a9f5f2"></td><td bgcolor="#a9e2f3"></td><td bgcolor="#a9d0f5"></td><td bgcolor="#a9bcf5"></td><td bgcolor="#a9a9f5"></td><td bgcolor="#bca9f5"></td><td bgcolor="#d0a9f5"></td><td bgcolor="#e2a9f3"></td><td bgcolor="#f5a9f2"></td><td bgcolor="#f5a9e1"></td><td bgcolor="#f5a9d0"></td><td bgcolor="#f5a9bc"></td><td bgcolor="#e6e6e6"></td></tr>
					<tr><td bgcolor="#f78181"></td><td bgcolor="#f79f81"></td><td bgcolor="#f7be81"></td><td bgcolor="#f5da81"></td><td bgcolor="#f3f781"></td><td bgcolor="#d8f781"></td><td bgcolor="#bef781"></td><td bgcolor="#9ff781"></td><td bgcolor="#81f781"></td><td bgcolor="#81f79f"></td><td bgcolor="#81f7be"></td><td bgcolor="#81f7d8"></td><td bgcolor="#81f7f3"></td><td bgcolor="#81daf5"></td><td bgcolor="#81bef7"></td><td bgcolor="#819ff7"></td><td bgcolor="#8181f7"></td><td bgcolor="#9f81f7"></td><td bgcolor="#be81f7"></td><td bgcolor="#da81f5"></td><td bgcolor="#f781f3"></td><td bgcolor="#f781d8"></td><td bgcolor="#f781be"></td><td bgcolor="#f7819f"></td><td bgcolor="#d8d8d8"></td></tr>
					<tr><td bgcolor="#fa5858"></td><td bgcolor="#fa8258"></td><td bgcolor="#faac58"></td><td bgcolor="#f7d358"></td><td bgcolor="#f4fa58"></td><td bgcolor="#d0fa58"></td><td bgcolor="#acfa58"></td><td bgcolor="#82fa58"></td><td bgcolor="#58fa58"></td><td bgcolor="#58fa82"></td><td bgcolor="#58faac"></td><td bgcolor="#58fad0"></td><td bgcolor="#58faf4"></td><td bgcolor="#58d3f7"></td><td bgcolor="#58acfa"></td><td bgcolor="#5882fa"></td><td bgcolor="#5858fa"></td><td bgcolor="#8258fa"></td><td bgcolor="#ac58fa"></td><td bgcolor="#d358f7"></td><td bgcolor="#fa58f4"></td><td bgcolor="#fa58d0"></td><td bgcolor="#fa58ac"></td><td bgcolor="#fa5882"></td><td bgcolor="#bdbdbd"></td></tr>
					<tr><td bgcolor="#fe2e2e"></td><td bgcolor="#fe642e"></td><td bgcolor="#fe9a2e"></td><td bgcolor="#facc2e"></td><td bgcolor="#f7fe2e"></td><td bgcolor="#c8fe2e"></td><td bgcolor="#9afe2e"></td><td bgcolor="#64fe2e"></td><td bgcolor="#2efe2e"></td><td bgcolor="#2efe64"></td><td bgcolor="#2efe9a"></td><td bgcolor="#2efec8"></td><td bgcolor="#2efef7"></td><td bgcolor="#2eccfa"></td><td bgcolor="#2e9afe"></td><td bgcolor="#2e64fe"></td><td bgcolor="#2e2efe"></td><td bgcolor="#642efe"></td><td bgcolor="#9a2efe"></td><td bgcolor="#cc2efa"></td><td bgcolor="#fe2ef7"></td><td bgcolor="#fe2ec8"></td><td bgcolor="#fe2e9a"></td><td bgcolor="#fe2e64"></td><td bgcolor="#a4a4a4"></td></tr>
					<tr><td bgcolor="#ff0000"></td><td bgcolor="#ff4000"></td><td bgcolor="#ff8000"></td><td bgcolor="#ffbf00"></td><td bgcolor="#ffff00"></td><td bgcolor="#bfff00"></td><td bgcolor="#80ff00"></td><td bgcolor="#40ff00"></td><td bgcolor="#00ff00"></td><td bgcolor="#00ff40"></td><td bgcolor="#00ff80"></td><td bgcolor="#00ffbf"></td><td bgcolor="#00ffff"></td><td bgcolor="#00bfff"></td><td bgcolor="#0080ff"></td><td bgcolor="#0040ff"></td><td bgcolor="#0000ff"></td><td bgcolor="#4000ff"></td><td bgcolor="#8000ff"></td><td bgcolor="#bf00ff"></td><td bgcolor="#ff00ff"></td><td bgcolor="#ff00bf"></td><td bgcolor="#ff0080"></td><td bgcolor="#ff0040"></td><td bgcolor="#848484"></td></tr>
					<tr><td bgcolor="#df0101"></td><td bgcolor="#df3a01"></td><td bgcolor="#df7401"></td><td bgcolor="#dba901"></td><td bgcolor="#d7df01"></td><td bgcolor="#a5df00"></td><td bgcolor="#74df00"></td><td bgcolor="#3adf00"></td><td bgcolor="#01df01"></td><td bgcolor="#01df3a"></td><td bgcolor="#01df74"></td><td bgcolor="#01dfa5"></td><td bgcolor="#01dfd7"></td><td bgcolor="#01a9db"></td><td bgcolor="#0174df"></td><td bgcolor="#013adf"></td><td bgcolor="#0101df"></td><td bgcolor="#3a01df"></td><td bgcolor="#7401df"></td><td bgcolor="#a901db"></td><td bgcolor="#df01d7"></td><td bgcolor="#df01a5"></td><td bgcolor="#df0174"></td><td bgcolor="#df013a"></td><td bgcolor="#6e6e6e"></td></tr>
					<tr><td bgcolor="#b40404"></td><td bgcolor="#b43104"></td><td bgcolor="#b45f04"></td><td bgcolor="#b18904"></td><td bgcolor="#aeb404"></td><td bgcolor="#86b404"></td><td bgcolor="#5fb404"></td><td bgcolor="#31b404"></td><td bgcolor="#04b404"></td><td bgcolor="#04b431"></td><td bgcolor="#04b45f"></td><td bgcolor="#04b486"></td><td bgcolor="#04b4ae"></td><td bgcolor="#0489b1"></td><td bgcolor="#045fb4"></td><td bgcolor="#0431b4"></td><td bgcolor="#0404b4"></td><td bgcolor="#3104b4"></td><td bgcolor="#5f04b4"></td><td bgcolor="#8904b1"></td><td bgcolor="#b404ae"></td><td bgcolor="#b40486"></td><td bgcolor="#b4045f"></td><td bgcolor="#b40431"></td><td bgcolor="#585858"></td></tr>
					<tr><td bgcolor="#8a0808"></td><td bgcolor="#8a2908"></td><td bgcolor="#8a4b08"></td><td bgcolor="#886a08"></td><td bgcolor="#868a08"></td><td bgcolor="#688a08"></td><td bgcolor="#4b8a08"></td><td bgcolor="#298a08"></td><td bgcolor="#088a08"></td><td bgcolor="#088a29"></td><td bgcolor="#088a4b"></td><td bgcolor="#088a68"></td><td bgcolor="#088a85"></td><td bgcolor="#086a87"></td><td bgcolor="#084b8a"></td><td bgcolor="#08298a"></td><td bgcolor="#08088a"></td><td bgcolor="#29088a"></td><td bgcolor="#4b088a"></td><td bgcolor="#6a0888"></td><td bgcolor="#8a0886"></td><td bgcolor="#8a0868"></td><td bgcolor="#8a084b"></td><td bgcolor="#8a0829"></td><td bgcolor="#424242"></td></tr>
					<tr><td bgcolor="#610b0b"></td><td bgcolor="#61210b"></td><td bgcolor="#61380b"></td><td bgcolor="#5f4c0b"></td><td bgcolor="#5e610b"></td><td bgcolor="#4b610b"></td><td bgcolor="#38610b"></td><td bgcolor="#21610b"></td><td bgcolor="#0b610b"></td><td bgcolor="#0b6121"></td><td bgcolor="#0b6138"></td><td bgcolor="#0b614b"></td><td bgcolor="#0b615e"></td><td bgcolor="#0b4c5f"></td><td bgcolor="#0b3861"></td><td bgcolor="#0b2161"></td><td bgcolor="#0b0b61"></td><td bgcolor="#210b61"></td><td bgcolor="#380b61"></td><td bgcolor="#4c0b5f"></td><td bgcolor="#610b5e"></td><td bgcolor="#610b4b"></td><td bgcolor="#610b38"></td><td bgcolor="#610b21"></td><td bgcolor="#2e2e2e"></td></tr>
					<tr><td bgcolor="#3b0b0b"></td><td bgcolor="#3b170b"></td><td bgcolor="#3b240b"></td><td bgcolor="#3a2f0b"></td><td bgcolor="#393b0b"></td><td bgcolor="#2e3b0b"></td><td bgcolor="#243b0b"></td><td bgcolor="#173b0b"></td><td bgcolor="#0b3b0b"></td><td bgcolor="#0b3b17"></td><td bgcolor="#0b3b24"></td><td bgcolor="#0b3b2e"></td><td bgcolor="#0b3b39"></td><td bgcolor="#0b2f3a"></td><td bgcolor="#0b243b"></td><td bgcolor="#0b173b"></td><td bgcolor="#0b0b3b"></td><td bgcolor="#170b3b"></td><td bgcolor="#240b3b"></td><td bgcolor="#2f0b3a"></td><td bgcolor="#3b0b39"></td><td bgcolor="#3b0b2e"></td><td bgcolor="#3b0b24"></td><td bgcolor="#3b0b17"></td><td bgcolor="#1c1c1c"></td></tr>
					<tr><td bgcolor="#2a0a0a"></td><td bgcolor="#2a120a"></td><td bgcolor="#2a1b0a"></td><td bgcolor="#29220a"></td><td bgcolor="#292a0a"></td><td bgcolor="#222a0a"></td><td bgcolor="#1b2a0a"></td><td bgcolor="#122a0a"></td><td bgcolor="#0a2a0a"></td><td bgcolor="#0a2a12"></td><td bgcolor="#0a2a1b"></td><td bgcolor="#0a2a22"></td><td bgcolor="#0a2a29"></td><td bgcolor="#0a2229"></td><td bgcolor="#0a1b2a"></td><td bgcolor="#0a122a"></td><td bgcolor="#0a0a2a"></td><td bgcolor="#120a2a"></td><td bgcolor="#1b0a2a"></td><td bgcolor="#220a29"></td><td bgcolor="#2a0a29"></td><td bgcolor="#2a0a22"></td><td bgcolor="#2a0a1b"></td><td bgcolor="#2a0a12"></td><td bgcolor="#151515"></td></tr>
					<tr><td bgcolor="#190707"></td><td bgcolor="#190b07"></td><td bgcolor="#191007"></td><td bgcolor="#181407"></td><td bgcolor="#181907"></td><td bgcolor="#141907"></td><td bgcolor="#101907"></td><td bgcolor="#0b1907"></td><td bgcolor="#071907"></td><td bgcolor="#07190b"></td><td bgcolor="#071910"></td><td bgcolor="#071914"></td><td bgcolor="#071918"></td><td bgcolor="#071418"></td><td bgcolor="#071019"></td><td bgcolor="#070b19"></td><td bgcolor="#070719"></td><td bgcolor="#0b0719"></td><td bgcolor="#100719"></td><td bgcolor="#140718"></td><td bgcolor="#190718"></td><td bgcolor="#190714"></td><td bgcolor="#190710"></td><td bgcolor="#19070b"></td><td bgcolor="#000000"></td></tr>
				</table>
			</td>
			<td width="80px" valign="top">
				<table border="0" width="100%" cellspacing="0" cellpadding="2px">
					<tr>
						<td>预览:</td>
					</tr>
					<tr>
						<td><div style="height:76px; border:#aaaaaa 1px solid" id="colorPreview">&nbsp;</div></td>
					</tr>
					<tr>
						<td style="padding-top:6px">选定:</td>
					</tr>
					<tr>
						<td><div style="height:22px; border:#aaaaaa 1px solid" id="selectedColorPreview">&nbsp;</div></td>
					</tr>
					<tr>
						<td><ext:field property="colorValue" onchange="document.getElementById('selectedColorPreview').style.backgroundColor=value"/></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</ext:form>