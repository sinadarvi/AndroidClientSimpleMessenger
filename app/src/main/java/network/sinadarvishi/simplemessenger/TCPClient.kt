package network.sinadarvishi.simplemessenger

import android.util.Log

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

/**
 * Created by Sina on 12/26/2016.
 */

class TCPClient(listener: OnMessageReceived) {
    private var serverMessage: String? = null
    private var mMessageListener: OnMessageReceived? = null
    private var mRun = false

    internal var printWriter: PrintWriter? = null
    internal var bufferedReader: BufferedReader? = null

    private val serverIP = "192.168.8.102" //your computer IP address
    private val serverPort = 2020

    //Declare the interface. The method messageReceived(String message) will must be implemented bufferedReader the MyActivity
    //class at on asynckTask doInBackground
    interface OnMessageReceived {
        fun messageReceived(message: String)
    }

    init {
        mMessageListener = listener
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    fun sendMessage(message: String) {
        if (printWriter != null && !printWriter!!.checkError()) {
            printWriter!!.println(message)
            printWriter!!.flush()
        }
    }

    fun stopClient() {
        mRun = false
    }

    fun run() {

        mRun = true

        try {
            //here you must put your computer's IP address.
            val serverAddr = InetAddress.getByName(serverIP)

            Log.e("TCP Client", "C: Connecting...")

            //create a socket to make the connection with the server
            val socket = Socket(serverAddr, serverPort)

            try {

                //send the message to the server
                printWriter = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)

                Log.e("TCP Client", "C: Sent.")

                Log.e("TCP Client", "C: Done.")

                //receive the message which the server sends back
                bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

                //bufferedReader this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = bufferedReader?.readLine()

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener!!.messageReceived(serverMessage!!)
                    }
                    serverMessage = null

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '$serverMessage'")

            } catch (e: Exception) {

                Log.e("TCP", "S: Error", e)

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close()
            }

        } catch (e: Exception) {

            Log.e("TCP", "C: Error", e)

        }

    }
}
