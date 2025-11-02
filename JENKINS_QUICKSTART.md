# 🚀 Jenkins Hızlı Başlangıç Rehberi

## 📝 İlk Adımlar

### 1. Jenkins'e Giriş Yapın

**URL:** http://localhost:8081

**Initial Admin Password:**
```bash
docker exec petstore-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

**Password:** `656a285c30334e0ca7361f89b815d009`

### 2. Plugin Kurulumu

"Getting Started" ekranında:

1. **"Install suggested plugins"** seçin ve bekleyin
2. Admin user oluşturun (kendi bilgilerinizle)
3. Jenkins URL'i onaylayın: http://localhost:8081

### 3. Ek Plugin'ler (Gerekirse)

Eğer manuel yüklemeniz gerekirse:
- **Manage Jenkins** → **Manage Plugins** → **Available**
- Şunları arayıp yükleyin:
  - ✅ Docker Pipeline
  - ✅ Docker
  - ✅ Git
  - ✅ Maven Integration
  - ✅ Pipeline
  - ✅ JUnit

---

## 🎯 İlk Pipeline Oluşturma

### Adım 1: Pipeline Oluştur

1. Jenkins Dashboard → **"New Item"** (veya "+ New Item")
2. **Item name:** `petstore-pipeline`
3. **Type:** Pipeline seçin
4. **OK**

### Adım 2: Pipeline Konfigürasyonu

Aşağıdaki ayarları yapın:

**Pipeline:**
- **Definition:** "Pipeline script from SCM" seçin
- **SCM:** Git seçin
- **Repository URL:** `file:///workspace` (veya Git repo URL'iniz)
- **Branch:** `*/main`
- **Script Path:** `Jenkinsfile`

**Advanced (opsiyonel):**
- Lightweight checkout: İşaretleyin (hızlı build için)

**Save**

### Adım 3: İlk Build

1. Dashboard → petstore-pipeline
2. **"Build Now"** butonuna tıklayın
3. Sol menüden **"Console Output"** linkini açın
4. Build sonuçlarını izleyin

---

## ✅ Build Başarılı Kontrol

Pipeline'ınız şunları yapmalı:

✅ **Checkout:** Kodu al
✅ **Build:** Maven compile
✅ **Test:** Unit testler
✅ **Package:** JAR oluştur
✅ **Docker Build:** Image build
✅ **Deploy:** docker-compose ile deploy
✅ **Health Check:** Uygulama kontrolü

Build başarılı olursa mavi ✓ işareti göreceksiniz!

---

## 🐛 Sorun Giderme

### Problem: "docker: command not found"

**Çözüm:**
Jenkins container'ında Docker yüklü olmayabilir. Şu komutu çalıştırın:

```bash
# Jenkins container'ına gir
docker exec -it petstore-jenkins bash

# Docker CLI kur (Debian/Ubuntu için)
apt-get update && apt-get install -y docker.io docker-compose-v2

# veya sadece docker-compose için
pip3 install docker-compose
```

### Problem: "docker-compose: command not found"

**Çözüm 1:** Docker Compose V2 kullanın (moderne):
Jenkinsfile'da şunu kullanın:
```groovy
sh 'docker compose down'
sh 'docker compose up -d --build'
```

**Çözüm 2:** Docker Compose'u manuel kurun:
```bash
docker exec -it petstore-jenkins bash
# Container içinde kurulum yap
```

### Problem: "Permission denied: docker.sock"

**Çözüm:**
```bash
# Host'ta çalıştırın
sudo chmod 666 /var/run/docker.sock
```

### Problem: Pipeline bulundu ama repo yok

**Çözüm:**
Jenkins'in projenizi görebilmesi için:
1. Repository URL'i absolute path olarak verin: `/Users/alperenalkan/IdeaProjects/PetStore`
2. Veya Git repository kullanıyorsanız URL'i verin

### Problem: Maven build hatası

**Çözüm:**
Jenkins'te Maven'in tanımlı olduğundan emin olun:
1. **Manage Jenkins** → **Tools**
2. Maven installation kontrol edin
3. Yoksa Maven'ı kurun veya local path verin

---

## 📊 Pipeline İstatistikleri

Build sonrası:
- **Dashboard:** Tüm pipeline'ları görüntüle
- **Build History:** Geçmiş build'leri gör
- **Test Results:** Test raporlarını incele
- **Artifacts:** Oluşturulan JAR dosyalarını indir

---

## 🔄 Otomatik Build (Git Hook)

Her git push'ta otomatik build için:

1. **Pipeline konfigürasyonunda** → **Build Triggers**
2. **"GitHub hook trigger for GITScm polling"** veya **"Poll SCM"** seçin
3. Poll schedule: `H/5 * * * *` (her 5 dakikada)

---

## 🎓 Yaygın Komutlar

```bash
# Jenkins logları
docker logs petstore-jenkins -f

# Jenkins restart
docker-compose restart jenkins

# Pipeline'ı manuel tetikle
curl -X POST http://localhost:8081/job/petstore-pipeline/build

# Tüm container'ları başlat
docker-compose up -d

# Tüm container'ları durdur
docker-compose down

# Clean build (volumes sil)
docker-compose down -v
```

---

## 📚 Kaynaklar

- **Jenkins Dashboard:** http://localhost:8081
- **PetStore App:** http://localhost:8080
- **Jenkins Docs:** https://www.jenkins.io/doc/
- **Pipeline Syntax:** https://www.jenkins.io/doc/book/pipeline/syntax/

---

## ✅ Başarı Kontrolü

Her şey doğru kurulmuşsa:

```bash
# Container durumu
docker-compose ps
# Tüm servisler "Up" olmalı

# Health check
curl http://localhost:8080/actuator/health
# {"status":"UP"} dönmeli

# Jenkins erişim
curl http://localhost:8081
# HTML yanıt almalısınız
```

**🎉 Başarılı! Şimdi pipeline'ınız çalışıyor!**

