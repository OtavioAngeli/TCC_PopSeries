package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uniandrade.br.edu.com.popseries.R;

public class AdministradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        Toolbar toolbar = findViewById(R.id.toolbarAdministrador);
        setSupportActionBar(toolbar);
        setTitle("Administrador");
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
        Button btnModeradores = findViewById(R.id.btnModeradores);
        Button btnComentarios = findViewById(R.id.btnComentarios);

        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdministradorActivity.this, "Lista Usuarios", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdministradorActivity.this, ListaUsuariosActivity.class);
                startActivity(intent);
            }
        });

        btnComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdministradorActivity.this, "Lista Comentários", Toast.LENGTH_SHORT).show();
            }
        });

        btnModeradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdministradorActivity.this, "Lista Moderadores", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdministradorActivity.this, ListaModeradoresActivity.class);
                startActivity(intent);
            }
        });


    }
}
