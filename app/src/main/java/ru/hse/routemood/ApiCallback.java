package ru.hse.routemood;

public interface ApiCallback<T> {
    void onSuccess(T result);

    void onError(String error);
}
