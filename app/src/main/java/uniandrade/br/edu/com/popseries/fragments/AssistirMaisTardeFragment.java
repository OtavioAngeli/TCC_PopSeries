package uniandrade.br.edu.com.popseries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.ListSerieOfflineAdapter;
import uniandrade.br.edu.com.popseries.api.SeriesResults;
import uniandrade.br.edu.com.popseries.helper.SeriesDbHelper;
import uniandrade.br.edu.com.popseries.model.Serie;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistirMaisTardeFragment extends Fragment {

    private RecyclerView recyclerView;

    public AssistirMaisTardeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        retornaQueroAssistir();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistir_mais_tarde, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewQueroAssistir);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        retornaQueroAssistir();

        return view;
    }

    private void retornaQueroAssistir() {
        SeriesDbHelper db = new SeriesDbHelper( getContext() );
        ListSerieOfflineAdapter listaSerieAdapter = new ListSerieOfflineAdapter(getContext());
        recyclerView.setAdapter(listaSerieAdapter);
        List<Serie> list = db.retornaQueroAssistir();
        if ( list != null ){
            listaSerieAdapter.adicionarListaSeries( list );
        }
        db.close();
    }

}
