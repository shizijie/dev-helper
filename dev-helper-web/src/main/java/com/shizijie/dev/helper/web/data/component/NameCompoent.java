package com.shizijie.dev.helper.web.data.component;

import com.shizijie.dev.helper.web.data.generator.NameGenerator;
import org.springframework.stereotype.Component;

@Component
public class NameCompoent implements BaseCompoent {
    @Override
    public String name() {
        return "姓名";
    }

    @Override
    public Type type() {
        return Type.String;
    }

    @Override
    public String get() {
        return NameGenerator.generate();
    }
}
