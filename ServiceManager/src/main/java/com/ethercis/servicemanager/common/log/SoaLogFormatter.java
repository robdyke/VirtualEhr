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


package com.ethercis.servicemanager.common.log;

import com.ethercis.servicemanager.cluster.RunTimeSingleton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A specific formatter for java.common.logging
 *
 */
public class SoaLogFormatter extends Formatter {
   private final String id;

   private static long instanceCounter;

   private RunTimeSingleton glob;

   /** Colored response to xterm */
   private boolean withXtermEscapeColor = false;

   /** Using ISO 8601 time or the default java.common.logging time? */
   private boolean iso8601Time = true;
   public final SimpleDateFormat iso8601Formater;
   private String iso8601Fmt = "yyyy-MM-dd HH:mm:ss.S";
   private String iso8601Timezone= null; // local time, else choose e.g. "GMT"

   private Date dat = new Date();

   private final static String format = "{0,date} {0,time}";

   private MessageFormat formatter;

   private Object args[] = new Object[1];

   private String lineSeparator;

   /** Reset color to original values */
   public static final String ESC = "\033[0m";

   // private static final String BOLD = "\033[1m";

   /** color foreground/background (for xterm).
      The ESC [30m ... ESC [38m should set the foreground
      color, and ESC [40m .. ESC [48m should set the background color
   */
   public static final String RED_BLACK = "\033[31;40m";

   public static final String GREEN_BLACK = "\033[32;40m";

   public static final String YELLOW_BLACK = "\033[33;40m";

   public static final String BLUE_BLACK = "\033[34;40m";
   public static final String PINK_BLACK = "\033[35;40m";

   public static final String LTGREEN_BLACK = "\033[36;40m";

   public static final String WHITE_BLACK = "\033[37;40m";
   public static final String WHITE_GREEN = "\033[37;42m";

   public static final String WHITE_RED = "\033[37;41m";
   public static final String BLACK_RED = "\033[30;41m";
   public static final String BLACK_GREEN = "\033[40;42m";
   public static final String BLACK_PINK = "\033[40;45m";
   public static final String BLACK_LTGREEN= "\033[40;46m";

   private final String severe;

   private final String warning;

   private final String info;

   private final String fine;

   private final String finer;

   private final String finest;

   /** Output text for different logging levels */
   public static final String severeX =  new String("SEVERE ");

   public static final String warningX = new String("WARNING");

   public static final String infoX =    new String(" INFO  ");

   public static final String fineX =    new String(" FINE  ");

   public static final String finerX =   new String(" FINER ");

   public static final String finestX =  new String("FINEST ");

   public static final String severeE = new String(RED_BLACK +     "SEVERE " + ESC);

   public static final String warningE = new String(YELLOW_BLACK + "WARNING" + ESC);

   public static final String infoE = new String(GREEN_BLACK +     " INFO  " + ESC);

   public static final String fineE = new String(LTGREEN_BLACK +   " FINE  " + ESC);

   public static final String finerE = new String(PINK_BLACK +     " FINER " + ESC);

   public static final String finestE = new String(WHITE_BLACK +   "FINEST " + ESC);

   public SoaLogFormatter() {
      this("default");
   }

   public SoaLogFormatter(String id) {
      this.id = id + "-" + instanceCounter++;

      String tmp = System.getProperty("ehrserver/iso8601Time");
      if (tmp != null) {
         iso8601Time = Boolean.valueOf(tmp).booleanValue();
      }
      iso8601Timezone = System.getProperty("ehrserver/iso8601Timezone");
      tmp = System.getProperty("ehrserver/iso8601Fmt");
      if (tmp != null)
         iso8601Fmt = tmp;

      iso8601Formater = new SimpleDateFormat(iso8601Fmt, Locale.US);
      if (iso8601Timezone != null)
         iso8601Formater.setTimeZone(TimeZone.getTimeZone(iso8601Timezone));

      if (withXtermColors()) {
         this.withXtermEscapeColor = true;
         this.severe = severeE;
         this.warning = warningE;
         this.info = infoE;
         this.fine = fineE;
         this.finer = finerE;
         this.finest = finestE;
      } else {
         this.withXtermEscapeColor = false;
         this.severe = severeX;
         this.warning = warningX;
         this.info = infoX;
         this.fine = fineX;
         this.finer = finerX;
         this.finest = finestX;
      }
      this.lineSeparator = System.getProperty("line.separator", "\n");
   }

   public void setRunTimeSingleton(RunTimeSingleton glob) {
      this.glob = glob;
   }

   /**
    * If we may switch on xterm colors.
    * java -DxmlBlaster/supressXtermColors ... suppresses those
    * @return true for none Linux systems
    */
   public static boolean withXtermColors() {
      String suppress = System.getProperty("ehrserver/supressXtermColors");
      if (suppress != null) return false;
      String osName = System.getProperty("os.name"); // "Linux" "Windows NT"...
      return !(osName == null || osName.startsWith("Window"));
   }

   public String convertLevelToString(int level) {
      if (level == Level.INFO.intValue())
         return info;
      else if (level == Level.WARNING.intValue())
         return warning;
      else if (level == Level.SEVERE.intValue())
         return severe;
      else if (level == Level.FINE.intValue())
         return fine;
      else if (level == Level.FINER.intValue())
         return finer;
      else if (level == Level.FINEST.intValue())
         return finest;
      else
         return "LEVEL" + level;
   }

   /*
    * (non-Javadoc)
    *
    * @see java.common.logging.Formatter#format(java.common.logging.LogRecord)
    */
   public String format(LogRecord record) {
      if (record == null) {
         return "";
      }
      String levelString = convertLevelToString(record.getLevel().intValue());
      StringBuffer sb = new StringBuffer();
      // Minimize memory allocations here.
      dat.setTime(record.getMillis());
      if (iso8601Time) {
         sb.append(iso8601Formater.format(dat));
         if ("GMT".equals(iso8601Timezone))
            sb.append("Z");
      }
      else {
         args[0] = dat;
         StringBuffer text = new StringBuffer();
         if (formatter == null) {
            formatter = new MessageFormat(format);
         }
         formatter.format(args, text, null);
         sb.append(text);
      }
      sb.append(" ");
      sb.append(levelString);//record.getLevel().getLocalizedName());
      sb.append(" ");
      if (record.getThreadID() > 0) {
         sb.append(""+record.getThreadID()).append("-");
         sb.append(Thread.currentThread().getName());
         sb.append(" ");
      }
      RunTimeSingleton g = this.glob;
      if (g != null && g.getRunlevel() != -1) {
         sb.append("RL").append(g.getRunlevel()).append(" ");
      }
      if (record.getSourceClassName() != null) {
         sb.append(record.getSourceClassName());
      } else {
         sb.append(record.getLoggerName());
      }
      if (record.getSourceMethodName() != null) {
         sb.append(" ");
         sb.append(record.getSourceMethodName());
      }
      String message = formatMessage(record);
      sb.append(": ");
      sb.append(message);
      sb.append(lineSeparator);
      if (record.getThrown() != null) {
         try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            sb.append(sw.toString());
         } catch (Exception ex) {
         }
      }
      return sb.toString();
   }

   /**
    * @return Returns the id.
    */
   public String getId() {
      return this.id;
   }

   /**
    * @return Returns the withXtermEscapeColor.
    */
   public boolean isWithXtermEscapeColor() {
      return this.withXtermEscapeColor;
   }

   /**
    * @return Returns the iso8601Time.
    */
   public boolean iso8601Time() {
      return this.iso8601Time;
   }

}

