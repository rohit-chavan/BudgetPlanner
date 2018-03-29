package planner.budget.budgetplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.github.clans.fab.FloatingActionButton;

public class AddIncome extends AppCompatActivity {

    private ArrayList<Income_SpinnerItem> nSpinnerList;
    private Income_Spinner_Adapter nAdapter;
    FloatingActionButton mFAB_add_income;
    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;
    String nclickedItemName;
    DatabaseHelper dbhelper;
    EditText Income_amt, Income_desc, Income_category, Income_date;
    Button income_btn;
    Date date;
    String FINAL_DATE,YEAR,MONTH,DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        dbhelper = new DatabaseHelper(this);

        //activity_add_income attributes init
        Income_amt = (EditText)findViewById(R.id.income_editamt);
        Income_desc = (EditText)findViewById(R.id.income_editspend);
        mFAB_add_income = (FloatingActionButton) findViewById(R.id.income_submit_btn);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        initList(); //Spinner function call

        showDialogOnButtonClick();  //Calendar function call

        income_addData();       //Income add data function call

        Spinner income_spinnerItems = (Spinner) findViewById(R.id.income_spinner);

        income_spinnerItems.setPrompt("Select a Category");

        nAdapter = new Income_Spinner_Adapter(this, nSpinnerList);
        income_spinnerItems.setAdapter(nAdapter);

        income_spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Income_SpinnerItem nclickedItem = (Income_SpinnerItem) parent.getItemAtPosition(position);
                nclickedItemName = nclickedItem.getnCategoryName();
                Toast.makeText(AddIncome.this, nclickedItemName + "\t Category Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void income_addData() {
        mFAB_add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float floatamt = Float.valueOf(Income_amt.getText().toString());
                YEAR = Integer.toString(year_x);
                MONTH = Integer.toString(month_x);
                DAY = Integer.toString(day_x);
                FINAL_DATE = DAY+"/"+MONTH+"/"+YEAR;
                try {
                    date = new SimpleDateFormat("dd/mm/yyyy").parse(FINAL_DATE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

               boolean isInserted =  dbhelper.income_insertData(floatamt, Income_desc.getText().toString(), nclickedItemName, date);
                if (isInserted == true)
                    Toast.makeText(AddIncome.this, "Data Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddIncome.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                //On Data Inserted redirect to display Income
                Intent toincome = new Intent(AddIncome.this,NavDrawer_Income.class);
                startActivity(toincome);
                finish();
            }
        });
    }

    // Methods to add elements to Spiner IncomePage
    private void initList(){
        nSpinnerList = new ArrayList<>();
        nSpinnerList.add(new Income_SpinnerItem("Acc to Acc", R.drawable.vc_acc_to_acc));
        nSpinnerList.add(new Income_SpinnerItem("CC Bill Payment", R.drawable.vc_credit_card));
        nSpinnerList.add(new Income_SpinnerItem("Gifts", R.drawable.vc_gift));
        nSpinnerList.add(new Income_SpinnerItem("Interest", R.drawable.vc_interest));
        nSpinnerList.add(new Income_SpinnerItem("Mutual Funds", R.drawable.vc_mutual_funds));
        nSpinnerList.add(new Income_SpinnerItem("Provident Fund", R.drawable.vc_provident_funds));
        nSpinnerList.add(new Income_SpinnerItem("Salary", R.drawable.vc_salary));
        nSpinnerList.add(new Income_SpinnerItem("Savings", R.drawable.vc_savings));
        nSpinnerList.add(new Income_SpinnerItem("Investments", R.drawable.vc_investments));
        nSpinnerList.add(new Income_SpinnerItem("Others", R.drawable.vc_others));

    }

    public void showDialogOnButtonClick(){
        income_btn = (Button) findViewById(R.id.income_calendar_button);

        income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id == Dialog_Id){
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;

    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            Toast.makeText(AddIncome.this, year_x + " / " + month_x + " / " + day_x, Toast.LENGTH_LONG).show();
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
