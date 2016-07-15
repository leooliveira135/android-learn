package com.example.leonardooliveira.boaviagem.activity.gasto;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.leonardooliveira.boaviagem.R;

import java.util.Calendar;

/**
 * Created by Leonardo Oliveira on 30/06/2016.
 */
public class GastoActivity extends AppCompatActivity {

    private int ano;
    private int mes;
    private int dia;
    private Button btnData;
    private Spinner categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_gasto);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        btnData = (Button)findViewById(R.id.btnData);
        btnData.setText(dia + "/" + (mes+1) + "/" + ano);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoria_gasto, R.layout.support_simple_spinner_dropdown_item);
        categoria = (Spinner)findViewById(R.id.spinnerCategoria);
        categoria.setAdapter(adapter);
    }

    public void selecionarData(View v){
        showDialog(v.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(R.id.btnData == id) {
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            btnData.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };
}
