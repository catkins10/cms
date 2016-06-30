<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<span id="projectJobholers">
	<ext:iterate id="jobholder" property="jobholders">
		<span style="display:inline-block; padding-right:8px; cursor:pointer;" onclick="editProjectJobholder(null, this)">
			<ext:write name="jobholder" property="jobholderCategory"/><ext:notEmpty name="jobholder" property="qualifications">(<ext:write name="jobholder" property="qualifications"/>)</ext:notEmpty>:<ext:write name="jobholder" property="jobholderNumber"/>人<img src="<%=request.getContextPath()%>/bidding/project/images/delete.gif" align="absmiddle" onclick="this.parentNode.parentNode.removeChild(this.parentNode)">
			<input type="hidden" name="jobholderCategory" value="<ext:write name="jobholder" property="jobholderCategory"/>">
			<input type="hidden" name="jobholderQualifications" value="<ext:write name="jobholder" property="qualifications"/>">
			<input type="hidden" name="jobholderNumber" value="<ext:write name="jobholder" property="jobholderNumber"/>">
		</span>
	</ext:iterate>
</span>
<input type="button" class="button" value="添加" onclick="addProjectJobholder(this)">

<script>
function addProjectJobholder(button) { //添加项目从业人员
	PopupMenu.popupMenu("项目经理\0注册建造师\0注册造价师\0注册监理师\0五大员", function(menuItemId, menuItemTitle) {
		editProjectJobholder(menuItemId);
	}, button, 120, 'left');
}
function editProjectJobholder(category, src) {
	if(!category) {
		category = src.getElementsByTagName('input')[0].value;
	}
	var inputs = [];
	var itemsText = "";
	var inputMode = 'multibox';
	var title = "等级";
	var required = false;
	if(category=="项目经理") {
		itemsText = "一级\\0 二级\\0 三级";
	}
	else if(category=="注册建造师") {
		itemsText = "一级\\0 二级";
	}
	else if(category=="注册造价师") {
		inputMode = "";
	}
	else if(category=="注册监理师") {
		itemsText = "初级\\0 中级\\0 高级";
	}
	else if(category=="五大员") {
		itemsText = "预算员\\0 施工员\\0 质检员\\0 安全员\\0 材料员";
		title = "岗位";
		inputMode = 'radio';
		required = true;
	}
	if(inputMode!="") {
		inputs.push({name:"qualification", title:title, defaultValue:(src ? src.getElementsByTagName('input')[1].value.replace(/,/g, '\\0') : ""), type:"string", length:10, inputMode:inputMode, itemsText:itemsText, required:required});
	}
	inputs.push({name:"number", title:"人数", defaultValue:(src ? src.getElementsByTagName('input')[2].value : ""), length:3, inputMode:'dropdown', itemsText:"1\\0 2\\0 3\\0 4\\0 5\\0 6\\0 7\\0 8\\0 9\\0 10\\0 20\\0 30", required:"true"});
	DialogUtils.openInputDialog(category, inputs, 400, 300, '', function(value) {
		var number = Number(value.number);
		if(isNaN(number) || number<=0) {
			return;
		}
		var jobholder = src ? src : document.createElement('span');
		jobholder.style.cssText = 'display:inline-block; padding-right:8px; cursor:pointer;';
		jobholder.onclick = function() {
			editProjectJobholder(null, this);
		};
		jobholder.innerHTML = category + (!value.qualification || value.qualification=="" ? "" : "(" + value.qualification + ")") + ":" + number + "人" +
							  '<img src="<%=request.getContextPath()%>/bidding/project/images/delete.gif" align="absmiddle" onclick="this.parentNode.parentNode.removeChild(this.parentNode)">' +
							  '<input type="hidden" name="jobholderCategory" value="' + category + '">' +
							  '<input type="hidden" name="jobholderQualifications" value="' + (!value.qualification ? '' : value.qualification) + '">' +
							  '<input type="hidden" name="jobholderNumber" value="' + number + '">';
		if(!src) {
			document.getElementById('projectJobholers').appendChild(jobholder);
		}
	});
}
</script>