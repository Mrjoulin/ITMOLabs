# Лабораторная #7
### Вариант `№4529`

## Запуск приложения:

### Чтобы запустить клиент (без DEBUG мода):
`java -jar client/build/libs/client-3.0-SNAPSHOT.jar`
### Для запуска в режиме отладки (с DEBUG):
`java -Dlogback.configurationFile=client/src/main/resources/logback-debug.xml -jar client/build/libs/client-3.0-SNAPSHOT.jar`

### Чтобы запустить сервер (без DEBUG мода):
`java -jar server/build/libs/server-3.0-SNAPSHOT.jar`
### Для запуска в режиме отладки (с DEBUG):
`java -Dlogback.configurationFile=server/src/main/resources/logback-debug.xml -jar server/build/libs/server-3.0-SNAPSHOT.jar`

#### Входная точка на клиенте и сервере `main.kt`

## Преременные окружения:
### Для клиента:
```text
HOST - IP сервера
PORT - порт, на котором работает сервер
CLIENT_TOKEN - токен клиента для быстрого доступа (без авторизации), действителен 7 дней
```
### Для сервера:
```text
PORT - Порт, на котором запускается сервер
DB_LOGIN - Логин для базы данных
DB_PASSWORD - Парль для базы данных
DB_HOST - Хост, на котором запущена база данных (IP:PORT)
DB_NAME - Название базы данных
```

## Общее описание проекта:

### Приложение для управления коллекцией объектов с использованием базы данных PostgreSQL
### Реализовано на языке Kotlin с использованием сборщика Gradle
