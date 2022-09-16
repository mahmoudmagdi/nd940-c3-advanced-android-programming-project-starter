package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME = "file_name"
        const val FILE_URL = "file_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        file_name.text =
            String.format(getString(R.string.file_name), intent.getStringExtra(FILE_NAME))


        go_back.setOnClickListener {
            motion_layout.transitionToEnd {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
