package uniandrade.br.edu.com.popseries.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import uniandrade.br.edu.com.popseries.R;

public class AmigosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAmigos);
        setSupportActionBar(toolbar);

    }
}
