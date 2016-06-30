package com.yuanluesoft.jeaf.payment.exception;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class TransferPasswordMismatchException extends ServiceException {

	public TransferPasswordMismatchException() {
		super();
	}

	public TransferPasswordMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransferPasswordMismatchException(String arg0) {
		super(arg0);
	}

	public TransferPasswordMismatchException(Throwable cause) {
		super(cause);
	}
}