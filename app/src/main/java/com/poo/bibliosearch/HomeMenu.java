package com.poo.bibliosearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Adapters.AdapterBook;
import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.Entities.User;
import com.poo.bibliosearch.Fragments.BookDetailFragment;
import com.poo.bibliosearch.Fragments.HomeFragment;
import com.poo.bibliosearch.Fragments.LibraryFragment;
import com.poo.bibliosearch.Fragments.UsersFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;

public class HomeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , iComunicaFragments, LibraryFragment.onFragmentButtonSelected {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    public ArrayList<Object> items;
    public static Type type;
    int logger;

    //        variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //variables para detalle de libro
    BookDetailFragment bookDetailFragment;

    TextView userLogged;
    TextView emailLogged;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        load(LOG);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen);
        logger = 0;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);

        // establecer onclick de navigation view
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //Desactiva el menú usuarios si no se es administrador
        Menu menut=navigationView.getMenu();
        if (!((Login) items.get(0)).getLogedAsAdmin()){
            menut.getItem(2).setVisible(false);
        }


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //Carga Fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new HomeFragment());
        fragmentTransaction.commit();




        View headerView = navigationView.getHeaderView(0);
        userLogged = headerView.findViewById(R.id.txt_logged_user);
        emailLogged = headerView.findViewById(R.id.txt_logged_email);
        userLogged.setText(((Login) items.get(0)).getUser().getFirstName() + " " + ((Login) items.get(0)).getUser().getLastName());
        emailLogged.setText(((Login) items.get(0)).getUser().getEmail());


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (logger != 1) {
            logger++;
            Toast.makeText(this, "presiona atras de nuevo para cerrar sesion", Toast.LENGTH_SHORT).show();
        } else {
            load(LOG);
            items.clear();
            items.add(new Login(new User(), false, false));
            save(items, LOG);
            startActivity(getParentActivityIntent());
            finish();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemCompare = item.getItemId();
        int title;
        switch (itemCompare) {
            case R.id.home_menu_item:
                title = R.string.home_menu;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


                break;
            case R.id.books_menu_item:
                title = R.string.books;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new LibraryFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.users_menu_item:
                title = R.string.users;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new UsersFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            //Se pueden agregar más y mas menus
            case R.id.logoutToolbarItem_menu:
                title = R.string.log_out;
                load(LOG);
                items.clear();
                items.add(new Login(new User(), false, false));
                save(items, LOG);
                Intent intent = new Intent(this, LoginScreen.class);
                startActivity(intent);
                finish();
                setTitle(getString(title));
                break;

            default:
                throw new IllegalArgumentException("menu option not implemented");
        }
        setTitle(getString(title));
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }


    @Override
    public void sendBooks(Book book) {
//        toda la logica para enviar el objeto

        bookDetailFragment = new BookDetailFragment();
        //Objeto Bundle para transporte de info
        Bundle bundle = new Bundle();
//        se envia el objeto con serializable
        bundle.putSerializable("book", book);
        bookDetailFragment.setArguments(bundle);
//        abrir fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, bookDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    public void load(String TYPE) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(TYPE, null);
        switch (TYPE) {
            case "BOOKS":
                type = new TypeToken<ArrayList<Book>>() {
                }.getType();
                break;
            case "USERS":
                type = new TypeToken<ArrayList<User>>() {
                }.getType();
                break;
            case "LOG":
                type = new TypeToken<ArrayList<Login>>() {
                }.getType();
                break;
        }
        items = gson.fromJson(itemsJson, type);
    }

    public void save(ArrayList<Object> items, String TYPE) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String itemJson = gson.toJson(items);
        editor.putString(TYPE, itemJson);
        editor.apply();
    }

    @Override
    public void onButtonSelected() {

        Intent intent = new Intent(getApplicationContext(), CreateBookScreen.class);
        startActivity(intent);
        finish();


    }
}