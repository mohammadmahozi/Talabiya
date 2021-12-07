package com.mahozi.sayed.talabiya.Storage.Person;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mahozi.sayed.talabiya.Storage.TalabiyaDatabase;

import java.util.List;

public class PersonRepository {



    private static volatile PersonRepository mRestaurantRepository;

    private PersonDao mPersonDao;


    private PersonRepository(){



    }

    public static PersonRepository getInstance(){

        if(mRestaurantRepository == null)
            mRestaurantRepository = new PersonRepository();

        return mRestaurantRepository;
    }

    public void init(Application application){
        TalabiyaDatabase talabiyaDatabase = TalabiyaDatabase.getDatabase(application);
        mPersonDao = talabiyaDatabase.personDao();

    }

    public void insertPerson(PersonEntity personEntity){
         mPersonDao.insert(personEntity);
    }

    public LiveData<List<PersonEntity>> selectAllPeople(){
        return mPersonDao.selectAll();
    }

    public void updatePerson(PersonEntity personEntity){
        mPersonDao.update(personEntity);
    }

    public void deletePerson(PersonEntity personEntity){
        mPersonDao.delete(personEntity);
    }
}
