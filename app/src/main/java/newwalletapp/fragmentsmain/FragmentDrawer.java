package newwalletapp.fragmentsmain;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;
import com.example.accounts.newwalletapp.SettingsActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.adpter.DrawerListAdpter;
import newwalletapp.database.DataPrefrences;
import newwalletapp.extras.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrawer extends android.support.v4.app.Fragment {


    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    public DrawerListAdpter drawerListAdpter;
    public ListView listViewDrawer;
    int flagForHome=0;
    View containerView;
    String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    boolean mUserLearnedDrawer;
    boolean mFromSavedInstanceState;
    DataPrefrences dataPrefrences;
    int selectedListItem=0;
    android.support.v4.app.Fragment fragmentDeshboard = new FragmentDashBoard();
    android.support.v4.app.Fragment fragmentRecords = new FragmentRecords();
    android.support.v4.app.Fragment fragmentChart = new FragmentChart();
    android.support.v4.app.Fragment fragmentReport = new FragmentReport();
    android.support.v4.app.Fragment fragmentBudget = new FragmentBudget();


    ArrayList<HashMap<String, String>> listItemMap;
    FragmentManager fragmentManager;

    public FragmentDrawer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dataPrefrences = new DataPrefrences(getActivity());
        if (!dataPrefrences.GetStoredPrefrence(KEY_USER_LEARNED_DRAWER).equals("true")) {
            mUserLearnedDrawer = true;
        } else {
            mUserLearnedDrawer = false;
        }
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Fragment", "On Create View Check Error");
        return inflater.inflate(R.layout.fragment_drawer_layout, container, false);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedfragment",selectedListItem);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("FragmentDrawer","OnPause()");
       // dataPrefrences.StorePrefrence("selectedfragment",""+selectedListItem);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment", "onActivityCreated");
        if(savedInstanceState!=null)
        {
            selectedListItem=savedInstanceState.getInt("selectedfragment");
            Log.d("FragmentDrawer","savedInstanceState!=null  value="+savedInstanceState.getInt("selectedfragment"));

        }

        listViewDrawer = (ListView) getActivity().findViewById(R.id.listView_drawer);
        listViewDrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listItemMap = new ArrayList<HashMap<String, String>>();
        fragmentManager = getFragmentManager();

        HashMap<String, String> map;

        map = new HashMap<String, String>();
        map.put("icon", "" + R.drawable.ic_dashboard);
        map.put("text", getActivity().getResources().getString(R.string.deshboard));
        listItemMap.add(map);

        map = new HashMap<String, String>();
        map.put("icon", "" + R.drawable.ic_records);
        map.put("text", getActivity().getResources().getString(R.string.records));
        listItemMap.add(map);

        map = new HashMap<String, String>();
        map.put("icon", "" + R.drawable.ic_chart);
        map.put("text", getActivity().getResources().getString(R.string.chart));
        listItemMap.add(map);

        map = new HashMap<String, String>();
        map.put("icon", "" + R.drawable.ic_report);
        map.put("text", getActivity().getResources().getString(R.string.reports));
        listItemMap.add(map);

        map = new HashMap<String, String>();
        map.put("icon", "" + R.drawable.ic_budget);
        map.put("text", getActivity().getResources().getString(R.string.budgets));
        listItemMap.add(map);


        map = new HashMap<String, String>();
        map.put("icon", "" + R.drawable.ic_settings);
        map.put("text",getActivity().getResources().getString(R.string.settings));
        listItemMap.add(map);

        drawerListAdpter = new DrawerListAdpter(getActivity(), listItemMap);
        listViewDrawer.setAdapter(drawerListAdpter);
        if (!dataPrefrences.GetStoredPrefrence("selectedfragment").equals("N/A"))
        {
            selectedListItem = Integer.parseInt(dataPrefrences.GetStoredPrefrence("selectedfragment"));
            Log.d("FragmentDrawer","selectedListItem Value ="+dataPrefrences.GetStoredPrefrence("selectedfragment"));
            dataPrefrences.StorePrefrence("selectedfragment","N/A");
            //selectListFragment(selectedListItem);
            //selectListFragment(Integer.parseInt(selectedListItem));
        }

        selectListFragment(selectedListItem);
        listViewDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),""+position,Toast.LENGTH_LONG).show();
                view.setSelected(true);
                listViewDrawer.setSelection(position);
                drawerListAdpter.setSelectedPosition(position);
                listViewDrawer.setItemChecked(position,true);
                //drawerListAdpter.setSelectedPosition(position);
                selectListFragment(position);
                if (position == 0) {

                    //listViewDrawer.setSelection(0);
/*
                    listViewDrawer.setItemChecked(0, true);
                    selectedListItem=0;
                    setTitle(getActivity().getResources().getString(R.string.deshboard));
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentDeshboard).commit();
                    mDrawerLayout.closeDrawer(containerView);
*/

                } else if (position == 1) {
/*
                    listViewDrawer.setItemChecked(1, true);
                    setTitle(getActivity().getResources().getString(R.string.records));
                    mDrawerLayout.closeDrawer(containerView);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentRecords).commit();
                    selectedListItem=1;
*/
                } else if (position == 2) {
/*
                    listViewDrawer.setItemChecked(2, true);
                    setTitle(getActivity().getResources().getString(R.string.chart));
                    mDrawerLayout.closeDrawer(containerView);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentChart).commit();
                    selectedListItem=2;
*/
                } else if (position == 3) {
/*
                    listViewDrawer.setItemChecked(3, true);
                    setTitle(getActivity().getResources().getString(R.string.reports));
                    mDrawerLayout.closeDrawer(containerView);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentReport).commit();
                    selectedListItem=3;
*/
                } else if (position == 4) {
/*
                    listViewDrawer.setItemChecked(4, true);
                    setTitle(getActivity().getResources().getString(R.string.budgets));
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentBudget).commit();
                    mDrawerLayout.closeDrawer(containerView);
                    selectedListItem=5;
*/
                } else if (position == 5) {
/*
                    listViewDrawer.setItemChecked(5, true);
                    setTitle(getActivity().getResources().getString(R.string.settings));
                    mDrawerLayout.closeDrawer(containerView);
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    selectedListItem=6;
*/
                }


            }
        });


    }

    void selectListFragment(int flag)
    {
        if (flag == 0) {
            //listViewDrawer.setSelection(0);

            selectedListItem=0;
            setTitle(getActivity().getResources().getString(R.string.deshboard));

            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentDeshboard).commit();
            mDrawerLayout.closeDrawer(containerView);

        } else if (flag == 1) {

            setTitle(getActivity().getResources().getString(R.string.records));
            mDrawerLayout.closeDrawer(containerView);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentRecords).commit();
            selectedListItem=1;
        } else if (flag == 2) {

            setTitle(getActivity().getResources().getString(R.string.chart));
            mDrawerLayout.closeDrawer(containerView);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentChart).commit();
            selectedListItem=2;
        } else if (flag == 3) {

            setTitle(getActivity().getResources().getString(R.string.reports));
            mDrawerLayout.closeDrawer(containerView);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentReport).commit();
            selectedListItem=3;
        } else if (flag == 4) {

            setTitle(getActivity().getResources().getString(R.string.budgets));
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentBudget).commit();
            mDrawerLayout.closeDrawer(containerView);
            selectedListItem=4;
        } else if (flag == 5) {

            setTitle(getActivity().getResources().getString(R.string.settings));
            mDrawerLayout.closeDrawer(containerView);
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            selectedListItem=5;
        }

    }

    public void setTitle(String title) {
        getActivity().setTitle(title);
    }


    public void setUp(int fragmentDrawerId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        containerView = getActivity().findViewById(fragmentDrawerId);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    //saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
}

