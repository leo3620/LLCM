FROM node:14.17.0-alpine as build-step
RUN mkdir -p /LLCM-front
WORKDIR /LLCM-front
COPY package.json /LLCM-front
RUN npm install
COPY . /LLCM-front
RUN npm run build --prod
FROM nginx:1.20.1
COPY --from=build-step /LLCM-front/dist/LLCM-front /usr/share/nginx/html
EXPOSE 4200:80