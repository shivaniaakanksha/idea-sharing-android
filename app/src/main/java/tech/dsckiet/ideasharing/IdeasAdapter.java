package tech.dsckiet.ideasharing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class IdeasAdapter extends RecyclerView.Adapter<IdeasAdapter.view_holder> {
    Context context;
    ArrayList<String> titleList, desclList, techList;

    public IdeasAdapter(Context applicationContext, ArrayList<String> titleList, ArrayList<String> desclList, ArrayList<String> techList) {
        this.context = applicationContext;
        this.titleList = titleList;
        this.desclList = desclList;
        this.techList = techList;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.idea_card, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final IdeasAdapter.view_holder holder, final int position) {
        holder.titletv.setText(titleList.get(position));
        holder.desctv.setText(desclList.get(position));
        holder.techtv.setText(techList.get(position));

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey, I have thought of Idea!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Title: " + titleList.get(position) + "\n\nDescription: " + desclList.get(position) + "\n\nTechnologies: " + techList.get(position));

                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        TextView titletv, desctv, techtv;
        CardView editBtn, deleteBtn, shareBtn;

        public view_holder(final View itemView) {
            super(itemView);

            titletv = itemView.findViewById(R.id.titletv);
            desctv = itemView.findViewById(R.id.desctv);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
//        techtv = itemView.findViewById(R.id.techtv);
        }
    }
}
