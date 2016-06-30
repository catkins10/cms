package com.yuanluesoft.jeaf.htmlparser;

import org.w3c.dom.Node;

/**
 * HTML元素遍历回调
 * @author linchuan
 *
 */
public interface HTMLTraversalCallback {

	/**
	 * 处理遍历到的节点,返回true则不继续遍历下级节点
	 * @param node
	 * @return
	 */
	public boolean processNode(Node node);
}