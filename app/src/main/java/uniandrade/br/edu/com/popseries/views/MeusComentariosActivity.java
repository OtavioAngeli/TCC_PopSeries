package uniandrade.br.edu.com.popseries.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.MeusComentariosAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Comentario;

public class MeusComentariosActivity extends AppCompatActivity {

    private ValueEventListener valueEventListenerMeusComentarios;
    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private MeusComentariosAdapter meusComentariosAdapter;
    private Comentario comentario;
    private List<Comentario> comentarioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_comentarios);
        Toolbar toolbar = findViewById(R.id.toolbarMeusComentarios);
        setSupportActionBar(toolbar);
        setTitle("Meus Comentários");
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


        RecyclerView recyclerView = findViewById(R.id.recyclerViewMeusComentarios);
        meusComentariosAdapter = new MeusComentariosAdapter(MeusComentariosActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MeusComentariosActivity.this);

        recyclerView.setAdapter(meusComentariosAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        comentario = new Comentario();
        comentarioList = new ArrayList<>();

        listarComentarios();

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerMeusComentarios );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerMeusComentarios );
        Log.i("ValueEventListener", "onStop");
    }

    private void listarComentarios() {
        Preferencias preferencias = new Preferencias(MeusComentariosActivity.this);

        databaseReference = ConfigFirebase.getFirebase().child("comentarios_usuarios").child( preferencias.getIdentificador() );

        valueEventListenerMeusComentarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                comentarioList.clear();
                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    comentario = dados.getValue( Comentario.class );
                    comentarioList.add( comentario );
                    meusComentariosAdapter.adicionarListaComentarios(comentarioList);
                }
                meusComentariosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
