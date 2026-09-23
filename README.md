# FinalProjectJayjay

Automation test framework gabungan **API** dan **Web UI**, dibangun pakai **Cucumber (BDD/Gherkin)** di atas Java + Gradle. Repo ini jadi portofolio hasil kombinasi materi Git & GitHub, Java, Gradle, RestAssured, Selenium, Cucumber, dan GitHub Actions.

## Daftar Isi
1. [Tech Stack & Library](#tech-stack--library)
2. [Target Under Test](#target-under-test)
3. [Struktur Project](#struktur-project)
4. [Ringkasan Coverage Test](#ringkasan-coverage-test)
5. [Detail Test Case](#detail-test-case)
6. [Cara Menjalankan Test](#cara-menjalankan-test)
7. [Report](#report)
8. [GitHub Actions (CI)](#github-actions-ci)
9. [Temuan & Known Issues](#temuan--known-issues)
10. [Catatan Tambahan](#catatan-tambahan)

---

## Tech Stack & Library

| Kategori | Tools / Library | Versi |
|---|---|---|
| Bahasa | Java | 21 (CI) |
| Build Tool | Gradle (Gradle Wrapper) | 9.6.0 |
| BDD Framework | Cucumber (`cucumber-java`, `cucumber-junit`) | 7.34.8 |
| Dependency Injection antar Step Definition | `cucumber-picocontainer` | 7.34.8 |
| Test Runner | JUnit4 (classic `@RunWith(Cucumber.class)`) | via `cucumber-junit` |
| API Testing | RestAssured | 6.0.1 |
| Web UI Testing | Selenium WebDriver | 4.48.0 |
| Driver Management | WebDriverManager | 6.3.3 |
| JSON Handling | Jackson Databind, org.json | 2.22.2 / 20260522 |
| CI/CD | GitHub Actions | - |

## Target Under Test

| Modul | Target | Autentikasi |
|---|---|---|
| **Web UI** | [demoblaze.com](https://www.demoblaze.com/) | Login/Sign Up via modal (native JS alert) |
| **API** | [dummyapi.io](https://dummyapi.io/docs) (`https://dummyapi.io/data/v1`) | Header `app-id` (statis, tanpa login/token) |

## Struktur Project

Sesuai requirement, kode Java, Step Definition, dan Feature file untuk **Web** dan **API** dipisah total ke package/folder masing-masing:

```
src/test/
├── java/
│   ├── DummyAPI/                     ← Modul API
│   │   ├── configs/                  → ApiConfig.java (base URL, app-id)
│   │   ├── context/                  → TestContext.java (shared state antar step def)
│   │   ├── models/                   → UserPayload.java (request body builder)
│   │   ├── requests/                 → UserRequest, TagRequest, SecurityRequest
│   │   ├── runners/                  → apiTestRunner.java (tags="@api")
│   │   └── stepdef/                  → UserStepDef, TagStepDef, SecurityStepDef, UserHook
│   │
│   └── DemoBlaze/                    ← Modul Web UI
│       ├── base/                     → DriverFactory.java (setup ChromeDriver, headless-aware)
│       ├── context/                  → TestContext.java (shared state + lazy Page Object init)
│       ├── pages/                    → HomePage, ProductDetailPage, CartPage, OrderPage, NavigationHeader
│       ├── runner/                   → WebTestRunner.java (tags="@web")
│       └── stepdef/                  → AuthStepDef, ProductStepDef, CartStepDef, CheckoutStepDef, NavbarStepDef, CucumberHooks
│
└── resources/
    ├── api/                          → user.feature, tag.feature, security.feature
    └── web/                          → auth.feature, product.feature, cart.feature, checkout.feature, navbar.feature
```

**Arsitektur kedua modul konsisten** — sama-sama pakai `TestContext` dengan dependency injection (`cucumber-picocontainer`) sebagai pusat state per scenario dan lazy initializer untuk Page Object / request helper, sehingga step definition class tidak perlu saling memanggil method satu sama lain secara langsung.

## Ringkasan Coverage Test

| Feature File | Modul | Jumlah Scenario | Total Eksekusi (termasuk Scenario Outline) |
|---|---|---|---|
| `user.feature` | API | 18 (2 Outline) | 26 |
| `tag.feature` | API | 2 | 2 |
| `security.feature` | API | 3 (2 Outline) | 5 |
| `auth.feature` | Web | 10 (2 Outline) | 13 |
| `product.feature` | Web | 6 (1 Outline) | 8 |
| `cart.feature` | Web | 7 | 7 |
| `checkout.feature` | Web | 4 (1 Outline) | 6 |
| `navbar.feature` | Web | 5 | 5 |
| **Total** | | **55 scenario** | **~72 eksekusi test** |

Setiap scenario mencakup kombinasi **positive**, **negative**, dan **exploratory/bug-documentation** case (misalnya validasi lemah pada endpoint update DummyAPI, atau perilaku checkout DemoBlaze pada input tidak valid).

## Detail Test Case

### Web — `auth.feature` (Authentication and User Management)
| Case | Tipe | Deskripsi |
|---|---|---|
| Sign up dengan username dinamis unik | Positive | Sign up sukses, alert "Sign up successful." muncul |
| Sign up dengan username yang sudah terdaftar | Negative | Alert "This user already exist." |
| Sign up dengan kredensial kosong (3 kombinasi) | Negative | Username kosong / password kosong / keduanya kosong → alert "Please fill out Username and Password." |
| Sign up dengan password 1 karakter | Exploratory / Known Weakness | Sign up tetap diterima meski password sangat lemah (lihat Known Issues) |
| Tutup modal sign up tanpa perubahan | Positive | Modal tertutup, tidak ada perubahan state |
| Login dengan kredensial valid | Positive | Navbar menampilkan "Welcome test", tombol logout muncul |
| Login dengan password salah | Negative | Alert "Wrong password." |
| Login dengan username tidak terdaftar | Negative | Alert "User does not exist." |
| Login dengan kredensial kosong (3 kombinasi) | Negative | Alert "Please fill out Username and Password." |
| Logout dari sesi yang sudah login | Positive | Navbar kembali menampilkan link "Log in" dan "Sign up" |

### Web — `product.feature` (Catalog Navigation and Product Detail Page)
| Case | Tipe | Deskripsi |
|---|---|---|
| Filter produk per kategori (Phones/Laptops/Monitors) | Positive | Produk yang tampil sesuai kategori yang dipilih |
| Navigasi pagination "Next" | Positive | Produk halaman 2 ter-load |
| Klik nama produk ke halaman detail | Positive | Detail page menampilkan judul & harga yang sesuai |
| Klik thumbnail gambar produk | Bug Documentation | Mendokumentasikan bahwa thumbnail gambar tidak ter-link ke detail page |
| Detail produk lengkap | Positive | Nama, harga, dan deskripsi produk tampil |
| Tambah produk ke cart dari detail page | Positive | Alert "Product added" muncul |

### Web — `cart.feature` (Cart Page)
| Case | Tipe | Deskripsi |
|---|---|---|
| Item di cart sesuai produk yang dipilih | Positive | Nama & harga di cart cocok dengan detail page |
| Item duplikat tampil sebagai baris terpisah | Positive | Menambahkan produk yang sama 2x menghasilkan 2 baris |
| Total harga sesuai penjumlahan item | Positive | Total price = sum of semua item individual |
| Hapus 1 item & rekalkulasi total | Positive | Item terhapus hilang dari list, total ter-update sesuai sisa item |
| Hapus semua item dari cart | Positive | Cart menjadi kosong sepenuhnya |
| Cart tetap ada setelah refresh halaman | Exploratory | Verifikasi persistence cart lintas page refresh |
| Modal checkout pada cart kosong | Bug Documentation | Mendokumentasikan apakah tombol Place Order bisa diklik meski cart kosong |

### Web — `checkout.feature` (Checkout Order)
| Case | Tipe | Deskripsi |
|---|---|---|
| Checkout sukses dengan 1 item | Positive | Konfirmasi pembelian sesuai Name, Card, Total Amount; cart kosong setelahnya |
| Checkout sukses dengan multi item | Positive | Sama seperti di atas, dengan 2 produk berbeda di cart |
| Submit form checkout tanpa isi field | Negative | Alert "Please fill out Name and Creditcard." |
| Input pembayaran tidak valid (3 kombinasi: no. kartu invalid, bulan 99, tahun 2010) | Bug Documentation | Mendokumentasikan apakah sistem tetap menerima input tidak valid atau menolaknya |

### Web — `navbar.feature` (Navbar Links and Miscellaneous Modals)
| Case | Tipe | Deskripsi |
|---|---|---|
| Kirim contact form dengan field kosong | Bug Documentation | Mendokumentasikan apakah sistem tetap menerima form kosong |
| Modal "About Us" & video player | Positive | Modal dan video player tampil saat diklik |
| Klik logo/Home dari cart page | Positive | Redirect kembali ke homepage product grid |
| Navigasi Home dari detail page | Positive | Redirect kembali ke homepage product grid |
| Kirim contact form dengan data valid | Positive | Alert "Thanks for the message!!" muncul |

### API — `security.feature` (Global Authentication Security API)
| Case | Tipe | Deskripsi |
|---|---|---|
| Akses endpoint tanpa header `app-id` (2 variasi endpoint) | Negative | Request ditolak karena header wajib tidak ada |
| Akses endpoint dengan `app-id` tidak valid (2 variasi endpoint) | Negative | Request ditolak karena app-id salah |
| Create user tanpa header `app-id` | Negative | Request pembuatan user ditolak |

### API — `tag.feature` (Tag API DummyAPI)
| Case | Tipe | Deskripsi |
|---|---|---|
| Get list of tags | Positive | Response berisi daftar tag yang valid |
| Get tags dengan endpoint path tidak valid | Negative | Request gagal sesuai ekspektasi |

### API — `user.feature` (User API DummyAPI)
| Case | Tipe | Deskripsi |
|---|---|---|
| Get list of users dengan pagination | Positive | Response ter paginasi dengan benar |
| Get user detail by valid ID | Positive | Data user sesuai ID yang diminta |
| Get user dengan ID tidak ada | Negative | Response error sesuai ekspektasi |
| Get user dengan format ID tidak valid | Negative | Response error sesuai ekspektasi |
| Get detail user yang baru dibuat | Positive | Verifikasi data user hasil create bisa di-fetch kembali |
| Create user dengan field mandatory valid | Positive | User berhasil dibuat |
| Create user dengan email yang sudah ada | Negative | Request ditolak karena duplikat |
| Create user dengan field mandatory hilang (3 kombinasi) | Negative | Request ditolak, validasi field wajib |
| Create user dengan format email tidak valid (5 kombinasi) | Negative | Request ditolak, validasi format email |
| Update satu field | Positive | Field ter-update dengan benar |
| Update beberapa field sekaligus | Positive | Semua field yang dikirim ter-update |
| Update nested object `location` | Positive | Struktur nested ter-update dengan benar |
| Update field `email` yang terlarang | Bug Documentation | Mendokumentasikan bahwa perubahan email diabaikan sistem |
| Update user dengan ID tidak ada | Negative | Request ditolak sesuai ekspektasi |
| Update user dengan format ID tidak valid | Negative | Request ditolak sesuai ekspektasi |
| Update `firstName` di bawah minimum length dokumentasi | Bug Documentation | Mendokumentasikan bahwa validasi minimum length tidak diterapkan |
| Delete user yang ada & verifikasi terhapus | Positive | User berhasil dihapus dan tidak lagi bisa diakses |
| Delete user yang tidak ada | Negative | Request ditolak sesuai ekspektasi |
| Delete user dengan format ID tidak valid | Negative | Request ditolak sesuai ekspektasi |
| Delete user yang sama dua kali | Negative | Percobaan delete kedua ditolak karena user sudah tidak ada |

## Cara Menjalankan Test

### Prasyarat
- JDK terpasang (disarankan JDK 21, sesuai environment CI)
- Google Chrome terpasang
- Koneksi internet (test mengakses situs/API publik secara langsung)

### Jalankan semua test (API + Web)
```bash
./gradlew test
```

### Jalankan HANYA test API (tag `@api`)
```bash
./gradlew apiTest
```

### Jalankan HANYA test Web UI (tag `@web`)
```bash
./gradlew webTest
```

## Report

Setiap run menghasilkan report dalam 2 format sesuai requirement:

| Modul | HTML | JSON |
|---|---|---|
| API | `build/reports/cucumber-api.html` | `build/reports/cucumber-api.json` |
| Web | `build/reports/cucumber-web.html` | `build/reports/cucumber-web.json` |

Report standar Gradle/JUnit (pass/fail per scenario + stack trace) juga tersedia di `build/reports/tests/apiTest/` dan `build/reports/tests/webTest/`.

## GitHub Actions (CI)

Workflow (`.github/workflows/main.yml`) berjalan otomatis pada:
- **Manual trigger** (`workflow_dispatch`)
- **Setiap Pull Request** ke branch `main`
- Push ke branch `main`

Terdiri dari **2 job independen**, masing-masing menjalankan task Gradle yang sesuai lalu meng-upload report-nya sebagai artifact:

| Job | Task yang dijalankan | Artifact |
|---|---|---|
| `api-tests` | `./gradlew apiTest` | `cucumber-api-reports` |
| `web-tests` | `./gradlew webTest` | `cucumber-web-reports` |

Browser untuk modul Web otomatis jalan **headless** di CI (dideteksi lewat environment variable `CI` yang di set otomatis oleh GitHub Actions), tanpa perlu konfigurasi tambahan di sisi user.

## Temuan & Known Issues

**Web (DemoBlaze):**
- Form Checkout dan Contact menerima input kosong/tidak valid tanpa penolakan tegas (nomor kartu, bulan, dan tahun kedaluwarsa sembarangan tetap diproses).
- Klik thumbnail gambar produk tidak melakukan navigasi ke halaman detail (hanya judul produk yang merupakan link).
- Sign Up tidak menerapkan syarat kompleksitas password (password 1 karakter tetap diterima).

## Catatan Tambahan

- **App-id DummyAPI** yang dipakai (`63a804408eb0cb069b57e43a`) adalah app-id contoh dari instruksi tugas dan bersifat shared/publik.
- **Kredensial login DemoBlaze** (`test`/`test`) yang dipakai pada beberapa scenario positive login memakai akun umum di sandbox publik DemoBlaze; sebagian scenario lain (Sign Up) sengaja men generate username unik secara dinamis untuk menghindari konflik data.
- **Flakiness eksternal DemoBlaze**: scenario yang bergantung pada native `alert()` setelah AJAX call (Sign Up, Login, Add to Cart) pernah terobservasi gagal massal dengan `TimeoutException` di suatu run, lalu PASSED 100% di run berikutnya tanpa perubahan kode apapun — mengindikasikan keterlambatan/ketidakstabilan backend publik DemoBlaze.