package com.shizijie.dev.helper.web.data.component;

import com.shizijie.dev.helper.web.data.generator.PhoneGenerator;

public class PhoneCompoent implements BaseCompoent{

    @Override
    public String name() {
        return "手机号";
    }

    @Override
    public Type type() {
        return Type.String;
    }

    @Override
    public String get() {
        return PhoneGenerator.generate();
    }
}
