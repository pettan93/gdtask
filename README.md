# task

### Prerequisites
 - Docker
 - JDK 11

#### How to build

```
gradlew test bootJar
```

#### How to run
Run using docker compose
```
docker-compose build
docker-compose up
```
Or run using docker compose with three api service replicas
```
docker-compose -f docker-compose-ha.yml build
docker-compose -f docker-compose-ha.yml up --scale api=3
```
Or run jar directly (Docker still needed for Postgres)
```
docker run --name db --port 5432:5432 -e POSTGRES_PASSWORD=pass -e POSTGRES_DB=db -d postgres:13.0
java -jar build\libs\gd-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=db
```

### Notes
Forbidden words loaded during startup from a file **forbidden.txt** located in project root directory (file also copied to Docker image).
Words should be in following csv format:
```
dog;noun
bark;verb
lazy;adjective
```

### API

##### GET /
```
{
    "message": "Service alive! Ready to generate sentences based on rules : 3 words total with given types of words in order: noun, verb, adjective"
}
```

##### GET /words
Response
```
[
    {
        "word": {
            "text": "cat",
            "wordCategory": "NOUN"
        }
    },
    {
        "word": {
            "text": "drinks",
            "wordCategory": "VERB"
        }
    }
]
```
##### GET /words/pageable?page=4&size=100
Same as /words but pageable 

##### GET /words/{word}
Response
```
{
    "word": {
        "text": {word},
        "wordCategory": "NOUN"
    }
}
```

##### PUT /words/{word}
Request
```
{
    "word": {
        "wordCategory": "NOUN"
    }
}
```
Response
```
{
    "word": {
        "text": {word},
        "wordCategory": "NOUN"
    }
}
```

##### GET /sentences
Response
```
[
    {
        "sentence": {
            "id": 6,
            "text": "cat drinks very",
            "created": "2020-11-10T21:20:49.884741"
        }
    },
    {
        "sentence": {
            "id": 7,
            "text": "dog walks slowly",
            "created": "2020-11-10T21:20:53.457476"
        }
    }
}
```
##### GET /sentences/pageable?page=4&size=100
Same as /sentences but pageable 

##### GET /sentences/{id}
Response
```
{
    "sentence": {
        "id": 6,
        "text": "cat drinks very",
        "created": "2020-11-10T21:20:49.884741",
        "showDisplayCount": 2
    }
}
```

##### GET /sentences/{sentenceID}/yodaTalk
Response
```
{
    "sentence": {
        "text": "drinks very cat"
    }
}
```


##### POST /sentences/generate 
Response
```
{
    "sentence": {
        "id": 1,
        "text": "cat drinks slowly",
        "created": "2020-11-10T21:19:01.9593459",
        "showDisplayCount": 1
    }
}
```

##### GET /sentences/duplicates
Response
```
[
    {
        "duplicateSentence": {
            "text": "dog walks slowly",
            "duplicateCount": 2,
            "duplicateSentencesIds": [7, 8]
            ]
        }
    },
    {
        "duplicateSentence": {
            "text": "cat drinks slowly",
            "duplicateCount": 3,
            "duplicateSentencesIds": [2, 3, 4]
            ]
        }
    }
]
```
