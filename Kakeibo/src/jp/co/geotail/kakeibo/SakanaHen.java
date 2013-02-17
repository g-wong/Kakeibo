package jp.co.geotail.kakeibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.Activity;

public class SakanaHen {

	private Activity activity;
	private ArrayList<String> sakanaList = new ArrayList<String>();
	private HashMap<String, String> sakanaMap = new HashMap<String, String>();
	private Random rand = new Random();
	
	SakanaHen(String fileName, Activity activity) {
		this.activity = activity;
		readSakanaHenList(fileName);
	}
	
	public int getMondaiSize() {
		return sakanaList.size();
	}

	/**
	 * 答えのチェック
	 * @param s
	 * @param answer
	 * @return
	 */
	public boolean checkAnswer(String kanji, String answer) {
		String yomi = sakanaMap.get(kanji);
		if (yomi.equals(answer)) return true;
		return false;
	}
	

	/**
	 * 魚へんをファイルから読み込む
	 * @param file
	 */
	private void readSakanaHenList(String file) {
		try {
			InputStream is = activity.getAssets().open(file);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				String kanji = st.nextToken();
				String yomi = st.nextToken();
				sakanaMap.put(kanji, yomi);
				sakanaList.add(kanji);
			}
		} catch (IOException e) {
			System.err.println("ファイル読み込みに失敗しました。 file=" + file.toString());
		}
	}
	

	/**
	 * HashMap から魚偏の漢字をランダムで返す
	 * @return 魚篇の漢字
	 */
	public String getRandomSakanaHenKanji() {
		int size = sakanaList.size();
		int ran = rand.nextInt(size);
		
		return sakanaList.get(ran);
	}
	
	/**
	 *  ランダムに魚へんの読みを取得する
	 * @return
	 */
	public String getRandomSakanaHenYomi() {
		return sakanaMap.get(getRandomSakanaHenKanji());
	}
	
	/**
	 * 魚へんの漢字と読みを設定する
	 * @param kanji
	 * @param yomi
	 */
	public void setSakanaHen(String kanji, String yomi) {
		sakanaMap.put(kanji, yomi);
	}
	
	/**
	 * 漢字から読みを取得する
	 * @param kanji
	 * @return
	 */
	public String getYomi(String kanji) {
		return sakanaMap.get(kanji);
	}
	
}
