package com.guilherme.inventorycontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etPrice;
    private Button btnSave;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSave);

        productDAO = new ProductDAO(this);
        productDAO.open();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String quantityStr = etQuantity.getText().toString().trim();
                String priceStr = etPrice.getText().toString().trim();

                if(name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);

                // Para simplificação, vamos definir uma categoria fixa (ex: 1)
                long categoryId = 1;

                long id = productDAO.insertProduct(name, quantity, price, categoryId);
                if(id != -1) {
                    Toast.makeText(AddProductActivity.this, "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, "Erro ao adicionar produto.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        productDAO.close();
        super.onDestroy();
    }
}
