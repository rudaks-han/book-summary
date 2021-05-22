# 03 데이터 모델링

## 3.1 매핑 API 이해하기

매핑은 색인 시 데이터가 어디에 어떻게 저장될지를 결정하는 설정이다. (데이터베이스의 스키마에 대응하는 개념)

스키마리스로 생성할 경우 다음과 같은 문제가 발생할 수도 있다.

```
# 문서1

{
	"movieCd": "20173732",
	"movieNm": "캡틴 아메리카"
}

# 문서2

{
	"movieCd": "XT001",
	"movieNm": "아이언맨"
}
```

* 첫 번째 문서를 매핑 설정 없이 색인하면 movieCd 필드는 숫자 타입으로 매핑되고 movieNm은 문자 타입으로 매핑된다. 
* 바로 두 번째 문서를 색인해보자. 아마도 색인에 실패할 것이다. 
* movieCd 필드가 이미 숫자 타입으로 매핑됐기 때문에 문자열 형태인 두 번째 문서의 movieCd값은 색인이 불가능하기 때문이다. 



매핑 정보를 설정할 때는 다음과 같은 사항을 고민해야 한다.

* 문자열을 분석할 것인가?
* _source에 어떤 필드를 정의할 것인가?
* 날짜 필드를 가지는 필드는 무엇인가?
* 매핑에 정의되지 않고 유입되는 필드는 어떻게 처리할 것인가?



### 3.1.1 매핑 인덱스 만들기

movie_search는 개봉 영화의 세부 정보를 제공하는 인덱스다.

| 매핑명         | 필드명              | 필드 타입         |
| :------------- | :------------------ | :---------------- |
| 인덱스 키      | movieCd             | keyword           |
| 영화제목_국문  | movieNm             | text              |
| 영화제목_영문  | movieNmEn           | text              |
| 제작연도       | prdtYear            | integer           |
| 개봉연도       | openDt              | integer           |
| 영화유형       | typeNm              | keyword           |
| 제작상태       | prodtStatNm         | keyword           |
| 제작국가(전체) | nationAlt           | keyword           |
| 장르(전체)     | genreAlt            | keyword           |
| 대표 제작국가  | repNationNm         | keyword           |
| 대표 장르      | repGenreNm          | keyword           |
| 영화감독명     | directors.peopleNm  | object -> keyword |
| 제작사코드     | companies.companyCd | object -> keyword |
| 제작사명       | companies.companyNm | object -> keyword |

실제 데이타는 다음과 같다.

```json
{
	"movieCd": "20173732",
  "movieNm": "살아남은 아이",
  "movieNmEn": "Last Child",
  "prdtYear": "2017",
  "openDt": "",
  "typeNm": "장편",
  "prdtStatNm": "기타",
  "nationAlt": "한국",
  "genreAlt": "드라마,가족",
  "repNationNm": "한국",
  "repGenreNm": "한국",
  "directors": [{
  	"peopleNm": "신동석"
  }],
  "companies": [
  	"companyCd": "",
  	"companyNm" ""
  ]
}
```

다음과 같이 인덱스를 생성한다.

```http
PUT movie_search
{
  "settings": {
    "number_of_shards": 5,
    "number_of_replicas": 1
  },
  "mappings": {
    "_doc": {
      "properties": {
        "movieCd": {
          "type": "keyword"
        },
        "movieNm": {
          "type": "text",
          "analyzer": "standard"
        },
        "movieNmEn": {
          "type": "text",
          "analyzer": "standard"
        },
        "prdtYear": {
          "type": "integer"
        },
        "openDt": {
          "type": "date"
        },
        "typeNm": {
          "type": "keyword"
        },
        "prdtStatNm": {
          "type": "keyword"
        },
        "nationAlt": {
          "type": "keyword"
        },
        "genreAlt": {
          "type": "keyword"
        },
        "repNationNm": {
          "type": "keyword"
        },
        "repGenreNm": {
          "type": "keyword"
        },
        "companies": {
          "properties": {
            "companyCd": {
              "type": "keyword"
            },
            "companyNm": {
              "type": "keyword"
            }
          }
        },
        "directors": {
        	"properties": {
        		"peopleNm": {
        			"type": "keyword"
        		}
        	}
        }
      }
    }
  }
}
```

### 3.1.2 매핑 확인

매핑을 확인하려면 _mapping AP를 사용할 수 있다.

```http
GET movie_search/_mapping
```



### 3.1.3 매핑 파라미터

##### analyzer

* 해당 필드의 데이터를 형태소 분석하겠다는 의미  
* text 타입의 필드는 analyzer 매핑 파라미터를 기본적으로 사용해야 한다.  
* 별도의 분석기를 지정하지 않으면 Standard Analyzer로 형태로 분석을 수행한다.

##### normalizer

* normalizer 매핑 파라미터는 term query에 분석기를 사용하기 위해 사용된다.  
* keyword 데이터 타입의 경우 원문을 기준으로 문서가 색인되기 때문에 cafe,Cafe는 서로 다른 문서로 인식된다.  
* 하지만 normalizer를 통해 분석기에 asciifolding과 같은 필터를 사용하면 같은 데이터로 인식되게 할 수 있다.

##### boost

* 필드에 가중치를 부여한다.  
* 가중치에 따라 유사도 점수가 달라지기 때문에 boost 설정 시 검색 결과의 노출 순서에 영향을 준다.

> 최신 엘라스틱서치는 색인 시 boost 설정을 할 수 없도록 바뀌었다.

##### coerce

* 색인 시 자동 변환을 허용할 지 여부를 설정하는 파라미터다.  
* 예를 들어 "10"과 같은 숫자 형태의 문자열이 integer 타입의 필드에 들어온다면 엘라스틱 서치는 자동으로 형변환을 수행해서 정상적으로 처리한다.  
* 하지만 coerce 설정을 미사용으로 변경한다면 색인에 실패할 것이다.

##### copy_to

* 매핑 파라미터를 추가한 필드의 값을 지정한 필드로 복사한다. 
* keyword 타입의 필드에 copy_to 매핑 파라미터를 사용해 다른 필드로 값을 복사하면 복사된 필드에서는 text 타입을 지정해 형태소 분석을 할 수도 있다.

##### fielddata

* 힙 공간에 생성하는 메모리 캐시다. 
* 최신 버전의 엘라스틱서치는 doc_values라는 새로운 형태의 캐시를 제공하고 있으며 text타입의 필드를 제외한 모든 필드는 기본적으로 doc_values 캐시를 사용한다.

text타입의 필드에서 집계나 정렬을 수행하는 경우에 부득이하게 fielddata를 사용할 수 있다. 하지만 fielddata는 메모리에 생성되는 캐시이기 때문에 최소한으로 사용해야 한다는 사실에 주의해야 한다.
fielddata는 메모리 소모가 크기 때문에 기본적으로 비활성화돼 있다.  

사용법은 다음과 같다.

```http
PUT movie_search_mapping/_mapping/_doc
{
    "properties": {
        "nationAltEn": {
            "text": "text",
            "fielddata": true
    }
}
```

##### doc_values

* 엘라스틱서치에서 사용하는 기본 캐시다. 
* text 타입을 제외한 모든 타입에서 기본적으로 doc_values 캐시를 사용한다. 
* doc_values는 루씬을 기반으로 하는 캐시방식이다.
* 과거에는 캐시를 모두 메모리에 올려 사용했으나 doc_values를 사용함으로써 힙 사용에 대한 부담을 없애고 운영체제의 파일 시스템 캐시를 통해 디스크에 있는 데이터에 빠르게 접근할 수 있다.
* 이로 인해 GC의 비용이 들지 않으면서도 메모리 연산과 비슷한 성능을 보여준다.

##### dynamic

* 매핑에 필드를 추가할 때 동적으로 생성할지, 생성하지 않을지를 결정한다.

| 값     | 의미                                                         |
| ------ | :----------------------------------------------------------- |
| true   | 새로 추가되는 필드를 매핑에 추가한다.                        |
| false  | 새로 추가되는 필드를 무시한다. 해당 필드는 색인되지 않아 검색할 수 없지만 _source에는 표시된다. |
| strict | 새로운 필드가 감지되면 예외가 발생하고 문서 자체가 색인되지 않는다. 새로 유입되는 필드는 사용자가 매핑에 명시적으로 추가해야 한다. |

##### enabled

* 검색 결과에는 포함하지만 색인은 하고 싶지 않은 경우도 있다. 메타 성격의 데이터가 그렇다. 
* 일반적인 게시판이라면 제목과 요약 글만 색인하고 날짜와 사용자 ID는 색인하지 않는 경우다.

##### format

* 날짜/시간을 문자열로 표시한다.

| 포캣       | 날짜 형식 | 비고       |
| :--------- | :-------- | :--------- |
| basic_date | yyyyMdd   | 년도/월/일 |
| ...        | ...       | ...        |

##### ignore_above

* 필드에 저장되는 문자열이 지정한 크기를 넘어서면 빈 값으로 색인한다. 
* 지정한 크기만큼의 색인되는 것이 아니라 빈 값으로 저장되므로 주의해야 한다.

##### ignore_malformed

* 잘못된 데이터 타입을 색인하려고 하면 예외가 발생하고 해당 문서 전체가 색인되지 않는다. 
* 이 매핑 파라미터를 사용하면 해당 필드만 무시하고 문서는 색인할 수 있다.

##### index

* 필드값을 색인할 지를 결정한다. 
* 기본값은 true이며, false로 변경하면 해당 필드를 색인하지 않는다.

##### fields

* 다중 필드를 설정할 수 있는 옵션이다.
* 필드 안에 또 다른 필드의정보를 추가할 수 있어 같은 string값을 다른 분석기로 처리하도록 설정할 수 있다.

##### norms

* 문서의 _scores 값 계산에 필요한 정규화 인수를 사용할지 여부를 설정한다. 기본값은 true이다. 
* _score 계산이 필요없거나 단순 필터링 용도로 사용하는 필드는 비활성화해서 디스크 공간을 절약할 수 있다.

##### null_value

* 엘라스틱서치는 색인 시 문서에 필드가 없거나 필드의 값이 null이면 색인 시 필드를 생성하지 않는다.
* 이 경우 null_value를 설정하면 문서의 값이 null이더라도 필드를 생성하고 그에 해당하는 값(default)으로 저장한다.

##### position_increment_gap

* 배열 형태의 데이터를 색인할 때 검색의 정확도를 높이기 위해 제공하는 옵션이다. 
* 필드 데이터 중 단어와 단어 사이의 간격을 허용하지를 결정한다.
* 검색 시 단어와 단어 사이의 간격을 기준으로 일치하는 문서를 찾는 데 필요하다.
* 예를 들어, 데이터가 ["John Abraham", "Lincon Smith"] 일때 "Abraham Lincon"으로 검색하더라도 검색이 가능하다.

##### properties

* Object 타입이나 Nested 타입의 스키마를 정의할 때 사용되는 옵션으로 필드의 타입을 매핑한다.
* Object 필드 및 Nested 필드에는 properties라는 서브 필드가 있다.

##### search_analyzer

* 일반적으로 색인과 검색 시 같은 분석기를 사용한다. 
* 만약 다른 분석기를 사용하고 있은 경우 search_analyzer를 설정해서 검색 시 사용할 분석기를 별도로 지정할 수 있다.

##### similarity

* 유사도 측정 알고리즘을 지정한다. 유사도 측정 방식을 기본 알고리즘인 BM25에서 다른 알고리즘으로 변경할 수 있다.

##### store

* 필드의 값을 저장해 검색 결과에 값을 포함하기 위한 매핑 파라미터다.
* 기본적으로 엘라스틱서치에서는 _source에 색인된 문서가 저장된다. 
* 하지만 store 매핑 파라미터를 사용하면 해당 필드를 자체적으로 저장할 수 있다.
* 예를 들어 10개의 필드가 존재하고 해당 필드에 데이터를 매핑한 상태라면 _source를 로딩해서 해당 필드를 찾는 것보다 사용할 각 필드만 로드해서 사용하는 편이 효율적이다.
* 하지만 매핑 파라미터를 사용하면 디스크를 더 많이 사용한다.

##### term_vector

* 루씬에서 분석된 용어의 정보를 포함할지 여부를 결정하는 매핑 파라미터이다.



## 3.2 메타 필드

메타 필드는 엘라스틱서치에서 생성한 문서에서 제공하는 특별한 필드다. 이것은 메타데이터를 저장하는 특수 목적의 필드로서 이를 이용하면 검색 시 문서를 다양한 형태로 제어하는 것이 가능해진다.

### 3.2.1 _index 메타 필드

해당 문서가 속한 인덱스의 이름을 담고 있다.

### 3.2.2 _type 메타 필드

해당 문서가 속한 매핑의 타입 정보를 담고 있다.

### 3.2.3 _id 메타 필드

문서를 식별하는 유일한 키 값이다.

### 3.2.4 _uid 메타 필드

특수한 목적의 식별자다. "#"태그를 사용해 _type과 _id 값을 조합해 사용한다. 내부적으로만 사용한다.

### 3.2.5 _source 메타 필드

문서의 원본 데이터를 제공한다.

### 3.2.6 _all 메타 필드

모든 필드의 정보를 가진 메타 필드다.

데이터 크기를 많이 차기해서 6.0이상부터 deprecated 되었다.

### 3.2.7 _routing 메타 필드

특정 문서를 특정 샤드에 저장하기 위해 사용자가 지정하는 메타필드다.

```http
PUT movie_routing/_doc/1?routing=ko
{
  "repGenreNm": "한국어",
  "movieNm": "살아남은 아이"
}
```

검색할 때도 _routing 값을 지정해야 한다.

```http
POST movie_routing/_doc/_search?routing=ko
```



## 3.3 필드 데이터 타입

### 3.3.1 Keyword 데이터 타입

* 말 그대로 키워드 형태로 사용할 데이터에 적합한 데이터 타입이다.
* Keyword 타입을 사용할 경우 별도의 분석기를 거치지 않고 원문 그대로 색인하기 때문에 특정 코드나 키워드 등 정형화된 콘텐츠에 주로 사용된다.
* 엘라스틱서치의 일부 기능은 형태소 분석을 하지 않아야만 사용이 가능한데 이 경우에도 Keyword 데이터 타입이 사용된다.

```http
PUT movie_search_datatype/_mapping/_doc
{
  "properties": {
    "multiMovieYn": {
      "type": "keyword"
    }
  }
}
```

Keyword 데이터 타입은 아래에 해당하는 항목에 많이 사용된다.

* 검색 시 필터링 되는 항목
* 정렬이 필요한 항목
* 집계해야 하는 항목

만약 'elastic search'라는 문자열이 keyword 타입으로 설정되면 'elastic'이나 'search'라는 질의로는 절대 검색되지 않는다. 
정확히 'elastic search'라고 질의해야만 검색된다.

Keyword 타입에서 설정 가능한 주요 파라미터를 아래와 같다.

| 파라미터   | 설명                                                         |
| :--------- | :----------------------------------------------------------- |
| boost      | 필드의 가중치로, 검색 결과 정렬에 영향을 준다. 기본값은 1.0으로서 1보다 크면 점수가 높게 오르고, 적으면 점수가 낮게 오른다. 이를 이용해 검색에 사용된 키워드와 문서 간의 유사도 스코어 값을 계산할 때 필드의 가중치 값을 얼마나 더 줄 것인지를 판단한다. |
| doc_values | 필드를 메모리에 로드해 캐시로 사용한다. (기본값: true)       |
| index      | 해당 필드를 검색에 사용할지를 설정한다. (기본값: true)       |
| null_value | 기본적으로 엘라스틱 서치는 데이터의 값이 없으면 필드를 생성하지 않는다. 데이터의 값이 없는 경우 null로 필드의 값을 대체할지를 설정한다. |
| store      | 필드 값을 필드와 별도로 _source에 저장하고 검색 가능하게 할지를 설정한다. (기본값: false) |



### 3.3.2 Text 데이터 타입

* Text 데이터 타입을 이용하면 색인 시 저정된 분석기가 칼럼의 데이터를 문자열 데이터로 인식하고 이를 분석한다.
* 만일 별도의 분석기를 정의하지 않았다면 기본적으로 Standard Analyzer를 사용한다.
* 영화의 제목이나 영화의 설명글과 같이 문장 형태의 데이터에 사용하기 적합한 데이터 타입이다.
* Text 데이터 타입은 전문 검색이 가능하다는 점이 가장 큰 특징이다.
* Text 타입으로 데이터를 색인하면 전체 텍스트가 토큰화되어 생성되며 특정 단어를 검색하는 것이 가능해진다.

다음은 Text 데이터 타입을 사용하는 예다.

```http
PUT movie_text/_mapping/_doc
{
  "properties": {
    "movieComment": {
      "type": "text"
    }
  }
}
```

Text 타입과 Keyword 타입을 동시에 갖도록 멀티 필드로 설정할 수 있다.

```http
PUT movie_search/_mapping/_doc
{
  "properties": {
    "movieComment": {
      "type": "text",
      "fields": {
        "movieComment_keyword": {
          "type": "keyword"
        }
      }
    }
  }
} 
```

Text 타입에서 설정 가능한 주요 파라미터는 아래와 같다.

| 파라미터        | 설명                                                         |
| :-------------- | :----------------------------------------------------------- |
| analyzer        | 인덱스와 검색에 사용할 형태소 분석기를 선택한다. 기본값은 Standard Anaylzer다. |
| boost           | 필드의 가중치로, 검색 결과 정렬에 영향을 준다. 기본값은 1.0으로서 1보다 크면 점수가 높게 오르고, 적으면 점수가 낮게 오른다. |
| fielddata       | 정렬, 집계, 스크립트 등에서 메모리에 저장된 필드 데이터를 사용할지를 설정한다. 기본값은 false다. |
| index           | 해당 필드를 검색에 사용할지를 설정한다. (기본값: true)       |
| norms           | 유사도 점수를 산정할 때 필드 길이를 고려할지를 결정한다. (기본값: true) |
| store           | 필드 값을 필드와 별도로 _source에 저장하고 검색 가능하게 할지를 설정한다. (기본값: false) |
| search_analyzer | 검색에 사용할 형태소 분석기를 선택한다.                      |
| similarity      | 유사도 점수를 구하는 알고리즘을 선택한다. (기본값: BM25)     |
| term_vector     | Analyzed 필드에 텀벡터를 저장할지를 결정한다. (기본값: no)   |



### 3.3.3 Array 데이터 타입

* 데이터는 대부분 1차원(하나의 필드에 하나의 값이 매핑)으로 표현되지만 2차원(하나의 필드에 여러 개의 값이 매핑)으로 존재하는 경우도 있을 것이다.
* Array 타입은 문자열이나 숫자처럼 일반적인 값을 지정할 수도 있지만 객체 형태로 정의할 수도 있다.
* 한가지 주의할 점은 Array 타입에 저장되는 값은 모두 같은 타입으로만 구성해야 한다는 점이다.
    * 문자열 배열: ["one", "two"]
    * 정수 배열: [1,2]
    * 객체 배열: [{"name": "Marry", "age": 12}, {"name": "John", "age": 10}]

Array 데이터 타입을 사용하는 예다.

```http
PUT movie_search_datatype/_doc/1
{
  "title": "해리포터와 마법사의 돌",
  "subtitleLang": ["ko", "en"]
}
```



### 3.3.4 Numeric 데이터 타입

* 엘라스틱서치에서 숫자 데이터 타입은 여러 가지 종류가 제공된다. 
* 숫자 데이터 타입이 여러 개 제공되는 이유는 데이터의 크기에 알맞은 타입을 제공함으로써 색인과 검색을 효율적으로 처리하기 위해서다.

| 데이타 타입 | 설명                                           |
| :---------- | :--------------------------------------------- |
| long        | 최소값과 최대값을 가지는 부호 있는 64비트 정수 |
| integer     | 최소값과 최대값을 가지는 부호 있는 32비트 정수 |
| short       | 최소값과 최대값을 가지는 부호 있는 16비트 정수 |
| byte        | 최소값과 최대값을 가지는 부호 있는 8비트 정수  |
| double      | 64비트 부동 소수점을 갖는 수                   |
| float       | 32비트 부동 소수점을 갖는 수                   |
| half_float  | 16비트 부동 소수점을 갖는 수                   |

다음은 컬럼에 Integer 데이터 타입을 사용하는 예다.

```http
PUT movie_text/_mapping/_doc
{
  "properties": {
    "year": {
      "type": "integer"
    }
  }
}
```

#### 

### 3.3.5 Date 데이터 타입

* Date 타입은 JSON 포맷에서 문자열로 처리된다. 
* 기본 형식은 "yyyy-MM-0ddTHH:mm:ssZ"로 지정된다.
* Date 타입은 세 가지 형태를 제공한다. 세 가지 중 어느 것을 사용해도 내부적으로 UTC 밀리초 단위로 변환해 저장한다.
    * 문자열이 포함된 날짜 형식: "2018-04-20", "2018/04.20", "2018/04/20 10:50:00"
    * ISO_INSTANT 포맷의 날짜 형식: "2018-04-10T10:50:00Z"
    * 밀리초: 1524449145579

다음은 컬럼에 Date 타입을 사용하는 예다.

```http
PUT movie_text/_mapping/_doc
{
	"properties": {
		"date": {
			"type": "date", "format": "yyyy-MM-dd HH:mm:ss"
		}
	}
}
```



### 3.3.6 Range 데이터 타입

* Range 데이터 타입은 범위가 있는 데이터를 저장할 때 사용하는 데이터 타입이다. 

* 만약 데이터의 범위가 10~20의 정수라면 10, 11, 12 ... 20까지의 숫자를 일일이 지정하는 것이 아니라 데이터의 시작과 끝만 정의하면 된다.

다음과 같이 숫자뿐 아니라 IP에 대한 범위도 Range 데이터 타입으로 정의할 수 있다.

| 데이타 타입   | 설명                                               |
| :------------ | :------------------------------------------------- |
| integer_range | 최소값과 최대값을 갖는 부호있는 32비트 정수 범위   |
| float_range   | 부동 소수점 값을 갖는 32비트 실수 범위             |
| long_range    | 최소값과 최대값을 갖는 부호있는 64비트 정수 범위   |
| double_range  | 부동 소수점 값을 갖는 64비트 실수 범위             |
| date_range    | 64비트 정수 형태의 밀리초로 표시되는 날짜값의 범위 |
| ip_range      | IPv4, IPv6 주소를 지원하는 IP값                    |

Range 데이터 타입을 사용해 개봉일과 종료일까지를 표시해 보자, 다음과 같이 필드를 date_range 타입으로 정의한다.

```http
PUT movie_search_datatype/_mapping/_doc
{
  "properties": {
    "showRange": {
      "type": "date_range"
    }
  }
}
```

데이터를 입력할 때 showRange 컬럼에 다음과 같이 시작값과 종료값의 범위를 지정해 줄 수 있다.

```http
PUT movie_search_datatype/_doc/1
{
  "showRange": {
    "gte": "2001-01-01",
    "lte": "2001-12-31"
  }
}
```



### 3.3.7 Boolean 데이터 타입

* Boolean 데이터 타입은 참과 거짓이라는 두 논리값을 가지는 데이터 타입이다. 참과 거짓 값을 문자열로 표현하는 것도 가능하다.

| 데이타 타입 | 설명           |
| :---------- | :------------- |
| 참          | true, "true"   |
| 거짓        | false, "false" |

다음은 컬럼에 Boolean 데이터 타입을 사용하는 예다.

```http
PUT movie_text/_mapping/_doc
{
  "properties": {
    "check": {
      "type": "boolean"
    }
  }
}
```

#### 

### 3.3.8 Geo-Point 데이터 타입

* 위도, 경도 등 위치정보를 담은 데이터를 저장할 때 Geo-Point 데이터 타입을 사용할 수 있다.

다음은 영화 촬영장소 정보를 저장하기 위해 Geo-Point 데이터 타입을 사용한 예다.

```http
PUT movie_search_datatype/_mapping/_doc
{
  "properties": {
    "filmLocation": {
      "type": "geo_point"
    }
  }
}
```

데이터를 색인할 때 위도와 경도값을 직접 지정하면 된다.

```http
PUT movie_search_datatype/_doc/1
{
  "title": "해리포터와 마법사의 돌",
  "filmLocation": {
    "lat": 55.4155828,
    "lon": -1.7081091
  }
}
```

#### 

#### 3.3.9 IP 데이터 타입

* IP 주소와 같은 데이터를 저장하는 데 사용한다. IPv4나 IPv6를 모두 지정할 수 있다.

```http
PUT movie_search_datatype/_mapping/_doc
{
  "properties": {
    "ipAddr": {
      "type": "ip"
    }
  }
}

```

데이터를 저장할 때 실제 IP 주소를 지정한다.

```http
PUT movie_search_datatype/_doc/2
{
  "ipAddr": "127.0.0.1"
}
```

#### 

#### 3.3.10 Object 데이터 타입

* JSON 포맷의 문서는 내부 객체를 계층적으로 포함할 수 있다. 
* 문서의 필드는 단순히 값을 가질 수도 있지만 복잡한 형태의 또 다른 문서를 포함하는 것도 가능하다.
* 이 처럼 값으로 문서를 가지는 필드의 데이터 타입을 Object 데이터 타입이라고 한다.
* Object 데이터 타입을 정의할 때는 다른 데이터 타입과 같이 특정 키워드를 이용하지 않는다. 단지 필드값으로 다른 문서의 구조를 입력하면 된다.

companies라는 컬럼은 또 다른 문서 정의를 값으로 가진다.

```http
PUT movie_search_datatype/_mapping/_doc
{
  "properties": {
    "companies": {
      "properties": {
        "companyName": {
          "type": "text"
        }
      }
    }
  }
}  
```

데이터를 입력할 때는 문서의 계층 구조에 따라 데이터를 입력해야 한다.

```http
PUT movie_search_datatype/_doc/3
{
  "title": "해리포터와 마법사의 돌",
  "companies": {
    "companyName": "워너브라더스"
  }
}
```

#### 

### 3.3.11 Nested 데이터 타입

* Nested 데이터 타입은 Object 객체 배열을 독립적으로 색인하고 질의하는 형태의 데이터 타입이다.
* 앞서 살펴본 바와 같이 특정 필드 내에 Object 형식으로 JSON 포맷을 표현할 수 있다.
* 그리고 필드에 객체가 배열 형태로도 저장될 수 있다.

```http
PUT movie_search_datatype/_doc/6
{
  "title": "해리포터와 마법사의 돌",
  "companies": [
    {
      "companyName": "워너브라더스"
    }, {
      "companyName": "Heyday Films"
    }
  ]
}
```

`데이터가 배열 형태로 저장되면 한 필드 내의 검색은 기본적으로 OR 조건으로 검색된다.`
이러한 특성탓에 저장되는 데이터의 구조가 조금만 복잡해지면 모호한 상황이 일어날 수 있다.

예를 들어, 다음과 같은 문서를 생각해보자. companies 컬럼에 2건의 영화사 정보가 배열로 저장된다.

```http
PUT movie_search_datatype/_doc/7
{
  "title": "해리포터와 마법사의 돌",
  "companies": [
    {
      "companyCd": "1",
      "companyName": "워너브라더스"
    }, {
      "companyCd": "2",
      "companyName": "Heyday Films"
    }
  ]
}
```

검색 시 companyName이 "워너브라더스"이고 companyCd가 "1"인 조건에서는 이 문서가 잘 검색된다.
그런데 companyName이 "워너브라더스"이고 companyCd가 "2"인 조건으로 검색하면 어떻게 될까?
아마도 이 문서가 검색결과로 나오지 않길 바랄 것이다.
그 이유는 배열 내부에 2개의 조건을 모두 만족하는 데이터가 존재하지 않기 때문이다.
하지만 우리의 의도와는 달리 위 조건으로 검색하면 이 문서가 검색결과로 출력된다.
그 이유는 companies 필드의 데이터 타입이 Array이기 때문이다.
앞에서 설명한 것처럼 Array 데이터 타입 내부에서의 검색은 모든 데이터를 기준으로 OR 연산이 이뤄진다.

object 타입으로 검색하면 검색이 된다.

```http
POST movie_search_datatype/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "companies.companyName": "워너브라더스"
          }
        },
        {
          "match": {
            "companies.companyCd": "2"
          }
        }
      ]
    }
  }
}
```

이런 문제를 해결하기 위해 nested 데이타 타입이 고안됐다.
이 데이터 타입은 Array 데이터 타입과 어떤 차이가 있는지 예제로 확인해보자.

```http
PUT movie_search_datatype/_mapping/_doc
{
  "properties": {
    "companies_nested": {
      "type": "nested"
    }
  }
}
```

생성된 인덱스에 데이터를 색인한다.

```http
PUT movie_search_datatype/_doc/8
{
  "title": "해리포터와 마법사의 돌",
  "companies_nested": [
    {
      "companyCd": "1",
      "companyName": "워너브라더스"
    }, {
      "companyCd": "2",
      "companyName": "Heyday Films"
    }
  ]
}
```

이전에 문자가 됐던 쿼리를 다시 실행해 보자.

```http
POST movie_search_datatype/_search
{
  "query": {
    "nested": {
      "path": "companies_nested",
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "companies.companyName": "워너브라더스"
              }
            },
            {
              "match": {
                "companies_nested.companyCd": "2"
              }
            }
          ]
        }
      }
    }
  }
}
```

이처럼 Nested 데이터 타입을 이용하면 검색할 때 일치하는 문서만 정확하게 출력할 수 있다.



## 3.4 엘라스틱서치 분석기









