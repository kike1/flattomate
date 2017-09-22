package com.flattomate;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.flattomate.Announcement.CreateAnnouncementActivity;
import com.flattomate.Profile.ProfileFragment;
import com.flattomate.Tabs.CollectionPagerAdapter;
import com.flattomate.Tabs.TabAnnouncementList;
import com.flattomate.Tabs.TabChats;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends ActionBarActivity {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    //PagerAdapter pagerAdapter;
    TabAnnouncementList tabAnnouncementList = new TabAnnouncementList();
    TabChats tabChats = new TabChats();
    ProfileFragment tabMyAccount = new ProfileFragment();
    private Context context = this;
    //private SessionManager manager = new SessionManager(context);
    //FlattomateService api;
    SharedPreferences manager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //ArrayList<Announcement> announcements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setToolbar(); // Añadir la toolbar

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAnnouncementActivity.class);
                startActivity(intent);
            }
        });

        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);
        //api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        String email = manager.getString("email", null);
        int idUser = manager.getInt("id", 0);
        //if user is found then create a ProfileFragment object
        if (idUser != 0) {
            tabMyAccount = ProfileFragment.newInstance(idUser, manager);
        } else
            Log.d("IDUSER", "User is 0");

        //if user is not logged then redirect to Home
        if (email == null || idUser == 0) {
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
        } else {
            // Setear adaptador al viewpager.
            setupViewPager(mViewPager);
            // Preparar las pestañas
            tabs.setupWithViewPager(mViewPager);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        adapter.addFragment(tabMyAccount, getString(R.string.title_tab_my_account));
        adapter.addFragment(tabChats, getString(R.string.title_tab_chats));
        viewPager.setAdapter(adapter);
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
            }
        }

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                /*TabAnnouncementList newAds = new TabAnnouncementList();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id., newAds).commit();*/
                if(getFragmentRefreshListener() != null){
                    getFragmentRefreshListener().onRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(getFragmentRefreshListener() != null){
                    getFragmentRefreshListener().onRefresh(newText);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //Snackbar.make(, R.string.action_edit, Snackbar.LENGTH_LONG).show();
                //Toast.makeText(this, "prueba", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //establish the font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Dashboard Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public interface FragmentRefreshListener{
        void onRefresh(String query);
    }

}