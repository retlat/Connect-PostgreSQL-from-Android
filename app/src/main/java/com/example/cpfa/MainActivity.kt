package com.example.cpfa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLTimeoutException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    var connection: Connection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        super.onPause()

        if (connection?.isClosed == false) {
            connection?.close()
        }
    }

    fun onClickConnect(v: View) {
        thread {
            val p = if (port.text.toString() != "") port.text.toString() else "5432"

            try {
                connection = DriverManager.getConnection(
                    "jdbc:postgresql://${host.text}:${p}/${database.text}",
                    user.text.toString(),
                    password.text.toString()
                )

                runOnUiThread {
                    Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
                }
            } catch (e: SQLException) {
                runOnUiThread {
                    Toast.makeText(this, "Failed to Connect", Toast.LENGTH_LONG).show()
                }

                Log.e(this::class.toString(), e.message, e)
            } catch (e: SQLTimeoutException) {
                runOnUiThread {
                    Toast.makeText(this, "Connection timeout", Toast.LENGTH_LONG).show()
                }

                Log.e(this::class.toString(), e.message, e)
            }
        }
    }

    fun onClickCreate(v: View) {
        if (connection == null || connection?.isClosed == true) {
            Toast.makeText(this, "Connection closed", Toast.LENGTH_LONG).show()
            return
        }

        thread {
            try {
                connection!!.createStatement().use {
                    it.execute("CREATE TABLE IF NOT EXISTS fruits (id SERIAL, name VARCHAR(30))")
                    it.execute("INSERT INTO fruits (name) VALUES ('apple'), ('orange'), ('grape')")
                }

                runOnUiThread {
                    Toast.makeText(this, "Created", Toast.LENGTH_LONG).show()
                }
            } catch (e: SQLException) {
                runOnUiThread {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }

                Log.e(this::class.toString(), e.message, e)
            } catch (e: SQLTimeoutException) {
                runOnUiThread {
                    Toast.makeText(this, "Timeout", Toast.LENGTH_LONG).show()
                }

                Log.e(this::class.toString(), e.message, e)
            }
        }
    }

    fun onClickSelect(v: View) {
        if (connection == null || connection?.isClosed == true) {
            Toast.makeText(this, "Connection closed", Toast.LENGTH_LONG).show()
            return
        }

        thread {
            try {
                connection!!.createStatement().use { s ->
                    s.executeQuery("SELECT * FROM fruits").use {
                        var r = ""

                        while (it.next()) {
                            val id = it.getInt("id")
                            val name = it.getString("name")

                            r += "${id}: ${name}\n"
                        }

                        runOnUiThread { result.text = r }
                    }
                }
            } catch (e: SQLException) {
                runOnUiThread {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }

                Log.e(this::class.toString(), e.message, e)
            }
        }
    }
}
