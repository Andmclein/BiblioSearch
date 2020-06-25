package com.poo.bibliosearch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.R;

import java.util.ArrayList;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.ViewHolder> implements View.OnClickListener {

    LayoutInflater inflater;
    ArrayList<Book> items;
    View.OnClickListener listener;


    public AdapterBook(Context context, ArrayList<Book> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.books_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = items.get(position).getName();
        String author = items.get(position).getAuthor();
        int cover = items.get(position).getCover();
        String id = String.valueOf(items.get(position).getIdNumber());

        holder.bookName.setText(name);
        holder.bookAuthor.setText(author);
        holder.cvCover.setImageResource(cover);
        holder.id.setText("id: "+id);
    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }else return 0;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookName, bookAuthor;
        TextView id;
        ImageView cvCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.cv_name);
            bookAuthor = itemView.findViewById(R.id.cv_b_Author);
            cvCover = itemView.findViewById(R.id.cv_b_cover);
            id = itemView.findViewById(R.id.cv_b_id);
        }
    }
}
