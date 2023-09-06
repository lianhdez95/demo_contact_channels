import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class GalleryPickChannelPage extends StatefulWidget {
  const GalleryPickChannelPage({Key? key}) : super(key: key);

  @override
  State<GalleryPickChannelPage> createState() => _GalleryPickChannelPageState();
}

class _GalleryPickChannelPageState extends State<GalleryPickChannelPage> {
  String _imagePath = '';
  static const platform = MethodChannel('com.example.gallery.pick');

  Future<void> _openGallery() async {
    try {
      final String? result = await platform.invokeMethod('openGallery');
      if (result != null) {
        setState(() => _imagePath = result);
      }
    } on PlatformException catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(e.message ?? 'Ocurrió un error desconocido')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Gallery Example')),
      body: Center(
        child: Column(
          children: [
            (_imagePath.isNotEmpty)
                ? Image.file(File.fromUri(Uri.parse(_imagePath))) // Use Uri.parse to convert the path to a Uri
                : Container(),
            ElevatedButton(
              onPressed: _openGallery,
              child: const Text('Open Gallery'),
            )
          ],
        ),
      ),
    );
  }
}
