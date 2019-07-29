package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navigation.R;

import java.util.ArrayList;

import model.Teams;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    ArrayList<Teams> teamsList;
    ArrayList<Teams> teamsListFull;

    public TeamAdapter(Activity context, ArrayList<Teams> teamsList) {
        this.context = context;
        this.teamsList = teamsList;
    }


    @Override
    public int getItemViewType(int position) {
        return teamsList.get(position).getId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.teamsitem, viewGroup, false);
        TeamAdapter.PersonViewHolder personViewHolder = new TeamAdapter.PersonViewHolder(group);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
         if (viewHolder instanceof TeamAdapter.PersonViewHolder){
            TeamAdapter.PersonViewHolder personViewHolder = (TeamAdapter.PersonViewHolder) viewHolder;
            personViewHolder.txtName.setText(teamsList.get(i).getName());
            personViewHolder.txtProjectname.setText(teamsList.get(i).getProjectName());
            personViewHolder.txtTotalMember.setText(teamsList.get(i).getTotalMember()+"");

            final int id = teamsList.get(i).getId();
//            final String avatar = teamsList.get(i).getAvatar();
            // Set avatar
//            Picasso.with(context).load(avatar).into(personViewHolder.img_person_avatar);
//
//            personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DetailEnActivity.class);
//                    intent.putExtra("id", id);
//                    intent.putExtra("avatar",avatar);
//                    context.startActivity(intent);
//                }
//            });
       }
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }


    private class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtProjectname, txtTotalMember;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_nameTeam);
            txtProjectname = itemView.findViewById(R.id.txt_nameProject);
            txtTotalMember = itemView.findViewById(R.id.txt_totalMember);
        }
    }
}

