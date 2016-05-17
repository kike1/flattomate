package myapp.flattomate;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import myapp.myapp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tx = (TextView)findViewById(R.id.nameApp);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lobster-Regular.ttf");
        tx.setTypeface(custom_font);

        /*String myLoginEmailAddress = getLoginEmailAddress();
        if(myLoginEmailAddress != null || !myLoginEmailAddress.equals("")){
            Snackbar.make(get, "Bienvenido"+myLoginEmailAddress, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else {
            loginInformation.setText("Your login email is missing");
        }*/

    }

    private String getLoginEmailAddress(){
        String storedEmail = "";
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        if(mBundle != null){
            storedEmail = mBundle.getString("EMAIL");
        }
        return storedEmail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
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

    public void loginView(View view) {

        Intent inent = new Intent(this, LoginActivity.class);
        startActivity(inent);
    }

    public void signupView(View view) {

        Intent inent = new Intent(this, RegisterActivity.class);
        startActivity(inent);
    }
}
