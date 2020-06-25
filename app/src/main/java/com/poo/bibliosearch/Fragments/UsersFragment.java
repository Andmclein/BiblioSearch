package com.poo.bibliosearch.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Adapters.AdapterUser;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.R;
import com.poo.bibliosearch.Entities.User;
import com.poo.bibliosearch.iComunicaFragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;
import static com.poo.bibliosearch.LoginScreen.USERS;

public class UsersFragment extends Fragment {
    ArrayList<User> users;
    ArrayList<Login> logins;
    SharedPreferences sharedPreferences;
    Type type;

    User aux;

    AdapterUser adapterUser;
    RecyclerView recyclerViewUsers;
    Button delete, newAdmin;
    View view;
    ConstraintLayout cl_user_fragment;
    TextView usuario_invalido;

    Activity actividad;
    iComunicaFragments interfaceComunicaFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.users_fragment, container, false);
        recyclerViewUsers = view.findViewById(R.id.rv_user_list);
        newAdmin = view.findViewById(R.id.rv_u_btn_newUser);
        cl_user_fragment = view.findViewById(R.id.cl_user_fragment);
        usuario_invalido = view.findViewById(R.id.tv_usuario_invalido);
        loadLog();
        loadUsers();
        if (!logins.get(0).getLogedAsAdmin()) {
            users.clear();
            cl_user_fragment.setVisibility(View.GONE);
            newAdmin.setVisibility(View.GONE);
        } else {
            usuario_invalido.setVisibility(View.GONE);
        }


        showData();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showData() {
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterUser = new AdapterUser(getContext(), users);
        recyclerViewUsers.setAdapter(adapterUser);

        adapterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aux = (users.get(recyclerViewUsers.getChildAdapterPosition(view)));
            }
        });

    }

    public void loadUsers() {
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(USERS, null);
        type = new TypeToken<ArrayList<User>>() {
        }.getType();
        users = gson.fromJson(itemsJson, type);

    }

    public void loadLog() {
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(LOG, null);
        type = new TypeToken<ArrayList<Login>>() {
        }.getType();
        logins = gson.fromJson(itemsJson, type);

    }


    public interface onFragmentButtonSelected {
        public void onButtonSelected();

    }
}

