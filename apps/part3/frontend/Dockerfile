# ---- Build ----
FROM node:16.15-alpine as builder

USER node
WORKDIR /usr/src/app
COPY --chown=node:node package.json ./
RUN npm install --legacy-peer-deps

COPY --chown=node:node . .
RUN npm run build

# ---- Release ----
FROM nginxinc/nginx-unprivileged:1.22-alpine

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /usr/src/app/build /frontend/build

RUN chown -R nginx:0 /var/cache/nginx \
    && chmod -R g+w /var/cache/nginx

USER nginx
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
