/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * @author Administrator
 *
 *
 */
public class ClassTeacher extends Record {
	private long classId;
	private String title;
	private long teacherId;
	
	private Teacher teacher; //老师 
	
	/**
	 * @return Returns the classId.
	 */
	public long getClassId() {
		return classId;
	}
	/**
	 * @param classId The classId to set.
	 */
	public void setClassId(long classId) {
		this.classId = classId;
	}
	/**
	 * @return Returns the teacherId.
	 */
	public long getTeacherId() {
		return teacherId;
	}
	/**
	 * @param teacherId The teacherId to set.
	 */
	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the teacher
	 */
	public Teacher getTeacher() {
		return teacher;
	}
	/**
	 * @param teacher the teacher to set
	 */
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}
