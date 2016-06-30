package com.yuanluesoft.jeaf.dataimport.dataimporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.dataimport.model.DataDirectoryTree;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryTableMapping;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.tree.util.TreeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 目录数据导入器
 * @author linchuan
 *
 */
public abstract class DirectoryDataImporter extends DataImporter {
	protected ThreadLocal threadLocal = new ThreadLocal();
	private DirectoryService directoryService = null;

	/**
	 * 生成获取子目录列表的SQL,必须返回directoryId,directoryName两个字段
	 * @param parentDirectoryId
	 * @return
	 */
	public abstract String generateListChildDirectoriesSQL(String parentDirectoryId);
	
	/**
	 * 获取目录服务名称
	 * @return
	 */
	public abstract String getDirectoryServiceName();
	
	/**
	 * 获取目录和表的映射关系,其中第一个是默认的目录类型
	 * @return
	 */
	public abstract DirectoryTableMapping[] getDirectoryTableMappings();
	
	/**
	 * 根据选中的源目录ID，生成获取数据的SQL
	 * @param selectedSourceDirectoryIds
	 * @return
	 */
	public abstract String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds);
	
	/**
	 * 保存导入的数据
	 * @param resultSet
	 * @param mappingDirectoryIds
	 * @param targetSite
	 * @param connection
	 * @param parameter
	 * @param sourceDataId
	 * @param importedRecordId 之前导入过的记录ID,-1表示无法确定是否导入过
	 * @param sameSystem
	 * @return 
	 * @throws Exception
	 */
	protected abstract long saveImportData(ResultSet resultSet, String mappingDirectoryIds, WebSite targetSite, Connection connection, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception;
	
	/**
	 * 按目录名称查找映射的目录
	 * @param directoryName
	 * @param targetSiteId
	 * @return
	 * @throws Exception
	 */
	protected abstract DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception;
	
	/**
	 * 加载目录树
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @return
	 * @throws Exception
	 */
	public DataDirectoryTree loadDataDirectoryTree(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		DataDirectoryTree directoryTree = new DataDirectoryTree();
		directoryTree.setName(getImportDataName());
		directoryTree.setSourceTree(loadSourceDirectoryTree(false, targetSiteId, dataImportServiceClass, connection, parameter, sameSystem)); //源
		//设置目标目录树
		Tree targetTree = getDirectoryService().createDirectoryTree(0, null, null, null, null, null);
		TreeNode root = targetTree.getRootNode();
		root.setNodeId(getImportDataName() + "target_" + root.getNodeId());
		directoryTree.setTargetTree(targetTree); //目标
		return directoryTree;
	}
	
	/**
	 * 加载子目录列表
	 * @param parentDirectoryId
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @return
	 * @throws Exception
	 */
	public List loadSourceDirectoryTreeNodes(String parentDirectoryId, long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		return loadSourceDirectoryTreeNodes(parentDirectoryId.substring(getImportDataName().length() + 1), new TreeNode(), false, targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
	}
	
	/**
	 * 保存源目录树
	 * @param request
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @throws Exception
	 */
	public void saveSourceDirectoryTree(HttpServletRequest request, long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		Tree sourceDirectoryTree = loadSourceDirectoryTree(true, targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
		updateDataTreeNode(sourceDirectoryTree.getRootNode(), request);
		FileOutputStream output = null;
		ObjectOutputStream oos = null;
		try {
			output = new FileOutputStream(getTreeSavePath(targetSiteId, dataImportServiceClass));
			oos = new ObjectOutputStream(output);
	        oos.writeObject(sourceDirectoryTree);
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        finally {
        	try {
        		oos.close();
        	}
        	catch(Exception e) {
            	
            }
        	try {
        		output.close();
        	}
        	catch(Exception e) {
            	
            }
        }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
	 */
	public String[] generateRetrieveDataSQL() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL(long, java.lang.String, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter, boolean)
	 */
	protected String[] generateRetrieveDataSQL(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		List selectedSourceDirectoryIds = listSelectedSourceDirectoryIds(targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
		if(selectedSourceDirectoryIds==null || selectedSourceDirectoryIds.isEmpty()) {
			return null;
		}
		return generateRetrieveDataSQL(selectedSourceDirectoryIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#importData(long, java.lang.String, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
	 */
	public void importData(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		//加载源目录树
		Tree sourceDirectoryTree = getSourceDirectoryTree(targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
		//复制子目录
		findDirectoryToCopy(sourceDirectoryTree.getRootNode(), connection, parameter, sameSystem);
		//开始导入数据,如文章、政府信息
		super.importData(targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
	}
	
	/**
	 * 递归:查找需要拷贝子目录的目录
	 * @param parentSourceNode
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @throws Exception
	 */
	private void findDirectoryToCopy(TreeNode parentSourceNode, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		if("true".equals(parentSourceNode.getExtendPropertyValue("copyChildDirectory"))) { //需要复制子目录
			String targetDirectoryIds = parentSourceNode.getExtendPropertyValue("targetDirectoryIds");
			if(targetDirectoryIds!=null && !targetDirectoryIds.isEmpty()) {
				//开始拷贝子目录
				String[] ids = targetDirectoryIds.split(",");
				for(int i=0; i<ids.length; i++) {
					int index = ids[i].indexOf("target_");
					long directoryId = Long.parseLong(index==-1 ? ids[i] : ids[i].substring(index + "target_".length()));
					if(!parentSourceNode.getNodeId().startsWith("root_")) { //不是根目录
						//子目录创建完成后
						afterChildDirectoryCreated(getDirectoryService(), getDirectoryService().getDirectory(directoryId), parentSourceNode.getNodeId().substring(getImportDataName().length() + 1), parentSourceNode.getNodeText(), sameSystem, connection, parameter);
					}
					//复制子目录
					copyChildDirectory(parentSourceNode, directoryId, connection, parameter, sameSystem);
				}
				return;
			}
		}
		//查找下级目录
		for(Iterator iterator = parentSourceNode.getChildNodes()==null ? null : parentSourceNode.getChildNodes().iterator(); iterator!=null && iterator.hasNext();) {
			TreeNode node = (TreeNode)iterator.next();
			findDirectoryToCopy(node, connection, parameter, sameSystem); //递归处理下级目录
		}
	}
	
	/**
	 * 递归:拷贝子目录
	 * @param parentSourceTreeNode
	 * @param targetDirectoryId
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @throws Exception
	 */
	private void copyChildDirectory(TreeNode parentSourceTreeNode, long targetDirectoryId, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		//拷贝下级目录
		for(Iterator iterator = parentSourceTreeNode.getChildNodes()==null ? null : parentSourceTreeNode.getChildNodes().iterator(); iterator!=null && iterator.hasNext();) {
			TreeNode node = (TreeNode)iterator.next();
			String nodeId = node.getNodeId().substring(getImportDataName().length() + 1);
			//创建子目录
			Directory directory = createChildDirectory(getDirectoryService(), targetDirectoryId, nodeId, node.getNodeText(), sameSystem, connection, parameter);
			//子目录创建完成后
			afterChildDirectoryCreated(getDirectoryService(), directory, nodeId, node.getNodeText(), sameSystem, connection, parameter);
			//递归拷贝下一级
			copyChildDirectory(node, directory.getId(), connection, parameter, sameSystem);
		}
	}
	
	/**
	 * 创建子目录
	 * @param parentDirectoryId
	 * @param newDirectoryId
	 * @param newDirectoryName
	 * @param sameSystem
	 * @return
	 * @throws Exception
	 */
	protected Directory createChildDirectory(DirectoryService directoryService, long parentDirectoryId, String newDirectoryId, String newDirectoryName, boolean sameSystem, Connection connection, DataImportParameter parameter) throws Exception {
		DirectoryTableMapping[] directoryTableMappings = getDirectoryTableMappings();
		if(!sameSystem) { //不同的系统
			return directoryService.createDirectory(-1, parentDirectoryId, newDirectoryName, directoryTableMappings[0].getDirectoryType(), "导入", 0, null);
		}
		//检查目录是否已经存在
		Directory directory = directoryService.getDirectory(Long.parseLong(newDirectoryId));
		if(directory!=null) {
			return directory;
		}
		//获取目录的详细数据
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select directoryType from " + directoryTableMappings[0].getBaseTableName() + " where id=" + newDirectoryId);
		resultSet.next();
		String directoryType = resultSet.getString("directoryType");
		resultSet.close();
		String sql = null;
		for(int i=0; i<directoryTableMappings.length; i++) {
			if(directoryTableMappings[i].getDirectoryType().equals(directoryType)) {
				sql = "select * from " + directoryTableMappings[i].getBaseTableName() + 
					  (directoryTableMappings[i].getTableName()==null ? "" : " left join " + directoryTableMappings[i].getTableName() + " on " + directoryTableMappings[i].getBaseTableName() + ".id=" + directoryTableMappings[i].getTableName() + ".id") +
					  " where " + directoryTableMappings[i].getBaseTableName() + ".id=" + newDirectoryId;
				directory = (Directory)Class.forName(directoryTableMappings[i].getPojoClassName()).newInstance();
				break;
			}
		}
		if(directory!=null) {
			Logger.info(sql);
			resultSet = statement.executeQuery(sql);
			resultSet.next();
			JdbcUtils.copyFields(directory, resultSet);
			resultSet.close();
			directory.setParentDirectoryId(parentDirectoryId); //重设父目录ID
			directoryService.createDirectory(directory);
		}
		statement.close();
		return directory;
	}
	
	/**
	 * 子目录创建完成后，继承者可以在子目录创建后做诸如拷贝模板等操作
	 * @param directoryService
	 * @param directory
	 * @param sourceDirectoryId
	 * @param sourceDirectoryName
	 * @param sameSystem
	 * @param connection
	 * @param parameter
	 * @throws Exception
	 */
	protected void afterChildDirectoryCreated(DirectoryService directoryService, Directory directory, String sourceDirectoryId, String sourceDirectoryName, boolean sameSystem, Connection connection, DataImportParameter parameter) throws Exception {
		//添加目录映射到源目录树,使导入的数据能被复制到新建的目录中
		Tree sourceDirectoryTree = (Tree)threadLocal.get();
		TreeNode dataTreeNode = TreeUtils.findDataTreeNodeById(sourceDirectoryTree.getRootNode(), getImportDataName() + "_" + sourceDirectoryId);
		String targetDirectoryIds = dataTreeNode.getExtendPropertyValue("targetDirectoryIds");
		if(targetDirectoryIds==null || targetDirectoryIds.equals("")) { //没有映射目录
			targetDirectoryIds = "" + directory.getId();
		}
		else if(("," + targetDirectoryIds + ",").indexOf("," + directory.getId() + ",")==-1) {
			targetDirectoryIds += "," + directory.getId();
		}
		dataTreeNode.setExtendPropertyValue("targetDirectoryIds", targetDirectoryIds);
	}
	
	/**
	 * 获取选中的目录ID
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @return
	 * @throws Exception
	 */
	public List listSelectedSourceDirectoryIds(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		List selectedSourceDirectoryIds = new ArrayList();
		Tree sourceDirectoryTree = getSourceDirectoryTree(targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
		listSelectedSourceDirectoryIds(sourceDirectoryTree.getRootNode(), selectedSourceDirectoryIds);
		return selectedSourceDirectoryIds;
	}
	
	/**
	 * 递归函数:获取选中的子目录ID
	 * @param sourceTreeNode
	 * @param selectedSourceDirectoryIds
	 * @throws Exception
	 */
	private void listSelectedSourceDirectoryIds(TreeNode parentSourceNode, List selectedSourceDirectoryIds) throws Exception {
		for(Iterator iterator = parentSourceNode.getChildNodes()==null ? null : parentSourceNode.getChildNodes().iterator(); iterator!=null && iterator.hasNext();) {
			TreeNode node = (TreeNode)iterator.next();
			//检查是否有设置目标目录
			String targetDirectoryIds = node.getExtendPropertyValue("targetDirectoryIds");
			if(targetDirectoryIds!=null && !targetDirectoryIds.isEmpty()) {
				selectedSourceDirectoryIds.add(node.getNodeId().substring(getImportDataName().length() + 1));
			}
			//递归处理下一级目录
			listSelectedSourceDirectoryIds(node, selectedSourceDirectoryIds);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#saveImportedData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DataImporterCallback, java.sql.ResultSet)
	 */
	protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		//加载源目录树
		Tree sourceDirectoryTree = getSourceDirectoryTree(targetSite.getId(), dataImportServiceClass, connection, parameter, sameSystem);
		String directoryId = JdbcUtils.getString(resultSet, "directoryId"); //原来的目录ID
		TreeNode dataTreeNode = TreeUtils.findDataTreeNodeById((TreeNode)sourceDirectoryTree.getRootNode(), getImportDataName() + "_" + directoryId);
		if(dataTreeNode==null) {
			return -1;
		}
		String targetDirectoryIds = dataTreeNode.getExtendPropertyValue("targetDirectoryIds");
		if(targetDirectoryIds==null || targetDirectoryIds.equals("")) { //没有映射目录
			return -1;
		}
		return saveImportData(resultSet, targetDirectoryIds, targetSite, connection, parameter, sourceDataId, importedRecordId, sameSystem);
	}
	
	/**
	 * 加载源目录树,被拷贝数据原来的目录
	 * @param deep
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @return
	 * @throws Exception
	 */
	protected Tree loadSourceDirectoryTree(boolean deep, long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		TreeNode root = new TreeNode();
		root.setNodeId("root_" + getImportDataName());
		root.setNodeText(getImportDataName());
		root.setChildNodes(loadSourceDirectoryTreeNodes(null, root, deep, targetSiteId, dataImportServiceClass, connection, parameter, sameSystem));
		Tree sourceDirectoryTree = new Tree();
		sourceDirectoryTree.setRootNode(root);
		return sourceDirectoryTree;
	}
	
	/**
	 * 加载子目录列表
	 * @param deep 是否加载所有的子目录
	 * @param sameSystem
	 * @param importerCallback
	 * @return
	 * @throws Exception
	 */
	private List loadSourceDirectoryTreeNodes(String parentDirectoryId, TreeNode parentNode, boolean deep, long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			List dirctoryNodes = readSourceDirctoryNodes(connection, parentDirectoryId, deep);
			if(dirctoryNodes==null || dirctoryNodes.isEmpty()) {
				return null;
			}
			parentNode.setChildNodes(dirctoryNodes);
			//从文件加载数据目录
			File file = new File(getTreeSavePath(targetSiteId, dataImportServiceClass));
			Tree dataTree = null;
			if(file.exists()) {
				fileInputStream = new FileInputStream(file);
				objectInputStream = new ObjectInputStream(fileInputStream);
				dataTree = (Tree)objectInputStream.readObject();
				if(dataTree!=null) {
					//把配置信息拷贝到oldTree
					updateDataTreeNode(parentNode, dataTree);
				}
			}
			//自动关联新的目录
			if(!sameSystem) {
				updateDirectoryMapping(parentNode, targetSiteId);
			}
			return dirctoryNodes;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			if(objectInputStream!=null) {
				objectInputStream.close();
			}
			if(fileInputStream!=null) {
				fileInputStream.close();
			}
		}
	}
	
	/**
	 * 递归：获取数据源子目录列表
	 * @param connection
	 * @param parentColumnId
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	private List readSourceDirctoryNodes(Connection connection, String parentDirectoryId, boolean deep) throws Exception {
		String sql = generateListChildDirectoriesSQL(parentDirectoryId);
		if(sql==null) {
			return null;
		}
		Logger.info(sql);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		List directoryNodes = new ArrayList();
		while(rs.next()) {
			TreeNode node = new TreeNode();
			String directoryId = getSourceDirectoryId(rs);
			node.setNodeId(getImportDataName() + "_" + directoryId);
			node.setNodeText(getSourceDirectoryName(rs).replaceAll("'", ""));
			node.setNodeIcon(Environment.getContextPath() + "/jeaf/dataimport/icons/no.gif");
			directoryNodes.add(node);
			//获取子栏目
			if(deep) {
				node.setChildNodes(readSourceDirctoryNodes(connection, directoryId, deep));
			}
			else {
				sql = generateListChildDirectoriesSQL(directoryId);
				if(sql!=null) {
					Statement statementChild = connection.createStatement();
					Logger.info(sql);
					ResultSet rsChild = statementChild.executeQuery(sql);
					node.setHasChildNodes(rsChild.next());
					rsChild.close();
					statementChild.close();
				}
			}
		}
		rs.close();
		statement.close();
		return directoryNodes;
	}
	
	/**
	 * 获取源目录ID
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	protected String getSourceDirectoryId(ResultSet rs) throws Exception {
		return JdbcUtils.getString(rs, "directoryId");
	}
	
	/**
	 * 获取源目录名称
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	protected String getSourceDirectoryName(ResultSet rs) throws Exception {
		return JdbcUtils.getString(rs, "directoryName");
	}
	
	/**
	 * 递归:更新节点
	 * @param treeNode
	 * @param fromDataTree
	 * @throws Exception
	 */
	private void updateDataTreeNode(TreeNode treeNode, Tree fromDataTree) throws Exception {
		//查找相同ID的节点
		TreeNode foundNode = treeNode.getNodeId()==null ? null : TreeUtils.findDataTreeNodeById(fromDataTree.getRootNode(), treeNode.getNodeId());
		if(foundNode!=null) {
			String ids = foundNode.getExtendPropertyValue("targetDirectoryIds");
			if(ids!=null) {
				//更新属性
				setTargetDirectory(treeNode, ids, foundNode.getExtendPropertyValue("targetDirectoryNames"), "true".equals(foundNode.getExtendPropertyValue("copyChildDirectory")));
			}
		}
		//更新子节点
		if(treeNode.getChildNodes()==null) {
			return;
		}
		for(Iterator iterator = treeNode.getChildNodes().iterator(); iterator.hasNext();) {
			treeNode = (TreeNode)iterator.next();
			updateDataTreeNode(treeNode, fromDataTree);
		}
	}
	
	/**
	 * 递归:根据提交的数据更新节点
	 * @param treeNode
	 * @param request
	 * @throws Exception
	 */
	private void updateDataTreeNode(TreeNode treeNode, HttpServletRequest request) throws Exception {
		//更新属性
		String targetDirectoryIds = treeNode.getNodeId()==null ? null : request.getParameter("targetDirectoryIds_" + treeNode.getNodeId());
		if(targetDirectoryIds!=null) {
			setTargetDirectory(treeNode, targetDirectoryIds, request.getParameter("targetDirectoryNames_" + treeNode.getNodeId()), "true".equals(request.getParameter("copyChildDirectory_" + treeNode.getNodeId())));
		}
		//更新子节点
		if(treeNode.getChildNodes()==null) {
			return;
		}
		for(Iterator iterator = treeNode.getChildNodes().iterator(); iterator.hasNext();) {
			treeNode = (TreeNode)iterator.next();
			updateDataTreeNode(treeNode, request);
		}
	}
	
	/**
	 * 递归：自动关联同名目录
	 * @param directoryService
	 * @param treeNode
	 * @throws Exception
	 */
	private void updateDirectoryMapping(TreeNode treeNode, long targetSiteId) throws Exception {
		String targetDirectoryIds = treeNode.getNodeId()==null ? null : treeNode.getExtendPropertyValue("targetDirectoryIds");
		if(targetDirectoryIds==null) { //没有关联过
			//查找同名目录
			DirectoryMapping directoryMapping = getSameNameDirectory(treeNode.getNodeText(), targetSiteId);
			if(directoryMapping!=null) {
				setTargetDirectory(treeNode, directoryMapping.getDirectoryId(), directoryMapping.getDirectoryFullName(), false);
			}
		}
		//更新子节点
		if(treeNode.getChildNodes()==null) {
			return;
		}
		for(Iterator iterator = treeNode.getChildNodes().iterator(); iterator.hasNext();) {
			treeNode = (TreeNode)iterator.next();
			updateDirectoryMapping(treeNode, targetSiteId);
		}
	}
	
	/**
	 * 设置目标目录
	 * @param directoryNode
	 * @param targetDirectoryIds
	 * @param targetDirectoryName
	 * @param copyChildDirectory
	 */
	private void setTargetDirectory(TreeNode directoryNode, String targetDirectoryIds, String targetDirectoryName, boolean copyChildDirectory) {
		directoryNode.setExtendPropertyValue("targetDirectoryIds", targetDirectoryIds);
		directoryNode.setExtendPropertyValue("targetDirectoryNames", targetDirectoryName);
		directoryNode.setExtendPropertyValue("copyChildDirectory", "" + copyChildDirectory);
		directoryNode.setNodeIcon(Environment.getContextPath() + "/jeaf/dataimport/icons/" + (targetDirectoryIds==null || targetDirectoryIds.equals("") ? "no.gif" : "yes.gif"));
	}
	
	/**
	 * 获取源目录树
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @return
	 */
	private Tree getSourceDirectoryTree(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		//加载源目录树
		Tree sourceDirectoryTree = (Tree)threadLocal.get();
		if(sourceDirectoryTree==null || !sourceDirectoryTree.getRootNode().getNodeId().equals("root_" + getImportDataName())) {
			WebSite targetSite = (WebSite)((SiteService)Environment.getService("siteService")).getDirectory(targetSiteId);
			sourceDirectoryTree = loadSourceDirectoryTree(true, targetSite.getId(), dataImportServiceClass, connection, parameter, sameSystem);
			threadLocal.set(sourceDirectoryTree);
		}
		return sourceDirectoryTree;
	}
	
	/**
	 * 获取树的存储路径
	 * @return
	 */
	private String getTreeSavePath(long targetSiteId, String dataImportServiceClass) {
		AttachmentService attachmentService = null;
		try {
			attachmentService = (AttachmentService)Environment.getService("attachmentService");
		}
		catch (ServiceException e) {
			
		}
		return attachmentService.getSavePath("jeaf/dataimport", "data", targetSiteId, true) + getImportDataName() + "_" + dataImportServiceClass + ".dat";
	}
	
	/**
	 * 获取目录服务
	 * @return
	 */
	protected DirectoryService getDirectoryService() {
		if(directoryService==null) {
			try {
				directoryService = (DirectoryService)Environment.getService(getDirectoryServiceName());
			}
			catch (ServiceException e) {
			
			}
		}
		return directoryService;
	}
}