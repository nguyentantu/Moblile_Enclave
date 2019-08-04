package com.example.navigation.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigation.DetailProjectActivity;
import com.example.navigation.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import model.Project;

public class InprogressFragment extends Fragment {

    String nameProject, techonology, category, status;
    int earning, id, total;
    RequestQueue requestQueue;
    //ArrayList<Project> movies;
    Project movie;
    AlertDialog mProgress;

    private static final String TAG = InprogressFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<Project> itemsList;
    StoreAdapter mAdapter;

    public InprogressFragment() {
        // Required empty public constructor
    }

    public static InprogressFragment newInstance(String param1, String param2) {
        InprogressFragment fragment = new InprogressFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inprogress, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        itemsList = new ArrayList<>();
        mAdapter = new StoreAdapter(getContext(),itemsList);
        mProgress = new SpotsDialog(getActivity(), R.style.Custom);
        mProgress.show();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        requestQueue = Volley.newRequestQueue(getContext());

        parseJSON();

        return view;
    }

    private void parseJSON() {
        String url = "http://si-enclave.herokuapp.com/api/v1/projects/groupBy?limit=100&offset=0&status=inProgress";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject p = (JSONObject) array.get(i);
                        id = p.getInt("id");
                        earning = p.getInt("earning");
                        nameProject = p.getString("name");
                        techonology = p.getString("technology");
                        status = p.getString("status");
                        JSONObject jsonObject = p.getJSONObject("category");
                        category = jsonObject.getString("name");
                        movie = new Project(nameProject, techonology, category, status, earning, id);
                        itemsList.add(movie);
                    }
                    mAdapter = new StoreAdapter(getContext(),itemsList);
                    recyclerView.setAdapter(mAdapter);
                    mProgress.dismiss();
                } catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter = new StoreAdapter(getContext(),itemsList);
            recyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                java.net.URL url = new URL("http://si-enclave.herokuapp.com/api/v1/projects/groupBy?limit=100&offset=0&status=inProgress");// link API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject jsonArray = new JSONObject(builder.toString());
                total = jsonArray.getInt("total");
                JSONArray array = jsonArray.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject p = (JSONObject) array.get(i);
                    id = p.getInt("id");
                    earning = p.getInt("earning");
                    nameProject = p.getString("name");
                    techonology = p.getString("technology");
                    status = p.getString("status");
                    JSONObject jsonObject = p.getJSONObject("category");
                    category = jsonObject.getString("name");
                    movie = new Project(nameProject, techonology, category, status, earning, id);
                    itemsList.add(movie);
                }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView adapter class to render items
     * This class can go into another separate class, but for simplicity
     */
    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
        private Context context;
        private List<Project> movieList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameProject, earning, category, status, technology;
            public CircleImageView imgLanguage;

            public MyViewHolder(View view) {
                super(view);
                nameProject = view.findViewById(R.id.txt_nameProject);
                earning = view.findViewById(R.id.txt_earning);
                category = view.findViewById(R.id.txt_category);
                imgLanguage = view.findViewById(R.id.img_language);
                technology = view.findViewById(R.id.txt_technology);
            }
        }

        public StoreAdapter(Context context, List<Project> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Project movie = movieList.get(position);
            NumberFormat currentLocale = NumberFormat.getInstance();
            String earn = currentLocale.format(movie.getEarning());

            holder.nameProject.setText(movie.getNameProject());
            holder.earning.setText(earn+" VNĐ");
            holder.category.setText(movie.getCategory());
            //holder.status.setText(movie.getStatus());
            holder.technology.setText(movie.getTechnology());

            final int id = movie.getId();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DetailProjectActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });
            String language = movie.getTechnology();
            switch (language){
                case "Swift":
                    Picasso.with(context).load(R.drawable.swift).into(holder.imgLanguage);
                    break;
                case "TypeScript":
                    Picasso.with(context).load(R.drawable.typescript).into(holder.imgLanguage);
                    break;
                case "React Native":
                    Picasso.with(context).load(R.drawable.react_native).into(holder.imgLanguage);
                    break;
                case "Angular JS":
                    Picasso.with(context).load(R.drawable.angular).into(holder.imgLanguage);
                    break;
                case "VueJS":
                    Picasso.with(context).load(R.drawable.vuejs).into(holder.imgLanguage);
                    break;
                case "Go lang":
                    Picasso.with(context).load(R.drawable.go_lang).into(holder.imgLanguage);
                    break;
                case "Objective-C":
                    Picasso.with(context).load(R.drawable.object_c).into(holder.imgLanguage);
                    break;
                case "R":
                    Picasso.with(context).load(R.drawable.r).into(holder.imgLanguage);
                    break;
                case "Nodejs":
                    holder.technology.setVisibility(View.GONE);
                    Picasso.with(context).load(R.drawable.nodejs).into(holder.imgLanguage);
                    break;
                case "PHP":
                    holder.technology.setVisibility(View.GONE);
                    Picasso.with(context).load(R.drawable.php).into(holder.imgLanguage);
                    break;
                case "JavaScript":
                    Picasso.with(context).load(R.drawable.javascript).into(holder.imgLanguage);
                    break;
                case "Java":
                    holder.technology.setVisibility(View.GONE);
                    Picasso.with(context).load(R.drawable.java).into(holder.imgLanguage);
                    break;
                case "C/C++":
                    holder.technology.setVisibility(View.GONE);
                    Picasso.with(context).load(R.drawable.cccc).into(holder.imgLanguage);
                    break;
                case "Python":
                    Picasso.with(context).load(R.drawable.python).into(holder.imgLanguage);
                    break;
                case "Ruby":
                    Picasso.with(context).load(R.drawable.ruby).into(holder.imgLanguage);
                    break;
                case "Visual Basic":
                    Picasso.with(context).load(R.drawable.visual_basic).into(holder.imgLanguage);
                    break;
                case "Kotlin":
                    Picasso.with(context).load(R.drawable.kotlin).into(holder.imgLanguage);
                    break;
                case "Perl":
                    Picasso.with(context).load(R.drawable.perl).into(holder.imgLanguage);
                    break;
                case "ReactJS":
                    Picasso.with(context).load(R.drawable.react_native).into(holder.imgLanguage);
                    break;
                case "C#":
                    holder.technology.setVisibility(View.GONE);
                    Picasso.with(context).load(R.drawable.c_sharp).into(holder.imgLanguage);
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }
}
