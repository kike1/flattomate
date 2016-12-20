package com.flattomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flattomate.Tabs.CollectionPagerAdapter;
import com.flattomate.Tabs.TabAnnouncementList;
import com.flattomate.Tabs.TabChats;
import com.flattomate.Tabs.TabMyAccount;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    PagerAdapter pagerAdapter;
    TabAnnouncementList tabAnnouncementList = new TabAnnouncementList();
    TabChats tabChats = new TabChats();
    TabMyAccount tabMyAcoount = new TabMyAccount();
    private Context context = this;
    //private SessionManager manager = new SessionManager(context);
    //FlattomateService api;
    SharedPreferences manager;

    //ArrayList<Announcement> announcements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setToolbar(); // Añadir la toolbar
        // Setear adaptador al viewpager.
        setupViewPager(mViewPager);
        // Preparar las pestañas
        tabs.setupWithViewPager(mViewPager);

        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);
        //api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        String email = manager.getString("email", null);

        //if user is not logged then redirect to Home
        if (email == null) {
            //View parentLayout = findViewById(R.id.dashboard);
            // Snackbar.make(parentLayout, "Sesión iniciada como "+email, Snackbar.LENGTH_LONG).show();
            //Log.d("debug", email);

            //now we redirect to user profile following iteration 1

            /*Intent intent = new Intent(context, CreateAnnouncementActivity.class);
            intent.putExtra("idUser", 1);
            intent.putExtra("idAnnouncement", 3);
            startActivity(intent);*/

            //Get announcements and create the adapter list
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Establece la toolbar como action bar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * Crea una instancia del view pager con los datos
     * predeterminados
     *
     * @param viewPager Nueva instancia
     */
    private void setupViewPager(ViewPager viewPager) {
        CollectionPagerAdapter adapter = new CollectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tabAnnouncementList, getString(R.string.title_tab_ad_list));
        adapter.addFragment(tabChats, getString(R.string.title_tab_chats));
        adapter.addFragment(tabMyAcoount, getString(R.string.title_tab_my_account));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //Snackbar.make(, R.string.action_edit, Snackbar.LENGTH_LONG).show();
                Toast.makeText(this, "prueba", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}