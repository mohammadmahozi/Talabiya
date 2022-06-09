package com.mahozi.sayed.talabiya

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class StorageH {

    fun backUp(context: Context) {
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()

            val currentDBPath = "//data//data//com.mahozi.sayed.talabiya//databases//talabiya_database"
            val backupDBPath = "talabiya//talabiya_database"

            val dbF = File(currentDBPath)
            val f = File(sd, "talabiya_database")
            Log.d("gggg", "backUp: ${f.createNewFile()}")

            val src = FileInputStream(dbF).channel
            val dst = FileOutputStream(f).channel
            dst.transferFrom(src, 0, src.size());
            src.close()
            dst.close()

            //val fis = FileInputStream(dbF)
            //val output = FileOutputStream("talabiya_database.db")

            // Transfer bytes from the input file to the output file
//            val buffer = ByteArray(1024)
//            var length: Int
//            while (fis.read(buffer).also { length = it } > 0) {
//                output.write(buffer, 0, length)
//            }
//            // Close the streams
//            output.flush();
//            output.close();
//            fis.close();

            Toast.makeText(
                context,
                "Backup is successful to SD card",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}