package com.example.technest.API;

import com.example.technest.Model.Product;
import java.util.List;

public interface ApiCallback {
    void onSuccess(List<Product> products);
    void onError(String errorMessage);
}