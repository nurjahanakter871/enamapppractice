
 package com.appdev.EnamAppSyncWebHost;


 import android.annotation.SuppressLint;
 import Utility.Global;
 import java.sql.Connection;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.HashMap;
 import java.util.List;
 import android.app.*;
 import android.app.AlertDialog;
 import android.app.DatePickerDialog;
 import android.app.Dialog;
 import android.app.TimePickerDialog;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.location.Location;
 import android.provider.Settings;
 import android.view.KeyEvent;
 import android.os.Bundle;
 import android.view.View;
 import android.view.MotionEvent;
 import android.widget.AdapterView;
 import android.widget.Button;
 import android.widget.CheckBox;
 import android.widget.DatePicker;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.LinearLayout;
 import android.widget.RadioButton;
 import android.widget.RadioGroup;
 import android.widget.SimpleAdapter;
 import android.widget.Spinner;
 import android.widget.TextView;
 import android.widget.TimePicker;
 import android.widget.ArrayAdapter;
 import android.graphics.Color;
 import android.view.WindowManager;
 import androidx.constraintlayout.widget.ConstraintLayout;
 import android.widget.AutoCompleteTextView;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.core.content.ContextCompat;
 import forms_datamodel.EligibilityScrToolforChild_DataModel;
 import Utility.*;
 import Common.*;
 import android.widget.Toast;
 import android.text.TextWatcher;
 import android.widget.CompoundButton;
 import android.text.Editable;

 import com.android.identity.android.legacy.Utility;

 public class EligibilityScrToolforChild extends AppCompatActivity {
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override 
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) 
             { return false; }
        else { return true;  }
    }

    boolean networkAvailable=false;
    Location currentLocation; 
    double currentLatitude,currentLongitude; 
    String VariableID;
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;

    Settings.Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    TextView lblHeading;
    LinearLayout secuuid;
    View lineuuid;
    TextView lbluuid;
    TextView Vlbluuid;
    EditText txtuuid;
    LinearLayout seclbl1;
    View linelbl1;
    TextView lbllbl1;
    TextView Vlbllbl1;
    EditText txtlbl1;
    LinearLayout secQ01;
    View lineQ01;
    TextView lblQ01;
    TextView VlblQ01;
    RadioGroup rdogrpQ01;
    RadioButton rdoQ011;
    RadioButton rdoQ012;
    LinearLayout secQ02Date;
    View lineQ02Date;
    TextView lblQ02Date;
    TextView VlblQ02Date;
    EditText txtQ02Date;
    LinearLayout secQ02Y;
    View lineQ02Y;
    TextView lblQ02Y;
    TextView VlblQ02Y;
    EditText txtQ02Y;
    LinearLayout secQ03;
    View lineQ03;
    TextView lblQ03;
    TextView VlblQ03;
    RadioGroup rdogrpQ03;
    RadioButton rdoQ031;
    RadioButton rdoQ032;
    LinearLayout secQ04;
    View lineQ04;
    TextView lblQ04;
    TextView VlblQ04;
    RadioGroup rdogrpQ04;
    RadioButton rdoQ041;
    RadioButton rdoQ042;
    LinearLayout secQ05;
    View lineQ05;
    TextView lblQ05;
    TextView VlblQ05;
    RadioGroup rdogrpQ05;
    RadioButton rdoQ051;
    RadioButton rdoQ052;
    LinearLayout secQ06;
    View lineQ06;
    TextView lblQ06;
    TextView VlblQ06;
    RadioGroup rdogrpQ06;
    RadioButton rdoQ061;
    RadioButton rdoQ062;
    LinearLayout secQ07;
    View lineQ07;
    TextView lblQ07;
    TextView VlblQ07;
    RadioGroup rdogrpQ07;
    RadioButton rdoQ071;
    RadioButton rdoQ072;

    static int MODULEID   = 0;
    static int LANGUAGEID = 0;
    static String TableName;

    static String STARTTIME = "";
    static String DEVICEID  = "";
    static String ENTRYUSER = "";
    MySharedPreferences sp;

    Bundle IDbundle;
    static String UUID = "";

 @SuppressLint("ClickableViewAccessibility")
 public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
   try
     {
         setContentView(R.layout.eligibilityscrtoolforchild);
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         C = new Connection(this);
         g = Global.getInstance();

         STARTTIME = g.CurrentTime24();
         DEVICEID  = MySharedPreferences.getValue(this, "deviceid");
         ENTRYUSER = MySharedPreferences.getValue(this, "userid");

         IDbundle = getIntent().getExtras();
         UUID = IDbundle.getString("uuid");

         TableName = "EligibilityScrToolforChild";
         MODULEID = 9;
         LANGUAGEID = Integer.parseInt(MySharedPreferences.getValue(this, "languageid"));
         lblHeading = (TextView)findViewById(R.id.lblHeading);

         ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
         cmdBack.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 AlertDialog.Builder adb = new AlertDialog.Builder(EligibilityScrToolforChild.this);
                 adb.setTitle("Close");
                 adb.setIcon(R.drawable.favicon);
                 adb.setMessage("Do you want to close this form[Yes/No]?");
                 adb.setNegativeButton("No", null);
                 adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         finish();
                     }});
                 adb.show();
             }});

        Initialization();
        Connection.LocalizeLanguage(EligibilityScrToolforChild.this, MODULEID, LANGUAGEID);


         txtQ02Date.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btnQ02Date"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });



         //Hide all skip variables
         secQ02Date.setVisibility(View.GONE);
         lineQ02Date.setVisibility(View.GONE);
         secQ02Y.setVisibility(View.GONE);
         lineQ02Y.setVisibility(View.GONE);
         secQ03.setVisibility(View.GONE);
         lineQ03.setVisibility(View.GONE);
         secQ04.setVisibility(View.GONE);
         lineQ04.setVisibility(View.GONE);
         secQ05.setVisibility(View.GONE);
         lineQ05.setVisibility(View.GONE);
         secQ06.setVisibility(View.GONE);
         lineQ06.setVisibility(View.GONE);
         secQ07.setVisibility(View.GONE);
         lineQ07.setVisibility(View.GONE);
         secQ04.setVisibility(View.GONE);
         lineQ04.setVisibility(View.GONE);
         secQ05.setVisibility(View.GONE);
         lineQ05.setVisibility(View.GONE);
         secQ06.setVisibility(View.GONE);
         lineQ06.setVisibility(View.GONE);
         secQ07.setVisibility(View.GONE);
         lineQ07.setVisibility(View.GONE);
         secQ05.setVisibility(View.GONE);
         lineQ05.setVisibility(View.GONE);
         secQ06.setVisibility(View.GONE);
         lineQ06.setVisibility(View.GONE);
         secQ07.setVisibility(View.GONE);
         lineQ07.setVisibility(View.GONE);
         secQ06.setVisibility(View.GONE);
         lineQ06.setVisibility(View.GONE);
         secQ07.setVisibility(View.GONE);
         lineQ07.setVisibility(View.GONE);
         secQ07.setVisibility(View.GONE);
         lineQ07.setVisibility(View.GONE);
         sec.setVisibility(View.GONE);
         line.setVisibility(View.GONE);


        DataSearch(UUID);


        Button cmdSave = (Button) findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) { 
            DataSave();
        }});

        Global.RoleManagement(cmdSave, MySharedPreferences.getValue(this, "userrole"));
     }
     catch(Exception  e)
     {
         Connection.MessageBox(EligibilityScrToolforChild.this, e.getMessage());
         return;
     }
 }

 @SuppressLint("ClickableViewAccessibility")
 private void Initialization()
 {
   try
     {
         secuuid = findViewById(R.id.secuuid);
         lineuuid = findViewById(R.id.lineuuid);
         lbluuid = findViewById(R.id.lbluuid);
         Vlbluuid = findViewById(R.id.Vlbluuid);
         txtuuid = findViewById(R.id.txtuuid);
         if (UUID.length() == 0) txtuuid.setText(Global.Get_UUID());
         else txtuuid.setText(UUID); 
         txtuuid.setEnabled(false);
         secuuid.setVisibility(View.GONE);
         seclbl1 = findViewById(R.id.seclbl1);
         linelbl1 = findViewById(R.id.linelbl1);
         lbllbl1 = findViewById(R.id.lbllbl1);
         Vlbllbl1 = findViewById(R.id.Vlbllbl1);
         txtlbl1 = findViewById(R.id.txtlbl1);
         secQ01 = findViewById(R.id.secQ01);
         lineQ01 = findViewById(R.id.lineQ01);
         lblQ01 =  findViewById(R.id.lblQ01);
         VlblQ01 =  findViewById(R.id.VlblQ01);
         rdogrpQ01 =  findViewById(R.id.rdogrpQ01);
         rdoQ011 =  findViewById(R.id.rdoQ011);
         rdoQ012 =  findViewById(R.id.rdoQ012);
         rdogrpQ01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
             String rbData = "";
             RadioButton rb;
             String[] d_rdogrpQ01 = new String[] {"1","2"};
             for (int i = 0; i < rdogrpQ01.getChildCount(); i++)
             {
               rb = (RadioButton) rdogrpQ01.getChildAt(i);
               if (rb.isChecked()) rbData = d_rdogrpQ01[i];
             }

             if(rbData.equalsIgnoreCase("2"))
             {
                    secQ02Date.setVisibility(View.GONE);
                    lineQ02Date.setVisibility(View.GONE);
                    txtQ02Date.setText("");
                    secQ02Y.setVisibility(View.GONE);
                    lineQ02Y.setVisibility(View.GONE);
                    txtQ02Y.setText("");
                    secQ03.setVisibility(View.GONE);
                    lineQ03.setVisibility(View.GONE);
                    rdogrpQ03.clearCheck();
                    secQ04.setVisibility(View.GONE);
                    lineQ04.setVisibility(View.GONE);
                    rdogrpQ04.clearCheck();
                    secQ05.setVisibility(View.GONE);
                    lineQ05.setVisibility(View.GONE);
                    rdogrpQ05.clearCheck();
                    secQ06.setVisibility(View.GONE);
                    lineQ06.setVisibility(View.GONE);
                    rdogrpQ06.clearCheck();
                    secQ07.setVisibility(View.GONE);
                    lineQ07.setVisibility(View.GONE);
                    rdogrpQ07.clearCheck();
             }
             else
             {
                    secQ02Date.setVisibility(View.VISIBLE);
                    lineQ02Date.setVisibility(View.VISIBLE);
                    secQ02Y.setVisibility(View.VISIBLE);
                    lineQ02Y.setVisibility(View.VISIBLE);
                    secQ03.setVisibility(View.VISIBLE);
                    lineQ03.setVisibility(View.VISIBLE);
                    secQ04.setVisibility(View.VISIBLE);
                    lineQ04.setVisibility(View.VISIBLE);
                    secQ05.setVisibility(View.VISIBLE);
                    lineQ05.setVisibility(View.VISIBLE);
                    secQ06.setVisibility(View.VISIBLE);
                    lineQ06.setVisibility(View.VISIBLE);
                    secQ07.setVisibility(View.VISIBLE);
                    lineQ07.setVisibility(View.VISIBLE);
             }
            }
         public void onNothingSelected(AdapterView<?> adapterView) {
             return;
            } 
         }); 
         secQ02Date = findViewById(R.id.secQ02Date);
         lineQ02Date = findViewById(R.id.lineQ02Date);
         lblQ02Date = findViewById(R.id.lblQ02Date);
         VlblQ02Date = findViewById(R.id.VlblQ02Date);
         txtQ02Date = findViewById(R.id.txtQ02Date);
         secQ02Y = findViewById(R.id.secQ02Y);
         lineQ02Y = findViewById(R.id.lineQ02Y);
         lblQ02Y = findViewById(R.id.lblQ02Y);
         VlblQ02Y = findViewById(R.id.VlblQ02Y);
         txtQ02Y = findViewById(R.id.txtQ02Y);
         secQ03 = findViewById(R.id.secQ03);
         lineQ03 = findViewById(R.id.lineQ03);
         lblQ03 =  findViewById(R.id.lblQ03);
         VlblQ03 =  findViewById(R.id.VlblQ03);
         rdogrpQ03 =  findViewById(R.id.rdogrpQ03);
         rdoQ031 =  findViewById(R.id.rdoQ031);
         rdoQ032 =  findViewById(R.id.rdoQ032);
         rdogrpQ03.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
             String rbData = "";
             RadioButton rb;
             String[] d_rdogrpQ03 = new String[] {"1","2"};
             for (int i = 0; i < rdogrpQ03.getChildCount(); i++)
             {
               rb = (RadioButton) rdogrpQ03.getChildAt(i);
               if (rb.isChecked()) rbData = d_rdogrpQ03[i];
             }

             if(rbData.equalsIgnoreCase("2"))
             {
                    secQ04.setVisibility(View.GONE);
                    lineQ04.setVisibility(View.GONE);
                    rdogrpQ04.clearCheck();
                    secQ05.setVisibility(View.GONE);
                    lineQ05.setVisibility(View.GONE);
                    rdogrpQ05.clearCheck();
                    secQ06.setVisibility(View.GONE);
                    lineQ06.setVisibility(View.GONE);
                    rdogrpQ06.clearCheck();
                    secQ07.setVisibility(View.GONE);
                    lineQ07.setVisibility(View.GONE);
                    rdogrpQ07.clearCheck();
             }
             else
             {
                    secQ04.setVisibility(View.VISIBLE);
                    lineQ04.setVisibility(View.VISIBLE);
                    secQ05.setVisibility(View.VISIBLE);
                    lineQ05.setVisibility(View.VISIBLE);
                    secQ06.setVisibility(View.VISIBLE);
                    lineQ06.setVisibility(View.VISIBLE);
                    secQ07.setVisibility(View.VISIBLE);
                    lineQ07.setVisibility(View.VISIBLE);
             }
            }
         public void onNothingSelected(AdapterView<?> adapterView) {
             return;
            } 
         }); 
         secQ04 = findViewById(R.id.secQ04);
         lineQ04 = findViewById(R.id.lineQ04);
         lblQ04 =  findViewById(R.id.lblQ04);
         VlblQ04 =  findViewById(R.id.VlblQ04);
         rdogrpQ04 =  findViewById(R.id.rdogrpQ04);
         rdoQ041 =  findViewById(R.id.rdoQ041);
         rdoQ042 =  findViewById(R.id.rdoQ042);
         rdogrpQ04.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
             String rbData = "";
             RadioButton rb;
             String[] d_rdogrpQ04 = new String[] {"1","2"};
             for (int i = 0; i < rdogrpQ04.getChildCount(); i++)
             {
               rb = (RadioButton) rdogrpQ04.getChildAt(i);
               if (rb.isChecked()) rbData = d_rdogrpQ04[i];
             }

             if(rbData.equalsIgnoreCase("1"))
             {
                    secQ05.setVisibility(View.GONE);
                    lineQ05.setVisibility(View.GONE);
                    rdogrpQ05.clearCheck();
                    secQ06.setVisibility(View.GONE);
                    lineQ06.setVisibility(View.GONE);
                    rdogrpQ06.clearCheck();
                    secQ07.setVisibility(View.GONE);
                    lineQ07.setVisibility(View.GONE);
                    rdogrpQ07.clearCheck();
             }
             else
             {
                    secQ05.setVisibility(View.VISIBLE);
                    lineQ05.setVisibility(View.VISIBLE);
                    secQ06.setVisibility(View.VISIBLE);
                    lineQ06.setVisibility(View.VISIBLE);
                    secQ07.setVisibility(View.VISIBLE);
                    lineQ07.setVisibility(View.VISIBLE);
             }
            }
         public void onNothingSelected(AdapterView<?> adapterView) {
             return;
            } 
         }); 
         secQ05 = findViewById(R.id.secQ05);
         lineQ05 = findViewById(R.id.lineQ05);
         lblQ05 =  findViewById(R.id.lblQ05);
         VlblQ05 =  findViewById(R.id.VlblQ05);
         rdogrpQ05 =  findViewById(R.id.rdogrpQ05);
         rdoQ051 =  findViewById(R.id.rdoQ051);
         rdoQ052 =  findViewById(R.id.rdoQ052);
         rdogrpQ05.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
             String rbData = "";
             RadioButton rb;
             String[] d_rdogrpQ05 = new String[] {"1","2"};
             for (int i = 0; i < rdogrpQ05.getChildCount(); i++)
             {
               rb = (RadioButton) rdogrpQ05.getChildAt(i);
               if (rb.isChecked()) rbData = d_rdogrpQ05[i];
             }

             if(rbData.equalsIgnoreCase("1"))
             {
                    secQ06.setVisibility(View.GONE);
                    lineQ06.setVisibility(View.GONE);
                    rdogrpQ06.clearCheck();
                    secQ07.setVisibility(View.GONE);
                    lineQ07.setVisibility(View.GONE);
                    rdogrpQ07.clearCheck();
             }
             else
             {
                    secQ06.setVisibility(View.VISIBLE);
                    lineQ06.setVisibility(View.VISIBLE);
                    secQ07.setVisibility(View.VISIBLE);
                    lineQ07.setVisibility(View.VISIBLE);
             }
            }
         public void onNothingSelected(AdapterView<?> adapterView) {
             return;
            } 
         }); 
         secQ06 = findViewById(R.id.secQ06);
         lineQ06 = findViewById(R.id.lineQ06);
         lblQ06 =  findViewById(R.id.lblQ06);
         VlblQ06 =  findViewById(R.id.VlblQ06);
         rdogrpQ06 =  findViewById(R.id.rdogrpQ06);
         rdoQ061 =  findViewById(R.id.rdoQ061);
         rdoQ062 =  findViewById(R.id.rdoQ062);
         rdogrpQ06.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
             String rbData = "";
             RadioButton rb;
             String[] d_rdogrpQ06 = new String[] {"1","2"};
             for (int i = 0; i < rdogrpQ06.getChildCount(); i++)
             {
               rb = (RadioButton) rdogrpQ06.getChildAt(i);
               if (rb.isChecked()) rbData = d_rdogrpQ06[i];
             }

             if(rbData.equalsIgnoreCase("2"))
             {
                    secQ07.setVisibility(View.GONE);
                    lineQ07.setVisibility(View.GONE);
                    rdogrpQ07.clearCheck();
             }
             else
             {
                    secQ07.setVisibility(View.VISIBLE);
                    lineQ07.setVisibility(View.VISIBLE);
             }
            }
         public void onNothingSelected(AdapterView<?> adapterView) {
             return;
            } 
         }); 
         secQ07 = findViewById(R.id.secQ07);
         lineQ07 = findViewById(R.id.lineQ07);
         lblQ07 =  findViewById(R.id.lblQ07);
         VlblQ07 =  findViewById(R.id.VlblQ07);
         rdogrpQ07 =  findViewById(R.id.rdogrpQ07);
         rdoQ071 =  findViewById(R.id.rdoQ071);
         rdoQ072 =  findViewById(R.id.rdoQ072);
         rdogrpQ07.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
         @Override
         public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
             String rbData = "";
             RadioButton rb;
             String[] d_rdogrpQ07 = new String[] {"1","2"};
             for (int i = 0; i < rdogrpQ07.getChildCount(); i++)
             {
               rb = (RadioButton) rdogrpQ07.getChildAt(i);
               if (rb.isChecked()) rbData = d_rdogrpQ07[i];
             }

             if(rbData.equalsIgnoreCase("2"))
             {
                    sec.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
             }
             else
             {
                    sec.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
             }
            }
         public void onNothingSelected(AdapterView<?> adapterView) {
             return;
            } 
         }); 
     }
     catch(Exception  e)
     {
         Connection.MessageBox(EligibilityScrToolforChild.this, e.getMessage());
         return;
     }
 }

 private void DataSave()
 {
   try
     {
         String ValidationMSG = ValidationCheck();
         if(ValidationMSG.length()>0)
         {
         	Connection.MessageBox(EligibilityScrToolforChild.this, ValidationMSG);
         	return;
         }
 
         String SQL = "";
         RadioButton rb;

         EligibilityScrToolforChild_DataModel objSave = new EligibilityScrToolforChild_DataModel();
         objSave.setuuid(txtuuid.getText().toString());
         objSave.setlbl1(txtlbl1.getText().toString());
         String[] d_rdogrpQ01 = new String[] {"1","2"};
         objSave.setQ01("");
         for (int i = 0; i < rdogrpQ01.getChildCount(); i++)
         {
             rb = (RadioButton) rdogrpQ01.getChildAt(i);
             if (rb.isChecked()) objSave.setQ01(d_rdogrpQ01[i]);
         }

         objSave.setQ02Date(txtQ02Date.getText().toString().length() > 0 ? Global.DateConvertYMD(txtQ02Date.getText().toString()) : txtQ02Date.getText().toString());
         objSave.setQ02Y(txtQ02Y.getText().toString());
         String[] d_rdogrpQ03 = new String[] {"1","2"};
         objSave.setQ03("");
         for (int i = 0; i < rdogrpQ03.getChildCount(); i++)
         {
             rb = (RadioButton) rdogrpQ03.getChildAt(i);
             if (rb.isChecked()) objSave.setQ03(d_rdogrpQ03[i]);
         }

         String[] d_rdogrpQ04 = new String[] {"1","2"};
         objSave.setQ04("");
         for (int i = 0; i < rdogrpQ04.getChildCount(); i++)
         {
             rb = (RadioButton) rdogrpQ04.getChildAt(i);
             if (rb.isChecked()) objSave.setQ04(d_rdogrpQ04[i]);
         }

         String[] d_rdogrpQ05 = new String[] {"1","2"};
         objSave.setQ05("");
         for (int i = 0; i < rdogrpQ05.getChildCount(); i++)
         {
             rb = (RadioButton) rdogrpQ05.getChildAt(i);
             if (rb.isChecked()) objSave.setQ05(d_rdogrpQ05[i]);
         }

         String[] d_rdogrpQ06 = new String[] {"1","2"};
         objSave.setQ06("");
         for (int i = 0; i < rdogrpQ06.getChildCount(); i++)
         {
             rb = (RadioButton) rdogrpQ06.getChildAt(i);
             if (rb.isChecked()) objSave.setQ06(d_rdogrpQ06[i]);
         }

         String[] d_rdogrpQ07 = new String[] {"1","2"};
         objSave.setQ07("");
         for (int i = 0; i < rdogrpQ07.getChildCount(); i++)
         {
             rb = (RadioButton) rdogrpQ07.getChildAt(i);
             if (rb.isChecked()) objSave.setQ07(d_rdogrpQ07[i]);
         }

         objSave.setStartTime(STARTTIME);
         objSave.setEndTime(g.CurrentTime24());
         objSave.setDeviceID(DEVICEID);
         objSave.setEntryUser(ENTRYUSER); //from data entry user list
         objSave.setLat(MySharedPreferences.getValue(this, "lat"));
         objSave.setLon(MySharedPreferences.getValue(this, "lon"));

         String status = objSave.SaveUpdateData(this);
         if(status.length()==0) {
             Intent returnIntent = new Intent();
             returnIntent.putExtra("res", "");
             setResult(Activity.RESULT_OK, returnIntent);

             Toast.makeText(EligibilityScrToolforChild.this,"Save Successfully...",Toast.LENGTH_SHORT).show();
             finish();
         }
         else{
             Connection.MessageBox(EligibilityScrToolforChild.this, status);
             return;
         }
     }
     catch(Exception  e)
     {
         Connection.MessageBox(EligibilityScrToolforChild.this, e.getMessage());
         return;
     }
 }

 private String ValidationCheck()
 {
   String ValidationMsg = "";
   String DV = "";
   try
     {
         ResetSectionColor();
         if(txtuuid.getText().toString().length()==0 & secuuid.isShown())
           {
             ValidationMsg += "\n"+ lbluuid.getText().toString() + " Required field: "+ Vlbluuid.getText().toString();
             secuuid.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtlbl1.getText().toString().length()==0 & seclbl1.isShown())
           {
             ValidationMsg += "\n"+ lbllbl1.getText().toString() + " Required field: "+ Vlbllbl1.getText().toString();
             seclbl1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoQ011.isChecked() & !rdoQ012.isChecked() & secQ01.isShown())
           {
             ValidationMsg += "\n"+ lblQ01.getText().toString() + " Required field: "+ VlblQ01.getText().toString();
             secQ01.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(txtQ02Date.getText().toString());
         if(DV.length()!=0 & secQ02Date.isShown())
           {
             ValidationMsg += "\n"+ lblQ02Date.getText().toString() + " Required field/Not a valid date format: "+ VlblQ02Date.getText().toString();
             secQ02Date.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtQ02Y.getText().toString().length()==0 & secQ02Y.isShown())
           {
             ValidationMsg += "\n"+ lblQ02Y.getText().toString() + " Required field: "+ VlblQ02Y.getText().toString();
             secQ02Y.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(secQ02Y.isShown() & (Integer.parseInt(txtQ02Y.getText().toString().length()==0 ? "1" : txtQ02Y.getText().toString()) < 1 || Integer.parseInt(txtQ02Y.getText().toString().length()==0 ? "2" : txtQ02Y.getText().toString()) > 2))
           {
             ValidationMsg += "\n"+ lblQ02Y.getText().toString() + " Value should be between 1 and 2 (" + VlblQ02Y.getText().toString() +")";
             secQ02Y.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoQ031.isChecked() & !rdoQ032.isChecked() & secQ03.isShown())
           {
             ValidationMsg += "\n"+ lblQ03.getText().toString() + " Required field: "+ VlblQ03.getText().toString();
             secQ03.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoQ041.isChecked() & !rdoQ042.isChecked() & secQ04.isShown())
           {
             ValidationMsg += "\n"+ lblQ04.getText().toString() + " Required field: "+ VlblQ04.getText().toString();
             secQ04.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoQ051.isChecked() & !rdoQ052.isChecked() & secQ05.isShown())
           {
             ValidationMsg += "\n"+ lblQ05.getText().toString() + " Required field: "+ VlblQ05.getText().toString();
             secQ05.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoQ061.isChecked() & !rdoQ062.isChecked() & secQ06.isShown())
           {
             ValidationMsg += "\n"+ lblQ06.getText().toString() + " Required field: "+ VlblQ06.getText().toString();
             secQ06.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoQ071.isChecked() & !rdoQ072.isChecked() & secQ07.isShown())
           {
             ValidationMsg += "\n"+ lblQ07.getText().toString() + " Required field: "+ VlblQ07.getText().toString();
             secQ07.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
     }
     catch(Exception  e)
     {
         ValidationMsg += "\n"+ e.getMessage();
     }

     return ValidationMsg;
 }

 private void ResetSectionColor()
 {
   try
     {
             secuuid.setBackgroundColor(Color.WHITE);
             seclbl1.setBackgroundColor(Color.WHITE);
             secQ01.setBackgroundColor(Color.WHITE);
             secQ02Date.setBackgroundColor(Color.WHITE);
             secQ02Y.setBackgroundColor(Color.WHITE);
             secQ02Y.setBackgroundColor(Color.WHITE);
             secQ03.setBackgroundColor(Color.WHITE);
             secQ04.setBackgroundColor(Color.WHITE);
             secQ05.setBackgroundColor(Color.WHITE);
             secQ06.setBackgroundColor(Color.WHITE);
             secQ07.setBackgroundColor(Color.WHITE);
     }
     catch(Exception  e)
     {
     }
 }

 private void DataSearch(String uuid)
     {
       try
        {     
           RadioButton rb;
           EligibilityScrToolforChild_DataModel d = new EligibilityScrToolforChild_DataModel();
           String SQL = "Select * from "+ TableName +"  Where uuid='"+ uuid +"'";
           List<EligibilityScrToolforChild_DataModel> data = d.SelectAll(this, SQL);
           for(EligibilityScrToolforChild_DataModel item : data){
             txtuuid.setText(item.getuuid());
             txtlbl1.setText(item.getlbl1());
             String[] d_rdogrpQ01 = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpQ01.length; i++)
             {
                 if (String.valueOf(item.getQ01()).equals(String.valueOf(d_rdogrpQ01[i])))
                 {
                     rb = (RadioButton) rdogrpQ01.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             txtQ02Date.setText(item.getQ02Date().toString().length()==0 ? "" : Global.DateConvertDMY(item.getQ02Date()));
             txtQ02Y.setText(String.valueOf(item.getQ02Y()));
             String[] d_rdogrpQ03 = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpQ03.length; i++)
             {
                 if (String.valueOf(item.getQ03()).equals(String.valueOf(d_rdogrpQ03[i])))
                 {
                     rb = (RadioButton) rdogrpQ03.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             String[] d_rdogrpQ04 = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpQ04.length; i++)
             {
                 if (String.valueOf(item.getQ04()).equals(String.valueOf(d_rdogrpQ04[i])))
                 {
                     rb = (RadioButton) rdogrpQ04.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             String[] d_rdogrpQ05 = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpQ05.length; i++)
             {
                 if (String.valueOf(item.getQ05()).equals(String.valueOf(d_rdogrpQ05[i])))
                 {
                     rb = (RadioButton) rdogrpQ05.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             String[] d_rdogrpQ06 = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpQ06.length; i++)
             {
                 if (String.valueOf(item.getQ06()).equals(String.valueOf(d_rdogrpQ06[i])))
                 {
                     rb = (RadioButton) rdogrpQ06.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             String[] d_rdogrpQ07 = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpQ07.length; i++)
             {
                 if (String.valueOf(item.getQ07()).equals(String.valueOf(d_rdogrpQ07[i])))
                 {
                     rb = (RadioButton) rdogrpQ07.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
           }
        }
        catch(Exception  e)
        {
            Connection.MessageBox(EligibilityScrToolforChild.this, e.getMessage());
            return;
        }
     }



 protected Dialog onCreateDialog(int id) {
   final Calendar c = Calendar.getInstance();
   hour = c.get(Calendar.HOUR_OF_DAY);
   minute = c.get(Calendar.MINUTE);
   switch (id) {
       case DATE_DIALOG:
           return new DatePickerDialog(this, mDateSetListener,g.mYear,g.mMonth-1,g.mDay);
       case TIME_DIALOG:
           return new TimePickerDialog(this, timePickerListener, hour, minute,false);
       }
     return null;
 }

 private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
      mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
      EditText txtDate;


      txtDate = findViewById(R.id.txtQ02Date);
      if (VariableID.equals("btnQ02Date"))
      {
          txtDate = findViewById(R.id.txtQ02Date);
      }
      txtDate.setText(new StringBuilder()
      .append(Global.Right("00"+mDay,2)).append("/")
      .append(Global.Right("00"+mMonth,2)).append("/")
      .append(mYear));
      }
  };

 private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
       hour = selectedHour; minute = selectedMinute;
       EditText tpTime;

    }
  };


 
 // turning off the GPS if its in on state. to avoid the battery drain.
 @Override
 protected void onDestroy() {
     // TODO Auto-generated method stub
     super.onDestroy();
 }
}