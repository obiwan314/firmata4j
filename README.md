## firmata4j
**firmata4j** is a client library of [Firmata](http://www.firmata.org/) written
in Java. The library allows controlling Arduino (or another
board) which runs Firmata protocol from your java program.

## Capabilities
- Interaction with a board and its pins in object-oriented style
- Abstraction over details of the protocol
- Provides an UI component that visualize the current state of every pin and
allows changing a mode and state of each of those

## Installation

### Maven
Add the following dependency to `pom.xml` of your project:

```xml
<dependency>
    <groupId>com.github.kurbatov</groupId>
    <artifactId>firmata4j</artifactId>
    <version>2.3.2</version>
</dependency>
```

## Usage
General scenario of usage is following:
```java
IODevice device = new FirmataDevice("/dev/ttyUSB0"); // construct the Firmata device instance using the name of a port
device.start(); // initiate communication to the device
device.ensureInitializationIsDone(); // wait for initialization is done
// do actual work here
device.stop(); // stop communication to the device
```

The "actual work" consists in sending commands to the board and processing
events that occur on it.

To react to events we have to subscribe listeners of the device or its pins.

```java
device.addEventListener(new IODeviceEventListener() {
    @Override
    public void onStart(IOEvent event) {
        // since this moment we are sure that the device is initialized
        // so we can hide initialization spinners and begin doing cool stuff
        System.out.println("Device is ready");
    }

    @Override
    public void onStop(IOEvent event) {
        // since this moment we are sure that the device is properly shut down
        System.out.println("Device has been stopped");
    }

    @Override
    public void onPinChange(IOEvent event) {
        // here we react to changes of pins' state
        Pin pin = event.getPin();
        System.out.println(
                String.format(
                    "Pin %d got a value of %d",
                    pin.getIndex(),
                    pin.getValue())
            );
    }

    @Override
    public void onMessageReceive(IOEvent event, String message) {
        // here we react to receiving a text message from the device
        System.out.println(message);
    }
});
```

To obtain more fine grained control we can subscribe to events of a particular
pin.

```java
Pin pin = device.getPin(2);
pin.addEventListener(new PinEventListener() {
    @Override
    public void onModeChange(IOEvent event) {
        System.out.println("Mode of the pin has been changed");
    }

    @Override
    public void onValueChange(IOEvent event) {
        System.out.println("Value of the pin has been changed");
    }
});
```

Changing the mode and value of a pin look like this:

```java
pin.setMode(Pin.Mode.OUTPUT); // our listeners will get event about this change
pin.setValue(1); // and then about this change
```

You can get visual representation of device's pins using `JPinboard` Swing component.

```java
JPinboard pinboard = new JPinboard(device);
JFrame frame = new JFrame("Pinboard Example");
frame.add(pinboard);
frame.pack();
frame.setVisible(true);
```

`JPinboard` allows setting the pin's mode by choosing one from a context menu of
the pin. State of the output pin can be changed by double clicking on it.

An example of `JPinboard` usage can be found in
[`org.firmata4j.Example` class](https://github.com/kurbatov/firmata4j/blob/master/src/main/java/org/firmata4j/Example.java).

## Versions
**firmata4j** sticks to Firmata protocol versions. The first available version
of **firmata4j** is 2.3.1.

**firmata4j**-2.3.x will work well with Fimata v. 2.3.x. Actually it should work
with Firmata v. 2.x.x but not necessarily support all of the protocol features.
The first digits of versions must be equal because those stand for incompatible
changes of the protocol.

## Uploading Firmata To Arduino
Arduino IDE is shipped with an implementation of Firmata protocol. You can
upload it as follows:

- Plug your Arduino to the computer
- Launch Arduino IDE
- Select `File -> Examples -> Firmata -> StandardFirmata` in IDE's menu
- Select your board in `Tools -> Board`
- Select the port in `Tools -> Port` (it is already selected if you have uploaded something to your Arduino)
- Click on `Upload` button

Arduino IDE may contain an outdated version of Firmata.
You can update it using the guide on
[this page](https://github.com/firmata/arduino).

## I2C
Currently **firmata4j** does not support working with I2C installations.
I have no idea how to implement that because I haven't dealt with it in my
applications. If you need this functionality and got an idea how that should be
done, please, share it.

## Contributing
Contributions are welcome. If you discover a bug or would like to propose a new
feature, please, open a new issue.

If you have an improvement to share, please, do the following:

1. Fork this repository
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Adds some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## License
**firmata4j** is distributed under the terms of the MIT License. See the
[LICENSE](https://github.com/kurbatov/firmata4j/blob/master/LICENSE) file.
