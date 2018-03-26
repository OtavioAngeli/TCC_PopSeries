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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.AmigosAdapter;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class SearchableActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseReference mUserDatabase;
    private List<Usuario> usuarioList;

    private AmigosAdapter amigosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        mToolbar = findViewById(R.id.toolbarSearchable);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserDatabase = FirebaseDatabase.getInstance().getReference("usuarios");

        RecyclerView recyclerView = findViewById(R.id.recyclerSearchable);
        amigosAdapter = new AmigosAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setAdapter(amigosAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        usuarioList = new ArrayList<>();

        handleSearch( getIntent() );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent( intent );
        handleSearch( intent );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.searchAmigos);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                usuarioList.clear();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                filterUsers( s );
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void handleSearch(Intent intent){
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String query = bundle.getString("query");
            filterUsers( query );
        }
    }

    private void filterUsers(String search) {
        setTitle( search );
        usuarioList.clear();
        Query firebaseSearchQuery = mUserDatabase.orderByChild("nome").startAt( search ).endAt( search + "\uf8ff");

        firebaseSearchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario usuario = new Usuario();
                usuario = dataSnapshot.getValue(Usuario.class);
                usuarioList.add(usuario);
                amigosAdapter.adicionarListaUsuarios(usuarioList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
