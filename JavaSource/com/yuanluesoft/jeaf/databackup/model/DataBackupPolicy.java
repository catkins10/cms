package com.yuanluesoft.jeaf.databackup.model;

import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 
 * 备份策略,备份服务可以同时使用多个策略
 * @author linchuan
 *
 */
public class DataBackupPolicy {
	private String backupSavePath; //备份数据的存放目录,每个策略使用的目录不能一样
	private int backupInterval; //备份时间间隔,以天为单位,注：执行频率通过定时器配置
	private int backupCycle; //一个周期做几次备份,第一次是完全备份,后面的是增量备份,新的周期开始前,自动备份上一个周期的全部记录
	private String excludeFileTypes; //不做备份的文件类型,如视频文件,日志文件
	private boolean zipped; //是否压缩
	
	/**
	 * @return the backupCycle
	 */
	public int getBackupCycle() {
		return backupCycle;
	}
	/**
	 * @param backupCycle the backupCycle to set
	 */
	public void setBackupCycle(int backupCycle) {
		this.backupCycle = backupCycle;
	}
	/**
	 * @return the backupInterval
	 */
	public int getBackupInterval() {
		return backupInterval;
	}
	/**
	 * @param backupInterval the backupInterval to set
	 */
	public void setBackupInterval(int backupInterval) {
		this.backupInterval = backupInterval;
	}
	/**
	 * @return the backupSavePath
	 */
	public String getBackupSavePath() {
		return backupSavePath;
	}
	/**
	 * @param backupSavePath the backupSavePath to set
	 */
	public void setBackupSavePath(String backupSavePath) {
		try {
			this.backupSavePath = FileUtils.createDirectory(backupSavePath); //自动创建备份目录
		}
		catch(Exception e) {
			this.backupSavePath = backupSavePath;
		}
	}
	/**
	 * @return the zipped
	 */
	public boolean isZipped() {
		return zipped;
	}
	/**
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}
	/**
	 * @return the excludeFileTypes
	 */
	public String getExcludeFileTypes() {
		return excludeFileTypes;
	}
	/**
	 * @param excludeFileTypes the excludeFileTypes to set
	 */
	public void setExcludeFileTypes(String excludeFileTypes) {
		this.excludeFileTypes = excludeFileTypes;
	}
}
