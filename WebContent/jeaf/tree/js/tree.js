var MAX_TREE_NODES = 200; //节点数量上限
/*树
 * 1.引用到的样式:treeExpand/树展开,treeCollapse/树折叠,treeLeaf/叶节点(没有子节点的节点),nodePadding/节点缩进,
 *				rootImgExpand/展开时根节点图片,rootImgCollapse/折叠时根节点图片,
 *			 nodeText/节点文字的样式,nodeTextSelected/选中时节点文字样式
 * 2.事件:onGetUrlForListChildNodes(nodeId)/获得获取子节点的URL, onNodeSeleced(nodeId, nodeText)/当节点被选中
 */
Tree = function(treeId, rootNodeText, rootNodeType, rootNodeImage, rootNodeExpandImage, parentElement, collapse, extendProperties) {
	this.treeNodes = [];
	this.selectedNode = null; //选中的节点
	this.parentElement = parentElement;
	//注册到节点列表
	this._registTreeNode(true, "", treeId, rootNodeText, rootNodeType, rootNodeImage, rootNodeExpandImage, true, true, extendProperties);
	//创建目录树
	this.treeTable = document.createElement("table");
	this.treeTable.border = 0;
	this.treeTable.cellSpacing = 0;
	this.treeTable.cellPadding = 0;
	this.treeTable.id = "tree";
	this.treeTable.className = "tree";
	parentElement.appendChild(this.treeTable);
	var rootNode = this.treeTable.insertRow(-1).insertCell(-1);
	rootNode.id = treeId;
	rootNode.name = "rootTreeNode";
	rootNode.noWrap = true;
	rootNode.vAlign = "top";
	//创建图标SPAN
	var rootImg = document.createElement("span");
	rootImg.title = rootNodeText.replace(/<(.*?)>/gi, '');
	rootImg.className = "rootImg" + (collapse ? "Collapse": "Expand");
	rootImg.style.cssText = rootNodeImage=='' ? "display:none" : "background-image:url(" + (collapse ? rootNodeImage : (rootNodeExpandImage=='' ? rootNodeImage : rootNodeExpandImage)) + ")";
	rootImg.innerHTML = "&nbsp;";
	rootNode.appendChild(rootImg);
	//创建文本SPAN
	var rootLabel = document.createElement("span");
	rootLabel.title = rootImg.title;
	rootLabel.innerHTML = rootNodeText;
	rootLabel.className = "nodeText";
	rootNode.appendChild(rootLabel);
	//事件处理
	var tree = this;
	rootImg.onselectstart = rootLabel.onselectstart = function(event) {
		return false;
	};
	rootImg.onclick = rootLabel.onclick = function(event) {
		tree.selectNode(this.parentNode);
	};
	rootImg.ondblclick = rootLabel.ondblclick = function(event) {
		tree._dblClickNode(this.parentNode);
	};
	rootImg.onmousedown = rootLabel.onmousedown = function(event) {
		tree._mouseDownNode(this.parentNode, event);
	};
	var childNodesCell = this.treeTable.insertRow(-1).insertCell(-1);
	childNodesCell.parentNode.style.display = (collapse ? "none" : "");
	if(!window.trees) {
		window.trees = [];
	}
	this.treeIndex = window.trees.length;
	window.trees.push(this);
};
//事件:节点被选中
Tree.prototype.onNodeSeleced = function(nodeId, nodeText, nodeType, leafNode) {

};
//事件:节点被双击
Tree.prototype.onDblClickNode = function(nodeId, nodeText, nodeType, leafNode) {

};
//事件:鼠标右键点击节点
Tree.prototype.onRightClickNode = function(nodeId, nodeText, nodeType, leafNode) {

};
//事件:获取URL,以得到子节点列表
Tree.prototype.onGetUrlForListChildNodes = function(nodeId, nodeType) {

};
//增加节点
Tree.prototype.appendNode = function(parentNodeId, nodeId, nodeText, nodeType, nodeImage, nodeExpandImage, hasChildNodes, expandTree, extendProperties) {
	var treeNode = this._registTreeNode(false, parentNodeId, nodeId, nodeText, nodeType, nodeImage, nodeExpandImage, hasChildNodes, expandTree, extendProperties);
	if(treeNode) {
		this._writeTreeNode(treeNode); //输出节点
	}
};
//删除树
Tree.prototype.remove = function() {
	this.treeTable.parentNode.removeChild(this.treeTable);
};
//注册节点对象
Tree.prototype._registTreeNode = function(isRootNode, parentNodeId, nodeId, nodeText, nodeType, nodeImage, nodeExpandImage, hasChildNodes, expandTree, extendProperties) {
	for(var i=0; i<this.treeNodes.length; i++) {
		if(this.treeNodes[i].nodeId==nodeId && this.treeNodes[i].parentNodeId==parentNodeId) {
			return;
		}
	}
	var treeNode = {parentNodeId: parentNodeId,
					nodeId: nodeId,
					nodeText: nodeText,
					nodeType: nodeType,
					nodeImage: nodeImage,
					nodeExpandImage: nodeExpandImage,
					hasChildNodes: hasChildNodes,
					expandTree: expandTree,
					extendProperties: extendProperties};
	this.treeNodes.push(treeNode);
	return treeNode;
};
//输出节点
Tree.prototype._writeTreeNode = function(treeNode) {
	try {
		var parentNode = DomUtils.getElement(this.treeTable, 'td', treeNode.parentNodeId);
		//检查父节点是否已经展开
		var spans = parentNode.getElementsByTagName("span");
		if(spans[0].className && parentNode.name!='rootTreeNode' && spans[0].className.indexOf("Minus", "")==-1) { //不是根节点,且未展开
			return; //不直接显示子节点,展开父节点时再显示
		}
		parentNode = parentNode.parentNode.nextSibling.cells[0];
		var childTree = parentNode.getElementsByTagName("table")[0];
		if(!childTree) { //创建子树
			//删除提示信息
			if(parentNode.innerHTML.indexOf("正在加载...") != - 1) {
				parentNode.innerHTML = "";
			}
			//检查节点数量是否超过上限,如果超过,删除同一层的其他分支的内容,从而避免占用过多的内存
			if(DomUtils.getOffsetParent(parentNode).id != "tree" && this.treeTable.getElementsByTagName("td").length / 2 > MAX_TREE_NODES) {
				this._removeNodesOfOtherBranch(parentNode.parentNode.previousSibling.cells[0]);
			}
			childTree = document.createElement("table");
			childTree.border = 0;
			childTree.cellSpacing = 0;
			childTree.cellPadding = 0;
			parentNode.appendChild(childTree);
		}
		else { 
			//检查下级节点中是否有相同ID的节点
			var k = childTree.rows.length - 1;
			for(; k>=0 && childTree.rows[k].cells[0].id!=treeNode.nodeId; k--);
			if(k>=0) {
				return;
			}
		}
		if(childTree.rows.length>0) {
			var prevNode = childTree.rows[childTree.rows.length - 2].cells[0];
			var span = prevNode.getElementsByTagName("span")[0];
			span.className = span.className.replace("L", "T");  //把前一个节点由L改为T
			childTree.rows[childTree.rows.length - 1].cells[0].className = "treeI";
		}
		var childNode = childTree.insertRow(-1).insertCell(-1);
		childNode.id = treeNode.nodeId;
		childNode.name = "treeNode";
		childNode.noWrap = true;
		childNode.vAlign = "bottom";
		//创建树线条SPAN
		var lineImg = document.createElement('span');
		lineImg.className = "treeL" + (treeNode.hasChildNodes ? (!treeNode.expandTree ? "Plus" : "Minus") : "");
		var tree = this;
		lineImg.onclick = function() {
			tree.expand(this.parentNode);
		};
		lineImg.innerHTML = "&nbsp;";
		childNode.appendChild(lineImg);
		//创建节点图标SPAN
		var nodeImg = document.createElement('span');
		nodeImg.className = "nodeImg" + (treeNode.expandTree ? "Expand" : "Collapse");
		nodeImg.style.cssText = treeNode.nodeImage=='' ? "display:none" : "background-image:url(" + (!treeNode.expandTree ? treeNode.nodeImage : (treeNode.nodeExpandImage=='' ? treeNode.nodeImage : treeNode.nodeExpandImage)) + ")";
		nodeImg.title = treeNode.nodeText.replace(/<(.*?)>/gi, '');
		nodeImg.innerHTML = "&nbsp;";
		childNode.appendChild(nodeImg);
		//创建节点文本SPAN
		var nodeLabel = document.createElement('span');
		nodeLabel.className = "nodeText";
		nodeLabel.title = nodeImg.title;
		nodeLabel.innerHTML = treeNode.nodeText;
		childNode.appendChild(nodeLabel);
		//事件处理
		nodeLabel.onselectstart = nodeImg.onselectstart = function(event) {
			return false;
		};
		nodeLabel.onclick = nodeImg.onclick = function(event) {
			tree.selectNode(this.parentNode);
			try {
				parentElement.focus();
			}
			catch(e) {
			
			}
		};
		nodeLabel.ondblclick = nodeImg.ondblclick = function(event) {
			tree._dblClickNode(this.parentNode);
		};
		nodeLabel.onmousedown = nodeImg.onmousedown = function(event) {
			tree._mouseDownNode(this.parentNode, event);
		};
		childTreeNode = childTree.insertRow(-1).insertCell(-1);
		childTreeNode.className = "treeB";
		childTreeNode.parentNode.style.display = (treeNode.expandTree ? "" : "none");
   }
   catch(e) {
   
   }
};
//递归函数:删除节点的其他分支的子节点,以节省内存
Tree.prototype._removeNodesOfOtherBranch = function(node) {
	var rows = DomUtils.getOffsetParent(node).rows;
	for(var j = rows.length-2; j>=0; j-=2) {
		var cell = rows[j].cells[0];
		if(cell.id==node.id) {
			continue;
		}
		var spans = cell.getElementsByTagName("span");
		if(spans[0].className != "treeL" && spans[0].className != "treeT") {
			this._setNodeStyle(cell, false);
			cell = rows[j+1].cells[0];
			cell.innerHTML = "";
			cell.parentNode.style.display = "none";
		}
	}
	//递归删除上一级目录的其他分支
	node = this._getParentNode(node);
	if(DomUtils.getOffsetParent(node).id != "tree") {
		this._removeNodesOfOtherBranch(node);
	}
};
//按ID获取节点对象
Tree.prototype._getTreeNode = function(nodeId) {
	for(var i=0; i<this.treeNodes.length; i++) {
		if(this.treeNodes[i].nodeId==nodeId) {
			return this.treeNodes[i];
		}
	}
	return null;
};
//按父节点id删除节点对象
Tree.prototype._removeTreeNodesByParentId = function(parentNodeId) {
	for(var i=0; i < this.treeNodes.length; i++) {
		if(this.treeNodes[i].parentNodeId==parentNodeId) {
			this.treeNodes.splice(i, 1);
			i--;
		}
	}
};
//按id删除节点对象
Tree.prototype._removeTreeNodeById = function(nodeId) {
	for(var i=0; i<this.treeNodes.length; i++) {
		if(this.treeNodes[i].nodeId==nodeId) {
			this.treeNodes.splice(i, 1);
			break;
		}
	}
};
//获取树的根节点
Tree.prototype.getRootNode = function() {
	return this.treeTable.rows[0].cells[0];
};
//把节点设置为叶节点
Tree.prototype.setLeafNode = function(nodeId) {
	var node = DomUtils.getElement(this.treeTable, "td", nodeId);
	if(node==null) {
		return;
	}
	var spans = node.getElementsByTagName("span");
	spans[0].className = spans[0].className.replace("Plus", "").replace("Minus", "");
	spans[0].onclick = "";
	spans[0].ondblclick = "";
	spans[1].ondblclick = "";
	node.parentNode.nextSibling.style.display = "none";
};
//选中节点
Tree.prototype.selectNode = function(node) {
	if(this.selectedNode) {
		try {
			var spans = this.selectedNode.getElementsByTagName("span");
			spans[spans.length - 1].className = "nodeText";
		}
		catch(e) {
		
		}
	}
	this.selectedNode = node;
	var spans = node.getElementsByTagName("span");
	spans[spans.length - 1].className = "nodeTextSelected";
	var nodeType = this.getNodeAttribute(node, "nodeType");
	var leafNode = spans[0].className.indexOf("Plus")==-1 && spans[0].className.indexOf("Minus")==-1;
	for(var parentNode = this._getParentNode(this.selectedNode); parentNode; parentNode = this._getParentNode(parentNode)) {
		this.expand(parentNode, true);
	}
	try {
		this.selectedNode.focus();
	}
	catch(e) {
	 
	}
	this.onNodeSeleced(node.id, spans[spans.length - 1].innerHTML, nodeType, leafNode);
};
Tree.prototype._dblClickNode = function(node) {
   this.expand(node);
   var spans = node.getElementsByTagName("span");
   var nodeType = this.getNodeAttribute(node, "nodeType");
   var leafNode = spans[0].className.indexOf("Plus")==-1 && spans[0].className.indexOf("Minus")==-1;
   this.onDblClickNode(node.id, spans[spans.length - 1].innerHTML, nodeType, leafNode);
};
Tree.prototype._mouseDownNode = function(node, event) {
   if(window.event) {
	  if(window.event.button!=2 && window.event.button!=3) {  
		 return;
	  }
   }
   else {
  	  if(event.which!=2 && window.which!=3) {
  		 return;
  	  }
   }
   var spans = node.getElementsByTagName("span");
   var nodeType = this.getNodeAttribute(node, "nodeType");
   var leafNode = spans[0].className.indexOf("Plus")==-1 && spans[0].className.indexOf("Minus")==-1;
   this.onRightClickNode(node.id, spans[spans.length - 1].innerHTML, nodeType, leafNode);
};
//展开/折叠节点
Tree.prototype.expand = function(node, expandOnly) {
   try {
	  var childTreeNode = node.parentNode.nextSibling.cells[0];
	  var spans = node.getElementsByTagName("span");
	  if(spans[0].className=="treeL" || spans[0].className=="treeT") {
	      return;
	  }
	  if(spans[0].className.indexOf("Minus") != - 1 || spans[0].className.indexOf("Expand") != - 1) { //折叠
	  	  if(!expandOnly) {
		      this._setNodeStyle(node, false);
		      childTreeNode.parentNode.style.display = "none";
		      this._onTreeChnaged(); //树有变化
	      }
	      return;
	  }
      this._setNodeStyle(node, true);
      childTreeNode.parentNode.style.display = "";
      if(!childTreeNode.getElementsByTagName("table")[0]) { //尚未得到子目录
      	 //检查子节点数组中是否存在该目录的子节点
      	 var foundChildNodes = false;
      	 for(var i=0; i<this.treeNodes.length; i++) {
      	 	if(this.treeNodes[i].parentNodeId==node.id) {
      	 		foundChildNodes = true;
      	 		this._writeTreeNode(this.treeNodes[i]);
      	 	}
      	 }
      	 if(!foundChildNodes) {
	         //请求获取子节点
	         var nodeType = this.getNodeAttribute(node, "nodeType");
	         var url = this.onGetUrlForListChildNodes(node.id, nodeType);
	         if(url!="") {
	         	 ScriptUtils.appendJsFile(document, url + (url.indexOf('?')==-1 ? "?" : "&") + "treeIndex=" + this.treeIndex + "&seq=" + Math.random(), "scriptListChildNodes");
	         	 var tree = this;
	         	 window.setTimeout(function() {
	         	 	tree._showLoading(node)
	         	 }, 500);
	    	 }
	     }
	     this._onTreeChnaged(); //树有变化
      }
   }
   catch(e) {
   
   }
};
//如果没有会话,登录后,重新获取子节点列表
Tree.prototype.relistChildNodes = function(urlForListChildNodes) {
	ScriptUtils.appendJsFile(document, urlForListChildNodes + "&seq=" + Math.random(), "scriptListChildNodes");
};
//设置节点样式
Tree.prototype._setNodeStyle = function(node, expanding) {
	var treeNode = this._getTreeNode(node.id); //获取节点对象
	var spans = node.getElementsByTagName("span");
	var from = expanding ? "Plus" : "Minus";
	var to = expanding ? "Minus" : "Plus";
	var className = spans[0].className;
	spans[0].className = spans[0].className.replace(from, to);
	spans[1].className = spans[1].className.replace(from, to);
	from = expanding ? "Collapse" : "Expand";
	to = expanding ? "Expand" : "Collapse";
	spans[0].className = spans[0].className.replace(from, to);
	spans[1].className = spans[1].className.replace(from, to);
	if(className != spans[0].className) {
		var img = (expanding && treeNode.nodeExpandImage ? treeNode.nodeExpandImage : treeNode.nodeImage);
		if(img && img!='') {
			spans[spans.length-2].style.backgroundImage = "url(" + img + ")";
		}
	}
};
//显示正在加载
Tree.prototype._showLoading = function(node) {
	var childTreeNode = node.parentNode.nextSibling.cells[0];
	if(childTreeNode.parentNode.style.display == "" && childTreeNode.innerHTML == "") {
		childTreeNode.innerHTML = "<span style=\"padding:3px;float:left\">正在加载...<span>";
	}
};
//更新节点名称
Tree.prototype.renameNode = function(nodeId, nodeText) {
	var treeNode = this._getTreeNode(nodeId); //获取节点对象
	treeNode.nodeText = nodeText;
	var node = DomUtils.getElement(this.treeTable, "td", nodeId);
	if(node!=null) {
		var spans = node.getElementsByTagName("span");
		spans[spans.length - 1].innerHTML = nodeText;
	}
	this._onTreeChnaged(); //树有变化
};
//获取子节点列表
Tree.prototype.getChildNodes = function(nodeId) {
	var parentNode = document.getElementById(nodeId);
	if(parentNode==null) {
		return null;
	}
	parentNode = parentNode.parentNode.nextSibling.cells[0];
	var childTree = parentNode.getElementsByTagName("table")[0];
	if(!childTree) {
		return null;
	}
	var childNodes = [];
	for(var i=0; i < childTree.rows.length; i+=2) {
		childNodes.push(childTree.rows[i].cells[0]);
	}
	return childNodes;
};
//获取节点的第一个子节点
Tree.prototype._getFirstChildNode = function(parentNode) {
	var childTree = parentNode.parentNode.nextSibling.cells[0].getElementsByTagName("table")[0];
	if(childTree) {
		return childTree.rows[0].cells[0];
	}
};
//重新加载子节点
Tree.prototype.reloadChildNodes = function(nodeId) {
    var node = DomUtils.getElement(this.treeTable, "td", nodeId);
	if(node==null) {
		return;
	}
	var spans = node.getElementsByTagName("span");
	if(node.name == "rootTreeNode") { //根节点
		spans[0].className = spans[0].className.replace("Expand", "Collapse");
	}
	else {
		spans[0].className = spans[0].className.substring(0, 5) + "Plus";
		this._setNodeStyle(node, false);
	}
	node.parentNode.nextSibling.cells[0].innerHTML = "";
	//删除父节点id为nodeId的节点对象
	this._removeTreeNodesByParentId(nodeId);
	//重新展开节点
	if(node) {
		this.expand(node);
	}
	this._onTreeChnaged(); //树有变化
};
//获取节点
Tree.prototype.getNodeById = function(nodeId) {
   return DomUtils.getElement(this.treeTable, "td", nodeId);
};
//选中节点
Tree.prototype.selectNodeById = function(nodeId) {
	var node = DomUtils.getElement(this.treeTable, "td", nodeId);
	if(node!=null) {
		this.selectNode(node);
		return node;
	}
};
//获取下一个节点
Tree.prototype.getNextSibling = function() {
	return this.selectedNode ? this._getNextSibling(this.selectedNode) : null;
};
Tree.prototype._getNextSibling = function(node) {
	if(node) {
		var nextNode = node.parentElement.nextSibling.nextSibling;
		return nextNode ? nextNode.cells[0] : null;
	}
	return null;
};
//删除节点
Tree.prototype.deleteNode = function(nodeId) {
	var node = DomUtils.getElement(this.treeTable, "td", nodeId);
	if(node==null) {
		return;
	}
	var parentNode = DomUtils.getOffsetParent(DomUtils.getOffsetParent(node)).parentNode.previousSibling.cells[0];
	var table = DomUtils.getOffsetParent(node);
	if(table.rows.length == 2) { //上级节点只有一个子节点
		table.deleteRow(1);
		table.deleteRow(0);
		this.setLeafNode(parentNode.id);
		this.selectNode(parentNode);
	}
	else {
		var rowIndex = node.parentNode.rowIndex;
		table.deleteRow(rowIndex + 1);
		table.deleteRow(rowIndex);
		if(rowIndex>=table.rows.length) { //删除的是最后一个节点
			//把节点样式从T改为L
			table.rows[table.rows.length-1].className = "treeB";
			var span = table.rows[table.rows.length-2].getElementsByTagName("span")[0];
			span.className = span.className.replace("T", "L");
			if(span.className.substring(0, 5)=="treeL" && table.rows[table.rows.length-1].cells[0].className=="treeI") {
				table.rows[table.rows.length-1].cells[0].className = "";
			}
		}
	}
	this.selectNode(parentNode);
	//删除节点对象
	this._removeTreeNodeById(nodeId);
	this._onTreeChnaged(); //树有变化
};
//获取选中节点
Tree.prototype.getSelectedNode = function() {
	return this.selectedNode;
};
//获取选中节点的ID
Tree.prototype.getSelectedNodeId = function() {
   return this.selectedNode ? this.selectedNode.id : "";
};
//获取选中节点的文本
Tree.prototype.getSelectedNodeText = function() {
   return !this.selectedNode ? '' : this.getNodeAttribute(this.selectedNode, 'text');
};
//获取选中节点的完整文本,从根开始用指定符合串联起来
Tree.prototype.getSelectedNodeFullText = function(delimiter) {
   if(!this.selectedNode) {
      return "";
   }
   if(!delimiter) {
   		delimiter = "/";
   }
   var text = "";
   var node = this.selectedNode;
   for(;;) {
	   var spans = node.getElementsByTagName("span");
	   text = spans[spans.length - 1].innerHTML + (text=="" ? "" : delimiter) + text;
	   if(node.name=="rootTreeNode") {
	   		break;
	   }
	   node  = this._getParentNode(node);
   }
   return text;
};
Tree.prototype._getParentNode = function(node) {
	if(node.name!='rootTreeNode') {
		return DomUtils.getOffsetParent(DomUtils.getOffsetParent(node)).parentNode.previousSibling.cells[0];
	}
};
//当前选中的节点是否为叶节点
Tree.prototype.isLeafNode = function(node) {
	var spans = node.getElementsByTagName("span");
	return spans[0].className.indexOf("Plus")==-1 && spans[0].className.indexOf("Minus")==-1 && spans[0].className.indexOf("root")==-1;
};
//获取选中节点的属性
Tree.prototype.getSelectedNodeAttribute = function(attributeName) {
   return !this.selectedNode ? '' : this.getNodeAttribute(this.selectedNode, attributeName);
};
//获取选中节点的类型
Tree.prototype.getSelectedNodeType = function() {
   return this.getSelectedNodeAttribute("nodeType");
};
//获取节点的属性
Tree.prototype.getNodeAttribute = function(node, attributeName) {
   var treeNode = this._getTreeNode(node.id); //获取节点对象
   if(attributeName=="id") {
      return node.id;
   }
   else if(attributeName=="text") {
   	  var spans = node.getElementsByTagName("span");
      return spans[spans.length - 1].innerHTML;
   }
   else if(attributeName=="nodeType" || attributeName=="type") {
   	  return treeNode.nodeType;
   }
   else if(attributeName.indexOf("fullText")==0) {
	   var text = "";
	   var index = attributeName.indexOf('/');
	   for(;;) {
		   var spans = node.getElementsByTagName("span");
		   text = spans[spans.length - 1].innerHTML + (text=="" ? "" : "/") + text;
		   if(node.name=="rootTreeNode") {
		   		break;
		   }
		   if(index!=-1 && this._getTreeNode(node.id).nodeType==attributeName.substring(index+1)) { //上溯到指定的节点类型
		   		break;
		   }
		   node  = this._getParentNode(node);
	   }
	   return text;
   }
   else {
   		if(!treeNode.extendProperties) {
			return null;
		}
		try {
			var propertyValue;
			eval('propertyValue = treeNode.extendProperties.' + attributeName + ';');
			return propertyValue ? propertyValue : null;
		}
		catch(e) {
			return null;
		}
   }
};
//设置节点的属性
Tree.prototype.setNodeAttribute = function(node, attributeName, value) {
   var treeNode = this._getTreeNode(node.id); //获取节点对象
   if(attributeName=="id") { //ID
      node.id = value;
      treeNode.nodeId = value;
   }
   else if(attributeName=="text") { //文本
   	  var spans = node.getElementsByTagName("span");
      spans[spans.length - 1].innerHTML = value;
      treeNode.nodeText = value;
   }
   else if(attributeName=="type") { //类型
   	  treeNode.nodeType = value;
   }
   else { //其它属性
	  	if(!treeNode.extendProperties) {
			treeNode.extendProperties = {};
		}
		eval('treeNode.extendProperties.' + attributeName + ' = value;');
   }
};
Tree.prototype._onTreeChnaged = function() { //树有变化
	if(document.all) {
		document.body.treeChangeTime = new Date();
	}
	else {
		var eventObj = document.createEvent('MouseEvents');
        eventObj.initEvent('mousedown', true, false);
        document.body.dispatchEvent(eventObj);
	}
};
Tree.prototype.searchTreeNodeByKey = function(key) {
	this._searchTreeNode(function(node) {
		return this.getNodeAttribute(node, 'text').indexOf(key)!=-1;
	});
};
Tree.prototype.searchTreeNode = function(comparator) { //节点搜索, comparator = function(node), 匹配时返回true
	if(!this.selectedNode) {
		return;
	}
	var node = _searchTreeNode(this.selectedNode, comparator);
	if(!node) {
		var root = this.getRootNode();
		if(root!=this.selectedNode) {
			node = _searchTreeNode(root, comparator);
		}
	}
	if(node) {
		this.selectNode(node);
	}
};
Tree.prototype._searchTreeNode = function(node, comparator) {
	if(node!=this.selectedNode && comparator.call(null, node)) { //比较节点
		return node;
	}
	//搜索子节点
	var childNode = this._getFirstChildNode(node);
	if(childNode) {
		return this._searchTreeNode(childNode, comparator);
	}
	//搜索兄弟节点
	var sibling = this._getNextSibling(node);
	if(sibling) {
		return this._searchTreeNode(sibling, comparator);
	}
	//搜索父节点的兄弟节点
	for(var parentNode = this._getParentNode(node); parentNode && parentNode.name!='rootTreeNode'; parentNode = this._getParentNode(parentNode))  {
		sibling = this._getNextSibling(parentNode);
		if(sibling) {
			return this._searchTreeNode(sibling, comparator);
		}
	}
	return null;
};