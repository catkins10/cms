package com.yuanluesoft.jeaf.databackup.processor;

import java.sql.Date;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 备份处理器,如mysql数据库备份,oracle数据备份,目录备份
 * @author linchuan
 *
 */
public interface DataBackupProcessor {

	/**
	 * 备份
	 * @param savePath 数据保存路径
	 * @param minDataTime 增量备份时,备份incrementDate之后修改的数据,null表示都备份
	 * @param excludeFileTypes 不需要备份的文件类型 
	 * @throws ServiceException
	 */
	public void backup(String savePath, Date incrementDate, String excludeFileTypes) throws ServiceException;
}