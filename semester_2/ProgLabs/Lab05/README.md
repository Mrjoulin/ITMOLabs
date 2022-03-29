# Lab #5
### Вариант `№41511`

## Запуск приложения:

### Перед запуском Вам необходимо добавить переменную окружения COLLECTION для того, чтобы программа загрузила из него стартовую коллекцию
### Вы можете добавить свою, или использовать готовую введя команду: 
#### `export COLLECTION=src/main/resources/collection.xml  `

### Чтобы запустить консольный клиент (без DEBUG мода):
#### `java -jar build/libs/Lab05-1.0-SNAPSHOT.jar`
### Для запуска в режиме отладки (с DEBUG):
#### `java -Dlogback.configurationFile=src/main/resources/logback-debug.xml -jar build/libs/Lab05-1.0-SNAPSHOT.jar`

#### Входная точка `main.kt`


## Общее описание проекта:

### Консольное приложение для управления коллекцией объектов
### Реализовано на языке Kotlin с использованием сборщика Gradle

### Документация в формате HTML доступна [тут](./build/dokka/html/index.html)
