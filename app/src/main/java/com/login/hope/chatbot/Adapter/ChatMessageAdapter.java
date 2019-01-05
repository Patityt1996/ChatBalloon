package com.login.hope.chatbot.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.login.hope.chatbot.Model.ChatMessage;
import com.login.hope.chatbot.R;

import java.util.List;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private static final int MY_MESSAGE = 0;
    private static final int BOT_MESSAGE = 1;

    public ChatMessageAdapter(@NonNull Context context, List<ChatMessage> data) {
        super(context, R.layout.user_query_layout, data);
    }//end of ChatMessageAdapter

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);

        if(item.isMine() && !item.isImage()){
            return MY_MESSAGE;
        }//end of if statement
        else{
            return BOT_MESSAGE;
        }//end of else statement
    }//end of getItemViewType

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int viewType = getItemViewType(position);

        if(viewType == MY_MESSAGE){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.user_query_layout, parent, false);

            TextView textView = convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }//end of if statements
        else if(viewType == BOT_MESSAGE){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.bots_reply_layout, parent, false);

            TextView textView = convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }//end of else if statement

        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getContext(), "Clicked...", Toast.LENGTH_SHORT).show();
            }//end of onClick
        });//counterView bracket

        return convertView;
    }//end of getView

    @Override
    public int getViewTypeCount() {
        return 2;
    }//end of getViewTypeCount
}
