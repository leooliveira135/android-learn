package com.example.leonardooliveira.boaviagem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.leonardooliveira.boaviagem.model.Viagem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leonardo Oliveira on 01/07/2016.
 */
public class BoaViagemDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public BoaViagemDAO(Context context){
        helper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDb(){
        if (db == null){
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close(){
        helper.close();
    }

    public List<Viagem> listarViagens(){
        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA,
                                        DatabaseHelper.Viagem.COLUNAS,
                                        null,null,null,null,null);
        List<Viagem> viagems = new ArrayList<>();

        while (cursor.moveToNext()){
            Viagem viagem = criarViagem(cursor);
            viagems.add(viagem);
        }
        cursor.close();

        return viagems;
    }

    private Viagem criarViagem(Cursor cursor){
        Viagem viagem = new Viagem(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Viagem._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Viagem.DESTINO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Viagem.TIPO_VIAGEM)),
                new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Viagem.DT_CHEGADA))),
                new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Viagem.DT_PARTIDA))),
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Viagem.ORCAMENTO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Viagem.QTD_PESSOAS))
        );

        return viagem;
    }

    public long inserir(Viagem viagem){
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.Viagem.DESTINO, viagem.getDestino());
        values.put(DatabaseHelper.Viagem.TIPO_VIAGEM, viagem.getTipoViagem());
        values.put(DatabaseHelper.Viagem.DT_CHEGADA, String.valueOf(viagem.getDataChegada()));
        values.put(DatabaseHelper.Viagem.DT_PARTIDA, String.valueOf(viagem.getDataPartida()));
        values.put(DatabaseHelper.Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(DatabaseHelper.Viagem.QTD_PESSOAS, viagem.getQtdPessoas());

        return getDb().insert(DatabaseHelper.Viagem.TABELA, null, values);
    }

    public long atualizar(Viagem viagem){
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.Viagem.DESTINO, viagem.getDestino());
        values.put(DatabaseHelper.Viagem.TIPO_VIAGEM, viagem.getTipoViagem());
        values.put(DatabaseHelper.Viagem.DT_CHEGADA, String.valueOf(viagem.getDataChegada()));
        values.put(DatabaseHelper.Viagem.DT_PARTIDA, String.valueOf(viagem.getDataPartida()));
        values.put(DatabaseHelper.Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(DatabaseHelper.Viagem.QTD_PESSOAS, viagem.getQtdPessoas());

        return getDb().update(DatabaseHelper.Viagem.TABELA, values,
                DatabaseHelper.Viagem._ID + " = ?", new String[]{viagem.getId().toString()});
    }
}
