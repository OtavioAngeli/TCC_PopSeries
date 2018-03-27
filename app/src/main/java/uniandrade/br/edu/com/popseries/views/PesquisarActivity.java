package uniandrade.br.edu.com.popseries.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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

    private ProgressBar progressBar;
    private EditText editText;
    private String pesquisa;

    private SearchSerieAdapter searchSerieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Pesquisar");
        //Ação para voltar pra tela inicial
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView txtPesquisar = findViewById(R.id.btnPesquisar);
        progressBar = findViewById(R.id.progressBarPesquisar);
        editText = findViewById(R.id.txtPesquisar);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPesquisar);
        searchSerieAdapter = new SearchSerieAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setAdapter(searchSerieAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        txtPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisa = editText.getText().toString();
                if (pesquisa.equals("")){
                    Toast.makeText(getApplicationContext(), "Digite o nome da série", Toast.LENGTH_SHORT).show();
                } else {
                    closeKeybord();
                    progressBar.setVisibility(View.VISIBLE);
                    loadJSON( pesquisa );
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
