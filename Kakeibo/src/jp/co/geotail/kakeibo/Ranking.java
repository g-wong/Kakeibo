package jp.co.geotail.kakeibo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;

public class Ranking {
	private Activity parent;
	/**
	 * ランキングが書かれている外部ファイル
	 */
	private final static String RANKING_FILE = "ranking.csv";
	
	private static final int RANKING_DISPLAY_SIZE = 10;
	
	private String fileName;
	private ArrayList<String[]> ranking = new ArrayList<String[]>();
	
	Ranking(Activity activity) {
		this(RANKING_FILE, activity);
	}
	
	Ranking(String fileName, Activity activity) {
		this.fileName = fileName;
		parent = activity;
		
		init();
	}
	
	private void init() {
		File file = new File(fileName);
		if (file.exists() ) {
			readRankingFile();
		}
		else {
			for (int i = 0; i < RANKING_DISPLAY_SIZE; i++) {
				String[] rank = new String[2];
				rank[0] = "PLAYER" + i;
				rank[1] = "0";
				ranking.add(rank);
			}
		}
	}
	
	public String getName(int index) {
		if (index > ranking.size()) return null;
		
		return ranking.get(index)[0];
	}
	
	public int getScore(int index) {
		if (index > ranking.size()) return -1;
		
		return Integer.valueOf(ranking.get(index)[1]);
	}
	
	public void setRanking(String name, int score) {
		ArrayList<String[]> newRanking = new ArrayList<String[]>();
		for (String[] list : ranking) {
			if(score >= Integer.valueOf(list[1])){
				String[] target = new String[2];
				target[0] = name;
				target[1] = String.valueOf(score);
				newRanking.add(target);
				score = -1;
			}
			else {
				newRanking.add(list);
			}
		}
		
		ranking = newRanking;
	}

	/**
	 * スコアからランキングを取得
	 * @param score
	 * @return ランキング（ランク外だった場合は -1）
	 */
	public int getRankingFor(int score){
		if (ranking.size() < RANKING_DISPLAY_SIZE) {
			try {
				throw new Exception("ランキングが設定されていません。ファイルの読み込みに失敗している可能性があります。");
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
		
		for(int i = 0; i < ranking.size(); i++) {
			if (score > getScore(i)) return i + 1; // 0位からなので +1 しておく
		}
		return -1;
	}
	
	private void readRankingFile() {
		try {
			FileInputStream inputStream = parent.openFileInput(RANKING_FILE);
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			while((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				String name = st.nextToken();
				String score = st.nextToken();
				setRanking(name, Integer.valueOf(score));
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("ファイル読み込みに失敗しました。 file=" + fileName.toString());
		}
	}
	
	public void writeRankingFile() {
		String filePath = fileName;
		try {
			FileOutputStream output;
			output = parent.openFileOutput(filePath, Context.MODE_PRIVATE);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
			for (int i = 0; i < RANKING_DISPLAY_SIZE; i++){
				writer.write(getName(i) + "," + getScore(i));
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			System.err.println("ファイル書き込みに失敗しました。 file=" + fileName.toString());
		}
	}
}
