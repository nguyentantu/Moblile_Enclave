package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.navigation.DetailProjectActivity;
import com.example.navigation.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Project;

public class ProjectAdapter extends ArrayAdapter<Project> {

    Activity context;
    int resource;

    public ProjectAdapter(@NonNull Activity context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource, null);

        TextView txtNameproject = view.findViewById(R.id.txt_nameProject);
        //TextView txtTechnology = view.findViewById(R.id.txt_technology);
        TextView txtEarning = view.findViewById(R.id.txt_earning);
        TextView txtCategory = view.findViewById(R.id.txt_category);
        TextView txtStatus = view.findViewById(R.id.txt_status);
        LinearLayout llProject = view.findViewById(R.id.ll_project);
        CircleImageView img_language = view.findViewById(R.id.img_language);

        //CircleImageView imageView = view.findViewById(R.id.img_status);

        final Project project = getItem(position);
        txtNameproject.setText(project.getNameProject());
        //txtTechnology.setText(project.getTechnology());

        NumberFormat currentLocale = NumberFormat.getInstance();
        String earning = currentLocale.format(project.getEarning());

        txtEarning.setText(earning+" VND");
        txtCategory.setText(project.getCategory());
        txtStatus.setText(project.getStatus().toUpperCase());

        if (project.getStatus().toUpperCase().equals("PENDING")) {
            txtStatus.setBackgroundColor(Color.YELLOW);
        } else if (project.getStatus().toUpperCase().equals("DONE")){
            txtStatus.setBackgroundColor(Color.RED);
        } else if (project.getStatus().toUpperCase().equals("INPROGRESS")){
            txtStatus.setBackgroundColor(Color.GREEN);
        }

        String language = project.getTechnology().toString();

        switch (language){
            case "Swift":
                Picasso.with(context).load(R.drawable.swift).into(img_language);
                break;
            case "TypeScript":
                Picasso.with(context).load(R.drawable.typescript).into(img_language);
                break;
            case "React Native":
                Picasso.with(context).load(R.drawable.react_native).into(img_language);
                break;
            case "Angular JS":
                Picasso.with(context).load(R.drawable.angular).into(img_language);
                break;
            case "VueJS":
                Picasso.with(context).load(R.drawable.vuejs).into(img_language);
                break;
            case "Go lang":
                Picasso.with(context).load(R.drawable.go_lang).into(img_language);
                break;
            case "Objective-C":
                Picasso.with(context).load(R.drawable.object_c).into(img_language);
                break;
            case "R":
                Picasso.with(context).load(R.drawable.r).into(img_language);
                break;
            case "Nodejs":
                Picasso.with(context).load(R.drawable.nodejs).into(img_language);
                break;
            case "PHP":
                Picasso.with(context).load(R.drawable.php).into(img_language);
                break;
            case "JavaScript":
                Picasso.with(context).load(R.drawable.javascript).into(img_language);
                break;
            case "Java":
                Picasso.with(context).load(R.drawable.java).into(img_language);
                break;
            case "C/C++":
                Picasso.with(context).load(R.drawable.cccc).into(img_language);
                break;
            case "Python":
                Picasso.with(context).load(R.drawable.python).into(img_language);
                break;
            case "Ruby":
                Picasso.with(context).load(R.drawable.ruby).into(img_language);
                break;
            case "Visual Basic":
                Picasso.with(context).load(R.drawable.visual_basic).into(img_language);
                break;
            case "Kotlin":
                Picasso.with(context).load(R.drawable.kotlin).into(img_language);
                break;
            case "Perl":
                Picasso.with(context).load(R.drawable.perl).into(img_language);
                break;
            case "ReactJS":
                Picasso.with(context).load(R.drawable.react_native).into(img_language);
                break;
            case "C#":
                Picasso.with(context).load(R.drawable.c_sharp).into(img_language);
                break;
           default:
               break;
        }


        llProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context2 = v.getContext();
                Intent intent = new Intent(context2, DetailProjectActivity.class);
                intent.putExtra("id", project.getId());
                context2.startActivity(intent);

                Log.e("idadapter", project.getId()+"hhhhh");
            }
        });

        return view;
    }
}