package com.poo.bibliosearch;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.Entities.User;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;
import static com.poo.bibliosearch.LoginScreen.USERS;


public class RegisterScreen extends AppCompatActivity {

    public static ArrayList<Object> items;
    public static Type type;

    public static  SharedPreferences sharedPreferences;
    public EditText txt_user_name, txt_first_name,
            txt_last_name, txt_email,
            txt_create_password, txt_confirm_password,txt_college,txt_document;

    public String college, confirmPassword, createPassword, email, lastName, firstName, userName,document;
    public Switch switch_is_admin;
    public Boolean switchIsAdmin=false;
    public Button register;

    User aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        txt_create_password = findViewById(R.id.txt_create_password);
        txt_email = findViewById(R.id.txt_r_email);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_college = findViewById(R.id.et_college);
        txt_user_name = findViewById(R.id.txt_user_name);
        switch_is_admin = findViewById(R.id.switch_is_admin);
        txt_document = findViewById(R.id.txt_document);
        register = findViewById(R.id.btn_confirm_register);

        load(LOG);

        if (!((Login)items.get(0)).getLogedAsAdmin()||!((Login)items.get(0)).getLogedIn()){
            switch_is_admin.setVisibility(View.GONE);
        }

        switch_is_admin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(switch_is_admin.isActivated()){
                    switch_is_admin.setActivated(false);
                    switchIsAdmin = false;
                }else {
                    switch_is_admin.setActivated(true);
                    switchIsAdmin = true;
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRegister(v);
            }
        });

    }



    // lee las entradas de el formulario y de ser entradas válidas crea el usuario
    public void ConfirmRegister(View view) {


        confirmPassword = txt_confirm_password.getText().toString();
        createPassword = txt_create_password.getText().toString();
        email = txt_email.getText().toString();
        college = txt_college.getText().toString();
        firstName = txt_first_name.getText().toString();
        lastName = txt_last_name.getText().toString();
        userName = txt_user_name.getText().toString();
        document = txt_document.getText().toString();



        if (validForm(email, confirmPassword, createPassword, userName)) {
            User newUser = new User(userName,document, createPassword, email, firstName, lastName, college, switchIsAdmin);
            items.add(newUser);
            System.out.println(items);
            save(USERS);

            Intent intent = new Intent(this, LoginScreen.class);
            startActivity(intent);

        } else {
            txt_create_password.setText("");
            txt_confirm_password.setText("");

        }

    }

    // Verifica que la entrada de todos los campos sea válida para la creación de usuario
    private boolean validForm(@NotNull String email, String pass1, String pass2, String u) {
        load(USERS);
        return !email.equals("")
                && checkEmail(email)
                && !emailExists(email)
                && !userExists(u)
                && passwordCheck(pass1, pass2)
                && !pass1.isEmpty()
                && !pass2.isEmpty();
    }

    // Verifica que no haya otro usuario con ese mismo nombre de usuario
    public boolean userExists(String userName) {
        boolean ans = false;

        if (userName.isEmpty()) {
            Toast.makeText(this, "Empty user", Toast.LENGTH_LONG).show();
            ans = true;
        } else {
            int i = 0;
            while (i < items.size()) {

                aux = (User) items.get(i);
                if (userName.equals(aux.getUserName())) {
                    ans = true;
                    break;
                }
                i++;

            }
            if (i == items.size()) ans = false;
        }
        if (ans) {
            Toast.makeText(this, "user already taken", Toast.LENGTH_SHORT).show();
            txt_user_name.setText("");
        }
        return ans;


    }

    // Verifica que no haya otro usuario con ese correo electrónico
    private boolean emailExists(String email) {
        boolean ans = false;

        if (email.isEmpty() || !checkEmail(email)) {
            Toast.makeText(this, "Invalid or empty email", Toast.LENGTH_LONG).show();
            ans = true;
        } else {
            int i = 0;
            while (i < items.size()) {

                aux = (User) items.get(i);
                if (email.equals(aux.getEmail())) {
                    ans = true;
                    break;
                }
                i++;

            }
            if (i == items.size()) {
                ans = false;
            }
        }
        if (ans) {
            Toast.makeText(this, "email already taken", Toast.LENGTH_SHORT).show();
            txt_email.setText("");
        }
        return ans;

    }

    // Verifica que la entrada sea un correo válido
    private boolean checkEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // Verifica que las contraseñas sean iguales
    public boolean passwordCheck(String p1, String p2) {
        if (!p1.equals(p2))
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        return p1.equals(p2);
    }

    // ---------------BLOQUE DE CARGA DE DATOS DESDE SHAREDPREFERENCES-----------------//

    /*Carga los datos del TYPE (BOOKS,USERS,LOG) solicitado de sharedPreferences*/
    public void load(String TYPE) {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
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

    /*Guarda los datos del TYPE (BOOKS,USERS,LOG) que se encuentren alojados en el
     arreglo items en tiempo de ejecución*/
    public void save(String TYPE) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String itemJson = gson.toJson(items);
        editor.putString(TYPE, itemJson);
        editor.apply();
    }

    // -------------------------------------------------------------------------------//


}