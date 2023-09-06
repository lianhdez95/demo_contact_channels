import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class DeviceInfoChannelPage extends StatefulWidget {
  const DeviceInfoChannelPage({super.key});

  @override
  State<DeviceInfoChannelPage> createState() => _DeviceInfoChannelPageState();
}

class _DeviceInfoChannelPageState extends State<DeviceInfoChannelPage> {
  final MethodChannel _deviceInfoChannel =
  const MethodChannel('com.example.device.info');
  String _api = '';
  String _androidVersion = '';
  String _architecture = '';

  Future<void> _getSDKInfo() async {
    try {
      final api = await _deviceInfoChannel.invokeMethod('getAPIInfo');
      setState(() {
        _api = api;
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error taking photo: ${e.message}');
      }
    }
  }

  Future<void> _getAndroidVersionInfo() async {
    try {
      final android =
      await _deviceInfoChannel.invokeMethod('getAndroidVersionInfo');
      setState(() {
        _androidVersion = android;
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error taking photo: ${e.message}');
      }
    }
  }

  Future<void> _getArchitectureInfo() async {
    try {
      final architecture = await _deviceInfoChannel.invokeMethod(
          'getArchitectureInfo');
      setState(() {
        _architecture = architecture;
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error taking photo: ${e.message}');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Device Info Channel'),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 30.0, horizontal: 20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  customLeading(const Icon(Icons.api, color: Colors.blue, size: 30,)),
                  const SizedBox(width: 50,),
                  customCard(_api)
                ],
              ),
              const SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  customLeading(const Icon(Icons.android, color: Colors.blue, size: 30,)),
                  const SizedBox(width: 50,),
                  customCard(_androidVersion)
                ],
              ),
              const SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  customLeading(const Icon(Icons.architecture, color: Colors.blue, size: 30,)),
                  const SizedBox(width: 50,),
                  customCard(_architecture)
                ],
              ),
              const SizedBox(height: 50),
              ElevatedButton(
                onPressed: () {
                  _getSDKInfo();
                  _getAndroidVersionInfo();
                  _getArchitectureInfo();
                },
                style: ButtonStyle(
                  fixedSize: MaterialStateProperty.all<Size>(
                      const Size(180, 50)),
                  shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10.0),
                    ),
                  ),
                ),
                child: const Text('Get device info',
                    style: TextStyle(fontSize: 20),
                    textAlign: TextAlign.center),
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget customLeading(Icon icon) {
    return Row(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          icon,
          const SizedBox(width: 20,)
        ]
    );
  }

  Widget customCard(String text) {
    return Card(
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
          side: const BorderSide(color: Colors.blue)),
      child: Container(
        width: 150,
        padding: const EdgeInsets.all(10.0),
        child: Text(text, style: const TextStyle(fontSize: 20)),
      ),
    );
  }
}
