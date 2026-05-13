-- 1. 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS shopping_mall;
USE shopping_mall;

-- 2. 도서(book) 테이블 생성 (기존 제공 코드)
CREATE TABLE IF NOT EXISTS book (
    id VARCHAR(50) PRIMARY KEY,      -- 도서 ID
    name VARCHAR(100) NOT NULL,      -- 도서명
    price INT NOT NULL,              -- 가격
    author VARCHAR(50),              -- 저자
    description TEXT,                -- 설명
    category VARCHAR(50),            -- 분야
    release_date VARCHAR(50)         -- 출판일
);

-- 3. 회원(member) 테이블 생성
-- 자바의 User 객체와 매핑되며, phone(이메일)을 식별자로 사용합니다.
CREATE TABLE IF NOT EXISTS member (
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(100) PRIMARY KEY,  -- 이메일 주소를 PK로 사용
    address VARCHAR(255)             -- 배송지 주소
);

-- 4. 주문(orders) 테이블 생성
-- 누가 총 얼마를 결제했는지 기록하는 마스터 테이블입니다.
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY, -- 주문 번호 (자동 생성)
    user_email VARCHAR(100),                 -- member 테이블의 phone과 연결
    total_price INT NOT NULL,                -- 총 주문 금액
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 주문 일시
    FOREIGN KEY (user_email) REFERENCES member(phone)
);

-- 5. 주문 상세(order_items) 테이블 생성
-- 한 번의 주문에 어떤 책들이 포함되었는지 상세 기록합니다.
CREATE TABLE IF NOT EXISTS order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,                            -- orders 테이블의 order_id와 연결
    book_id VARCHAR(50),                     -- book 테이블의 id와 연결
    quantity INT NOT NULL,                   -- 수량
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

-- ---------------------------------------------------------
-- [테스트 데이터 삽입] - 자바 실행 전 반드시 실행하세요
-- ---------------------------------------------------------

-- 1) 도서 샘플 데이터
INSERT INTO book (id, name, price, author, description, category, release_date)
VALUES ('ISBN1234', '자바 마스터', 30000, '홍길동', '자바 기초부터 DB까지', '프로그래밍', '2026-05-01');

-- 2) 회원 샘플 데이터 (로그인 시 입력할 이름과 이메일 정보를 넣으세요)
INSERT INTO member (name, phone, address)
VALUES ('사용자', 'test@gmail.com', '대전광역시 유성구');

-- 3) 주문 기록 데이터 (이미 주문한 내역이 있어야 조회가 가능합니다)
INSERT INTO orders (user_email, total_price)
VALUES ('test@gmail.com', 30000);

-- 4) 주문 상세 데이터 (주문번호 1번에 해당 도서 연결)
-- 처음 실행 시 order_id는 1로 자동 부여됩니다.
INSERT INTO order_items (order_id, book_id, quantity)
VALUES (1, 'ISBN1234', 1);