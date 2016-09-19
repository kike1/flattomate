package myapp.flattomate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapp.flattomate.Model.Language;
import myapp.flattomate.Model.User;
import myapp.flattomate.REST.FlattomateService;
import myapp.flattomate.REST.restAPI;
import myapp.myapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    private boolean logged = false;
    Context context;

    SharedPreferences manager;
    User user;
    int idUser;
    FlattomateService api;
    List<Language> languages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = this;
        user = new User();
        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //petición asincrona para loguear al usuario y obtener los datos para guardar sesión
        Call<User> call = api.login(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 401) //login incorrecto
                {
                    onLoginFailed();

                }else if(response.code() == 200) {
                    if(response.isSuccessful()){
                        user = response.body();
                        idUser = user.getId(); //first I need user id for retrieve languages

                        //second call for saving languages
                        Call<List<Language>> callL = api.getLanguages(idUser);
                        callL.enqueue(new Callback<List<Language>>() {
                            @Override
                            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                                if(response.code() == 404)
                                {
                                    Log.d("ERROR", String.valueOf(response.code()));
                                }else if(response.code() == 200) {
                                    languages = response.body();
                                    onLoginSuccess();
                                    //redirect to dashboard
                                    Intent intent = new Intent(context, DashboardActivity.class);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Language>> call, Throwable t) {
                                call.cancel();
                                Toast.makeText(getBaseContext(), "ERROR: "+t.toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }else
                        onLoginFailed();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                Toast.makeText(getBaseContext(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                        if(logged)
                            finish();
                    }
                }, 3000);
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginFailed(){
        //View parentLayout = findViewById(R.id.textView);
        Snackbar.make(findViewById(android.R.id.content), "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG)
                .show();
        Toast.makeText(getBaseContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public void onLoginSuccess() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Sesión iniciada como "+user.getEmail(), Snackbar.LENGTH_LONG);

        snackbar.show();

        _loginButton.setEnabled(true);
        logged = true;

        //set user data
        setUserPreferences(user);
    }

    private void setUserPreferences(User user){
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();

        user.setLanguages(languages); //sets the languages that user speaks

        //save languages in a Set
        Set<String> setLanguages = new HashSet<String>();
        if(languages != null)
            for(Language language : languages){
                Log.d("name: ", language.getName());
                setLanguages.add(language.getName());
            }

        //datos en la sesion del usuario
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putInt("age", user.getAges());
        editor.putString("birthdate", user.getBirthdate());
        editor.putString("avatar", user.getId()+".jpg");
        editor.putString("activity", user.getActivity());
        editor.putString("sex", user.getSex());
        editor.putInt("smoke", user.getSmoke());
        editor.putInt("sociable", user.getSociable());
        editor.putInt("tidy", user.getTidy());
        editor.putString("bio", user.getBio());
        editor.putStringSet("languages", setLanguages);

        editor.apply();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}