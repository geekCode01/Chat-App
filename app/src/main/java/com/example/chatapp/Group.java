package com.example.chatapp;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
public class Group extends AppCompatActivity {
    private static final String activityName = "Group";
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference myRef = database.getReference();
    private static final DatabaseReference myErrorRef = database.getReference().child("errors").child(activityName);
    private ErrorClass error_class = new ErrorClass();
    private mydatabase db = new mydatabase(this);
    private ListView Lst_Chat;
    private EditText txt_msg;
    private ArrayList<msg_class> onlineMessages = new ArrayList<>();
    private ArrayList<msg_class> offlineMessages = new ArrayList<>();
    private ArrayList<String> profile = new ArrayList<>();
    private String groupNumber, userId, lastmsg;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        try {
            Lst_Chat = findViewById(R.id.lst_Chat);
            txt_msg = findViewById(R.id.txt_msg);
            groupNumber = Objects.requireNonNull(getIntent().getExtras()).getString("groupNumber");
            userId = db.getString("SELECT user_id FROM user ORDER BY id DESC LIMIT 1");
            String qry = "SELECT userName, userGender FROM profile ORDER BY id DESC LIMIT 1";
            profile = db.getProfileChat(qry);
            lastmsg = db.getString("SELECT msgId FROM chat WHERE groupNumber ='" + groupNumber + "' ORDER BY id DESC LIMIT 1");
            getData();
            getOnlineMsg();
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getOnlineMsg() {
        try {
            myRef.child("groups").child(groupNumber).orderByKey().startAt(lastmsg).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                        onlineMessages.add(singleSnapshot.getValue(msg_class.class));
                    }
                    if (onlineMessages.size() > 0)
                        saveLocalDB();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveLocalDB() {
        try {
            String userName, userGender, userId, msgId, msg;
            boolean result;
            for (int i = 0; i < onlineMessages.size(); i++) {
                msgId = onlineMessages.get(i).msgId;
                int id = db.getInteger("SELECT id FROM chat WHERE msgId = '" + msgId + "'");
                if (id == -1) {
                    userId = onlineMessages.get(i).userId;
                    userName = onlineMessages.get(i).userName;
                    userGender = onlineMessages.get(i).userGender;
                    msg = onlineMessages.get(i).msg;
                    result = db.insertMsg(msgId, userName, userId, msg, userGender, groupNumber);
                    if (!result)
                        Toast.makeText(this, getResources().getString(R.string.ErrorSave), Toast.LENGTH_LONG).show();
                }
            }
            onlineMessages.clear();
            getData();
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getData() {
        try {
            String qry = "SELECT * FROM chat WHERE groupNumber = '" + groupNumber + "'";
            offlineMessages = db.getMsg(qry);
            if (!offlineMessages.get(0).empty.matches("1"))
                viewData();
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void viewData() {
        try {
            Listadapter listadapter = new Listadapter();
            Lst_Chat.setAdapter(listadapter);
            Lst_Chat.setSelection(listadapter.getCount() - 1);
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void msgSend(View view) {
        try {
            String msg = txt_msg.getText().toString().trim();
            String userName = profile.get(0);
            String userGender = profile.get(1);
            String key = myRef.child("groups").child(groupNumber).push().getKey();
            msg_class msgClass = new msg_class(key, userName, userId, msg, userGender);
            Map<String, Object> map = msgClass.toMap();
            assert key != null;
            myRef.child("groups").child(groupNumber).child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    txt_msg.setText("");
                }
            });
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void oneToOne(int position) {
        try {
            String id = offlineMessages.get(position).userId;
            if (!userId.matches(id))
                goToOneToOne(position);
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void goToOneToOne(int position) {
        try {
            String id = offlineMessages.get(position).userId;
            String name = offlineMessages.get(position).userName;
            Intent intent = new Intent(this, OneToOne.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            startActivity(intent);
        } catch (Exception e) {
            String functionName = Objects.requireNonNull(new Object() {
            }.getClass().getEnclosingMethod()).getName();
            int i = 0;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().contains(activityName))
                    break;
                i++;
            }
            String lineError = e.getStackTrace()[i].getLineNumber() + "";
            String msg = e.getMessage();
            error_class.sendError(myErrorRef, lineError, msg, functionName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    class Listadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return offlineMessages.size();
        }
        @Override
        public Object getItem(int position) {
            return offlineMessages.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.chat_adapter, parent, false);
            TextView partner_msg = convertView.findViewById(R.id.partner_msg);
            TextView txt_partnerName = convertView.findViewById(R.id.txt_partnerName);
            TextView my_msg = convertView.findViewById(R.id.my_msg);
            TextView txt_myName = convertView.findViewById(R.id.txt_myName);
            LinearLayout partner_layout = convertView.findViewById(R.id.partner_layout);
            LinearLayout my_layout = convertView.findViewById(R.id.my_layout);
            String g;
            if (offlineMessages.get(position).userGender.matches("Male"))
                g = " ,M";
            else
                g = " ,F";
            if (offlineMessages.get(position).userId.matches(userId)) {
                my_msg.setText(offlineMessages.get(position).msg);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) partner_layout.getLayoutParams();
                layoutParams.height = 0;
                partner_layout.setLayoutParams(layoutParams);
                txt_myName.setText(offlineMessages.get(position).userName + g);
            } else {
                partner_msg.setText(offlineMessages.get(position).msg);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) my_layout.getLayoutParams();
                layoutParams.height = 0;
                my_layout.setLayoutParams(layoutParams);
                txt_partnerName.setText(offlineMessages.get(position).userName + g);
            }
            txt_partnerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneToOne(position);
                }
            });
            return convertView;
        }
    }
}
