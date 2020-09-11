package com.example.jenga;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.Format;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JengaActivity extends AppCompatActivity{

    Database database;

    private TextView textView,timer;
    private Button button,button1,button2,btnEdit;
    private ImageView diceImg1;
    private ImageView diceImg2;
    ArrayList<ListItem> listItems = new ArrayList<>();
    Random random = new Random();
    int ranID;

    public static final int REQUEST_CODE_ADD = 999;
    public static final int REQUEST_CODE_EDIT = 998;
    public static final int RESULT_CODE_ADD = 100;
    public static final int RESULT_CODE_EDIT = 101;
    public static final int RESULT_CODE_DELETE = 102;
    public static final long TIMELEFT_DEFAULT = 5100;
    public long timeLeft = TIMELEFT_DEFAULT;

    //MAIN FUNCTION
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //database
        database = new Database(this,"jenga.sqlite",null,1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Jenga(Id INTEGER PRIMARY KEY AUTOINCREMENT, Quote VARCHAR, Dice BOOLEAN, Timer BIGINT)");
//        database.QueryData("INSERT INTO Jenga VALUES(null,'Liệt kê 3 điểm mạnh của người bên trái trong 5 giây',false,5)");
//        database.QueryData("INSERT INTO Jenga VALUES(null,'Chọn 1 người trong nhóm để đổ xúc xắc và đoán chính xác tổng của chúng trong 3 lần thử. Bạn được hỏi gợi ý và người kia được phải trả lời thật nhưng không cần đầy đủ',true,0)");
        loadData();
        //view para
        init();
        if (database ==null){
            button.setEnabled(false);
        }else{
            button.setEnabled(true);
        }
        //BUTTON
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    button.setText("Next");
                    randomGeneration();
                    btnEdit.setEnabled(true);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                countdown();
            }
        });


    }

    public void loadData (){
        listItems.clear();
        Cursor dataLoad = database.GetData("SELECT * FROM Jenga");
        while (dataLoad.moveToNext()){
            int id =dataLoad.getInt(0);
            String content = dataLoad.getString(1);
            Boolean diceC = dataLoad.getInt(2)>0;
            Long seconds = dataLoad.getLong(3);
            listItems.add(new ListItem(id,content,diceC,seconds));
//            showToast(dataLoad.getString(1));
        }
    }
    public void init(){
        textView = (TextView) findViewById(R.id.text);
        timer = (TextView) findViewById(R.id.timer);
        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        btnEdit = (Button) findViewById(R.id.edit);
        diceImg1 = (ImageView) findViewById(R.id.dice1);
        diceImg2 = (ImageView) findViewById(R.id.dice2);
//        listQuotes.add("Chỉ được kêu “gâu gâu” cho đến lượt tiếp theo");
//        listQuotes.add("Chỉ được kêu “gâu gâu” cho đến lượt tiếp theo");
//        listQuotes.add("Người bên trái và người bên phải chơi kéo búa bao (được quyền thảo luận kết quả). Cược kết quả");
//        listQuotes.add("Nếu được làm 1 con vật, bạn sẽ làm con vật gì. Miêu tả về con vật đó bằng hành động cho cả nhóm");
//        listQuotes.add("Đưa 2 lựa chọn cho 2 người bất kì. Nếu họ cùng lúc chọn trùng nhau, bạn bị phạt");
//        listQuotes.add("Nói 1 câu thả thính người ngồi đối diện");
//        listQuotes.add("Chọn 1-3 tương ứng đồ vật được nhóm chọn và đánh số để đong shot rượu/nước");
//        listQuotes.add("Chơi kéo búa bao thua với người bên trái. Ai thua sẽ bị phạt");
//        listQuotes.add("Đổ xúc xắc. Thắng nếu đạt số >3");
//        listQuotes.add("Vẽ 1 hình minh họa 1 bài hát và 1 ca sĩ cho nhóm. Ai đoán đúng được miễn phạt");
//        listQuotes.add("Hỏi mọi người 1 món quà thích nhất được tặng từ người yêu/ex");
//        listQuotes.add("Hỏi mọi người đâu là bộ phận quyến rũ nhất của bạn");
//        listQuotes.add("Mọi người nêu 1 tật xấu của người bên trái với mình");
//        listQuotes.add("Khen 1 câu với người bên phải");
//        listQuotes.add("Câu nói nào của người yêu/ex làm bạn phê đê mê nhất?");
//        listQuotes.add("Miễn 1 lần bị phạt");
//        listQuotes.add("Đút thanh gỗ ngược lại vào tháp (được phép dùng tối đa 3 ngón tay)");
//        listQuotes.add("Giữ lại thanh gỗ cho tới khi có người tiếp theo bị phạt và đổi chỗ với người đó");
//        listQuotes.add("Đổi chỗ 2 người bất kì");
//        listQuotes.add("Đặt thanh gỗ nằm đứng thay vì nằm ngang");
//        listQuotes.add("Thắng 3 trận Champ-champ-champ liên tiếp với 3 người trong nhóm");
//        listQuotes.add("Cả nhóm chơi 3-6-9. Ai thua bị phạt");
//        listQuotes.add("Món đồ đắt nhất từng mua là gì?");
//        listQuotes.add("Người có tổng các chữ số trong số điện thoại sẽ bị phạt");
//        listQuotes.add("Liệt kê 3 điểm mạnh của người bên trái trong 5 giây");
//        listQuotes.add("Chọn 1 người trong nhóm để đổ xúc xắc và đoán chính xác tổng của chúng trong 3 lần thử. Bạn được hỏi gợi ý và người kia được phải trả lời thật nhưng không cần đầy đủ");
//        listQuotes.add("Bạn hãy quảng cáo trong vòng 1 phút cho 1 đồ vật bất kì do nhóm chọn mà không được biết đồ vật ấy là gì. Có thể bắt đầu bằng “Đây là 1 sản phẩm rất hữu dụng cho…”");
//        listQuotes.add("Hát 1 câu có chữ cái bắt đầu là chữ cái của tên 1 trong 2 người bên cạnh");
//        listQuotes.add("Hát 1 câu có chữ 'khóc'");
//        listQuotes.add("Hát 1 câu có chữ 'yêu'");

    }
//ADD BTN
    public void addQuote(View view){
        Intent myIntent = new Intent(JengaActivity.this,AddQuote.class);
        startActivityForResult(myIntent,REQUEST_CODE_ADD);
    }

//EDIT BTN
    public void editQuote(View view){
        Intent myIntent = new Intent(JengaActivity.this,EditQuote.class);
        ListItem item = listItems.get(ranID);
        myIntent.putExtra("item", item);
        startActivityForResult(myIntent,REQUEST_CODE_EDIT);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD){
            if (resultCode == RESULT_CODE_ADD){
                Bundle addBundle = data.getBundleExtra("addBundle");
                String quote = addBundle.getString("quote","Câu hỏi");
                Boolean diceChecked = addBundle.getBoolean("diceChecked",false);
                String seconds = addBundle.getString("seconds","0");
                database.QueryData("INSERT INTO Jenga VALUES(null,'"+quote+"',"+diceChecked+","+Long.parseLong(seconds)+")");
//                textView.setText(quote+diceChecked.toString()+seconds);
                defaultState();
                loadData();
                showToast("ADDED");
            }

        }
        if (requestCode == REQUEST_CODE_EDIT){
            if (resultCode == RESULT_CODE_EDIT){
                Bundle editBundle = data.getBundleExtra("editBundle");
                String quote = editBundle.getString("quote","Câu hỏi");
                Boolean diceChecked = editBundle.getBoolean("diceChecked",false);
                String seconds = editBundle.getString("seconds","0");
                database.QueryData("UPDATE Jenga SET Quote = '"+quote+"',Dice = '"+diceChecked+"',Timer = '"+Long.parseLong(seconds)+"' WHERE Id = '"+listItems.get(ranID).getId()+"'");
//                textView.setText(quote+diceChecked.toString()+seconds);
                defaultState();
                loadData();
                showToast("EDITED");
            }
            if (resultCode ==RESULT_CODE_DELETE){
                database.QueryData("DELETE FROM Jenga WHERE Id = '"+listItems.get(ranID).getId()+"'");
//                textView.setText(String.valueOf(listItems.get(ranID).getId()));
                defaultState();
                loadData();
                showToast("DELETED");
            }
        }
    }

//EDIT BTN
//CHILD FUNCTION
    private void randomGeneration(){
        CountDownTimer cTimer = new CountDownTimer(510,100) {
            @Override
            public void onTick(long l) {
                ranID = random.nextInt(listItems.size());
                textView.setTextColor(Color.rgb(random.nextInt(265),random.nextInt(265),random.nextInt(265)));
//                textView.setText(listQuotes.get(ranID).toString());
                textView.setText(listItems.get(ranID).getContent().toString());
            }

            @Override
            public void onFinish() {

                timer.setVisibility(View.INVISIBLE);
                diceImg1.setVisibility(View.INVISIBLE);
                diceImg2.setVisibility(View.INVISIBLE);

//                if(listDice.get(ranID)==true){
                if (listItems.get(ranID).isDiceChecked()){
                    button.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    button1.setClickable(true);
                    diceImg1.setVisibility(View.VISIBLE);
                    diceImg2.setVisibility(View.VISIBLE);
                }

//                if(listTime.get(ranID)!=0){
                if (listItems.get(ranID).getSeconds()!=0){
                    button.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button2.setClickable(true);
                    timer.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }
    private void rollDice(){
        CountDownTimer cTimer = new CountDownTimer(2100,100) {
            @Override
            public void onTick(long l) {
                int dice1 = random.nextInt(5)+1;
                int dice2 = random.nextInt(5)+1;
                int id1 = getResources().getIdentifier("dice_"+dice1,"drawable",getPackageName());
                int id2 = getResources().getIdentifier("dice_"+dice2,"drawable",getPackageName());
                diceImg1.setImageResource(id1);
                diceImg2.setImageResource(id2);
            }

            @Override
            public void onFinish() {
                button1.setVisibility(View.INVISIBLE);
                button1.setClickable(false);
                button.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    private void countdown(){
//        timeLeft = listTime.get(ranID)*1000+100;
        timeLeft = listItems.get(ranID).getSeconds()*1000+100;
        CountDownTimer cTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                timer.setText(""+String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeLeft),TimeUnit.MILLISECONDS.toSeconds(timeLeft)));
            }

            @Override
            public void onFinish() {
                button2.setVisibility(View.INVISIBLE);
                button2.setClickable(false);
                button.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void defaultState(){
        textView.setText("Câu hỏi");
        button.setVisibility(View.VISIBLE);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        diceImg2.setVisibility(View.INVISIBLE);
        diceImg1.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.INVISIBLE);
    }
    public void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
}