package jp.co.geotail.kakeibo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// GUI
	private TextView text1;
	private TextView resultMessageText;
	private TextView sakanaHenText;
	private Button btn1;
	private RadioGroup radioGroup;
	private RadioButton r_btn[] = new RadioButton[5];
	
	/**
	 * 魚へん
	 */
	private SakanaHen sakanaHen;
	
	/**
	 * 現在出ている問題
	 */
	private String mondai;
	/**
	 * 既に出した問題を格納するためのリスト
	 */
	private ArrayList<String> mondaiList = new ArrayList<String>();
	
	/**
	 * 正解数
	 */
	private int seikaiCounter = 0;
	
	/**
	 * 不正解数
	 */
	private int noSeikaiCounter = 0;
	
	/**
	 * 問題数
	 */
	@SuppressWarnings("unused")
	private int mondaiCounter = 0;

	/**
	 * 問題が書かれている外部ファイル
	 */
	private final static String SAKANA_LIST_FILE = "sakanaHen.csv";
	
	private Ranking ranking;
	
	//////////////////////////////////////////////////
	// ゲームの状態
	//////////////////////////////////////////////////
	/**
	 * ゲーム開始
	 */
	private final static int STATE_START_GAME = 0;
	
	/**
	 * 問題を出しているところ
	 */
	private final static int STATE_QUIZ_RUNNING = 1;
	
	/**
	 * ゲームクリアまたはゲームオーバー
	 */
	private final static int STATE_CLEAR = 2;
	
	/**
	 * ゲームの状態を保持するための変数
	 */
	private static int state = STATE_QUIZ_RUNNING;
	
	////////////////////////////////////////////////////
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 画面の初期化
		initialize();
		
		// ファイルから魚へんを読み込んでインスタンスを生成
		this.sakanaHen = new SakanaHen(SAKANA_LIST_FILE, this);
		
		//問題表示
		nextQuestion();
		
		// ランキング
		ranking = new Ranking(this);
	}
	
	/**
	 * GUI初期化
	 */
	private void initialize() {
		setContentView(R.layout.activity_main);
		text1 = (TextView)this.findViewById(R.id.TextView01);
		text1.setText("");
		sakanaHenText = (TextView) findViewById(R.id.uohen);
		
		//ラジオボタン
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		r_btn[0] = (RadioButton) findViewById(R.id.radiobutton_1);
		r_btn[1] = (RadioButton) findViewById(R.id.radiobutton_2);
		r_btn[2] = (RadioButton) findViewById(R.id.radiobutton_3);
		r_btn[3] = (RadioButton) findViewById(R.id.radiobutton_4);
		r_btn[4] = (RadioButton) findViewById(R.id.radiobutton_5);
		
		// 解答ボタン
		btn1 = (Button)this.findViewById(R.id.Button01);
		resultMessageText = (TextView)this.findViewById(R.id.TextView02);
		
		btn1.setOnClickListener(new MyClickAdapter());
	}
	
	private void setMondai(String kanji) {
		this.mondai = kanji;
	}
	
	private String getMondai() {
		return mondai;
	}
	
	/**
	 * 問題の出題
	 */
	public void nextQuestion() {
		// ランダムに漢字を取得して問題にする
		// 既に出た問題は出さない
		String mondaiKanji;
		do {
			mondaiKanji = sakanaHen.getRandomSakanaHenKanji();
		} while(mondaiList.contains(mondaiKanji));
		mondaiList.add(mondaiKanji);
		setMondai(mondaiKanji);
		sakanaHenText.setText(getMondai() + " の読みは？");

        // 正解の読みを選択肢のどこかに設定
    	Set<String> set = new HashSet<String>();
        int r = new Random().nextInt(5);
        String seikai = sakanaHen.getYomi(getMondai());
        r_btn[r].setText(seikai);
        set.add(seikai);
        
		// 選択肢の魚へんの読みを重複なしでランダムに設定
        for(int i = 0; i < 5; i++) {
        	// 正解が設定されている箇所だったら、設定しない
        	if (r == i) continue;
        	String text;
        	// 重複のチェック
        	do {
        		text = sakanaHen.getRandomSakanaHenYomi().toString();
        	} while (set.contains(text));
        	set.add(text);
        	// ラジオボタンに魚へんの読みを設定
        	r_btn[i].setText(text);
        }
	}
	
	/**
	 * チェックされているラジオボタンのテキスト文字列を取得する
	 * @return
	 */
	private String getAnswer() {
		// チェックされているラジオボタンの ID を取得します
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioButton == null) return null;
        return radioButton.getText().toString();
	}
	
	/**
	 * ゲームクリア
	 */
	private void clearGame() {
		sakanaHenText.setText("");
		mondaiList.clear();
		for(int i = 0; i < 5; i++) {
			r_btn[i].setText("");
			radioGroup.clearCheck();
		}
	}
	
	/**
	 * ゲームクリアしたときにメッセージを出力する
	 */
	private void putClearMessage() {
		// 正解数を表示
		Toast.makeText(this, "正解数 : " + seikaiCounter , Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 正解
	 */
	private void success() {
		resultMessageText.setText("正解!");
		seikaiCounter++;
	}
	
	/**
	 * 不正解
	 * @param kanji 問題の漢字
	 */
	private void fail(String kanji) {
		resultMessageText.setText("不正解！"+ kanji+ "は[" + sakanaHen.getYomi(kanji) + "]");
		noSeikaiCounter++;
	}
	
	/**
	 * ランキングに入っていれば、名前を入力するダイアログを表示して
	 * ランキング登録をおこなう
	 */
	private void setRanking() {
		int rank = ranking.getRankingFor(seikaiCounter);
		if(rank == -1) {
			return;
		}
		 //テキスト入力を受け付けるビューを作成します。
	    final EditText editView = new EditText(MainActivity.this);
	    new AlertDialog.Builder(MainActivity.this)
	        .setIcon(android.R.drawable.ic_dialog_info)
	        .setTitle(rank + "位にランクインしました！　名前を入力してください")
	        //setViewにてビューを設定します。
	        .setView(editView)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                String name = editView.getText().toString();
	                ranking.setRanking(name, seikaiCounter);
	            }
	        })
	        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            }
	        })
	        .show();
	    
	    ranking.writeRankingFile();
	}
	
	/**
	 * STARTボタン＆解答ボタンが押された時の動作
	 * @author Administrator
	 *
	 */
	class MyClickAdapter implements OnClickListener {
		public void onClick(View view) {
			if (state == STATE_START_GAME) {
				state = STATE_QUIZ_RUNNING;
				resultMessageText.setText("");
				btn1.setText("解答する");
				nextQuestion();
			}
			// 問題は解いている最中
			else if (state == STATE_QUIZ_RUNNING) {
				String kanji = getMondai();
				String answer = getAnswer();
				if(answer == null) {
					resultMessageText.setText("選択されていません");
					return ;
				}
				// 選択されたラジオボタンをクリアする
		        radioGroup.clearCheck();
		        
		        // 解答のチェック
				if (sakanaHen.checkAnswer(kanji, answer)){
					success();
				} else {
					fail(kanji);
				}
				mondaiCounter++;
		        
		        // １回間違え、または、全ての問題を出し切ったら終了
		        if ( noSeikaiCounter >= 1 
		        		|| mondaiList.size() >= sakanaHen.getMondaiSize()) {
		        	state = STATE_CLEAR;
		        	putClearMessage();
		        	//setRanking();
		        	clearGame();
		        	btn1.setText("もう一度！");
		        	return;
		        }
		        
		        nextQuestion();
			}
			// クイズが終了したとき
			else if (state == STATE_CLEAR) {
				noSeikaiCounter = 0;
				seikaiCounter = 0;
				mondaiCounter = 0;
				state = STATE_START_GAME;
				onClick(view);
			}
		}
	}
}
