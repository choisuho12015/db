package com.market.member;

/**
 * 시스템 이용자 정보를 담는 클래스입니다.
 * Person 클래스를 상속받아 이름, 전화번호, 주소 등의 기본 정보를 관리합니다.
 */
public class User extends Person {

	/**
	 * 이름과 전화번호만으로 유저 객체를 생성하는 생성자입니다.
	 * 주로 로그인 초기 단계나 주소 정보가 없는 신규 가입 시 사용됩니다.
	 */
	public User(String name, String phone) {
		super(name, phone);
	}

	/**
	 * 이름, 전화번호, 주소를 모두 포함하여 유저 객체를 생성하는 생성자입니다.
	 * DBConnection.saveUser()를 통해 DB에서 과거 주문 이력과 
	 * 저장된 주소 정보를 불러왔을 때 이 생성자가 호출되어 객체를 완성합니다.
	 */
	public User(String username, String phone, String address) {
		super(username, phone, address);
	}
}