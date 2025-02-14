package com.guilherme.inventorycontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "estoque.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela de Usuários
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COL_USUARIO_ID = "id";
    public static final String COL_USUARIO_NOME = "username";
    public static final String COL_USUARIO_SENHA = "password";

    // Tabela de Itens
    public static final String TABLE_ITENS = "itens";
    public static final String COL_ITEM_ID = "id";
    public static final String COL_ITEM_NOME = "nome";
    public static final String COL_ITEM_DESCRICAO = "descricao";
    public static final String COL_ITEM_IMAGEM = "imagem"; // Armazenado como BLOB

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_USUARIOS_TABLE = "CREATE TABLE " + TABLE_USUARIOS + "("
                    + COL_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_USUARIO_NOME + " TEXT UNIQUE,"
                    + COL_USUARIO_SENHA + " TEXT"
                    + ")";
            db.execSQL(CREATE_USUARIOS_TABLE);

            String CREATE_ITENS_TABLE = "CREATE TABLE " + TABLE_ITENS + "("
                    + COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_ITEM_NOME + " TEXT,"
                    + COL_ITEM_DESCRICAO + " TEXT,"
                    + COL_ITEM_IMAGEM + " BLOB"
                    + ")";
            db.execSQL(CREATE_ITENS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITENS);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cadastro de usuário
    public boolean registerUser(String username, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_USUARIO_NOME, username);
            values.put(COL_USUARIO_SENHA, password);
            long result = db.insert(TABLE_USUARIOS, null, values);
            db.close();
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verifica login do usuário
    public boolean loginUser(String username, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE " +
                            COL_USUARIO_NOME + "=? AND " + COL_USUARIO_SENHA + "=?",
                    new String[]{username, password});
            boolean result = (cursor.getCount() > 0);
            cursor.close();
            db.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Adiciona um novo item ao estoque
    public boolean addItem(String nome, String descricao, Bitmap imagem) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_ITEM_NOME, nome);
            values.put(COL_ITEM_DESCRICAO, descricao);
            values.put(COL_ITEM_IMAGEM, getBytesFromBitmap(imagem));
            long result = db.insert(TABLE_ITENS, null, values);
            db.close();
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove item pelo ID
    public boolean removeItem(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.delete(TABLE_ITENS, COL_ITEM_ID + "=?", new String[]{String.valueOf(id)});
            db.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Atualiza item pelo ID
    public boolean updateItem(int id, String nome, String descricao, Bitmap imagem) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_ITEM_NOME, nome);
            values.put(COL_ITEM_DESCRICAO, descricao);
            values.put(COL_ITEM_IMAGEM, getBytesFromBitmap(imagem));
            int result = db.update(TABLE_ITENS, values, COL_ITEM_ID + "=?", new String[]{String.valueOf(id)});
            db.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Converte Bitmap para byte[]
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Converte byte[] para Bitmap
    public static Bitmap getBitmapFromBytes(byte[] image) {
        try {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retorna todos os itens cadastrados
    public Cursor getAllItems() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT * FROM " + TABLE_ITENS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

