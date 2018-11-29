package com.hyperpakhsh.sadeq.bazaartracker.Order;


public class OrderItem {
    private int RowNo, StoreStuffRelationByLevelID,quantity,QntPrice,total,factorId,ProductLocalId;
    private String Des,product;
    private int mysqlId;
    private int productId;
    private int id;
    private int sqliteFactorId;

    public void setSqliteFactorId(int sqliteFactorId) {
        this.sqliteFactorId = sqliteFactorId;
    }

    public int getSqliteFactorId() {
        return sqliteFactorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public void setMysqlId(int mysqlId) {
        this.mysqlId = mysqlId;
    }

    public int getMysqlId() {
        return mysqlId;
    }

    public void setProductLocalId(int productLocalId) {
        ProductLocalId = productLocalId;
    }

    public int getProductLocalId() {
        return ProductLocalId;
    }

    public int getRowNo() {
        return RowNo;
    }

    public void setFactorId(int factorId) {
        this.factorId = factorId;
    }

    public int getFactorId() {
        return factorId;
    }

    public void setRowNo(int rowNo) {
        RowNo = rowNo;
    }

    public int getStoreStuffRelationByLevelID() {
        return StoreStuffRelationByLevelID;
    }

    public void setStoreStuffRelationByLevelID(int storeStuffRelationByLevelID) {
        StoreStuffRelationByLevelID = storeStuffRelationByLevelID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQntPrice() {
        return QntPrice;
    }

    public void setQntPrice(int qntPrice) {
        QntPrice = qntPrice;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }
}
