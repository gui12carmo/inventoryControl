package com.guilherme.inventorycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    ListView listViewProducts;
    Button btnAddProduct, btnRefresh;
    DatabaseHelper dbHelper;
    ArrayList<Product> productList;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        dbHelper = new DatabaseHelper(this);

        listViewProducts = findViewById(R.id.listViewProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnRefresh = findViewById(R.id.btnRefresh);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        listViewProducts.setAdapter(adapter);

        loadProducts();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(ProductListActivity.this, AddProductActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loadProducts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Clique no item para exibir uma mensagem com o nome do produto (pode ser expandido para editar/detalhar)
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(ProductListActivity.this, "Produto: " + productList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProducts() {
        productList.clear();
        try {
            Cursor cursor = dbHelper.getAllItems();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_NOME));
                    String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_DESCRICAO));
                    byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_IMAGEM));
                    Bitmap image = DatabaseHelper.getBitmapFromBytes(imageBytes);
                    productList.add(new Product(id, name, description, image));
                } while (cursor.moveToNext());
            }
            if(cursor != null){
                cursor.close();
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar produtos", Toast.LENGTH_SHORT).show();
        }
    }
}
