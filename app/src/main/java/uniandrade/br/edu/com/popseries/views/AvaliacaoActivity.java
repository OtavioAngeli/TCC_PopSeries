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
import uniandrade.br.edu.com.popseries.adapter.MinhasAvaliacoesAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Avaliacao;

public class AvaliacaoActivity extends AppCompatActivity {

    private ValueEventListener valueEventListenerMinhasAvaliacoes;
    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();

    private MinhasAvaliacoesAdapter minhasAvaliacoesAdapter;

    private Avaliacao avaliacao;
    private List<Avaliacao> avaliacaoList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);
        Toolbar toolbar = findViewById(R.id.toolbarMinhasAvaliacoes);
        setSupportActionBar(toolbar);
        setTitle("Minhas Avaliações");
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

         recyclerView  = findViewById(R.id.recyclerViewMinhasAvaliacoes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(AvaliacaoActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        avaliacaoList = new ArrayList<>();
        avaliacao = new Avaliacao();

        listarMinhasAvaliacoes();

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener( valueEventListenerMinhasAvaliacoes );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerMinhasAvaliacoes );
        Log.i("ValueEventListener", "onStop");
    }

    private void listarMinhasAvaliacoes() {
        Preferencias preferencias = new Preferencias(AvaliacaoActivity.this);

        databaseReference = ConfigFirebase.getFirebase().child("avaliacao_usuarios").child( preferencias.getIdentificador() );

        valueEventListenerMinhasAvaliacoes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                minhasAvaliacoesAdapter = new MinhasAvaliacoesAdapter(AvaliacaoActivity.this);
                recyclerView.setAdapter(minhasAvaliacoesAdapter);
                //Limpar lista
                avaliacaoList.clear();
                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    avaliacao = dados.getValue( Avaliacao.class );
                    avaliacaoList.add( avaliacao );
                    minhasAvaliacoesAdapter.adicionarListaAvaliacao( avaliacaoList );
                }
                minhasAvaliacoesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}