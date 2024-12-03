FROM nginx:alpine

# Set the working directory in the container
WORKDIR /usr/share/nginx/html

COPY nginx/nginx.conf /etc/nginx/nginx.conf

COPY build-react/ /usr/share/nginx/html/

EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]