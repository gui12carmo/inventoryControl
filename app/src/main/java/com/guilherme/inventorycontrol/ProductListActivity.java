package com.guilherme.inventorycontrol;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductListActivity extends AppCompatActivity {

    private ProductDAO productDAO;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    // Mapeamento das colunas para as views do layout de item
    final String[] fromColumns = {
            InventoryDbHelper.COLUMN_PRODUCT_NAME,
            InventoryDbHelper.COLUMN_PRODUCT_QUANTITY,
            InventoryDbHelper.COLUMN_PRODUCT_PRICE
    };
    final int[] toViews = { R.id.tvProductName, R.id.tvQuantity, R.id.tvPrice };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        listView = findViewById(R.id.listViewProducts);
        productDAO = new ProductDAO(this);
        productDAO.open();

        loadProducts();

        // Ao clicar em um item, abrir a tela de edição
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Abre EditProductActivity passando o ID do produto
                Intent intent = new Intent(ProductListActivity.this, EditProductActivity.class);
                intent.putExtra("PRODUCT_ID", id);
                startActivity(intent);
            }
        });

        // Botão para adicionar novo produto (pode ser um FloatingActionButton)
        findViewById(R.id.fabAddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(ProductListActivity.this, AddProductActivity.class);
                startActivity(addIntent);
            }
        });
    }

    private void loadProducts() {
        Cursor cursor = productDAO.getAllProducts();
        if (cursor != null && cursor.getCount() > 0) {
            adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.list_item_product,
                    cursor,
                    fromColumns,
                    toViews,
                    0);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nenhum produto cadastrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reabre a conexão e atualiza a lista
        productDAO.open();
        loadProducts();
    }

    @Override
    protected void onPause() {
        productDAO.close();
        super.onPause();
    }
}

