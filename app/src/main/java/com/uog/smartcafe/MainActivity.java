package com.uog.smartcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.uog.smartcafe.Fragment.FavoriteMenuFragment;
import com.uog.smartcafe.Fragment.HomeFragment;
import com.uog.smartcafe.Fragment.MenuCategoryFragment;
import com.uog.smartcafe.Fragment.OrderHistoryFragment;
import com.uog.smartcafe.Fragment.ProfileFragment;
import com.uog.smartcafe.util.CustomApplication;
import com.uog.smartcafe.util.DrawCart;
import com.uog.notification.NotificationActivity;

public class MainActivity extends BaseCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    private int mCount;

    String customerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mCount = ((CustomApplication)getApplication()).cartItemCount();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.content_main, fragment);
        fragmentTransaction.addToBackStack("home");
        fragmentTransaction.commit();

        customerType = getIntent().getStringExtra("customertype");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (id == R.id.nav_menu_category) {
                    fragment = new MenuCategoryFragment();
                } else if (id == R.id.nav_order_history) {
                    fragment = new OrderHistoryFragment();
                } else if (id == R.id.nav_favorites) {
                    fragment = new FavoriteMenuFragment();
                }
                else if (id == R.id.nav_notification) {
                    Intent notificationIntent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                }
                else if (id == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                }
                else if (id == R.id.nav_complaint)
                {
                    Intent complaintActivity = new Intent(MainActivity.this,ComplaintActivity.class);
                    startActivity(complaintActivity);
                }
                else if (id == R.id.nav_logout) {
                    //remove user data from shared preference
                    SharedPreferences mShared = ((CustomApplication)getApplication()).getShared().getInstanceOfSharedPreference();
                    mShared.edit().clear().apply();

                    //Navigate to login page
                    Intent loginPageIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginPageIntent);
                    finish();
                }

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, fragment);
                fragmentTransaction.addToBackStack("frag");
                transaction.commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            MainActivity.this.finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_shop);
        DrawCart dCart = new DrawCart(this);
        menuItem.setIcon(dCart.buildCounterDrawable(mCount, R.drawable.cart));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_shop) {
            Intent checkoutIntent = new Intent(MainActivity.this, CartActivity.class);
            checkoutIntent.putExtra("customertype",customerType);
            startActivity(checkoutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        mCount = ((CustomApplication)getApplication()).cartItemCount();
        super.onResume();
    }
}
