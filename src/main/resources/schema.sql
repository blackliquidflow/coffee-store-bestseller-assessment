CREATE TABLE IF NOT EXISTS drinks
(
    id    IDENTITY,
    name  VARCHAR(50) NOT NULL,
    price DOUBLE      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS toppings
(
    id    IDENTITY,
    name  VARCHAR(50) NOT NULL,
    price DOUBLE      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS beverages
(
    id       IDENTITY,
    hash     LONG NOT NULL,
    drink_id long,
    PRIMARY KEY (id),
    CONSTRAINT beverages_fk FOREIGN KEY (drink_id) REFERENCES drinks (id)
);

CREATE TABLE IF NOT EXISTS beverage_toppings
(
    id          IDENTITY,
    beverage_id LONG NOT NULL,
    topping_id  LONG NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT beverage_toppings_fk1 FOREIGN KEY (beverage_id) REFERENCES beverages (id),
    CONSTRAINT beverage_toppings_fk2 FOREIGN KEY (topping_id) REFERENCES toppings (id)
);

CREATE TABLE IF NOT EXISTS cart
(
    id          IDENTITY,
    user_id     LONG   NOT NULL,
    beverage_id LONG   NOT NULL,
    price       DOUBLE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT cart_fk FOREIGN KEY (beverage_id) REFERENCES beverages (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id                        IDENTITY,
    user_id                   LONG NOT NULL,
    total_price               DOUBLE,
    total_price_with_discount DOUBLE,
    creation_time             datetime,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_lines
(
    id          IDENTITY,
    order_id    LONG NOT NULL,
    beverage_id LONG NOT NULL,
    price       DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT order_lines_fk FOREIGN KEY (beverage_id) REFERENCES beverages (id)
);

CREATE TABLE IF NOT EXISTS orders_history
(
    id              IDENTITY,
    order_id        LONG                     NOT NULL,
    order_line_id   LONG                     NOT NULL,
    order_date_time datetime,
    beverage_id     LONG                     NOT NULL,
    ingredient_type ENUM ('drink','topping') NOT NULL,
    ingredient_id   LONG                     NOT NULL,
    ingredient_name VARCHAR(50)              NOT NULL,
    price           DOUBLE                   NOT NULL,
    PRIMARY KEY (id)
);
