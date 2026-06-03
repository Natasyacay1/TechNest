package com.example.technest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.technest.Model.Product;
import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    // Nama & Versi Database
    private static final String DATABASE_NAME = "TechNest.db";
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel & Kolom
    private static final String TABLE_CART = "cart";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PRICE = "price";
    private static final String KEY_THUMBNAIL = "thumbnail";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tempat membuat tabel pertama kali saat aplikasi diinstal
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // 1. FUNGSI TAMBAH BARANG KE KERANJANG (Add to Cart)
    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Memasukkan data objek Product ke dalam kolom database
        values.put(KEY_ID, product.getId());
        values.put(KEY_TITLE, product.getTitle());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_THUMBNAIL, product.getThumbnail());

        // Insert data ke tabel, jika ID sudah ada maka akan ditimpa (menghindari duplikasi)
        db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // 2. FUNGSI AMBIL SEMUA BARANG DI KERANJANG (Tampil di CartFragment)
    public List<Product> getAllCartItems() {
        List<Product> cartList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping semua baris database dan mengubahnya kembali menjadi Objek Product
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_THUMBNAIL)));

                cartList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cartList;
    }

    // 3. FUNGSI HAPUS SATU BARANG (Jika user membatalkan belanjaan)
    public void deleteCartItem(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // 4. FUNGSI BERSIHKAN KERANJANG (Saat user sukses Checkout)
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }
}