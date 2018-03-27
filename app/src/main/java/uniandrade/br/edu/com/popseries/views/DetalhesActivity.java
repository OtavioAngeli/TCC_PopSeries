package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.TextView;
import android.widget.Toast;

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

public class DetalhesActivity extends AppCompatActivity {

    public static String API_KEY = "042df6719b1c27335641d1d7a9e2e66e";
    public static String LANGUAGE = "pt-BR";

    TextView nameOfSerie, txtSinopse, userRating, releaseDate, txtApiRate, txtUserRate, txtAppRate;
    ImageView imgThumbnail;
    Button btnAvaliar, btnAdicionar;

    LinearLayoutCompat linLayComent;

    Bundle bundle;
    int serie_id;

    private SimilarSerieAdapter similarSerieAdapter;
    private ProgressBar progressBar;

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

        initCollapsingToolbar();

        progressBar = findViewById(R.id.progressBarSeriesRelacionadas);

        linLayComent    = findViewById(R.id.linLayComent);
        imgThumbnail    = findViewById(R.id.thumbnailImageHeader);
        nameOfSerie     = findViewById(R.id.txtTitle);
        txtSinopse      = findViewById(R.id.txtSinopse);
        txtApiRate      = findViewById(R.id.apiRate);
        btnAvaliar      = findViewById(R.id.btnAvaliar);
        btnAdicionar    = findViewById(R.id.btnAdicionar);
//        userRating          = findViewById(R.id.userrating);
//        releaseDate         = findViewById(R.id.releasedate);

        Intent intent = getIntent();

        if (intent != null){
            bundle = intent.getExtras();
            if (bundle != null){

                serie_id = bundle.getInt("serie_id");

                Picasso.with(this)
                        .load(bundle.getString("poster"))
                        .into(imgThumbnail);

                nameOfSerie.setText(bundle.getString("original_title"));
                txtSinopse.setText(bundle.getString("overview"));
                txtApiRate.setText(bundle.getString("apiRate"));
            }
        }

        // BOTÃO AVALIAR
        btnAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Avaliar", Toast.LENGTH_SHORT).show();
            }
        });

        // BOTÃO ADICIONAR
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Adicionar", Toast.LENGTH_SHORT).show();
            }
        });

        // EVENTO DE CLICK NO LAYOUT DE COMENTÁRIOS
        linLayComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Comentários", Toast.LENGTH_SHORT).show();
            }
        });

        similarSerieAdapter = new SimilarSerieAdapter(getApplicationContext());

        initView();

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

}
