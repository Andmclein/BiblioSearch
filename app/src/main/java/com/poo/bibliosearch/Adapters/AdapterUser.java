package com.poo.bibliosearch.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poo.bibliosearch.R;
import com.poo.bibliosearch.Entities.User;

import java.util.ArrayList;

import static com.poo.bibliosearch.LoginScreen.USERS;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> implements View.OnClickListener {

    LayoutInflater inflater;
    ArrayList<User> items;
    View.OnClickListener listener;
int position;
    String name;
    String email;
    String college;
    String username;
    Boolean isActive;
    User user;


    public AdapterUser(Context context, ArrayList<User> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    public void DeleteUser(int position) {
        items.remove(position);
        notifyItemRemoved(position);


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

        this.position = position;
        name = items.get(position).getUserName();
        email = items.get(position).getEmail();
        college = items.get(position).getCollege();
        username = items.get(position).getUserName();
        isActive = items.get(position).getActive();

        holder.cvName.setText(name);
        holder.cvUserName.setText(username);
        holder.cvCollege.setText(college);
        holder.cvEmail.setText(email);

        if (isActive) {
            holder.cvIsActive.setTextColor(Color.rgb(0, 255, 0));
            holder.cvIsActive.setText("Activo");
            holder.cvActivar.setVisibility(View.GONE);

        } else {
            holder.cvIsActive.setTextColor(Color.rgb(255, 0, 0));
            holder.cvIsActive.setText("Inactivo");
            holder.cvActivar.setVisibility(View.VISIBLE);
        }

        holder.cvActivar.setOnClickListener(this);
        holder.cvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String aux = items.get(position).getEmail();
                System.out.println("eliminando "+ aux);
                items.remove(position);
                notifyItemRemoved(position);


            }
        });

    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else return 0;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case (R.id.cv_u_btn_delete_user):
//                for (int i = 0;i<items.size();i++){
//
//           }

                break;
            case (R.id.cv_u_btn_activate_user):
                break;
            default:
                if (listener != null) {
                    listener.onClick(view);

                }
                break;

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cvName, cvUserName, cvEmail, cvCollege, cvIsActive;
        Button cvActivar, cvEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvName = itemView.findViewById(R.id.cv_u_name);
            cvUserName = itemView.findViewById(R.id.cv_u_username);
            cvEmail = itemView.findViewById(R.id.cv_u_email);
            cvCollege = itemView.findViewById(R.id.cv_u_college);
            cvIsActive = itemView.findViewById(R.id.cv_u_isActive);
            cvActivar = itemView.findViewById(R.id.cv_u_btn_activate_user);
            cvEliminar = itemView.findViewById(R.id.cv_u_btn_delete_user);

        }
    }
}
