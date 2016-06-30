package com.yuanluesoft.enterprise.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 知识类别(exam_knowledge)
 * @author linchuan
 *
 */
public class ExamKnowledge extends Record {
	private String knowledge; //知识类别

	/**
	 * @return the knowledge
	 */
	public String getKnowledge() {
		return knowledge;
	}

	/**
	 * @param knowledge the knowledge to set
	 */
	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}
}