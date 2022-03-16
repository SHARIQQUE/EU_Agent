package agent.purchase_billpayment_deposit_menu.electricity_water.electricity;

public class ModalClassStringObject {

    String billNumber = null;
    String billDate = null;
    String billDueDate = null;
    String amountBillpay = null;
    boolean selected = false;



    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(String billDueDate) {
        this.billDueDate = billDueDate;
    }

    public String getAmountBillpay() {
        return amountBillpay;
    }

    public void setAmountBillpay(String amountBillpay) {
        this.amountBillpay = amountBillpay;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
