package com.droider.sampleposconnector.pos;

import static com.droider.droiderposconnectorlibrary.DroiderConstant.DROIDER_FAILURE_RESULT_CODE;
import static com.droider.droiderposconnectorlibrary.DroiderConstant.DROIDER_REQUEST_CODE;
import static com.droider.droiderposconnectorlibrary.DroiderConstant.DROIDER_SUCCESS_RESULT_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.droider.droiderposconnectorlibrary.HBLServices;
import com.droider.droiderposconnectorlibrary.Logger;
import com.droider.sampleposconnector.R;

public class MainActivity extends AppCompatActivity {

    Button send_amount, void_txn, settlement, sale_qr_txn;
    TextView result_tv, approval_code, invoice_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send_amount = findViewById(R.id.send_amount);
        result_tv = findViewById(R.id.result_tv);
        sale_qr_txn = findViewById(R.id.sale_qr_txn);
        settlement = findViewById(R.id.settlement);
        void_txn = findViewById(R.id.void_txn);
        invoice_number = findViewById(R.id.invoice_number);
        approval_code = findViewById(R.id.approval_code);


        send_amount.setOnClickListener(v -> {
            HBLServices.connectPOS().performSale(this, "4798.32");
        });

        sale_qr_txn.setOnClickListener(v -> {
            HBLServices.connectPOS().performSaleWithQR(this, "0.32");
        });
        void_txn.setOnClickListener(v -> {
            HBLServices.connectPOS().performVoid(this);
        });
        settlement.setOnClickListener(v -> {
            HBLServices.connectPOS().performSettlement(this);
        });

        result_tv.setText("init");
        approval_code.setText("init");
        invoice_number.setText("init");
        Logger.v("init_mainAtivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.v("onActivityResult===requestCode===" + requestCode + "------resultCode===" + resultCode);


        if (resultCode == DROIDER_FAILURE_RESULT_CODE && requestCode == DROIDER_REQUEST_CODE) {
            Logger.v("getTransactionStatus===" + HBLServices.connectPOS().getTransactionStatus(data));
            result_tv.setText(HBLServices.connectPOS().getTransactionStatus(data));
        } else if (resultCode == DROIDER_SUCCESS_RESULT_CODE && requestCode == DROIDER_REQUEST_CODE) {
            Logger.v("getTransactionStatus===" + HBLServices.connectPOS().getTransactionStatus(data));
            Logger.v("getTransactionResponseCode===" + HBLServices.connectPOS().getTransactionResponseCode(data));
            Logger.v("getTransactionInvoiceNumber===" + HBLServices.connectPOS().getTransactionInvoiceNumber(data));
            boolean isCompleted = HBLServices.connectPOS().isTransactionCompleted(data);
            Logger.v("isCompleted===" + isCompleted);
            result_tv.setText(HBLServices.connectPOS().getTransactionStatus(data));
            approval_code.setText(HBLServices.connectPOS().getTransactionResponseCode(data));
            invoice_number.setText(HBLServices.connectPOS().getTransactionInvoiceNumber(data));
        }
    }

}