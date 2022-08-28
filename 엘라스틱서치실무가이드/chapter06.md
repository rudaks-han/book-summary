# 06 고급 검색

## 6.1 한글 형태소 분석기 사용하기

### 6.1.1 은전한닢 형태소 분석기

```http
./bin/elasticsearch-plugin install https://github.com/javacafe-project/elastic-book-etc/raw/master/plugin/elasticsearch-analysis-seunjeon-6.4.3.zip
```



### 6.1.2 Nori 형태소 분석기

* Nori는 엘라스틱서치 6.4버전에서 공식적으로 릴리스됐다.
* 기존 형태소 분석기에 비해 30%이상 빠르고 메모리 사용량도 현저하게 줄었다.

#### 6.1.2.1 nori_tokenizer 토크나이저

토크나이저는 형태소를 토큰 형태로 분리하는 데 사용한다.

* decompound_mode: 복합명사를 토크나이저가 처리하는 방식
* user_dictionary: 사용자 사전 정의

##### 1) decompound_mode

decompound_mode는 토크나이저가 복합명사를 처리하는 방식을 결정한다. 복합명사가 있을 경우 단어를 어떻게 쪼갤지 결정한다. 단어를 쪼개는 방법은 다음과 같이 세 가지 중에서 설정할 수 있다.

| 파라미터명 | 파라미터 값 | 설명 | 예제 |
| :--- | :--- | :--- |:--- |
| decompound_mode | none | 복합명사로 분리하지 않는다 |월미도<br />영종도|
|      | discard | 복합명사로 분리하고 원본 데이터는 삭제한다. |잠실역=>[잠실,역]|
|      | mixed | 복합명사로 분리하고 원본 데이터는 유지한다. |잠실역=>[잠실,역,잠실역]|

##### 2) user_dictionary

* Nori 토크나이저는 내부적으로 세종 말뭉치와 mecab-ko-dic 사전을 사용한다.
* user_dictionary를 이용해 사용자가 정의한 명사를 사전에 추가로 등록할 수 있다.
* user_dictionary는 엘라스틱서치 서버가 설치된 데이터 아래에 config/userdic_ko.txt 형태로 생성해서 사용할 수 있으며 인덱스 매핑 시 분석기의 파라미터로 사전 경로를 등록하면 된다.

userdic_ko.txt 파일에 명사를 추가하는 방법은 다음과 같다.

- 삼성전자
- 삼성전자 삼성 전자

userdict_ko.txt 사용자 사전을 추가한 예다.

```http
PUT nori_analyzer
{
  "settings": {
    "index": {
      "analysis": {
        "tokenizer": {
          "nori_user_dict_tokenizer": {
            "type": "nori_tokenizer",
            "decompound_mode": "mixed",
            "user_dictionary": "userdict_ko.txt"
          }
        },
        "analyzer": {
          "nori_token_analyzer": {
            "type": "custom",
            "tokenizer": "nori_user_dict_tokenizer"
          }
        }
      }
    }
  }
}
```

생성된 nori_analyzer 인덱스에 설정된 nori_token_analyzer를 테스트해보자.

```http
POST nori_analyzer/_analyze
{
  "analyzer": "nori_token_analyzer",
  "text": "잠실역"
}
```



#### 6.1.2.2 nori_part_speech 토큰 필터

* nori_part_speech 토큰 필터는 품사 태그 세트와 일치하는 토큰을 찾아 제거하는 토큰 필터다.
* 이를 이용하면 문서에 존재하는 모든 명사를 역색인으로 생성하는 것이 아니라 역색인될 명사를 선택적으로 고를 수 있다.
* 이를 통해 사용하고 싶지 않은 형태소를 제거할 수 있다.
* 해당 토큰 필터는 stoptags라는 파라터를 제공하는데, 이 파라미터를 이용해 분리된 토큰에서 제거할 특정 형태소를 지정하는 것이 가능하다.



한글 형태소 분석기로 Nori를 사용하려면 다음과 같이 설정하면 된다.

```
PUT nori_full_analyzer
{
  "mappings": {
    "_doc": {
      "properties": {
        "description": {
          "type": "text",
          "analyzer": "korean_analyzer"
        }
      }
    }
  },
  "settings": {
    "index": {
      "analysis": {
        "analyzer": {
          "korean_analyzer": {
            "filter": [
              "pos_filter_speech",
              "nori_readingform",
              "lowercase"
            ],
            "tokenizer": "nori_tokenizer"
          }
        },
        "filter": {
          "pos_filter_speech": {
            "type": "nori_part_of_speech",
            "stoptags": [
              "E",
              "IC",
              "J",
              "MAG",
              "MAJ",
              "MM",
              "NA",
              "NR",
              "SC",
              "SE",
              "SF",
              "SH",
              "SL",
              "SN",
              "SP",
              "SSC",
              "SSO",
              "SY",
              "UNA",
              "UNKNOWN",
              "VA",
              "VCN",
              "VCP",
              "VSV",
              "VV",
              "VX",
              "XPN",
              "XR",
              "XSA",
              "XSN",
              "XSV"
            ]
          }
        }
      }
    }
  }
}
```





## 6.2 검색 결과 하이라이트하기

하이라이트는 문서 검색 결과를 웹상에서 출력할 때 사용자가 입력한 검색어를 강조하는 기능이다.

간단한 데이터를 하나 생성해보자.

```http
PUT movie_highlighting/_doc/1
{
  "title": "Harry Potter and the Deathly Hallows"
}
```

데이터를 검색할 때 "highlight" 옵션을 이용해 하이라이트를 수행할 필드를 지정하면 검색 결과로 하이라이트된 데이터의 일부가 함께 리턴된다.

```http
POST movie_highlighting/_search
{
  "query": {
    "match": {
      "title": {
        "query": "harry"
      }
    }
  },
  "highlight": {
    "fields": {
      "title": {}
    }
  }
}
```

<em> 태그가 아닌 별도의 태그를 이용하려면 "highlight" 옵션 내부에 원하는 태그를 정의하면 된다.

```http
POST movie_highlighting/_search
{
  "query": {
    "match": {
      "title": {
        "query": "harry"
      }
    }
  },
  "highlight": {
    "pre_tags": [
      "<strong>"
    ], 
    "post_tags": [
      "</strong>"
    ], 
    "fields": {
      "title": {}
    }
  }
}
```



## 6.3 스크립트를 이용해 동적으로 필드 추가하기

* 엘라스틱서치는 스크립트를 이용해 사용자가 특정 로직을 삽입하는 것이 가능하다.
* 이러한 방식을 스크립팅(Scripting)이라 한다.
* 스크립팅을 이용하면 두 개 이상의 필드 스코어를 하나로 합하거나 계산된 스코어를 특정 수식으로 재계산하는 등의 작업이 가능해진다.

> 참고
>
> 엘라스틱서치에서 스크립팅을 사용하는 두 가지 방법이 있다.
>
> 1. config 폴더에 스크립팅을 저장하는 방식: 스크립트 파일을 config 폴더에 저장한 다음, 이름을 지정해 코드에서 호출한다.
> 2. in-requests 방식: 동적 스크립팅이라고 하며 API를 호출할 때 코드 내에서 스크립트를 직접 정의해서 사용한다.
>
> 일반적으로 동적 스크립팅 방식이 많이 사용되며 elasticsearch.yml 파일에 다음과 같은 설정을 추가해야 한다.
>
> script.disable_dynamic: false



##### 필드 추가

영화 평점을 정의한 문서가 하나 있고 movieList 필드 내부에는 총 3건의 영화 평점이 존재한다고 해보자.

```http
```



## 6.4 검색 템플릿을 이용한 동적 쿼리 제공



## 6.5 별칭을 이용해 항상 최신 인덱스 유지하기

_reindex API를 사용해 movie_info 인덱스를 생성해 보자.

```http
POST _reindex
{
  "source": {
    "index": "movie_search"
  },
  "dest": {
    "index": "movie_info"
  }
}
```

인덱스가 만들어지면 _alias를 통해 두 인덱스를 movie라는 별칭으로 만들어 보자.

```http
POST _aliases
{
  "actions": [
    {
      "add": {"index": "movie_search", "alias": "movie"}
    },
    {
      "add": {"index": "movie_info", "alias": "movie"}
    }
  ]
}
```

이제 movie라는 별칭으로 문서를 조회해 보자.

```http
POST movie/_search
```



## 6.6 스냅숏을 이용한 백업과 복구



