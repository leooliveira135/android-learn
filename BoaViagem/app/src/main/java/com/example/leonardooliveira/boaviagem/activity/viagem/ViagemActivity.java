package com.example.leonardooliveira.boaviagem.activity.viagem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.leonardooliveira.boaviagem.R;
import com.example.leonardooliveira.boaviagem.activity.gasto.GastoActivity;
import com.example.leonardooliveira.boaviagem.calendar.CalendarService;
import com.example.leonardooliveira.boaviagem.dao.BoaViagemDAO;
import com.example.leonardooliveira.boaviagem.dao.DatabaseHelper;
import com.example.leonardooliveira.boaviagem.model.Constantes;
import com.example.leonardooliveira.boaviagem.model.Viagem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.leonardooliveira.boaviagem.model.Constantes.NOME_CONTA;
import static com.example.leonardooliveira.boaviagem.model.Constantes.PREFERENCIAS;
import static com.example.leonardooliveira.boaviagem.model.Constantes.TOKEN_ACESSO;

/**
 * Created by Leonardo Oliveira on 30/06/2016.
 */
public class ViagemActivity extends AppCompatActivity {

    private int dia;
    private int mes;
    private int ano;
    private Date dataChegada;
    private Date dataPartida;
    private Button btnPartida;
    private Button btnChegada;
    private DatabaseHelper databaseHelper;
    private EditText edDestino;
    private EditText edQtdPessoas;
    private EditText edOrcamento;
    private RadioGroup radioGroup;
    private String id;
    private String SELECT_VIAGEM = "SELECT TIPO_VIAGEM, DESTINO, DT_CHEGADA, DT_PARTIDA, QTD_PESSOAS, ORCAMENTO" +
            "FROM VIAGEM WHERE _ID = ?";
    private CalendarService calendarService;
    private BoaViagemDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_nova_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.DAY_OF_MONTH);

        btnChegada = (Button)findViewById(R.id.btnDataChegada);
        btnChegada.setText(dia + "/" + (mes+1) + "/" + ano);
        btnPartida = (Button)findViewById(R.id.btnDtPartida);
        btnPartida.setText(dia + "/" + (mes+1) + "/" + ano);

        edDestino = (EditText)findViewById(R.id.edDestino);
        edQtdPessoas = (EditText)findViewById(R.id.edQtdPessoas);
        edOrcamento = (EditText)findViewById(R.id.edOrcamento);
        radioGroup = (RadioGroup)findViewById(R.id.tipo_viagem);

        databaseHelper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if (id != null){
            prepararEdicao();
        }

        calendarService = criarCalendarService();
    }

    private CalendarService criarCalendarService(){
        SharedPreferences preferences = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
        String nomeConta = preferences.getString(NOME_CONTA, null);
        String tokenAcesso = preferences.getString(TOKEN_ACESSO, null);

        return new CalendarService(nomeConta, tokenAcesso);
    }

    public void prepararEdicao(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_VIAGEM,new String[]{id});
        cursor.moveToFirst();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(cursor.getInt(0) == Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.rbLazer);
        }
        else{
            radioGroup.check(R.id.rbNegocio);
        }

        edDestino.setText(cursor.getString(1));
        dataChegada = new Date(cursor.getLong(2));
        dataPartida = new Date(cursor.getLong(3));
        btnChegada.setText(dateFormat.format(dataChegada));
        btnPartida.setText(dateFormat.format(dataPartida));
        edQtdPessoas.setText(cursor.getString(4));
        edOrcamento.setText(cursor.getString(5));
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    public void salvarViagem(View v){
        Viagem viagem = new Viagem();
        viagem.setDestino(edDestino.getText().toString());
        viagem.setDataChegada(dataChegada);
        viagem.setDataPartida(dataPartida);
        viagem.setOrcamento(Double.valueOf(edOrcamento.getText().toString()));
        viagem.setQtdPessoas(Integer.valueOf(edQtdPessoas.getText().toString()));

        int tipo = radioGroup.getCheckedRadioButtonId();
        if(tipo == R.id.rbLazer){
            viagem.setTipoViagem(Constantes.VIAGEM_LAZER);
        }
        else{
            viagem.setTipoViagem(Constantes.VIAGEM_NEGOCIOS);
        }

        long resultado;
        if(id.equals(-1)){
            resultado = dao.inserir(viagem);
            new Task().execute(viagem);
        }
        else{
            resultado = dao.atualizar(viagem);
        }

        if(resultado != -1){
            Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, getString(R.string.erro_login), Toast.LENGTH_LONG).show();
        }
    }

    private class Task extends AsyncTask<Viagem, Void, Void>{

        @Override
        protected Void doInBackground(Viagem... viagems) {
            Viagem viagem = viagems[0];
            calendarService.criarEvento(viagem);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            case R.id.remover:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selecionarData(View v){
        showDialog(v.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(R.id.btnDataChegada == id){
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }
        if(R.id.btnDtPartida == id){
            return new DatePickerDialog(this, pickerListener, ano, mes, dia);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            btnChegada.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            btnPartida.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };
}
