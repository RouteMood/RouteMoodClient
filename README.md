# RouteMood - Генератор маршрутов для Android

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Содержание
- [Описание](#описание)
- [Возможности](#возможности)
- [Скриншоты](#скриншоты)
- [Технологии](#технологии)
- [Установка и запуск](#установка-и-запуск)
- [Лицензия](#лицензия)

## Описание
Android-приложение для создания и оценки пеших маршрутов. Позволяет генерировать маршруты с помощью YandexGPT или создавать их вручную на карте.

## Возможности
✔ Генерация маршрутов через YandexGPT  
✔ Ручное создание маршрутов на карте  
✔ Сохранение маршрутов локально (Room Database)  
✔ Обмен маршрутами с другими пользователями  
✔ Рейтинговая система (1-5 звёзд)  
✔ Пользовательские аватары (Datastore + сервер)  
✔ Воспроизведение видео при генерации маршрута  

## Скриншоты
| Экран входа | Настройки маршрута | Профиль |
|-------------|-------------------|---------|
| <img src="https://lh3.googleusercontent.com/keep-bbsk/AFgXFlJA98inIg-p0bALx3hnMtseZycTqESwTe19UxnT3E5CcTRmhFByGkhUTPomM6_88u4DR9jRnILGqMBoz_zffRIxfAAnnfXDWUScESXFuFWTwvqryt0a2w=s512" width="200"/> | <img src="https://lh3.googleusercontent.com/keep-bbsk/AFgXFlJ2faS1d_ZddRJVyVnSi3VGY6D8VUs0ihYITypEzlqNadjjpBb-m5InXKUCrSA3QgOVORx7AxlBUReIz58slAiFhJbQdnogRMKje4AbFs3VpNzoKo4HYA=s512" width="200"/> | <img src="https://lh3.googleusercontent.com/keep-bbsk/AFgXFlJZTP4eEkB0q54aYoqXXhnECQNg13jOFbQUXurAFGny4pu4eLhAyb_M4Odwh19Eq-cbEruzpcrWJ40w8WGJrjzUFE1i3vlMYHGCVOvJBXlzoEBzm65J7Q=s2048" width="200"/> |

| Создание маршрута | Просмотр маршрута | Рейтинг |
|-------------------|------------------|---------|
| <img src="https://lh3.googleusercontent.com/keep-bbsk/AFgXFlJ6UJmdXGHydamiZOtDx3PBRpNFhWjUCzfVlzzqKVBTwErKGWexXw6xcsKduLjKKvmDxPPsdd6U-gXjrZpHXuFinB3ShsWyHu05tmqrdlH1ytYvLYBqaw=s2048" width="200"/> | <img src="https://lh3.googleusercontent.com/keep-bbsk/AFgXFlLYzn_xD2CaDe-BVTJl-r0i3P6VOoxD6nmnRcnv1atkMh0UJV-0XH_nbR8KglvcULRoRs1C8LY5QqMDeIJ5MRypJHKQwDQHgtLBwYsijMvR1YEoudVO-Q=s2048" width="200"/> | <img src="https://lh3.googleusercontent.com/keep-bbsk/AFgXFlLvJvKL5XjkivkClluPaswBUxt-mSmh18i88gx826dR_Q7vIBa8KIMo7eXptc0rZ6Ad5Dm14QNTR-OOcNsm3nA15fwZfDpJplb4Ehcm92igzWRGWcK5RQ=s2048" width="200"/> |

## Технологии
- Язык: Kotlin, Jetpack Compose
- Карты: Google Maps SDK
- Локальное хранилище: Room Database, DataStore
- Сетевое взаимодействие: Retrofit
- Генерация маршрутов: YandexGPT API
- Архитектура: MVVM

## Установка и запуск
1. Установите [Android Studio](https://developer.android.com/studio)
2. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/RouteMood/RouteMoodClient.git
3. Откройте проект в Android Studio

4. Добавьте API ключи:

Создайте файл local.default.properties в корне проекта
```properties
# Ключ для Google Maps SDK
MAPS_API_KEY=your_google_maps_key

# Ключ для Google Directions API
MAPS_SERVER_API_KEY=your_directions_api_key

    