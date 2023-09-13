import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CameraOpenChannelPage extends StatefulWidget {
  const CameraOpenChannelPage({Key? key});

  @override
  State<CameraOpenChannelPage> createState() => _CameraOpenChannelPageState();
}

class _CameraOpenChannelPageState extends State<CameraOpenChannelPage> {
  final MethodChannel _channel = const MethodChannel('com.example.camera.open');
  String _imagePath = '';

  Future<void> _takePhoto() async {
    try {
      final imagePath = await _channel.invokeMethod('openCamera');
      setState(() {
        _imagePath = imagePath;
      });
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print('Error taking photo: ${e.message}');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final screenHeight = MediaQuery.of(context).size.height;

    return Scaffold(
        appBar: AppBar(
          title: const Text('Camera Open'),
        ),
        body: _loadPhoto(screenWidth * 0.7, screenHeight * 0.7),
        floatingActionButton: FloatingActionButton(
          onPressed: _takePhoto,
          child: const Icon(Icons.camera),
        ));
  }

  Widget _loadPhoto(double imageWidth, double imageHeight) {
    return SafeArea(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(height: MediaQuery.of(context).size.height * 0.02),
            _imagePath.isNotEmpty
                ? Image.file(
                    File(_imagePath),
                    width: imageWidth,
                    height: imageHeight,
                  )
                : const Icon(
                    Icons.image_not_supported,
                    color: Colors.grey,
                    size: 100,
                  ),
          ],
        ),
      ),
    );
  }
}
