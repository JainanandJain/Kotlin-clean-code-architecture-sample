package com.anand.limitless

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anand.limitless.repository.inDb.StatePostRepository
import com.anand.limitless.ui.StateActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        withDatabase.setOnClickListener {
            show()
        }

    }

    private fun show() {
        val intent = StateActivity.intentFor(this, StatePostRepository.Type.DB)
        startActivity(intent)
    }
}
