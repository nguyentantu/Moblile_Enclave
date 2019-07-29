package adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.navigation.R;

import java.util.List;

import Common.Common;
import Interface.IOnAlphabetClickListenner;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.MyViewHolder> {

    List<String> alphabetList;
    IOnAlphabetClickListenner iOnAlphabetClickListenner;

    public void setiOnAlphabetClickListenner(IOnAlphabetClickListenner iOnAlphabetClickListenner) {
        this.iOnAlphabetClickListenner = iOnAlphabetClickListenner;
    }

    public AlphabetAdapter() {
        alphabetList = Common.genAlphabet();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alphabet_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        TextDrawable drawable;
        // Get available position of characters
        final int available_position = Common.alphabet_available.indexOf(alphabetList.get(i));
        if (available_position != -1){ // if characters is available in list
            drawable = TextDrawable.builder().buildRound(alphabetList.get(i), Color.BLUE);
        } else {
            drawable = TextDrawable.builder().buildRound(alphabetList.get(i), Color.GRAY);
            myViewHolder.itemView.setClickable(false);
        }
        myViewHolder.alphabet_avatar.setImageDrawable(drawable);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when user choose alphabet character, just go back MainActivity  and scroll  to position of first character
                iOnAlphabetClickListenner.onAlphabetClickListenner(alphabetList.get(i), available_position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alphabetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView alphabet_avatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            alphabet_avatar = (ImageView)itemView.findViewById(R.id.alphabet_avatar);
        }
    }
}
