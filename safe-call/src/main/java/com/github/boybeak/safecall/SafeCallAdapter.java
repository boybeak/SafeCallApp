package com.github.boybeak.safecall;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;

class SafeCallAdapter<R> implements CallAdapter<R, SafeCall<R>> {

    private final Type responseType;
    private final Executor executor;

    public SafeCallAdapter(Type responseType, Executor executor) {
        this.responseType = responseType;
        this.executor = executor;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public SafeCall<R> adapt(@NotNull Call<R> call) {
        return new SafeCall<>(call, executor);
    }
}