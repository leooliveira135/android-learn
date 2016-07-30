package com.example.leonardooliveira.boaviagem.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.leonardooliveira.boaviagem.dao.DatabaseHelper;

import static com.example.leonardooliveira.boaviagem.provider.BoaViagemContract.AUTHORITY;
import static com.example.leonardooliveira.boaviagem.provider.BoaViagemContract.GASTO_PATH;
import static com.example.leonardooliveira.boaviagem.provider.BoaViagemContract.Gasto;
import static com.example.leonardooliveira.boaviagem.provider.BoaViagemContract.VIAGEM_PATH;
import static com.example.leonardooliveira.boaviagem.provider.BoaViagemContract.Viagem;

/**
 * Created by Leonardo Oliveira on 03/07/2016.
 */
public class BoaViagemProvider extends ContentProvider{

    private static final int VIAGENS = 1;
    private static final int VIAGEM_ID = 2;
    private static final int GASTOS = 3;
    private static final int GASTO_ID = 4;
    private static final int GASTOS_VIAGEM_ID = 5;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        URI_MATCHER.addURI(AUTHORITY, VIAGEM_PATH, VIAGENS);
        URI_MATCHER.addURI(AUTHORITY, VIAGEM_PATH + "/#", VIAGEM_ID);
        URI_MATCHER.addURI(AUTHORITY, GASTO_PATH, GASTOS);
        URI_MATCHER.addURI(AUTHORITY, GASTO_PATH + "/#", GASTO_ID);
        URI_MATCHER.addURI(AUTHORITY, GASTO_PATH + "/" + VIAGEM_PATH + "/#", GASTOS_VIAGEM_ID);
    }
    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = helper.getReadableDatabase();

        switch (URI_MATCHER.match(uri)){
            case VIAGENS:
                return database.query(VIAGEM_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            case VIAGEM_ID:
                selection = Viagem._ID = " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.query(VIAGEM_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            case GASTOS:
                return database.query(GASTO_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            case GASTO_ID:
                selection = Gasto._ID = " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.query(GASTO_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            case GASTOS_VIAGEM_ID:
                selection = Gasto.VIAGEM_ID = " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.query(GASTO_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(URI_MATCHER.match(uri)){
            case VIAGENS:
                return Viagem.CONTENT_TYPE;
            case VIAGEM_ID:
                return Viagem.CONTENT_ITEM_TYPE;
            case GASTOS:
            case GASTOS_VIAGEM_ID:
                return Gasto.CONTENT_TYPE;
            case GASTO_ID:
                return Gasto.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = helper.getWritableDatabase();
        long id;

        switch(URI_MATCHER.match(uri)){
            case VIAGENS:
                id = database.insert(VIAGEM_PATH, null, values);
                return Uri.withAppendedPath(Viagem.CONTENT_URI, String.valueOf(id));

            case GASTOS:
                id = database.insert(GASTO_PATH, null, values);
                return Uri.withAppendedPath(Gasto.CONTENT_URI, String.valueOf(id));

            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();

        switch(URI_MATCHER.match(uri)){
            case VIAGEM_ID:
                selection = Viagem._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.delete(VIAGEM_PATH, selection, selectionArgs);

            case GASTO_ID:
                selection = Gasto._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.delete(GASTO_PATH, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();

        switch(URI_MATCHER.match(uri)){
            case VIAGEM_ID:
                selection = Viagem._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.update(VIAGEM_PATH, values, selection, selectionArgs);

            case GASTO_ID:
                selection = Gasto._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.update(GASTO_PATH, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }
}
