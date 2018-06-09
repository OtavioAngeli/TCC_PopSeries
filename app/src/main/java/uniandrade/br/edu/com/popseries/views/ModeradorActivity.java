package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import uniandrade.br.edu.com.popseries.R;

public class ModeradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderador);
        Toolbar toolbar = findViewById(R.id.toolbarModerador);
        setSupportActionBar(toolbar);
        setTitle("Moderador");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Ação para voltar pra tela inicial
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnUsuarios = findViewById(R.id.btnUsuarios);
        Button btnComentarios = findViewById(R.id.btnComentarios);

        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModeradorActivity.this, ListaUsuariosActivity.class);
                startActivity(intent);
            }
        });

        btnComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModeradorActivity.this, ListaComentariosReportadosActivity.class);
                startActivity(intent);
            }
        });
    }
}
