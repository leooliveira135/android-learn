package com.example.leonardooliveira.boaviagem.activity.gasto;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardooliveira.boaviagem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo Oliveira on 30/06/2016.
 */
public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<Map<String,Object>> gastos;
    private String dataAnterior = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"data","descricao","valor","categoria"};
        int[] para = {R.id.date, R.id.txtDesc, R.id.valor, R.id.categoria};

        SimpleAdapter adapter = new SimpleAdapter(this, listarGastos(), R.layout.activity_lista_gasto, de, para);
        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,Object> map = gastos.get(position);
        String descricao = (String)map.get("descricao");
        String msg = "Gasto selecionado: " + descricao;
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private List<Map<String,Object>> listarGastos(){
        gastos = new ArrayList<>();

        Map<String,Object> item = new HashMap<>();
        item.put("data","04/02/2012");
        item.put("descricao","Diária Hotel");
        item.put("valor","R$ 260,00");
        item.put("categoria", R.color.hospedagem);

        gastos.add(item);
        return gastos;
    }

    private class GastoViewBinder implements SimpleAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if(view.getId() == R.id.date){
                if(!dataAnterior.equals(data)){
                    TextView textView = (TextView)view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                }
                else{
                    view.setVisibility(View.GONE);
                }
                return true;
            }

            if(view.getId() == R.id.categoria){
                Integer id = (Integer)data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.remove){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            return true;
        }

        return super.onContextItemSelected(item);
    }
}
