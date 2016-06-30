package com.yuanluesoft.jeaf.taglib;

import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class TreeTag extends BodyTagSupport {
	private String name;
	private String property;
	private String scope;
	private String parentElementId;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(name==null) {
			name = Constants.BEAN_KEY;
		}
		Tree tree = (Tree)TagUtils.getInstance().lookup(pageContext, name, property, scope);
		if(tree==null) {
			return (SKIP_BODY );
		}
		this.setId("tree" + UUIDLongGenerator.generateId());
		String html = "<script language=\"javascript\">" +
					  "var " + this.getId() + " = new Tree('" + tree.getRootNode().getNodeId() + "'," +
					  "	'" + tree.getRootNode().getNodeText().replaceAll("'", "\\\\'") + "'," +
					  "	'" + tree.getRootNode().getNodeType() + "'," +
					  "	'" + (tree.getRootNode().getNodeIcon()==null ? "" : tree.getRootNode().getNodeIcon()) + "'," +
					  "	'" + (tree.getRootNode().getNodeExpandIcon()==null ? "" : tree.getRootNode().getNodeExpandIcon()) + "'," +
					  "	document.getElementById('" + parentElementId + "')," +
					  "	false";
		if(tree.getRootNode().getExtendNodeProperties()!=null) {
			html += ",{";
			for(Iterator iterator = tree.getRootNode().getExtendNodeProperties().iterator(); iterator.hasNext();) {
				Attribute attribute = (Attribute)iterator.next();
				html += attribute.getName() + ": '" + (attribute.getValue()==null ? "" : attribute.getValue().replaceAll("'", "\\\\'")) + "'";
				if(iterator.hasNext()) {
					html += ",";
				}
			}
			html += "}";
		}
		html += ");\r\n" +
				"window.tree = " + this.getId() + ";\r\n";
		TagUtils.getInstance().write(this.pageContext, html);
		//输出子节点
		writeChildNodes(tree.getRootNode());
		TagUtils.getInstance().write(this.pageContext, "</script>");
        return (EVAL_BODY_AGAIN );
	}
	
	/**
	 * 递归:输出子节点
	 * @param parentNode
	 * @return
	 * @throws JspException
	 */
	private void writeChildNodes(TreeNode parentNode) throws JspException {
		if(parentNode.getChildNodes()==null) {
			return;
		}
		for(Iterator iteratorNode = parentNode.getChildNodes().iterator(); iteratorNode.hasNext();) {
			TreeNode node = (TreeNode)iteratorNode.next();
			String html = this.getId() + ".appendNode('" + parentNode.getNodeId() + "'," +
						  "	'" + node.getNodeId() + "'," +
						  " '" + node.getNodeText().replaceAll("'", "\\\\'") + "'," +
						  "	'" + node.getNodeType() + "'," +
						  "	'" + (node.getNodeIcon()==null ? "" : node.getNodeIcon()) + "'," +
						  "	'" + (node.getNodeExpandIcon()==null ? "" : node.getNodeExpandIcon()) + "'," +
						  "	" + node.isHasChildNodes() + "," +
						  "	" + node.isExpandTree();
			if(node.getExtendNodeProperties()!=null) {
				html += ",{";
				for(Iterator iterator = node.getExtendNodeProperties().iterator(); iterator.hasNext();) {
					Attribute attribute = (Attribute)iterator.next();
					html += attribute.getName() + ":'" + (attribute.getValue()==null ? "" : attribute.getValue().replaceAll("'", "\\\\'")) + "'";
					if(iterator.hasNext()) {
						html += ",";
					}
				}
				html += "}";
			}
			html += ");\r\n";
			TagUtils.getInstance().write(this.pageContext, html);
			writeChildNodes(node); //添加当前节点的子节点
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	public void release() {
		super.release();
		name = null;
		property = null;
		scope = null;
		parentElementId = null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the parentElementId
	 */
	public String getParentElementId() {
		return parentElementId;
	}

	/**
	 * @param parentElementId the parentElementId to set
	 */
	public void setParentElementId(String parentElementId) {
		this.parentElementId = parentElementId;
	}
}