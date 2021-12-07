package com.example.movierecommender;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<Movie> itemList;
    //public final Context context;

    // Constructor of the class
    public ItemArrayAdapter(int layoutId, ArrayList<Movie> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.itemText;
        ImageView itemImage= holder.itemImage;;
        TextView itemId= holder.itemId;
        item.setText(itemList.get(listPosition).getTitle());

       // Picasso.get().load("https://i.imgur.com/DvpvklR.png").noPlaceholder().into(holder.itemImage);
       Picasso.get().load("https://image.tmdb.org/t/p/original/"+itemList.get(listPosition).getPoster()).placeholder(R.drawable.ic_launcher_foreground).into(holder.itemImage);
        itemId.setText(itemList.get(listPosition).getId());
        Log.d("picasso"," "+itemList.get(listPosition).getPoster());

        itemImage.setVisibility(View.VISIBLE);
        //holder.onClick(holder.item);
        if (listPosition == itemList.size() - 1) {
            // Reached End of List
           // LoadApi lApi = new LoadApi();
            //lApi.getMoviesFromApi(lApi.getResponse(listPosition/20 +1));
        }
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemText;
        public ImageView itemImage;
        public TextView itemId;
        public final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemText = (TextView) itemView.findViewById(R.id.txtTitle);
            itemImage=(ImageView) itemView.findViewById(R.id.itemImage);
            itemId=(TextView) itemView.findViewById(R.id.textView2);
            context=itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + itemText.getText());
            final Intent intent=new Intent(context,MovieInfoActivity.class);
            intent.putExtra("id", itemId.getText());

            context.startActivity(intent);
        }
    }
}
