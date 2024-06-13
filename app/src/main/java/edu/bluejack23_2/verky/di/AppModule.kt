package edu.bluejack23_2.verky.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.bluejack23_2.verky.data.auth.AuthRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance();

//    @Provides
//    fun provideAuthRepository(impl : AuthRepository): AuthRepository = impl
}