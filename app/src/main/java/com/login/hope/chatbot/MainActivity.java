package com.login.hope.chatbot;

import android.content.res.AssetManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.login.hope.chatbot.Adapter.ChatMessageAdapter;
import com.login.hope.chatbot.Model.ChatMessage;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton sendButton;
    EditText editTextMsg;
    ImageView imageView;

    private Bot bot;
    public static Chat chat;
    private ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        sendButton = findViewById(R.id.sendButton);
        editTextMsg = findViewById(R.id.editTextMsg);
        imageView = findViewById(R.id.imageView);

        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        listView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMsg.getText().toString();
                String response = chat.multisentenceRespond(editTextMsg.getText().toString());

                if(TextUtils.isEmpty(message)){
                    Toast.makeText(MainActivity.this, "Please enter a query", Toast.LENGTH_SHORT).show();
                    return;
                }//end of if statement

                sendMessage(message);
                botsReply(response);

                //to clear editText
                editTextMsg.setText("");
                listView.setSelection(adapter.getCount() - 1);

            }//end of onClick
        });//setOnClickListener

        boolean available = isSDCardAvailable();
        AssetManager assets = getResources().getAssets();
        File fileName = new File(Environment.getExternalStorageDirectory().toString() + "/TBC/bots/TBC");
        boolean makeFile = fileName.mkdirs();

        if(fileName.exists()){
            try{
                for(String dir : assets.list("TBC")){
                    File subDir = new File(fileName.getPath() + "/" + dir);
                    boolean subDir_Check = subDir.mkdirs();

                    for(String file : assets.list("TBC/" + dir)){
                        File newFile = new File(fileName.getPath() + "/" + dir + "/" + file);

                        if(newFile.exists()){
                            continue;
                        }//end of if statement

                        InputStream in;
                        OutputStream out;
                        String str;
                        in = assets.open("TBC/" + dir + "/" + file);
                        out = new FileOutputStream(fileName.getPath() + "/" + dir + "/" + file);

                        //copy files from assets to the mobile's sd or any secondary memory available
                        copyFile(in, out);
                        in.close();
                        out.flush();
                        out.close();
                    }//end of for loop
                }//end of for loop
            }catch (IOException e){
                e.printStackTrace();
            }
        }//end of if statement

        //get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/TBC";
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
        bot = new Bot("TBC", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
    }//end of onCreate

    private void copyFile(InputStream in, OutputStream out) throws IOException{
        byte[] buffer = new byte[1024];
        int read;

        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }//end of while loop
    }//end of copyFile

    private void sendMessage(String message){
        ChatMessage chatMessage = new ChatMessage(false, true, message);
        adapter.add(chatMessage);
    }//end of sendMessage

    private void botsReply(String response){
        ChatMessage chatMessage = new ChatMessage(false, true, response);
        adapter.add(chatMessage);
    }//end of botsReply

    public static boolean isSDCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
    }//end of isSDCardAvailable
    }
