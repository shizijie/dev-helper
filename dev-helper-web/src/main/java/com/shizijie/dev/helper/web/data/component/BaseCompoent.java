package com.shizijie.dev.helper.web.data.component;

public interface BaseCompoent {
    String name();

    Type type();

    String get();

    enum Type{
        String,
        Number,
        Date,
        BigDecimal
    }
}
