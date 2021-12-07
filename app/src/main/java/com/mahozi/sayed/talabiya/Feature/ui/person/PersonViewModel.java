package com.mahozi.sayed.talabiya.Feature.ui.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;
import com.mahozi.sayed.talabiya.Storage.Person.PersonRepository;
import com.mahozi.sayed.talabiya.Storage.order.OrderRepository;
import com.mahozi.sayed.talabiya.Storage.order.OrderAndPersonSuborder;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {


    private LiveData<List<PersonEntity>> mAllPersonEntities;

    private PersonEntity mSelectedPerson;

    private PersonRepository mPersonRepository;

    private OrderRepository mOrderRepository;


    public PersonViewModel(@NonNull Application application) {
        super(application);

        mPersonRepository = PersonRepository.getInstance();
        mPersonRepository.init(application);


        mOrderRepository = OrderRepository.getInstance();
        mOrderRepository.init(application);

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

        return mOrderRepository.selectPersonInfo(personName);
    }

    public void updateSuborderStatus(String date, int status, long id){

        mOrderRepository.updateSuborderStatus(date, status, id);
    }

    public void updateOrderStatus(String date, int status, long id){

        mOrderRepository.updateOrderStatus(date, status, id);
    }

    public List<OrderAndPersonSuborder>  selectAllPersonInfo(String personName){

        return mOrderRepository.selectAllPersonInfo(personName);
    }
}
