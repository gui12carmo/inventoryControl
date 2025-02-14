package com.guilherme.inventorycontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Product> products;
    private LayoutInflater inflater;

    public ProductAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return products.size();
    }
    @Override
    public Object getItem(int position) {
        return products.get(position);
    }
    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.product_item, parent, false);
            holder = new ViewHolder();
            holder.ivProduct = convertView.findViewById(R.id.ivProduct);
            holder.tvName = convertView.findViewById(R.id.tvProductName);
            holder.tvDescription = convertView.findViewById(R.id.tvProductDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvDescription.setText(product.getDescription());
        Bitmap image = product.getImage();
        if(image != null){
            holder.ivProduct.setImageBitmap(image);
        }
        return convertView;
    }
    private static class ViewHolder{
        ImageView ivProduct;
        TextView tvName, tvDescription;
    }
}

