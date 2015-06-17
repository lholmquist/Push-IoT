/**
 * Copyright Matthias Weßendorf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wessendorf.devnation.mqttbridge;

import net.wessendorf.devnation.simplepush.OpenShiftIntegrator;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.wessendorf.devnation.DemoConfiguration.*;

public class MQTTBridge {

    private static final Logger LOGGER = Logger.getLogger(MQTTBridge.class.getName());

    final OpenShiftIntegrator pushClient = new OpenShiftIntegrator();


    public static void main(String... args) throws Exception{

        // connect to OpenShift (SimplePush and AeroGear UPS)
        final OpenShiftIntegrator pushClient = new OpenShiftIntegrator();


        // attach a listener that is MQTT aware
        // and publishes something on a topoc
        pushClient.setMessageListener(ack -> {

            try {
                final  MqttClient mqttClient  = new MqttClient(MQTT_URL, "publisher", new MemoryPersistence());
                Date datum = new Date();
                mqttClient.connect();
                mqttClient.publish(MQTT_TOPIC, (datum + " MQTT Bridge received a SimplePush Notification!").getBytes(), 1, false);

                LOGGER.info("Delivered message to MQTT broker");


                mqttClient.disconnect();

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        });


        // prevent the JVM from stopping
        new Semaphore(0).acquire();

    }
}
