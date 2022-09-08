package com.example;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

public class RuntimeReflectionRegistrationFeature implements Feature{
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        try {
            RuntimeReflection.register(Hello.class);
            RuntimeReflection.register(Hello.class.getDeclaredConstructor());
            RuntimeReflection.register(Hello.class.getDeclaredMethod("say"));
        } catch (NoSuchMethodException /*| NoSuchFieldException*/ e) {}
    }
}
