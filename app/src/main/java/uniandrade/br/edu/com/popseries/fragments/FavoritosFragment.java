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
public class FavoritosFragment extends Fragment {

    private RecyclerView recyclerView;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        retornaFavoritos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewFavoritos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        retornaFavoritos();

        return view;

    }

    private void retornaFavoritos() {
        SeriesDbHelper db = new SeriesDbHelper( getContext() );
        ListSerieOfflineAdapter listaSerieAdapter = new ListSerieOfflineAdapter(getContext());
        recyclerView.setAdapter(listaSerieAdapter);
        List<Serie> list = db.retornaFavoritos();
        if ( list != null ){
            listaSerieAdapter.adicionarListaSeries( list );
        }
        db.close();
    }

}
