# Android-LoginGoogle
Autentificacion Login con Google


paso 1 : instalar o actualizar el google play services

paso 2 : Ir a "console.firebase.google.com" y crear un nuevo proyecto que habilite los servicios de autentificacion de google.
Nombre de proyecto -> añade firebase a android -> aca pedira el nombre del paquete y sha1

Luego descargaremos el google-services.json, en android studio pasaremos a la pestaña project y copiaremos el archivo descargado a la " app "

Luego añadiremos la dependencia al build.grafle del proyecto y compilaremos un plugin en el build.gradle del modulo

Paso 3 : Ahora compilaremos 2 dependencias
1) compile 'com.google.android.gms:play-services-auth:9.8.0'  --> este es para el login de google
2) compile 'com.github.bumptech.glide:glide:3.7.0' --> para descargar la imagen del gmail del usuario

Paso 4 : Programamos la actividad Login (los pasos estaran en el proyecto documentado)
