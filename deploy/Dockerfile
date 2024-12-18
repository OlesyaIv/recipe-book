# Pull the minimal Ubuntu image
FROM ubuntu:22.04

# Install Nginx
RUN apt-get -y update && apt-get -y install nginx

# Copy the Nginx config
COPY ./volumes/nginx/default.conf /etc/nginx/conf.d/
COPY ./volumes/nginx/nginx.conf /etc/nginx/

# Expose the port for access
EXPOSE 8080/tcp

# Run the Nginx server
ENTRYPOINT ["/usr/sbin/nginx", "-g", "daemon off;"]
