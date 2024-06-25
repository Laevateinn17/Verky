package edu.bluejack23_2.verky.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.bluejack23_2.verky.data.chat.ChatRepository
import edu.bluejack23_2.verky.data.chat.ChatRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance();

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase = FirebaseDatabase.getInstance("https://verky-123-default-rtdb.asia-southeast1.firebasedatabase.app");

    @Provides
    @Singleton
    fun provideChatRepository(
        firebaseDatabase: FirebaseDatabase
    ): ChatRepository {
        return ChatRepositoryImpl(firebaseDatabase)
    }
}