// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class BatteryStatusChannelPage extends StatefulWidget {
  const BatteryStatusChannelPage({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _BatteryStatusChannelPageState createState() => _BatteryStatusChannelPageState();
}

class _BatteryStatusChannelPageState extends State<BatteryStatusChannelPage> {
  static const batteryPlatform = MethodChannel('com.example.battery');
  String _batteryStatus = '';

  @override
  void initState() {
    super.initState();
    _getBatteryStatus();
  }

  Future<void> _getBatteryStatus() async {
    try {
      final dynamic result = await batteryPlatform.invokeMethod('getBatteryStatus');
      setState(() {
        _batteryStatus = result.toString();
      });
    } on PlatformException catch (e) {
      print("Failed to get battery status: '${e.message}'.");
      setState(() {
        _batteryStatus = '';
      });
    }
  }

   void _updateBatteryStatus() {
    setState(() {
      _batteryStatus = 'Updating...';
    });
    _getBatteryStatus();
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Battery Channel'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.battery_5_bar_rounded, color: Colors.blue, size: 100,),

            SizedBox(height: 30,),

            Text(
              _batteryStatus,
              style: const TextStyle(fontSize: 30),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _updateBatteryStatus,
        child: const Icon(Icons.update),
      ),
    );
  }
}