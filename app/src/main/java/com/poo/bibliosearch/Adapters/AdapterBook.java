package com.poo.bibliosearch.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.ViewHolder> implements View.OnClickListener, Filterable {

    LayoutInflater inflater;
    ArrayList<Book> items;
    ArrayList<Book> itemsFull;
    View.OnClickListener listener;


    public AdapterBook(Context context, ArrayList<Book> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        itemsFull = new ArrayList<>(items);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.books_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = items.get(position).getName();
        String author = items.get(position).getAuthor();
        int cover = items.get(position).getCover();
        String id = String.valueOf(items.get(position).getIdNumber());
        int cant = items.get(position).getCant();

        holder.bookName.setText(name);
        holder.bookAuthor.setText(author);
        holder.cvCover.setImageResource(cover);
        holder.id.setText("id: " + id);
        if (items.get(position).getCant() > 0) {
            holder.available.setText("Disponible");
            holder.available.setTextColor(Color.rgb(30, 215, 96));
            holder.cant.setText("Copias disponibles: " + cant);
        } else {
            holder.available.setText("No Disponible");
            holder.available.setTextColor(Color.rgb(215, 9, 20));
            holder.cant.setVisibility(View.GONE);
        }

        holder.cant.setText("Copias disponibles: " + cant);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;

    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else return 0;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);

        }

    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    public void setFilter(ArrayList<Book> books){
        
        items.clear();
        items.addAll(books);
        notifyDataSetChanged();

    }

    Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Book> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(itemsFull);
            }else{
                for(Book book:itemsFull){
                    if (book.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(book);
                    }
                }                
            }

            FilterResults filteredResults = new FilterResults();
            filteredResults.values = filteredList;
            return filteredResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        items.clear();
        items.addAll((Collection<? extends Book>) results.values);
        notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookName, bookAuthor, available;
        TextView id, cant;
        ImageView cvCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.cv_name);
            bookAuthor = itemView.findViewById(R.id.cv_b_Author);
            cvCover = itemView.findViewById(R.id.cv_b_cover);
            id = itemView.findViewById(R.id.cv_b_id);
            available = itemView.findViewById(R.id.cv_b_available_book);
            cant = itemView.findViewById(R.id.cv_b_cant);
        }
    }

}
