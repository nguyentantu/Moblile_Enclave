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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.navigation.DetailEnActivity;
import com.example.navigation.R;

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

        TextView txtId = (TextView) view.findViewById(R.id.txt_id);
        TextView txtName = (TextView) view.findViewById(R.id.txt_name);
//        TextView txtUserName = (TextView) view.findViewById(R.id.txt_username);
//        TextView txtEmail = (TextView) view.findViewById(R.id.txt_email);
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.ll_main);

        final Engineers engineers =getItem(position);


        txtId.setText(engineers.getId()+"");
        txtName.setText(engineers.getName());
//        txtUserName.setText(engineers.getUsername());
//        txtEmail.setText(engineers.getEmail());

        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<=position;i++){
                    if (position==i){
                        Context context = view.getContext();
                        Intent intent = new Intent(context, DetailEnActivity.class);
                        intent.putExtra("id", engineers.getId());
                        context.startActivity(intent);
                    }
                }
            }
        });



        return view;
    }
}
