package network.sinadarvishi.simplemessenger

import android.os.AsyncTask
import android.os.Bundle

import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var arrayList: ArrayList<String>
    private lateinit var adapter: MyCustomAdapter
    private var tcpClient: TCPClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayList = ArrayList()
        adapter = MyCustomAdapter(this, arrayList)
        list.adapter = adapter


        // connect to the server
        ConnectTask().execute("192.168.8.102", "2020")

        send_button.setOnClickListener {
            val message = editText.text.toString()
            //add the text in the arrayList
            arrayList.add("c: $message")
            //sends the message to the server
            tcpClient?.sendMessage(message)
            //refresh the list
            adapter.notifyDataSetChanged()
            editText.setText("")
        }
    }

    inner class ConnectTask : AsyncTask<String, String, Void>() {

        override fun doInBackground(vararg params: String): Void? {

            //we create a TCPClient object and
            tcpClient = TCPClient(params[0], params[1], object : TCPClient.OnMessageReceived {
                //here the messageReceived method is implemented
                override fun messageReceived(message: String) {
                    //this method calls the onProgressUpdate
                    publishProgress(message)
                }
            })
            tcpClient?.run()

            return null
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)

            //in the arrayList we add the messaged received from server
            arrayList.add(values[0])
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            adapter.notifyDataSetChanged()
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            tcpClient?.stopClient()
            tcpClient = null
        }
    }
}
