# Etapa 1: Build da aplicação
FROM node:18-alpine as build

WORKDIR /app
COPY . .
RUN yarn install --immutable
RUN yarn build

# Etapa 2: Servir com nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
