# ExpSys

Мобильная экспертная система на базе продукций для диагностики
аппаратных неисправностей ПК и ноутбуков.

## Возможности

- Ввод симптомов чекбоксами (набор формируется из базы правил).
- Продукционный вывод: подбор причины и рекомендаций из `rules.json`.
- Fallback на нейросеть (DeepSeek) при отсутствии полного совпадения.
- История запросов на Room (SQLite).
- Тёмная тема navy blue, glow-эффект на кнопках, шрифт Consolas.

## Стек

- Kotlin + Jetpack Compose (Material 3)
- Room (история)
- OkHttp + Gson (LLM-запросы)
- AGP 8.2.2, Kotlin 1.9.22, minSdk 24, targetSdk 34

## Сборка

1. Скопировать `local.properties.example` → `local.properties` и вписать `LLM_API_KEY`.
2. (Опционально) положить `consolas.ttf` в `app/src/main/res/font/`.
   Без него используется системный Monospace.
3. Открыть проект в Android Studio или выполнить:

```bash
./gradlew assembleDebug
```

APK появится в `app/build/outputs/apk/debug/`.

## Архитектура

```
UI (Compose)  →  ExpertSystem (логика)  →  rules.json + Room
```

- `logic/ExpertSystem.kt` — загрузка правил, прямой вывод, вызов LLM.
- `data/*` — модель правила и Room-таблица истории.
- `ui/*` — три экрана: Home / Result / History.

## Безопасность

API-ключ хранится вне репозитория (`local.properties`), попадает в код
только через `BuildConfig.LLM_API_KEY`. Сетевой доступ ограничен
`INTERNET`-разрешением.
