CREATE TABLE IF NOT EXISTS system_config(
    service_fee DECIMAL(5,2),
    small_order_fee DECIMAL(5,2)
);
-- TODO: add audit table, See required modifications, see unimplemented columns
------------------------------USER & CUSTOMER---------------------
CREATE TABLE IF NOT EXISTS permission(
    permission_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
    permission VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS role(
    role_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
    role_name VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS role_permission(
    permission_id INT NOT NULL,-- REFERENCES permission(permission)
    role_id INT NOT NULL -- REFERENCES role(role_id)
);
CREATE TABLE IF NOT EXISTS user_type(
    user_type_id INT GENERATED always as IDENTITY,
    user_type_name VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS users(
  user_id UUID  PRIMARY KEY DEFAULT uuidv7(),
  user_type_id INT NOT NULL, -- REFERENCES user_type(user_type_id)
  user_first_name VARCHAR(50) NOT NULL,
  user_last_name VARCHAR(50) NOT NULL ,
  user_birth_date DATE,
  user_phone VARCHAR(15) NOT NULL ,
  user_email VARCHAR(50) NOT NULL UNIQUE ,
  user_password VARCHAR(255) NOT NULL ,
--     user_gender CHAR(1),
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP,
  is_enabled BIT DEFAULT CAST(1 AS BIT)
);
CREATE TABLE IF NOT EXISTS user_role(
    role_id INT NOT NULL, --REFERENCES role(role_id)
    user_id UUID NOT NULL  --REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS customer(
    customer_id  UUID PRIMARY KEY DEFAULT uuidv7(),
    customer_user_id UUID NOT NULL UNIQUE, -- REFERENCES users(user_id)
    customer_default_address_id UUID,  --REFERENCES customer_address(customer_address_id)
    customer_preferred_payment_id INT --  REFERENCES payment_type_config(payment_type_config_id)
);
CREATE TABLE IF NOT EXISTS customer_address(
    customer_address_id UUID PRIMARY KEY DEFAULT uuidv7(),
    customer_address_customer_id UUID NOT NULL, --REFERENCES customer(customer_id)
    customer_address_label VARCHAR(20) NOT NULL ,
    customer_address_city VARCHAR(20) NOT NULL ,
    customer_address_street VARCHAR(20) NOT NULL ,
    customer_address_building VARCHAR(20) NOT NULL ,
    customer_address_apartment VARCHAR(20) NOT NULL ,
    customer_address_phone_number VARCHAR(15) NOT NULL ,
    customer_address_note VARCHAR(500)
);
------------------------------RESTAURANT---------------------
CREATE TABLE IF NOT EXISTS restaurant(
    restaurant_id UUID PRIMARY KEY DEFAULT uuidv7(),
    restaurant_name VARCHAR(100) NOT NULL ,
    restaurant_description VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS restaurant_branch(
    branch_id UUID PRIMARY KEY DEFAULT uuidv7(),
    branch_rest_id UUID NOT NULL, --REFERENCES restaurant(restaurant_id)
    branch_delivery_fee DECIMAL(6,2) CHECK ( branch_delivery_fee >= 0 ),
    branch_min_order DECIMAL(6,2) CHECK ( branch_min_order >= 0 ),
    branch_city VARCHAR(20) NOT NULL ,
    branch_open_time time NOT NULL ,
    branch_close_time TIME NOT NULL ,
    branch_phone_number VARCHAR(15) NOT NULL,
    branch_estimated_delivery_time INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP,
    created_by UUID NOT NULL , --REFERENCES users(user_id)
    modified_by UUID , --REFERENCES users(user_id)
    admin_id UUID --REFERENCES users(user_id)
);
CREATE TABLE IF NOT EXISTS category(
    category_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
    category_name VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS restaurant_category(
    category_id INT NOT NULL , --REFERENCES category(category_id)
    restaurant_id UUID NOT NULL,-- REFERENCES restaurant(restaurant_id)
    PRIMARY KEY (category_id, restaurant_id)
);
CREATE TABLE IF NOT EXISTS restaurant_menu(
    restaurant_menu_id UUID PRIMARY KEY DEFAULT uuidv7(),
    restaurant_menu_rest_id UUID NOT NULL, --  REFERENCES restaurant(restaurant_id)
    restaurant_menu_name VARCHAR(30) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP,
    created_by UUID NOT NULL , --REFERENCES users(user_id)
    modified_by UUID --REFERENCES users(user_id)
);
CREATE TABLE IF NOT EXISTS menu_item(
    menu_item_id UUID PRIMARY KEY DEFAULT uuidv7(),
    restaurant_menu_id UUID NOT NULL,-- REFERENCES restaurant_menu(restaurant_menu_id)
    menu_item_description VARCHAR(255),
    menu_item_name VARCHAR(50) NOT NULL ,
    menu_item_price DECIMAL(9,2) NOT NULL CHECK ( menu_item_price > 0 ),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP,
    created_by UUID NOT NULL , --REFERENCES users(user_id)
    modified_by UUID --REFERENCES users(user_id)
);
CREATE TABLE IF NOT EXISTS restaurant_rate(
    restaurant_rate_id UUID PRIMARY KEY DEFAULT uuidv7(),
    restaurant_rate_restaurant_id UUID NOT NULL,-- REFERENCES restaurant(restaurant_id)
    restaurant_rate_customer_id UUID NOT NULL,-- REFERENCES customer(customer_id)
    restaurant_rate_rating INT,
    restaurant_rate_comment VARCHAR(500) NOT NULL ,
    restaurant_rate_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS coupon(
    coupon_id UUID PRIMARY KEY DEFAULT uuidv7(),
    coupon_restaurant_id UUID NOT NULL,-- REFERENCES restaurant(restaurant_id)
    coupon_amount DECIMAL(6,2) CHECK ( coupon_amount >0 ),
--     coupon_min_value, WHAT DOES THIS COLUMN DO
--     coupon_discount_percent, WHAT DOES THIS COLUMN DO
    coupon_available_from TIMESTAMP NOT NULL ,
    coupon_available_to TIMESTAMP NOT NULL ,
    coupon_is_active BIT NOT NULL ,
    coupon_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    coupon_last_modified TIMESTAMP

);
------------------------------CART---------------------

-- we could use the customer_id as PK here
CREATE TABLE IF NOT EXISTS cart(
    cart_id UUID PRIMARY KEY DEFAULT uuidv7(),
    cart_customer_id UUID NOT NULL , -- REFERENCES customer(customer_id)
    is_locked BIT DEFAULT CAST(0 AS BIT),
    cart_current_rest_id UUID --REFERENCES restaurant_branch(branch_id)
);
CREATE TABLE IF NOT EXISTS cart_item(
    cart_item_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
    cart_item_cart_id UUID ,--REFERENCES cart(cart_id)
    menu_item_id UUID, --REFERENCES menu_item(menu_item_id)
    cart_item_quantity INT CHECK ( cart_item_quantity > 0 ),
    cart_item_note VARCHAR(255)
);

------------------------------ORDER---------------------

CREATE TABLE IF NOT EXISTS order_status(
    order_status_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_status_name VARCHAR(20),
    order_status_description VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS order_tracking(
    order_tracking_id UUID PRIMARY KEY DEFAULT uuidv7(),
    order_tracking_status_id INT NOT NULL,-- REFERENCES order_status(order_status_id)
    order_tracking_order_id UUID NOT NULL, --REFERENCES orders(order_id)
    order_tracking_description VARCHAR(50) NOT NULL ,
    order_tracking_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--  CHANGED THE COL CHANGED AT TO CREATED AT SINCE IT WILL ONLY CHANGE ONCE

);
-- REMOVED ORDER_STATUS COL SINCE IT IS ALREADY IN ORDER TRACKING
CREATE TABLE IF NOT EXISTS orders(
    order_id UUID PRIMARY KEY DEFAULT uuidv7(),
    order_address_id UUID NOT NULL , --REFERENCES customer_address(customer_address_id)
    order_customer_id UUID NOT NULL ,--REFERENCES customer(customer_id)
    order_restaurant_branch_id UUID NOT NULL , --REFERENCES restaurant_branch(branch_id)
    order_coupon_id UUID,-- REFERENCES coupon(coupon_id)
    order_discount_value DECIMAL(6,2),
    order_subtotal DECIMAL(7,2) CHECK ( order_subtotal > 0 ),
    order_fee DECIMAL(6,2) DEFAULT 0,
    order_total DECIMAL(10,2) NOT NULL ,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_note VARCHAR(255),
    order_status_id INT NOT NULL --REFERENCES order_status(order_status_id)
);

CREATE TABLE IF NOT EXISTS order_item(
    order_item_id UUID PRIMARY KEY DEFAULT uuidv7(),
    order_item_order_id UUID NOT NULL , -- REFERENCES orders(order_id)
    order_item_menu_item_id UUID NOT NULL , --REFERENCES menu_item(menu_item_id)
    order_item_unit_price DECIMAL(9,2) NOT NULL CHECK ( order_item_unit_price > 0 ),
    order_item_quantity INT NOT NULL check ( order_item_quantity > 0 ),
    order_item_subtotal DECIMAL(10,2),
    order_item_note VARCHAR(255)
);
------------------------------PAYMENT---------------------

CREATE TABLE IF NOT EXISTS payment_integration_type(
    payment_integration_type_name VARCHAR(20) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS payment_type_config(
    payment_type_config_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
     payment_integration_type VARCHAR(20) NOT NULL , --REFERENCES payment_integration_type(payment_integration_type_name)
    config_details TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS transaction_status(
status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions(
    transaction_id UUID PRIMARY KEY DEFAULT uuidv7(),
    transaction_status VARCHAR(20) NOT NULL , --REFERENCES transaction_status(status)
    transaction_order_id UUID NOT NULL ,--REFERENCES orders(order_id)
    transaction_payment_type VARCHAR(20), --REFERENCES payment_integration_type(payment_integration_type_name)
    transaction_customer_id UUID NOT NULL ,--REFERENCES customer(customer_id)
    transaction_rest_branch_id UUID NOT NULL ,--REFERENCES restaurant_branch(branch_id)
    transaction_amount DECIMAL(10,2) CHECK (transaction_amount > 0),
    transaction_time TIMESTAMP DEFAULT  CURRENT_TIMESTAMP
);

