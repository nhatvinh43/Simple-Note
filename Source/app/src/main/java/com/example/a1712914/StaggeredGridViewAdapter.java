package com.example.a1712914;

import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class StaggeredGridViewAdapter extends RecyclerView.Adapter<SampleViewHolders> implements Filterable {
    public List<Note> itemList;
    public List<Note> filteredItemList;
    private Context context;
    private Activity activity;
    private Note mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private int deletable = 1;



    public StaggeredGridViewAdapter(Context context, Activity activity, List<Note> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        this.activity = activity;
        this.filteredItemList = itemList;
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview_item, null);
        SampleViewHolders rcv = new SampleViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position)
    {
        holder.gv_title.setText(filteredItemList.get(position).getTitle());
        holder.gv_date.setText(filteredItemList.get(position).getDate());
        holder.gv_content.setText(filteredItemList.get(position).getContent());
        holder.gv_tags.setText(filteredItemList.get(position).getTags());
        holder.gv_align = filteredItemList.get(position).getAlign();
    }

    @Override
    public int getItemCount()
    {
        if(filteredItemList==null)
        {
            return 0;
        }
        return this.filteredItemList.size();
    }

    public void deleteItem(int position) {
        if(deletable==0)
        {
            return;
        }
        mRecentlyDeletedItem = this.filteredItemList.get(position);
        mRecentlyDeletedItemPosition = position;
        this.filteredItemList.remove(position);

        notifyItemRemoved(position);

        SharedPreferences sharedPreference=activity.getSharedPreferences ("note",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String data = gson.toJson(this.filteredItemList);
        editor.putString("Note",data);
        editor.commit();
        this.itemList = this.filteredItemList;
        showUndoSnackbar();

    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        filteredItemList.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);

        SharedPreferences sharedPreference=activity.getSharedPreferences ("note",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String data = gson.toJson(this.filteredItemList);
        editor.putString("Note",data);
        editor.commit();
        this.itemList = this.filteredItemList;

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredItemList = itemList;
                    deletable =1;
                } else {
                    deletable = 0;
                    List<Note> filteredList = new ArrayList<>();
                    for (Note row : itemList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getTags().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                        filteredItemList = filteredList;
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItemList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItemList = (ArrayList<Note>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}
