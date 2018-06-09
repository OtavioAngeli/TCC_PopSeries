package uniandrade.br.edu.com.popseries.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.SimilarSerieAdapter;
import uniandrade.br.edu.com.popseries.api.Client;
import uniandrade.br.edu.com.popseries.api.SeriesResults;
import uniandrade.br.edu.com.popseries.api.Service;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.helper.SeriesDbHelper;
import uniandrade.br.edu.com.popseries.model.Avaliacao;
import uniandrade.br.edu.com.popseries.model.Date;
import uniandrade.br.edu.com.popseries.model.Serie;

public class DetalhesActivity extends AppCompatActivity {

    public static String API_KEY = "042df6719b1c27335641d1d7a9e2e66e";
    public static String LANGUAGE = "pt-BR";

    private TextView nameOfSerie, txtDataLancamento, txtSinopse, userRating, txtApiRate, txtUserRate, txtAppRate;
    ImageView imgThumbnail;
    Button btnAvaliar, btnAdicionar;

    LinearLayoutCompat linLayComent;

    Bundle bundle;
    private int serie_id;
    private String dataLancamento;

    private SimilarSerieAdapter similarSerieAdapter;
    private ProgressBar progressBar;

    // DIALOG VARIAVEIS
    private ProgressBar progressBarAdd;
    private Dialog myDialog;
    private TextView txtClosePopup;
    private Button btnFavoritos, btnAssistidos, btnQueroAssistir;
    private RatingBar userAvaliacao, allAvaliation;

    private SeriesDbHelper db;
    private String userID;

    private TextView numTotalComentarios;
    private long numComentarios;
    private long numTotalAvaliacoes = 0;
    private float avaliacaoFinal = 0;
    private float somaAvalicao = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Toolbar toolbar = findViewById(R.id.toolbarDetalhes);
        toolbar.setTitle("");
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

        db = new SeriesDbHelper(DetalhesActivity.this);
        Preferencias preferencias = new Preferencias(DetalhesActivity.this);
        userID = preferencias.getIdentificador();

        initCollapsingToolbar();

        progressBar = findViewById(R.id.progressBarSeriesRelacionadas);

        linLayComent    = findViewById(R.id.linLayComent);
        imgThumbnail    = findViewById(R.id.thumbnailImageHeader);
        nameOfSerie     = findViewById(R.id.txtTitle);
        txtSinopse      = findViewById(R.id.txtSinopse);
        txtApiRate      = findViewById(R.id.apiRate);
        btnAvaliar      = findViewById(R.id.btnAvaliar);
        btnAdicionar    = findViewById(R.id.btnAdicionar);
        txtAppRate      = findViewById(R.id.appRate);
//        userRating          = findViewById(R.id.userrating);
        txtDataLancamento = findViewById(R.id.txtDataLancamento);
        numTotalComentarios = findViewById(R.id.txtNumTotalComent);
        allAvaliation = findViewById(R.id.ratingBarAllAvaliation);

        Intent intent = getIntent();

        if (intent != null){
            bundle = intent.getExtras();
            if (bundle != null){

                serie_id = bundle.getInt("serie_id");
                dataLancamento = bundle.getString("data_lancamento");

                Picasso.with(this)
                        .load(bundle.getString("poster"))
                        .into(imgThumbnail);

                nameOfSerie.setText(bundle.getString("original_title"));
                txtSinopse.setText(bundle.getString("overview"));
                txtApiRate.setText(bundle.getString("apiRate"));
            }
        }

        Date date = new Date();
        date.setData(dataLancamento);
        String data = date.getConvertedDate();
        txtDataLancamento.setText(data);

        // BOTÃO AVALIAR
        btnAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogAvaliar();
                Toast.makeText(getApplicationContext(), "Avaliar", Toast.LENGTH_SHORT).show();
            }
        });

        // BOTÃO ADICIONAR
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialog();
            }
        });

        // EVENTO DE CLICK NO LAYOUT DE COMENTÁRIOS
        linLayComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesActivity.this, ComentariosActivity.class);
                intent.putExtras( bundle );
                startActivity(intent);
            }
        });

        contarComentarios();
        exibirAvaliacao();

        similarSerieAdapter = new SimilarSerieAdapter(DetalhesActivity.this);

        initView();

    }

    private void abrirDialogAvaliar(){
        // DIALOG
        myDialog = new Dialog(DetalhesActivity.this);
        myDialog.setContentView(R.layout.custom_popup_avaliar);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // VARIAVEIS
        txtClosePopup = myDialog.findViewById(R.id.txtClosePopupAvaliar);
        userAvaliacao = myDialog.findViewById(R.id.ratingBarAvaliacao);
        Button buttonConfirmar = myDialog.findViewById(R.id.btnConfirmarAvaliacao);
        // AÇÔES DE CLICK
        txtClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                somaAvalicao = 0;
                salvarAvaliacao( userAvaliacao.getRating() );
            }
        });

        myDialog.show();
    }

    private void salvarAvaliacao(float rating) {
        //Recuperar identificador usuario logado (base64)
        Preferencias preferencias = new Preferencias(DetalhesActivity.this);
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        DatabaseReference databaseReference = ConfigFirebase.getFirebase();

        DatabaseReference avaliacaoSerie =
                databaseReference.child("avaliacao_series")
                        .child( Integer.toString(serie_id) ).child( identificadorUsuarioLogado );
        DatabaseReference avaliacaoUsuario =
                databaseReference.child("avaliacao_usuarios")
                        .child( identificadorUsuarioLogado ).child( Integer.toString(serie_id) );

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setSerie_name( bundle.getString("original_title") );
        avaliacao.setSerie_poster( bundle.getString("thumbnail") );
        avaliacao.setAvaliacao( rating );
        avaliacao.setSerie_id( String.valueOf(serie_id) );

        avaliacaoSerie.setValue( avaliacao );
        avaliacaoUsuario.setValue( avaliacao );
        myDialog.dismiss();
        Snackbar.make(findViewById(R.id.activity_detalhes), "Avaliação realizada com sucesso", Snackbar.LENGTH_SHORT).show();
    }

    private void abrirDialog() {
        // DIALOG
        myDialog = new Dialog(DetalhesActivity.this);
        myDialog.setContentView(R.layout.custom_popup_add);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // VARIAVEIS
        txtClosePopup = myDialog.findViewById(R.id.txtClosePopup);
        btnFavoritos = myDialog.findViewById(R.id.btnAdicionarFavoritos);
        btnAssistidos = myDialog.findViewById(R.id.btnAdicionarAssistidos);
        btnQueroAssistir = myDialog.findViewById(R.id.btnAdicionarQueroAssistir);
        progressBarAdd = myDialog.findViewById(R.id.progressBarAdd);
        // AÇÔES DE CLICK
        txtClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        boolean favorita = verificarFavorito();
        boolean assistida = verificarAssistido();
        boolean quero_assistir = verificarQueroAssistir();

        if ( !favorita ){
            btnFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOnDatabase(1);
                }
            });
        }else {
            btnFavoritos.setText( "Remover Favoritos" );
            btnFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeSerieOnDatabase(1);
                }
            });
        }
        if ( !assistida ){
            btnAssistidos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOnDatabase(2);
                }
            });
        }else {
            btnAssistidos.setText( "Remover Assistidos" );
            btnAssistidos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeSerieOnDatabase(2);
                }
            });
        }
        if ( !quero_assistir ){
            btnQueroAssistir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOnDatabase(3);
                }
            });
        }else {
            btnQueroAssistir.setText( "Remover Quero Assistir" );
            btnQueroAssistir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeSerieOnDatabase(3);
                }
            });
        }



        myDialog.show();
    }

    private boolean verificarFavorito() {
        return db.verificaFavorito( serie_id );
    }

    private boolean verificarAssistido() {
        return db.verificaAssistido( serie_id );
    }

    private boolean verificarQueroAssistir() {
        return db.verificaQueroAssistir( serie_id );
    }

    private void removeSerieOnDatabase(int i) {
        String column;
        switch (i) {
            case 1:
                column = "favorita";
                db.removerFavorito( column, serie_id );
                myDialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_detalhes), "Removido dos Favoritos", Snackbar.LENGTH_SHORT).show();
                break;
            case 2:
                column = "assistida";
                db.removerAssistido( column, serie_id );
                myDialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_detalhes), "Removido dos Assistidos", Snackbar.LENGTH_SHORT).show();
                break;
            case 3:
                column = "quero_assistir";
                db.removerQueroAssistir( column, serie_id );
                myDialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_detalhes), "Removido dos Quero Assistir", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveOnDatabase(int i) {
        switch (i){
            case 1:
                db.addFavorito( new Serie(serie_id,bundle.getString("poster"),
                        nameOfSerie.getText().toString(),txtSinopse.getText().toString(),
                        txtApiRate.getText().toString(),dataLancamento) );
                myDialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_detalhes), "Adicionado aos Favoritos", Snackbar.LENGTH_SHORT).show();
                break;
            case 2:
                db.addAssistida( new Serie(serie_id, bundle.getString("poster"),
                        nameOfSerie.getText().toString(), txtSinopse.getText().toString(),
                        txtApiRate.getText().toString(), dataLancamento) );
                Snackbar.make(findViewById(R.id.activity_detalhes), "Já Assisti", Snackbar.LENGTH_SHORT).show();
                myDialog.dismiss();
                break;
            case 3:
                db.addQueroAssistir( new Serie(serie_id, bundle.getString("poster"),
                        nameOfSerie.getText().toString(), txtSinopse.getText().toString(),
                        txtApiRate.getText().toString(), dataLancamento) );
                Snackbar.make(findViewById(R.id.activity_detalhes), "Quero Assistir", Snackbar.LENGTH_SHORT).show();
                myDialog.dismiss();
                break;
        }
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.similarSerieLayout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(similarSerieAdapter);
        progressBar.setVisibility(View.VISIBLE);
        loadJSON();
    }

    private void loadJSON() {
        try {
            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);

            Call<SeriesResults> call = apiService.getSimilarSeries(serie_id, LANGUAGE, API_KEY);
            call.enqueue(new Callback<SeriesResults>() {
                @Override
                public void onResponse(@NonNull Call<SeriesResults> call, @NonNull Response<SeriesResults> response) {
                    if (response.isSuccessful()){
                        SeriesResults results = response.body();
                        List<SeriesResults.ResultsBean> listSeries = null;
                        if (results != null) {
                            listSeries = results.getResults();
                        }
                        progressBar.setVisibility(View.GONE);
                        similarSerieAdapter.adicionarListaSeries(listSeries);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Erro ao obter dados", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SeriesResults> call, @NonNull Throwable t) {
                    Log.d("ERROR", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Erro ao obter dados", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset){
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle(bundle.getString("original_title"));
                    isShow = true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void contarComentarios() {
        DatabaseReference databaseReference = ConfigFirebase.getFirebase().child("comentarios_series").child(Integer.toString(serie_id));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numComentarios = dataSnapshot.getChildrenCount();
                numTotalComentarios.setText( "( "+ numComentarios +" )" );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void exibirAvaliacao() {
        DatabaseReference databaseReference = ConfigFirebase.getFirebase().child("avaliacao_series").child(Integer.toString(serie_id));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numTotalAvaliacoes = dataSnapshot.getChildrenCount();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Avaliacao avaliacao = dados.getValue( Avaliacao.class );
                    somaAvalicao = somaAvalicao + avaliacao.getAvaliacao();
                }
                if ( numTotalAvaliacoes != 0 ) {
                    avaliacaoFinal = somaAvalicao / numTotalAvaliacoes;
                    txtAppRate.setText(String.valueOf(avaliacaoFinal));
                    allAvaliation.setRating(avaliacaoFinal);
                }else {
                    txtAppRate.setText("0.0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}