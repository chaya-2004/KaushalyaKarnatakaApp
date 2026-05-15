# Kaushalya Karnataka 🔧
### Local Skilled Worker Showcase App
**Internship Major Project — Android Studio (Kotlin)**

---

## 📱 App Overview
A hyper-local Android application where skilled workers (electricians, plumbers, carpenters, painters, mechanics, etc.) create digital portfolios and customers can find, review, and contact them.

---

## 🛠 Tech Stack
| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM + Repository Pattern |
| Database | Room (offline-first) |
| UI | Material Design 3 |
| Navigation | Navigation Component |
| Image Loading | Glide 4.x |
| Async | Coroutines + Flow |
| Binding | ViewBinding |

---

## 📂 Project Structure
```
com.kaushalya.app
├── data
│   ├── database/      AppDatabase.kt
│   ├── dao/           UserDao, WorkerDao, ServiceDao, ReviewDao, PortfolioDao
│   ├── entities/      UserEntity, WorkerEntity, ServiceEntity, ReviewEntity, PortfolioImageEntity
│   └── repository/    UserRepository, WorkerRepository, ServiceRepository, ReviewRepository, PortfolioRepository
├── ui
│   ├── activities/    SplashActivity, WorkerDetailActivity, AddEditWorkerActivity, AddEditServiceActivity
│   ├── auth/          LoginActivity, SignupActivity
│   ├── fragments/     HomeFragment, SearchFragment, ProfileFragment, FavoritesFragment
│   ├── adapters/      WorkerAdapter, ServiceAdapter, ReviewAdapter, PortfolioAdapter
│   └── viewmodel/     AuthViewModel, WorkerViewModel, ServiceViewModel, ReviewViewModel, PortfolioViewModel, ViewModelFactory
├── utils/             SessionManager, Extensions, Constants
└── MainActivity.kt
```

---

## 🚀 Setup Instructions

### Step 1 – Open in Android Studio
1. Launch **Android Studio Hedgehog** (or later)
2. Click **File → Open** and select this `KaushalyaKarnataka` folder
3. Wait for Gradle sync to complete

### Step 2 – Sync Dependencies
All dependencies are declared in `app/build.gradle`. If Gradle sync fails:
- Go to **File → Invalidate Caches / Restart**
- Or run: `./gradlew build` from terminal

### Step 3 – Run the App
- Connect a physical device **or** start an emulator (API 24+)
- Click the **▶ Run** button

---

## ✅ Features Implemented
- [x] Splash Screen with animation
- [x] Login + Signup with Room DB
- [x] Session management (SharedPreferences)
- [x] Worker profile CRUD
- [x] Service card CRUD
- [x] Portfolio image upload (Activity Result API)
- [x] Reviews & star ratings with average calculation
- [x] Home dashboard with RecyclerView
- [x] Category filter chips
- [x] Search workers by name/category
- [x] Top-rated workers section
- [x] Favourite workers
- [x] Call / WhatsApp / Maps intent buttons
- [x] "Hire Me" simulated booking
- [x] Dark mode support
- [x] Kannada tagline
- [x] Empty state UI
- [x] Sample data pre-populated on first install

---

## 📦 Sample Data
On first launch, 5 sample workers are inserted automatically:
- Raju Electricals (Jayanagar)
- Suresh Plumbing Works (Koramangala)
- Kiran Carpentry (Indiranagar)
- Manjunath Painters (Rajajinagar)
- Venkatesh Auto Service (Whitefield)

---

## 👨‍💻 Resume Description
> Developed a full-featured Android application using Kotlin, MVVM architecture, and Room Database to digitally connect local skilled workers with customers through service listings, portfolio management, ratings, and search functionality.

---

## 📝 Internship Evaluation Checklist
- [x] Clean MVVM architecture
- [x] Room Database with 5 entities
- [x] Repository pattern
- [x] Coroutines + Flow
- [x] Navigation Component
- [x] ViewBinding throughout
- [x] Material Design 3 UI
- [x] RecyclerView + DiffUtil
- [x] Image picker (Activity Result API)
- [x] Input validation
- [x] Offline-first functionality
- [x] Dark mode
- [x] Regional language (Kannada)
