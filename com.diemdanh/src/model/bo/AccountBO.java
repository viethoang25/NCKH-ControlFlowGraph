package model.bo;

import model.bean.Account;
import model.dao.Provider;

public class AccountBO {

	public static Account checkAccount(String username, String password) {
		return Provider.getInstance().checkAccount(username, password);
	}
}
