package com.jollypanda.gitforce.di.module

import android.os.Build
import android.util.Log
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jollypanda.gitforce.BuildConfig
import com.jollypanda.gitforce.data.network.AuthInterceptor
import com.jollypanda.gitforce.data.network.GithubAccountApi
import com.jollypanda.gitforce.data.network.GithubApi
import com.jollypanda.gitforce.data.network.Tls12SocketFactory
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext

/**
 * @author Yamushev Igor
 * @since  11.06.17
 */
@Module
class NetworkModule(val baseUrl: String) {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit.Builder, client: OkHttpClient.Builder, authInterceptor: AuthInterceptor): GithubApi {

        client.interceptors().clear()
        client.networkInterceptors().clear()

        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        client.addInterceptor {
            val original = it.request()

            val changed = original.newBuilder()
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .addHeader("User-Agent", "Gitforce")
                    .method(original.method(), original.body())
                    .build()

            return@addInterceptor it.proceed(changed)
        }

        client.addNetworkInterceptor(authInterceptor)

        return retrofit.client(client.build()).build().create(GithubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builer = GsonBuilder()
                .setExclusionStrategies(object : ExclusionStrategy {
                    override fun shouldSkipClass(clazz: Class<*>?) = false

                    override fun shouldSkipField(f: FieldAttributes?) =
                            f?.declaringClass?.equals(RealmObject::class.java) ?: false
                })
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .registerTypeAdapter(Date::class.java, MyDateTypeAdapter())

        return builer.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)

        return retrofit
    }

    @Provides
    @Singleton
    @Named("accRetrofit")
    fun provideAccRetrofit(gson: Gson): Retrofit.Builder {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://github.com/")

        return retrofit
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient.Builder =
        enableTls12OnPreLollipop(OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS))
                .followRedirects(true)

    @Provides
    @Singleton
    internal fun provideAuthInterceptor() = AuthInterceptor()

    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                val specs = ArrayList<ConnectionSpec>()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)

                client.connectionSpecs(specs)
            } catch (exc: Throwable) {
                Log.e("OkHttp", "Error while setting TLS 1.2", exc)
            }

        }
        return client
    }

    @Provides
    @Singleton
    fun provideAccountApi(@Named("accRetrofit") retrofit: Retrofit.Builder, client: OkHttpClient.Builder, authInterceptor: AuthInterceptor): GithubAccountApi {

        client.interceptors().clear()
        client.networkInterceptors().clear()

        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        client.addInterceptor {
            val original = it.request()

            val changed = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("User-Agent", "Gitforce")
                    .method(original.method(), original.body())
                    .build()

            return@addInterceptor it.proceed(changed)
        }

        client.addNetworkInterceptor(authInterceptor)

        return retrofit.client(client.build()).build().create(GithubAccountApi::class.java)
    }

}