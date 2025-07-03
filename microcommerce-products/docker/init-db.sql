-- Script d'initialisation de la base de données PostgreSQL pour Microcommerce-products

-- Création de la table products
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion des données d'exemple
INSERT INTO products (name, price) VALUES 
    ('Product A', 29.99),
    ('Product B', 49.99),
    ('Product C', 19.99),
    ('Laptop Dell XPS', 999.99),
    ('iPhone 15', 799.99),
    ('Samsung Galaxy S24', 699.99),
    ('MacBook Pro', 1299.99),
    ('iPad Air', 549.99),
    ('AirPods Pro', 249.99),
    ('Apple Watch', 399.99)
ON CONFLICT DO NOTHING;

-- Fonction pour mettre à jour automatiquement updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger pour mettre à jour automatiquement updated_at
CREATE TRIGGER update_products_updated_at 
    BEFORE UPDATE ON products 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Vérification des données insérées
SELECT 'Database initialized successfully!' as status;
SELECT COUNT(*) as total_products FROM products;
