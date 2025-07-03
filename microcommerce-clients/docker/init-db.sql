-- Script d'initialisation de la base de données PostgreSQL pour Microcommerce-Clients

-- Création de la table clients
CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion des données d'exemple
INSERT INTO clients (name, email, address, city, country, phone) VALUES
('Alice Dupont', 'alice.dupont@example.com', '123 Rue de Paris', 'Paris', 'France', '0123456789'),
('Bob Martin', 'bob.martin@example.com', '456 Avenue des Champs-Élysées', 'Paris', 'France', '9876543210'),
('Charlie Brown', 'charlie.brown@example.com', '789 Boulevard Saint-Germain', 'Paris', 'France', '0147258369');
ON CONFLICT (email) DO NOTHING;

-- Fonction pour mettre à jour automatiquement updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger pour mettre à jour automatiquement updated_at
CREATE TRIGGER update_clients_updated_at
    BEFORE UPDATE ON clients
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
-- Vérification des données insérées
SELECT 'Database initialized successfully!' as status;
SELECT COUNT(*) as total_clients FROM clients;
