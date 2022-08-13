## Documents 관리



### 18. Indices 생성 및 삭제

#### 생성

```
PUT products
{
  "settings": {
    "number_of_shards": 2,
    "number_of_replicas": 2
  }
}
```

#### 삭제

```
DELETE products
```



### 19. Documents 인덱싱

```
POST prodcts/_doc
{
  "name": "Coffee Maker",
  "price": 64,
  "in_stock": 10
}
```

#### 결과

```
{
  "_index" : "prodcts",
  "_type" : "_doc",
  "_id" : "sRA_l4IBt484IutFSnQZ",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 7,
  "_primary_term" : 1
}
```

아래와 같이 수정을 하는 경우에도 새로 생성이 된다.

```
PUT prodcts/_doc/100
{
  "name": "Toaster",
  "price": 49,
  "in_stock": 10
}
```

이는 actoin.auto_create_index setting에서 존재하지 않는 문서를 새로 추가할 것인지 설정이 된다. (default: true)



### 20. document의 ID로 조회하기

#### 조회

```
GET products/_doc/100
```



### 21. Documents 수정

#### 기존 필드 수정

```
POST products/_update/100
{
  "doc": {
    "in_stock": 3
  }
}
```

없는 필드를 수정하는 경우에도 사용할 수 있다.

```
POST products/_update/100
{
  "doc": {
    "tags": ["electronics"]
  }
}
```



### Documents는 immutable하다.

* Elasticsearch Documents는 immutable하다. 기존 document를 수정할 수 없다.
* 기존 문서를 수정하는 경우는? 사실 수정하지 않고 대체했다.
* Update API는 문서를 update하는 것처럼 보인다. (Create -> delete)
* Update API는 단순하고 네트워크 트래픽을 줄여준다. 
    * 문서가 저장된 샤드 내에 1번의 요청만 발생, 
    * update api가 없다면 2번 요청을 해야 한다. (create -> delete)




### Update API가 어떻게 동작하는가?

* 현재 문서가 조회된다.
* 필드값이 변경된다.
* 기존 문서는 수정된 문서로 대체된다.
* Application 수준에서 동일한 작업을 할 수 있다.



### 22. Script로 수정하기

* ctx는 context의 약어이다.
* _source를 통해 기존 문서에 접근할 수 있다.

```
POST products/_update/100
{
  "script": {
    "source": "ctx._source.in_stock--"
  }
}
```

기존 필드에 값을 대입하려면

```
POST products/_update/100
{
  "script": {
    "source": "ctx._source.in_stock = 10"
  }
}
```

params를 통해 값을 대입받을 수 있다.

```
POST products/_update/100
{
  "script": {
    "source": "ctx._source.in_stock -= params.quantity",
    "params": {
      "quantity": 4
    }
  }
}
```

source에 조건문을 추가하려면

```
POST products/_update/100
{
  "script": {
    "source": """
      if (ctx._source.in_stock == 0) {
        ctx.op = 'noop';
      }
      
      ctx._source.in_stock--;
    """,
    "params": {
      "quantity": 4
    }
  }
}
```

in_stock이 0보다 적으면 result가 noop이고 그렇지 않으면 updated로 표시될 것이다.

혹은 아래와 같이 표현할 수 있다.

```
POST products/_update/100
{
  "script": {
    "source": """
      if (ctx._source.in_stock > 0) {
        ctx._source.in_stock--;
      }
    """,
    "params": {
      "quantity": 4
    }
  }
}
```

또는 in_stock이 1보다 적으면 문서를 delete하게 하는 방법이다.

```
POST products/_update/100
{
  "script": {
    "source": """
      if (ctx._source.in_stock < 1) {
        ctx.op = 'delete';
      }
      
      ctx._source.in_stock--;
    """,
    "params": {
      "quantity": 4
    }
  }
}
```



