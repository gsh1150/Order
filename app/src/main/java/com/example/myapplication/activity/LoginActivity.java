package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {
    private EditText et_account, et_password;
    private Button btn_login;
    private MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        SQLinit();
    }

    /**
     * 初始化界面控件
     */
    private void init() {
        myHelper = new MyHelper(LoginActivity.this);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_account.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                else if (et_password.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                else {
                    if (judge(et_account.getText().toString(), et_password.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, ShopActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void SQLinit() {
        add();
    }

    //向数据库表中插入数据
    private void add() {
        SQLiteDatabase db;
        ContentValues values;
        db = myHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("account", "123");
        values.put("password", "123");
        db.insert("information", null, values);
        Toast.makeText(LoginActivity.this, "add!", Toast.LENGTH_SHORT).show();
        db.close();
    }

    //查询用户名与密码是否匹配
    private boolean judge(String account, String password) {
        SQLiteDatabase db;
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("information", null, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(0).equals(account)) {
                    if (cursor.getString(1).equals(password)) {
                        Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        Toast.makeText(LoginActivity.this, "密码不正确！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }
        cursor.close();
        db.close();
        Toast.makeText(LoginActivity.this, "用户不存在！", Toast.LENGTH_SHORT).show();
        return false;
    }

    class MyHelper extends SQLiteOpenHelper {
        public MyHelper(Context context) {
            super(context, "mysql.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE information(account VARCHAR(20) PRIMARY KEY ,  password VARCHAR(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}