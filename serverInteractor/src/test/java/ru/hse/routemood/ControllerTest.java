package ru.hse.routemood;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.routemood.dto.AuthRequest;
import ru.hse.routemood.dto.AuthResponse;
import ru.hse.routemood.dto.GptRequest;
import ru.hse.routemood.dto.ImageLoadResponse;
import ru.hse.routemood.dto.ImageSaveResponse;
import ru.hse.routemood.dto.PageResponse;
import ru.hse.routemood.dto.RateRequest;
import ru.hse.routemood.dto.RatingRequest;
import ru.hse.routemood.dto.RatingResponse;
import ru.hse.routemood.dto.RegisterRequest;
import ru.hse.routemood.dto.UserResponse;
import ru.hse.routemood.models.Route;
import ru.hse.routemood.models.User;

class ControllerTest {

    private final Route emptyRoute = Route.builder().build();
    @Mock
    private RouteMoodServerApi mockApi;
    @Mock
    private SessionManager mockSessionManager;
    private Controller controller;

    @BeforeEach
    void setUp()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        MockitoAnnotations.openMocks(this);

        Constructor<Controller> testControllerConstructor = Controller.class.getDeclaredConstructor(
            RouteMoodServerApi.class, SessionManager.class);
        testControllerConstructor.setAccessible(true);
        controller = testControllerConstructor.newInstance(mockApi, mockSessionManager);

        when(mockSessionManager.getToken()).thenReturn("token");
    }

    @SuppressWarnings("unchecked")
    private <T> ArgumentCaptor<Callback<T>> captureCallback(Call<T> call) {
        ArgumentCaptor<Callback<T>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(call).enqueue(captor.capture());
        return captor;
    }

    @SuppressWarnings("unchecked")
    private <T> Call<T> mockCall() {
        return (Call<T>) mock(Call.class);
    }

    @SuppressWarnings("unchecked")
    private <T> ApiCallback<T> mockApiCallback() {
        return (ApiCallback<T>) mock(ApiCallback.class);
    }

    @Test
    void testGetFictiveRoute_success() {
        Call<Route> call = mockCall();
        Route route = emptyRoute;
        when(mockApi.getFictiveRoute(0.0, 0.0, "Default walk", "token")).thenReturn(call);
        ApiCallback<Route> callback = mockApiCallback();

        controller.getFictiveRoute(callback);
        ArgumentCaptor<Callback<Route>> captor = captureCallback(call);

        captor.getValue().onResponse(call, Response.success(route));
        verify(callback).onSuccess(route);
    }

    @Test
    void testGetFictiveRoute_error() {
        Call<Route> call = mockCall();
        when(mockApi.getFictiveRoute(0.0, 0.0, "Default walk", "token")).thenReturn(call);
        ApiCallback<Route> callback = mockApiCallback();

        controller.getFictiveRoute(callback);
        ArgumentCaptor<Callback<Route>> captor = captureCallback(call);

        Response<Route> errorResponse = Response.error(400,
            ResponseBody.create(MediaType.parse("text/plain"), "Bad Request"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 400: " + errorResponse.message());
    }

    @Test
    void testGetFictiveRoute_failure() {
        Call<Route> call = mockCall();
        when(mockApi.getFictiveRoute(0.0, 0.0, "Default walk", "token")).thenReturn(call);
        ApiCallback<Route> callback = mockApiCallback();

        controller.getFictiveRoute(callback);
        ArgumentCaptor<Callback<Route>> captor = captureCallback(call);

        captor.getValue().onFailure(call, new Throwable("network error"));
        verify(callback).onError("network error");
    }

    @Test
    void testGetRoute_success() {
        Call<Route> call = mockCall();
        GptRequest request = new GptRequest();
        Route route = emptyRoute;
        when(mockApi.getRoute(request, "token")).thenReturn(call);
        ApiCallback<Route> callback = mockApiCallback();

        controller.getRoute(request, callback);
        ArgumentCaptor<Callback<Route>> captor = captureCallback(call);

        captor.getValue().onResponse(call, Response.success(route));
        verify(callback).onSuccess(route);
    }

    @Test
    void testGetRoute_error() {
        Call<Route> call = mockCall();
        GptRequest request = new GptRequest();
        when(mockApi.getRoute(request, "token")).thenReturn(call);
        ApiCallback<Route> callback = mockApiCallback();

        controller.getRoute(request, callback);
        ArgumentCaptor<Callback<Route>> captor = captureCallback(call);

        Response<Route> errorResponse = Response.error(500,
            ResponseBody.create(MediaType.parse("text/plain"), "Server Error"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 500: " + errorResponse.message());
    }

    @Test
    void testGetRoute_failure() {
        Call<Route> call = mockCall();
        GptRequest request = new GptRequest();
        when(mockApi.getRoute(request, "token")).thenReturn(call);
        ApiCallback<Route> callback = mockApiCallback();

        controller.getRoute(request, callback);
        ArgumentCaptor<Callback<Route>> captor = captureCallback(call);

        captor.getValue().onFailure(call, new Throwable("fail"));
        verify(callback).onError("fail");
    }

    @Test
    void testRegisterUser_success() {
        Call<AuthResponse> call = mockCall();
        RegisterRequest req = new RegisterRequest();
        AuthResponse resp = new AuthResponse();
        resp.setToken("tok");
        when(mockApi.registerUser(req)).thenReturn(call);
        ApiCallback<AuthResponse> callback = mockApiCallback();

        controller.registerUser(req, callback);
        ArgumentCaptor<Callback<AuthResponse>> captor = captureCallback(call);

        captor.getValue().onResponse(call, Response.success(resp));
        verify(mockSessionManager).setToken("Bearer tok");
        verify(callback).onSuccess(resp);
    }

    @Test
    void testRegisterUser_error() {
        Call<AuthResponse> call = mockCall();
        RegisterRequest req = new RegisterRequest();
        when(mockApi.registerUser(req)).thenReturn(call);
        ApiCallback<AuthResponse> callback = mockApiCallback();

        controller.registerUser(req, callback);
        ArgumentCaptor<Callback<AuthResponse>> captor = captureCallback(call);

        Response<AuthResponse> errorResponse = Response.error(401,
            ResponseBody.create(MediaType.parse("text/plain"), "Unauthorized"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 401: " + errorResponse.message());
    }

    @Test
    void testRegisterUser_failure() {
        Call<AuthResponse> call = mockCall();
        RegisterRequest req = new RegisterRequest();
        when(mockApi.registerUser(req)).thenReturn(call);
        ApiCallback<AuthResponse> callback = mockApiCallback();

        controller.registerUser(req, callback);
        ArgumentCaptor<Callback<AuthResponse>> captor = captureCallback(call);

        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testLoginUser_success() {
        Call<AuthResponse> call = mockCall();
        AuthRequest req = new AuthRequest();
        AuthResponse resp = new AuthResponse();
        resp.setToken("tk");
        when(mockApi.loginUser(req)).thenReturn(call);
        ApiCallback<AuthResponse> callback = mockApiCallback();

        controller.loginUser(req, callback);
        ArgumentCaptor<Callback<AuthResponse>> captor = captureCallback(call);

        captor.getValue().onResponse(call, Response.success(resp));
        verify(mockSessionManager).setToken("Bearer tk");
        verify(callback).onSuccess(resp);
    }

    @Test
    void testLoginUser_error() {
        Call<AuthResponse> call = mockCall();
        AuthRequest req = new AuthRequest();
        when(mockApi.loginUser(req)).thenReturn(call);
        ApiCallback<AuthResponse> callback = mockApiCallback();

        controller.loginUser(req, callback);
        ArgumentCaptor<Callback<AuthResponse>> captor = captureCallback(call);

        Response<AuthResponse> errorResponse = Response.error(403,
            ResponseBody.create(MediaType.parse("text/plain"), "Forbidden"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 403: " + errorResponse.message());
    }

    @Test
    void testLoginUser_failure() {
        Call<AuthResponse> call = mockCall();
        AuthRequest req = new AuthRequest();
        when(mockApi.loginUser(req)).thenReturn(call);
        ApiCallback<AuthResponse> callback = mockApiCallback();

        controller.loginUser(req, callback);
        ArgumentCaptor<Callback<AuthResponse>> captor = captureCallback(call);

        captor.getValue().onFailure(call, new Throwable("fail"));
        verify(callback).onError("fail");
    }

    @Test
    void testListUsers_success() {
        Call<List<User>> call = mockCall();
        List<User> list = Collections.singletonList(new User());
        when(mockApi.listUsers("token")).thenReturn(call);
        ApiCallback<List<User>> callback = mockApiCallback();

        controller.listUsers(callback);
        ArgumentCaptor<Callback<List<User>>> captor = captureCallback(call);

        captor.getValue().onResponse(call, Response.success(list));
        verify(callback).onSuccess(list);
    }

    @Test
    void testListUsers_error() {
        Call<List<User>> call = mockCall();
        when(mockApi.listUsers("token")).thenReturn(call);
        ApiCallback<List<User>> callback = mockApiCallback();

        controller.listUsers(callback);
        ArgumentCaptor<Callback<List<User>>> captor = captureCallback(call);

        Response<List<User>> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testListUsers_failure() {
        Call<List<User>> call = mockCall();
        when(mockApi.listUsers("token")).thenReturn(call);
        ApiCallback<List<User>> callback = mockApiCallback();

        controller.listUsers(callback);
        ArgumentCaptor<Callback<List<User>>> captor = captureCallback(call);

        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testGetUserInfo_success() {
        Call<UserResponse> call = mockCall();
        UserResponse resp = new UserResponse();
        when(mockApi.getUserInfo("user", "token")).thenReturn(call);
        ApiCallback<UserResponse> callback = mockApiCallback();

        controller.getUserInfo("user", callback);
        ArgumentCaptor<Callback<UserResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(resp));
        verify(callback).onSuccess(resp);
    }

    @Test
    void testGetUserInfo_error() {
        Call<UserResponse> call = mockCall();
        when(mockApi.getUserInfo("user", "token")).thenReturn(call);
        ApiCallback<UserResponse> callback = mockApiCallback();

        controller.getUserInfo("user", callback);
        ArgumentCaptor<Callback<UserResponse>> captor = captureCallback(call);
        Response<UserResponse> errorResponse = Response.error(500,
            ResponseBody.create(MediaType.parse("text/plain"), "Err"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 500: " + errorResponse.message());
    }

    @Test
    void testGetUserInfo_failure() {
        Call<UserResponse> call = mockCall();
        when(mockApi.getUserInfo("user", "token")).thenReturn(call);
        ApiCallback<UserResponse> callback = mockApiCallback();

        controller.getUserInfo("user", callback);
        ArgumentCaptor<Callback<UserResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("fail"));
        verify(callback).onError("fail");
    }

    @Test
    void testUpdateAvatar_success() {
        MultipartBody.Part part = mock(MultipartBody.Part.class);
        Call<UUID> call = mockCall();
        UUID id = UUID.randomUUID();
        when(mockApi.updateAvatar(part, "token")).thenReturn(call);
        ApiCallback<UUID> callback = mockApiCallback();

        controller.updateAvatar(part, callback);
        ArgumentCaptor<Callback<UUID>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(id));
        verify(callback).onSuccess(id);
    }

    @Test
    void testUpdateAvatar_error() {
        MultipartBody.Part part = mock(MultipartBody.Part.class);
        Call<UUID> call = mockCall();
        when(mockApi.updateAvatar(part, "token")).thenReturn(call);
        ApiCallback<UUID> callback = mockApiCallback();

        controller.updateAvatar(part, callback);
        ArgumentCaptor<Callback<UUID>> captor = captureCallback(call);
        Response<UUID> errorResponse = Response.error(400,
            ResponseBody.create(MediaType.parse("text/plain"), "Bad"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 400: " + errorResponse.message());
    }

    @Test
    void testUpdateAvatar_failure() {
        MultipartBody.Part part = mock(MultipartBody.Part.class);
        Call<UUID> call = mockCall();
        when(mockApi.updateAvatar(part, "token")).thenReturn(call);
        ApiCallback<UUID> callback = mockApiCallback();

        controller.updateAvatar(part, callback);
        ArgumentCaptor<Callback<UUID>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testSaveRoute_success() {
        RatingRequest req = new RatingRequest();
        RatingResponse resp = new RatingResponse();
        Call<RatingResponse> call = mockCall();
        when(mockApi.saveRoute(req, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.saveRoute(req, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(resp));
        verify(callback).onSuccess(resp);
    }

    @Test
    void testSaveRoute_error() {
        RatingRequest req = new RatingRequest();
        Call<RatingResponse> call = mockCall();
        when(mockApi.saveRoute(req, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.saveRoute(req, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        Response<RatingResponse> errorResponse = Response.error(422,
            ResponseBody.create(MediaType.parse("text/plain"), "Bad data"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 422: " + errorResponse.message());
    }

    @Test
    void testSaveRoute_failure() {
        RatingRequest req = new RatingRequest();
        Call<RatingResponse> call = mockCall();
        when(mockApi.saveRoute(req, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.saveRoute(req, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testDeleteRoute_success() {
        UUID id = UUID.randomUUID();
        Call<Void> call = mockCall();
        when(mockApi.deleteRoute(id, "token")).thenReturn(call);
        ApiCallback<Void> callback = mockApiCallback();

        controller.deleteRoute(id, callback);
        ArgumentCaptor<Callback<Void>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(null));
        verify(callback).onSuccess(null);
    }

    @Test
    void testDeleteRoute_error() {
        UUID id = UUID.randomUUID();
        Call<Void> call = mockCall();
        when(mockApi.deleteRoute(id, "token")).thenReturn(call);
        ApiCallback<Void> callback = mockApiCallback();

        controller.deleteRoute(id, callback);
        ArgumentCaptor<Callback<Void>> captor = captureCallback(call);
        Response<Void> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testDeleteRoute_failure() {
        UUID id = UUID.randomUUID();
        Call<Void> call = mockCall();
        when(mockApi.deleteRoute(id, "token")).thenReturn(call);
        ApiCallback<Void> callback = mockApiCallback();

        controller.deleteRoute(id, callback);
        ArgumentCaptor<Callback<Void>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testAddRate_success() {
        RateRequest req = new RateRequest();
        RatingResponse resp = new RatingResponse();
        Call<RatingResponse> call = mockCall();
        when(mockApi.addRate(req, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.addRate(req, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(resp));
        verify(callback).onSuccess(resp);
    }

    @Test
    void testAddRate_error() {
        RateRequest req = new RateRequest();
        Call<RatingResponse> call = mockCall();
        when(mockApi.addRate(req, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.addRate(req, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        Response<RatingResponse> errorResponse = Response.error(400,
            ResponseBody.create(MediaType.parse("text/plain"), "Bad"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 400: " + errorResponse.message());
    }

    @Test
    void testAddRate_failure() {
        RateRequest req = new RateRequest();
        Call<RatingResponse> call = mockCall();
        when(mockApi.addRate(req, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.addRate(req, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testGetUserRate_success() {
        UUID id = UUID.randomUUID();
        Call<Integer> call = mockCall();
        when(mockApi.getUserRate(id, "user", "token")).thenReturn(call);
        ApiCallback<Integer> callback = mockApiCallback();

        controller.getUserRate(id, "user", callback);
        ArgumentCaptor<Callback<Integer>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(5));
        verify(callback).onSuccess(5);
    }

    @Test
    void testGetUserRate_error() {
        UUID id = UUID.randomUUID();
        Call<Integer> call = mockCall();
        when(mockApi.getUserRate(id, "user", "token")).thenReturn(call);
        ApiCallback<Integer> callback = mockApiCallback();

        controller.getUserRate(id, "user", callback);
        ArgumentCaptor<Callback<Integer>> captor = captureCallback(call);
        Response<Integer> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testGetUserRate_failure() {
        UUID id = UUID.randomUUID();
        Call<Integer> call = mockCall();
        when(mockApi.getUserRate(id, "user", "token")).thenReturn(call);
        ApiCallback<Integer> callback = mockApiCallback();

        controller.getUserRate(id, "user", callback);
        ArgumentCaptor<Callback<Integer>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testGetRatedRouteById_success() {
        UUID id = UUID.randomUUID();
        Call<RatingResponse> call = mockCall();
        RatingResponse response = new RatingResponse();
        when(mockApi.getRouteById(id, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.getRatedRouteById(id, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(response));
        verify(callback).onSuccess(response);
    }

    @Test
    void testGetRatedRouteById_error() {
        UUID id = UUID.randomUUID();
        Call<RatingResponse> call = mockCall();
        when(mockApi.getRouteById(id, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.getRatedRouteById(id, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        Response<RatingResponse> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testGetRatedRouteById_failure() {
        UUID id = UUID.randomUUID();
        Call<RatingResponse> call = mockCall();
        when(mockApi.getRouteById(id, "token")).thenReturn(call);
        ApiCallback<RatingResponse> callback = mockApiCallback();

        controller.getRatedRouteById(id, callback);
        ArgumentCaptor<Callback<RatingResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testGetListRatedRoutesByAuthorUsername_success() {
        Call<List<RatingResponse>> call = mockCall();
        List<RatingResponse> responses = Collections.singletonList(new RatingResponse());
        when(mockApi.getListRatedRoutesByAuthorUsername("user", "token")).thenReturn(call);
        ApiCallback<List<RatingResponse>> callback = mockApiCallback();

        controller.getListRatedRoutesByAuthorUsername("user", callback);
        ArgumentCaptor<Callback<List<RatingResponse>>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(responses));
        verify(callback).onSuccess(responses);
    }

    @Test
    void testGetListRatedRoutesByAuthorUsername_error() {
        Call<List<RatingResponse>> call = mockCall();
        when(mockApi.getListRatedRoutesByAuthorUsername("user", "token")).thenReturn(call);
        ApiCallback<List<RatingResponse>> callback = mockApiCallback();

        controller.getListRatedRoutesByAuthorUsername("user", callback);
        ArgumentCaptor<Callback<List<RatingResponse>>> captor = captureCallback(call);
        Response<List<RatingResponse>> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testGetListRatedRoutesByAuthorUsername_failure() {
        Call<List<RatingResponse>> call = mockCall();
        when(mockApi.getListRatedRoutesByAuthorUsername("user", "token")).thenReturn(call);
        ApiCallback<List<RatingResponse>> callback = mockApiCallback();

        controller.getListRatedRoutesByAuthorUsername("user", callback);
        ArgumentCaptor<Callback<List<RatingResponse>>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testGetFirstPage_success() {
        Call<PageResponse> call = mockCall();
        PageResponse response = new PageResponse();
        when(mockApi.getFirstPage("token")).thenReturn(call);
        ApiCallback<PageResponse> callback = mockApiCallback();

        controller.getFirstPage(callback);
        ArgumentCaptor<Callback<PageResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(response));
        verify(callback).onSuccess(response);
    }

    @Test
    void testGetFirstPage_error() {
        Call<PageResponse> call = mockCall();
        when(mockApi.getFirstPage("token")).thenReturn(call);
        ApiCallback<PageResponse> callback = mockApiCallback();

        controller.getFirstPage(callback);
        ArgumentCaptor<Callback<PageResponse>> captor = captureCallback(call);
        Response<PageResponse> errorResponse = Response.error(500,
            ResponseBody.create(MediaType.parse("text/plain"), "Server Error"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 500: " + errorResponse.message());
    }

    @Test
    void testGetFirstPage_failure() {
        Call<PageResponse> call = mockCall();
        when(mockApi.getFirstPage("token")).thenReturn(call);
        ApiCallback<PageResponse> callback = mockApiCallback();

        controller.getFirstPage(callback);
        ArgumentCaptor<Callback<PageResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testGetNextPage_success() {
        Call<PageResponse> call = mockCall();
        PageResponse response = new PageResponse();
        when(mockApi.getNextPage("nextPageToken", "token")).thenReturn(call);
        ApiCallback<PageResponse> callback = mockApiCallback();

        controller.getNextPage("nextPageToken", callback);
        ArgumentCaptor<Callback<PageResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(response));
        verify(callback).onSuccess(response);
    }

    @Test
    void testGetNextPage_error() {
        Call<PageResponse> call = mockCall();
        when(mockApi.getNextPage("nextPageToken", "token")).thenReturn(call);
        ApiCallback<PageResponse> callback = mockApiCallback();

        controller.getNextPage("nextPageToken", callback);
        ArgumentCaptor<Callback<PageResponse>> captor = captureCallback(call);
        Response<PageResponse> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "No more pages"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testGetNextPage_failure() {
        Call<PageResponse> call = mockCall();
        when(mockApi.getNextPage("nextPageToken", "token")).thenReturn(call);
        ApiCallback<PageResponse> callback = mockApiCallback();

        controller.getNextPage("nextPageToken", callback);
        ArgumentCaptor<Callback<PageResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testListRoutes_success() {
        Call<List<RatingResponse>> call = mockCall();
        List<RatingResponse> responses = Collections.singletonList(new RatingResponse());
        when(mockApi.listRoutes("token")).thenReturn(call);
        ApiCallback<List<RatingResponse>> callback = mockApiCallback();

        controller.listRoutes(callback);
        ArgumentCaptor<Callback<List<RatingResponse>>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(responses));
        verify(callback).onSuccess(responses);
    }

    @Test
    void testListRoutes_error() {
        Call<List<RatingResponse>> call = mockCall();
        when(mockApi.listRoutes("token")).thenReturn(call);
        ApiCallback<List<RatingResponse>> callback = mockApiCallback();

        controller.listRoutes(callback);
        ArgumentCaptor<Callback<List<RatingResponse>>> captor = captureCallback(call);
        Response<List<RatingResponse>> errorResponse = Response.error(500,
            ResponseBody.create(MediaType.parse("text/plain"), "Server Error"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 500: " + errorResponse.message());
    }

    @Test
    void testListRoutes_failure() {
        Call<List<RatingResponse>> call = mockCall();
        when(mockApi.listRoutes("token")).thenReturn(call);
        ApiCallback<List<RatingResponse>> callback = mockApiCallback();

        controller.listRoutes(callback);
        ArgumentCaptor<Callback<List<RatingResponse>>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testSaveImage_success() {
        MultipartBody.Part part = mock(MultipartBody.Part.class);
        Call<ImageSaveResponse> call = mockCall();
        ImageSaveResponse response = new ImageSaveResponse();
        when(mockApi.saveImage(part, "token")).thenReturn(call);
        ApiCallback<ImageSaveResponse> callback = mockApiCallback();

        controller.saveImage(part, callback);
        ArgumentCaptor<Callback<ImageSaveResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(response));
        verify(callback).onSuccess(response);
    }

    @Test
    void testSaveImage_error() {
        MultipartBody.Part part = mock(MultipartBody.Part.class);
        Call<ImageSaveResponse> call = mockCall();
        when(mockApi.saveImage(part, "token")).thenReturn(call);
        ApiCallback<ImageSaveResponse> callback = mockApiCallback();

        controller.saveImage(part, callback);
        ArgumentCaptor<Callback<ImageSaveResponse>> captor = captureCallback(call);
        Response<ImageSaveResponse> errorResponse = Response.error(400,
            ResponseBody.create(MediaType.parse("text/plain"), "Invalid image"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 400: " + errorResponse.message());
    }

    @Test
    void testSaveImage_failure() {
        MultipartBody.Part part = mock(MultipartBody.Part.class);
        Call<ImageSaveResponse> call = mockCall();
        when(mockApi.saveImage(part, "token")).thenReturn(call);
        ApiCallback<ImageSaveResponse> callback = mockApiCallback();

        controller.saveImage(part, callback);
        ArgumentCaptor<Callback<ImageSaveResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testLoadImage_success() {
        UUID id = UUID.randomUUID();
        Call<ImageLoadResponse> call = mockCall();
        ImageLoadResponse response = new ImageLoadResponse();
        when(mockApi.loadImage(id, "token")).thenReturn(call);
        ApiCallback<ImageLoadResponse> callback = mockApiCallback();

        controller.loadImage(id, callback);
        ArgumentCaptor<Callback<ImageLoadResponse>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(response));
        verify(callback).onSuccess(response);
    }

    @Test
    void testLoadImage_error() {
        UUID id = UUID.randomUUID();
        Call<ImageLoadResponse> call = mockCall();
        when(mockApi.loadImage(id, "token")).thenReturn(call);
        ApiCallback<ImageLoadResponse> callback = mockApiCallback();

        controller.loadImage(id, callback);
        ArgumentCaptor<Callback<ImageLoadResponse>> captor = captureCallback(call);
        Response<ImageLoadResponse> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Image not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testLoadImage_failure() {
        UUID id = UUID.randomUUID();
        Call<ImageLoadResponse> call = mockCall();
        when(mockApi.loadImage(id, "token")).thenReturn(call);
        ApiCallback<ImageLoadResponse> callback = mockApiCallback();

        controller.loadImage(id, callback);
        ArgumentCaptor<Callback<ImageLoadResponse>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }

    @Test
    void testDeleteImage_success() {
        UUID id = UUID.randomUUID();
        Call<Void> call = mockCall();
        when(mockApi.deleteImage(id, "token")).thenReturn(call);
        ApiCallback<Void> callback = mockApiCallback();

        controller.deleteImage(id, callback);
        ArgumentCaptor<Callback<Void>> captor = captureCallback(call);
        captor.getValue().onResponse(call, Response.success(null));
        verify(callback).onSuccess(null);
    }

    @Test
    void testDeleteImage_error() {
        UUID id = UUID.randomUUID();
        Call<Void> call = mockCall();
        when(mockApi.deleteImage(id, "token")).thenReturn(call);
        ApiCallback<Void> callback = mockApiCallback();

        controller.deleteImage(id, callback);
        ArgumentCaptor<Callback<Void>> captor = captureCallback(call);
        Response<Void> errorResponse = Response.error(404,
            ResponseBody.create(MediaType.parse("text/plain"), "Image not found"));
        captor.getValue().onResponse(call, errorResponse);
        verify(callback).onError("Error 404: " + errorResponse.message());
    }

    @Test
    void testDeleteImage_failure() {
        UUID id = UUID.randomUUID();
        Call<Void> call = mockCall();
        when(mockApi.deleteImage(id, "token")).thenReturn(call);
        ApiCallback<Void> callback = mockApiCallback();

        controller.deleteImage(id, callback);
        ArgumentCaptor<Callback<Void>> captor = captureCallback(call);
        captor.getValue().onFailure(call, new Throwable("err"));
        verify(callback).onError("err");
    }
}
