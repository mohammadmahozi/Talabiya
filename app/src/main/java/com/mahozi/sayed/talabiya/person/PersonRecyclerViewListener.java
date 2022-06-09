package com.mahozi.sayed.talabiya.person;

import com.mahozi.sayed.talabiya.person.store.PersonEntity;

public interface PersonRecyclerViewListener {


    void onClick(PersonEntity personEntity);
    void onLongClick(PersonEntity personEntity);
}
