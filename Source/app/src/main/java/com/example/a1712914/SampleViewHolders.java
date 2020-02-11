package com.example.a1712914;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class SampleViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView gv_title, gv_date, gv_content, gv_tags;
    int gv_align;

    public SampleViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        gv_title = (TextView) itemView.findViewById(R.id.gv_title);
        gv_date = (TextView) itemView.findViewById(R.id.gv_date);
        gv_content = (TextView) itemView.findViewById(R.id.gv_content);
        gv_tags = (TextView) itemView.findViewById(R.id.gv_tags);
    }

    @Override
    public void onClick(View view)
    {
        Context context = view.getContext();
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("MODE", 2);
        intent.putExtra("POSITION",getAdapterPosition());
        intent.putExtra("ALIGN", this.gv_align);
        context.startActivity(intent);
    }
}
