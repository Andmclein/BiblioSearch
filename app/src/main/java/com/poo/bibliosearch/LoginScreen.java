package com.poo.bibliosearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import static com.poo.bibliosearch.R.layout.login_screen;

public class LoginScreen extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_preferences"; /*Ref: nombre del shared preferences*/
    public static final String USERS = "USERS"; /*Hace referencia a los libros que se van a guardar*/
    public static final String BOOKS = "BOOKS";
    public static final String LOG = "LOG";

    public static Boolean isLogedIn = false;
    public static Boolean logedAsAdmin = false;
    public SharedPreferences sharedPreferences;

    public static ArrayList<Object> items;
    public static Type type;

    public User aux;
    public EditText txt_email, txt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(login_screen);

        load(USERS);
        load(BOOKS);
        load(LOG);
        isLogedIn = ((Login) items.get(0)).getLogedIn();
        logedAsAdmin = ((Login) items.get(0)).getLogedAsAdmin();
        if (isLogedIn) {

            Intent intent = new Intent(this, HomeMenu.class);
            startActivity(intent);
            finish();
        }

    }

    public void loginButton(View view) {
        load(USERS);
        txt_email = findViewById(R.id.txt_login_email);
        txt_password = findViewById(R.id.txt_login_password);

        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        if (email.isEmpty() || !checkEmail(email)) {
            Toast.makeText(LoginScreen.this, "Invalid or empty email", Toast.LENGTH_LONG).show();
        } else {
            int i = 0;

            while (i < items.size()) {

                aux = (User) items.get(i);
                if (email.equals(aux.getEmail())) {

                    checkPassword(aux, password);
                    break;
                }
                i++;

            }
            if (i == items.size()) {
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "PLEASE REGISTER", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*Evalua si la contraseña ingresada corresponde al usuario ingresado
    y ejecuta el inicio de si corresponde, llevando el usuario a la pantalla principal*/
    private void checkPassword(@NotNull User aux, String passwordToCheck) {
        String password = aux.getPassword();
        if (!passwordToCheck.isEmpty()) {
            if (passwordToCheck.equals(password)) {
                isLogedIn = true;
                Login newLogin = new Login(aux, isLogedIn, aux.isAdmin);
                load(LOG);
                items.clear();
                items.add(newLogin);
                save(LOG);
                Intent intent = new Intent(this, HomeMenu.class);
                startActivity(intent);
                finish();


            } else {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                txt_password.setText("");
            }
        } else {
            Toast.makeText(this, "enter a password", Toast.LENGTH_SHORT).show();
        }
    }

    /* Redirige al usuario a la actividad para registrar un nuevo usuario*/
    public void registerButton(View view) {
        Intent intent = new Intent(this, RegisterScreen.class);
        startActivity(intent);
    }

    /*Asegura que el correo ingresado corresponde al formato de correo electrónico **@**.** */
    private boolean checkEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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
        if (items == null) createItems(TYPE);
    }

    /*De no existir ningún dato alojado en el sharedPreferences*/
    public void createItems(String TYPE) {

        items = new ArrayList<>();
        String loremIpsum = "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum, written by Cicero in 45 BC Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? 1914 translation by H. Rackham But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?";
        switch (TYPE) {
            case "BOOKS": {
                Book item1 = new Book(items.size() + 1, (R.drawable.ic_book_1), "Book Name 1", "admin", loremIpsum, 1996, 9);
                items.add(item1);
                Book item2 = new Book(items.size() + 1, (R.drawable.ic_book_2), "Book Name 2", "author 2", loremIpsum, 1996, 9);
                items.add(item2);
                Toast.makeText(this, TYPE + " created first time", Toast.LENGTH_SHORT).show();
                break;
            }
            case "USERS":

                User admin = new User("admin", "admin", "admin@example.com", "admin", "example", "UNAL", true);
                User normal_user = new User("normal", "user", "user@example.com", "user", "normal", "UNAL");
                items.add(admin);
                items.add(normal_user);
                Toast.makeText(this, TYPE + " created first time", Toast.LENGTH_SHORT).show();
                System.out.println(((User) items.get(0)).getFirstName());
                break;
            case "LOG":
                User example = new User();
                Login actual = new Login(example, false, example.isAdmin);
                items.add(actual);
                break;
        }
        save(TYPE);
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