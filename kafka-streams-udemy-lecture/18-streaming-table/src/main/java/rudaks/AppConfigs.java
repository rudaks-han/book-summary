/*
 * Copyright (c) 2019. Prashant Kumar Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package rudaks;

class AppConfigs {
    final static String applicationID = "StreamingTable";
    final static String bootstrapServers = "localhost:9092";
    final static String topicName = "stock-tick";
    final static String stateStoreLocation = "tmp/state-store";
    final static String stateStoreName = "kt01-store";
    final static String regExSymbol = "(?i)HDFCBANK|TCS";
    final static String queryServerHost = "localhost";
    final static int queryServerPort = 7010;

}
