# PetStore - Pet Shop Management Application

Spring Boot tabanlı bir pet shop yönetim uygulaması. Docker ve Jenkins entegrasyonu ile CI/CD desteği sağlar.

## 🚀 Teknolojiler

- **Framework**: Spring Boot 2.7.14
- **Database**: PostgreSQL 15
- **Java**: JDK 17
- **Security**: Spring Security + JWT
- **Containerization**: Docker & Docker Compose
- **CI/CD**: Jenkins
- **Build Tool**: Maven 3.9+

## 📋 Özellikler

- Kullanıcı kayıt ve giriş işlemleri (JWT tabanlı)
- Rol bazlı yetkilendirme (Admin, Store Owner, Customer)
- Pet yönetimi (CRUD işlemleri)
- Docker containerization
- Jenkins CI/CD pipeline
- Health check ve monitoring

## 🛠️ Kurulum ve Çalıştırma

### Lokal Geliştirme (Docker Compose İle)

1. **Repository'yi klonlayın:**
```bash
git clone <repo-url>
cd PetStore
```

2. **Docker Compose ile çalıştırın:**
```bash
docker-compose up -d --build
```

3. **Uygulamaya erişin:**
- API: http://localhost:8080
- Health Check: http://localhost:8080/actuator/health
- Database: localhost:5432

### Manuel Lokal Geliştirme

1. **PostgreSQL'i başlatın:**
```bash
docker run -d --name postgres \
  -e POSTGRES_DB=petshopapp \
  -e POSTGRES_USER=techpront \
  -e POSTGRES_PASSWORD=125322 \
  -p 5432:5432 postgres:15-alpine
```

2. **Uygulamayı çalıştırın:**
```bash
./mvnw spring-boot:run
```

## 🐳 Docker Komutları

### İmaj Oluşturma
```bash
docker build -t petstore:latest .
```

### Container Çalıştırma
```bash
docker run -d -p 8080:8080 --name petstore-app petstore:latest
```

### Container Yönetimi
```bash
# Container'ları başlat
docker-compose up -d

# Container'ları durdur
docker-compose down

# Logları görüntüle
docker-compose logs -f app

# Container durumunu kontrol et
docker-compose ps
```

### 🎯 Docker'ın Kazandırdıkları

#### 🌍 Works on My Machine Sorunu Çözüldü
- **Önceki Durum**: Her developer farklı ortam
  - Developer A: PostgreSQL 12, Java 11
  - Developer B: PostgreSQL 14, Java 17
  - Production: PostgreSQL 15, Java 17
  - Sonuç: "Benim makinede çalışıyor" problemi! 😫
  
- **Şimdi**: Herkes aynı ortam
  - Herkes aynı Docker image kullanır
  - Development = Test = Production
  - Sonuç: Tutarlı çalışan sistem! ✅

#### ⚡ Hızlı Başlangıç
```bash
# Önceki: 30-60 dakika setup
# ✓ JDK kurulum
# ✓ Maven kurulum
# ✓ PostgreSQL kurulum
# ✓ Environment variable ayarları
# ✓ Database yapılandırması

# Şimdi: 5 dakika setup
docker-compose up -d  # Hepsi hazır!
```

#### 🔄 Kolay Rollback
- Eski version'a dönmek: Dakikalar içinde
- Bozuk deployment: Hemen önceki çalışan version
- Zero-downtime deployment

#### 📦 Portability
- Windows'ta çalışır
- Mac'te çalışır
- Linux'ta çalışır
- Cloud'ta çalışır
- **Aynı image, her yerde!**

#### 💰 Maliyet Tasarrufu
- Önceki: Sunucuda tüm dependency'ler kurulu
- Şimdi: Sadece Docker Engine gerekli
- Resource izolasyonu: Daha az kaynak kullanımı

#### 🚀 Hızlı Deployment
```bash
# Yeni developer onboarding
git clone <repo>
cd PetStore
docker-compose up -d  # 5 dakika sonra çalışıyor!

# Yeni sunucu setup
docker-compose up -d  # İşte bu kadar!
```

#### 🔒 İzolasyon ve Güvenlik
- Her servis ayrı container
- Bir servis crash olsa diğerleri çalışır
- Network izolasyonu
- Resource limitleri

#### 📈 Scalability
```bash
# Production'a geçerken
docker-compose up -d --scale app=3  # 3 instance!
docker-compose up -d --scale app=10 # 10 instance!
```

#### 🧹 Temiz Ortam
- Kurulum kalıntıları yok
- Bağımlılık çakışmaları yok
- Her yeni container temiz başlar
```bash
docker-compose down -v  # Tamamen temiz!
```

#### 📝 Infrastructure as Code
- `docker-compose.yml` = Tüm infrastrüktür tanımı
- Version kontrolünde
- Takım üyeleri arasında paylaşılabilir

#### 🎯 Development-Production Parity
- Development'ta ne çalışıyorsa production'da da o
- Local test = Production test
- Hatayı erken yakalama

### 📊 Docker vs Manuel Setup Karşılaştırması

| Özellik | Manuel Setup | Docker |
|---------|--------------|--------|
| ⏱️ Initial Setup | 30-60 dakika | 5 dakika |
| 🔄 Rollback | Zor (manuel) | Kolay (1 komut) |
| 🌍 Portability | Sınırlı | Her platform |
| 💰 Kaynak Kullanımı | Yüksek | Optimize |
| 🚀 Deployment | Yavaş | Hızlı |
| 🔒 İzolasyon | Yok | Güçlü |
| 📦 Bakım | Zor | Kolay |
| 👥 Onboarding | Uzun | Kısa |

### 💡 Gerçek Hayat Örnekleri

**Senaryo 1: Yeni Developer**
- Önceki: "PostgreSQL kur, Java kur, Maven kur..." (1 saat)
- Şimdi: `docker-compose up` (5 dakika) 🎉

**Senaryo 2: Production'a Deploy**
- Önceki: Sunucuda setup, config, dependency hell (2-3 saat)
- Şimdi: `docker-compose up -d` (5 dakika) 🚀

**Senaryo 3: Scaling**
- Önceki: Sunucu kurulumu, config, network ayarları (1 gün)
- Şimdi: `--scale app=10` (1 dakika) ⚡

**Senaryo 4: Rollback**
- Önceki: Manuel geri alma, config değişikliği (30 dakika)
- Şimdi: Eski image'i çalıştır (1 dakika) ↩️

## 🔄 Jenkins CI/CD Pipeline

### Pipeline Adımları

1. **Checkout**: Repository'den kodu al
2. **Build**: Maven ile derleme
3. **Test**: Unit testlerini çalıştır
4. **Package**: JAR dosyası oluştur
5. **Docker Build**: Docker imajı oluştur
6. **Docker Push**: Registry'ye gönder (main branch için)
7. **Deploy**: Docker Compose ile deploy et
8. **Health Check**: Uygulama sağlığını kontrol et

### Jenkins Kurulumu

**Docker Compose ile tüm servisleri başlatın:**
```bash
docker-compose up -d
```

Bu komut şu servisleri başlatır:
- ✅ PostgreSQL database
- ✅ PetStore application
- ✅ Jenkins CI/CD

**Jenkins Erişimi:**
- URL: http://localhost:8081
- İlk kurulum için initial admin password:
```bash
docker exec petstore-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

**Gerekli Plugin'leri yükleyin:**
Jenkins Dashboard → Manage Jenkins → Manage Plugins → Available:
- Docker Pipeline
- Docker
- Git
- Maven Integration
- Pipeline
- JUnit

**Pipeline'ı yapılandırın:**
- Jenkins Dashboard → New Item → Pipeline
- Name: `petstore-pipeline`
- Definition: Pipeline script from SCM
- SCM: Git
- Repository URL: Bu projenin Git URL'i
- Script Path: `Jenkinsfile`

### Jenkinsfile Özelleştirme

`Jenkinsfile` içindeki şu değerleri kendi ortamınıza göre güncelleyin:
- `REGISTRY`: Docker registry URL'iniz
- `DOCKER_CREDENTIALS`: Jenkins credentials ID'niz
- Email adresleri

### 🎯 Jenkins'in Kazandırdıkları

#### ⚡ Otomatizasyon
- **Önceki Durum**: Her deploy için 5-10 dakika manuel işlem
  - Code değişikliği
  - Maven build
  - Docker build
  - Container stop/start
  - Test etme
  
- **Şimdi**: Tek git push ile otomatik deploy!
  ```bash
  git push origin main  # Hepsi otomatik!
  ```

#### ⏱️ Zaman Tasarrufu
- Haftada 2-3 saat kazanım
- Developer daha fazla kod yazabilir
- Daha az operasyonel iş

#### 🎯 Hata Azaltma
- Tutarlı, her seferinde aynı işlem
- İnsan hatası riski minimize
- Build adımları hiç unutulmaz

#### 👥 Ekip Verimliliği
- DevOps bilgisi gerektirmez
- Junior developer bile deploy edebilir
- Standart süreç herkes için geçerli

#### 📢 Hızlı Feedback
- Her build'de bildirim
- Başarı/hata anında görülür
- Build süresi takibi

#### 📝 Audit Trail
- Her deploy loglanır
- Hangi commit, ne zaman, kim
- Tam versiyon kontrolü

#### ↩️ Kolay Rollback
- Sorun varsa tek tıkla geri dön
- Önceki başarılı version'a dönüş
- Sıfır downtime

#### 📈 Scalability
- 1 sunucu için çalışıyor
- 10 sunucu için de aynı şekilde çalışır
- Geleceğe hazır yapı

### 📊 Toplam Kazanç

| Metrik | Değer |
|--------|-------|
| ⏱️ Zaman Tasarrufu | Haftada 2-3 saat |
| 💰 Maliyet Azaltma | Developer zamanı = Para |
| 😌 Stres Azaltma | Daha az ops stresi |
| 🎯 Kalite Artışı | Tutarlı deploy |
| ⚡ Deploy Hızı | Dakikalar içinde |
| 🔒 Güvenlik | Her deploy güvenli |

## 🗄️ Database

### Varsayılan Kullanıcılar

Uygulama ilk çalıştığında otomatik oluşturulur:

**Admin:**
- Username: Admin
- Password: 12345678
- Email: admin@petstore.app

**Store Owner:**
- Username: StoreOwner
- Password: 12345678
- Email: storeowner@petstore.app

### Database Bağlantı Bilgileri

- Host: localhost (local) / postgres (Docker)
- Port: 5432
- Database: petshopapp
- Username: techpront
- Password: 125322

## 📡 API Endpoints

### Authentication
- `POST /auth/register` - Kullanıcı kaydı
- `POST /auth/login` - Kullanıcı girişi

### Users
- `GET /user/**` - Kullanıcı işlemleri

### Pets
- `GET /pets` - Tüm pet'leri listele
- `GET /pets/{id}` - Pet detayı
- `POST /pets` - Yeni pet ekle
- `PUT /pets/{id}` - Pet güncelle
- `DELETE /pets/{id}` - Pet sil

### Monitoring
- `GET /actuator/health` - Health check
- `GET /actuator/info` - Uygulama bilgileri

## 🔐 Security

Uygulama JWT (JSON Web Token) tabanlı authentication kullanır. API çağrıları için token gereklidir (auth endpoint'leri hariç).

## 🧪 Test

```bash
# Tüm testleri çalıştır
./mvnw test

# Test raporlarını görüntüle
open target/surefire-reports/index.html
```

## 📝 Log Yönetimi

Loglar `log/pet_app.log` dosyasına yazılır (local) veya `/app/logs/` dizinine (Docker).

```bash
# Logları görüntüle
tail -f log/pet_app.log

# Docker içinde logları görüntüle
docker-compose logs -f app
```

## 🔍 Troubleshooting

### Port Çakışması
Eğer 8080 veya 5432 portları kullanımdaysa `docker-compose.yml` içindeki portları değiştirin.

### Database Bağlantı Hatası
- PostgreSQL container'ının çalıştığından emin olun
- Docker network ayarlarını kontrol edin

### Actuator Health Check Hatası
- `management.endpoints.web.exposure.include=health` ayarını kontrol edin
- `/actuator/**` endpoint'lerinin security'de permitAll olduğunu kontrol edin

## 👥 Roller

- **ADMIN**: Tüm yetkilere sahip
- **STORE_OWNER**: Pet yönetim yetkisi
- **CUSTOMER**: Limitli erişim

## 📦 Build

```bash
# Clean build
./mvnw clean package

# Skip tests
./mvnw clean package -DskipTests

# Docker ile build
docker build -t petstore:latest .
```

## 🌍 Environment Variables

### Docker Environment

`docker-compose.yml` içinde şu environment variable'lar tanımlıdır:
- `SPRING_PROFILES_ACTIVE`: docker
- `SPRING_DATASOURCE_URL`: jdbc:postgresql://postgres:5432/petshopapp
- `SPRING_DATASOURCE_USERNAME`: techpront
- `SPRING_DATASOURCE_PASSWORD`: 125322

### Production için

Production ortamında bu değerleri değiştirin:
- Güçlü bir JWT secret kullanın
- Database şifrelerini güçlendirin
- HTTPS kullanın

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

## 🤝 Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit edin (`git commit -m 'Add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request açın

---

**Not**: Bu proje geliştirme amaçlıdır. Production kullanımında güvenlik ve performans optimizasyonları yapılmalıdır.

