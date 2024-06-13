package edu.bluejack23_2.verky.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.bluejack23_2.verky.data.auth.AuthRepository

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance();

    @Provides
    fun provideAuthRepository(impl : AuthRepository): AuthRepository = impl
}