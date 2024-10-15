package com.api.store.domain.service;

import com.api.store.domain.Wallet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stellar.sdk.KeyPair;

import java.math.BigDecimal;

@Service
public class StellarService {

    @Value("${stellar.network.url}")
    private String network;

    @Value("${stellar.store.account}")
    private String storeAccountId;

    public static final BigDecimal STROOP = new BigDecimal("0.0000001");;

    public Wallet createAccount(String username){
        //crea cuenta con pk que empiece en 'S' y pk convertible a ASCII de cada char de 2 digitos
        KeyPair pair = KeyPair.random();
        String secretKey = String.valueOf(pair.getSecretSeed());
        while (secretKey.charAt(0) != 'S' || !containAsciiBetween10and99(secretKey)){
            System.out.println(new String(pair.getSecretSeed())+" charAt(0) != S");
            pair = KeyPair.random();
            secretKey  = String.valueOf(pair.getSecretSeed());
        }
        System.out.println("public key: " +  pair.getAccountId());
        System.out.println("secret key: " +  new String(pair.getSecretSeed()));
        return new Wallet(username+pair.getAccountId(),pair.getAccountId(), new String(pair.getSecretSeed()));
    }

    private Boolean containAsciiBetween10and99(String pk){
        char[] ascii1 = pk.toCharArray();
        for(char ch:ascii1){
            if((int)ch<10 || (int)ch>99)
                return false;
        }
        return true;
    }

    public String getBalance(String publicKey) {
//        BigDecimal accountBalance = new BigDecimal("0");
//        Server server = new Server(network);
//        KeyPair destination = KeyPair.fromAccountId(publicKey);
//        try {
//            AccountResponse accountResponse = server.accounts().account(destination.getAccountId());
//            System.out.println("Balances for account " + publicKey);
//            for (AccountResponse.Balance balance : accountResponse.getBalances()) {
//                accountBalance = accountBalance.add(new BigDecimal(balance.getBalance()));
//                System.out.println(String.format("Type: %s, Code: %s, Balance: %s", balance.getAssetType(),
//                        balance.getAssetCode(), accountBalance));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return accountBalance.toString();
        return "";
    }

    public enum TxType {
        CREATE_ACCOUNT, PAYMENT, PAYMENT_2_OP
    }

    public String sendSinglePayment(String sourceSecretSeed, String destinationPublicKey, String amount, String memo){
//
//        TxType txType = TxType.PAYMENT;
//        Server server = new Server(network);
//        KeyPair source = KeyPair.fromSecretSeed(sourceSecretSeed);
//        KeyPair destination = KeyPair.fromAccountId(destinationPublicKey);
//
//        try {
//            //check to make sure that the destination account exists
//            server.accounts().account(destination.getAccountId());
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            //if account don't exist
//            if(Float.parseFloat(amount) >= 1)
//                txType = TxType.CREATE_ACCOUNT;
//            else
//                return "Transaction Fail: Amount must be >= 1";
//        }
//
//        switch (txType) {
//            case CREATE_ACCOUNT:
//                try {
//                    AccountResponse sourceAccount = server.accounts().account(source.getAccountId());
//                    Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET).setBaseFee(Transaction.MIN_BASE_FEE)
//                            .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), amount).build())
//                            .addMemo(Memo.text("create account"))
//                            .setTimeout(180)
//                            .build();
//
//                    transaction.sign(source);
//                    SubmitTransactionResponse response = server.submitTransaction(transaction);
//                    System.out.println("Transaction Succes? "+response.isSuccess());
//                    System.out.println(response.getRateLimitRemaining());
//                    System.out.println(response.getRateLimitLimit());
//                    System.out.println(response.getRateLimitReset());
//                    System.out.println(response.getLedger());
//                    return("Transaction "+response.isSuccess());
//                } catch (Exception e) {
//                    System.out.println("(CREATE_ACCOUNT) Something went wrong! " + e.getMessage());
//                }
//                break;
//
//            case PAYMENT:
//                try{
//                    BigDecimal storePCT = new BigDecimal("0.05");
//                    BigDecimal customerPCT = new BigDecimal("0.95");
//
//                    BigDecimal xlmPrice = new BigDecimal(amount);
//                    BigDecimal numberOfOperations = new BigDecimal("2");
//                    BigDecimal xlmFee = numberOfOperations.multiply(STROOP).multiply(new BigDecimal(Transaction.MIN_BASE_FEE));
//                    BigDecimal xlmToStore = (xlmPrice.multiply(storePCT)).subtract(xlmFee);
//                    xlmToStore = xlmToStore.setScale(7, RoundingMode.HALF_UP);
//                    BigDecimal xlmToCustomer = xlmPrice.multiply(customerPCT);
//                    xlmToCustomer = xlmToCustomer.setScale(7, RoundingMode.HALF_UP);
//
//                    AccountResponse sourceAccount = server.accounts().account(source.getAccountId());
//                    Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET).setBaseFee(Transaction.MIN_BASE_FEE)
//                            .addOperation(new PaymentOperation.Builder(storeAccountId, new AssetTypeNative(), xlmToStore.toString()).build())
//                            .addOperation(new PaymentOperation.Builder(destination.getAccountId(), new AssetTypeNative(), xlmToCustomer.toString()).build())
//                            .addMemo(Memo.text("buying item"))
//                            .setTimeout(180)
//                            .build();
//                    transaction.sign(source);
//                    SubmitTransactionResponse response = server.submitTransaction(transaction);
//                    System.out.println("Transaction Succes? "+response.isSuccess());
//                    System.out.println(response.getRateLimitRemaining());
//                    System.out.println(response.getRateLimitLimit());
//                    System.out.println(response.getRateLimitReset());
//                    System.out.println(response.getLedger());
//                    System.out.println(response.toString());
//                    return("Transaction "+response.isSuccess());
//                } catch (Exception e) {
//                    System.out.println("(PAYMENT) Something went wrong!");
//                    System.out.println(e.getMessage());
//                }
//                break;
//
//            default:
//                System.out.println("default");
//                break;
//        }
        return "Transaction Fail";
    }

    public String sendDualPayment(String sourceSecretSeed, String destinationPublicKey, String middlemanPublicKey, String amount, String memo){
//        TxType txType = TxType.PAYMENT_2_OP;
//        Server server = new Server(network);
//        KeyPair source = KeyPair.fromSecretSeed(sourceSecretSeed);
//        KeyPair destination = KeyPair.fromAccountId(destinationPublicKey);
//        KeyPair middleman = KeyPair.fromAccountId(middlemanPublicKey);
//
//        try {
//            //check to make sure that the destination account exists
//            server.accounts().account(destination.getAccountId());
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            //if account don't exist
//            if(Float.parseFloat(amount) >= 1)
//                txType = TxType.CREATE_ACCOUNT;
//            else
//                return "Transaction Fail: Amount must be >= 1";
//        }
//
//        switch (txType) {
//            case CREATE_ACCOUNT:
//                try {
//                    AccountResponse sourceAccount = server.accounts().account(source.getAccountId());
//                    Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET).setBaseFee(Transaction.MIN_BASE_FEE)
//                            .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), amount).build())
//                            .addMemo(Memo.text("create account"))
//                            .setTimeout(180)
//                            .build();
//
//                    transaction.sign(source);
//                    SubmitTransactionResponse response = server.submitTransaction(transaction);
//                    return("Transaction "+response.isSuccess());
//                } catch (Exception e) {
//                    System.out.println("(CREATE_ACCOUNT) Something went wrong! " + e.getMessage());
//                }
//                break;
//
//            case PAYMENT_2_OP:
//                try{
//                    BigDecimal storePCT = new BigDecimal("0.04");
//                    BigDecimal customerPCT = new BigDecimal("0.95");
//                    BigDecimal middlemanPCT = new BigDecimal("0.01");
//
//                    BigDecimal xlmPrice = new BigDecimal(amount);
//                    BigDecimal numberOfOperations = new BigDecimal("3");
//                    BigDecimal xlmFee = numberOfOperations.multiply(STROOP).multiply(new BigDecimal(Transaction.MIN_BASE_FEE));
//                    System.out.println("XLM FEE = " + xlmFee);
//                    BigDecimal xlmToStore = (xlmPrice.multiply(storePCT)).subtract(xlmFee);
//                    xlmToStore = xlmToStore.setScale(7, RoundingMode.HALF_UP);
//                    System.err.println(xlmToStore);
//                    BigDecimal xlmToCustomer = xlmPrice.multiply(customerPCT);
//                    xlmToCustomer = xlmToCustomer.setScale(7, RoundingMode.HALF_UP);
//                    System.err.println(xlmToCustomer);
//                    BigDecimal xlmToMiddleman = xlmPrice.multiply(middlemanPCT);
//                    xlmToMiddleman = xlmToMiddleman.setScale(7, RoundingMode.HALF_UP);
//                    System.err.println(xlmToMiddleman);
//
//                    AccountResponse sourceAccount = server.accounts().account(source.getAccountId());
//                    Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET).setBaseFee(Transaction.MIN_BASE_FEE)
//                            .addOperation(new PaymentOperation.Builder(storeAccountId, new AssetTypeNative(), xlmToStore.toString()).build())
//                            .addOperation(new PaymentOperation.Builder(destination.getAccountId(), new AssetTypeNative(), xlmToCustomer.toString()).build())
//                            .addOperation(new PaymentOperation.Builder(middleman.getAccountId(), new AssetTypeNative(), xlmToMiddleman.toString()).build())
//                            .addMemo(Memo.text("buying item"))
//                            .setTimeout(180)
//                            .build();
//                    transaction.sign(source);
//                    SubmitTransactionResponse response = server.submitTransaction(transaction);
//                    System.out.println("Transaction Succes? "+response.isSuccess());
//                    System.out.println(response.getRateLimitRemaining());
//                    System.out.println(response.getRateLimitLimit());
//                    System.out.println(response.getRateLimitReset());
//                    System.out.println(response.getLedger());
//                    System.out.println(response.toString());
//                    return("Transaction "+response.isSuccess());
//                } catch (Exception e) {
//                    System.out.println("(PAYMENT) Something went wrong!");
//                    System.out.println(e.getMessage());
//                }
//                break;
//
//            default:
//                System.out.println("default");
//                break;
//        }
        return "Transaction Fail";
    }
}
