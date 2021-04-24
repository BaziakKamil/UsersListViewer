package pl.kamilbaziak.userslistviewer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import pl.kamilbaziak.userslistviewer.repository.UserRepository
import pl.kamilbaziak.userslistviewer.retrofit.UserServiceApi
import pl.kamilbaziak.userslistviewer.utils.UserListViewerApplication
import pl.kamilbaziak.userslistviewer.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideUrl() = Utils.serverName

    @Singleton
    @Provides
    fun provideOkHttpClient() =
        OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, url :String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(url)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): UserServiceApi = retrofit.create(UserServiceApi::class.java)

    @Provides
    fun provideUserRepository(serviceApi: UserServiceApi) = UserRepository(serviceApi)

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideApplication() = UserListViewerApplication()
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope