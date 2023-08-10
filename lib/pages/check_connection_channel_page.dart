// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


class CheckConnectionChannelPage extends StatefulWidget {
  const CheckConnectionChannelPage({super.key});

  @override
  State<CheckConnectionChannelPage> createState() => _CheckConnectionChannelPageState();
}

class _CheckConnectionChannelPageState extends State<CheckConnectionChannelPage> {
  static const networkChannel = MethodChannel('com.example.network');
  bool _isMobileDataEnabled = false;
  bool _isWifiEnabled = false;

  Future<bool> isMobileDataEnabled() async {
    final bool isEnabled =
        await networkChannel.invokeMethod('isMobileDataEnabled');
    return isEnabled;
  }

  Future<bool> isWifiEnabled() async {
    final bool isEnabled = await networkChannel.invokeMethod('isWifiEnabled');
    return isEnabled;
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

      setState(() {
        _isMobileDataEnabled = mobileDataEnabled;
        _isWifiEnabled = wifiEnabled;
      });
    } on PlatformException catch (e) {
      print("Failed to get network status: '${e.message}'.");
      setState(() {
        _isMobileDataEnabled = false;
        _isWifiEnabled = false;
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
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center, 
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Mobile Data Enabled: \n$_isMobileDataEnabled',
              style: const TextStyle(fontSize: 30),
            ),
            const SizedBox(height: 30),
            Text(
              'Wi-Fi Enabled: \n$_isWifiEnabled',
              style: const TextStyle(fontSize: 30),
            ),
          ],
        ),
      ),
    );
  }
}
