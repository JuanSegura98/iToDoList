package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

// Pagina con acceso directos directos a las plataformas de entrega de la etsii
public class Entregas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
    }
    public void moodle (View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://moodle.upm.es/titulaciones/oficiales/login/login.php"));
        startActivity(browserIntent);
    }
    public void aulaweb (View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://aulaweb.etsii.upm.es/"));
        startActivity(browserIntent);
    }
    public void webmail (View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.upm.es/webmail_alumnos/?_task=mail"));
        startActivity(browserIntent);
    }
}