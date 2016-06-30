package com.yuanluesoft.jeaf.databackup.processor.impl;

import java.io.File;
import java.sql.Date;

import com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 目录备份,注:当文件不可读时,会自动跳过
 * @author linchuan
 *
 */
public class DirectoryBackupProcessor implements DataBackupProcessor {
	private String path; //路径
	private String alias; //别名,必须唯一
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor#backup(java.lang.String, java.sql.Date)
	 */
	public void backup(String savePath, Date incrementDate, String excludeFileTypes) throws ServiceException {
		//按别名创建目录
		backupDirectory(path, FileUtils.createDirectory(savePath + alias), incrementDate, excludeFileTypes==null ? null : excludeFileTypes.split(","));
	}
	
	/**
	 * 递归:目录备份
	 * @param tobackupDirctory
	 * @param savePath
	 * @param incrementDate
	 * @param excludeFileTypes
	 */
	public static void backupDirectory(String tobackupDirctory, String savePath, Date incrementDate, String[] excludeFileTypes) {
		File tobackup = new File(tobackupDirctory);
		File[] files = tobackup.listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) { //目录
				//在备份存储目录创建同名子目录
				backupDirectory(tobackupDirctory + files[i].getName() + "/", FileUtils.createDirectory(savePath + files[i].getName()), incrementDate, excludeFileTypes); //备份子目录
				continue;
			}
			//处理文件
			if(incrementDate!=null && files[i].lastModified()<incrementDate.getTime()) { //文件修改时间在增量备份时间前
				continue; //不备份
			}
			//检查是否属于不需要备份的文件类型
			if(excludeFileTypes!=null) {
				String fileType = files[i].getName();
				int index = fileType.lastIndexOf('.');
				if(index!=-1) {
					fileType = fileType.substring(index + 1);
					int j = excludeFileTypes.length - 1;
					for(; j>=0 && !excludeFileTypes[j].equalsIgnoreCase(fileType); j--);
					if(j>=0) { //属于不需要备份的类型
						continue;
					}
				}
			}
			//备份文件
			FileUtils.copyFile(tobackupDirctory + files[i].getName(), savePath + files[i].getName(), true, false);
		}
		//如果没有文件,删除空目录
		if(FileUtils.isEmpty(savePath)) {
			FileUtils.deleteDirectory(savePath);
		}
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		if(!path.endsWith("/")) {
			path += "/";
		}
		this.path = path;
	}
}