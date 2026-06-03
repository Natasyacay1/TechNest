package com.example.technest.API;

public interface ApiCallback<T> {
    void onSuccess(T response);
    void onFailure(Throwable t);
}