package com.example.olskr.populationliblesson6.di.modules;

import com.example.olskr.populationliblesson6.mvp.model.api.IDataSource;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


import javax.inject.Named;

@Module
public class ApiModule {

    @Named("baseUrlProd")
    @Provides //чтобы дагер понял, что это функция что то нам предоставляет пишем @Provides
    public String baseUrlProduction(){
        return "https://api.github.com/";
    }

    @Named("baseUrlDev")
    @Provides
    public String baseUrlDev(){
        return "https://api.github.com/"; //Different from baseUrlProduction
    }


    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor loggingInterceptor){
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    //(1)
    public IDataSource iDataSource(Gson gson, @Named("baseUrlProd") String baseUrl){
       return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(IDataSource.class);
    }

    @Provides
    //при создании Dagger сам поймет, что если нужен Gson и у него он есть то сам отправит в (1)
    public Gson gson(){ //выташили, чтобы можно было менять конфигурацию
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    @Provides
    //Interceptor - перехватывает запрос и передаем нам, чтобы мы могли что то с ним делать
    public HttpLoggingInterceptor loggingInterceptor(){ //один из готовых - логирует
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //разные уровни логирования
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
