# Carrot API 명세서

## 인증 (Auth)

---

### 로그인

사용자 이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받는다.

**POST** `/api/auth/login`

#### Request

**Headers**

| Key          | Value            |
|--------------|------------------|
| Content-Type | application/json |

**Body**

```json
{
  "email": "user@example.com",
  "password": "password123!"
}
```

| 필드     | 타입   | 필수 | 설명        |
|----------|--------|------|-------------|
| email    | String | Y    | 사용자 이메일 |
| password | String | Y    | 비밀번호      |

---

#### Response

**200 OK** — 로그인 성공

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": 3600
}
```

| 필드         | 타입   | 설명                        |
|--------------|--------|-----------------------------|
| accessToken  | String | JWT 액세스 토큰 (유효기간 1시간) |
| refreshToken | String | JWT 리프레시 토큰 (유효기간 7일) |
| expiresAt    | Long   | 액세스 토큰 만료 시간(초)       |

---

#### Error Response

**400 Bad Request** — 필수 파라미터 누락 또는 형식 오류

```json
{
  "code": "INVALID_REQUEST",
  "message": "이메일 또는 비밀번호를 입력해주세요."
}
```

**401 Unauthorized** — 이메일 또는 비밀번호 불일치

```json
{
  "code": "INVALID_CREDENTIALS",
  "message": "이메일 또는 비밀번호가 올바르지 않습니다."
}
```

**404 Not Found** — 존재하지 않는 사용자

```json
{
  "code": "USER_NOT_FOUND",
  "message": "존재하지 않는 사용자입니다."
}
```

**500 Internal Server Error** — 서버 오류

```json
{
  "code": "INTERNAL_SERVER_ERROR",
  "message": "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
}
```

---

### 토큰 재발급

리프레시 토큰으로 새 액세스 토큰을 발급받는다.

**POST** `/api/auth/reissue`

#### Request

**Headers**

| Key           | Value                          |
|---------------|--------------------------------|
| Content-Type  | application/json               |
| Authorization | Bearer {refreshToken}          |

#### Response

**200 OK**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

**401 Unauthorized** — 리프레시 토큰 만료 또는 유효하지 않음

```json
{
  "code": "INVALID_TOKEN",
  "message": "토큰이 만료되었습니다. 다시 로그인해주세요."
}
```

---

### 로그아웃

**POST** `/api/auth/logout`

#### Request

**Headers**

| Key           | Value                 |
|---------------|-----------------------|
| Authorization | Bearer {accessToken}  |

#### Response

**200 OK**

```json
{
  "message": "로그아웃 되었습니다."
}
```

**401 Unauthorized** — 유효하지 않은 토큰

```json
{
  "code": "INVALID_TOKEN",
  "message": "유효하지 않은 토큰입니다."
}
```
