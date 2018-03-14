package uniandrade.br.edu.com.popseries.views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.SearchSerieAdapter;
import uniandrade.br.edu.com.popseries.api.Client;
import uniandrade.br.edu.com.popseries.api.SeriesResults;
import uniandrade.br.edu.com.popseries.api.Service;

public class PesquisarActivity extends AppCompatActivity {

    public static String API_KEY = "042df6719b1c27335641d1d7a9e2e66e";
    public static String LANGUAGE = "pt-BR";
    public static String TESTE = "casa";

    private ProgressBar progressBar;
    private EditText editText;

    private SearchSerieAdapter searchSerieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Pesquisar");

        ImageButton imageButton = findViewById(R.id.imgBtnPesquisar);
        progressBar = findViewById(R.id.progressBarPesquisar);
        editText = findViewById(R.id.txtPesquisar);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPesquisar);
        searchSerieAdapter = new SearchSerieAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setAdapter(searchSerieAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesquisa;
                pesquisa = editText.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                loadJSON( pesquisa );
            }
        });


    }

    private void loadJSON(String pesquisa) {
        try {
            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);

            Call<SeriesResults> call = apiService.getSearchSeries(LANGUAGE, API_KEY, pesquisa);
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
                        searchSerieAdapter.adicionarListaSeries(listSeries);
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
}
