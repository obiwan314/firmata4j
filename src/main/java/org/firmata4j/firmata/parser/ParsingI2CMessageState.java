/* 
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Oleg Kurbatov (o.v.kurbatov@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.firmata4j.firmata.parser;

import org.firmata4j.fsm.AbstractState;
import org.firmata4j.fsm.Event;
import org.firmata4j.fsm.FiniteStateMachine;

import static org.firmata4j.firmata.parser.FirmataToken.*;

/**
 * This class parses inbound I2C messages and publishes them when they are complete.
 * @author William Reichardt;
 */
public class ParsingI2CMessageState extends AbstractState {

    private int portId, counter, value;

    public ParsingI2CMessageState(FiniteStateMachine fsm) {
        super(fsm);
    }

    // Called once for each byte in the message

    /*
     * /* I2C reply
     * -------------------------------
     * 0  START_SYSEX (0xF0) (MIDI System Exclusive)
     * 1  I2C_REPLY (0x77)
     * 2  slave address (LSB)
     * ... Lotsa Bytes
     * n  END_SYSEX (0xF7)
     */

    /** @param b the input byte
     */
    @Override
    public void process(byte b) {
        if (b == END_SYSEX) {
            byte[] buffer = getBuffer();
            Event event = new Event(I2C_MESSAGE, FIRMATA_MESSAGE_EVENT_TYPE);
            event.setBodyItem(I2C_MESSAGE, buffer);
            transitTo(WaitingForMessageState.class);
            publish(event);
        } else {
            bufferize(b);
        }
    }

}
