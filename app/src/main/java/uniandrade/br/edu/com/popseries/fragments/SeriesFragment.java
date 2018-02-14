package uniandrade.br.edu.com.popseries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.ListaSerieAdapter;
import uniandrade.br.edu.com.popseries.api.Client;
import uniandrade.br.edu.com.popseries.api.SeriesResults;
import uniandrade.br.edu.com.popseries.api.Service;

/**
 Clien
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {

    public static String API_KEY = "042df6719b1c27335641d1d7a9e2e66e";
    public static String LANGUAGE = "pt-BR";

    private RecyclerView recyclerView;
    private ListaSerieAdapter listaSerieAdapter;

    public SeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSeries);
        listaSerieAdapter = new ListaSerieAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(listaSerieAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        loadJSON();

        return view;
    }

    private void loadJSON() {
       try {
           Client Client = new Client();
           Service apiService = Client.getClient().create(Service.class);

           Call<SeriesResults> call = apiService.getPopularSeries(LANGUAGE, API_KEY);
           call.enqueue(new Callback<SeriesResults>() {
               @Override
               public void onResponse(Call<SeriesResults> call, Response<SeriesResults> response) {
                   if (response.isSuccessful()){
                       SeriesResults results = response.body();
                       List<SeriesResults.ResultsBean> listSeries = results.getResults();
                       listaSerieAdapter.adicionarListaSeries(listSeries);
                   }
               }

               @Override
               public void onFailure(Call<SeriesResults> call, Throwable t) {
                   Log.d("ERROR", t.getMessage());
                   Toast.makeText(getContext(), "Erro ao obter dados", Toast.LENGTH_SHORT).show();
               }
           });

       }catch (Exception e){
           Log.d("ERROR", e.getMessage());
           Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
       }

    }

}
