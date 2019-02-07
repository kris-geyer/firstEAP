package architecture.geyerk.sensorlab.firsteap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeInvisibleComponents();
        initializeVisibleComponent();
        initializeService();   
    }

    private void initializeInvisibleComponents() {
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
    }

    private void updateTextView(String result) {
        TextView tv = findViewById(R.id.tvResult);
        tv.setText(result);
    }

    private void initializeVisibleComponent() {
        updateTextView("total time: " + prefs.getLong("total screen on", 0));
        Button btn = findViewById(R.id.btnReport);
        btn.setOnClickListener(this);
    }

    private void initializeService() {
        startService(new Intent(this, service.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReport:
                updateTextView("total time: " + prefs.getLong("total screen on", 0));
                break;
        }
    }
}
