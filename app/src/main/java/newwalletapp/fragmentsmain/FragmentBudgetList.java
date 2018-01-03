package newwalletapp.fragmentsmain;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.adpter.BudgetListAdpter;
import newwalletapp.interfaces.BudgetInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBudgetList extends android.support.v4.app.Fragment {


    ListView listViewBudgetList;
    ArrayList<HashMap<String,String>> arrayList;
    BudgetListAdpter budgetListAdpter;
    public FragmentBudgetList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budgetlist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewBudgetList= (ListView) getActivity().findViewById(R.id.listViewBudgetListFilterd);
        Bundle b=getArguments();
        if (b!=null)
        {
          arrayList= (ArrayList<HashMap<String, String>>) b.getSerializable("map");
        }
        budgetListAdpter=new BudgetListAdpter(getActivity(),arrayList);
        listViewBudgetList.setAdapter(budgetListAdpter);
        listViewBudgetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BudgetInterface budgetInterface = (BudgetInterface) getActivity();
                Bundle b=new Bundle();
                b.putSerializable("map",arrayList.get(position));

                budgetInterface.beginBudgetTransaction(b);

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuItem=menu.findItem(R.id.action_addnew_record);
        menuItem.setVisible(false);

//        MenuItem menuItemFilter=menu.findItem(R.id.action_filter);
//        menuItemFilter.setVisible(false);

        //inflater.inflate(R.menu.menu_budget, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add) {
            BudgetInterface budgetInterface = (BudgetInterface) getActivity();
            budgetInterface.beginBudgetTransaction(null);
        }
        return super.onOptionsItemSelected(item);
    }
}
