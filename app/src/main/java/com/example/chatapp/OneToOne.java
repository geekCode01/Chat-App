//package com.example.chatapp;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ContentProviderOperation;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.BitSet;
//import java.util.Objects;
//
//public class OneToOne extends AppCompatActivity {
//
//    private static final String activityName = "OneToOne";
//    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
//    private static final DatabaseReference myRef = database.getReference();
//    private static final DatabaseReference myErrorRef = database.getReference().child("errors").child(activityName);
//    private ErrorClass error_class = new ErrorClass();
//    private mydatabase db = new mydatabase(this);
//    private ListView Lst_Chat;
//    private EditText txt_msg;
//    private ArrayList<msg_class> onlineMessages = new ArrayList<>();
//    private ArrayList<msg_class> offlineMessages = new ArrayList<>();
//    private ArrayList<String> profile = new ArrayList<>();
//    private LinearLayout layout_translateSettings;
//    private CheckBox cb_enableT;
//    private Spinner spnr_from, spnr_to;
//    private String lastmsg, chatRoomId, userId, partnerId, partnerName, sourceLanguage, targetLanguage;
//    private String[] lang;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_one_to_one);
//
//        try {
//            Lst_Chat = findViewById(R.id.Lst_Chat);
//            txt_msg = findViewById(R.id.txt_msg);
//            layout_translateSettings = findViewById(R.id.layout_translateSettings);
//            cb_enableT = findViewById(R.id.cb_enableT);
//            spnr_from = findViewById(R.id.spnr_from);
//            spnr_to = findViewById(R.id.spnr_to);
//            TextView txt_partnerName = findViewById(R.id.txt_partnerName);
//            lang = getResources().getStringArray(R.array.lang);
//            userId = db.getString("SELECT user_id FROM user ORDER BY id DESC LIMIT 1");
//            String qry = "SELECT userName, userGender FROM profile ORDER BY id DESC LIMIT 1";
//            profile = db.getProfileChat(qry);
//            partnerId = Objects.requireNonNull(getIntent().getExtras()).getString("id");
//            partnerName = Objects.requireNonNull(getIntent().getExtras()).getString("name");
//            txt_partnerName.setText(partnerName);
//            if (userId.compareTo(partnerId) >= 0)
//                chatRoomId = userId + "-" + partnerId;
//            else
//                chatRoomId = partnerId + "-" + userId;
//            lastmsg = db.getString("SELECT msgId FROM chat WHERE groupNumber ='" + chatRoomId + "' ORDER BY id DESC LIMIT 1");
//            getData();
//            getOnlineMsg();
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void getData() {
//        try {
//            String qry = "SELECT * FROM chat WHERE groupNumber ='" + chatRoomId + "'";
//            offlineMessages = db.getMsg(qry);
//            if (!offlineMessages.get(0).empty.matches("1"))
//                viewData();
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void viewData() {
//        try {
//            Group.Listadapter listadapter = new Group.Listadapter();
//            Lst_Chat.setAdapter(listadapter);
//            Lst_Chat.setSelection(listadapter.getCount() - 1);
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void getOnlineMsg() {
//        try {
//            myRef.child("oneToOne").child(chatRoomId).orderByKey().startAt(lastmsg).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot singleSnapShot : snapshot.getChildren())
//                        onlineMessages.add(singleSnapShot.getValue(msg_class.class));
//                    if (onlineMessages.size() > 0)
//                        saveLocalDB();
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void saveLocalDB() {
//        try {
//            String userName, userGender, userId, msgId, msg;
//            boolean result;
//            for (int i = 0; i < onlineMessages.size(); i++) {
//                msgId = onlineMessages.get(i).msgId;
//                int id = db.getInteger("SELECT id FROM chat WHERE msgId ='" + msgId + "'");
//                if (id == -1) { /* -1 not 1 */
//                    userId = onlineMessages.get(i).userId;
//                    userName = onlineMessages.get(i).userName;
//                    userGender = onlineMessages.get(i).userGender;
//                    msg = onlineMessages.get(i).msg;
//                    result = db.insertMsg(msgId, userName, userId, msg, userGender, chatRoomId);
//                    if (!result)
//                        Toast.makeText(this, getResources().getString(R.string.ErrorSave), Toast.LENGTH_LONG).show();
//                    String qry = "REPLACE INTO partners(roomId, partner) VALUES('" + chatRoomId + "','" + partnerName + "')";
//                    db.insertPartner(qry);
//                }
//            }
//            onlineMessages.clear();
//            getData();
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void translate() {
//        try {
//            TranslatorOptions options =
//                    new TranslatorOptions.Builder()
//                            .setSourceLanguage(sourceLanguage)
//                            .setTargetLanguage(targetLanguage)
//                            .build();
//            final Translator translator =
//                    Translation.getClient(options);
//            ContentProviderOperation conditions = new DownloadConditions.Builder()
//                    .requireWifi()
//                    .build();
//            translator.downloadModelIfNeeded(conditions)
//                    .addOnSuccessListener(
//                            new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void v) {
//                                    translator.translate(txt_msg.getText().toString().trim())
//                                            .addOnSuccessListener(
//                                                    new OnSuccessListener<String>() {
//                                                        @Override
//                                                        public void onSuccess(@NonNull String translatedText) {
//                                                            txt_msg.setText(translatedText);
//                                                            _msgSend();
//                                                        }
//                                                    })
//                                            .addOnFailureListener(
//                                                    new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            String functionName = Objects.requireNonNull(new Object() {
//                                                            }.getClass().getEnclosingMethod()).getName();
//                                                            int i = 0;
//                                                            for (StackTraceElement ste : e.getStackTrace()) {
//                                                                if (ste.getClassName().contains(activityName))
//                                                                    break;
//                                                                i++;
//                                                            }
//                                                            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//                                                            String msg = e.getMessage();
//                                                            error_class.sendError(myErrorRef, lineError, msg, functionName);
//                                                        }
//                                                    });
//                                }
//                            })
//                    .addOnFailureListener(
//                            new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    String functionName = Objects.requireNonNull(new Object() {
//                                    }.getClass().getEnclosingMethod()).getName();
//                                    int i = 0;
//                                    for (StackTraceElement ste : e.getStackTrace()) {
//                                        if (ste.getClassName().contains(activityName))
//                                            break;
//                                        i++;
//                                    }
//                                    String lineError = e.getStackTrace()[i].getLineNumber() + "";
//                                    String msg = e.getMessage();
//                                    error_class.sendError(myErrorRef, lineError, msg, functionName);
//                                }
//                            });
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void getLangs() {
//        try {
//            int s, t;
//            s = spnr_from.getSelectedItemPosition();
//            t = spnr_to.getSelectedItemPosition();
//            sourceLanguage = lang[s];
//            targetLanguage = lang[t];
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void translationS(View view) {
//        try {
//            if (layout_translateSettings.getVisibility() == View.VISIBLE) {
//                layout_translateSettings.setVisibility(View.GONE);
//                Lst_Chat.setVisibility(View.VISIBLE);
//            } else {
//                layout_translateSettings.setVisibility(View.VISIBLE);
//                Lst_Chat.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void back(View view) {
//        try {
//            layout_translateSettings.setVisibility(View.GONE);
//            Lst_Chat.setVisibility(View.VISIBLE);
//        } catch (Exception e) {
//            String functionName = Objects.requireNonNull(new Object() {
//            }.getClass().getEnclosingMethod()).getName();
//            int i = 0;
//            for (StackTraceElement ste : e.getStackTrace()) {
//                if (ste.getClassName().contains(activityName))
//                    break;
//                i++;
//            }
//            String lineError = e.getStackTrace()[i].getLineNumber() + "";
//            String msg = e.getMessage();
//            error_class.sendError(myErrorRef, lineError, msg, functionName);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//
//
//
//
//
//    class Listadapter extends BaseAdapter {
//        private BitSet offlineMessages;
//
//        @Override
//        public int getCount() {
//            return offlineMessages.size();
//        }
//        @Override
//        public Object getItem(int position) {
//            return offlineMessages.get(position);
//        }
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = getLayoutInflater();
//            convertView = inflater.inflate(R.layout.chat_adapter, parent, false);
//            TextView partner_msg = convertView.findViewById(R.id.partner_msg);
//            TextView txt_partnerName = convertView.findViewById(R.id.txt_partnerName);
//            TextView my_msg = convertView.findViewById(R.id.my_msg);
//            TextView txt_myName = convertView.findViewById(R.id.txt_myName);
//            LinearLayout partner_layout = convertView.findViewById(R.id.partner_layout);
//            LinearLayout my_layout = convertView.findViewById(R.id.my_layout);
//            String g;
//            if (offlineMessages.get(position).userGender.matches("Male")) {
//                g = " ,M";
//            } else
//                g = " ,F";
//            if (offlineMessages.get(position).userID.matches(userId)) {
//                my_msg.setText(offlineMessages.get(position).msg);
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) partner_layout.getLayoutParams();
//                layoutParams.height = 0;
//                partner_layout.setLayoutParams(layoutParams);
//                txt_myName.setText(offlineMessages.get(position).userName + g);
//            } else {
//                partner_msg.setText(offlineMessages.get(position).msg);
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) my_layout.getLayoutParams();
//                layoutParams.height = 0;
//                my_layout.setLayoutParams(layoutParams);
//                txt_partnerName.setText(offlineMessages.get(position).userName + g);
//            }
//
//
//
//            return null;
//        }
//    }
//
//}

package com.example.chatapp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
public class OneToOne extends AppCompatActivity {
    private static final String activityName = "OneToOne";
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
    private LinearLayout layout_translateSettings;
    private CheckBox cb_enableT;
    private Spinner spnr_from, spnr_to;
    private String lastmsg, chatRoomId, userId, partnerId, partnerName, sourceLanguage, targetLanguage;
    private String[] lang;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one);
        try {
            Lst_Chat = findViewById(R.id.Lst_Chat);
            txt_msg = findViewById(R.id.txt_msg);
            layout_translateSettings = findViewById(R.id.layout_translateSettings);
            cb_enableT = findViewById(R.id.cb_enableT);
            spnr_from = findViewById(R.id.spnr_from);
            spnr_to = findViewById(R.id.spnr_to);
            TextView txt_partnerName = findViewById(R.id.txt_partnerName);
            lang = getResources().getStringArray(R.array.lang);
            userId = db.getString("SELECT user_id FROM user ORDER BY id DESC LIMIT 1");
            String qry = "SELECT userName, userGender FROM profile ORDER BY id DESC LIMIT 1";
            profile = db.getProfileChat(qry);
            partnerId = Objects.requireNonNull(getIntent().getExtras()).getString("id");
            partnerName = Objects.requireNonNull(getIntent().getExtras()).getString("name");
            txt_partnerName.setText(partnerName);
            if (userId.compareTo(partnerId) >= 0)
                chatRoomId = userId + "-" + partnerId;
            else
                chatRoomId = partnerId + "-" + userId;
            lastmsg = db.getString("SELECT msgId FROM chat WHERE groupNumber ='" + chatRoomId + "' ORDER BY id DESC LIMIT 1");
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
    private void getData() {
        try {
            String qry = "SELECT * FROM chat WHERE groupNumber ='" + chatRoomId + "'";
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
    private void getOnlineMsg() {
        try {
            myRef.child("oneToOne").child(chatRoomId).orderByKey().startAt(lastmsg).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleSnapShot : snapshot.getChildren())
                        onlineMessages.add(singleSnapShot.getValue(msg_class.class));
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
                int id = db.getInteger("SELECT id FROM chat WHERE msgId ='" + msgId + "'");
                if (id == -1) { /* -1 not 1 */
                    userId = onlineMessages.get(i).userId;
                    userName = onlineMessages.get(i).userName;
                    userGender = onlineMessages.get(i).userGender;
                    msg = onlineMessages.get(i).msg;
                    result = db.insertMsg(msgId, userName, userId, msg, userGender, chatRoomId);
                    if (!result)
                        Toast.makeText(this, getResources().getString(R.string.ErrorSave), Toast.LENGTH_LONG).show();
                    String qry = "REPLACE INTO partners(roomId, partner) VALUES('" + chatRoomId + "','" + partnerName + "')";
                    db.insertPartner(qry);
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
    private void translate() {
        try {
            TranslatorOptions options =
                    new TranslatorOptions.Builder()
                            .setSourceLanguage(sourceLanguage)
                            .setTargetLanguage(targetLanguage)
                            .build();
            final Translator translator =
                    Translation.getClient(options);
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    translator.translate(txt_msg.getText().toString().trim())
                                            .addOnSuccessListener(
                                                    new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(@NonNull String translatedText) {
                                                            txt_msg.setText(translatedText);
                                                            _msgSend();
                                                        }
                                                    })
                                            .addOnFailureListener(
                                                    new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
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
                                                    });
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
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
    private void _msgSend() {
        try {
            String msg = txt_msg.getText().toString().trim();
            String userName = profile.get(0);
            final String userGender = profile.get(1);
            String key = myRef.child("oneToOne").child(chatRoomId).push().getKey();
            msg_class msgClass = new msg_class(key, userName, userId, msg, userGender);
            Map<String, Object> map = msgClass.toMap();
            assert key != null;
            myRef.child("oneToOne").child(chatRoomId).child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    txt_msg.setText("");
                    String key = myRef.child("oneToOneRooms").child(partnerId).child(userId).push().getKey();
                    myRef.child("oneToOneRooms").child(partnerId).child(userId).setValue(key);
                    myRef.child("oneToOneRooms").child(userId).child(partnerId).setValue(key);
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
    private void getLangs() {
        try {
            int s, t;
            s = spnr_from.getSelectedItemPosition();
            t = spnr_to.getSelectedItemPosition();
            sourceLanguage = lang[s];
            targetLanguage = lang[t];
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
    public void translationS(View view) {
        try {
            if (layout_translateSettings.getVisibility() == View.VISIBLE) {
                layout_translateSettings.setVisibility(View.GONE);
                Lst_Chat.setVisibility(View.VISIBLE);
            } else {
                layout_translateSettings.setVisibility(View.VISIBLE);
                Lst_Chat.setVisibility(View.GONE);
            }
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
    public void back(View view) {
        try {
            layout_translateSettings.setVisibility(View.GONE);
            Lst_Chat.setVisibility(View.VISIBLE);
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
            if (cb_enableT.isChecked()) {
                getLangs();
                translate();
            } else
                _msgSend();
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
            return convertView;
        }
    }
}