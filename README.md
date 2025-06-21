# 🐠 Tienda Virtual de Acuarismo - App Android

Aplicación móvil desarrollada en Android Studio que permite a los usuarios comprar productos especializados en acuarismo (peceras, filtros, decoración, accesorios y más). Utiliza Firebase como backend para autenticación, base de datos en tiempo real, almacenamiento de imágenes y gestión de usuarios.

---

## 📱 Capturas

<img src="https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/Screenshot%2FScreenshot_inicio_sesion.png?alt=media&token=fad2993e-f360-4622-bcde-291cdda4af55" width="200" />
<img src="https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/Screenshot%2FScreenshot_registro.png?alt=media&token=04f848af-f756-44ee-a0eb-6a9978ab352a" width="200" />
<img src="https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/Screenshot%2FScreenshot_home.png?alt=media&token=3cc1ee43-6139-4081-a727-326309e9dfa8" width="200" />
<img src="https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/Screenshot%2FScreenshot_detalle_producto.png?alt=media&token=c5877785-c41a-4187-9947-69228b19d8fe" width="200" />
<img src="https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/Screenshot%2FScreenshot_carrito.png?alt=media&token=6eec713a-7c4e-49f9-a9e1-0bd22fca38d9" width="200" />
<img src="https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/Screenshot%2FScreenshot_perfil.png?alt=media&token=af863cfe-2746-4f16-93ea-05646fb8cce1" width="200" />

---

## 🚀 Características principales

- ✅ Registro y login de usuarios (Firebase Authentication)
- 🛒 Catálogo de productos con imágenes, categorías y stock
- ❤️ Gestión de favoritos
- 🛍 Carrito de compras con control de stock en tiempo real
- 📦 Historial de órdenes por usuario
- 👤 Perfil de usuario con edición de datos
- 🧾 Sección de información sobre el proyecto
- 🔍 Navegación fluida entre fragments
- 🖼 Carrusel de imágenes con ViewPager2

---

## 🔧 Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Backend:** Firebase
  - Authentication
  - Firestore
  - Realtime Database
  - Firebase Storage
- **Librerías adicionales:**
  - Glide (carga de imágenes)
  - ViewBinding
  - Navigation Component + Safe Args

---

## 🧪 Estructura del proyecto

```text
app/
├── java/pe/edu/cibertec/proyecto_efrst/
│   ├── activities/
│   ├── adapters/
│   ├── firebase/
│   ├── fragments/
│   │   └── home/
│   ├── models/
│   ├── utils/
│   └── viewholders/
├── res/
│   ├── drawable/
│   ├── font/
│   ├── layout/
│   ├── menu/
│   ├── mipmap/
│   ├── navigation/
│   └── values/
└── AndroidManifest.xml

