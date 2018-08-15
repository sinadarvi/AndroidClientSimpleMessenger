package network.sinadarvishi.simplemessenger

import android.os.AsyncTask
import android.os.Bundle

import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var arrayList = ArrayList<String>()
    private lateinit var adapter: MyCustomAdapter
    private var tcpClient: TCPClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //relate the listView from java to the one created bufferedReader xml
        adapter = MyCustomAdapter(this, arrayList)
        list.adapter = adapter

        // connect to the server
        connectTask().execute("")

        send_button.setOnClickListener {
            val message = editText.text.toString()

            //add the text bufferedReader the arrayList
            arrayList.add("c: $message")

            //sends the message to the server
            tcpClient?.sendMessage(message)

            //refresh the list
            adapter.notifyDataSetChanged()
            editText.setText("")
        }
    }

    inner class connectTask : AsyncTask<String, String, TCPClient>() {

        override fun doInBackground(vararg message: String): TCPClient? {

            //we create a TCPClient object and
            tcpClient = TCPClient(TCPClient.OnMessageReceived {
                //here the messageReceived method is implemented
                //this method calls the onProgressUpdate
                publishProgress(it)
            })
            tcpClient?.run()

            return null
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)

            //bufferedReader the arrayList we add the messaged received from server
            arrayList.add(values[0])
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            adapter.notifyDataSetChanged()
        }
    }
}
