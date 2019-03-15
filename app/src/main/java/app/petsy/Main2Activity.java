package app.petsy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = findViewById(R.id.ms_Pictures);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        MyAdapter myAdapter = new MyAdapter(getListOfPets());
        recyclerView.setAdapter(myAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        int picId1 = R.drawable.fight_club;
        PetModel petModel1 = new PetModel(city1, description1,picId1, phoneNumber1);

        String city2 = "Tel Aviv";
        String description2 = "description2";
        String phoneNumber2 = "123456789";
        int picId2 = R.drawable.slaughterofthesoul;
        PetModel petModel2 = new PetModel(city2, description2, picId2, phoneNumber2);



        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel2);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);
        pets.add(petModel1);

        return pets;

    }
}
