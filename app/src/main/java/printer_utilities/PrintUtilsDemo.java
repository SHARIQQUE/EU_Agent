package printer_utilities;

import android.content.Context;
import android.graphics.Bitmap;

import com.datecs.api.printer.Printer;

import java.io.IOException;
import java.util.ArrayList;

import agent.activities.R;
import callback.TaskCompleteToPrint;
import model.BillModel;

public class PrintUtilsDemo {

    TaskCompleteToPrint taskCompleteToPrint;
    Printer mPrinter;
    Bitmap logoBitmap;
    Context ctx;

    public PrintUtilsDemo(TaskCompleteToPrint taskCompleteToPrint, Printer mPrinter, Bitmap logoBitmap, Context ctx) {

        this.taskCompleteToPrint = taskCompleteToPrint;
        this.mPrinter = mPrinter;
        this.logoBitmap = logoBitmap;
        this.ctx = ctx;
    }

    //===============================  Header   =============================================
    public void print_Header(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}{br} {br}{br}\n  ");
        sb.append("{reset}{left}"+ctx.getResources().getString(R.string.dateRecepit)+"    "+ctx.getResources().getString(R.string.timeReceipt)+"       "+ctx.getResources().getString(R.string.transactionIdReceiptNew)+" {br}");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "           " + dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        try {
            if (dataList.size() > 5) {
                sb.append("{reset}{left}{b}OLD TRANSACTION ID: " + dataList.get(5) + "  " + "{br}");
                sb.append("{reset}{left}    " + "{br}{br}");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_Header_TransactionCancel(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}{br} {br}{br}\n  ");
        sb.append("{reset}{left}Date:      Time:       Txn ID: {br}");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "         " + dataList.get(2) + "           " + dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}OLD TRANSACTION ID: " + dataList.get(5) + "  " + "{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void print_Header_Reports(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}{br} {br}{br}\n  ");
        sb.append("{reset}{left}{b} Date           Time        {br}{br}{br}");
        sb.append("{reset}{left}{s}  " + dataList.get(1) + "              " + dataList.get(2)   + "{br}{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}{b} REPORTS:    " + dataList.get(3) + "{br}{br}{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        // sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void print_Footer() {
        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);

           /*     int x = logoBitmap.getWidth();
                int y = logoBitmap.getHeight();
                int[] intArray = new int[x * y];
               logoBitmap.getPixels(intArray, 0, x, 0, 0, x, y);
                mPrinter.printImage(intArray, 40, 40, Printer.ALIGN_RIGHT, false);
                  mPrinter.flush();*/


            final int width = logoBitmap.getWidth();
            final int height = logoBitmap.getHeight();
            final int[] argb = new int[width * height];
            logoBitmap.getPixels(argb, 0, width, 0, 0, width, height);
            //  logoBitmap.recycle();

            mPrinter.printImage(argb, width, height, Printer.ALIGN_RIGHT, false);
            mPrinter.flush();
            StringBuffer sb = new StringBuffer();
            sb.append("{reset}{left}    " + "{br}{br}");
            sb.append("{reset}{left}    " + "{br}{br}");
            sb.append("{reset}{left}    " + "{br}{br}");
            sb.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            print(sb.toString(), Printer.ALIGN_LEFT);
            //  sb.append("{reset}{left}    " + "{br}{br}");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //============================================================================
    public void print_Footer_hp() {
        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);

           /*     int x = logoBitmap.getWidth();
                int y = logoBitmap.getHeight();
                int[] intArray = new int[x * y];
               logoBitmap.getPixels(intArray, 0, x, 0, 0, x, y);
                mPrinter.printImage(intArray, 40, 40, Printer.ALIGN_RIGHT, false);
                  mPrinter.flush();*/


            final int width = logoBitmap.getWidth();
            final int height = logoBitmap.getHeight();
            final int[] argb = new int[width * height];
            logoBitmap.getPixels(argb, 0, width, 0, 0, width, height);
            //  logoBitmap.recycle();

            mPrinter.printImage(argb, width, height, Printer.ALIGN_CENTER, false);
            mPrinter.flush();
            StringBuffer sb = new StringBuffer();
            //  sb.append("{reset}{left}    " + "{br}{br}");
            // sb.append("{reset}{left}    " + "{br}{br}");

            sb.append("{reset}{left}{b}" + "        @-fuel station" + "{br}{br}{br}{br}");   // Bold




            // sb.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            print(sb.toString(), Printer.ALIGN_LEFT);
            //  sb.append("{reset}{left}    " + "{br}{br}");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //============================================================================

    //============================================================================
    private void print(String printString, int alignment) {
        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.setAlign(alignment);
            mPrinter.printTaggedText(printString);
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //============================================================================

    public void print_CreateAgent(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}    " + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt) + dataList.get(0) + "  " + "{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscribeNameReceipt)+ dataList.get(1) + "{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)+ dataList.get(2) + "{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.VilleReceiptNew)+":" + dataList.get(3) + "{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.Country)+":");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.presentAgentnameNew));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}{br}");


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public boolean print_ReceiveMoneyToName(ArrayList<String> dataList) {
        boolean ret = false;


   /*  responseListForPrinter_Body.add(0, recieptData[4]);   // Destination Country
        responseListForPrinter_Body.add(1, recieptData[5]);     // Destination Name
        responseListForPrinter_Body.add(2, spinnerCurrenceyString); // currency Type
        responseListForPrinter_Body.add(3, recieptData[7]);        // Amount
        responseListForPrinter_Body.add(4, recieptData[6]);    // fees
        responseListForPrinter_Body.add(5, totalAmountString);  // Total Amount
        responseListForPrinter_Body.add(6, agentName);           // Agent Name
        responseListForPrinter_Body.add(7, recieptData[8]);     // Agent Branch NAme
        responseListForPrinter_Body.add(8, spinnerCountryString); // Agent Country*/

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}Destination Country:");
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Destination Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Currency Type:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Total Amount Receive:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:                 ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_ReceiveMoneyToMobile(ArrayList<String> dataList) {
        boolean ret = false;


     /* responseListForPrinter_Body.add(0, recieptData[4]);   // Destination Country
        responseListForPrinter_Body.add(1, recieptData[9]);     // Destination Name
        responseListForPrinter_Body.add(2, recieptData[5]);    //  Destination Number
        responseListForPrinter_Body.add(3, spinnerCurrenceyString); // currency Type
        responseListForPrinter_Body.add(4, recieptData[7]);        // Amount
        responseListForPrinter_Body.add(5, recieptData[6]);    // fees
        responseListForPrinter_Body.add(6, totalAmountString);  // Total Amount
        responseListForPrinter_Body.add(7, agentName);           // Agent Name
        responseListForPrinter_Body.add(8, recieptData[8]);     // Agent Branch NAme
        responseListForPrinter_Body.add(9, spinnerCountryString); // Agent Country
	*/

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationCountryRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountReceive));
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview));
        sb.append("{reset}{left}{s}" + dataList.get(10) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");


      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"      ");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
        return ret;
    }

    public boolean print_ReceiveMoney_transactionApproval(ArrayList<String> dataList) {
        boolean ret = false;



        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}Destination Country:");
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Destination Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Destination Number:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Currency Type:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Total Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:                 ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
        return ret;
    }


    public void print_SendMoney(ArrayList<String> dataList) {


      /*  responseListForPrinter_Body.add(0, recieptData[11]);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
        responseListForPrinter_Body.add(2, spinnerCountryString); //  Destination Country Camero
        responseListForPrinter_Body.add(3, recieptData[9]);   //  Destination name
        responseListForPrinter_Body.add(4, recieptData[5]);  //  Destination number
        responseListForPrinter_Body.add(5, recieptData[7]);  //  Countyr Type
        responseListForPrinter_Body.add(6, recieptData[7]);  // Amoount
        responseListForPrinter_Body.add(7, recieptData[6]);  // fees
        responseListForPrinter_Body.add(8, totalAmountString);  // Total Amount
        responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(10, agentName);   //
        responseListForPrinter_Body.add(11, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(12, recieptData[4]);  // Agent Country*/


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Sender Name: " + dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}Sender Number: " + dataList.get(1) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Name: " + dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Number: " + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}Currency Type: " + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}Amount: " + dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}Fees: " + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}Total Amount: " + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}Transaction Code: " + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:" + dataList.get(11) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Sign:                Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }
    public void print_SendMoneyToMobile(ArrayList<String> dataList) {


      /*  responseListForPrinter_Body.add(0, recieptData[11]);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
       // responseListForPrinter_Body.add(2, spinnerCountryString); //  Destination Country Camero   // remove 02082017
        responseListForPrinter_Body.add(3, recieptData[9]);   //  Destination name
        responseListForPrinter_Body.add(4, recieptData[5]);  //  Destination number
        responseListForPrinter_Body.add(5, recieptData[7]);  //  Countyr Type
        responseListForPrinter_Body.add(6, recieptData[7]);  // Amoount
        responseListForPrinter_Body.add(7, recieptData[6]);  // fees
        responseListForPrinter_Body.add(8, totalAmountString);  // Total Amount
        responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(10, agentName);   //
        responseListForPrinter_Body.add(11, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(12, recieptData[4]);  // Agent Country*/


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+ dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNumberReceipt)+ dataList.get(1) + "{br}{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt)+ dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)  + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public void print_SendMoneyToMobile_ConfcodeDisplay(ArrayList<String> dataList) {


      /*  responseListForPrinter_Body.add(0, recieptData[11]);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
       // responseListForPrinter_Body.add(2, spinnerCountryString); //  Destination Country Camero   // remove 02082017
        responseListForPrinter_Body.add(3, recieptData[9]);   //  Destination name
        responseListForPrinter_Body.add(4, recieptData[5]);  //  Destination number
        responseListForPrinter_Body.add(5, recieptData[7]);  //  Countyr Type
        responseListForPrinter_Body.add(6, recieptData[7]);  // Amoount
        responseListForPrinter_Body.add(7, recieptData[6]);  // fees
        responseListForPrinter_Body.add(8, totalAmountString);  // Total Amount
        responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(10, agentName);   //
        responseListForPrinter_Body.add(11, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(12, recieptData[4]);  // Agent Country*/


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+ dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNumberReceipt)+ dataList.get(1) + "{br}{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt)+ dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)  + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_SendMoneyToMobile_ConfcodeHide(ArrayList<String> dataList) {


      /*  responseListForPrinter_Body.add(0, recieptData[11]);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
       // responseListForPrinter_Body.add(2, spinnerCountryString); //  Destination Country Camero   // remove 02082017
        responseListForPrinter_Body.add(3, recieptData[9]);   //  Destination name
        responseListForPrinter_Body.add(4, recieptData[5]);  //  Destination number
        responseListForPrinter_Body.add(5, recieptData[7]);  //  Countyr Type
        responseListForPrinter_Body.add(6, recieptData[7]);  // Amoount
        responseListForPrinter_Body.add(7, recieptData[6]);  // fees
        responseListForPrinter_Body.add(8, totalAmountString);  // Total Amount
        responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(10, agentName);   //
        responseListForPrinter_Body.add(11, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(12, recieptData[4]);  // Agent Country*/


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+ dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNumberReceipt)+ dataList.get(1) + "{br}{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt)+ dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)  + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+dataList.get(11) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_cashToCashSameCountry(ArrayList<String> dataList) {




        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.referenceNumberReceipt)+ dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+ dataList.get(1) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNumberReceipt)+ dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationCountryRecepit)+ dataList.get(3) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt)+ dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(9) + "{br}{br}");
        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)  + dataList.get(11) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+dataList.get(13) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_cashToCashReceiveMoneySameCountry(ArrayList<String> dataList) {

       /* responseListForPrinter_Body.add(0, recieptData[8]);  // Reference
        responseListForPrinter_Body.add(1, "");             // Destination Country
        responseListForPrinter_Body.add(2, recieptData[4]);      // destination name
        responseListForPrinter_Body.add(3, recieptData[5]);  // destination number
        responseListForPrinter_Body.add(4, currencySearchString); // Curencey Type
        responseListForPrinter_Body.add(5, amountSearch); // Amount
        responseListForPrinter_Body.add(6, amountSearchNoToWord); // amount in word
        responseListForPrinter_Body.add(7, agentName);   //  agent Name
        responseListForPrinter_Body.add(8, recieptData[6]);   // Agnet branch Name
        responseListForPrinter_Body.add(9, recieptData[7]);  // Agent Country
        responseListForPrinter_Body.add(10, commentString);  // comment

     */

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.referenceNumberReceipt)+ dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationCountryRecepit)+ dataList.get(1) + "{br}{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt)+ dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountWord) + dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentBranceNameReceipt)  + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+dataList.get(10) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }



    public void print_SendMoneyToMobile_diffrentCountry(ArrayList<String> dataList) {


   /*     responseListForPrinter_Body.add(0, SenderNameAgentIdentity);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
        responseListForPrinter_Body.add(2, countrySelection); //  Destination Country
        responseListForPrinter_Body.add(3, destinationNameAgentIdentity);   //  Destination name
        responseListForPrinter_Body.add(4, destinationNoString);  //  Destination number
        responseListForPrinter_Body.add(5, spinnerCurrenceyString);  //  currency Type Type
        responseListForPrinter_Body.add(6, totalAmountString);  // Amoount
        responseListForPrinter_Body.add(7, recieptData[6]);  // fees
        responseListForPrinter_Body.add(8, recieptData[7]);  // Total Amount
        responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(10, agentName);   //  agent Name
        responseListForPrinter_Body.add(11, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(12, recieptData[4]);  // Agent Country
        responseListForPrinter_Body.add(13, commentString);  // comment
*/

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+ dataList.get(0) + "{br}{br}"); //
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNumberReceipt)+dataList.get(1) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationCountryRecepit)+ dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNumberReceipt)+ dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.currencyTypeReceipt)+ dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)  + dataList.get(11) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+ dataList.get(13) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");



        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_SendMoney_transactionApproval(ArrayList<String> dataList) {





        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Sender Name: " + dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}Sender Number: " + dataList.get(1) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Name: " + dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Number: " + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}Currency Type: " + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}Amount: " + dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}Fees: " + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}Total Amount: " + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}Transaction Code: " + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:" + dataList.get(11) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}Agent Sign:                    Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public boolean print_cashOutWithdrawal(ArrayList<String> dataList) {

        boolean ret = false;

      /*  responseListForPrinter_Body.add(0, recieptData[5]);   // Subscriber number
        responseListForPrinter_Body.add(1, recieptData[10]);  // Subscriber name
        responseListForPrinter_Body.add(2, recieptData[8]);   // Attach Branch name
        responseListForPrinter_Body.add(3, recieptData[9]);  // City number
        responseListForPrinter_Body.add(4, recieptData[4]);  // Country
        responseListForPrinter_Body.add(5, recieptData[7]);  // Amount
        responseListForPrinter_Body.add(6, recieptData[6]);  // fees
        responseListForPrinter_Body.add(7, agentName);  // Agent NAme
        responseListForPrinter_Body.add(8, spinnerCountryString);   // Agent Country
*/
        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Subscriber Number:");
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Subscriber Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Attach Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}City:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Country:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:    ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public void print_SendMoneyToName(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Sender Name: " + dataList.get(0) + "{br}{br}");
        sb.append("{reset}{left}{s}Sender Number: " + dataList.get(1) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}Destination Name: " + dataList.get(3) + "{br}{br}");
        // sb.append("{reset}{left}{s}Destination Number: " + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}Currency Type: " + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}Amount: " + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}Fees: " + dataList.get(6) + "{br}{br}");
        sb.append("{reset}{left}{s}Total Amount: " + dataList.get(7) + "{br}{br}");
        sb.append("{reset}{left}{s}Transaction Code: " + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(9) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:" + dataList.get(10) + "{br}{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(11) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Sign:                    Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public boolean print_CashIn(ArrayList<String> dataList) {
        boolean ret = false;
        // dataList values
        //1 subscriber Number
        //2 subscriber name
        //3 Attach Branch name
        //4 city
        //5 country
        //6 Amount
        //7 Fees
        //8 Agent Name
        // Agent Country

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt));  // subscriber Number
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscribeNameReceipt));  // Subscriber name
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.VilleReceiptNew)+":");     //VilleReceiptNew
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.Country)+":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit));    // Amount                                   // Amount
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");   //subscriberSignRecepit

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

//();

        return ret;
    }

    public boolean print_petrol(ArrayList<String> dataList) {
        boolean ret = false;

        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}{b}" + "   Auth.Dealer : Bharat Petroleum Corp. ltd."+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + " Tigaon Road,Sec-87,Vill Baselwa,Near Modern DSP" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "                   Faridabad - 121002" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "               VAT TIN:43545850"+ "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "               TEL : 0129-6450172"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{b}" + "             ORIGINAL" + "{br}{br}");   // Bold
        sb.append("{reset}{left}{s}" + "          Receipt ID      :  80258"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Date            :  2017/10/19"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Time            :  21:18:12"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          TRX. ID         :  595987"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          TRX. Type       :  Cash"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Pump No.        :  2"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Nozzle No.      :  3 "+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Product         :  HSD "+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Price (RS)      :  67.10"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Quantity        :  32 Ltr "+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Total Sale (RS) :  2147.02"+"{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Vehicle No      :  2258"+"{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}" + "              Thanks & Visit Again  "+"{br}{br}");
        sb.append("{reset}{left}{s}" + "              Save fuel,Save Money  "+"{br}{br}{br}{br}{br}{br}{br}{br}{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"         ");   // agentSignRecepit
        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");   //subscriberSignRecepit

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

//();

        return ret;
    }

    public boolean print_CashIn_cashOut_TransactionApproval(ArrayList<String> dataList) {
        boolean ret = false;
        // dataList values
        //1 subscriber Number
        //2 subscriber name
        //3 Attach Branch name
        //4 city
        //5 country
        //6 Amount
        //7 Fees
        //8 Agent Name
        // Agent Country

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}Subscriber Number:");
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Subscriber Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Attach Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}City:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Country:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:                 ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

//();

        return ret;
    }



    public boolean print_reportHistory(ArrayList<String> dataList) {
        boolean ret = false;
/*9297923|2585.0|237000271515|237000271502|10/07/2017 13:27:03|APPROVE*/

/*sb.append("{reset}{left}Date           Time        {br}");*/
        StringBuffer sb = new StringBuffer();
        try {
            for (int k = 0; k < dataList.size(); k++) {
                String[] temp = dataList.get(k).split("\\|");
                sb.append("{reset}{left}{s}TxnId: " + temp[0] + "     " + "Amount: " + temp[1] + "{br}{br}{br}");
                sb.append("{reset}{left}{s}Source: " + temp[2] + "  " + "Destination: " + temp[3] + "{br}{br}{br}");
                sb.append("{reset}{left}{s}Date: " + temp[4]  + "  " + "Trans Type: " + temp[5]+"{br}{br}{br}{br}{br}{br}{br}{br}");


            }
            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
        }catch (Exception e){
            e.printStackTrace();
        }
      /*  sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Source:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Destination:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Date:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");*/

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/


       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/




//();

        return ret;
    }


    public boolean print_cashOutSameBranch(ArrayList<String> dataList) {

        boolean ret = false;

      /*  responseListForPrinter_Body.add(0, recieptData[5]);   // Subscriber number
        responseListForPrinter_Body.add(1, recieptData[10]);  // Subscriber name
        responseListForPrinter_Body.add(2, recieptData[8]);   // Attach Branch name
        responseListForPrinter_Body.add(3, recieptData[9]);  // City number
        responseListForPrinter_Body.add(4, recieptData[4]);  // Country
        responseListForPrinter_Body.add(5, recieptData[7]);  // Amount
        responseListForPrinter_Body.add(6, agentName);  // Agent NAme
        responseListForPrinter_Body.add(7, spinnerCountryString);   // Agent Country*/

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Subscriber Number:");
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Subscriber Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Attach Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}City:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Country:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:                 ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_cashDiffrentBranch(ArrayList<String> dataList) {

        boolean ret = false;

      /*  responseListForPrinter_Body.add(0, recieptData[5]);   // Subscriber number
        responseListForPrinter_Body.add(1, recieptData[10]);  // Subscriber name
        responseListForPrinter_Body.add(2, recieptData[8]);   // Attach Branch name
        responseListForPrinter_Body.add(3, recieptData[9]);  // City number
        responseListForPrinter_Body.add(4, recieptData[4]);  // Country
        responseListForPrinter_Body.add(5, recieptData[7]);  // Amount
        responseListForPrinter_Body.add(6, agentName);  // Agent NAme
        responseListForPrinter_Body.add(7, spinnerCountryString);   // Agent Country*/

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt)); // subscriber number
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscribeNameReceipt));  // Subscriber name
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.VilleReceiptNew)+":");     //City
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.Country)+":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit));    // Amount
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"         ");      // Agent Sign    ");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");   //Subscriber Sign
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }
    public boolean print_cancelReceipt_cashToMerchant(ArrayList<String> dataList) {


        boolean ret = false;

        // dataList values
        //1 Merchant Name
        //2 Bill Number
        //3 Label Name
        //4 Destination Name
        //5 Sender Name
        //6 Amount
        //7 Fees
        //8 Total Amount
        //9 Txn Code
        //10 Agent Name
        //11 Agent Branch Name
        //12 Agent Country
        //13 Comments

        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}Merchant Name: " + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Bill Number:" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Label Name: " + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Destination Name: " + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Sender Name: " + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Total Amount: " + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Transaction Code: " + dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(9) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name: " + dataList.get(10) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(11) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Comments: " + dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");
        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}Agent Sign:                 Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }



    public boolean print_CashToMerchant(ArrayList<String> dataList)
    {


        boolean ret = false;

        // dataList values
        //1 Merchant Name
        //2 Bill Number
        //3 Label Name
        //4 Destination Name
        //5 Sender Name
        //6 Amount
        //7 Fees
        //8 Total Amount
        //9 Txn Code
        //10 Agent Name
        //11 Agent Branch Name
        //12 Agent Country
        //13 Comments

        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.marchantNameReceipt)+ dataList.get(0) + "{br}{br}{br}{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.billNumberReceipt)+ dataList.get(1) + "{br}{br}{br}{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.labelNameReceipt)+ dataList.get(2) + "{br}{br}{br}{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(3) + "{br}{br}{br}{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+dataList.get(4) + "{br}{br}{br}{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit)+ dataList.get(5) + "{br}{br}{br}{br}");   // amountRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}{br}{br}{br}");  // feesRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit)+ dataList.get(7) + "{br}{br}{br}{br}"); // totalamountRecepit

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit)+ dataList.get(8) + "{br}{br}{br}{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit)+ dataList.get(9) + "{br}{br}{br}{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)+ dataList.get(10) + "{br}{br}{br}{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit)+ dataList.get(11) + "{br}{br}{br}{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+ dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"     "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }
    public boolean print_CashToMerchant_Mer(ArrayList<String> dataList)
    {


        boolean ret = false;

        // dataList values
        //1 Merchant Name
        //2 Bill Number
        //3 Label Name
        //4 Destination Name
        //5 Sender Name
        //6 Amount
        //7 Fees
        //8 Total Amount
        //9 Txn Code
        //10 Agent Name
        //11 Agent Branch Name
        //12 Agent Country
        //13 Comments

        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.marchantNameReceipt)+ dataList.get(0) + "{br}{br}{br}{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.billNumberReceipt)+ dataList.get(1) + "{br}{br}{br}{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.labelNameReceipt)+ dataList.get(2) + "{br}{br}{br}{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(3) + "{br}{br}{br}{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+dataList.get(4) + "{br}{br}{br}{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit)+ dataList.get(5) + "{br}{br}{br}{br}");   // amountRecepit

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit)+ dataList.get(6) + "{br}{br}{br}{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit)+ dataList.get(7) + "{br}{br}{br}{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)+ dataList.get(8) + "{br}{br}{br}{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit)+ dataList.get(9) + "{br}{br}{br}{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+ dataList.get(10) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"     "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }




    public boolean print_CashToMerchant_Sub(ArrayList<String> dataList)
    {


        boolean ret = false;

        // dataList values
        //1 Merchant Name
        //2 Bill Number
        //3 Label Name
        //4 Destination Name
        //5 Sender Name
        //6 Amount
        //7 Fees
        //8 Total Amount
        //9 Txn Code
        //10 Agent Name
        //11 Agent Branch Name
        //12 Agent Country
        //13 Comments

        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.marchantNameReceipt)+ dataList.get(0) + "{br}{br}{br}{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.billNumberReceipt)+ dataList.get(1) + "{br}{br}{br}{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.labelNameReceipt)+ dataList.get(2) + "{br}{br}{br}{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt)+ dataList.get(3) + "{br}{br}{br}{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderNameReceipt)+dataList.get(4) + "{br}{br}{br}{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit)+ dataList.get(5) + "{br}{br}{br}{br}");   // amountRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}{br}{br}{br}");  // feesRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit)+ dataList.get(7) + "{br}{br}{br}{br}"); // totalamountRecepit

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit)+ dataList.get(8) + "{br}{br}{br}{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit)+ dataList.get(9) + "{br}{br}{br}{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.attachBranceNameReceipt)+ dataList.get(10) + "{br}{br}{br}{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit)+ dataList.get(11) + "{br}{br}{br}{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+ dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"     "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_prepaidBill(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}Merchant Name: " + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}ENEL Number:" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Meter ID: " + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}ENEO Transaction ID: " + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Customer Name: " + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:" + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Energy Const:" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Energetic Value:" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name: " + dataList.get(9) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(10) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Comments: " + dataList.get(11) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");
        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}Agent Sign:                 Subscriber Sign:{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_billPay(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.marchantNameReceipt)+ dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.billNumberReceipt)+ dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.CustomerIdReceipt) + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.customerNameReceipt) + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit)+ dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit)+ dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.agentBranceNameReceipt)+ dataList.get(9) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(10) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(11) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");
        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"     "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_reprint_billPay(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        //sb.append("{reset}{left}{b}OLD TRANSACTION ID: " + dataList.get(5) + "  " + "{br}");


        sb.append("{reset}{left}{b}"+ ctx.getResources().getString(R.string.oldtitle_new)+ dataList.get(0) + "{br}{br}{br}{br}");


        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.marchantNameReceipt)+ dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.billNumberReceipt)+ dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.CustomerIdReceipt) + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.customerNameReceipt) + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit)+ dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit)+ dataList.get(9) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.agentBranceNameReceipt)+ dataList.get(10) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}");

        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"     "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_billPay_footer(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(0) + "{br}{br}{br}{br}");    //  Agent Name
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(1) + "{br}{br}{br}{br}");    //  Agent Branch name
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentCountryRecepit)+ dataList.get(2) + "{br}{br}{br}{br}");   //  Agent Country
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview)+ dataList.get(3) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");  // comment


       /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"    "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");


        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean printPaidInvoices_CustomerID_BillPay(ArrayList<BillModel> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();
        for (int i=0;i<dataList.size();i++){
            sb.append("{reset}{left}{s}" + (i+1) + "{br}{br}");    // line count
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.prompt_InvoiceNo_code)+ dataList.get(i).getBillName() + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountRecepit)+ dataList.get(i).getTotalAmount() + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit) + dataList.get(i).getFeeAmount() + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(i).getAmount() + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.billDateTitleReview)+ dataList.get(i).getDueDate() + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.name) + dataList.get(i).getName() + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.customerNameReceipt)  + dataList.get(i).getCustomerName() + "{br}{br}{br}{br}");
            sb.append("{br}{br}{br}{br}{br}{br}{br}{br}");

        }



        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public void printMediBill(Bitmap bitmap) {

        try {


            StringBuffer sb = new StringBuffer();

            sb.append("{reset}{left}{b}Merchant Name: " + "Ms.Komal Medicose" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}Bill Number:" + "22001" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}Label Name: " + "D120112" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}Date/Time: " + "24/04/2017-03:15:23" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}Customer Number: " + "CC121" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{b}Customer Name: " + "Mr Shubham Sharma" + "{br}{br}{br}{br}{br}{br}{br}");

            sb.append("{reset}{left}{b}Drugs" + "    Quantity  " + "Rate/Amount" + "{br}{br}{br}");
            sb.append("{reset}{left}{s}DICLOFENAC SODIUM:" + "    16   - " + " 500" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}IBUPROFEN:" + "   12   - " + "  800" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}ROSUVASTATIN :" + "    30   - " + "  100" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}SALBUTAMOL :" + "    6   - " + "  170" + "{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}CANDESARTAN :" + "    10   - " + "  700" + "{br}{br}{br}{br}{br}{br}{br}{br}{br}{br}");

            sb.append("{reset}{left}{b}Total Amount:" + "   2270   INR" + "{br}{br}{br}{br}{br}{br}{br}{br}{br}{br}");
            sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}{br}");
            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
            mPrinter.setLineSpace(3);

           /*     int x = logoBitmap.getWidth();
                int y = logoBitmap.getHeight();
                int[] intArray = new int[x * y];
               logoBitmap.getPixels(intArray, 0, x, 0, 0, x, y);
                mPrinter.printImage(intArray, 40, 40, Printer.ALIGN_RIGHT, false);
                  mPrinter.flush();*/


            final int width1 = bitmap.getWidth();
            final int height1 = bitmap.getHeight();
            final int[] argb1 = new int[width1 * height1];
            bitmap.getPixels(argb1, 0, width1, 0, 0, width1, height1);
            //  logoBitmap.recycle();

            mPrinter.printImage(argb1, width1, height1, Printer.ALIGN_RIGHT, false);
            mPrinter.flush();
            StringBuffer sb1 = new StringBuffer();
            sb1.append("{reset}{left}    " + "{br}{br}");
            sb1.append("{reset}{left}    " + "{br}{br}");
            sb1.append("{reset}{left}    " + "{br}{br}");
            sb1.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            //  sb.append("{reset}{left}    " + "{br}{br}");
            print(sb1.toString(), Printer.ALIGN_LEFT);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean print_Receipt(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.marchantNameReceipt)+ dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.billNumberReceipt)+ dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.senderName) + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.amountNew2) + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.feesRecepit)+ dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.totalamountRecepit)+ dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit)+ dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentNameRecepit)+ dataList.get(9) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.agentBranceNameReceipt)+ dataList.get(10) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");
        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.agentSignRecepit)+"     "+ctx.getResources().getString(R.string.subscriberSignRecepit)+"{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_PrintbillPay(ArrayList<String> dataList) {

        boolean ret = false;


      /*  responseListForPrinter_Body.add(0, "Merchant Name"); // Marchant Name
        responseListForPrinter_Body.add(1, "Bill Number");      // Bill Number
        responseListForPrinter_Body.add(2, recieptData[7]);      // Label Name
        responseListForPrinter_Body.add(2, "Customer ID");      // Cutomer ID
        responseListForPrinter_Body.add(3, "Customer number");     // Customer number :
        responseListForPrinter_Body.add(4, recieptData[2]);   // Customer Name
        responseListForPrinter_Body.add(5, recieptData[5]);  // Amount
        responseListForPrinter_Body.add(6, recieptData[4]); // Fees
        responseListForPrinter_Body.add(7, "totalAmountString");      // Total Amount
        responseListForPrinter_Body.add(9, agentName); // Agent name
        responseListForPrinter_Body.add(10, recieptData[9]); // Agent Branch Name
        responseListForPrinter_Body.add(11, spinnerCountryString); // Agent Country*/

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Merchant Name: " + dataList.get(0) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Bill Number:" + dataList.get(1) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Label Name: " + dataList.get(2) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Customer ID: " + dataList.get(3) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Customer Number: " + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Customer Name: " + dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Amount:" + dataList.get(6) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Fees:" + dataList.get(7) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Total Amount:" + dataList.get(8) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(9) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Branch Name: " + dataList.get(10) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(11) + "{br}{br}{br}{br}{br}{br}{br}{br}{br}{br}");


        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
        return ret;
    }
    public void print_Header_billpayCustomerId(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}{br} {br}{br}\n  ");
        sb.append("{reset}{left}"+ctx.getResources().getString(R.string.dateRecepit)+"    "+ctx.getResources().getString(R.string.timeReceipt)+"       "+ctx.getResources().getString(R.string.transactionIdReceiptNew)+" {br}");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "          " + dataList.get(3) + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.marchantNameReceipt)+dataList.get(5) + "{br}{br}{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");


        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
