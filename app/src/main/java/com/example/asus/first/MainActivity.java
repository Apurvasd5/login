package com.example.asus.first;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class MainActivity extends AppCompatActivity {


    // Declaring layout button, edit texts
    Button login;
    EditText username,password;
    ProgressBar progressBar;
    // End Declaring layout button, edit texts

    // Declaring connection variables
    Connection con;
    String un,pass,db,ip;
    private int contentView;
    //End Declaring connection variables


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting values from button, texts and progress bar

        login = findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // End Getting values from button, texts and progress bar

// Declaring Server ip, username, database name and password
        ip = "192:168:43:2";
        db = "android";
        un = "root";
        pass = "";
        // Declaring Server ip, username, database name and password

        // Setting up the function when button login is clicked
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
        //End Setting up the function when button login is clicked
    }

    //private void findViewById(int button) {
    //}

    public void setContentView(int contentView) {
        this.contentView = contentView;
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(MainActivity.this , "Login Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            String usernam = username.getText().toString();
            String passwordd = password.getText().toString();
            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else
            {
                try
                {
                    con = connectionclass(un, pass, db, ip);        // Connect to database
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        // Change below query according to your own database.
                        String query = "select * from login where user_name= '" + usernam.toString() + "' and pass_word = '"+ passwordd.toString() +"'  ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            z = "Login successful";
                            isSuccess=true;
                            con.close();
                        }
                        else
                        {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }


    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server+ ";databasename="+ database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}





/*public class MainActivity extends AppCompatActivity  {

    static public String user_id;
    static public String user_name;
    EditText pass;
    Button login;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        pass= (EditText) findViewById(R.id.pass);
        
        login= (Button) findViewById(R.id.login);

        connectionClass = new ConnectionClass();

        progressDialog=new ProgressDialog(this);

       register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main4Activity.class));
          }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dologin dologin=new Dologin();
                dologin.execute();
            }
        });

    }*/

  /*  private class Dologin extends AsyncTask<String,String,String>{

        private  password;
        String passstr=password.getText().toString();
        String z="";
        boolean isSuccess=false;
        String em,password;
        private Object connectionClass;
        private ActionBar progressDialog;

        @Override
        protected void onPreExecute() {
            AlertDialog progressDialog;
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            z = "default message";
            String name = "";
            if(passstr.trim().equals(""))
                z = "Please enter all fields....";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {
                        String query=" select * from user where  PASSWORD = '"+passstr+"'";
                        Statement stmt = con.createStatement();
                        // stmt.executeUpdate(query);
                        ResultSet rs=stmt.executeQuery(query);

                        while (rs.next())
                        {
                            name = rs.getString(1);
                            em=rs.getString(2);
                            password=rs.getString(3);
                            if(password.equals(passstr))
                            {
                                isSuccess=true;

                                z = "Welcome "+name;
                                String user_name = name;
                            }
                            else
                            {
                                isSuccess=false;
                                z = "Login unsuccessfull";
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }
            return z;
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();
            if(isSuccess) {

                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
            progressDialog.hide();
        }
    }

}*/

