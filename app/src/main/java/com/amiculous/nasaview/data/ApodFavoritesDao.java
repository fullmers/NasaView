package com.amiculous.nasaview.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
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
