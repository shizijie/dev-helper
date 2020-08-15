package com.shizijie.dev.helper.web.data.component;

import com.shizijie.dev.helper.web.data.generator.IdCardGenerator;

public class IdCardCompoent implements BaseCompoent {
    @Override
    public String name() {
        return "身份证号";
    }

    @Override
    public Type type() {
        return Type.String;
    }

    @Override
    public String get() {
        return IdCardGenerator.generate();
    }
}
