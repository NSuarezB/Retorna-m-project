package com.example.retornam20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

/**
 * Classe d'entrada principal del programa. En cas de no tenir la sessió iniciada saltarà a la
 * LoginActivity per a iniciar-la.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseUser user;

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton nouObjecte;
    FloatingActionButton nouPrestec;

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // FIREBASE
        comprovaUsuari();

        // NAV BAR
        configuraBarraNavegacio();

        // HAMBURGER MENU
        configuraMenuDesplegable();

        // FLOATING BUTTONS
        configuraBotoFlotant();


        // EVENTS
        nouObjecte = findViewById(R.id.nouObjecte);
        nouObjecte.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NouObjecteActivity.class);
            startActivity(i);
        });

        nouPrestec = findViewById(R.id.nouPrestec);
        nouPrestec.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SelectObjecteActivity.class);
            startActivity(i);
        });

        /**
         *
         * Tancar la sessió
         *
         */
        if (user != null) {
            View header = navigationView.getHeaderView(0);
            TextView usernameMenuField = header.findViewById(R.id.header_username);
            TextView emailMenuField = header.findViewById(R.id.header_email);
            usernameMenuField.setText(mAuth.getCurrentUser().getEmail());
            emailMenuField.setText(mAuth.getCurrentUser().getDisplayName());
            logout = header.findViewById(R.id.button7);
            logout.setOnClickListener(view -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                        getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }

    /**
     * Comprova que l'usuari estigui iniciat, si no ho esta , l'envia al Login
     */
    private void comprovaUsuari() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
    }

    /**
     * Configura la barra de navegació i lliga els events a la activity
     */
    private void configuraBarraNavegacio() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(this);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Configura el hamburger menu, els items estan a activity_main_drawer.xml
     */
    private void configuraMenuDesplegable() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);
    }

    /**
     * Configura els botons flotants (nou prestec i nou objecte)
     */
    private void configuraBotoFlotant() {
        floatingActionMenu = findViewById(R.id.menuFlotant);
        floatingActionMenu.setClosedOnTouchOutside(true);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Té tota la logistica de selecció d'elements en el hamburger
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Log.d("MAIN_ACTIVITY", "ITEM CLICKED: " + item.getItemId());

        Fragment fragment;
        String titol;
        switch (item.getItemId()) {
            case R.id.nav_objectes:
                titol = getString(R.string.menu_objectes);
                fragment = ObjectesContentFragment.newInstance();

                break;
            case R.id.nav_home:
                titol = getString(R.string.menu_home);
                fragment = HomeContentFragment.newInstance();

                break;
            case R.id.nav_settings:
                titol = getString(R.string.menu_settings);
                fragment = SettingsContentFragment.newInstance();

                break;
            case R.id.nav_history:
                titol = getString(R.string.menu_history);
                fragment = HistoryContentFragment.newInstance();

                break;
            default:
                throw new IllegalArgumentException("not implement!");
        }

        setTitle(titol);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.home_content, fragment)
                .commit();

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull @NotNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull @NotNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}