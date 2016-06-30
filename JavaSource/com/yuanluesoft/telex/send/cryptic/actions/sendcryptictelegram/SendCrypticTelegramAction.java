package com.yuanluesoft.telex.send.cryptic.actions.sendcryptictelegram;

import com.yuanluesoft.telex.send.base.actions.sendtelegram.SendTelegramAction;

/**
 * 
 * @author linchuan
 *
 */
public class SendCrypticTelegramAction extends SendTelegramAction {

	public SendCrypticTelegramAction() {
		super();
		isCryptic = true;
	}
}
