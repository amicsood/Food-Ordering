package com.samael.foodordering.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.samael.foodordering.R


class CustomerSupportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_customer_support, container, false)
        val sendbtn = view.findViewById<Button>(R.id.mailButton)

        sendbtn.setOnClickListener {
            val email = "abc@gmail.com"
            val subject = view.findViewById<EditText>(R.id.EmailSubject).text.toString()
            val message = view.findViewById<EditText>(R.id.EmailMessage).text.toString()


            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            val urlString =
                "mailto:" + Uri.encode(email) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(
                    message
                )
            selectorIntent.data = Uri.parse(urlString)

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.selector = selectorIntent
            if (intent.resolveActivity(activity!!.packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Send email"))
            } else {
                Toast.makeText(
                    activity,
                    "Required App is not installed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return view
    }

}
