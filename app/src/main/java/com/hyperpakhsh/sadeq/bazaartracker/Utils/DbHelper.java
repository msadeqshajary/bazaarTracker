package com.hyperpakhsh.sadeq.bazaartracker.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyperpakhsh.sadeq.bazaartracker.Customers.CustomerListItem;
import com.hyperpakhsh.sadeq.bazaartracker.Order.FactorItem;
import com.hyperpakhsh.sadeq.bazaartracker.Order.OrderItem;
import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static String TABLE_TEMPORDER = "temp_order";
    private static String TEMPORDER_ID = "id";
    private static String TEMPORDER_SOFTWAREID = "software_id";
    private static String TEMPORDER_SQLITEPRODUCTID = "sqliteProductId";
    private static String TEMPORDER_NAME = "name";
    private static String TEMPORDER_QUANTITY = "quantity";
    private static String TEMPORDER_PRICE = "price";
    private static String TEMPORDER_LOCALID = "localId";

    public static String TABLE_CUSTOMERS = "customers";
    private static String CUSTOMERS_ID = "id";
    private static String CUSTOMERS_SOFTWAREID = "softwareId";
    private static String CUSTOMERS_NAME = "name";
    private static String CUSTOMERS_PHONE = "phone";
    private static String CUSTOMERS_WINDOWS = "windows";

    public static String TABLE_PRODUCTS = "products";
    private static String PRODUCTS_ID = "id";
    private static String PRODUCTS_WINDOWS = "windows";
    private static String PRODUCTS_SOFTWAREID = "softwareId";
    private static String PRODUCTS_NAME = "name";
    private static String PRODUCTS_LOCALID = "localId";
    private static String PRODUCTS_PRICE = "price";

    public static String TABLE_SALEARTICLE = "SaleFactorArticle";
    private static String SALEARTICLE_ID = "id";
    private static String SALEARTICLE_ROW = "row";
    private static String SALEARTICLE_PRODUCTID = "productId";
    private static String SALEARTICLE_QUANTITY = "quantity";
    private static String SALEARTICLE_PRICE = "price";
    private static String SALEARTICLE_TOTAL = "total";
    private static String SALEARTICLE_FACTORID = "factorId";
    private static String SALEARTICLE_SQLITE_PRODUCTID = "sqlite_productId";
    private static String SALEARTICLE_PRODUCTLOCALID = "productLocalId";
    private static String SALEARTICLE_DES = "desc";
    private static String SALEARTICLE_MYSQLFACTORID = "mysqlId";

    public static String TABLE_FACTORS = "factors";
    private static String FACTORS_ID = "id";
    private static String FACTORS_SALEMALIID = "saleMaliId";
    private static String FACTORS_CUSTOMERID = "customerId";
    private static String FACTORS_TOTAL = "total";
    private static String FACTORS_DISCOUNT = "discount";
    private static String FACTORS_SHIPPING = "shipping";
    private static String FACTORS_DATE = "date";
    private static String FACTORS_TIME = "time";
    private static String FACTORS_DESC = "desc";
    private static String FACTORS_SENT = "sent";
    private static String FACTORS_USERID = "userId";
    private static String FACTORS_MYSQLFACTORID = "mysqlId";



    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, "bazzarDb", null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTempOrderTable = "CREATE TABLE "+TABLE_TEMPORDER+"("+ TEMPORDER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+TEMPORDER_SOFTWAREID+" INTEGER,"+
                TEMPORDER_SQLITEPRODUCTID+" INTEGER,"+TEMPORDER_NAME+","+TEMPORDER_LOCALID+" INTEGER,"+TEMPORDER_QUANTITY+" INTEGER,"+TEMPORDER_PRICE+" INTEGER);";
        String createCustomersTable = "CREATE TABLE "+TABLE_CUSTOMERS+"("+CUSTOMERS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CUSTOMERS_SOFTWAREID
                +" INTEGER,"+CUSTOMERS_NAME+","+CUSTOMERS_PHONE+","+CUSTOMERS_WINDOWS+" INTEGER);";
        String createProductsTable = "CREATE TABLE "+TABLE_PRODUCTS+"("+PRODUCTS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+PRODUCTS_SOFTWAREID+" INTEGER,"+
                PRODUCTS_WINDOWS+" INTEGER,"+PRODUCTS_NAME+","+PRODUCTS_LOCALID+" INTEGER,"+PRODUCTS_PRICE+" INTEGER);";
        String createArticleTable = "CREATE TABLE "+TABLE_SALEARTICLE+"("+SALEARTICLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+SALEARTICLE_ROW+" INTEGER,"+
                SALEARTICLE_FACTORID+" INTEGER,"+SALEARTICLE_PRODUCTID+" INTEGER,"+SALEARTICLE_PRODUCTLOCALID+" INTEGER,"+SALEARTICLE_PRICE+" INTEGER,"+
                SALEARTICLE_QUANTITY+" INTEGER,"+SALEARTICLE_TOTAL+" INTEGER,"+SALEARTICLE_MYSQLFACTORID+" INTEGER,"+SALEARTICLE_SQLITE_PRODUCTID+" INTEGER,"+SALEARTICLE_DES+")";
        String createFactorsTable = "CREATE TABLE "+TABLE_FACTORS+"("+FACTORS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+FACTORS_CUSTOMERID+" INTEGER,"+
                FACTORS_SALEMALIID+" INTEGER,"+FACTORS_USERID+" INTEGER,"+FACTORS_DATE+" DATE,"+FACTORS_TIME+" TIME,"+FACTORS_TOTAL+" INTEGER,"+FACTORS_SHIPPING+" INTEGER,"+
                FACTORS_DISCOUNT+" INTEGER,"+FACTORS_SENT+" INTEGER,"+FACTORS_MYSQLFACTORID+" INTEGER,"+FACTORS_DESC+")";
        db.execSQL(createTempOrderTable);
        db.execSQL(createCustomersTable);
        db.execSQL(createProductsTable);
        db.execSQL(createArticleTable);
        db.execSQL(createFactorsTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void updateTempOrderItem(ProductItem item){
        Cursor c = db.rawQuery("SELECT COUNT(*) as count FROM "+TABLE_TEMPORDER+" WHERE "+TEMPORDER_SOFTWAREID+"="+item.getSoftwareID(),null);
        int price = Integer.parseInt(item.getPrice());
        int quantity = item.getQuantity();
        c.moveToFirst();

        int count = c.getInt(c.getColumnIndex("count"));
        if(count>0){
            String insertCommand = "UPDATE "+TABLE_TEMPORDER+" SET "+TEMPORDER_PRICE+"="+price+","+TEMPORDER_QUANTITY+"="+quantity+" WHERE " +
                    TEMPORDER_SOFTWAREID + "="+item.getSoftwareID()+";";
            db.execSQL(insertCommand);
            Log.e("ITEM UPDATE:",insertCommand);
        }



        else{
            ContentValues values = new ContentValues();
            values.put(TEMPORDER_NAME, item.getName());
            values.put(TEMPORDER_PRICE,Integer.valueOf(item.getPrice()));
            values.put(TEMPORDER_QUANTITY,item.getQuantity());
            values.put(TEMPORDER_SOFTWAREID, Integer.valueOf(item.getSoftwareID()));
            values.put(TEMPORDER_LOCALID,Integer.valueOf(item.getLocalId()));
            values.put(TEMPORDER_SQLITEPRODUCTID,item.getSqliteId());
            db.insert(TABLE_TEMPORDER,null,values);
        }
        c.close();
    }

    public void deleteItem(String name){
        db.execSQL("DELETE FROM "+TABLE_TEMPORDER+" WHERE "+TEMPORDER_NAME+"='"+name+"'");
    }

    public ArrayList<ProductItem> getTempProductItems(){
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_TEMPORDER,null);
        ArrayList<ProductItem> items = new ArrayList<>();
        if(c.getCount()>0){
            if(c.moveToFirst()){
                do{
                    ProductItem item = new ProductItem();
                    item.setPrice(String.valueOf(c.getInt(c.getColumnIndex(TEMPORDER_PRICE))));
                    item.setQuantity(c.getInt(c.getColumnIndex(TEMPORDER_QUANTITY)));
                    item.setName(c.getString(c.getColumnIndex(TEMPORDER_NAME)));
                    item.setSoftwareID(String.valueOf(c.getInt(c.getColumnIndex(TEMPORDER_SOFTWAREID))));
                    item.setLocalId(String.valueOf(c.getInt(c.getColumnIndex(TEMPORDER_LOCALID))));
                    item.setSqliteId(c.getInt(c.getColumnIndex(TEMPORDER_SQLITEPRODUCTID)));

                    Log.e("ITEM",item.getName()+"/"+item.getPrice()+"/"+item.getQuantity());
                    items.add(item);


                }while (c.moveToNext());
            }
        }
        c.close();

        return items;
    }

    public void insertCustomers(ArrayList<CustomerListItem> customers){
        for(CustomerListItem customer : customers){
            ContentValues values = new ContentValues();
            values.put(CUSTOMERS_NAME,customer.getName());
            values.put(CUSTOMERS_PHONE,customer.getPhone());
            values.put(CUSTOMERS_SOFTWAREID,Integer.valueOf(customer.getSoftwareId()));
            values.put(CUSTOMERS_WINDOWS, customer.getWindows());

            db.insert(TABLE_CUSTOMERS,null,values);
        }
    }

    public ArrayList<CustomerListItem> getCustoemrs(@Nullable String search){
        ArrayList<CustomerListItem> customers = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_CUSTOMERS +" WHERE "+CUSTOMERS_NAME+" LIKE '%"+search+"%'",null);
        c.moveToFirst();
        if(c.getCount()>0){
            do {
                CustomerListItem customer = new CustomerListItem();
                customer.setPhone(c.getString(c.getColumnIndex(CUSTOMERS_PHONE)));
                customer.setName(c.getString(c.getColumnIndex(CUSTOMERS_NAME)));
                customer.setSoftwareId(String.valueOf(c.getInt(c.getColumnIndex(CUSTOMERS_SOFTWAREID))));
                customer.setWindows(c.getInt(c.getColumnIndex(CUSTOMERS_WINDOWS)));

                customers.add(customer);
            }while (c.moveToNext());
            c.close();
            return customers;
        }else return null;
    }

    public void insertProducts(ArrayList<ProductItem> products){
        for(ProductItem product: products){
            ContentValues values = new ContentValues();
            values.put(PRODUCTS_NAME,product.getName());
            values.put(PRODUCTS_SOFTWAREID,Integer.valueOf(product.getSoftwareID()));
            values.put(PRODUCTS_PRICE,Integer.valueOf(product.getPrice()));
            values.put(PRODUCTS_WINDOWS,product.getWindows());
            values.put(PRODUCTS_LOCALID,product.getLocalId());
            db.insert(TABLE_PRODUCTS,null,values);
        }
    }

    public ArrayList<ProductItem> getProducts(@Nullable String text){
        ArrayList<ProductItem> products = new ArrayList<>();
        Cursor c;
        if(text == null || text.equals(""))c = db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS,null);
        else c = db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+" WHERE "+PRODUCTS_NAME+" LIKE '%"+text+"%'",null);
        c.moveToFirst();

        if (c.getCount() > 0){
            do{
                ProductItem product = new ProductItem();
                product.setName(c.getString(c.getColumnIndex(PRODUCTS_NAME)));
                product.setPrice(String.valueOf(c.getInt(c.getColumnIndex(PRODUCTS_PRICE))));
                product.setWindows(c.getInt(c.getColumnIndex(PRODUCTS_WINDOWS)));
                product.setSoftwareID(String.valueOf(c.getInt(c.getColumnIndex(PRODUCTS_SOFTWAREID))));
                product.setLocalId(String.valueOf(c.getInt(c.getColumnIndex(PRODUCTS_LOCALID))));
                product.setSqliteId(c.getInt(c.getColumnIndex(PRODUCTS_ID)));
                products.add(product);
            }while (c.moveToNext());
            c.close();
            return products;
        }else{
            c.close();
            return null;
        }
    }

    public void deleteTableDatas(String table){
        db.execSQL("DELETE FROM "+table);
    }

    public int insertFactor(FactorItem factorItem,int sentStatus){
        ContentValues values = new ContentValues();
        values.put(FACTORS_CUSTOMERID,factorItem.getCustomerId());
        values.put(FACTORS_DATE,factorItem.getDate());
        values.put(FACTORS_DESC,factorItem.getDes());
        values.put(FACTORS_DISCOUNT,factorItem.getDiscountAmount());
        values.put(FACTORS_SALEMALIID,factorItem.getSaleMaliId());
        values.put(FACTORS_SHIPPING,factorItem.getCharge());
        values.put(FACTORS_TIME, factorItem.getRegTime());
        values.put(FACTORS_TOTAL,factorItem.getTotal());
        values.put(FACTORS_SENT,sentStatus);
        values.put(FACTORS_MYSQLFACTORID,factorItem.getMysqlId());

        db.insert(TABLE_FACTORS,null,values);

        Cursor c = db.rawQuery("SELECT last_insert_rowid()",null);
        c.moveToFirst();
        int lastId = c.getInt(0);
        c.close();
        return lastId;
    }

    public void insertOrders(OrderItem[] orderItems,int factorId){
        for(OrderItem order: orderItems){
            ContentValues values = new ContentValues();
            values.put(SALEARTICLE_DES,order.getDes());
            values.put(SALEARTICLE_FACTORID,factorId);
            values.put(SALEARTICLE_PRICE,order.getQntPrice());
            values.put(SALEARTICLE_PRODUCTID,order.getStoreStuffRelationByLevelID());
            values.put(SALEARTICLE_PRODUCTLOCALID,order.getProductLocalId());
            values.put(SALEARTICLE_QUANTITY,order.getQuantity());
            values.put(SALEARTICLE_ROW,order.getRowNo());
            values.put(SALEARTICLE_TOTAL,order.getTotal());
            values.put(SALEARTICLE_MYSQLFACTORID,order.getMysqlId());
            values.put(SALEARTICLE_SQLITE_PRODUCTID,order.getProductId());

            db.insert(TABLE_SALEARTICLE,null,values);
        }
    }

    public ArrayList<FactorItem> getFactors(@Nullable String search){
        ArrayList<FactorItem> factors = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_FACTORS+";",null);
        c.moveToFirst();
        if (c.getCount()>0) {
            do {
                FactorItem factorItem = new FactorItem();
                int customerId = c.getInt(c.getColumnIndex(FACTORS_CUSTOMERID));
                String customer = getCustomerById(customerId);

                factorItem.setCustomer(customer);
                factorItem.setDate(c.getString(c.getColumnIndex(FACTORS_DATE)).replace("/","-"));
                factorItem.setRegTime(c.getString(c.getColumnIndex(FACTORS_TIME)));
                factorItem.setTotal(c.getInt(c.getColumnIndex(FACTORS_TOTAL)));
                factorItem.setSentStatus(c.getInt(c.getColumnIndex(FACTORS_SENT)));
                factorItem.setId(c.getInt(c.getColumnIndex(FACTORS_ID)));
                factorItem.setMysqlId(c.getInt(c.getColumnIndex(FACTORS_MYSQLFACTORID)));
                factorItem.setSaleMaliId(c.getInt(c.getColumnIndex(FACTORS_SALEMALIID)));
                factorItem.setCharge(c.getInt(c.getColumnIndex(FACTORS_SHIPPING)));
                factorItem.setCustomerId(customerId);
                factorItem.setDes(c.getString(c.getColumnIndex(FACTORS_DESC)));
                factorItem.setDiscountAmount(c.getInt(c.getColumnIndex(FACTORS_DISCOUNT)));
                factorItem.setUserId(c.getInt(c.getColumnIndex(FACTORS_USERID)));

                if(search != null){
                    if(customer.contains(search)){
                        factors.add(factorItem);
                    }
                }else{
                    factors.add(factorItem);
                }

            } while (c.moveToNext());
            c.close();
            return factors;
        }return null;
    }

    private String getCustomerById(int customerId){
        Cursor c = db.rawQuery("SELECT "+CUSTOMERS_NAME+" FROM "+TABLE_CUSTOMERS+" WHERE "+CUSTOMERS_SOFTWAREID+"="+customerId,null);
        c.moveToFirst();
        if(c.getCount()>0){
            String name = c.getString(0);
            c.close();
            return name;
        }else{
            c.close();
            return "";
        }
    }

    public ArrayList<OrderItem> getOrdersByFactorId(int factorId){
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_SALEARTICLE+" WHERE "+SALEARTICLE_FACTORID+"="+factorId,null);
        c.moveToFirst();

        if(c.getCount()>0){
            ArrayList<OrderItem> items = new ArrayList<>();
            do{
                OrderItem orderItem = new OrderItem();
                int productId = c.getInt(c.getColumnIndex(SALEARTICLE_SQLITE_PRODUCTID));
                orderItem.setMysqlId(c.getInt(c.getColumnIndex(SALEARTICLE_MYSQLFACTORID)));
                orderItem.setQuantity(c.getInt(c.getColumnIndex(SALEARTICLE_QUANTITY)));
                orderItem.setDes(c.getString(c.getColumnIndex(SALEARTICLE_DES)));
                orderItem.setTotal(c.getInt(c.getColumnIndex(SALEARTICLE_TOTAL)));
                orderItem.setProductLocalId(c.getInt(c.getColumnIndex(SALEARTICLE_PRODUCTLOCALID)));
                orderItem.setFactorId(factorId);
                orderItem.setId(c.getInt(c.getColumnIndex(SALEARTICLE_ID)));
                orderItem.setQntPrice(c.getInt(c.getColumnIndex(SALEARTICLE_PRICE)));
                orderItem.setRowNo(c.getInt(c.getColumnIndex(SALEARTICLE_ROW)));
                orderItem.setStoreStuffRelationByLevelID(c.getInt(c.getColumnIndex(SALEARTICLE_PRODUCTID)));
                orderItem.setProduct(getProductNameById(productId));

                items.add(orderItem);
            }while (c.moveToNext());
            c.close();
            return items;
        }else return null;
    }

    public void updateFactorStatus(int factorId){
        db.execSQL("UPDATE "+TABLE_FACTORS+" SET "+FACTORS_SENT+"=1"+ " WHERE "+FACTORS_ID+"="+factorId);
    }

    private String getProductNameById(int productId){
        Cursor c = db.rawQuery("SELECT "+PRODUCTS_NAME+" FROM "+TABLE_PRODUCTS+" WHERE "+PRODUCTS_ID+"="+productId,null);
        c.moveToFirst();
        if(c.getCount()>0){
            String name = c.getString(0);
            c.close();
            return name;
        }else return "";
    }

    public void updateOrder(int OrderId,int price,int count,int factorId){
        int totalRow = count * price;
        db.execSQL("UPDATE "+TABLE_SALEARTICLE+" SET "+SALEARTICLE_PRICE+"="+price+","+SALEARTICLE_QUANTITY+"="+count+","+SALEARTICLE_TOTAL+"="+totalRow+" WHERE "+SALEARTICLE_ID+"="+OrderId);
        db.execSQL("UPDATE "+TABLE_FACTORS+" SET "+FACTORS_TOTAL+"="+getFactorTotal(factorId)+" WHERE "+FACTORS_ID+"="+factorId);
        Log.e("QUERY","UPDATE "+TABLE_FACTORS+" SET "+FACTORS_TOTAL+"="+getFactorTotal(factorId)+" WHERE "+FACTORS_ID+"="+factorId);
        Log.e("QUERY 2","UPDATE "+TABLE_SALEARTICLE+" SET "+SALEARTICLE_PRICE+"="+price+","+SALEARTICLE_QUANTITY+"="+count+","+SALEARTICLE_TOTAL+"="+price+" WHERE "+SALEARTICLE_ID+"="+OrderId);
    }

    private int getFactorTotal(int factorId){
        Cursor c = db.rawQuery("SELECT "+SALEARTICLE_TOTAL+" FROM "+TABLE_SALEARTICLE+" WHERE "+SALEARTICLE_FACTORID+"="+factorId,null);
        c.moveToFirst();
        int total = 0;
        do{
            total += c.getInt(0);
        }while (c.moveToNext());
        c.close();
        return total;
    }

    public ArrayList<ProductItem> getProductsByFactorId(int factorId){
        Cursor c = db.rawQuery("SELECT "+SALEARTICLE_QUANTITY+","+TABLE_SALEARTICLE+"."+SALEARTICLE_PRICE+","+PRODUCTS_NAME+
        " FROM "+TABLE_SALEARTICLE+" JOIN "+ TABLE_PRODUCTS+" ON "+TABLE_SALEARTICLE+"."+SALEARTICLE_SQLITE_PRODUCTID+"="+TABLE_PRODUCTS+"."+PRODUCTS_ID+
                " WHERE "+SALEARTICLE_FACTORID+"="+factorId+" GROUP BY "+SALEARTICLE_SQLITE_PRODUCTID,null
        );

        ArrayList<ProductItem> items = new ArrayList<>();
        c.moveToFirst();
        if(c.getCount()>0){
            do{
                ProductItem item = new ProductItem();
                item.setQuantity(c.getInt(c.getColumnIndex(SALEARTICLE_QUANTITY)));
                item.setPrice(String.valueOf(c.getInt(c.getColumnIndex(SALEARTICLE_PRICE))));
                item.setName(c.getString(c.getColumnIndex(PRODUCTS_NAME)));

                items.add(item);
            }while (c.moveToNext());
            c.close();
            return items;
        }return null;
    }

}
