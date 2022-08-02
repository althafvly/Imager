package org.sorrybro.imager

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File

class RemoteService : Service() {

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private lateinit var mMessenger: Messenger

    /**
     * Handler of incoming messages from clients.
     */
    internal class IncomingHandler(
        context: Context,
        private val mContext: Context = context.applicationContext
    ) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {

            val tag = "RemoteService"
            // caller package should he asking for this int to get image.
            val image = 123
            val lastSaveImage = "temp.jpg"

            val callerId =
                mContext.packageManager.getNameForUid(msg.sendingUid)

            // check if the caller is allowed app
            if (callerId == "com.android.testapp") {
                when (msg.what) {

                    image -> {
                        // Reply
                        try {
                            // make the RPC invocation
                            val message = Message.obtain(null, image, 0, 0)
                            val file = File(mContext.dataDir.toString(), lastSaveImage)
                            if (file.exists()) {
                                // Send image as bytearray to the caller
                                val imageByte = ByteArrayOutputStream()
                                val bitmap = BitmapFactory.decodeFile(file.path)
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageByte)
                                message.data = Bundle().apply {
                                    putByteArray("image", imageByte.toByteArray())
                                }
                            }
                            val replyTo = msg.replyTo
                            replyTo.send(message)
                        } catch (e: RemoteException) {
                            Log.e(tag,"Invocation Failed !! $e")
                        }
                    }

                    else -> {
                        Log.e(tag,"Unknown call !!")
                    }
                }
            }
        }
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }
}