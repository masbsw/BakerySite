UPDATE product
SET product_price = 0
WHERE product_price IS NULL;

UPDATE product
SET product_weight = 0
WHERE product_weight IS NULL;

UPDATE product
SET product_quantity = 0
WHERE product_quantity IS NULL;

ALTER TABLE product
    ALTER COLUMN product_price SET DEFAULT 0,
    ALTER COLUMN product_price SET NOT NULL,
    ALTER COLUMN product_weight SET DEFAULT 0,
    ALTER COLUMN product_weight SET NOT NULL,
    ALTER COLUMN product_quantity SET DEFAULT 0,
    ALTER COLUMN product_quantity SET NOT NULL;

SELECT setval(
    pg_get_serial_sequence('product', 'product_id'),
    COALESCE(MAX(product_id), 1),
    MAX(product_id) IS NOT NULL
)
FROM product;
