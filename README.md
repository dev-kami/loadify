# 🖼️ Loadify - The Ultimate Image Loading Library for Jetpack Compose

![sample](sample.gif)

**Loadify** is a lightweight yet powerful image loading library designed for Jetpack Compose. It supports modern image types and features while offering an easy-to-use API. Forget about Coil, Picasso, or Glide. With Loadify, you get full control, performance, and custom rendering – all in pure Kotlin and Compose.

---

## 🚀 Features

✅ Load from **URL**, **Drawable**, **Bitmap**, **Uri**  
✅ Full support for **GIFs** (AnimatedImageDrawable)  
✅ Render **SVGs**, **WebP**, and **Vector Drawables**  
✅ **Lottie** animation support from raw resources & URL  
✅ **Caching**: memory + disk  
✅ **Image Transformations**: Circle crop, blur  
✅ Easy integration with **Compose UI**  
✅ Placeholder, error, loading, success callbacks  
✅ No Coil or third-party dependency required

---

## 🧩 Supported Formats

| Format | Source | Supported |
|--------|--------|-----------|
| PNG, JPG, BMP | URL, Drawable | ✅ |
| WebP | URL, Drawable | ✅ |
| SVG | URL only | ✅ |
| GIF | URL, Drawable | ✅ |
| Lottie JSON | RawRes, URL | ✅ |
| Bitmap | Direct instance | ✅ |
| Uri | Content provider | ✅ |

---

## 📦 Installation

Coming soon to Maven Central...

For now, clone the repo and include as a module in your project:

```groovy
// settings.gradle
include(":loadify")

// app/build.gradle
implementation(project(":loadify"))
```

---

## 🧪 Usage

```kotlin
Loadify(
    data = "https://example.com/image.jpg",
    modifier = Modifier.size(100.dp),
    placeholder = { CircularProgressIndicator() },
    onLoadSuccess = { println("Loaded!") },
    onLoadError = { println("Failed") }
)
```

### 🎞️ Load GIF

```kotlin
Loadify(data = R.drawable.animated_gif)
```

### 🎨 Vector Drawable

```kotlin
Loadify(data = R.drawable.ic_vector)
```

### 💡 Lottie Animation (RawRes or URL)

```kotlin
Loadify(data = R.raw.loading_animation)

Loadify(data = "https://example.com/lottie.json")
```

---

## 🛠️ Customization

```kotlin
Loadify(
    data = imageUrl,
    circleCrop = true,
    blurRadius = 12,
    headers = mapOf("Authorization" to "Bearer xyz"),
    contentScale = ContentScale.Fit
)
```

---

## 🧠 Why Loadify?

- You’re building with Jetpack Compose.
- You need **GIF + SVG + Lottie** support without hacks.
- You want to avoid 3rd-party bloat like Coil.
- You want full control over rendering.

---

## 📸 Sample Gallery

- ✔️ Vector Drawable
- ✔️ WebP
- ✔️ Lottie URL
- ✔️ Lottie Raw
- ✔️ GIF URL
- ✔️ URI from Picker
- ✔️ Bitmap
- ✔️ PNG/JPG

---

## 📂 Sample App

Clone and run the sample app in `sample/` module to try everything.

---

## 📜 License

```
MIT License
Copyright (c) 2025
```

---