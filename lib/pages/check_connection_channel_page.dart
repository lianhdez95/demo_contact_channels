// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CheckConnectionChannelPage extends StatefulWidget {
  const CheckConnectionChannelPage({super.key});

  @override
  State<CheckConnectionChannelPage> createState() =>
      _CheckConnectionChannelPageState();
}

class _CheckConnectionChannelPageState
    extends State<CheckConnectionChannelPage> {
  static const networkChannel = MethodChannel('com.example.network');
  bool _isMobileDataEnabled = false;
  bool _isWifiEnabled = false;
    bool _isBluetoothEnabled = false;

  Future<bool> isMobileDataEnabled() async {
    try {
      final bool result =
          await networkChannel.invokeMethod('isMobileDataEnabled');
      return result;
    } on PlatformException catch (e) {
      print("Failed to check mobile data status: ${e.message}");
      return false;
    }
  }

  Future<bool> isWifiEnabled() async {
    try {
      final bool result = await networkChannel.invokeMethod('isWifiEnabled');
      return result;
    } on PlatformException catch (e) {
      print("Failed to check WiFi status: ${e.message}");
      return false;
    }
  }

  Future<bool> isBluetoothEnabled() async {
    try {
      final bool result =
          await networkChannel.invokeMethod('isBluetoothEnabled');
      return result;
    } on PlatformException catch (e) {
      print("Failed to check Bluetooth status: ${e.message}");
      return false;
    }
  }

  @override
  void initState() {
    super.initState();
    _getNetworkStatus();
  }

  Future<void> _getNetworkStatus() async {
    try {
      final bool mobileDataEnabled = await isMobileDataEnabled();
      final bool wifiEnabled = await isWifiEnabled();
      final bluetoothEnabled = await isBluetoothEnabled();

      setState(() {
        _isMobileDataEnabled = mobileDataEnabled;
        _isWifiEnabled = wifiEnabled;
        _isBluetoothEnabled = bluetoothEnabled;
      });
    } on PlatformException catch (e) {
      print("Failed to get network status: '${e.message}'.");
      setState(() {
        _isMobileDataEnabled = false;
        _isWifiEnabled = false;
        _isBluetoothEnabled = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Network Status'),
      ),
      body: Center(
          child: showConnectionData(
              _isWifiEnabled.toString(), _isMobileDataEnabled.toString(), _isBluetoothEnabled.toString())),
    );
  }

  Widget showConnectionData(String isWifiEnabled, String isMobileDataEnabled, String isBluetoothEnabled) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
          Icon(_isMobileDataEnabled
                  ? Icons.network_cell
                  : Icons.network_locked_outlined,
                  color: _isMobileDataEnabled
                          ? Colors.blue
                          : Colors.red,
                  size: 50,
          ), 

          const SizedBox(width: 30,), 
          
          Icon(_isWifiEnabled
                  ?Icons.wifi
                  :Icons.wifi_off,
                  color: _isWifiEnabled
                    ? Colors.blue
                    : Colors.red,
                  size: 50,
          ),

          const SizedBox(width: 30,), 
          
          Icon(_isBluetoothEnabled
                  ?Icons.bluetooth
                  :Icons.bluetooth_disabled,
                  color: _isBluetoothEnabled
                    ? Colors.blue
                    : Colors.red,
                  size: 50,
          )
          ],
        ),
        const SizedBox(height: 30,),
        Container(
          alignment: Alignment.centerLeft,
          margin: const EdgeInsets.symmetric(horizontal: 20),
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.blue,
            borderRadius: BorderRadius.circular(10),
          ),
          child: _isMobileDataEnabled
              ? const Text(
                  'Mobile Data Status: Enabled',
                  textAlign: TextAlign.left,
                  style: TextStyle(fontSize: 20, color: Colors.white),
                )
              : const Text(
                  'Mobile Data Enabled: Disabled',
                  style: TextStyle(fontSize: 20, color: Colors.white),
                ),
        ),
        const SizedBox(height: 30),
        Container(
          alignment: Alignment.centerLeft,
          margin: const EdgeInsets.symmetric(horizontal: 20),
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.blue,
            borderRadius: BorderRadius.circular(10),
          ),
          child: _isWifiEnabled
              ? const Text(
                  'Wi-Fi Status: Enabled',
                  style: TextStyle(fontSize: 20, color: Colors.white,),
                )
              : const Text(
                  'Wi-Fi Status: Disabled',
                  style: TextStyle(fontSize: 20, color: Colors.white),
                ),
        ),
        const SizedBox(height: 30),
        Container(
          alignment: Alignment.centerLeft,
          margin: const EdgeInsets.symmetric(horizontal: 20),
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.blue,
            borderRadius: BorderRadius.circular(10),
          ),
          child: _isBluetoothEnabled
              ? const Text(
                  'Bluetooth Status: Enabled',
                  style: TextStyle(fontSize: 20, color: Colors.white,),
                )
              : const Text(
                  'Bluetooth Status: Disabled',
                  style: TextStyle(fontSize: 20, color: Colors.white),
                ),
        )
      ],
    );
  }
}
