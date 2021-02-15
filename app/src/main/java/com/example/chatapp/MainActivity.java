package com.example.chatapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.acl.Group;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String activityName="Profile";
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference myErrorRef = database.getReference().child("errors").child(activityName);
    private ErrorClass errorClass = new ErrorClass();

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ListView lst_menu=findViewById(R.id.lst_menu);
            String[] menu=getResources().getStringArray(R.array.menu);

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
            lst_menu.setAdapter(arrayAdapter);

            lst_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position)
                    {
                        case 0: {
                            Intent intent = new Intent(MainActivity.this, Profile.class);
                            startActivity(intent);
                            break;
                        }

                        case 1:{
                            Intent intent=new Intent(MainActivity.this, Group.class);
                            intent.putExtra("groupNumber","1");
                            startActivity(intent);
                            break;
                        }

                        case 2:{
                            Intent intent=new Intent(MainActivity.this, Group.class);
                            intent.putExtra("groupNumber","2");
                            startActivity(intent);
                            break;
                        }

                        case 3:{
                            Intent intent=new Intent(MainActivity.this, Group.class);
                            intent.putExtra("groupNumber","3");
                            startActivity(intent);
                            break;
                        }

                        case 4:
                        {
                            Intent intent=new Intent(MainActivity.this,OneToOneChats.class);
                            startActivity(intent);
                            break;
                        }

                        case 5:{
                            logout();
                            break;
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            String functionName= Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i=0;
            for(StackTraceElement ste:e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }

            String lineError=e.getStackTrace()[i].getLineNumber()+ "";
            String msg=e.getMessage();
            errorClass.sendError(myErrorRef,lineError,msg,functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void logout(){
        try{
            mAuth.signOut();
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,new OnCompleteListener<Void>(){

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent=new Intent(MainActivity.this,GoogleSignInActivity.class);
                    startActivity(intent);
                }
            });
        }

        catch (Exception e)
        {
            String functionName= Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i=0;
            for(StackTraceElement ste:e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }

            String lineError=e.getStackTrace()[i].getLineNumber()+ "";
            String msg=e.getMessage();
            errorClass.sendError(myErrorRef,lineError,msg,functionName);
        }
    }
}
