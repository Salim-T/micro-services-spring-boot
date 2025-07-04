-- Script d'initialisation de la base de données PostgreSQL pour Microcommerce-Orders

-- Création de la table orders
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    description TEXT,
    total DECIMAL(10, 2) NOT NULL,
    client_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Création de la table order_products pour les lignes de produits de chaque commande
CREATE TABLE IF NOT EXISTS order_products (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    UNIQUE(order_id, product_id)  -- Un produit ne peut apparaître qu'une fois par commande
);

-- Fonction pour mettre à jour automatiquement updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger pour mettre à jour automatiquement updated_at
CREATE TRIGGER update_orders_updated_at 
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Insertion de données de test (optionnel)
-- INSERT INTO orders (description, total, client_id) VALUES 
--     ('Première commande test', 150.50, 1),
--     ('Deuxième commande test', 299.99, 2);

-- INSERT INTO order_products (order_id, product_id, quantity, unit_price, subtotal) VALUES
--     (1, 1, 2, 50.00, 100.00),    -- 2x Produit 1 à 50€ = 100€
--     (1, 2, 1, 50.50, 50.50),     -- 1x Produit 2 à 50,50€ = 50,50€
--     (2, 3, 3, 99.99, 299.97);    -- 3x Produit 3 à 99,99€ = 299,97€

-- Vérification des données insérées
SELECT 'Database initialized successfully!' as status;
SELECT COUNT(*) as total_orders FROM orders;
SELECT COUNT(*) as total_order_products FROM order_products;   
