package com.fs.game.gobang.main;

import com.fs.game.gobang.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//�������Activity
public class MainActivity extends Activity implements OnClickListener{
    
	private Chessboard gameView;
	private Button btn_restart;
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_game_view);
        gameView = (Chessboard) findViewById(R.id.snake);
        gameView.setTextView((TextView)findViewById(R.id.text));
//        btn_start = (Button) findViewById(R.id.btn_start);
//        btn_start.setOnClickListener(this);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(this);
        gameView.setRestartButton(btn_restart);
        
        new MyTask().execute();
    }
    
    class MyTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			gameView.restart();
			gameView.setMode(Chessboard.RUNNING);
		}
		
		
    	
    }

	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_start:
//			btn_start.setVisibility(View.GONE);
//			gameView.restart();
//			gameView.setMode(Chessboard.RUNNING);
//			break;
		case R.id.btn_restart:
			btn_restart.setVisibility(View.GONE);
			gameView.restart();
			gameView.setMode(Chessboard.RUNNING);
			break;
		}
	}
    
}