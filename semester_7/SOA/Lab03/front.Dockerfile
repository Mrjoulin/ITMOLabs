FROM node:16-alpine AS BUILD

WORKDIR /app

COPY front/package*.json ./

RUN npm install

COPY front/public/ ./public/
COPY front/src/ ./src/

RUN npm run build
# RUN npm install -g serve

# Start the serve command to serve the production build
# CMD ["serve", "-s", "build"]

FROM nginx:alpine AS RUN

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/mime.types /etc/nginx/mime.types

EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]
