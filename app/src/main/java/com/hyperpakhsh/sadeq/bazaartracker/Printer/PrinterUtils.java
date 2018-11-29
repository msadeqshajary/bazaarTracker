//package com.hyperpakhsh.sadeq.bazaartracker.Printer;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.bxl.BXLConst;
//import com.bxl.config.editor.BXLConfigLoader;
//import com.hyperpakhsh.sadeq.bazaartracker.Order.OrderItem;
//import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;
//import com.hyperpakhsh.sadeq.bazaartracker.R;
//
//import java.nio.ByteBuffer;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import jpos.JposConst;
//import jpos.JposException;
//import jpos.MSR;
//import jpos.POSPrinter;
//import jpos.POSPrinterConst;
//import jpos.config.JposEntry;
//import jpos.events.DataEvent;
//import jpos.events.DataListener;
//import jpos.events.ErrorEvent;
//import jpos.events.ErrorListener;
//import jpos.events.OutputCompleteEvent;
//import jpos.events.OutputCompleteListener;
//import jpos.events.StatusUpdateEvent;
//import jpos.events.StatusUpdateListener;
//
//public class PrinterUtils implements ErrorListener,StatusUpdateListener,OutputCompleteListener,DataListener{
//
//    private Activity activity;
//    private MSR msr;
//
//    public PrinterUtils(Activity activity) {
//        this.activity = activity;
//        msr = new MSR();
//    }
//
//    /**
//     * First, check if no devices are connected, then list all available paired bluetooth devices to select. if select,
//     * then Mac address and logical name are exported from it.
//     */
//    private String logicalName;
//    private String MacAddress;
//
//    public void findPairedDevices(){
//        //TO DO: codes for list devices and select printer device
//        logicalName = "SPP-R210";
//        MacAddress = "74:F0:7D:E3:AD:DF";
//    }
//
//    /**
//     * Second, Delete all Entries in BxlConfigLoader and then assign new Entry to it
//     */
//    private BXLConfigLoader bxlConfigLoader;
//    private POSPrinter posPrinter;
//
//    public void setBxlConfigLoader(Context context){
//        if(bxlConfigLoader == null){
//            bxlConfigLoader = new BXLConfigLoader(context);
//        }
//
//        try {
//            bxlConfigLoader.openFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//            bxlConfigLoader.newFile();
//        }
//
//        if(posPrinter == null){
//            posPrinter = new POSPrinter(context);
//        }
//
//        posPrinter.addOutputCompleteListener(this);
//        posPrinter.addStatusUpdateListener(this);
//        posPrinter.addErrorListener(this);
//
//        try {
//            for(Object Entry : bxlConfigLoader.getEntries()){
//                JposEntry jposEntry = (JposEntry) Entry;
//                bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            bxlConfigLoader.addEntry(logicalName,BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER,
//                    BXLConst.SPP_R210,BXLConfigLoader.DEVICE_BUS_BLUETOOTH,MacAddress);
//            bxlConfigLoader.saveFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * Just connect to device!
//     */
//
//    public void connect(Context context){
//        try {
//            posPrinter.open(logicalName);
//            posPrinter.claim(0);
//            posPrinter.setDeviceEnabled(true);
//            posPrinter.setAsyncMode(true);
//        } catch (JposException e) {
//            e.printStackTrace();
//            try {
//                posPrinter.close();
//            } catch (JposException e1) {
//                e1.printStackTrace();
//            }
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void disconnect(Context context){
//        try {
//            posPrinter.close();
//        } catch (JposException e) {
//            e.printStackTrace();
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * Now time to print a sample page
//     */
//
//    public void PrintOrders(ArrayList<ProductItem> items,String customer,int discount,String totalX) {
//        try {
//
//            //Header
//            BitmapDrawable bitmapDrawable;
//            Bitmap bitmap;
//
//            try {
//
//                bitmapDrawable = (BitmapDrawable) activity.getApplicationContext().getResources().getDrawable(R.drawable.logo);
//                bitmap = bitmapDrawable.getBitmap();
//
//                ByteBuffer bitmapbuffer = ByteBuffer.allocate(4);
//                bitmapbuffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
//                bitmapbuffer.put((byte) 80);
//                bitmapbuffer.put((byte) 0x00);
//                bitmapbuffer.put((byte) 0x00);
//
//                try {
//                    posPrinter.printBitmap(bitmapbuffer.getInt(0), bitmap, posPrinter.getRecLineWidth(), POSPrinterConst.PTR_BM_CENTER);
//                } catch (JposException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n");
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//
//                //Header Items
//                Calendar calendar = Calendar.getInstance();
//                DateFormat simpleDateFormat = SimpleDateFormat.getDateTimeInstance();
//                String date = simpleDateFormat.format(calendar.getTime());
//
//
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n" + customer + EscapeSequence.Center);
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n" + date + EscapeSequence.Center);
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n" + "**************" + EscapeSequence.Center);
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n");
//
//            //first factor row
//            String productx = "محصول";
//            String countx = "تعداد";
//            String single = "واحد";
//            String totalx = "جمع";
//
//            StringBuilder bufferx = new StringBuilder();
//            bufferx.append(productx);
//            for (int j = bufferx.length(); j < 28; j++) {
//                bufferx.append(" ");
//            }
//
//            for (int j = totalx.length(); j < 8; j++) {
//                bufferx.append(" ");
//            }
//            bufferx.append(totalx);
//
//            for (int j = single.length(); j < 8; j++) {
//                bufferx.append(" ");
//            }
//            bufferx.append(single);
//
//            for (int j = countx.length(); j < 8; j++) {
//                bufferx.append(" ");
//            }
//            bufferx.append(countx);
//
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n" + bufferx.toString() + EscapeSequence.Right_justify + EscapeSequence.ESCAPE_CHARACTERS +
//                    EscapeSequence.Scale_1_time_horizontally + EscapeSequence.Scale_1_time_vertically + EscapeSequence.Bold);
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n" + "----------------------" + EscapeSequence.Center);
//
//                for (ProductItem item : items) {
//                    String product, count, price, total;
//
//                    if (item.getQuantity() != 0) {
//                        product = item.getName();
//                        count = String.valueOf(item.getQuantity());
//                        price = item.getPrice();
//                        total = String.valueOf(item.getQuantity() * Integer.valueOf(item.getPrice()));
//
//                        StringBuilder buffer = new StringBuilder(product);
//                        for (int j = buffer.length(); j < 40; j++) {
//                            buffer.append(" ");
//                        }
//
//                        for (int j = total.length(); j < 8; j++) {
//                            buffer.append(" ");
//                        }
//                        buffer.append(total);
//
//                        for (int j = price.length(); j < 6; j++) {
//                            buffer.append(" ");
//                        }
//                        buffer.append(price);
//
//                        for (int j = count.length(); j < 4; j++) {
//                            buffer.append(" ");
//                        }
//                        buffer.append(count);
//                        posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n " + buffer.toString() + EscapeSequence.Right_justify + EscapeSequence.ESCAPE_CHARACTERS +
//                                EscapeSequence.Scale_1_time_horizontally + EscapeSequence.Scale_1_time_vertically);
//                    }
//                }
//
//                posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n" + "------------------------" + EscapeSequence.Center);
//                //posPrinter.cutPaper(90);
//
//            } catch (JposException e) {
//                e.printStackTrace();
//            }
//
//            //total row
//            StringBuffer bufferDiscount = new StringBuffer();
//             bufferDiscount.append("تخفیف");
//            for(int i=bufferDiscount.length();i<20;i++) bufferDiscount.append(" ");
//            for(int i = String.valueOf(discount).length();i<5;i++)bufferDiscount.append(" ");
//            bufferDiscount.append(String.valueOf(discount));
//
//        try {
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n"+bufferDiscount+ EscapeSequence.Center);
//        } catch (JposException e) {
//            e.printStackTrace();
//        }
//
//        StringBuffer bufferTotal = new StringBuffer();
//        bufferTotal.append("جمع");
//        for(int i = bufferTotal.length();i<20;i++) bufferTotal.append(" ");
//        for(int i = String.valueOf(totalX).length();i<5;i++)bufferTotal.append(" ");
//        bufferTotal.append(String.valueOf(discount));
//
//        try {
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n"+bufferTotal+ EscapeSequence.Center);
//        } catch (JposException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n"+"-------------------------"+ EscapeSequence.Center);
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n"+"-------------------------"+ EscapeSequence.Center);
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,"\n"+"-------------------------"+ EscapeSequence.Center);
//        } catch (JposException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void errorOccurred(final ErrorEvent errorEvent) {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                String error;
//                switch (errorEvent.getErrorCodeExtended()){
//                    case POSPrinterConst.JPOS_EPTR_COVER_OPEN:
//                        error = "COVER IS OPEN";break;
//                    case POSPrinterConst.JPOS_EPTR_REC_EMPTY:
//                        error = "PAPER EMPTY";break;
//                    case  JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
//                        error = "PRINTER IS OFF";break;
//                    default: error="UNKNOWN";
//                }
//
//                Log.e("PRINTER ERROR",error);
//            }
//        });
//    }
//
//    @Override
//    public void statusUpdateOccurred(final StatusUpdateEvent statusUpdateEvent) {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (statusUpdateEvent.getStatus()){
//                    case JposConst.JPOS_SUE_POWER_ONLINE:
//                        Log.e("PRINTER STATUS", "Power on");
//                    case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
//                        Log.e("PRINTER STATUS", "Power off");
//                    case POSPrinterConst.PTR_SUE_COVER_OPEN:
//                        Log.e("PRINTER STATUS", "cover open");
//                    case POSPrinterConst.PTR_SUE_COVER_OK:
//                        Log.e("PRINTER STATUS", "cover ok");
//                    case POSPrinterConst.PTR_SUE_REC_EMPTY:
//                        Log.e("PRINTER STATUS", "Recipt paper empty");
//                    case POSPrinterConst.PTR_SUE_REC_NEAREMPTY:
//                        Log.e("PRINTER STATUS", "Receipt paper near empty");
//                    case POSPrinterConst.PTR_SUE_REC_PAPEROK:
//                        Log.e("PRINTER STATUS", "Receipt paper OK");
//                    case POSPrinterConst.PTR_SUE_IDLE:
//                        Log.e("PRINTER STATUS", "Printer Idle");
//                    default:
//                        Log.e("PRINTER STATUS", "UNKNOWN");
//                }
//            }
//        });
//    }
//
//    @Override
//    public void outputCompleteOccurred(OutputCompleteEvent outputCompleteEvent) {
//        Log.e("PRINTER","COMPLETED");
//    }
//
//    @Override
//    public void dataOccurred(DataEvent dataEvent) {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String data =  new String(msr.getTrack1Data());
//                    data += new String(msr.getTrack2Data());
//                    data += new String(msr.getTrack3Data());
//                    Log.e("PRINTER DATA",data);
//                } catch (JposException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//
//
//}
