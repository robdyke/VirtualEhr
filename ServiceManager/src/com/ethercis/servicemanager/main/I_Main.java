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

/*
This code is a refactoring and adaptation of the original
work provided by the XmlBlaster project (see http://xmlblaster.org)
for more details.
This code is therefore supplied under LGPL 2.1
 */

/**
 * Project: EtherCIS system application
 * 
 * @author <a href="mailto:christian@adoc.co.th">Christian Chevalley</a>
 * @author <a href="mailto:michele@laghi.eu">Michele Laghi</a>
 * @author <a href="mailto:xmlblast@marcelruff.info">Marcel Ruff</a>
 */


package com.ethercis.servicemanager.main;

import com.ethercis.servicemanager.cluster.RunTimeSingleton;


/**
 * I_Main interface is a minimized interface to control Main.java.
 * <p />
 * It allows instantiating ehrserver for test purpose.
 */
public interface I_Main
{
   public void init(RunTimeSingleton g);
   public void init(java.util.Properties p);
   /** Same logonservice shutdown() but does additionally an engine.global.shutdown() */
   public void destroy();
   /** Release all knowledge */
   public void shutdown();
   public boolean isHalted();
   public RunTimeSingleton getRunTimeSingleton();
}
