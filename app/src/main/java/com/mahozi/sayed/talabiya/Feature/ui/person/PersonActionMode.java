package com.mahozi.sayed.talabiya.Feature.ui.person;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantActionMode;
import com.mahozi.sayed.talabiya.R;

public class PersonActionMode implements ActionMode.Callback{


    private PersonActionMode.OnSelectionActionMode mOnSelectionActionMode;

    public PersonActionMode(PersonActionMode.OnSelectionActionMode onSelectionActionMode){

        mOnSelectionActionMode = onSelectionActionMode;

    }

    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.restaurant_action_menu, menu);
        return true;
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_restaurant:

                mOnSelectionActionMode.delete(mode);

                return true;

            case R.id.edit_restaurant:
                mOnSelectionActionMode.edit(mode);
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mode = null;

        mOnSelectionActionMode.finished();
    }


    public interface OnSelectionActionMode{
        void finished();
        void delete(ActionMode actionMode);
        void edit(ActionMode actionMode);
    }
}
