package com.qoobico.remindme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.qoobico.remindme.R;
import com.qoobico.remindme.adapter.TabsFragmentAdapter;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.util.Constants;

public class MainActivity extends AppCompatActivity   {

    private static final int LAYOUT = R.layout.activity_main;



    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    TextView drawerUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_logout:
                        MyApplication.getInstance().logout();
                        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.news1,
                R.drawable.personal,
                R.drawable.flights,
                R.drawable.crew

        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);


    }



    private void initNavigationView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
       // View header = getLayoutInflater().inflate(R.layout.navigation_header, null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

         drawerUser = (TextView) findViewById(R.id.user_nav);
       // TextView drawerEmail = (TextView) findViewById(R.id.navigation_email);

//        drawerEmail.setText(NavUserEmail);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.actionstaff:
                        viewPager.setCurrentItem(Constants.TAB_ONE);
                        break;
                    case R.id.actionNotificationItem:
                        viewPager.setCurrentItem(Constants.TAB_TWO);
                        break;
                    case R.id.actionFlights:
                        viewPager.setCurrentItem(Constants.TAB_THREE);
                        break;
                    case R.id.actionnotification:
                        viewPager.setCurrentItem(Constants.TAB_FOR);
                        break;
                    case R.id.calendar:
                        Intent anal = new Intent(getApplicationContext(), AnalyticsActivity.class);
                        startActivity(anal);
                        break;
                    case R.id.tools:
                        Toast.makeText(getApplicationContext(),
                                "Инструменты находятся разработке", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_share:
                        Intent goChat = new Intent(getApplicationContext(),ChatRoomsActivity.class);
                        startActivity(goChat);
                        break;

                    case R.id.nav_send:
                        Toast.makeText(getApplicationContext(),
                                "Отправить находится разработке", Toast.LENGTH_SHORT).show();
                        break;


                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

}