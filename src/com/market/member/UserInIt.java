package com.market.member;

public class UserInIt {
	private static User mUser; // 현재 시스템에 로그인한 사용자 정보를 유지하는 정적 객체

	/**
	 * 외부에서 생성된 User 객체를 현재 시스템의 로그인 유저로 설정합니다.
	 * DBConnection.saveUser()에서 조회된 정보를 바탕으로 유저 정보를 업데이트할 때 사용됩니다.
	 */
	public static void setmUser(User mUser) {
		UserInIt.mUser = mUser;
	}

	/**
	 * 사용자의 이름과 전화번호(이메일)를 받아 새로운 User 객체를 생성하고 초기화합니다.
	 * 로그인 프로세스의 시작 단계에서 호출됩니다.
	 */
	public static void init(String name, String phone) {
		mUser = new User(name, phone);
	}
	
	/**
	 * 현재 로그인 중인 사용자의 객체를 반환합니다.
	 * 이 메서드를 통해 프로그램 어디서든 사용자의 주소, 이름 등 정보를 참조할 수 있습니다.
	 */
	public static User getmUser() {
		return mUser;
	}
}