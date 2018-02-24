package uniandrade.br.edu.com.popseries.Views;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import uniandrade.br.edu.com.popseries.R;

public class DetalhesActivity extends AppCompatActivity {

    TextView nameOfSerie, txtSinopse, userRating, releaseDate, txtApiRate;
    ImageView imgThumbnail;

    LinearLayoutCompat linLayComent;

    Bundle bundle;
//    int movie_id;

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

        initCollapsingToolbar();

        linLayComent        = findViewById(R.id.linLayComent);
        imgThumbnail        = findViewById(R.id.thumbnailImageHeader);
        nameOfSerie         = findViewById(R.id.txtTitle);
        txtSinopse          = findViewById(R.id.txtSinopse);
        txtApiRate          = findViewById(R.id.apiRate);
//        userRating          = findViewById(R.id.userrating);
//        releaseDate         = findViewById(R.id.releasedate);

        Intent intent = getIntent();

        if (intent != null){
            bundle = intent.getExtras();
            if (bundle != null){

                Picasso.with(this)
                        .load(bundle.getString("poster"))
                        .into(imgThumbnail);

                nameOfSerie.setText(bundle.getString("original_title"));
                txtSinopse.setText(bundle.getString("overview"));
                txtApiRate.setText(bundle.getString("apiRate"));
            }
        }

        linLayComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Comentários", Toast.LENGTH_SHORT).show();
            }
        });

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
