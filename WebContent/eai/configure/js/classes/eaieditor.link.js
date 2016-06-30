//链接
EAIEditor.Link = function(eaiEditor, link, surface) {
	GraphicsEditor.RectNode.call(this, eaiEditor, link, surface, link.title, 3 * eaiEditor.gridSize, 15 * eaiEditor.gridSize, 0);
	this.eaiEditor = eaiEditor;
	this.link = link;
	this.jsonObjectPool = eaiEditor.jsonObjectPool;
};
EAIEditor.Link.prototype = new GraphicsEditor.RectNode();
//抽象方法(由继承者实现):获取属性目录树
EAIEditor.Link.prototype.getPropertyTree = function() {
	var tree = {id: this.link.id, label: this.link.title==null ? '链接' : this.link.title, childNodes: []};
	if(this.surface.readOnly) {
		return tree;
	}
	tree.childNodes.push({id: "manager", label: "管理员"}); //管理员
	tree.childNodes.push({id: "visitor", label: "访问权限"}); //访问者
	return tree;
};
//抽象方法(由继承者实现):获取属性列表
EAIEditor.Link.prototype.getPropertyList = function(propertyName) {
	var linkElement = this;
	var link = this.link;
	if(propertyName=="manager") { //管理员
		return [new GraphicsEditor.UserListProperty(propertyName, "管理员", link.managers, this.jsonObjectPool, this.surface.readOnly || this.surface.configOnly, function(value) {
			link.managers = value;
		})];
	}
	else if(propertyName=="visitor") { //访问者
		return [new GraphicsEditor.UserListProperty(propertyName, "访问权限", link.visitors, this.jsonObjectPool, this.surface.readOnly || this.surface.configOnly, function(value) {
			link.visitors = value;
		})];
	}
	else if(!this.surface.readOnly && !this.surface.configOnly) { //基本属性
	    var propertyList = [];
	   	propertyList.push(new GraphicsEditor.TextProperty("title", "名称", link.title, this.surface.displayOnly, function(value) {
			link.title = value;
			linkElement.setTitle(link.title);
		}));
		var href = link.url ? link.url.replace(/'/g,  '\"') : '';
		if(this.surface.displayOnly) {
			propertyList.push(new GraphicsEditor.TextProperty("href", "链接地址", href, true));
		}
		else {
	       	var doGetItemsText = function() {
				var itemsText = '';
				var links = linkElement.eaiEditor.eai.links;
		        for(var i = 0; i < links.length; i++) {
					if(ListUtils.findObjectByProperty(linkElement.eaiEditor.elements, "link.uuid", links[i].uuid)==null) {
						var link = ListUtils.findObjectByProperty(linkElement.jsonObjectPool, "uuid", links[i].uuid);
						itemsText += (itemsText=='' ? '' : '\0') + link.title + '|' + link.uuid;
					}
			    }
			    return itemsText;
		    };
		    propertyList.push(new GraphicsEditor.DropdownProperty("href", "链接地址", href, doGetItemsText, false, function(value) {
				//判断用户是否选择了已存在的链接, 如果是,则更新name
		    	var selectedLink = ListUtils.findObjectByProperty(linkElement.jsonObjectPool, 'uuid', value);
				if(!selectedLink) { //不是选择的连接
			    	link.url = value;
			    }
				else if(link.name) { //已经选择过
			    	link.url = selectedLink.url;
			    }
			    else {
			    	ListUtils.removeObjectByProperty(linkElement.eaiEditor.eai.links, 'uuid', selectedLink.uuid);
			    	ListUtils.removeObjectByProperty(linkElement.jsonObjectPool, 'uuid', link.uuid);
			    	var title = link.title;
				    selectedLink.uuid = link.uuid;
				    selectedLink.id = link.id;
				    linkElement.link = linkElement.model = selectedLink;
				    if(title.indexOf("链接")!=0) {
				        selectedLink.title = title;
				    }
					linkElement.setTitle(selectedLink.title);
				}
				linkElement.eaiEditor.showPropertyTree();
			}));
		}
		return propertyList;
	}
};