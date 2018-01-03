package newwalletapp.fragments;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.adpter.CategoriesSpinnerAdpter;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.StoreSQLiteData;
import newwalletapp.database.UpdateSQLiteData;
import newwalletapp.interfaces.BeginPopStack;
import newwalletapp.views.CustomIconDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddCategory extends android.support.v4.app.Fragment {


    LinearLayout linearLayoutIcon;
    ImageView imageViewIcon;
    EditText editTextCategoryName, editTextDescription;
    RadioGroup radioGroupCategoryType;
    Spinner spinnerCategoryParentType;
    String catType = "expense", parentCat = "";
    CategoriesSpinnerAdpter categoriesSpinnerAdpter;
    ReadSQLiteData readSQLiteData;
    int selectedIcon = 26;
    ArrayList<HashMap<String, String>> arrayCategories;
    int backColor;
    int FlagForEdit = 0;
    String recId,catNameForUpdate;


    public FragmentAddCategory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }


    @Override
    public void onResume() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            FlagForEdit = 1;
            HashMap<String, String> map = (HashMap<String, String>) bundle.getSerializable("map");
            recId = map.get("id");
            selectedIcon = Integer.parseInt(map.get("icon"));
            Drawable drawable = getActivity().getResources().getDrawable(getActivity().getResources().getIdentifier("categoury_w_" + (selectedIcon), "drawable", getActivity().getApplicationContext().getPackageName()));
            imageViewIcon.setImageDrawable(drawable);
            backColor = Color.parseColor(map.get("color"));
            linearLayoutIcon.setBackgroundColor(backColor);
            catNameForUpdate=map.get("name");
            editTextCategoryName.setText(map.get("name"));
            catType = map.get("type");
            if (catType.equals("expense")) {

                RadioButton radioButton = (RadioButton) getActivity().findViewById(R.id.radio_expence);
                radioButton.setChecked(true);
            } else {
                RadioButton radioButton = (RadioButton) getActivity().findViewById(R.id.radio_income);
                radioButton.setChecked(true);
            }

            String parent = map.get("parent");
            if (parent.equals("")) {
                spinnerCategoryParentType.setSelection(0);
            } else {
                for (int i = 1; i < arrayCategories.size(); i++) {
                    if (parent.equals(arrayCategories.get(i).get("name"))) {
                        spinnerCategoryParentType.setSelection(i);
                        break;
                    }
                }
            }
            editTextDescription.setText(map.get("des"));

        }

        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readSQLiteData = new ReadSQLiteData(getActivity());
        linearLayoutIcon = (LinearLayout) getActivity().findViewById(R.id.linearLayoutAddCategoriesIconBackground);
        imageViewIcon = (ImageView) getActivity().findViewById(R.id.imageViewAddCategoriesIcon);
        editTextCategoryName = (EditText) getActivity().findViewById(R.id.editTextAddCategoryName);
        radioGroupCategoryType = (RadioGroup) getActivity().findViewById(R.id.radioGroupAddCategoryType);
        spinnerCategoryParentType = (Spinner) getActivity().findViewById(R.id.spinnerAddCategoryParent);
        editTextDescription = (EditText) getActivity().findViewById(R.id.editTextAddCategoryDescription);
        arrayCategories = new ArrayList<HashMap<String, String>>(readSQLiteData.getAllCategories());
        arrayCategories.add(0, null);
        categoriesSpinnerAdpter = new CategoriesSpinnerAdpter(getActivity(), arrayCategories);
        spinnerCategoryParentType.setAdapter(categoriesSpinnerAdpter);

        radioGroupCategoryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) getActivity().findViewById(checkedId);
                if (radioButton.getText().equals(getActivity().getResources().getString(R.string.expense))) {
                    catType = "expense";
                } else {
                    catType = "income";
                }
            }
        });


        spinnerCategoryParentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    Log.d("FragmentAddCategory", "Selected CategoryName" + arrayCategories.get(position).get("name"));
                    parentCat = arrayCategories.get(position).get("name");
                } else {
                    parentCat = "";
                    Log.d("FragmentAddCategory", "Selected CategoryName");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linearLayoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backColor = Color.TRANSPARENT;
                Drawable background = linearLayoutIcon.getBackground();
                if (background instanceof ColorDrawable)
                    backColor = ((ColorDrawable) background).getColor();
                CustomIconDialog customIconDialog = new CustomIconDialog(getActivity(), backColor, selectedIcon, true, new CustomIconDialog.OnColorPickerListener() {
                    @Override
                    public void onCancel(CustomIconDialog dialog) {

                    }

                    @Override
                    public void onOk(CustomIconDialog dialog, int color, int icon) {

                        linearLayoutIcon.setBackgroundColor(color);

                        Drawable drawable = getActivity().getResources().getDrawable(getActivity().getResources().getIdentifier("categoury_w_" + (icon + 1), "drawable", getActivity().getApplicationContext().getPackageName()));
                        imageViewIcon.setImageDrawable(drawable);
                        selectedIcon = icon + 1;
                        backColor = color;
                        Log.d("FragmentCategory", "Color =" + color + " and icon =" + icon);

                    }
                });
                customIconDialog.show();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {

            if (!editTextCategoryName.getText().toString().equals("")) {
                if (FlagForEdit == 0) {
                    if (readSQLiteData.getDublicateCategory(editTextCategoryName.getText().toString()) == 0) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", editTextCategoryName.getText().toString());
                        map.put("des", editTextDescription.getText().toString());
                        map.put("icon", "" + selectedIcon);
                        backColor = Color.TRANSPARENT;
                        Drawable background = linearLayoutIcon.getBackground();
                        if (background instanceof ColorDrawable)
                            backColor = ((ColorDrawable) background).getColor();
                        //Integer.toHexString(color).toUpperCase()
                        Log.d("FragmentAddCategory","Color Check 1 ="+"#"+Integer.toHexString(backColor));


                        map.put("color", ("#"+Integer.toHexString(backColor)).trim());
                        //map.put("color", ("#" + Integer.toHexString(backColor & 0x00FFFFFF)).trim());
                        map.put("type", catType);
                        map.put("parent", "" + parentCat);

                        StoreSQLiteData storeSQLiteData = new StoreSQLiteData(getActivity());
                        storeSQLiteData.AddNewCategoury(map);
                        Toast.makeText(getActivity(), getActivity().getString(R.string.category_added_toast), Toast.LENGTH_LONG).show();
                        BeginPopStack beginPopStack = (BeginPopStack) getActivity();
                        beginPopStack.beginPopStackTransaction();

                    } else {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.category_duplicate_toast), Toast.LENGTH_LONG).show();
                    }

                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", editTextCategoryName.getText().toString());
                    map.put("des", editTextDescription.getText().toString());
                    map.put("icon", "" + selectedIcon);
                    backColor = Color.TRANSPARENT;
                    Drawable background = linearLayoutIcon.getBackground();
                    if (background instanceof ColorDrawable)
                        backColor = ((ColorDrawable) background).getColor();

                    map.put("color", ("#"+Integer.toHexString(backColor)).trim());
                    //map.put("color", ("#" + Integer.toHexString(backColor & 0x00FFFFFF)).trim());
                    map.put("type", catType);
                    map.put("parent", "" + parentCat);

                    UpdateSQLiteData updateSQLiteData = new UpdateSQLiteData(getActivity());
                    updateSQLiteData.updateCategory(recId,catNameForUpdate, map);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.category_update_toast), Toast.LENGTH_LONG).show();
                    BeginPopStack beginPopStack = (BeginPopStack) getActivity();
                    beginPopStack.beginPopStackTransaction();

                }
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.category_enter_name_toast), Toast.LENGTH_LONG).show();
            }


        }
        return super.onOptionsItemSelected(item);
    }
}
