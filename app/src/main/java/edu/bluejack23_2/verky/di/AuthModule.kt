package edu.bluejack23_2.verky.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import edu.bluejack23_2.verky.data.auth.AuthRepository
import edu.bluejack23_2.verky.data.auth.AuthRepositoryImpl
import edu.bluejack23_2.verky.data.user.UserRepository
import edu.bluejack23_2.verky.data.user.UserRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesAuthRepo(authRepositoryImpl: AuthRepositoryImpl): AuthRepository {
        return authRepositoryImpl
    }

    @Provides
    @ViewModelScoped
    fun providesUserRepo(userRepositoryImpl: UserRepositoryImpl) : UserRepository{
        return userRepositoryImpl
    }
}
