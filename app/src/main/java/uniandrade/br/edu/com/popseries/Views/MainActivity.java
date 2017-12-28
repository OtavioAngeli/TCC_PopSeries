package uniandrade.br.edu.com.popseries.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.fragments.AssistidosFragment;
import uniandrade.br.edu.com.popseries.fragments.AssistirMaisTardeFragment;
import uniandrade.br.edu.com.popseries.fragments.FavoritosFragment;
import uniandrade.br.edu.com.popseries.fragments.SeriesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, new SeriesFragment()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_serie);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_bottom);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_bottom_series:
                        setTitle(R.string.app_name);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_main, new SeriesFragment()).commit();
                        break;
                    case R.id.nav_bottom_favoritos:
                        setTitle("Favoritos");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_main, new FavoritosFragment()).commit();
                        break;
                    case R.id.nav_bottom_assistidos:
                        setTitle("Assistidos");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_main, new AssistidosFragment()).commit();
                        break;
                    case R.id.nav_bottom_favorito4:
                        setTitle("Assistir Mais Tarde");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_main, new AssistirMaisTardeFragment()).commit();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.menu_pesquisa){
           Toast.makeText(getApplicationContext(),"Menu Pesquisa", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_serie) {
            Toast.makeText(getApplicationContext(),"Menu Séries", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_amigos) {
            Toast.makeText(getApplicationContext(),"Menu Amigos", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_perfil) {
            Toast.makeText(getApplicationContext(),"Menu Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_ajuda) {
            Toast.makeText(getApplicationContext(),"Menu Ajuda", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_sobre) {
            Toast.makeText(getApplicationContext(),"Menu Sobre", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
