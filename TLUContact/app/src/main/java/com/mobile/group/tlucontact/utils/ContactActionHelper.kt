package com.mobile.group.tlucontact.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class ContactActionHelper {
    companion object {
        fun sendSms(context: Context, phoneNumber: String) {
            val smsIntent = Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + phoneNumber));
            context.startActivity(smsIntent);
        }

        fun call(context: Context, phoneNumber: String) {
            val callIntent = Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(callIntent);
        }

        fun sendEmail(context: Context, email: String) {
            val emailIntent = Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            context.startActivity(emailIntent);
        }

        fun copyToClipboard(context: Context, label: String, value: String) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clipData = ClipData.newPlainText(label, value)
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, label + " đã được sao chép!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}