package app.petsy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDrawerAndNavigation(toolbar);
        setRecyclerView();

    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ms_Pictures);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        MyAdapter myAdapter = new MyAdapter(getListOfPets());
        recyclerView.setAdapter(myAdapter);
    }

    private void setDrawerAndNavigation(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<PetModel> getListOfPets() {
        ArrayList<PetModel> pets = new ArrayList<>();
        String city1 = "Holon";
        String description1 = "description";
        String phoneNumber1 = "123456789";
        int picId1 = R.drawable.picture1;
        PetModel petModel1 = new PetModel(city1, description1, picId1, phoneNumber1);

        String city2 = "Tel Aviv";
        String description2 = "description2";
        String phoneNumber2 = "123456789";
        int picId2 = R.drawable.picture2;
        PetModel petModel2 = new PetModel(city2, description2, picId2, phoneNumber2);

        String city3 = "Beer Sheba";
        String description3 = "description2";
        String phoneNumber3 = "123456789";
        int picId3 = R.drawable.picture3;
        PetModel petModel3 = new PetModel(city3, description3, picId3, phoneNumber3);

        String city4 = "Beer Sheba";
        String description4 = "description2";
        String phoneNumber4 = "123456789";
        int picId4 = R.drawable.picture4;
        PetModel petModel4 = new PetModel(city4, description4, picId4, phoneNumber4);

        String city5 = "Kfar Saba";
        String description5 = "description5";
        String phoneNumber5 = "123456789";
        int picId5 = R.drawable.picture5;
        PetModel petModel5 = new PetModel(city5, description5, picId5, phoneNumber5);

        String city6 = "Kiriyat Shmona";
        String description6 = "description5";
        String phoneNumber6 = "123456789";
        int picId6 = R.drawable.picture6;
        PetModel petModel6 = new PetModel(city6, description6, picId6, phoneNumber6);

        String city7 = "Holon";
        String description7 = "description";
        String phoneNumber7 = "123456789";
        int picId7 = R.drawable.picture7;
        PetModel petModel7 = new PetModel(city7, description7, picId7, phoneNumber7);

        String city9 = "Tel Aviv";
        String description9 = "description2";
        String phoneNumber9 = "123456789";
        int picId9 = R.drawable.picture9;
        PetModel petModel9 = new PetModel(city9, description9, picId9, phoneNumber9);

        String city10 = "Beer Sheba";
        String description10 = "description2";
        String phoneNumber10 = "123456789";
        int picId10 = R.drawable.picture10;
        PetModel petModel10 = new PetModel(city10, description10, picId10, phoneNumber10);

        String city11 = "Beer Sheba";
        String description11 = "description2";
        String phoneNumber11 = "123456789";
        int picId11 = R.drawable.picture11;
        PetModel petModel11 = new PetModel(city11, description11, picId11, phoneNumber11);

        String city12 = "Kfar Saba";
        String description12 = "description5";
        String phoneNumber12 = "123456789";
        int picId12 = R.drawable.picture12;
        PetModel petModel12 = new PetModel(city12, description12, picId12, phoneNumber12);

        String city13 = "Kiriyat Shmona";
        String description13 = "description5";
        String phoneNumber13 = "123456789";
        int picId13 = R.drawable.picture6;
        PetModel petModel13 = new PetModel(city13, description13, picId13, phoneNumber13);

        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel3);
        pets.add(petModel4);
        pets.add(petModel5);
        pets.add(petModel6);
        pets.add(petModel7);
        pets.add(petModel9);
        pets.add(petModel10);
        pets.add(petModel11);
        pets.add(petModel12);
        pets.add(petModel13);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel3);
        pets.add(petModel4);
        pets.add(petModel5);
        pets.add(petModel6);
        pets.add(petModel7);
        pets.add(petModel9);
        pets.add(petModel10);
        pets.add(petModel11);
        pets.add(petModel12);
        pets.add(petModel13);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel3);
        pets.add(petModel4);
        pets.add(petModel5);
        pets.add(petModel6);
        pets.add(petModel7);
        pets.add(petModel9);
        pets.add(petModel10);
        pets.add(petModel11);
        pets.add(petModel12);
        pets.add(petModel13);


        return pets;

    }
}
