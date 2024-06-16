package com.example.lab6variant4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab6variant4.ui.theme.Lab6Variant4Theme
import com.example.lab6variant4.network.Api.retrofitService
import com.example.lab6variant4.network.PhoneNumberResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab6Variant4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainUi(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


    private fun sendRequest(phoneNumber: String, onResponse: (PhoneNumberResponse?) -> Unit) {
        val jsonBody = "[ \"$phoneNumber\" ]"
        val call = retrofitService.cleanPhone(jsonBody)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("uwu", "Response: $responseBody")
                    responseBody?.let {
                        val responseObject = Gson().fromJson(it, Array<PhoneNumberResponse>::class.java).firstOrNull()
                        onResponse(responseObject)
                    } ?: onResponse(null)
                } else {
                    Log.d("uwu", "Error: ${response.raw()}")
                    onResponse(null)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("uwu", "Error: ${t.message}")
                onResponse(null)
            }
        })
    }

    @Composable
    fun MainUi(name: String, modifier: Modifier = Modifier) {
        var userInputText by remember { mutableStateOf("раб 846)231.60.14 *139") }
        var phoneResponse by remember { mutableStateOf<PhoneNumberResponse?>(null) }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Column {
                TextField(
                    value = userInputText,
                    onValueChange = { userInputText = it },
                    label= { Text("Enter phone number") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        sendRequest(userInputText) { response ->
                            phoneResponse = response
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Send request")
                }

                phoneResponse?.let {
                    ResponseDisplay(it)
                }

            }
        }

    }

    @Composable
    fun ResponseDisplay(response: PhoneNumberResponse) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text("Source: ${response.source}", style = MaterialTheme.typography.bodyLarge)
            Text("Type: ${response.type}", style = MaterialTheme.typography.bodyLarge)
            Text("Phone: ${response.phone}", style = MaterialTheme.typography.bodyLarge)
            Text("Country Code: ${response.countryCode}", style = MaterialTheme.typography.bodyLarge)
            Text("City Code: ${response.cityCode}", style = MaterialTheme.typography.bodyLarge)
            Text("Number: ${response.number}", style = MaterialTheme.typography.bodyLarge)
            response.extension?.let { Text("Extension: $it", style = MaterialTheme.typography.bodyLarge) }
            Text("Provider: ${response.provider}", style = MaterialTheme.typography.bodyLarge)
            Text("Country: ${response.country}", style = MaterialTheme.typography.bodyLarge)
            Text("Region: ${response.region}", style = MaterialTheme.typography.bodyLarge)
            Text("City: ${response.city}", style = MaterialTheme.typography.bodyLarge)
            Text("Timezone: ${response.timezone}", style = MaterialTheme.typography.bodyLarge)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Lab6Variant4Theme {
            MainUi("Android")
        }
    }
}

