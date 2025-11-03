# WAS-Neon
HTTP 요청을 직접 파싱하고 처리하는 미니 웹 애플리케이션 서버를 구현했습니다.
Socket 통신 기반으로 Request/Response 구조, Dispatcher, Handler 등의 핵심 WAS 동작 원리를 직접 설계하며 웹 서버의 내부 구조를 이해하는 데 초점을 맞췄습니다.

## 동작 흐름
![동작흐름](https://mudhub-bucket.s3.ap-northeast-2.amazonaws.com/gist/Screenshot+2025-11-03+at+11.25.48%E2%80%AFAM.png)
## 내부 구조

### WebServer
- 클라이언트 요청을 처리하기 위해 Thread Pool을 생성합니다. 
- ServerSocket 객체가 8080 포트를 리슨하며 대기합니다. 
- 8080 포트로 요청이 들어오면 Socket을 생성하고, Thread Pool에서 하나의 Thread를 할당합니다.

### RequestHandler
- RequestParser, Dispatcher, Handler 등을 통해 HTTP 요청을 전반적으로 처리하고, 최종적으로 HTTP 응답 메시지를 클라이언트에 전달합니다.

### RequestParser
- Socket의 InputStream에 담긴 HTTP 요청 메시지를 파싱하는 역할을 담당합니다. 
- HTTP 요청은 Request Line, Header, Body, Query Params, Cookie로 구분하여 각각 객체로 나눕니다. 
- 나눠진 객체들은 최종적으로 Request 객체로 묶여 관리됩니다.

### Dispatcher
- HTTP 요청에 따라 알맞은 Handler를 할당하는 역할을 담당합니다. 
- HTTP 요청의 Method와 Path를 확인하여, 정적 요청이면 StaticHandler, 동적 요청이면 DynamicHandler를 할당합니다.

### Handler
- HTTP 요청에 맞는 HTTP 응답 메시지를 생성하는 역할을 담당합니다. 
- HTTP Body에 들어갈 HTML 파일은 FileContentUtil을 통해 byte 배열 형태로 읽어옵니다. 
- 응답에는 Status Code, Header, Body, Content Type, Redirect URL, Session ID 정보가 포함됩니다. 
- Response 객체는 ResponseBuilder를 통해 생성되며, 동적으로 매핑해야 하는 데이터는 TemplateEngine을 통해 처리됩니다.

#### StaticHandler
- 요청한 경로의 정적 파일을 찾아 HTTP 응답을 작성합니다.
- 파일이 존재하지 않을 경우, 404 상태 코드가 포함된 Response 객체를 반환합니다.

#### DynamicHandler 
- 요청에 따라 회원가입, 로그인, 로그아웃 등 동적 로직을 실행합니다. 
- 로그인 요청의 경우, 로그인 정보가 담긴 Session 객체를 생성합니다. 
- Session은 싱글톤 패턴으로 구현된 SessionContainer를 통해 관리되어 로그인 상태를 유지합니다.
