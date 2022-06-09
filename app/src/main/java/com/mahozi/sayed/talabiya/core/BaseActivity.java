package com.mahozi.sayed.talabiya.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mahozi.sayed.talabiya.order.OrderActivity;
import com.mahozi.sayed.talabiya.person.PersonActivity;
import com.mahozi.sayed.talabiya.resturant.view.RestaurantActivity;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.StorageH;

import java.io.File;

public class BaseActivity extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;



    protected void onCreateDrawer() {


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);



        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = findViewById(R.id.drawer_layout);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch(id) {
                    case R.id.order_navigation_menu:
                        Intent intent = new Intent(BaseActivity.this, OrderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        BaseActivity.this.startActivity(intent);

                        break;

                    case R.id.restaurant_navigation_menu:
                        intent = new Intent(BaseActivity.this, RestaurantActivity.class);

                        if (BaseActivity.this.getLocalClassName().equals("Feature.ui.order.OrderActivity")){
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        }

                        else {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        }
                        BaseActivity.this.startActivity(intent);

                        break;


                    case R.id.person_navigation_menu:
                        intent = new Intent(BaseActivity.this, PersonActivity.class);
                        if (BaseActivity.this.getLocalClassName().equals("Feature.ui.order.OrderActivity")){
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        }
                        else {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        }

                        intent.putExtra("","");//to prevent null when starting the activity from somewhere else with extras
                        BaseActivity.this.startActivity(intent);
                        break;

                    case R.id.export_database:
                        new StorageH().backUp(BaseActivity.this);
                        break;


                    default:
                        return true;
                }
                return true;
            }
        });

    }



    static class DBFileProvider extends FileProvider {

        Uri getDatabaseURI(Context c, String dbName) {
            File file = c.getDatabasePath(dbName);
            return getFileUri(c, file);
    }

        private Uri getFileUri(Context context, File file) {
        return getUriForFile(context, "com.android.example.provider", file);
    }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
