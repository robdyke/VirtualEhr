/*
 * Copyright (c) 2015 Christian Chevalley
 * This file is part of Project Ethercis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ethercis.servicemanager.runlevel;

/**
 * ETHERCIS Project VirtualEhr
 * Created by Christian Chevalley on 6/25/2015.
 */
public interface I_ServiceRunMode {
    /** indicate the number of sessions used by this user */

    //conditional service parameters
    enum DialectSpace {
        STANDARD {
            @Override
            public String toString(){
                return "STANDARD";
            }
        },
        EHRSCAPE {
            @Override
            public String toString(){
                return "EHRSCAPE";
            }
        };

        public DialectSpace value(String val){
            switch (val){
                case "STANDARD": return STANDARD;
                case "EHRSCAPE": return EHRSCAPE;
                default :
                    return null;
            }
        }
    }

    public final String SERVER_DIALECT_PARAMETER = "server.mode.dialect";

}
