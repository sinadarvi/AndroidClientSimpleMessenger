package network.sinadarvishi.simplemessenger

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var arrayList: ArrayList<String>? = null
    private var mAdapter: MyCustomAdapter? = null
    private var mTcpClient: TCPClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayList = ArrayList()

        val editText = findViewById<View>(R.id.editText) as EditText
        val send = findViewById<View>(R.id.send_button) as Button

        //relate the listView from java to the one created in xml
        val mList = findViewById<View>(R.id.list) as ListView
        mAdapter = MyCustomAdapter(this, arrayList!!)
        mList.adapter = mAdapter


        // connect to the server
//        private val serverIP = "192.168.8.102" //your computer IP address
//        private val serverPort = 2020
        ConnectTask().execute("192.168.8.102","2020")

        send.setOnClickListener {
            val message = editText.text.toString()

            //add the text in the arrayList
            arrayList?.add("c: $message")

            //sends the message to the server
            if (mTcpClient != null) {
                mTcpClient?.sendMessage(message)
            }

            //refresh the list
            mAdapter?.notifyDataSetChanged()
            editText.setText("")
        }
    }

    inner class ConnectTask : AsyncTask<String, String, Void>() {

        override fun doInBackground(vararg params: String): Void? {

            //we create a TCPClient object and
            mTcpClient = TCPClient(params[0], params[1], object : TCPClient.OnMessageReceived {
                //here the messageReceived method is implemented
                override fun messageReceived(message: String) {
                    //this method calls the onProgressUpdate
                    publishProgress(message)
                }
            })
            mTcpClient?.run()

            return null
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)

            //in the arrayList we add the messaged received from server
            arrayList?.add(values[0])
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter?.notifyDataSetChanged()
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            mTcpClient = null
        }
    }
}
