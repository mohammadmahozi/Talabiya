package com.mahozi.sayed.talabiya.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase;
import com.mahozi.sayed.talabiya.person.store.PersonEntity;
import com.mahozi.sayed.talabiya.person.store.PersonRepository;
import com.mahozi.sayed.talabiya.order.store.OrderStore;
import com.mahozi.sayed.talabiya.order.store.OrderAndPersonSuborder;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {


    private LiveData<List<PersonEntity>> mAllPersonEntities;

    private PersonEntity mSelectedPerson;

    private PersonRepository mPersonRepository;

    private OrderStore mOrderStore;


    public PersonViewModel(@NonNull Application application) {
        super(application);

        mPersonRepository = PersonRepository.getInstance();
        mPersonRepository.init(application);


        //mOrderStore = new OrderStore(TalabiyaDatabase.getDatabase(application).orderDao());


        mAllPersonEntities = mPersonRepository.selectAllPeople();
    }

    public PersonEntity getSelectedPerson() {

        return mSelectedPerson;
    }

    public void setSelectedPerson(PersonEntity mSelectedPerson) {
        this.mSelectedPerson = mSelectedPerson;
    }

    public LiveData<List<PersonEntity>> getAllPersonEntities() {
        return mAllPersonEntities;
    }

    public void insertPerson(PersonEntity personEntity){

        mPersonRepository.insertPerson(personEntity);
    }


    public void updatePerson(PersonEntity personEntity){
        mPersonRepository.updatePerson(personEntity);
    }

    public void deletePerson(PersonEntity personEntity){
        mPersonRepository.deletePerson(personEntity);
    }

    public List<OrderAndPersonSuborder> selectPersonInfo(String personName){

        return mOrderStore.selectPersonInfo(personName);
    }

    public void updateSuborderStatus(String date, int status, long id){

        mOrderStore.updateSuborderStatus(date, status, id);
    }

    public void updateOrderStatus(String date, int status, long id){

        mOrderStore.updateOrderStatus(date, status, id);
    }

    public List<OrderAndPersonSuborder>  selectAllPersonInfo(String personName){

        return mOrderStore.selectAllPersonInfo(personName);
    }
}
