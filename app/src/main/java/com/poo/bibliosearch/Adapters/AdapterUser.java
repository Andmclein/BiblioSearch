package com.poo.bibliosearch.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poo.bibliosearch.Entities.Login;
import com.poo.bibliosearch.R;
import com.poo.bibliosearch.Entities.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.LOG;
import static com.poo.bibliosearch.LoginScreen.SHARED_PREFS;
import static com.poo.bibliosearch.LoginScreen.USERS;
import static com.poo.bibliosearch.RegisterScreen.sharedPreferences;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> implements View.OnClickListener {

    LayoutInflater inflater;
    View.OnClickListener listener;
    int position;
    String name;
    String email;
    String college;
    String username;
    Boolean isActive;
    ArrayList<User> users;
    ArrayList<Login> logins;
    Type type;
    SharedPreferences sharedPreferences;
    Context context;


    public AdapterUser(Context context, ArrayList<User> items) {
        this.inflater = LayoutInflater.from(context);
        this.users = items;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.users_list, parent, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        name = users.get(position).getUserName();
        email = users.get(position).getEmail();
        college = users.get(position).getCollege();
        username = users.get(position).getUserName();
        isActive = users.get(position).getActive();

        holder.cvName.setText(name);
        holder.cvUserName.setText(username);
        holder.cvCollege.setText(college);
        holder.cvEmail.setText(email);

        if (!users.get(position).isAdmin) {
            if (isActive) {
                holder.cvIsActive.setTextColor(Color.rgb(30, 215, 96));
                holder.cvIsActive.setText("Activo");
                holder.cvDesactivar.setVisibility(View.VISIBLE);
                holder.cvActivar.setVisibility(View.GONE);

            } else if (!isActive){
                holder.cvIsActive.setTextColor(Color.rgb(215, 9, 20));
                holder.cvIsActive.setText("Inactivo");
                holder.cvActivar.setVisibility(View.VISIBLE);
                holder.cvDesactivar.setVisibility(View.GONE);
            }
        }else {
            holder.cvIsActive.setText("");
            holder.cvActivar.setVisibility(View.GONE);
            holder.cvDesactivar.setVisibility(View.GONE);
        }
        holder.cvDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUsers();
                users.get(position).setActive(false);
                saveUsers();
                System.out.println("usuario desactivado");
                notifyDataSetChanged();
            }
        });

        holder.cvActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUsers();
                users.get(position).setActive(true);
                saveUsers();
                System.out.println("usuario activado");
                notifyDataSetChanged();
            }
        });

        holder.cvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLog();
                final String aux = users.get(position).getEmail();
                if (aux.equals(logins.get(0).getUser().getEmail())) {
                    Toast.makeText(v.getRootView().getContext(), "no puede eliminar su propio usuaro", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("Confirmación");
                    builder.setMessage("¿Seguro que desea eliminar este usuario?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("eliminando " + aux);
                            users.remove(position);
                            notifyDataSetChanged();
                            saveUsers();

                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false)
                            .show();

                }
            }
        });
    }



    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        } else return 0;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cvName, cvUserName, cvEmail, cvCollege, cvIsActive;
        Button cvActivar, cvEliminar, cvDesactivar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvName = itemView.findViewById(R.id.cv_u_name);
            cvUserName = itemView.findViewById(R.id.cv_u_username);
            cvEmail = itemView.findViewById(R.id.cv_u_email);
            cvCollege = itemView.findViewById(R.id.cv_u_college);
            cvIsActive = itemView.findViewById(R.id.cv_u_isActive);
            cvActivar = itemView.findViewById(R.id.cv_u_btn_activate_user);
            cvDesactivar = itemView.findViewById(R.id.cv_u_btn_deactivate_user);
            cvEliminar = itemView.findViewById(R.id.cv_u_btn_delete_user);

        }
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


    public void saveUsers() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String itemJson = gson.toJson(users);
        editor.putString(USERS, itemJson);
        editor.apply();
    }
}
