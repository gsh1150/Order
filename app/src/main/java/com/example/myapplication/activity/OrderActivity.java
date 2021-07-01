package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.List;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.bean.FoodBean;

public class OrderActivity extends AppCompatActivity {
    private ListView lv_order;
    private OrderAdapter adapter;
    private List<FoodBean> carFoodList;
    private TextView tv_title, tv_back, tv_distribution_cost, tv_total_cost,
            tv_cost, tv_payment, tv_payway;
    private RelativeLayout rl_title_bar;
    private BigDecimal money, distributionCost;
    private String[] payways = {"支付宝", "微信"};
    private int payway = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //获取购物车中的数据
        carFoodList = (List<FoodBean>) getIntent().getSerializableExtra("carFoodList");
        //获取购物车中菜的总价格
        money = new BigDecimal(getIntent().getStringExtra("totalMoney"));
        //获取店铺的配送费
        distributionCost = new BigDecimal(getIntent().getStringExtra(
                "distributionCost"));
        initView();
        setData();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("订单");
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tv_back = findViewById(R.id.tv_back);
        tv_payway = findViewById(R.id.tv_payway);
        lv_order = findViewById(R.id.lv_order);
        tv_distribution_cost = findViewById(R.id.tv_distribution_cost);
        tv_total_cost = findViewById(R.id.tv_total_cost);
        tv_cost = findViewById(R.id.tv_cost);
        tv_payment = findViewById(R.id.tv_payment);
        // 返回键的点击事件
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //“去支付”按钮的点击事件
        tv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(OrderActivity.this, R.style.Dialog_Style);
                if (tv_payway.getText() == "支付宝") {
                    dialog.setContentView(R.layout.qr_code_alipay);
                    dialog.show();
                } else if (tv_payway.getText() == "微信") {
                    dialog.setContentView(R.layout.qr_code_wechatpay);
                    dialog.show();
                } else
                    Toast.makeText(OrderActivity.this, "请选择支付方式！", Toast.LENGTH_SHORT).show();
            }
        });
        //”支付方式“按钮的点击事件
        tv_payway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("请选择支付方式")           //设置标题
                        .setSingleChoiceItems(new String[]{"支付宝", "微信"}, payway, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                payway = which;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //为TextView设置在单选对话框中选择支付方式
                                tv_payway.setText(payways[payway]);
                                dialog.dismiss(); //关闭对话框
                            }
                        })//添加“确定”按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * 设置界面数据
     */
    private void setData() {
        adapter = new OrderAdapter(this);
        lv_order.setAdapter(adapter);
        adapter.setData(carFoodList);
        tv_cost.setText("￥" + money);
        tv_distribution_cost.setText("￥" + distributionCost);
        tv_total_cost.setText("￥" + (money.add(distributionCost)));
    }
}
