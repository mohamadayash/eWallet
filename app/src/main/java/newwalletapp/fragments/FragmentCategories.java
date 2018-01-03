package newwalletapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import newwalletapp.adpter.CategoriesAdpter;
import newwalletapp.database.DeleteSQLiteData;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.AddCategory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategories extends android.support.v4.app.Fragment {


    FragmentManager fragmentManager;
    CategoriesAdpter categoriesAdpter;
    ListView listViewCategories;
    ReadSQLiteData readSQLiteData;
    public FragmentCategories() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_categories);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager=getFragmentManager();
        log("OnActivityCreated");
        readSQLiteData =new ReadSQLiteData(getActivity());
        log("readSQLiteData");
        listViewCategories= (ListView) getActivity().findViewById(R.id.listViewCategories);
        log("listViewCategories");
        categoriesAdpter=new CategoriesAdpter(getActivity(),readSQLiteData.getAllCategories());
        log("categoriesAdpter");
        TextView textViewBlackList= (TextView) getActivity().findViewById(R.id.textViewCategoriesDefaultListText);
        log("textViewBlackList");
        listViewCategories.setEmptyView(textViewBlackList);
        log("listViewCategories.setEmptyView");
        listViewCategories.setAdapter(categoriesAdpter);
        log("listViewCategories.setAdapte");
    }

    @Override
    public void onResume() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_currencies);
        Bundle bundle=getArguments();
        if(bundle!=null) {
        }
            super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings_child, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add) {
            //Toast.makeText(getActivity(),"Hii",Toast.LENGTH_SHORT).show();
            AddCategory addCategory= (AddCategory) getActivity();
            addCategory.beginAddCategoryTransaction();
/*
            FragmentAddCategory fragmentAddCategory=new FragmentAddCategory();
            fragmentManager.beginTransaction().replace(R.id.layout_settings_container,fragmentAddCategory).addToBackStack("addcategories").commit();
*/
        }
        return super.onOptionsItemSelected(item);
    }


    void log(String msg)
    {
        Log.d("FragmentCategories",msg);
    }

    public void deleteCategory(String id,String name) {
        DeleteSQLiteData deleteSQLiteData=new DeleteSQLiteData(getActivity());
        deleteSQLiteData.deleteCategory(id,name);
        categoriesAdpter=new CategoriesAdpter(getActivity(),readSQLiteData.getAllCategories());
        categoriesAdpter.notifyDataSetChanged();
        listViewCategories.setAdapter(categoriesAdpter);
    }
}
