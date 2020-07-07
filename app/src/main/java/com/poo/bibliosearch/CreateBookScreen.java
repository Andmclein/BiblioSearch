package com.poo.bibliosearch;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.Entities.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.BOOKS;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;

public class CreateBookScreen extends AppCompatActivity {


    Type type;
    ArrayList<Object> items;
    EditText et_c_name, et_c_author, et_c_description, et_c_release_year,et_c_cant;
    Button btn_c_save_book, btn_c_upload_cover;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book_screen);
    }


    public void createBook(View view) {

        et_c_name = findViewById(R.id.et_c_name);
        et_c_author = findViewById(R.id.et_c_author);
        et_c_description = findViewById(R.id.et_c_description);
        et_c_release_year = findViewById(R.id.et_c_release_year);
        et_c_cant = findViewById(R.id.et_c_name2);
        btn_c_save_book = findViewById(R.id.btn_c_save_book);
        ratingBar = findViewById(R.id.c_ratingbar);

        String name = et_c_name.getText().toString();
        String author = et_c_author.getText().toString();
        String description = et_c_description.getText().toString();
        int releaseYear = Integer.parseInt(et_c_release_year.getText().toString());
        int cant = Integer.parseInt(et_c_cant.getText().toString());
        float ratingBarStars = ratingBar.getRating();


        if (isValidBook(name, author, description, releaseYear)) {
            load(BOOKS);
            Book newBook = new Book(items.size(), R.drawable.ic_book_1,
                    name, author, description, releaseYear, ratingBarStars,cant);
            items.add(newBook);
            save(items, BOOKS);
            Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean isValidBook(String name, String author, String description, int releaseYear) {
        boolean isValidBook;
        if (name.isEmpty() || author.isEmpty() || description.isEmpty() || releaseYear == 0) {
            isValidBook = false;
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        } else isValidBook = true;
        return (isValidBook);
    }

    @Override
    public void onBackPressed() {
        startActivity(getParentActivityIntent());
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
}