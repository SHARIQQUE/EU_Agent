package model;

/**
 * Created by shariqueanwar on 06/12/18.
 */

public class OtherAccountModel {


    String accountTypeCode ="";
    String accountNumber ="";
    String accounttitle ="";
    String acctypedescription ="";
    String accountstatusdescription ="";
    String mobilebankingservice ="";

    public String getAccounttitle() {
        return accounttitle;
    }

    public void setAccounttitle(String accounttitle) {
        this.accounttitle = accounttitle;
    }

    public String getAcctypedescription() {
        return acctypedescription;
    }

    public void setAcctypedescription(String acctypedescription) {
        this.acctypedescription = acctypedescription;
    }

    public String getAccountstatusdescription() {
        return accountstatusdescription;
    }

    public void setAccountstatusdescription(String accountstatusdescription) {
        this.accountstatusdescription = accountstatusdescription;
    }

    public String getMobilebankingservice() {
        return mobilebankingservice;
    }

    public void setMobilebankingservice(String mobilebankingservice) {
        this.mobilebankingservice = mobilebankingservice;
    }



    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status ="";
}
