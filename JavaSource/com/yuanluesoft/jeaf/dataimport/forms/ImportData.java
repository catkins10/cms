package com.yuanluesoft.jeaf.dataimport.forms;

import java.util.List;

import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class ImportData extends TreeDialog {
	private long targetSiteId; //目标站点ID
	private String targetSiteName; //目标站点名称
	private String dataImportServiceName; //数据导入服务名称
	private String dataImportServiceClass; //数据导入服务类名称
	private DataImportParameter parameter = new DataImportParameter(); //数据导入参数配置
	private List dataDirectoryTrees; //目录树列表

	//URL参数: 获取树的子节点时有效
	private String importDataName; //URL参数:导入的数据名称
	private boolean sourceTree; //URL参数:是否源目录树
	
	/**
	 * @return the dataDirectoryTrees
	 */
	public List getDataDirectoryTrees() {
		return dataDirectoryTrees;
	}

	/**
	 * @param dataDirectoryTrees the dataDirectoryTrees to set
	 */
	public void setDataDirectoryTrees(List dataDirectoryTrees) {
		this.dataDirectoryTrees = dataDirectoryTrees;
	}

	/**
	 * @return the importDataName
	 */
	public String getImportDataName() {
		return importDataName;
	}

	/**
	 * @param importDataName the importDataName to set
	 */
	public void setImportDataName(String importDataName) {
		this.importDataName = importDataName;
	}

	/**
	 * @return the sourceTree
	 */
	public boolean isSourceTree() {
		return sourceTree;
	}

	/**
	 * @param sourceTree the sourceTree to set
	 */
	public void setSourceTree(boolean sourceTree) {
		this.sourceTree = sourceTree;
	}

	/**
	 * @return the targetSiteId
	 */
	public long getTargetSiteId() {
		return targetSiteId;
	}

	/**
	 * @param targetSiteId the targetSiteId to set
	 */
	public void setTargetSiteId(long targetSiteId) {
		this.targetSiteId = targetSiteId;
	}

	/**
	 * @return the targetSiteName
	 */
	public String getTargetSiteName() {
		return targetSiteName;
	}

	/**
	 * @param targetSiteName the targetSiteName to set
	 */
	public void setTargetSiteName(String targetSiteName) {
		this.targetSiteName = targetSiteName;
	}

	/**
	 * @return the parameter
	 */
	public DataImportParameter getParameter() {
		return parameter;
	}

	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(DataImportParameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * @return the dataImportServiceClass
	 */
	public String getDataImportServiceClass() {
		return dataImportServiceClass;
	}

	/**
	 * @param dataImportServiceClass the dataImportServiceClass to set
	 */
	public void setDataImportServiceClass(String dataImportServiceClass) {
		this.dataImportServiceClass = dataImportServiceClass;
	}

	/**
	 * @return the dataImportServiceName
	 */
	public String getDataImportServiceName() {
		return dataImportServiceName;
	}

	/**
	 * @param dataImportServiceName the dataImportServiceName to set
	 */
	public void setDataImportServiceName(String dataImportServiceName) {
		this.dataImportServiceName = dataImportServiceName;
	}
}