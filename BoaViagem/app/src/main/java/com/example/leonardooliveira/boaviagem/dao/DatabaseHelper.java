package com.example.leonardooliveira.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leonardo Oliveira on 30/06/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "Boa Viagem";
    private static int VERSAO = 1;
    private static String TB_VIAGEM = "CREATE TABLE VIAGEM" +
            "(_ID INTEGER PRIMARY KEY," +
            "DESTINO TEXT," +
            "TIPO_VIAGEM INTEGER," +
            "DT_CHEGADA DATE," +
            "DT_SAIDA DATE," +
            "ORCAMENTO DOUBLE," +
            "QTD_PESSOAS INTEGER);";
    private static String TB_GASTO = "CREATE TABLE GASTO" +
            "(_ID INTEGER PRIMARY KEY," +
            "CATEGORIA TEXT," +
            "DATA DATE," +
            "VALOR DOUBLE," +
            "DESCRICAO TEXT," +
            "LOCAL TEXT," +
            "VIAGEM_ID INTEGER," +
            "FOREIGN KEY(VIAGEM_ID) REFERENCES VIAGEM(_ID);";
    private static String UPDATE_GASTO = "ALTER TABLE GASTO ADD COLUMN PESSOA TEXT";

    public static class Viagem{
        public static final String TABELA = "viagem";
        public static final String _ID = "_id";
        public static final String DESTINO = "destino";
        public static final String DT_CHEGADA = "dt_chegada";
        public static final String DT_PARTIDA = "dt_partida";
        public static final String ORCAMENTO = "orcamento";
        public static final String QTD_PESSOAS = "qtd_pessoas";
        public static final String TIPO_VIAGEM = "tipo_viagem";
        public static final String[] COLUNAS = new String[]{
                _ID, DESTINO, DT_CHEGADA, DT_PARTIDA, TIPO_VIAGEM, ORCAMENTO, QTD_PESSOAS
        };
    }

    public static class Gasto{
        public static final String TABELA = "gasto";
        public static final String _ID = "_id";
        public static final String VIAGEM_ID = "viagem_id";
        public static final String CATEGORIA = "categoria";
        public static final String DATA = "data";
        public static final String DESCRICAO = "descricao";
        public static final String VALOR = "valor";
        public static final String LOCAL = "local";
        public static final String[] COLUNAS = new String[]{
                _ID, VIAGEM_ID, CATEGORIA, DATA, DESCRICAO, VALOR, LOCAL
        };
    }

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_VIAGEM);
        db.execSQL(TB_GASTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UPDATE_GASTO);
    }


}
