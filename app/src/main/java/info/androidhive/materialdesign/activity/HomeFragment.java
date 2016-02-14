package info.androidhive.materialdesign.activity;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Handler;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.ListViewWallAdapter;
import info.androidhive.materialdesign.model.ListViewWall;


public class HomeFragment extends ListFragment {

    private ImageButton imageButton;
    private View rootView;
    private ArrayList<ListViewWall> listWall;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeFragment context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpGetWall httpGetWall = new HttpGetWall(this);
        httpGetWall.execute();
        context=this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                HttpGetWall httpGetWall = new HttpGetWall(context);
                httpGetWall.execute();
                mSwipeRefreshLayout.setRefreshing(false);


            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setListWall(ArrayList<ListViewWall> list) {

         listWall = list;
        System.out.println("List wall size :" + listWall.size());
        setListAdapter(new ListViewWallAdapter(rootView.getContext(), listWall));
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        ListViewWall item = listWall.get(position);
        Intent i = new Intent(rootView.getContext(),SendNotesActivity.class);
        i.putExtra("idWall", item.id);
        startActivity(i);
        // do something
        Toast.makeText(getActivity(), "Wall selectionné : " + item.name, Toast.LENGTH_SHORT).show();

    }


}
