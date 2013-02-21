package jp.co.geotail.kakeibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TitleActivity extends Activity {

    LinearLayout layout;
    Button imgbtn;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.title);
        
        layout = (LinearLayout) findViewById(R.id.linearLayout1);
        
        ImageView titleImage = (ImageView) findViewById(R.id.image_title);
        titleImage.setBackgroundColor(Color.argb(255, 255, 255, 255));
        
        setStartButton();
        
    }
    
    private void setStartButton() {
    	// ボタンを生成
        imgbtn = (Button) findViewById(R.id.imgbtn_id);
        
        // ゲーム開始ボタン押下処理
        imgbtn.setOnClickListener(new View.OnClickListener() {
            
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
    
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
