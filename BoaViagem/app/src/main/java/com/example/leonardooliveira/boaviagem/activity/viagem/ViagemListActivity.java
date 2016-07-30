package com.example.leonardooliveira.boaviagem.activity.viagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.example.leonardooliveira.boaviagem.R;
import com.example.leonardooliveira.boaviagem.activity.gasto.GastoActivity;
import com.example.leonardooliveira.boaviagem.activity.gasto.GastoListActivity;
import com.example.leonardooliveira.boaviagem.dao.DatabaseHelper;
import com.example.leonardooliveira.boaviagem.model.Constantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Oliveira on 30/06/2016.
 */
public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener,
        DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {

    private List<Map<String,Object>> viagens;
    private AlertDialog alertDialog;
    private AlertDialog dialogConfirm;
    private int viagemSelecionada;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;
    private String SELECT_VIAGEM = "SELECT _ID, TIPO_VIAGEM, DESTINO, DT_CHEGADA, DT_PARTIDA, ORCAMENTO FROM VIAGEM";
    private String SELECT_TOTAL_GASTO = "SELECT SUM(VALOR) FROM GASTO WHERE VIAGEM_ID = ?";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.viagemSelecionada = position;
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"imagem","destino","data","total","barraProgresso"};
        int[] para = {R.id.ivTipoViagem,R.id.txtDestiny,R.id.txtDate,R.id.txtValue,R.id.barraProgresso};

        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(), R.layout.activity_lista_viagem, de, para);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        adapter.setViewBinder(this);
        this.alertDialog = criaAlertDialog();
        this.dialogConfirm = criaDialogConfirmacao();

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String valor = preferences.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);
    }

    private List<Map<String,Object>> listarViagens(){
        viagens = new ArrayList<Map<String,Object>>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_VIAGEM, null);
        cursor.moveToFirst();

        for (int i=0; i < cursor.getCount(); i++){
            Map<String,Object> item = new HashMap<>();
            String id = cursor.getString(0);
            int tipoViagem = cursor.getInt(1);
            String destino = cursor.getString(2);
            long dataChegada = cursor.getLong(3);
            long dataPartida = cursor.getLong(4);
            double orcamento = cursor.getDouble(5);

            item.put("id", id);
            if (tipoViagem == Constantes.VIAGEM_LAZER){
                item.put("imagem", R.drawable.lazer);
            }
            else{
                item.put("imagem", R.drawable.negocios);
            }
            item.put("destino", destino);
            Date chegadaDate = new Date(dataChegada);
            Date partidaDate = new Date(dataPartida);
            String periodo = dateFormat.format(chegadaDate) + " a " + dateFormat.format(partidaDate);
            item.put("data", periodo);
            double totalGasto = calcularTotalGasto(db, id);
            item.put("total","Gasto total R$ " + totalGasto);
            double alerta = orcamento * valorLimite / 100;
            Double[] valores = new Double[]{orcamento, alerta, totalGasto};
            item.put("barraProgresso", valores);

            viagens.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return viagens;
    }

    private double calcularTotalGasto(SQLiteDatabase db, String id){
        Cursor cursor = db.rawQuery(SELECT_TOTAL_GASTO, new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();

        return total;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        Intent intent;
        String id = (String)viagens.get(viagemSelecionada).get("id");

        switch (which){
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                String destino = viagens.get(viagemSelecionada).get("destino").toString();
                intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, GastoListActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 3:
                dialogConfirm.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(viagemSelecionada);
                removerViagem(id);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirm.dismiss();
                break;
        }
    }

    public void removerViagem(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String where[] = new String[]{id};
        db.delete("gasto", "viagem_id=?", where);
        db.delete("viagem", "_id=?", where);
    }

    private AlertDialog criaAlertDialog(){
        final CharSequence[] items = {
                getString(R.string.edit),
                getString(R.string.novo_gasto),
                getString(R.string.gasto_realizado),
                getString(R.string.remover)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcao);
        builder.setItems(items, this);

        return builder.create();
    }

    private AlertDialog criaDialogConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirma_excluir);
        builder.setPositiveButton(getString(R.string.op_sim), this);
        builder.setNegativeButton(getString(R.string.op_nao), this);

        return builder.create();
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation){
        if(view.getId() == R.id.barraProgresso){
            Double valores[] = (Double[])data;
            ProgressBar progressBar = (ProgressBar)view;
            progressBar.setMax(valores[0].intValue());
            progressBar.setSecondaryProgress(valores[1].intValue());
            progressBar.setProgress(valores[2].intValue());
            return true;
        }

        return false;
    }
}
