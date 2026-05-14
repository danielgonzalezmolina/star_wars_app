package com.example.star_wars.di

import android.content.Context
import com.example.star_wars.data.dao.FilmDao
import com.example.star_wars.data.dao.PlanetDao
import com.example.star_wars.data.database.StarWarsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StarWarsDatabase {
        return StarWarsDatabase.getDatabase(context)
    }

    @Provides
    fun providePlanetDao(db: StarWarsDatabase): PlanetDao {
        return db.getPlanetDao()
    }

    @Provides
    fun provideFilmDao(db: StarWarsDatabase): FilmDao {
        return db.getFilmDao()
    }
}