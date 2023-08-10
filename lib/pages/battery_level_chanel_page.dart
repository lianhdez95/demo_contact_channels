// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class BatteryLevelChannelPage extends StatefulWidget {
  const BatteryLevelChannelPage({super.key});

  @override
  // ignore: library_private_types_in_public_api
  _BatteryStatusPageState createState() => _BatteryStatusPageState();
}

class _BatteryStatusPageState extends State<BatteryLevelChannelPage> {
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
        child: Text(
          _batteryStatus,
          style: const TextStyle(fontSize: 30),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _updateBatteryStatus,
        child: const Icon(Icons.update),
      ),
    );
  }
}