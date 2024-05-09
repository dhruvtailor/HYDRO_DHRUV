package com.example.hydro_dhruv

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hydro_dhruv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MAIN_ACTIVITY"

    lateinit var binding: ActivityMainBinding

    private val morningRate: Double = 0.132
    private val eveningRate: Double = 0.094
    private val salesTaxRate: Double = 0.13

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener {
            if (binding.etMorningUsage.text.toString() != "" && binding.etEveningUsage.text.toString() != "") {
                binding.tvError.visibility = View.GONE

                var morningUsage: Double = binding.etMorningUsage.text.toString().toDouble()
                var eveningUsage: Double = binding.etEveningUsage.text.toString().toDouble()

                calculateBill(morningUsage, eveningUsage)
            } else {
                binding.tvError.visibility = View.VISIBLE
                binding.resultLayout.visibility = View.GONE
            }
        }

        binding.btnReset.setOnClickListener {
            binding.etMorningUsage.text.clear()
            binding.etEveningUsage.text.clear()

            binding.tvChargeBreakdown.text = ""
            binding.tvTotalPayable.text = ""

            binding.tvError.visibility = View.GONE
            binding.resultLayout.visibility = View.GONE
        }
    }

    private fun calculateBill(morningUsage: Double, eveningUsage: Double) {
        var morningUsageCost: Double = Math.round((morningUsage * morningRate) * 1000.0) / 1000.0
        var eveningUsageCost: Double = Math.round((eveningUsage * eveningRate) * 1000.0) / 1000.0

        var totalUsageCost: Double = morningUsageCost + eveningUsageCost
        var environmentalRebate: Double = 0.0

        if (binding.swReusableEnergy.isChecked) {
            environmentalRebate = Math.round((totalUsageCost * 0.09) * 1000.0) / 1000.0
        }

        var subTotal: Double = Math.round((totalUsageCost - environmentalRebate) * 1000.0) / 1000.0
        var salesTax: Double = Math.round((totalUsageCost * salesTaxRate) * 1000.0) / 1000.0
        var totalPayable = subTotal + salesTax

        binding.tvChargeBreakdown.text = """
            Morning Usage Cost: $${morningUsageCost}
            Evening Usage Cost: $${eveningUsageCost}
            Total Usage Cost: $${totalUsageCost}
            Environmental Rebate: $${environmentalRebate}
            Subtotal: $${subTotal}
            Tax: $${salesTax}
        """.trimIndent()

        binding.tvTotalPayable.text = "YOU MUST PAY: $${totalPayable}"

        binding.resultLayout.visibility = View.VISIBLE
    }
}