package com.poo.bibliosearch.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.Entities.User;
import com.poo.bibliosearch.R;
import com.poo.bibliosearch.iComunicaFragments;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.BOOKS;
import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;
import static com.poo.bibliosearch.LoginScreen.USERS;


public class BookDetailFragment extends Fragment {

    TextView name, author, releaseYear, id, description, usuarioBloqueado;
    ImageView cover;
    Button historial, reservar, devolver;
    RatingBar rating;

    ArrayList<User> users;
    ArrayList<Login> logins;
    ArrayList<Book> books;
    SharedPreferences sharedPreferences;
    Type type;

    Book book = null;
    User auxLogged;
    String inputText = "";
    String texto ="";


    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_book_fragment, container, false);
        loadUsers();
        loadBooks();
        loadLog();

        auxLogged = logins.get(0).getUser();

        usuarioBloqueado = view.findViewById(R.id.tv_usuario_bloqueado);
        name = view.findViewById(R.id.detail_book_name);
        author = view.findViewById(R.id.detail_book_author);
        releaseYear = view.findViewById(R.id.detail_book_release);
        description = view.findViewById(R.id.detail_book_description);
        id = view.findViewById(R.id.detail_book_id);
        cover = view.findViewById(R.id.detail_book_cover);
        rating = view.findViewById(R.id.detail_book_ratingBar);
        reservar = view.findViewById(R.id.reservar_libro);
        devolver = view.findViewById(R.id.devolucion_libro);
        historial = view.findViewById(R.id.historial_libro);
//        crear objeto bundle para recibir objeto enviado
        Bundle bundleIn = getArguments();

//        validacion de argumentos para mostrar
        if (bundleIn != null) {
            book = (Book) bundleIn.getSerializable("book");

        }
        //        establecer los datos en las vistas
        name.setText(book.getName());
        author.setText(book.getAuthor());
        releaseYear.setText("Publicado en: " + (book.getReleaseYear()));
        description.setText(book.getDescription());
        id.setText("id: " + (book.getIdNumber()));
        cover.setImageResource(book.getCover());
        rating.setRating(book.getRating());
        rating.setClickable(false);

        if (!auxLogged.isAdmin) {
            historial.setVisibility(View.GONE);
            devolver.setVisibility(View.GONE);


            for (int i = 0; i < auxLogged.getMyBooks().size(); i++) {
                if ((!auxLogged.getActive() || book.getCant() == 0) || (book.getIdNumber() == auxLogged.getMyBooks().get(i).getIdNumber())) {
                    reservar.setVisibility(View.GONE);
                    break;
                }
            }
            if (!auxLogged.getActive()) {
                usuarioBloqueado.setVisibility(View.VISIBLE);
                reservar.setVisibility(View.GONE);
            }
        } else {
            reservar.setVisibility(View.GONE);
        }

        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmación");
                builder.setMessage("¿Seguro que desea reservar el libro?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int k = 0; k < books.size(); k++) {
                            if (book.getIdNumber() == books.get(k).getIdNumber()) {
                                books.get(k).setCant(book.getCant() - 1);
                                book.setCant(book.getCant() - 1);
                                break;
                            }

                        }
                        for (int i = 0; i < users.size(); i++) {

                            if (auxLogged.getDocument().equals(users.get(i).getDocument())) {

                                users.get(i).addBook(book);
                                break;
                            }
                        }
                        logins.get(0).getUser().addBook(book);

                        saveUsers();
                        saveLogins();
                        saveBooks();

                        Toast.makeText(getContext(), "Libro reservado correctamente", Toast.LENGTH_SHORT).show();
                        reservar.setVisibility(View.GONE);


                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false)
                        .show();

            }
        });

        devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("inserte el documento de identidad");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       // se deben cargar de nuevo los datos
                        loadUsers();
                        loadLog();
                        loadBooks();
                        int CASE = 0;
                        inputText = input.getText().toString();


                        for (int i = 0; i < users.size(); i++) {
                        //recorre los usuarios
                            if (inputText.equals(users.get(i).getDocument())) {
                            //si lo encuentra inicia el for, si no reinicia el bucle anterior
                            //si termina sin encontrar el usuario CASE =1

                                for (int k = 0; k < users.get(i).getMyBooks().size(); k++) {
                                // Recorre los libros
                                    if(users.get(i).getMyBooks().isEmpty()){
                                    //si ese usuario no tiene libros no ejecuta la busqueda y sale del ciclo
                                    //CASE = 3

                                        CASE = 3;
                                        break;
                                    }else if (users.get(i).getMyBooks().get(k).getIdNumber() == book.getIdNumber()) {
                                        //si el usuario tiene el libro entra a retirarlo
                                        // de tener libros pero no ese CASE = 2

                                        users.get(i).getMyBooks().remove(k);
                                        book.setCant(book.getCant() + 1);
                                        for (int j = 0; j < books.size(); j++) {
                                        //Retira el libro satisfactoriamente CASE = 0 y sale del bucle
                                            if (book.getIdNumber() == books.get(j).getIdNumber()) {
                                                books.get(j).setCant(book.getCant());
                                                CASE = 0;
                                                break;
                                            }
                                        }
                                        break;
                                    } else {
                                        CASE = 2;
                                    }
                                }
                                break;
                            }else {
                                CASE = 1;
                            }
                        }
                        switch (CASE) {
                            case 1:
                                 texto = "usuario no encontrado";
                                break;
                            case 2:
                                texto = "el usuario no tiene este libro en prestamo";
                                break;
                            case 3:
                                texto = "el usuario no tiene libros en prestamo";
                                break;
                            default:
                                texto = "libro devuelto correctamente";
                                break;
                        }
                        saveUsers();
                        saveLogins();
                        saveBooks();
                        Toast.makeText(getActivity(),texto,Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show().setCancelable(false);

            }
        });


        return view;
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

    public void loadBooks() {
        Gson gson = new Gson();
        String itemsJson = sharedPreferences.getString(BOOKS, null);
        type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        books = gson.fromJson(itemsJson, type);

    }


    public void saveUsers() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String itemJson = gson.toJson(users);
        editor.putString(USERS, itemJson);
        editor.apply();
    }

    public void saveLogins() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String itemJson = gson.toJson(logins);
        editor.putString(LOG, itemJson);
        editor.apply();
    }

    public void saveBooks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String itemJson = gson.toJson(books);
        editor.putString(BOOKS, itemJson);
        editor.apply();
    }


}
