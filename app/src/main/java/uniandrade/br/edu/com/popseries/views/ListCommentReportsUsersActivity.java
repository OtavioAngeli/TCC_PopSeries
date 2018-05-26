package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.CommentsReportsAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.model.Comentario;

public class ListCommentReportsUsersActivity extends AppCompatActivity {

    private ValueEventListener valueEventListenerCommentsReports;
    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();

    private String serie_id;
    private CommentsReportsAdapter commentsReportsAdapter;
    private List<Comentario> comentarioList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comment_reports_users);
        Toolbar toolbar = findViewById(R.id.toolbarCommentsReports);
        setSupportActionBar(toolbar);
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

        Intent intent = getIntent();

        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                String serie_name = bundle.getString("original_title");
                serie_id = bundle.getString("serie_id");
                setTitle(serie_name);
            }
        }

        recyclerView = findViewById(R.id.recyclerViewCommentsReports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListCommentReportsUsersActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        comentarioList = new ArrayList<>();
        listarComentarios();
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerCommentsReports );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerCommentsReports );
        Log.i("ValueEventListener", "onStop");
    }

    private void listarComentarios() {
        databaseReference = ConfigFirebase.getFirebase().child("comentarios_reportados").child( serie_id ).child("reportados");

        valueEventListenerCommentsReports = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentsReportsAdapter = new CommentsReportsAdapter(ListCommentReportsUsersActivity.this);
                recyclerView.setAdapter(commentsReportsAdapter);
                //Limpar lista
                comentarioList.clear();

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Comentario comentario = dados.getValue( Comentario.class );
                    comentarioList.add( comentario );
                    if (comentarioList != null){
                        commentsReportsAdapter.adicionarListaComentarios(comentarioList);
                    }
                }

                commentsReportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListCommentReportsUsersActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        };

    }

}
