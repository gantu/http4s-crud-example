CREATE TABLE offices (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       zip VARCHAR(100) UNIQUE,
       name VARCHAR(255) NOT NULL
);

CREATE TABLE shipments (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       office_id INT8 NOT NULL REFERENCES offices(id) ON DELETE CASCADE,
       shipment_type VARCHAR(20) NOT NULL
       );
