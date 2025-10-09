-- Script para configurar o banco MySQL para o projeto Challenge Festo Twinovate Backend

-- 1. Criar o banco de dados
CREATE DATABASE IF NOT EXISTS app_challenge_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. Criar o usuário (se não existir)
CREATE USER IF NOT EXISTS 'glauro'@'localhost' IDENTIFIED BY 'Gaby@06850';

-- 3. Conceder privilégios no banco correto
GRANT ALL PRIVILEGES ON app_challenge_db.* TO 'glauro'@'localhost';

-- 4. Aplicar as mudanças
FLUSH PRIVILEGES;

-- 5. Verificar se o banco foi criado
SHOW DATABASES;

-- 6. Verificar o usuário
SELECT User, Host FROM mysql.user WHERE User = 'glauro';

-- 7. Usar o banco criado
USE app_challenge_db;

-- 8. Mostrar informações do banco
SELECT DATABASE() as 'Current Database';