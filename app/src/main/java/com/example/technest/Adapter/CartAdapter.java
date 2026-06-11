package com.example.technest.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.technest.Model.Product;
import com.example.technest.R;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartList;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Product product, int position);
    }

    public CartAdapter(List<Product> cartList, OnDeleteClickListener deleteListener) {
        this.cartList = cartList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartList.get(position);

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText("$" + product.getPrice());
        holder.cbSelectItem.setOnCheckedChangeListener(null);
        holder.cbSelectItem.setChecked(product.isSelected());
        holder.cbSelectItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setSelected(isChecked);
        });

        Glide.with(holder.itemView.getContext())
                .load(product.getThumbnail())
                .placeholder(R.drawable.ic_technest_logo)
                .centerCrop()
                .into(holder.imgProduct);

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(product, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    public void updateData(List<Product> newList) {
        this.cartList = newList;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvTitle, tvPrice;
        ImageButton btnDelete;
        CheckBox cbSelectItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_cart_product);
            tvTitle = itemView.findViewById(R.id.tv_cart_title);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            btnDelete = itemView.findViewById(R.id.btn_delete_cart);
            cbSelectItem = itemView.findViewById(R.id.cb_select_item);
        }
    }
}