package org.dbs.robot.driver.arduino.serial;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Component;

/**
 * Implementation of SerialPortFactory that delegates to jSerialComm's SerialPort.
 */
@Component
public class JSerialCommFactory implements SerialPortFactory {

    @Override
    public SerialPortWrapper[] getCommPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        SerialPortWrapper[] wrappers = new SerialPortWrapper[ports.length];
        
        for (int i = 0; i < ports.length; i++) {
            wrappers[i] = new JSerialCommWrapper(ports[i]);
        }
        
        return wrappers;
    }
}