package com.example.getandpostlocation

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.postrequestapprevisited.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val apiInterface = APIClient().getClient()?.create(ApiInterface::class.java)
    lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPost.setOnClickListener {
            val obUser=DataItem(edName.text.toString(),edLocation.text.toString())
            post(obUser)
        }
        btnGetLocation.setOnClickListener {
            getUserLocation()
        }
    }
    fun post(obUser:DataItem){
        funProgressDialog()

        if (apiInterface!=null){
            apiInterface.addUserToApi(obUser).enqueue(object : Callback<DataItem?> {
                override fun onResponse(call: Call<DataItem?>, response: Response<DataItem?>) {
                    progressDialog.dismiss()
                    edName.setText("")
                    edLocation.setText("")
                    Toast.makeText(applicationContext, "user added", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<DataItem?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Error ", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun getUserLocation() {
        funProgressDialog()
        if (apiInterface != null) {
            apiInterface.getUser()?.enqueue(object : Callback<Array<DataItem>?> {
                override fun onResponse(
                    call: Call<Array<DataItem>?>,
                    response: Response<Array<DataItem>?>
                ) {
                    progressDialog.dismiss()
                    for (user in response.body()!!) {
                        if(user.name==edNameToGet.text.toString()){
                            textView.text=user.location

                        }
                    }
                    Toast.makeText(applicationContext, "not found ", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Array<DataItem>?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    fun funProgressDialog(){
        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
    }
}