package jp.co.geotail.kakeibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);
        setMoveActivity();
    }
    
    /**
     * ゲーム開始ボタン押下処理
     */
    private void setMoveActivity() {
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = 
                    new Intent(TitleActivity.this, MainActivity.class);
                
                // メイン画面の起動
                startActivity(intent);
                
            }
        });
    }
}
