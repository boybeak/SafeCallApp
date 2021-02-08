package com.github.boybeak.safecall;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

public class SafeCallAdapter<R> implements CallAdapter<R, SafeCall<R>> {

    private final Type responseType;

    public SafeCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public SafeCall<R> adapt(Call<R> call) {
        return new SafeCall<>(call);
    }
}