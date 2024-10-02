# Проект: Оценка спроса/дефицита пропускной способности транспортных объектов в результате возникновения дополнительного спроса

## Описание
Этот проект представляет собой сервис для оценки  спроса/дефицита пропускной способности транспортных объектов с учетом дополнительного спроса, который возникает в связи с застройкой территорий. Сервис позволяет моделировать и анализировать потенциальные изменения в транспортных потоках, что может быть полезно для городских планировщиков и инженеров.

## Название команды
"Кибербобры"

## Состав команды
Антонов Евгений Федорович, Братцев Александр Максимович, Иванищев Никита Евгеньевич, Носов Андрей Никитович

## Установка и запуск

1. **Склонируйте репозиторий:**
    ```bash
    git clone https://github.com/yevgn/it_hack.git
    ```

2. **Перейдите в директорию проекта:**
    ```bash
    cd it_hack
    ```

3. **Соберите контейнеры с помощью Docker Compose:**
    ```bash
    docker compose build
    ```

4. **Запустите сервис:**
    ```bash
    docker compose up
    ```


## Доступ к сервису

После успешного запуска, сервис будет доступен по следующей ссылке:
- **Сервис:** [http://localhost:8080](http://localhost:8080)

Документация API в формате Swagger будет доступна по следующей ссылке:
- **Swagger Документация:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Использование

Для использования сервиса и ознакомления с API, перейдите по ссылке документации Swagger. Там вы найдёте описание всех доступных эндпоинтов и примеры запросов.

## Требования

- Docker
- Docker Compose

## Замечания
К сожелению, мы не успели сделать front-часть, однако API можно использовать. Back-часть хранится в директории src->main->java->ru>mephi->backend. Будем рады, если вы внимательно изучите код проекта.

## Контакты

Если у вас есть вопросы или предложения, вы можете связаться с авторами проекта через Issues или Pull Requests на GitHub.
