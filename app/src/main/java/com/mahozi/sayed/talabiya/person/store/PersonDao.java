package com.mahozi.sayed.talabiya.person.store;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(PersonEntity personEntity);

    @Query("SELECT * FROM PersonEntity ORDER BY name")
    LiveData<List<PersonEntity>> selectAll();

    @Update
    void update(PersonEntity personEntity);

    @Delete
    void delete(PersonEntity personEntity);
}
