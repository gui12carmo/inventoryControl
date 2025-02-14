package com.guilherme.inventorycontrol;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InventoryActivity extends AppCompatActivity {

    EditText etItemName, etItemDescription, etItemId;
    Button btnAdd, btnUpdate, btnDelete, btnBack, btnLoad;
    ImageView ivItemImage;
    DatabaseHelper dbHelper;
    // Para este exemplo, usamos uma imagem padrão do drawable
    Bitmap defaultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        dbHelper = new DatabaseHelper(this);

        etItemId = findViewById(R.id.etItemId);
        etItemName = findViewById(R.id.etItemName);
        etItemDescription = findViewById(R.id.etItemDescription);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        btnLoad = findViewById(R.id.btnLoad);
        ivItemImage = findViewById(R.id.ivItemImage);

        // Carrega imagem padrão (certifique-se de ter o arquivo default_image.png em res/drawable)
        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_image);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = etItemName.getText().toString();
                    String description = etItemDescription.getText().toString();
                    if (dbHelper.addItem(name, description, defaultImage)) {
                        Toast.makeText(InventoryActivity.this, "Item adicionado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InventoryActivity.this, "Erro ao adicionar item", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int id = Integer.parseInt(etItemId.getText().toString());
                    String name = etItemName.getText().toString();
                    String description = etItemDescription.getText().toString();
                    if (dbHelper.updateItem(id, name, description, defaultImage)) {
                        Toast.makeText(InventoryActivity.this, "Item atualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InventoryActivity.this, "Erro ao atualizar item", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int id = Integer.parseInt(etItemId.getText().toString());
                    if (dbHelper.removeItem(id)) {
                        Toast.makeText(InventoryActivity.this, "Item removido", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InventoryActivity.this, "Erro ao remover item", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int id = Integer.parseInt(etItemId.getText().toString());
                    Cursor cursor = dbHelper.getAllItems();
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_ID)) == id) {
                                etItemName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_NOME)));
                                etItemDescription.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_DESCRICAO)));
                                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_IMAGEM));
                                Bitmap imageBitmap = DatabaseHelper.getBitmapFromBytes(imageBytes);
                                ivItemImage.setImageBitmap(imageBitmap);
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    if(cursor != null){
                        cursor.close();
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
                    finish(); // Volta para a tela anterior
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

