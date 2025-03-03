# 설치 방법
### 방법 1 인텔리J 설치
1. https://www.jetbrains.com/ko-kr/idea/download/?section=mac
2. git 프로젝트 clone 
### 방법 2 터미널 활용
```bash
$ cd {설치할 디렉토리}
$ git clone https://github.com/Marc416/nota-ai-assignment-RBACT.git
```
# 실행 방법
### 방법 1 인텔리J 활용
`NotaAiAssignmentRdbacApplication.kt` 의 `main` 함수를 실행
### 방법 2 터미널 활용
```bash
$ cd {git clone한 디렉토리}
$  ./gradlew build
$ java -jar build/libs/nota-ai-assignment-rdbac-0.0.1-SNAPSHOT.jar
```

# API 명세
### 이메일 인증 코드 발송 및 확인
**이메일 인증 코드 발송**  
```
Request
POST /account/verify/email
Body
{
    "email": "abc@def.com"
}

Response 200 OK
{
    "code": "RS_000",
    "message": "성공",
    "data": null
}
```
**이메일 인증 코드 확인**  
현재는 이메일을 실제로 보내지 않고 있습니다. 구현체를 교체하여 변경가능합니다.  
콘솔로그를 확인하여 인증코드를 확인합니다.  
`Verification code sent to foodiy@naver.com: 334506`

**이메일 인증코드를 인증**  
```
Request
POST /account/verify/email/code
Body
{
    "email":"foodiy@naver.com",
    "code":334506
}

Response 200 OK
{
    "code": "RS_000",
    "message": "성공",
    "data": null
}
```
### 회원가입
이메일 인증을 했다는 가정하에 진행됩니다.  
유저의 역할은 `ADMIN, USER` 중 선택 가능합니다.  
해당 부분에 대한 제약 조건은 비지니스 영역이라 생각하여 우선은 모두 가능하다는 전제하에 진행하였습니다.
```
Request
POST /account/signup
Body
{
    "email": "viewer2@abc",
    "password": "1323",
    "tenantKey": "joonhee",
    "role": "ADMIN" # ADMIN, USER 중 선택
}

Response 200 OK
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "id": 121,
        "createdAt": "2025-03-03T15:00:56.520927"
    }
}
```
### 로그인
```
Request
POST /account/signin
Body
{
    "email": "projectOwner@naver.com",
    "password": "1323",
    "tenantKey": "joonhee"
}

Response 200 OK
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOjEsInJvbGUiOiJBRE1JTiIsInRlbmFudEtleSI6Impvb25oZWUiLCJleHAiOjE3NDEwNjY1MzMsImlhdCI6MTc0MDk4MDEzM30.Ebp54O_MtJzXVrGJuQ2ICzp_XFu54M45mmqa9OZeSA4"
    }
}
```
### 비밀번호 재설정 요청 및 갱신
```
Request
PATCH /account/reset/password
Authorization: Bearer {access_token}
Body
{
    "newPassword": "132312"
}

Response 200 OK
{
    "code": "RS_000",
    "message": "성공",
    "data": null
}
```
### 프로젝트 생성, 수정, 삭제
**프로젝트 생성**  
프로젝트를 생성하면서 팀원을 초대할 수 있습니다. 팀원없이 초대를 하려면 `memberRequests`를 빈 배열을 전달합니다.  
```
Request
POST /project
Authorization: Bearer {access_token}
Body
{
    "title":"new title10",
    "memberRequests" :[
        {
            "accountId":2,
            "role": "EDITOR"
        }
    ]   
}

Response 200 OK
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "projectId": 1
    }
}
```

**프로젝트 수정**
```
Request
PUT /project/{projectId}
Authorization: Bearer {access_token}
Body
{
    "title":"new new title"
}

Response
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "projectId": 1
    }
}
```
**프로젝트 삭제**
```
Request
DELETE /project/{projectId}
Authorization: Bearer {access_token}

Response
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "projectId": 1
    }
}
```
### 팀원 초대 및 역할 부여
**멤버 추가(역할 수정)**
```
Request
POST /project/{projectId}/member
Authorization: Bearer {access_token}
Body
{
    "memberRequests" :[
        {
            "accountId":2,
            "role": "VIEWER"
        }
    ]   
}

Response
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "projectId": 1
    }
}
```
**멤버 삭제**
```
Request
DELETE /project/{projectId}/member
Authorization: Bearer {access_token}
Body
{
    "memberIds" :[
        2
    ]   
}

Response
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "projectId": 1
    }
}
```
### 프로젝트 조회
NoOffset 방식으로 페이징 조회를 합니다. 조회시 인증된 사용자만 조회가 가능합니다.  
**첫 페이지 조회**  
```
Request
GET /project?size=5
Authorization: Bearer {access_token}

Response
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "content": [
            {
                "projectId": 46,
                "title": "6",
                "projectOwner": 1
            },
            {
                "projectId": 45,
                "title": "5",
                "projectOwner": 1
            },
            {
                "projectId": 44,
                "title": "4",
                "projectOwner": 1
            },
            {
                "projectId": 43,
                "title": "3",
                "projectOwner": 1
            },
            {
                "projectId": 42,
                "title": "2",
                "projectOwner": 1
            }
        ],
        "nextCursor": "42"
    }
}
```
**다음페이지 조회**
```
Request
GET /project?size=5&cursor=42
Authorization: Bearer {access_token}

Response
{
    "code": "RS_000",
    "message": "성공",
    "data": {
        "content": [
            {
                "projectId": 41,
                "title": "1",
                "projectOwner": 1
            },
            {
                "projectId": 40,
                "title": "new title10",
                "projectOwner": 1
            }
        ],
        "nextCursor": null
    }
}
```

# 권한 체계 참조
유저의 역할은 `ADMIN, USER`로 구분하였습니다.
ADMIN은 모든 권한을 가지고 있으며, USER는 기본권한인 VIEWER 권한을 가지고 있습니다.
프로젝트에 대한 권한으로 ProjectRole 을 정의하였습니다.  
ProectRole은 `PROJECT_OWNER, ADMIN, EDITOR, VIEWER`로 구분하였습니다.  
`ADMIN > PROJECT_OWNER > EDITOR > VIEWER` 순으로 권한이 높아집니다.  

상위 권한은 하위 권한을 가집니다.

각각의 API에 대한 권한은 AOP를 활용하여 구현하였습니다.

# 추가 개발
- Input Validation
- Swagger 추가

---
### 📌 과제 개요

회사 **NotaAI**는 SaaS 기반의 프로젝트 관리 도구를 개발하고 있습니다. 최근 기업 고객의 요구로 인해 **역할(Role) 기반의 접근 제어(RBAC, Role-Based Access Control) 시스템을 강화**해야 합니다.

이에 따라, 다음의 요구 사항을 충족하는 **간단한 계정 및 권한 관리 시스템**을 설계하고 구현해 주세요.

### 📖 시나리오

NotaAI의 서비스에서는 사용자가 프로젝트를 생성하고 팀원을 초대하여 공동으로 작업할 수 있습니다. 하지만 최근 **역할 및 권한 관리가 미흡**하다는 피드백이 많아졌고, 특정 기능에 대한 접근 제한이 필요해졌습니다.

따라서, 사용자는 아래와 같은 역할을 가질 수 있어야 합니다.

1. **Admin**: 조직의 모든 프로젝트와 계정을 관리할 수 있습니다.
2. **Project Owner**: 본인이 생성한 프로젝트에 대해 팀원을 초대하고 역할을 부여할 수 있습니다.
3. **Editor**: 프로젝트의 내용을 수정하고 새로운 작업을 생성할 수 있습니다.
4. **Viewer**: 프로젝트 내용을 조회만 할 수 있습니다.

각 역할에 따라 수행할 수 있는 액션은 다음과 같습니다.

| **액션/역할** | **Admin** | **Project Owner** | **Editor** | **Viewer (default)** |
| --- | --- | --- | --- | --- |
| 프로젝트 생성 | ✅ | ✅ | ❌ | ❌ |
| 팀원 초대 및 역할 부여 | ✅ | ✅ | ❌ | ❌ |
| 프로젝트 수정 | ✅ | ✅ | ✅ | ❌ |
| 프로젝트 조회 | ✅ | ✅ | ✅ | ✅ |
| 프로젝트 삭제 | ✅ | ✅ | ❌ | ❌ |
| 사용자 계정 삭제 | ✅ | ❌ | ❌ | ❌ |
- Admin만이 자신이 속하지 않은 전체 프로젝트 수정, 조회, 삭제 권한을 가집니다.

### 🎯 구현 요구 사항

1. **사용자 인증(Authentication)**
    - 로그인 및 회원가입 기능을 제공합니다.
    - 이메일 인증을 통해 회원가입을 완료해야 합니다. 이메일 인증 코드 발급 및 검증 기능을 구현하세요.
    - 비밀번호 재설정 기능을 제공해야 합니다. 사용자가 이메일을 통해 비밀번호 재설정을 요청할 수 있도록 구현하세요.
2. **역할(Role) 및 권한(Permission) 관리**
    - 역할별로 접근 가능한 API 엔드포인트가 다르게 동작해야 합니다.
    - SaaS 환경에서의 다중 테넌트(Tenant) 지원을 고려한 접근 제어 방안을 포함해야 합니다.
3. **API 설계 및 구현**
    1. 프로젝트 생성, 수정, 삭제
    2. 팀원 초대 및 역할 부여
    3. 프로젝트 조회
    4. 회원가입 (이메일 인증 포함 - mock 방식 가능)
    5. 로그인
    6. 이메일 인증 코드 발송 및 확인
    7. 비밀번호 재설정 요청 및 갱신
4. **테스트 코드 작성**
    - 주요 API에 대해 단위 테스트 및 간단한 통합 테스트를 작성해 주세요.

### 🚀 제출 방식

- Github Repository에 코드를 업로드한 후, 링크를 공유해 주세요.
- `README.md` 파일을 작성하여 **설치 방법, 실행 방법 및 API 명세**를 포함해 주세요.
- RESTful API를 구현할 경우, `Swagger` 또는 `Postman Collection`을 제공하면 가산점이 부여됩니다.

### 💡 추가 고려 사항 (선택 사항)

- 데이터베이스는 **PostgreSQL, MySQL, MongoDB** 중 자유롭게 선택해 주세요.
- 프레임워크는 Python(FastAPI)를 활용해 주세요.
- 추가적인 보안 조치 (예: rate limiting, input validation, logging 등)를 적용하면 가산점이 부여됩니다.

---

이 과제는 ‘**실제 서비스에서 권한 관리가 어떻게 적용되는지’**를 고민할 기회를 제공합니다. 단순한 기능 구현 뿐만 아니라 보안과 확장성까지 고려하여 설계해 주세요.