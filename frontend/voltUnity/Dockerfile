################################
## BUILD ENVIRONMENT ###########
################################
FROM node:22-alpine3.20 AS build

# Cria o diretório de trabalho corretamente
WORKDIR /usr/src/app

# Copia os arquivos de dependências (verifique se package-lock.json existe)
COPY package.json package-lock.json ./

# Instala dependências com npm (modo estrito)
RUN npm ci --omit=dev

# Copia o restante do projeto (excluindo node_modules)
COPY . .

# Build da aplicação React
RUN npm run build

################################
#### PRODUCTION ENVIRONMENT ####
################################
FROM nginx:stable-alpine AS production

# Copia os arquivos buildados
COPY --from=build /usr/src/app/dist /usr/share/nginx/html

# Configuração do Nginx
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]