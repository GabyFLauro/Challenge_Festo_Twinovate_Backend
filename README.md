Sprint 01 - Advanced Programming & Mobile Dev - 3ECA_FIAP_2025 - Twinovate_Challenge_Festo

Sprint 02 - Advanced Programming & Mobile Dev - 3ECA_FIAP_2025 - Twinovate_Challenge_Festo

Sprint 03 - Advanced Programming & Mobile Dev - 3ECA_FIAP_2025 - Twinovate_Challenge_Festo

Luana Alves de Santana RM: 98546

Kauhe Serpa Do Val RM: 552027

Gabriella Francisco de Lauro RM: 99280

Luis Felipe Machareth Bannwart RM: 99879

Caio Yudi Ozaki Godinho RM: 552505

---

# Como rodar o projeto completo (backend + frontend)

## Pré-requisitos
- Java 17+
- Node.js 18+
- npm ou yarn
- (Opcional) Postman para testar APIs

## 1. Rodando o Backend (Spring Boot)

1. Clone este repositório e entre na pasta do backend:
   ```sh
   git clone <repo-backend-url>
   cd Challenge_Festo_Twinovate_Backend
   ```
2. No Windows, rode:
   ```sh
   mvnw.cmd spring-boot:run
   ```
   No Linux/Mac:
   ```sh
   ./mvnw spring-boot:run
   ```
3. O backend estará disponível em: http://localhost:8080

## 2. Rodando o Frontend (React/React Native)

1. Clone o repositório do frontend:
   ```sh
   git clone https://github.com/GabyFLauro/Challenge_Festo_Twinovate.git
   cd Challenge_Festo_Twinovate
   ```
2. Instale as dependências:
   ```sh
   npm install
   # ou
   yarn
   ```
3. Rode o frontend:
   ```sh
   npm start
   # ou
   yarn start
   ```
4. O frontend estará disponível em: http://localhost:8081

## 3. Fluxo de uso
- Cadastre um usuário pelo frontend (ou via POST /usuarios no backend)
- Faça login pelo frontend (ou via POST /auth/login no backend)
- Use as funcionalidades do sistema normalmente

## 4. Observações
- O backend usa banco H2 local (acessível em http://localhost:8080/h2-console)
- O backend já está configurado para CORS e integração com o frontend
- Endpoints principais: /usuarios, /auth/login, /sensores, /api/readings

---
