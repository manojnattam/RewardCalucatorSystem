CREATE TABLE CUSTOMER(
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(20) NOT NULL
            );
CREATE TABLE TRANSACTION(
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        customer_id BIGINT NOT NULL,
        amount DOUBLE NOT NULL,
        date DATE NOT NULL,
        CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id)
        );

INSERT INTO CUSTOMER (id, name) VALUES (1, 'Ram');
INSERT INTO CUSTOMER (id, name) VALUES (2, 'Sam');

-- Transactions for Ram (customer id = 1)
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 1, 120.0, '2025-04-14'); -- 90 pts
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 1, 75.0, '2025-03-25'); -- 25 pts
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 1, 45.0, '2025-04-25'); -- 0 pts
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 1, 45.0, '2025-02-25'); -- 0 pts

-- Transactions for Jane Smith (customer_id = 2)
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 2, 200.0, '2025-04-14'); -- 250 pts
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 2, 99.0, '2025-03-25'); -- 49 pts
INSERT INTO TRANSACTION ( customer_id, amount, date) VALUES ( 2, 51.0,'2025-02-25'); -- 1 pt