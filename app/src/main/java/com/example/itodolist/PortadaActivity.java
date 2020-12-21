package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


// Pagina inicial de la aplicacion. Nos permite navegar a la lista de tareas si se ha iniciado sesion previamente, sino
// se navegara a la pantalla de inicio de sesion.
// Tambien nos permite navegar a Entregas aunque no hayamos iniciado sesion
public class PortadaActivity extends AppCompatActivity {


    public static final int RC_SIGN_IN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        MaterialButton button = (MaterialButton) findViewById(R.id.startBtn);
        MaterialButton button2 = (MaterialButton) findViewById(R.id.EntregasBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEntregas();
            }
        });
    }

    public void openMainActivity() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
             //   new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
// Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    public void openEntregas() {
        Intent intent = new Intent(this, Entregas.class);
        startActivity(intent);
        this.finish();
    }

}