package com.market.member;

public class UserInIt {
	public static User mUser; // static으로 선언하여 공유

	public static void init(String name, String phone) {
		mUser = new User(name, phone);
	}
	
	public static User getmUser() {
		return mUser;
	}

	public static void setmUser(User mUser) {
		UserInIt.mUser = mUser;
	}
}