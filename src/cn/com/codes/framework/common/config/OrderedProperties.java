package cn.com.codes.framework.common.config;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class OrderedProperties extends Properties{


    public void store(OutputStream out, String header)
        throws IOException {
        Vector keys = new Vector();

        for (Enumeration e = this.propertyNames(); e.hasMoreElements();) {
            keys.addElement((String) (e.nextElement()));
        }

        //sort them
        Collections.sort(keys);

        //write the header
        DataOutputStream dataoutputstream = new DataOutputStream(out);
        dataoutputstream.writeBytes("#" + header + "\n");

        //write the date/time
        Date now = new Date();
        //dataoutputstream.writeBytes("#" + now + "\n");


        //now, loop through and write out the properties
        String oneline;
        String thekey;
        String thevalue;

        for (int i = 0; i < keys.size(); i++) {
            thekey = (String) keys.elementAt(i);
            thevalue = (String) this.getProperty(thekey);
            thevalue = doubleSlash(thevalue);

            oneline = thekey + "=" + thevalue + "\n";
            dataoutputstream.writeBytes(oneline);
        }

        dataoutputstream.flush();
        dataoutputstream.close();
    }


    private String doubleSlash(String orig) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < orig.length(); i++) {
            if (orig.charAt(i) == '\\') {
                buf.append("\\\\");
            } else {
                buf.append(orig.charAt(i));
            }
        }

        return buf.toString();
    }
}
