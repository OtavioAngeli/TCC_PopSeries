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
import uniandrade.br.edu.com.popseries.adapter.ListaSeriesCommentReportsAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.model.CommentReport;

public class ListaComentariosReportadosActivity extends AppCompatActivity {

    private ValueEventListener valueEventListenerComentariosReportados;
    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();

    private ListaSeriesCommentReportsAdapter listaSeriesCommentReportsAdapter;
    private List<CommentReport> serieList;
    private RecyclerView recyclerView;

    private long numCommentReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comentarios_reportados);
        Toolbar toolbar = findViewById(R.id.toolbarComentariosReportados);
        setSupportActionBar(toolbar);
        setTitle("Comentários Reportados");
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

        recyclerView = findViewById(R.id.recyclerViewComentariosReportados);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListaComentariosReportadosActivity.this);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        serieList = new ArrayList<>();

        listarComentarios();
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerComentariosReportados );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerComentariosReportados );
        Log.i("ValueEventListener", "onStop");
    }

    private void listarComentarios() {
        databaseReference = ConfigFirebase.getFirebase().child("comentarios_reportados");

        valueEventListenerComentariosReportados = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaSeriesCommentReportsAdapter = new ListaSeriesCommentReportsAdapter(ListaComentariosReportadosActivity.this);
                recyclerView.setAdapter(listaSeriesCommentReportsAdapter);
                //Limpar lista
                serieList.clear();
                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    CommentReport serie = dados.getValue( CommentReport.class );
                    numCommentReport = dados.child("reportados").getChildrenCount();
                    if (numCommentReport != 0){
                        if (serie != null) {
                            serie.setNum_comment_reports(numCommentReport);
                        }
                        serieList.add( serie );
                        if (serieList != null){
                            listaSeriesCommentReportsAdapter.adicionarListaComentarios(serieList);
                        }
                    }

                }
                listaSeriesCommentReportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

}
