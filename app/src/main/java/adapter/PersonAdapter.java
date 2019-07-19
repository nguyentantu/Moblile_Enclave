package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigation.AlphalbetActivity;
import com.example.navigation.DetailEnActivity;
import com.example.navigation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Common.Common;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Person;

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Person> personList;

    public PersonAdapter(Activity context, List<Person> personList) {
        this.context = context;
        this.personList = personList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == Common.VIEWTYPE_GROUP) {
            ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.group_layout, viewGroup, false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(group);
            return groupViewHolder;
        } else if (viewType == Common.VIEWTYPE_PERSON) {
            ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.person_layout, viewGroup, false);
            PersonViewHolder personViewHolder = new PersonViewHolder(group);
            return personViewHolder;
        }
        else {
            ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.group_layout, viewGroup, false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(group);
            return groupViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return personList.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) viewHolder;
            groupViewHolder.txt_group_title.setText(personList.get(i).getName());
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start activity to display all alphabet characters to choose
                    ((Activity)context).startActivityForResult(new Intent(context, AlphalbetActivity.class), Common.RESULT_CODE);
                }
            });
        } else  if (viewHolder instanceof PersonViewHolder){
            PersonViewHolder personViewHolder = (PersonViewHolder) viewHolder;
            personViewHolder.txt_person_name.setText(personList.get(i).getName());
            personViewHolder.txt_skype.setText(personList.get(i).getSkype());

//            // Set avatar
//            ColorGenerator generator  = ColorGenerator.MATERIAL;
//            TextDrawable drawable = (TextDrawable) TextDrawable.builder().buildRound(String.valueOf(String.valueOf(personList.get(i).getName()).charAt(0))
//                    , generator.getRandomColor());
//            personViewHolder.img_person_avatar.setImageDrawable(drawable);

            final int id = personList.get(i).getId();
            final String avatar = personList.get(i).getAvatar();
            // Set avatar
            Picasso.with(context).load(avatar).into(personViewHolder.img_person_avatar);


            personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, personList.get(i).getId()+"", Toast.LENGTH_SHORT).show();
                    Context context = v.getContext();
                            Intent intent = new Intent(context, DetailEnActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("avatar",avatar);
                            context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView txt_group_title;
        LinearLayout llEngineer;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_group_title = itemView.findViewById(R.id.txt_group_title);
            llEngineer = itemView.findViewById(R.id.ll_engineer);
        }
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView txt_person_name, txt_skype;
        CircleImageView img_person_avatar;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_person_name = itemView.findViewById(R.id.txt_person_name);
            txt_skype = itemView.findViewById(R.id.txt_skype);
            img_person_avatar = itemView.findViewById(R.id.person_avatar);
        }
    }
}
