# 🐠 Tienda Virtual de Acuarismo - App Android

Aplicación móvil desarrollada en Android Studio que permite a los usuarios comprar productos especializados en acuarismo (peceras, filtros, decoración, accesorios y más). Usa Firebase como backend para autenticación, base de datos, almacenamiento y órdenes en tiempo real.

---

## 📱 Capturas

<img src="https://via.placeholder.com/300x600?text=Inicio" width="200" />
<img src="https://via.placeholder.com/300x600?text=Detalle+Producto" width="200" />
<img src="https://via.placeholder.com/300x600?text=Carrito" width="200" />
<img src="https://via.placeholder.com/300x600?text=Órdenes" width="200" />

---

## 🚀 Características principales

- ✅ Registro y login de usuarios (Firebase Authentication)
- 🛒 Catálogo de productos con imágenes, categorías y stock
- ❤️ Gestión de favoritos
- 🛍 Carrito de compras con control de stock en tiempo real
- 📦 Historial de órdenes por usuario
- 👤 Perfil de usuario con edición de datos
- 📉 Descuentos de stock en compras confirmadas
- 🔍 Navegación fluida entre fragments
- 🧾 Sección de información sobre el proyecto

---

## 🔧 Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Backend:** Firebase
  - Authentication
  - Firestore
  - Realtime Database
  - Firebase Storage
- **Otras librerías:**
  - Glide (carga de imágenes)
  - ViewBinding
  - Navigation Component + Safe Args

---

## 🧪 Estructura del proyecto

<pre> ```text app/ ├── java/pe/edu/cibertec/proyecto_efrst/ │ ├── activities/ │ ├── adapters/ │ ├── firebase/ │ ├── fragments/ │ │ └── home/ │ ├── models/ │ ├── utils/ │ └── viewholders/ ├── res/ │ ├── drawable/ │ ├── font/ │ ├── layout/ │ ├── menu/ │ ├── mipmap/ │ ├── navigation/ │ └── values/ └── AndroidManifest.xml ``` </pre>
