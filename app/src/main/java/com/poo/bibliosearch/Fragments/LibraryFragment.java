package com.poo.bibliosearch.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class LibraryFragment extends Fragment {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);
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

        adapterBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceComunicaFragments.sendBooks(books.get(recyclerViewBooks.getChildAdapterPosition(view)));
            }
        });
    }

    public interface onFragmentButtonSelected {
        public void onButtonSelected();

    }


}
