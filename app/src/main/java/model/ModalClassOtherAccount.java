package model;

/**
 * Created by sharique on 12-Jun-17.
 */

public class ModalClassOtherAccount {

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobileBankingService() {
        return mobileBankingService;
    }

    public void setMobileBankingService(String mobileBankingService) {
        this.mobileBankingService = mobileBankingService;
    }

    String accountNumber="",action="",accountType="",title="",mobileBankingService;
}

