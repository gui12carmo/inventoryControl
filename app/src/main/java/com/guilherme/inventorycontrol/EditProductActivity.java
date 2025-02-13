package com.guilherme.inventorycontrol;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etPrice;
    private Button btnUpdate, btnDelete;
    private ProductDAO productDAO;
    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        etName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        productDAO = new ProductDAO(this);
        productDAO.open();

        // Recebe o ID do produto a partir da intent
        Intent intent = getIntent();
        productId = intent.getLongExtra("PRODUCT_ID", -1);
        if(productId == -1) {
            Toast.makeText(this, "Produto não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Carregar os dados do produto
        loadProductDetails();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String quantityStr = etQuantity.getText().toString().trim();
                String priceStr = etPrice.getText().toString().trim();

                if(name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()){
                    Toast.makeText(EditProductActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);
                long categoryId = 1; // Para simplificação

                int rowsAffected = productDAO.updateProduct(productId, name, quantity, price, categoryId);
                if(rowsAffected > 0) {
                    Toast.makeText(EditProductActivity.this, "Produto atualizado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProductActivity.this, "Erro ao atualizar produto.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rowsDeleted = productDAO.deleteProduct(productId);
                if(rowsDeleted > 0) {
                    Toast.makeText(EditProductActivity.this, "Produto removido!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProductActivity.this, "Erro ao remover produto.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProductDetails() {
        // Buscando o produto pelo ID
        String query = "SELECT * FROM " + InventoryDbHelper.TABLE_PRODUCTS +
                " WHERE " + InventoryDbHelper.COLUMN_PRODUCT_ID + " = " + productId;
        Cursor cursor = productDAO.getAllProducts(); // Para simplificar, suponha que este cursor contenha os dados (ou crie um método getProductById)
        // Em um projeto real, implemente um método específico para obter o produto por ID.
        if(cursor != null && cursor.moveToFirst()){
            etName.setText(cursor.getString(cursor.getColumnIndex(InventoryDbHelper.COLUMN_PRODUCT_NAME)));
            etQuantity.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(InventoryDbHelper.COLUMN_PRODUCT_QUANTITY))));
            etPrice.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex(InventoryDbHelper.COLUMN_PRODUCT_PRICE))));
        }
        if(cursor != null)
            cursor.close();
    }

    @Override
    protected void onDestroy() {
        productDAO.close();
        super.onDestroy();
    }
}
