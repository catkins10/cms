package com.yuanluesoft.jeaf.timetable.exception;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class NoTimetableException extends ServiceException {

	public NoTimetableException() {
		super();
	}

	public NoTimetableException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoTimetableException(String arg0) {
		super(arg0);
	}

	public NoTimetableException(Throwable cause) {
		super(cause);
	}
}