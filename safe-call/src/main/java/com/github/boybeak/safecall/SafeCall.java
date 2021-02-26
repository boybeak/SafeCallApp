package com.github.boybeak.safecall;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeCall<R> implements LifecycleEventObserver {

    private static final int STATE_IDLE = 0, STATE_EXECUTING = 1, STATE_END = 2;

    private Call<R> call;

    private OnStart onStart;
    private OnComplete onComplete;
    private OnSuccess<R> onSuccess;
    private OnFailure<R> onFailure;
    private OnCancel onCancel;

    private int state = STATE_IDLE;

    private final Executor executor;

    SafeCall(Call<R> call, Executor executor) {
        this.call = call;
        this.executor = executor;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_STOP) {
            cancel();
        }
    }

    private void cancel() {
        if (!call.isCanceled() && !isEnd()) {
            call.cancel();
        }
        if (onCancel != null) {
            onCancel.onCancel();
            onCancel = null;
        }
        release();
    }

    private void release() {
        onStart = null;
        onSuccess = null;
        onFailure = null;
        onComplete = null;

        call = null;
    }

    public SafeCall<R> onStart(OnStart onStart) {
        this.onStart = onStart;
        return this;
    }

    public SafeCall<R> onSuccess(OnSuccess<R> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public SafeCall<R> onFailure(OnFailure<R> onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    public SafeCall<R> onComplete(OnComplete onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public SafeCall<R> onCancel(OnCancel onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public boolean isExecuting() {
        return state == STATE_EXECUTING;
    }

    private boolean isEnd() {
        return state == STATE_END;
    }

    private void setState(int newState) {
        if (state == newState) {
            return;
        }
        state = newState;
        switch (state) {
            case STATE_IDLE:
                break;
            case STATE_EXECUTING:

                break;
            case STATE_END:
                if (onComplete != null) {
                    onComplete.onComplete();
                    onComplete = null;
                }
                break;
        }
    }

    public void observe(LifecycleOwner owner) {
        if (isExecuting()) {
            throw new IllegalStateException("Can not recall a executing call.");
        }
        setState(STATE_EXECUTING);
        owner.getLifecycle().addObserver(this);
        if (onStart != null) {
            onStart.onStart();
            onStart = null;
        }
        call.enqueue(new Callback<R>() {
            @Override
            public void onResponse(@NotNull Call<R> call, @NotNull Response<R> response) {
                executor.execute(() -> {
                    if (onSuccess != null) {
                        onSuccess.onResponse(call, response);
                        onSuccess = null;
                    }
                    setState(STATE_END);
                });
            }

            @Override
            public void onFailure(@NotNull Call<R> call, @NotNull Throwable t) {
                executor.execute(() -> {
                    if (onFailure != null) {
                        onFailure.onFailure(call, t);
                        onFailure = null;
                    }
                    setState(STATE_END);
                });
            }
        });
    }

    public interface OnStart {
        void onStart();
    }

    public interface OnSuccess<R> {
        void onResponse(@NotNull Call<R> call, @NotNull Response<R> response);
    }

    public interface OnFailure<R> {
        void onFailure(@NotNull Call<R> call, @NotNull Throwable t);
    }

    public interface OnComplete {
        void onComplete();
    }

    public interface OnCancel {
        void onCancel();
    }

}