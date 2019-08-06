package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navigation.DetailProjectActivity;
import com.example.navigation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Project;

public class Project2Adapter extends RecyclerView.Adapter<Project2Adapter.ExampleViewHolder>{
    private Context context;

    private ArrayList<Project> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameTeam;
        public TextView txtNameProject;
        public TextView txtTotalMember, technology, txtStatus;
        CircleImageView imgLanguage;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            txtNameTeam = itemView.findViewById(R.id.txt_nameProject);
            txtNameProject = itemView.findViewById(R.id.txt_category);
            txtTotalMember = itemView.findViewById(R.id.txt_earning);
            imgLanguage = itemView.findViewById(R.id.img_language);
            technology = itemView.findViewById(R.id.txt_technology);
            txtStatus = itemView.findViewById(R.id.txt_status);
        }
    }

    public Project2Adapter(Context context, ArrayList<Project> exampleList) {
        mExampleList = exampleList;
        this.context = context;
    }

    @Override
    public Project2Adapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item,
                parent, false);
        Project2Adapter.ExampleViewHolder evh = new Project2Adapter.ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull Project2Adapter.ExampleViewHolder viewHolder, int i) {
        final Project currentItem = mExampleList.get(i);
        viewHolder.txtNameTeam.setText(currentItem.getNameProject());
        viewHolder.txtNameProject.setText(currentItem.getCategory());
        viewHolder.txtTotalMember.setText(currentItem.getEarning()+"");
        viewHolder.technology.setText(currentItem.getTechnology());

        if (currentItem.getStatus().toUpperCase().equals("INPROGRESS")){
            viewHolder.txtStatus.setText(currentItem.getStatus().toUpperCase());
            viewHolder.txtStatus.setBackgroundColor(Color.GREEN);
        } else  if (currentItem.getStatus().toUpperCase().equals("DONE")){
            viewHolder.txtStatus.setText(currentItem.getStatus().toUpperCase());
            viewHolder.txtStatus.setBackgroundColor(Color.RED);
        } else  if (currentItem.getStatus().toUpperCase().equals("PENDING")){
            viewHolder.txtStatus.setText(currentItem.getStatus().toUpperCase());
            viewHolder.txtStatus.setBackgroundColor(Color.YELLOW);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailProjectActivity.class);
                intent.putExtra("id", currentItem.getId());
                context.startActivity(intent);
            }
        });

        String language = currentItem.getTechnology();
        switch (language){
            case "Swift":
                Picasso.with(context).load(R.drawable.swift).into(viewHolder.imgLanguage);
                break;
            case "TypeScript":
                Picasso.with(context).load(R.drawable.typescript).into(viewHolder.imgLanguage);
                break;
            case "React Native":
                Picasso.with(context).load(R.drawable.react_native).into(viewHolder.imgLanguage);
                break;
            case "Angular JS":
                Picasso.with(context).load(R.drawable.angular).into(viewHolder.imgLanguage);
                break;
            case "VueJS":
                Picasso.with(context).load(R.drawable.vuejs).into(viewHolder.imgLanguage);
                break;
            case "Go lang":
                Picasso.with(context).load(R.drawable.go_lang).into(viewHolder.imgLanguage);
                break;
            case "Objective-C":
                Picasso.with(context).load(R.drawable.object_c).into(viewHolder.imgLanguage);
                break;
            case "R":
                Picasso.with(context).load(R.drawable.r).into(viewHolder.imgLanguage);
                break;
            case "Nodejs":
                viewHolder.technology.setVisibility(View.GONE);
                Picasso.with(context).load(R.drawable.nodejs).into(viewHolder.imgLanguage);
                break;
            case "PHP":
                viewHolder.technology.setVisibility(View.GONE);
                Picasso.with(context).load(R.drawable.php).into(viewHolder.imgLanguage);
                break;
            case "JavaScript":
                Picasso.with(context).load(R.drawable.javascript).into(viewHolder.imgLanguage);
                break;
            case "Java":
                viewHolder.technology.setVisibility(View.GONE);
                Picasso.with(context).load(R.drawable.java).into(viewHolder.imgLanguage);
                break;
            case "C/C++":
                viewHolder.technology.setVisibility(View.GONE);
                Picasso.with(context).load(R.drawable.cccc).into(viewHolder.imgLanguage);
                break;
            case "Python":
                Picasso.with(context).load(R.drawable.python).into(viewHolder.imgLanguage);
                break;
            case "Ruby":
                Picasso.with(context).load(R.drawable.ruby).into(viewHolder.imgLanguage);
                break;
            case "Visual Basic":
                Picasso.with(context).load(R.drawable.visual_basic).into(viewHolder.imgLanguage);
                break;
            case "Kotlin":
                Picasso.with(context).load(R.drawable.kotlin).into(viewHolder.imgLanguage);
                break;
            case "Perl":
                Picasso.with(context).load(R.drawable.perl).into(viewHolder.imgLanguage);
                break;
            case "ReactJS":
                Picasso.with(context).load(R.drawable.react_native).into(viewHolder.imgLanguage);
                break;
            case "C#":
                viewHolder.technology.setVisibility(View.GONE);
                Picasso.with(context).load(R.drawable.c_sharp).into(viewHolder.imgLanguage);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<Project> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}
