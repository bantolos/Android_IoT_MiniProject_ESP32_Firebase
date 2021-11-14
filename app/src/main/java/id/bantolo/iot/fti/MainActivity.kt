package id.bantolo.iot.fti

import android.content.ContentValues
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import id.bantolo.iot.fti.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Write a message to the database
        database = Firebase.database.reference

        binding.setByIntensitasCahaya.setOnClickListener {
            if (binding.setByIntensitasCahaya.isChecked){
                database.child("Relay/setByIntensitasCahaya").setValue(1)
            }
            else{
                database.child("Relay/setByIntensitasCahaya").setValue(0)
            }
        }

        binding.lampu.setOnClickListener {
            if (binding.lampu.isChecked){
                database.child("Relay/Lampu").setValue(1)
            }
            else{
                database.child("Relay/Lampu").setValue(0)
            }
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val intensitasCahaya = dataSnapshot.child("SensorCahaya/IntensitasCahaya").getValue<Double>().toString()
                binding.tvIntensitasCahaya.text = intensitasCahaya

                val kondisiLampu = dataSnapshot.child("Relay/Lampu").getValue<Int>()

                if(kondisiLampu == 1) {
                    binding.lampu.isChecked = true
                } else {
                    binding.lampu.isChecked = false
                }

                if (binding.lampu.isChecked){
                    binding.bg.setBackgroundColor(Color.WHITE)
                }
                else{
                    binding.bg.setBackgroundColor(Color.LTGRAY)
                }


                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
    }
}