package com.example.calculatorforprogrammer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.market.sdk.UpdateResponse;
import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.xiaomi.market.sdk.XiaomiUpdateListener;


public class MainActivity extends Activity {
    long a = 0;
    long tempNum = 0;                                     //a计算变量 tempNum计算辅助变量
    int isCalculating = 0;                              //存储当前是否处于计算中,1:+   2:-  3:*   4:/   5:%   6:|   7:^   8:&   9:+-   10:!
    int bitLength = 256;
    int radixMode = 10;                                 //存储当期的进制模式
    int isJustFinish = 0;                               //当前是否刚刚完成一轮计算
    TextView mainNumView,equationView;                  //主显示文本框
    String stringTemp;                                  //显示前处理临时字符串
    String equationString = "";                              //用于存储equation_view系那是内容的临时字符串
    LinearLayout hexViewLinearLayoutButton,decViewLinearLayoutButton,octViewLinearLayoutButton,binViewLinearLayoutButton;   //进制切换表格行按钮
    TextView hexViewButton,hexView,decViewButton,decView,octViewButton,octView,binViewButton,binView;   //进制显示
    TextView hexOnView,decOnView,octOnView,binOnView;       //进制指示条
    Button numKeyboardButton,bitKeyboardButton;            //键盘切换按钮
    Button bitLengthButton;                                     //位长度切换按钮
    ImageView numKeyboardOnView,bitKeyboardOnView;              //键盘彩色指示条
    Button orButton,xorButton,notButton,andButton,modButton;    //逻辑运算按钮
    Button set0Button,set1Button;                               //二进制置零置一按钮
    FrameLayout numKeyboard,bitKeyboard;                        //键盘布局管理器
    Button ceButton,clearButton,clearOneButton;                                           //CE，C，清除按钮
    Button numAButton,numBButton,numCButton,numDButton,numEButton,numFButton;                           //十六进制数按钮
    Button num0Button,num1Button,num2Button,num3Button,num4Button,num5Button;
    Button num6Button,num7Button,num8Button,num9Button,bracketLeftButton,bracketRightButton;         //数字、符号按钮
    Button mulButton,subButton,addButton,equalButton,oppButton,divideButton;                            //运算符按钮
    Button bit0,bit1,bit2,bit3,bit4,bit5,bit6,bit7,bit8,bit9,bit10,bit11,bit12,bit13,bit14,bit15;
    Button bit16,bit17,bit18,bit19,bit20,bit21,bit22,bit23,bit24,bit25,bit26,bit27,bit28,bit29,bit30,bit31;
    Button bit32,bit33,bit34,bit35,bit36,bit37,bit38,bit39,bit40,bit41,bit42,bit43,bit44,bit45,bit46,bit47;
    Button bit48,bit49,bit50,bit51,bit52,bit53,bit54,bit55,bit56,bit57,bit58,bit59,bit60,bit61,bit62,bit63;//位键盘按钮
    TextView bit_flag_60,bit_flag_56,bit_flag_52,bit_flag_48;
    TextView bit_flag_44,bit_flag_40,bit_flag_36,bit_flag_32;
    TextView bit_flag_28,bit_flag_24,bit_flag_20,bit_flag_16;
    TextView bit_flag_12,bit_flag_8,bit_flag_4,bit_flag_0;                          //位标志文本框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);XiaomiUpdateAgent.setUpdateAutoPopup(false);
        XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.STATUS_UPDATE:
                        // 有更新， UpdateResponse为本次更新的详细信息
                        // 其中包含更新信息，下载地址，MD5校验信息等，可自行处理下载安装
                        // 如果希望 SDK继续接管下载安装事宜，可调用
                        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                        alert.setIcon(R.drawable.icon_30);
                        alert.setTitle(getResources().getString(R.string.app_name));
                        alert.setMessage(getResources().getString(R.string.checked_new_alert_message));
                        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_update_yes_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                XiaomiUpdateAgent.arrange();
                            }
                        });
                        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                        break;
                    case UpdateStatus.STATUS_NO_UPDATE:
                        // 无更新， UpdateResponse为null
                        //Toast.makeText(MainActivity.this, getResources().getString(R.string.checked_updated), Toast.LENGTH_LONG).show();
                        break;
                    case UpdateStatus.STATUS_NO_WIFI:
                        // 设置了只在WiFi下更新，且WiFi不可用时， UpdateResponse为null
                        break;
                    case UpdateStatus.STATUS_NO_NET:
                        // 没有网络， UpdateResponse为null
                        //Toast.makeText(MainActivity.this, getResources().getString(R.string.checked_no_network), Toast.LENGTH_LONG).show();
                        break;
                    case UpdateStatus.STATUS_FAILED:
                        // 检查更新与服务器通讯失败，可稍后再试， UpdateResponse为null
                        //Toast.makeText(MainActivity.this, getResources().getString(R.string.checked_no_server), Toast.LENGTH_LONG).show();
                        break;
                    case UpdateStatus.STATUS_LOCAL_APP_FAILED:
                        // 检查更新获取本地安装应用信息失败， UpdateResponse为null
                        //Toast.makeText(MainActivity.this, getResources().getString(R.string.checked_error), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
        XiaomiUpdateAgent.update(this);
        initActivity();
        freshAllView(tempNum);
    }

    public void onClickNumKeyboardButton(View view){
        numKeyboardButton.setTextColor(getResources().getColor(R.color.blue));
        bitKeyboardButton.setTextColor(getResources().getColor(R.color.black));
        numKeyboardOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        bitKeyboardOnView.setBackgroundColor(getResources().getColor(R.color.gray_two));
        equalButton.setBackgroundColor(getResources().getColor(R.color.blue));
        bitKeyboard.setVisibility(View.GONE);
        numKeyboard.setVisibility(View.VISIBLE);
        orButton.setVisibility(View.VISIBLE);
        xorButton.setVisibility(View.VISIBLE);
        andButton.setVisibility(View.VISIBLE);
        modButton.setVisibility(View.VISIBLE);
        set0Button.setVisibility(View.GONE);
        set1Button.setVisibility(View.GONE);

    }
    public void onClickBitKeyboardButton(View view){
        isJustFinish = 0;
        numKeyboardButton.setTextColor(getResources().getColor(R.color.black));
        bitKeyboardButton.setTextColor(getResources().getColor(R.color.blue));
        numKeyboardOnView.setBackgroundColor(getResources().getColor(R.color.gray_two));
        bitKeyboardOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        bitKeyboard.setVisibility(View.VISIBLE);
        numKeyboard.setVisibility(View.GONE);
        if(isCalculating == 0)
            freshBitKeyboard(tempNum);
        else
            freshBitKeyboard(a);

        orButton.setVisibility(View.GONE);
        xorButton.setVisibility(View.GONE);
        andButton.setVisibility(View.GONE);
        modButton.setVisibility(View.GONE);
        set0Button.setVisibility(View.VISIBLE);
        set1Button.setVisibility(View.VISIBLE);
    }
    public void tuneMainText(){
        if(40 <= mainNumView.getText().length()){
            mainNumView.setTextSize(20);
        }else {
            mainNumView.setTextSize(30);
        }
    }

    public String numViewTreatment(String s,int radix){
        stringTemp = "";
        int i;
        int firstNum;
        if(radix == 16 || radix == 2 ){
            if(s.length()<=4) return s;
            else{
                firstNum = s.length()%4;
                if(radix == 2 && firstNum != 0){
                    int j = 0;
                    for(;j<(4-firstNum);j++){
                        stringTemp = stringTemp + "0";
                    }
                }
                if(firstNum != 0)
                    stringTemp = stringTemp + s.substring(0,firstNum)+" " +s.substring(firstNum,firstNum+4);
                else
                    stringTemp = s.substring(0,4);
                i = firstNum+4;
                for(;i<s.length();i+=4){
                    stringTemp = stringTemp + " " +s.substring(i,i+4);
                }
                if(stringTemp.length()>40){
                    stringTemp = stringTemp.substring(0,39) + "\n" + stringTemp.substring(40);
                }
            }
        }else if(radix == 10){
            if(s.length()<=3) return s;
            else{
                firstNum = s.length()%3;
                if(firstNum != 0)
                    if(s.substring(0,firstNum).equals("-"))
                        stringTemp = s.substring(0,firstNum) + s.substring(firstNum,firstNum+3);
                    else
                        stringTemp = s.substring(0,firstNum)+"," +s.substring(firstNum,firstNum+3);
                else
                    stringTemp = s.substring(0,3);
                i = firstNum+3;
                for(;i<s.length();i+=3){
                    stringTemp = stringTemp + "," +s.substring(i,i+3);
                }
            }

        }else {
            if(s.length()<=3) return s;
            else{
                firstNum = s.length()%3;
                if(firstNum != 0)
                    stringTemp = s.substring(0,firstNum)+" " +s.substring(firstNum,firstNum+3);
                else
                    stringTemp = s.substring(0,3);
                i = firstNum+3;
                for(;i<s.length();i+=3){
                    stringTemp = stringTemp + " " +s.substring(i,i+3);
                }
            }
        }
        return stringTemp;
        //return s;
    }

    public void freshRadixView(long a){
        hexView.setText(numViewTreatment(Long.toHexString(a).toUpperCase(), 16));
        decView.setText(numViewTreatment(Long.toString(a),10));
        octView.setText(numViewTreatment(Long.toOctalString(a), 8));
        binView.setText(numViewTreatment(Long.toBinaryString(a), 2));
    }
    public void freshMainNumView(){
        freshEquationView();
        if(radixMode == 16){
            mainNumView.setText(hexView.getText());
        }else if(radixMode == 10){
            mainNumView.setText(decView.getText());
        }else if(radixMode == 8){
            mainNumView.setText(octView.getText());
        }else{
            mainNumView.setText(binView.getText());
        }
        tuneMainText();
    }
    public void freshAllView(long a){
        freshRadixView(a);
        freshMainNumView();
    }

    public void freshEquationView(){
        if(isCalculating == 0){ equationView.setText(""); return ;}
        if(radixMode == 16){
            equationString = Long.toHexString(a).toUpperCase();
        }else if(radixMode == 10){
            equationString = Long.toString(a);
        }else if(radixMode == 8){
            equationString = Long.toOctalString(a);
        }else if(radixMode == 2){
            equationString = Long.toBinaryString(a);
        }
        if(isCalculating == '+'){
            equationString = equationString+ " " + getResources().getString(R.string.add_button);
        }else if(isCalculating == '-'){
            equationString = equationString+ " " + getResources().getString(R.string.sub_button);
        }else if(isCalculating == '*'){
            equationString = equationString+ " " + getResources().getString(R.string.mul_button);
        }else if(isCalculating == '/'){
            equationString = equationString+ " " + getResources().getString(R.string.divide_button);
        }else if(isCalculating == '%'){
            equationString = equationString+ " " + getResources().getString(R.string.mod_button);
        }else if(isCalculating == '|'){
            equationString = equationString+ " " + getResources().getString(R.string.or_button);
        }else if(isCalculating == '^'){
            equationString = equationString+ " " + getResources().getString(R.string.xor_button);
        }else if(isCalculating == '&'){
            equationString = equationString+ " " + getResources().getString(R.string.and_button);
        }
        equationView.setText(equationString);
    }

    public void onClickSwitchHEX(View view){
        radixMode = 16;
        freshMainNumView();
        tuneMainText();
        hexViewButton.setTextColor(getResources().getColor(R.color.blue));
        hexView.setTextColor(getResources().getColor(R.color.blue));

        decViewButton.setTextColor(getResources().getColor(R.color.black));
        decView.setTextColor(getResources().getColor(R.color.black));

        octViewButton.setTextColor(getResources().getColor(R.color.black));
        octView.setTextColor(getResources().getColor(R.color.black));

        binViewButton.setTextColor(getResources().getColor(R.color.black));
        binView.setTextColor(getResources().getColor(R.color.black));

        hexOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        decOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        octOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        binOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));

        numAButton.setTextColor(getResources().getColor(R.color.black));
        numBButton.setTextColor(getResources().getColor(R.color.black));
        numCButton.setTextColor(getResources().getColor(R.color.black));
        numDButton.setTextColor(getResources().getColor(R.color.black));
        numEButton.setTextColor(getResources().getColor(R.color.black));
        numFButton.setTextColor(getResources().getColor(R.color.black));
        num8Button.setTextColor(getResources().getColor(R.color.black));
        num9Button.setTextColor(getResources().getColor(R.color.black));
        num2Button.setTextColor(getResources().getColor(R.color.black));
        num3Button.setTextColor(getResources().getColor(R.color.black));
        num4Button.setTextColor(getResources().getColor(R.color.black));
        num5Button.setTextColor(getResources().getColor(R.color.black));
        num6Button.setTextColor(getResources().getColor(R.color.black));
        num7Button.setTextColor(getResources().getColor(R.color.black));
        num1Button.setTextColor(getResources().getColor(R.color.black));
        num0Button.setTextColor(getResources().getColor(R.color.black));
    }
    public void onClickSwitchDEC(View view){
        radixMode = 10;
        freshMainNumView();
        tuneMainText();
        hexViewButton.setTextColor(getResources().getColor(R.color.black));
        hexView.setTextColor(getResources().getColor(R.color.black));
        decViewButton.setTextColor(getResources().getColor(R.color.blue));
        decView.setTextColor(getResources().getColor(R.color.blue));
        octViewButton.setTextColor(getResources().getColor(R.color.black));
        octView.setTextColor(getResources().getColor(R.color.black));
        binViewButton.setTextColor(getResources().getColor(R.color.black));
        binView.setTextColor(getResources().getColor(R.color.black));


        hexOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        decOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        octOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        binOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        numAButton.setTextColor(getResources().getColor(R.color.gray_word));
        numBButton.setTextColor(getResources().getColor(R.color.gray_word));
        numCButton.setTextColor(getResources().getColor(R.color.gray_word));
        numDButton.setTextColor(getResources().getColor(R.color.gray_word));
        numEButton.setTextColor(getResources().getColor(R.color.gray_word));
        numFButton.setTextColor(getResources().getColor(R.color.gray_word));
        num8Button.setTextColor(getResources().getColor(R.color.black));
        num9Button.setTextColor(getResources().getColor(R.color.black));
        num2Button.setTextColor(getResources().getColor(R.color.black));
        num3Button.setTextColor(getResources().getColor(R.color.black));
        num4Button.setTextColor(getResources().getColor(R.color.black));
        num5Button.setTextColor(getResources().getColor(R.color.black));
        num6Button.setTextColor(getResources().getColor(R.color.black));
        num7Button.setTextColor(getResources().getColor(R.color.black));
        num1Button.setTextColor(getResources().getColor(R.color.black));
        num0Button.setTextColor(getResources().getColor(R.color.black));
    }
    public void onClickSwitchOCT(View view){
        radixMode = 8;
        freshMainNumView();
        //mainNumView.setText(octView.getText().toString());
        tuneMainText();
        hexViewButton.setTextColor(getResources().getColor(R.color.black));
        hexView.setTextColor(getResources().getColor(R.color.black));
        decViewButton.setTextColor(getResources().getColor(R.color.black));
        decView.setTextColor(getResources().getColor(R.color.black));
        octViewButton.setTextColor(getResources().getColor(R.color.blue));
        octView.setTextColor(getResources().getColor(R.color.blue));
        binViewButton.setTextColor(getResources().getColor(R.color.black));
        binView.setTextColor(getResources().getColor(R.color.black));


        hexOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        decOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        octOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        binOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        numAButton.setTextColor(getResources().getColor(R.color.gray_word));
        numBButton.setTextColor(getResources().getColor(R.color.gray_word));
        numCButton.setTextColor(getResources().getColor(R.color.gray_word));
        numDButton.setTextColor(getResources().getColor(R.color.gray_word));
        numEButton.setTextColor(getResources().getColor(R.color.gray_word));
        numFButton.setTextColor(getResources().getColor(R.color.gray_word));
        num8Button.setTextColor(getResources().getColor(R.color.gray_word));
        num9Button.setTextColor(getResources().getColor(R.color.gray_word));
        num2Button.setTextColor(getResources().getColor(R.color.black));
        num3Button.setTextColor(getResources().getColor(R.color.black));
        num4Button.setTextColor(getResources().getColor(R.color.black));
        num5Button.setTextColor(getResources().getColor(R.color.black));
        num6Button.setTextColor(getResources().getColor(R.color.black));
        num7Button.setTextColor(getResources().getColor(R.color.black));
        num1Button.setTextColor(getResources().getColor(R.color.black));
        num0Button.setTextColor(getResources().getColor(R.color.black));
    }
    public void onClickSwitchBIN(View view){
        radixMode = 2;
        freshMainNumView();
        //mainNumView.setText(binView.getText().toString());
        tuneMainText();
        hexViewButton.setTextColor(getResources().getColor(R.color.black));
        hexView.setTextColor(getResources().getColor(R.color.black));
        decViewButton.setTextColor(getResources().getColor(R.color.black));
        decView.setTextColor(getResources().getColor(R.color.black));
        octViewButton.setTextColor(getResources().getColor(R.color.black));
        octView.setTextColor(getResources().getColor(R.color.black));
        binViewButton.setTextColor(getResources().getColor(R.color.blue));
        binView.setTextColor(getResources().getColor(R.color.blue));

        hexOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        decOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        octOnView.setBackgroundColor(getResources().getColor(R.color.gray_one));
        binOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        numAButton.setTextColor(getResources().getColor(R.color.gray_word));
        numBButton.setTextColor(getResources().getColor(R.color.gray_word));
        numCButton.setTextColor(getResources().getColor(R.color.gray_word));
        numDButton.setTextColor(getResources().getColor(R.color.gray_word));
        numEButton.setTextColor(getResources().getColor(R.color.gray_word));
        numFButton.setTextColor(getResources().getColor(R.color.gray_word));
        num8Button.setTextColor(getResources().getColor(R.color.gray_word));
        num9Button.setTextColor(getResources().getColor(R.color.gray_word));
        num2Button.setTextColor(getResources().getColor(R.color.gray_word));
        num3Button.setTextColor(getResources().getColor(R.color.gray_word));
        num4Button.setTextColor(getResources().getColor(R.color.gray_word));
        num5Button.setTextColor(getResources().getColor(R.color.gray_word));
        num6Button.setTextColor(getResources().getColor(R.color.gray_word));
        num7Button.setTextColor(getResources().getColor(R.color.gray_word));
        num1Button.setTextColor(getResources().getColor(R.color.black));
        num0Button.setTextColor(getResources().getColor(R.color.black));
    }
    public void initActivity(){
        mainNumView = (TextView)findViewById(R.id.main_num_view);
        equationView = (TextView)findViewById(R.id.equation_view);                   //主显示文本框
        hexViewLinearLayoutButton = (LinearLayout)findViewById(R.id.hex_view_linear_layout);
        decViewLinearLayoutButton = (LinearLayout)findViewById(R.id.dec_view_linear_layout);
        octViewLinearLayoutButton = (LinearLayout)findViewById(R.id.oct_view_linear_layout);
        binViewLinearLayoutButton = (LinearLayout)findViewById(R.id.bin_view_linear_layout);    //进制切换表格行按钮
        hexViewButton = (TextView)findViewById(R.id.hex_view_button);
        hexView = (TextView)findViewById(R.id.hex_view);
        decViewButton = (TextView)findViewById(R.id.dec_view_button);
        decView = (TextView)findViewById(R.id.dec_view);
        octViewButton = (TextView)findViewById(R.id.oct_view_button);
        octView = (TextView)findViewById(R.id.oct_view);
        binViewButton = (TextView)findViewById(R.id.bin_view_button);
        binView = (TextView)findViewById(R.id.bin_view);                            //进制显示
        hexOnView = (TextView)findViewById(R.id.hex_on_view);
        decOnView = (TextView)findViewById(R.id.dec_on_view);
        octOnView = (TextView)findViewById(R.id.oct_on_view);
        binOnView = (TextView)findViewById(R.id.bin_on_view);                       //进制指示条
        numKeyboardButton = (Button)findViewById(R.id.num_keyboard_button);
        bitKeyboardButton = (Button)findViewById(R.id.bit_keyboard_button);
        bitLengthButton = (Button)findViewById(R.id.bit_length_button);             //位长度切换按钮
        numKeyboardOnView = (ImageView)findViewById(R.id.num_keyboard_on_view);
        bitKeyboardOnView = (ImageView)findViewById(R.id.bit_keyboard_on_view);     //彩色指示条
        orButton = (Button)findViewById(R.id.or_button);
        xorButton = (Button)findViewById(R.id.xor_button);
        notButton = (Button)findViewById(R.id.not_button);
        andButton = (Button)findViewById(R.id.and_button);
        modButton = (Button)findViewById(R.id.mod_button);                          //逻辑运算按钮
        set0Button = (Button)findViewById(R.id.set_0_button);
        set1Button = (Button)findViewById(R.id.set_1_button);                       //二进制置零置一按钮
        numKeyboard = (FrameLayout)findViewById(R.id.num_keyboard);
        bitKeyboard = (FrameLayout)findViewById(R.id.bit_keyboard);                 //键盘布局管理器
        ceButton = (Button)findViewById(R.id.ce_button);
        clearButton = (Button)findViewById(R.id.clear_button);
        clearOneButton = (Button)findViewById(R.id.clear_one_button);          //清除按钮
        numAButton = (Button)findViewById(R.id.num_a_button);
        numBButton = (Button)findViewById(R.id.num_b_button);
        numCButton = (Button)findViewById(R.id.num_c_button);
        numDButton = (Button)findViewById(R.id.num_d_button);
        numEButton = (Button)findViewById(R.id.num_e_button);
        numFButton = (Button)findViewById(R.id.num_f_button);                       //十六进制数按钮
        num0Button = (Button)findViewById(R.id.num_0_button);
        num1Button = (Button)findViewById(R.id.num_1_button);
        num2Button = (Button)findViewById(R.id.num_2_button);
        num3Button = (Button)findViewById(R.id.num_3_button);
        num4Button = (Button)findViewById(R.id.num_4_button);
        num5Button = (Button)findViewById(R.id.num_5_button);
        num6Button = (Button)findViewById(R.id.num_6_button);
        num7Button = (Button)findViewById(R.id.num_7_button);
        num8Button = (Button)findViewById(R.id.num_8_button);
        num9Button = (Button)findViewById(R.id.num_9_button);
        bracketLeftButton = (Button)findViewById(R.id.bracket_left_button);
        bracketRightButton = (Button)findViewById(R.id.bracket_right_button);       //数字符号按钮
        mulButton = (Button)findViewById(R.id.mul_button);
        subButton = (Button)findViewById(R.id.sub_button);
        addButton = (Button)findViewById(R.id.add_button);
        equalButton = (Button)findViewById(R.id.equal_button);
        oppButton = (Button)findViewById(R.id.opp_button);
        divideButton = (Button)findViewById(R.id.divide_button);                    //运算符按钮
        bit0 = (Button)findViewById(R.id.bit0);
        bit1= (Button)findViewById(R.id.bit1);
        bit2= (Button)findViewById(R.id.bit2);
        bit3= (Button)findViewById(R.id.bit3);
        bit4= (Button)findViewById(R.id.bit4);
        bit5= (Button)findViewById(R.id.bit5);
        bit6= (Button)findViewById(R.id.bit6);
        bit7= (Button)findViewById(R.id.bit7);
        bit8= (Button)findViewById(R.id.bit8);
        bit9= (Button)findViewById(R.id.bit9);
        bit10= (Button)findViewById(R.id.bit10);
        bit11= (Button)findViewById(R.id.bit11);
        bit12= (Button)findViewById(R.id.bit12);
        bit13= (Button)findViewById(R.id.bit13);
        bit14= (Button)findViewById(R.id.bit14);
        bit15= (Button)findViewById(R.id.bit15);
        bit16= (Button)findViewById(R.id.bit16);
        bit17= (Button)findViewById(R.id.bit17);
        bit18= (Button)findViewById(R.id.bit18);
        bit19= (Button)findViewById(R.id.bit19);
        bit20= (Button)findViewById(R.id.bit20);
        bit21= (Button)findViewById(R.id.bit21);
        bit22= (Button)findViewById(R.id.bit22);
        bit23= (Button)findViewById(R.id.bit23);
        bit24= (Button)findViewById(R.id.bit24);
        bit25= (Button)findViewById(R.id.bit25);
        bit26= (Button)findViewById(R.id.bit26);
        bit27= (Button)findViewById(R.id.bit27);
        bit28= (Button)findViewById(R.id.bit28);
        bit29= (Button)findViewById(R.id.bit29);
        bit30= (Button)findViewById(R.id.bit30);
        bit31= (Button)findViewById(R.id.bit31);
        bit32= (Button)findViewById(R.id.bit32);
        bit33= (Button)findViewById(R.id.bit33);
        bit34= (Button)findViewById(R.id.bit34);
        bit35= (Button)findViewById(R.id.bit35);
        bit36= (Button)findViewById(R.id.bit36);
        bit37= (Button)findViewById(R.id.bit37);
        bit38= (Button)findViewById(R.id.bit38);
        bit39= (Button)findViewById(R.id.bit39);
        bit40= (Button)findViewById(R.id.bit40);
        bit41= (Button)findViewById(R.id.bit41);
        bit42= (Button)findViewById(R.id.bit42);
        bit43= (Button)findViewById(R.id.bit43);
        bit44= (Button)findViewById(R.id.bit44);
        bit45= (Button)findViewById(R.id.bit45);
        bit46= (Button)findViewById(R.id.bit46);
        bit47= (Button)findViewById(R.id.bit47);
        bit48= (Button)findViewById(R.id.bit48);
        bit49= (Button)findViewById(R.id.bit49);
        bit50= (Button)findViewById(R.id.bit50);
        bit51= (Button)findViewById(R.id.bit51);
        bit52= (Button)findViewById(R.id.bit52);
        bit53= (Button)findViewById(R.id.bit53);
        bit54= (Button)findViewById(R.id.bit54);
        bit55= (Button)findViewById(R.id.bit55);
        bit56= (Button)findViewById(R.id.bit56);
        bit57= (Button)findViewById(R.id.bit57);
        bit58= (Button)findViewById(R.id.bit58);
        bit59= (Button)findViewById(R.id.bit59);
        bit60= (Button)findViewById(R.id.bit60);
        bit61= (Button)findViewById(R.id.bit61);
        bit62= (Button)findViewById(R.id.bit62);
        bit63= (Button)findViewById(R.id.bit63);                            //位键盘按钮
        bit_flag_0 =(TextView)findViewById(R.id.bit_flag_0);
        bit_flag_4=(TextView)findViewById(R.id.bit_flag_4);
        bit_flag_8 =(TextView)findViewById(R.id.bit_flag_8);
        bit_flag_12 =(TextView)findViewById(R.id.bit_flag_12);
        bit_flag_16 =(TextView)findViewById(R.id.bit_flag_16);
        bit_flag_20 =(TextView)findViewById(R.id.bit_flag_20);
        bit_flag_24 =(TextView)findViewById(R.id.bit_flag_24);
        bit_flag_28 =(TextView)findViewById(R.id.bit_flag_28);
        bit_flag_32 =(TextView)findViewById(R.id.bit_flag_32);
        bit_flag_36 =(TextView)findViewById(R.id.bit_flag_36);
        bit_flag_40 =(TextView)findViewById(R.id.bit_flag_40);
        bit_flag_44 =(TextView)findViewById(R.id.bit_flag_44);
        bit_flag_48 =(TextView)findViewById(R.id.bit_flag_48);
        bit_flag_52 =(TextView)findViewById(R.id.bit_flag_52);
        bit_flag_56 =(TextView)findViewById(R.id.bit_flag_56);
        bit_flag_60 =(TextView)findViewById(R.id.bit_flag_60);      //位标志文本框
        numKeyboardButton.setTextColor(getResources().getColor(R.color.blue));
        bitKeyboardButton.setTextColor(getResources().getColor(R.color.black));
        numKeyboardOnView.setBackgroundColor(getResources().getColor(R.color.blue));
        bitKeyboardOnView.setBackgroundColor(getResources().getColor(R.color.gray_two));
        equalButton.setBackgroundColor(getResources().getColor(R.color.blue));
        hexViewButton.setTextColor(getResources().getColor(R.color.black));
        hexView.setTextColor(getResources().getColor(R.color.black));
        decViewButton.setTextColor(getResources().getColor(R.color.blue));
        decView.setTextColor(getResources().getColor(R.color.blue));
        octViewButton.setTextColor(getResources().getColor(R.color.black));
        octView.setTextColor(getResources().getColor(R.color.black));
        binViewButton.setTextColor(getResources().getColor(R.color.black));
        binView.setTextColor(getResources().getColor(R.color.black));

    }
    public void onClickNum0Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        tempNum *= radixMode;
        tempNum += 0;
        freshAllView(tempNum);
    }
    public void onClickNum1Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        tempNum *= radixMode;
        tempNum += 1;
        freshAllView(tempNum);
    }
    public void onClickNum2Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2) return;
        tempNum *= radixMode;
        tempNum += 2;
        freshAllView(tempNum);
    }
    public void onClickNum3Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2) return;
        tempNum *= radixMode;
        tempNum += 3;
        freshAllView(tempNum);
    }
    public void onClickNum4Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2) return;
        tempNum *= radixMode;
        tempNum += 4;
        freshAllView(tempNum);
    }
    public void onClickNum5Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2) return;
        tempNum *= radixMode;
        tempNum += 5;
        freshAllView(tempNum);
    }
    public void onClickNum6Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2) return;
        tempNum *= radixMode;
        tempNum += 6;
        freshAllView(tempNum);
    }
    public void onClickNum7Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2) return;
        tempNum *= radixMode;
        tempNum += 7;
        freshAllView(tempNum);
    }
    public void onClickNum8Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2|| radixMode == 8) return;
        tempNum *= radixMode;
        tempNum += 8;
        freshAllView(tempNum);
    }
    public void onClickNum9Button(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode == 2|| radixMode == 8) return;
        tempNum *= radixMode;
        tempNum += 9;
        freshAllView(tempNum);
    }

    public void onClickNumFButton(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode != 16) return;
        tempNum *= radixMode;
        tempNum += 15;
        freshAllView(tempNum);
    }
    public void onClickNumEButton(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode != 16) return;
        tempNum *= radixMode;
        tempNum += 14;
        freshAllView(tempNum);
    }
    public void onClickNumDButton(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode != 16) return;
        tempNum *= radixMode;
        tempNum += 13;
        freshAllView(tempNum);
    }
    public void onClickNumCButton(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode != 16) return;
        tempNum *= radixMode;
        tempNum += 12;
        freshAllView(tempNum);
    }
    public void onClickNumBButton(View view){
        if(isJustFinish == 1){
            tempNum = 0;
            isJustFinish = 0;
        }
        if(radixMode != 16) return;
        tempNum *= radixMode;
        tempNum += 11;
        freshAllView(tempNum);
    }
    public void onClickNumAButton(View view){
        if(radixMode != 16) return;
        tempNum *= radixMode;
        tempNum += 10;
        freshAllView(tempNum);
    }

    public void onClickClearOneButton(View view){
        isJustFinish = 0;
        if(tempNum == 0) return;
        if(radixMode == 16){
            tempNum /= radixMode;
        }
        else if(radixMode == 10){
            tempNum /= radixMode;
        }
        else if(radixMode == 8){
            tempNum /= radixMode;
        }
        else if(radixMode == 2){
            tempNum /= radixMode;
        }
        freshAllView(tempNum);
    }

    public void onClickClearButton(View view){
        isJustFinish = 0;
        tempNum = 0;
        a = 0;
        isCalculating = 0;
        freshAllView(tempNum);
    }


    public void onClickCEButton(View view){
        isJustFinish = 0;
        tempNum = 0;
        freshAllView(tempNum);
    }

    public String numTo64BitBinaryString(long a){
        int i, N;
        stringTemp = Long.toBinaryString(a);
        StringBuilder stringBuilder = new StringBuilder(stringTemp);
        N = 64 - stringTemp.length();
        for(i=0;i<N;i++){
            stringBuilder.insert(0,"0");
        }
        return stringBuilder.toString();
    }

    public void freshBitKeyboard(long a){
        int i = 0;
        stringTemp = Long.toBinaryString(a);
        stringTemp = new StringBuilder(stringTemp).reverse().toString();
        bit0.setText("0");
        bit1.setText("0");
        bit2.setText("0");
        bit3.setText("0");
        bit4.setText("0");
        bit5.setText("0");
        bit6.setText("0");
        bit7.setText("0");
        bit8.setText("0");
        bit9.setText("0");
        bit10.setText("0");
        bit11.setText("0");
        bit12.setText("0");
        bit13.setText("0");
        bit14.setText("0");
        bit15.setText("0");
        bit16.setText("0");
        bit17.setText("0");
        bit18.setText("0");
        bit19.setText("0");
        bit20.setText("0");
        bit21.setText("0");
        bit22.setText("0");
        bit23.setText("0");
        bit24.setText("0");
        bit25.setText("0");
        bit26.setText("0");
        bit27.setText("0");
        bit28.setText("0");
        bit29.setText("0");
        bit30.setText("0");
        bit31.setText("0");
        bit32.setText("0");
        bit33.setText("0");
        bit34.setText("0");
        bit35.setText("0");
        bit36.setText("0");
        bit37.setText("0");
        bit38.setText("0");
        bit39.setText("0");
        bit40.setText("0");
        bit41.setText("0");
        bit42.setText("0");
        bit43.setText("0");
        bit44.setText("0");
        bit45.setText("0");
        bit46.setText("0");
        bit47.setText("0");
        bit48.setText("0");
        bit49.setText("0");
        bit50.setText("0");
        bit51.setText("0");
        bit52.setText("0");
        bit53.setText("0");
        bit54.setText("0");
        bit55.setText("0");
        bit56.setText("0");
        bit57.setText("0");
        bit58.setText("0");
        bit59.setText("0");
        bit60.setText("0");
        bit61.setText("0");
        bit62.setText("0");
        bit63.setText("0");

        for(;i<stringTemp.length();i++) {
            if (i == 0) bit0.setText(String.valueOf(stringTemp.charAt(0)));
            else if (i == 1) bit1.setText(String.valueOf(stringTemp.charAt(1)));
            else if (i == 2) bit2.setText(String.valueOf(stringTemp.charAt(2)));
            else if (i == 3) bit3.setText(String.valueOf(stringTemp.charAt(3)));
            else if (i == 4) bit4.setText(String.valueOf(stringTemp.charAt(4)));
            else if (i == 5) bit5.setText(String.valueOf(stringTemp.charAt(5)));
            else if (i == 6) bit6.setText(String.valueOf(stringTemp.charAt(6)));
            else if (i == 7) bit7.setText(String.valueOf(stringTemp.charAt(7)));
            else if (i == 8) bit8.setText(String.valueOf(stringTemp.charAt(8)));
            else if (i == 9) bit9.setText(String.valueOf(stringTemp.charAt(9)));
            else if (i == 10) bit10.setText(String.valueOf(stringTemp.charAt(10)));
            else if (i == 11) bit11.setText(String.valueOf(stringTemp.charAt(11)));
            else if (i == 12) bit12.setText(String.valueOf(stringTemp.charAt(12)));
            else if (i == 13) bit13.setText(String.valueOf(stringTemp.charAt(13)));
            else if (i == 14) bit14.setText(String.valueOf(stringTemp.charAt(14)));
            else if (i == 15) bit15.setText(String.valueOf(stringTemp.charAt(15)));
            else if (i == 16) bit16.setText(String.valueOf(stringTemp.charAt(16)));
            else if (i == 17) bit17.setText(String.valueOf(stringTemp.charAt(17)));
            else if (i == 18) bit18.setText(String.valueOf(stringTemp.charAt(18)));
            else if (i == 19) bit19.setText(String.valueOf(stringTemp.charAt(19)));
            else if (i == 20) bit20.setText(String.valueOf(stringTemp.charAt(20)));
            else if (i == 21) bit21.setText(String.valueOf(stringTemp.charAt(21)));
            else if (i == 22) bit22.setText(String.valueOf(stringTemp.charAt(22)));
            else if (i == 23) bit23.setText(String.valueOf(stringTemp.charAt(23)));
            else if (i == 24) bit24.setText(String.valueOf(stringTemp.charAt(24)));
            else if (i == 25) bit25.setText(String.valueOf(stringTemp.charAt(25)));
            else if (i == 26) bit26.setText(String.valueOf(stringTemp.charAt(26)));
            else if (i == 27) bit27.setText(String.valueOf(stringTemp.charAt(27)));
            else if (i == 28) bit28.setText(String.valueOf(stringTemp.charAt(28)));
            else if (i == 29) bit29.setText(String.valueOf(stringTemp.charAt(29)));
            else if (i == 30) bit30.setText(String.valueOf(stringTemp.charAt(30)));
            else if (i == 31) bit31.setText(String.valueOf(stringTemp.charAt(31)));
            else if (i == 32) bit32.setText(String.valueOf(stringTemp.charAt(32)));
            else if (i == 33) bit33.setText(String.valueOf(stringTemp.charAt(33)));
            else if (i == 34) bit34.setText(String.valueOf(stringTemp.charAt(34)));
            else if (i == 35) bit35.setText(String.valueOf(stringTemp.charAt(35)));
            else if (i == 36) bit36.setText(String.valueOf(stringTemp.charAt(36)));
            else if (i == 37) bit37.setText(String.valueOf(stringTemp.charAt(37)));
            else if (i == 38) bit38.setText(String.valueOf(stringTemp.charAt(38)));
            else if (i == 39) bit39.setText(String.valueOf(stringTemp.charAt(39)));
            else if (i == 40) bit40.setText(String.valueOf(stringTemp.charAt(40)));
            else if (i == 41) bit41.setText(String.valueOf(stringTemp.charAt(41)));
            else if (i == 42) bit42.setText(String.valueOf(stringTemp.charAt(42)));
            else if (i == 43) bit43.setText(String.valueOf(stringTemp.charAt(43)));
            else if (i == 44) bit44.setText(String.valueOf(stringTemp.charAt(44)));
            else if (i == 45) bit45.setText(String.valueOf(stringTemp.charAt(45)));
            else if (i == 46) bit46.setText(String.valueOf(stringTemp.charAt(46)));
            else if (i == 47) bit47.setText(String.valueOf(stringTemp.charAt(47)));
            else if (i == 48) bit48.setText(String.valueOf(stringTemp.charAt(48)));
            else if (i == 49) bit49.setText(String.valueOf(stringTemp.charAt(49)));
            else if (i == 50) bit50.setText(String.valueOf(stringTemp.charAt(50)));
            else if (i == 51) bit51.setText(String.valueOf(stringTemp.charAt(51)));
            else if (i == 52) bit52.setText(String.valueOf(stringTemp.charAt(52)));
            else if (i == 53) bit53.setText(String.valueOf(stringTemp.charAt(53)));
            else if (i == 54) bit54.setText(String.valueOf(stringTemp.charAt(54)));
            else if (i == 55) bit55.setText(String.valueOf(stringTemp.charAt(55)));
            else if (i == 56) bit56.setText(String.valueOf(stringTemp.charAt(56)));
            else if (i == 57) bit57.setText(String.valueOf(stringTemp.charAt(57)));
            else if (i == 58) bit58.setText(String.valueOf(stringTemp.charAt(58)));
            else if (i == 59) bit59.setText(String.valueOf(stringTemp.charAt(59)));
            else if (i == 60) bit60.setText(String.valueOf(stringTemp.charAt(60)));
            else if (i == 61) bit61.setText(String.valueOf(stringTemp.charAt(61)));
            else if (i == 62) bit62.setText(String.valueOf(stringTemp.charAt(62)));
            else if (i == 63) bit63.setText(String.valueOf(stringTemp.charAt(63)));
        }
        if(bit0.getText().toString().equals("1")) bit0.setTextColor(getResources().getColor(R.color.blue)); else bit0.setTextColor(getResources().getColor(R.color.black));
        if(bit1.getText().toString().equals("1")) bit1.setTextColor(getResources().getColor(R.color.blue)); else bit1.setTextColor(getResources().getColor(R.color.black));
        if(bit2.getText().toString().equals("1")) bit2.setTextColor(getResources().getColor(R.color.blue)); else bit2.setTextColor(getResources().getColor(R.color.black));
        if(bit3.getText().toString().equals("1")) bit3.setTextColor(getResources().getColor(R.color.blue)); else bit3.setTextColor(getResources().getColor(R.color.black));
        if(bit4.getText().toString().equals("1")) bit4.setTextColor(getResources().getColor(R.color.blue)); else bit4.setTextColor(getResources().getColor(R.color.black));
        if(bit5.getText().toString().equals("1")) bit5.setTextColor(getResources().getColor(R.color.blue)); else bit5.setTextColor(getResources().getColor(R.color.black));
        if(bit6.getText().toString().equals("1")) bit6.setTextColor(getResources().getColor(R.color.blue)); else bit6.setTextColor(getResources().getColor(R.color.black));
        if(bit7.getText().toString().equals("1")) bit7.setTextColor(getResources().getColor(R.color.blue)); else bit7.setTextColor(getResources().getColor(R.color.black));
        if(bit8.getText().toString().equals("1")) bit8.setTextColor(getResources().getColor(R.color.blue)); else bit8.setTextColor(getResources().getColor(R.color.black));
        if(bit9.getText().toString().equals("1")) bit9.setTextColor(getResources().getColor(R.color.blue)); else bit9.setTextColor(getResources().getColor(R.color.black));
        if(bit10.getText().toString().equals("1")) bit10.setTextColor(getResources().getColor(R.color.blue)); else bit10.setTextColor(getResources().getColor(R.color.black));
        if(bit11.getText().toString().equals("1")) bit11.setTextColor(getResources().getColor(R.color.blue)); else bit11.setTextColor(getResources().getColor(R.color.black));
        if(bit12.getText().toString().equals("1")) bit12.setTextColor(getResources().getColor(R.color.blue)); else bit12.setTextColor(getResources().getColor(R.color.black));
        if(bit13.getText().toString().equals("1")) bit13.setTextColor(getResources().getColor(R.color.blue)); else bit13.setTextColor(getResources().getColor(R.color.black));
        if(bit14.getText().toString().equals("1")) bit14.setTextColor(getResources().getColor(R.color.blue)); else bit14.setTextColor(getResources().getColor(R.color.black));
        if(bit15.getText().toString().equals("1")) bit15.setTextColor(getResources().getColor(R.color.blue)); else bit15.setTextColor(getResources().getColor(R.color.black));
        if(bit16.getText().toString().equals("1")) bit16.setTextColor(getResources().getColor(R.color.blue)); else bit16.setTextColor(getResources().getColor(R.color.black));
        if(bit17.getText().toString().equals("1")) bit17.setTextColor(getResources().getColor(R.color.blue)); else bit17.setTextColor(getResources().getColor(R.color.black));
        if(bit18.getText().toString().equals("1")) bit18.setTextColor(getResources().getColor(R.color.blue)); else bit18.setTextColor(getResources().getColor(R.color.black));
        if(bit19.getText().toString().equals("1")) bit19.setTextColor(getResources().getColor(R.color.blue)); else bit19.setTextColor(getResources().getColor(R.color.black));
        if(bit20.getText().toString().equals("1")) bit20.setTextColor(getResources().getColor(R.color.blue)); else bit20.setTextColor(getResources().getColor(R.color.black));
        if(bit21.getText().toString().equals("1")) bit21.setTextColor(getResources().getColor(R.color.blue)); else bit21.setTextColor(getResources().getColor(R.color.black));
        if(bit22.getText().toString().equals("1")) bit22.setTextColor(getResources().getColor(R.color.blue)); else bit22.setTextColor(getResources().getColor(R.color.black));
        if(bit23.getText().toString().equals("1")) bit23.setTextColor(getResources().getColor(R.color.blue)); else bit23.setTextColor(getResources().getColor(R.color.black));
        if(bit24.getText().toString().equals("1")) bit24.setTextColor(getResources().getColor(R.color.blue)); else bit24.setTextColor(getResources().getColor(R.color.black));
        if(bit25.getText().toString().equals("1")) bit25.setTextColor(getResources().getColor(R.color.blue)); else bit25.setTextColor(getResources().getColor(R.color.black));
        if(bit26.getText().toString().equals("1")) bit26.setTextColor(getResources().getColor(R.color.blue)); else bit26.setTextColor(getResources().getColor(R.color.black));
        if(bit27.getText().toString().equals("1")) bit27.setTextColor(getResources().getColor(R.color.blue)); else bit27.setTextColor(getResources().getColor(R.color.black));
        if(bit28.getText().toString().equals("1")) bit28.setTextColor(getResources().getColor(R.color.blue)); else bit28.setTextColor(getResources().getColor(R.color.black));
        if(bit29.getText().toString().equals("1")) bit29.setTextColor(getResources().getColor(R.color.blue)); else bit29.setTextColor(getResources().getColor(R.color.black));
        if(bit30.getText().toString().equals("1")) bit30.setTextColor(getResources().getColor(R.color.blue)); else bit30.setTextColor(getResources().getColor(R.color.black));
        if(bit31.getText().toString().equals("1")) bit31.setTextColor(getResources().getColor(R.color.blue)); else bit31.setTextColor(getResources().getColor(R.color.black));
        if(bit32.getText().toString().equals("1")) bit32.setTextColor(getResources().getColor(R.color.blue)); else bit32.setTextColor(getResources().getColor(R.color.black));
        if(bit33.getText().toString().equals("1")) bit33.setTextColor(getResources().getColor(R.color.blue)); else bit33.setTextColor(getResources().getColor(R.color.black));
        if(bit34.getText().toString().equals("1")) bit34.setTextColor(getResources().getColor(R.color.blue)); else bit34.setTextColor(getResources().getColor(R.color.black));
        if(bit35.getText().toString().equals("1")) bit35.setTextColor(getResources().getColor(R.color.blue)); else bit35.setTextColor(getResources().getColor(R.color.black));
        if(bit36.getText().toString().equals("1")) bit36.setTextColor(getResources().getColor(R.color.blue)); else bit36.setTextColor(getResources().getColor(R.color.black));
        if(bit37.getText().toString().equals("1")) bit37.setTextColor(getResources().getColor(R.color.blue)); else bit37.setTextColor(getResources().getColor(R.color.black));
        if(bit38.getText().toString().equals("1")) bit38.setTextColor(getResources().getColor(R.color.blue)); else bit38.setTextColor(getResources().getColor(R.color.black));
        if(bit39.getText().toString().equals("1")) bit39.setTextColor(getResources().getColor(R.color.blue)); else bit39.setTextColor(getResources().getColor(R.color.black));
        if(bit40.getText().toString().equals("1")) bit40.setTextColor(getResources().getColor(R.color.blue)); else bit40.setTextColor(getResources().getColor(R.color.black));
        if(bit41.getText().toString().equals("1")) bit41.setTextColor(getResources().getColor(R.color.blue)); else bit41.setTextColor(getResources().getColor(R.color.black));
        if(bit42.getText().toString().equals("1")) bit42.setTextColor(getResources().getColor(R.color.blue)); else bit42.setTextColor(getResources().getColor(R.color.black));
        if(bit43.getText().toString().equals("1")) bit43.setTextColor(getResources().getColor(R.color.blue)); else bit43.setTextColor(getResources().getColor(R.color.black));
        if(bit44.getText().toString().equals("1")) bit44.setTextColor(getResources().getColor(R.color.blue)); else bit44.setTextColor(getResources().getColor(R.color.black));
        if(bit45.getText().toString().equals("1")) bit45.setTextColor(getResources().getColor(R.color.blue)); else bit45.setTextColor(getResources().getColor(R.color.black));
        if(bit46.getText().toString().equals("1")) bit46.setTextColor(getResources().getColor(R.color.blue)); else bit46.setTextColor(getResources().getColor(R.color.black));
        if(bit47.getText().toString().equals("1")) bit47.setTextColor(getResources().getColor(R.color.blue)); else bit47.setTextColor(getResources().getColor(R.color.black));
        if(bit48.getText().toString().equals("1")) bit48.setTextColor(getResources().getColor(R.color.blue)); else bit48.setTextColor(getResources().getColor(R.color.black));
        if(bit49.getText().toString().equals("1")) bit49.setTextColor(getResources().getColor(R.color.blue)); else bit49.setTextColor(getResources().getColor(R.color.black));
        if(bit50.getText().toString().equals("1")) bit50.setTextColor(getResources().getColor(R.color.blue)); else bit50.setTextColor(getResources().getColor(R.color.black));
        if(bit51.getText().toString().equals("1")) bit51.setTextColor(getResources().getColor(R.color.blue)); else bit51.setTextColor(getResources().getColor(R.color.black));
        if(bit52.getText().toString().equals("1")) bit52.setTextColor(getResources().getColor(R.color.blue)); else bit52.setTextColor(getResources().getColor(R.color.black));
        if(bit53.getText().toString().equals("1")) bit53.setTextColor(getResources().getColor(R.color.blue)); else bit53.setTextColor(getResources().getColor(R.color.black));
        if(bit54.getText().toString().equals("1")) bit54.setTextColor(getResources().getColor(R.color.blue)); else bit54.setTextColor(getResources().getColor(R.color.black));
        if(bit55.getText().toString().equals("1")) bit55.setTextColor(getResources().getColor(R.color.blue)); else bit55.setTextColor(getResources().getColor(R.color.black));
        if(bit56.getText().toString().equals("1")) bit56.setTextColor(getResources().getColor(R.color.blue)); else bit56.setTextColor(getResources().getColor(R.color.black));
        if(bit57.getText().toString().equals("1")) bit57.setTextColor(getResources().getColor(R.color.blue)); else bit57.setTextColor(getResources().getColor(R.color.black));
        if(bit58.getText().toString().equals("1")) bit58.setTextColor(getResources().getColor(R.color.blue)); else bit58.setTextColor(getResources().getColor(R.color.black));
        if(bit59.getText().toString().equals("1")) bit59.setTextColor(getResources().getColor(R.color.blue)); else bit59.setTextColor(getResources().getColor(R.color.black));
        if(bit60.getText().toString().equals("1")) bit60.setTextColor(getResources().getColor(R.color.blue)); else bit60.setTextColor(getResources().getColor(R.color.black));
        if(bit61.getText().toString().equals("1")) bit61.setTextColor(getResources().getColor(R.color.blue)); else bit61.setTextColor(getResources().getColor(R.color.black));
        if(bit62.getText().toString().equals("1")) bit62.setTextColor(getResources().getColor(R.color.blue)); else bit62.setTextColor(getResources().getColor(R.color.black));
        if(bit63.getText().toString().equals("1")) bit63.setTextColor(getResources().getColor(R.color.blue)); else bit63.setTextColor(getResources().getColor(R.color.black));

        stringTemp = Long.toHexString(a);
        stringTemp = new StringBuilder(stringTemp).reverse().toString();
        bit_flag_0.setText("0");
        bit_flag_4.setText("0");
        bit_flag_8.setText("0");
        bit_flag_12.setText("0");
        bit_flag_16.setText("0");
        bit_flag_20.setText("0");
        bit_flag_24.setText("0");
        bit_flag_28.setText("0");
        bit_flag_32.setText("0");
        bit_flag_36.setText("0");
        bit_flag_40.setText("0");
        bit_flag_44.setText("0");
        bit_flag_48.setText("0");
        bit_flag_52.setText("0");
        bit_flag_56.setText("0");
        bit_flag_60.setText("0");
        for(i = 0;i<stringTemp.length();i++) {
            if(i ==0) bit_flag_0.setText(String.valueOf(stringTemp.charAt(0)).toUpperCase());
            else if(i ==1)bit_flag_4.setText(String.valueOf(stringTemp.charAt(1)).toUpperCase());
            else if(i ==2)bit_flag_8.setText(String.valueOf(stringTemp.charAt(2)).toUpperCase());
            else if(i == 3)bit_flag_12.setText(String.valueOf(stringTemp.charAt(3)).toUpperCase());
            else if(i ==4)bit_flag_16.setText(String.valueOf(stringTemp.charAt(4)).toUpperCase());
            else if(i ==5)bit_flag_20.setText(String.valueOf(stringTemp.charAt(5)).toUpperCase());
            else if(i ==6)bit_flag_24.setText(String.valueOf(stringTemp.charAt(6)).toUpperCase());
            else if(i ==7)bit_flag_28.setText(String.valueOf(stringTemp.charAt(7)).toUpperCase());
            else if(i ==8)bit_flag_32.setText(String.valueOf(stringTemp.charAt(8)).toUpperCase());
            else if(i ==9)bit_flag_36.setText(String.valueOf(stringTemp.charAt(9)).toUpperCase());
            else if(i ==10)bit_flag_40.setText(String.valueOf(stringTemp.charAt(10)).toUpperCase());
            else if(i ==11)bit_flag_44.setText(String.valueOf(stringTemp.charAt(11)).toUpperCase());
            else if(i ==12)bit_flag_48.setText(String.valueOf(stringTemp.charAt(12)).toUpperCase());
            else if(i ==13)bit_flag_52.setText(String.valueOf(stringTemp.charAt(13)).toUpperCase());
            else if(i ==14)bit_flag_56.setText(String.valueOf(stringTemp.charAt(14)).toUpperCase());
            else if(i ==15)bit_flag_60.setText(String.valueOf(stringTemp.charAt(15)).toUpperCase());

        }
    }

    public void nClickBit0(View view){

        stringTemp = numTo64BitBinaryString(tempNum);
        StringBuilder stringBuilder = new StringBuilder(stringTemp);
        if(bit0.getText().equals("1")){
            stringBuilder.setCharAt(63,'0');
        }
        else{
            stringBuilder.setCharAt(63,'1');
        }
        stringTemp = stringBuilder.toString();
        tempNum =parseUnsignedLongToBinary(stringTemp);
        freshBitKeyboard(tempNum);
        freshAllView(tempNum);
    }

    public long parseUnsignedLongToBinary(String string){
        StringBuilder stringBuilder = new StringBuilder(string);
        if(stringBuilder.charAt(0) == '1'){
            stringBuilder.setCharAt(0,'-');
            for(int i = 1;i<stringBuilder.length();i++){
                if(stringBuilder.charAt(i) == '1')
                    stringBuilder.setCharAt(i,'0');
                else stringBuilder.setCharAt(i,'1');
            }
            return Long.parseLong(stringBuilder.toString(),2) - 1;
        }else return Long.parseLong(stringBuilder.toString(),2);
    }

    public void nClickBit1(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit1.getText().	equals("1")){	        stringBuilder.setCharAt(62,	'0');	    }	    else{	        stringBuilder.setCharAt(62,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit2(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit2.getText().	equals("1")){	        stringBuilder.setCharAt(61,	'0');	    }	    else{	        stringBuilder.setCharAt(61,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit3(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit3.getText().	equals("1")){	        stringBuilder.setCharAt(60,	'0');	    }	    else{	        stringBuilder.setCharAt(60,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit4(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit4.getText().	equals("1")){	        stringBuilder.setCharAt(59,	'0');	    }	    else{	        stringBuilder.setCharAt(59,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit5(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit5.getText().	equals("1")){	        stringBuilder.setCharAt(58,	'0');	    }	    else{	        stringBuilder.setCharAt(58,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit6(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit6.getText().	equals("1")){	        stringBuilder.setCharAt(57,	'0');	    }	    else{	        stringBuilder.setCharAt(57,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit7(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit7.getText().	equals("1")){	        stringBuilder.setCharAt(56,	'0');	    }	    else{	        stringBuilder.setCharAt(56,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit8(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit8.getText().	equals("1")){	        stringBuilder.setCharAt(55,	'0');	    }	    else{	        stringBuilder.setCharAt(55,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit9(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit9.getText().	equals("1")){	        stringBuilder.setCharAt(54,	'0');	    }	    else{	        stringBuilder.setCharAt(54,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit10(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit10.getText().	equals("1")){	        stringBuilder.setCharAt(53,	'0');	    }	    else{	        stringBuilder.setCharAt(53,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit11(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit11.getText().	equals("1")){	        stringBuilder.setCharAt(52,	'0');	    }	    else{	        stringBuilder.setCharAt(52,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit12(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit12.getText().	equals("1")){	        stringBuilder.setCharAt(51,	'0');	    }	    else{	        stringBuilder.setCharAt(51,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit13(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit13.getText().	equals("1")){	        stringBuilder.setCharAt(50,	'0');	    }	    else{	        stringBuilder.setCharAt(50,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit14(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit14.getText().	equals("1")){	        stringBuilder.setCharAt(49,	'0');	    }	    else{	        stringBuilder.setCharAt(49,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit15(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit15.getText().	equals("1")){	        stringBuilder.setCharAt(48,	'0');	    }	    else{	        stringBuilder.setCharAt(48,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit16(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit16.getText().	equals("1")){	        stringBuilder.setCharAt(47,	'0');	    }	    else{	        stringBuilder.setCharAt(47,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit17(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit17.getText().	equals("1")){	        stringBuilder.setCharAt(46,	'0');	    }	    else{	        stringBuilder.setCharAt(46,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit18(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit18.getText().	equals("1")){	        stringBuilder.setCharAt(45,	'0');	    }	    else{	        stringBuilder.setCharAt(45,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit19(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit19.getText().	equals("1")){	        stringBuilder.setCharAt(44,	'0');	    }	    else{	        stringBuilder.setCharAt(44,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit20(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit20.getText().	equals("1")){	        stringBuilder.setCharAt(43,	'0');	    }	    else{	        stringBuilder.setCharAt(43,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit21(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit21.getText().	equals("1")){	        stringBuilder.setCharAt(42,	'0');	    }	    else{	        stringBuilder.setCharAt(42,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit22(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit22.getText().	equals("1")){	        stringBuilder.setCharAt(41,	'0');	    }	    else{	        stringBuilder.setCharAt(41,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit23(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit23.getText().	equals("1")){	        stringBuilder.setCharAt(40,	'0');	    }	    else{	        stringBuilder.setCharAt(40,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit24(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit24.getText().	equals("1")){	        stringBuilder.setCharAt(39,	'0');	    }	    else{	        stringBuilder.setCharAt(39,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit25(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit25.getText().	equals("1")){	        stringBuilder.setCharAt(38,	'0');	    }	    else{	        stringBuilder.setCharAt(38,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit26(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit26.getText().	equals("1")){	        stringBuilder.setCharAt(37,	'0');	    }	    else{	        stringBuilder.setCharAt(37,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit27(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit27.getText().	equals("1")){	        stringBuilder.setCharAt(36,	'0');	    }	    else{	        stringBuilder.setCharAt(36,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit28(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit28.getText().	equals("1")){	        stringBuilder.setCharAt(35,	'0');	    }	    else{	        stringBuilder.setCharAt(35,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit29(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit29.getText().	equals("1")){	        stringBuilder.setCharAt(34,	'0');	    }	    else{	        stringBuilder.setCharAt(34,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit30(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit30.getText().	equals("1")){	        stringBuilder.setCharAt(33,	'0');	    }	    else{	        stringBuilder.setCharAt(33,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit31(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit31.getText().	equals("1")){	        stringBuilder.setCharAt(32,	'0');	    }	    else{	        stringBuilder.setCharAt(32,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit32(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit32.getText().	equals("1")){	        stringBuilder.setCharAt(31,	'0');	    }	    else{	        stringBuilder.setCharAt(31,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit33(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit33.getText().	equals("1")){	        stringBuilder.setCharAt(30,	'0');	    }	    else{	        stringBuilder.setCharAt(30,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit34(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit34.getText().	equals("1")){	        stringBuilder.setCharAt(29,	'0');	    }	    else{	        stringBuilder.setCharAt(29,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit35(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit35.getText().	equals("1")){	        stringBuilder.setCharAt(28,	'0');	    }	    else{	        stringBuilder.setCharAt(28,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit36(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit36.getText().	equals("1")){	        stringBuilder.setCharAt(27,	'0');	    }	    else{	        stringBuilder.setCharAt(27,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit37(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit37.getText().	equals("1")){	        stringBuilder.setCharAt(26,	'0');	    }	    else{	        stringBuilder.setCharAt(26,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit38(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit38.getText().	equals("1")){	        stringBuilder.setCharAt(25,	'0');	    }	    else{	        stringBuilder.setCharAt(25,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit39(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit39.getText().	equals("1")){	        stringBuilder.setCharAt(24,	'0');	    }	    else{	        stringBuilder.setCharAt(24,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit40(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit40.getText().	equals("1")){	        stringBuilder.setCharAt(23,	'0');	    }	    else{	        stringBuilder.setCharAt(23,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit41(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit41.getText().	equals("1")){	        stringBuilder.setCharAt(22,	'0');	    }	    else{	        stringBuilder.setCharAt(22,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit42(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit42.getText().	equals("1")){	        stringBuilder.setCharAt(21,	'0');	    }	    else{	        stringBuilder.setCharAt(21,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit43(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit43.getText().	equals("1")){	        stringBuilder.setCharAt(20,	'0');	    }	    else{	        stringBuilder.setCharAt(20,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit44(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit44.getText().	equals("1")){	        stringBuilder.setCharAt(19,	'0');	    }	    else{	        stringBuilder.setCharAt(19,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit45(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit45.getText().	equals("1")){	        stringBuilder.setCharAt(18,	'0');	    }	    else{	        stringBuilder.setCharAt(18,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit46(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit46.getText().	equals("1")){	        stringBuilder.setCharAt(17,	'0');	    }	    else{	        stringBuilder.setCharAt(17,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit47(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit47.getText().	equals("1")){	        stringBuilder.setCharAt(16,	'0');	    }	    else{	        stringBuilder.setCharAt(16,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit48(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit48.getText().	equals("1")){	        stringBuilder.setCharAt(15,	'0');	    }	    else{	        stringBuilder.setCharAt(15,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit49(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit49.getText().	equals("1")){	        stringBuilder.setCharAt(14,	'0');	    }	    else{	        stringBuilder.setCharAt(14,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit50(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit50.getText().	equals("1")){	        stringBuilder.setCharAt(13,	'0');	    }	    else{	        stringBuilder.setCharAt(13,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit51(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit51.getText().	equals("1")){	        stringBuilder.setCharAt(12,	'0');	    }	    else{	        stringBuilder.setCharAt(12,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit52(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit52.getText().	equals("1")){	        stringBuilder.setCharAt(11,	'0');	    }	    else{	        stringBuilder.setCharAt(11,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit53(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit53.getText().	equals("1")){	        stringBuilder.setCharAt(10,	'0');	    }	    else{	        stringBuilder.setCharAt(10,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit54(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit54.getText().	equals("1")){	        stringBuilder.setCharAt(9,	'0');	    }	    else{	        stringBuilder.setCharAt(9,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit55(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit55.getText().	equals("1")){	        stringBuilder.setCharAt(8,	'0');	    }	    else{	        stringBuilder.setCharAt(8,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit56(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit56.getText().	equals("1")){	        stringBuilder.setCharAt(7,	'0');	    }	    else{	        stringBuilder.setCharAt(7,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit57(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit57.getText().	equals("1")){	        stringBuilder.setCharAt(6,	'0');	    }	    else{	        stringBuilder.setCharAt(6,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit58(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit58.getText().	equals("1")){	        stringBuilder.setCharAt(5,	'0');	    }	    else{	        stringBuilder.setCharAt(5,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit59(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit59.getText().	equals("1")){	        stringBuilder.setCharAt(4,	'0');	    }	    else{	        stringBuilder.setCharAt(4,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit60(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit60.getText().	equals("1")){	        stringBuilder.setCharAt(3,	'0');	    }	    else{	        stringBuilder.setCharAt(3,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit61(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit61.getText().	equals("1")){	        stringBuilder.setCharAt(2,	'0');	    }	    else{	        stringBuilder.setCharAt(2,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit62(View view){	    stringTemp = numTo64BitBinaryString(tempNum);	    StringBuilder stringBuilder = new StringBuilder(stringTemp);	    if(bit62.getText().	equals("1")){	        stringBuilder.setCharAt(1,	'0');	    }	    else{	        stringBuilder.setCharAt(1,	'1');	    }	    stringTemp = stringBuilder.toString();	    tempNum = parseUnsignedLongToBinary(stringTemp);	    freshBitKeyboard(tempNum);	    freshAllView(tempNum);	}
    public void nClickBit63(View view){
        stringTemp = numTo64BitBinaryString(tempNum);
        StringBuilder stringBuilder = new StringBuilder(stringTemp);
        if(bit63.getText().	equals("1")){
            stringBuilder.setCharAt(0,	'0');
        }
        else{
            stringBuilder.setCharAt(0, '1');
        }
        stringTemp = stringBuilder.toString();
        tempNum = parseUnsignedLongToBinary(stringTemp);
        freshBitKeyboard(tempNum);
        freshAllView(tempNum);
    }

    public void onClickAddButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '+';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '+';
        tempNum = 0;
        freshAllView(a);
    }
    public void onClickMulButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '*';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '*';
        tempNum = 0;
        freshAllView(a);
    }
    public void onClickSubButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '-';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '-';
        tempNum = 0;
        freshAllView(a);
    }

    public void onClickDivideButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '/';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '/';
        tempNum = 0;
        freshAllView(a);
    }
    public void onClickModButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '%';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '%';
        tempNum = 0;
        freshAllView(a);
    }
    public void onClickOrButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '|';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            freshBitKeyboard(0);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '|';
        tempNum = 0;
        freshAllView(a);
        freshBitKeyboard(0);
    }
    public void onClickXorButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '^';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            freshBitKeyboard(0);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '^';
        tempNum = 0;
        freshAllView(a);
        freshBitKeyboard(0);
    }

    public void onClickAndButton(View view){
        isJustFinish = 0;
        if(isCalculating == 0){
            isCalculating = '&';
            a = tempNum;
            tempNum = 0;
            freshAllView(a);
            freshBitKeyboard(0);
            return ;
        }
        if(calculate() == 0) return;
        isCalculating = '&';
        tempNum = 0;
        freshAllView(a);
        freshBitKeyboard(0);
    }

    public void onClickNotButton(View view){
        isJustFinish = 0;
        tempNum = ~tempNum;
        freshAllView(tempNum);
        freshBitKeyboard(tempNum);
    }
    public void onClickOppButton(View view){
        isJustFinish = 0;
        tempNum = -tempNum;
        freshAllView(tempNum);
        freshBitKeyboard(tempNum);
    }

    public void onClickSet0Button(View view){
        tempNum = 0;
        freshBitKeyboard(tempNum);
        freshAllView(tempNum);
    }

    public void onClickSet1Button(View view){
        tempNum = -1;
        freshBitKeyboard(tempNum);
        freshAllView(tempNum);
    }

    public int calculate(){
        if(isCalculating == 0) return 0;
        else if(isCalculating == '+'){
            a += tempNum;
        }else if(isCalculating == '-'){
            a -= tempNum;
        }else if(isCalculating == '*'){
            a *= tempNum;
        }else if(isCalculating == '/'){
            if(tempNum == 0){
                mainNumView.setText(getResources().getString(R.string.error_of_divide));
                return 0;
            }else{
                a/=tempNum;
            }
        }else if(isCalculating == '%'){
            if(tempNum == 0){
                mainNumView.setText(getResources().getString(R.string.error_of_mod));
                return 0;
            }else {
                a %= tempNum;
            }
        }else if(isCalculating == '|'){
            a |= tempNum;
        }else if(isCalculating == '^'){
            a ^= tempNum;
        }else if(isCalculating == '&'){
            a &= tempNum;
        }
        return 1;
    }

    public void onClickEqualButton(View view){
        if(calculate() == 0) return;
        isCalculating = 0;
        tempNum = a;
        a = 0;
        isJustFinish = 1;
        freshAllView(tempNum);
        freshEquationView();
        equalButton.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    public void onClickAboutButton(View view){
        Intent intent = new Intent(MainActivity.this,AboutAppActivity.class);
        startActivity(intent);
    }

}
