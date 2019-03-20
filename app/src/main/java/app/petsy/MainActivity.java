package app.petsy;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements FoundFragment.OnFragmentInteractionListener, LostFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager vpPager = findViewById(R.id.vpPager);
        final MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        Button found = findViewById(R.id.btnFound);
        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpPager.setCurrentItem(0);
            }

        });
        Button lost = findViewById(R.id.btnLost);
        lost.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                vpPager.setCurrentItem(1);            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
