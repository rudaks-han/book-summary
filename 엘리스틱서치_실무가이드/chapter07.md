# 07 한글 검색 확장 기능

## 7.1 Suggest API 소개

Suggest API는 총 4가지 방식을 제공한다.

* Term Suggest API: 추천 단어 제안
* Completion Suggest API: 자동완성 제안
* Phrase Suggest: API 추천 문장 제안
* Context Suggest API: 추천 문맥 제안

이런한 기능은 영문에서는 잘 동작하지만 한글에서는 잘 동작하지 않는다.



### 7.1.1 Term Suggest API

```http
PUT movie_term_suggest/_doc/1
{
  "movieNm": "lover"
}

put movie_term_suggest/_doc/2
{
  "movieNm": "Fall love"
}

put movie_term_suggest/_doc/3
{
  "movieNm": "lovely"
}

put movie_term_suggest/_doc/4
{
  "movieNm": "lovestory"
}
```

검색어로 "lave"를 입력했다.

```http
POST movie_term_suggest/_search
{
  "suggest": {
    "spell-suggest": {
      "text": "lave",
      "term": {
        "field": "movieNm"
      }
    }
  }
}
```



### 7.1.2 Completion Suggest API

```http
put movie_term_completion
{
  "mappings": {
    "properties": {
      "movieNmEnComple": {
        "type": "completion"
      }
    }
  }
}
```

생성된 인덱스에 문서 추가

```http
PUT movie_term_completion/_doc/1
{
  "movieNmEnComple": "After love"
}
PUT movie_term_completion/_doc/2
{
  "movieNmEnComple": "Lover"
}
PUT movie_term_completion/_doc/3
{
  "movieNmEnComple": "Love for a mother"
}
PUT movie_term_completion/_doc/4
{
  "movieNmEnComple": "Fall love"
}
PUT movie_term_completion/_doc/5
{
  "movieNmEnComple": "My lovely wife"
}
```

"L"로 시작하는 모든 영화 제목을 검색해보자.

```http
POST movie_term_completion/_search
{
  "suggest": {
    "movie_completion": {
      "prefix": "l",
      "completion": {
        "field": "movieNmEnComple",
        "size": 5
      }
    }
  }
}
```



## 7.2 맞춤법 검사기

### 7.2.1 Term Suggester API를 이용한 오타 교정



## 7.3 한글 키워드 자동완성

