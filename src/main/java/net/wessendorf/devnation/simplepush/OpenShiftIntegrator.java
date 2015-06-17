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
package net.wessendorf.devnation.simplepush;

import org.jboss.aerogear.simplepush.MessageListener;
import org.jboss.aerogear.simplepush.SimplePushClient;
import org.jboss.aerogear.unifiedpush.PushConfig;
import org.jboss.aerogear.unifiedpush.UnifiedPushClient;

import java.util.logging.Logger;

import static net.wessendorf.devnation.DemoConfiguration.*;

public class OpenShiftIntegrator {

    private static final Logger LOGGER = Logger.getLogger(OpenShiftIntegrator.class.getName());

    final UnifiedPushClient unifiedPushClient = new UnifiedPushClient(UPS_URL, UPS_VARIANT_ID, UPS_VARIANT_SECRET);
    final SimplePushClient simplePushClient = new SimplePushClient(SPS_URL);


    public OpenShiftIntegrator() {

        // 1) connect to WSS based Push Network
        simplePushClient.connect();

        // 2) register on push network (for channel)
        simplePushClient.register((channelId, simplePushEndPoint) -> {
            LOGGER.info("DeviceToken (URI) for PUT updates: " + simplePushEndPoint);

            // 3) returned payload from Push Network is stored on PUS
            final PushConfig config = new PushConfig();
            config.setDeviceToken(simplePushEndPoint);
            unifiedPushClient.register(config);
        });
    }

    /**
     * Take message handler / lamda for incoming SimplePush notifications
     */
    public void setMessageListener(MessageListener listener) {
        // 4) apply a given Message Handler!
        this.simplePushClient.addMessageListener(listener);
    }
}
