package com.yuanluesoft.jeaf.tree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class TreeNode implements Serializable {
	private String nodeId; //节点ID
	private String nodeText; //节点文本
	private String nodeType; //节点类型
	private String nodeIcon; //节点图标
	private String nodeExpandIcon; //节点展开时的图标
	private boolean hasChildNodes; //是否有子节点
	private boolean expandTree; //是否需要展开
	private List extendNodeProperties; //扩展属性(Attribute模型)列表
	
	private List childNodes; //子节点列表
	
	public TreeNode() {
		super();
	}

	public TreeNode(String nodeId, String nodeText, String nodeType, String nodeIcon, boolean hasChildNodes) {
		super();
		if(nodeText!=null) {
			nodeText = nodeText.replaceAll("[\\r\\n]", "");
		}
		this.nodeId = nodeId;
		this.nodeText = nodeText;
		this.nodeType = nodeType;
		this.nodeIcon = nodeIcon;
		this.hasChildNodes = hasChildNodes;
	}
	
	/**
	 * 添加子节点
	 * @param nodeId
	 * @param nodeText
	 * @param nodeType
	 * @param nodeIcon
	 * @param hasChildNodes
	 */
	public TreeNode appendChildNode(String nodeId, String nodeText, String nodeType, String nodeIcon, boolean hasChildNodes) {
		this.hasChildNodes = true;
		if(childNodes==null) {
			childNodes = new ArrayList();
		}
		TreeNode childNode = new TreeNode(nodeId, nodeText, nodeType, nodeIcon, hasChildNodes);
		childNodes.add(childNode);
		return childNode;
	}
	
	/**
	 * 获取扩展属性的值
	 * @param propertyName
	 * @return
	 */
	public String getExtendPropertyValue(String propertyName) {
		try {
			Attribute attribute = (Attribute)ListUtils.findObjectByProperty(extendNodeProperties, "name", propertyName);
			return (attribute==null ? null : attribute.getValue());
		}
		catch(ClassCastException e) {
			//保持和com.yuanluesoft.jeaf.base.model.property.Attribute兼容
			Object attribute = ListUtils.findObjectByProperty(extendNodeProperties, "name", propertyName);
			try {
				return (attribute==null ? null : "" + PropertyUtils.getProperty(attribute, "value"));
			}
			catch(Exception ex) {
				return null;
			}
		}
	}
	
	/**
	 * 设置扩展属性的值
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setExtendPropertyValue(String propertyName, String propertyValue) {
		if(propertyValue==null) {
			if(extendNodeProperties!=null) {
				ListUtils.removeObjectByProperty(extendNodeProperties, "name", propertyName);
			 	if(extendNodeProperties.isEmpty()) {
					extendNodeProperties = null;
				}
			}
			return;
		}
		propertyValue = propertyValue.replaceAll("[\\r\\n]", "");
		Attribute attribute = (Attribute)ListUtils.findObjectByProperty(extendNodeProperties, "name", propertyName);
		if(attribute!=null) {
			attribute.setValue(propertyValue);
		}
		else {
			attribute = new Attribute(propertyName, propertyValue);
			if(extendNodeProperties==null) {
				extendNodeProperties = new ArrayList();
			}
			extendNodeProperties.add(attribute);
		}
	}
	
	/**
	 * @return the expandTree
	 */
	public boolean isExpandTree() {
		return expandTree;
	}
	/**
	 * @param expandTree the expandTree to set
	 */
	public void setExpandTree(boolean expandTree) {
		this.expandTree = expandTree;
	}
	/**
	 * @return the extendNodeProperties
	 */
	public List getExtendNodeProperties() {
		return extendNodeProperties;
	}
	/**
	 * @param extendNodeProperties the extendNodeProperties to set
	 */
	public void setExtendNodeProperties(List extendNodeProperties) {
		this.extendNodeProperties = extendNodeProperties;
	}
	/**
	 * @return the hasChildNodes
	 */
	public boolean isHasChildNodes() {
		return hasChildNodes;
	}
	/**
	 * @param hasChildNodes the hasChildNodes to set
	 */
	public void setHasChildNodes(boolean hasChildNodes) {
		this.hasChildNodes = hasChildNodes;
	}
	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the nodeText
	 */
	public String getNodeText() {
		return nodeText;
	}
	/**
	 * @param nodeText the nodeText to set
	 */
	public void setNodeText(String nodeText) {
		if(nodeText!=null) {
			nodeText = nodeText.replaceAll("[\\r\\n]", "");
		}
		this.nodeText = nodeText;
	}
	/**
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}
	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	/**
	 * @return the nodeExpandIcon
	 */
	public String getNodeExpandIcon() {
		return nodeExpandIcon;
	}

	/**
	 * @param nodeExpandIcon the nodeExpandIcon to set
	 */
	public void setNodeExpandIcon(String nodeExpandIcon) {
		this.nodeExpandIcon = nodeExpandIcon;
	}

	/**
	 * @return the nodeIcon
	 */
	public String getNodeIcon() {
		return nodeIcon;
	}
	/**
	 * @param nodeIcon the nodeIcon to set
	 */
	public void setNodeIcon(String nodeIcon) {
		this.nodeIcon = nodeIcon;
	}
	/**
	 * @return the childNodes
	 */
	public List getChildNodes() {
		return childNodes;
	}
	/**
	 * @param childNodes the childNodes to set
	 */
	public void setChildNodes(List childNodes) {
		this.childNodes = childNodes;
		if(childNodes!=null && !childNodes.isEmpty()) {
			hasChildNodes = true;
		}
	}
}
