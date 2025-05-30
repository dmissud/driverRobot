package org.dbs.robot.driverrobot.arduino.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementation of SerialPortWrapper that delegates to jSerialComm's SerialPort.
 */
public class JSerialCommWrapper implements SerialPortWrapper {
    private final SerialPort serialPort;

    /**
     * Constructor that wraps a SerialPort instance.
     *
     * @param serialPort The SerialPort instance to wrap
     */
    public JSerialCommWrapper(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public String getSystemPortName() {
        return serialPort.getSystemPortName();
    }

    @Override
    public String getDescriptivePortName() {
        return serialPort.getDescriptivePortName();
    }

    @Override
    public boolean setBaudRate(int baudRate) {
        return serialPort.setBaudRate(baudRate);
    }

    @Override
    public void setComPortTimeouts(int mode, int readTimeout, int writeTimeout) {
        serialPort.setComPortTimeouts(mode, readTimeout, writeTimeout);
    }

    @Override
    public boolean openPort() {
        return serialPort.openPort();
    }

    @Override
    public boolean isOpen() {
        return serialPort.isOpen();
    }

    @Override
    public InputStream getInputStream() {
        return serialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return serialPort.getOutputStream();
    }

    @Override
    public boolean closePort() {
        return serialPort.closePort();
    }
}