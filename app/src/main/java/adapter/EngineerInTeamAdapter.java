package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigation.DetailEnActivity;
import com.example.navigation.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Person;

public class EngineerInTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int resource;
    Context context;
    ArrayList<Person> personsList;

    public EngineerInTeamAdapter(Activity context, ArrayList<Person> personsList) {
        this.context = context;
        this.personsList = personsList;
    }

    public void  updatePersonList(ArrayList<Person> personsList) {
        this.personsList = personsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.person_inteam_layout, viewGroup, false);
        EngineerInTeamAdapter.PersonViewHolder personViewHolder = new EngineerInTeamAdapter.PersonViewHolder(group);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof EngineerInTeamAdapter.PersonViewHolder){
            EngineerInTeamAdapter.PersonViewHolder personViewHolder = (EngineerInTeamAdapter.PersonViewHolder) viewHolder;
            personViewHolder.txtName.setText(personsList.get(i).getLastName());
            personViewHolder.txtEmail.setText(personsList.get(i).getEmail());
            //personViewHolder.txtRole.setText(personsList.get(i).getRole());
            NumberFormat currentLocale = NumberFormat.getInstance();
            String salary = currentLocale.format(personsList.get(i).getSalary());
            personViewHolder.txtSalary.setText(salary+" VND");
            if (personsList.get(i).getExperienceYear() < 3){
                personViewHolder.txtExperienceYear.setText("SW 1");
            } else if (personsList.get(i).getExperienceYear() < 5){
                personViewHolder.txtExperienceYear.setText("SW 2");
            }
            else if (personsList.get(i).getExperienceYear() < 7){
                personViewHolder.txtExperienceYear.setText("SW 3");
            } else {
                personViewHolder.txtExperienceYear.setText("SW 4");
            }
            final String avatar = personsList.get(i).getAvatar();
            // Set avatar
            Picasso.get().load(avatar).into(personViewHolder.imgPerson);

            if (personsList.get(i).getRole().toLowerCase().equals("developer") ){
                personViewHolder.txtRole.setBackgroundColor(Color.parseColor("#36c6d3"));
                personViewHolder.txtRole.setText("DEV");
            } else if (personsList.get(i).getRole().toLowerCase().equals("leader") ){
                personViewHolder.txtRole.setBackgroundColor(Color.parseColor("#eb2f06"));
                personViewHolder.txtRole.setText(personsList.get(i).getRole().toUpperCase());
            } else if (personsList.get(i).getRole().toLowerCase().equals("quality assurance") ){
                personViewHolder.txtRole.setText("QA");
                personViewHolder.txtRole.setBackgroundColor(Color.parseColor("#36c6d3"));
            }
            final int id = personsList.get(i).getIdPerson();
            personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        return personsList.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtRole, txtSalary, txtExperienceYear;
        CircleImageView imgPerson;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_person_name);
            txtEmail = itemView.findViewById(R.id.txt_skype);
            imgPerson = itemView.findViewById(R.id.person_avatar);
            txtRole = itemView.findViewById(R.id.txt_role);
            txtSalary = itemView.findViewById(R.id.txt_salary);
            txtExperienceYear = itemView.findViewById(R.id.txt_experienceYear);
        }
    }
}