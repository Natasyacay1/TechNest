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

    private static final String DATABASE_NAME = "TechNest.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_CART = "cart";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PRICE = "price";
    private static final String KEY_THUMBNAIL = "thumbnail";

    // Tabel Cache Produk (untuk offline)
    private static final String TABLE_CACHE = "product_cache";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_DESC = "description";
    private static final String KEY_RATING = "rating";
    private static final String KEY_CATEGORY = "category";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel cart
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_CART_TABLE);

        // Buat tabel cache produk
        String CREATE_CACHE_TABLE = "CREATE TABLE " + TABLE_CACHE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_THUMBNAIL + " TEXT,"
                + KEY_BRAND + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_RATING + " REAL,"
                + KEY_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_CACHE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE);
        onCreate(db);
    }

    //CART METHODS

    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, product.getId());
        values.put(KEY_TITLE, product.getTitle());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_THUMBNAIL, product.getThumbnail());
        db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<Product> getAllCartItems() {
        List<Product> cartList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
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

    public void deleteCartItem(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    //CACHE METHODS (Offline)

    public void saveProductsToCache(List<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CACHE, null, null); // Hapus cache lama
        for (Product product : products) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, product.getId());
            values.put(KEY_TITLE, product.getTitle());
            values.put(KEY_PRICE, product.getPrice());
            values.put(KEY_THUMBNAIL, product.getThumbnail());
            values.put(KEY_BRAND, product.getBrand());
            values.put(KEY_DESC, product.getDescription());
            values.put(KEY_RATING, product.getRating());
            values.put(KEY_CATEGORY, product.getCategory());
            db.insertWithOnConflict(TABLE_CACHE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }

    public List<Product> getCachedProducts() {
        List<Product> cacheList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CACHE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_THUMBNAIL)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
                product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_RATING)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                cacheList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cacheList;
    }

    public boolean isCacheEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CACHE, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count == 0;
    }
}