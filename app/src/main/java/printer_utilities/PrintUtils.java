package printer_utilities;

import android.content.Context;
import android.graphics.Bitmap;

import com.datecs.api.printer.Printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import agent.activities.R;
import callback.TaskCompleteToPrint;
import commonutilities.ComponentMd5SharedPre;
import model.BillModel;

public class PrintUtils {

    TaskCompleteToPrint taskCompleteToPrint;
    Printer mPrinter;
    Bitmap logoBitmap;
    Context ctx;
    String[] tempStringReport, tempStringAgentReport,feesTypetutionFee_array;
    ComponentMd5SharedPre mComponentInfo;

    public PrintUtils(TaskCompleteToPrint taskCompleteToPrint, Printer mPrinter, Bitmap logoBitmap, Context ctx) {

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
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
        sb.append("{reset}{left}" + ctx.getResources().getString(R.string.dateRecepit) + "     " + ctx.getResources().getString(R.string.timeReceipt) + "      " + ctx.getResources().getString(R.string.transactionIdReceiptNew) + " {br}");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "       " + dataList.get(3) + "{br}{br}");
        // sb.append("{reset}{left}    " + "{br}{br}");

        sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        // sb.append("{reset}{left}    " + "{br}{br}");
        //   sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        //  sb.append("{reset}{left}    " + "{br}{br}");
        try {
            if (dataList.size() > 5) {

                sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.oldTrasns) + dataList.get(5) + "  " + "{br}");
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
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
        sb.append("{reset}{left}Date:      Time:       Txn ID: {br}");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "         " + dataList.get(2) + "      " + dataList.get(3) + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}OLD TRANSACTION ID: " + dataList.get(5) + "  " + "{br}");


        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_Header_CashToCashSendMoneyReceiveMoney(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type
        try {


            StringBuffer sb = new StringBuffer();
            sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
            sb.append("{reset}{left}" + ctx.getResources().getString(R.string.dateRecepit) + "     " + ctx.getResources().getString(R.string.timeReceipt) + "      " + ctx.getResources().getString(R.string.transactionIdReceiptNew) + " {br}");
            sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "       " + dataList.get(3) + "{br}");

            sb.append("{reset}{left}    " + "{br}{br}");
            sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.cashToCashTitile_reprints) + "{br}{br}");

            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_Header_tutionfees(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type
        try {


            StringBuffer sb = new StringBuffer();
            sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
            sb.append("{reset}{left}" + ctx.getResources().getString(R.string.dateRecepit) + "     " + ctx.getResources().getString(R.string.timeReceipt) + "      " + ctx.getResources().getString(R.string.transactionIdReceiptNew) + " {br}");
            sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "       " + dataList.get(3) + "{br}");

            sb.append("{reset}{left}    " + "{br}{br}");
            sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.tution_fees_capital) + "{br}{br}");

            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_Header_receiveMoney(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type
        try {


            StringBuffer sb = new StringBuffer();
            sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
            sb.append("{reset}{left}" + ctx.getResources().getString(R.string.dateRecepit) + "     " + ctx.getResources().getString(R.string.timeReceipt) + "      " + ctx.getResources().getString(R.string.transactionIdReceiptNew) + " {br}");
            sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "       " + dataList.get(3) + "{br}");

            sb.append("{reset}{left}    " + "{br}{br}");
            sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.cashToCashTitile_receiveCash_reprints) + "{br}{br}");

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
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.dateRecepit) + "           " + ctx.getResources().getString(R.string.timeReceipt) + "{br}");
        sb.append("{reset}{left}{s}  " + dataList.get(1) + "              " + dataList.get(2) + "{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.reports) + "    : " + dataList.get(3) + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        // sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        //  sb.append("{reset}{left}    " + "{br}{br}");


        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_Header_Reports_commisionReport(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.dateRecepit) + "           " + ctx.getResources().getString(R.string.timeReceipt) + "{br}");
        sb.append("{reset}{left}{s}  " + dataList.get(1) + "              " + dataList.get(2) + "{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}{b}" + dataList.get(3) + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        // sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        //  sb.append("{reset}{left}    " + "{br}{br}");


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
            // sb.append("{reset}{left}    " + "{br}{br}");
            // sb.append("{reset}{left}    " + "{br}{br}");
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
    public void print_ReprintCreateAgent(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();

        // sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new) + dataList.get(0) + "  " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt) + dataList.get(1) + "  " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.presentAgentnameNew));
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_CreateAgent(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();

        // sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt) + dataList.get(0) + "  " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.presentAgentnameNew));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public void print_createAgentNew(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt) + dataList.get(0) + "  " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.presentAgentnameNew));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }
    public void print_tutionFees_part_1(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();




        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.schoolDetail) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.schoolCode) + "   " + dataList.get(0) + "{br}");
      /*  sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.region_small) + "   " + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.division) + "   " + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.sub_division) + "   " + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.city_cashtocash) + "   " + dataList.get(4) + "{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.schoolname_receiptpage) + "   " + dataList.get(1) + "{br}{br}");


        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.studentDetails) + "  " + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.registrationNumber) + " : " + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.name_receiptpage) + "  " + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.firstName_receiptpage) + "  " + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.gender_receiptpage) + "  " + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.birth_date) + "  " + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Email) + "  " + dataList.get(7) + "{br}{br}");

        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.payerdetails) + "  " + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.mobileNumber) + "  " + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.name_payer) + "  " + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.payerEmail) + "  " + dataList.get(10) + "{br}{br}");


        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.classDetails) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.level) + "  " + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.option) + "  " + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.classname_name_receiptpage) + "  " + dataList.get(13) + "{br}{br}");



        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.feesDetails) + "  " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_tutionFees_part_2(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();



        sb.append("{reset}{left}{b}{br}{br}" + ctx.getResources().getString(R.string.transactionDetails) + "  " + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionAmount) + "  " + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.fees) + "  " + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.vat) + "  " + dataList.get(2) + "{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamount_cashtocash) + "  " + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amount_in_letter) + "  " + dataList.get(4) + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + "  " + dataList.get(5) + "{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt_tutionfees) + "  " + dataList.get(6) + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "      ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public void print_receiveCash(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();



        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.reference_number_cashtocash) + "  " + dataList.get(0) + "  " + "{br}{br}");

        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.senderDetail) + "  " + "{br}{br}");



        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderfirstname_eui) + "   " + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.sendername_reprints) + "   " + dataList.get(2) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.sendernumber_reprints) + " : " + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderCountry_receiptss) + "  " + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + "  " + dataList.get(5) + "{br}{br}");


        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.recipient_details_cashtocah) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderfirstname_eui) + "  " + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + "  " + dataList.get(7) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + "  " + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationidproof_eui_recieve) + "  " + dataList.get(9) + "  " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationidproof_issue_Recipient) + "   " + dataList.get(10) + "{br}{br}");

        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.amounts_details_title) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destination_currencyType_reprint) + "  " + dataList.get(11) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + "  " + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + "  " + dataList.get(13) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.tax_reprints) + "  " + dataList.get(14) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.otherTax_reprints) + "  " + dataList.get(15) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountpay_reprints) + "  " + dataList.get(16) + "{br}{br}");
      //  sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountTopay_reprints) + "  " + dataList.get(16) + "{br}{br}");


        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.other_details_title) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.test_question_reprints) + "  " + dataList.get(17) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.test_answer_reprints) + "  " + dataList.get(18) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + "   " + dataList.get(19) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + "   " + dataList.get(20) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + "   " + dataList.get(21) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_sendCash(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();




        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.reference_number_cashtocash) + "  " + dataList.get(0) + "  " + "{br}{br}");


        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.senderDetail) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderfirstname_eui) + "   " + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.sendername_reprints) + "   " + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.sendernumber_reprints) + " : " + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderCountry_receiptss) + "  " + dataList.get(4) + "{br}");  // ID document - Issuing Country
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderidproof_reciept_title) + "  " + dataList.get(5) + "  " + "{br}");   // ID document type:
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderidproof_issueDate_reciept_title) + "   " + dataList.get(6) + "{br}{br}"); //ID document issue date:




        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.destination_details_title) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + "  " + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderfirstname_eui) + "  " + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + "  " + dataList.get(9) + "{br}");



        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + "  " + dataList.get(10) + "{br}{br}");

        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.amounts_details_title) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.sender_currencyType_reprints) + "  " + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountToSend_new) + "  " + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + "  " + dataList.get(13) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.tax_new) + "  " + dataList.get(14) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.otherTax_reprints) + "  " + dataList.get(15) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Tax_one) + " 0 " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Tax_Two) + " 0 " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Tax_Three) + " 0 " + "{br}");

             sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountsend_eui) + "  " + dataList.get(16) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destination_durrency_type) + "  " + dataList.get(17) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountTopay_reprints) + "  " + dataList.get(18) + "{br}{br}");

        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.other_details_title) + "  " + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.test_question_reprints) + "  " + dataList.get(19) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.test_answer_reprints) + "  " + dataList.get(20) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + "   " + dataList.get(21) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + "   " + dataList.get(22) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + "   " + dataList.get(23) + "{br}");



        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "      ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public void print_updateAccountNew(ArrayList<String> dataList) {

        System.out.print(dataList);

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt) + dataList.get(0) + "  " + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.presentAgentnameNew));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

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
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}Destination Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}Currency Type:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}Total Amount Receive:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:                 ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}");


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
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountReceive));
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview));
        sb.append("{reset}{left}{s}" + dataList.get(10) + "{br}");


      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "      ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
        return ret;
    }

    public boolean print_ReprintReceiveMoneyToMobile(ArrayList<String> dataList) {
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
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new));
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountReceive));
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));
        sb.append("{reset}{left}{s}" + dataList.get(10) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview));
        sb.append("{reset}{left}{s}" + dataList.get(11) + "{br}");


      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "      ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
        return ret;
    }

    public boolean print_ReceiveMoney_transactionApproval(ArrayList<String> dataList) {
        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(2) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(4) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(5) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(6) + "{br}"); // totalamountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(7) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(9) + "{br}");


            /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

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
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}Agent Sign:                Subscriber Sign:{br}{br}");


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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_ReprintSendMoneyToMobile(ArrayList<String> dataList) {


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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(2) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(8) + "{br}");
        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(11) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public boolean print_feesType_tutionFees(ArrayList<String> dataList) {
        boolean ret = false;


        Collections.reverse(dataList);

        //next, convert the list back to String array
     //   feesTypetutionFee_array = (String[]) dataList.toArray();



        StringBuffer sb = new StringBuffer();
        try {
            for (int k = 0; k < dataList.size(); k++) {
                feesTypetutionFee_array = dataList.get(k).split("\\|");   // dateReport   source  feesRecepit agentTransId

                sb.append("{reset}{left}{s}"+ feesTypetutionFee_array[0] +"    "+feesTypetutionFee_array[1] +"{br}");

            }


            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
             return ret;
    }

    public boolean reprint_tutionFees_sh(ArrayList<String> dataList) {
        boolean ret = false;


        Collections.reverse(dataList);

        //next, convert the list back to String array
        //   feesTypetutionFee_array = (String[]) dataList.toArray();



        StringBuffer sb = new StringBuffer();
        try {

            /*
            for (int k = 0; k < dataList.size(); k++) {

            //    sb.append("{reset}{left}{s}"+ feesTypetutionFee_array[0] +"    "+feesTypetutionFee_array[1] +"{br}");
                sb.append("{reset}{left}{s}"+ dataList +"{br}");

            }

             */

           String str =  dataList.toString();
            str = str.substring(1,  dataList.toString().length() - 1);

             String temdata[] = str.split("\\,");

            for (int k = 0; k < temdata.length; k++) {
               // sb.append("{reset}{left}{s}"+ temdata.toString() +"{br}");

                sb.append("{reset}{left}{s}"+ temdata[k] +"{br}");

            }


            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public boolean print_reportHistory_agentBankReportReport(ArrayList<String> dataList) {
        boolean ret = false;


        // Commission sur Depot en compte 6654234 Mtt: 700 / Fr: 100|6.0|237000271099|24/11/2017 15:52:53


        StringBuffer sb = new StringBuffer();
        try {
            for (int k = 0; k < dataList.size(); k++) {
                tempStringAgentReport = dataList.get(k).split("\\|");   // dateReport   source  feesRecepit agentTransId


              /*  textView_transactionId.setText(data[6]);         //transaction ID
                textView_transaction_amount.setText(data[3]);    // Transaction amount
                textView_date.setText(data[1]);                  //Transaction Date
                textView_transactionType.setText(data[2]);            // Transaction Type
                textView_debit.setText(data[4]);                      // Debit
                textView_credit.setText(data[5]);                     // Credit
                textView_closing.setText(data[0]);                 // Closing

    */


                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionId) + tempStringAgentReport[6] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.dateReport) + tempStringAgentReport[1] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountNew) + tempStringAgentReport[3] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactiontype) + tempStringAgentReport[2] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.debit_amount) + tempStringAgentReport[4] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.credit_amount) + tempStringAgentReport[5] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.closing_amount) + tempStringAgentReport[0] + "{br}{br}");


            }


            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
        } catch (Exception e) {
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

    public void print_Header_Reports_agentBank(ArrayList<String> dataList) {
        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type

        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.dateRecepit) + "           " + ctx.getResources().getString(R.string.timeReceipt) + "{br}");
        sb.append("{reset}{left}{s}  " + dataList.get(1) + "              " + dataList.get(2) + "{br}");

        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}{b}" + dataList.get(3) + "{br}{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        // sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        //  sb.append("{reset}{left}    " + "{br}{br}");


        try {
            mPrinter.reset();
            mPrinter.setLineSpace(3);
            mPrinter.printTaggedText(sb.toString());
            mPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean print_imoney_withdrwal(ArrayList<String> dataList) {
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
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.mobileNumberReviewPage));  // fd account

        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country

        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount                                   // Amount

        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt));      // Agent Branch
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");

      /*  sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);


//();

        return ret;
    }


    public void print_cashToCashCancel(ArrayList<String> dataList) {

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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.referenceNumberReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameNewReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountWord) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesReceipt) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public boolean print_imoney_deposit(ArrayList<String> dataList) {
        boolean ret = false;


        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.mobileNumberReviewPage));  // fd account name

        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");
        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount
        // Amount
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees

        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name

        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));  // Attach Branch Name

        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");

      /*  sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);


//();

        return ret;
    }


    public void print_cashToCashSameCountry(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.referenceNumberReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(3) + "{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(9) + "{br}");
        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(13) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        sb.append("{reset}{left}    " + "{br}");
       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}");*/

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_ReprintcashToCashSameCountry(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.referenceNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(4) + "{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(10) + "{br}");
        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(13) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(14) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        sb.append("{reset}{left}    " + "{br}");
       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}");*/

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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.referenceNumberReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameNewReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountWord) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(11) + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_reprint_cashToCashReceiveMoneySameCountry(ArrayList<String> dataList) {

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


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.referenceNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameNewReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(3) + "{br}");
        // sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}"); //  // remove 02082017
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(7) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountWord) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public void print_SendMoneyToMobile_diffrentCountryHide(ArrayList<String> dataList) {


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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}"); //
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(8) + "{br}");
        // sb.append("{reset}{left}{s}"+ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_SendMoneyToMobile_diffrentCountryDisplayConfcode(ArrayList<String> dataList) {


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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}"); //
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit_SendMoneyToMobile) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(13) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_SendMoney_transactionApproval_confcodeHide(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}"); // totalamountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public void print_SendMoney_transactionApproval_confcodeDisplay(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}"); // totalamountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);
    }


    public void print_SendMoney_transactionApproval(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationCountryRecepit) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNumberReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.currencyTypeReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}"); // totalamountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


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
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}Subscriber Name:");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}Attach Branch Name:");
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}City:");
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}Country:");
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}Amount:");
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}Fees:");
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}Agent Name:");
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}Agent Country:");
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");

       /* sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public void print_SendMoneyToName(ArrayList<String> dataList) {


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}Sender Name: " + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}Sender Number: " + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}Destination Country: " + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}Destination Name: " + dataList.get(3) + "{br}");
        // sb.append("{reset}{left}{s}Destination Number: " + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}Currency Type: " + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}Amount: " + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}Fees: " + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}Total Amount: " + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}Transaction Code: " + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}Agent Name: " + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}Agent Branch Name:" + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}Agent Country: " + dataList.get(11) + "{br}{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);
    }

    public boolean print_ReprintCashIn(ArrayList<String> dataList) {
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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new));
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt));  // subscriber Number
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt));  // Subscriber name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":");     //VilleReceiptNew
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount                                   // Amount
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}");

      /*  sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);


//();

        return ret;
    }

    public boolean print_Demo_Map(HashMap<String, String> map) {
        boolean ret = false;

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + "Sender mobile no " + " : ");
        sb.append("{reset}{left}{s}" + map.get("senderMobileNo") + "{br}");

        sb.append("{reset}{left}{s}" + "Sender name  " + " : ");
        sb.append("{reset}{left}{s}" + map.get("senderName") + "{br}");

        sb.append("{reset}{left}{s}" + "Receiver mobile no  " + " : ");
        sb.append("{reset}{left}{s}" + map.get("reciverMobileNo") + "{br}");

        sb.append("{reset}{left}{s}" + "Amount  " + " : ");
        sb.append("{reset}{left}{s}" + map.get("amount") + "{br}");

        sb.append("{reset}{left}{s}" + "Receiver name " + " : ");
        sb.append("{reset}{left}{s}" + map.get("reciverName") + "{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}" + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);


//();

        return ret;
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
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt));  // Subscriber name
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":");     //VilleReceiptNew
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount                                   // Amount
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsCashIn));           // commentsCashIn
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}");

      /*  sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");
        sb.append("{reset}{left}    " + "{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);


//();

        return ret;
    }

    public boolean print_petrol(ArrayList<String> dataList) {
        boolean ret = false;

        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}{b}" + "   Auth.Dealer : Bharat Petroleum Corp. ltd." + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + " Tigaon Road,Sec-87,Vill Baselwa,Near Modern DSP" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "                   Faridabad - 121002" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "               VAT TIN:43545850" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "               TEL : 0129-6450172" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{b}" + "             ORIGINAL" + "{br}{br}");   // Bold
        sb.append("{reset}{left}{s}" + "          Receipt ID      :  259865" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Date            :  2018/03/22" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Time            :  20:14:06" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          TRX. ID         :  259867" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          TRX. Type       :  Cash" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Pump No.        :  2" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Nozzle No.      :  3 " + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Product         :  HSD " + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Price (RS)      :  69.20" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Quantity        :  32 Ltr " + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Total Sale (RS) :  2214.04" + "{br}{br}{br}{br}");
        sb.append("{reset}{left}{s}" + "          Vehicle No      :  2258" + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

        sb.append("{reset}{left}{s}" + "              Thanks & Visit Again  " + "{br}{br}");
        sb.append("{reset}{left}{s}" + "              Save fuel,Save Money  " + "{br}{br}{br}{br}{br}{br}{br}{br}{br}");

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


        StringBuffer sb = new StringBuffer();
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt)); // subscriber number
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt));  // Subscriber name
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":");     //City
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}");

      /*  sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "         ");   // agentSignRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //subscriberSignRecepit


        print(sb.toString(), Printer.ALIGN_LEFT);


        return ret;
    }


    public boolean print_reportHistory_feesAddTotal_list(ArrayList<String> dataList) {

        boolean ret = false;

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}{b}" + ctx.getResources().getString(R.string.total) + "       " + dataList.get(0) + "{br}{br}");

        print(sb.toString(), Printer.ALIGN_LEFT);

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


                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionIdReceipt) + temp[0] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountNew2) + " " + temp[1] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationReports) + temp[3] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.source) + " : " + temp[2] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transType) + " : " + temp[5] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.dateReport) + temp[4] + "{br}{br}");

            }
            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
        } catch (Exception e) {
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

    public boolean print_reportHistory_commisionReport(ArrayList<String> dataList) {
        boolean ret = false;


        // Commission sur Depot en compte 6654234 Mtt: 700 / Fr: 100|6.0|237000271099|24/11/2017 15:52:53


        StringBuffer sb = new StringBuffer();
        try {
            for (int k = 0; k < dataList.size(); k++) {
                tempStringReport = dataList.get(k).split("\\|");   // dateReport   source  feesRecepit agentTransId


                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.dateReport) + tempStringReport[3] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.source) + " : " + tempStringReport[2] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commissions) + " : " + tempStringReport[1] + "{br}");
                sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentTransId) + " : " + tempStringReport[0] + "{br}{br}");

            }


            print(sb.toString(), Printer.ALIGN_LEFT);
            mPrinter.reset();
        } catch (Exception e) {
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

    public boolean print_reprint_cashOut(ArrayList<String> dataList) {

        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new)); // old
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt)); // subscriber number
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt)); // subscriber number
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":");     //City
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(9) + "{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "   ");      // Agent Sign    ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //Subscriber Sign


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_cashOutSameBranch(ArrayList<String> dataList) {

        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt)); // subscriber number
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberNumberReceipt)); // subscriber number
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":");     //City
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}Agent Sign:   ");
        sb.append("{reset}{left}{s}Subscriber Sign:{br}{br}");


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
        sb.append("{reset}{left}{s}" + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscribeNameReceipt));  // Subscriber name
        sb.append("{reset}{left}{s}" + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt));  // Attach Branch Name
        sb.append("{reset}{left}{s}" + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.VilleReceiptNew) + ":");     //City
        sb.append("{reset}{left}{s}" + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.Country) + ":");        //  Country
        sb.append("{reset}{left}{s}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit));    // Amount
        sb.append("{reset}{left}{s}" + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit));           // fees
        sb.append("{reset}{left}{s}" + dataList.get(6) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsCashIn));     // comments
        sb.append("{reset}{left}{s}" + dataList.get(7) + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit));     // Agent name
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");



        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit));      // Agent Country
        sb.append("{reset}{left}{s}" + dataList.get(8) + "{br}");

       /* sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "   ");      // Agent Sign    ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //Subscriber Sign

        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_approval_cashToM(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


       /* responseListForPrinter_Body_CashtoM_transactionApproval.add(0, recieptData[17]);   // Merchnat Name
        responseListForPrinter_Body_CashtoM_transactionApproval.add(1, "");               // Bill Number  tag is not coming From Server number
        responseListForPrinter_Body_CashtoM_transactionApproval.add(2, recieptData[18]);  // label name
        responseListForPrinter_Body_CashtoM_transactionApproval.add(3, recieptData[15]); // Destination Name
        responseListForPrinter_Body_CashtoM_transactionApproval.add(4, recieptData[11]); // Sender name
        responseListForPrinter_Body_CashtoM_transactionApproval.add(5, recieptData[7]);        // Amount
        responseListForPrinter_Body_CashtoM_transactionApproval.add(6, recieptData[6]);    // fees
        responseListForPrinter_Body_CashtoM_transactionApproval.add(7, totalAmountString);  //  Total Amount
        responseListForPrinter_Body_CashtoM_transactionApproval.add(8, recieptData[14]);   //  transaction Code
        responseListForPrinter_Body_CashtoM_transactionApproval.add(9,  agentName);           // Agent Name
        responseListForPrinter_Body_CashtoM_transactionApproval.add(10, recieptData[10]);     // Agent Branch NAme
        responseListForPrinter_Body_CashtoM_transactionApproval.add(11, recieptData[4]); // Agent Country
        responseListForPrinter_Body_CashtoM_transactionApproval.add(12, recieptData[2]); // Coment
       */

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(4) + "{br}");  // senderNameReceipt

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(7) + "{br}"); // totalamountRecepit


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(10) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}"); // commentsTitleReview


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "   ");      // Agent Sign    ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //Subscriber Sign


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_cancelReceipt_BillPay(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(4) + "{br}");  // senderNameReceipt

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(7) + "{br}"); // totalamountRecepit


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(10) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}"); // commentsTitleReview


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "   ");      // Agent Sign    ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //Subscriber Sign


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_cancelReceipt_cashToMerchant(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(4) + "{br}");  // senderNameReceipt

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(7) + "{br}"); // totalamountRecepit


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(10) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}{br}"); // commentsTitleReview


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "   ");      // Agent Sign    ");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");   //Subscriber Sign


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_reprint_CashToMerchant(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(1) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(2) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(3) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(4) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(5) + "{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}"); // totalamountRecepit

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(11) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(13) + "{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_CashToMerchant(ArrayList<String> dataList) {


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


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(4) + "{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(7) + "{br}"); // totalamountRecepit

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_CashToMerchant_Mer(ArrayList<String> dataList) {


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


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(4) + "{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");   // amountRecepit

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(6) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(7) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(8) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(9) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(10) + "{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_CashToMerchant_Sub(ArrayList<String> dataList) {


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


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");   // marchantNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");    // billNumberReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");    // labelNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(3) + "{br}");  // destinationNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderNameReceipt) + dataList.get(4) + "{br}");  // senderNameReceipt


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");   // amountRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");  // feesRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(7) + "{br}"); // totalamountRecepit

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(8) + "{br}");  // transactionCodeRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}"); //agentNameRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}"); // attachBranceNameReceipt
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");  //  agentCountryRecepit
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}"); // commentsTitleReview


        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_orderTransferApproval(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.orderFrom) + " " + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.orderto) + " " + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + " " + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(3) + " " + "{br}");
        //  sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(4) + " " + "{br}");
        // sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(4) + "{br}");


        // enelNumberReceipt

        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_orderTransfer(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.benificeAccountNumber) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(2) + "{br}");
        //  sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(4) + "{br}");


        // enelNumberReceipt

        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_prepaidBill(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.enelNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.meterIDReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.enoTransactionNumberReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerName) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.EnergyConst) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.energeticValue) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(11) + "{br}");


        // enelNumberReceipt

        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }


    public boolean print_billPay(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.CustomerIdReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerNameReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(11) + "{br}");
        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_reprint_billPay(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        //sb.append("{reset}{left}{b}OLD TRANSACTION ID: " + dataList.get(5) + "  " + "{br}");


        //sb.append("{reset}{left}{b}"+ ctx.getResources().getString(R.string.oldtitle_reprint)+ dataList.get(0) + "{br}");

        sb.append("{reset}{left}{b}" + ctx.getResources().getString(R.string.oldTrasns) + dataList.get(0) + "  " + "{br}");


        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.CustomerIdReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerNameReceipt) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(12) + "{br}");

        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

        //  sb.append("{reset}{left}    " + "{br}{br}");
        //  sb.append("{reset}{left}    " + "{br}{br}");
        //  sb.append("{reset}{left}    " + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_billPay_footer(ArrayList<String> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(0) + "{br}");    //  Agent Name
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.attachBranceNameReceipt) + dataList.get(1) + "{br}");    //  Agent Branch name
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(2) + "{br}");   //  Agent Country
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(3) + "{br}");  // comment


       /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "    " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean printPaidInvoices_CustomerID_BillPay(ArrayList<BillModel> dataList) {


        boolean ret = false;


        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataList.size(); i++) {
            sb.append("{reset}{left}{s}" + (i + 1) + "{br}");    // line count
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.prompt_InvoiceNo_code) + dataList.get(i).getBillName() + "{br}");
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountRecepit) + dataList.get(i).getTotalAmount() + "{br}");
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(i).getFeeAmount() + "{br}");
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(i).getAmount() + "{br}");
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billDateTitleReview) + dataList.get(i).getDueDate() + "{br}");
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.name) + dataList.get(i).getName() + "{br}");
            sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerNameReceipt) + dataList.get(i).getCustomerName() + "{br}{br}");

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

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.oldTransaction_new) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(2) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.destinationNameReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.senderName) + dataList.get(5) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountNew2) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.transactionCodeRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(11) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(12) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.commentsTitleReview) + dataList.get(13) + "{br}{br}");
        /*sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");


        print(sb.toString(), Printer.ALIGN_LEFT);

        return ret;
    }

    public boolean print_PrintbillPay(ArrayList<String> dataList) {

        boolean ret = false;

        StringBuffer sb = new StringBuffer();

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(0) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.billNumberReceipt) + dataList.get(1) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.labelNameReceipt) + dataList.get(2) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerIdTitleReviewPage) + dataList.get(3) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerNumbReceipt) + dataList.get(4) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.customerNameReceipt) + dataList.get(5) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.amountNew2) + dataList.get(6) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.feesRecepit) + dataList.get(7) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.totalamountRecepit) + dataList.get(8) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentNameRecepit) + dataList.get(9) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentBranceNameReceipt) + dataList.get(10) + "{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentCountryRecepit) + dataList.get(11) + "{br}");

        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.agentSignRecepit) + "     " + ctx.getResources().getString(R.string.subscriberSignRecepit) + "{br}{br}");

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
        sb.append("{reset}{center}{b} " + dataList.get(0) + " {br}\n  ");
        sb.append("{reset}{left}" + ctx.getResources().getString(R.string.dateRecepit) + "    " + ctx.getResources().getString(R.string.timeReceipt) + "       " + ctx.getResources().getString(R.string.transactionIdReceiptNew) + " {br}");
        sb.append("{reset}{left}{s}" + dataList.get(1) + "       " + dataList.get(2) + "          " + dataList.get(3) + "{br}");

        /*sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}");
        sb.append("{reset}{left}    " + "{br}{br}");*/
        sb.append("{reset}{left}{b}" + dataList.get(4) + "{br}{br}");
        sb.append("{reset}{left}{s}" + ctx.getResources().getString(R.string.marchantNameReceipt) + dataList.get(5) + "{br}");


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
