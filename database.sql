DROP DATABASE IF EXISTS car_rental_ms;
CREATE DATABASE car_rental_ms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE car_rental_ms;

-- ===============================================
-- USER & ROLE MODULE (4 tables)
-- ===============================================

-- Table: user
CREATE TABLE users
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password      VARCHAR(255)        NOT NULL,
    phone_number  VARCHAR(20),
    date_of_birth DATE,
    gender        ENUM ('MALE', 'FEMALE', 'OTHER'),
    avatar_url    VARCHAR(500),
    status        ENUM ('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE',
    created_at    TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user_email (email),
    INDEX idx_user_username (username),
    INDEX idx_user_status (status)
);

-- Table: role
CREATE TABLE roles
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_role_name (name)
);

-- Table: user_role
CREATE TABLE user_roles
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    user_id    INT NOT NULL,
    role_id    INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_role (user_id, role_id),
    INDEX idx_user_role_user (user_id),
    INDEX idx_user_role_role (role_id)
);

-- Table: user_session
CREATE TABLE user_sessions
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT                 NOT NULL,
    refresh_token_id VARCHAR(255) UNIQUE NOT NULL,
    device_info      VARCHAR(500),
    ip_address       VARCHAR(45),
    user_agent       TEXT,
    login_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expired_at       TIMESTAMP           NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_user_session_user (user_id),
    INDEX idx_user_session_token (refresh_token_id),
    INDEX idx_user_session_expired (expired_at)
);

-- Table: otp_request
CREATE TABLE otp_requests
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    code         VARCHAR(10)                                                   NOT NULL,
    email        VARCHAR(100)                                                  NOT NULL,
    request_type ENUM ('REGISTRATION', 'FORGOT_PASSWORD', 'HOST_REGISTRATION') NOT NULL,
    status       ENUM ('PENDING', 'VERIFIED', 'FAILED', 'EXPIRED', 'CANCELLED') DEFAULT 'PENDING',
    created_at   TIMESTAMP                                                      DEFAULT CURRENT_TIMESTAMP,
    expired_at   TIMESTAMP                                                     NOT NULL,

    INDEX idx_otp_email (email),
    INDEX idx_otp_code (code),
    INDEX idx_otp_status (status),
    INDEX idx_otp_expired (expired_at)
);

-- ===============================================
-- USER IDENTIFICATION MODULE (3 tables)
-- ===============================================

-- Table: driving_license
CREATE TABLE driving_licenses
(
    id                      INT PRIMARY KEY AUTO_INCREMENT,
    user_id                 INT                NOT NULL,
    verified_by_user_id     INT,
    verified_at             TIMESTAMP          NULL,
    license_number          VARCHAR(50) UNIQUE NOT NULL,
    full_name_on_license    VARCHAR(100)       NOT NULL,
    status                  ENUM ('PENDING', 'VERIFIED', 'REJECTED') DEFAULT 'PENDING',
    license_front_image_url VARCHAR(500)       NOT NULL,
    license_back_image_url  VARCHAR(500)       NOT NULL,
    created_at              TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (verified_by_user_id) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_driving_license_user (user_id),
    INDEX idx_driving_license_number (license_number),
    INDEX idx_driving_license_status (status)
);

-- Table: user_identification
CREATE TABLE user_identifications
(
    id                                INT PRIMARY KEY AUTO_INCREMENT,
    user_id                           INT                NOT NULL,
    verified_by_user_id               INT,
    verified_at                       TIMESTAMP          NULL,
    full_name                         VARCHAR(100)       NOT NULL,
    phone_number                      VARCHAR(20)        NOT NULL,
    email                             VARCHAR(100)       NOT NULL,
    national_id_number                VARCHAR(20) UNIQUE NOT NULL,
    national_id_front_image_url       VARCHAR(500)       NOT NULL,
    selfie_with_national_id_image_url VARCHAR(500)       NOT NULL,
    status                            ENUM ('PENDING', 'VERIFIED', 'REJECTED') DEFAULT 'PENDING',
    created_at                        TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    updated_at                        TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (verified_by_user_id) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_user_identification_user (user_id),
    INDEX idx_user_identification_national_id (national_id_number),
    INDEX idx_user_identification_status (status)
);

-- ===============================================
-- CAR MODULE (4 tables)
-- ===============================================

-- Table: car_location
CREATE TABLE car_locations
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    user_id         INT          NOT NULL,
    province        VARCHAR(100) NOT NULL,
    district        VARCHAR(100) NOT NULL,
    ward            VARCHAR(100) NOT NULL,
    address_details TEXT         NOT NULL,
    latitude        DECIMAL(10, 8),
    longitude       DECIMAL(11, 8),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_car_location_user (user_id),
    INDEX idx_car_location_coords (latitude, longitude)
);

-- Table: car
CREATE TABLE cars
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    owner_id             INT                                               NOT NULL,
    location_id          INT                                               NOT NULL,
    license_plate_number VARCHAR(20) UNIQUE                                NOT NULL,
    year_of_manufacture  INT                                               NOT NULL,
    brand                VARCHAR(50)                                       NOT NULL,
    model                VARCHAR(100)                                      NOT NULL,
    number_of_seats      INT                                               NOT NULL,
    fuel_type            ENUM ('GASOLINE', 'DIESEL', 'ELECTRIC', 'HYBRID') NOT NULL,
    transmission_type    ENUM ('MANUAL', 'AUTOMATIC')                      NOT NULL,
    fuel_consumption     DECIMAL(4, 2),
    base_price_per_day   DECIMAL(10, 2)                                    NOT NULL,
    description          TEXT,
    approval_status      ENUM ('PENDING', 'APPROVED', 'REJECTED')        DEFAULT 'PENDING',
    status               ENUM ('ACTIVE', 'RENTED', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE',
    created_at           TIMESTAMP                                       DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP                                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES car_locations (id) ON DELETE RESTRICT,
    INDEX idx_car_owner (owner_id),
    INDEX idx_car_location (location_id),
    INDEX idx_car_license_plate (license_plate_number),
    INDEX idx_car_status (status),
    INDEX idx_car_approval_status (approval_status),
    INDEX idx_car_brand_model (brand, model)
);

-- Table: car_image
CREATE TABLE car_images
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    car_id      INT          NOT NULL,
    image_order INT          NOT NULL,
    image_url   VARCHAR(500) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE,
    UNIQUE KEY unique_car_image_order (car_id, image_order),
    INDEX idx_car_image_car (car_id),
    INDEX idx_car_image_order (image_order)
);

-- Table: car_certificate
CREATE TABLE car_certificates
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    car_id           INT NOT NULL,
    registration_url VARCHAR(500),
    inspection_url   VARCHAR(500),
    insurance_url    VARCHAR(500),
    front_image_url  VARCHAR(500),
    left_image_url   VARCHAR(500),
    right_image_url  VARCHAR(500),
    back_image_url   VARCHAR(500),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE,
    INDEX idx_car_certificate_car (car_id)
);

-- ===============================================
-- TRIP MODULE (3 tables)
-- ===============================================

-- Table: trip
CREATE TABLE trips
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    trip_code     VARCHAR(20) UNIQUE NOT NULL,
    car_id        INT                NOT NULL,
    renter_id     INT                NOT NULL,
    pickup_date   DATETIME           NOT NULL,
    return_date   DATETIME           NOT NULL,
    total_amount  DECIMAL(12, 2)     NOT NULL,
    status        ENUM ('PENDING', 'APPROVED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    approval_time TIMESTAMP          NULL,
    created_at    TIMESTAMP                                                                         DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP                                                                         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE RESTRICT,
    FOREIGN KEY (renter_id) REFERENCES users (id) ON DELETE RESTRICT,
    INDEX idx_trip_code (trip_code),
    INDEX idx_trip_car (car_id),
    INDEX idx_trip_renter (renter_id),
    INDEX idx_trip_status (status),
    INDEX idx_trip_dates (pickup_date, return_date),

    CONSTRAINT chk_trip_dates CHECK (return_date > pickup_date),
    CONSTRAINT chk_trip_amount CHECK (total_amount >= 0)
);

-- Table: trip_cancellation
CREATE TABLE trip_cancellations
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    trip_id              INT NOT NULL,
    cancelled_by_user_id INT NOT NULL,
    cancelled_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE,
    FOREIGN KEY (cancelled_by_user_id) REFERENCES users (id) ON DELETE RESTRICT,
    INDEX idx_trip_cancellation_trip (trip_id),
    INDEX idx_trip_cancellation_user (cancelled_by_user_id)
);


-- ADD ROLEs
INSERT INTO roles (name, description)
VALUES ('ADMIN', 'Administrator with full access'),
       ('HOST', 'Car owner who can manage their own cars'),
       ('RENTER', 'User who can rent cars');


-- passwrod: Thien123456
INSERT INTO  users (email, password, username, status)
VALUES ('thienvolc@gmail.com', '$2a$10$DMDGPmWAKn/js5uv.2OivuISa5oOdT68vEr/gEGYPEcWDGF4O7SUO', 'thiendeptraivocung', 'ACTIVE');

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'thienvolc@gmail.com'), (SELECT id FROM roles WHERE name = 'ADMIN'));
