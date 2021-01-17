FROM node:lts-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install -g @quasar/cli
COPY . .
RUN npm install
RUN npm rebuild node-sass
ARG BACKEND_URL
ENV BACKEND_URL=${BACKEND_URL}
RUN env
RUN quasar build

EXPOSE 8083
CMD ["quasar","serve", "dist/spa", "--hostname", "0.0.0.0", "--port", "8083"]
