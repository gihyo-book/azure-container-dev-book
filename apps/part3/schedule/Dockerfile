# ---- Build ----
FROM node:16.15-alpine as builder

WORKDIR /usr/src/app
COPY --chown=node:node package.json .
RUN npm install --only=production 
 
# ---- Release ----
FROM gcr.io/distroless/nodejs:16 as release

COPY --chown=nonroot:nonroot --from=builder /usr/src/app/node_modules ./node_modules
COPY --chown=nonroot:nonroot . .

USER nonroot
EXPOSE 8083
CMD [ "server.js" ]
