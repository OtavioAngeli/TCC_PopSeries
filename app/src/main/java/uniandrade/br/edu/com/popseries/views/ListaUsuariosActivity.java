package uniandrade.br.edu.com.popseries.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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
import uniandrade.br.edu.com.popseries.adapter.ListUserAdapter;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class ListaUsuariosActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private List<Usuario> usuarioList;
    private ListUserAdapter listUserAdapter;

    private String userSearch;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        Toolbar toolbar = findViewById(R.id.toolbarUsuarios);
        setSupportActionBar(toolbar);
        setTitle("Lista Usuários");
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

        TextView txtPesquisarUsuario = findViewById(R.id.btnPesquisarUsuario);
        editText = findViewById(R.id.txtPesquisarUsuario);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("usuarios");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPesquisarUser);
        listUserAdapter = new ListUserAdapter(ListaUsuariosActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setAdapter(listUserAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        usuarioList = new ArrayList<>();

        txtPesquisarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSearch = editText.getText().toString();
                if (userSearch.equals("")){
                    Toast.makeText(getApplicationContext(), "Digite o nome do usuário", Toast.LENGTH_SHORT).show();
                } else {
                    closeKeybord();
//                    progressBar.setVisibility(View.VISIBLE);
                    pesquisarUsuario( userSearch );
                }
            }
        });

    }

    private void closeKeybord() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void pesquisarUsuario(String userSearch) {
        usuarioList.clear();
        Query firebaseSearchQuery = mUserDatabase.orderByChild("nome").startAt( userSearch ).endAt( userSearch + "\uf8ff");

        firebaseSearchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario usuario;
                usuario = dataSnapshot.getValue(Usuario.class);
                usuarioList.add(usuario);
                listUserAdapter.adicionarListaUsuarios(usuarioList);
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
