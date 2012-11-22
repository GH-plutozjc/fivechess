package com.fs.game.gobang.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fs.game.gobang.R;
import com.fs.game.gobang.player.AiFactory;
import com.fs.game.gobang.player.concrete.HumanPlayer;
import com.fs.game.gobang.player.interfaces.IPlayer;
//����
public class Chessboard extends View implements IChessboard{

	//��Ϸ״̬������
    //��׼���ã��ɿ���
    private static final int READY = 1;
    //�ѿ���
    public static final int RUNNING = 2;
    //�ѽ���
    private static final int PLAYER_TWO_LOST = 3;
    private static final int PLAYER_ONE_LOST = 4;
    
    //��ǰ״̬��Ĭ��Ϊ�ɿ���״̬
    private int currentMode = READY;
    
	//���ʶ���
	private final Paint paint = new Paint();
	
	//�����ɫ
	private static final int GREEN = 0;
	private static final int NEW_GREEN = 1;
	
	//��ɫ
	private static final int RED = 2;
	//��ɫ
	private static final int NEW_RED = 3;
	
	//���С
    private static int pointSize = 30;
	
    //������ʾ��Ӯ���ı��ؼ�
	private TextView textView = null;
	//���¿�ʼ��ť
	private Button bt_restart = null;
	
	//��ͬ��ɫ��Bigmap����
	private Bitmap[] pointArray = new Bitmap[4];
	
	//��Ļ���½ǵ����ֵ����������ֵ
    private static int maxX;
    private static int maxY;
    
    //��һ��ƫ�����ϽǴ�����Ϊ�����̾���
	private static int yOffset;
	private static int xOffset;
	
	//�������
	//��һ�����Ĭ��Ϊ�������
	private IPlayer player1 = new HumanPlayer();
	//�ڶ�������ѡ���˻�ս���Ƕ�սģʽ����ʼ��
	private IPlayer player2 = computer;
	//Ԥ�ȳ�ʼ�����ڶ����
	//�������
	private static final IPlayer computer = AiFactory.getInstance(2);
	//�������
	private static final IPlayer human = new HumanPlayer();
	
	// ����δ�µĿհ׵�
	private final List<Point> allFreePoints = new ArrayList<Point>();
	
    public Chessboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        
        //�������ɫ�ĵ�׼���ã�����������
        Resources r = this.getContext().getResources();
        fillPointArrays(GREEN,r.getDrawable(R.drawable.green_point));
        fillPointArrays(NEW_GREEN,r.getDrawable(R.drawable.new_green_point));
        fillPointArrays(RED,r.getDrawable(R.drawable.red_point));
        fillPointArrays(NEW_RED,r.getDrawable(R.drawable.new_red_point));
        
        //���û���ʱ�õ���ɫ
        paint.setColor(Color.LTGRAY);
   }
    
    
    //��ʼ���ߺ����ߵ���Ŀ
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        maxX = (int) Math.floor(w / pointSize);
        maxY = (int) Math.floor(h / pointSize);

        //����X��Y���΢��ֵ��Ŀ����������
        xOffset = ((w - (pointSize * maxX)) / 2);
        yOffset = ((h - (pointSize * maxY)) / 2);
        //���������ϵ�����
        createLines();
        //��ʼ�����������пհ׵�
        createPoints();
    }
    
    //�������������е���
    private void createLines(){
    	for (int i = 0; i <= maxX; i++) {//����
    		lines.add(new Line(xOffset+i*pointSize-pointSize/2, yOffset, xOffset+i*pointSize-pointSize/2, yOffset+maxY*pointSize));
		}
    	for (int i = 0; i <= maxY; i++) {//����
    		lines.add(new Line(xOffset, yOffset+i*pointSize-pointSize/2, xOffset+maxX*pointSize, yOffset+i*pointSize-pointSize/2));
		}
    }
    
    //������
    private List<Line> lines = new ArrayList<Line>();
    private void drawChssboardLines(Canvas canvas){
    	for (Line line : lines) {
    		canvas.drawLine(line.xStart, line.yStart, line.xStop, line.yStop, paint);
		}
    }
    
    //����
    class Line{
    	float xStart,yStart,xStop,yStop;
		public Line(float xStart, float yStart, float xStop, float yStop) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xStop = xStop;
			this.yStop = yStop;
		}
    }
    
    //����
    private void drawPoint(Canvas canvas,Point p,int color){
    	canvas.drawBitmap(pointArray[color],p.x*pointSize+xOffset,p.y*pointSize+yOffset,paint);
    }
    
    
    

    //��������״̬
	public void setMode(int newMode) {
		currentMode = newMode;
		if(currentMode==PLAYER_TWO_LOST){
			//��ʾ���2����
			textView.setText(R.string.player_two_lost);
			textView.setVisibility(View.VISIBLE);
			currentMode = READY;
			this.bt_restart.setVisibility(View.VISIBLE);
		}else if(currentMode==RUNNING){
			textView.setText(null);
		}else if(currentMode==READY){
			textView.setText(R.string.mode_ready);
		}else if(currentMode==PLAYER_ONE_LOST){
			//��ʾ���1����
			textView.setText(R.string.player_one_lost);
			textView.setVisibility(View.VISIBLE);
			currentMode = READY;
			this.bt_restart.setVisibility(View.VISIBLE);
		}
	}
	

	//������ʾ�ؼ�
	public void setTextView(TextView textView) {
		this.textView = textView;
	}
	
	public void setRestartButton(Button btn){
		this.bt_restart = btn;
	}

	//��������¼�
	/*@Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (currentMode == READY && (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
        	if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){//���Ҽ��˻��ս
        		player2 = computer;
        	}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){//�������--�˶�ս
        		player2 = human;
        	}
        	restart();
        	setMode(RUNNING);
        }else if(currentMode==RUNNING && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){//���¿�ʼ
        	restart();
        	setMode(READY);
        }else{
        	return false;
        }
        return true;
	}*/
	
	//��ݴ���������ҵ���Ӧ��
	private Point newPoint(Float x, Float y){
		Point p = new Point(0, 0);
		for (int i = 0; i < maxX; i++) {
			if ((i * pointSize + xOffset) <= x
					&& x < ((i + 1) * pointSize + xOffset)) {
				p.setX(i);
			}
		}
		for (int i = 0; i < maxY; i++) {
			if ((i * pointSize + yOffset) <= y
					&& y < ((i + 1) * pointSize + yOffset)) {
				p.setY(i);
			}
		}
		return p;
	}
	
	//���¿�ʼ
	public void restart() {
		textView.setVisibility(View.INVISIBLE);
		createPoints();
		player1.setChessboard(this);
		player2.setChessboard(this);
		setPlayer1Run();
		//ˢ��һ��
		refressCanvas();
	}
	
	//�Ƿ��ѿ���
	private boolean hasStart(){
		return currentMode==RUNNING;
	}

	//���?���¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��û�п��֣������ǰ����¼��������?ֻ���?�ֺ�Ĵ��������¼�
		if(!hasStart() || event.getAction()!=MotionEvent.ACTION_UP){
			return true;
		}
		//�Ƿ����ڴ���һ����Ĺ����
		if(onProcessing()){
			return true;
		}
		
		playerRun(event);
		
		return true;
	}
	
	private synchronized void playerRun(MotionEvent event){
		if(isPlayer1Run()){//��һ�������
			player1Run(event);
		}else if(isPlayer2Run()){//�ڶ��������
			player2Run(event);
		}
	}
	
	
	private void player1Run(MotionEvent event){
		Point point = newPoint(event.getX(), event.getY());
		if(allFreePoints.contains(point)){//�����Ƿ����
			setOnProcessing();
			player1.run(player2.getMyPoints(),point);
			//playerOnePoints.add(point);
			//ˢ��һ������
			refressCanvas();
			//�жϵ�һ������Ƿ��Ѿ�����
			if(!player1.hasWin()){//�һ�û��Ӯ
				if(player2==computer){//���ڶ�����ǵ���
					//10�����Ÿ����2����
					refreshHandler.computerRunAfter(10);
				}else{
					setPlayer2Run();
				}
			}else{
				//������ʾ��Ϸ����
				setMode(PLAYER_TWO_LOST);
			}
		}
	}
	
	private void player2Run(MotionEvent event){
		Point point = newPoint(event.getX(), event.getY());
		if(allFreePoints.contains(point)){//�����Ƿ����
			setOnProcessing();
			player2.run(player1.getMyPoints(),point);
//			playerTwoPoints.add(point);
			//ˢ��һ������
			refressCanvas();
			//�ж����Ƿ�Ӯ��
			if(!player2.hasWin()){//�һ�û��Ӯ
				setPlayer1Run();
			}else{
				//������ʾ��Ϸ����
				setMode(PLAYER_ONE_LOST);
			}
		}
	}
	
	
	private RefreshHandler refreshHandler = new RefreshHandler();
	class RefreshHandler extends Handler {

		//���������Ҫ��ָ����ʱ�̷�һ����Ϣ
        public void computerRunAfter(long delayMillis) {
        	this.removeMessages(0);
        	//����Ϣ����handleMessage����
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
        
        //�յ���Ϣ
        @Override
        public void handleMessage(Message msg) {
        	//������һ������
    		player2.run(player1.getMyPoints(),null);
    		//ˢ��һ��
    		refressCanvas();
    		if(!player2.hasWin()){
    			//����
    			setPlayer1Run();
    		}else{//�ڶ������Ӯ��
    			setMode(PLAYER_ONE_LOST);
    		}
        }
    };
	
    //�Ƿ�������ĳһ�������У����ǵ�������ʱ��Ҫ�ϳ��ļ���ʱ�䣬���ڼ�һ������������Ӧ�����¼�
	private boolean onProcessing() {
		return whoRun == -1;
	}


	//Ĭ�ϵ�һ���������
	private int whoRun = 1;
	private void setPlayer1Run(){
		whoRun = 1;
	}
	private void setOnProcessing(){
		whoRun = -1;
	}
	//�Ƿ��ֵ������������
	private boolean isPlayer1Run(){
		return whoRun==1;
	}
	
	//�Ƿ��ֵ������������
	private boolean isPlayer2Run(){
		return whoRun==2;
	}
	
	private void setPlayer2Run(){
		whoRun = 2;
	}
	
	private void refressCanvas(){
		//����onDraw����
        Chessboard.this.invalidate();
	}
	
	private void drawPlayer1Point(Canvas canvas){
		int size = player1.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player1.getMyPoints().get(i), GREEN);
		}
		//����µ�һ�����ɻ�ɫ
		drawPoint(canvas, player1.getMyPoints().get(size), NEW_GREEN);
	}
	
	private void drawPlayer2Point(Canvas canvas){
		if(player2==null){
			return ;
		}
		int size = player2.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player2.getMyPoints().get(i), RED);
		}
		//����µ�һ�����ɻ�ɫ
		drawPoint(canvas, player2.getMyPoints().get(size), NEW_RED);
	}
    
	
	//��ʼ����������ɫ�ĵ�
    public void fillPointArrays(int color,Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(pointSize, pointSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, pointSize, pointSize);
        drawable.draw(canvas);
        pointArray[color] = bitmap;
    }
    
    //doRun�����������ǿ�������ڴ���ݣ��˷������������ͼ���ķ�ʽ���ֳ��������Ի�֮ǰ���һ��Ҫ��׼����
    @Override
    protected void onDraw(Canvas canvas) {
    	drawChssboardLines(canvas);
    	//��������ڵĵ�
    	drawPlayer1Point(canvas);
    	//�������µ�����
    	drawPlayer2Point(canvas);
    }


	public List<Point> getFreePoints() {
		return allFreePoints;
	}
	
	//��ʼ���հ׵㼯��
	private void createPoints(){
		allFreePoints.clear();
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				allFreePoints.add(new Point(i, j));
			}
		}
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}
    
}
