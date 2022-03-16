package agent.eui.sendmoney_cashtocash_fragment;



public class ModalSendMoney {

    static String senderMobileNumber = "";


    static String senderMobileNoCodePrefix = "";

    static String amountString = "";
    static String currencySenderCode = "";
    static String destinationMobileNumber = "";
    static String currencyDestinationCode = "";
    static String countrySenderCode = "";
    static String countryDestinationCode = "";
    static String reasonOfTransfer = "";
    static String reasonOfTransferCode = "";
    static String countrySenderName = "";
    static String countryDestinationName = "";
    static String fees_fromServer = "";
    static String vat_fromserver = "";
    static String otherTax = "";
    static String tax_fromServer = "";
    static String totalAmount = "";
    static String amountToPay_fromserver = "";
    static String idProofPlaceOfIssue = "";
    static String idDocumnetDateOfIssue = "";
    static String IdDocumentCountryOfIssue = "";
    static String idDocumentCountryOfIssue_code = "";
    static String city = "";
    static String fixHomePhoneNumber = "";


    static String preFixCountryHomePhoneNumber = "";

    static String address1 = "";
    static String address2 = "";
    static String firstNameSender = "";
    static String nameSender = "";
    static String emailSender = "";
    static String firstNameReceiver = "";
    static String nameReceiver = "";
    static String emailReceiver = "";
    static String genderType = "";
    static String genderType_code = "";
    static String nationality = "";
    static String question_name = "";
    static String question_code = "";
    static String answer_name = "";
    static String answer_code = "";
    static String idDocumentNumber = "";
    static String profession = "";
    static String resident = "";
    static String idDocumnetType = "";
    static String idDocumnetType_code = "";
    static  String countryName;
    static  String countryCode;
    static  String dateTime_reprint;
    static  String transactionid_reprint;
    static  String rate_calculation;
    static  String amount_fromServer;
    static  String referenceNumber_print;
    static  String attachedBranchName;
    static  String comments;


    public static String getPreFixCountryHomePhoneNumber() {
        return preFixCountryHomePhoneNumber;
    }

    public static void setPreFixCountryHomePhoneNumber(String preFixCountryHomePhoneNumber) {
        ModalSendMoney.preFixCountryHomePhoneNumber = preFixCountryHomePhoneNumber;
    }




    public static String getDisplayFees() {
        return displayFees;
    }

    public static void setDisplayFees(String displayFees) {
        ModalSendMoney.displayFees = displayFees;
    }

    public static String getDisplayVAT() {
        return displayVAT;
    }

    public static void setDisplayVAT(String displayVAT) {
        ModalSendMoney.displayVAT = displayVAT;
    }

    static  String residentCode;
    static  String displayFees;
    static  String displayVAT;

    public static String getVATinPercentage() {
        return VATinPercentage;
    }

    public static void setVATinPercentage(String VATinPercentage) {
        ModalSendMoney.VATinPercentage = VATinPercentage;
    }

    static  String VATinPercentage;

    static  String firstName_sender_agentidentity;

    public static String getFirstName_sender_agentidentity() {
        return firstName_sender_agentidentity;
    }

    public static String getSenderMobileNoCodePrefix() {
        return senderMobileNoCodePrefix;
    }

    public static void setSenderMobileNoCodePrefix(String senderMobileNoCodePrefix) {
        ModalSendMoney.senderMobileNoCodePrefix = senderMobileNoCodePrefix;
    }
    public static void setFirstName_sender_agentidentity(String firstName_sender_agentidentity) {
        ModalSendMoney.firstName_sender_agentidentity = firstName_sender_agentidentity;
    }

    public static String getResidentCode() {
        return residentCode;
    }

    public static void setResidentCode(String residentCode) {
        ModalSendMoney.residentCode = residentCode;
    }

    public static String getComments() {
        return comments;
    }

    public static void setComments(String comments) {
        ModalSendMoney.comments = comments;
    }

    public static String getAttachedBranchName() {
        return attachedBranchName;
    }

    public static void setAttachedBranchName(String attachedBranchName) {
        ModalSendMoney.attachedBranchName = attachedBranchName;
    }

    public static String getReferenceNumber_print() {
        return referenceNumber_print;
    }

    public static void setReferenceNumber_print(String referenceNumber_print) {
        ModalSendMoney.referenceNumber_print = referenceNumber_print;
    }

    public static String getQuestion_name() {
        return question_name;
    }

    public static void setQuestion_name(String question_name) {
        ModalSendMoney.question_name = question_name;
    }

    public static String getAnswer_name() {
        return answer_name;
    }

    public static void setAnswer_name(String answer_name) {
        ModalSendMoney.answer_name = answer_name;
    }

    public static String getAnswer_code() {
        return answer_code;
    }

    public static void setAnswer_code(String answer_code) {
        ModalSendMoney.answer_code = answer_code;
    }

    public static String getQuestion_code() {
        return question_code;
    }

    public static void setQuestion_code(String question_code) {
        ModalSendMoney.question_code = question_code;
    }

    public static String getTax_fromServer() {
        return tax_fromServer;
    }

    public static void setTax_fromServer(String tax_fromServer) {
        ModalSendMoney.tax_fromServer = tax_fromServer;
    }

    public static String getRate_calculation() {
        return rate_calculation;
    }

    public static String getFees_fromServer() {
        return fees_fromServer;
    }

    public static void setFees_fromServer(String fees_fromServer) {
        ModalSendMoney.fees_fromServer = fees_fromServer;
    }

    public static void setRate_calculation(String rate_calculation) {
        ModalSendMoney.rate_calculation = rate_calculation;
    }

    public static String getVat_fromserver() {
        return vat_fromserver;
    }

    public static void setVat_fromserver(String vat_fromserver) {
        ModalSendMoney.vat_fromserver = vat_fromserver;
    }

    public static String getAmount_fromServer() {
        return amount_fromServer;
    }

    public static void setAmount_fromServer(String amount_fromServer) {
        ModalSendMoney.amount_fromServer = amount_fromServer;
    }

    public static String getAmountToPay_fromserver() {
        return amountToPay_fromserver;
    }

    public static void setAmountToPay_fromserver(String amountToPay_fromserver) {
        ModalSendMoney.amountToPay_fromserver = amountToPay_fromserver;
    }
/* static  String countryCode_sender_getCurrency;
    static  String countryCode_receiver_getCurrency;


    public static String getCountryCode_sender_getCurrency() {
        return countryCode_sender_getCurrency;
    }

    public static void setCountryCode_sender_getCurrency(String countryCode_sender_getCurrency) {
        ModalSendMoney.countryCode_sender_getCurrency = countryCode_sender_getCurrency;
    }

    public static String getCountryCode_receiver_getCurrency() {
        return countryCode_receiver_getCurrency;
    }

    public static void setCountryCode_receiver_getCurrency(String countryCode_receiver_getCurrency) {
        ModalSendMoney.countryCode_receiver_getCurrency = countryCode_receiver_getCurrency;
    }*/



    public static String getTransactionid_reprint() {
        return transactionid_reprint;
    }

    public static void setTransactionid_reprint(String transactionid_reprint) {
        ModalSendMoney.transactionid_reprint = transactionid_reprint;
    }

    public static String getDateTime_reprint() {
        return dateTime_reprint;
    }

    public static void setDateTime_reprint(String dateTime_reprint) {
        ModalSendMoney.dateTime_reprint = dateTime_reprint;
    }

    public static String getGenderType_code() {
        return genderType_code;
    }

    public static void setGenderType_code(String genderType_code) {
        ModalSendMoney.genderType_code = genderType_code;
    }


    public static String getCountryName() {
        return countryName;
    }

    public static void setCountryName(String countryName) {
        ModalSendMoney.countryName = countryName;
    }

    public static String getCountryCode() {
        return countryCode;
    }

    public static void setCountryCode(String countryCode) {
        ModalSendMoney.countryCode = countryCode;
    }

    public static String getIdDocumnetType_code() {
        return idDocumnetType_code;
    }

    public static void setIdDocumnetType_code(String idDocumnetType_code) {
        ModalSendMoney.idDocumnetType_code = idDocumnetType_code;
    }

    public static String getIdDocumentCountryOfIssue_code() {
        return idDocumentCountryOfIssue_code;
    }

    public static void setIdDocumentCountryOfIssue_code(String idDocumentCountryOfIssue_code) {
        ModalSendMoney.idDocumentCountryOfIssue_code = idDocumentCountryOfIssue_code;
    }

    public static String getReasonOfTransferCode() {
        return reasonOfTransferCode;
    }

    public static void setReasonOfTransferCode(String reasonOfTransferCode) {
        ModalSendMoney.reasonOfTransferCode = reasonOfTransferCode;
    }

    public static String getIdDocumnetType() {
        return idDocumnetType;
    }

    public static void setIdDocumnetType(String idDocumnetType) {
        ModalSendMoney.idDocumnetType = idDocumnetType;
    }

    public static String getResident() {
        return resident;
    }

    public static void setResident(String resident) {
        ModalSendMoney.resident = resident;
    }

    public static String getProfession() {
        return profession;
    }

    public static void setProfession(String profession) {
        ModalSendMoney.profession = profession;
    }

    public static String getIdDocumentNumber() {
        return idDocumentNumber;
    }

    public static void setIdDocumentNumber(String idDocumentNumber) {
        ModalSendMoney.idDocumentNumber = idDocumentNumber;
    }



    public static String getNationality() {
        return nationality;
    }

    public static void setNationality(String nationality) {
        ModalSendMoney.nationality = nationality;
    }

    public static String getGenderType() {
        return genderType;
    }

    public static void setGenderType(String genderType) {
        ModalSendMoney.genderType = genderType;
    }

    public static String getDestinationMobileNumber() {
        return destinationMobileNumber;
    }

    public static void setDestinationMobileNumber(String destinationMobileNumber) {
        ModalSendMoney.destinationMobileNumber = destinationMobileNumber;
    }

    public static String getIdProofPlaceOfIssue() {
        return idProofPlaceOfIssue;
    }

    public static void setIdProofPlaceOfIssue(String idProofPlaceOfIssue) {
        ModalSendMoney.idProofPlaceOfIssue = idProofPlaceOfIssue;
    }

    public static String getIdDocumnetDateOfIssue() {
        return idDocumnetDateOfIssue;
    }

    public static void setIdDocumnetDateOfIssue(String idDocumnetDateOfIssue) {
        ModalSendMoney.idDocumnetDateOfIssue = idDocumnetDateOfIssue;
    }

    public static String getIdDocumentCountryOfIssue() {
        return IdDocumentCountryOfIssue;
    }

    public static void setIdDocumentCountryOfIssue(String idDocumentCountryOfIssue) {
        IdDocumentCountryOfIssue = idDocumentCountryOfIssue;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        ModalSendMoney.city = city;
    }

    public static String getFixHomePhoneNumber() {
        return fixHomePhoneNumber;
    }

    public static void setFixHomePhoneNumber(String fixHomePhoneNumber) {
        ModalSendMoney.fixHomePhoneNumber = fixHomePhoneNumber;
    }

    public static String getAddress1() {
        return address1;
    }

    public static void setAddress1(String address1) {
        ModalSendMoney.address1 = address1;
    }

    public static String getAddress2() {
        return address2;
    }

    public static void setAddress2(String address2) {
        ModalSendMoney.address2 = address2;
    }

    public static String getFirstNameSender() {
        return firstNameSender;
    }

    public static void setFirstNameSender(String firstNameSender) {
        ModalSendMoney.firstNameSender = firstNameSender;
    }

    public static String getNameSender() {
        return nameSender;
    }

    public static void setNameSender(String nameSender) {
        ModalSendMoney.nameSender = nameSender;
    }

    public static String getEmailSender() {
        return emailSender;
    }

    public static void setEmailSender(String emailSender) {
        ModalSendMoney.emailSender = emailSender;
    }

    public static String getFirstNameReceiver() {
        return firstNameReceiver;
    }

    public static void setFirstNameReceiver(String firstNameReceiver) {
        ModalSendMoney.firstNameReceiver = firstNameReceiver;
    }

    public static String getNameReceiver() {
        return nameReceiver;
    }

    public static void setNameReceiver(String nameReceiver) {
        ModalSendMoney.nameReceiver = nameReceiver;
    }

    public static String getEmailReceiver() {
        return emailReceiver;
    }

    public static void setEmailReceiver(String emailReceiver) {
        ModalSendMoney.emailReceiver = emailReceiver;
    }




    public static String getOtherTax() {
        return otherTax;
    }

    public static void setOtherTax(String otherTax) {
        ModalSendMoney.otherTax = otherTax;
    }

    public static String getTotalAmount() {
        return totalAmount;
    }

    public static void setTotalAmount(String totalAmount) {
        ModalSendMoney.totalAmount = totalAmount;
    }


    public static String getCountrySenderName() {
        return countrySenderName;
    }

    public static void setCountrySenderName(String countrySenderName) {
        ModalSendMoney.countrySenderName = countrySenderName;
    }

    public static String getCountryDestinationName() {
        return countryDestinationName;
    }

    public static void setCountryDestinationName(String countryDestinationName) {
        ModalSendMoney.countryDestinationName = countryDestinationName;
    }

    public static String getReasonOfTransfer() {
        return reasonOfTransfer;
    }

    public static void setReasonOfTransfer(String reasonOfTransfer) {
        ModalSendMoney.reasonOfTransfer = reasonOfTransfer;
    }

    public static String getCountrySenderCode() {
        return countrySenderCode;
    }

    public static void setCountrySenderCode(String countrySenderCode) {
        ModalSendMoney.countrySenderCode = countrySenderCode;
    }

    public static String getCountryDestinationCode() {
        return countryDestinationCode;
    }

    public static void setCountryDestinationCode(String countryDestinationCode) {
        ModalSendMoney.countryDestinationCode = countryDestinationCode;
    }

    public static String getCurrencySenderCode() {
        return currencySenderCode;
    }

    public static void setCurrencySenderCode(String currencySenderCode) {
        ModalSendMoney.currencySenderCode = currencySenderCode;
    }


    public static String getCurrencyDestinationCode() {
        return currencyDestinationCode;
    }

    public static void setCurrencyDestinationCode(String currencyDestinationCode) {
        ModalSendMoney.currencyDestinationCode = currencyDestinationCode;
    }

    public static String getSenderMobileNumber() {
        return senderMobileNumber;
    }

    public static void setSenderMobileNumber(String senderMobileNumber) {
        ModalSendMoney.senderMobileNumber = senderMobileNumber;
    }

    public static String getAmountString() {
        return amountString;
    }

    public static void setAmountString(String amountString) {
        ModalSendMoney.amountString = amountString;
    }







}