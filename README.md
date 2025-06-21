# 🐠 Tienda Virtual de Acuarismo - App Android

Aplicación móvil desarrollada en Android Studio que permite a los usuarios comprar productos especializados en acuarismo (peceras, filtros, decoración, accesorios y más). Utiliza Firebase como backend para autenticación, base de datos en tiempo real, almacenamiento de imágenes y gestión de usuarios.

---

## 📱 Capturas

> *(Agrega aquí tus imágenes con Markdown si deseas)*

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

