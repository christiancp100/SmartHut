FROM node:9-alpine as builder

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

ENV PATH /usr/src/app/node_modules/.bin:$PATH

COPY smart-hut/. /usr/src/app

RUN npm install --silent
RUN npm install react-scripts@1.1.1 -g --silent
RUN npm run build


FROM nginx:alpine

# Install envsubst
RUN apk add --no-cache gettext

COPY --from=builder /usr/src/app/build /usr/share/nginx/html

# Delete default config
RUN rm -r /etc/nginx/conf.d && rm /etc/nginx/nginx.conf

# Create folder for PID file
RUN mkdir -p /run/nginx

# Copy nginx config
COPY nginx/nginx.conf /etc/nginx/nginx.template.conf

EXPOSE 80
ENTRYPOINT ["/bin/sh", "-c", "envsubst '${BACKEND_URL}' </etc/nginx/nginx.template.conf >/etc/nginx/nginx.conf && nginx -g 'daemon off;'"]
