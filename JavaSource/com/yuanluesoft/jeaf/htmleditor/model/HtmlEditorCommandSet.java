package com.yuanluesoft.jeaf.htmleditor.model;


/**
 * HTML编辑器命令集合 
 * @author linchuan
 *
 */
public class HtmlEditorCommandSet {
	private String name; //集合名称
	private String plugins; //插件列表
	private String commands; //命令列表
	
	/**
	 * @return the commands
	 */
	public String getCommands() {
		return commands;
	}
	/**
	 * @param commands the commands to set
	 */
	public void setCommands(String commands) {
		this.commands = commands;
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
	 * @return the plugins
	 */
	public String getPlugins() {
		return plugins;
	}
	/**
	 * @param plugins the plugins to set
	 */
	public void setPlugins(String plugins) {
		this.plugins = plugins;
	}
}