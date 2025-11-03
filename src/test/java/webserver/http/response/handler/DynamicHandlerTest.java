package webserver.http.response.handler;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webserver.http.request.param.BodyParams;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DynamicHandlerTest {

    @BeforeEach
    void setUp() {
        User user = new User("testerId3", "1234", "테스터3", "tester3@examplemail.com");
        Database.addUser(user);
    }

    @Test
    @DisplayName("회원가입 성공하고 루트 URL로 리다이렉트한다.")
    void createUserSuccessTest() {
        //Given
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/create");
        when(request.getRequestLine("method")).thenReturn("POST");
        when(request.getBody())
                .thenReturn(new BodyParams(Map.of("userId", "testerId1",
                        "password", "1234",
                        "name", "테스터1",
                        "email", "tester1@examplemail.com")));

        //When
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        //Then
        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 302 Found");
        assertThat(responseMessage).contains("Location: /");
    }

    @Test
    @DisplayName("GET 요청으로 회원가입 시 회원가입이 실패하면서 400 에러가 발생한다.")
    void createUserFailTest() {
        //Given
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/create");
        when(request.getRequestLine("method")).thenReturn("GET");
        when(request.getBody())
                .thenReturn(new BodyParams(Map.of("userId", "testerId2",
                        "password", "1234",
                        "name", "테스터2",
                        "email", "tester2@examplemail.com")));

        //When
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        //Then
        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 400 Bad Request");
    }

    @Test
    @DisplayName("testerId1과 1234로 로그인하면 로그인을 성공하고 루트 URL로 리다이렉트한다.")
    public void loginRedirectsToRootTest() {
        //Given
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/login");
        when(request.getRequestLine("method")).thenReturn("POST");
        when(request.getBody())
                .thenReturn(new BodyParams(Map.of("userId", "testerId3",
                        "password", "1234")));

        //When
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        //Then
        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 302 Found");
        assertThat(responseMessage).contains("Location: /");
    }

    @Test
    @DisplayName("testerId1과 1234로 로그인하면 로그인 성공하면 응답 쿠키 정보에 session-id가 저장된다.")
    void loginCreatesSessionTest() {
        //Given
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/login");
        when(request.getRequestLine("method")).thenReturn("POST");
        when(request.getBody())
                .thenReturn(new BodyParams(Map.of("userId", "testerId3",
                        "password", "1234")));

        //When
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        //Then
        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("Set-Cookie");
        assertThat(responseMessage).contains("sid=");
    }

    @Test
    @DisplayName("tester3과 5678로 로그인하면 로그인 실패하고 로그인 실패 페이지로 이동한다.")
    void loginFailTest() {
        //Given
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/login");
        when(request.getRequestLine("method")).thenReturn("POST");
        when(request.getBody())
                .thenReturn(new BodyParams(Map.of("userId", "testerId3",
                        "password", "5678")));

        //When
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        //Then
        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 401 Unauthorized");
        assertThat(responseMessage).contains("로그인 실패");
    }
}
