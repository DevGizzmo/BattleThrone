package net.gizzmo.battlethrone.database;

public interface Callback<T> {
    void onSuccess(T var1);

    default void onFailure(Throwable cause) {
    }
}
