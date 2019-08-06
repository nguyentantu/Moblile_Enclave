package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigation.R;
import com.example.navigation.TeamActivity;
import com.example.navigation.TeamDetailActivity;

import java.util.ArrayList;

import model.Teams;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ExampleViewHolder>{

    private ArrayList<Teams> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameTeam;
        public TextView txtNameProject;
        public TextView txtTotalMember;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            txtNameTeam = itemView.findViewById(R.id.txt_nameTeam);
            txtNameProject = itemView.findViewById(R.id.txt_nameProject);
            txtTotalMember = itemView.findViewById(R.id.txt_totalMember);
        }
    }

    public TeamAdapter(TeamActivity mainActivity, ArrayList<Teams> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.teamsitem,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder viewHolder, int i) {
        final Teams currentItem = mExampleList.get(i);
        viewHolder.txtNameTeam.setText(currentItem.getName());
        viewHolder.txtNameProject.setText(currentItem.getProjectName());
        viewHolder.txtTotalMember.setText(currentItem.getTotalMember()+"");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TeamDetailActivity.class);
                intent.putExtra("id", currentItem.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<Teams> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}
