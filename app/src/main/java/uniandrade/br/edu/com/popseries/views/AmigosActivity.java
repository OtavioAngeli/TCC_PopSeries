package uniandrade.br.edu.com.popseries.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.AmigosAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Amigo;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class AmigosActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContatos;

    private List<Usuario> usuarioList;

    private AmigosAdapter amigosAdapter;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        Toolbar toolbar = findViewById(R.id.toolbarAmigos);
        setSupportActionBar(toolbar);
        setTitle("Amigos");
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

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAmigos);
        amigosAdapter = new AmigosAdapter(AmigosActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setAdapter(amigosAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        usuario = new Usuario();
        usuarioList = new ArrayList<>();

        listarAmigos();

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerContatos );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerContatos );
        Log.i("ValueEventListener", "onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.searchAmigos);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(AmigosActivity.this, SearchableActivity.class);
                usuarioList.clear();
                Bundle bundle = new Bundle();
                bundle.putString("query", s);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.clearFocus();

        return super.onCreateOptionsMenu(menu);
    }

    private void listarAmigos() {
        Preferencias preferencias = new Preferencias(AmigosActivity.this);
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReference = ConfigFirebase.getFirebase().child("amigos").child(identificadorUsuarioLogado);

        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                usuarioList.clear();
                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Usuario amigo = dados.getValue( Usuario.class );
                    usuarioList.add( amigo );
                    amigosAdapter.adicionarListaUsuarios(usuarioList);
                }
                amigosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

}
