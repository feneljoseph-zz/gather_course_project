package cop_4331c.gather;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseUser;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.ArrayList;
import java.util.List;

import cop_4331c.gather.util.MessageService;

public class MessagingActivity  extends Activity {

    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private MyMessageClientListener messageClientListener;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageAdapter messageAdapter;
    private ListView messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        messagesList = (ListView) findViewById(R.id.lstMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);

        //get recipientId from the intent
        Intent intent = getIntent();
        recipientId = intent.getStringExtra("RECIPIENT_ID");
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        messageBodyField = (EditText) findViewById(R.id.txtTextBody);

        //listen for a click on the send button
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send the message!
                messageBody = messageBodyField.getText().toString();
                if (messageBody.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }
                messageService.sendMessage(recipientId, messageBody);
                messageBodyField.setText("");
            }
        });
    }

    //unbind the service when the activity is destroyed
    @Override
    public void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageService.addMessageClientListener(messageClientListener);
            messageService = (MessageService.MessageServiceInterface) iBinder;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService.removeMessageClientListener(messageClientListener);
            messageService = null;
        }
    }

    private class MyMessageClientListener implements MessageClientListener {
        //Notify the user if their message failed to send
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo) {
            Toast.makeText(MessagingActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            if (message.getSenderId().equals(recipientId)) {
                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
            }
        }
        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {
            //Display the message that was just sent
            //Later, I'll show you how to store the
            //message in Parse, so you can retrieve and
            //display them every time the conversation is opened
            WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
        }
        //Do you want to notify your user when the message is delivered?
        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}
        //Don't worry about this right now
        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
    }

    public class MessageAdapter extends BaseAdapter {
        public static final int DIRECTION_INCOMING = 0;
        public static final int DIRECTION_OUTGOING = 1;
        private List<Pair<WritableMessage, Integer>> messages;
        private LayoutInflater layoutInflater;
        public MessageAdapter(Activity activity) {
            layoutInflater = activity.getLayoutInflater();
            messages = new ArrayList<Pair<WritableMessage, Integer>>();
        }
        public void addMessage(WritableMessage message, int direction) {
            messages.add(new Pair(message, direction));
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return messages.size();
        }
        @Override
        public Object getItem(int i) {
            return messages.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public int getViewTypeCount() {
            return 2;
        }
        @Override
        public int getItemViewType(int i) {
            return messages.get(i).second;
        }
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            int direction = getItemViewType(i);
            //show message on left or right, depending on if
            //it's incoming or outgoing
            if (convertView == null) {
                int res = 0;
                if (direction == DIRECTION_INCOMING) {
                    res = R.layout.message_right;
                } else if (direction == DIRECTION_OUTGOING) {
                    res = R.layout.message_left;
                }
                convertView = layoutInflater.inflate(res, viewGroup, false);
            }
            WritableMessage message = messages.get(i).first;
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
            txtMessage.setText(message.getTextBody());
            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messaging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
