package com.poo.bibliosearch.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.poo.bibliosearch.Entities.Book;
import com.poo.bibliosearch.R;


public class BookDetailFragment extends Fragment {

    TextView name, author, releaseYear, id, description;
    ImageView cover;
    RatingBar rating;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_book_fragment, container, false);
        name = view.findViewById(R.id.detail_book_name);
        author = view.findViewById(R.id.detail_book_author);
        releaseYear = view.findViewById(R.id.detail_book_release);
        description = view.findViewById(R.id.detail_book_description);
        id = view.findViewById(R.id.detail_book_id);
        cover = view.findViewById(R.id.detail_book_cover);
        rating = view.findViewById(R.id.detail_book_ratingBar);
//        crear objeto bundle para recibir objeto enviado
        Bundle bundleIn = getArguments();
        Book book = null;
//        validacion de argumentos para mostrar
        if (bundleIn != null) {
//
            book = (Book) bundleIn.getSerializable("book");
//        establecer los datos en las vistas
            name.setText(book.getName());
            author.setText(book.getAuthor());
//            releaseYear.setText((book.getReleaseYear()));
            description.setText(book.getDescription());
//            id.setText((book.getIdNumber()));
            cover.setImageResource(book.getCover());
            rating.setRating(book.getRating());
//            rating.setClickable(false);
        }

        return view;
    }


}
