# 04 데이터 검색 

* 문서는 색인 시 설정한 분석기에 의해 분석 과정을 거쳐 토큰을 분리
* 분석기는 색인 시점에 사용할 수도 있지만 검색 시점에 사용하는 것도 가능하다.
* 특정 문장이 검색어로 요청되면 분석기를 통해 분석된 토큰의 일치 여부를 판단해서 그 결과에 점수(score)를 매긴다.
* 이를 기반으로 순서를 적용해 결과를 사용자에게 최종적으로 출력하게 된다.



## 4.1 검색 API

### 4.1.1 검색 질의 표현 방식

* URI 검색
* Request Body 검색

##### URI 검색

HTTP GET 요청을 활용하는 방식으로 파라미터를 'Key=Value'형태로 전달하는 방식이다.

파라미터를 이용한 간단한 URI 검색 쿼리의 예다.

```http
GET movie_search/_search?q=prdtYear:2018
```

##### Request Body 검색

```http
POST movie_search/_search
{
	"query": {
		"term": { "prdtYear": "2018"}
	}
}
```



### 4.1.2 URI 검색

```http
POST movie_search/_search?q=movieNmEn:Family
```

```http
POST movie_search/_search?q=movieNmEn:* AND prdtYear:2017&analyze_wildcard=true&from=0&size=5&sort=_score:desc&_source_includes=movieCd,movieNm,movieNmEn,typeNm
```



### 4.1.3 Request Body 검색

```http
POST movie_search/_search
{
	"query": {
		"query_string": {
			"default_field": "movieNmEn",
			"query": "family"
		}
	}
}
```

```http
POST movie_search/_search
{
	"query": {
		"query_string": {
			"default_field": "movieNmEn",
			"query": "movieNmEn:* OR prdtYear:2017"
		}
	},
	"from": 0,
	"size": 5,
	"sort": [{
	    "_score": {
	      "order": "desc"
	    },
	    "movieCd": {
	      "order": "asc"
	    }
	 }],
	 "_source": [
	   "movieCd",
	   "movieNm",
	   "movieNmEn",
	   "typeNm"
	 ]
}
```



## 4.2 Query DSL 이해하기

Query DSL로 쿼리를 작성하려면 미리 정의된 문법에 따라 JSON 구조를 작성해야 한다.

```http
{
	"size":  // 리턴받는 결과의 개수. 기본값: 10
	"from": // 몇 번째 문서부터 가져올 지정. 기본값: 0
	"timeout": // 검색을 요청해서 결과를 받는데까지 걸리는 시간. 기본값: 무한대
	
	"_source": {} // 검색 시 필요한 필드만 출력하고 싶을 때
	"query": {} // 검색 조건문이 들어가는 공간
	"aggs": {} // 통계 및 집계 데이터를 사용할 때 사용
	"sort": {} // 문서 결과를 어떻게 출력할 지에 대한 조건
}
```

응답 JSON 구조는 아래와 같다.

```http
{
	"took": // 쿼리를 실행한 시간
	"time_out": // 쿼리 시간이 초과할 경우
	"_shards": {
		"total": // 쿼리를 요청한 전체 샤드의 개수
		"sucessful": // 성공적으로 응답한 샤드의 개수
		"failed": // 실패한 샤드의 개수
	},
	
	"hits": {
		"total": // 매칭된 문서의 전체 개수
		"max_score": // 문서의 스코어 값 중 가장 높은 값
		"hits": [] //각 문서 정보와 스코어 값
	}
}
```



### 4.2.2 Query DSL 쿼리와 필터

* 실제 분석기에 의한 전문 분석이 필요한 경우와 단순히 'Yes/No'로 판단할 수 있는 조건 검색의 경우
* 전자를 쿼리(Queries) 컨텍스트라 하고, 후자를 필터(Filter) 컨텍스트라는 용어로 구분한다.



|     | 쿼리 컨텍스트  | 필터 컨텍스트 |
| :-------- | :------ | :-------- |
| 용도 | 전문 검색 시 사용 | 조건 검색 시 사용(예: Yes/No) |
| 특징 | 분석기에 의해 분석이 수행됨<br />연관성 관련 Score를 계산<br />루씬 레벨에서 분석 과정을 거쳐야 하므로 상대적으로 느림 | Yes/No로 단순 판별 가능<br />연관성 관련 계산을 하지 않음<br />엘라스틱서치 레벨에서 처리가 가능하므로 상대적으로 빠름 |
| 사용 예 | "Harry Potter" 같은 문장 분석 | "create_year"필드의 값이 2018년인지 여부<br />"status" 필드에 'use'라는 코드 포함 여부 |



#### 쿼리 컨텍스트

* 문서가 쿼리와 얼마나 유사한지 스코어로 계산한다.
* 질의가 요청될 때마다 엘라스틱서치에서 내부의 루씬을 이용해 계산을 수행한다.(이때 결과가 캐싱되지 않는다)
* 일반적으로 전문 검색에 많이 사용한다.
* 캐싱되지 않고 디스크 연산을 수행하기 때문에 상대적으로 느리다.

다음 예제는 '기묘한 가족'이라는 문장을 대상으로 형태소 분석을 수행하여 movieNm 필드를 검색한다. <u>이때 검색 결과를 얻기 위해 모든 문서의 movieNm 필드 데이터를 분석하는 과정을 거친다.</u>

```http
POST movie_search/_search
{
	"query": {
	  "match": {
	    "movieNm": "기묘한 가족"
	  }
	}
}
```



#### 필터 컨텍스트

* 쿼리의 조건과 문서가 일치하는지(Yes/No)를 구분한다.
* 별도의 스코어를 계산하지 않고 단순 매칭 여부를 검사한다.
* 자주 사용되는 필터의 결과는 엘라스틱서치가 내부적으로 캐싱한다.
* 기본적으로 메모리 연산을 수행하기 때문에 상대적으로 빠르다.

다음 예제에서는 전체 문서 중 repGenreNm 필드의 값이 '다큐멘터리'인 문서만 필터링해서 검색한다. <u>검색하기 전에 필터링 과정을 미리 거치게 되며 검색 대상 문서의 수가 줄어들기 때문에 빠르게 결과를 얻을 수 있다.</u>

```http
POST movie_search/_search
{
	"query": {
	  "bool": {
	    "must": [
	      {
	        "match_all": {}
	      }
	    ],
	    "filter": {
	      "term": {
	        "repGenreNm": "다큐멘터리"
	      }
	    }
	  }
	}
}
```



### 4.2.3 Query DSL의 주요 파라미터

##### Multi Index 검색

기본적으로 모든 검색 요청은 Multi Index 및 Multi Type 검색이 가능하다.

```http
POST movie_search,movie_auto/_search
{
	"query": {
		"term": {
			"repGenreNm": "다큐멘터리"
		}
	}
}
```

검색 요청 시 인덱스 이름을 지정할 때 "*"를 와일드카드로 사용할 수 있다.

```http
POST /logs-2019-*/_search
```



##### 쿼리 결과 페이징

문서의 시작을 나타내기 위해 from 파라미터를 사용하고 문서의 개수를 나타내기 위해 size 파라미터를 사용하면 된다.

```http
# 첫 번째 페이지 요청
POST movie_search/_search
{
	"from": 0,
	"size": 5,
	"query": {
		"term": {
			"repNationNm": "한국"
		}
	}
}
```

두 번째 페이지를 요청하기 위해서는 from값을 조정하면 된다.

```http
# 두 번째 페이지 요청
POST movie_search/_search
{
	"from": 5,
	"size": 5,
	"query": {
		"term": {
			"repNationNm": "한국"
		}
	}
}
```

엘라스틱서치는 관계형 데이터베이스와 다르게 페이징된 해당 문서만 선택적으로 가져오는 것이 아니라 모든 데이터를 읽게 된다. 예를 들어, 예제와 같이 5건씩 페이징한 검색 결과의 2페이지를 요청하더라도 총 10건의 문서를 읽어야 한다. 설정된 페이지를 제공하기 위해서는 전체를 읽어서 사이즈만큼 필터링해서 제공하는 구조이기 때문에 페이지 번호가 높아질수록 쿼리 비용은 덩달아 높아질 수밖에 없다는 점에 주의해야 한다.



##### 쿼리 결과 정렬

```http
POST movie_search/_search
{
	"query": {
		"term": {
			"repNationNm": "한국"
		}
	},
	"sort": {
		"prdtYear": {
			"order": "asc"
		}
	}
}
```

1번째 정렬값이 같은 경우 2번째 값으로 정렬

```http
POST movie_search/_search
{
	"query": {
		"term": {
			"repNationNm": "한국"
		}
	},
	"sort": {
		"prdtYear": {
			"order": "asc"
		},
		"_score": {
		  "order": "desc"
		}
	}
}
```



##### _source 필드 필터링

movieNm 필드만 필터링하여 출력

```http
POST movie_search/_search
{
	"_source": [
		"movieNm"
	],
	"query": {
		"term": {
			"repNationNm": "한국"
		}
	}
}
```



##### 범위 검색

* lt: <
* gt: >
* lte: <=
* gte: >=



2016년부터 2017년까지 조회

```http
POST movie_search/_search
{
	"query": {
		"range": {
			"prdtYear": {
				"gte": "2016",
				"lte": "2017"
			}
		}
	}
}
```



##### operator 설정

엘라스틱서치는 검색 시 문장에 들어올 경우 기본적으로 OR 연산으로 동작한다.

```http
POST movie_search/_search
{
  "query": {
    "match": {
      "movieNm": {
        "query": "자전차왕 엄복동"
        , "operator": "and"
      }
    }
  }
}
```

operator 파라미터를 생략하면 OR 연산으로 동작해서 "저전차왕"이라는 단어 혹은 "엄복동"이라는 단어가 들어있는 모든 문서가 검색될 것이다. 하지만 예제에서는 operator 파라미터를 이용해 "and" 값을 명시했으므로 두 개의 텀이 모두 존재하는 문서만 결과로 제공한다.



##### minimum_should_match 설정

일반적으로 OR 연산을 수행할 경우 검색 결과가 너무 많아질 수 있다. 이 경우 텀의 개수가 몇 개 이상 매칭될 때만 결과로 나오게 할 수 있는데 이때 사용하는 파라미터가 minimum_should_match다.

```http
POST movie_search/_search
{
  "query": {
    "match": {
      "movieNm": {
        "query": "자전차왕 엄복동",
        "minimum_should_match": 2
      }
    }
  }
}
```



##### fuziness 설정

fuziness 파라미터를 사용하면 단순히 같은 값을 찾는 Match Query를 유사한 값을 찾는 Fuzzy Query로 변경할 수 있다. 이는 레벤슈타인(Levenshtein) 편집 거리 알고리즘을 기반으로 문서의 필드 값을 여러 번 변경하는 방식으로 동작한다. 유사한 검색 결과를 찾기 위해 허용 범위의 텀으로 변경해 가며 문서를 찾아 결과로 출력한다.



```http
POST movie_search/_search
{
  "query": {
    "match": {
      "movieNmEn": {
        "query": "Fli",
        "fuzziness": 1
      }
    }
  }
}
```



##### boost 설정

이 파라미터는 관련성이 높은 필드나 키워드에 가중치를 더 줄 수 있게 해준다. 영화 데이터의 경우 한글 영화 제목과 영문 영화 제목으로 두 가지 제목 필드를 제공하고 있다. 이때 한글 영화 제목에 좀 더 가중치를 부여해서 검색 결과를 좀 더 상위로 올리고 싶을 때 사용할 수 있다.

```http
POST movie_search/_search
{
  "query": {
    "multi_match": {
      "query": "Fly",
      "fields": ["movieNm^3", "movieNmEn"]
    }
  }
}
```



## 4.3 Query DSL의 주요 쿼리

### 4.3.1 Match All Query









