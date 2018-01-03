package newwalletapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.security.NoSuchAlgorithmException;

import newwalletapp.database.DataPrefrences;
import newwalletapp.extras.Constants;
import newwalletapp.interfaces.BeginPopStack;
import newwalletapp.interfaces.PasswordInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPassword extends android.support.v4.app.Fragment implements View.OnClickListener{

    DataPrefrences dataPrefrences;
    EditText editTextPassword;
    ImageView imageViewErase;
    int password_count = 0,flag = 0;
    String tempPassword;
    TextView textViewTitle,textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,textView0,textViewOk;
    public FragmentPassword() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password, container, false);
        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Password");
        Bundle b = getArguments();
        if(b!=null)
        {
            flag = b.getInt("flag");
        }
        dataPrefrences = new DataPrefrences(getActivity());
        textViewTitle= (TextView) getActivity().findViewById(R.id.textViewNumberPasswordLable);
        textViewTitle.setText(getActivity().getResources().getString(R.string.enterpassword_title));
        editTextPassword= (EditText) getActivity().findViewById(R.id.editTextPassword);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
/*
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
*/
/*
        InputMethodManager im = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(editTextPassword.getWindowToken(),0);
*/
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = editTextPassword.getInputType(); // backup the input type
                editTextPassword.setInputType(InputType.TYPE_NULL); // disable soft input
                editTextPassword.onTouchEvent(event); // call native handler
                editTextPassword.setInputType(inType); // restore input type
                editTextPassword.setSelection(editTextPassword.getText().length());
                return true; // consume touch even
            }
        });
        imageViewErase = (ImageView) getActivity().findViewById(R.id.imageViewBackspace);
        imageViewErase.setOnClickListener(this);

        textView0= (TextView) getView().findViewById(R.id.textViewNumber0);
        textView0.setOnClickListener(this);

        textView1= (TextView) getView().findViewById(R.id.textViewNumber1);
        textView1.setOnClickListener(this);

        textView2= (TextView) getView().findViewById(R.id.textViewNumber2);
        textView2.setOnClickListener(this);

        textView3= (TextView) getView().findViewById(R.id.textViewNumber3);
        textView3.setOnClickListener(this);

        textView4= (TextView) getView().findViewById(R.id.textViewNumber4);
        textView4.setOnClickListener(this);

        textView5= (TextView) getView().findViewById(R.id.textViewNumber5);
        textView5.setOnClickListener(this);

        textView6= (TextView) getView().findViewById(R.id.textViewNumber6);
        textView6.setOnClickListener(this);

        textView7= (TextView) getView().findViewById(R.id.textViewNumber7);
        textView7.setOnClickListener(this);

        textView8= (TextView) getView().findViewById(R.id.textViewNumber8);
        textView8.setOnClickListener(this);

        textView9= (TextView) getView().findViewById(R.id.textViewNumber9);
        textView9.setOnClickListener(this);

        textViewOk= (TextView) getView().findViewById(R.id.textViewNumberok);
        textViewOk.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == textView0)
        {
          if (!editTextPassword.getText().toString().equals(""))
          {
              editTextPassword.setText(editTextPassword.getText().toString()+"0");
          }
          else
          {
              editTextPassword.setText("0");
          }
            editTextPassword.setSelection(editTextPassword.getText().length());
        }
        else if(v == textView1)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"1");
            }
            else
            {
                editTextPassword.setText("1");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());

        }
        else if(v == textView2)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"2");
            }
            else
            {
                editTextPassword.setText("2");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());

        }
        else if(v == textView3)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"3");
            }
            else
            {
                editTextPassword.setText("3");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());

        }
        else if(v == textView4)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"4");
            }
            else
            {
                editTextPassword.setText("4");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());

        }
        else if(v == textView5)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"5");
            }
            else
            {
                editTextPassword.setText("5");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());

        }
        else if(v == textView6)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"6");
            }
            else
            {
                editTextPassword.setText("6");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        }
        else if(v == textView7)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"7");
            }
            else
            {
                editTextPassword.setText("7");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        }
        else if(v == textView8)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"8");
            }
            else
            {
                editTextPassword.setText("8");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        }
        else if(v == textView9)
        {
            if (!editTextPassword.getText().toString().equals(""))
            {
                editTextPassword.setText(editTextPassword.getText().toString()+"9");
            }
            else
            {
                editTextPassword.setText("9");
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        }
        else if(v == textViewOk)
        {
            if(dataPrefrences.GetStoredPrefrence("password").equals("N/A"))
            {
              if(password_count == 0)
              {
                  if(editTextPassword.getText().toString().length()>=5)
                  {
                      tempPassword = editTextPassword.getText().toString();
                      editTextPassword.setText("");
                      textViewTitle.setText(getActivity().getResources().getString(R.string.conformpassword_title));
                      password_count=1;
                  }
                  else
                  {
                      Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.password_length_toast),Toast.LENGTH_LONG).show();
                  }

              }
              else
              {
                  if(editTextPassword.getText().toString().length()>=5)
                  {

                      if(tempPassword.equals(editTextPassword.getText().toString()))
                      {
                          try {
                              dataPrefrences.StorePrefrence("password", Constants.computeHash(editTextPassword.getText().toString()));
                          } catch (NoSuchAlgorithmException e) {
                              e.printStackTrace();
                          }
                          BeginPopStack beginPopStack = (BeginPopStack) getActivity();
                          beginPopStack.beginPopStackTransaction();
                      }
                      else
                      {
                          Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.password_missmatch_toast),Toast.LENGTH_LONG).show();
                      }
                  }
                  else
                  {
                      Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.password_length_toast),Toast.LENGTH_LONG).show();
                  }

              }
            }
            else
            {
               String pass = dataPrefrences.GetStoredPrefrence("password");
                try {
                    if (Constants.computeHash(editTextPassword.getText().toString()).equals(pass))
                    {
                        if (flag==0)
                        {
                            dataPrefrences.StorePrefrence("password","N/A");
                            BeginPopStack beginPopStack = (BeginPopStack) getActivity();
                            beginPopStack.beginPopStackTransaction();

                        }
                        else
                        {

                            PasswordInterface passwordInterface= (PasswordInterface) getActivity();
                            passwordInterface.beginTransactionFromPassword();

                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.password_invalid_toast),Toast.LENGTH_LONG).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(v==imageViewErase)
        {
            String textInBox = editTextPassword.getText().toString();
            if(textInBox.length()>0)
            {
                String newText = textInBox.substring(0, textInBox.length()-1);
                editTextPassword.setText(newText);
            }

            editTextPassword.setSelection(editTextPassword.getText().length());


        }

    }
}
