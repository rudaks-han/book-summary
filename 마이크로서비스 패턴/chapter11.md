# 11장. 프로덕션 레디 서비스 개발

## 11.1 보안 서비스 개발
 - 애플리케이션은 아래와 같이 4가지 보안 요소를 구현함
    1) 인증 : 접근 권한 (ex. ID/PW, API key 등 자격 증명)
    2) 인가 : Data에 어떤 작업을 요청하여 수행할 수 있는 권한
    3) 감사 : 보안 이슈 탐지 등.
    4) 보안 IPC : 서비스간 통신 보안. TLS(Transport Layer Security)를 두어 경유 하도록 하는게 이상적
 - MSA에서의 보안 구현
    1) API G/W에서 인증/인가 처리
      - 인가 처리는 각 서비스에서 구현하는걸 추천
      - API G/W와 서비스와의 결합도 때문
    2) JWT를 통한 신원/역할 관리
      - 토큰이 탈취 되면 보안상 문제 있음
      - 만료를 짧게 잡아 토큰을 발행해서 해당 문제를 회피 하지만 근본적인 해결책 아님
    3) OAuth
      - OAuth 2.0 
        1) 인증 서버 (Authorization Server) : 액세스/리프레시 토큰 획득 API 제공
        2) 액세스 토큰 (Access Token) : 리소스 서버 접근 허가 토큰
        3) 리프레시 토큰 (Refresh Token) : 액세스 토큰을 얻기 위한 토큰이며, 수명은 길지만 취소 가능 함.
        4) 리소스 서버 (Resource Server) : 서비스
        5) 클라이언트 (Client) : 서비스(리소스 서버)에 접근 하는 클라이언트
      - 액세스/리프레시 2개의 토큰이 발행 됨.
      - 액세스 토큰이 만료되는 경우 리프레시 토큰을 이용하여 새로운 액세스 토큰을 방행함.
    
## 11.2 구성 가능한 서비스 설계
 - 구성값 서비스 전달
    1) Push model : 배포 인프라에서 서비스로 전달
    2) Pull model : 서비스에서 구성서버에 접속하여 읽어 옴.

## 11.3 관측 가능한 서비스 설계
 - 관측 서비스 설계 패턴
    1) 헬스 체크 (health check)
      - 서비스 인스턴스는 자신이 서비스 가능한 상태인지 배포 인프라에 알려야 함. (호출)
      - 서비스 인스턴스는 서비스 가능한 상태인지 헬스 체크 끝점을 구현 해야 함. (구현)
    2) 로그 수집 (log aggregation)
      - 로그를 수집, 검색 함 (ex. elasticsearch, logstash, kibana)
    3) 분삭 추적 (distributed tracing)
      - 서비스별 ID를 부여하여 AOP등을 상용하여 수집한 후 추적 서버에 전달하여 관리 함.
    4) 예외 추적 (exception tracing)
      - 예외를 감시하여 출현 하면 알림을 보내고 로깅함.
    5) 애플리케이션 지표 (application metrics)
      - 정의된 지표를 수집하여 push,pull 방식으로 전달 함.
      - 알려진 오픈 소스로는 prometheus 가 있음. 
    6) 감사 로깅 (audit logging)
      - 사용자 액션을 기록하는 것으로 아래와 같이 3가지 방식이 있음.
        1) 코드에 구현
        2) AOP 사용
        3) event sourcing

## 11.4 서비스 개발: 마이크로서비스 섀시 패턴
 - MSA의 횡단 관심사를 처리 하기 위한 F/W
 - 서비스 매시는 특정 언어 종속성이 없는 표준 기술 기반의 횡단 관심사를 처리 하기 위한 F/W (ex. 이스티오, 링커드, 콘듀이트)

## 11.5 마치며
 - 서비스는 본연의 기능을 충종은 당연하고, 관측 가능 해야 함.
 - 일반 적으로 MSA에서는 API G/W에서 인증,인가를 담당 하며, OAuth2.0은 이를 위한 훌륭한 수단 임.
 - 공통 프로퍼티가 존재 하고, 이를 분산 서비스에게 전달 하기 위해 push/pull 방식으로 프로퍼트를 전달 함.
 - 횡단 관심사에 대해서는 "마이크로서비스 섀시"를 사용하여 비지니스 구현에 집중 할 수 있도록 함.

## Q&A
 1. attic에서 인증과 인가는 어떤 시스템에서 어떻게 구현되어있나요? (441)
    - UAA에서 Manage 되고 있으며, G/W에서 기능을 담당 하고 있음
 
 2. 보안 컨텍스트를 정적 스레드 로컬 변수에 저장한다는게 무슨말인가요? (443)
    - SecurityContextHolder와 Authentication
    - SecurityContext 제공
    - 하나의 Thread에서 Authentication 공유하기 위해서 ThreadLocal 사용
    - Authentication는 Principal과 GrantAuthority 제공
    - Principal은 사용자에 대한 정보
    - GrantAuthority는 권한 정보 (인가 및 권한 확인할 때 사용)
    ```
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       // 사용자 정보
       Object principal = authentication.getPrincipal();
       // 사용자 권한
       Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
       // 인증 여부
       boolean authenticated = authentication.isAuthenticated();
    ```
    
 3. 해시코프 볼트와 AWS 파라미터 스토어 설명 해주세요. (454)
    - AWS 파라미터 : 발급된 key로 그에 mapping 되어 있는 property 값을 리턴해주는 방식 
 4. 지표전달 방식 푸시와 풀 방식 중 어떤게 좋을까요? (470)
    - 책에 나와 있는 예제 지표는 Push 방식이 더 적합해 보인다.
      응용 서비스에서 발생하는 data로 발생 즉시 중앙 서버에 발송 하는것이 좋아 보이며, 중앙 서버에서 취합 및 배치 작업이 있어야 할 것으로 보임.
      반대의 경우(pull) 방식으로 구현 된다면 응용 서비스에서 pull 텀마다 data를 모았다가 넘겨주는 추가 로직이 필요 할 것으로 보임.
 5. 감사로깅은 사용자의 액션을 기록하여 DB에 저장한다고 했는데, 영구적으로 누적되어 저장되는건가요? 그렇다면 데이터가 너무 많아지지않을까요? (472)
    - 서비스별로 보관 주기 설정이 필요 할 것으로 보임.
 
 6. 헬스 체크 끝점 구현에서 jdbc 데이터소스를 사용하는 서비스에는 테스트 쿼리를 실행하는 헬스 체크를, RabiitMQ 메시지 브로커를 쓰는 서비스에는 RabiitMQ 서버가 가동중인지 확인하는 헬스 체크 로직을 자동으로 구성하는 내용(예제)을 보여주세요. 이 장에서 얘기하는 헬스체크, 로그수집, 분산수집, 예외추적 지표, 감사 등을 현재 attic 교육 코드에 적용해서 보여줄 수 있을까요? (461)
 
 7. oauth1과 oauth2의 차이점이 뭔가요? (450)
 
 8. 세션 배수(session draining) 매커니즘에 대해 설명해주세요. (444)
 
 9. 같은 언어와 플랫폼의 시스템이라면 섀시와 메시의 차이가 없을까요? (473)
    - 섀시와 메시는 다른 개념으로 보여 집니다.
      섀시 : 서비스 내에서 구현 해야 하는.
      메시 : 서비스로 제공 되는.
