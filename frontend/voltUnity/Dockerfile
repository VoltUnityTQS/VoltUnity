# # Etapa 1: Build da aplicação
# FROM node:18-alpine as build

# WORKDIR /app
# COPY . .
# RUN yarn install --immutable
# RUN yarn build

# # Etapa 2: Servir com nginx
# FROM nginx:alpine
# COPY --from=build /app/dist /usr/share/nginx/html
# COPY nginx.conf /etc/nginx/nginx.conf
# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]

# Stage 1: Build React app
FROM node:18-alpine AS build

WORKDIR /app

# Copia package.json e package-lock.json para cache de dependências
COPY package*.json ./

RUN npm install

# Copia o restante do código
COPY . .

# Executa build
RUN npm run build

# Stage 2: Serve com Nginx
FROM nginx:stable-alpine

# Copia build React para o diretório padrão do nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Remove o default.conf e adiciona sua configuração, se precisar
# COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
