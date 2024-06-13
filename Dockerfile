# Utiliser l'image node comme image de base
FROM node:14-alpine AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le package.json et le package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install --force

# Copier tout le code source dans le conteneur
COPY . .

# Construire l'application Angular
RUN npm run build --prod

# Utiliser l'image nginx comme image de base pour servir l'application
FROM nginx:1.19.0-alpine

# Copier les fichiers de construction Angular dans le répertoire de Nginx
COPY --from=build /app/dist/cvparse_front /usr/share/nginx/html

# Exposer le port 80
EXPOSE 80

# Lancer Nginx
CMD ["nginx", "-g", "daemon off;"]
