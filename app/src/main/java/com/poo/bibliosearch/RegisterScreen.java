package com.poo.bibliosearch;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;
import static com.poo.bibliosearch.LoginScreen.USERS;


public class RegisterScreen extends AppCompatActivity {

    public static ArrayList<Object> items;
    public static Type type;

    public static  SharedPreferences sharedPreferences;
    public EditText txt_user_name, txt_first_name,
            txt_last_name, txt_email,
            txt_create_password, txt_confirm_password,txt_college;

    public TextView tv_confirm_password, tv_create_password;
    public String college, confirmPassword, createPassword, email, lastName, firstName, userName;

    User aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        tv_confirm_password = findViewById(R.id.tv_confirm_password);
        tv_create_password = findViewById(R.id.tv_create_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        txt_create_password = findViewById(R.id.txt_create_password);
        txt_email = findViewById(R.id.txt_r_email);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_college = findViewById(R.id.et_college);
        txt_user_name = findViewById(R.id.txt_user_name);

    }

    // lee las entradas de el formulario y de ser entradas válidas crea el usuario
    public void ConfirmRegister(View view) {


        confirmPassword = txt_confirm_password.getText().toString();
        createPassword = txt_create_password.getText().toString();
        email = txt_email.getText().toString();
        firstName = txt_first_name.getText().toString();
        lastName = txt_last_name.getText().toString();
        userName = txt_user_name.getText().toString();


        if (validForm(email, confirmPassword, createPassword, userName)) {
            User newUser = new User(userName, createPassword, email, firstName, lastName, college);
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
            case "UNIVERSITIES":
                type = new TypeToken<ArrayList<String>>() {
                }.getType();
                break;

        }
        items = gson.fromJson(itemsJson, type);
        if (items == null) createItems(TYPE);
    }

    /*De no existir ningún dato alojado en el sharedPreferences*/
    public void createItems(String TYPE) {

        items = new ArrayList<>();
        String loremIpsum = String.valueOf((R.string.lorem_ipsum));
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
            case "UNIVERSITIES":
                String[] universidades = {"Academia de dibujo profesional", "Alexander Von Humboldt", "CECAR (Sincelejo)", "CEIPA - Medellín", "Centro Colombo Americano Manizales", "Centro Corporación Universitaria Reformada - Barranquilla", "Centro INCA", "CESDE - Medellín", "CESMAG", "Colegiatura Colombiana", "Colegio de Estudios Superiores de Administración (CESA)", "Colegio Mayor de Antioquia", "Colegio Normalistas - Manizales", "Colombo Americano - Ibagué", "Coorporación Educativa del Litoral - Barranquilla", "Corporación Unificada Nacional de la Educación Superior - CUN - Sede Cali", "Corporacion Auntonoma de Nariño- AUNAR", "Corporación Autonoma de las Américas", "Corporación Autónoma de Nariño", "Corporación Unificada Nacional de Educación Superior CUN-Sede Ibagué", "Corporación Unificada Nacional de Educación Superior CUN-Sede Neiva", "Corporación Unificada Nacional de Educación Superior-CUN-Sede Villavicencio", "Corporación Universitaria Adventista - UNAC", "Corporación Universitaria Americana", "Corporación Universitaria del Huila (CORHUILA)", "Corporación Universitaria Iberoamericana", "Corporación Universitaria empresarial de Salamanca", "Corporación Universitaria Lasallista - Sede Medellín", "Corporación universitaria Latinoamericana", "Corporación Universitaria Republicana", "Corporación Universitaria Minuto De Dios", "Corporación Universitaria Rafael Núñez", "Corporación Universitaria Remington - Sede Medellín", "Corporación Universitaria Unisabaneta", "Corposucre", "CUN - Magdalena", "CUN - Medellín", "CUN Corporaciòn Unificada Nacional de Educaciòn Superior-Bogotá", "EAM", "ESAP (Escuela Superior de Administración Pública)", "Escuela Colombiana de Carreras Industriales (ECCI)", "Escuela Colombiana de Ingeniería Julio Garavito (ECI)", "Escuela de Ingeniería de Antioquia - EIA", "ESUMER", "Fundación Autónoma Universitaria", "Fundación de Estudios Superiores Comfanorte", "Fundación Escuela Colombia De Mercadotecnia- ESCOLME- Medellín", "Fundación Escuela Superior Profesional INPAHU", "Fundación Escuela Teconologica de Neiva - FET-", "Fundación Tecnológica Antonio de Arévalo", "Fundación Universidad de Popayán", "Fundación Universitaria Bellas Artes- Medellín", "Fundación Universitaria Cafam", "Fundación Universitaria CERVANTINA", "Fundacion Universitaria de Monserrate", "Fundación Universitaria del Área Andina - Bogotá", "Fundación Universitaria del Área Andina - Pereira", "Fundacion Universitaria del Área Andina - Valledupar", "Fundación Universitaria Empresarial - Uniempresarial", "Fundación Universitaria Juan de Castellanos", "Fundación Universitaria Konrad Lorenz", "Fundación Universitaria Luis Amigó - Manizales", "Fundación Universitaria de educación superior San José", "Fundación Universitaria Luis Amigó - Medellín", "Fundación Universitaria María Cano", "Fundación Universitaria Navarra", "Fundación Universitaria San Martin - Ibagué", "Fundación Universitaria San Martín - Medellín", "Fundación Universitaria San Martin - Sede Armenia", "Fundación Universitaria San Martín - Valledupar", "Fundación Universitaria San Mateo", "Fundación Universitaria Tecnlógico de Comfenalco", "FUNDES - Espinal", "Humboldt", "INCCA", "Institución Universitaría Bellas Artes - Cali", "Institución Universitaria Colegio Mayor del Cauca", "Institución Universitaria Colegios de Colombia (UNICOC)", "Institución Universitaria de Envigado", "Institucion Universitaria Marco Fidel Suarez", "Institución Universitaria Pascual Bravo", "Institución Universitaria Salazar y Herrera", "Institución UniversitariaTecnológicade COMFACAUCA", "Instituto Meyer en Manizales", "Instituto Técnico CENIS, Boyacá", "Instituto Técnico COTEL, Boyacá", "Instituto Técnico de los Andes, Boyacá", "Instituto Técnico Superior De Artes (IDEARTES)", "Instituto Tecnológico de Soledad Atlántico - ITSA", "Instituto Tecnológico Metropolitano", "La Salle college", "Oxford - Centro de Idiomas - Ibagué", "Politécnico - Medellín", "Politecnico Central - Ibagué", "Politecnico Gran Colombiano - Medellín", "Politécnico Grancolombiano - Bogotá", "Politecnico Jaime Isaza Cadavid", "Pontificia Universidad Javeriana", "SENA - Bogotá", "SENA - Cúcuta", "SENA - Medellin", "SENA - Tolima", "SENA", "SENECA College", "Taller 5 - Bogotá", "Tecnológico de Antioquia", "TEINCO", "Tell me the way - Centro de Idiomas - Ibagué", "UAN - Bogotá", "UNAD", "UNICOC", "Unidad Central del Valle del Cauca - UCEVA", "Unidades Tecnológicas de Santander", "Unimeta", "Uniminuto - Ibagué", "UNITEC", "Universidad Agraria - Bogotá", "UNIVERSIDAD AGUSTINIANA", "Universidad Antonio Nariño", "Universidad Antonio Nariño", "Universidad Antonio Nariño", "Universidad Antonio Nariño - Ibagué", "Universidad Antonio Nariño - Pereira", "Universidad Antonio Nariño - Sede Armenia", "Universidad Antonio Nariño (UAN) - Sede Neiva", "Universidad Antonio Nariño Sede Boyacá", "Universidad Autónoma -Bogotá", "Universidad Autónoma de Bucaramanga", "Universidad Autónoma de Colombia (FUAC) - Bogotá", "Universidad Autónoma de las Américas - Medellín", "Universidad Autonoma de Manizales UAM", "Universidad Autónoma de Occidente - UAO", "Universidad Autónoma de Pereira", "Universidad Autónoma del Caribe", "Universidad Autónoma del Cauca", "Universidad Autónoma Latinoamericana (UNAULA)", "Universidad Católica - Cali", "Universidad Católica de Colombia", "Universidad Católica de Manizales", "Universidad Católica de Oriente", "Universidad Católica de Pereira", "Universidad Católica de Risaralda", "Universidad Central", "Universidad CES", "Universidad Colegio Mayor De Cundinamarca", "Universidad Cooperativa - Popayán", "Universidad Cooperativa de Colombia - Bucaramanga", "Universidad Cooperativa de Colombia - Ibagué", "Universidad Cooperativa de Colombia - Pereira", "Universidad Cooperativa de Colombia - Santa Marta", "Universidad Cooperativa de Colombia - sede Cali", "Universidad Cooperativa de Colombia - Sede Medellín", "Universidad Cooperativa de Colombia - Sede Villavicencio", "Universidad Cooperativa de Colombia – Pasto", "Universidad Cooperativa de Colombia (UCC) - Sede Neiva", "Universidad Cooperativa de Colombia UCC", "Universidad Cooperativa de Colombia-Bogotá", "Universidad Corhuila", "Universidad CUC", "Universidad de América - Bogotá", "Universidad de Antioquia", "Universidad de Boyacá", "Universidad de Caldas", "Universidad de Cartagena", "Universidad de Ciencias Aplicadas y Ambientales (UDCA)", "Universidad de Córdoba", "Universidad de Ibagué", "Universidad de Ibagué", "Universidad de Investigación y Desarrollo", "Universidad de la Guajira", "Universidad de la Sabana", "Universidad de la Salle - Bogotá", "Universidad de la Salle - Medellín", "Universidad de Los Andes", "Universidad de los Llanos", "Universidad de Manizales", "Universidad de Medellín", "Universidad de Nariño", "Universidad de Pamplona", "Universidad de San Buenaventura - Bogotá", "Universidad de San Buenaventura - Cali", "Universidad de San Buenaventura - Cartagena", "Universidad de Santander", "Universidad de Santander - Bucaramanga", "Universidad de Santander - Valledupar", "Universidad de Sucre", "Universidad del Altántico", "Universidad del Cauca", "Universidad del Magdalena", "Universidad del Norte", "Universidad del Quindío", "Universidad del Rosario", "Universidad del Sinú", "Universidad del Sinú", "Universidad del Tolima", "Universidad del Valle - Cali", "Universidad del Valle - Tuluá", "Universidad Distrital Francisco José de Caldas", "Universidad EAFIT", "Universidad EAN", "Universidad ECCI", "Universidad El Bosque", "Universidad Externado De Colombia", "Universidad Francisco de Paula Santander", "Universidad ICESI", "Universidad Industrial de Santander", "Universidad Jorge Tadeo Lozano", "Universidad Jorge Tadeo Lozano - Caribe", "Universidad La Gran Colombia", "Universidad la Gran Colombia - Armenia", "Universidad La Mariana - Pasto", "Universidad Libre - Barranquilla", "Universidad Libre - Bogotá", "Universidad Libre - Cali", "Universidad Libre - Cartagena", "Universidad Libre - Pereira", "Universidad Libre – Cúcuta", "Universidad Los libertadores - Bogotá", "Universidad Manuela Beltrán-Bogotá", "Universidad Manuela Beltrán-ITAE- Bucaramanga", "Universidad Maria Cano", "Universidad Militar Nueva Granada (UMNG)", "Universidad Minuto de Dios", "Universidad Minuto De Dios - Bogotá", "Universidad Minuto De Dios - Medellín", "Universidad Minuto de Dios - Sede Neiva", "Universidad Nacional a Distancia - Ibagué", "Universidad Nacional Abierta y a Distancia - Valledupar", "Universidad Nacional Abierta y a Distancia -Bogota", "Universidad Nacional de Colombia - Bogotá", "Universidad Nacional de Colombia - Medellín", "Universidad Nacional de Colombia- Manizales", "Universidad Navarra, Uninavarra", "Universidad Panamericana - Bogotá", "Universidad Pedagógica Nacional - Bogota", "Universidad Pedagógica y Tecnológica de Colombia", "Universidad Piloto de Colombia", "Universidad Pontificia Bolivariana - Bucaramanga", "Universidad Pontificia Bolivariana - Medellín", "Universidad Pontificia Bolivariana - Montería", "Universidad Pontificia Javeriana", "Universidad Popular del Cesar", "Universidad Remington - Tuluá", "Universidad Remington sede Tunja", "Universidad San Buenaventura - Sede Armenia", "Universidad San Buenaventura - Sede Medellin", "Universidad San Martín - Bogotá", "Universidad San Martín - Pasto", "Universidad Santiago de Cali", "Universidad Santo Tomás", "Universidad Santo Tomás - Bucaramanga", "Universidad Santo Tomás - Valledupar", "Universidad Santo Tomás - Villavicencio", "Universidad Santo Tomas sede Tunja", "Universidad Sergio Arboleda - Bogotá", "Universidad Sergio Arboleda - Santa Marta", "Universidad Sergio Arboleda -Barranquilla", "Universidad Simón Bolivar - Barranquilla", "Universidad Simón Bolivar - Cúcuta", "Universidad Surcolombiana (USCO)"};
                items.addAll(Arrays.asList(universidades));
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