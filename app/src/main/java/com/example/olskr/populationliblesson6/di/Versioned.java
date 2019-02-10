package com.example.olskr.populationliblesson6.di;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
//для проекта не очень нужно
public @interface Versioned { //своя реализация @Named (это колифаер)

    int value() default 0;
}
