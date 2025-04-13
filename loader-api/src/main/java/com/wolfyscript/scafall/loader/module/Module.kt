package com.wolfyscript.scafall.loader.module;

public interface Module<T> {

    void onLoad();

    void onUnload();

    T getBridge();

}
