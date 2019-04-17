package com.amiculous.nasaview.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ApodFavoritesDao {
    @Query("SELECT * FROM apod_favorites where date like :date")
    LiveData<ApodEntity> loadApod(String date);

    @Query("SELECT * FROM apod_favorites where date like :date")
    boolean hasApod(String date);

    @Query("SELECT * FROM apod_favorites where isFavorite is 1")
    LiveData<List<ApodEntity>> loadAllFavoriteApods();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertApod(ApodEntity apodEntity);

    @Query("UPDATE apod_favorites SET isFavorite = 1 where id like :id")
    void markFavorite(long id);

    @Query("UPDATE apod_favorites SET isFavorite = 0 where id like :id")
    void markNotFavorite(long id);

    @Query("DELETE FROM apod_favorites where id like :id")
    void deleteApod(Long id);

    @Query("DELETE FROM apod_favorites")
    void deleteAll();
}
