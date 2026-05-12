-- 1. 기존 DB 삭제 (노란 느낌표가 떠도 무시하세요. 없으면 안 지운다는 뜻입니다.)
DROP DATABASE IF EXISTS shopping_db;

-- 2. DB 새로 만들기
CREATE DATABASE shopping_db;

-- 3. 이 DB를 사용하겠다고 선언
USE shopping_db;

CREATE TABLE member (
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(100) PRIMARY KEY, -- 자바에서 이메일을 여기에 저장함
    address VARCHAR(255)            -- 배송 주소
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY, -- 주문 번호 (자동 생성)
    user_email VARCHAR(100) NOT NULL,        -- 주문자 이메일
    total_price INT NOT NULL,                -- 최종 결제 금액
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 주문 시각
    
    -- member 테이블의 phone(이메일)과 연결
    FOREIGN KEY (user_email) REFERENCES member(phone)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,                   -- 위 orders 테이블의 번호와 연결
    book_id VARCHAR(50) NOT NULL,            -- 도서 ID
    quantity INT NOT NULL,                   -- 수량
    
    -- orders 테이블이 삭제되면 상세 내역도 함께 삭제
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;