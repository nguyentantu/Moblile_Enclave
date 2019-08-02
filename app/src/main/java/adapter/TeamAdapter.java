package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navigation.R;
import com.example.navigation.TeamActivity;

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
            //mImageView = itemView.findViewById(R.id.imageView);
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
        Teams currentItem = mExampleList.get(i);

        //holder.mImageView.setImageResource(currentItem.getImageResource());
        viewHolder.txtNameTeam.setText(currentItem.getName());
        viewHolder.txtNameProject.setText(currentItem.getProjectName());
        viewHolder.txtTotalMember.setText(currentItem.getTotalMember()+"");
    }

//    @Override
//    public void onBindViewHolder(ExampleViewHolder holder, int position) {
//        Teams currentItem = mExampleList.get(position);
//
//        //holder.mImageView.setImageResource(currentItem.getImageResource());
//        holder.txtNameTeam.setText(currentItem.getName());
//        holder.txtNameProject.setText(currentItem.getProjectName());
//        holder.txtTotalMember.setText(currentItem.getTotalMember()+"");
//    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<Teams> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}

//    Context context;
//    ArrayList<Teams> teamsList;
//    ArrayList<Teams> teamsListFull;
//
//    public TeamAdapter(Activity context, ArrayList<Teams> teamsList) {
//        this.context = context;
//        this.teamsList = teamsList;
//    }
//
//
//    @Override
//    public int getItemViewType(int position) {
//        return teamsList.get(position).getId();
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.teamsitem, viewGroup, false);
//        TeamAdapter.PersonViewHolder personViewHolder = new TeamAdapter.PersonViewHolder(group);
//        return personViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
//         if (viewHolder instanceof TeamAdapter.PersonViewHolder){
//            TeamAdapter.PersonViewHolder personViewHolder = (TeamAdapter.PersonViewHolder) viewHolder;
//            personViewHolder.txtName.setText(teamsList.get(i).getName());
//            personViewHolder.txtProjectname.setText(teamsList.get(i).getProjectName());
//            personViewHolder.txtTotalMember.setText(teamsList.get(i).getTotalMember()+"");
//
//            final int id = teamsList.get(i).getId();
////            final String avatar = teamsList.get(i).getAvatar();
//            // Set avatar
////            Picasso.with(context).load(avatar).into(personViewHolder.img_person_avatar);
////
////            personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Context context = v.getContext();
////                    Intent intent = new Intent(context, DetailEnActivity.class);
////                    intent.putExtra("id", id);
////                    intent.putExtra("avatar",avatar);
////                    context.startActivity(intent);
////                }
////            });
//       }
//    }
//
//    @Override
//    public int getItemCount() {
//        return teamsList.size();
//    }
//
//
//    private class PersonViewHolder extends RecyclerView.ViewHolder {
//        TextView txtName, txtProjectname, txtTotalMember;
//        public PersonViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txtName = itemView.findViewById(R.id.txt_nameTeam);
//            txtProjectname = itemView.findViewById(R.id.txt_nameProject);
//            txtTotalMember = itemView.findViewById(R.id.txt_totalMember);
//        }
//    }
//}

