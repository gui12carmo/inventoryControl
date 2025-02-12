package com.guilherme.inventorycontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProductDAO {

    private InventoryDbHelper dbHelper;
    private SQLiteDatabase database;

    public ProductDAO(Context context) {
        dbHelper = new InventoryDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Inserir novo produto
    public long insertProduct(String name, int quantity, double price, long categoryId) {
        ContentValues values = new ContentValues();
        values.put(InventoryDbHelper.COLUMN_PRODUCT_NAME, name);
        values.put(InventoryDbHelper.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(InventoryDbHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(InventoryDbHelper.COLUMN_PRODUCT_CATEGORY_ID, categoryId);
        return database.insert(InventoryDbHelper.TABLE_PRODUCTS, null, values);
    }

    // Obter todos os produtos
    public Cursor getAllProducts() {
        String[] columns = {
                InventoryDbHelper.COLUMN_PRODUCT_ID,
                InventoryDbHelper.COLUMN_PRODUCT_NAME,
                InventoryDbHelper.COLUMN_PRODUCT_QUANTITY,
                InventoryDbHelper.COLUMN_PRODUCT_PRICE
        };
        return database.query(InventoryDbHelper.TABLE_PRODUCTS,
                columns, null, null, null, null, InventoryDbHelper.COLUMN_PRODUCT_NAME + " ASC");
    }

    // Atualizar produto
    public int updateProduct(long id, String name, int quantity, double price, long categoryId) {
        ContentValues values = new ContentValues();
        values.put(InventoryDbHelper.COLUMN_PRODUCT_NAME, name);
        values.put(InventoryDbHelper.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(InventoryDbHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(InventoryDbHelper.COLUMN_PRODUCT_CATEGORY_ID, categoryId);
        String whereClause = InventoryDbHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        return database.update(InventoryDbHelper.TABLE_PRODUCTS, values, whereClause, whereArgs);
    }

    // Excluir produto
    public int deleteProduct(long id) {
        String whereClause = InventoryDbHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        return database.delete(InventoryDbHelper.TABLE_PRODUCTS, whereClause, whereArgs);
    }
}

