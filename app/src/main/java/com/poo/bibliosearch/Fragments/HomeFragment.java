package com.poo.bibliosearch.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
import com.poo.bibliosearch.Entities.User;
import com.poo.bibliosearch.R;
import com.poo.bibliosearch.iComunicaFragments;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;

public class HomeFragment extends Fragment {


    Type type;
    ArrayList<Login> logins;
    ArrayList<Book> books;
    SharedPreferences sharedPreferences;
    TextView tv_name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        loadLog();
        tv_name = view.findViewById(R.id.tv_hs_name);
        String t1 = logins.get(0).getUser().getFirstName() + "  ";
        String t2 = logins.get(0).getUser().getLastName();
        tv_name.setText(t1 + t2);

        return view;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

    }

    public void loadLog() {
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(LOG, null);
        type = new TypeToken<ArrayList<Login>>() {
        }.getType();
        logins = gson.fromJson(itemsJson, type);
    }


}
