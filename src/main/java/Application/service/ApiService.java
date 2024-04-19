package Application.service;

public interface ApiService<RequestModel, ResponseModel> {
    ResponseModel getData(RequestModel request) throws Exception;
}
