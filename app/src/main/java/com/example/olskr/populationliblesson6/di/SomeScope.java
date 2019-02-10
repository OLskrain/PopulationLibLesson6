package com.example.olskr.populationliblesson6.di;

import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Documented
@Retention(RUNTIME)
public @interface SomeScope {} //не нужно для проекта
