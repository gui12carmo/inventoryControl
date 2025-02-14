package com.guilherme.inventorycontrol;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText etProductName, etProductDescription;
    ImageView ivProductImage;
    Button btnSelectImage, btnAddProduct, btnBack;
    DatabaseHelper dbHelper;
    Bitmap selectedImage = null; // imagem escolhida pelo usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        dbHelper = new DatabaseHelper(this);

        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.btnBack);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = etProductName.getText().toString();
                    String description = etProductDescription.getText().toString();
                    if (name.isEmpty() || description.isEmpty() || selectedImage == null) {
                        Toast.makeText(AddProductActivity.this, "Preencha todos os campos e selecione uma imagem", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dbHelper.addItem(name, description, selectedImage)) {
                        Toast.makeText(AddProductActivity.this, "Produto adicionado", Toast.LENGTH_SHORT).show();
                        finish(); // volta para a lista de produtos
                    } else {
                        Toast.makeText(AddProductActivity.this, "Erro ao adicionar produto", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProductImage.setImageBitmap(selectedImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
