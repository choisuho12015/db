package com.market.member;

public class User extends Person {
	// 생성자 1: 이름과 연락처(이메일)
	public User(String name, String phone) {
		super(name, phone);
	}

	// 생성자 2: 이름, 연락처, 주소
	public User(String username, String phone, String address) {
		super(username, phone, address);
	}

	/**
	 * ✅ MainWindow에서 호출하는 메서드
	 * 구글 이메일이 Person의 phone 필드에 저장되므로 이를 반환합니다.
	 */
	public String getEmail() {
		return super.getPhone();
	}

	// Person에 getPhone, getAddress, setAddress가 구현되어 있다고 가정합니다.
	// 만약 Person에 없다면 여기에 직접 구현해야 합니다.
}