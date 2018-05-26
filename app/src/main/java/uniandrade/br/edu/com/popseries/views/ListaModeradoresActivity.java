package uniandrade.br.edu.com.popseries.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.ListModAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class ListaModeradoresActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerModeradores;

    private List<Usuario> usuarioList;

    private ListModAdapter listModAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_moderadores);
        Toolbar toolbar = findViewById(R.id.toolbarListModerador);
        setSupportActionBar(toolbar);
        setTitle("Lista Moderadores");
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

        recyclerView = findViewById(R.id.recyclerViewListaModeradores);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        usuarioList = new ArrayList<>();

        listarModeradores();

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerModeradores );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerModeradores );
        Log.i("ValueEventListener", "onStop");
    }

    private void listarModeradores() {
        databaseReference = ConfigFirebase.getFirebase().child("moderadores");

        valueEventListenerModeradores = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listModAdapter = new ListModAdapter(ListaModeradoresActivity.this);
                recyclerView.setAdapter(listModAdapter);
                //Limpar lista
                usuarioList.clear();
                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Usuario amigo = dados.getValue( Usuario.class );
                    usuarioList.add( amigo );
                    if (usuarioList != null) {
                        listModAdapter.adicionarListaModeradores(usuarioList);
                    }
                }
                listModAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
