package com.example.dailycalorieneedcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTextHeight: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var editTextAge: EditText
    private lateinit var spinnerActivityLevel: Spinner
    private lateinit var checkBoxMale: CheckBox
    private lateinit var checkBoxFemale: CheckBox
    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewDes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // UI elemanlarını tanımla
        editTextHeight = findViewById(R.id.editTextHeight)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextAge = findViewById(R.id.editTextAge)
        spinnerActivityLevel = findViewById(R.id.spinnerActivityLevel)
        checkBoxMale = findViewById(R.id.checkBoxMale)
        checkBoxFemale = findViewById(R.id.checkBoxFemale)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        textViewResult = findViewById(R.id.textViewResult)
        textViewDes = findViewById(R.id.textViewDes)

        // Hesapla butonuna tıklandığında işlemi yapar
        buttonCalculate.setOnClickListener {
            calculateDailyCalorieNeeds()
        }

        val activityLevels = arrayOf(
            " ", "Sedanter (Hareket etmiyorum veya çok az hareket ediyorum.)",
            "Az hareketli (Hafif hareketli bir yaşam / Haftada 1-3 gün egzersiz yapıyorum.)",
            "Orta derece hareketli (Hareketli bir yaşam / Haftada 3-5 gün egzersiz yapıyorum.)",
            "Çok hareketli (Çok hareketli bir yaşam / Haftada 6-7 gün egzersiz yapıyorum.)",
            "Aşırı hareketli (Profesyonel sporcu, atlet seviyesi.)"
        )
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activityLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActivityLevel.adapter = adapter


        // CheckBox aynı anda seçilmesini engelle

        checkBoxMale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxFemale.isChecked =
                    false
                checkBoxFemale.isEnabled = false
            } else {
                checkBoxFemale.isEnabled = true
            }
        }

        checkBoxFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBoxMale.isChecked =
                    false
                checkBoxMale.isEnabled = false
            } else {
                checkBoxMale.isEnabled = true
            }
        }

    }

    private fun calculateDailyCalorieNeeds() {
        // Kullanıcının girdiği verileri al
        val height = editTextHeight.text.toString().toFloatOrNull() ?: 0f
        val weight = editTextWeight.text.toString().toFloatOrNull() ?: 0f
        val age = editTextAge.text.toString().toIntOrNull() ?: 0
        val activityLevel = spinnerActivityLevel.selectedItem.toString()
        val gender = if (checkBoxMale.isChecked) "male" else "female"

        // Aktivite düzeyine göre aktivite faktörünü hesapla
        var activityFactor = 1.0
        when (activityLevel) {
            "Sedanter (Hareket etmiyorum veya çok az hareket ediyorum.)" -> activityFactor = 1.2
            "Az hareketli (Hafif hareketli bir yaşam / Haftada 1-3 gün egzersiz yapıyorum.)" -> activityFactor = 1.375
            "Orta derece hareketli (Hareketli bir yaşam / Haftada 3-5 gün egzersiz yapıyorum.)" -> activityFactor = 1.55
            "Çok hareketli (Çok hareketli bir yaşam / Haftada 6-7 gün egzersiz yapıyorum.)" -> activityFactor = 1.725
            "Aşırı hareketli (Profesyonel sporcu, atlet seviyesi.)" -> activityFactor = 1.9
        }

        // Cinsiyete ve aktivite faktörüne göre günlük kalori ihtiyacını hesapla

        val weightText = editTextWeight.text.toString().trim()
        val heightText = editTextHeight.text.toString().trim()
        val ageText = editTextAge.text.toString().trim()
        //val cbTerms1 = findViewById<CheckBox>(R.id.checkBoxFemale)
        //val cbTerms2 = findViewById<CheckBox>(R.id.checkBoxMale)
        val termsChecked = findViewById<CheckBox>(R.id.checkBoxFemale).isChecked || findViewById<CheckBox>(R.id.checkBoxMale).isChecked

        if (weightText.isEmpty() || heightText.isEmpty() || ageText.isEmpty() || spinnerActivityLevel.selectedItemPosition == 0 || !termsChecked) {
            // Gerekli alanlar boş ise
            Toast.makeText(
                applicationContext,
                "Lütfen tüm alanları doldurup terkrar deneyin.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Tüm alanlar dolu ise
            val weight = weightText.toDouble()
            val height = heightText.toDouble()
            val age = ageText.toInt()

            // Bazal Metabolik Hız (BMR) hesaplaması
            val basalMetabolicRate: Float = if (gender == "male") {
                (10 * weight + 6.25 * height - 5 * age + 5).toFloat()
            } else {
                (10 * weight + 6.25 * height - 5 * age - 161).toFloat()
            }

            // Aktivite faktörüne göre günlük kalori ihtiyacının hesaplanması
            val dailyCalorieNeeds = (basalMetabolicRate * activityFactor).toInt()


            // Sonucu ekrana yazdır
            textViewResult.text = "Günlük Kalori İhtiyacınız: $dailyCalorieNeeds kcal"
            textViewDes.text =
                "Kalori, yiyecek veya içeceklerin vücuda sağladığı enerji birimidir. Günlük kalori ihtiyacı ise, günlük olarak aktivite seviyenize göre ihtiyaç duyduğunuz kalori miktarı anlamına gelir. Günlük kalori ihtiyacı hesaplanırken bazal metabolizma hızı ve aktivite seviyesi katsayısı dikkate alınır."

        }
    }
}
