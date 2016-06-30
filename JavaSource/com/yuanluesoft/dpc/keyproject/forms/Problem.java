package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProblem;

/**
 * 
 * @author linchuan
 *
 */
public class Problem extends Project {
	private KeyProjectProblem problem = new KeyProjectProblem();

	/**
	 * @return the problem
	 */
	public KeyProjectProblem getProblem() {
		return problem;
	}

	/**
	 * @param problem the problem to set
	 */
	public void setProblem(KeyProjectProblem problem) {
		this.problem = problem;
	}
}