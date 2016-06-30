package com.yuanluesoft.jeaf.dataimport.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;


/**
 * 数据导入服务
 * @author linchuan
 *
 */
public abstract class DataImportService {
	protected String name; //导入服务名称
	protected boolean sameSystem = false; //是否同样的系统
	
	/**
	 * 获取数据导入器列表
	 * @return
	 */
	protected abstract List getDataImporters();
	
	/**
	 * 获取数据导入服务名称
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * 获取类名称
	 * @return
	 */
	public String getClassName() {
		return this.getClass().getName();
	}
	
	/**
	 * 导入数据
	 * @param targetSiteId
	 * @param parameter
	 * @throws Exception
	 */
	public void importData(long targetSiteId, DataImportParameter parameter) throws Exception {
		Connection connection = createConnection(parameter);
		for(Iterator iterator = getDataImporters().iterator(); iterator.hasNext();) {
			DataImporter importer = (DataImporter)iterator.next();
			try {
				importer.importData(targetSiteId, this.getClass().getName(), connection, parameter, sameSystem);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		connection.close();
	}
	
	/**
	 * 获取数据目录树列表
	 * @param targetSiteId
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public List listDataDirectoryTrees(long targetSiteId, DataImportParameter parameter) throws Exception {
		Connection connection = createConnection(parameter);
		List trees = new ArrayList();
		for(Iterator iterator = getDataImporters().iterator(); iterator.hasNext();) {
			DataImporter importer = (DataImporter)iterator.next();
			if(importer instanceof DirectoryDataImporter) {
				try {
					trees.add(((DirectoryDataImporter)importer).loadDataDirectoryTree(targetSiteId, this.getClass().getName(), connection, parameter, sameSystem));
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
		connection.close();
		return trees.isEmpty() ? null : trees;
	}
	
	/**
	 * 获取子节点列表
	 * @param importDataName
	 * @param parentDirectoryId
	 * @param isSourceTree
	 * @param targetSiteId
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public List listDataDirectoryTreeNodes(String importDataName, String parentDirectoryId, boolean isSourceTree, long targetSiteId, DataImportParameter parameter) throws Exception {
		Connection connection = createConnection(parameter);
		try {
			for(Iterator iterator = getDataImporters().iterator(); iterator.hasNext();) {
				DataImporter importer = (DataImporter)iterator.next();
				if(!(importer instanceof DirectoryDataImporter)) { //不是目录方式
					continue;
				}
				DirectoryDataImporter directoryDataImporter = (DirectoryDataImporter)importer;
				if(!directoryDataImporter.getImportDataName().equals(importDataName)) { //目录不匹配
					continue;
				}
				if(isSourceTree) { //源目录树
					return directoryDataImporter.loadSourceDirectoryTreeNodes(parentDirectoryId, targetSiteId, this.getClass().getName(), connection, parameter, sameSystem);
				}
				else { //目标目录树
					DirectoryService directoryService = (DirectoryService)Environment.getService(directoryDataImporter.getDirectoryServiceName());
					return directoryService.listChildTreeNodes(Long.parseLong(parentDirectoryId), null, null, null, null, null);
				}
			}
			return null;
		}
		finally {
			connection.close();
		}
	}
	
	/**
	 * 保存数据目录树列表
	 * @param request
	 * @param targetSiteId
	 * @param parameter
	 * @throws Exception
	 */
	public void saveDataDirectoryTrees(HttpServletRequest request, long targetSiteId, DataImportParameter parameter) throws Exception {
		Connection connection = createConnection(parameter);
		for(Iterator iterator = getDataImporters().iterator(); iterator.hasNext();) {
			DataImporter importer = (DataImporter)iterator.next();
			if(importer instanceof DirectoryDataImporter) {
				try {
					((DirectoryDataImporter)importer).saveSourceDirectoryTree(request, targetSiteId, this.getClass().getName(), connection, parameter, sameSystem);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
		connection.close();
	}
	
	/**
	 * 创建连接
	 * @param parameter
	 * @return
	 * @throws ServiceException
	 */
	protected Connection createConnection(DataImportParameter parameter) throws ServiceException {
		try {
			Class.forName(parameter.getJdbcDriver());
			return DriverManager.getConnection(parameter.getJdbcURL(), parameter.getJdbcUserName(), parameter.getJdbcPassword());
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}