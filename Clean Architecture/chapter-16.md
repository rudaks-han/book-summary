# 16장 독립성

좋은 아키텍처는 다음을 지원해야 한다.

* 시스템의 유스케이스
* 시스템의 운영
* 시스템의 개발
* 시스템의 배포



## 유스케이스

첫번째 중요항목인 유스케이스의 경우, 시스템의 아키텍처는 시스템의 의도를 지원해야 한다는 뜻이다. 만약 시스템이 장바구니 애플리케이션이라면, 이 아키텍처는 장바구니와 관련된 유스케이스를 지원해야 한다. 실제로 아키텍트의 최우선 관심사는 유스케이스이며, 아키텍처에서도 유스케이스가 최우선이다. 아키텍처는 반드시 유스케이스를 지원해야 한다.



## 운영

시스템이 초당 100,000명의 고객을 처리해야 한다면, 이 요구와 관련된 각 유스케이스에 걸맞은 처리량과 응답시간을 보장해야 한다.



## 개발

아키텍처는 개발환경을 지원하는 데 있어 핵심적인 역할을 수행한다. 

콘웨이 법칙은 아래와 같다.

> 시스템을 설계하는 조직이라면 어디든지 그 조직의 의사소통 구조와 동일한 구조의 설계를 만들어 낼 것이다.

많은 팀으로 구성되어 관심사가 다양한 조직에서 시스템을 개발해야 한다면 각 팀이 독립적으로 행동하기 편한 아키텍처를 반드시 확보하여 개발하는 동안 서로 방해하지 않도록 해야 한다.



## 배포

배포의 목표는 '즉각적인 배포'다. 좋은 아키텍처는 수십개의 작은 설정 스크립트나 속성 파일을 약간씩 수정하는 방식을 사용하지 않는다. 또한 디텍터리나 파일을 수작으로 생성하게 내버려두지 않는다. 좋은 아키텍처는 빌드 후 즉각 배포할 수 있도록 지원해야 한다.



## 선택사항 열어놓기



## 유스케이스 결합 분리



## 결합 분리 모드



## 개발 독립성



## 배포 독립성



## 중복

중복에도 여러가지가 있다. 그중 하나는 진짜 중복이다.

또 다른 중복은 거짓된 또는 우발적인 중복이다.  중복으로 보이는 두 코드 영역이 각자의 경로로 발전한다면, 즉 서로 다른 속도와 다른 이유로 변경된다면 이 두 코드는 진짜 중복이 아니다. 몇년이 지나 다시 보면 두 코드가 매우 다르다는 사실을 알게 될 것이다.



## 결합 분리 모드 (다시)



## 결론



