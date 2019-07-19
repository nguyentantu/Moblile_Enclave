package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.navigation.DetailEnActivity;
import com.example.navigation.R;
import com.squareup.picasso.Picasso;


import model.Engineers;

public class MainAdapter extends ArrayAdapter<Engineers> {


    Activity context;
    int resource;

    public MainAdapter(@NonNull Activity context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource, null);

        TextView txtName = (TextView) view.findViewById(R.id.txt_name);
        TextView txtSkype = (TextView) view.findViewById(R.id.txt_skype);
        TextView my_letter = (TextView) view.findViewById(R.id.my_letter);
//        TextView txtEmail = (TextView) view.findViewById(R.id.txt_email);
        ImageView imgAva = view.findViewById(R.id.img_ava);
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.ll_main);

        final Engineers engineers =getItem(position);


        txtSkype.setText(engineers.getSkype()); //id 1
        txtName.setText(engineers.getName());

        if (engineers.getAvatar() != null){
            Picasso.with(context).load(engineers.getAvatar()).into(imgAva);
            my_letter.setVisibility(View.GONE);
        }else {
            my_letter.setText(String.valueOf(txtName.getText().toString().charAt(0)));
            imgAva.setVisibility(View.GONE);
        }

        my_letter.setText(String.valueOf(txtName.getText().toString().charAt(0)));

        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<=position;i++){
                    if (position==i){
                        Context context = view.getContext();
                        Intent intent = new Intent(context, DetailEnActivity.class);
                        intent.putExtra("id", engineers.getId());
                        intent.putExtra("avata", engineers.getAvatar());
                        context.startActivity(intent);
                    }
                }
            }
        });

        return view;
    }
}
