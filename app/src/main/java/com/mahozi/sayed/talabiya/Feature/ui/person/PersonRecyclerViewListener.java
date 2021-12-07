package com.mahozi.sayed.talabiya.Feature.ui.person;

import com.mahozi.sayed.talabiya.Domain.Person.Person;
import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;

public interface PersonRecyclerViewListener {


    void onClick(PersonEntity personEntity);
    void onLongClick(PersonEntity personEntity);
}
