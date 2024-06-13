package edu.bluejack23_2.verky.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import edu.bluejack23_2.verky.data.auth.AuthRepository

@Module
@InstallIn(ActivityComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun provideAuthRepository(impl : AuthRepository): AuthRepository

}