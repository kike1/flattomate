package myapp.flattomate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import myapp.myapp.R;

public class DashboardActivity extends AppCompatActivity {

    private SessionManager manager = new SessionManager();
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String email = manager.getLoginEmailAddress(context);
        if(email != null || !email.equals("")){
            View parentLayout = findViewById(R.id.dashboard);
            Snackbar.make(parentLayout, "Sesión iniciada como "+email, Snackbar.LENGTH_LONG).show();
            Log.d("debug", email);

            Intent intent = new Intent(context, ProfileActivity.class);
            startActivity(intent);
        }

    }
}
