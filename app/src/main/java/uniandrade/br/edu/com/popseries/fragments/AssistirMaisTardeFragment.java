package uniandrade.br.edu.com.popseries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.ListaSerieAdapter;
import uniandrade.br.edu.com.popseries.helper.SeriesDbHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistirMaisTardeFragment extends Fragment {

    private ListaSerieAdapter listaSerieAdapter;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewQueroAssistir);
        listaSerieAdapter = new ListaSerieAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(listaSerieAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        retornaQueroAssistir();

        return view;
    }

    private void retornaQueroAssistir() {
        SeriesDbHelper db = new SeriesDbHelper( getContext() );
        listaSerieAdapter.adicionarListaSeries( db.retornaQueroAssistir() );
        db.close();
    }

}
