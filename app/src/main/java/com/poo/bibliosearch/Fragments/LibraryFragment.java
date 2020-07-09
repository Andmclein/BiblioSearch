package com.poo.bibliosearch.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Adapters.AdapterBook;
import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.R;
import com.poo.bibliosearch.iComunicaFragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.BOOKS;
import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;


public class LibraryFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    //Para acción del botón
    private onFragmentButtonSelected listener;
    AdapterBook adapterBook;
    RecyclerView recyclerViewBooks;
    Button botonCrear;
    ArrayList<Book> books;
    ArrayList<Login> logins;
    SharedPreferences sharedPreferences;
    Type type;
    //referencias para comunicar fragments
    Activity actividad;
    iComunicaFragments interfaceComunicaFragments;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);
        setHasOptionsMenu(true);
        recyclerViewBooks = view.findViewById(R.id.rv_library_list);
        loadLog();
        botonCrear = view.findViewById(R.id.btn_newBook);
        if (!logins.get(0).getLogedAsAdmin()) {
            botonCrear.setVisibility(View.GONE);
            recyclerViewBooks.setPadding(0,0,0,0);
        } else {
            botonCrear.setVisibility(View.VISIBLE);
            recyclerViewBooks.setPadding(0,0,0,50);
        }
        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonSelected();
            }
        });

        //cargar la lista
        loadBooks();
        //mostrar los datos
        showData();

        adapterBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Book> aux1 = adapterBook.getItems();
                interfaceComunicaFragments.sendBooks(aux1.get(recyclerViewBooks.getChildAdapterPosition(view)));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {

        if (context instanceof onFragmentButtonSelected) {
            listener = (onFragmentButtonSelected) context;
        }

        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        if (context instanceof Activity) {
            this.actividad = (Activity) context;
            interfaceComunicaFragments = (iComunicaFragments) this.actividad;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void loadBooks() {
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(BOOKS, null);
        type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        books = gson.fromJson(itemsJson, type);
    }
    public void loadLog() {
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(LOG, null);
        type = new TypeToken<ArrayList<Login>>() {
        }.getType();
        logins = gson.fromJson(itemsJson, type);
    }
    private void showData() {
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterBook = new AdapterBook(getContext(), books);
        recyclerViewBooks.setAdapter(adapterBook);


    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        adapterBook.setFilter(books);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        loadBooks();
        if(newText ==null||newText.trim().isEmpty()){
            adapterBook.setFilter(books);
            return false;
        }

        newText = newText.toLowerCase();
        final ArrayList<Book> filteredBooksList =new ArrayList<>();
        for(Book book:books) {
            final String name = book.getName().toLowerCase();
            final String id = String.valueOf(book.getIdNumber());
            
            if(name.contains(newText)||id.contains(newText)){
                filteredBooksList.add(book);
            }
        }
        adapterBook.setFilter(filteredBooksList);
        return true;
    }

    public interface onFragmentButtonSelected {
        void onButtonSelected();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search_menu);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
