package com.example.leonardooliveira.boaviagem.provider;

import android.net.Uri;

/**
 * Created by Leonardo Oliveira on 03/07/2016.
 */
public final class BoaViagemContract {
    public static final String AUTHORITY = "com.example.leonardooliveira.boaviagem.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final String VIAGEM_PATH = "viagem";
    public static final String GASTO_PATH = "gasto";
    public static final class Viagem{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, VIAGEM_PATH);
        public static String _ID = "_id";
        public static String DESTINO = "destino";
        public static String DT_CHEGADA = "dt_chegada";
        public static String DT_PARTIDA = "dt_partida";
        public static String ORCAMENTO = "orcamento";
        public static String QTD_PESSOAS = "qtd_pessoas";
        public static String CONTENT_TYPE = "vnd.android.cursor.dir/" +
                "vnd.com.example.leonardooliveira.boaviagem.provider/viagem";
        public static String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" +
                "vnd.com.example.leonardooliveira.boaviagem.provider/viagem";
    }
    public static final class Gasto{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, GASTO_PATH);
        public static String _ID = "_id";
        public static String VIAGEM_ID = "viagem_id";
        public static String CATEGORIA = "categoria";
        public static String DATA = "data";
        public static String DESCRICAO = "descricao";
        public static String LOCAL = "local";
        public static String CONTENT_TYPE = "vnd.android.cursor.dir/" +
                "vnd.com.example.leonardooliveira.boaviagem.provider/gasto";
        public static String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" +
                "vnd.com.example.leonardooliveira.boaviagem.provider/gasto";
    }
}
