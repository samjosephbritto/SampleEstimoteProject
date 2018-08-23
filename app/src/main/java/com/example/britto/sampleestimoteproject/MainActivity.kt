package com.example.britto.sampleestimoteproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.scanning_plugin.api.EstimoteBluetoothScannerFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RequirementsWizardFactory.createEstimoteRequirementsWizardForAndroidThings()
                .fulfillRequirements(
                        applicationContext,
                        onRequirementsFulfilled = {

                            val scanner = EstimoteBluetoothScannerFactory(applicationContext).getSimpleScanner()
                            val scanHandler = scanner.estimoteTelemetryFullScan()
                                    .withLowLatencyPowerMode()
                                    .withOnPacketFoundAction {
                                        Log.e("TAG", "Identifier Equals ${it.identifier} " +
                                                "New light level: ${it.ambientLightInLux} " +
                                                "Temperature : ${it.temperatureInCelsiusDegrees} " +
                                                "Accelerometer X: ${it.acceleration.xAxis } " +
                                                "Accelerometer Y: ${it.acceleration.yAxis } " +
                                                "Accelerometer Z: ${it.acceleration.zAxis } " +
                                                "Motion State : ${it.motionState}")

                                    }.start()
                        },
                        onRequirementsMissing = {
                            Log.e("TAG", "Unable to start scan. Requirements not fulfilled: ${it.joinToString()}")
                        },
                        onError = {
                            Log.e("TAG", "Error while checking requirements: ${it.message}")
                        })


    }
}
