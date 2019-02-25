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

    @Query("SELECT * FROM apod_favorites")
    LiveData<List<ApodEntity>> loadAllFavoriteApods();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertApod(ApodEntity apodEntity);

    @Query("DELETE FROM apod_favorites where date like :date")
    void deleteApod(String date);

    @Query("DELETE FROM apod_favorites")
    void deleteAll();
}
