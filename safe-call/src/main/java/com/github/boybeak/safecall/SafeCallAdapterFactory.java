package com.github.boybeak.safecall;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class SafeCallAdapterFactory extends CallAdapter.Factory {

    private static final String TAG = SafeCallAdapterFactory.class.getSimpleName();

    public static SafeCallAdapterFactory create() {
        return new SafeCallAdapterFactory();
    }

    private SafeCallAdapterFactory(){}

    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);
        if (getRawType(returnType) != SafeCall.class) {
            return null;
        }
        return new SafeCallAdapter<>(innerType, retrofit.callbackExecutor());
    }

}